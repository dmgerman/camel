begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|resources
package|;
end_package

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|LanguageResource
specifier|public
class|class
name|LanguageResource
extends|extends
name|CamelChildResourceSupport
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|method|LanguageResource (CamelContextResource contextResource, String id)
specifier|public
name|LanguageResource
parameter_list|(
name|CamelContextResource
name|contextResource
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|contextResource
argument_list|)
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
block|}
end_class

end_unit

