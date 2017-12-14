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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
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
name|atomic
operator|.
name|AtomicInteger
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
name|AsyncCallback
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
name|Endpoint
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
name|impl
operator|.
name|DefaultAsyncProducer
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
name|URISupport
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
name|Callback
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
name|clients
operator|.
name|producer
operator|.
name|ProducerRecord
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
name|RecordMetadata
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
name|utils
operator|.
name|Bytes
import|;
end_import

begin_class
DECL|class|KafkaProducer
specifier|public
class|class
name|KafkaProducer
extends|extends
name|DefaultAsyncProducer
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|field|kafkaProducer
specifier|private
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
name|kafkaProducer
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|KafkaEndpoint
name|endpoint
decl_stmt|;
DECL|field|workerPool
specifier|private
name|ExecutorService
name|workerPool
decl_stmt|;
DECL|field|shutdownWorkerPool
specifier|private
name|boolean
name|shutdownWorkerPool
decl_stmt|;
DECL|method|KafkaProducer (KafkaEndpoint endpoint)
specifier|public
name|KafkaProducer
parameter_list|(
name|KafkaEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|getProps ()
name|Properties
name|getProps
parameter_list|()
block|{
name|Properties
name|props
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|createProducerProperties
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|updateClassProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
comment|// brokers can be configured on endpoint or component level
name|String
name|brokers
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getBrokers
argument_list|()
decl_stmt|;
if|if
condition|(
name|brokers
operator|==
literal|null
condition|)
block|{
name|brokers
operator|=
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|getBrokers
argument_list|()
expr_stmt|;
block|}
name|props
operator|.
name|put
argument_list|(
name|ProducerConfig
operator|.
name|BOOTSTRAP_SERVERS_CONFIG
argument_list|,
name|brokers
argument_list|)
expr_stmt|;
return|return
name|props
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|getKafkaProducer ()
specifier|public
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
name|getKafkaProducer
parameter_list|()
block|{
return|return
name|kafkaProducer
return|;
block|}
comment|/**      * To use a custom {@link org.apache.kafka.clients.producer.KafkaProducer} instance.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|setKafkaProducer (org.apache.kafka.clients.producer.KafkaProducer kafkaProducer)
specifier|public
name|void
name|setKafkaProducer
parameter_list|(
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
name|kafkaProducer
parameter_list|)
block|{
name|this
operator|.
name|kafkaProducer
operator|=
name|kafkaProducer
expr_stmt|;
block|}
DECL|method|getWorkerPool ()
specifier|public
name|ExecutorService
name|getWorkerPool
parameter_list|()
block|{
return|return
name|workerPool
return|;
block|}
DECL|method|setWorkerPool (ExecutorService workerPool)
specifier|public
name|void
name|setWorkerPool
parameter_list|(
name|ExecutorService
name|workerPool
parameter_list|)
block|{
name|this
operator|.
name|workerPool
operator|=
name|workerPool
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
name|getProps
argument_list|()
decl_stmt|;
if|if
condition|(
name|kafkaProducer
operator|==
literal|null
condition|)
block|{
name|ClassLoader
name|threadClassLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
comment|// Kafka uses reflection for loading authentication settings, use its classloader
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
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
name|kafkaProducer
operator|=
operator|new
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
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|threadClassLoader
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if we are in asynchronous mode we need a worker pool
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isSynchronous
argument_list|()
operator|&&
name|workerPool
operator|==
literal|null
condition|)
block|{
name|workerPool
operator|=
name|endpoint
operator|.
name|createProducerExecutor
argument_list|()
expr_stmt|;
comment|// we create a thread pool so we should also shut it down
name|shutdownWorkerPool
operator|=
literal|true
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
name|kafkaProducer
operator|!=
literal|null
condition|)
block|{
name|kafkaProducer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|shutdownWorkerPool
operator|&&
name|workerPool
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdown
argument_list|(
name|workerPool
argument_list|)
expr_stmt|;
name|workerPool
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|createRecorder (Exchange exchange)
specifier|protected
name|Iterator
argument_list|<
name|ProducerRecord
argument_list|>
name|createRecorder
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|topic
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopic
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
name|String
name|headerTopic
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|TOPIC
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|allowHeader
init|=
literal|true
decl_stmt|;
comment|// when we do not bridge then detect if we try to send back to ourselves
comment|// which we most likely do not want to do
if|if
condition|(
name|headerTopic
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isCircularTopicDetection
argument_list|()
condition|)
block|{
name|Endpoint
name|from
init|=
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|from
operator|instanceof
name|KafkaEndpoint
condition|)
block|{
name|String
name|fromTopic
init|=
operator|(
operator|(
name|KafkaEndpoint
operator|)
name|from
operator|)
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopic
argument_list|()
decl_stmt|;
name|allowHeader
operator|=
operator|!
name|headerTopic
operator|.
name|equals
argument_list|(
name|fromTopic
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|allowHeader
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Circular topic detected from message header."
operator|+
literal|" Cannot send to same topic as the message comes from: {}"
operator|+
literal|". Will use endpoint configured topic: {}"
argument_list|,
name|from
argument_list|,
name|topic
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|allowHeader
operator|&&
name|headerTopic
operator|!=
literal|null
condition|)
block|{
name|topic
operator|=
name|headerTopic
expr_stmt|;
block|}
block|}
if|if
condition|(
name|topic
operator|==
literal|null
condition|)
block|{
comment|// if topic property was not received from configuration or header parameters take it from the remaining URI
name|topic
operator|=
name|URISupport
operator|.
name|extractRemainderPath
argument_list|(
operator|new
name|URI
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// endpoint take precedence over header configuration
specifier|final
name|Integer
name|partitionKey
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPartitionKey
argument_list|()
operator|!=
literal|null
condition|?
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPartitionKey
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|hasPartitionKey
init|=
name|partitionKey
operator|!=
literal|null
decl_stmt|;
comment|// endpoint take precedence over header configuration
name|Object
name|key
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getKey
argument_list|()
operator|!=
literal|null
condition|?
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getKey
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|messageKey
init|=
name|key
operator|!=
literal|null
condition|?
name|tryConvertToSerializedType
argument_list|(
name|exchange
argument_list|,
name|key
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getKeySerializerClass
argument_list|()
argument_list|)
else|:
literal|null
decl_stmt|;
specifier|final
name|boolean
name|hasMessageKey
init|=
name|messageKey
operator|!=
literal|null
decl_stmt|;
name|Object
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
comment|// is the message body a list or something that contains multiple values
name|Iterator
argument_list|<
name|Object
argument_list|>
name|iterator
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|msg
operator|instanceof
name|Iterable
condition|)
block|{
name|iterator
operator|=
operator|(
operator|(
name|Iterable
argument_list|<
name|Object
argument_list|>
operator|)
name|msg
operator|)
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|msg
operator|instanceof
name|Iterator
condition|)
block|{
name|iterator
operator|=
operator|(
name|Iterator
argument_list|<
name|Object
argument_list|>
operator|)
name|msg
expr_stmt|;
block|}
if|if
condition|(
name|iterator
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Iterator
argument_list|<
name|Object
argument_list|>
name|msgList
init|=
name|iterator
decl_stmt|;
specifier|final
name|String
name|msgTopic
init|=
name|topic
decl_stmt|;
return|return
operator|new
name|Iterator
argument_list|<
name|ProducerRecord
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|msgList
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ProducerRecord
name|next
parameter_list|()
block|{
comment|// must convert each entry of the iterator into the value according to the serializer
name|Object
name|next
init|=
name|msgList
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|tryConvertToSerializedType
argument_list|(
name|exchange
argument_list|,
name|next
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSerializerClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|hasPartitionKey
operator|&&
name|hasMessageKey
condition|)
block|{
return|return
operator|new
name|ProducerRecord
argument_list|(
name|msgTopic
argument_list|,
name|partitionKey
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|hasMessageKey
condition|)
block|{
return|return
operator|new
name|ProducerRecord
argument_list|(
name|msgTopic
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ProducerRecord
argument_list|(
name|msgTopic
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|msgList
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// must convert each entry of the iterator into the value according to the serializer
name|Object
name|value
init|=
name|tryConvertToSerializedType
argument_list|(
name|exchange
argument_list|,
name|msg
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSerializerClass
argument_list|()
argument_list|)
decl_stmt|;
name|ProducerRecord
name|record
decl_stmt|;
if|if
condition|(
name|hasPartitionKey
operator|&&
name|hasMessageKey
condition|)
block|{
name|record
operator|=
operator|new
name|ProducerRecord
argument_list|(
name|topic
argument_list|,
name|partitionKey
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|hasMessageKey
condition|)
block|{
name|record
operator|=
operator|new
name|ProducerRecord
argument_list|(
name|topic
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|record
operator|=
operator|new
name|ProducerRecord
argument_list|(
name|topic
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|record
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
comment|// Camel calls this method if the endpoint isSynchronous(), as the KafkaEndpoint creates a SynchronousDelegateProducer for it
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Iterator
argument_list|<
name|ProducerRecord
argument_list|>
name|c
init|=
name|createRecorder
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Future
argument_list|<
name|RecordMetadata
argument_list|>
argument_list|>
name|futures
init|=
operator|new
name|LinkedList
argument_list|<
name|Future
argument_list|<
name|RecordMetadata
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|RecordMetadata
argument_list|>
name|recordMetadatas
init|=
operator|new
name|ArrayList
argument_list|<
name|RecordMetadata
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isRecordMetadata
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|KAFKA_RECORDMETA
argument_list|,
name|recordMetadatas
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|KAFKA_RECORDMETA
argument_list|,
name|recordMetadatas
argument_list|)
expr_stmt|;
block|}
block|}
while|while
condition|(
name|c
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProducerRecord
name|rec
init|=
name|c
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sending message to topic: {}, partition: {}, key: {}"
argument_list|,
name|rec
operator|.
name|topic
argument_list|()
argument_list|,
name|rec
operator|.
name|partition
argument_list|()
argument_list|,
name|rec
operator|.
name|key
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|futures
operator|.
name|add
argument_list|(
name|kafkaProducer
operator|.
name|send
argument_list|(
name|rec
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Future
argument_list|<
name|RecordMetadata
argument_list|>
name|f
range|:
name|futures
control|)
block|{
comment|//wait for them all to be sent
name|recordMetadatas
operator|.
name|add
argument_list|(
name|f
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
try|try
block|{
name|Iterator
argument_list|<
name|ProducerRecord
argument_list|>
name|c
init|=
name|createRecorder
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|KafkaProducerCallBack
name|cb
init|=
operator|new
name|KafkaProducerCallBack
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
decl_stmt|;
while|while
condition|(
name|c
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|cb
operator|.
name|increment
argument_list|()
expr_stmt|;
name|ProducerRecord
name|rec
init|=
name|c
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sending message to topic: {}, partition: {}, key: {}"
argument_list|,
name|rec
operator|.
name|topic
argument_list|()
argument_list|,
name|rec
operator|.
name|partition
argument_list|()
argument_list|,
name|rec
operator|.
name|key
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|kafkaProducer
operator|.
name|send
argument_list|(
name|rec
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
return|return
name|cb
operator|.
name|allSent
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**      * Attempts to convert the object to the same type as the serialized class specified      */
DECL|method|tryConvertToSerializedType (Exchange exchange, Object object, String serializerClass)
specifier|protected
name|Object
name|tryConvertToSerializedType
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|object
parameter_list|,
name|String
name|serializerClass
parameter_list|)
block|{
name|Object
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|KafkaConstants
operator|.
name|KAFKA_DEFAULT_SERIALIZER
operator|.
name|equals
argument_list|(
name|serializerClass
argument_list|)
condition|)
block|{
name|answer
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"org.apache.kafka.common.serialization.ByteArraySerializer"
operator|.
name|equals
argument_list|(
name|serializerClass
argument_list|)
condition|)
block|{
name|answer
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|exchange
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"org.apache.kafka.common.serialization.ByteBufferSerializer"
operator|.
name|equals
argument_list|(
name|serializerClass
argument_list|)
condition|)
block|{
name|answer
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|ByteBuffer
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"org.apache.kafka.common.serialization.BytesSerializer"
operator|.
name|equals
argument_list|(
name|serializerClass
argument_list|)
condition|)
block|{
comment|// we need to convert to byte array first
name|byte
index|[]
name|array
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|exchange
argument_list|,
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|Bytes
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
operator|!=
literal|null
condition|?
name|answer
else|:
name|object
return|;
block|}
DECL|class|KafkaProducerCallBack
specifier|private
specifier|final
class|class
name|KafkaProducerCallBack
implements|implements
name|Callback
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|field|count
specifier|private
specifier|final
name|AtomicInteger
name|count
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|recordMetadatas
specifier|private
specifier|final
name|List
argument_list|<
name|RecordMetadata
argument_list|>
name|recordMetadatas
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|KafkaProducerCallBack (Exchange exchange, AsyncCallback callback)
name|KafkaProducerCallBack
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isRecordMetadata
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|KAFKA_RECORDMETA
argument_list|,
name|recordMetadatas
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|KAFKA_RECORDMETA
argument_list|,
name|recordMetadatas
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|increment ()
name|void
name|increment
parameter_list|()
block|{
name|count
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
DECL|method|allSent ()
name|boolean
name|allSent
parameter_list|()
block|{
if|if
condition|(
name|count
operator|.
name|decrementAndGet
argument_list|()
operator|==
literal|0
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"All messages sent, continue routing."
argument_list|)
expr_stmt|;
comment|//was able to get all the work done while queuing the requests
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|onCompletion (RecordMetadata recordMetadata, Exception e)
specifier|public
name|void
name|onCompletion
parameter_list|(
name|RecordMetadata
name|recordMetadata
parameter_list|,
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|recordMetadatas
operator|.
name|add
argument_list|(
name|recordMetadata
argument_list|)
expr_stmt|;
if|if
condition|(
name|count
operator|.
name|decrementAndGet
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// use worker pool to continue routing the exchange
comment|// as this thread is from Kafka Callback and should not be used by Camel routing
name|workerPool
operator|.
name|submit
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"All messages sent, continue routing."
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

