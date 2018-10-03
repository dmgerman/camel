begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cometd.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cometd
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
name|cometd
operator|.
name|bayeux
operator|.
name|server
operator|.
name|BayeuxServer
operator|.
name|Extension
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
comment|/**  * The cometd component is a transport for working with the Jetty implementation  * of the cometd/bayeux protocol.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.cometd"
argument_list|)
DECL|class|CometdComponentConfiguration
specifier|public
class|class
name|CometdComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the cometd component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * The password for the keystore when using SSL.      */
DECL|field|sslKeyPassword
specifier|private
name|String
name|sslKeyPassword
decl_stmt|;
comment|/**      * The password when using SSL.      */
DECL|field|sslPassword
specifier|private
name|String
name|sslPassword
decl_stmt|;
comment|/**      * The path to the keystore.      */
DECL|field|sslKeystore
specifier|private
name|String
name|sslKeystore
decl_stmt|;
comment|/**      * To use a custom configured SecurityPolicy to control authorization. The      * option is a org.cometd.bayeux.server.SecurityPolicy type.      */
DECL|field|securityPolicy
specifier|private
name|String
name|securityPolicy
decl_stmt|;
comment|/**      * To use a list of custom BayeuxServer.Extension that allows modifying      * incoming and outgoing requests.      */
DECL|field|extensions
specifier|private
name|List
argument_list|<
name|Extension
argument_list|>
name|extensions
decl_stmt|;
comment|/**      * To configure security using SSLContextParameters. The option is a      * org.apache.camel.support.jsse.SSLContextParameters type.      */
DECL|field|sslContextParameters
specifier|private
name|String
name|sslContextParameters
decl_stmt|;
comment|/**      * Enable usage of global SSL context parameters.      */
DECL|field|useGlobalSslContextParameters
specifier|private
name|Boolean
name|useGlobalSslContextParameters
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
DECL|method|getSslKeyPassword ()
specifier|public
name|String
name|getSslKeyPassword
parameter_list|()
block|{
return|return
name|sslKeyPassword
return|;
block|}
DECL|method|setSslKeyPassword (String sslKeyPassword)
specifier|public
name|void
name|setSslKeyPassword
parameter_list|(
name|String
name|sslKeyPassword
parameter_list|)
block|{
name|this
operator|.
name|sslKeyPassword
operator|=
name|sslKeyPassword
expr_stmt|;
block|}
DECL|method|getSslPassword ()
specifier|public
name|String
name|getSslPassword
parameter_list|()
block|{
return|return
name|sslPassword
return|;
block|}
DECL|method|setSslPassword (String sslPassword)
specifier|public
name|void
name|setSslPassword
parameter_list|(
name|String
name|sslPassword
parameter_list|)
block|{
name|this
operator|.
name|sslPassword
operator|=
name|sslPassword
expr_stmt|;
block|}
DECL|method|getSslKeystore ()
specifier|public
name|String
name|getSslKeystore
parameter_list|()
block|{
return|return
name|sslKeystore
return|;
block|}
DECL|method|setSslKeystore (String sslKeystore)
specifier|public
name|void
name|setSslKeystore
parameter_list|(
name|String
name|sslKeystore
parameter_list|)
block|{
name|this
operator|.
name|sslKeystore
operator|=
name|sslKeystore
expr_stmt|;
block|}
DECL|method|getSecurityPolicy ()
specifier|public
name|String
name|getSecurityPolicy
parameter_list|()
block|{
return|return
name|securityPolicy
return|;
block|}
DECL|method|setSecurityPolicy (String securityPolicy)
specifier|public
name|void
name|setSecurityPolicy
parameter_list|(
name|String
name|securityPolicy
parameter_list|)
block|{
name|this
operator|.
name|securityPolicy
operator|=
name|securityPolicy
expr_stmt|;
block|}
DECL|method|getExtensions ()
specifier|public
name|List
argument_list|<
name|Extension
argument_list|>
name|getExtensions
parameter_list|()
block|{
return|return
name|extensions
return|;
block|}
DECL|method|setExtensions (List<Extension> extensions)
specifier|public
name|void
name|setExtensions
parameter_list|(
name|List
argument_list|<
name|Extension
argument_list|>
name|extensions
parameter_list|)
block|{
name|this
operator|.
name|extensions
operator|=
name|extensions
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|String
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters (String sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|String
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getUseGlobalSslContextParameters ()
specifier|public
name|Boolean
name|getUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|useGlobalSslContextParameters
return|;
block|}
DECL|method|setUseGlobalSslContextParameters ( Boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|Boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
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

