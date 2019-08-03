begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.http.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|http
operator|.
name|common
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
name|util
operator|.
name|Arrays
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
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|AsyncContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServlet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|RuntimeCamelException
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

begin_comment
comment|/**  * A servlet to use as a Camel route as entry.  */
end_comment

begin_class
DECL|class|CamelServlet
specifier|public
class|class
name|CamelServlet
extends|extends
name|HttpServlet
block|{
DECL|field|ASYNC_PARAM
specifier|public
specifier|static
specifier|final
name|String
name|ASYNC_PARAM
init|=
literal|"async"
decl_stmt|;
DECL|field|METHODS
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|METHODS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"GET"
argument_list|,
literal|"HEAD"
argument_list|,
literal|"POST"
argument_list|,
literal|"PUT"
argument_list|,
literal|"DELETE"
argument_list|,
literal|"TRACE"
argument_list|,
literal|"OPTIONS"
argument_list|,
literal|"CONNECT"
argument_list|,
literal|"PATCH"
argument_list|)
decl_stmt|;
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|7061982839117697829L
decl_stmt|;
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
comment|/**      *  We have to define this explicitly so the name can be set as we can not always be      *  sure that it is already set via the init method      */
DECL|field|servletName
specifier|private
name|String
name|servletName
decl_stmt|;
DECL|field|async
specifier|private
name|boolean
name|async
decl_stmt|;
DECL|field|servletResolveConsumerStrategy
specifier|private
name|ServletResolveConsumerStrategy
name|servletResolveConsumerStrategy
init|=
operator|new
name|HttpServletResolveConsumerStrategy
argument_list|()
decl_stmt|;
DECL|field|consumers
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|HttpConsumer
argument_list|>
name|consumers
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|init (ServletConfig config)
specifier|public
name|void
name|init
parameter_list|(
name|ServletConfig
name|config
parameter_list|)
throws|throws
name|ServletException
block|{
name|super
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|this
operator|.
name|servletName
operator|=
name|config
operator|.
name|getServletName
argument_list|()
expr_stmt|;
specifier|final
name|String
name|asyncParam
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
name|ASYNC_PARAM
argument_list|)
decl_stmt|;
name|this
operator|.
name|async
operator|=
name|asyncParam
operator|==
literal|null
condition|?
literal|false
else|:
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|asyncParam
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"servlet '{}' initialized with: async={}"
argument_list|,
name|servletName
argument_list|,
name|async
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|service (HttpServletRequest req, HttpServletResponse resp)
specifier|protected
name|void
name|service
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
if|if
condition|(
name|isAsync
argument_list|()
condition|)
block|{
specifier|final
name|AsyncContext
name|context
init|=
name|req
operator|.
name|startAsync
argument_list|()
decl_stmt|;
comment|//run async
name|context
operator|.
name|start
argument_list|(
parameter_list|()
lambda|->
name|doServiceAsync
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doService
argument_list|(
name|req
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This is used to handle request asynchronously      * @param context the {@link AsyncContext}      */
DECL|method|doServiceAsync (AsyncContext context)
specifier|protected
name|void
name|doServiceAsync
parameter_list|(
name|AsyncContext
name|context
parameter_list|)
block|{
specifier|final
name|HttpServletRequest
name|request
init|=
operator|(
name|HttpServletRequest
operator|)
name|context
operator|.
name|getRequest
argument_list|()
decl_stmt|;
specifier|final
name|HttpServletResponse
name|response
init|=
operator|(
name|HttpServletResponse
operator|)
name|context
operator|.
name|getResponse
argument_list|()
decl_stmt|;
try|try
block|{
name|doService
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//An error shouldn't occur as we should handle most of error in doService
name|log
operator|.
name|error
argument_list|(
literal|"Error processing request"
argument_list|,
name|e
argument_list|)
expr_stmt|;
try|try
block|{
name|response
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Cannot send reply to client!"
argument_list|,
name|e1
argument_list|)
expr_stmt|;
block|}
comment|//Need to wrap it in RuntimeException as it occurs in a Runnable
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|context
operator|.
name|complete
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * This is the logical implementation to handle request with {@link CamelServlet}      * This is where most exceptions should be handled      *      * @param request the {@link HttpServletRequest}      * @param response the {@link HttpServletResponse}      * @throws ServletException      * @throws IOException      */
DECL|method|doService (HttpServletRequest request, HttpServletResponse response)
specifier|protected
name|void
name|doService
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Service: {}"
argument_list|,
name|request
argument_list|)
expr_stmt|;
comment|// Is there a consumer registered for the request.
name|HttpConsumer
name|consumer
init|=
name|resolve
argument_list|(
name|request
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumer
operator|==
literal|null
condition|)
block|{
comment|// okay we cannot process this requires so return either 404 or 405.
comment|// to know if its 405 then we need to check if any other HTTP method would have a consumer for the "same" request
name|boolean
name|hasAnyMethod
init|=
name|METHODS
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|m
lambda|->
name|getServletResolveConsumerStrategy
argument_list|()
operator|.
name|isHttpMethodAllowed
argument_list|(
name|request
argument_list|,
name|m
argument_list|,
name|getConsumers
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|hasAnyMethod
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"No consumer to service request {} as method {} is not allowed"
argument_list|,
name|request
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_METHOD_NOT_ALLOWED
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"No consumer to service request {} as resource is not found"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|response
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|// are we suspended?
if|if
condition|(
name|consumer
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Consumer suspended, cannot service request {}"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|response
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_SERVICE_UNAVAILABLE
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// if its an OPTIONS request then return which method is allowed
if|if
condition|(
literal|"OPTIONS"
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
operator|&&
operator|!
name|consumer
operator|.
name|isOptionsEnabled
argument_list|()
condition|)
block|{
name|String
name|allowedMethods
init|=
name|METHODS
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|m
lambda|->
name|getServletResolveConsumerStrategy
argument_list|()
operator|.
name|isHttpMethodAllowed
argument_list|(
name|request
argument_list|,
name|m
argument_list|,
name|getConsumers
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|","
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|allowedMethods
operator|==
literal|null
operator|&&
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|allowedMethods
operator|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|allowedMethods
operator|==
literal|null
condition|)
block|{
comment|// allow them all
name|allowedMethods
operator|=
literal|"GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,CONNECT,PATCH"
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|allowedMethods
operator|.
name|contains
argument_list|(
literal|"OPTIONS"
argument_list|)
condition|)
block|{
name|allowedMethods
operator|=
name|allowedMethods
operator|+
literal|",OPTIONS"
expr_stmt|;
block|}
name|response
operator|.
name|addHeader
argument_list|(
literal|"Allow"
argument_list|,
name|allowedMethods
argument_list|)
expr_stmt|;
name|response
operator|.
name|setStatus
argument_list|(
name|HttpServletResponse
operator|.
name|SC_OK
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
operator|.
name|contains
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
name|response
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_METHOD_NOT_ALLOWED
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
literal|"TRACE"
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
operator|&&
operator|!
name|consumer
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|response
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_METHOD_NOT_ALLOWED
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// create exchange and set data on it
name|Exchange
name|exchange
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SKIP_GZIP_ENCODING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SKIP_WWW_FORM_URLENCODED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|isDisableStreamCache
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|DISABLE_HTTP_STREAM_CACHE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
comment|// we override the classloader before building the HttpMessage just in case the binding
comment|// does some class resolution
name|ClassLoader
name|oldTccl
init|=
name|overrideTccl
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|HttpHelper
operator|.
name|setCharsetFromContentType
argument_list|(
name|request
operator|.
name|getContentType
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|HttpMessage
argument_list|(
name|exchange
argument_list|,
name|consumer
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
argument_list|)
expr_stmt|;
comment|// set context path as header
name|String
name|contextPath
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelServletContextPath"
argument_list|,
name|contextPath
argument_list|)
expr_stmt|;
name|String
name|httpPath
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|)
decl_stmt|;
comment|// here we just remove the CamelServletContextPath part from the HTTP_PATH
if|if
condition|(
name|contextPath
operator|!=
literal|null
operator|&&
name|httpPath
operator|.
name|startsWith
argument_list|(
name|contextPath
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
name|httpPath
operator|.
name|substring
argument_list|(
name|contextPath
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// we want to handle the UoW
try|try
block|{
name|consumer
operator|.
name|createUoW
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error processing request"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ServletException
argument_list|(
name|e
argument_list|)
throw|;
block|}
try|try
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing request for exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// process the exchange
name|consumer
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
block|}
try|try
block|{
comment|// now lets output to the response
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Writing response for exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Integer
name|bs
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getResponseBufferSize
argument_list|()
decl_stmt|;
if|if
condition|(
name|bs
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Using response buffer size: {}"
argument_list|,
name|bs
argument_list|)
expr_stmt|;
name|response
operator|.
name|setBufferSize
argument_list|(
name|bs
argument_list|)
expr_stmt|;
block|}
name|consumer
operator|.
name|getBinding
argument_list|()
operator|.
name|writeResponse
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error processing request"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error processing request"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ServletException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|consumer
operator|.
name|doneUoW
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|restoreTccl
argument_list|(
name|exchange
argument_list|,
name|oldTccl
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @deprecated use {@link ServletResolveConsumerStrategy#resolve(javax.servlet.http.HttpServletRequest, java.util.Map)}      */
annotation|@
name|Deprecated
DECL|method|resolve (HttpServletRequest request)
specifier|protected
name|HttpConsumer
name|resolve
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
return|return
name|getServletResolveConsumerStrategy
argument_list|()
operator|.
name|resolve
argument_list|(
name|request
argument_list|,
name|getConsumers
argument_list|()
argument_list|)
return|;
block|}
DECL|method|connect (HttpConsumer consumer)
specifier|public
name|void
name|connect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Connecting consumer: {}"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|put
argument_list|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|disconnect (HttpConsumer consumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Disconnecting consumer: {}"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|remove
argument_list|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getServletName ()
specifier|public
name|String
name|getServletName
parameter_list|()
block|{
return|return
name|servletName
return|;
block|}
DECL|method|setServletName (String servletName)
specifier|public
name|void
name|setServletName
parameter_list|(
name|String
name|servletName
parameter_list|)
block|{
name|this
operator|.
name|servletName
operator|=
name|servletName
expr_stmt|;
block|}
DECL|method|getServletResolveConsumerStrategy ()
specifier|public
name|ServletResolveConsumerStrategy
name|getServletResolveConsumerStrategy
parameter_list|()
block|{
return|return
name|servletResolveConsumerStrategy
return|;
block|}
DECL|method|setServletResolveConsumerStrategy (ServletResolveConsumerStrategy servletResolveConsumerStrategy)
specifier|public
name|void
name|setServletResolveConsumerStrategy
parameter_list|(
name|ServletResolveConsumerStrategy
name|servletResolveConsumerStrategy
parameter_list|)
block|{
name|this
operator|.
name|servletResolveConsumerStrategy
operator|=
name|servletResolveConsumerStrategy
expr_stmt|;
block|}
DECL|method|isAsync ()
specifier|public
name|boolean
name|isAsync
parameter_list|()
block|{
return|return
name|async
return|;
block|}
DECL|method|setAsync (boolean async)
specifier|public
name|void
name|setAsync
parameter_list|(
name|boolean
name|async
parameter_list|)
block|{
name|this
operator|.
name|async
operator|=
name|async
expr_stmt|;
block|}
DECL|method|getConsumers ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|HttpConsumer
argument_list|>
name|getConsumers
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|consumers
argument_list|)
return|;
block|}
comment|/**      * Override the Thread Context ClassLoader if need be.      * @return old classloader if overridden; otherwise returns null      */
DECL|method|overrideTccl (final Exchange exchange)
specifier|protected
name|ClassLoader
name|overrideTccl
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|ClassLoader
name|oldClassLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|ClassLoader
name|appCtxCl
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldClassLoader
operator|==
literal|null
operator|||
name|appCtxCl
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|oldClassLoader
operator|.
name|equals
argument_list|(
name|appCtxCl
argument_list|)
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|appCtxCl
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Overrode TCCL for exchangeId {} to {} on thread {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|exchange
operator|.
name|getExchangeId
argument_list|()
block|,
name|appCtxCl
block|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|oldClassLoader
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Restore the Thread Context ClassLoader if the old TCCL is not null.      */
DECL|method|restoreTccl (final Exchange exchange, ClassLoader oldTccl)
specifier|protected
name|void
name|restoreTccl
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
name|ClassLoader
name|oldTccl
parameter_list|)
block|{
if|if
condition|(
name|oldTccl
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|oldTccl
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Restored TCCL for exchangeId {} to {} on thread {}"
argument_list|,
operator|new
name|String
index|[]
block|{
name|exchange
operator|.
name|getExchangeId
argument_list|()
block|,
name|oldTccl
operator|.
name|toString
argument_list|()
block|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

