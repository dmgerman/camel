begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|api
operator|.
name|model
operator|.
name|EndpointAddress
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|api
operator|.
name|model
operator|.
name|EndpointPort
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|api
operator|.
name|model
operator|.
name|EndpointSubset
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|api
operator|.
name|model
operator|.
name|Endpoints
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|AutoAdaptableKubernetesClient
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|ConfigBuilder
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
name|cloud
operator|.
name|ServiceDefinition
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
name|kubernetes
operator|.
name|KubernetesConfiguration
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
name|impl
operator|.
name|cloud
operator|.
name|DefaultServiceDefinition
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
name|util
operator|.
name|IOHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|KubernetesClientServiceDiscovery
specifier|public
class|class
name|KubernetesClientServiceDiscovery
extends|extends
name|KubernetesServiceDiscovery
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|KubernetesClientServiceDiscovery
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|namespace
specifier|private
specifier|final
name|String
name|namespace
decl_stmt|;
DECL|field|portName
specifier|private
specifier|final
name|String
name|portName
decl_stmt|;
DECL|field|client
specifier|private
name|AutoAdaptableKubernetesClient
name|client
decl_stmt|;
DECL|method|KubernetesClientServiceDiscovery (KubernetesConfiguration configuration)
specifier|public
name|KubernetesClientServiceDiscovery
parameter_list|(
name|KubernetesConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|configuration
operator|.
name|getNamespace
argument_list|()
operator|!=
literal|null
condition|?
name|configuration
operator|.
name|getNamespace
argument_list|()
else|:
name|System
operator|.
name|getenv
argument_list|(
literal|"KUBERNETES_NAMESPACE"
argument_list|)
expr_stmt|;
name|this
operator|.
name|portName
operator|=
name|configuration
operator|.
name|getPortName
argument_list|()
expr_stmt|;
name|this
operator|.
name|client
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getServices (String name)
specifier|public
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|getServices
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Discovering endpoints from namespace: {} with name: {}"
argument_list|,
name|this
operator|.
name|namespace
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|Endpoints
name|endpoints
init|=
name|client
operator|.
name|endpoints
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|this
operator|.
name|namespace
argument_list|)
operator|.
name|withName
argument_list|(
name|name
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoints
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found {} endpoints in namespace: {} for name: {} and portName: {}"
argument_list|,
name|endpoints
operator|.
name|getSubsets
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|this
operator|.
name|namespace
argument_list|,
name|name
argument_list|,
name|this
operator|.
name|portName
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|EndpointSubset
name|subset
range|:
name|endpoints
operator|.
name|getSubsets
argument_list|()
control|)
block|{
if|if
condition|(
name|subset
operator|.
name|getPorts
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|addServers
argument_list|(
name|name
argument_list|,
name|result
argument_list|,
name|subset
operator|.
name|getPorts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|subset
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|List
argument_list|<
name|EndpointPort
argument_list|>
name|ports
init|=
name|subset
operator|.
name|getPorts
argument_list|()
decl_stmt|;
specifier|final
name|int
name|portSize
init|=
name|ports
operator|.
name|size
argument_list|()
decl_stmt|;
name|EndpointPort
name|port
decl_stmt|;
for|for
control|(
name|int
name|p
init|=
literal|0
init|;
name|p
operator|<
name|portSize
condition|;
name|p
operator|++
control|)
block|{
name|port
operator|=
name|ports
operator|.
name|get
argument_list|(
name|p
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|this
operator|.
name|portName
argument_list|)
operator|||
name|this
operator|.
name|portName
operator|.
name|endsWith
argument_list|(
name|port
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|addServers
argument_list|(
name|name
argument_list|,
name|result
argument_list|,
name|port
argument_list|,
name|subset
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|addServers (String name, List<ServiceDefinition> servers, EndpointPort port, EndpointSubset subset)
specifier|protected
name|void
name|addServers
parameter_list|(
name|String
name|name
parameter_list|,
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|servers
parameter_list|,
name|EndpointPort
name|port
parameter_list|,
name|EndpointSubset
name|subset
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|EndpointAddress
argument_list|>
name|addresses
init|=
name|subset
operator|.
name|getAddresses
argument_list|()
decl_stmt|;
specifier|final
name|int
name|size
init|=
name|addresses
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|servers
operator|.
name|add
argument_list|(
operator|new
name|DefaultServiceDefinition
argument_list|(
name|name
argument_list|,
name|addresses
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getIp
argument_list|()
argument_list|,
name|port
operator|.
name|getPort
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
specifier|final
name|KubernetesConfiguration
name|configuration
init|=
name|getConfiguration
argument_list|()
decl_stmt|;
name|ConfigBuilder
name|builder
init|=
operator|new
name|ConfigBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|withMasterUrl
argument_list|(
name|configuration
operator|.
name|getMasterUrl
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getUsername
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
operator|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getOauthToken
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withUsername
argument_list|(
name|configuration
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|withPassword
argument_list|(
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|withOauthToken
argument_list|(
name|configuration
operator|.
name|getOauthToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getCaCertData
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withCaCertData
argument_list|(
name|configuration
operator|.
name|getCaCertData
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getCaCertFile
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withCaCertFile
argument_list|(
name|configuration
operator|.
name|getCaCertFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getClientCertData
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withClientCertData
argument_list|(
name|configuration
operator|.
name|getClientCertData
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getClientCertFile
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withClientCertFile
argument_list|(
name|configuration
operator|.
name|getClientCertFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getApiVersion
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withApiVersion
argument_list|(
name|configuration
operator|.
name|getApiVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getClientKeyAlgo
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withClientKeyAlgo
argument_list|(
name|configuration
operator|.
name|getClientKeyAlgo
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getClientKeyData
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withClientKeyData
argument_list|(
name|configuration
operator|.
name|getClientKeyData
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getClientKeyFile
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withClientKeyFile
argument_list|(
name|configuration
operator|.
name|getClientKeyFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getClientKeyPassphrase
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withClientKeyPassphrase
argument_list|(
name|configuration
operator|.
name|getClientKeyPassphrase
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getTrustCerts
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withTrustCerts
argument_list|(
name|configuration
operator|.
name|getTrustCerts
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|client
operator|=
operator|new
name|AutoAdaptableKubernetesClient
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|client
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"KubernetesClientServiceDiscovery{"
operator|+
literal|"namespace='"
operator|+
name|namespace
operator|+
literal|'\''
operator|+
literal|", portName='"
operator|+
name|portName
operator|+
literal|'\''
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

