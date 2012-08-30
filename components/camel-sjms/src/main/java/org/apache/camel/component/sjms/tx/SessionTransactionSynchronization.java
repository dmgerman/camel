begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.tx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|tx
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
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
name|sjms
operator|.
name|TransactionCommitStrategy
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
name|Synchronization
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
comment|/**  * SessionTransactionSynchronization is called at the completion of each {@link org.apache.camel.Exhcnage}.  */
end_comment

begin_class
DECL|class|SessionTransactionSynchronization
specifier|public
class|class
name|SessionTransactionSynchronization
implements|implements
name|Synchronization
block|{
DECL|field|log
specifier|private
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
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
DECL|field|commitStrategy
specifier|private
specifier|final
name|TransactionCommitStrategy
name|commitStrategy
decl_stmt|;
DECL|method|SessionTransactionSynchronization (Session session, TransactionCommitStrategy commitStrategy)
specifier|public
name|SessionTransactionSynchronization
parameter_list|(
name|Session
name|session
parameter_list|,
name|TransactionCommitStrategy
name|commitStrategy
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
if|if
condition|(
name|commitStrategy
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|commitStrategy
operator|=
operator|new
name|DefaultTransactionCommitStrategy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|commitStrategy
operator|=
name|commitStrategy
expr_stmt|;
block|}
block|}
comment|/**      * @see      * org.apache.camel.spi.Synchronization#onFailure(org.apache.camel.Exchange)      * @param exchange      */
annotation|@
name|Override
DECL|method|onFailure (Exchange exchange)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|commitStrategy
operator|.
name|rollback
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Processing failure of Exchange id:{}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|session
operator|!=
literal|null
operator|&&
name|session
operator|.
name|getTransacted
argument_list|()
condition|)
block|{
name|this
operator|.
name|session
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to rollback the session: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @see      * org.apache.camel.spi.Synchronization#onComplete(org.apache.camel.Exchange      * )      * @param exchange      */
annotation|@
name|Override
DECL|method|onComplete (Exchange exchange)
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|commitStrategy
operator|.
name|commit
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Processing completion of Exchange id:{}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|session
operator|!=
literal|null
operator|&&
name|session
operator|.
name|getTransacted
argument_list|()
condition|)
block|{
name|this
operator|.
name|session
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to commit the session: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

