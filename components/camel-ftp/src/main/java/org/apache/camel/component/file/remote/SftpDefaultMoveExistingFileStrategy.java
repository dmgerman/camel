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

begin_class
DECL|class|SftpDefaultMoveExistingFileStrategy
specifier|public
class|class
name|SftpDefaultMoveExistingFileStrategy
implements|implements
name|FileMoveExistingStrategy
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SftpDefaultMoveExistingFileStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Moves any existing file due fileExists=Move is in use.      */
annotation|@
name|Override
DECL|method|moveExistingFile (GenericFileEndpoint endpoint, GenericFileOperations operations, String fileName)
specifier|public
name|boolean
name|moveExistingFile
parameter_list|(
name|GenericFileEndpoint
name|endpoint
parameter_list|,
name|GenericFileOperations
name|operations
parameter_list|,
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
comment|// we only support relative paths for the ftp component, so dont provide any parent
name|String
name|parent
init|=
literal|null
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
comment|// we only support relative paths for the ftp component, so strip any leading paths
name|to
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|to
argument_list|)
expr_stmt|;
comment|// normalize accordingly to configuration
name|to
operator|=
operator|(
operator|(
name|SftpEndpoint
operator|)
name|endpoint
operator|)
operator|.
name|getConfiguration
argument_list|()
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
comment|// do we have a sub directory
name|String
name|dir
init|=
name|FileUtil
operator|.
name|onlyPath
argument_list|(
name|to
argument_list|)
decl_stmt|;
if|if
condition|(
name|dir
operator|!=
literal|null
condition|)
block|{
comment|// ensure directory exists
name|operations
operator|.
name|buildDirectory
argument_list|(
name|dir
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|// deal if there already exists a file
if|if
condition|(
name|operations
operator|.
name|existsFile
argument_list|(
name|to
argument_list|)
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isEagerDeleteTargetFile
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Deleting existing file: {}"
argument_list|,
name|to
argument_list|)
expr_stmt|;
name|operations
operator|.
name|deleteFile
argument_list|(
name|to
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot move existing file from: "
operator|+
name|fileName
operator|+
literal|" to: "
operator|+
name|to
operator|+
literal|" as there already exists a file: "
operator|+
name|to
argument_list|)
throw|;
block|}
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Moving existing file: {} to: {}"
argument_list|,
name|fileName
argument_list|,
name|to
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|operations
operator|.
name|renameFile
argument_list|(
name|fileName
argument_list|,
name|to
argument_list|)
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
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

