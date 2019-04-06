begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|List
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
name|cloud
operator|.
name|ServiceDiscovery
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
name|ServiceDiscoveryFactory
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
name|annotations
operator|.
name|CloudServiceFactory
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

begin_class
annotation|@
name|CloudServiceFactory
argument_list|(
literal|"combined-service-discovery"
argument_list|)
DECL|class|CombinedServiceDiscoveryFactory
specifier|public
class|class
name|CombinedServiceDiscoveryFactory
implements|implements
name|ServiceDiscoveryFactory
block|{
DECL|field|serviceDiscoveryList
specifier|private
name|List
argument_list|<
name|ServiceDiscovery
argument_list|>
name|serviceDiscoveryList
decl_stmt|;
DECL|method|CombinedServiceDiscoveryFactory ()
specifier|public
name|CombinedServiceDiscoveryFactory
parameter_list|()
block|{     }
comment|// *************************************************************************
comment|// Properties
comment|// *************************************************************************
DECL|method|getServiceDiscoveryList ()
specifier|public
name|List
argument_list|<
name|ServiceDiscovery
argument_list|>
name|getServiceDiscoveryList
parameter_list|()
block|{
return|return
name|serviceDiscoveryList
return|;
block|}
DECL|method|setServiceDiscoveryList (List<ServiceDiscovery> serviceDiscoveryList)
specifier|public
name|void
name|setServiceDiscoveryList
parameter_list|(
name|List
argument_list|<
name|ServiceDiscovery
argument_list|>
name|serviceDiscoveryList
parameter_list|)
block|{
name|this
operator|.
name|serviceDiscoveryList
operator|=
name|serviceDiscoveryList
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Factory
comment|// *************************************************************************
annotation|@
name|Override
DECL|method|newInstance (CamelContext camelContext)
specifier|public
name|ServiceDiscovery
name|newInstance
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|serviceDiscoveryList
argument_list|,
literal|"ServiceDiscovery list"
argument_list|)
expr_stmt|;
return|return
operator|new
name|CombinedServiceDiscovery
argument_list|(
name|serviceDiscoveryList
argument_list|)
return|;
block|}
block|}
end_class

end_unit

