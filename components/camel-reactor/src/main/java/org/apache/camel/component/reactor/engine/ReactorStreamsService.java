begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactor.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactor
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
name|function
operator|.
name|Function
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
name|Supplier
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
name|ReactiveStreamsCamelSubscriber
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
name|ReactiveStreamsHelper
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
name|util
operator|.
name|BodyConverter
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
name|function
operator|.
name|Suppliers
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

begin_import
import|import
name|reactor
operator|.
name|core
operator|.
name|publisher
operator|.
name|Flux
import|;
end_import

begin_import
import|import
name|reactor
operator|.
name|core
operator|.
name|publisher
operator|.
name|Mono
import|;
end_import

begin_class
DECL|class|ReactorStreamsService
specifier|final
class|class
name|ReactorStreamsService
extends|extends
name|ServiceSupport
implements|implements
name|CamelReactiveStreamsService
block|{
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|field|unwrapStreamProcessorSupplier
specifier|private
specifier|final
name|Supplier
argument_list|<
name|UnwrapStreamProcessor
argument_list|>
name|unwrapStreamProcessorSupplier
decl_stmt|;
DECL|field|publishers
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|ReactorCamelProcessor
argument_list|>
name|publishers
decl_stmt|;
DECL|field|subscribers
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|ReactiveStreamsCamelSubscriber
argument_list|>
name|subscribers
decl_stmt|;
DECL|field|publishedUriToStream
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|publishedUriToStream
decl_stmt|;
DECL|field|requestedUriToStream
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestedUriToStream
decl_stmt|;
DECL|method|ReactorStreamsService (CamelContext context)
name|ReactorStreamsService
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|publishers
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|subscribers
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|publishedUriToStream
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|requestedUriToStream
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|unwrapStreamProcessorSupplier
operator|=
name|Suppliers
operator|.
name|memorize
argument_list|(
name|UnwrapStreamProcessor
operator|::
operator|new
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|ReactorStreamsConstants
operator|.
name|SERVICE_NAME
return|;
block|}
comment|// ******************************************
comment|// Lifecycle
comment|// ******************************************
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|ReactorCamelProcessor
name|processor
range|:
name|publishers
operator|.
name|values
argument_list|()
control|)
block|{
name|processor
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|ReactiveStreamsCamelSubscriber
name|subscriber
range|:
name|subscribers
operator|.
name|values
argument_list|()
control|)
block|{
name|subscriber
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|// ******************************************
comment|//
comment|// ******************************************
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
name|getCamelProcessor
argument_list|(
name|name
argument_list|)
operator|.
name|getPublisher
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|fromStream (String name, Class<T> type)
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
name|type
parameter_list|)
block|{
specifier|final
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|publisher
init|=
name|fromStream
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|Exchange
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|Publisher
operator|.
name|class
operator|.
name|cast
argument_list|(
name|publisher
argument_list|)
return|;
block|}
return|return
name|Flux
operator|.
name|from
argument_list|(
name|publisher
argument_list|)
operator|.
name|map
argument_list|(
name|BodyConverter
operator|.
name|forType
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|streamSubscriber (String name)
specifier|public
name|ReactiveStreamsCamelSubscriber
name|streamSubscriber
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|subscribers
operator|.
name|computeIfAbsent
argument_list|(
name|name
argument_list|,
name|n
lambda|->
operator|new
name|ReactiveStreamsCamelSubscriber
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
specifier|final
name|Subscriber
argument_list|<
name|Exchange
argument_list|>
name|subscriber
init|=
name|streamSubscriber
argument_list|(
name|name
argument_list|)
decl_stmt|;
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
name|Subscriber
operator|.
name|class
operator|.
name|cast
argument_list|(
name|subscriber
argument_list|)
return|;
block|}
return|return
operator|new
name|ConvertingSubscriber
argument_list|<>
argument_list|(
name|subscriber
argument_list|,
name|context
argument_list|)
return|;
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
return|return
name|doRequest
argument_list|(
name|name
argument_list|,
name|ReactiveStreamsHelper
operator|.
name|convertToExchange
argument_list|(
name|context
argument_list|,
name|data
argument_list|)
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
specifier|final
name|String
name|name
init|=
name|publishedUriToStream
operator|.
name|computeIfAbsent
argument_list|(
name|uri
argument_list|,
name|camelUri
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
name|context
operator|.
name|addRoutes
argument_list|(
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
name|camelUri
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
decl_stmt|;
return|return
name|fromStream
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|from (String name, Class<T> type)
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
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
specifier|final
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|publisher
init|=
name|from
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|Exchange
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|Publisher
operator|.
name|class
operator|.
name|cast
argument_list|(
name|publisher
argument_list|)
return|;
block|}
return|return
name|Flux
operator|.
name|from
argument_list|(
name|publisher
argument_list|)
operator|.
name|map
argument_list|(
name|BodyConverter
operator|.
name|forType
argument_list|(
name|type
argument_list|)
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
name|context
operator|.
name|addRoutes
argument_list|(
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
argument_list|<>
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
name|String
name|streamName
init|=
name|requestedUriToStream
operator|.
name|computeIfAbsent
argument_list|(
name|uri
argument_list|,
name|camelUri
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
name|context
operator|.
name|addRoutes
argument_list|(
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
name|camelUri
argument_list|)
expr_stmt|;
block|}
block|}
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
decl_stmt|;
return|return
name|toStream
argument_list|(
name|streamName
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
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|publisher
init|=
name|to
argument_list|(
name|uri
argument_list|,
name|data
argument_list|)
decl_stmt|;
return|return
name|Flux
operator|.
name|from
argument_list|(
name|publisher
argument_list|)
operator|.
name|map
argument_list|(
name|BodyConverter
operator|.
name|forType
argument_list|(
name|type
argument_list|)
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
name|context
operator|.
name|addRoutes
argument_list|(
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
name|Mono
operator|.
name|just
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
name|unwrapStreamProcessorSupplier
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
name|publisher
lambda|->
name|processor
operator|.
name|apply
argument_list|(
name|Flux
operator|.
name|from
argument_list|(
name|publisher
argument_list|)
operator|.
name|map
argument_list|(
name|BodyConverter
operator|.
name|forType
argument_list|(
name|type
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// ******************************************
comment|// Producer
comment|// ******************************************
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
name|getCamelProcessor
argument_list|(
name|name
argument_list|)
operator|.
name|attach
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
name|getCamelProcessor
argument_list|(
name|name
argument_list|)
operator|.
name|detach
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|sendCamelExchange (String name, Exchange exchange)
specifier|public
name|void
name|sendCamelExchange
parameter_list|(
name|String
name|name
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|getCamelProcessor
argument_list|(
name|name
argument_list|)
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|getCamelProcessor (String name)
specifier|private
name|ReactorCamelProcessor
name|getCamelProcessor
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|publishers
operator|.
name|computeIfAbsent
argument_list|(
name|name
argument_list|,
name|key
lambda|->
operator|new
name|ReactorCamelProcessor
argument_list|(
name|this
argument_list|,
name|key
argument_list|)
argument_list|)
return|;
block|}
comment|// ******************************************
comment|// Consumer
comment|// ******************************************
annotation|@
name|Override
DECL|method|attachCamelConsumer (String name, ReactiveStreamsConsumer consumer)
specifier|public
name|ReactiveStreamsCamelSubscriber
name|attachCamelConsumer
parameter_list|(
name|String
name|name
parameter_list|,
name|ReactiveStreamsConsumer
name|consumer
parameter_list|)
block|{
name|ReactiveStreamsCamelSubscriber
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
name|ReactiveStreamsCamelSubscriber
name|subscriber
init|=
name|streamSubscriber
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|subscriber
operator|.
name|detachConsumer
argument_list|()
expr_stmt|;
block|}
comment|// *******************************************
comment|// Helpers
comment|// *******************************************
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
return|return
name|Mono
operator|.
expr|<
name|Exchange
operator|>
name|create
argument_list|(
name|sink
lambda|->
name|data
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|Synchronization
argument_list|()
block|{
block|@Override                 public void onComplete(Exchange exchange
argument_list|)
block|{
name|sink
operator|.
name|success
argument_list|(
name|exchange
argument_list|)
block|;                 }
expr|@
name|Override
specifier|public
name|void
name|onFailure
argument_list|(
name|Exchange
name|exchange
argument_list|)
block|{
name|Throwable
name|throwable
operator|=
name|exchange
operator|.
name|getException
argument_list|()
block|;
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
name|sink
operator|.
name|error
argument_list|(
name|throwable
argument_list|)
return|;
block|}
block|}
end_class

begin_expr_stmt
unit|)         )
operator|.
name|doOnSubscribe
argument_list|(
name|subs
lambda|->
name|consumer
operator|.
name|process
argument_list|(
name|data
argument_list|,
name|ReactorStreamsConstants
operator|.
name|EMPTY_ASYNC_CALLBACK
argument_list|)
argument_list|)
expr_stmt|;
end_expr_stmt

unit|} }
end_unit

