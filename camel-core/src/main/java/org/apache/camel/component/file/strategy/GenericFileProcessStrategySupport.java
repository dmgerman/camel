begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.strategy
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
name|strategy
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
name|component
operator|.
name|file
operator|.
name|GenericFileEndpoint
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
name|GenericFileExclusiveReadLockStrategy
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
name|GenericFileOperationFailedException
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
name|GenericFileOperations
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
name|GenericFileProcessStrategy
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
comment|/**  * Base class for implementations of {@link GenericFileProcessStrategy}.  */
end_comment

begin_class
DECL|class|GenericFileProcessStrategySupport
specifier|public
specifier|abstract
class|class
name|GenericFileProcessStrategySupport
parameter_list|<
name|T
parameter_list|>
implements|implements
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
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
DECL|field|exclusiveReadLockStrategy
specifier|protected
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|T
argument_list|>
name|exclusiveReadLockStrategy
decl_stmt|;
DECL|method|prepareOnStartup (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint)
specifier|public
name|void
name|prepareOnStartup
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|exclusiveReadLockStrategy
operator|!=
literal|null
condition|)
block|{
name|exclusiveReadLockStrategy
operator|.
name|prepareOnStartup
argument_list|(
name|operations
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|begin (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, Exchange exchange, GenericFile<T> file)
specifier|public
name|boolean
name|begin
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
throws|throws
name|Exception
block|{
comment|// if we use exclusive read then acquire the exclusive read (waiting until we got it)
if|if
condition|(
name|exclusiveReadLockStrategy
operator|!=
literal|null
condition|)
block|{
name|boolean
name|lock
init|=
name|exclusiveReadLockStrategy
operator|.
name|acquireExclusiveReadLock
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|lock
condition|)
block|{
comment|// do not begin since we could not get the exclusive read lock
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|abort (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, Exchange exchange, GenericFile<T> file)
specifier|public
name|void
name|abort
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|exclusiveReadLockStrategy
operator|!=
literal|null
condition|)
block|{
name|exclusiveReadLockStrategy
operator|.
name|releaseExclusiveReadLock
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|deleteLocalWorkFile
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|commit (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, Exchange exchange, GenericFile<T> file)
specifier|public
name|void
name|commit
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|exclusiveReadLockStrategy
operator|!=
literal|null
condition|)
block|{
name|exclusiveReadLockStrategy
operator|.
name|releaseExclusiveReadLock
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|deleteLocalWorkFile
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|rollback (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, Exchange exchange, GenericFile<T> file)
specifier|public
name|void
name|rollback
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|exclusiveReadLockStrategy
operator|!=
literal|null
condition|)
block|{
name|exclusiveReadLockStrategy
operator|.
name|releaseExclusiveReadLock
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|deleteLocalWorkFile
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|getExclusiveReadLockStrategy ()
specifier|public
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|T
argument_list|>
name|getExclusiveReadLockStrategy
parameter_list|()
block|{
return|return
name|exclusiveReadLockStrategy
return|;
block|}
DECL|method|setExclusiveReadLockStrategy (GenericFileExclusiveReadLockStrategy<T> exclusiveReadLockStrategy)
specifier|public
name|void
name|setExclusiveReadLockStrategy
parameter_list|(
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|T
argument_list|>
name|exclusiveReadLockStrategy
parameter_list|)
block|{
name|this
operator|.
name|exclusiveReadLockStrategy
operator|=
name|exclusiveReadLockStrategy
expr_stmt|;
block|}
DECL|method|renameFile (GenericFileOperations<T> operations, GenericFile<T> from, GenericFile<T> to)
specifier|protected
name|GenericFile
argument_list|<
name|T
argument_list|>
name|renameFile
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|from
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|to
parameter_list|)
throws|throws
name|IOException
block|{
comment|// deleting any existing files before renaming
try|try
block|{
name|operations
operator|.
name|deleteFile
argument_list|(
name|to
operator|.
name|getAbsoluteFilePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GenericFileOperationFailedException
name|e
parameter_list|)
block|{
comment|// ignore the file does not exists
block|}
comment|// make parent folder if missing
name|boolean
name|mkdir
init|=
name|operations
operator|.
name|buildDirectory
argument_list|(
name|to
operator|.
name|getParent
argument_list|()
argument_list|,
name|to
operator|.
name|isAbsolute
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|mkdir
condition|)
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot create directory: "
operator|+
name|to
operator|.
name|getParent
argument_list|()
operator|+
literal|" (could be because of denied permissions)"
argument_list|)
throw|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Renaming file: {} to: {}"
argument_list|,
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
name|boolean
name|renamed
init|=
name|operations
operator|.
name|renameFile
argument_list|(
name|from
operator|.
name|getAbsoluteFilePath
argument_list|()
argument_list|,
name|to
operator|.
name|getAbsoluteFilePath
argument_list|()
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
literal|"Cannot rename file: "
operator|+
name|from
operator|+
literal|" to: "
operator|+
name|to
argument_list|)
throw|;
block|}
return|return
name|to
return|;
block|}
DECL|method|deleteLocalWorkFile (Exchange exchange)
specifier|private
name|void
name|deleteLocalWorkFile
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// delete local work file, if it was used (eg by ftp component)
name|File
name|local
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
name|FILE_LOCAL_WORK_PATH
argument_list|,
name|File
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|local
operator|!=
literal|null
operator|&&
name|local
operator|.
name|exists
argument_list|()
condition|)
block|{
name|boolean
name|deleted
init|=
name|FileUtil
operator|.
name|deleteFile
argument_list|(
name|local
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Local work file: {} was deleted: {}"
argument_list|,
name|local
argument_list|,
name|deleted
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

