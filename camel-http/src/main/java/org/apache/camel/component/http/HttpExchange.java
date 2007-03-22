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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
comment|/**  * Represents a HTTP exchange  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|HttpExchange
specifier|public
class|class
name|HttpExchange
extends|extends
name|DefaultExchange
block|{
DECL|field|request
specifier|private
name|HttpServletRequest
name|request
decl_stmt|;
DECL|field|response
specifier|private
name|HttpServletResponse
name|response
decl_stmt|;
DECL|method|HttpExchange (CamelContext context)
specifier|public
name|HttpExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|HttpExchange (CamelContext context, HttpServletRequest request, HttpServletResponse response)
specifier|public
name|HttpExchange
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|request
operator|=
name|request
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
name|setIn
argument_list|(
operator|new
name|HttpMessage
argument_list|(
name|request
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the underlying Servlet request for inbound HTTP requests      *      * @return the underlying Servlet request for inbound HTTP requests      */
DECL|method|getRequest ()
specifier|public
name|HttpServletRequest
name|getRequest
parameter_list|()
block|{
return|return
name|request
return|;
block|}
comment|/**      * Returns the underlying Servlet response for inbound HTTP requests      *      * @return the underlying Servlet response for inbound HTTP requests      */
DECL|method|getResponse ()
specifier|public
name|HttpServletResponse
name|getResponse
parameter_list|()
block|{
return|return
name|response
return|;
block|}
block|}
end_class

end_unit

