begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty.proxy
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
name|proxy
package|;
end_package

begin_class
DECL|class|MyCoolServiceBean
specifier|public
class|class
name|MyCoolServiceBean
implements|implements
name|MyCoolService
block|{
DECL|method|hello (String name)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|MyAppException
block|{
if|if
condition|(
literal|"Kaboom"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MyAppException
argument_list|(
literal|"Invalid name"
argument_list|,
literal|"Kaboom"
argument_list|)
throw|;
block|}
return|return
literal|"Hello "
operator|+
name|name
return|;
block|}
block|}
end_class

end_unit

