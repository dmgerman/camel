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
name|concurrent
operator|.
name|TimeUnit
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
literal|"caching-service-discovery"
argument_list|)
DECL|class|CachingServiceDiscoveryFactory
specifier|public
class|class
name|CachingServiceDiscoveryFactory
implements|implements
name|ServiceDiscoveryFactory
block|{
DECL|field|timeout
specifier|private
name|Integer
name|timeout
decl_stmt|;
DECL|field|units
specifier|private
name|TimeUnit
name|units
decl_stmt|;
DECL|field|serviceDiscovery
specifier|private
name|ServiceDiscovery
name|serviceDiscovery
decl_stmt|;
DECL|method|CachingServiceDiscoveryFactory ()
specifier|public
name|CachingServiceDiscoveryFactory
parameter_list|()
block|{     }
comment|// *************************************************************************
comment|// Properties
comment|// *************************************************************************
DECL|method|getTimeout ()
specifier|public
name|Integer
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Integer timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Integer
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getUnits ()
specifier|public
name|TimeUnit
name|getUnits
parameter_list|()
block|{
return|return
name|units
return|;
block|}
DECL|method|setUnits (TimeUnit units)
specifier|public
name|void
name|setUnits
parameter_list|(
name|TimeUnit
name|units
parameter_list|)
block|{
name|this
operator|.
name|units
operator|=
name|units
expr_stmt|;
block|}
DECL|method|getServiceDiscovery ()
specifier|public
name|ServiceDiscovery
name|getServiceDiscovery
parameter_list|()
block|{
return|return
name|serviceDiscovery
return|;
block|}
DECL|method|setServiceDiscovery (ServiceDiscovery serviceDiscovery)
specifier|public
name|void
name|setServiceDiscovery
parameter_list|(
name|ServiceDiscovery
name|serviceDiscovery
parameter_list|)
block|{
name|this
operator|.
name|serviceDiscovery
operator|=
name|serviceDiscovery
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
name|serviceDiscovery
argument_list|,
literal|"ServiceDiscovery configuration"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|timeout
argument_list|,
literal|"CachingServiceDiscovery timeout"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|units
argument_list|,
literal|"CachingServiceDiscovery time units"
argument_list|)
expr_stmt|;
return|return
operator|new
name|CachingServiceDiscovery
argument_list|(
name|serviceDiscovery
argument_list|,
name|units
operator|.
name|toMillis
argument_list|(
name|timeout
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

