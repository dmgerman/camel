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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|CamelContextAware
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
name|spi
operator|.
name|IdAware
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
name|AsyncProcessorConverterHelper
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
name|AsyncProcessorSupport
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
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * An implementation of the<a  * href="http://camel.apache.org/idempotent-consumer.html">Idempotent Consumer</a> pattern.  *<p/>  * This implementation supports idempotent repositories implemented as {@link org.apache.camel.spi.IdempotentRepository}.  *  * @see org.apache.camel.spi.IdempotentRepository  */
end_comment

begin_class
DECL|class|IdempotentConsumer
specifier|public
class|class
name|IdempotentConsumer
extends|extends
name|AsyncProcessorSupport
implements|implements
name|CamelContextAware
implements|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
implements|,
name|IdAware
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
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
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|field|idempotentRepository
specifier|private
specifier|final
name|IdempotentRepository
name|idempotentRepository
decl_stmt|;
DECL|field|eager
specifier|private
specifier|final
name|boolean
name|eager
decl_stmt|;
DECL|field|completionEager
specifier|private
specifier|final
name|boolean
name|completionEager
decl_stmt|;
DECL|field|skipDuplicate
specifier|private
specifier|final
name|boolean
name|skipDuplicate
decl_stmt|;
DECL|field|removeOnFailure
specifier|private
specifier|final
name|boolean
name|removeOnFailure
decl_stmt|;
DECL|field|duplicateMessageCount
specifier|private
specifier|final
name|AtomicLong
name|duplicateMessageCount
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|method|IdempotentConsumer (Expression messageIdExpression, IdempotentRepository idempotentRepository, boolean eager, boolean completionEager, boolean skipDuplicate, boolean removeOnFailure, Processor processor)
specifier|public
name|IdempotentConsumer
parameter_list|(
name|Expression
name|messageIdExpression
parameter_list|,
name|IdempotentRepository
name|idempotentRepository
parameter_list|,
name|boolean
name|eager
parameter_list|,
name|boolean
name|completionEager
parameter_list|,
name|boolean
name|skipDuplicate
parameter_list|,
name|boolean
name|removeOnFailure
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
name|completionEager
operator|=
name|completionEager
expr_stmt|;
name|this
operator|.
name|skipDuplicate
operator|=
name|skipDuplicate
expr_stmt|;
name|this
operator|.
name|removeOnFailure
operator|=
name|removeOnFailure
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
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
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
specifier|final
name|AsyncCallback
name|target
decl_stmt|;
specifier|final
name|String
name|messageId
decl_stmt|;
try|try
block|{
name|messageId
operator|=
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
expr_stmt|;
if|if
condition|(
name|messageId
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|NoMessageIdException
argument_list|(
name|exchange
argument_list|,
name|messageIdExpression
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
try|try
block|{
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
name|exchange
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// check if we already have the key
name|newKey
operator|=
operator|!
name|idempotentRepository
operator|.
name|contains
argument_list|(
name|exchange
argument_list|,
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
comment|// mark the exchange as duplicate
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|DUPLICATE_MESSAGE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// we already have this key so its a duplicate message
name|onDuplicate
argument_list|(
name|exchange
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
if|if
condition|(
name|skipDuplicate
condition|)
block|{
comment|// if we should skip duplicate then we are done
name|log
operator|.
name|debug
argument_list|(
literal|"Ignoring duplicate message with id: {} for exchange: {}"
argument_list|,
name|messageId
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
specifier|final
name|Synchronization
name|onCompletion
init|=
operator|new
name|IdempotentOnCompletion
argument_list|(
name|idempotentRepository
argument_list|,
name|messageId
argument_list|,
name|eager
argument_list|,
name|removeOnFailure
argument_list|)
decl_stmt|;
name|target
operator|=
operator|new
name|IdempotentConsumerCallback
argument_list|(
name|exchange
argument_list|,
name|onCompletion
argument_list|,
name|callback
argument_list|,
name|completionEager
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|completionEager
condition|)
block|{
comment|// the scope is to do the idempotent completion work as an unit of work on the exchange when its done being routed
name|exchange
operator|.
name|addOnCompletion
argument_list|(
name|onCompletion
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// process the exchange
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|target
argument_list|)
return|;
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
argument_list|<>
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
DECL|method|getDuplicateMessageCount ()
specifier|public
name|long
name|getDuplicateMessageCount
parameter_list|()
block|{
return|return
name|duplicateMessageCount
operator|.
name|get
argument_list|()
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
comment|// must add before start so it will have CamelContext injected first
if|if
condition|(
operator|!
name|camelContext
operator|.
name|hasService
argument_list|(
name|idempotentRepository
argument_list|)
condition|)
block|{
name|camelContext
operator|.
name|addService
argument_list|(
name|idempotentRepository
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|processor
argument_list|,
name|idempotentRepository
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
name|stopService
argument_list|(
name|processor
argument_list|,
name|idempotentRepository
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|processor
argument_list|,
name|idempotentRepository
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|removeService
argument_list|(
name|idempotentRepository
argument_list|)
expr_stmt|;
block|}
DECL|method|isEager ()
specifier|public
name|boolean
name|isEager
parameter_list|()
block|{
return|return
name|eager
return|;
block|}
DECL|method|isCompletionEager ()
specifier|public
name|boolean
name|isCompletionEager
parameter_list|()
block|{
return|return
name|completionEager
return|;
block|}
DECL|method|isSkipDuplicate ()
specifier|public
name|boolean
name|isSkipDuplicate
parameter_list|()
block|{
return|return
name|skipDuplicate
return|;
block|}
DECL|method|isRemoveOnFailure ()
specifier|public
name|boolean
name|isRemoveOnFailure
parameter_list|()
block|{
return|return
name|removeOnFailure
return|;
block|}
comment|/**      * Resets the duplicate message counter to<code>0L</code>.      */
DECL|method|resetDuplicateMessageCount ()
specifier|public
name|void
name|resetDuplicateMessageCount
parameter_list|()
block|{
name|duplicateMessageCount
operator|.
name|set
argument_list|(
literal|0L
argument_list|)
expr_stmt|;
block|}
DECL|method|onDuplicate (Exchange exchange, String messageId)
specifier|private
name|void
name|onDuplicate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|messageId
parameter_list|)
block|{
name|duplicateMessageCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|onDuplicateMessage
argument_list|(
name|exchange
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
comment|/**      * Clear the idempotent repository      */
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|idempotentRepository
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * A strategy method to allow derived classes to overload the behaviour of      * processing a duplicate message      *      * @param exchange  the exchange      * @param messageId the message ID of this exchange      */
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
comment|// noop
block|}
comment|/**      * {@link org.apache.camel.AsyncCallback} that is invoked when the idempotent consumer block ends      */
DECL|class|IdempotentConsumerCallback
specifier|private
specifier|static
class|class
name|IdempotentConsumerCallback
implements|implements
name|AsyncCallback
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|onCompletion
specifier|private
specifier|final
name|Synchronization
name|onCompletion
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|field|completionEager
specifier|private
specifier|final
name|boolean
name|completionEager
decl_stmt|;
DECL|method|IdempotentConsumerCallback (Exchange exchange, Synchronization onCompletion, AsyncCallback callback, boolean completionEager)
name|IdempotentConsumerCallback
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Synchronization
name|onCompletion
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|boolean
name|completionEager
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|onCompletion
operator|=
name|onCompletion
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|completionEager
operator|=
name|completionEager
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|done (boolean doneSync)
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|completionEager
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|isFailed
argument_list|()
condition|)
block|{
name|onCompletion
operator|.
name|onFailure
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|onCompletion
operator|.
name|onComplete
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if completion is not eager then the onCompletion is invoked as part of the UoW of the Exchange
block|}
finally|finally
block|{
name|callback
operator|.
name|done
argument_list|(
name|doneSync
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
literal|"IdempotentConsumerCallback"
return|;
block|}
block|}
block|}
end_class

end_unit

