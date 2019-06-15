begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The hazelcast-instance component is used to consume join/leave events of the  * cache instance in the cluster.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|HazelcastInstanceEndpointBuilderFactory
specifier|public
interface|interface
name|HazelcastInstanceEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Hazelcast Instance component.      */
DECL|interface|HazelcastInstanceEndpointBuilder
specifier|public
specifier|static
interface|interface
name|HazelcastInstanceEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedHazelcastInstanceEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedHazelcastInstanceEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The name of the cache.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|cacheName (String cacheName)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|cacheName
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"cacheName"
argument_list|,
name|cacheName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define if the endpoint will use a reliable Topic struct or not.          * The option is a<code>boolean</code> type.          * @group common          */
DECL|method|reliable (boolean reliable)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|reliable
parameter_list|(
name|boolean
name|reliable
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"reliable"
argument_list|,
name|reliable
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define if the endpoint will use a reliable Topic struct or not.          * The option will be converted to a<code>boolean</code> type.          * @group common          */
DECL|method|reliable (String reliable)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|reliable
parameter_list|(
name|String
name|reliable
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"reliable"
argument_list|,
name|reliable
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To specify a default operation to use, if no operation header has          * been provided.          * The option is a          *<code>org.apache.camel.component.hazelcast.HazelcastOperation</code>          * type.          * @group consumer          */
DECL|method|defaultOperation ( HazelcastOperation defaultOperation)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|defaultOperation
parameter_list|(
name|HazelcastOperation
name|defaultOperation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultOperation"
argument_list|,
name|defaultOperation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To specify a default operation to use, if no operation header has          * been provided.          * The option will be converted to a          *<code>org.apache.camel.component.hazelcast.HazelcastOperation</code>          * type.          * @group consumer          */
DECL|method|defaultOperation ( String defaultOperation)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|defaultOperation
parameter_list|(
name|String
name|defaultOperation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultOperation"
argument_list|,
name|defaultOperation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hazelcast instance reference which can be used for hazelcast          * endpoint.          * The option is a<code>com.hazelcast.core.HazelcastInstance</code>          * type.          * @group consumer          */
DECL|method|hazelcastInstance ( Object hazelcastInstance)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|hazelcastInstance
parameter_list|(
name|Object
name|hazelcastInstance
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hazelcastInstance"
argument_list|,
name|hazelcastInstance
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hazelcast instance reference which can be used for hazelcast          * endpoint.          * The option will be converted to a          *<code>com.hazelcast.core.HazelcastInstance</code> type.          * @group consumer          */
DECL|method|hazelcastInstance ( String hazelcastInstance)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|hazelcastInstance
parameter_list|(
name|String
name|hazelcastInstance
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hazelcastInstance"
argument_list|,
name|hazelcastInstance
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The hazelcast instance reference name which can be used for hazelcast          * endpoint. If you don't specify the instance reference, camel use the          * default hazelcast instance from the camel-hazelcast instance.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|hazelcastInstanceName ( String hazelcastInstanceName)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|hazelcastInstanceName
parameter_list|(
name|String
name|hazelcastInstanceName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"hazelcastInstanceName"
argument_list|,
name|hazelcastInstanceName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use concurrent consumers polling from the SEDA queue.          * The option is a<code>int</code> type.          * @group seda          */
DECL|method|concurrentConsumers ( int concurrentConsumers)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|concurrentConsumers
parameter_list|(
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"concurrentConsumers"
argument_list|,
name|concurrentConsumers
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use concurrent consumers polling from the SEDA queue.          * The option will be converted to a<code>int</code> type.          * @group seda          */
DECL|method|concurrentConsumers ( String concurrentConsumers)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|concurrentConsumers
parameter_list|(
name|String
name|concurrentConsumers
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"concurrentConsumers"
argument_list|,
name|concurrentConsumers
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Milliseconds before consumer continues polling after an error has          * occurred.          * The option is a<code>int</code> type.          * @group seda          */
DECL|method|onErrorDelay (int onErrorDelay)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|onErrorDelay
parameter_list|(
name|int
name|onErrorDelay
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"onErrorDelay"
argument_list|,
name|onErrorDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Milliseconds before consumer continues polling after an error has          * occurred.          * The option will be converted to a<code>int</code> type.          * @group seda          */
DECL|method|onErrorDelay ( String onErrorDelay)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|onErrorDelay
parameter_list|(
name|String
name|onErrorDelay
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"onErrorDelay"
argument_list|,
name|onErrorDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The timeout used when consuming from the SEDA queue. When a timeout          * occurs, the consumer can check whether it is allowed to continue          * running. Setting a lower value allows the consumer to react more          * quickly upon shutdown.          * The option is a<code>int</code> type.          * @group seda          */
DECL|method|pollTimeout (int pollTimeout)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|pollTimeout
parameter_list|(
name|int
name|pollTimeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"pollTimeout"
argument_list|,
name|pollTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The timeout used when consuming from the SEDA queue. When a timeout          * occurs, the consumer can check whether it is allowed to continue          * running. Setting a lower value allows the consumer to react more          * quickly upon shutdown.          * The option will be converted to a<code>int</code> type.          * @group seda          */
DECL|method|pollTimeout (String pollTimeout)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|pollTimeout
parameter_list|(
name|String
name|pollTimeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"pollTimeout"
argument_list|,
name|pollTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If set to true then the consumer runs in transaction mode, where the          * messages in the seda queue will only be removed if the transaction          * commits, which happens when the processing is complete.          * The option is a<code>boolean</code> type.          * @group seda          */
DECL|method|transacted (boolean transacted)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|transacted
parameter_list|(
name|boolean
name|transacted
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transacted"
argument_list|,
name|transacted
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If set to true then the consumer runs in transaction mode, where the          * messages in the seda queue will only be removed if the transaction          * commits, which happens when the processing is complete.          * The option will be converted to a<code>boolean</code> type.          * @group seda          */
DECL|method|transacted (String transacted)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|transacted
parameter_list|(
name|String
name|transacted
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transacted"
argument_list|,
name|transacted
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If set to true the whole Exchange will be transfered. If header or          * body contains not serializable objects, they will be skipped.          * The option is a<code>boolean</code> type.          * @group seda          */
DECL|method|transferExchange ( boolean transferExchange)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|transferExchange
parameter_list|(
name|boolean
name|transferExchange
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transferExchange"
argument_list|,
name|transferExchange
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If set to true the whole Exchange will be transfered. If header or          * body contains not serializable objects, they will be skipped.          * The option will be converted to a<code>boolean</code> type.          * @group seda          */
DECL|method|transferExchange ( String transferExchange)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|transferExchange
parameter_list|(
name|String
name|transferExchange
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transferExchange"
argument_list|,
name|transferExchange
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Hazelcast Instance component.      */
DECL|interface|AdvancedHazelcastInstanceEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedHazelcastInstanceEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|HazelcastInstanceEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|HazelcastInstanceEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedHazelcastInstanceEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedHazelcastInstanceEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedHazelcastInstanceEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedHazelcastInstanceEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.hazelcast.HazelcastOperation</code>      * enum.      */
DECL|enum|HazelcastOperation
specifier|public
specifier|static
enum|enum
name|HazelcastOperation
block|{
DECL|enumConstant|put
DECL|enumConstant|delete
DECL|enumConstant|get
DECL|enumConstant|update
DECL|enumConstant|query
DECL|enumConstant|getAll
DECL|enumConstant|clear
DECL|enumConstant|putIfAbsent
DECL|enumConstant|allAll
DECL|enumConstant|removeAll
DECL|enumConstant|retainAll
DECL|enumConstant|evict
DECL|enumConstant|evictAll
DECL|enumConstant|valueCount
DECL|enumConstant|containsKey
DECL|enumConstant|containsValue
DECL|enumConstant|keySet
DECL|enumConstant|removevalue
DECL|enumConstant|increment
DECL|enumConstant|decrement
DECL|enumConstant|setvalue
DECL|enumConstant|destroy
DECL|enumConstant|compareAndSet
DECL|enumConstant|getAndAdd
DECL|enumConstant|add
DECL|enumConstant|offer
DECL|enumConstant|peek
DECL|enumConstant|poll
DECL|enumConstant|remainingCapacity
DECL|enumConstant|drainTo
DECL|enumConstant|removeIf
DECL|enumConstant|take
DECL|enumConstant|publish
DECL|enumConstant|readOnceHeal
DECL|enumConstant|readOnceTail
DECL|enumConstant|capacity
name|put
block|,
name|delete
block|,
name|get
block|,
name|update
block|,
name|query
block|,
name|getAll
block|,
name|clear
block|,
name|putIfAbsent
block|,
name|allAll
block|,
name|removeAll
block|,
name|retainAll
block|,
name|evict
block|,
name|evictAll
block|,
name|valueCount
block|,
name|containsKey
block|,
name|containsValue
block|,
name|keySet
block|,
name|removevalue
block|,
name|increment
block|,
name|decrement
block|,
name|setvalue
block|,
name|destroy
block|,
name|compareAndSet
block|,
name|getAndAdd
block|,
name|add
block|,
name|offer
block|,
name|peek
block|,
name|poll
block|,
name|remainingCapacity
block|,
name|drainTo
block|,
name|removeIf
block|,
name|take
block|,
name|publish
block|,
name|readOnceHeal
block|,
name|readOnceTail
block|,
name|capacity
block|;     }
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.hazelcast.queue.HazelcastQueueConsumerMode</code> enum.      */
DECL|enum|HazelcastQueueConsumerMode
specifier|public
specifier|static
enum|enum
name|HazelcastQueueConsumerMode
block|{
DECL|enumConstant|listen
DECL|enumConstant|poll
name|listen
block|,
name|poll
block|;     }
comment|/**      * The hazelcast-instance component is used to consume join/leave events of      * the cache instance in the cluster. Creates a builder to build endpoints      * for the Hazelcast Instance component.      */
DECL|method|hazelcastInstance (String path)
specifier|default
name|HazelcastInstanceEndpointBuilder
name|hazelcastInstance
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|HazelcastInstanceEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|HazelcastInstanceEndpointBuilder
implements|,
name|AdvancedHazelcastInstanceEndpointBuilder
block|{
specifier|public
name|HazelcastInstanceEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"hazelcast-instance"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|HazelcastInstanceEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

