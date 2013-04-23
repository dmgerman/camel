begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|util
package|;
end_package

begin_comment
comment|/**  * Some String helper methods  */
end_comment

begin_class
DECL|class|Strings
specifier|public
class|class
name|Strings
block|{
comment|/**      * Returns true if the given text is null or empty string      */
DECL|method|isNullOrEmpty (String text)
specifier|public
specifier|static
name|boolean
name|isNullOrEmpty
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|text
operator|==
literal|null
operator|||
name|text
operator|.
name|length
argument_list|()
operator|==
literal|0
return|;
block|}
comment|/**      * Returns the value or the defaultValue if it is null      */
DECL|method|getOrElse (String text, String defaultValue)
specifier|public
specifier|static
name|String
name|getOrElse
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
return|return
operator|(
name|text
operator|!=
literal|null
operator|)
condition|?
name|text
else|:
name|defaultValue
return|;
block|}
block|}
end_class

end_unit

