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
annotation|@
name|Override
DECL|method|pollDirectory (String fileName, List<GenericFile<ChannelSftp.LsEntry>> fileList, int depth)
specifier|protected
name|boolean
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
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
name|String
name|currentDir
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isStepwise
argument_list|()
condition|)
block|{
comment|// must remember current dir so we stay in that directory after the poll
name|currentDir
operator|=
name|operations
operator|.
name|getCurrentDirectory
argument_list|()
expr_stmt|;
block|}
comment|// strip trailing slash
name|fileName
operator|=
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|boolean
name|answer
init|=
name|doPollDirectory
argument_list|(
name|fileName
argument_list|,
literal|null
argument_list|,
name|fileList
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentDir
operator|!=
literal|null
condition|)
block|{
name|operations
operator|.
name|changeCurrentDirectory
argument_list|(
name|currentDir
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|pollSubDirectory (String absolutePath, String dirName, List<GenericFile<ChannelSftp.LsEntry>> fileList)
specifier|protected
name|boolean
name|pollSubDirectory
parameter_list|(
name|String
name|absolutePath
parameter_list|,
name|String
name|dirName
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
name|boolean
name|answer
init|=
name|doPollDirectory
argument_list|(
name|absolutePath
argument_list|,
name|dirName
argument_list|,
name|fileList
argument_list|)
decl_stmt|;
comment|// change back to parent directory when finished polling sub directory
if|if
condition|(
name|isStepwise
argument_list|()
condition|)
block|{
name|operations
operator|.
name|changeToParentDirectory
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|doPollDirectory (String absolutePath, String dirName, List<GenericFile<ChannelSftp.LsEntry>> fileList)
specifier|protected
name|boolean
name|doPollDirectory
parameter_list|(
name|String
name|absolutePath
parameter_list|,
name|String
name|dirName
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
name|log
operator|.
name|trace
argument_list|(
literal|"doPollDirectory from absolutePath: {}, dirName: {}"
argument_list|,
name|absolutePath
argument_list|,
name|dirName
argument_list|)
expr_stmt|;
comment|// remove trailing /
name|dirName
operator|=
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
name|dirName
argument_list|)
expr_stmt|;
comment|// compute dir depending on stepwise is enabled or not
name|String
name|dir
decl_stmt|;
if|if
condition|(
name|isStepwise
argument_list|()
condition|)
block|{
name|dir
operator|=
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|dirName
argument_list|)
condition|?
name|dirName
else|:
name|absolutePath
expr_stmt|;
name|operations
operator|.
name|changeCurrentDirectory
argument_list|(
name|dir
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dir
operator|=
name|absolutePath
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Polling directory: {}"
argument_list|,
name|dir
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|files
decl_stmt|;
if|if
condition|(
name|isStepwise
argument_list|()
condition|)
block|{
name|files
operator|=
name|operations
operator|.
name|listFiles
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|files
operator|=
name|operations
operator|.
name|listFiles
argument_list|(
name|dir
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|files
operator|==
literal|null
operator|||
name|files
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// no files in this directory to poll
name|log
operator|.
name|trace
argument_list|(
literal|"No files found in directory: {}"
argument_list|,
name|dir
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
comment|// we found some files
name|log
operator|.
name|trace
argument_list|(
literal|"Found {} in directory: {}"
argument_list|,
name|files
operator|.
name|size
argument_list|()
argument_list|,
name|dir
argument_list|)
expr_stmt|;
block|}
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
comment|// check if we can continue polling in files
if|if
condition|(
operator|!
name|canPollMoreFiles
argument_list|(
name|fileList
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
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
name|absolutePath
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
name|subDirectory
init|=
name|file
operator|.
name|getFilename
argument_list|()
decl_stmt|;
name|String
name|path
init|=
name|absolutePath
operator|+
literal|"/"
operator|+
name|subDirectory
decl_stmt|;
name|boolean
name|canPollMore
init|=
name|pollSubDirectory
argument_list|(
name|path
argument_list|,
name|subDirectory
argument_list|,
name|fileList
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|canPollMore
condition|)
block|{
return|return
literal|false
return|;
block|}
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
name|absolutePath
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
if|if
condition|(
name|isInProgress
argument_list|(
name|remote
argument_list|)
condition|)
block|{
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
literal|"Skipping as file is already in progress: {}"
argument_list|,
name|remote
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
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
return|return
literal|true
return|;
block|}
DECL|method|asRemoteFile (String absolutePath, ChannelSftp.LsEntry file)
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
name|absolutePath
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
comment|// absolute or relative path
name|boolean
name|absolute
init|=
name|FileUtil
operator|.
name|hasLeadingSeparator
argument_list|(
name|absolutePath
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setAbsolute
argument_list|(
name|absolute
argument_list|)
expr_stmt|;
comment|// create a pseudo absolute name
name|String
name|dir
init|=
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
name|absolutePath
argument_list|)
decl_stmt|;
name|String
name|absoluteFileName
init|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|dir
operator|+
literal|"/"
operator|+
name|file
operator|.
name|getFilename
argument_list|()
argument_list|)
decl_stmt|;
comment|// if absolute start with a leading separator otherwise let it be relative
if|if
condition|(
name|absolute
condition|)
block|{
name|absoluteFileName
operator|=
literal|"/"
operator|+
name|absoluteFileName
expr_stmt|;
block|}
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
comment|// the file name should be the relative path
name|answer
operator|.
name|setFileName
argument_list|(
name|answer
operator|.
name|getRelativeFilePath
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|isStepwise ()
specifier|private
name|boolean
name|isStepwise
parameter_list|()
block|{
name|RemoteFileConfiguration
name|config
init|=
operator|(
name|RemoteFileConfiguration
operator|)
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
return|return
name|config
operator|.
name|isStepwise
argument_list|()
return|;
block|}
block|}
end_class

end_unit

