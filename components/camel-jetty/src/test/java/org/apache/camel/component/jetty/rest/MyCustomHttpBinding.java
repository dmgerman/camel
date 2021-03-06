begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty.rest
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
operator|.
name|rest
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
name|Message
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
name|jetty
operator|.
name|JettyRestHttpBinding
import|;
end_import

begin_class
DECL|class|MyCustomHttpBinding
specifier|public
class|class
name|MyCustomHttpBinding
extends|extends
name|JettyRestHttpBinding
block|{
DECL|field|greeting
specifier|private
name|String
name|greeting
decl_stmt|;
DECL|method|MyCustomHttpBinding (String greeting)
specifier|public
name|MyCustomHttpBinding
parameter_list|(
name|String
name|greeting
parameter_list|)
block|{
name|this
operator|.
name|greeting
operator|=
name|greeting
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doWriteResponse (Message message, HttpServletResponse response, Exchange exchange)
specifier|public
name|void
name|doWriteResponse
parameter_list|(
name|Message
name|message
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|message
operator|.
name|setBody
argument_list|(
name|greeting
operator|+
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|doWriteResponse
argument_list|(
name|message
argument_list|,
name|response
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

