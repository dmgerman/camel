begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|io
operator|.
name|undertow
operator|.
name|client
operator|.
name|ClientCallback
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|client
operator|.
name|ClientConnection
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|client
operator|.
name|ClientExchange
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|client
operator|.
name|ClientRequest
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|client
operator|.
name|UndertowClient
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|Headers
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|Protocols
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
name|TypeConverter
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
name|ExchangeHelper
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

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|BufferAllocator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|ByteBufferSlicePool
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|IoFuture
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|OptionMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|Xnio
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|XnioWorker
import|;
end_import

begin_comment
comment|/**  * The Undertow producer.  *  * The implementation of Producer is considered as experimental. The Undertow client classes are not thread safe,  * their purpose is for the reverse proxy usage inside Undertow itself. This may change in the future versions and  * general purpose HTTP client wrapper will be added. Therefore this Producer may be changed too.  */
end_comment

begin_class
DECL|class|UndertowProducer
specifier|public
class|class
name|UndertowProducer
extends|extends
name|DefaultAsyncProducer
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
name|UndertowProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|UndertowEndpoint
name|endpoint
decl_stmt|;
DECL|method|UndertowProducer (UndertowEndpoint endpoint)
specifier|public
name|UndertowProducer
parameter_list|(
name|UndertowEndpoint
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
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|UndertowEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
annotation|@
name|Override
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
specifier|final
name|UndertowClient
name|client
init|=
name|UndertowClient
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|XnioWorker
name|worker
init|=
name|Xnio
operator|.
name|getInstance
argument_list|()
operator|.
name|createWorker
argument_list|(
name|OptionMap
operator|.
name|EMPTY
argument_list|)
decl_stmt|;
name|IoFuture
argument_list|<
name|ClientConnection
argument_list|>
name|connect
init|=
name|client
operator|.
name|connect
argument_list|(
name|endpoint
operator|.
name|getHttpURI
argument_list|()
argument_list|,
name|worker
argument_list|,
operator|new
name|ByteBufferSlicePool
argument_list|(
name|BufferAllocator
operator|.
name|DIRECT_BYTE_BUFFER_ALLOCATOR
argument_list|,
literal|8192
argument_list|,
literal|8192
operator|*
literal|8192
argument_list|)
argument_list|,
name|OptionMap
operator|.
name|EMPTY
argument_list|)
decl_stmt|;
name|ClientRequest
name|request
init|=
operator|new
name|ClientRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setProtocol
argument_list|(
name|Protocols
operator|.
name|HTTP_1_1
argument_list|)
expr_stmt|;
name|request
operator|.
name|setPath
argument_list|(
name|endpoint
operator|.
name|getHttpURI
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|getRequestBody
argument_list|(
name|request
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|TypeConverter
name|tc
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
name|ByteBuffer
name|bodyAsByte
init|=
name|tc
operator|.
name|convertTo
argument_list|(
name|ByteBuffer
operator|.
name|class
argument_list|,
name|body
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getRequestHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|Headers
operator|.
name|CONTENT_LENGTH
argument_list|,
name|bodyAsByte
operator|.
name|array
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
name|connect
operator|.
name|get
argument_list|()
operator|.
name|sendRequest
argument_list|(
name|request
argument_list|,
operator|new
name|UndertowProducerCallback
argument_list|(
name|bodyAsByte
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
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
comment|// use async routing engine
return|return
literal|false
return|;
block|}
DECL|method|getRequestBody (ClientRequest request, Exchange camelExchange)
specifier|private
name|Object
name|getRequestBody
parameter_list|(
name|ClientRequest
name|request
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
block|{
name|Object
name|result
decl_stmt|;
name|result
operator|=
name|endpoint
operator|.
name|getUndertowHttpBinding
argument_list|()
operator|.
name|toHttpRequest
argument_list|(
name|request
argument_list|,
name|camelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Everything important happens in callback      */
DECL|class|UndertowProducerCallback
specifier|private
class|class
name|UndertowProducerCallback
implements|implements
name|ClientCallback
argument_list|<
name|ClientExchange
argument_list|>
block|{
DECL|field|body
specifier|private
specifier|final
name|ByteBuffer
name|body
decl_stmt|;
DECL|field|camelExchange
specifier|private
specifier|final
name|Exchange
name|camelExchange
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|method|UndertowProducerCallback (ByteBuffer body, Exchange camelExchange, AsyncCallback callback)
specifier|public
name|UndertowProducerCallback
parameter_list|(
name|ByteBuffer
name|body
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
name|this
operator|.
name|camelExchange
operator|=
name|camelExchange
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
comment|// TODO: Add some logging of those events at trace or debug level
annotation|@
name|Override
DECL|method|completed (ClientExchange clientExchange)
specifier|public
name|void
name|completed
parameter_list|(
name|ClientExchange
name|clientExchange
parameter_list|)
block|{
name|clientExchange
operator|.
name|setResponseListener
argument_list|(
operator|new
name|ClientCallback
argument_list|<
name|ClientExchange
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|completed
parameter_list|(
name|ClientExchange
name|clientExchange
parameter_list|)
block|{
try|try
block|{
name|Message
name|message
init|=
name|endpoint
operator|.
name|getUndertowHttpBinding
argument_list|()
operator|.
name|toCamelMessage
argument_list|(
name|clientExchange
argument_list|,
name|camelExchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|camelExchange
argument_list|)
condition|)
block|{
name|camelExchange
operator|.
name|setOut
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|camelExchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|camelExchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// make sure to call callback
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|failed
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|camelExchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// make sure to call callback
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
try|try
block|{
comment|//send body if exists
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|clientExchange
operator|.
name|getRequestChannel
argument_list|()
operator|.
name|write
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|camelExchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// make sure to call callback
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|failed (IOException e)
specifier|public
name|void
name|failed
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|camelExchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// make sure to call callback
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

