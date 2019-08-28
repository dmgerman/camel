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
comment|/**  * The Kubernetes Persistent Volumes component provides a producer to execute  * kubernetes persistent volume operations.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|KubernetesPersistentVolumesEndpointBuilderFactory
specifier|public
interface|interface
name|KubernetesPersistentVolumesEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Kubernetes Persistent Volume component.      */
DECL|interface|KubernetesPersistentVolumesEndpointBuilder
specifier|public
interface|interface
name|KubernetesPersistentVolumesEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedKubernetesPersistentVolumesEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedKubernetesPersistentVolumesEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The Kubernetes API Version to use.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|apiVersion ( String apiVersion)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|apiVersion
parameter_list|(
name|String
name|apiVersion
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The dns domain, used for ServiceCall EIP.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|dnsDomain ( String dnsDomain)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|dnsDomain
parameter_list|(
name|String
name|dnsDomain
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Default KubernetesClient to use if provided.          *           * The option is a:          *<code>io.fabric8.kubernetes.client.KubernetesClient</code> type.          *           * Group: producer          */
DECL|method|kubernetesClient ( Object kubernetesClient)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|kubernetesClient
parameter_list|(
name|Object
name|kubernetesClient
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Default KubernetesClient to use if provided.          *           * The option will be converted to a          *<code>io.fabric8.kubernetes.client.KubernetesClient</code> type.          *           * Group: producer          */
DECL|method|kubernetesClient ( String kubernetesClient)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|kubernetesClient
parameter_list|(
name|String
name|kubernetesClient
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The port name, used for ServiceCall EIP.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|portName ( String portName)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|portName
parameter_list|(
name|String
name|portName
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The port protocol, used for ServiceCall EIP.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|portProtocol ( String portProtocol)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|portProtocol
parameter_list|(
name|String
name|portProtocol
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The CA Cert Data.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|caCertData ( String caCertData)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|caCertData
parameter_list|(
name|String
name|caCertData
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The CA Cert File.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|caCertFile ( String caCertFile)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|caCertFile
parameter_list|(
name|String
name|caCertFile
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The Client Cert Data.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|clientCertData ( String clientCertData)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|clientCertData
parameter_list|(
name|String
name|clientCertData
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The Client Cert File.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|clientCertFile ( String clientCertFile)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|clientCertFile
parameter_list|(
name|String
name|clientCertFile
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The Key Algorithm used by the client.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|clientKeyAlgo ( String clientKeyAlgo)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|clientKeyAlgo
parameter_list|(
name|String
name|clientKeyAlgo
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The Client Key data.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|clientKeyData ( String clientKeyData)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|clientKeyData
parameter_list|(
name|String
name|clientKeyData
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The Client Key file.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|clientKeyFile ( String clientKeyFile)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|clientKeyFile
parameter_list|(
name|String
name|clientKeyFile
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The Client Key Passphrase.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|clientKeyPassphrase ( String clientKeyPassphrase)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|clientKeyPassphrase
parameter_list|(
name|String
name|clientKeyPassphrase
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * The Auth Token.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|oauthToken ( String oauthToken)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|oauthToken
parameter_list|(
name|String
name|oauthToken
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Password to connect to Kubernetes.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|password ( String password)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Define if the certs we used are trusted anyway or not.          *           * The option is a:<code>java.lang.Boolean</code> type.          *           * Group: security          */
DECL|method|trustCerts ( Boolean trustCerts)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|trustCerts
parameter_list|(
name|Boolean
name|trustCerts
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Define if the certs we used are trusted anyway or not.          *           * The option will be converted to a<code>java.lang.Boolean</code>          * type.          *           * Group: security          */
DECL|method|trustCerts ( String trustCerts)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|trustCerts
parameter_list|(
name|String
name|trustCerts
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Username to connect to Kubernetes.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|username ( String username)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|username
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|doSetProperty
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
comment|/**      * Advanced builder for endpoint for the Kubernetes Persistent Volume      * component.      */
DECL|interface|AdvancedKubernetesPersistentVolumesEndpointBuilder
specifier|public
interface|interface
name|AdvancedKubernetesPersistentVolumesEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|KubernetesPersistentVolumesEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedKubernetesPersistentVolumesEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedKubernetesPersistentVolumesEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Connection timeout in milliseconds to use when making requests to the          * Kubernetes API server.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: advanced          */
DECL|method|connectionTimeout ( Integer connectionTimeout)
specifier|default
name|AdvancedKubernetesPersistentVolumesEndpointBuilder
name|connectionTimeout
parameter_list|(
name|Integer
name|connectionTimeout
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Connection timeout in milliseconds to use when making requests to the          * Kubernetes API server.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: advanced          */
DECL|method|connectionTimeout ( String connectionTimeout)
specifier|default
name|AdvancedKubernetesPersistentVolumesEndpointBuilder
name|connectionTimeout
parameter_list|(
name|String
name|connectionTimeout
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedKubernetesPersistentVolumesEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedKubernetesPersistentVolumesEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
comment|/**      * Kubernetes Persistent Volume (camel-kubernetes)      * The Kubernetes Persistent Volumes component provides a producer to      * execute kubernetes persistent volume operations.      *       * Category: container,cloud,paas      * Available as of version: 2.17      * Maven coordinates: org.apache.camel:camel-kubernetes      *       * Syntax:<code>kubernetes-persistent-volumes:masterUrl</code>      *       * Path parameter: masterUrl (required)      * Kubernetes Master url      */
DECL|method|kubernetesPersistentVolumes ( String path)
specifier|default
name|KubernetesPersistentVolumesEndpointBuilder
name|kubernetesPersistentVolumes
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|KubernetesPersistentVolumesEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|KubernetesPersistentVolumesEndpointBuilder
implements|,
name|AdvancedKubernetesPersistentVolumesEndpointBuilder
block|{
specifier|public
name|KubernetesPersistentVolumesEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"kubernetes-persistent-volumes"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|KubernetesPersistentVolumesEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

