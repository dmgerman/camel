begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.slack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|slack
package|;
end_package

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
name|spi
operator|.
name|annotations
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
name|support
operator|.
name|DefaultComponent
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"slack"
argument_list|)
DECL|class|SlackComponent
specifier|public
class|class
name|SlackComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|webhookUrl
specifier|private
name|String
name|webhookUrl
decl_stmt|;
DECL|method|SlackComponent ()
specifier|public
name|SlackComponent
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|SlackComponent (CamelContext context)
specifier|public
name|SlackComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|registerExtension
argument_list|(
operator|new
name|SlackComponentVerifierExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a slack endpoint      *      * @param uri         the full URI of the endpoint      * @param channelName the channel or username that the message should be sent to      * @param parameters  the optional parameters passed in      * @return the camel endpoint      */
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String channelName, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|channelName
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
operator|new
name|SlackEndpoint
argument_list|(
name|uri
argument_list|,
name|channelName
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getWebhookUrl ()
specifier|public
name|String
name|getWebhookUrl
parameter_list|()
block|{
return|return
name|webhookUrl
return|;
block|}
comment|/**      * The incoming webhook URL      */
DECL|method|setWebhookUrl (String webhookUrl)
specifier|public
name|void
name|setWebhookUrl
parameter_list|(
name|String
name|webhookUrl
parameter_list|)
block|{
name|this
operator|.
name|webhookUrl
operator|=
name|webhookUrl
expr_stmt|;
block|}
block|}
end_class

end_unit

