begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.springboot.commands.crsh
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|springboot
operator|.
name|commands
operator|.
name|crsh
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
name|commands
operator|.
name|StringEscape
import|;
end_import

begin_class
DECL|class|NoopStringEscape
class|class
name|NoopStringEscape
implements|implements
name|StringEscape
block|{
DECL|method|unescapeJava (String s)
specifier|public
name|String
name|unescapeJava
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
return|;
block|}
DECL|method|escapeJava (String s)
specifier|public
name|String
name|escapeJava
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
return|;
block|}
DECL|method|hex (char c)
specifier|public
name|String
name|hex
parameter_list|(
name|char
name|c
parameter_list|)
block|{
return|return
name|Character
operator|.
name|toString
argument_list|(
name|c
argument_list|)
return|;
block|}
block|}
end_class

end_unit

