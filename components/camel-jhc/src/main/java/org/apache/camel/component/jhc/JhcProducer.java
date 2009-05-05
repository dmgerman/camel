begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jhc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jhc
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
name|InterruptedIOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
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
name|concurrent
operator|.
name|ThreadFactory
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
name|impl
operator|.
name|DefaultProducer
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
name|HeaderFilterStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|Header
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|ByteArrayEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|DefaultConnectionReuseStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|nio
operator|.
name|DefaultClientIOEventDispatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|nio
operator|.
name|reactor
operator|.
name|DefaultConnectingIOReactor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|message
operator|.
name|BasicHttpEntityEnclosingRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|message
operator|.
name|BasicHttpRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|NHttpConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|protocol
operator|.
name|BufferingHttpClientHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|protocol
operator|.
name|EventListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|protocol
operator|.
name|HttpRequestExecutionHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|reactor
operator|.
name|ConnectingIOReactor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|reactor
operator|.
name|IOEventDispatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|reactor
operator|.
name|SessionRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|reactor
operator|.
name|SessionRequestCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|HttpParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|BasicHttpProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|RequestConnControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|RequestContent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|RequestExpectContinue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|RequestTargetHost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|RequestUserAgent
import|;
end_import

begin_class
DECL|class|JhcProducer
specifier|public
class|class
name|JhcProducer
extends|extends
name|DefaultProducer
implements|implements
name|Processor
block|{
DECL|field|HTTP_RESPONSE_CODE
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_RESPONSE_CODE
init|=
literal|"http.responseCode"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|JhcProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|nbThreads
specifier|private
name|int
name|nbThreads
init|=
literal|2
decl_stmt|;
DECL|field|ioReactor
specifier|private
name|ConnectingIOReactor
name|ioReactor
decl_stmt|;
DECL|field|threadFactory
specifier|private
name|ThreadFactory
name|threadFactory
decl_stmt|;
DECL|field|runner
specifier|private
name|Thread
name|runner
decl_stmt|;
DECL|method|JhcProducer (JhcEndpoint endpoint)
specifier|public
name|JhcProducer
parameter_list|(
name|JhcEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|JhcEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|JhcEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|HttpParams
name|params
init|=
name|getEndpoint
argument_list|()
operator|.
name|getParams
argument_list|()
decl_stmt|;
name|ioReactor
operator|=
operator|new
name|DefaultConnectingIOReactor
argument_list|(
name|nbThreads
argument_list|,
name|threadFactory
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|BasicHttpProcessor
name|httpproc
init|=
operator|new
name|BasicHttpProcessor
argument_list|()
decl_stmt|;
name|httpproc
operator|.
name|addInterceptor
argument_list|(
operator|new
name|RequestContent
argument_list|()
argument_list|)
expr_stmt|;
name|httpproc
operator|.
name|addInterceptor
argument_list|(
operator|new
name|RequestTargetHost
argument_list|()
argument_list|)
expr_stmt|;
name|httpproc
operator|.
name|addInterceptor
argument_list|(
operator|new
name|RequestConnControl
argument_list|()
argument_list|)
expr_stmt|;
name|httpproc
operator|.
name|addInterceptor
argument_list|(
operator|new
name|RequestUserAgent
argument_list|()
argument_list|)
expr_stmt|;
name|httpproc
operator|.
name|addInterceptor
argument_list|(
operator|new
name|RequestExpectContinue
argument_list|()
argument_list|)
expr_stmt|;
name|BufferingHttpClientHandler
name|handler
init|=
operator|new
name|BufferingHttpClientHandler
argument_list|(
name|httpproc
argument_list|,
operator|new
name|MyHttpRequestExecutionHandler
argument_list|()
argument_list|,
operator|new
name|DefaultConnectionReuseStrategy
argument_list|()
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|handler
operator|.
name|setEventListener
argument_list|(
operator|new
name|EventLogger
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|IOEventDispatch
name|ioEventDispatch
init|=
operator|new
name|DefaultClientIOEventDispatch
argument_list|(
name|handler
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|runner
operator|=
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|ioReactor
operator|.
name|execute
argument_list|(
name|ioEventDispatch
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedIOException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Interrupted"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"I/O error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Shutdown"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|runner
operator|.
name|start
argument_list|()
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
name|ioReactor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|runner
operator|.
name|join
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"process: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|SocketAddress
name|addr
init|=
operator|new
name|InetSocketAddress
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getHost
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|ioReactor
operator|.
name|connect
argument_list|(
name|addr
argument_list|,
literal|null
argument_list|,
name|exchange
argument_list|,
operator|new
name|MySessionRequestCallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createRequest (Exchange exchange)
specifier|protected
name|HttpRequest
name|createRequest
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// force creation of uri
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
expr_stmt|;
name|HttpEntity
name|entity
init|=
name|createEntity
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|HttpRequest
name|req
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
name|req
operator|=
operator|new
name|BasicHttpRequest
argument_list|(
literal|"GET"
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|req
operator|=
operator|new
name|BasicHttpEntityEnclosingRequest
argument_list|(
literal|"POST"
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BasicHttpEntityEnclosingRequest
operator|)
name|req
operator|)
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
comment|// propagate headers as HTTP headers
name|HeaderFilterStrategy
name|strategy
init|=
operator|(
operator|(
name|JhcEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getHeaderFilterStrategy
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|headerName
range|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|headerValue
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|headerName
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|strategy
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|req
operator|.
name|addHeader
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|req
return|;
block|}
DECL|method|createEntity (Exchange exchange)
specifier|protected
name|HttpEntity
name|createEntity
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|HttpEntity
name|entity
init|=
name|in
operator|.
name|getBody
argument_list|(
name|HttpEntity
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
name|byte
index|[]
name|data
init|=
name|in
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|entity
operator|=
operator|new
name|ByteArrayEntity
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|String
name|contentType
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|ByteArrayEntity
operator|)
name|entity
operator|)
operator|.
name|setContentType
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
name|String
name|contentEncoding
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"Content-Encoding"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentEncoding
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|ByteArrayEntity
operator|)
name|entity
operator|)
operator|.
name|setContentEncoding
argument_list|(
name|contentEncoding
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|entity
return|;
block|}
DECL|class|MySessionRequestCallback
specifier|static
class|class
name|MySessionRequestCallback
implements|implements
name|SessionRequestCallback
block|{
DECL|method|completed (SessionRequest sessionRequest)
specifier|public
name|void
name|completed
parameter_list|(
name|SessionRequest
name|sessionRequest
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Completed"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|failed (SessionRequest sessionRequest)
specifier|public
name|void
name|failed
parameter_list|(
name|SessionRequest
name|sessionRequest
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Failed"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|timeout (SessionRequest sessionRequest)
specifier|public
name|void
name|timeout
parameter_list|(
name|SessionRequest
name|sessionRequest
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Timeout"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|cancelled (SessionRequest sessionRequest)
specifier|public
name|void
name|cancelled
parameter_list|(
name|SessionRequest
name|sessionRequest
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cancelled"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|MyHttpRequestExecutionHandler
class|class
name|MyHttpRequestExecutionHandler
implements|implements
name|HttpRequestExecutionHandler
block|{
DECL|field|REQUEST_SENT
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_SENT
init|=
literal|"request-sent"
decl_stmt|;
DECL|field|RESPONSE_RECEIVED
specifier|private
specifier|static
specifier|final
name|String
name|RESPONSE_RECEIVED
init|=
literal|"response-received"
decl_stmt|;
DECL|method|initalizeContext (HttpContext httpContext, Object o)
specifier|public
name|void
name|initalizeContext
parameter_list|(
name|HttpContext
name|httpContext
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Initialize context"
argument_list|)
expr_stmt|;
block|}
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|Exchange
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|(
name|Exchange
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
DECL|method|submitRequest (HttpContext httpContext)
specifier|public
name|HttpRequest
name|submitRequest
parameter_list|(
name|HttpContext
name|httpContext
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Submit request: "
operator|+
name|httpContext
argument_list|)
expr_stmt|;
block|}
name|Object
name|flag
init|=
name|httpContext
operator|.
name|getAttribute
argument_list|(
name|REQUEST_SENT
argument_list|)
decl_stmt|;
if|if
condition|(
name|flag
operator|==
literal|null
condition|)
block|{
comment|// Stick some object into the context
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|REQUEST_SENT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
operator|(
name|Exchange
operator|)
name|httpContext
operator|.
name|getAttribute
argument_list|(
name|Exchange
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|createRequest
argument_list|(
name|e
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|handleResponse (HttpResponse httpResponse, HttpContext httpContext)
specifier|public
name|void
name|handleResponse
parameter_list|(
name|HttpResponse
name|httpResponse
parameter_list|,
name|HttpContext
name|httpContext
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Handle response"
argument_list|)
expr_stmt|;
block|}
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|RESPONSE_RECEIVED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
operator|(
name|Exchange
operator|)
name|httpContext
operator|.
name|getAttribute
argument_list|(
name|Exchange
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|httpResponse
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|HeaderFilterStrategy
name|strategy
init|=
name|getEndpoint
argument_list|()
operator|.
name|getHeaderFilterStrategy
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|httpResponse
operator|.
name|headerIterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Header
name|h
init|=
operator|(
name|Header
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|strategy
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|h
operator|.
name|getName
argument_list|()
argument_list|,
name|h
operator|.
name|getValue
argument_list|()
argument_list|,
name|e
argument_list|)
condition|)
block|{
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|h
operator|.
name|getName
argument_list|()
argument_list|,
name|h
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HTTP_RESPONSE_CODE
argument_list|,
name|httpResponse
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|finalizeContext (HttpContext httpContext)
specifier|public
name|void
name|finalizeContext
parameter_list|(
name|HttpContext
name|httpContext
parameter_list|)
block|{         }
block|}
DECL|class|EventLogger
specifier|static
class|class
name|EventLogger
implements|implements
name|EventListener
block|{
DECL|method|connectionOpen (final NHttpConnection conn)
specifier|public
name|void
name|connectionOpen
parameter_list|(
specifier|final
name|NHttpConnection
name|conn
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Connection open: "
operator|+
name|conn
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|connectionTimeout (final NHttpConnection conn)
specifier|public
name|void
name|connectionTimeout
parameter_list|(
specifier|final
name|NHttpConnection
name|conn
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Connection timed out: "
operator|+
name|conn
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|connectionClosed (final NHttpConnection conn)
specifier|public
name|void
name|connectionClosed
parameter_list|(
specifier|final
name|NHttpConnection
name|conn
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Connection closed: "
operator|+
name|conn
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|fatalIOException (final IOException ex, final NHttpConnection conn)
specifier|public
name|void
name|fatalIOException
parameter_list|(
specifier|final
name|IOException
name|ex
parameter_list|,
specifier|final
name|NHttpConnection
name|conn
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"I/O error: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|fatalProtocolException (final HttpException ex, final NHttpConnection conn)
specifier|public
name|void
name|fatalProtocolException
parameter_list|(
specifier|final
name|HttpException
name|ex
parameter_list|,
specifier|final
name|NHttpConnection
name|conn
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"HTTP error: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

