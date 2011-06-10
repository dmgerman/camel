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
name|DefaultProducer
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
name|channel
operator|.
name|DirectChannel
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
name|MessageHandler
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
name|support
operator|.
name|channel
operator|.
name|BeanFactoryChannelResolver
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
name|support
operator|.
name|channel
operator|.
name|ChannelResolver
import|;
end_import

begin_comment
comment|/**  * A producer of exchanges for the Spring Integration  * Please specify the outputChannel in the endpoint url for this producer.  * If the message pattern is inOut, the inputChannel property  * should be set for receiving the response message.  * @version   */
end_comment

begin_class
DECL|class|SpringIntegrationProducer
specifier|public
class|class
name|SpringIntegrationProducer
extends|extends
name|DefaultProducer
implements|implements
name|Processor
block|{
DECL|field|channelResolver
specifier|private
specifier|final
name|ChannelResolver
name|channelResolver
decl_stmt|;
DECL|field|inputChannel
specifier|private
name|DirectChannel
name|inputChannel
decl_stmt|;
DECL|field|outputChannel
specifier|private
name|MessageChannel
name|outputChannel
decl_stmt|;
DECL|method|SpringIntegrationProducer (SpringCamelContext context, SpringIntegrationEndpoint endpoint)
specifier|public
name|SpringIntegrationProducer
parameter_list|(
name|SpringCamelContext
name|context
parameter_list|,
name|SpringIntegrationEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|channelResolver
operator|=
operator|new
name|BeanFactoryChannelResolver
argument_list|(
name|context
operator|.
name|getApplicationContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SpringIntegrationEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SpringIntegrationEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getMessageChannel
argument_list|()
operator|==
literal|null
condition|)
block|{
name|String
name|outputChannelName
init|=
name|getEndpoint
argument_list|()
operator|.
name|getDefaultChannel
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|outputChannelName
argument_list|)
condition|)
block|{
name|outputChannelName
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getInputChannel
argument_list|()
expr_stmt|;
block|}
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|outputChannelName
argument_list|,
literal|"OutputChannelName"
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|outputChannel
operator|=
name|channelResolver
operator|.
name|resolveChannelName
argument_list|(
name|outputChannelName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|outputChannel
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getMessageChannel
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|outputChannel
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot resolve OutputChannel on "
operator|+
name|getEndpoint
argument_list|()
argument_list|)
throw|;
block|}
comment|// if we do in-out we need to setup the input channel as well
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isInOut
argument_list|()
condition|)
block|{
comment|// we need to setup right inputChannel for further processing
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getInputChannel
argument_list|()
argument_list|,
literal|"InputChannel"
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|inputChannel
operator|=
operator|(
name|DirectChannel
operator|)
name|channelResolver
operator|.
name|resolveChannelName
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getInputChannel
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|inputChannel
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot resolve InputChannel on "
operator|+
name|getEndpoint
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
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
comment|// we want to do in-out so the inputChannel is mandatory (used to receive reply from spring integration)
if|if
condition|(
name|inputChannel
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"InputChannel has not been configured on "
operator|+
name|getEndpoint
argument_list|()
argument_list|)
throw|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|MessageHeaders
operator|.
name|REPLY_CHANNEL
argument_list|,
name|inputChannel
argument_list|)
expr_stmt|;
comment|// subscribe so we can receive the reply from spring integration
name|inputChannel
operator|.
name|subscribe
argument_list|(
operator|new
name|MessageHandler
argument_list|()
block|{
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
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Received {} from InputChannel: {}"
argument_list|,
name|message
argument_list|,
name|inputChannel
argument_list|)
expr_stmt|;
name|SpringIntegrationBinding
operator|.
name|storeToCamelMessage
argument_list|(
name|message
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|Message
name|siOutmessage
init|=
name|SpringIntegrationBinding
operator|.
name|createSpringIntegrationMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// send the message to spring integration
name|log
operator|.
name|debug
argument_list|(
literal|"Sending {} to OutputChannel: {}"
argument_list|,
name|siOutmessage
argument_list|,
name|outputChannel
argument_list|)
expr_stmt|;
name|outputChannel
operator|.
name|send
argument_list|(
name|siOutmessage
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

