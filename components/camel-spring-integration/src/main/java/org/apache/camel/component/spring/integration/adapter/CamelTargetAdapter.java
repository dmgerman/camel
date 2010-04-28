begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.integration.adapter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|integration
operator|.
name|adapter
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
name|ExchangePattern
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
name|ProducerTemplate
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
name|spring
operator|.
name|integration
operator|.
name|SpringIntegrationBinding
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
name|DefaultCamelContext
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
name|DefaultExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|core
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|core
operator|.
name|MessageChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|core
operator|.
name|MessageHeaders
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|message
operator|.
name|MessageDeliveryException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|message
operator|.
name|MessageHandler
import|;
end_import

begin_comment
comment|/**  * CamelTargetAdapter will redirect the Spring Integration message to the Camel context.  * When we inject the camel context into it, we need also specify the Camel endpoint url  * we will route the Spring Integration message to the Camel context  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelTargetAdapter
specifier|public
class|class
name|CamelTargetAdapter
extends|extends
name|AbstractCamelAdapter
implements|implements
name|MessageHandler
block|{
DECL|field|camelTemplate
specifier|private
name|ProducerTemplate
name|camelTemplate
decl_stmt|;
DECL|field|replyChannel
specifier|private
name|MessageChannel
name|replyChannel
decl_stmt|;
DECL|method|setReplyChannel (MessageChannel channel)
specifier|public
name|void
name|setReplyChannel
parameter_list|(
name|MessageChannel
name|channel
parameter_list|)
block|{
name|replyChannel
operator|=
name|channel
expr_stmt|;
block|}
DECL|method|getReplyChannel ()
specifier|public
name|MessageChannel
name|getReplyChannel
parameter_list|()
block|{
return|return
name|replyChannel
return|;
block|}
DECL|method|getCamelTemplate ()
specifier|public
name|ProducerTemplate
name|getCamelTemplate
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelTemplate
operator|==
literal|null
condition|)
block|{
name|CamelContext
name|ctx
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|ctx
operator|==
literal|null
condition|)
block|{
name|ctx
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
block|}
name|camelTemplate
operator|=
name|ctx
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
block|}
return|return
name|camelTemplate
return|;
block|}
DECL|method|send (Message<?> message)
specifier|public
name|boolean
name|send
parameter_list|(
name|Message
argument_list|<
name|?
argument_list|>
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|ExchangePattern
name|pattern
decl_stmt|;
name|boolean
name|result
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|isExpectReply
argument_list|()
condition|)
block|{
name|pattern
operator|=
name|ExchangePattern
operator|.
name|InOut
expr_stmt|;
block|}
else|else
block|{
name|pattern
operator|=
name|ExchangePattern
operator|.
name|InOnly
expr_stmt|;
block|}
name|Exchange
name|inExchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|pattern
argument_list|)
decl_stmt|;
name|SpringIntegrationBinding
operator|.
name|storeToCamelMessage
argument_list|(
name|message
argument_list|,
name|inExchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|outExchange
init|=
name|getCamelTemplate
argument_list|()
operator|.
name|send
argument_list|(
name|getCamelEndpointUri
argument_list|()
argument_list|,
name|inExchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|outExchange
operator|.
name|getOut
argument_list|()
operator|!=
literal|null
operator|&&
name|outExchange
operator|.
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
condition|)
block|{
name|result
operator|=
literal|true
expr_stmt|;
block|}
name|Message
name|response
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isExpectReply
argument_list|()
condition|)
block|{
comment|//Check the message header for the return address
name|response
operator|=
name|SpringIntegrationBinding
operator|.
name|storeToSpringIntegrationMessage
argument_list|(
name|outExchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|replyChannel
operator|==
literal|null
condition|)
block|{
name|MessageChannel
name|messageReplyChannel
init|=
operator|(
name|MessageChannel
operator|)
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|MessageHeaders
operator|.
name|REPLY_CHANNEL
argument_list|)
decl_stmt|;
if|if
condition|(
name|messageReplyChannel
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|messageReplyChannel
operator|.
name|send
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|MessageDeliveryException
argument_list|(
name|response
argument_list|,
literal|"Can't find reply channel from the CamelTargetAdapter or MessageHeaders"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|result
operator|=
name|replyChannel
operator|.
name|send
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|handleMessage (Message<?> message)
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
argument_list|<
name|?
argument_list|>
name|message
parameter_list|)
throws|throws
name|MessageDeliveryException
block|{
try|try
block|{
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MessageDeliveryException
argument_list|(
name|message
argument_list|,
literal|"Cannot send message"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

