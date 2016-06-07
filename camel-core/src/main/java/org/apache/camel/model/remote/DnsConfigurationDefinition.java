begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|remote
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

begin_comment
comment|/**  * DNS remote service call configuration  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing,remote"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"dnsConfiguration"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|DnsConfigurationDefinition
specifier|public
class|class
name|DnsConfigurationDefinition
extends|extends
name|ServiceCallConfigurationDefinition
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
name|String
name|proto
init|=
literal|"_tcp"
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|domain
name|String
name|domain
decl_stmt|;
DECL|method|DnsConfigurationDefinition ()
specifier|public
name|DnsConfigurationDefinition
parameter_list|()
block|{     }
DECL|method|DnsConfigurationDefinition (ServiceCallDefinition parent)
specifier|public
name|DnsConfigurationDefinition
parameter_list|(
name|ServiceCallDefinition
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
comment|// -------------------------------------------------------------------------
comment|// Getter/Setter
comment|// -------------------------------------------------------------------------
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
comment|// -------------------------------------------------------------------------
comment|// Fluent API
comment|// -------------------------------------------------------------------------
DECL|method|proto (String proto)
specifier|public
name|DnsConfigurationDefinition
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
DECL|method|domain (String domain)
specifier|public
name|DnsConfigurationDefinition
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

