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
name|javax
operator|.
name|servlet
operator|.
name|ServletException
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
DECL|field|endpoint
specifier|private
name|HttpEndpoint
name|endpoint
decl_stmt|;
DECL|method|CamelServlet ()
specifier|public
name|CamelServlet
parameter_list|()
block|{     }
DECL|method|CamelServlet (HttpEndpoint endpoint)
specifier|public
name|CamelServlet
parameter_list|(
name|HttpEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
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
name|HttpEndpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"No endpoint found for request: "
operator|+
name|request
operator|.
name|getRequestURI
argument_list|()
argument_list|)
throw|;
block|}
name|HttpExchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// HC: The getBinding() interesting because it illustrates the impedance miss-match between
comment|// HTTP's stream oriented protocol, and Camels more message oriented protocol exchanges.
comment|// now lets output to the response
name|endpoint
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
DECL|method|resolveEndpoint (HttpServletRequest request, HttpServletResponse response)
specifier|protected
name|HttpEndpoint
name|resolveEndpoint
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
block|{
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

