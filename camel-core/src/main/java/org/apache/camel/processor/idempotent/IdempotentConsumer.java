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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Navigate
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
name|Processor
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
name|impl
operator|.
name|ServiceSupport
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
name|util
operator|.
name|ServiceHelper
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
comment|/**  * An implementation of the<a  * href="http://camel.apache.org/idempotent-consumer.html">Idempotent Consumer</a> pattern.  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|IdempotentConsumer
specifier|public
class|class
name|IdempotentConsumer
extends|extends
name|ServiceSupport
implements|implements
name|Processor
implements|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
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
name|IdempotentConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|messageIdExpression
specifier|private
specifier|final
name|Expression
name|messageIdExpression
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|idempotentRepository
specifier|private
specifier|final
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
name|idempotentRepository
decl_stmt|;
DECL|field|eager
specifier|private
specifier|final
name|boolean
name|eager
decl_stmt|;
DECL|method|IdempotentConsumer (Expression messageIdExpression, IdempotentRepository<String> idempotentRepository, boolean eager, Processor processor)
specifier|public
name|IdempotentConsumer
parameter_list|(
name|Expression
name|messageIdExpression
parameter_list|,
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
name|idempotentRepository
parameter_list|,
name|boolean
name|eager
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|messageIdExpression
operator|=
name|messageIdExpression
expr_stmt|;
name|this
operator|.
name|idempotentRepository
operator|=
name|idempotentRepository
expr_stmt|;
name|this
operator|.
name|eager
operator|=
name|eager
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
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
literal|"IdempotentConsumer["
operator|+
name|messageIdExpression
operator|+
literal|" -> "
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|String
name|messageId
init|=
name|messageIdExpression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|messageId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoMessageIdException
argument_list|(
name|exchange
argument_list|,
name|messageIdExpression
argument_list|)
throw|;
block|}
name|boolean
name|newKey
decl_stmt|;
if|if
condition|(
name|eager
condition|)
block|{
comment|// add the key to the repository
name|newKey
operator|=
name|idempotentRepository
operator|.
name|add
argument_list|(
name|messageId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// check if we alrady have the key
name|newKey
operator|=
operator|!
name|idempotentRepository
operator|.
name|contains
argument_list|(
name|messageId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|newKey
condition|)
block|{
comment|// we already have this key so its a duplicate message
name|onDuplicateMessage
argument_list|(
name|exchange
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// register our on completion callback
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|IdempotentOnCompletion
argument_list|(
name|idempotentRepository
argument_list|,
name|messageId
argument_list|,
name|eager
argument_list|)
argument_list|)
expr_stmt|;
comment|// process the exchange
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|next ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Processor
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|processor
operator|!=
literal|null
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getMessageIdExpression ()
specifier|public
name|Expression
name|getMessageIdExpression
parameter_list|()
block|{
return|return
name|messageIdExpression
return|;
block|}
DECL|method|getIdempotentRepository ()
specifier|public
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
name|getIdempotentRepository
parameter_list|()
block|{
return|return
name|idempotentRepository
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
comment|/**      * A strategy method to allow derived classes to overload the behaviour of      * processing a duplicate message      *       * @param exchange the exchange      * @param messageId the message ID of this exchange      */
DECL|method|onDuplicateMessage (Exchange exchange, String messageId)
specifier|protected
name|void
name|onDuplicateMessage
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
literal|"Ignoring duplicate message with id: "
operator|+
name|messageId
operator|+
literal|" for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

