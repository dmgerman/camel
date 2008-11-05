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
name|impl
operator|.
name|DefaultEndpoint
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
name|ScheduledPollEndpoint
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
name|core
operator|.
name|MessageChannel
import|;
end_import

begin_comment
comment|/**  * Defines the<a href="http://activemq.apache.org/camel/springIntergration.html">Spring Intergration Endpoint</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|SpringIntegrationEndpoint
specifier|public
class|class
name|SpringIntegrationEndpoint
extends|extends
name|ScheduledPollEndpoint
argument_list|<
name|SpringIntegrationExchange
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|SpringIntegrationEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|inputChannel
specifier|private
name|String
name|inputChannel
decl_stmt|;
DECL|field|outputChannel
specifier|private
name|String
name|outputChannel
decl_stmt|;
DECL|field|defaultChannel
specifier|private
name|String
name|defaultChannel
decl_stmt|;
DECL|field|messageChannel
specifier|private
name|MessageChannel
name|messageChannel
decl_stmt|;
DECL|field|inOut
specifier|private
name|boolean
name|inOut
decl_stmt|;
DECL|method|SpringIntegrationEndpoint (String uri, String channel, SpringIntegrationComponent component)
specifier|public
name|SpringIntegrationEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|channel
parameter_list|,
name|SpringIntegrationComponent
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
name|defaultChannel
operator|=
name|channel
expr_stmt|;
block|}
DECL|method|SpringIntegrationEndpoint (String uri, MessageChannel channel, CamelContext context)
specifier|public
name|SpringIntegrationEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|MessageChannel
name|channel
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
name|messageChannel
operator|=
name|channel
expr_stmt|;
block|}
DECL|method|SpringIntegrationEndpoint (String endpointUri, MessageChannel messageChannel)
specifier|public
name|SpringIntegrationEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|MessageChannel
name|messageChannel
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|messageChannel
operator|=
name|messageChannel
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|SpringIntegrationExchange
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SpringIntegrationProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
argument_list|<
name|SpringIntegrationExchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|SpringIntegrationConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|SpringIntegrationExchange
name|createExchange
parameter_list|()
block|{
return|return
name|createExchange
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|SpringIntegrationExchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|SpringIntegrationExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|pattern
argument_list|)
return|;
block|}
DECL|method|setInputChannel (String input)
specifier|public
name|void
name|setInputChannel
parameter_list|(
name|String
name|input
parameter_list|)
block|{
name|inputChannel
operator|=
name|input
expr_stmt|;
block|}
DECL|method|getInputChannel ()
specifier|public
name|String
name|getInputChannel
parameter_list|()
block|{
return|return
name|inputChannel
return|;
block|}
DECL|method|setOutputChannel (String output)
specifier|public
name|void
name|setOutputChannel
parameter_list|(
name|String
name|output
parameter_list|)
block|{
name|outputChannel
operator|=
name|output
expr_stmt|;
block|}
DECL|method|getOutputChannel ()
specifier|public
name|String
name|getOutputChannel
parameter_list|()
block|{
return|return
name|outputChannel
return|;
block|}
DECL|method|getDefaultChannel ()
specifier|public
name|String
name|getDefaultChannel
parameter_list|()
block|{
return|return
name|defaultChannel
return|;
block|}
DECL|method|getMessageChannel ()
specifier|public
name|MessageChannel
name|getMessageChannel
parameter_list|()
block|{
return|return
name|messageChannel
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|setInOut (boolean inOut)
specifier|public
name|void
name|setInOut
parameter_list|(
name|boolean
name|inOut
parameter_list|)
block|{
name|this
operator|.
name|inOut
operator|=
name|inOut
expr_stmt|;
block|}
DECL|method|isInOut ()
specifier|public
name|boolean
name|isInOut
parameter_list|()
block|{
return|return
name|this
operator|.
name|inOut
return|;
block|}
block|}
end_class

end_unit

