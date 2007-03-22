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
name|impl
operator|.
name|DefaultMessage
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|HttpMessage
specifier|public
class|class
name|HttpMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|request
specifier|private
name|HttpServletRequest
name|request
decl_stmt|;
DECL|method|HttpMessage ()
specifier|public
name|HttpMessage
parameter_list|()
block|{     }
DECL|method|HttpMessage (HttpServletRequest request)
specifier|public
name|HttpMessage
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|this
operator|.
name|request
operator|=
name|request
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
return|return
name|super
operator|.
name|createBody
argument_list|()
return|;
comment|/** TODO */
block|}
annotation|@
name|Override
DECL|method|createHeaders ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|createHeaders
parameter_list|()
block|{
return|return
name|super
operator|.
name|createHeaders
argument_list|()
return|;
comment|/** TODO */
block|}
block|}
end_class

end_unit

