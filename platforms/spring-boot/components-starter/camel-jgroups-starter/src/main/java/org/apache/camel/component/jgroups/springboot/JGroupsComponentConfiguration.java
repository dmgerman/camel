begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jgroups.springboot
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
operator|.
name|springboot
package|;
end_package

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
name|jgroups
operator|.
name|JChannel
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
comment|/**  * The jgroups component provides exchange of messages between Camel and JGroups  * clusters.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.jgroups"
argument_list|)
DECL|class|JGroupsComponentConfiguration
specifier|public
class|class
name|JGroupsComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Channel to use      */
annotation|@
name|NestedConfigurationProperty
DECL|field|channel
specifier|private
name|JChannel
name|channel
decl_stmt|;
comment|/**      * Specifies configuration properties of the JChannel used by the endpoint.      */
DECL|field|channelProperties
specifier|private
name|String
name|channelProperties
decl_stmt|;
comment|/**      * If set to true the consumer endpoint will receive org.jgroups.View      * messages as well (not only org.jgroups.Message instances). By default      * only regular messages are consumed by the endpoint.      */
DECL|field|enableViewMessages
specifier|private
name|Boolean
name|enableViewMessages
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
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
DECL|method|getEnableViewMessages ()
specifier|public
name|Boolean
name|getEnableViewMessages
parameter_list|()
block|{
return|return
name|enableViewMessages
return|;
block|}
DECL|method|setEnableViewMessages (Boolean enableViewMessages)
specifier|public
name|void
name|setEnableViewMessages
parameter_list|(
name|Boolean
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
block|}
end_class

end_unit

