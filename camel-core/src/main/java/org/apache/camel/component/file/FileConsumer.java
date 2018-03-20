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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Exchange
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
name|Message
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
DECL|field|extendedAttributes
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|extendedAttributes
decl_stmt|;
DECL|method|FileConsumer (FileEndpoint endpoint, Processor processor, GenericFileOperations<File> operations)
specifier|public
name|FileConsumer
parameter_list|(
name|FileEndpoint
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
if|if
condition|(
name|endpoint
operator|.
name|getExtendedAttributes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|extendedAttributes
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|attribute
range|:
name|endpoint
operator|.
name|getExtendedAttributes
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|extendedAttributes
operator|.
name|add
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|pollDirectory (String fileName, List<GenericFile<File>> fileList, int depth)
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
name|File
argument_list|>
argument_list|>
name|fileList
parameter_list|,
name|int
name|depth
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"pollDirectory from fileName: {}"
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|depth
operator|++
expr_stmt|;
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
name|log
operator|.
name|debug
argument_list|(
literal|"Cannot poll as directory does not exists or its not a directory: {}"
argument_list|,
name|directory
argument_list|)
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isDirectoryMustExist
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Directory does not exist: "
operator|+
name|directory
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Polling directory: {}"
argument_list|,
name|directory
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|File
index|[]
name|dirFiles
init|=
name|directory
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|dirFiles
operator|==
literal|null
operator|||
name|dirFiles
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
literal|"No files found in directory: {}"
argument_list|,
name|directory
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
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
literal|"Found {} in directory: {}"
argument_list|,
name|dirFiles
operator|.
name|length
argument_list|,
name|directory
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|File
argument_list|>
name|files
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|dirFiles
argument_list|)
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isPreSort
argument_list|()
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|files
argument_list|,
parameter_list|(
name|a
parameter_list|,
name|b
parameter_list|)
lambda|->
name|a
operator|.
name|getAbsoluteFile
argument_list|()
operator|.
name|compareTo
argument_list|(
name|a
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|File
name|file
range|:
name|dirFiles
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
literal|"Found file: {} [isAbsolute: {}, isDirectory: {}, isFile: {}, isHidden: {}]"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|file
block|,
name|file
operator|.
name|isAbsolute
argument_list|()
block|,
name|file
operator|.
name|isDirectory
argument_list|()
block|,
name|file
operator|.
name|isFile
argument_list|()
block|,
name|file
operator|.
name|isHidden
argument_list|()
block|}
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
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getCharset
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|isProbeContentType
argument_list|()
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
name|depth
operator|<
name|endpoint
operator|.
name|getMaxDepth
argument_list|()
operator|&&
name|isValidFile
argument_list|(
name|gf
argument_list|,
literal|true
argument_list|,
name|files
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
name|boolean
name|canPollMore
init|=
name|pollDirectory
argument_list|(
name|subDirectory
argument_list|,
name|fileList
argument_list|,
name|depth
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
block|}
else|else
block|{
comment|// Windows can report false to a file on a share so regard it always as a file (if its not a directory)
if|if
condition|(
name|depth
operator|>=
name|endpoint
operator|.
name|minDepth
operator|&&
name|isValidFile
argument_list|(
name|gf
argument_list|,
literal|false
argument_list|,
name|files
argument_list|)
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Adding valid file: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
comment|// matched file so add
if|if
condition|(
name|extendedAttributes
operator|!=
literal|null
condition|)
block|{
name|Path
name|path
init|=
name|file
operator|.
name|toPath
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|allAttributes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|attribute
range|:
name|extendedAttributes
control|)
block|{
try|try
block|{
name|String
name|prefix
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|attribute
operator|.
name|endsWith
argument_list|(
literal|":*"
argument_list|)
condition|)
block|{
name|prefix
operator|=
name|attribute
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|attribute
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|attribute
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|prefix
operator|=
literal|"basic:"
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attributes
init|=
name|Files
operator|.
name|readAttributes
argument_list|(
name|path
argument_list|,
name|attribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|attributes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|attributes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|allAttributes
operator|.
name|put
argument_list|(
name|prefix
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|attribute
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|allAttributes
operator|.
name|put
argument_list|(
literal|"basic:"
operator|+
name|attribute
argument_list|,
name|Files
operator|.
name|getAttribute
argument_list|(
name|path
argument_list|,
name|attribute
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|allAttributes
operator|.
name|put
argument_list|(
name|attribute
argument_list|,
name|Files
operator|.
name|getAttribute
argument_list|(
name|path
argument_list|,
name|attribute
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
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
literal|"Unable to read attribute {} on file {}"
argument_list|,
name|attribute
argument_list|,
name|file
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|gf
operator|.
name|setExtendedAttributes
argument_list|(
name|allAttributes
argument_list|)
expr_stmt|;
block|}
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
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isMatched (GenericFile<File> file, String doneFileName, List<File> files)
specifier|protected
name|boolean
name|isMatched
parameter_list|(
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|,
name|String
name|doneFileName
parameter_list|,
name|List
argument_list|<
name|File
argument_list|>
name|files
parameter_list|)
block|{
name|String
name|onlyName
init|=
name|FileUtil
operator|.
name|stripPath
argument_list|(
name|doneFileName
argument_list|)
decl_stmt|;
comment|// the done file name must be among the files
for|for
control|(
name|File
name|f
range|:
name|files
control|)
block|{
if|if
condition|(
name|f
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|onlyName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Done file: {} does not exist"
argument_list|,
name|doneFileName
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|/**      * Creates a new GenericFile<File> based on the given file.      *      * @param endpointPath the starting directory the endpoint was configured with      * @param file the source file      * @return wrapped as a GenericFile      * @deprecated use {@link #asGenericFile(String, File, String, boolean)}      */
annotation|@
name|Deprecated
DECL|method|asGenericFile (String endpointPath, File file, String charset)
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
parameter_list|,
name|String
name|charset
parameter_list|)
block|{
return|return
name|asGenericFile
argument_list|(
name|endpointPath
argument_list|,
name|file
argument_list|,
name|charset
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Creates a new GenericFile<File> based on the given file.      *      * @param endpointPath the starting directory the endpoint was configured with      * @param file the source file      * @param probeContentType whether to probe the content type of the file or not      * @return wrapped as a GenericFile      */
DECL|method|asGenericFile (String endpointPath, File file, String charset, boolean probeContentType)
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
parameter_list|,
name|String
name|charset
parameter_list|,
name|boolean
name|probeContentType
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
argument_list|(
name|probeContentType
argument_list|)
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
name|setCharset
argument_list|(
name|charset
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
name|answer
operator|.
name|setDirectory
argument_list|(
name|file
operator|.
name|isDirectory
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
comment|// compute the file path as relative to the starting directory
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
operator|+
name|File
operator|.
name|separator
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
annotation|@
name|Override
DECL|method|updateFileHeaders (GenericFile<File> file, Message message)
specifier|protected
name|void
name|updateFileHeaders
parameter_list|(
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|long
name|length
init|=
name|file
operator|.
name|getFile
argument_list|()
operator|.
name|length
argument_list|()
decl_stmt|;
name|long
name|modified
init|=
name|file
operator|.
name|getFile
argument_list|()
operator|.
name|lastModified
argument_list|()
decl_stmt|;
name|file
operator|.
name|setFileLength
argument_list|(
name|length
argument_list|)
expr_stmt|;
name|file
operator|.
name|setLastModified
argument_list|(
name|modified
argument_list|)
expr_stmt|;
if|if
condition|(
name|length
operator|>=
literal|0
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_LENGTH
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|modified
operator|>=
literal|0
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_LAST_MODIFIED
argument_list|,
name|modified
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|FileEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|FileEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
block|}
end_class

end_unit

