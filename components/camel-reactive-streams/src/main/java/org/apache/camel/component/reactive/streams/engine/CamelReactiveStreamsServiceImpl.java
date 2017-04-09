begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|engine
package|;
end_package

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
name|ExecutorService
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
name|Function
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeDataSupport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|OpenDataException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|OpenType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|SimpleType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularDataSupport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularType
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
name|ExchangePattern
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|RouteBuilder
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|ReactiveStreamsComponent
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|ReactiveStreamsConsumer
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|ReactiveStreamsProducer
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsService
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|DispatchCallback
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|util
operator|.
name|ConvertingPublisher
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|util
operator|.
name|ConvertingSubscriber
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|util
operator|.
name|MonoPublisher
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|util
operator|.
name|UnwrapStreamProcessor
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
name|spi
operator|.
name|Synchronization
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
name|reactivestreams
operator|.
name|Publisher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Subscriber
import|;
end_import

begin_comment
comment|/**  * The default implementation of the reactive streams service.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed CamelReactiveStreamsService"
argument_list|)
DECL|class|CamelReactiveStreamsServiceImpl
specifier|public
class|class
name|CamelReactiveStreamsServiceImpl
extends|extends
name|ServiceSupport
implements|implements
name|CamelReactiveStreamsService
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|workerPool
specifier|private
name|ExecutorService
name|workerPool
decl_stmt|;
DECL|field|publishers
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|CamelPublisher
argument_list|>
name|publishers
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|subscribers
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|CamelSubscriber
argument_list|>
name|subscribers
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|publishedUriToStream
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|publishedUriToStream
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|requestedUriToStream
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestedUriToStream
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|CamelReactiveStreamsServiceImpl ()
specifier|public
name|CamelReactiveStreamsServiceImpl
parameter_list|()
block|{     }
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
name|ReactiveStreamsComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"reactive-streams"
argument_list|,
name|ReactiveStreamsComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ReactiveStreamsEngineConfiguration
name|config
init|=
name|component
operator|.
name|getInternalEngineConfiguration
argument_list|()
decl_stmt|;
name|this
operator|.
name|workerPool
operator|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
name|config
operator|.
name|getThreadPoolName
argument_list|()
argument_list|,
name|config
operator|.
name|getThreadPoolMinSize
argument_list|()
argument_list|,
name|config
operator|.
name|getThreadPoolMaxSize
argument_list|()
argument_list|)
expr_stmt|;
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
name|this
operator|.
name|workerPool
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|this
operator|.
name|workerPool
argument_list|)
expr_stmt|;
name|this
operator|.
name|workerPool
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|fromStream (String name)
specifier|public
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|fromStream
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|UnwrappingPublisher
argument_list|<>
argument_list|(
name|getPayloadPublisher
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|fromStream (String name, Class<T> cls)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Publisher
argument_list|<
name|T
argument_list|>
name|fromStream
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|Exchange
operator|.
name|class
operator|.
name|equals
argument_list|(
name|cls
argument_list|)
condition|)
block|{
return|return
operator|(
name|Publisher
argument_list|<
name|T
argument_list|>
operator|)
name|fromStream
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
operator|new
name|ConvertingPublisher
argument_list|<
name|T
argument_list|>
argument_list|(
name|fromStream
argument_list|(
name|name
argument_list|)
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|streamSubscriber (String name)
specifier|public
name|CamelSubscriber
name|streamSubscriber
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|subscribers
operator|.
name|computeIfAbsent
argument_list|(
name|name
argument_list|,
name|n
lambda|->
operator|new
name|CamelSubscriber
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|subscribers
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|streamSubscriber (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Subscriber
argument_list|<
name|T
argument_list|>
name|streamSubscriber
parameter_list|(
name|String
name|name
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
name|Exchange
operator|.
name|class
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
operator|(
name|Subscriber
argument_list|<
name|T
argument_list|>
operator|)
name|streamSubscriber
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
operator|new
name|ConvertingSubscriber
argument_list|<
name|T
argument_list|>
argument_list|(
name|streamSubscriber
argument_list|(
name|name
argument_list|)
argument_list|,
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|sendCamelExchange (String name, Exchange exchange, DispatchCallback<Exchange> callback)
specifier|public
name|void
name|sendCamelExchange
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|DispatchCallback
argument_list|<
name|Exchange
argument_list|>
name|callback
parameter_list|)
block|{
name|StreamPayload
argument_list|<
name|Exchange
argument_list|>
name|payload
init|=
operator|new
name|StreamPayload
argument_list|<>
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
decl_stmt|;
name|getPayloadPublisher
argument_list|(
name|name
argument_list|)
operator|.
name|publish
argument_list|(
name|payload
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toStream (String name, Object data)
specifier|public
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|toStream
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|data
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|convertToExchange
argument_list|(
name|data
argument_list|)
decl_stmt|;
return|return
name|doRequest
argument_list|(
name|name
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toStream (String name)
specifier|public
name|Function
argument_list|<
name|?
argument_list|,
name|?
extends|extends
name|Publisher
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|toStream
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|data
lambda|->
name|toStream
argument_list|(
name|name
argument_list|,
name|data
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toStream (String name, Object data, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Publisher
argument_list|<
name|T
argument_list|>
name|toStream
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|data
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|ConvertingPublisher
argument_list|<>
argument_list|(
name|toStream
argument_list|(
name|name
argument_list|,
name|data
argument_list|)
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|doRequest (String name, Exchange data)
specifier|protected
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|doRequest
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|data
parameter_list|)
block|{
name|ReactiveStreamsConsumer
name|consumer
init|=
name|streamSubscriber
argument_list|(
name|name
argument_list|)
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
if|if
condition|(
name|consumer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No consumers attached to the stream "
operator|+
name|name
argument_list|)
throw|;
block|}
name|DelayedMonoPublisher
argument_list|<
name|Exchange
argument_list|>
name|publisher
init|=
operator|new
name|DelayedMonoPublisher
argument_list|<>
argument_list|(
name|this
operator|.
name|workerPool
argument_list|)
decl_stmt|;
name|data
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|Synchronization
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|publisher
operator|.
name|setData
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Throwable
name|throwable
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|throwable
operator|==
literal|null
condition|)
block|{
name|throwable
operator|=
operator|new
name|IllegalStateException
argument_list|(
literal|"Unknown Exception"
argument_list|)
expr_stmt|;
block|}
name|publisher
operator|.
name|setException
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|process
argument_list|(
name|data
argument_list|,
name|doneSync
lambda|->
block|{         }
argument_list|)
expr_stmt|;
return|return
name|publisher
return|;
block|}
annotation|@
name|Override
DECL|method|toStream (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Function
argument_list|<
name|Object
argument_list|,
name|Publisher
argument_list|<
name|T
argument_list|>
argument_list|>
name|toStream
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|data
lambda|->
name|toStream
argument_list|(
name|name
argument_list|,
name|data
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|getPayloadPublisher (String name)
specifier|private
name|CamelPublisher
name|getPayloadPublisher
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|publishers
operator|.
name|computeIfAbsent
argument_list|(
name|name
argument_list|,
name|n
lambda|->
operator|new
name|CamelPublisher
argument_list|(
name|this
operator|.
name|workerPool
argument_list|,
name|this
operator|.
name|context
argument_list|,
name|n
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|publishers
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|from (String uri)
specifier|public
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|from
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|publishedUriToStream
operator|.
name|computeIfAbsent
argument_list|(
name|uri
argument_list|,
name|u
lambda|->
block|{
try|try
block|{
name|String
name|uuid
init|=
name|context
operator|.
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
decl_stmt|;
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|u
argument_list|)
operator|.
name|to
argument_list|(
literal|"reactive-streams:"
operator|+
name|uuid
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|uuid
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to create source reactive stream from direct URI: "
operator|+
name|uri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|fromStream
argument_list|(
name|publishedUriToStream
operator|.
name|get
argument_list|(
name|uri
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|from (String uri, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Publisher
argument_list|<
name|T
argument_list|>
name|from
parameter_list|(
name|String
name|uri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|ConvertingPublisher
argument_list|<
name|T
argument_list|>
argument_list|(
name|from
argument_list|(
name|uri
argument_list|)
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|subscriber (String uri)
specifier|public
name|Subscriber
argument_list|<
name|Exchange
argument_list|>
name|subscriber
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
try|try
block|{
name|String
name|uuid
init|=
name|context
operator|.
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
decl_stmt|;
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"reactive-streams:"
operator|+
name|uuid
argument_list|)
operator|.
name|to
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|streamSubscriber
argument_list|(
name|uuid
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to create source reactive stream towards direct URI: "
operator|+
name|uri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|subscriber (String uri, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Subscriber
argument_list|<
name|T
argument_list|>
name|subscriber
parameter_list|(
name|String
name|uri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|ConvertingSubscriber
argument_list|<
name|T
argument_list|>
argument_list|(
name|subscriber
argument_list|(
name|uri
argument_list|)
argument_list|,
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|to (String uri, Object data)
specifier|public
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|to
parameter_list|(
name|String
name|uri
parameter_list|,
name|Object
name|data
parameter_list|)
block|{
name|requestedUriToStream
operator|.
name|computeIfAbsent
argument_list|(
name|uri
argument_list|,
name|u
lambda|->
block|{
try|try
block|{
name|String
name|uuid
init|=
name|context
operator|.
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
decl_stmt|;
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"reactive-streams:"
operator|+
name|uuid
argument_list|)
operator|.
name|to
argument_list|(
name|u
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|uuid
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to create requested reactive stream from direct URI: "
operator|+
name|uri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|toStream
argument_list|(
name|requestedUriToStream
operator|.
name|get
argument_list|(
name|uri
argument_list|)
argument_list|,
name|data
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|to (String uri)
specifier|public
name|Function
argument_list|<
name|Object
argument_list|,
name|Publisher
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|to
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|data
lambda|->
name|to
argument_list|(
name|uri
argument_list|,
name|data
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|to (String uri, Object data, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Publisher
argument_list|<
name|T
argument_list|>
name|to
parameter_list|(
name|String
name|uri
parameter_list|,
name|Object
name|data
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|ConvertingPublisher
argument_list|<
name|T
argument_list|>
argument_list|(
name|to
argument_list|(
name|uri
argument_list|,
name|data
argument_list|)
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|to (String uri, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Function
argument_list|<
name|Object
argument_list|,
name|Publisher
argument_list|<
name|T
argument_list|>
argument_list|>
name|to
parameter_list|(
name|String
name|uri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|data
lambda|->
name|to
argument_list|(
name|uri
argument_list|,
name|data
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|process (String uri, Function<? super Publisher<Exchange>, ?> processor)
specifier|public
name|void
name|process
parameter_list|(
name|String
name|uri
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|Publisher
argument_list|<
name|Exchange
argument_list|>
argument_list|,
name|?
argument_list|>
name|processor
parameter_list|)
block|{
try|try
block|{
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|uri
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|Exchange
name|copy
init|=
name|exchange
operator|.
name|copy
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|processor
operator|.
name|apply
argument_list|(
operator|new
name|MonoPublisher
argument_list|<>
argument_list|(
name|copy
argument_list|)
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|UnwrapStreamProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to add reactive stream processor to the direct URI: "
operator|+
name|uri
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|process (String uri, Class<T> type, Function<? super Publisher<T>, ?> processor)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|process
parameter_list|(
name|String
name|uri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|Publisher
argument_list|<
name|T
argument_list|>
argument_list|,
name|?
argument_list|>
name|processor
parameter_list|)
block|{
name|process
argument_list|(
name|uri
argument_list|,
name|exPub
lambda|->
name|processor
operator|.
name|apply
argument_list|(
operator|new
name|ConvertingPublisher
argument_list|<
name|T
argument_list|>
argument_list|(
name|exPub
argument_list|,
name|type
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|attachCamelConsumer (String name, ReactiveStreamsConsumer consumer)
specifier|public
name|CamelSubscriber
name|attachCamelConsumer
parameter_list|(
name|String
name|name
parameter_list|,
name|ReactiveStreamsConsumer
name|consumer
parameter_list|)
block|{
name|CamelSubscriber
name|subscriber
init|=
name|streamSubscriber
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|subscriber
operator|.
name|attachConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|subscriber
return|;
block|}
annotation|@
name|Override
DECL|method|detachCamelConsumer (String name)
specifier|public
name|void
name|detachCamelConsumer
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|streamSubscriber
argument_list|(
name|name
argument_list|)
operator|.
name|detachConsumer
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|attachCamelProducer (String name, ReactiveStreamsProducer producer)
specifier|public
name|void
name|attachCamelProducer
parameter_list|(
name|String
name|name
parameter_list|,
name|ReactiveStreamsProducer
name|producer
parameter_list|)
block|{
name|getPayloadPublisher
argument_list|(
name|name
argument_list|)
operator|.
name|attachProducer
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|detachCamelProducer (String name)
specifier|public
name|void
name|detachCamelProducer
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|getPayloadPublisher
argument_list|(
name|name
argument_list|)
operator|.
name|detachProducer
argument_list|()
expr_stmt|;
block|}
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
name|context
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
name|this
operator|.
name|context
return|;
block|}
DECL|method|convertToExchange (Object data)
specifier|private
name|Exchange
name|convertToExchange
parameter_list|(
name|Object
name|data
parameter_list|)
block|{
name|Exchange
name|exchange
decl_stmt|;
if|if
condition|(
name|data
operator|instanceof
name|Exchange
condition|)
block|{
name|exchange
operator|=
operator|(
name|Exchange
operator|)
name|data
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Information about Camel Reactive subscribers"
argument_list|)
DECL|method|camelSubscribers ()
specifier|public
name|TabularData
name|camelSubscribers
parameter_list|()
block|{
try|try
block|{
specifier|final
name|TabularData
name|answer
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|subscriptionsTabularType
argument_list|()
argument_list|)
decl_stmt|;
name|subscribers
operator|.
name|forEach
argument_list|(
parameter_list|(
name|k
parameter_list|,
name|v
parameter_list|)
lambda|->
block|{
try|try
block|{
name|String
name|name
init|=
name|k
decl_stmt|;
name|long
name|inflight
init|=
name|v
operator|.
name|getInflightCount
argument_list|()
decl_stmt|;
name|long
name|requested
init|=
name|v
operator|.
name|getRequested
argument_list|()
decl_stmt|;
name|long
name|bufferSize
init|=
name|v
operator|.
name|getBufferSize
argument_list|()
decl_stmt|;
name|String
name|backpressure
init|=
name|v
operator|.
name|getBackpressureStrategy
argument_list|()
operator|!=
literal|null
condition|?
name|v
operator|.
name|getBackpressureStrategy
argument_list|()
operator|.
name|name
argument_list|()
else|:
literal|""
decl_stmt|;
name|CompositeType
name|ct
init|=
name|subscriptionsCompositeType
argument_list|()
decl_stmt|;
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|ct
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"name"
operator|,
literal|"inflight"
operator|,
literal|"requested"
operator|,
literal|"buffer size"
operator|,
literal|"back pressure"
block|}
operator|,
operator|new
name|Object
index|[]
block|{
name|name
operator|,
name|inflight
operator|,
name|requested
operator|,
name|bufferSize
operator|,
name|backpressure
block|}
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|)
class|;
end_class

begin_return
return|return
name|answer
return|;
end_return

begin_expr_stmt
unit|} catch
operator|(
name|Exception
name|e
operator|)
block|{
throw|throw
argument_list|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
block|;         }
end_expr_stmt

begin_expr_stmt
unit|}      @
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Information about Camel Reactive publishers"
argument_list|)
DECL|method|camelPublishers ()
specifier|public
name|TabularData
name|camelPublishers
argument_list|()
block|{
try|try
block|{
specifier|final
name|TabularData
name|answer
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|publishersTabularType
argument_list|()
argument_list|)
decl_stmt|;
name|publishers
operator|.
name|forEach
argument_list|(
parameter_list|(
name|k
parameter_list|,
name|v
parameter_list|)
lambda|->
block|{
try|try
block|{
name|String
name|name
init|=
name|k
decl_stmt|;
name|int
name|subscribers
init|=
name|v
operator|.
name|getSubscriptionSize
argument_list|()
decl_stmt|;
comment|// TODO: include more subscriber information, either as a nested table or flattern
name|CompositeType
name|ct
init|=
name|publishersCompositeType
argument_list|()
decl_stmt|;
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|ct
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"name"
operator|,
literal|"subscribers"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
operator|,
name|subscribers
block|}
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
end_expr_stmt

begin_catch
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
end_catch

begin_empty_stmt
unit|})
empty_stmt|;
end_empty_stmt

begin_return
return|return
name|answer
return|;
end_return

begin_expr_stmt
unit|} catch
operator|(
name|Exception
name|e
operator|)
block|{
throw|throw
argument_list|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
block|;         }
end_expr_stmt

begin_function
unit|}      private
DECL|method|subscriptionsCompositeType ()
specifier|static
name|CompositeType
name|subscriptionsCompositeType
parameter_list|()
throws|throws
name|OpenDataException
block|{
return|return
operator|new
name|CompositeType
argument_list|(
literal|"subscriptions"
argument_list|,
literal|"Subscriptions"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"name"
block|,
literal|"inflight"
block|,
literal|"requested"
block|,
literal|"buffer size"
block|,
literal|"back pressure"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Name"
block|,
literal|"Inflight"
block|,
literal|"Requested"
block|,
literal|"Buffer Size"
block|,
literal|"Back Pressure"
block|}
argument_list|,
operator|new
name|OpenType
index|[]
block|{
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|LONG
block|,
name|SimpleType
operator|.
name|LONG
block|,
name|SimpleType
operator|.
name|LONG
block|,
name|SimpleType
operator|.
name|STRING
block|}
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|subscriptionsTabularType ()
specifier|private
specifier|static
name|TabularType
name|subscriptionsTabularType
parameter_list|()
throws|throws
name|OpenDataException
block|{
name|CompositeType
name|ct
init|=
name|subscriptionsCompositeType
argument_list|()
decl_stmt|;
return|return
operator|new
name|TabularType
argument_list|(
literal|"subscriptions"
argument_list|,
literal|"Information about Camel Reactive subscribers"
argument_list|,
name|ct
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"name"
block|}
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|publishersCompositeType ()
specifier|private
specifier|static
name|CompositeType
name|publishersCompositeType
parameter_list|()
throws|throws
name|OpenDataException
block|{
return|return
operator|new
name|CompositeType
argument_list|(
literal|"publishers"
argument_list|,
literal|"Publishers"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"name"
block|,
literal|"subscribers"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Name"
block|,
literal|"Subscribers"
block|}
argument_list|,
operator|new
name|OpenType
index|[]
block|{
name|SimpleType
operator|.
name|STRING
block|,
name|SimpleType
operator|.
name|INTEGER
block|}
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|publishersTabularType ()
specifier|private
specifier|static
name|TabularType
name|publishersTabularType
parameter_list|()
throws|throws
name|OpenDataException
block|{
name|CompositeType
name|ct
init|=
name|publishersCompositeType
argument_list|()
decl_stmt|;
return|return
operator|new
name|TabularType
argument_list|(
literal|"publishers"
argument_list|,
literal|"Information about Camel Reactive publishers"
argument_list|,
name|ct
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"name"
block|}
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

