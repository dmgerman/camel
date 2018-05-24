begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.service
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|Endpoint
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
name|cloud
operator|.
name|ServiceRegistry
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
name|DefaultComponent
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
name|ServiceRegistryHelper
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
name|ServiceRegistrySelectors
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StringHelper
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
name|URISupport
import|;
end_import

begin_class
DECL|class|ServiceComponent
specifier|public
class|class
name|ServiceComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|service
specifier|private
name|ServiceRegistry
name|service
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|serviceSelector
specifier|private
name|ServiceRegistry
operator|.
name|Selector
name|serviceSelector
decl_stmt|;
DECL|method|ServiceComponent ()
specifier|public
name|ServiceComponent
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ServiceComponent (CamelContext context)
specifier|public
name|ServiceComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|serviceSelector
operator|=
name|ServiceRegistrySelectors
operator|.
name|DEFAULT_SELECTOR
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|String
name|serviceName
init|=
name|StringHelper
operator|.
name|before
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|delegateUri
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|serviceName
argument_list|,
literal|"Service Name"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|delegateUri
argument_list|,
literal|"Delegate URI"
argument_list|)
expr_stmt|;
comment|// Lookup the service registry, this may be a static selected service
comment|// or dynamically selected one through a ServiceRegistry.Selector
specifier|final
name|ServiceRegistry
name|service
init|=
name|getServiceRegistry
argument_list|()
decl_stmt|;
comment|// Compute service definition from parameters, this is used as default
comment|// definition
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|parameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
name|ServiceDefinition
operator|.
name|SERVICE_META_PREFIX
argument_list|)
condition|)
block|{
continue|continue;
block|}
specifier|final
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
specifier|final
name|String
name|val
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
comment|// add service name, this is always set from an uri path param
name|params
operator|.
name|put
argument_list|(
name|ServiceDefinition
operator|.
name|SERVICE_META_NAME
argument_list|,
name|serviceName
argument_list|)
expr_stmt|;
comment|// remove all the service related options so the underlying component
comment|// does not fail because of unknown parameters
name|parameters
operator|.
name|keySet
argument_list|()
operator|.
name|removeAll
argument_list|(
name|parameters
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|ServiceEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|service
argument_list|,
name|params
argument_list|,
name|URISupport
operator|.
name|appendParametersToURI
argument_list|(
name|delegateUri
argument_list|,
name|parameters
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getService ()
specifier|public
name|ServiceRegistry
name|getService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
comment|/**      * Inject the service to use.      */
DECL|method|setService (ServiceRegistry service)
specifier|public
name|void
name|setService
parameter_list|(
name|ServiceRegistry
name|service
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
block|}
DECL|method|getServiceSelector ()
specifier|public
name|ServiceRegistry
operator|.
name|Selector
name|getServiceSelector
parameter_list|()
block|{
return|return
name|serviceSelector
return|;
block|}
comment|/**      *      * Inject the service selector used to lookup the {@link ServiceRegistry} to use.      */
DECL|method|setServiceSelector (ServiceRegistry.Selector serviceSelector)
specifier|public
name|void
name|setServiceSelector
parameter_list|(
name|ServiceRegistry
operator|.
name|Selector
name|serviceSelector
parameter_list|)
block|{
name|this
operator|.
name|serviceSelector
operator|=
name|serviceSelector
expr_stmt|;
block|}
comment|// *****************
comment|// Helpers
comment|// *****************
DECL|method|getServiceRegistry ()
specifier|private
name|ServiceRegistry
name|getServiceRegistry
parameter_list|()
block|{
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
return|return
name|ServiceRegistryHelper
operator|.
name|lookupService
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|serviceSelector
argument_list|)
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|IllegalStateException
argument_list|(
literal|"No cluster service found"
argument_list|)
argument_list|)
return|;
block|}
return|return
name|service
return|;
block|}
block|}
end_class

end_unit

