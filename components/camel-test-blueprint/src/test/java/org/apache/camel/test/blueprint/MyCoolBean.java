begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
package|;
end_package

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|MyCoolBean
specifier|public
class|class
name|MyCoolBean
block|{
DECL|field|say
specifier|private
name|String
name|say
decl_stmt|;
DECL|field|echo
specifier|private
name|String
name|echo
decl_stmt|;
DECL|method|getSay ()
specifier|public
name|String
name|getSay
parameter_list|()
block|{
return|return
name|say
return|;
block|}
DECL|method|setSay (String say)
specifier|public
name|void
name|setSay
parameter_list|(
name|String
name|say
parameter_list|)
block|{
name|this
operator|.
name|say
operator|=
name|say
expr_stmt|;
block|}
DECL|method|getEcho ()
specifier|public
name|String
name|getEcho
parameter_list|()
block|{
return|return
name|echo
return|;
block|}
DECL|method|setEcho (String echo)
specifier|public
name|void
name|setEcho
parameter_list|(
name|String
name|echo
parameter_list|)
block|{
name|this
operator|.
name|echo
operator|=
name|echo
expr_stmt|;
block|}
DECL|method|saySomething (String s)
specifier|public
name|String
name|saySomething
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|say
operator|+
literal|" "
operator|+
name|s
return|;
block|}
DECL|method|echoSomething (String s)
specifier|public
name|String
name|echoSomething
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|echo
operator|+
literal|" "
operator|+
name|s
operator|+
name|echo
operator|+
literal|" "
operator|+
name|s
return|;
block|}
block|}
end_class

end_unit

