begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jgroups
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jgroups
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

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|JChannel
import|;
end_import

begin_comment
comment|/**  * Component providing support for messages multicasted from- or to JGroups channels ({@code org.jgroups.Channel}).  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"jgroups"
argument_list|)
DECL|class|JGroupsComponent
specifier|public
class|class
name|JGroupsComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|channel
specifier|private
name|JChannel
name|channel
decl_stmt|;
DECL|field|channelProperties
specifier|private
name|String
name|channelProperties
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|enableViewMessages
specifier|private
name|boolean
name|enableViewMessages
decl_stmt|;
DECL|method|JGroupsComponent ()
specifier|public
name|JGroupsComponent
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String clusterName, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|clusterName
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
return|return
operator|new
name|JGroupsEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|channel
argument_list|,
name|clusterName
argument_list|,
name|channelProperties
argument_list|,
name|enableViewMessages
argument_list|)
return|;
block|}
DECL|method|getChannel ()
specifier|public
name|JChannel
name|getChannel
parameter_list|()
block|{
return|return
name|channel
return|;
block|}
comment|/**      * Channel to use      */
DECL|method|setChannel (JChannel channel)
specifier|public
name|void
name|setChannel
parameter_list|(
name|JChannel
name|channel
parameter_list|)
block|{
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
block|}
DECL|method|getChannelProperties ()
specifier|public
name|String
name|getChannelProperties
parameter_list|()
block|{
return|return
name|channelProperties
return|;
block|}
comment|/**      * Specifies configuration properties of the JChannel used by the endpoint.      */
DECL|method|setChannelProperties (String channelProperties)
specifier|public
name|void
name|setChannelProperties
parameter_list|(
name|String
name|channelProperties
parameter_list|)
block|{
name|this
operator|.
name|channelProperties
operator|=
name|channelProperties
expr_stmt|;
block|}
DECL|method|isEnableViewMessages ()
specifier|public
name|boolean
name|isEnableViewMessages
parameter_list|()
block|{
return|return
name|enableViewMessages
return|;
block|}
comment|/**      * If set to true, the consumer endpoint will receive org.jgroups.View messages as well (not only org.jgroups.Message instances).      * By default only regular messages are consumed by the endpoint.      */
DECL|method|setEnableViewMessages (boolean enableViewMessages)
specifier|public
name|void
name|setEnableViewMessages
parameter_list|(
name|boolean
name|enableViewMessages
parameter_list|)
block|{
name|this
operator|.
name|enableViewMessages
operator|=
name|enableViewMessages
expr_stmt|;
block|}
block|}
end_class

end_unit

