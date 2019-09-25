begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"routing,cloud,service-discovery"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"dnsServiceDiscovery"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|DnsServiceCallServiceDiscoveryConfiguration
specifier|public
class|class
name|DnsServiceCallServiceDiscoveryConfiguration
extends|extends
name|ServiceCallServiceDiscoveryConfiguration
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"_tcp"
argument_list|)
DECL|field|proto
specifier|private
name|String
name|proto
init|=
literal|"_tcp"
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|domain
specifier|private
name|String
name|domain
decl_stmt|;
DECL|method|DnsServiceCallServiceDiscoveryConfiguration ()
specifier|public
name|DnsServiceCallServiceDiscoveryConfiguration
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|DnsServiceCallServiceDiscoveryConfiguration (ServiceCallDefinition parent)
specifier|public
name|DnsServiceCallServiceDiscoveryConfiguration
parameter_list|(
name|ServiceCallDefinition
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
literal|"dns-service-discovery"
argument_list|)
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
name|proto
return|;
block|}
comment|/**      * The transport protocol of the desired service.      */
DECL|method|setProto (String proto)
specifier|public
name|void
name|setProto
parameter_list|(
name|String
name|proto
parameter_list|)
block|{
name|this
operator|.
name|proto
operator|=
name|proto
expr_stmt|;
block|}
DECL|method|getDomain ()
specifier|public
name|String
name|getDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
comment|/**      * The domain name;      */
DECL|method|setDomain (String domain)
specifier|public
name|void
name|setDomain
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Fluent API
comment|// *************************************************************************
comment|/**      * The transport protocol of the desired service.      */
DECL|method|proto (String proto)
specifier|public
name|DnsServiceCallServiceDiscoveryConfiguration
name|proto
parameter_list|(
name|String
name|proto
parameter_list|)
block|{
name|setProto
argument_list|(
name|proto
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The domain name;      */
DECL|method|domain (String domain)
specifier|public
name|DnsServiceCallServiceDiscoveryConfiguration
name|domain
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
name|setDomain
argument_list|(
name|domain
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

