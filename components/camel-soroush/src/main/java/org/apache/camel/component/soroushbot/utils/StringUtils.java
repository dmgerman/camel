begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.soroushbot.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|soroushbot
operator|.
name|utils
package|;
end_package

begin_class
DECL|class|StringUtils
specifier|public
specifier|final
class|class
name|StringUtils
block|{
DECL|method|StringUtils ()
specifier|protected
name|StringUtils
parameter_list|()
block|{     }
comment|/**      * create ordinal value for each number. like 1st, 2nd, 3rd, 4th ...      *      * @param number      * @return the ordinal value of {@code number}      */
DECL|method|ordinal (int number)
specifier|public
specifier|static
name|String
name|ordinal
parameter_list|(
name|int
name|number
parameter_list|)
block|{
return|return
name|number
operator|%
literal|100
operator|==
literal|11
operator|||
name|number
operator|%
literal|100
operator|==
literal|12
operator|||
name|number
operator|%
literal|100
operator|==
literal|13
condition|?
name|number
operator|+
literal|"th"
else|:
name|number
operator|+
operator|new
name|String
index|[]
block|{
literal|"th"
block|,
literal|"st"
block|,
literal|"nd"
block|,
literal|"rd"
block|,
literal|"th"
block|,
literal|"th"
block|,
literal|"th"
block|,
literal|"th"
block|,
literal|"th"
block|,
literal|"th"
block|}
index|[
name|number
operator|%
literal|10
index|]
return|;
block|}
block|}
end_class

end_unit

