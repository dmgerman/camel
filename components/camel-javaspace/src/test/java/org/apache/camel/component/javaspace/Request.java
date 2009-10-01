begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.javaspace
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|javaspace
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|Request
specifier|public
class|class
name|Request
implements|implements
name|Serializable
block|{
comment|/**      *       */
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|5468959998325283568L
decl_stmt|;
DECL|field|payload
specifier|private
name|String
name|payload
decl_stmt|;
DECL|method|getPayload ()
specifier|public
name|String
name|getPayload
parameter_list|()
block|{
return|return
name|payload
return|;
block|}
DECL|method|setPayload (String payload)
specifier|public
name|void
name|setPayload
parameter_list|(
name|String
name|payload
parameter_list|)
block|{
name|this
operator|.
name|payload
operator|=
name|payload
expr_stmt|;
block|}
block|}
end_class

end_unit

