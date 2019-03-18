begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
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
name|support
operator|.
name|ScheduledPollEndpoint
import|;
end_import

begin_comment
comment|/**  * The yammer component allows you to interact with the Yammer enterprise social network.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.12.0"
argument_list|,
name|scheme
operator|=
literal|"yammer"
argument_list|,
name|title
operator|=
literal|"Yammer"
argument_list|,
name|syntax
operator|=
literal|"yammer:function"
argument_list|,
name|label
operator|=
literal|"social"
argument_list|)
DECL|class|YammerEndpoint
specifier|public
class|class
name|YammerEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
annotation|@
name|UriParam
DECL|field|config
specifier|private
name|YammerConfiguration
name|config
decl_stmt|;
DECL|method|YammerEndpoint ()
specifier|public
name|YammerEndpoint
parameter_list|()
block|{     }
DECL|method|YammerEndpoint (String uri, YammerComponent component)
specifier|public
name|YammerEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|YammerComponent
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
block|}
DECL|method|YammerEndpoint (String uri, YammerComponent yammerComponent, YammerConfiguration config)
specifier|public
name|YammerEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|YammerComponent
name|yammerComponent
parameter_list|,
name|YammerConfiguration
name|config
parameter_list|)
block|{
name|this
operator|.
name|setConfig
argument_list|(
name|config
argument_list|)
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
name|YammerMessageProducer
argument_list|(
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
switch|switch
condition|(
name|config
operator|.
name|getFunctionType
argument_list|()
condition|)
block|{
case|case
name|MESSAGES
case|:
case|case
name|ALGO
case|:
case|case
name|FOLLOWING
case|:
case|case
name|MY_FEED
case|:
case|case
name|PRIVATE
case|:
case|case
name|SENT
case|:
case|case
name|RECEIVED
case|:
return|return
operator|new
name|YammerMessagePollingConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
case|case
name|USERS
case|:
case|case
name|CURRENT
case|:
return|return
operator|new
name|YammerUserPollingConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|Exception
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s is not a valid Yammer function type."
argument_list|,
name|config
operator|.
name|getFunction
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
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
DECL|method|getConfig ()
specifier|public
name|YammerConfiguration
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|setConfig (YammerConfiguration config)
specifier|public
name|void
name|setConfig
parameter_list|(
name|YammerConfiguration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"yammer://%s?consumerKey=%s&consumerSecret=%s&accessToken=%s"
argument_list|,
name|config
operator|.
name|getFunction
argument_list|()
argument_list|,
name|config
operator|.
name|getConsumerKey
argument_list|()
argument_list|,
name|config
operator|.
name|getConsumerSecret
argument_list|()
argument_list|,
name|config
operator|.
name|getAccessToken
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

