begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|etcd
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
name|etcd
operator|.
name|EtcdConfiguration
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
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_class
DECL|class|EtcdServiceDiscoveryFactory
specifier|public
class|class
name|EtcdServiceDiscoveryFactory
implements|implements
name|ServiceDiscoveryFactory
block|{
DECL|field|configuration
specifier|private
specifier|final
name|EtcdConfiguration
name|configuration
decl_stmt|;
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
DECL|method|EtcdServiceDiscoveryFactory ()
specifier|public
name|EtcdServiceDiscoveryFactory
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|EtcdConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|EtcdServiceDiscoveryFactory (EtcdConfiguration configuration)
specifier|public
name|EtcdServiceDiscoveryFactory
parameter_list|(
name|EtcdConfiguration
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
DECL|method|getUris ()
specifier|public
name|String
name|getUris
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getUris
argument_list|()
return|;
block|}
DECL|method|setUris (String uris)
specifier|public
name|void
name|setUris
parameter_list|(
name|String
name|uris
parameter_list|)
block|{
name|configuration
operator|.
name|setUris
argument_list|(
name|uris
argument_list|)
expr_stmt|;
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
DECL|method|getTimeToLive ()
specifier|public
name|Integer
name|getTimeToLive
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getTimeToLive
argument_list|()
return|;
block|}
DECL|method|setTimeToLive (Integer timeToLive)
specifier|public
name|void
name|setTimeToLive
parameter_list|(
name|Integer
name|timeToLive
parameter_list|)
block|{
name|configuration
operator|.
name|setTimeToLive
argument_list|(
name|timeToLive
argument_list|)
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|Long
name|getTimeout
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getTimeout
argument_list|()
return|;
block|}
DECL|method|setTimeout (Long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Long
name|timeout
parameter_list|)
block|{
name|configuration
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
DECL|method|getServicePath ()
specifier|public
name|String
name|getServicePath
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getServicePath
argument_list|()
return|;
block|}
DECL|method|setServicePath (String servicePath)
specifier|public
name|void
name|setServicePath
parameter_list|(
name|String
name|servicePath
parameter_list|)
block|{
name|configuration
operator|.
name|setServicePath
argument_list|(
name|servicePath
argument_list|)
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
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
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|"watch"
argument_list|,
name|type
argument_list|,
literal|true
argument_list|)
condition|?
operator|new
name|EtcdWatchServiceDiscovery
argument_list|(
name|configuration
argument_list|)
else|:
operator|new
name|EtcdOnDemandServiceDiscovery
argument_list|(
name|configuration
argument_list|)
return|;
block|}
block|}
end_class

end_unit

