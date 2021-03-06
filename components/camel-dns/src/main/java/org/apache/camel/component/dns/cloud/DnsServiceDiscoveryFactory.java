begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dns.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dns
operator|.
name|cloud
package|;
end_package

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
name|component
operator|.
name|dns
operator|.
name|DnsConfiguration
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

begin_class
annotation|@
name|CloudServiceFactory
argument_list|(
literal|"dns-service-discovery"
argument_list|)
DECL|class|DnsServiceDiscoveryFactory
specifier|public
class|class
name|DnsServiceDiscoveryFactory
implements|implements
name|ServiceDiscoveryFactory
block|{
DECL|field|configuration
specifier|private
specifier|final
name|DnsConfiguration
name|configuration
decl_stmt|;
DECL|method|DnsServiceDiscoveryFactory ()
specifier|public
name|DnsServiceDiscoveryFactory
parameter_list|()
block|{
name|this
operator|.
name|configuration
operator|=
operator|new
name|DnsConfiguration
argument_list|()
expr_stmt|;
block|}
DECL|method|DnsServiceDiscoveryFactory (DnsConfiguration configuration)
specifier|public
name|DnsServiceDiscoveryFactory
parameter_list|(
name|DnsConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Properties
comment|// *************************************************************************
DECL|method|getProto ()
specifier|public
name|String
name|getProto
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getProto
argument_list|()
return|;
block|}
DECL|method|setProto (String proto)
specifier|public
name|void
name|setProto
parameter_list|(
name|String
name|proto
parameter_list|)
block|{
name|configuration
operator|.
name|setProto
argument_list|(
name|proto
argument_list|)
expr_stmt|;
block|}
DECL|method|getDomain ()
specifier|public
name|String
name|getDomain
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getDomain
argument_list|()
return|;
block|}
DECL|method|setDomain (String domain)
specifier|public
name|void
name|setDomain
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
name|configuration
operator|.
name|setDomain
argument_list|(
name|domain
argument_list|)
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
return|return
operator|new
name|DnsServiceDiscovery
argument_list|(
name|configuration
argument_list|)
return|;
block|}
block|}
end_class

end_unit

