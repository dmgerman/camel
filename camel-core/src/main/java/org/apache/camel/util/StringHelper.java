begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_comment
comment|/**  * Helper methods for working with Strings.   */
end_comment

begin_class
DECL|class|StringHelper
specifier|public
specifier|final
class|class
name|StringHelper
block|{
comment|/**      * Constructor of utility class should be private.      */
DECL|method|StringHelper ()
specifier|private
name|StringHelper
parameter_list|()
block|{     }
comment|/**      * Ensures that<code>s</code> is friendly for a URL or file system.      *       * @param s      *            String to be sanitized.      * @return sanitized version of<code>s</code>.      * @throws NullPointerException      *             if<code>s</code> is<code>null</code>.      */
DECL|method|sanitize (String s)
specifier|public
specifier|static
name|String
name|sanitize
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|.
name|replace
argument_list|(
literal|':'
argument_list|,
literal|'-'
argument_list|)
operator|.
name|replace
argument_list|(
literal|'_'
argument_list|,
literal|'-'
argument_list|)
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'-'
argument_list|)
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'-'
argument_list|)
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'-'
argument_list|)
return|;
block|}
block|}
end_class

end_unit

