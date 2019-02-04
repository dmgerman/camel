begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|MyOtherFooBean
specifier|public
class|class
name|MyOtherFooBean
block|{
DECL|method|echo (String s)
specifier|public
name|String
name|echo
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|+
name|s
return|;
block|}
DECL|method|echo (Integer i)
specifier|public
name|Integer
name|echo
parameter_list|(
name|Integer
name|i
parameter_list|)
block|{
return|return
name|i
operator|.
name|intValue
argument_list|()
operator|*
name|i
operator|.
name|intValue
argument_list|()
return|;
block|}
DECL|method|toString (Object input)
specifier|public
name|String
name|toString
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
return|return
literal|"toString(Object) was called"
return|;
block|}
DECL|method|toString (String input)
specifier|public
name|String
name|toString
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
literal|"toString(String) was called"
return|;
block|}
DECL|interface|InterfaceSize
specifier|public
interface|interface
name|InterfaceSize
block|{
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
block|}
DECL|class|AbstractClassSize
specifier|public
specifier|abstract
specifier|static
class|class
name|AbstractClassSize
block|{
DECL|method|size ()
specifier|public
specifier|abstract
name|int
name|size
parameter_list|()
function_decl|;
block|}
DECL|class|SuperClazz
specifier|public
specifier|static
class|class
name|SuperClazz
extends|extends
name|AbstractClassSize
implements|implements
name|InterfaceSize
block|{
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
block|}
DECL|class|Clazz
specifier|public
specifier|static
class|class
name|Clazz
extends|extends
name|SuperClazz
block|{     }
block|}
end_class

end_unit

