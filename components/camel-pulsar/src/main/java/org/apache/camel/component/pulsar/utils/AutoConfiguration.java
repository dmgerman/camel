begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pulsar.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pulsar
operator|.
name|utils
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|admin
operator|.
name|PulsarAdmin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|admin
operator|.
name|PulsarAdminException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|admin
operator|.
name|Tenants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|common
operator|.
name|policies
operator|.
name|data
operator|.
name|TenantInfo
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
comment|/**  * Automatically create Pulsar tenant and namespace if enabled  */
end_comment

begin_class
DECL|class|AutoConfiguration
specifier|public
class|class
name|AutoConfiguration
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AutoConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|pulsarAdmin
specifier|private
specifier|final
name|PulsarAdmin
name|pulsarAdmin
decl_stmt|;
DECL|field|clusters
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|clusters
decl_stmt|;
DECL|method|AutoConfiguration (PulsarAdmin pulsarAdmin, Set<String> clusters)
specifier|public
name|AutoConfiguration
parameter_list|(
name|PulsarAdmin
name|pulsarAdmin
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|clusters
parameter_list|)
block|{
name|this
operator|.
name|pulsarAdmin
operator|=
name|pulsarAdmin
expr_stmt|;
name|this
operator|.
name|clusters
operator|=
name|clusters
expr_stmt|;
block|}
DECL|method|ensureNameSpaceAndTenant (String path)
specifier|public
name|void
name|ensureNameSpaceAndTenant
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|pulsarAdmin
operator|!=
literal|null
condition|)
block|{
name|PulsarPath
name|pulsarPath
init|=
operator|new
name|PulsarPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|pulsarPath
operator|.
name|isAutoConfigurable
argument_list|()
condition|)
block|{
name|String
name|tenant
init|=
name|pulsarPath
operator|.
name|getTenant
argument_list|()
decl_stmt|;
name|String
name|namespace
init|=
name|pulsarPath
operator|.
name|getNamespace
argument_list|()
decl_stmt|;
try|try
block|{
name|ensureTenant
argument_list|(
name|tenant
argument_list|)
expr_stmt|;
name|ensureNameSpace
argument_list|(
name|tenant
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PulsarAdminException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|ensureNameSpace (String tenant, String namespace)
specifier|private
name|void
name|ensureNameSpace
parameter_list|(
name|String
name|tenant
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|PulsarAdminException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|namespaces
init|=
name|pulsarAdmin
operator|.
name|namespaces
argument_list|()
operator|.
name|getNamespaces
argument_list|(
name|tenant
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|namespaces
operator|.
name|contains
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|pulsarAdmin
operator|.
name|namespaces
argument_list|()
operator|.
name|createNamespace
argument_list|(
name|namespace
argument_list|,
name|clusters
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|ensureTenant (String tenant)
specifier|private
name|void
name|ensureTenant
parameter_list|(
name|String
name|tenant
parameter_list|)
throws|throws
name|PulsarAdminException
block|{
name|Tenants
name|tenants
init|=
name|pulsarAdmin
operator|.
name|tenants
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|tenantNames
init|=
name|tenants
operator|.
name|getTenants
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|tenantNames
operator|.
name|contains
argument_list|(
name|tenant
argument_list|)
condition|)
block|{
name|TenantInfo
name|tenantInfo
init|=
operator|new
name|TenantInfo
argument_list|()
decl_stmt|;
name|tenantInfo
operator|.
name|setAllowedClusters
argument_list|(
name|clusters
argument_list|)
expr_stmt|;
name|pulsarAdmin
operator|.
name|tenants
argument_list|()
operator|.
name|createTenant
argument_list|(
name|tenant
argument_list|,
name|tenantInfo
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isAutoConfigurable ()
specifier|public
name|boolean
name|isAutoConfigurable
parameter_list|()
block|{
return|return
name|pulsarAdmin
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

