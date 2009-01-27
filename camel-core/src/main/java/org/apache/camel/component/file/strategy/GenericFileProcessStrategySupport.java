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
DECL|field|exclusiveReadLockStrategy
specifier|private
name|GenericFileExclusiveReadLockStrategy
name|exclusiveReadLockStrategy
decl_stmt|;
DECL|method|begin (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, GenericFileExchange<T> exchange, GenericFile<T> file)
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
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
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
comment|// is we use excluse read then acquire the exclusive read (waiting until we got it)
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
comment|// do not begin sice we could not get the exclusive read lcok
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|commit (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, GenericFileExchange<T> exchange, GenericFile<T> file)
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
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
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
block|}
DECL|method|rollback (GenericFileOperations<T> operations, GenericFileEndpoint<T> endpoint, GenericFileExchange<T> exchange, GenericFile<T> file)
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
name|GenericFileExchange
argument_list|<
name|T
argument_list|>
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
block|}
DECL|method|getExclusiveReadLockStrategy ()
specifier|public
name|GenericFileExclusiveReadLockStrategy
name|getExclusiveReadLockStrategy
parameter_list|()
block|{
return|return
name|exclusiveReadLockStrategy
return|;
block|}
DECL|method|setExclusiveReadLockStrategy (GenericFileExclusiveReadLockStrategy exclusiveReadLockStrategy)
specifier|public
name|void
name|setExclusiveReadLockStrategy
parameter_list|(
name|GenericFileExclusiveReadLockStrategy
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
block|}
end_class

end_unit

