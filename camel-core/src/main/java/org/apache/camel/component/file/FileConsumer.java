begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
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
package|;
end_package

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
name|util
operator|.
name|List
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
comment|/**  * File consumer.  */
end_comment

begin_class
DECL|class|FileConsumer
specifier|public
class|class
name|FileConsumer
extends|extends
name|GenericFileConsumer
argument_list|<
name|File
argument_list|>
block|{
DECL|field|endpointPath
specifier|private
name|String
name|endpointPath
decl_stmt|;
DECL|method|FileConsumer (GenericFileEndpoint<File> endpoint, Processor processor, GenericFileOperations<File> operations)
specifier|public
name|FileConsumer
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|File
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|GenericFileOperations
argument_list|<
name|File
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
DECL|method|pollDirectory (String fileName, List<GenericFile<File>> fileList)
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
name|File
argument_list|>
argument_list|>
name|fileList
parameter_list|)
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
literal|"pollDirectory from fileName: "
operator|+
name|fileName
argument_list|)
expr_stmt|;
block|}
name|File
name|directory
init|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|directory
operator|.
name|exists
argument_list|()
operator|||
operator|!
name|directory
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Cannot poll as directory does not exists or its not a directory: "
operator|+
name|directory
argument_list|)
expr_stmt|;
block|}
return|return;
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
name|directory
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|File
index|[]
name|files
init|=
name|directory
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|==
literal|null
operator|||
name|files
operator|.
name|length
operator|==
literal|0
condition|)
block|{
comment|// no files in this directory to poll
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
literal|"No files found in directory: "
operator|+
name|directory
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
else|else
block|{
comment|// we found some files
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
literal|"Found "
operator|+
name|files
operator|.
name|length
operator|+
literal|" in directory: "
operator|+
name|directory
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
comment|// trace log as Windows/Unix can have different views what the file is?
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
literal|"Found file: "
operator|+
name|file
operator|+
literal|" [isAbsolute: "
operator|+
name|file
operator|.
name|isAbsolute
argument_list|()
operator|+
literal|", isDirectory: "
operator|+
name|file
operator|.
name|isDirectory
argument_list|()
operator|+
literal|", isFile: "
operator|+
name|file
operator|.
name|isFile
argument_list|()
operator|+
literal|", isHidden: "
operator|+
name|file
operator|.
name|isHidden
argument_list|()
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
comment|// creates a generic file
name|GenericFile
argument_list|<
name|File
argument_list|>
name|gf
init|=
name|asGenericFile
argument_list|(
name|endpointPath
argument_list|,
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isRecursive
argument_list|()
operator|&&
name|isValidFile
argument_list|(
name|gf
argument_list|,
literal|true
argument_list|)
condition|)
block|{
comment|// recursive scan and add the sub files and folders
name|String
name|subDirectory
init|=
name|fileName
operator|+
name|File
operator|.
name|separator
operator|+
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
name|pollDirectory
argument_list|(
name|subDirectory
argument_list|,
name|fileList
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// Windows can report false to a file on a share so regard it always as a file (if its not a directory)
if|if
condition|(
name|isValidFile
argument_list|(
name|gf
argument_list|,
literal|false
argument_list|)
condition|)
block|{
if|if
condition|(
name|isInProgress
argument_list|(
name|gf
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
literal|"Skipping as file is already in progress: "
operator|+
name|gf
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
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
literal|"Adding valid file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
comment|// matched file so add
name|fileList
operator|.
name|add
argument_list|(
name|gf
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/**      * Creates a new GenericFile<File> based on the given file.      *      * @param endpointPath the starting directory the endpoint was configured with      * @param file the source file      * @return wrapped as a GenericFile      */
DECL|method|asGenericFile (String endpointPath, File file)
specifier|public
specifier|static
name|GenericFile
argument_list|<
name|File
argument_list|>
name|asGenericFile
parameter_list|(
name|String
name|endpointPath
parameter_list|,
name|File
name|file
parameter_list|)
block|{
name|GenericFile
argument_list|<
name|File
argument_list|>
name|answer
init|=
operator|new
name|GenericFile
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
comment|// use file specific binding
name|answer
operator|.
name|setBinding
argument_list|(
operator|new
name|FileBinding
argument_list|()
argument_list|)
expr_stmt|;
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
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setFileNameOnly
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setFileLength
argument_list|(
name|file
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
comment|// must use FileUtil.isAbsolute to have consistent check for whether the file is
comment|// absolute or not. As windows do not consider \ paths as absolute where as all
comment|// other OS platforms will consider \ as absolute. The logic in Camel mandates
comment|// that we align this for all OS. That is why we must use FileUtil.isAbsolute
comment|// to return a consistent answer for all OS platforms.
name|answer
operator|.
name|setAbsolute
argument_list|(
name|FileUtil
operator|.
name|isAbsolute
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setAbsoluteFilePath
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setLastModified
argument_list|(
name|file
operator|.
name|lastModified
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
comment|// use absolute path as relative
name|answer
operator|.
name|setRelativeFilePath
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|File
name|path
decl_stmt|;
name|String
name|endpointNormalized
init|=
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|endpointPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|getPath
argument_list|()
operator|.
name|startsWith
argument_list|(
name|endpointNormalized
argument_list|)
condition|)
block|{
comment|// skip duplicate endpoint path
name|path
operator|=
operator|new
name|File
argument_list|(
name|ObjectHelper
operator|.
name|after
argument_list|(
name|file
operator|.
name|getPath
argument_list|()
argument_list|,
name|endpointNormalized
operator|+
name|File
operator|.
name|separator
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|path
operator|=
operator|new
name|File
argument_list|(
name|file
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRelativeFilePath
argument_list|(
name|path
operator|.
name|getParent
argument_list|()
operator|+
name|File
operator|.
name|separator
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|.
name|setRelativeFilePath
argument_list|(
name|path
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// use file as body as we have converters if needed as stream
name|answer
operator|.
name|setBody
argument_list|(
name|file
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

