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
name|support
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
name|spi
operator|.
name|Metadata
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|springframework
operator|.
name|messaging
operator|.
name|MessageChannel
import|;
end_import

begin_comment
comment|/**  * Bridges Camel with Spring Integration.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.4.0"
argument_list|,
name|scheme
operator|=
literal|"spring-integration"
argument_list|,
name|title
operator|=
literal|"Spring Integration"
argument_list|,
name|syntax
operator|=
literal|"spring-integration:defaultChannel"
argument_list|,
name|label
operator|=
literal|"spring,eventbus"
argument_list|)
DECL|class|SpringIntegrationEndpoint
specifier|public
class|class
name|SpringIntegrationEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|messageChannel
specifier|private
name|MessageChannel
name|messageChannel
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|defaultChannel
specifier|private
name|String
name|defaultChannel
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|inputChannel
specifier|private
name|String
name|inputChannel
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|outputChannel
specifier|private
name|String
name|outputChannel
decl_stmt|;
annotation|@
name|UriParam
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
name|this
operator|.
name|defaultChannel
operator|=
name|channel
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SpringIntegrationProducer
argument_list|(
operator|(
name|SpringCamelContext
operator|)
name|getCamelContext
argument_list|()
argument_list|,
name|this
argument_list|)
return|;
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
name|SpringIntegrationConsumer
name|answer
init|=
operator|new
name|SpringIntegrationConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * The Spring integration input channel name that this endpoint wants to consume from Spring integration.      */
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
comment|/**      * The Spring integration output channel name that is used to send messages to Spring integration.      */
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
comment|/**      * The default channel name which is used by the Spring Integration Spring context.      * It will equal to the inputChannel name for the Spring Integration consumer and the outputChannel name for the Spring Integration provider.      */
DECL|method|setDefaultChannel (String defaultChannel)
specifier|public
name|void
name|setDefaultChannel
parameter_list|(
name|String
name|defaultChannel
parameter_list|)
block|{
name|this
operator|.
name|defaultChannel
operator|=
name|defaultChannel
expr_stmt|;
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
annotation|@
name|Deprecated
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
comment|/**      * The exchange pattern that the Spring integration endpoint should use.      * If inOut=true then a reply channel is expected, either from the Spring Integration Message header or configured on the endpoint.      */
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

