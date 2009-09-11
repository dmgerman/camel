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
comment|// TODO: should use the new Async API and allow end users to define if they want Jetty continuation support or not
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
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
comment|// final DefaultExchange exchange = new DefaultExchange(consumer.getEndpoint(), ExchangePattern.InOut);
comment|// exchange.setProperty(HttpConstants.SERVLET_REQUEST, request);
comment|// exchange.setProperty(HttpConstants.SERVLET_RESPONSE, response);
comment|// exchange.setIn(new HttpMessage(exchange, request));
comment|// boolean sync = consumer.getAsyncProcessor().process(exchange, new AsyncCallback() {
comment|//     public void done(boolean sync) {
comment|//        if (sync) {
comment|//            return;
comment|//        }
comment|//        continuation.setObject(exchange);
comment|//        continuation.resume();
comment|//    }
comment|//});
comment|//if (!sync) {
comment|// Wait for the exchange to get processed.
comment|// This might block until it completes or it might return via an exception and
comment|// then this method is re-invoked once the the exchange has finished processing
comment|//    continuation.suspend(0);
comment|//}
comment|// HC: The getBinding() is interesting because it illustrates the
comment|// impedance miss-match between HTTP's stream oriented protocol, and
comment|// Camels more message oriented protocol exchanges.
comment|// now lets output to the response
comment|//consumer.getBinding().writeResponse(exchange, response);
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
name|Exchange
name|exchange
init|=
operator|(
name|Exchange
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
argument_list|,
name|response
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

