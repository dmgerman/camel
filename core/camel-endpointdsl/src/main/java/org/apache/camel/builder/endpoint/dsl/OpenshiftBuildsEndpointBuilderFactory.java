begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The Openshift Builds component provides a producer to execute openshift build  * operations.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|OpenshiftBuildsEndpointBuilderFactory
specifier|public
interface|interface
name|OpenshiftBuildsEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Openshift Builds component.      */
DECL|interface|OpenshiftBuildsEndpointBuilder
specifier|public
specifier|static
interface|interface
name|OpenshiftBuildsEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|public
specifier|default
name|AdvancedOpenshiftBuildsEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedOpenshiftBuildsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Kubernetes Master url.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|masterUrl (String masterUrl)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|masterUrl
parameter_list|(
name|String
name|masterUrl
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"masterUrl"
argument_list|,
name|masterUrl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The Kubernetes API Version to use.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|apiVersion ( String apiVersion)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|apiVersion
parameter_list|(
name|String
name|apiVersion
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"apiVersion"
argument_list|,
name|apiVersion
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The dns domain, used for ServiceCall EIP.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|dnsDomain (String dnsDomain)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|dnsDomain
parameter_list|(
name|String
name|dnsDomain
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"dnsDomain"
argument_list|,
name|dnsDomain
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Default KubernetesClient to use if provided.          * The option is a          *<code>io.fabric8.kubernetes.client.KubernetesClient</code> type.          * @group producer          */
DECL|method|kubernetesClient ( Object kubernetesClient)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|kubernetesClient
parameter_list|(
name|Object
name|kubernetesClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"kubernetesClient"
argument_list|,
name|kubernetesClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Default KubernetesClient to use if provided.          * The option will be converted to a          *<code>io.fabric8.kubernetes.client.KubernetesClient</code> type.          * @group producer          */
DECL|method|kubernetesClient ( String kubernetesClient)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|kubernetesClient
parameter_list|(
name|String
name|kubernetesClient
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"kubernetesClient"
argument_list|,
name|kubernetesClient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The port name, used for ServiceCall EIP.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|portName (String portName)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|portName
parameter_list|(
name|String
name|portName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"portName"
argument_list|,
name|portName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The port protocol, used for ServiceCall EIP.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|portProtocol ( String portProtocol)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|portProtocol
parameter_list|(
name|String
name|portProtocol
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"portProtocol"
argument_list|,
name|portProtocol
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The CA Cert Data.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|caCertData ( String caCertData)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|caCertData
parameter_list|(
name|String
name|caCertData
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"caCertData"
argument_list|,
name|caCertData
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The CA Cert File.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|caCertFile ( String caCertFile)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|caCertFile
parameter_list|(
name|String
name|caCertFile
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"caCertFile"
argument_list|,
name|caCertFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The Client Cert Data.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|clientCertData ( String clientCertData)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|clientCertData
parameter_list|(
name|String
name|clientCertData
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientCertData"
argument_list|,
name|clientCertData
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The Client Cert File.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|clientCertFile ( String clientCertFile)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|clientCertFile
parameter_list|(
name|String
name|clientCertFile
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientCertFile"
argument_list|,
name|clientCertFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The Key Algorithm used by the client.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|clientKeyAlgo ( String clientKeyAlgo)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|clientKeyAlgo
parameter_list|(
name|String
name|clientKeyAlgo
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientKeyAlgo"
argument_list|,
name|clientKeyAlgo
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The Client Key data.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|clientKeyData ( String clientKeyData)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|clientKeyData
parameter_list|(
name|String
name|clientKeyData
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientKeyData"
argument_list|,
name|clientKeyData
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The Client Key file.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|clientKeyFile ( String clientKeyFile)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|clientKeyFile
parameter_list|(
name|String
name|clientKeyFile
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientKeyFile"
argument_list|,
name|clientKeyFile
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The Client Key Passphrase.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|clientKeyPassphrase ( String clientKeyPassphrase)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|clientKeyPassphrase
parameter_list|(
name|String
name|clientKeyPassphrase
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientKeyPassphrase"
argument_list|,
name|clientKeyPassphrase
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The Auth Token.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|oauthToken ( String oauthToken)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|oauthToken
parameter_list|(
name|String
name|oauthToken
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"oauthToken"
argument_list|,
name|oauthToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Password to connect to Kubernetes.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|password (String password)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define if the certs we used are trusted anyway or not.          * The option is a<code>java.lang.Boolean</code> type.          * @group security          */
DECL|method|trustCerts ( Boolean trustCerts)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|trustCerts
parameter_list|(
name|Boolean
name|trustCerts
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"trustCerts"
argument_list|,
name|trustCerts
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define if the certs we used are trusted anyway or not.          * The option will be converted to a<code>java.lang.Boolean</code>          * type.          * @group security          */
DECL|method|trustCerts ( String trustCerts)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|trustCerts
parameter_list|(
name|String
name|trustCerts
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"trustCerts"
argument_list|,
name|trustCerts
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Username to connect to Kubernetes.          * The option is a<code>java.lang.String</code> type.          * @group security          */
DECL|method|username (String username)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|username
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Openshift Builds component.      */
DECL|interface|AdvancedOpenshiftBuildsEndpointBuilder
specifier|public
specifier|static
interface|interface
name|AdvancedOpenshiftBuildsEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|OpenshiftBuildsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|public
specifier|default
name|AdvancedOpenshiftBuildsEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|public
specifier|default
name|AdvancedOpenshiftBuildsEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Connection timeout in milliseconds to use when making requests to the          * Kubernetes API server.          * The option is a<code>java.lang.Integer</code> type.          * @group advanced          */
DECL|method|connectionTimeout ( Integer connectionTimeout)
specifier|public
specifier|default
name|AdvancedOpenshiftBuildsEndpointBuilder
name|connectionTimeout
parameter_list|(
name|Integer
name|connectionTimeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"connectionTimeout"
argument_list|,
name|connectionTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Connection timeout in milliseconds to use when making requests to the          * Kubernetes API server.          * The option will be converted to a<code>java.lang.Integer</code>          * type.          * @group advanced          */
DECL|method|connectionTimeout ( String connectionTimeout)
specifier|public
specifier|default
name|AdvancedOpenshiftBuildsEndpointBuilder
name|connectionTimeout
parameter_list|(
name|String
name|connectionTimeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"connectionTimeout"
argument_list|,
name|connectionTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|public
specifier|default
name|AdvancedOpenshiftBuildsEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( String synchronous)
specifier|public
specifier|default
name|AdvancedOpenshiftBuildsEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * The Openshift Builds component provides a producer to execute openshift      * build operations. Creates a builder to build endpoints for the Openshift      * Builds component.      */
DECL|method|openshiftBuilds (String path)
specifier|public
specifier|default
name|OpenshiftBuildsEndpointBuilder
name|openshiftBuilds
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|OpenshiftBuildsEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|OpenshiftBuildsEndpointBuilder
implements|,
name|AdvancedOpenshiftBuildsEndpointBuilder
block|{
specifier|public
name|OpenshiftBuildsEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"openshift-builds"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|OpenshiftBuildsEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

