begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.cloud.springboot
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
name|Map
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

begin_comment
comment|/**  * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
DECL|class|KubernetesServiceCallServiceDiscoveryConfigurationCommon
specifier|public
class|class
name|KubernetesServiceCallServiceDiscoveryConfigurationCommon
block|{
comment|/**      * How to perform service lookup. Possible values: client, dns, environment.      * When using client, then the client queries the kubernetes master to      * obtain a list of active pods that provides the service, and then random      * (or round robin) select a pod. When using dns the service name is      * resolved as name.namespace.svc.dnsDomain. When using dnssrv the service      * name is resolved with SRV query for _._...svc... When using environment      * then environment variables are used to lookup the service. By default      * environment is used.      */
DECL|field|lookup
specifier|private
name|String
name|lookup
init|=
literal|"environment"
decl_stmt|;
comment|/**      * Sets the DNS domain to use for DNS lookup.      */
DECL|field|dnsDomain
specifier|private
name|String
name|dnsDomain
decl_stmt|;
comment|/**      * Sets the Port Name to use for DNS/DNSSRV lookup.      */
DECL|field|portName
specifier|private
name|String
name|portName
decl_stmt|;
comment|/**      * Sets the Port Protocol to use for DNS/DNSSRV lookup.      */
DECL|field|portProtocol
specifier|private
name|String
name|portProtocol
decl_stmt|;
comment|/**      * Sets the namespace to use. Will by default use namespace from the ENV      * variable KUBERNETES_MASTER.      */
DECL|field|namespace
specifier|private
name|String
name|namespace
decl_stmt|;
comment|/**      * Sets the API version when using client lookup      */
DECL|field|apiVersion
specifier|private
name|String
name|apiVersion
decl_stmt|;
comment|/**      * Sets the URL to the master when using client lookup      */
DECL|field|masterUrl
specifier|private
name|String
name|masterUrl
decl_stmt|;
comment|/**      * Sets the username for authentication when using client lookup      */
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
comment|/**      * Sets the password for authentication when using client lookup      */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**      * Sets the OAUTH token for authentication (instead of username/password)      * when using client lookup      */
DECL|field|oauthToken
specifier|private
name|String
name|oauthToken
decl_stmt|;
comment|/**      * Sets the Certificate Authority data when using client lookup      */
DECL|field|caCertData
specifier|private
name|String
name|caCertData
decl_stmt|;
comment|/**      * Sets the Certificate Authority data that are loaded from the file when      * using client lookup      */
DECL|field|caCertFile
specifier|private
name|String
name|caCertFile
decl_stmt|;
comment|/**      * Sets the Client Certificate data when using client lookup      */
DECL|field|clientCertData
specifier|private
name|String
name|clientCertData
decl_stmt|;
comment|/**      * Sets the Client Certificate data that are loaded from the file when using      * client lookup      */
DECL|field|clientCertFile
specifier|private
name|String
name|clientCertFile
decl_stmt|;
comment|/**      * Sets the Client Keystore algorithm, such as RSA when using client lookup      */
DECL|field|clientKeyAlgo
specifier|private
name|String
name|clientKeyAlgo
decl_stmt|;
comment|/**      * Sets the Client Keystore data when using client lookup      */
DECL|field|clientKeyData
specifier|private
name|String
name|clientKeyData
decl_stmt|;
comment|/**      * Sets the Client Keystore data that are loaded from the file when using      * client lookup      */
DECL|field|clientKeyFile
specifier|private
name|String
name|clientKeyFile
decl_stmt|;
comment|/**      * Sets the Client Keystore passphrase when using client lookup      */
DECL|field|clientKeyPassphrase
specifier|private
name|String
name|clientKeyPassphrase
decl_stmt|;
comment|/**      * Sets whether to turn on trust certificate check when using client lookup      */
DECL|field|trustCerts
specifier|private
name|Boolean
name|trustCerts
init|=
literal|false
decl_stmt|;
comment|/**      * Set client properties to use. These properties are specific to what      * service call implementation are in use. For example if using ribbon, then      * the client properties are define in      * com.netflix.client.config.CommonClientConfigKey.      */
DECL|field|properties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
decl_stmt|;
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
DECL|method|getProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|setProperties (Map<String, String> properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
block|}
end_class

end_unit

