begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo.client.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
operator|.
name|client
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
name|Set
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
comment|/**  * Connect to OPC UA servers using the binary protocol for acquiring telemetry  * data  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.milo-client"
argument_list|)
DECL|class|MiloClientComponentConfiguration
specifier|public
class|class
name|MiloClientComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the milo-client component. This      * is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * All default options for client      */
DECL|field|defaultConfiguration
specifier|private
name|MiloClientConfigurationNestedConfiguration
name|defaultConfiguration
decl_stmt|;
comment|/**      * Default application name      */
DECL|field|applicationName
specifier|private
name|String
name|applicationName
decl_stmt|;
comment|/**      * Default application URI      */
DECL|field|applicationUri
specifier|private
name|String
name|applicationUri
decl_stmt|;
comment|/**      * Default product URI      */
DECL|field|productUri
specifier|private
name|String
name|productUri
decl_stmt|;
comment|/**      * Default reconnect timeout      */
DECL|field|reconnectTimeout
specifier|private
name|Long
name|reconnectTimeout
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getDefaultConfiguration ()
specifier|public
name|MiloClientConfigurationNestedConfiguration
name|getDefaultConfiguration
parameter_list|()
block|{
return|return
name|defaultConfiguration
return|;
block|}
DECL|method|setDefaultConfiguration ( MiloClientConfigurationNestedConfiguration defaultConfiguration)
specifier|public
name|void
name|setDefaultConfiguration
parameter_list|(
name|MiloClientConfigurationNestedConfiguration
name|defaultConfiguration
parameter_list|)
block|{
name|this
operator|.
name|defaultConfiguration
operator|=
name|defaultConfiguration
expr_stmt|;
block|}
DECL|method|getApplicationName ()
specifier|public
name|String
name|getApplicationName
parameter_list|()
block|{
return|return
name|applicationName
return|;
block|}
DECL|method|setApplicationName (String applicationName)
specifier|public
name|void
name|setApplicationName
parameter_list|(
name|String
name|applicationName
parameter_list|)
block|{
name|this
operator|.
name|applicationName
operator|=
name|applicationName
expr_stmt|;
block|}
DECL|method|getApplicationUri ()
specifier|public
name|String
name|getApplicationUri
parameter_list|()
block|{
return|return
name|applicationUri
return|;
block|}
DECL|method|setApplicationUri (String applicationUri)
specifier|public
name|void
name|setApplicationUri
parameter_list|(
name|String
name|applicationUri
parameter_list|)
block|{
name|this
operator|.
name|applicationUri
operator|=
name|applicationUri
expr_stmt|;
block|}
DECL|method|getProductUri ()
specifier|public
name|String
name|getProductUri
parameter_list|()
block|{
return|return
name|productUri
return|;
block|}
DECL|method|setProductUri (String productUri)
specifier|public
name|void
name|setProductUri
parameter_list|(
name|String
name|productUri
parameter_list|)
block|{
name|this
operator|.
name|productUri
operator|=
name|productUri
expr_stmt|;
block|}
DECL|method|getReconnectTimeout ()
specifier|public
name|Long
name|getReconnectTimeout
parameter_list|()
block|{
return|return
name|reconnectTimeout
return|;
block|}
DECL|method|setReconnectTimeout (Long reconnectTimeout)
specifier|public
name|void
name|setReconnectTimeout
parameter_list|(
name|Long
name|reconnectTimeout
parameter_list|)
block|{
name|this
operator|.
name|reconnectTimeout
operator|=
name|reconnectTimeout
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
DECL|class|MiloClientConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|MiloClientConfigurationNestedConfiguration
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
name|milo
operator|.
name|client
operator|.
name|MiloClientConfiguration
operator|.
name|class
decl_stmt|;
DECL|field|endpointUri
specifier|private
name|String
name|endpointUri
decl_stmt|;
comment|/**          * An alternative discovery URI          */
DECL|field|discoveryEndpointUri
specifier|private
name|String
name|discoveryEndpointUri
decl_stmt|;
comment|/**          * A suffix for endpoint URI when discovering          */
DECL|field|discoveryEndpointSuffix
specifier|private
name|String
name|discoveryEndpointSuffix
decl_stmt|;
comment|/**          * A virtual client id to force the creation of a new connection          * instance          */
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
comment|/**          * The application name          */
DECL|field|applicationName
specifier|private
name|String
name|applicationName
init|=
literal|"Apache Camel adapter for Eclipse Milo"
decl_stmt|;
comment|/**          * The application URI          */
DECL|field|applicationUri
specifier|private
name|String
name|applicationUri
init|=
literal|"http://camel.apache.org/EclipseMilo/Client"
decl_stmt|;
comment|/**          * The product URI          */
DECL|field|productUri
specifier|private
name|String
name|productUri
init|=
literal|"http://camel.apache.org/EclipseMilo"
decl_stmt|;
comment|/**          * Request timeout in milliseconds          */
DECL|field|requestTimeout
specifier|private
name|Long
name|requestTimeout
decl_stmt|;
comment|/**          * Channel lifetime in milliseconds          */
DECL|field|channelLifetime
specifier|private
name|Long
name|channelLifetime
decl_stmt|;
comment|/**          * Session name          */
DECL|field|sessionName
specifier|private
name|String
name|sessionName
decl_stmt|;
comment|/**          * Session timeout in milliseconds          */
DECL|field|sessionTimeout
specifier|private
name|Long
name|sessionTimeout
decl_stmt|;
comment|/**          * The maximum number of pending publish requests          */
DECL|field|maxPendingPublishRequests
specifier|private
name|Long
name|maxPendingPublishRequests
decl_stmt|;
comment|/**          * The maximum number of bytes a response message may have          */
DECL|field|maxResponseMessageSize
specifier|private
name|Long
name|maxResponseMessageSize
decl_stmt|;
comment|/**          * The key store type          */
DECL|field|keyStoreType
specifier|private
name|String
name|keyStoreType
decl_stmt|;
comment|/**          * The name of the key in the keystore file          */
DECL|field|keyAlias
specifier|private
name|String
name|keyAlias
decl_stmt|;
comment|/**          * The keystore password          */
DECL|field|keyStorePassword
specifier|private
name|String
name|keyStorePassword
decl_stmt|;
comment|/**          * The key password          */
DECL|field|keyPassword
specifier|private
name|String
name|keyPassword
decl_stmt|;
comment|/**          * A set of allowed security policy URIs. Default is to accept all and          * use the highest.          */
DECL|field|allowedSecurityPolicies
specifier|private
name|Set
name|allowedSecurityPolicies
decl_stmt|;
comment|/**          * Override the server reported endpoint host with the host from the          * endpoint URI.          */
DECL|field|overrideHost
specifier|private
name|Boolean
name|overrideHost
init|=
literal|false
decl_stmt|;
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
return|return
name|endpointUri
return|;
block|}
DECL|method|setEndpointUri (String endpointUri)
specifier|public
name|void
name|setEndpointUri
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|this
operator|.
name|endpointUri
operator|=
name|endpointUri
expr_stmt|;
block|}
DECL|method|getDiscoveryEndpointUri ()
specifier|public
name|String
name|getDiscoveryEndpointUri
parameter_list|()
block|{
return|return
name|discoveryEndpointUri
return|;
block|}
DECL|method|setDiscoveryEndpointUri (String discoveryEndpointUri)
specifier|public
name|void
name|setDiscoveryEndpointUri
parameter_list|(
name|String
name|discoveryEndpointUri
parameter_list|)
block|{
name|this
operator|.
name|discoveryEndpointUri
operator|=
name|discoveryEndpointUri
expr_stmt|;
block|}
DECL|method|getDiscoveryEndpointSuffix ()
specifier|public
name|String
name|getDiscoveryEndpointSuffix
parameter_list|()
block|{
return|return
name|discoveryEndpointSuffix
return|;
block|}
DECL|method|setDiscoveryEndpointSuffix (String discoveryEndpointSuffix)
specifier|public
name|void
name|setDiscoveryEndpointSuffix
parameter_list|(
name|String
name|discoveryEndpointSuffix
parameter_list|)
block|{
name|this
operator|.
name|discoveryEndpointSuffix
operator|=
name|discoveryEndpointSuffix
expr_stmt|;
block|}
DECL|method|getClientId ()
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
DECL|method|setClientId (String clientId)
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
block|}
DECL|method|getApplicationName ()
specifier|public
name|String
name|getApplicationName
parameter_list|()
block|{
return|return
name|applicationName
return|;
block|}
DECL|method|setApplicationName (String applicationName)
specifier|public
name|void
name|setApplicationName
parameter_list|(
name|String
name|applicationName
parameter_list|)
block|{
name|this
operator|.
name|applicationName
operator|=
name|applicationName
expr_stmt|;
block|}
DECL|method|getApplicationUri ()
specifier|public
name|String
name|getApplicationUri
parameter_list|()
block|{
return|return
name|applicationUri
return|;
block|}
DECL|method|setApplicationUri (String applicationUri)
specifier|public
name|void
name|setApplicationUri
parameter_list|(
name|String
name|applicationUri
parameter_list|)
block|{
name|this
operator|.
name|applicationUri
operator|=
name|applicationUri
expr_stmt|;
block|}
DECL|method|getProductUri ()
specifier|public
name|String
name|getProductUri
parameter_list|()
block|{
return|return
name|productUri
return|;
block|}
DECL|method|setProductUri (String productUri)
specifier|public
name|void
name|setProductUri
parameter_list|(
name|String
name|productUri
parameter_list|)
block|{
name|this
operator|.
name|productUri
operator|=
name|productUri
expr_stmt|;
block|}
DECL|method|getRequestTimeout ()
specifier|public
name|Long
name|getRequestTimeout
parameter_list|()
block|{
return|return
name|requestTimeout
return|;
block|}
DECL|method|setRequestTimeout (Long requestTimeout)
specifier|public
name|void
name|setRequestTimeout
parameter_list|(
name|Long
name|requestTimeout
parameter_list|)
block|{
name|this
operator|.
name|requestTimeout
operator|=
name|requestTimeout
expr_stmt|;
block|}
DECL|method|getChannelLifetime ()
specifier|public
name|Long
name|getChannelLifetime
parameter_list|()
block|{
return|return
name|channelLifetime
return|;
block|}
DECL|method|setChannelLifetime (Long channelLifetime)
specifier|public
name|void
name|setChannelLifetime
parameter_list|(
name|Long
name|channelLifetime
parameter_list|)
block|{
name|this
operator|.
name|channelLifetime
operator|=
name|channelLifetime
expr_stmt|;
block|}
DECL|method|getSessionName ()
specifier|public
name|String
name|getSessionName
parameter_list|()
block|{
return|return
name|sessionName
return|;
block|}
DECL|method|setSessionName (String sessionName)
specifier|public
name|void
name|setSessionName
parameter_list|(
name|String
name|sessionName
parameter_list|)
block|{
name|this
operator|.
name|sessionName
operator|=
name|sessionName
expr_stmt|;
block|}
DECL|method|getSessionTimeout ()
specifier|public
name|Long
name|getSessionTimeout
parameter_list|()
block|{
return|return
name|sessionTimeout
return|;
block|}
DECL|method|setSessionTimeout (Long sessionTimeout)
specifier|public
name|void
name|setSessionTimeout
parameter_list|(
name|Long
name|sessionTimeout
parameter_list|)
block|{
name|this
operator|.
name|sessionTimeout
operator|=
name|sessionTimeout
expr_stmt|;
block|}
DECL|method|getMaxPendingPublishRequests ()
specifier|public
name|Long
name|getMaxPendingPublishRequests
parameter_list|()
block|{
return|return
name|maxPendingPublishRequests
return|;
block|}
DECL|method|setMaxPendingPublishRequests (Long maxPendingPublishRequests)
specifier|public
name|void
name|setMaxPendingPublishRequests
parameter_list|(
name|Long
name|maxPendingPublishRequests
parameter_list|)
block|{
name|this
operator|.
name|maxPendingPublishRequests
operator|=
name|maxPendingPublishRequests
expr_stmt|;
block|}
DECL|method|getMaxResponseMessageSize ()
specifier|public
name|Long
name|getMaxResponseMessageSize
parameter_list|()
block|{
return|return
name|maxResponseMessageSize
return|;
block|}
DECL|method|setMaxResponseMessageSize (Long maxResponseMessageSize)
specifier|public
name|void
name|setMaxResponseMessageSize
parameter_list|(
name|Long
name|maxResponseMessageSize
parameter_list|)
block|{
name|this
operator|.
name|maxResponseMessageSize
operator|=
name|maxResponseMessageSize
expr_stmt|;
block|}
DECL|method|getKeyStoreType ()
specifier|public
name|String
name|getKeyStoreType
parameter_list|()
block|{
return|return
name|keyStoreType
return|;
block|}
DECL|method|setKeyStoreType (String keyStoreType)
specifier|public
name|void
name|setKeyStoreType
parameter_list|(
name|String
name|keyStoreType
parameter_list|)
block|{
name|this
operator|.
name|keyStoreType
operator|=
name|keyStoreType
expr_stmt|;
block|}
DECL|method|getKeyAlias ()
specifier|public
name|String
name|getKeyAlias
parameter_list|()
block|{
return|return
name|keyAlias
return|;
block|}
DECL|method|setKeyAlias (String keyAlias)
specifier|public
name|void
name|setKeyAlias
parameter_list|(
name|String
name|keyAlias
parameter_list|)
block|{
name|this
operator|.
name|keyAlias
operator|=
name|keyAlias
expr_stmt|;
block|}
DECL|method|getKeyStorePassword ()
specifier|public
name|String
name|getKeyStorePassword
parameter_list|()
block|{
return|return
name|keyStorePassword
return|;
block|}
DECL|method|setKeyStorePassword (String keyStorePassword)
specifier|public
name|void
name|setKeyStorePassword
parameter_list|(
name|String
name|keyStorePassword
parameter_list|)
block|{
name|this
operator|.
name|keyStorePassword
operator|=
name|keyStorePassword
expr_stmt|;
block|}
DECL|method|getKeyPassword ()
specifier|public
name|String
name|getKeyPassword
parameter_list|()
block|{
return|return
name|keyPassword
return|;
block|}
DECL|method|setKeyPassword (String keyPassword)
specifier|public
name|void
name|setKeyPassword
parameter_list|(
name|String
name|keyPassword
parameter_list|)
block|{
name|this
operator|.
name|keyPassword
operator|=
name|keyPassword
expr_stmt|;
block|}
DECL|method|getAllowedSecurityPolicies ()
specifier|public
name|Set
name|getAllowedSecurityPolicies
parameter_list|()
block|{
return|return
name|allowedSecurityPolicies
return|;
block|}
DECL|method|setAllowedSecurityPolicies (Set allowedSecurityPolicies)
specifier|public
name|void
name|setAllowedSecurityPolicies
parameter_list|(
name|Set
name|allowedSecurityPolicies
parameter_list|)
block|{
name|this
operator|.
name|allowedSecurityPolicies
operator|=
name|allowedSecurityPolicies
expr_stmt|;
block|}
DECL|method|getOverrideHost ()
specifier|public
name|Boolean
name|getOverrideHost
parameter_list|()
block|{
return|return
name|overrideHost
return|;
block|}
DECL|method|setOverrideHost (Boolean overrideHost)
specifier|public
name|void
name|setOverrideHost
parameter_list|(
name|Boolean
name|overrideHost
parameter_list|)
block|{
name|this
operator|.
name|overrideHost
operator|=
name|overrideHost
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

