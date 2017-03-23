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
name|Closeable
import|;
end_import

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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|Channel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|BlockingDeque
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
name|LinkedBlockingDeque
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
name|Consumer
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
name|util
operator|.
name|HeaderMap
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
name|HttpString
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
name|ChannelExceptionHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|ChannelListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|ChannelListeners
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|IoUtils
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

begin_comment
comment|/**  * Undertow {@link ClientCallback} that will get notified when the HTTP  * connection is ready or when the client failed to connect. It will also handle  * writing the request and reading the response in  * {@link #writeRequest(ClientExchange, ByteBuffer)} and  * {@link #setupResponseListner(ClientExchange)}. The main entry point is  * {@link #completed(ClientConnection)} or {@link #failed(IOException)} in case  * of errors, every error condition that should terminate Camel {@link Exchange}  * should go to {@link #hasFailedWith(Exception)} and successful execution of  * the exchange should end with {@link #finish(Message)}. Any  * {@link ClientCallback}s that are added here should extend  * {@link ErrorHandlingClientCallback}, best way to do that is to use the  * {@link #on(Consumer)} helper method.  */
end_comment

begin_class
DECL|class|UndertowClientCallback
class|class
name|UndertowClientCallback
implements|implements
name|ClientCallback
argument_list|<
name|ClientConnection
argument_list|>
block|{
comment|/**      * {@link ClientCallback} that handles failures automatically by propagating      * the exception to Camel {@link Exchange} and notifies Camel that the      * exchange finished by calling {@link AsyncCallback#done(boolean)}.      */
DECL|class|ErrorHandlingClientCallback
specifier|final
class|class
name|ErrorHandlingClientCallback
parameter_list|<
name|T
parameter_list|>
implements|implements
name|ClientCallback
argument_list|<
name|T
argument_list|>
block|{
DECL|field|consumer
specifier|private
specifier|final
name|Consumer
argument_list|<
name|T
argument_list|>
name|consumer
decl_stmt|;
DECL|method|ErrorHandlingClientCallback (final Consumer<T> consumer)
specifier|private
name|ErrorHandlingClientCallback
parameter_list|(
specifier|final
name|Consumer
argument_list|<
name|T
argument_list|>
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|completed (final T result)
specifier|public
name|void
name|completed
parameter_list|(
specifier|final
name|T
name|result
parameter_list|)
block|{
name|consumer
operator|.
name|accept
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|failed (final IOException e)
specifier|public
name|void
name|failed
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
name|UndertowClientCallback
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|body
specifier|private
specifier|final
name|ByteBuffer
name|body
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
comment|/**      * A queue of resources that will be closed when the exchange ends, add more      * resources via {@link #deferClose(Closeable)}.      */
DECL|field|closables
specifier|private
specifier|final
name|BlockingDeque
argument_list|<
name|Closeable
argument_list|>
name|closables
init|=
operator|new
name|LinkedBlockingDeque
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|UndertowEndpoint
name|endpoint
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|request
specifier|private
specifier|final
name|ClientRequest
name|request
decl_stmt|;
DECL|field|throwExceptionOnFailure
specifier|private
specifier|final
name|Boolean
name|throwExceptionOnFailure
decl_stmt|;
DECL|method|UndertowClientCallback (final Exchange exchange, final AsyncCallback callback, final UndertowEndpoint endpoint, final ClientRequest request, final ByteBuffer body)
name|UndertowClientCallback
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
specifier|final
name|UndertowEndpoint
name|endpoint
parameter_list|,
specifier|final
name|ClientRequest
name|request
parameter_list|,
specifier|final
name|ByteBuffer
name|body
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
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|request
operator|=
name|request
expr_stmt|;
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
name|throwExceptionOnFailure
operator|=
name|endpoint
operator|.
name|getThrowExceptionOnFailure
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|completed (final ClientConnection connection)
specifier|public
name|void
name|completed
parameter_list|(
specifier|final
name|ClientConnection
name|connection
parameter_list|)
block|{
comment|// we have established connection, make sure we close it
name|deferClose
argument_list|(
name|connection
argument_list|)
expr_stmt|;
comment|// now we can send the request and perform the exchange: writing the
comment|// request and reading the response
name|connection
operator|.
name|sendRequest
argument_list|(
name|request
argument_list|,
name|on
argument_list|(
name|this
operator|::
name|performClientExchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|failed (final IOException e)
specifier|public
name|void
name|failed
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
DECL|method|asyncWriter (final ByteBuffer body)
name|ChannelListener
argument_list|<
name|StreamSinkChannel
argument_list|>
name|asyncWriter
parameter_list|(
specifier|final
name|ByteBuffer
name|body
parameter_list|)
block|{
return|return
name|channel
lambda|->
block|{
try|try
block|{
name|write
argument_list|(
name|channel
argument_list|,
name|body
argument_list|)
expr_stmt|;
if|if
condition|(
name|body
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|channel
operator|.
name|resumeWrites
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|flush
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
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
return|;
block|}
DECL|method|deferClose (final Closeable closeable)
name|void
name|deferClose
parameter_list|(
specifier|final
name|Closeable
name|closeable
parameter_list|)
block|{
try|try
block|{
name|closables
operator|.
name|putFirst
argument_list|(
name|closeable
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|InterruptedException
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
DECL|method|finish (final Message result)
name|void
name|finish
parameter_list|(
specifier|final
name|Message
name|result
parameter_list|)
block|{
for|for
control|(
specifier|final
name|Closeable
name|closeable
range|:
name|closables
control|)
block|{
name|IoUtils
operator|.
name|safeClose
argument_list|(
name|closeable
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|setOut
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setIn
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|hasFailedWith (final Exception e)
name|void
name|hasFailedWith
parameter_list|(
specifier|final
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Exchange has failed with"
argument_list|,
name|e
argument_list|)
expr_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|throwExceptionOnFailure
argument_list|)
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
name|finish
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|on (final Consumer<T> consumer)
parameter_list|<
name|T
parameter_list|>
name|ClientCallback
argument_list|<
name|T
argument_list|>
name|on
parameter_list|(
specifier|final
name|Consumer
argument_list|<
name|T
argument_list|>
name|consumer
parameter_list|)
block|{
return|return
operator|new
name|ErrorHandlingClientCallback
argument_list|<>
argument_list|(
name|consumer
argument_list|)
return|;
block|}
DECL|method|performClientExchange (final ClientExchange clientExchange)
name|void
name|performClientExchange
parameter_list|(
specifier|final
name|ClientExchange
name|clientExchange
parameter_list|)
block|{
comment|// add response listener to the exchange, we could receive the response
comment|// at any time (async)
name|setupResponseListner
argument_list|(
name|clientExchange
argument_list|)
expr_stmt|;
comment|// write the request
name|writeRequest
argument_list|(
name|clientExchange
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|setupResponseListner (final ClientExchange clientExchange)
name|void
name|setupResponseListner
parameter_list|(
specifier|final
name|ClientExchange
name|clientExchange
parameter_list|)
block|{
name|clientExchange
operator|.
name|setResponseListener
argument_list|(
name|on
argument_list|(
name|response
lambda|->
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"completed: {}"
argument_list|,
name|clientExchange
argument_list|)
expr_stmt|;
try|try
block|{
name|storeCookies
argument_list|(
name|clientExchange
argument_list|)
expr_stmt|;
specifier|final
name|UndertowHttpBinding
name|binding
init|=
name|endpoint
operator|.
name|getUndertowHttpBinding
argument_list|()
decl_stmt|;
specifier|final
name|Message
name|result
init|=
name|binding
operator|.
name|toCamelMessage
argument_list|(
name|clientExchange
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// we end Camel exchange here
name|finish
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|storeCookies (final ClientExchange clientExchange)
name|void
name|storeCookies
parameter_list|(
specifier|final
name|ClientExchange
name|clientExchange
parameter_list|)
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
if|if
condition|(
name|endpoint
operator|.
name|getCookieHandler
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// creating the url to use takes 2-steps
specifier|final
name|String
name|url
init|=
name|UndertowHelper
operator|.
name|createURL
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
specifier|final
name|URI
name|uri
init|=
name|UndertowHelper
operator|.
name|createURI
argument_list|(
name|exchange
argument_list|,
name|url
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
specifier|final
name|HeaderMap
name|headerMap
init|=
name|clientExchange
operator|.
name|getResponse
argument_list|()
operator|.
name|getResponseHeaders
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|m
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|HttpString
name|headerName
range|:
name|headerMap
operator|.
name|getHeaderNames
argument_list|()
control|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|headerValue
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|headerMap
operator|.
name|count
argument_list|(
name|headerName
argument_list|)
condition|;
name|i
operator|++
control|)
block|{
name|headerValue
operator|.
name|add
argument_list|(
name|headerMap
operator|.
name|get
argument_list|(
name|headerName
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|m
operator|.
name|put
argument_list|(
name|headerName
operator|.
name|toString
argument_list|()
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|getCookieHandler
argument_list|()
operator|.
name|storeCookies
argument_list|(
name|exchange
argument_list|,
name|uri
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|writeRequest (final ClientExchange clientExchange, final ByteBuffer body)
name|void
name|writeRequest
parameter_list|(
specifier|final
name|ClientExchange
name|clientExchange
parameter_list|,
specifier|final
name|ByteBuffer
name|body
parameter_list|)
block|{
specifier|final
name|StreamSinkChannel
name|requestChannel
init|=
name|clientExchange
operator|.
name|getRequestChannel
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// try writing, we could be on IO thread and ready to write to
comment|// the socket (or not)
name|write
argument_list|(
name|requestChannel
argument_list|,
name|body
argument_list|)
expr_stmt|;
if|if
condition|(
name|body
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
comment|// we did not write all of body (or at all) register a write
comment|// listener to write asynchronously
name|requestChannel
operator|.
name|getWriteSetter
argument_list|()
operator|.
name|set
argument_list|(
name|asyncWriter
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
name|requestChannel
operator|.
name|resumeWrites
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// we are done, we need to flush the request
name|flush
argument_list|(
name|requestChannel
argument_list|)
expr_stmt|;
block|}
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
DECL|method|flush (final StreamSinkChannel channel)
specifier|static
name|void
name|flush
parameter_list|(
specifier|final
name|StreamSinkChannel
name|channel
parameter_list|)
throws|throws
name|IOException
block|{
comment|// the canonical way of flushing Xnio channels
name|channel
operator|.
name|shutdownWrites
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|channel
operator|.
name|flush
argument_list|()
condition|)
block|{
specifier|final
name|ChannelListener
argument_list|<
name|StreamSinkChannel
argument_list|>
name|safeClose
init|=
name|IoUtils
operator|::
name|safeClose
decl_stmt|;
specifier|final
name|ChannelExceptionHandler
argument_list|<
name|Channel
argument_list|>
name|closingChannelExceptionHandler
init|=
name|ChannelListeners
operator|.
name|closingChannelExceptionHandler
argument_list|()
decl_stmt|;
specifier|final
name|ChannelListener
argument_list|<
name|StreamSinkChannel
argument_list|>
name|flushingChannelListener
init|=
name|ChannelListeners
operator|.
name|flushingChannelListener
argument_list|(
name|safeClose
argument_list|,
name|closingChannelExceptionHandler
argument_list|)
decl_stmt|;
name|channel
operator|.
name|getWriteSetter
argument_list|()
operator|.
name|set
argument_list|(
name|flushingChannelListener
argument_list|)
expr_stmt|;
name|channel
operator|.
name|resumeWrites
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|write (final StreamSinkChannel channel, final ByteBuffer body)
specifier|static
name|void
name|write
parameter_list|(
specifier|final
name|StreamSinkChannel
name|channel
parameter_list|,
specifier|final
name|ByteBuffer
name|body
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|written
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|body
operator|.
name|hasRemaining
argument_list|()
operator|&&
name|written
operator|>
literal|0
condition|)
block|{
name|written
operator|=
name|channel
operator|.
name|write
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

