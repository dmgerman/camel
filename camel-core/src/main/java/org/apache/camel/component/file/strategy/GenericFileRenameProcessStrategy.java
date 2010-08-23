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
name|GenericFileOperations
import|;
end_import

begin_class
DECL|class|GenericFileRenameProcessStrategy
specifier|public
class|class
name|GenericFileRenameProcessStrategy
parameter_list|<
name|T
parameter_list|>
extends|extends
name|GenericFileProcessStrategySupport
argument_list|<
name|T
argument_list|>
block|{
DECL|field|beginRenamer
specifier|private
name|GenericFileRenamer
argument_list|<
name|T
argument_list|>
name|beginRenamer
decl_stmt|;
DECL|field|failureRenamer
specifier|private
name|GenericFileRenamer
argument_list|<
name|T
argument_list|>
name|failureRenamer
decl_stmt|;
DECL|field|commitRenamer
specifier|private
name|GenericFileRenamer
argument_list|<
name|T
argument_list|>
name|commitRenamer
decl_stmt|;
DECL|method|GenericFileRenameProcessStrategy ()
specifier|public
name|GenericFileRenameProcessStrategy
parameter_list|()
block|{     }
annotation|@
name|Override
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
comment|// must invoke super
name|boolean
name|result
init|=
name|super
operator|.
name|begin
argument_list|(
name|operations
argument_list|,
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|beginRenamer
operator|!=
literal|null
condition|)
block|{
name|GenericFile
argument_list|<
name|T
argument_list|>
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
argument_list|<
name|T
argument_list|>
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
if|if
condition|(
name|to
operator|!=
literal|null
condition|)
block|{
name|to
operator|.
name|bindToExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
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
comment|// must invoke super
name|super
operator|.
name|rollback
argument_list|(
name|operations
argument_list|,
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
if|if
condition|(
name|failureRenamer
operator|!=
literal|null
condition|)
block|{
comment|// create a copy and bind the file to the exchange to be used by the renamer to evaluate the file name
name|Exchange
name|copy
init|=
name|exchange
operator|.
name|copy
argument_list|()
decl_stmt|;
name|file
operator|.
name|bindToExchange
argument_list|(
name|copy
argument_list|)
expr_stmt|;
comment|// must preserve message id
name|copy
operator|.
name|getIn
argument_list|()
operator|.
name|setMessageId
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|copy
operator|.
name|setExchangeId
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|GenericFile
argument_list|<
name|T
argument_list|>
name|newName
init|=
name|failureRenamer
operator|.
name|renameFile
argument_list|(
name|copy
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
annotation|@
name|Override
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
comment|// must invoke super
name|super
operator|.
name|commit
argument_list|(
name|operations
argument_list|,
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|)
expr_stmt|;
if|if
condition|(
name|commitRenamer
operator|!=
literal|null
condition|)
block|{
comment|// create a copy and bind the file to the exchange to be used by the renamer to evaluate the file name
name|Exchange
name|copy
init|=
name|exchange
operator|.
name|copy
argument_list|()
decl_stmt|;
name|file
operator|.
name|bindToExchange
argument_list|(
name|copy
argument_list|)
expr_stmt|;
comment|// must preserve message id
name|copy
operator|.
name|getIn
argument_list|()
operator|.
name|setMessageId
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|copy
operator|.
name|setExchangeId
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|GenericFile
argument_list|<
name|T
argument_list|>
name|newName
init|=
name|commitRenamer
operator|.
name|renameFile
argument_list|(
name|copy
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
DECL|method|getBeginRenamer ()
specifier|public
name|GenericFileRenamer
argument_list|<
name|T
argument_list|>
name|getBeginRenamer
parameter_list|()
block|{
return|return
name|beginRenamer
return|;
block|}
DECL|method|setBeginRenamer (GenericFileRenamer<T> beginRenamer)
specifier|public
name|void
name|setBeginRenamer
parameter_list|(
name|GenericFileRenamer
argument_list|<
name|T
argument_list|>
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
argument_list|<
name|T
argument_list|>
name|getCommitRenamer
parameter_list|()
block|{
return|return
name|commitRenamer
return|;
block|}
DECL|method|setCommitRenamer (GenericFileRenamer<T> commitRenamer)
specifier|public
name|void
name|setCommitRenamer
parameter_list|(
name|GenericFileRenamer
argument_list|<
name|T
argument_list|>
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
DECL|method|getFailureRenamer ()
specifier|public
name|GenericFileRenamer
argument_list|<
name|T
argument_list|>
name|getFailureRenamer
parameter_list|()
block|{
return|return
name|failureRenamer
return|;
block|}
DECL|method|setFailureRenamer (GenericFileRenamer<T> failureRenamer)
specifier|public
name|void
name|setFailureRenamer
parameter_list|(
name|GenericFileRenamer
argument_list|<
name|T
argument_list|>
name|failureRenamer
parameter_list|)
block|{
name|this
operator|.
name|failureRenamer
operator|=
name|failureRenamer
expr_stmt|;
block|}
block|}
end_class

end_unit

