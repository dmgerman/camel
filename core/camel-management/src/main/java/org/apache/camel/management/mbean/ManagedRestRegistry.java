begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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
name|management
operator|.
name|openmbean
operator|.
name|CompositeData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeDataSupport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularDataSupport
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
name|Producer
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
name|RuntimeCamelException
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|CamelOpenMBeanTypes
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedRestRegistryMBean
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
name|RestRegistry
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed RestRegistry"
argument_list|)
DECL|class|ManagedRestRegistry
specifier|public
class|class
name|ManagedRestRegistry
extends|extends
name|ManagedService
implements|implements
name|ManagedRestRegistryMBean
block|{
DECL|field|registry
specifier|private
specifier|final
name|RestRegistry
name|registry
decl_stmt|;
DECL|field|apiProducer
specifier|private
specifier|transient
name|Producer
name|apiProducer
decl_stmt|;
DECL|method|ManagedRestRegistry (CamelContext context, RestRegistry registry)
specifier|public
name|ManagedRestRegistry
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|RestRegistry
name|registry
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|registry
argument_list|)
expr_stmt|;
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
block|}
DECL|method|getRegistry ()
specifier|public
name|RestRegistry
name|getRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
annotation|@
name|Override
DECL|method|getNumberOfRestServices ()
specifier|public
name|int
name|getNumberOfRestServices
parameter_list|()
block|{
return|return
name|registry
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|listRestServices ()
specifier|public
name|TabularData
name|listRestServices
parameter_list|()
block|{
try|try
block|{
name|TabularData
name|answer
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|CamelOpenMBeanTypes
operator|.
name|listRestServicesTabularType
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RestRegistry
operator|.
name|RestService
argument_list|>
name|services
init|=
name|registry
operator|.
name|listAllRestServices
argument_list|()
decl_stmt|;
for|for
control|(
name|RestRegistry
operator|.
name|RestService
name|entry
range|:
name|services
control|)
block|{
name|CompositeType
name|ct
init|=
name|CamelOpenMBeanTypes
operator|.
name|listRestServicesCompositeType
argument_list|()
decl_stmt|;
name|String
name|url
init|=
name|entry
operator|.
name|getUrl
argument_list|()
decl_stmt|;
name|String
name|baseUrl
init|=
name|entry
operator|.
name|getBaseUrl
argument_list|()
decl_stmt|;
name|String
name|basePath
init|=
name|entry
operator|.
name|getBasePath
argument_list|()
decl_stmt|;
name|String
name|uriTemplate
init|=
name|entry
operator|.
name|getUriTemplate
argument_list|()
decl_stmt|;
name|String
name|method
init|=
name|entry
operator|.
name|getMethod
argument_list|()
decl_stmt|;
name|String
name|consumes
init|=
name|entry
operator|.
name|getConsumes
argument_list|()
decl_stmt|;
name|String
name|produces
init|=
name|entry
operator|.
name|getProduces
argument_list|()
decl_stmt|;
name|String
name|state
init|=
name|entry
operator|.
name|getState
argument_list|()
decl_stmt|;
name|String
name|inType
init|=
name|entry
operator|.
name|getInType
argument_list|()
decl_stmt|;
name|String
name|outType
init|=
name|entry
operator|.
name|getOutType
argument_list|()
decl_stmt|;
name|String
name|routeId
init|=
name|entry
operator|.
name|getRouteId
argument_list|()
decl_stmt|;
name|String
name|description
init|=
name|entry
operator|.
name|getDescription
argument_list|()
decl_stmt|;
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|ct
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"url"
block|,
literal|"baseUrl"
block|,
literal|"basePath"
block|,
literal|"uriTemplate"
block|,
literal|"method"
block|,
literal|"consumes"
block|,
literal|"produces"
block|,
literal|"inType"
block|,
literal|"outType"
block|,
literal|"state"
block|,
literal|"routeId"
block|,
literal|"description"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|url
block|,
name|baseUrl
block|,
name|basePath
block|,
name|uriTemplate
block|,
name|method
block|,
name|consumes
block|,
name|produces
block|,
name|inType
block|,
name|outType
block|,
name|state
block|,
name|routeId
block|,
name|description
block|}
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|apiDocAsJson ()
specifier|public
name|String
name|apiDocAsJson
parameter_list|()
block|{
return|return
name|registry
operator|.
name|apiDocAsJson
argument_list|()
return|;
block|}
block|}
end_class

end_unit

