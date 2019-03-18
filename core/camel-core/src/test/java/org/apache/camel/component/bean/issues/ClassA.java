begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean.issues
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
operator|.
name|issues
package|;
end_package

begin_class
DECL|class|ClassA
specifier|public
class|class
name|ClassA
block|{
DECL|method|foo ()
specifier|public
name|String
name|foo
parameter_list|()
block|{
return|return
literal|"A"
return|;
block|}
DECL|method|foo (String param1)
specifier|public
name|String
name|foo
parameter_list|(
name|String
name|param1
parameter_list|)
block|{
return|return
literal|"B"
return|;
block|}
DECL|method|foo (String param1, String param2)
specifier|public
name|String
name|foo
parameter_list|(
name|String
name|param1
parameter_list|,
name|String
name|param2
parameter_list|)
block|{
return|return
literal|"C"
return|;
block|}
DECL|method|foo (String param1, ClassB param2)
specifier|public
name|String
name|foo
parameter_list|(
name|String
name|param1
parameter_list|,
name|ClassB
name|param2
parameter_list|)
block|{
return|return
literal|"D"
return|;
block|}
DECL|method|foo (boolean param1, String param2)
specifier|public
name|String
name|foo
parameter_list|(
name|boolean
name|param1
parameter_list|,
name|String
name|param2
parameter_list|)
block|{
return|return
literal|"E"
return|;
block|}
block|}
end_class

end_unit

