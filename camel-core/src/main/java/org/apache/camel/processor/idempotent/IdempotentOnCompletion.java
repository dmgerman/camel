begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
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
name|Synchronization
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
name|support
operator|.
name|ExchangeHelper
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
comment|/**  * On completion strategy for {@link org.apache.camel.processor.idempotent.IdempotentConsumer}.  *<p/>  * This strategy adds the message id to the idempotent repository in cast the exchange  * was processed successfully. In case of failure the message id is<b>not</b> added.  */
end_comment

begin_class
DECL|class|IdempotentOnCompletion
specifier|public
class|class
name|IdempotentOnCompletion
implements|implements
name|Synchronization
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
name|IdempotentOnCompletion
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|idempotentRepository
specifier|private
specifier|final
name|IdempotentRepository
name|idempotentRepository
decl_stmt|;
DECL|field|messageId
specifier|private
specifier|final
name|String
name|messageId
decl_stmt|;
DECL|field|eager
specifier|private
specifier|final
name|boolean
name|eager
decl_stmt|;
DECL|field|removeOnFailure
specifier|private
specifier|final
name|boolean
name|removeOnFailure
decl_stmt|;
DECL|method|IdempotentOnCompletion (IdempotentRepository idempotentRepository, String messageId, boolean eager, boolean removeOnFailure)
specifier|public
name|IdempotentOnCompletion
parameter_list|(
name|IdempotentRepository
name|idempotentRepository
parameter_list|,
name|String
name|messageId
parameter_list|,
name|boolean
name|eager
parameter_list|,
name|boolean
name|removeOnFailure
parameter_list|)
block|{
name|this
operator|.
name|idempotentRepository
operator|=
name|idempotentRepository
expr_stmt|;
name|this
operator|.
name|messageId
operator|=
name|messageId
expr_stmt|;
name|this
operator|.
name|eager
operator|=
name|eager
expr_stmt|;
name|this
operator|.
name|removeOnFailure
operator|=
name|removeOnFailure
expr_stmt|;
block|}
DECL|method|onComplete (Exchange exchange)
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|ExchangeHelper
operator|.
name|isFailureHandled
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
comment|// the exchange did not process successfully but was failure handled by the dead letter channel
comment|// and thus moved to the dead letter queue. We should thus not consider it as complete.
name|onFailedMessage
argument_list|(
name|exchange
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|onCompletedMessage
argument_list|(
name|exchange
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onFailure (Exchange exchange)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|onFailedMessage
argument_list|(
name|exchange
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
comment|/**      * A strategy method to allow derived classes to overload the behavior of      * processing a completed message      *      * @param exchange  the exchange      * @param messageId the message ID of this exchange      */
DECL|method|onCompletedMessage (Exchange exchange, String messageId)
specifier|protected
name|void
name|onCompletedMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|messageId
parameter_list|)
block|{
if|if
condition|(
operator|!
name|eager
condition|)
block|{
comment|// if not eager we should add the key when its complete
name|idempotentRepository
operator|.
name|add
argument_list|(
name|exchange
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
name|idempotentRepository
operator|.
name|confirm
argument_list|(
name|exchange
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
comment|/**      * A strategy method to allow derived classes to overload the behavior of      * processing a failed message      *      * @param exchange  the exchange      * @param messageId the message ID of this exchange      */
DECL|method|onFailedMessage (Exchange exchange, String messageId)
specifier|protected
name|void
name|onFailedMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|messageId
parameter_list|)
block|{
if|if
condition|(
name|removeOnFailure
condition|)
block|{
name|idempotentRepository
operator|.
name|remove
argument_list|(
name|exchange
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Removed from repository as exchange failed: {} with id: {}"
argument_list|,
name|exchange
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"IdempotentOnCompletion["
operator|+
name|messageId
operator|+
literal|']'
return|;
block|}
block|}
end_class

end_unit

