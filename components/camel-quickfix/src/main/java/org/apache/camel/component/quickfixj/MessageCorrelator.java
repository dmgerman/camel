begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfixj
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quickfixj
package|;
end_package

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
name|Callable
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
name|CopyOnWriteArrayList
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
name|CountDownLatch
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
name|TimeUnit
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
name|ExchangeTimedOutException
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|SessionID
import|;
end_import

begin_class
DECL|class|MessageCorrelator
specifier|public
class|class
name|MessageCorrelator
implements|implements
name|QuickfixjEventListener
block|{
DECL|field|DEFAULT_CORRELATION_TIMEOUT
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_CORRELATION_TIMEOUT
init|=
literal|1000L
decl_stmt|;
DECL|field|rules
specifier|private
specifier|final
name|List
argument_list|<
name|MessageCorrelationRule
argument_list|>
name|rules
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|MessageCorrelationRule
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|getReply (SessionID sessionID, Exchange exchange)
specifier|public
name|Callable
argument_list|<
name|Message
argument_list|>
name|getReply
parameter_list|(
name|SessionID
name|sessionID
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExchangeTimedOutException
block|{
name|MessagePredicate
name|messageCriteria
init|=
operator|(
name|MessagePredicate
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|QuickfixjProducer
operator|.
name|CORRELATION_CRITERIA_KEY
argument_list|)
decl_stmt|;
specifier|final
name|MessageCorrelationRule
name|correlationRule
init|=
operator|new
name|MessageCorrelationRule
argument_list|(
name|exchange
argument_list|,
name|sessionID
argument_list|,
name|messageCriteria
argument_list|)
decl_stmt|;
name|rules
operator|.
name|add
argument_list|(
name|correlationRule
argument_list|)
expr_stmt|;
specifier|final
name|long
name|timeout
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|QuickfixjProducer
operator|.
name|CORRELATION_TIMEOUT_KEY
argument_list|,
name|DEFAULT_CORRELATION_TIMEOUT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|new
name|Callable
argument_list|<
name|Message
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Message
name|call
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|correlationRule
operator|.
name|getLatch
argument_list|()
operator|.
name|await
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ExchangeTimedOutException
argument_list|(
name|correlationRule
operator|.
name|getExchange
argument_list|()
argument_list|,
name|timeout
argument_list|)
throw|;
block|}
return|return
name|correlationRule
operator|.
name|getReplyMessage
argument_list|()
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|onEvent (QuickfixjEventCategory eventCategory, SessionID sessionID, Message message)
specifier|public
name|void
name|onEvent
parameter_list|(
name|QuickfixjEventCategory
name|eventCategory
parameter_list|,
name|SessionID
name|sessionID
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|MessageCorrelationRule
name|rule
range|:
name|rules
control|)
block|{
if|if
condition|(
name|rule
operator|.
name|getMessageCriteria
argument_list|()
operator|.
name|evaluate
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|rule
operator|.
name|setReplyMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|rules
operator|.
name|remove
argument_list|(
name|rule
argument_list|)
expr_stmt|;
name|rule
operator|.
name|getLatch
argument_list|()
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|class|MessageCorrelationRule
specifier|private
specifier|static
class|class
name|MessageCorrelationRule
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|latch
specifier|private
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|messageCriteria
specifier|private
specifier|final
name|MessagePredicate
name|messageCriteria
decl_stmt|;
DECL|field|replyMessage
specifier|private
name|Message
name|replyMessage
decl_stmt|;
DECL|method|MessageCorrelationRule (Exchange exchange, SessionID sessionID, MessagePredicate messageCriteria)
specifier|public
name|MessageCorrelationRule
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SessionID
name|sessionID
parameter_list|,
name|MessagePredicate
name|messageCriteria
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
name|messageCriteria
operator|=
name|messageCriteria
expr_stmt|;
block|}
DECL|method|setReplyMessage (Message message)
specifier|public
name|void
name|setReplyMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|replyMessage
operator|=
name|message
expr_stmt|;
block|}
DECL|method|getReplyMessage ()
specifier|public
name|Message
name|getReplyMessage
parameter_list|()
block|{
return|return
name|replyMessage
return|;
block|}
DECL|method|getLatch ()
specifier|public
name|CountDownLatch
name|getLatch
parameter_list|()
block|{
return|return
name|latch
return|;
block|}
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
DECL|method|getMessageCriteria ()
specifier|public
name|MessagePredicate
name|getMessageCriteria
parameter_list|()
block|{
return|return
name|messageCriteria
return|;
block|}
block|}
block|}
end_class

end_unit

