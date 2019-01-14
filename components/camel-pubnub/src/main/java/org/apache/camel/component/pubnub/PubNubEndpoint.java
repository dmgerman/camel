begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pubnub
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pubnub
package|;
end_package

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|PNConfiguration
import|;
end_import

begin_import
import|import
name|com
operator|.
name|pubnub
operator|.
name|api
operator|.
name|PubNub
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * To send and receive messages to PubNub data stream network for connected devices.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.19.0"
argument_list|,
name|scheme
operator|=
literal|"pubnub"
argument_list|,
name|title
operator|=
literal|"PubNub"
argument_list|,
name|syntax
operator|=
literal|"pubnub:channel"
argument_list|,
name|label
operator|=
literal|"cloud,iot,messaging"
argument_list|)
DECL|class|PubNubEndpoint
specifier|public
class|class
name|PubNubEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|pubnub
specifier|private
name|PubNub
name|pubnub
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|PubNubConfiguration
name|configuration
decl_stmt|;
DECL|method|PubNubEndpoint (String uri, PubNubComponent component, PubNubConfiguration configuration)
specifier|public
name|PubNubEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|PubNubComponent
name|component
parameter_list|,
name|PubNubConfiguration
name|configuration
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
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
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
name|PubNubProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
annotation|@
name|Override
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
return|return
operator|new
name|PubNubConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|)
return|;
block|}
annotation|@
name|Override
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
DECL|method|getConfiguration ()
specifier|public
name|PubNubConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * Reference to a Pubnub client in the registry.      */
DECL|method|getPubnub ()
specifier|public
name|PubNub
name|getPubnub
parameter_list|()
block|{
return|return
name|pubnub
return|;
block|}
DECL|method|setPubnub (PubNub pubnub)
specifier|public
name|void
name|setPubnub
parameter_list|(
name|PubNub
name|pubnub
parameter_list|)
block|{
name|this
operator|.
name|pubnub
operator|=
name|pubnub
expr_stmt|;
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|pubnub
operator|!=
literal|null
condition|)
block|{
name|pubnub
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|pubnub
operator|=
literal|null
expr_stmt|;
block|}
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
name|this
operator|.
name|pubnub
operator|=
name|getPubnub
argument_list|()
operator|!=
literal|null
condition|?
name|getPubnub
argument_list|()
else|:
name|getInstance
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
DECL|method|getInstance ()
specifier|private
name|PubNub
name|getInstance
parameter_list|()
block|{
name|PubNub
name|answer
init|=
literal|null
decl_stmt|;
name|PNConfiguration
name|pnConfiguration
init|=
operator|new
name|PNConfiguration
argument_list|()
decl_stmt|;
name|pnConfiguration
operator|.
name|setPublishKey
argument_list|(
name|configuration
operator|.
name|getPublishKey
argument_list|()
argument_list|)
expr_stmt|;
name|pnConfiguration
operator|.
name|setSubscribeKey
argument_list|(
name|configuration
operator|.
name|getSubscribeKey
argument_list|()
argument_list|)
expr_stmt|;
name|pnConfiguration
operator|.
name|setSecretKey
argument_list|(
name|configuration
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|pnConfiguration
operator|.
name|setAuthKey
argument_list|(
name|configuration
operator|.
name|getAuthKey
argument_list|()
argument_list|)
expr_stmt|;
name|pnConfiguration
operator|.
name|setCipherKey
argument_list|(
name|configuration
operator|.
name|getCipherKey
argument_list|()
argument_list|)
expr_stmt|;
name|pnConfiguration
operator|.
name|setSecure
argument_list|(
name|configuration
operator|.
name|isSecure
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getUuid
argument_list|()
argument_list|)
condition|)
block|{
name|pnConfiguration
operator|.
name|setUuid
argument_list|(
name|configuration
operator|.
name|getUuid
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
operator|new
name|PubNub
argument_list|(
name|pnConfiguration
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

