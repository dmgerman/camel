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
name|HttpServletRequest
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
DECL|class|HttpBinding
specifier|public
class|class
name|HttpBinding
block|{
comment|/**      * Writes the  exchange to the servlet response      */
DECL|method|writeResponse (HttpExchange exchange)
specifier|public
name|void
name|writeResponse
parameter_list|(
name|HttpExchange
name|exchange
parameter_list|)
block|{
comment|/** TODO */
block|}
comment|/**      * Parses the body from a HTTP message      */
DECL|method|parseBody (HttpMessage httpMessage)
specifier|public
name|Object
name|parseBody
parameter_list|(
name|HttpMessage
name|httpMessage
parameter_list|)
throws|throws
name|IOException
block|{
comment|// lets assume the body is a reader
name|HttpServletRequest
name|request
init|=
name|httpMessage
operator|.
name|getRequest
argument_list|()
decl_stmt|;
return|return
name|request
operator|.
name|getReader
argument_list|()
return|;
block|}
block|}
end_class

end_unit

