begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven.connector
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|connector
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|GitHelper
specifier|public
specifier|final
class|class
name|GitHelper
block|{
DECL|method|GitHelper ()
specifier|private
name|GitHelper
parameter_list|()
block|{     }
DECL|method|findGitFolder ()
specifier|public
specifier|static
name|File
name|findGitFolder
parameter_list|()
block|{
name|File
name|baseDir
init|=
operator|new
name|File
argument_list|(
literal|""
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
return|return
name|findGitFolder
argument_list|(
name|baseDir
argument_list|)
return|;
block|}
DECL|method|findGitFolder (File basedir)
specifier|private
specifier|static
name|File
name|findGitFolder
parameter_list|(
name|File
name|basedir
parameter_list|)
block|{
name|File
name|gitDir
init|=
operator|new
name|File
argument_list|(
name|basedir
argument_list|,
literal|".git"
argument_list|)
decl_stmt|;
if|if
condition|(
name|gitDir
operator|.
name|exists
argument_list|()
operator|&&
name|gitDir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return
name|gitDir
return|;
block|}
name|File
name|parent
init|=
name|basedir
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|findGitFolder
argument_list|(
name|parent
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns the remote git URL for the given folder; looking for the .git/config file in the current directory or a parent directory      */
DECL|method|extractGitUrl (File basedir)
specifier|public
specifier|static
name|String
name|extractGitUrl
parameter_list|(
name|File
name|basedir
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|basedir
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|basedir
operator|.
name|exists
argument_list|()
operator|&&
name|basedir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
name|gitConfig
init|=
operator|new
name|File
argument_list|(
name|basedir
argument_list|,
literal|".git/config"
argument_list|)
decl_stmt|;
if|if
condition|(
name|gitConfig
operator|.
name|isFile
argument_list|()
operator|&&
name|gitConfig
operator|.
name|exists
argument_list|()
condition|)
block|{
name|String
name|text
init|=
name|FileHelper
operator|.
name|loadText
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|gitConfig
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|extractGitUrl
argument_list|(
name|text
argument_list|)
return|;
block|}
block|}
name|File
name|parentFile
init|=
name|basedir
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentFile
operator|!=
literal|null
condition|)
block|{
return|return
name|extractGitUrl
argument_list|(
name|parentFile
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns the remote git URL for the given git config file text lets extract the      */
DECL|method|extractGitUrl (String configText)
specifier|private
specifier|static
name|String
name|extractGitUrl
parameter_list|(
name|String
name|configText
parameter_list|)
block|{
name|String
name|remote
init|=
literal|null
decl_stmt|;
name|String
name|lastUrl
init|=
literal|null
decl_stmt|;
name|String
name|firstUrl
init|=
literal|null
decl_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|StringReader
argument_list|(
name|configText
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|remoteUrls
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|String
name|line
init|=
literal|null
decl_stmt|;
try|try
block|{
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore should never happen!
block|}
if|if
condition|(
name|line
operator|==
literal|null
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"[remote "
argument_list|)
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|line
operator|.
name|split
argument_list|(
literal|"\""
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|remote
operator|=
name|parts
index|[
literal|1
index|]
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
condition|)
block|{
name|remote
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|remote
operator|!=
literal|null
operator|&&
name|line
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
name|Character
operator|.
name|isWhitespace
argument_list|(
name|line
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|trimmed
init|=
name|line
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|trimmed
operator|.
name|startsWith
argument_list|(
literal|"url "
argument_list|)
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|trimmed
operator|.
name|split
argument_list|(
literal|"="
argument_list|,
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|lastUrl
operator|=
name|parts
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|firstUrl
operator|==
literal|null
condition|)
block|{
name|firstUrl
operator|=
name|lastUrl
expr_stmt|;
block|}
name|remoteUrls
operator|.
name|put
argument_list|(
name|remote
argument_list|,
name|lastUrl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|String
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|remoteUrls
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|lastUrl
return|;
block|}
elseif|else
if|if
condition|(
name|remoteUrls
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|answer
operator|=
name|remoteUrls
operator|.
name|get
argument_list|(
literal|"origin"
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|firstUrl
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

