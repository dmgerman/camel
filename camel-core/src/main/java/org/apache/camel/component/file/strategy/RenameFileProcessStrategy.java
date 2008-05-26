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
name|component
operator|.
name|file
operator|.
name|FileEndpoint
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
name|FileExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A strategy to rename a file  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|RenameFileProcessStrategy
specifier|public
class|class
name|RenameFileProcessStrategy
extends|extends
name|FileProcessStrategySupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|RenameFileProcessStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|renamer
specifier|private
name|FileRenamer
name|renamer
decl_stmt|;
DECL|method|RenameFileProcessStrategy ()
specifier|public
name|RenameFileProcessStrategy
parameter_list|()
block|{
name|this
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|RenameFileProcessStrategy (boolean lock)
specifier|public
name|RenameFileProcessStrategy
parameter_list|(
name|boolean
name|lock
parameter_list|)
block|{
name|this
argument_list|(
name|lock
argument_list|,
literal|".camel/"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
DECL|method|RenameFileProcessStrategy (boolean lock, String namePrefix, String namePostfix)
specifier|public
name|RenameFileProcessStrategy
parameter_list|(
name|boolean
name|lock
parameter_list|,
name|String
name|namePrefix
parameter_list|,
name|String
name|namePostfix
parameter_list|)
block|{
name|this
argument_list|(
name|lock
argument_list|,
operator|new
name|DefaultFileRenamer
argument_list|(
name|namePrefix
argument_list|,
name|namePostfix
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|RenameFileProcessStrategy (boolean lock, FileRenamer renamer)
specifier|public
name|RenameFileProcessStrategy
parameter_list|(
name|boolean
name|lock
parameter_list|,
name|FileRenamer
name|renamer
parameter_list|)
block|{
name|super
argument_list|(
name|lock
argument_list|)
expr_stmt|;
name|this
operator|.
name|renamer
operator|=
name|renamer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|commit (FileEndpoint endpoint, FileExchange exchange, File file)
specifier|public
name|void
name|commit
parameter_list|(
name|FileEndpoint
name|endpoint
parameter_list|,
name|FileExchange
name|exchange
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|newName
init|=
name|renamer
operator|.
name|renameFile
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Renaming file: "
operator|+
name|file
operator|+
literal|" to: "
operator|+
name|newName
argument_list|)
expr_stmt|;
block|}
comment|// deleting any existing files before renaming
if|if
condition|(
name|newName
operator|.
name|exists
argument_list|()
condition|)
block|{
name|newName
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
comment|// make parent folder if missing
name|newName
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|boolean
name|renamed
init|=
name|file
operator|.
name|renameTo
argument_list|(
name|newName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|renamed
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not rename file from: "
operator|+
name|file
operator|+
literal|" to "
operator|+
name|newName
argument_list|)
expr_stmt|;
block|}
comment|// must commit to release the lock
name|super
operator|.
name|commit
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
DECL|method|getRenamer ()
specifier|public
name|FileRenamer
name|getRenamer
parameter_list|()
block|{
return|return
name|renamer
return|;
block|}
DECL|method|setRenamer (FileRenamer renamer)
specifier|public
name|void
name|setRenamer
parameter_list|(
name|FileRenamer
name|renamer
parameter_list|)
block|{
name|this
operator|.
name|renamer
operator|=
name|renamer
expr_stmt|;
block|}
block|}
end_class

end_unit

