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

begin_import
import|import
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
name|GenericFile
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
name|util
operator|.
name|FileUtil
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Secure FTP consumer  */
end_comment

begin_class
DECL|class|SftpConsumer
specifier|public
class|class
name|SftpConsumer
extends|extends
name|RemoteFileConsumer
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
block|{
DECL|field|endpointPath
specifier|private
name|String
name|endpointPath
decl_stmt|;
DECL|method|SftpConsumer (RemoteFileEndpoint<ChannelSftp.LsEntry> endpoint, Processor processor, RemoteFileOperations<ChannelSftp.LsEntry> operations)
specifier|public
name|SftpConsumer
parameter_list|(
name|RemoteFileEndpoint
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|RemoteFileOperations
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|operations
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|operations
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpointPath
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDirectory
argument_list|()
expr_stmt|;
block|}
DECL|method|pollDirectory (String fileName, List<GenericFile<ChannelSftp.LsEntry>> fileList)
specifier|protected
name|void
name|pollDirectory
parameter_list|(
name|String
name|fileName
parameter_list|,
name|List
argument_list|<
name|GenericFile
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
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
comment|// remove trailing /
name|fileName
operator|=
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|file
operator|.
name|getAttrs
argument_list|()
operator|.
name|isDir
argument_list|()
condition|)
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
name|endpoint
operator|.
name|isRecursive
argument_list|()
operator|&&
name|isValidFile
argument_list|(
name|remote
argument_list|,
literal|true
argument_list|)
condition|)
block|{
comment|// recursive scan and add the sub files and folders
name|String
name|directory
init|=
name|fileName
operator|+
literal|"/"
operator|+
name|file
operator|.
name|getFilename
argument_list|()
decl_stmt|;
name|pollDirectory
argument_list|(
name|directory
argument_list|,
name|fileList
argument_list|)
expr_stmt|;
block|}
comment|// we cannot use file.getAttrs().isLink on Windows, so we dont invoke the method
comment|// just assuming its a file we should poll
block|}
else|else
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
name|answer
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
name|answer
operator|.
name|setEndpointPath
argument_list|(
name|endpointPath
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setFileName
argument_list|(
name|file
operator|.
name|getFilename
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setFileNameOnly
argument_list|(
name|file
operator|.
name|getFilename
argument_list|()
argument_list|)
expr_stmt|;
name|answer
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
name|answer
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
name|answer
operator|.
name|setHostname
argument_list|(
operator|(
operator|(
name|RemoteFileConfiguration
operator|)
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|)
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
comment|// all ftp files is considered as relative
name|answer
operator|.
name|setAbsolute
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// create a pseudo absolute name
name|String
name|absoluteFileName
init|=
operator|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|directory
argument_list|)
condition|?
name|directory
operator|+
literal|"/"
else|:
literal|""
operator|)
operator|+
name|file
operator|.
name|getFilename
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setAbsoluteFilePath
argument_list|(
name|absoluteFileName
argument_list|)
expr_stmt|;
comment|// the relative filename, skip the leading endpoint configured path
name|String
name|relativePath
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|absoluteFileName
argument_list|,
name|endpointPath
argument_list|)
decl_stmt|;
comment|// skip trailing /
name|relativePath
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|relativePath
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setRelativeFilePath
argument_list|(
name|relativePath
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

