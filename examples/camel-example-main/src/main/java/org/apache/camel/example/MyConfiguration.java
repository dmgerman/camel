begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
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
name|BindToRegistry
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
name|PropertyInject
import|;
end_import

begin_comment
comment|/**  * Class to configure the Camel application.  */
end_comment

begin_class
DECL|class|MyConfiguration
specifier|public
class|class
name|MyConfiguration
block|{
annotation|@
name|BindToRegistry
DECL|method|myBean (@ropertyInjectR) String hi)
specifier|public
name|MyBean
name|myBean
parameter_list|(
annotation|@
name|PropertyInject
argument_list|(
literal|"hi"
argument_list|)
name|String
name|hi
parameter_list|)
block|{
comment|// this will create an instance of this bean with the name of the method (eg myBean)
return|return
operator|new
name|MyBean
argument_list|(
name|hi
argument_list|)
return|;
block|}
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// this method is optional and can be removed if no additional configuration is needed.
block|}
block|}
end_class

end_unit

