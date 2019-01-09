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
name|Collections
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
name|concurrent
operator|.
name|locks
operator|.
name|Lock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|locks
operator|.
name|ReentrantLock
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
name|CamelExchangeException
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
name|Expression
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
name|DefaultExchange
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
name|DefaultProducer
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
name|LRUCacheFactory
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
name|ServiceHelper
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
name|StringHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Generic file producer  */
end_comment

begin_class
DECL|class|GenericFileProducer
specifier|public
class|class
name|GenericFileProducer
parameter_list|<
name|T
parameter_list|>
extends|extends
name|DefaultProducer
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|protected
specifier|final
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
decl_stmt|;
DECL|field|operations
specifier|protected
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
decl_stmt|;
comment|// assume writing to 100 different files concurrently at most for the same file producer
DECL|field|locks
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Lock
argument_list|>
name|locks
init|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
name|LRUCacheFactory
operator|.
name|newLRUCache
argument_list|(
literal|100
argument_list|)
argument_list|)
decl_stmt|;
DECL|method|GenericFileProducer (GenericFileEndpoint<T> endpoint, GenericFileOperations<T> operations)
specifier|protected
name|GenericFileProducer
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|operations
operator|=
name|operations
expr_stmt|;
block|}
DECL|method|getFileSeparator ()
specifier|public
name|String
name|getFileSeparator
parameter_list|()
block|{
return|return
name|File
operator|.
name|separator
return|;
block|}
DECL|method|normalizePath (String name)
specifier|public
name|String
name|normalizePath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// store any existing file header which we want to keep and propagate
specifier|final
name|String
name|existing
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// create the target file name
name|String
name|target
init|=
name|createFileName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// use lock for same file name to avoid concurrent writes to the same file
comment|// for example when you concurrently append to the same file
name|Lock
name|lock
init|=
name|locks
operator|.
name|computeIfAbsent
argument_list|(
name|target
argument_list|,
name|f
lambda|->
operator|new
name|ReentrantLock
argument_list|()
argument_list|)
decl_stmt|;
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|processExchange
argument_list|(
name|exchange
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// do not remove as the locks cache has an upper bound
comment|// this ensure the locks is appropriate reused
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// and remove the write file name header as we only want to use it once (by design)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|OVERRULE_FILE_NAME
argument_list|)
expr_stmt|;
comment|// and restore existing file name
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|existing
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sets the operations to be used.      *<p/>      * Can be used to set a fresh operations in case of recovery attempts      *      * @param operations the operations      */
DECL|method|setOperations (GenericFileOperations<T> operations)
specifier|public
name|void
name|setOperations
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|)
block|{
name|this
operator|.
name|operations
operator|=
name|operations
expr_stmt|;
block|}
comment|/**      * Perform the work to process the fileExchange      *      * @param exchange fileExchange      * @param target   the target filename      * @throws Exception is thrown if some error      */
DECL|method|processExchange (Exchange exchange, String target)
specifier|protected
name|void
name|processExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|target
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing file: {} for exchange: {}"
argument_list|,
name|target
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
try|try
block|{
name|preWriteCheck
argument_list|()
expr_stmt|;
comment|// should we write to a temporary name and then afterwards rename to real target
name|boolean
name|writeAsTempAndRename
init|=
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getTempFileName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|tempTarget
init|=
literal|null
decl_stmt|;
comment|// remember if target exists to avoid checking twice
name|Boolean
name|targetExists
decl_stmt|;
if|if
condition|(
name|writeAsTempAndRename
condition|)
block|{
comment|// compute temporary name with the temp prefix
name|tempTarget
operator|=
name|createTempFileName
argument_list|(
name|exchange
argument_list|,
name|target
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Writing using tempNameFile: {}"
argument_list|,
name|tempTarget
argument_list|)
expr_stmt|;
comment|//if we should eager delete target file before deploying temporary file
if|if
condition|(
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|!=
name|GenericFileExist
operator|.
name|TryRename
operator|&&
name|endpoint
operator|.
name|isEagerDeleteTargetFile
argument_list|()
condition|)
block|{
comment|// cater for file exists option on the real target as
comment|// the file operations code will work on the temp file
comment|// if an existing file already exists what should we do?
name|targetExists
operator|=
name|operations
operator|.
name|existsFile
argument_list|(
name|target
argument_list|)
expr_stmt|;
if|if
condition|(
name|targetExists
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"EagerDeleteTargetFile, target exists"
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Ignore
condition|)
block|{
comment|// ignore but indicate that the file was written
name|log
operator|.
name|trace
argument_list|(
literal|"An existing file already exists: {}. Ignore and do not override it."
argument_list|,
name|target
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Fail
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"File already exist: "
operator|+
name|target
operator|+
literal|". Cannot write new file."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Move
condition|)
block|{
comment|// move any existing file first
name|doMoveExistingFile
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|isEagerDeleteTargetFile
argument_list|()
operator|&&
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Override
condition|)
block|{
comment|// we override the target so we do this by deleting it so the temp file can be renamed later
comment|// with success as the existing target file have been deleted
name|log
operator|.
name|trace
argument_list|(
literal|"Eagerly deleting existing file: {}"
argument_list|,
name|target
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|operations
operator|.
name|deleteFile
argument_list|(
name|target
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot delete file: "
operator|+
name|target
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|// delete any pre existing temp file
if|if
condition|(
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|!=
name|GenericFileExist
operator|.
name|TryRename
operator|&&
name|operations
operator|.
name|existsFile
argument_list|(
name|tempTarget
argument_list|)
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Deleting existing temp file: {}"
argument_list|,
name|tempTarget
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|operations
operator|.
name|deleteFile
argument_list|(
name|tempTarget
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot delete file: "
operator|+
name|tempTarget
argument_list|)
throw|;
block|}
block|}
block|}
comment|// write/upload the file
name|writeFile
argument_list|(
name|exchange
argument_list|,
name|tempTarget
operator|!=
literal|null
condition|?
name|tempTarget
else|:
name|target
argument_list|)
expr_stmt|;
comment|// if we did write to a temporary name then rename it to the real
comment|// name after we have written the file
if|if
condition|(
name|tempTarget
operator|!=
literal|null
condition|)
block|{
comment|// if we did not eager delete the target file
if|if
condition|(
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|!=
name|GenericFileExist
operator|.
name|TryRename
operator|&&
operator|!
name|endpoint
operator|.
name|isEagerDeleteTargetFile
argument_list|()
condition|)
block|{
comment|// if an existing file already exists what should we do?
name|targetExists
operator|=
name|operations
operator|.
name|existsFile
argument_list|(
name|target
argument_list|)
expr_stmt|;
if|if
condition|(
name|targetExists
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Not using EagerDeleteTargetFile, target exists"
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Ignore
condition|)
block|{
comment|// ignore but indicate that the file was written
name|log
operator|.
name|trace
argument_list|(
literal|"An existing file already exists: {}. Ignore and do not override it."
argument_list|,
name|target
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Fail
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"File already exist: "
operator|+
name|target
operator|+
literal|". Cannot write new file."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getFileExist
argument_list|()
operator|==
name|GenericFileExist
operator|.
name|Override
condition|)
block|{
comment|// we override the target so we do this by deleting it so the temp file can be renamed later
comment|// with success as the existing target file have been deleted
name|log
operator|.
name|trace
argument_list|(
literal|"Deleting existing file: {}"
argument_list|,
name|target
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|operations
operator|.
name|deleteFile
argument_list|(
name|target
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot delete file: "
operator|+
name|target
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|// now we are ready to rename the temp file to the target file
name|log
operator|.
name|trace
argument_list|(
literal|"Renaming file: [{}] to: [{}]"
argument_list|,
name|tempTarget
argument_list|,
name|target
argument_list|)
expr_stmt|;
name|boolean
name|renamed
init|=
name|operations
operator|.
name|renameFile
argument_list|(
name|tempTarget
argument_list|,
name|target
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|renamed
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot rename file from: "
operator|+
name|tempTarget
operator|+
literal|" to: "
operator|+
name|target
argument_list|)
throw|;
block|}
block|}
comment|// any done file to write?
if|if
condition|(
name|endpoint
operator|.
name|getDoneFileName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|doneFileName
init|=
name|endpoint
operator|.
name|createDoneFileName
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|doneFileName
argument_list|,
literal|"doneFileName"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
comment|// create empty exchange with empty body to write as the done file
name|Exchange
name|empty
init|=
operator|new
name|DefaultExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|empty
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Writing done file: [{}]"
argument_list|,
name|doneFileName
argument_list|)
expr_stmt|;
comment|// delete any existing done file
if|if
condition|(
name|operations
operator|.
name|existsFile
argument_list|(
name|doneFileName
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|operations
operator|.
name|deleteFile
argument_list|(
name|doneFileName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot delete existing done file: "
operator|+
name|doneFileName
argument_list|)
throw|;
block|}
block|}
name|writeFile
argument_list|(
name|empty
argument_list|,
name|doneFileName
argument_list|)
expr_stmt|;
block|}
comment|// let's store the name we really used in the header, so end-users
comment|// can retrieve it
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_PRODUCED
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleFailedWrite
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|postWriteCheck
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|doMoveExistingFile (String fileName)
specifier|private
name|void
name|doMoveExistingFile
parameter_list|(
name|String
name|fileName
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
comment|// need to evaluate using a dummy and simulate the file first, to have access to all the file attributes
comment|// create a dummy exchange as Exchange is needed for expression evaluation
comment|// we support only the following 3 tokens.
name|Exchange
name|dummy
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|String
name|parent
init|=
name|FileUtil
operator|.
name|onlyPath
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|String
name|onlyName
init|=
name|FileUtil
operator|.
name|stripPath
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|dummy
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|dummy
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_ONLY
argument_list|,
name|onlyName
argument_list|)
expr_stmt|;
name|dummy
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_PARENT
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|String
name|to
init|=
name|endpoint
operator|.
name|getMoveExisting
argument_list|()
operator|.
name|evaluate
argument_list|(
name|dummy
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// we must normalize it (to avoid having both \ and / in the name which confuses java.io.File)
name|to
operator|=
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|to
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|to
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"moveExisting evaluated as empty String, cannot move existing file: "
operator|+
name|fileName
argument_list|)
throw|;
block|}
name|boolean
name|renamed
init|=
name|operations
operator|.
name|renameFile
argument_list|(
name|fileName
argument_list|,
name|to
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|renamed
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot rename file from: "
operator|+
name|fileName
operator|+
literal|" to: "
operator|+
name|to
argument_list|)
throw|;
block|}
block|}
comment|/**      * If we fail writing out a file, we will call this method. This hook is      * provided to disconnect from servers or clean up files we created (if needed).      */
DECL|method|handleFailedWrite (Exchange exchange, Exception exception)
specifier|public
name|void
name|handleFailedWrite
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Exception
name|exception
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
name|exception
throw|;
block|}
comment|/**      * Perform any actions that need to occur before we write such as connecting to an FTP server etc.      */
DECL|method|preWriteCheck ()
specifier|public
name|void
name|preWriteCheck
parameter_list|()
throws|throws
name|Exception
block|{
comment|// nothing needed to check
block|}
comment|/**      * Perform any actions that need to occur after we are done such as disconnecting.      */
DECL|method|postWriteCheck (Exchange exchange)
specifier|public
name|void
name|postWriteCheck
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// nothing needed to check
block|}
DECL|method|writeFile (Exchange exchange, String fileName)
specifier|public
name|void
name|writeFile
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|fileName
parameter_list|)
throws|throws
name|GenericFileOperationFailedException
block|{
comment|// build directory if auto create is enabled
if|if
condition|(
name|endpoint
operator|.
name|isAutoCreate
argument_list|()
condition|)
block|{
comment|// we must normalize it (to avoid having both \ and / in the name which confuses java.io.File)
name|String
name|name
init|=
name|FileUtil
operator|.
name|normalizePath
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
comment|// use java.io.File to compute the file path
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|String
name|directory
init|=
name|file
operator|.
name|getParent
argument_list|()
decl_stmt|;
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
if|if
condition|(
name|directory
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|operations
operator|.
name|buildDirectory
argument_list|(
name|directory
argument_list|,
name|absolute
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Cannot build directory [{}] (could be because of denied permissions)"
argument_list|,
name|directory
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// upload
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
literal|"About to write [{}] to [{}] from exchange [{}]"
argument_list|,
name|fileName
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|boolean
name|success
init|=
name|operations
operator|.
name|storeFile
argument_list|(
name|fileName
argument_list|,
name|exchange
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|success
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Error writing file ["
operator|+
name|fileName
operator|+
literal|"]"
argument_list|)
throw|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Wrote [{}] to [{}]"
argument_list|,
name|fileName
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createFileName (Exchange exchange)
specifier|public
name|String
name|createFileName
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|answer
decl_stmt|;
comment|// overrule takes precedence
name|Object
name|value
decl_stmt|;
name|Object
name|overrule
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|OVERRULE_FILE_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|overrule
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|overrule
operator|instanceof
name|Expression
condition|)
block|{
name|value
operator|=
name|overrule
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|overrule
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|value
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|)
expr_stmt|;
block|}
comment|// if we have an overrule then override the existing header to use the overrule computed name from this point forward
if|if
condition|(
name|overrule
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|String
operator|&&
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|,
literal|"simple"
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Simple expression: {} detected in header: {} of type String. This feature has been removed (see CAMEL-6748)."
argument_list|,
name|value
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|)
expr_stmt|;
block|}
comment|// expression support
name|Expression
name|expression
init|=
name|endpoint
operator|.
name|getFileName
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Expression
condition|)
block|{
name|expression
operator|=
operator|(
name|Expression
operator|)
name|value
expr_stmt|;
block|}
comment|// evaluate the name as a String from the value
name|String
name|name
decl_stmt|;
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Filename evaluated as expression: {}"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
name|name
operator|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|// flatten name
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|isFlatten
argument_list|()
condition|)
block|{
comment|// check for both windows and unix separators
name|int
name|pos
init|=
name|Math
operator|.
name|max
argument_list|(
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
argument_list|,
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|"\\"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|!=
operator|-
literal|1
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
comment|// compute path by adding endpoint starting directory
name|String
name|endpointPath
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDirectory
argument_list|()
decl_stmt|;
name|String
name|baseDir
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|endpointPath
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// Its a directory so we should use it as a base path for the filename
comment|// If the path isn't empty, we need to add a trailing / if it isn't already there
name|baseDir
operator|=
name|endpointPath
expr_stmt|;
name|boolean
name|trailingSlash
init|=
name|endpointPath
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
operator|||
name|endpointPath
operator|.
name|endsWith
argument_list|(
literal|"\\"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|trailingSlash
condition|)
block|{
name|baseDir
operator|+=
name|getFileSeparator
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|baseDir
operator|+
name|name
expr_stmt|;
block|}
else|else
block|{
comment|// use a generated filename if no name provided
name|answer
operator|=
name|baseDir
operator|+
name|endpoint
operator|.
name|getGeneratedFileName
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|isJailStartingDirectory
argument_list|()
condition|)
block|{
comment|// check for file must be within starting directory (need to compact first as the name can be using relative paths via ../ etc)
name|String
name|compatchAnswer
init|=
name|FileUtil
operator|.
name|compactPath
argument_list|(
name|answer
argument_list|)
decl_stmt|;
name|String
name|compatchBaseDir
init|=
name|FileUtil
operator|.
name|compactPath
argument_list|(
name|baseDir
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|compatchAnswer
operator|.
name|startsWith
argument_list|(
name|compatchBaseDir
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot write file with name: "
operator|+
name|compatchAnswer
operator|+
literal|" as the filename is jailed to the starting directory: "
operator|+
name|compatchBaseDir
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|needToNormalize
argument_list|()
condition|)
block|{
comment|// must normalize path to cater for Windows and other OS
name|answer
operator|=
name|normalizePath
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createTempFileName (Exchange exchange, String fileName)
specifier|public
name|String
name|createTempFileName
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|fileName
parameter_list|)
block|{
name|String
name|answer
init|=
name|fileName
decl_stmt|;
name|String
name|tempName
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// its a generated filename then add it to header so we can evaluate the expression
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
name|fileName
argument_list|)
argument_list|)
expr_stmt|;
name|tempName
operator|=
name|endpoint
operator|.
name|getTempFileName
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// and remove it again after evaluation
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tempName
operator|=
name|endpoint
operator|.
name|getTempFileName
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|// check for both windows and unix separators
name|int
name|pos
init|=
name|Math
operator|.
name|max
argument_list|(
name|answer
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
argument_list|,
name|answer
operator|.
name|lastIndexOf
argument_list|(
literal|"\\"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|==
operator|-
literal|1
condition|)
block|{
comment|// no path so use temp name as calculated
name|answer
operator|=
name|tempName
expr_stmt|;
block|}
else|else
block|{
comment|// path should be prefixed before the temp name
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|answer
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
operator|+
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|tempName
argument_list|)
expr_stmt|;
name|answer
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|needToNormalize
argument_list|()
condition|)
block|{
comment|// must normalize path to cater for Windows and other OS
name|answer
operator|=
name|normalizePath
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
comment|// stack path in case the temporary file uses .. paths
name|answer
operator|=
name|FileUtil
operator|.
name|compactPath
argument_list|(
name|answer
argument_list|,
name|getFileSeparator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|locks
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|locks
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

