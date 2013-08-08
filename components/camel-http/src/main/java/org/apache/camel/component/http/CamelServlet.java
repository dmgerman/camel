begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
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
name|component
operator|.
name|http
operator|.
name|helper
operator|.
name|HttpHelper
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|CamelServlet
specifier|public
class|class
name|CamelServlet
extends|extends
name|HttpServlet
block|{
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
specifier|transient
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
DECL|field|consumers
specifier|private
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
argument_list|<
name|String
argument_list|,
name|HttpConsumer
argument_list|>
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
block|}
annotation|@
name|Override
DECL|method|service (HttpServletRequest request, HttpServletResponse response)
specifier|protected
name|void
name|service
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
name|log
operator|.
name|debug
argument_list|(
literal|"No consumer to service request {}"
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
name|equals
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
operator|new
name|DefaultExchange
argument_list|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
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
DECL|method|resolve (HttpServletRequest request)
specifier|protected
name|HttpConsumer
name|resolve
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|String
name|path
init|=
name|request
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|HttpConsumer
name|answer
init|=
name|consumers
operator|.
name|get
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|String
name|key
range|:
name|consumers
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|consumers
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|getEndpoint
argument_list|()
operator|.
name|isMatchOnUriPrefix
argument_list|()
operator|&&
name|path
operator|.
name|startsWith
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|answer
operator|=
name|consumers
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|answer
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
name|getPath
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
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Override the Thread Context ClassLoader if need be.      * @param exchange      * @return old classloader if overridden; otherwise returns null      */
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

