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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|AbstractPollableChannel
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
name|MessageHeaders
import|;
end_import

begin_comment
comment|/**  * A producer of exchanges for the Spring Integration  * Please specify the outputChannel in the endpoint url for this producer.  * If the message pattern is inOut, the inputChannel property  * should be set for receiving the response message.  * @version $Revision$  */
end_comment

begin_class
DECL|class|SpringIntegrationProducer
specifier|public
class|class
name|SpringIntegrationProducer
extends|extends
name|DefaultProducer
argument_list|<
name|SpringIntegrationExchange
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
name|SpringIntegrationProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|context
specifier|private
name|SpringCamelContext
name|context
decl_stmt|;
DECL|field|inputChannel
specifier|private
name|AbstractPollableChannel
name|inputChannel
decl_stmt|;
DECL|field|outputChannel
specifier|private
name|MessageChannel
name|outputChannel
decl_stmt|;
DECL|field|outputChannelName
specifier|private
name|String
name|outputChannelName
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
DECL|method|SpringIntegrationProducer (SpringIntegrationEndpoint endpoint)
specifier|public
name|SpringIntegrationProducer
parameter_list|(
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
name|getCamelContext
argument_list|()
expr_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|getMessageChannel
argument_list|()
operator|==
literal|null
condition|)
block|{
name|outputChannelName
operator|=
name|endpoint
operator|.
name|getDefaultChannel
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
if|if
condition|(
name|ObjectHelper
operator|.
name|isNullOrBlank
argument_list|(
name|outputChannelName
argument_list|)
condition|)
block|{
name|outputChannelName
operator|=
name|endpoint
operator|.
name|getInputChannel
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNullOrBlank
argument_list|(
name|outputChannelName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Can't find the right outputChannelName,"
operator|+
literal|"please check the endpoint uri outputChannel part!"
argument_list|)
throw|;
block|}
else|else
block|{
name|outputChannel
operator|=
operator|(
name|AbstractPollableChannel
operator|)
name|channelRegistry
operator|.
name|lookupChannel
argument_list|(
name|outputChannelName
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|endpoint
operator|.
name|getMessageChannel
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|outputChannel
operator|=
name|endpoint
operator|.
name|getMessageChannel
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Can't find the right message channel, please check your configuration."
argument_list|)
throw|;
block|}
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
comment|// we need to setup right inputChannel for further processing
if|if
condition|(
name|ObjectHelper
operator|.
name|isNullOrBlank
argument_list|(
name|endpoint
operator|.
name|getInputChannel
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Can't find the right inputChannel, "
operator|+
literal|"please check the endpoint uri inputChannel part!"
argument_list|)
throw|;
block|}
else|else
block|{
name|inputChannel
operator|=
operator|(
name|AbstractPollableChannel
operator|)
name|channelRegistry
operator|.
name|lookupChannel
argument_list|(
name|endpoint
operator|.
name|getInputChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isInCapable
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|MessageHeaders
operator|.
name|RETURN_ADDRESS
argument_list|,
name|inputChannel
argument_list|)
expr_stmt|;
block|}
name|org
operator|.
name|springframework
operator|.
name|integration
operator|.
name|message
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
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isInCapable
argument_list|()
condition|)
block|{
comment|//Set the return channel address
name|outputChannel
operator|.
name|send
argument_list|(
name|siOutmessage
argument_list|)
expr_stmt|;
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
argument_list|()
decl_stmt|;
name|SpringIntegrationBinding
operator|.
name|storeToCamelMessage
argument_list|(
name|siInMessage
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|outputChannel
operator|.
name|send
argument_list|(
name|siOutmessage
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

