begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_comment
comment|/**  * An example POJO with no annotations or interfaces to test out the POJO  * Camel binding  */
end_comment

begin_class
DECL|class|ExampleBean
specifier|public
class|class
name|ExampleBean
block|{
DECL|method|sayHello (String name)
specifier|public
name|String
name|sayHello
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|name
operator|+
literal|"!"
return|;
block|}
DECL|method|sayGoodbye (String name)
specifier|public
name|String
name|sayGoodbye
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|"Bye "
operator|+
name|name
operator|+
literal|"!"
return|;
block|}
DECL|method|timesTwo (int value)
specifier|public
name|long
name|timesTwo
parameter_list|(
name|int
name|value
parameter_list|)
block|{
return|return
name|value
operator|*
literal|2
return|;
block|}
block|}
end_class

end_unit

