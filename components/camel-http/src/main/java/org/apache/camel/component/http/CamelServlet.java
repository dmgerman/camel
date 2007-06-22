begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelServlet
specifier|public
class|class
name|CamelServlet
extends|extends
name|HttpServlet
block|{
DECL|field|consumers
specifier|private
name|ConcurrentHashMap
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
DECL|method|CamelServlet ()
specifier|public
name|CamelServlet
parameter_list|()
block|{     }
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
comment|// Have the camel process the HTTP exchange.
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
comment|// HC: The getBinding() is interesting because it illustrates the impedance miss-match between
comment|// HTTP's stream oriented protocol, and Camels more message oriented protocol exchanges.
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
return|return
name|consumers
operator|.
name|get
argument_list|(
name|path
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
block|}
end_class

end_unit

