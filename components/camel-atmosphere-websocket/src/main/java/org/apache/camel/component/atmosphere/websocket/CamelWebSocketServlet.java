begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atmosphere.websocket
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atmosphere
operator|.
name|websocket
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
name|component
operator|.
name|servlet
operator|.
name|CamelHttpTransportServlet
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
name|http
operator|.
name|common
operator|.
name|HttpConsumer
import|;
end_import

begin_comment
comment|/**  * This servlet is used to add some websocket specific handling at the moment.  *   * REVISIT  * we might be able to get rid of this servlet by overriding some of the binding  * code that is executed between the servlet and the consumer.  *   */
end_comment

begin_class
DECL|class|CamelWebSocketServlet
specifier|public
class|class
name|CamelWebSocketServlet
extends|extends
name|CamelHttpTransportServlet
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1764707448550670635L
decl_stmt|;
DECL|field|RESEND_ALL_WEBSOCKET_EVENTS_PARAM_KEY
specifier|private
specifier|static
specifier|final
name|String
name|RESEND_ALL_WEBSOCKET_EVENTS_PARAM_KEY
init|=
literal|"events"
decl_stmt|;
DECL|field|enableEventsResending
specifier|private
name|boolean
name|enableEventsResending
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
name|String
name|eventsResendingParameter
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
name|RESEND_ALL_WEBSOCKET_EVENTS_PARAM_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"true"
operator|.
name|equals
argument_list|(
name|eventsResendingParameter
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Events resending enabled"
argument_list|)
expr_stmt|;
name|enableEventsResending
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Override
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
if|if
condition|(
operator|!
operator|(
name|consumer
operator|instanceof
name|WebsocketConsumer
operator|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Consumer not supporting websocket {}"
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
name|log
operator|.
name|debug
argument_list|(
literal|"Dispatching to Websocket Consumer at {}"
argument_list|,
name|consumer
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|WebsocketConsumer
operator|)
name|consumer
operator|)
operator|.
name|service
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
name|enableEventsResending
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

