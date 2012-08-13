begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|CamelServlet
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
name|HttpConsumer
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
name|HttpMessage
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
name|eclipse
operator|.
name|jetty
operator|.
name|continuation
operator|.
name|Continuation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|continuation
operator|.
name|ContinuationSupport
import|;
end_import

begin_comment
comment|/**  * Servlet which leverage<a href="http://wiki.eclipse.org/Jetty/Feature/Continuations">Jetty Continuations</a>.  *  * @version   */
end_comment

begin_class
DECL|class|CamelContinuationServlet
specifier|public
class|class
name|CamelContinuationServlet
extends|extends
name|CamelServlet
block|{
DECL|field|EXCHANGE_ATTRIBUTE_NAME
specifier|static
specifier|final
name|String
name|EXCHANGE_ATTRIBUTE_NAME
init|=
literal|"CamelExchange"
decl_stmt|;
DECL|field|EXCHANGE_ATTRIBUTE_ID
specifier|static
specifier|final
name|String
name|EXCHANGE_ATTRIBUTE_ID
init|=
literal|"CamelExchangeId"
decl_stmt|;
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
comment|// jetty will by default use 30000 millis as default timeout
DECL|field|continuationTimeout
specifier|private
name|Long
name|continuationTimeout
decl_stmt|;
comment|// we must remember expired exchanges as Jetty will initiate a new continuation when we send
comment|// back the error when timeout occurred, and thus in the async callback we cannot check the
comment|// continuation if it was previously expired. So that's why we have our own map for that
DECL|field|expiredExchanges
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|expiredExchanges
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|service (final HttpServletRequest request, final HttpServletResponse response)
specifier|protected
name|void
name|service
parameter_list|(
specifier|final
name|HttpServletRequest
name|request
parameter_list|,
specifier|final
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
comment|// is there a consumer registered for the request.
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
specifier|final
name|Exchange
name|result
init|=
operator|(
name|Exchange
operator|)
name|request
operator|.
name|getAttribute
argument_list|(
name|EXCHANGE_ATTRIBUTE_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
comment|// no asynchronous result so leverage continuation
specifier|final
name|Continuation
name|continuation
init|=
name|ContinuationSupport
operator|.
name|getContinuation
argument_list|(
name|request
argument_list|)
decl_stmt|;
if|if
condition|(
name|continuation
operator|.
name|isInitial
argument_list|()
operator|&&
name|continuationTimeout
operator|!=
literal|null
condition|)
block|{
comment|// set timeout on initial
name|continuation
operator|.
name|setTimeout
argument_list|(
name|continuationTimeout
argument_list|)
expr_stmt|;
block|}
comment|// are we suspended and a request is dispatched initially?
if|if
condition|(
name|consumer
operator|.
name|isSuspended
argument_list|()
operator|&&
name|continuation
operator|.
name|isInitial
argument_list|()
condition|)
block|{
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
name|continuation
operator|.
name|isExpired
argument_list|()
condition|)
block|{
name|String
name|id
init|=
operator|(
name|String
operator|)
name|continuation
operator|.
name|getAttribute
argument_list|(
name|EXCHANGE_ATTRIBUTE_ID
argument_list|)
decl_stmt|;
comment|// remember this id as expired
name|expiredExchanges
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Continuation expired of exchangeId: {}"
argument_list|,
name|id
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
comment|// a new request so create an exchange
specifier|final
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
name|log
operator|.
name|trace
argument_list|(
literal|"Suspending continuation of exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|continuation
operator|.
name|setAttribute
argument_list|(
name|EXCHANGE_ATTRIBUTE_ID
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// must suspend before we process the exchange
name|continuation
operator|.
name|suspend
argument_list|()
expr_stmt|;
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
comment|// use the asynchronous API to process the exchange
name|consumer
operator|.
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// check if the exchange id is already expired
name|boolean
name|expired
init|=
name|expiredExchanges
operator|.
name|remove
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|expired
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Resuming continuation of exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// resume processing after both, sync and async callbacks
name|continuation
operator|.
name|setAttribute
argument_list|(
name|EXCHANGE_ATTRIBUTE_NAME
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|continuation
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot resume expired continuation of exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
comment|// return to let Jetty continuation to work as it will resubmit and invoke the service
comment|// method again when its resumed
return|return;
block|}
try|try
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Resumed continuation and writing response for exchangeId: {}"
argument_list|,
name|result
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// now lets output to the response
name|consumer
operator|.
name|getBinding
argument_list|()
operator|.
name|writeResponse
argument_list|(
name|result
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
block|}
DECL|method|getContinuationTimeout ()
specifier|public
name|Long
name|getContinuationTimeout
parameter_list|()
block|{
return|return
name|continuationTimeout
return|;
block|}
DECL|method|setContinuationTimeout (Long continuationTimeout)
specifier|public
name|void
name|setContinuationTimeout
parameter_list|(
name|Long
name|continuationTimeout
parameter_list|)
block|{
name|this
operator|.
name|continuationTimeout
operator|=
name|continuationTimeout
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|expiredExchanges
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

