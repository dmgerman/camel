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
name|CamelException
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
name|CamelExchangeException
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
name|serialization
operator|.
name|Serializer
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
DECL|method|loadClass (Object o, ClassLoader loader)
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
name|Object
name|o
parameter_list|,
name|ClassLoader
name|loader
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
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|o
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
name|?
argument_list|>
name|c
decl_stmt|;
try|try
block|{
name|c
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|name
argument_list|,
literal|true
argument_list|,
name|loader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|c
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|c
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|name
argument_list|,
literal|true
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|c
operator|=
literal|null
expr_stmt|;
block|}
block|}
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|c
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|name
argument_list|,
literal|true
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
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|c
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|c
return|;
block|}
DECL|method|replaceWithClass (Properties props, String key, ClassLoader loader, Class<?> type)
name|void
name|replaceWithClass
parameter_list|(
name|Properties
name|props
parameter_list|,
name|String
name|key
parameter_list|,
name|ClassLoader
name|loader
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
name|loader
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
if|if
condition|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ClassLoader
name|loader
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
name|replaceWithClass
argument_list|(
name|props
argument_list|,
literal|"key.serializer"
argument_list|,
name|loader
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
literal|"value.serializer"
argument_list|,
name|loader
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
literal|"partitioner.class"
argument_list|,
name|loader
argument_list|,
name|Partitioner
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getBrokers
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|ProducerConfig
operator|.
name|BOOTSTRAP_SERVERS_CONFIG
argument_list|,
name|endpoint
operator|.
name|getBrokers
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|props
return|;
block|}
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
literal|"unchecked"
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
name|CamelException
block|{
name|String
name|topic
init|=
name|endpoint
operator|.
name|getTopic
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
name|topic
operator|=
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
name|topic
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|topic
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"No topic key set"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
specifier|final
name|Object
name|partitionKey
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
name|PARTITION_KEY
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
specifier|final
name|Object
name|messageKey
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
name|KEY
argument_list|)
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
operator|new
name|Integer
argument_list|(
name|partitionKey
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|messageKey
argument_list|,
name|msgList
operator|.
name|next
argument_list|()
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
name|messageKey
argument_list|,
name|msgList
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
return|return
operator|new
name|ProducerRecord
argument_list|(
name|msgTopic
argument_list|,
name|msgList
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
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
operator|new
name|Integer
argument_list|(
name|partitionKey
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|messageKey
argument_list|,
name|msg
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
name|messageKey
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"No message key or partition key set"
argument_list|)
expr_stmt|;
name|record
operator|=
operator|new
name|ProducerRecord
argument_list|(
name|topic
argument_list|,
name|msg
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
literal|"unchecked"
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
name|ProducerRecord
argument_list|>
argument_list|>
name|futures
init|=
operator|new
name|LinkedList
argument_list|<
name|Future
argument_list|<
name|ProducerRecord
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|c
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|futures
operator|.
name|add
argument_list|(
name|kafkaProducer
operator|.
name|send
argument_list|(
name|c
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Future
argument_list|<
name|ProducerRecord
argument_list|>
name|f
range|:
name|futures
control|)
block|{
comment|//wait for them all to be sent
name|f
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
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
name|kafkaProducer
operator|.
name|send
argument_list|(
name|c
operator|.
name|next
argument_list|()
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

