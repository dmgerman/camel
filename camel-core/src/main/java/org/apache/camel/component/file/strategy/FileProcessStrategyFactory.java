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
name|util
operator|.
name|Map
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
name|CamelContext
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
name|LoggingLevel
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
name|spi
operator|.
name|IdempotentRepository
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
name|Language
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

begin_class
DECL|class|FileProcessStrategyFactory
specifier|public
specifier|final
class|class
name|FileProcessStrategyFactory
block|{
DECL|method|FileProcessStrategyFactory ()
specifier|private
name|FileProcessStrategyFactory
parameter_list|()
block|{     }
DECL|method|createGenericFileProcessStrategy (CamelContext context, Map<String, Object> params)
specifier|public
specifier|static
name|GenericFileProcessStrategy
argument_list|<
name|File
argument_list|>
name|createGenericFileProcessStrategy
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
parameter_list|)
block|{
comment|// We assume a value is present only if its value not null for String and 'true' for boolean
name|Expression
name|moveExpression
init|=
operator|(
name|Expression
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"move"
argument_list|)
decl_stmt|;
name|Expression
name|moveFailedExpression
init|=
operator|(
name|Expression
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"moveFailed"
argument_list|)
decl_stmt|;
name|Expression
name|preMoveExpression
init|=
operator|(
name|Expression
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"preMove"
argument_list|)
decl_stmt|;
name|boolean
name|isNoop
init|=
name|params
operator|.
name|get
argument_list|(
literal|"noop"
argument_list|)
operator|!=
literal|null
decl_stmt|;
name|boolean
name|isDelete
init|=
name|params
operator|.
name|get
argument_list|(
literal|"delete"
argument_list|)
operator|!=
literal|null
decl_stmt|;
name|boolean
name|isMove
init|=
name|moveExpression
operator|!=
literal|null
operator|||
name|preMoveExpression
operator|!=
literal|null
operator|||
name|moveFailedExpression
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
name|isDelete
condition|)
block|{
name|GenericFileDeleteProcessStrategy
argument_list|<
name|File
argument_list|>
name|strategy
init|=
operator|new
name|GenericFileDeleteProcessStrategy
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|strategy
operator|.
name|setExclusiveReadLockStrategy
argument_list|(
name|getExclusiveReadLockStrategy
argument_list|(
name|params
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|preMoveExpression
operator|!=
literal|null
condition|)
block|{
name|GenericFileExpressionRenamer
argument_list|<
name|File
argument_list|>
name|renamer
init|=
operator|new
name|GenericFileExpressionRenamer
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|renamer
operator|.
name|setExpression
argument_list|(
name|preMoveExpression
argument_list|)
expr_stmt|;
name|strategy
operator|.
name|setBeginRenamer
argument_list|(
name|renamer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|moveFailedExpression
operator|!=
literal|null
condition|)
block|{
name|GenericFileExpressionRenamer
argument_list|<
name|File
argument_list|>
name|renamer
init|=
operator|new
name|GenericFileExpressionRenamer
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|renamer
operator|.
name|setExpression
argument_list|(
name|moveFailedExpression
argument_list|)
expr_stmt|;
name|strategy
operator|.
name|setFailureRenamer
argument_list|(
name|renamer
argument_list|)
expr_stmt|;
block|}
return|return
name|strategy
return|;
block|}
elseif|else
if|if
condition|(
name|isMove
operator|||
name|isNoop
condition|)
block|{
name|GenericFileRenameProcessStrategy
argument_list|<
name|File
argument_list|>
name|strategy
init|=
operator|new
name|GenericFileRenameProcessStrategy
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|strategy
operator|.
name|setExclusiveReadLockStrategy
argument_list|(
name|getExclusiveReadLockStrategy
argument_list|(
name|params
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isNoop
condition|)
block|{
comment|// move on commit is only possible if not noop
if|if
condition|(
name|moveExpression
operator|!=
literal|null
condition|)
block|{
name|GenericFileExpressionRenamer
argument_list|<
name|File
argument_list|>
name|renamer
init|=
operator|new
name|GenericFileExpressionRenamer
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|renamer
operator|.
name|setExpression
argument_list|(
name|moveExpression
argument_list|)
expr_stmt|;
name|strategy
operator|.
name|setCommitRenamer
argument_list|(
name|renamer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|strategy
operator|.
name|setCommitRenamer
argument_list|(
name|getDefaultCommitRenamer
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// both move and noop supports pre move
if|if
condition|(
name|preMoveExpression
operator|!=
literal|null
condition|)
block|{
name|GenericFileExpressionRenamer
argument_list|<
name|File
argument_list|>
name|renamer
init|=
operator|new
name|GenericFileExpressionRenamer
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|renamer
operator|.
name|setExpression
argument_list|(
name|preMoveExpression
argument_list|)
expr_stmt|;
name|strategy
operator|.
name|setBeginRenamer
argument_list|(
name|renamer
argument_list|)
expr_stmt|;
block|}
comment|// both move and noop supports move failed
if|if
condition|(
name|moveFailedExpression
operator|!=
literal|null
condition|)
block|{
name|GenericFileExpressionRenamer
argument_list|<
name|File
argument_list|>
name|renamer
init|=
operator|new
name|GenericFileExpressionRenamer
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|renamer
operator|.
name|setExpression
argument_list|(
name|moveFailedExpression
argument_list|)
expr_stmt|;
name|strategy
operator|.
name|setFailureRenamer
argument_list|(
name|renamer
argument_list|)
expr_stmt|;
block|}
return|return
name|strategy
return|;
block|}
else|else
block|{
comment|// default strategy will move files in a .camel/ subfolder where the file was consumed
name|GenericFileRenameProcessStrategy
argument_list|<
name|File
argument_list|>
name|strategy
init|=
operator|new
name|GenericFileRenameProcessStrategy
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|strategy
operator|.
name|setExclusiveReadLockStrategy
argument_list|(
name|getExclusiveReadLockStrategy
argument_list|(
name|params
argument_list|)
argument_list|)
expr_stmt|;
name|strategy
operator|.
name|setCommitRenamer
argument_list|(
name|getDefaultCommitRenamer
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|strategy
return|;
block|}
block|}
DECL|method|getDefaultCommitRenamer (CamelContext context)
specifier|private
specifier|static
name|GenericFileExpressionRenamer
argument_list|<
name|File
argument_list|>
name|getDefaultCommitRenamer
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
comment|// use context to lookup language to let it be loose coupled
name|Language
name|language
init|=
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"file"
argument_list|)
decl_stmt|;
name|Expression
name|expression
init|=
name|language
operator|.
name|createExpression
argument_list|(
literal|"${file:parent}/.camel/${file:onlyname}"
argument_list|)
decl_stmt|;
return|return
operator|new
name|GenericFileExpressionRenamer
argument_list|<
name|File
argument_list|>
argument_list|(
name|expression
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getExclusiveReadLockStrategy (Map<String, Object> params)
specifier|private
specifier|static
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|File
argument_list|>
name|getExclusiveReadLockStrategy
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
parameter_list|)
block|{
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|File
argument_list|>
name|strategy
init|=
operator|(
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|File
argument_list|>
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"exclusiveReadLockStrategy"
argument_list|)
decl_stmt|;
if|if
condition|(
name|strategy
operator|!=
literal|null
condition|)
block|{
return|return
name|strategy
return|;
block|}
comment|// no explicit strategy set then fallback to readLock option
name|String
name|readLock
init|=
operator|(
name|String
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"readLock"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|readLock
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"none"
operator|.
name|equals
argument_list|(
name|readLock
argument_list|)
operator|||
literal|"false"
operator|.
name|equals
argument_list|(
name|readLock
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
literal|"markerFile"
operator|.
name|equals
argument_list|(
name|readLock
argument_list|)
condition|)
block|{
name|strategy
operator|=
operator|new
name|MarkerFileExclusiveReadLockStrategy
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"fileLock"
operator|.
name|equals
argument_list|(
name|readLock
argument_list|)
condition|)
block|{
name|strategy
operator|=
operator|new
name|FileLockExclusiveReadLockStrategy
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"rename"
operator|.
name|equals
argument_list|(
name|readLock
argument_list|)
condition|)
block|{
name|strategy
operator|=
operator|new
name|FileRenameExclusiveReadLockStrategy
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"changed"
operator|.
name|equals
argument_list|(
name|readLock
argument_list|)
condition|)
block|{
name|FileChangedExclusiveReadLockStrategy
name|readLockStrategy
init|=
operator|new
name|FileChangedExclusiveReadLockStrategy
argument_list|()
decl_stmt|;
name|Long
name|minLength
init|=
operator|(
name|Long
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"readLockMinLength"
argument_list|)
decl_stmt|;
if|if
condition|(
name|minLength
operator|!=
literal|null
condition|)
block|{
name|readLockStrategy
operator|.
name|setMinLength
argument_list|(
name|minLength
argument_list|)
expr_stmt|;
block|}
name|Long
name|minAge
init|=
operator|(
name|Long
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"readLockMinAge"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|minAge
condition|)
block|{
name|readLockStrategy
operator|.
name|setMinAge
argument_list|(
name|minAge
argument_list|)
expr_stmt|;
block|}
name|strategy
operator|=
name|readLockStrategy
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"idempotent"
operator|.
name|equals
argument_list|(
name|readLock
argument_list|)
condition|)
block|{
name|FileIdempotentRepositoryReadLockStrategy
name|readLockStrategy
init|=
operator|new
name|FileIdempotentRepositoryReadLockStrategy
argument_list|()
decl_stmt|;
name|Boolean
name|readLockRemoveOnRollback
init|=
operator|(
name|Boolean
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"readLockRemoveOnRollback"
argument_list|)
decl_stmt|;
if|if
condition|(
name|readLockRemoveOnRollback
operator|!=
literal|null
condition|)
block|{
name|readLockStrategy
operator|.
name|setRemoveOnRollback
argument_list|(
name|readLockRemoveOnRollback
argument_list|)
expr_stmt|;
block|}
name|Boolean
name|readLockRemoveOnCommit
init|=
operator|(
name|Boolean
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"readLockRemoveOnCommit"
argument_list|)
decl_stmt|;
if|if
condition|(
name|readLockRemoveOnCommit
operator|!=
literal|null
condition|)
block|{
name|readLockStrategy
operator|.
name|setRemoveOnCommit
argument_list|(
name|readLockRemoveOnCommit
argument_list|)
expr_stmt|;
block|}
name|IdempotentRepository
name|repo
init|=
operator|(
name|IdempotentRepository
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"readLockIdempotentRepository"
argument_list|)
decl_stmt|;
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
name|readLockStrategy
operator|.
name|setIdempotentRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
name|strategy
operator|=
name|readLockStrategy
expr_stmt|;
block|}
if|if
condition|(
name|strategy
operator|!=
literal|null
condition|)
block|{
name|Long
name|timeout
init|=
operator|(
name|Long
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"readLockTimeout"
argument_list|)
decl_stmt|;
if|if
condition|(
name|timeout
operator|!=
literal|null
condition|)
block|{
name|strategy
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
name|Long
name|checkInterval
init|=
operator|(
name|Long
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"readLockCheckInterval"
argument_list|)
decl_stmt|;
if|if
condition|(
name|checkInterval
operator|!=
literal|null
condition|)
block|{
name|strategy
operator|.
name|setCheckInterval
argument_list|(
name|checkInterval
argument_list|)
expr_stmt|;
block|}
name|LoggingLevel
name|readLockLoggingLevel
init|=
operator|(
name|LoggingLevel
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"readLockLoggingLevel"
argument_list|)
decl_stmt|;
if|if
condition|(
name|readLockLoggingLevel
operator|!=
literal|null
condition|)
block|{
name|strategy
operator|.
name|setReadLockLoggingLevel
argument_list|(
name|readLockLoggingLevel
argument_list|)
expr_stmt|;
block|}
name|Boolean
name|readLockMarkerFile
init|=
operator|(
name|Boolean
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"readLockMarkerFile"
argument_list|)
decl_stmt|;
if|if
condition|(
name|readLockMarkerFile
operator|!=
literal|null
condition|)
block|{
name|strategy
operator|.
name|setMarkerFiler
argument_list|(
name|readLockMarkerFile
argument_list|)
expr_stmt|;
block|}
name|Boolean
name|readLockDeleteOrphanLockFiles
init|=
operator|(
name|Boolean
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"readLockDeleteOrphanLockFiles"
argument_list|)
decl_stmt|;
if|if
condition|(
name|readLockDeleteOrphanLockFiles
operator|!=
literal|null
condition|)
block|{
name|strategy
operator|.
name|setDeleteOrphanLockFiles
argument_list|(
name|readLockDeleteOrphanLockFiles
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|strategy
return|;
block|}
block|}
end_class

end_unit

