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
name|HttpExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|util
operator|.
name|ajax
operator|.
name|Continuation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|util
operator|.
name|ajax
operator|.
name|ContinuationSupport
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelContinuationServlet
specifier|public
class|class
name|CamelContinuationServlet
extends|extends
name|CamelServlet
block|{
comment|// private static final String EXCHANGE_ATTRIBUTE =
comment|// CamelContinuationServlet.class.getName()+".EXCHANGE_ATTRIBUTE";
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
try|try
block|{
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
comment|// HttpExchange exchange =
comment|// (HttpExchange)request.getAttribute(EXCHANGE_ATTRIBUTE);
comment|// if( exchange == null ) {
comment|// exchange = new HttpExchange(consumer.getEndpoint(), request,
comment|// response);
comment|// }
comment|// Continuation continuation =
comment|// ContinuationSupport.getContinuation(request, exchange);
specifier|final
name|Continuation
name|continuation
init|=
name|ContinuationSupport
operator|.
name|getContinuation
argument_list|(
name|request
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|continuation
operator|.
name|isNew
argument_list|()
condition|)
block|{
comment|// Have the camel process the HTTP exchange.
specifier|final
name|HttpExchange
name|exchange
init|=
operator|new
name|HttpExchange
argument_list|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
decl_stmt|;
name|boolean
name|sync
init|=
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
name|sync
parameter_list|)
block|{
if|if
condition|(
name|sync
condition|)
block|{
return|return;
block|}
name|continuation
operator|.
name|setObject
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|continuation
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|sync
condition|)
block|{
comment|// Wait for the exchange to get processed.
comment|// This might block until it completes or it might return via an exception and
comment|// then this method is re-invoked once the the exchange has finished processing
name|continuation
operator|.
name|suspend
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|// HC: The getBinding() is interesting because it illustrates the
comment|// impedance miss-match between
comment|// HTTP's stream oriented protocol, and Camels more message oriented
comment|// protocol exchanges.
comment|// now lets output to the response
name|consumer
operator|.
name|getBinding
argument_list|()
operator|.
name|writeResponse
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|continuation
operator|.
name|isResumed
argument_list|()
condition|)
block|{
name|HttpExchange
name|exchange
init|=
operator|(
name|HttpExchange
operator|)
name|continuation
operator|.
name|getObject
argument_list|()
decl_stmt|;
comment|// now lets output to the response
name|consumer
operator|.
name|getBinding
argument_list|()
operator|.
name|writeResponse
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

