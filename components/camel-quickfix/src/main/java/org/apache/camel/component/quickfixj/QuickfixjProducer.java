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
name|concurrent
operator|.
name|Callable
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
name|Endpoint
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
name|impl
operator|.
name|DefaultProducer
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
name|MessageUtils
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Session
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
DECL|class|QuickfixjProducer
specifier|public
class|class
name|QuickfixjProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|CORRELATION_TIMEOUT_KEY
specifier|public
specifier|static
specifier|final
name|String
name|CORRELATION_TIMEOUT_KEY
init|=
literal|"CorrelationTimeout"
decl_stmt|;
DECL|field|CORRELATION_CRITERIA_KEY
specifier|public
specifier|static
specifier|final
name|String
name|CORRELATION_CRITERIA_KEY
init|=
literal|"CorrelationCriteria"
decl_stmt|;
DECL|field|sessionID
specifier|private
specifier|final
name|SessionID
name|sessionID
decl_stmt|;
DECL|method|QuickfixjProducer (Endpoint endpoint)
specifier|public
name|QuickfixjProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|sessionID
operator|=
operator|(
operator|(
name|QuickfixjEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getSessionID
argument_list|()
expr_stmt|;
block|}
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
try|try
block|{
name|sendMessage
argument_list|(
name|exchange
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
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
block|}
block|}
DECL|method|sendMessage (Exchange exchange, org.apache.camel.Message camelMessage)
name|void
name|sendMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|message
init|=
name|camelMessage
operator|.
name|getBody
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Sending FIX message: {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|SessionID
name|messageSessionID
init|=
name|sessionID
decl_stmt|;
if|if
condition|(
name|messageSessionID
operator|==
literal|null
condition|)
block|{
name|messageSessionID
operator|=
name|MessageUtils
operator|.
name|getSessionID
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|Session
name|session
init|=
name|getSession
argument_list|(
name|messageSessionID
argument_list|)
decl_stmt|;
if|if
condition|(
name|session
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unknown session: "
operator|+
name|messageSessionID
argument_list|)
throw|;
block|}
name|Callable
argument_list|<
name|Message
argument_list|>
name|callable
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|QuickfixjEndpoint
name|endpoint
init|=
operator|(
name|QuickfixjEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
name|MessageCorrelator
name|messageCorrelator
init|=
name|endpoint
operator|.
name|getEngine
argument_list|()
operator|.
name|getMessageCorrelator
argument_list|()
decl_stmt|;
name|callable
operator|=
name|messageCorrelator
operator|.
name|getReply
argument_list|(
name|endpoint
operator|.
name|getSessionID
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|session
operator|.
name|send
argument_list|(
name|message
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CannotSendException
argument_list|(
literal|"Cannot send FIX message: "
operator|+
name|message
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|callable
operator|!=
literal|null
condition|)
block|{
name|Message
name|reply
init|=
name|callable
operator|.
name|call
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|reply
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getSession (SessionID messageSessionID)
name|Session
name|getSession
parameter_list|(
name|SessionID
name|messageSessionID
parameter_list|)
block|{
return|return
name|Session
operator|.
name|lookupSession
argument_list|(
name|messageSessionID
argument_list|)
return|;
block|}
block|}
end_class

end_unit

