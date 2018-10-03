begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|support
operator|.
name|jsse
operator|.
name|SSLContextParameters
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
literal|"etcdServiceDiscovery"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|EtcdServiceCallServiceDiscoveryConfiguration
specifier|public
class|class
name|EtcdServiceCallServiceDiscoveryConfiguration
extends|extends
name|ServiceCallServiceDiscoveryConfiguration
block|{
annotation|@
name|XmlAttribute
DECL|field|uris
specifier|private
name|String
name|uris
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|timeout
specifier|private
name|Long
name|timeout
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"/services/"
argument_list|)
DECL|field|servicePath
specifier|private
name|String
name|servicePath
init|=
literal|"/services/"
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"on-demand"
argument_list|,
name|enums
operator|=
literal|"on-demand,watch"
argument_list|)
DECL|field|type
specifier|private
name|String
name|type
init|=
literal|"on-demand"
decl_stmt|;
DECL|method|EtcdServiceCallServiceDiscoveryConfiguration ()
specifier|public
name|EtcdServiceCallServiceDiscoveryConfiguration
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|EtcdServiceCallServiceDiscoveryConfiguration (ServiceCallDefinition parent)
specifier|public
name|EtcdServiceCallServiceDiscoveryConfiguration
parameter_list|(
name|ServiceCallDefinition
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
literal|"etcd-service-discovery"
argument_list|)
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
name|uris
return|;
block|}
comment|/**      * The URIs the client can connect to.      */
DECL|method|setUris (String uris)
specifier|public
name|void
name|setUris
parameter_list|(
name|String
name|uris
parameter_list|)
block|{
name|this
operator|.
name|uris
operator|=
name|uris
expr_stmt|;
block|}
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
comment|/**      * The user name to use for basic authentication.      */
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * The password to use for basic authentication.      */
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|Long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * To set the maximum time an action could take to complete.      */
DECL|method|setTimeout (Long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Long
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
DECL|method|getServicePath ()
specifier|public
name|String
name|getServicePath
parameter_list|()
block|{
return|return
name|servicePath
return|;
block|}
comment|/**      * The path to look for for service discovery      */
DECL|method|setServicePath (String servicePath)
specifier|public
name|void
name|setServicePath
parameter_list|(
name|String
name|servicePath
parameter_list|)
block|{
name|this
operator|.
name|servicePath
operator|=
name|servicePath
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
comment|/**      * To configure security using SSLContextParameters.      */
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
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
comment|/**      * To set the discovery type, valid values are on-demand and watch.      */
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
comment|// Fluent API
comment|// *************************************************************************
comment|/**      * The URIs the client can connect to.      */
DECL|method|uris (String uris)
specifier|public
name|EtcdServiceCallServiceDiscoveryConfiguration
name|uris
parameter_list|(
name|String
name|uris
parameter_list|)
block|{
name|setUris
argument_list|(
name|uris
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The user name to use for basic authentication.      */
DECL|method|userName (String userName)
specifier|public
name|EtcdServiceCallServiceDiscoveryConfiguration
name|userName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|setUserName
argument_list|(
name|userName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The password to use for basic authentication.      */
DECL|method|password (String password)
specifier|public
name|EtcdServiceCallServiceDiscoveryConfiguration
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * To set the maximum time an action could take to complete.      */
DECL|method|timeout (Long timeout)
specifier|public
name|EtcdServiceCallServiceDiscoveryConfiguration
name|timeout
parameter_list|(
name|Long
name|timeout
parameter_list|)
block|{
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The path to look for for service discovery      */
DECL|method|servicePath (String servicePath)
specifier|public
name|EtcdServiceCallServiceDiscoveryConfiguration
name|servicePath
parameter_list|(
name|String
name|servicePath
parameter_list|)
block|{
name|setServicePath
argument_list|(
name|servicePath
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * To configure security using SSLContextParameters.      */
DECL|method|sslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|EtcdServiceCallServiceDiscoveryConfiguration
name|sslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|setSslContextParameters
argument_list|(
name|sslContextParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * To set the discovery type, valid values are on-demand and watch.      */
DECL|method|type (String type)
specifier|public
name|EtcdServiceCallServiceDiscoveryConfiguration
name|type
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

