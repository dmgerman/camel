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
comment|/**  * File utilities  */
end_comment

begin_class
DECL|class|FileUtil
specifier|public
specifier|final
class|class
name|FileUtil
block|{
DECL|method|FileUtil ()
specifier|private
name|FileUtil
parameter_list|()
block|{     }
comment|/**      * Normalizes the path to cater for Windows and other platforms      */
DECL|method|normalizePath (String path)
specifier|public
specifier|static
name|String
name|normalizePath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
comment|// special handling for Windows where we need to convert / to \\
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Windows"
argument_list|)
operator|&&
name|path
operator|.
name|indexOf
argument_list|(
literal|"/"
argument_list|)
operator|>=
literal|0
condition|)
block|{
return|return
name|path
operator|.
name|replaceAll
argument_list|(
literal|"/"
argument_list|,
literal|"\\\\"
argument_list|)
return|;
block|}
return|return
name|path
return|;
block|}
block|}
end_class

end_unit

