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
name|GenericFileOperations
import|;
end_import

begin_comment
comment|/**  * Acquires exclusive read lock to the given file. Will wait until the lock is granted.  * After granting the read lock it is released, we just want to make sure that when we start  * consuming the file its not currently in progress of being written by third party.  *<p/>  * This implementation is only supported by the File component, that leverages the {@link MarkerFileExclusiveReadLockStrategy}  * as well, to ensure only acquiring locks on files, which is not already in progress by another process,  * that have marked this using the marker file.  *<p/>  * Setting the option {@link #setMarkerFiler(boolean)} to<tt>false</tt> allows to turn off using marker files.  */
end_comment

begin_class
DECL|class|FileRenameExclusiveReadLockStrategy
specifier|public
class|class
name|FileRenameExclusiveReadLockStrategy
extends|extends
name|GenericFileRenameExclusiveReadLockStrategy
argument_list|<
name|File
argument_list|>
block|{
DECL|field|marker
specifier|private
name|MarkerFileExclusiveReadLockStrategy
name|marker
init|=
operator|new
name|MarkerFileExclusiveReadLockStrategy
argument_list|()
decl_stmt|;
DECL|field|markerFile
specifier|private
name|boolean
name|markerFile
init|=
literal|true
decl_stmt|;
annotation|@
name|Override
DECL|method|acquireExclusiveReadLock (GenericFileOperations<File> operations, GenericFile<File> file, Exchange exchange)
specifier|public
name|boolean
name|acquireExclusiveReadLock
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// must call marker first
if|if
condition|(
name|markerFile
operator|&&
operator|!
name|marker
operator|.
name|acquireExclusiveReadLock
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|super
operator|.
name|acquireExclusiveReadLock
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|releaseExclusiveReadLockOnAbort (GenericFileOperations<File> operations, GenericFile<File> file, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLockOnAbort
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// must call marker first
try|try
block|{
if|if
condition|(
name|markerFile
condition|)
block|{
name|marker
operator|.
name|releaseExclusiveReadLockOnAbort
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|super
operator|.
name|releaseExclusiveReadLockOnAbort
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|releaseExclusiveReadLockOnRollback (GenericFileOperations<File> operations, GenericFile<File> file, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLockOnRollback
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// must call marker first
try|try
block|{
if|if
condition|(
name|markerFile
condition|)
block|{
name|marker
operator|.
name|releaseExclusiveReadLockOnRollback
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|super
operator|.
name|releaseExclusiveReadLockOnRollback
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|releaseExclusiveReadLockOnCommit (GenericFileOperations<File> operations, GenericFile<File> file, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLockOnCommit
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// must call marker first
try|try
block|{
if|if
condition|(
name|markerFile
condition|)
block|{
name|marker
operator|.
name|releaseExclusiveReadLockOnCommit
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|super
operator|.
name|releaseExclusiveReadLockOnCommit
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|setMarkerFiler (boolean markerFile)
specifier|public
name|void
name|setMarkerFiler
parameter_list|(
name|boolean
name|markerFile
parameter_list|)
block|{
name|this
operator|.
name|markerFile
operator|=
name|markerFile
expr_stmt|;
block|}
block|}
end_class

end_unit
