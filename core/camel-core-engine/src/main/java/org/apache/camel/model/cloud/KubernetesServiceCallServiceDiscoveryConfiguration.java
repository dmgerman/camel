begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"routing,cloud,service-discovery"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"kubernetesServiceDiscovery"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|KubernetesServiceCallServiceDiscoveryConfiguration
specifier|public
class|class
name|KubernetesServiceCallServiceDiscoveryConfiguration
extends|extends
name|ServiceCallServiceDiscoveryConfiguration
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"environment"
argument_list|,
name|enums
operator|=
literal|"environment,dns,client"
argument_list|)
DECL|field|lookup
specifier|private
name|String
name|lookup
init|=
literal|"environment"
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"dns,dnssrv"
argument_list|)
DECL|field|dnsDomain
specifier|private
name|String
name|dnsDomain
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"dns,dnssrv"
argument_list|)
DECL|field|portName
specifier|private
name|String
name|portName
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"dns,dnssrv"
argument_list|)
DECL|field|portProtocol
specifier|private
name|String
name|portProtocol
init|=
literal|"tcp"
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|namespace
specifier|private
name|String
name|namespace
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|apiVersion
specifier|private
name|String
name|apiVersion
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"client"
argument_list|)
DECL|field|masterUrl
specifier|private
name|String
name|masterUrl
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"client"
argument_list|)
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"client"
argument_list|)
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"client"
argument_list|)
DECL|field|oauthToken
specifier|private
name|String
name|oauthToken
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"client"
argument_list|)
DECL|field|caCertData
specifier|private
name|String
name|caCertData
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"client"
argument_list|)
DECL|field|caCertFile
specifier|private
name|String
name|caCertFile
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"client"
argument_list|)
DECL|field|clientCertData
specifier|private
name|String
name|clientCertData
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"client"
argument_list|)
DECL|field|clientCertFile
specifier|private
name|String
name|clientCertFile
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"client"
argument_list|)
DECL|field|clientKeyAlgo
specifier|private
name|String
name|clientKeyAlgo
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"client"
argument_list|)
DECL|field|clientKeyData
specifier|private
name|String
name|clientKeyData
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"client"
argument_list|)
DECL|field|clientKeyFile
specifier|private
name|String
name|clientKeyFile
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"client"
argument_list|)
DECL|field|clientKeyPassphrase
specifier|private
name|String
name|clientKeyPassphrase
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"client"
argument_list|)
DECL|field|trustCerts
specifier|private
name|Boolean
name|trustCerts
decl_stmt|;
DECL|method|KubernetesServiceCallServiceDiscoveryConfiguration ()
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|KubernetesServiceCallServiceDiscoveryConfiguration (ServiceCallDefinition parent)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
parameter_list|(
name|ServiceCallDefinition
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
literal|"kubernetes-service-discovery"
argument_list|)
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Properties
comment|// *************************************************************************
DECL|method|getMasterUrl ()
specifier|public
name|String
name|getMasterUrl
parameter_list|()
block|{
return|return
name|masterUrl
return|;
block|}
comment|/**      * Sets the URL to the master when using client lookup      */
DECL|method|setMasterUrl (String masterUrl)
specifier|public
name|void
name|setMasterUrl
parameter_list|(
name|String
name|masterUrl
parameter_list|)
block|{
name|this
operator|.
name|masterUrl
operator|=
name|masterUrl
expr_stmt|;
block|}
DECL|method|getNamespace ()
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
comment|/**      * Sets the namespace to use. Will by default use namespace from the ENV      * variable KUBERNETES_MASTER.      */
DECL|method|setNamespace (String namespace)
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
DECL|method|getApiVersion ()
specifier|public
name|String
name|getApiVersion
parameter_list|()
block|{
return|return
name|apiVersion
return|;
block|}
comment|/**      * Sets the API version when using client lookup      */
DECL|method|setApiVersion (String apiVersion)
specifier|public
name|void
name|setApiVersion
parameter_list|(
name|String
name|apiVersion
parameter_list|)
block|{
name|this
operator|.
name|apiVersion
operator|=
name|apiVersion
expr_stmt|;
block|}
DECL|method|getLookup ()
specifier|public
name|String
name|getLookup
parameter_list|()
block|{
return|return
name|lookup
return|;
block|}
comment|/**      * How to perform service lookup. Possible values: client, dns, environment.      *<p/>      * When using client, then the client queries the kubernetes master to      * obtain a list of active pods that provides the service, and then random      * (or round robin) select a pod.      *<p/>      * When using dns the service name is resolved as      *<tt>name.namespace.svc.dnsDomain</tt>.      *<p/>      * When using dnssrv the service name is resolved with SRV query for      *<tt>_<port_name>._<port_proto>.<serviceName>.<namespace>.svc.<zone>.</tt>.      *<p/>      * When using environment then environment variables are used to lookup the      * service.      *<p/>      * By default environment is used.      */
DECL|method|setLookup (String lookup)
specifier|public
name|void
name|setLookup
parameter_list|(
name|String
name|lookup
parameter_list|)
block|{
name|this
operator|.
name|lookup
operator|=
name|lookup
expr_stmt|;
block|}
DECL|method|getDnsDomain ()
specifier|public
name|String
name|getDnsDomain
parameter_list|()
block|{
return|return
name|dnsDomain
return|;
block|}
comment|/**      * Sets the DNS domain to use for DNS lookup.      */
DECL|method|setDnsDomain (String dnsDomain)
specifier|public
name|void
name|setDnsDomain
parameter_list|(
name|String
name|dnsDomain
parameter_list|)
block|{
name|this
operator|.
name|dnsDomain
operator|=
name|dnsDomain
expr_stmt|;
block|}
DECL|method|getPortName ()
specifier|public
name|String
name|getPortName
parameter_list|()
block|{
return|return
name|portName
return|;
block|}
comment|/**      * Sets the Port Name to use for DNS/DNSSRV lookup.      */
DECL|method|setPortName (String portName)
specifier|public
name|void
name|setPortName
parameter_list|(
name|String
name|portName
parameter_list|)
block|{
name|this
operator|.
name|portName
operator|=
name|portName
expr_stmt|;
block|}
DECL|method|getPortProtocol ()
specifier|public
name|String
name|getPortProtocol
parameter_list|()
block|{
return|return
name|portProtocol
return|;
block|}
comment|/**      * Sets the Port Protocol to use for DNS/DNSSRV lookup.      */
DECL|method|setPortProtocol (String portProtocol)
specifier|public
name|void
name|setPortProtocol
parameter_list|(
name|String
name|portProtocol
parameter_list|)
block|{
name|this
operator|.
name|portProtocol
operator|=
name|portProtocol
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/**      * Sets the username for authentication when using client lookup      */
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * Sets the password for authentication when using client lookup      */
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getOauthToken ()
specifier|public
name|String
name|getOauthToken
parameter_list|()
block|{
return|return
name|oauthToken
return|;
block|}
comment|/**      * Sets the OAUTH token for authentication (instead of username/password)      * when using client lookup      */
DECL|method|setOauthToken (String oauthToken)
specifier|public
name|void
name|setOauthToken
parameter_list|(
name|String
name|oauthToken
parameter_list|)
block|{
name|this
operator|.
name|oauthToken
operator|=
name|oauthToken
expr_stmt|;
block|}
DECL|method|getCaCertData ()
specifier|public
name|String
name|getCaCertData
parameter_list|()
block|{
return|return
name|caCertData
return|;
block|}
comment|/**      * Sets the Certificate Authority data when using client lookup      */
DECL|method|setCaCertData (String caCertData)
specifier|public
name|void
name|setCaCertData
parameter_list|(
name|String
name|caCertData
parameter_list|)
block|{
name|this
operator|.
name|caCertData
operator|=
name|caCertData
expr_stmt|;
block|}
DECL|method|getCaCertFile ()
specifier|public
name|String
name|getCaCertFile
parameter_list|()
block|{
return|return
name|caCertFile
return|;
block|}
comment|/**      * Sets the Certificate Authority data that are loaded from the file when      * using client lookup      */
DECL|method|setCaCertFile (String caCertFile)
specifier|public
name|void
name|setCaCertFile
parameter_list|(
name|String
name|caCertFile
parameter_list|)
block|{
name|this
operator|.
name|caCertFile
operator|=
name|caCertFile
expr_stmt|;
block|}
DECL|method|getClientCertData ()
specifier|public
name|String
name|getClientCertData
parameter_list|()
block|{
return|return
name|clientCertData
return|;
block|}
comment|/**      * Sets the Client Certificate data when using client lookup      */
DECL|method|setClientCertData (String clientCertData)
specifier|public
name|void
name|setClientCertData
parameter_list|(
name|String
name|clientCertData
parameter_list|)
block|{
name|this
operator|.
name|clientCertData
operator|=
name|clientCertData
expr_stmt|;
block|}
DECL|method|getClientCertFile ()
specifier|public
name|String
name|getClientCertFile
parameter_list|()
block|{
return|return
name|clientCertFile
return|;
block|}
comment|/**      * Sets the Client Certificate data that are loaded from the file when using      * client lookup      */
DECL|method|setClientCertFile (String clientCertFile)
specifier|public
name|void
name|setClientCertFile
parameter_list|(
name|String
name|clientCertFile
parameter_list|)
block|{
name|this
operator|.
name|clientCertFile
operator|=
name|clientCertFile
expr_stmt|;
block|}
DECL|method|getClientKeyAlgo ()
specifier|public
name|String
name|getClientKeyAlgo
parameter_list|()
block|{
return|return
name|clientKeyAlgo
return|;
block|}
comment|/**      * Sets the Client Keystore algorithm, such as RSA when using client lookup      */
DECL|method|setClientKeyAlgo (String clientKeyAlgo)
specifier|public
name|void
name|setClientKeyAlgo
parameter_list|(
name|String
name|clientKeyAlgo
parameter_list|)
block|{
name|this
operator|.
name|clientKeyAlgo
operator|=
name|clientKeyAlgo
expr_stmt|;
block|}
DECL|method|getClientKeyData ()
specifier|public
name|String
name|getClientKeyData
parameter_list|()
block|{
return|return
name|clientKeyData
return|;
block|}
comment|/**      * Sets the Client Keystore data when using client lookup      */
DECL|method|setClientKeyData (String clientKeyData)
specifier|public
name|void
name|setClientKeyData
parameter_list|(
name|String
name|clientKeyData
parameter_list|)
block|{
name|this
operator|.
name|clientKeyData
operator|=
name|clientKeyData
expr_stmt|;
block|}
DECL|method|getClientKeyFile ()
specifier|public
name|String
name|getClientKeyFile
parameter_list|()
block|{
return|return
name|clientKeyFile
return|;
block|}
comment|/**      * Sets the Client Keystore data that are loaded from the file when using      * client lookup      */
DECL|method|setClientKeyFile (String clientKeyFile)
specifier|public
name|void
name|setClientKeyFile
parameter_list|(
name|String
name|clientKeyFile
parameter_list|)
block|{
name|this
operator|.
name|clientKeyFile
operator|=
name|clientKeyFile
expr_stmt|;
block|}
DECL|method|getClientKeyPassphrase ()
specifier|public
name|String
name|getClientKeyPassphrase
parameter_list|()
block|{
return|return
name|clientKeyPassphrase
return|;
block|}
comment|/**      * Sets the Client Keystore passphrase when using client lookup      */
DECL|method|setClientKeyPassphrase (String clientKeyPassphrase)
specifier|public
name|void
name|setClientKeyPassphrase
parameter_list|(
name|String
name|clientKeyPassphrase
parameter_list|)
block|{
name|this
operator|.
name|clientKeyPassphrase
operator|=
name|clientKeyPassphrase
expr_stmt|;
block|}
DECL|method|getTrustCerts ()
specifier|public
name|Boolean
name|getTrustCerts
parameter_list|()
block|{
return|return
name|trustCerts
return|;
block|}
comment|/**      * Sets whether to turn on trust certificate check when using client lookup      */
DECL|method|setTrustCerts (Boolean trustCerts)
specifier|public
name|void
name|setTrustCerts
parameter_list|(
name|Boolean
name|trustCerts
parameter_list|)
block|{
name|this
operator|.
name|trustCerts
operator|=
name|trustCerts
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Fluent API
comment|// *************************************************************************
comment|/**      * Sets the URL to the master when using client lookup      */
DECL|method|masterUrl (String masterUrl)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|masterUrl
parameter_list|(
name|String
name|masterUrl
parameter_list|)
block|{
name|setMasterUrl
argument_list|(
name|masterUrl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the namespace to use. Will by default use namespace from the ENV      * variable KUBERNETES_MASTER.      */
DECL|method|namespace (String namespace)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|namespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|setNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the API version when using client lookup      */
DECL|method|apiVersion (String apiVersion)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|apiVersion
parameter_list|(
name|String
name|apiVersion
parameter_list|)
block|{
name|setApiVersion
argument_list|(
name|apiVersion
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * How to perform service lookup, @see {@link #setLookup(String)}.      */
DECL|method|lookup (String lookup)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|lookup
parameter_list|(
name|String
name|lookup
parameter_list|)
block|{
name|setLookup
argument_list|(
name|lookup
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the DNS domain to use for DNS/SNDSRV lookup.      */
DECL|method|dnsDomain (String dnsDomain)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|dnsDomain
parameter_list|(
name|String
name|dnsDomain
parameter_list|)
block|{
name|setDnsDomain
argument_list|(
name|dnsDomain
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets Port Name to use for DNS/SNDSRV lookup.      */
DECL|method|portName (String portName)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|portName
parameter_list|(
name|String
name|portName
parameter_list|)
block|{
name|setPortName
argument_list|(
name|portName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets Port Protocol to use for DNS/SNDSRV lookup.      */
DECL|method|portProtocol (String portProtocol)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|portProtocol
parameter_list|(
name|String
name|portProtocol
parameter_list|)
block|{
name|setPortProtocol
argument_list|(
name|portProtocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the username for authentication when using client lookup      */
DECL|method|username (String username)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|username
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the password for authentication when using client lookup      */
DECL|method|password (String password)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the OAUTH token for authentication (instead of username/password)      * when using client lookup      */
DECL|method|oauthToken (String oauthToken)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|oauthToken
parameter_list|(
name|String
name|oauthToken
parameter_list|)
block|{
name|setOauthToken
argument_list|(
name|oauthToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the Certificate Authority data when using client lookup      */
DECL|method|caCertData (String caCertData)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|caCertData
parameter_list|(
name|String
name|caCertData
parameter_list|)
block|{
name|setCaCertData
argument_list|(
name|caCertData
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the Certificate Authority data that are loaded from the file when      * using client lookup      */
DECL|method|caCertFile (String caCertFile)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|caCertFile
parameter_list|(
name|String
name|caCertFile
parameter_list|)
block|{
name|setCaCertFile
argument_list|(
name|caCertFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the Client Certificate data when using client lookup      */
DECL|method|clientCertData (String clientCertData)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|clientCertData
parameter_list|(
name|String
name|clientCertData
parameter_list|)
block|{
name|setClientCertData
argument_list|(
name|clientCertData
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the Client Certificate data that are loaded from the file when using      * client lookup      */
DECL|method|clientCertFile (String clientCertFile)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|clientCertFile
parameter_list|(
name|String
name|clientCertFile
parameter_list|)
block|{
name|setClientCertFile
argument_list|(
name|clientCertFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the Client Keystore algorithm, such as RSA when using client lookup      */
DECL|method|clientKeyAlgo (String clientKeyAlgo)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|clientKeyAlgo
parameter_list|(
name|String
name|clientKeyAlgo
parameter_list|)
block|{
name|setClientKeyAlgo
argument_list|(
name|clientKeyAlgo
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the Client Keystore data when using client lookup      */
DECL|method|clientKeyData (String clientKeyData)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|clientKeyData
parameter_list|(
name|String
name|clientKeyData
parameter_list|)
block|{
name|setClientKeyData
argument_list|(
name|clientKeyData
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the Client Keystore data that are loaded from the file when using      * client lookup      */
DECL|method|clientKeyFile (String clientKeyFile)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|clientKeyFile
parameter_list|(
name|String
name|clientKeyFile
parameter_list|)
block|{
name|setClientKeyFile
argument_list|(
name|clientKeyFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the Client Keystore passphrase when using client lookup      */
DECL|method|clientKeyPassphrase (String clientKeyPassphrase)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|clientKeyPassphrase
parameter_list|(
name|String
name|clientKeyPassphrase
parameter_list|)
block|{
name|setClientKeyPassphrase
argument_list|(
name|clientKeyPassphrase
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether to turn on trust certificate check when using client lookup      */
DECL|method|trustCerts (boolean trustCerts)
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|trustCerts
parameter_list|(
name|boolean
name|trustCerts
parameter_list|)
block|{
name|setTrustCerts
argument_list|(
name|trustCerts
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

