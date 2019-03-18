begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * The bean class which implements the business logical  */
end_comment

begin_class
DECL|class|HelloWorldService
specifier|public
class|class
name|HelloWorldService
block|{
DECL|field|count
specifier|private
name|int
name|count
decl_stmt|;
DECL|field|greetName
specifier|private
name|String
name|greetName
decl_stmt|;
DECL|method|sayHello (String name)
specifier|public
name|String
name|sayHello
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|count
operator|++
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|name
return|;
block|}
DECL|method|greet (String name)
specifier|public
name|void
name|greet
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|greetName
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getGreetName ()
specifier|public
name|String
name|getGreetName
parameter_list|()
block|{
return|return
name|greetName
return|;
block|}
DECL|method|getCount ()
specifier|public
name|int
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
block|}
end_class

end_unit

