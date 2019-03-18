begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|PlatformTransactionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|TransactionStatus
import|;
end_import

begin_class
DECL|class|NoopPlatformTransactionManager
specifier|public
class|class
name|NoopPlatformTransactionManager
implements|implements
name|PlatformTransactionManager
block|{
annotation|@
name|Override
DECL|method|getTransaction (TransactionDefinition definition)
specifier|public
name|TransactionStatus
name|getTransaction
parameter_list|(
name|TransactionDefinition
name|definition
parameter_list|)
throws|throws
name|TransactionException
block|{
return|return
operator|new
name|StubTransactionStatus
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|commit (TransactionStatus status)
specifier|public
name|void
name|commit
parameter_list|(
name|TransactionStatus
name|status
parameter_list|)
throws|throws
name|TransactionException
block|{
comment|// empty
block|}
annotation|@
name|Override
DECL|method|rollback (TransactionStatus status)
specifier|public
name|void
name|rollback
parameter_list|(
name|TransactionStatus
name|status
parameter_list|)
throws|throws
name|TransactionException
block|{
comment|// empty
block|}
DECL|class|StubTransactionStatus
specifier|protected
specifier|static
class|class
name|StubTransactionStatus
implements|implements
name|TransactionStatus
block|{
annotation|@
name|Override
DECL|method|isNewTransaction ()
specifier|public
name|boolean
name|isNewTransaction
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hasSavepoint ()
specifier|public
name|boolean
name|hasSavepoint
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|setRollbackOnly ()
specifier|public
name|void
name|setRollbackOnly
parameter_list|()
block|{
comment|// empty
block|}
annotation|@
name|Override
DECL|method|isRollbackOnly ()
specifier|public
name|boolean
name|isRollbackOnly
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|flush ()
specifier|public
name|void
name|flush
parameter_list|()
block|{
comment|// empty
block|}
annotation|@
name|Override
DECL|method|isCompleted ()
specifier|public
name|boolean
name|isCompleted
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|createSavepoint ()
specifier|public
name|Object
name|createSavepoint
parameter_list|()
throws|throws
name|TransactionException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|rollbackToSavepoint (Object savepoint)
specifier|public
name|void
name|rollbackToSavepoint
parameter_list|(
name|Object
name|savepoint
parameter_list|)
throws|throws
name|TransactionException
block|{
comment|// empty
block|}
annotation|@
name|Override
DECL|method|releaseSavepoint (Object savepoint)
specifier|public
name|void
name|releaseSavepoint
parameter_list|(
name|Object
name|savepoint
parameter_list|)
throws|throws
name|TransactionException
block|{
comment|// empty
block|}
block|}
block|}
end_class

end_unit

