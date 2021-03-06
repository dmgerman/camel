begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|InputStream
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
name|nio
operator|.
name|channels
operator|.
name|Channels
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|ReadableByteChannel
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|channels
operator|.
name|StreamSinkChannel
import|;
end_import

begin_class
DECL|class|UndertowStreamingClientCallback
class|class
name|UndertowStreamingClientCallback
extends|extends
name|UndertowClientCallback
block|{
DECL|field|bodyStream
specifier|private
name|InputStream
name|bodyStream
decl_stmt|;
DECL|method|UndertowStreamingClientCallback (Exchange exchange, AsyncCallback callback, UndertowEndpoint endpoint, ClientRequest request, ByteBuffer body)
name|UndertowStreamingClientCallback
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|UndertowEndpoint
name|endpoint
parameter_list|,
name|ClientRequest
name|request
parameter_list|,
name|ByteBuffer
name|body
parameter_list|)
block|{
name|super
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|endpoint
argument_list|,
name|request
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|this
operator|.
name|bodyStream
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|UndertowStreamingClientCallback (Exchange exchange, AsyncCallback callback, UndertowEndpoint endpoint, ClientRequest request, InputStream body)
name|UndertowStreamingClientCallback
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|UndertowEndpoint
name|endpoint
parameter_list|,
name|ClientRequest
name|request
parameter_list|,
name|InputStream
name|body
parameter_list|)
block|{
name|super
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|endpoint
argument_list|,
name|request
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|bodyStream
operator|=
name|body
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|finish (Message result)
specifier|protected
name|void
name|finish
parameter_list|(
name|Message
name|result
parameter_list|)
block|{
name|boolean
name|close
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
operator|&&
name|result
operator|.
name|getBody
argument_list|()
operator|instanceof
name|InputStream
condition|)
block|{
comment|// no connection closing as streaming continues downstream
name|close
operator|=
literal|false
expr_stmt|;
block|}
name|finish
argument_list|(
name|result
argument_list|,
name|close
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|writeRequest (ClientExchange clientExchange)
specifier|protected
name|void
name|writeRequest
parameter_list|(
name|ClientExchange
name|clientExchange
parameter_list|)
block|{
if|if
condition|(
name|bodyStream
operator|==
literal|null
condition|)
block|{
name|super
operator|.
name|writeRequest
argument_list|(
name|clientExchange
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// send request stream
name|StreamSinkChannel
name|requestChannel
init|=
name|clientExchange
operator|.
name|getRequestChannel
argument_list|()
decl_stmt|;
try|try
init|(
name|ReadableByteChannel
name|source
init|=
name|Channels
operator|.
name|newChannel
argument_list|(
name|bodyStream
argument_list|)
init|)
block|{
name|IOHelper
operator|.
name|transfer
argument_list|(
name|source
argument_list|,
name|requestChannel
argument_list|)
expr_stmt|;
name|flush
argument_list|(
name|requestChannel
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
name|e
parameter_list|)
block|{
name|hasFailedWith
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

