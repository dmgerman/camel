begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.messaging.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|messaging
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|AtomixClient
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|catalyst
operator|.
name|transport
operator|.
name|Address
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
name|atomix
operator|.
name|client
operator|.
name|messaging
operator|.
name|AtomixMessaging
operator|.
name|Action
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
name|atomix
operator|.
name|client
operator|.
name|messaging
operator|.
name|AtomixMessaging
operator|.
name|BroadcastType
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
name|atomix
operator|.
name|client
operator|.
name|messaging
operator|.
name|AtomixMessagingComponent
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
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * Camel Atomix support  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.atomix-messaging"
argument_list|)
DECL|class|AtomixMessagingComponentConfiguration
specifier|public
class|class
name|AtomixMessagingComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * The shared component configuration      */
DECL|field|configuration
specifier|private
name|AtomixMessagingConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * The shared AtomixClient instance      */
annotation|@
name|NestedConfigurationProperty
DECL|field|atomix
specifier|private
name|AtomixClient
name|atomix
decl_stmt|;
comment|/**      * The nodes the AtomixClient should connect to      */
DECL|field|nodes
specifier|private
name|List
argument_list|<
name|Address
argument_list|>
name|nodes
decl_stmt|;
comment|/**      * The path to the AtomixClient configuration      */
DECL|field|configurationUri
specifier|private
name|String
name|configurationUri
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|AtomixMessagingConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( AtomixMessagingConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|AtomixMessagingConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getAtomix ()
specifier|public
name|AtomixClient
name|getAtomix
parameter_list|()
block|{
return|return
name|atomix
return|;
block|}
DECL|method|setAtomix (AtomixClient atomix)
specifier|public
name|void
name|setAtomix
parameter_list|(
name|AtomixClient
name|atomix
parameter_list|)
block|{
name|this
operator|.
name|atomix
operator|=
name|atomix
expr_stmt|;
block|}
DECL|method|getNodes ()
specifier|public
name|List
argument_list|<
name|Address
argument_list|>
name|getNodes
parameter_list|()
block|{
return|return
name|nodes
return|;
block|}
DECL|method|setNodes (List<Address> nodes)
specifier|public
name|void
name|setNodes
parameter_list|(
name|List
argument_list|<
name|Address
argument_list|>
name|nodes
parameter_list|)
block|{
name|this
operator|.
name|nodes
operator|=
name|nodes
expr_stmt|;
block|}
DECL|method|getConfigurationUri ()
specifier|public
name|String
name|getConfigurationUri
parameter_list|()
block|{
return|return
name|configurationUri
return|;
block|}
DECL|method|setConfigurationUri (String configurationUri)
specifier|public
name|void
name|setConfigurationUri
parameter_list|(
name|String
name|configurationUri
parameter_list|)
block|{
name|this
operator|.
name|configurationUri
operator|=
name|configurationUri
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|AtomixMessagingConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|AtomixMessagingConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|messaging
operator|.
name|AtomixMessagingConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * The default action.          */
DECL|field|defaultAction
specifier|private
name|Action
name|defaultAction
init|=
name|Action
operator|.
name|DIRECT
decl_stmt|;
comment|/**          * The Atomix Group member name          */
DECL|field|memberName
specifier|private
name|String
name|memberName
decl_stmt|;
comment|/**          * The messaging channel name          */
DECL|field|channelName
specifier|private
name|String
name|channelName
decl_stmt|;
comment|/**          * The broadcast type.          */
DECL|field|broadcastType
specifier|private
name|BroadcastType
name|broadcastType
init|=
name|BroadcastType
operator|.
name|ALL
decl_stmt|;
comment|/**          * The header that wil carry the result.          */
DECL|field|resultHeader
specifier|private
name|String
name|resultHeader
decl_stmt|;
DECL|method|getDefaultAction ()
specifier|public
name|Action
name|getDefaultAction
parameter_list|()
block|{
return|return
name|defaultAction
return|;
block|}
DECL|method|setDefaultAction (Action defaultAction)
specifier|public
name|void
name|setDefaultAction
parameter_list|(
name|Action
name|defaultAction
parameter_list|)
block|{
name|this
operator|.
name|defaultAction
operator|=
name|defaultAction
expr_stmt|;
block|}
DECL|method|getMemberName ()
specifier|public
name|String
name|getMemberName
parameter_list|()
block|{
return|return
name|memberName
return|;
block|}
DECL|method|setMemberName (String memberName)
specifier|public
name|void
name|setMemberName
parameter_list|(
name|String
name|memberName
parameter_list|)
block|{
name|this
operator|.
name|memberName
operator|=
name|memberName
expr_stmt|;
block|}
DECL|method|getChannelName ()
specifier|public
name|String
name|getChannelName
parameter_list|()
block|{
return|return
name|channelName
return|;
block|}
DECL|method|setChannelName (String channelName)
specifier|public
name|void
name|setChannelName
parameter_list|(
name|String
name|channelName
parameter_list|)
block|{
name|this
operator|.
name|channelName
operator|=
name|channelName
expr_stmt|;
block|}
DECL|method|getBroadcastType ()
specifier|public
name|BroadcastType
name|getBroadcastType
parameter_list|()
block|{
return|return
name|broadcastType
return|;
block|}
DECL|method|setBroadcastType (BroadcastType broadcastType)
specifier|public
name|void
name|setBroadcastType
parameter_list|(
name|BroadcastType
name|broadcastType
parameter_list|)
block|{
name|this
operator|.
name|broadcastType
operator|=
name|broadcastType
expr_stmt|;
block|}
DECL|method|getResultHeader ()
specifier|public
name|String
name|getResultHeader
parameter_list|()
block|{
return|return
name|resultHeader
return|;
block|}
DECL|method|setResultHeader (String resultHeader)
specifier|public
name|void
name|setResultHeader
parameter_list|(
name|String
name|resultHeader
parameter_list|)
block|{
name|this
operator|.
name|resultHeader
operator|=
name|resultHeader
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

