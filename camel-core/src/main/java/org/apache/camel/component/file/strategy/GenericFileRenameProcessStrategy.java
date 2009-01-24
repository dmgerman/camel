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
name|GenericFileExchange
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

begin_class
DECL|class|GenericFileRenameProcessStrategy
specifier|public
class|class
name|GenericFileRenameProcessStrategy
extends|extends
name|GenericFileProcessStrategySupport
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
name|GenericFileRenameProcessStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|beginRenamer
specifier|private
name|GenericFileRenamer
name|beginRenamer
decl_stmt|;
DECL|field|commitRenamer
specifier|private
name|GenericFileRenamer
name|commitRenamer
decl_stmt|;
DECL|method|GenericFileRenameProcessStrategy ()
specifier|public
name|GenericFileRenameProcessStrategy
parameter_list|()
block|{     }
DECL|method|GenericFileRenameProcessStrategy (String namePrefix, String namePostfix)
specifier|public
name|GenericFileRenameProcessStrategy
parameter_list|(
name|String
name|namePrefix
parameter_list|,
name|String
name|namePostfix
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|GenericFileDefaultRenamer
argument_list|(
name|namePrefix
argument_list|,
name|namePostfix
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|GenericFileRenameProcessStrategy (String namePrefix, String namePostfix, String preNamePrefix, String preNamePostfix)
specifier|public
name|GenericFileRenameProcessStrategy
parameter_list|(
name|String
name|namePrefix
parameter_list|,
name|String
name|namePostfix
parameter_list|,
name|String
name|preNamePrefix
parameter_list|,
name|String
name|preNamePostfix
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|GenericFileDefaultRenamer
argument_list|(
name|namePrefix
argument_list|,
name|namePostfix
argument_list|)
argument_list|,
operator|new
name|GenericFileDefaultRenamer
argument_list|(
name|preNamePrefix
argument_list|,
name|preNamePostfix
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|GenericFileRenameProcessStrategy (GenericFileRenamer commitRenamer, GenericFileRenamer beginRenamer)
specifier|public
name|GenericFileRenameProcessStrategy
parameter_list|(
name|GenericFileRenamer
name|commitRenamer
parameter_list|,
name|GenericFileRenamer
name|beginRenamer
parameter_list|)
block|{
name|this
operator|.
name|commitRenamer
operator|=
name|commitRenamer
expr_stmt|;
name|this
operator|.
name|beginRenamer
operator|=
name|beginRenamer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|begin (GenericFileOperations operations, GenericFileEndpoint endpoint, GenericFileExchange exchange, GenericFile file)
specifier|public
name|boolean
name|begin
parameter_list|(
name|GenericFileOperations
name|operations
parameter_list|,
name|GenericFileEndpoint
name|endpoint
parameter_list|,
name|GenericFileExchange
name|exchange
parameter_list|,
name|GenericFile
name|file
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|beginRenamer
operator|!=
literal|null
condition|)
block|{
name|GenericFile
name|newName
init|=
name|beginRenamer
operator|.
name|renameFile
argument_list|(
name|exchange
argument_list|,
name|file
argument_list|)
decl_stmt|;
name|GenericFile
name|to
init|=
name|renameFile
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|newName
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setGenericFile
argument_list|(
name|to
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|commit (GenericFileOperations operations, GenericFileEndpoint endpoint, GenericFileExchange exchange, GenericFile file)
specifier|public
name|void
name|commit
parameter_list|(
name|GenericFileOperations
name|operations
parameter_list|,
name|GenericFileEndpoint
name|endpoint
parameter_list|,
name|GenericFileExchange
name|exchange
parameter_list|,
name|GenericFile
name|file
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|commitRenamer
operator|!=
literal|null
condition|)
block|{
name|GenericFile
name|newName
init|=
name|commitRenamer
operator|.
name|renameFile
argument_list|(
name|exchange
argument_list|,
name|file
argument_list|)
decl_stmt|;
name|renameFile
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|newName
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|renameFile (GenericFileOperations operations, GenericFile from, GenericFile to)
specifier|private
specifier|static
name|GenericFile
name|renameFile
parameter_list|(
name|GenericFileOperations
name|operations
parameter_list|,
name|GenericFile
name|from
parameter_list|,
name|GenericFile
name|to
parameter_list|)
throws|throws
name|IOException
block|{
comment|// deleting any existing files before renaming
name|boolean
name|deleted
init|=
literal|false
decl_stmt|;
try|try
block|{
name|deleted
operator|=
name|operations
operator|.
name|deleteFile
argument_list|(
name|to
operator|.
name|getAbsoluteFileName
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
if|if
condition|(
operator|!
name|deleted
condition|)
block|{
comment|// if we could not delete any existing file then maybe the folder is missing
comment|// build folder if needed
name|String
name|name
init|=
name|to
operator|.
name|getAbsoluteFileName
argument_list|()
decl_stmt|;
name|int
name|lastPathIndex
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastPathIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|directory
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastPathIndex
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|operations
operator|.
name|buildDirectory
argument_list|(
name|directory
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot build directory: "
operator|+
name|directory
operator|+
literal|" (maybe because of denied permissions)"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|from
operator|+
literal|" to: "
operator|+
name|to
argument_list|)
expr_stmt|;
block|}
name|boolean
name|renamed
init|=
name|operations
operator|.
name|renameFile
argument_list|(
name|from
operator|.
name|getAbsoluteFileName
argument_list|()
argument_list|,
name|to
operator|.
name|getAbsoluteFileName
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
DECL|method|getBeginRenamer ()
specifier|public
name|GenericFileRenamer
name|getBeginRenamer
parameter_list|()
block|{
return|return
name|beginRenamer
return|;
block|}
DECL|method|setBeginRenamer (GenericFileRenamer beginRenamer)
specifier|public
name|void
name|setBeginRenamer
parameter_list|(
name|GenericFileRenamer
name|beginRenamer
parameter_list|)
block|{
name|this
operator|.
name|beginRenamer
operator|=
name|beginRenamer
expr_stmt|;
block|}
DECL|method|getCommitRenamer ()
specifier|public
name|GenericFileRenamer
name|getCommitRenamer
parameter_list|()
block|{
return|return
name|commitRenamer
return|;
block|}
DECL|method|setCommitRenamer (GenericFileRenamer commitRenamer)
specifier|public
name|void
name|setCommitRenamer
parameter_list|(
name|GenericFileRenamer
name|commitRenamer
parameter_list|)
block|{
name|this
operator|.
name|commitRenamer
operator|=
name|commitRenamer
expr_stmt|;
block|}
block|}
end_class

end_unit

