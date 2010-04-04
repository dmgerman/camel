begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
package|;
end_package

begin_class
DECL|class|TestConfig
specifier|public
specifier|final
class|class
name|TestConfig
block|{
DECL|method|TestConfig ()
specifier|private
name|TestConfig
parameter_list|()
block|{     }
DECL|method|getBaseUri ()
specifier|public
specifier|static
name|String
name|getBaseUri
parameter_list|()
block|{
return|return
name|getBaseUri
argument_list|(
literal|"http"
argument_list|)
return|;
block|}
DECL|method|getBaseUri (String scheme)
specifier|public
specifier|static
name|String
name|getBaseUri
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
return|return
name|scheme
operator|+
literal|"://localhost:"
operator|+
name|getPort
argument_list|()
return|;
block|}
DECL|method|getPort ()
specifier|public
specifier|static
name|int
name|getPort
parameter_list|()
block|{
return|return
literal|7441
return|;
block|}
block|}
end_class

end_unit

