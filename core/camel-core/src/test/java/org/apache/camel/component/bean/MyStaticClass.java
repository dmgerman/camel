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
comment|/** * */
end_comment

begin_class
DECL|class|MyStaticClass
specifier|public
specifier|final
class|class
name|MyStaticClass
block|{
DECL|method|MyStaticClass ()
specifier|private
name|MyStaticClass
parameter_list|()
block|{     }
DECL|method|changeSomething (String s)
specifier|public
specifier|static
name|String
name|changeSomething
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
literal|"Hello World"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
literal|"Bye World"
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|isCamel (String body)
specifier|public
specifier|static
name|boolean
name|isCamel
parameter_list|(
name|String
name|body
parameter_list|)
block|{
return|return
name|body
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
return|;
block|}
DECL|method|doSomething ()
specifier|public
name|void
name|doSomething
parameter_list|()
block|{
comment|// noop
block|}
block|}
end_class

end_unit

