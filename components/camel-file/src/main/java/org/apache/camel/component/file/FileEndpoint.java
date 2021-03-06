begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|FileNotFoundException
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
name|attribute
operator|.
name|PosixFilePermission
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
name|HashSet
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
name|Component
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
name|PollingConsumer
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
name|strategy
operator|.
name|FileMoveExistingStrategy
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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
name|support
operator|.
name|processor
operator|.
name|idempotent
operator|.
name|MemoryIdempotentRepository
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
comment|/**  * The file component is used for reading or writing files.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.0.0"
argument_list|,
name|scheme
operator|=
literal|"file"
argument_list|,
name|title
operator|=
literal|"File"
argument_list|,
name|syntax
operator|=
literal|"file:directoryName"
argument_list|,
name|label
operator|=
literal|"core,file"
argument_list|)
DECL|class|FileEndpoint
specifier|public
class|class
name|FileEndpoint
extends|extends
name|GenericFileEndpoint
argument_list|<
name|File
argument_list|>
block|{
DECL|field|CHMOD_WRITE_MASK
specifier|private
specifier|static
specifier|final
name|Integer
name|CHMOD_WRITE_MASK
init|=
literal|02
decl_stmt|;
DECL|field|CHMOD_READ_MASK
specifier|private
specifier|static
specifier|final
name|Integer
name|CHMOD_READ_MASK
init|=
literal|04
decl_stmt|;
DECL|field|CHMOD_EXECUTE_MASK
specifier|private
specifier|static
specifier|final
name|Integer
name|CHMOD_EXECUTE_MASK
init|=
literal|01
decl_stmt|;
DECL|field|operations
specifier|private
specifier|final
name|FileOperations
name|operations
init|=
operator|new
name|FileOperations
argument_list|(
name|this
argument_list|)
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|name
operator|=
literal|"directoryName"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|file
specifier|private
name|File
name|file
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|copyAndDeleteOnRenameFail
specifier|private
name|boolean
name|copyAndDeleteOnRenameFail
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|renameUsingCopy
specifier|private
name|boolean
name|renameUsingCopy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|startingDirectoryMustExist
specifier|private
name|boolean
name|startingDirectoryMustExist
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|startingDirectoryMustHaveAccess
specifier|private
name|boolean
name|startingDirectoryMustHaveAccess
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|directoryMustExist
specifier|private
name|boolean
name|directoryMustExist
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|probeContentType
specifier|private
name|boolean
name|probeContentType
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|extendedAttributes
specifier|private
name|String
name|extendedAttributes
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|forceWrites
specifier|private
name|boolean
name|forceWrites
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|)
DECL|field|chmod
specifier|private
name|String
name|chmod
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|)
DECL|field|chmodDirectory
specifier|private
name|String
name|chmodDirectory
decl_stmt|;
DECL|method|FileEndpoint ()
specifier|public
name|FileEndpoint
parameter_list|()
block|{     }
DECL|method|FileEndpoint (String endpointUri, Component component)
specifier|public
name|FileEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|FileConsumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|operations
argument_list|,
literal|"operations"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|file
argument_list|,
literal|"file"
argument_list|)
expr_stmt|;
comment|// auto create starting directory if needed
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
name|isAutoCreate
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Creating non existing starting directory: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|boolean
name|absolute
init|=
name|FileUtil
operator|.
name|isAbsolute
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|boolean
name|created
init|=
name|operations
operator|.
name|buildDirectory
argument_list|(
name|file
operator|.
name|getPath
argument_list|()
argument_list|,
name|absolute
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|created
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot auto create starting directory: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|isStartingDirectoryMustExist
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Starting directory does not exist: "
operator|+
name|file
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
operator|!
name|isStartingDirectoryMustExist
argument_list|()
operator|&&
name|isStartingDirectoryMustHaveAccess
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You cannot set startingDirectoryMustHaveAccess=true without setting startingDirectoryMustExist=true"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|isStartingDirectoryMustExist
argument_list|()
operator|&&
name|isStartingDirectoryMustHaveAccess
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|file
operator|.
name|canRead
argument_list|()
operator|||
operator|!
name|file
operator|.
name|canWrite
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Starting directory permission denied: "
operator|+
name|file
argument_list|)
throw|;
block|}
block|}
name|FileConsumer
name|result
init|=
name|newFileConsumer
argument_list|(
name|processor
argument_list|,
name|operations
argument_list|)
decl_stmt|;
if|if
condition|(
name|isDelete
argument_list|()
operator|&&
name|getMove
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You cannot set both delete=true and move options"
argument_list|)
throw|;
block|}
comment|// if noop=true then idempotent should also be configured
if|if
condition|(
name|isNoop
argument_list|()
operator|&&
operator|!
name|isIdempotentSet
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Endpoint is configured with noop=true so forcing endpoint to be idempotent as well"
argument_list|)
expr_stmt|;
name|setIdempotent
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// if idempotent and no repository set then create a default one
if|if
condition|(
name|isIdempotentSet
argument_list|()
operator|&&
name|isIdempotent
argument_list|()
operator|&&
name|idempotentRepository
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Using default memory based idempotent repository with cache max size: {}"
argument_list|,
name|DEFAULT_IDEMPOTENT_CACHE_SIZE
argument_list|)
expr_stmt|;
name|idempotentRepository
operator|=
name|MemoryIdempotentRepository
operator|.
name|memoryIdempotentRepository
argument_list|(
name|DEFAULT_IDEMPOTENT_CACHE_SIZE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getReadLock
argument_list|()
argument_list|)
condition|)
block|{
comment|// check if its a valid
name|String
name|valid
init|=
literal|"none,markerFile,fileLock,rename,changed,idempotent,idempotent-changed,idempotent-rename"
decl_stmt|;
name|String
index|[]
name|arr
init|=
name|valid
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|boolean
name|matched
init|=
name|Arrays
operator|.
name|stream
argument_list|(
name|arr
argument_list|)
operator|.
name|anyMatch
argument_list|(
name|n
lambda|->
name|n
operator|.
name|equals
argument_list|(
name|getReadLock
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|matched
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ReadLock invalid: "
operator|+
name|getReadLock
argument_list|()
operator|+
literal|", must be one of: "
operator|+
name|valid
argument_list|)
throw|;
block|}
block|}
comment|// set max messages per poll
name|result
operator|.
name|setMaxMessagesPerPoll
argument_list|(
name|getMaxMessagesPerPoll
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setEagerLimitMaxMessagesPerPoll
argument_list|(
name|isEagerMaxMessagesPerPoll
argument_list|()
argument_list|)
expr_stmt|;
name|configureConsumer
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|operations
argument_list|,
literal|"operations"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|file
argument_list|,
literal|"file"
argument_list|)
expr_stmt|;
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
literal|"Creating GenericFilePollingConsumer with queueSize: {} blockWhenFull: {} blockTimeout: {}"
argument_list|,
name|getPollingConsumerQueueSize
argument_list|()
argument_list|,
name|isPollingConsumerBlockWhenFull
argument_list|()
argument_list|,
name|getPollingConsumerBlockTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|GenericFilePollingConsumer
name|result
init|=
operator|new
name|GenericFilePollingConsumer
argument_list|(
name|this
argument_list|)
decl_stmt|;
comment|// should not call configurePollingConsumer when its GenericFilePollingConsumer
name|result
operator|.
name|setBlockWhenFull
argument_list|(
name|isPollingConsumerBlockWhenFull
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setBlockTimeout
argument_list|(
name|getPollingConsumerBlockTimeout
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|GenericFileProducer
argument_list|<
name|File
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|operations
argument_list|,
literal|"operations"
argument_list|)
expr_stmt|;
comment|// you cannot use temp file and file exists append
if|if
condition|(
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Append
operator|&&
operator|(
operator|(
name|getTempPrefix
argument_list|()
operator|!=
literal|null
operator|)
operator|||
operator|(
name|getTempFileName
argument_list|()
operator|!=
literal|null
operator|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You cannot set both fileExist=Append and tempPrefix/tempFileName options"
argument_list|)
throw|;
block|}
comment|// ensure fileExist and moveExisting is configured correctly if in use
if|if
condition|(
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Move
operator|&&
name|getMoveExisting
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You must configure moveExisting option when fileExist=Move"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|getMoveExisting
argument_list|()
operator|!=
literal|null
operator|&&
name|getFileExist
argument_list|()
operator|!=
name|GenericFileExist
operator|.
name|Move
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You must configure fileExist=Move when moveExisting has been set"
argument_list|)
throw|;
block|}
if|if
condition|(
name|this
operator|.
name|getMoveExistingFileStrategy
argument_list|()
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|setMoveExistingFileStrategy
argument_list|(
name|createDefaultMoveExistingFileStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|GenericFileProducer
argument_list|<>
argument_list|(
name|this
argument_list|,
name|operations
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange (GenericFile<File> file)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|file
operator|.
name|bindToExchange
argument_list|(
name|exchange
argument_list|,
name|probeContentType
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
comment|/**      * Strategy to create a new {@link FileConsumer}      *      * @param processor  the given processor      * @param operations file operations      * @return the created consumer      */
DECL|method|newFileConsumer (Processor processor, GenericFileOperations<File> operations)
specifier|protected
name|FileConsumer
name|newFileConsumer
parameter_list|(
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
return|return
operator|new
name|FileConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|operations
argument_list|,
name|processStrategy
operator|!=
literal|null
condition|?
name|processStrategy
else|:
name|createGenericFileStrategy
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Default Existing File Move Strategy      * @return the default implementation for file component      */
DECL|method|createDefaultMoveExistingFileStrategy ()
specifier|private
name|FileMoveExistingStrategy
name|createDefaultMoveExistingFileStrategy
parameter_list|()
block|{
return|return
operator|new
name|GenericFileDefaultMoveExistingFileStrategy
argument_list|()
return|;
block|}
DECL|method|getFile ()
specifier|public
name|File
name|getFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
comment|/**      * The starting directory      */
DECL|method|setFile (File file)
specifier|public
name|void
name|setFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
comment|// update configuration as well
name|getConfiguration
argument_list|()
operator|.
name|setDirectory
argument_list|(
name|FileUtil
operator|.
name|isAbsolute
argument_list|(
name|file
argument_list|)
condition|?
name|file
operator|.
name|getAbsolutePath
argument_list|()
else|:
name|file
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getScheme ()
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
literal|"file"
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
name|getFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getFileSeparator ()
specifier|public
name|char
name|getFileSeparator
parameter_list|()
block|{
return|return
name|File
operator|.
name|separatorChar
return|;
block|}
annotation|@
name|Override
DECL|method|isAbsolute (String name)
specifier|public
name|boolean
name|isAbsolute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// relative or absolute path?
return|return
name|FileUtil
operator|.
name|isAbsolute
argument_list|(
operator|new
name|File
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isCopyAndDeleteOnRenameFail ()
specifier|public
name|boolean
name|isCopyAndDeleteOnRenameFail
parameter_list|()
block|{
return|return
name|copyAndDeleteOnRenameFail
return|;
block|}
comment|/**      * Whether to fallback and do a copy and delete file, in case the file could not be renamed directly. This option is not available for the FTP component.      */
DECL|method|setCopyAndDeleteOnRenameFail (boolean copyAndDeleteOnRenameFail)
specifier|public
name|void
name|setCopyAndDeleteOnRenameFail
parameter_list|(
name|boolean
name|copyAndDeleteOnRenameFail
parameter_list|)
block|{
name|this
operator|.
name|copyAndDeleteOnRenameFail
operator|=
name|copyAndDeleteOnRenameFail
expr_stmt|;
block|}
DECL|method|isRenameUsingCopy ()
specifier|public
name|boolean
name|isRenameUsingCopy
parameter_list|()
block|{
return|return
name|renameUsingCopy
return|;
block|}
comment|/**      * Perform rename operations using a copy and delete strategy.      * This is primarily used in environments where the regular rename operation is unreliable (e.g. across different file systems or networks).      * This option takes precedence over the copyAndDeleteOnRenameFail parameter that will automatically fall back to the copy and delete strategy,      * but only after additional delays.      */
DECL|method|setRenameUsingCopy (boolean renameUsingCopy)
specifier|public
name|void
name|setRenameUsingCopy
parameter_list|(
name|boolean
name|renameUsingCopy
parameter_list|)
block|{
name|this
operator|.
name|renameUsingCopy
operator|=
name|renameUsingCopy
expr_stmt|;
block|}
DECL|method|isStartingDirectoryMustExist ()
specifier|public
name|boolean
name|isStartingDirectoryMustExist
parameter_list|()
block|{
return|return
name|startingDirectoryMustExist
return|;
block|}
comment|/**      * Whether the starting directory must exist. Mind that the autoCreate option is default enabled,      * which means the starting directory is normally auto created if it doesn't exist.      * You can disable autoCreate and enable this to ensure the starting directory must exist. Will thrown an exception if the directory doesn't exist.      */
DECL|method|setStartingDirectoryMustExist (boolean startingDirectoryMustExist)
specifier|public
name|void
name|setStartingDirectoryMustExist
parameter_list|(
name|boolean
name|startingDirectoryMustExist
parameter_list|)
block|{
name|this
operator|.
name|startingDirectoryMustExist
operator|=
name|startingDirectoryMustExist
expr_stmt|;
block|}
DECL|method|isStartingDirectoryMustHaveAccess ()
specifier|public
name|boolean
name|isStartingDirectoryMustHaveAccess
parameter_list|()
block|{
return|return
name|startingDirectoryMustHaveAccess
return|;
block|}
comment|/**      * Whether the starting directory has access permissions. Mind that the      * startingDirectoryMustExist parameter must be set to true in order to verify that the      * directory exists. Will thrown an exception if the directory doesn't have      * read and write permissions.      */
DECL|method|setStartingDirectoryMustHaveAccess (boolean startingDirectoryMustHaveAccess)
specifier|public
name|void
name|setStartingDirectoryMustHaveAccess
parameter_list|(
name|boolean
name|startingDirectoryMustHaveAccess
parameter_list|)
block|{
name|this
operator|.
name|startingDirectoryMustHaveAccess
operator|=
name|startingDirectoryMustHaveAccess
expr_stmt|;
block|}
DECL|method|isDirectoryMustExist ()
specifier|public
name|boolean
name|isDirectoryMustExist
parameter_list|()
block|{
return|return
name|directoryMustExist
return|;
block|}
comment|/**      * Similar to the startingDirectoryMustExist option but this applies during polling (after starting the consumer).      */
DECL|method|setDirectoryMustExist (boolean directoryMustExist)
specifier|public
name|void
name|setDirectoryMustExist
parameter_list|(
name|boolean
name|directoryMustExist
parameter_list|)
block|{
name|this
operator|.
name|directoryMustExist
operator|=
name|directoryMustExist
expr_stmt|;
block|}
DECL|method|isForceWrites ()
specifier|public
name|boolean
name|isForceWrites
parameter_list|()
block|{
return|return
name|forceWrites
return|;
block|}
comment|/**      * Whether to force syncing writes to the file system.      * You can turn this off if you do not want this level of guarantee, for example if writing to logs / audit logs etc; this would yield better performance.      */
DECL|method|setForceWrites (boolean forceWrites)
specifier|public
name|void
name|setForceWrites
parameter_list|(
name|boolean
name|forceWrites
parameter_list|)
block|{
name|this
operator|.
name|forceWrites
operator|=
name|forceWrites
expr_stmt|;
block|}
DECL|method|isProbeContentType ()
specifier|public
name|boolean
name|isProbeContentType
parameter_list|()
block|{
return|return
name|probeContentType
return|;
block|}
comment|/**      * Whether to enable probing of the content type. If enable then the consumer uses {@link Files#probeContentType(java.nio.file.Path)} to      * determine the content-type of the file, and store that as a header with key {@link Exchange#FILE_CONTENT_TYPE} on the {@link Message}.      */
DECL|method|setProbeContentType (boolean probeContentType)
specifier|public
name|void
name|setProbeContentType
parameter_list|(
name|boolean
name|probeContentType
parameter_list|)
block|{
name|this
operator|.
name|probeContentType
operator|=
name|probeContentType
expr_stmt|;
block|}
DECL|method|getExtendedAttributes ()
specifier|public
name|String
name|getExtendedAttributes
parameter_list|()
block|{
return|return
name|extendedAttributes
return|;
block|}
comment|/**      * To define which file attributes of interest. Like posix:permissions,posix:owner,basic:lastAccessTime,      * it supports basic wildcard like posix:*, basic:lastAccessTime      */
DECL|method|setExtendedAttributes (String extendedAttributes)
specifier|public
name|void
name|setExtendedAttributes
parameter_list|(
name|String
name|extendedAttributes
parameter_list|)
block|{
name|this
operator|.
name|extendedAttributes
operator|=
name|extendedAttributes
expr_stmt|;
block|}
comment|/**      * Chmod value must be between 000 and 777; If there is a leading digit like in 0755 we will ignore it.      */
DECL|method|chmodPermissionsAreValid (String chmod)
specifier|public
name|boolean
name|chmodPermissionsAreValid
parameter_list|(
name|String
name|chmod
parameter_list|)
block|{
if|if
condition|(
name|chmod
operator|==
literal|null
operator|||
name|chmod
operator|.
name|length
argument_list|()
operator|<
literal|3
operator|||
name|chmod
operator|.
name|length
argument_list|()
operator|>
literal|4
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|permissionsString
init|=
name|chmod
operator|.
name|trim
argument_list|()
operator|.
name|substring
argument_list|(
name|chmod
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|)
decl_stmt|;
comment|// if 4 digits chop off leading one
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|permissionsString
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Character
name|c
init|=
name|permissionsString
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Character
operator|.
name|isDigit
argument_list|(
name|c
argument_list|)
operator|||
name|Integer
operator|.
name|parseInt
argument_list|(
name|c
operator|.
name|toString
argument_list|()
argument_list|)
operator|>
literal|7
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|getPermissions ()
specifier|public
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|getPermissions
parameter_list|()
block|{
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|permissions
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|chmod
argument_list|)
condition|)
block|{
return|return
name|permissions
return|;
block|}
name|String
name|chmodString
init|=
name|chmod
operator|.
name|substring
argument_list|(
name|chmod
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|)
decl_stmt|;
comment|// if 4 digits chop off leading one
name|Integer
name|ownerValue
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|chmodString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|Integer
name|groupValue
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|chmodString
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|Integer
name|othersValue
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|chmodString
operator|.
name|substring
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|ownerValue
operator|&
name|CHMOD_WRITE_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|OWNER_WRITE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|ownerValue
operator|&
name|CHMOD_READ_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|OWNER_READ
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|ownerValue
operator|&
name|CHMOD_EXECUTE_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|OWNER_EXECUTE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|groupValue
operator|&
name|CHMOD_WRITE_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|GROUP_WRITE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|groupValue
operator|&
name|CHMOD_READ_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|GROUP_READ
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|groupValue
operator|&
name|CHMOD_EXECUTE_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|GROUP_EXECUTE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|othersValue
operator|&
name|CHMOD_WRITE_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|OTHERS_WRITE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|othersValue
operator|&
name|CHMOD_READ_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|OTHERS_READ
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|othersValue
operator|&
name|CHMOD_EXECUTE_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|OTHERS_EXECUTE
argument_list|)
expr_stmt|;
block|}
return|return
name|permissions
return|;
block|}
DECL|method|getChmod ()
specifier|public
name|String
name|getChmod
parameter_list|()
block|{
return|return
name|chmod
return|;
block|}
comment|/**      * Specify the file permissions which is sent by the producer, the chmod value must be between 000 and 777;      * If there is a leading digit like in 0755 we will ignore it.      */
DECL|method|setChmod (String chmod)
specifier|public
name|void
name|setChmod
parameter_list|(
name|String
name|chmod
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|chmod
argument_list|)
operator|&&
name|chmodPermissionsAreValid
argument_list|(
name|chmod
argument_list|)
condition|)
block|{
name|this
operator|.
name|chmod
operator|=
name|chmod
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"chmod option ["
operator|+
name|chmod
operator|+
literal|"] is not valid"
argument_list|)
throw|;
block|}
block|}
DECL|method|getDirectoryPermissions ()
specifier|public
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|getDirectoryPermissions
parameter_list|()
block|{
name|Set
argument_list|<
name|PosixFilePermission
argument_list|>
name|permissions
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|chmodDirectory
argument_list|)
condition|)
block|{
return|return
name|permissions
return|;
block|}
name|String
name|chmodString
init|=
name|chmodDirectory
operator|.
name|substring
argument_list|(
name|chmodDirectory
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|)
decl_stmt|;
comment|// if 4 digits chop off leading one
name|Integer
name|ownerValue
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|chmodString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|Integer
name|groupValue
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|chmodString
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|Integer
name|othersValue
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|chmodString
operator|.
name|substring
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|ownerValue
operator|&
name|CHMOD_WRITE_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|OWNER_WRITE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|ownerValue
operator|&
name|CHMOD_READ_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|OWNER_READ
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|ownerValue
operator|&
name|CHMOD_EXECUTE_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|OWNER_EXECUTE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|groupValue
operator|&
name|CHMOD_WRITE_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|GROUP_WRITE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|groupValue
operator|&
name|CHMOD_READ_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|GROUP_READ
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|groupValue
operator|&
name|CHMOD_EXECUTE_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|GROUP_EXECUTE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|othersValue
operator|&
name|CHMOD_WRITE_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|OTHERS_WRITE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|othersValue
operator|&
name|CHMOD_READ_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|OTHERS_READ
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|othersValue
operator|&
name|CHMOD_EXECUTE_MASK
operator|)
operator|>
literal|0
condition|)
block|{
name|permissions
operator|.
name|add
argument_list|(
name|PosixFilePermission
operator|.
name|OTHERS_EXECUTE
argument_list|)
expr_stmt|;
block|}
return|return
name|permissions
return|;
block|}
DECL|method|getChmodDirectory ()
specifier|public
name|String
name|getChmodDirectory
parameter_list|()
block|{
return|return
name|chmodDirectory
return|;
block|}
comment|/**      * Specify the directory permissions used when the producer creates missing directories, the chmod value must be between 000 and 777;      * If there is a leading digit like in 0755 we will ignore it.      */
DECL|method|setChmodDirectory (String chmodDirectory)
specifier|public
name|void
name|setChmodDirectory
parameter_list|(
name|String
name|chmodDirectory
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|chmodDirectory
argument_list|)
operator|&&
name|chmodPermissionsAreValid
argument_list|(
name|chmodDirectory
argument_list|)
condition|)
block|{
name|this
operator|.
name|chmodDirectory
operator|=
name|chmodDirectory
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"chmodDirectory option ["
operator|+
name|chmodDirectory
operator|+
literal|"] is not valid"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

