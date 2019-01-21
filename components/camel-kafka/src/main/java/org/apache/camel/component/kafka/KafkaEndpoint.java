begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|ExecutorService
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
name|Consumer
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
name|Message
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
name|MultipleConsumersSupport
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
name|Processor
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
name|Producer
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
name|impl
operator|.
name|SynchronousDelegateProducer
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
name|ClassResolver
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
name|UriEndpoint
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
name|UriParam
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
name|DefaultEndpoint
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
name|CastUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|Partitioner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|ProducerConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|common
operator|.
name|serialization
operator|.
name|Deserializer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|common
operator|.
name|serialization
operator|.
name|Serializer
import|;
end_import

begin_comment
comment|/**  * The kafka component allows messages to be sent to (or consumed from) Apache Kafka brokers.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.13.0"
argument_list|,
name|scheme
operator|=
literal|"kafka"
argument_list|,
name|title
operator|=
literal|"Kafka"
argument_list|,
name|syntax
operator|=
literal|"kafka:topic"
argument_list|,
name|label
operator|=
literal|"messaging"
argument_list|)
DECL|class|KafkaEndpoint
specifier|public
class|class
name|KafkaEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|MultipleConsumersSupport
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|KafkaConfiguration
name|configuration
init|=
operator|new
name|KafkaConfiguration
argument_list|()
decl_stmt|;
DECL|method|KafkaEndpoint ()
specifier|public
name|KafkaEndpoint
parameter_list|()
block|{     }
DECL|method|KafkaEndpoint (String endpointUri, KafkaComponent component)
specifier|public
name|KafkaEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|KafkaComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|KafkaComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|KafkaComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|KafkaConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (KafkaConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|KafkaConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|KafkaConsumer
name|consumer
init|=
operator|new
name|KafkaConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|KafkaProducer
name|producer
init|=
name|createProducer
argument_list|(
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSynchronous
argument_list|()
condition|)
block|{
return|return
operator|new
name|SynchronousDelegateProducer
argument_list|(
name|producer
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|producer
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isMultipleConsumersSupported ()
specifier|public
name|boolean
name|isMultipleConsumersSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|loadParitionerClass (ClassResolver resolver, Properties props)
specifier|private
name|void
name|loadParitionerClass
parameter_list|(
name|ClassResolver
name|resolver
parameter_list|,
name|Properties
name|props
parameter_list|)
block|{
name|replaceWithClass
argument_list|(
name|props
argument_list|,
literal|"partitioner.class"
argument_list|,
name|resolver
argument_list|,
name|Partitioner
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|loadClass (Object o, ClassResolver resolver, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|loadClass
parameter_list|(
name|Object
name|o
parameter_list|,
name|ClassResolver
name|resolver
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|o
operator|instanceof
name|Class
condition|)
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|o
argument_list|)
return|;
block|}
name|String
name|name
init|=
name|o
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|T
argument_list|>
name|c
init|=
name|resolver
operator|.
name|resolveClass
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
name|c
operator|=
name|resolver
operator|.
name|resolveClass
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
name|c
operator|=
name|resolver
operator|.
name|resolveClass
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|KafkaProducer
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
DECL|method|replaceWithClass (Properties props, String key, ClassResolver resolver, Class<?> type)
name|void
name|replaceWithClass
parameter_list|(
name|Properties
name|props
parameter_list|,
name|String
name|key
parameter_list|,
name|ClassResolver
name|resolver
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
name|loadClass
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|,
name|resolver
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|updateClassProperties (Properties props)
specifier|public
name|void
name|updateClassProperties
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ClassResolver
name|resolver
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
decl_stmt|;
name|replaceWithClass
argument_list|(
name|props
argument_list|,
name|ProducerConfig
operator|.
name|KEY_SERIALIZER_CLASS_CONFIG
argument_list|,
name|resolver
argument_list|,
name|Serializer
operator|.
name|class
argument_list|)
expr_stmt|;
name|replaceWithClass
argument_list|(
name|props
argument_list|,
name|ProducerConfig
operator|.
name|VALUE_SERIALIZER_CLASS_CONFIG
argument_list|,
name|resolver
argument_list|,
name|Serializer
operator|.
name|class
argument_list|)
expr_stmt|;
name|replaceWithClass
argument_list|(
name|props
argument_list|,
name|ConsumerConfig
operator|.
name|KEY_DESERIALIZER_CLASS_CONFIG
argument_list|,
name|resolver
argument_list|,
name|Deserializer
operator|.
name|class
argument_list|)
expr_stmt|;
name|replaceWithClass
argument_list|(
name|props
argument_list|,
name|ConsumerConfig
operator|.
name|VALUE_DESERIALIZER_CLASS_CONFIG
argument_list|,
name|resolver
argument_list|,
name|Deserializer
operator|.
name|class
argument_list|)
expr_stmt|;
try|try
block|{
comment|//doesn't exist in old version of Kafka client so detect and only call the method if
comment|//the field/config actually exists
name|Field
name|f
init|=
name|ProducerConfig
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"PARTITIONER_CLASS_CONFIG"
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
name|loadParitionerClass
argument_list|(
name|resolver
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
comment|//doesn't work as it needs to be List<String>  :(
comment|//replaceWithClass(props, "partition.assignment.strategy", resolver, PartitionAssignor.class);
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//can ignore and Kafka itself might be able to handle it, if not, it will throw an exception
name|log
operator|.
name|debug
argument_list|(
literal|"Problem loading classes for Serializers"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createExecutor ()
specifier|public
name|ExecutorService
name|createExecutor
parameter_list|()
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newFixedThreadPool
argument_list|(
name|this
argument_list|,
literal|"KafkaConsumer["
operator|+
name|configuration
operator|.
name|getTopic
argument_list|()
operator|+
literal|"]"
argument_list|,
name|configuration
operator|.
name|getConsumerStreams
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createProducerExecutor ()
specifier|public
name|ExecutorService
name|createProducerExecutor
parameter_list|()
block|{
name|int
name|core
init|=
name|getConfiguration
argument_list|()
operator|.
name|getWorkerPoolCoreSize
argument_list|()
decl_stmt|;
name|int
name|max
init|=
name|getConfiguration
argument_list|()
operator|.
name|getWorkerPoolMaxSize
argument_list|()
decl_stmt|;
return|return
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
literal|"KafkaProducer["
operator|+
name|configuration
operator|.
name|getTopic
argument_list|()
operator|+
literal|"]"
argument_list|,
name|core
argument_list|,
name|max
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|createKafkaExchange (ConsumerRecord record)
specifier|public
name|Exchange
name|createKafkaExchange
parameter_list|(
name|ConsumerRecord
name|record
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION
argument_list|,
name|record
operator|.
name|partition
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|TOPIC
argument_list|,
name|record
operator|.
name|topic
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|OFFSET
argument_list|,
name|record
operator|.
name|offset
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|HEADERS
argument_list|,
name|record
operator|.
name|headers
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|TIMESTAMP
argument_list|,
name|record
operator|.
name|timestamp
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|record
operator|.
name|key
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|,
name|record
operator|.
name|key
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setBody
argument_list|(
name|record
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createProducer (KafkaEndpoint endpoint)
specifier|protected
name|KafkaProducer
name|createProducer
parameter_list|(
name|KafkaEndpoint
name|endpoint
parameter_list|)
block|{
return|return
operator|new
name|KafkaProducer
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
block|}
end_class

end_unit

