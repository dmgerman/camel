begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote.strategy
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
operator|.
name|strategy
package|;
end_package

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
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|ChannelSftp
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
name|component
operator|.
name|file
operator|.
name|strategy
operator|.
name|GenericFileDeleteProcessStrategy
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
name|GenericFileExpressionRenamer
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
name|GenericFileNoOpProcessStrategy
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
name|GenericFileRenameExclusiveReadLockStrategy
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
name|GenericFileRenameProcessStrategy
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
DECL|class|SftpProcessStrategyFactory
specifier|public
specifier|final
class|class
name|SftpProcessStrategyFactory
block|{
DECL|method|SftpProcessStrategyFactory ()
specifier|private
name|SftpProcessStrategyFactory
parameter_list|()
block|{     }
DECL|method|createGenericFileProcessStrategy (CamelContext context, Map<String, Object> params)
specifier|public
specifier|static
name|GenericFileProcessStrategy
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
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
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|strategy
init|=
operator|new
name|GenericFileDeleteProcessStrategy
argument_list|<>
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
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|renamer
init|=
operator|new
name|GenericFileExpressionRenamer
argument_list|<>
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
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|renamer
init|=
operator|new
name|GenericFileExpressionRenamer
argument_list|<>
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
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|strategy
init|=
operator|new
name|GenericFileRenameProcessStrategy
argument_list|<>
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
operator|&&
name|moveExpression
operator|!=
literal|null
condition|)
block|{
comment|// move on commit is only possible if not noop
name|GenericFileExpressionRenamer
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|renamer
init|=
operator|new
name|GenericFileExpressionRenamer
argument_list|<>
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
comment|// both move and noop supports pre move
if|if
condition|(
name|moveFailedExpression
operator|!=
literal|null
condition|)
block|{
name|GenericFileExpressionRenamer
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|renamer
init|=
operator|new
name|GenericFileExpressionRenamer
argument_list|<>
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
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|renamer
init|=
operator|new
name|GenericFileExpressionRenamer
argument_list|<>
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
return|return
name|strategy
return|;
block|}
else|else
block|{
comment|// default strategy will do nothing
name|GenericFileNoOpProcessStrategy
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|strategy
init|=
operator|new
name|GenericFileNoOpProcessStrategy
argument_list|<>
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
return|return
name|strategy
return|;
block|}
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
name|ChannelSftp
operator|.
name|LsEntry
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
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|strategy
init|=
operator|(
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
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
literal|"rename"
operator|.
name|equals
argument_list|(
name|readLock
argument_list|)
condition|)
block|{
name|GenericFileRenameExclusiveReadLockStrategy
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|readLockStrategy
init|=
operator|new
name|GenericFileRenameExclusiveReadLockStrategy
argument_list|<>
argument_list|()
decl_stmt|;
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
name|readLockStrategy
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
name|readLockStrategy
operator|.
name|setCheckInterval
argument_list|(
name|checkInterval
argument_list|)
expr_stmt|;
block|}
return|return
name|readLockStrategy
return|;
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
name|SftpChangedExclusiveReadLockStrategy
name|readLockStrategy
init|=
operator|new
name|SftpChangedExclusiveReadLockStrategy
argument_list|()
decl_stmt|;
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
name|readLockStrategy
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
name|readLockStrategy
operator|.
name|setCheckInterval
argument_list|(
name|checkInterval
argument_list|)
expr_stmt|;
block|}
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
name|Boolean
name|fastExistsCheck
init|=
operator|(
name|Boolean
operator|)
name|params
operator|.
name|get
argument_list|(
literal|"fastExistsCheck"
argument_list|)
decl_stmt|;
if|if
condition|(
name|fastExistsCheck
operator|!=
literal|null
condition|)
block|{
name|readLockStrategy
operator|.
name|setFastExistsCheck
argument_list|(
name|fastExistsCheck
argument_list|)
expr_stmt|;
block|}
return|return
name|readLockStrategy
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

