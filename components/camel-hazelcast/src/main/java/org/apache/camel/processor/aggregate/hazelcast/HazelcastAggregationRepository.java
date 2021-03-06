begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.hazelcast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
operator|.
name|hazelcast
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|locks
operator|.
name|Lock
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|config
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|config
operator|.
name|XmlConfigBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|Hazelcast
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|IMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|TransactionalMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|transaction
operator|.
name|TransactionContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|transaction
operator|.
name|TransactionOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|OptimisticLockingAggregationRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|RecoverableAggregationRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|DefaultExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|DefaultExchangeHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|service
operator|.
name|ServiceSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StringHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A Hazelcast-based AggregationRepository implementing  * {@link RecoverableAggregationRepository} and {@link OptimisticLockingAggregationRepository}.  * Defaults to thread-safe (non-optimistic) locking and recoverable strategy.  * Hazelcast settings are given to an end-user and can be controlled with repositoryName and persistentRespositoryName,  * both are {@link com.hazelcast.core.IMap}&lt;String, Exchange&gt;. However HazelcastAggregationRepository  * can run it's own Hazelcast instance, but obviously no benefits of Hazelcast clustering are gained this way.  * If the {@link HazelcastAggregationRepository} uses it's own local {@link HazelcastInstance} it will DESTROY this  * instance on {@link #doStop()}. You should control {@link HazelcastInstance} lifecycle yourself whenever you instantiate  * {@link HazelcastAggregationRepository} passing a reference to the instance.  *  */
end_comment

begin_class
DECL|class|HazelcastAggregationRepository
specifier|public
class|class
name|HazelcastAggregationRepository
extends|extends
name|ServiceSupport
implements|implements
name|RecoverableAggregationRepository
implements|,
name|OptimisticLockingAggregationRepository
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|HazelcastAggregationRepository
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|COMPLETED_SUFFIX
specifier|private
specifier|static
specifier|final
name|String
name|COMPLETED_SUFFIX
init|=
literal|"-completed"
decl_stmt|;
DECL|field|optimistic
specifier|private
name|boolean
name|optimistic
decl_stmt|;
DECL|field|useLocalHzInstance
specifier|private
name|boolean
name|useLocalHzInstance
decl_stmt|;
DECL|field|useRecovery
specifier|private
name|boolean
name|useRecovery
init|=
literal|true
decl_stmt|;
DECL|field|cache
specifier|private
name|IMap
argument_list|<
name|String
argument_list|,
name|DefaultExchangeHolder
argument_list|>
name|cache
decl_stmt|;
DECL|field|persistedCache
specifier|private
name|IMap
argument_list|<
name|String
argument_list|,
name|DefaultExchangeHolder
argument_list|>
name|persistedCache
decl_stmt|;
DECL|field|hzInstance
specifier|private
name|HazelcastInstance
name|hzInstance
decl_stmt|;
DECL|field|mapName
specifier|private
name|String
name|mapName
decl_stmt|;
DECL|field|persistenceMapName
specifier|private
name|String
name|persistenceMapName
decl_stmt|;
DECL|field|deadLetterChannel
specifier|private
name|String
name|deadLetterChannel
decl_stmt|;
DECL|field|recoveryInterval
specifier|private
name|long
name|recoveryInterval
init|=
literal|5000
decl_stmt|;
DECL|field|maximumRedeliveries
specifier|private
name|int
name|maximumRedeliveries
init|=
literal|3
decl_stmt|;
DECL|field|allowSerializedHeaders
specifier|private
name|boolean
name|allowSerializedHeaders
decl_stmt|;
comment|/**      * Creates new {@link HazelcastAggregationRepository} that defaults to non-optimistic locking      * with recoverable behavior and a local Hazelcast instance. Recoverable repository name defaults to      * {@code repositoryName} + "-compeleted".      * @param repositoryName {@link IMap} repository name;      */
DECL|method|HazelcastAggregationRepository (final String repositoryName)
specifier|public
name|HazelcastAggregationRepository
parameter_list|(
specifier|final
name|String
name|repositoryName
parameter_list|)
block|{
name|mapName
operator|=
name|repositoryName
expr_stmt|;
name|persistenceMapName
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"%s%s"
argument_list|,
name|mapName
argument_list|,
name|COMPLETED_SUFFIX
argument_list|)
expr_stmt|;
name|optimistic
operator|=
literal|false
expr_stmt|;
name|useLocalHzInstance
operator|=
literal|true
expr_stmt|;
block|}
comment|/**     * Creates new {@link HazelcastAggregationRepository} that defaults to non-optimistic locking     * with recoverable behavior and a local Hazelcast instance.     * @param repositoryName {@link IMap} repository name;     * @param  persistentRepositoryName {@link IMap} recoverable repository name;     */
DECL|method|HazelcastAggregationRepository (final String repositoryName, final String persistentRepositoryName)
specifier|public
name|HazelcastAggregationRepository
parameter_list|(
specifier|final
name|String
name|repositoryName
parameter_list|,
specifier|final
name|String
name|persistentRepositoryName
parameter_list|)
block|{
name|mapName
operator|=
name|repositoryName
expr_stmt|;
name|persistenceMapName
operator|=
name|persistentRepositoryName
expr_stmt|;
name|optimistic
operator|=
literal|false
expr_stmt|;
name|useLocalHzInstance
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Creates new {@link HazelcastAggregationRepository} with recoverable behavior and a local Hazelcast instance.      * Recoverable repository name defaults to {@code repositoryName} + "-compeleted".      * @param repositoryName {@link IMap} repository name;      * @param  optimistic whether to use optimistic locking manner.      */
DECL|method|HazelcastAggregationRepository (final String repositoryName, boolean optimistic)
specifier|public
name|HazelcastAggregationRepository
parameter_list|(
specifier|final
name|String
name|repositoryName
parameter_list|,
name|boolean
name|optimistic
parameter_list|)
block|{
name|this
argument_list|(
name|repositoryName
argument_list|)
expr_stmt|;
name|this
operator|.
name|optimistic
operator|=
name|optimistic
expr_stmt|;
name|useLocalHzInstance
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Creates new {@link HazelcastAggregationRepository} with recoverable behavior and a local Hazelcast instance.      * @param repositoryName {@link IMap} repository name;      * @param  persistentRepositoryName {@link IMap} recoverable repository name;      * @param optimistic whether to use optimistic locking manner.      */
DECL|method|HazelcastAggregationRepository (final String repositoryName, final String persistentRepositoryName, boolean optimistic)
specifier|public
name|HazelcastAggregationRepository
parameter_list|(
specifier|final
name|String
name|repositoryName
parameter_list|,
specifier|final
name|String
name|persistentRepositoryName
parameter_list|,
name|boolean
name|optimistic
parameter_list|)
block|{
name|this
argument_list|(
name|repositoryName
argument_list|,
name|persistentRepositoryName
argument_list|)
expr_stmt|;
name|this
operator|.
name|optimistic
operator|=
name|optimistic
expr_stmt|;
name|useLocalHzInstance
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Creates new {@link HazelcastAggregationRepository} that defaults to non-optimistic locking      * with recoverable behavior. Recoverable repository name defaults to      * {@code repositoryName} + "-compeleted".      * @param repositoryName {@link IMap} repository name;      * @param hzInstanse externally configured {@link HazelcastInstance}.      */
DECL|method|HazelcastAggregationRepository (final String repositoryName, HazelcastInstance hzInstanse)
specifier|public
name|HazelcastAggregationRepository
parameter_list|(
specifier|final
name|String
name|repositoryName
parameter_list|,
name|HazelcastInstance
name|hzInstanse
parameter_list|)
block|{
name|this
argument_list|(
name|repositoryName
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|hzInstance
operator|=
name|hzInstanse
expr_stmt|;
name|useLocalHzInstance
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * Creates new {@link HazelcastAggregationRepository} that defaults to non-optimistic locking      * with recoverable behavior.      * @param repositoryName {@link IMap} repository name;      * @param  persistentRepositoryName {@link IMap} recoverable repository name;      * @param hzInstanse externally configured {@link HazelcastInstance}.      */
DECL|method|HazelcastAggregationRepository (final String repositoryName, final String persistentRepositoryName, HazelcastInstance hzInstanse)
specifier|public
name|HazelcastAggregationRepository
parameter_list|(
specifier|final
name|String
name|repositoryName
parameter_list|,
specifier|final
name|String
name|persistentRepositoryName
parameter_list|,
name|HazelcastInstance
name|hzInstanse
parameter_list|)
block|{
name|this
argument_list|(
name|repositoryName
argument_list|,
name|persistentRepositoryName
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|hzInstance
operator|=
name|hzInstanse
expr_stmt|;
name|useLocalHzInstance
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * Creates new {@link HazelcastAggregationRepository} with recoverable behavior.      * Recoverable repository name defaults to {@code repositoryName} + "-compeleted".      * @param repositoryName {@link IMap} repository name;      * @param  optimistic whether to use optimistic locking manner;      * @param hzInstance  externally configured {@link HazelcastInstance}.      */
DECL|method|HazelcastAggregationRepository (final String repositoryName, boolean optimistic, HazelcastInstance hzInstance)
specifier|public
name|HazelcastAggregationRepository
parameter_list|(
specifier|final
name|String
name|repositoryName
parameter_list|,
name|boolean
name|optimistic
parameter_list|,
name|HazelcastInstance
name|hzInstance
parameter_list|)
block|{
name|this
argument_list|(
name|repositoryName
argument_list|,
name|optimistic
argument_list|)
expr_stmt|;
name|this
operator|.
name|hzInstance
operator|=
name|hzInstance
expr_stmt|;
name|useLocalHzInstance
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * Creates new {@link HazelcastAggregationRepository} with recoverable behavior.      * @param repositoryName {@link IMap} repository name;      * @param optimistic whether to use optimistic locking manner;      * @param persistentRepositoryName {@link IMap} recoverable repository name;      * @param hzInstance  externally configured {@link HazelcastInstance}.      */
DECL|method|HazelcastAggregationRepository (final String repositoryName, final String persistentRepositoryName, boolean optimistic, HazelcastInstance hzInstance)
specifier|public
name|HazelcastAggregationRepository
parameter_list|(
specifier|final
name|String
name|repositoryName
parameter_list|,
specifier|final
name|String
name|persistentRepositoryName
parameter_list|,
name|boolean
name|optimistic
parameter_list|,
name|HazelcastInstance
name|hzInstance
parameter_list|)
block|{
name|this
argument_list|(
name|repositoryName
argument_list|,
name|persistentRepositoryName
argument_list|,
name|optimistic
argument_list|)
expr_stmt|;
name|this
operator|.
name|hzInstance
operator|=
name|hzInstance
expr_stmt|;
name|useLocalHzInstance
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (CamelContext camelContext, String key, Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|add
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
throws|throws
name|OptimisticLockingException
block|{
if|if
condition|(
operator|!
name|optimistic
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Adding an Exchange with ID {} for key {} in an optimistic manner."
argument_list|,
name|newExchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
name|DefaultExchangeHolder
name|holder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|newExchange
argument_list|,
literal|true
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
specifier|final
name|DefaultExchangeHolder
name|misbehaviorHolder
init|=
name|cache
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
name|holder
argument_list|)
decl_stmt|;
if|if
condition|(
name|misbehaviorHolder
operator|!=
literal|null
condition|)
block|{
name|Exchange
name|misbehaviorEx
init|=
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
name|misbehaviorHolder
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"Optimistic locking failed for exchange with key {}: IMap#putIfAbsend returned Exchange with ID {}, while it's expected no exchanges to be returned"
argument_list|,
name|key
argument_list|,
name|misbehaviorEx
operator|!=
literal|null
condition|?
name|misbehaviorEx
operator|.
name|getExchangeId
argument_list|()
else|:
literal|"<null>"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OptimisticLockingException
argument_list|()
throw|;
block|}
block|}
else|else
block|{
name|DefaultExchangeHolder
name|oldHolder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|oldExchange
argument_list|,
literal|true
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
name|DefaultExchangeHolder
name|newHolder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|newExchange
argument_list|,
literal|true
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cache
operator|.
name|replace
argument_list|(
name|key
argument_list|,
name|oldHolder
argument_list|,
name|newHolder
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Optimistic locking failed for exchange with key {}: IMap#replace returned no Exchanges, while it's expected to replace one"
argument_list|,
name|key
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OptimisticLockingException
argument_list|()
throw|;
block|}
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Added an Exchange with ID {} for key {} in optimistic manner."
argument_list|,
name|newExchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
name|oldExchange
return|;
block|}
annotation|@
name|Override
DECL|method|add (CamelContext camelContext, String key, Exchange exchange)
specifier|public
name|Exchange
name|add
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|optimistic
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Adding an Exchange with ID {} for key {} in a thread-safe manner."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|Lock
name|l
init|=
name|hzInstance
operator|.
name|getLock
argument_list|(
name|mapName
argument_list|)
decl_stmt|;
try|try
block|{
name|l
operator|.
name|lock
argument_list|()
expr_stmt|;
name|DefaultExchangeHolder
name|newHolder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
name|DefaultExchangeHolder
name|oldHolder
init|=
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|newHolder
argument_list|)
decl_stmt|;
return|return
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
name|oldHolder
argument_list|)
return|;
block|}
finally|finally
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Added an Exchange with ID {} for key {} in a thread-safe manner."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|l
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|scan (CamelContext camelContext)
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|scan
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|useRecovery
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Scanning for exchanges to recover in {} context"
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|scanned
init|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|persistedCache
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Found {} keys for exchanges to recover in {} context"
argument_list|,
name|scanned
operator|.
name|size
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|scanned
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"What for to run recovery scans in {} context while repository {} is running in non-recoverable aggregation repository mode?!"
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|mapName
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|recover (CamelContext camelContext, String exchangeId)
specifier|public
name|Exchange
name|recover
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|exchangeId
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Recovering an Exchange with ID {}."
argument_list|,
name|exchangeId
argument_list|)
expr_stmt|;
return|return
name|useRecovery
condition|?
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
name|persistedCache
operator|.
name|get
argument_list|(
name|exchangeId
argument_list|)
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setRecoveryInterval (long interval, TimeUnit timeUnit)
specifier|public
name|void
name|setRecoveryInterval
parameter_list|(
name|long
name|interval
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|this
operator|.
name|recoveryInterval
operator|=
name|timeUnit
operator|.
name|toMillis
argument_list|(
name|interval
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setRecoveryInterval (long interval)
specifier|public
name|void
name|setRecoveryInterval
parameter_list|(
name|long
name|interval
parameter_list|)
block|{
name|this
operator|.
name|recoveryInterval
operator|=
name|interval
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRecoveryIntervalInMillis ()
specifier|public
name|long
name|getRecoveryIntervalInMillis
parameter_list|()
block|{
return|return
name|recoveryInterval
return|;
block|}
annotation|@
name|Override
DECL|method|setUseRecovery (boolean useRecovery)
specifier|public
name|void
name|setUseRecovery
parameter_list|(
name|boolean
name|useRecovery
parameter_list|)
block|{
name|this
operator|.
name|useRecovery
operator|=
name|useRecovery
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseRecovery ()
specifier|public
name|boolean
name|isUseRecovery
parameter_list|()
block|{
return|return
name|useRecovery
return|;
block|}
annotation|@
name|Override
DECL|method|setDeadLetterUri (String deadLetterUri)
specifier|public
name|void
name|setDeadLetterUri
parameter_list|(
name|String
name|deadLetterUri
parameter_list|)
block|{
name|this
operator|.
name|deadLetterChannel
operator|=
name|deadLetterUri
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDeadLetterUri ()
specifier|public
name|String
name|getDeadLetterUri
parameter_list|()
block|{
return|return
name|deadLetterChannel
return|;
block|}
annotation|@
name|Override
DECL|method|setMaximumRedeliveries (int maximumRedeliveries)
specifier|public
name|void
name|setMaximumRedeliveries
parameter_list|(
name|int
name|maximumRedeliveries
parameter_list|)
block|{
name|this
operator|.
name|maximumRedeliveries
operator|=
name|maximumRedeliveries
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMaximumRedeliveries ()
specifier|public
name|int
name|getMaximumRedeliveries
parameter_list|()
block|{
return|return
name|maximumRedeliveries
return|;
block|}
annotation|@
name|Override
DECL|method|get (CamelContext camelContext, String key)
specifier|public
name|Exchange
name|get
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|unmarshallExchange
argument_list|(
name|camelContext
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Checks if the key in question is in the repository.      *       * @param key Object - key in question      */
DECL|method|containsKey (Object key)
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
return|return
name|cache
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|isAllowSerializedHeaders ()
specifier|public
name|boolean
name|isAllowSerializedHeaders
parameter_list|()
block|{
return|return
name|allowSerializedHeaders
return|;
block|}
DECL|method|setAllowSerializedHeaders (boolean allowSerializedHeaders)
specifier|public
name|void
name|setAllowSerializedHeaders
parameter_list|(
name|boolean
name|allowSerializedHeaders
parameter_list|)
block|{
name|this
operator|.
name|allowSerializedHeaders
operator|=
name|allowSerializedHeaders
expr_stmt|;
block|}
comment|/**      * This method performs transactional operation on removing the {@code exchange}      * from the operational storage and moving it into the persistent one if the {@link HazelcastAggregationRepository}      * runs in recoverable mode and {@code optimistic} is false. It will act at<u>your own</u> risk otherwise.      * @param camelContext   the current CamelContext      * @param key            the correlation key      * @param exchange       the exchange to remove      */
annotation|@
name|Override
DECL|method|remove (CamelContext camelContext, String key, Exchange exchange)
specifier|public
name|void
name|remove
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|DefaultExchangeHolder
name|holder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|,
name|allowSerializedHeaders
argument_list|)
decl_stmt|;
if|if
condition|(
name|optimistic
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Removing an exchange with ID {} for key {} in an optimistic manner."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|holder
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Optimistic locking failed for exchange with key {}: IMap#remove removed no Exchanges, while it's expected to remove one."
argument_list|,
name|key
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OptimisticLockingException
argument_list|()
throw|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Removed an exchange with ID {} for key {} in an optimistic manner."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|useRecovery
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Putting an exchange with ID {} for key {} into a recoverable storage in an optimistic manner."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|persistedCache
operator|.
name|put
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|holder
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Put an exchange with ID {} for key {} into a recoverable storage in an optimistic manner."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|useRecovery
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Removing an exchange with ID {} for key {} in a thread-safe manner."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
comment|// The only considerable case for transaction usage is fault tolerance:
comment|// the transaction will be rolled back automatically (default timeout is 2 minutes)
comment|// if no commit occurs during the timeout. So we are still consistent whether local node crashes.
name|TransactionOptions
name|tOpts
init|=
operator|new
name|TransactionOptions
argument_list|()
decl_stmt|;
name|tOpts
operator|.
name|setTransactionType
argument_list|(
name|TransactionOptions
operator|.
name|TransactionType
operator|.
name|ONE_PHASE
argument_list|)
expr_stmt|;
name|TransactionContext
name|tCtx
init|=
name|hzInstance
operator|.
name|newTransactionContext
argument_list|(
name|tOpts
argument_list|)
decl_stmt|;
try|try
block|{
name|tCtx
operator|.
name|beginTransaction
argument_list|()
expr_stmt|;
name|TransactionalMap
argument_list|<
name|String
argument_list|,
name|DefaultExchangeHolder
argument_list|>
name|tCache
init|=
name|tCtx
operator|.
name|getMap
argument_list|(
name|cache
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|TransactionalMap
argument_list|<
name|String
argument_list|,
name|DefaultExchangeHolder
argument_list|>
name|tPersistentCache
init|=
name|tCtx
operator|.
name|getMap
argument_list|(
name|persistedCache
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|DefaultExchangeHolder
name|removedHolder
init|=
name|tCache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Putting an exchange with ID {} for key {} into a recoverable storage in a thread-safe manner."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|tPersistentCache
operator|.
name|put
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|removedHolder
argument_list|)
expr_stmt|;
name|tCtx
operator|.
name|commitTransaction
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Removed an exchange with ID {} for key {} in a thread-safe manner."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Put an exchange with ID {} for key {} into a recoverable storage in a thread-safe manner."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|tCtx
operator|.
name|rollbackTransaction
argument_list|()
expr_stmt|;
specifier|final
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Transaction with ID %s was rolled back for remove operation with a key %s and an Exchange ID %s."
argument_list|,
name|tCtx
operator|.
name|getTxnId
argument_list|()
argument_list|,
name|key
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
name|msg
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|msg
argument_list|,
name|throwable
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|confirm (CamelContext camelContext, String exchangeId)
specifier|public
name|void
name|confirm
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|exchangeId
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Confirming an exchange with ID {}."
argument_list|,
name|exchangeId
argument_list|)
expr_stmt|;
if|if
condition|(
name|useRecovery
condition|)
block|{
name|persistedCache
operator|.
name|remove
argument_list|(
name|exchangeId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getKeys ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getKeys
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|cache
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return Persistent repository {@link IMap} name;      */
DECL|method|getPersistentRepositoryName ()
specifier|public
name|String
name|getPersistentRepositoryName
parameter_list|()
block|{
return|return
name|persistenceMapName
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|maximumRedeliveries
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Maximum redelivery retries must be zero or a positive integer."
argument_list|)
throw|;
block|}
if|if
condition|(
name|recoveryInterval
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Recovery interval must be zero or a positive integer."
argument_list|)
throw|;
block|}
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|mapName
argument_list|,
literal|"repositoryName"
argument_list|)
expr_stmt|;
if|if
condition|(
name|useLocalHzInstance
condition|)
block|{
name|Config
name|cfg
init|=
operator|new
name|XmlConfigBuilder
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setProperty
argument_list|(
literal|"hazelcast.version.check.enabled"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|hzInstance
operator|=
name|Hazelcast
operator|.
name|newHazelcastInstance
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|hzInstance
argument_list|,
literal|"hzInstanse"
argument_list|)
expr_stmt|;
block|}
name|cache
operator|=
name|hzInstance
operator|.
name|getMap
argument_list|(
name|mapName
argument_list|)
expr_stmt|;
if|if
condition|(
name|useRecovery
condition|)
block|{
name|persistedCache
operator|=
name|hzInstance
operator|.
name|getMap
argument_list|(
name|persistenceMapName
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|//noop
if|if
condition|(
name|useLocalHzInstance
condition|)
block|{
name|hzInstance
operator|.
name|getLifecycleService
argument_list|()
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|unmarshallExchange (CamelContext camelContext, DefaultExchangeHolder holder)
specifier|protected
name|Exchange
name|unmarshallExchange
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|DefaultExchangeHolder
name|holder
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|holder
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|DefaultExchangeHolder
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
name|holder
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

