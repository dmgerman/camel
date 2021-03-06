begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|health
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|ConcurrentHashMap
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
name|ConcurrentMap
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
name|ScheduledExecutorService
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
name|ScheduledFuture
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
name|StampedLock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiConsumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|health
operator|.
name|HealthCheck
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
name|health
operator|.
name|HealthCheckHelper
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
name|health
operator|.
name|HealthCheckRegistry
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
name|health
operator|.
name|HealthCheckService
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
name|concurrent
operator|.
name|LockHelper
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

begin_class
DECL|class|DefaultHealthCheckService
specifier|public
specifier|final
class|class
name|DefaultHealthCheckService
extends|extends
name|ServiceSupport
implements|implements
name|HealthCheckService
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultHealthCheckService
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|checks
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|HealthCheck
argument_list|,
name|HealthCheck
operator|.
name|Result
argument_list|>
name|checks
decl_stmt|;
DECL|field|options
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|options
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|List
argument_list|<
name|BiConsumer
argument_list|<
name|HealthCheck
operator|.
name|State
argument_list|,
name|HealthCheck
argument_list|>
argument_list|>
name|listeners
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|StampedLock
name|lock
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|executorService
specifier|private
name|ScheduledExecutorService
name|executorService
decl_stmt|;
DECL|field|checkInterval
specifier|private
name|long
name|checkInterval
decl_stmt|;
DECL|field|checkIntervalUnit
specifier|private
name|TimeUnit
name|checkIntervalUnit
decl_stmt|;
DECL|field|registry
specifier|private
specifier|volatile
name|HealthCheckRegistry
name|registry
decl_stmt|;
DECL|field|future
specifier|private
specifier|volatile
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|future
decl_stmt|;
DECL|method|DefaultHealthCheckService ()
specifier|public
name|DefaultHealthCheckService
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultHealthCheckService (CamelContext camelContext)
specifier|public
name|DefaultHealthCheckService
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|checks
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|options
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|listeners
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|lock
operator|=
operator|new
name|StampedLock
argument_list|()
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|checkInterval
operator|=
literal|30
expr_stmt|;
name|this
operator|.
name|checkIntervalUnit
operator|=
name|TimeUnit
operator|.
name|SECONDS
expr_stmt|;
block|}
comment|// ************************************
comment|// Properties
comment|// ************************************
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|getHealthCheckRegistry ()
specifier|public
name|HealthCheckRegistry
name|getHealthCheckRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
DECL|method|setHealthCheckRegistry (HealthCheckRegistry registry)
specifier|public
name|void
name|setHealthCheckRegistry
parameter_list|(
name|HealthCheckRegistry
name|registry
parameter_list|)
block|{
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
block|}
DECL|method|getCheckInterval ()
specifier|public
name|long
name|getCheckInterval
parameter_list|()
block|{
return|return
name|checkInterval
return|;
block|}
DECL|method|setCheckInterval (long checkInterval)
specifier|public
name|void
name|setCheckInterval
parameter_list|(
name|long
name|checkInterval
parameter_list|)
block|{
name|this
operator|.
name|checkInterval
operator|=
name|checkInterval
expr_stmt|;
block|}
DECL|method|setCheckInterval (long interval, TimeUnit intervalUnit)
specifier|public
name|void
name|setCheckInterval
parameter_list|(
name|long
name|interval
parameter_list|,
name|TimeUnit
name|intervalUnit
parameter_list|)
block|{
name|setCheckInterval
argument_list|(
name|interval
argument_list|)
expr_stmt|;
name|setCheckIntervalUnit
argument_list|(
name|intervalUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|getCheckIntervalUnit ()
specifier|public
name|TimeUnit
name|getCheckIntervalUnit
parameter_list|()
block|{
return|return
name|checkIntervalUnit
return|;
block|}
DECL|method|setCheckIntervalUnit (TimeUnit checkIntervalUnit)
specifier|public
name|void
name|setCheckIntervalUnit
parameter_list|(
name|TimeUnit
name|checkIntervalUnit
parameter_list|)
block|{
name|this
operator|.
name|checkIntervalUnit
operator|=
name|checkIntervalUnit
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addStateChangeListener (BiConsumer<HealthCheck.State, HealthCheck> consumer)
specifier|public
name|void
name|addStateChangeListener
parameter_list|(
name|BiConsumer
argument_list|<
name|HealthCheck
operator|.
name|State
argument_list|,
name|HealthCheck
argument_list|>
name|consumer
parameter_list|)
block|{
name|LockHelper
operator|.
name|doWithWriteLock
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
name|listeners
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeStateChangeListener (BiConsumer<HealthCheck.State, HealthCheck> consumer)
specifier|public
name|void
name|removeStateChangeListener
parameter_list|(
name|BiConsumer
argument_list|<
name|HealthCheck
operator|.
name|State
argument_list|,
name|HealthCheck
argument_list|>
name|consumer
parameter_list|)
block|{
name|LockHelper
operator|.
name|doWithWriteLock
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
name|listeners
operator|.
name|removeIf
argument_list|(
name|listener
lambda|->
name|listener
operator|.
name|equals
argument_list|(
name|consumer
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setHealthCheckOptions (String id, Map<String, Object> options)
specifier|public
name|void
name|setHealthCheckOptions
parameter_list|(
name|String
name|id
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call (String id)
specifier|public
name|Optional
argument_list|<
name|HealthCheck
operator|.
name|Result
argument_list|>
name|call
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|call
argument_list|(
name|id
argument_list|,
name|options
operator|.
name|getOrDefault
argument_list|(
name|id
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|call (String id, Map<String, Object> options)
specifier|public
name|Optional
argument_list|<
name|HealthCheck
operator|.
name|Result
argument_list|>
name|call
parameter_list|(
name|String
name|id
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
return|return
name|registry
operator|.
name|getCheck
argument_list|(
name|id
argument_list|)
operator|.
name|map
argument_list|(
name|check
lambda|->
name|invoke
argument_list|(
name|check
argument_list|,
name|options
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|notify (HealthCheck check, HealthCheck.Result result)
specifier|public
name|void
name|notify
parameter_list|(
name|HealthCheck
name|check
parameter_list|,
name|HealthCheck
operator|.
name|Result
name|result
parameter_list|)
block|{
name|LockHelper
operator|.
name|doWithWriteLock
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
name|processResult
argument_list|(
name|check
argument_list|,
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getResults ()
specifier|public
name|Collection
argument_list|<
name|HealthCheck
operator|.
name|Result
argument_list|>
name|getResults
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|this
operator|.
name|checks
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|// ************************************
comment|// Lifecycle
comment|// ************************************
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|)
expr_stmt|;
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|executorService
operator|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|this
argument_list|,
literal|"DefaultHealthCheckService"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|future
operator|!=
literal|null
condition|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
name|registry
operator|=
name|HealthCheckRegistry
operator|.
name|get
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|registry
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|future
argument_list|)
condition|)
block|{
comment|// Start the health check task only if the health check registry
comment|// has been registered.
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Schedule health-checks to be executed every {} ({})"
argument_list|,
name|checkInterval
argument_list|,
name|checkIntervalUnit
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|future
operator|=
name|executorService
operator|.
name|scheduleAtFixedRate
argument_list|(
parameter_list|()
lambda|->
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
comment|// do not invoke the check if the service is not yet
comment|// properly started.
return|return;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Invoke health-checks (scheduled)"
argument_list|)
expr_stmt|;
name|registry
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|groupingBy
argument_list|(
name|HealthCheckHelper
operator|::
name|getGroup
argument_list|)
argument_list|)
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Map
operator|.
name|Entry
operator|::
name|getValue
argument_list|)
operator|.
name|flatMap
argument_list|(
name|Collection
operator|::
name|stream
argument_list|)
operator|.
name|sorted
argument_list|(
name|Comparator
operator|.
name|comparingInt
argument_list|(
name|HealthCheck
operator|::
name|getOrder
argument_list|)
argument_list|)
operator|.
name|forEach
argument_list|(
name|this
operator|::
name|invoke
argument_list|)
expr_stmt|;
block|}
argument_list|,
name|checkInterval
argument_list|,
name|checkInterval
argument_list|,
name|checkIntervalUnit
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
if|if
condition|(
name|future
operator|!=
literal|null
condition|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|future
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|executorService
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|// ************************************
comment|// Helpers
comment|// ************************************
DECL|method|processResult (HealthCheck check, HealthCheck.Result result)
specifier|private
name|HealthCheck
operator|.
name|Result
name|processResult
parameter_list|(
name|HealthCheck
name|check
parameter_list|,
name|HealthCheck
operator|.
name|Result
name|result
parameter_list|)
block|{
specifier|final
name|HealthCheck
operator|.
name|Result
name|cachedResult
init|=
name|checks
operator|.
name|get
argument_list|(
name|check
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isSameResult
argument_list|(
name|result
argument_list|,
name|cachedResult
argument_list|)
condition|)
block|{
comment|// Maybe make the listener aware of the reason, i.e.
comment|// the service is still un-healthy but the message
comment|// or error has changed.
name|listeners
operator|.
name|forEach
argument_list|(
name|listener
lambda|->
name|listener
operator|.
name|accept
argument_list|(
name|result
operator|.
name|getState
argument_list|()
argument_list|,
name|check
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// replace the old result with the new one even if the
comment|// state has not changed but the reason/error may be
comment|// changed.
name|checks
operator|.
name|put
argument_list|(
name|check
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|invoke (HealthCheck check)
specifier|private
name|HealthCheck
operator|.
name|Result
name|invoke
parameter_list|(
name|HealthCheck
name|check
parameter_list|)
block|{
return|return
name|invoke
argument_list|(
name|check
argument_list|,
name|options
operator|.
name|getOrDefault
argument_list|(
name|check
operator|.
name|getId
argument_list|()
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|invoke (HealthCheck check, Map<String, Object> options)
specifier|private
name|HealthCheck
operator|.
name|Result
name|invoke
parameter_list|(
name|HealthCheck
name|check
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
return|return
name|LockHelper
operator|.
name|supplyWithWriteLock
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Invoke health-check {}"
argument_list|,
name|check
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|processResult
argument_list|(
name|check
argument_list|,
name|check
operator|.
name|call
argument_list|(
name|options
argument_list|)
argument_list|)
return|;
block|}
argument_list|)
return|;
block|}
comment|/**      * Check if two results are equals by checking only the state, this method      * does not check if the result comes from the same health check, this should      * be done by the caller.      *<p>      * A future implementation should check all the parameter of the result.      */
DECL|method|isSameResult (HealthCheck.Result r1, HealthCheck.Result r2)
specifier|private
name|boolean
name|isSameResult
parameter_list|(
name|HealthCheck
operator|.
name|Result
name|r1
parameter_list|,
name|HealthCheck
operator|.
name|Result
name|r2
parameter_list|)
block|{
if|if
condition|(
name|Objects
operator|.
name|equals
argument_list|(
name|r1
argument_list|,
name|r2
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|r1
operator|!=
literal|null
operator|&&
name|r2
operator|!=
literal|null
condition|)
block|{
return|return
name|r1
operator|.
name|getState
argument_list|()
operator|==
name|r2
operator|.
name|getState
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

