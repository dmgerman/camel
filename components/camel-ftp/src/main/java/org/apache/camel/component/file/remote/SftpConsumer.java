begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|remote
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|ChannelSftp
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
import|;
end_import

begin_comment
comment|/**  * SFTP consumer  */
end_comment

begin_class
DECL|class|SftpConsumer
specifier|public
class|class
name|SftpConsumer
extends|extends
name|RemoteFileConsumer
block|{
DECL|method|SftpConsumer (RemoteFileEndpoint endpoint, Processor processor, RemoteFileOperations remoteFileOperations)
specifier|public
name|SftpConsumer
parameter_list|(
name|RemoteFileEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|RemoteFileOperations
name|remoteFileOperations
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|remoteFileOperations
argument_list|)
expr_stmt|;
block|}
DECL|method|pollDirectory (String fileName, boolean processDir, List<RemoteFile> fileList)
specifier|protected
name|void
name|pollDirectory
parameter_list|(
name|String
name|fileName
parameter_list|,
name|boolean
name|processDir
parameter_list|,
name|List
argument_list|<
name|RemoteFile
argument_list|>
name|fileList
parameter_list|)
block|{
if|if
condition|(
name|fileName
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|fileName
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|fileName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fileName
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Polling directory: "
operator|+
name|fileName
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|files
init|=
name|operations
operator|.
name|listFiles
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
for|for
control|(
name|ChannelSftp
operator|.
name|LsEntry
name|file
range|:
name|files
control|)
block|{
name|RemoteFile
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|remote
init|=
name|asRemoteFile
argument_list|(
name|fileName
argument_list|,
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|processDir
operator|&&
name|file
operator|.
name|getAttrs
argument_list|()
operator|.
name|isDir
argument_list|()
condition|)
block|{
if|if
condition|(
name|isValidFile
argument_list|(
name|remote
argument_list|,
literal|true
argument_list|)
condition|)
block|{
comment|// recursive scan and add the sub files and folders
name|pollDirectory
argument_list|(
name|file
operator|.
name|getFilename
argument_list|()
argument_list|,
name|endpoint
operator|.
name|isRecursive
argument_list|()
argument_list|,
name|fileList
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|file
operator|.
name|getAttrs
argument_list|()
operator|.
name|isLink
argument_list|()
condition|)
block|{
if|if
condition|(
name|isValidFile
argument_list|(
name|remote
argument_list|,
literal|false
argument_list|)
condition|)
block|{
comment|// matched file so add
name|fileList
operator|.
name|add
argument_list|(
name|remote
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Ignoring unsupported remote file type: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Polls the given file      *      * @param fileName  the file name      * @param fileList  current list of files gathered      */
DECL|method|pollFile (String fileName, List<RemoteFile> fileList)
specifier|protected
name|void
name|pollFile
parameter_list|(
name|String
name|fileName
parameter_list|,
name|List
argument_list|<
name|RemoteFile
argument_list|>
name|fileList
parameter_list|)
block|{
name|String
name|directory
init|=
literal|"."
decl_stmt|;
name|int
name|index
init|=
name|fileName
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
operator|-
literal|1
condition|)
block|{
name|directory
operator|=
name|fileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
comment|// list the files in the fold and poll the first file
name|List
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|list
init|=
name|operations
operator|.
name|listFiles
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|ChannelSftp
operator|.
name|LsEntry
name|file
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|RemoteFile
name|remoteFile
init|=
name|asRemoteFile
argument_list|(
name|directory
argument_list|,
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|isValidFile
argument_list|(
name|remoteFile
argument_list|,
literal|false
argument_list|)
condition|)
block|{
comment|// matched file so add
name|fileList
operator|.
name|add
argument_list|(
name|remoteFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|asRemoteFile (String directory, ChannelSftp.LsEntry file)
specifier|private
name|RemoteFile
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|asRemoteFile
parameter_list|(
name|String
name|directory
parameter_list|,
name|ChannelSftp
operator|.
name|LsEntry
name|file
parameter_list|)
block|{
name|RemoteFile
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|remote
init|=
operator|new
name|RemoteFile
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
argument_list|()
decl_stmt|;
name|remote
operator|.
name|setFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|remote
operator|.
name|setFileName
argument_list|(
name|file
operator|.
name|getFilename
argument_list|()
argument_list|)
expr_stmt|;
name|remote
operator|.
name|setFileLength
argument_list|(
name|file
operator|.
name|getAttrs
argument_list|()
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|remote
operator|.
name|setLastModified
argument_list|(
name|file
operator|.
name|getAttrs
argument_list|()
operator|.
name|getMTime
argument_list|()
operator|*
literal|1000L
argument_list|)
expr_stmt|;
name|remote
operator|.
name|setHostname
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|absoluteFileName
init|=
name|directory
operator|+
literal|"/"
operator|+
name|file
operator|.
name|getFilename
argument_list|()
decl_stmt|;
name|remote
operator|.
name|setAbsolutelFileName
argument_list|(
name|absoluteFileName
argument_list|)
expr_stmt|;
comment|// the relative filename
name|String
name|ftpBasePath
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|String
name|relativePath
init|=
name|absoluteFileName
operator|.
name|substring
argument_list|(
name|ftpBasePath
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|relativePath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|relativePath
operator|=
name|relativePath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|remote
operator|.
name|setRelativeFileName
argument_list|(
name|relativePath
argument_list|)
expr_stmt|;
return|return
name|remote
return|;
block|}
block|}
end_class

end_unit

