begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
package|;
end_package

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|MyEcho
specifier|public
specifier|final
class|class
name|MyEcho
block|{
DECL|method|MyEcho ()
specifier|private
name|MyEcho
parameter_list|()
block|{
comment|//Helper class
block|}
DECL|method|echoString (String str)
specifier|public
specifier|static
name|String
name|echoString
parameter_list|(
name|String
name|str
parameter_list|)
block|{
comment|// throw a runtime exception
if|if
condition|(
name|str
operator|==
literal|null
operator|||
name|str
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot take the null String."
argument_list|)
throw|;
block|}
return|return
name|str
return|;
block|}
block|}
end_class

end_unit

