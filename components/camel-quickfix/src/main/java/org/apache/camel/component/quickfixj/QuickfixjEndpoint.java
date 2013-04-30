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
name|CopyOnWriteArrayList
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
name|Component
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
name|Consumer
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
name|MultipleConsumersSupport
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
name|Producer
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
name|ResolveEndpointFailedException
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
name|quickfixj
operator|.
name|converter
operator|.
name|QuickfixjConverters
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
name|DefaultEndpoint
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
DECL|class|QuickfixjEndpoint
specifier|public
class|class
name|QuickfixjEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|QuickfixjEventListener
implements|,
name|MultipleConsumersSupport
block|{
DECL|field|EVENT_CATEGORY_KEY
specifier|public
specifier|static
specifier|final
name|String
name|EVENT_CATEGORY_KEY
init|=
literal|"EventCategory"
decl_stmt|;
DECL|field|SESSION_ID_KEY
specifier|public
specifier|static
specifier|final
name|String
name|SESSION_ID_KEY
init|=
literal|"SessionID"
decl_stmt|;
DECL|field|MESSAGE_TYPE_KEY
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_TYPE_KEY
init|=
literal|"MessageType"
decl_stmt|;
DECL|field|DATA_DICTIONARY_KEY
specifier|public
specifier|static
specifier|final
name|String
name|DATA_DICTIONARY_KEY
init|=
literal|"DataDictionary"
decl_stmt|;
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
name|QuickfixjEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|sessionID
specifier|private
name|SessionID
name|sessionID
decl_stmt|;
DECL|field|consumers
specifier|private
specifier|final
name|List
argument_list|<
name|QuickfixjConsumer
argument_list|>
name|consumers
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|QuickfixjConsumer
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|engine
specifier|private
specifier|final
name|QuickfixjEngine
name|engine
decl_stmt|;
annotation|@
name|Deprecated
DECL|method|QuickfixjEndpoint (QuickfixjEngine engine, String uri, CamelContext context)
specifier|public
name|QuickfixjEndpoint
parameter_list|(
name|QuickfixjEngine
name|engine
parameter_list|,
name|String
name|uri
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|engine
operator|=
name|engine
expr_stmt|;
block|}
DECL|method|QuickfixjEndpoint (QuickfixjEngine engine, String uri, Component component)
specifier|public
name|QuickfixjEndpoint
parameter_list|(
name|QuickfixjEngine
name|engine
parameter_list|,
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|engine
operator|=
name|engine
expr_stmt|;
block|}
DECL|method|getSessionID ()
specifier|public
name|SessionID
name|getSessionID
parameter_list|()
block|{
return|return
name|sessionID
return|;
block|}
DECL|method|setSessionID (SessionID sessionID)
specifier|public
name|void
name|setSessionID
parameter_list|(
name|SessionID
name|sessionID
parameter_list|)
block|{
name|this
operator|.
name|sessionID
operator|=
name|sessionID
expr_stmt|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating QuickFIX/J consumer: {}, ExchangePattern={}"
argument_list|,
name|sessionID
operator|!=
literal|null
condition|?
name|sessionID
else|:
literal|"No Session"
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|)
expr_stmt|;
name|QuickfixjConsumer
name|consumer
init|=
operator|new
name|QuickfixjConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating QuickFIX/J producer: {}"
argument_list|,
name|sessionID
operator|!=
literal|null
condition|?
name|sessionID
else|:
literal|"No Session"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isWildcarded
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResolveEndpointFailedException
argument_list|(
literal|"Cannot create consumer on wildcarded session identifier: "
operator|+
name|sessionID
argument_list|)
throw|;
block|}
return|return
operator|new
name|QuickfixjProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
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
name|this
operator|.
name|sessionID
operator|==
literal|null
operator|||
name|isMatching
argument_list|(
name|sessionID
argument_list|)
condition|)
block|{
for|for
control|(
name|QuickfixjConsumer
name|consumer
range|:
name|consumers
control|)
block|{
name|Exchange
name|exchange
init|=
name|QuickfixjConverters
operator|.
name|toExchange
argument_list|(
name|this
argument_list|,
name|sessionID
argument_list|,
name|message
argument_list|,
name|eventCategory
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exchange
operator|.
name|getException
argument_list|()
throw|;
block|}
block|}
block|}
block|}
DECL|method|isMatching (SessionID sessionID)
specifier|private
name|boolean
name|isMatching
parameter_list|(
name|SessionID
name|sessionID
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|sessionID
operator|.
name|equals
argument_list|(
name|sessionID
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|isMatching
argument_list|(
name|this
operator|.
name|sessionID
operator|.
name|getBeginString
argument_list|()
argument_list|,
name|sessionID
operator|.
name|getBeginString
argument_list|()
argument_list|)
operator|&&
name|isMatching
argument_list|(
name|this
operator|.
name|sessionID
operator|.
name|getSenderCompID
argument_list|()
argument_list|,
name|sessionID
operator|.
name|getSenderCompID
argument_list|()
argument_list|)
operator|&&
name|isMatching
argument_list|(
name|this
operator|.
name|sessionID
operator|.
name|getSenderSubID
argument_list|()
argument_list|,
name|sessionID
operator|.
name|getSenderSubID
argument_list|()
argument_list|)
operator|&&
name|isMatching
argument_list|(
name|this
operator|.
name|sessionID
operator|.
name|getSenderLocationID
argument_list|()
argument_list|,
name|sessionID
operator|.
name|getSenderLocationID
argument_list|()
argument_list|)
operator|&&
name|isMatching
argument_list|(
name|this
operator|.
name|sessionID
operator|.
name|getTargetCompID
argument_list|()
argument_list|,
name|sessionID
operator|.
name|getTargetCompID
argument_list|()
argument_list|)
operator|&&
name|isMatching
argument_list|(
name|this
operator|.
name|sessionID
operator|.
name|getTargetSubID
argument_list|()
argument_list|,
name|sessionID
operator|.
name|getTargetSubID
argument_list|()
argument_list|)
operator|&&
name|isMatching
argument_list|(
name|this
operator|.
name|sessionID
operator|.
name|getTargetLocationID
argument_list|()
argument_list|,
name|sessionID
operator|.
name|getTargetLocationID
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isMatching (String s1, String s2)
specifier|private
name|boolean
name|isMatching
parameter_list|(
name|String
name|s1
parameter_list|,
name|String
name|s2
parameter_list|)
block|{
return|return
name|s1
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
operator|||
name|s1
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
operator|||
name|s1
operator|.
name|equals
argument_list|(
name|s2
argument_list|)
return|;
block|}
DECL|method|isWildcarded ()
specifier|private
name|boolean
name|isWildcarded
parameter_list|()
block|{
if|if
condition|(
name|sessionID
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|sessionID
operator|.
name|getBeginString
argument_list|()
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
operator|||
name|sessionID
operator|.
name|getSenderCompID
argument_list|()
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
operator|||
name|sessionID
operator|.
name|getSenderSubID
argument_list|()
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
operator|||
name|sessionID
operator|.
name|getSenderLocationID
argument_list|()
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
operator|||
name|sessionID
operator|.
name|getTargetCompID
argument_list|()
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
operator|||
name|sessionID
operator|.
name|getTargetSubID
argument_list|()
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
operator|||
name|sessionID
operator|.
name|getTargetLocationID
argument_list|()
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
return|;
block|}
DECL|method|isMultipleConsumersSupported ()
specifier|public
name|boolean
name|isMultipleConsumersSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getEngine ()
specifier|public
name|QuickfixjEngine
name|getEngine
parameter_list|()
block|{
return|return
name|engine
return|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// clear list of consumers
name|consumers
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

