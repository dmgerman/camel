begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes
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
package|;
end_package

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
name|Config
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
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|DefaultKubernetesClient
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
name|KubernetesClient
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

begin_comment
comment|/**  * Helper moethods for Kubernetes resources.  */
end_comment

begin_class
DECL|class|KubernetesHelper
specifier|public
specifier|final
class|class
name|KubernetesHelper
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
name|KubernetesHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|KubernetesHelper ()
specifier|private
name|KubernetesHelper
parameter_list|()
block|{     }
DECL|method|getKubernetesClient (KubernetesConfiguration configuration)
specifier|public
specifier|static
name|KubernetesClient
name|getKubernetesClient
parameter_list|(
name|KubernetesConfiguration
name|configuration
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|.
name|getKubernetesClient
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|configuration
operator|.
name|getKubernetesClient
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|configuration
operator|.
name|getMasterUrl
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|createKubernetesClient
argument_list|(
name|configuration
argument_list|)
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating default kubernetes client without applying configuration"
argument_list|)
expr_stmt|;
return|return
operator|new
name|DefaultKubernetesClient
argument_list|()
return|;
block|}
block|}
DECL|method|createKubernetesClient (KubernetesConfiguration configuration)
specifier|private
specifier|static
name|KubernetesClient
name|createKubernetesClient
parameter_list|(
name|KubernetesConfiguration
name|configuration
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Create Kubernetes client with the following Configuration: {}"
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
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
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getConnectionTimeout
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withConnectionTimeout
argument_list|(
name|configuration
operator|.
name|getConnectionTimeout
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
name|getNamespace
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|withNamespace
argument_list|(
name|configuration
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Config
name|conf
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
operator|new
name|DefaultKubernetesClient
argument_list|(
name|conf
argument_list|)
return|;
block|}
block|}
end_class

end_unit

