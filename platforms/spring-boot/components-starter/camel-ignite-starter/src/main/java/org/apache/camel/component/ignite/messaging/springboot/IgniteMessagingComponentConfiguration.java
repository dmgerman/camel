begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite.messaging.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
operator|.
name|messaging
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

begin_comment
comment|/**  * The Ignite Messaging endpoint is one of camel-ignite endpoints which allows  * you to send and consume messages from an Ignite topic.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.ignite-messaging"
argument_list|)
DECL|class|IgniteMessagingComponentConfiguration
specifier|public
class|class
name|IgniteMessagingComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the ignite-messaging component.      * This is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Sets the Ignite instance. The option is a org.apache.ignite.Ignite type.      */
DECL|field|ignite
specifier|private
name|String
name|ignite
decl_stmt|;
comment|/**      * Sets the resource from where to load the configuration. It can be a: URI,      * String (URI) or an InputStream. The option is a java.lang.Object type.      */
DECL|field|configurationResource
specifier|private
name|String
name|configurationResource
decl_stmt|;
comment|/**      * Allows the user to set a programmatic IgniteConfiguration. The option is      * a org.apache.ignite.configuration.IgniteConfiguration type.      */
DECL|field|igniteConfiguration
specifier|private
name|String
name|igniteConfiguration
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getIgnite ()
specifier|public
name|String
name|getIgnite
parameter_list|()
block|{
return|return
name|ignite
return|;
block|}
DECL|method|setIgnite (String ignite)
specifier|public
name|void
name|setIgnite
parameter_list|(
name|String
name|ignite
parameter_list|)
block|{
name|this
operator|.
name|ignite
operator|=
name|ignite
expr_stmt|;
block|}
DECL|method|getConfigurationResource ()
specifier|public
name|String
name|getConfigurationResource
parameter_list|()
block|{
return|return
name|configurationResource
return|;
block|}
DECL|method|setConfigurationResource (String configurationResource)
specifier|public
name|void
name|setConfigurationResource
parameter_list|(
name|String
name|configurationResource
parameter_list|)
block|{
name|this
operator|.
name|configurationResource
operator|=
name|configurationResource
expr_stmt|;
block|}
DECL|method|getIgniteConfiguration ()
specifier|public
name|String
name|getIgniteConfiguration
parameter_list|()
block|{
return|return
name|igniteConfiguration
return|;
block|}
DECL|method|setIgniteConfiguration (String igniteConfiguration)
specifier|public
name|void
name|setIgniteConfiguration
parameter_list|(
name|String
name|igniteConfiguration
parameter_list|)
block|{
name|this
operator|.
name|igniteConfiguration
operator|=
name|igniteConfiguration
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
block|}
end_class

end_unit

