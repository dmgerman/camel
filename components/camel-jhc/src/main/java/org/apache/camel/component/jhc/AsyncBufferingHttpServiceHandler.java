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
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|ConnectionReuseStrategy
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
name|HttpResponseFactory
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
name|HttpStatus
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
name|HttpVersion
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
name|ProtocolVersion
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
name|DefaultHttpResponseFactory
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
name|NHttpServerConnection
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
name|util
operator|.
name|ByteBufferAllocator
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
name|params
operator|.
name|HttpParamsLinker
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
name|ExecutionContext
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
name|HttpProcessor
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
name|HttpRequestHandler
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
name|ResponseConnControl
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
name|ResponseContent
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
name|ResponseDate
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
name|ResponseServer
import|;
end_import

begin_comment
comment|/**  * Created by IntelliJ IDEA.  * User: gnodet  * Date: Sep 11, 2007  * Time: 6:57:34 PM  * To change this template use File | Settings | File Templates.  */
end_comment

begin_class
DECL|class|AsyncBufferingHttpServiceHandler
specifier|public
class|class
name|AsyncBufferingHttpServiceHandler
extends|extends
name|BufferingHttpServiceHandler
block|{
DECL|method|AsyncBufferingHttpServiceHandler (final HttpParams params)
specifier|public
name|AsyncBufferingHttpServiceHandler
parameter_list|(
specifier|final
name|HttpParams
name|params
parameter_list|)
block|{
name|super
argument_list|(
name|createDefaultProcessor
argument_list|()
argument_list|,
operator|new
name|DefaultHttpResponseFactory
argument_list|()
argument_list|,
operator|new
name|DefaultConnectionReuseStrategy
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
DECL|method|AsyncBufferingHttpServiceHandler (final HttpProcessor httpProcessor, final HttpResponseFactory responseFactory, final ConnectionReuseStrategy connStrategy, final HttpParams params)
specifier|public
name|AsyncBufferingHttpServiceHandler
parameter_list|(
specifier|final
name|HttpProcessor
name|httpProcessor
parameter_list|,
specifier|final
name|HttpResponseFactory
name|responseFactory
parameter_list|,
specifier|final
name|ConnectionReuseStrategy
name|connStrategy
parameter_list|,
specifier|final
name|HttpParams
name|params
parameter_list|)
block|{
name|super
argument_list|(
name|httpProcessor
argument_list|,
name|responseFactory
argument_list|,
name|connStrategy
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
DECL|method|AsyncBufferingHttpServiceHandler (final HttpProcessor httpProcessor, final HttpResponseFactory responseFactory, final ConnectionReuseStrategy connStrategy, final ByteBufferAllocator allocator, final HttpParams params)
specifier|public
name|AsyncBufferingHttpServiceHandler
parameter_list|(
specifier|final
name|HttpProcessor
name|httpProcessor
parameter_list|,
specifier|final
name|HttpResponseFactory
name|responseFactory
parameter_list|,
specifier|final
name|ConnectionReuseStrategy
name|connStrategy
parameter_list|,
specifier|final
name|ByteBufferAllocator
name|allocator
parameter_list|,
specifier|final
name|HttpParams
name|params
parameter_list|)
block|{
name|super
argument_list|(
name|httpProcessor
argument_list|,
name|responseFactory
argument_list|,
name|connStrategy
argument_list|,
name|allocator
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
DECL|method|createDefaultProcessor ()
specifier|protected
specifier|static
name|HttpProcessor
name|createDefaultProcessor
parameter_list|()
block|{
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
name|ResponseDate
argument_list|()
argument_list|)
expr_stmt|;
name|httpproc
operator|.
name|addInterceptor
argument_list|(
operator|new
name|ResponseServer
argument_list|()
argument_list|)
expr_stmt|;
name|httpproc
operator|.
name|addInterceptor
argument_list|(
operator|new
name|ResponseContent
argument_list|()
argument_list|)
expr_stmt|;
name|httpproc
operator|.
name|addInterceptor
argument_list|(
operator|new
name|ResponseConnControl
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|httpproc
return|;
block|}
DECL|method|processRequest ( final NHttpServerConnection conn, final HttpRequest request)
specifier|protected
name|void
name|processRequest
parameter_list|(
specifier|final
name|NHttpServerConnection
name|conn
parameter_list|,
specifier|final
name|HttpRequest
name|request
parameter_list|)
throws|throws
name|IOException
throws|,
name|HttpException
block|{
name|HttpContext
name|context
init|=
name|conn
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|ProtocolVersion
name|ver
init|=
name|request
operator|.
name|getRequestLine
argument_list|()
operator|.
name|getProtocolVersion
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ver
operator|.
name|lessEquals
argument_list|(
name|HttpVersion
operator|.
name|HTTP_1_1
argument_list|)
condition|)
block|{
comment|// Downgrade protocol version if greater than HTTP/1.1
name|ver
operator|=
name|HttpVersion
operator|.
name|HTTP_1_1
expr_stmt|;
block|}
name|context
operator|.
name|setAttribute
argument_list|(
name|ExecutionContext
operator|.
name|HTTP_REQUEST
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
name|ExecutionContext
operator|.
name|HTTP_CONNECTION
argument_list|,
name|conn
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|httpProcessor
operator|.
name|process
argument_list|(
name|request
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|HttpRequestHandler
name|handler
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|handlerResolver
operator|!=
literal|null
condition|)
block|{
name|String
name|requestURI
init|=
name|request
operator|.
name|getRequestLine
argument_list|()
operator|.
name|getUri
argument_list|()
decl_stmt|;
name|handler
operator|=
name|handlerResolver
operator|.
name|lookup
argument_list|(
name|requestURI
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|handler
operator|instanceof
name|AsyncHttpRequestHandler
condition|)
block|{
operator|(
operator|(
name|AsyncHttpRequestHandler
operator|)
name|handler
operator|)
operator|.
name|handle
argument_list|(
name|request
argument_list|,
name|context
argument_list|,
operator|new
name|AsyncResponseHandler
argument_list|()
block|{
specifier|public
name|void
name|sendResponse
parameter_list|(
name|HttpResponse
name|response
parameter_list|)
throws|throws
name|IOException
throws|,
name|HttpException
block|{
try|try
block|{
name|AsyncBufferingHttpServiceHandler
operator|.
name|this
operator|.
name|sendResponse
argument_list|(
name|conn
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|HttpException
name|ex
parameter_list|)
block|{
name|response
operator|=
name|AsyncBufferingHttpServiceHandler
operator|.
name|this
operator|.
name|responseFactory
operator|.
name|newHttpResponse
argument_list|(
name|HttpVersion
operator|.
name|HTTP_1_0
argument_list|,
name|HttpStatus
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|,
name|conn
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|HttpParamsLinker
operator|.
name|link
argument_list|(
name|response
argument_list|,
name|AsyncBufferingHttpServiceHandler
operator|.
name|this
operator|.
name|params
argument_list|)
expr_stmt|;
name|AsyncBufferingHttpServiceHandler
operator|.
name|this
operator|.
name|handleException
argument_list|(
name|ex
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|AsyncBufferingHttpServiceHandler
operator|.
name|this
operator|.
name|sendResponse
argument_list|(
name|conn
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// just hanlder the request with sync request handler
name|HttpResponse
name|response
init|=
name|this
operator|.
name|responseFactory
operator|.
name|newHttpResponse
argument_list|(
name|ver
argument_list|,
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|conn
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|HttpParamsLinker
operator|.
name|link
argument_list|(
name|response
argument_list|,
name|this
operator|.
name|params
argument_list|)
expr_stmt|;
name|context
operator|.
name|setAttribute
argument_list|(
name|ExecutionContext
operator|.
name|HTTP_RESPONSE
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|handler
operator|.
name|handle
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|sendResponse
argument_list|(
name|conn
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// add the default handler here
name|HttpResponse
name|response
init|=
name|this
operator|.
name|responseFactory
operator|.
name|newHttpResponse
argument_list|(
name|ver
argument_list|,
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|conn
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|response
operator|.
name|setStatusCode
argument_list|(
name|HttpStatus
operator|.
name|SC_NOT_IMPLEMENTED
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|HttpException
name|ex
parameter_list|)
block|{
name|HttpResponse
name|response
init|=
name|this
operator|.
name|responseFactory
operator|.
name|newHttpResponse
argument_list|(
name|HttpVersion
operator|.
name|HTTP_1_0
argument_list|,
name|HttpStatus
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|HttpParamsLinker
operator|.
name|link
argument_list|(
name|response
argument_list|,
name|this
operator|.
name|params
argument_list|)
expr_stmt|;
name|handleException
argument_list|(
name|ex
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|sendResponse
argument_list|(
name|conn
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

