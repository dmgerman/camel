begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|integration
package|;
end_package

begin_class
DECL|class|SpringIntegrationHelper
specifier|public
specifier|final
class|class
name|SpringIntegrationHelper
block|{
DECL|method|SpringIntegrationHelper ()
specifier|private
name|SpringIntegrationHelper
parameter_list|()
block|{
comment|// Helper class
block|}
DECL|method|checkSpringBeanInstance (Object bean, String name)
specifier|public
specifier|static
name|void
name|checkSpringBeanInstance
parameter_list|(
name|Object
name|bean
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|bean
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can't find the bean: "
operator|+
name|name
operator|+
literal|" from the Spring context"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

