begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
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
name|consul
operator|.
name|ConsulConfiguration
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
name|support
operator|.
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_class
annotation|@
name|CloudServiceFactory
argument_list|(
literal|"consul-service-discovery"
argument_list|)
DECL|class|ConsulServiceDiscoveryFactory
specifier|public
class|class
name|ConsulServiceDiscoveryFactory
implements|implements
name|ServiceDiscoveryFactory
block|{
DECL|field|configuration
specifier|private
specifier|final
name|ConsulConfiguration
name|configuration
decl_stmt|;
DECL|method|ConsulServiceDiscoveryFactory ()
specifier|public
name|ConsulServiceDiscoveryFactory
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|ConsulConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ConsulServiceDiscoveryFactory (ConsulConfiguration configuration)
specifier|public
name|ConsulServiceDiscoveryFactory
parameter_list|(
name|ConsulConfiguration
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
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getUrl
argument_list|()
return|;
block|}
DECL|method|setUrl (String url)
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|configuration
operator|.
name|setUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated, @deprecated replaced by {@link #getDatacenter()} ()}      *       * @return      */
annotation|@
name|Deprecated
DECL|method|getDc ()
specifier|public
name|String
name|getDc
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getDatacenter
argument_list|()
return|;
block|}
comment|/**      * @deprecated, @deprecated replaced by {@link #setDatacenter(String)}} ()}      */
annotation|@
name|Deprecated
DECL|method|setDc (String dc)
specifier|public
name|void
name|setDc
parameter_list|(
name|String
name|dc
parameter_list|)
block|{
name|configuration
operator|.
name|setDc
argument_list|(
name|dc
argument_list|)
expr_stmt|;
block|}
DECL|method|setDatacenter (String dc)
specifier|public
name|void
name|setDatacenter
parameter_list|(
name|String
name|dc
parameter_list|)
block|{
name|configuration
operator|.
name|setDatacenter
argument_list|(
name|dc
argument_list|)
expr_stmt|;
block|}
DECL|method|getDatacenter ()
specifier|public
name|String
name|getDatacenter
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getDatacenter
argument_list|()
return|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
return|;
block|}
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|configuration
operator|.
name|setSslContextParameters
argument_list|(
name|sslContextParameters
argument_list|)
expr_stmt|;
block|}
DECL|method|getAclToken ()
specifier|public
name|String
name|getAclToken
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getAclToken
argument_list|()
return|;
block|}
DECL|method|setAclToken (String aclToken)
specifier|public
name|void
name|setAclToken
parameter_list|(
name|String
name|aclToken
parameter_list|)
block|{
name|configuration
operator|.
name|setAclToken
argument_list|(
name|aclToken
argument_list|)
expr_stmt|;
block|}
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getUserName
argument_list|()
return|;
block|}
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|configuration
operator|.
name|setUserName
argument_list|(
name|userName
argument_list|)
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getPassword
argument_list|()
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|configuration
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
DECL|method|getConnectTimeoutMillis ()
specifier|public
name|Long
name|getConnectTimeoutMillis
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getConnectTimeoutMillis
argument_list|()
return|;
block|}
DECL|method|setConnectTimeoutMillis (Long connectTimeoutMillis)
specifier|public
name|void
name|setConnectTimeoutMillis
parameter_list|(
name|Long
name|connectTimeoutMillis
parameter_list|)
block|{
name|configuration
operator|.
name|setConnectTimeoutMillis
argument_list|(
name|connectTimeoutMillis
argument_list|)
expr_stmt|;
block|}
DECL|method|getReadTimeoutMillis ()
specifier|public
name|Long
name|getReadTimeoutMillis
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getReadTimeoutMillis
argument_list|()
return|;
block|}
DECL|method|setReadTimeoutMillis (Long readTimeoutMillis)
specifier|public
name|void
name|setReadTimeoutMillis
parameter_list|(
name|Long
name|readTimeoutMillis
parameter_list|)
block|{
name|configuration
operator|.
name|setReadTimeoutMillis
argument_list|(
name|readTimeoutMillis
argument_list|)
expr_stmt|;
block|}
DECL|method|getWriteTimeoutMillis ()
specifier|public
name|Long
name|getWriteTimeoutMillis
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getWriteTimeoutMillis
argument_list|()
return|;
block|}
DECL|method|setWriteTimeoutMillis (Long writeTimeoutMillis)
specifier|public
name|void
name|setWriteTimeoutMillis
parameter_list|(
name|Long
name|writeTimeoutMillis
parameter_list|)
block|{
name|configuration
operator|.
name|setWriteTimeoutMillis
argument_list|(
name|writeTimeoutMillis
argument_list|)
expr_stmt|;
block|}
DECL|method|getBlockSeconds ()
specifier|public
name|Integer
name|getBlockSeconds
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getBlockSeconds
argument_list|()
return|;
block|}
DECL|method|setBlockSeconds (Integer blockSeconds)
specifier|public
name|void
name|setBlockSeconds
parameter_list|(
name|Integer
name|blockSeconds
parameter_list|)
block|{
name|configuration
operator|.
name|setBlockSeconds
argument_list|(
name|blockSeconds
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
name|ConsulServiceDiscovery
argument_list|(
name|configuration
argument_list|)
return|;
block|}
block|}
end_class

end_unit

