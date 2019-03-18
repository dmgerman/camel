begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
operator|.
name|internal
package|;
end_package

begin_comment
comment|/**  * Util class.  */
end_comment

begin_class
DECL|class|MatchUtil
specifier|public
specifier|final
class|class
name|MatchUtil
block|{
DECL|method|MatchUtil ()
specifier|private
name|MatchUtil
parameter_list|()
block|{     }
DECL|method|matchWildcard (String name, String pattern)
specifier|public
specifier|static
name|boolean
name|matchWildcard
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
return|return
name|pattern
operator|.
name|endsWith
argument_list|(
literal|"*"
argument_list|)
operator|&&
name|name
operator|.
name|startsWith
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pattern
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

