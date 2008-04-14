begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.integration
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
name|CamelExchangeException
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
name|RuntimeCamelException
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
name|ScheduledPollConsumer
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
name|spring
operator|.
name|SpringCamelContext
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
name|ObjectHelper
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
name|channel
operator|.
name|ChannelRegistry
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
name|channel
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
name|config
operator|.
name|MessageBusParser
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
name|GenericMessage
import|;
end_import

begin_comment
comment|/**  * A consumer of exchanges for the Spring Integration  * Please specify the inputChannel in the endpoint url for this consumer.  * If the message pattern is inOut, the outputChannel property  * should be set for the outgoing message.  * @version  */
end_comment

begin_class
DECL|class|SpringIntegrationConsumer
specifier|public
class|class
name|SpringIntegrationConsumer
extends|extends
name|ScheduledPollConsumer
argument_list|<
name|SpringIntegrationExchange
argument_list|>
block|{
DECL|field|context
specifier|private
name|SpringCamelContext
name|context
decl_stmt|;
DECL|field|inputChannel
specifier|private
name|MessageChannel
name|inputChannel
decl_stmt|;
DECL|field|outputChannel
specifier|private
name|MessageChannel
name|outputChannel
decl_stmt|;
DECL|field|inputChannelName
specifier|private
name|String
name|inputChannelName
decl_stmt|;
DECL|field|channelRegistry
specifier|private
name|ChannelRegistry
name|channelRegistry
decl_stmt|;
DECL|field|endpoint
specifier|private
name|SpringIntegrationEndpoint
name|endpoint
decl_stmt|;
DECL|method|SpringIntegrationConsumer (SpringIntegrationEndpoint endpoint, Processor processor)
specifier|public
name|SpringIntegrationConsumer
parameter_list|(
name|SpringIntegrationEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|context
operator|=
operator|(
name|SpringCamelContext
operator|)
name|endpoint
operator|.
name|getContext
argument_list|()
expr_stmt|;
name|channelRegistry
operator|=
operator|(
name|ChannelRegistry
operator|)
name|context
operator|.
name|getApplicationContext
argument_list|()
operator|.
name|getBean
argument_list|(
name|MessageBusParser
operator|.
name|MESSAGE_BUS_BEAN_NAME
argument_list|)
expr_stmt|;
name|inputChannelName
operator|=
name|endpoint
operator|.
name|getDefaultChannel
argument_list|()
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNullOrBlank
argument_list|(
name|inputChannelName
argument_list|)
condition|)
block|{
name|inputChannelName
operator|=
name|endpoint
operator|.
name|getInputChannel
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isNullOrBlank
argument_list|(
name|inputChannelName
argument_list|)
condition|)
block|{
name|inputChannel
operator|=
operator|(
name|MessageChannel
operator|)
name|channelRegistry
operator|.
name|lookupChannel
argument_list|(
name|inputChannelName
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|inputChannel
argument_list|,
literal|"The inputChannel with the name ["
operator|+
name|inputChannelName
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Can't find the right inputChannelName"
argument_list|)
throw|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|isInOut
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|message
operator|.
name|Message
name|siInMessage
init|=
name|inputChannel
operator|.
name|receive
argument_list|(
name|this
operator|.
name|getDelay
argument_list|()
argument_list|)
decl_stmt|;
name|SpringIntegrationExchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|SpringIntegrationMessage
argument_list|(
name|siInMessage
argument_list|)
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isInOut
argument_list|()
condition|)
block|{
comment|// get the output channel from message header
name|Object
name|returnAddress
init|=
name|siInMessage
operator|.
name|getHeader
argument_list|()
operator|.
name|getReturnAddress
argument_list|()
decl_stmt|;
name|MessageChannel
name|reply
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|returnAddress
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|returnAddress
operator|instanceof
name|String
condition|)
block|{
name|reply
operator|=
operator|(
name|MessageChannel
operator|)
name|context
operator|.
name|getApplicationContext
argument_list|()
operator|.
name|getBean
argument_list|(
operator|(
name|String
operator|)
name|returnAddress
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|returnAddress
operator|instanceof
name|MessageChannel
condition|)
block|{
name|reply
operator|=
operator|(
name|MessageChannel
operator|)
name|returnAddress
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|outputChannel
operator|!=
literal|null
condition|)
block|{
comment|// using the outputChannel
name|reply
operator|=
name|outputChannel
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNullOrBlank
argument_list|(
name|endpoint
operator|.
name|getOutputChannel
argument_list|()
argument_list|)
condition|)
block|{
name|outputChannel
operator|=
operator|(
name|MessageChannel
operator|)
name|channelRegistry
operator|.
name|lookupChannel
argument_list|(
name|endpoint
operator|.
name|getOutputChannel
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|inputChannel
argument_list|,
literal|"The outputChannel with the name ["
operator|+
name|endpoint
operator|.
name|getOutputChannel
argument_list|()
operator|+
literal|"]"
argument_list|)
expr_stmt|;
name|reply
operator|=
name|outputChannel
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Can't find the right outputChannelName"
argument_list|)
throw|;
block|}
block|}
block|}
comment|// put the message back the outputChannel if we need
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|message
operator|.
name|Message
name|siOutMessage
init|=
name|SpringIntegrationBinding
operator|.
name|storeToSpringIntegrationMessage
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
decl_stmt|;
name|reply
operator|.
name|send
argument_list|(
name|siOutMessage
argument_list|)
expr_stmt|;
block|}
block|}
comment|//TODO We need to clean the channel when shutdown the endpoint
block|}
end_class

end_unit

