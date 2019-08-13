begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd.springboot
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
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The camel etcd component allows you to work with Etcd, a distributed reliable  * key-value store.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.etcd"
argument_list|)
DECL|class|EtcdComponentConfiguration
specifier|public
class|class
name|EtcdComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the etcd component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To set the URIs the client connects.      */
DECL|field|uris
specifier|private
name|String
name|uris
decl_stmt|;
comment|/**      * To configure security using SSLContextParameters. The option is a      * org.apache.camel.support.jsse.SSLContextParameters type.      */
DECL|field|sslContextParameters
specifier|private
name|String
name|sslContextParameters
decl_stmt|;
comment|/**      * The user name to use for basic authentication.      */
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
comment|/**      * The password to use for basic authentication.      */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**      * Sets the common configuration shared among endpoints      */
DECL|field|configuration
specifier|private
name|EtcdConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Enable usage of global SSL context parameters.      */
DECL|field|useGlobalSslContextParameters
specifier|private
name|Boolean
name|useGlobalSslContextParameters
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
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
DECL|method|getSslContextParameters ()
specifier|public
name|String
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters (String sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|String
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
DECL|method|getConfiguration ()
specifier|public
name|EtcdConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( EtcdConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|EtcdConfigurationNestedConfiguration
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
DECL|method|getUseGlobalSslContextParameters ()
specifier|public
name|Boolean
name|getUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|useGlobalSslContextParameters
return|;
block|}
DECL|method|setUseGlobalSslContextParameters ( Boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|Boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|class|EtcdConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|EtcdConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
operator|.
name|class
decl_stmt|;
comment|/**          * To set the URIs the client connects.          */
DECL|field|uris
specifier|private
name|String
name|uris
init|=
literal|"http://localhost:2379,http://localhost:4001"
decl_stmt|;
comment|/**          * To configure security using SSLContextParameters.          */
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
comment|/**          * The user name to use for basic authentication.          */
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
comment|/**          * The password to use for basic authentication.          */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**          * To send an empty message in case of timeout watching for a key.          */
DECL|field|sendEmptyExchangeOnTimeout
specifier|private
name|Boolean
name|sendEmptyExchangeOnTimeout
init|=
literal|false
decl_stmt|;
comment|/**          * To apply an action recursively.          */
DECL|field|recursive
specifier|private
name|Boolean
name|recursive
init|=
literal|false
decl_stmt|;
comment|/**          * To set the lifespan of a key in milliseconds.          */
DECL|field|timeToLive
specifier|private
name|Integer
name|timeToLive
decl_stmt|;
comment|/**          * To set the maximum time an action could take to complete.          */
DECL|field|timeout
specifier|private
name|Long
name|timeout
decl_stmt|;
comment|/**          * The index to watch from          */
DECL|field|fromIndex
specifier|private
name|Long
name|fromIndex
init|=
literal|0L
decl_stmt|;
comment|/**          * The path to look for for service discovery          */
DECL|field|servicePath
specifier|private
name|String
name|servicePath
init|=
literal|"/services/"
decl_stmt|;
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
DECL|method|setSslContextParameters ( SSLContextParameters sslContextParameters)
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
DECL|method|getSendEmptyExchangeOnTimeout ()
specifier|public
name|Boolean
name|getSendEmptyExchangeOnTimeout
parameter_list|()
block|{
return|return
name|sendEmptyExchangeOnTimeout
return|;
block|}
DECL|method|setSendEmptyExchangeOnTimeout ( Boolean sendEmptyExchangeOnTimeout)
specifier|public
name|void
name|setSendEmptyExchangeOnTimeout
parameter_list|(
name|Boolean
name|sendEmptyExchangeOnTimeout
parameter_list|)
block|{
name|this
operator|.
name|sendEmptyExchangeOnTimeout
operator|=
name|sendEmptyExchangeOnTimeout
expr_stmt|;
block|}
DECL|method|getRecursive ()
specifier|public
name|Boolean
name|getRecursive
parameter_list|()
block|{
return|return
name|recursive
return|;
block|}
DECL|method|setRecursive (Boolean recursive)
specifier|public
name|void
name|setRecursive
parameter_list|(
name|Boolean
name|recursive
parameter_list|)
block|{
name|this
operator|.
name|recursive
operator|=
name|recursive
expr_stmt|;
block|}
DECL|method|getTimeToLive ()
specifier|public
name|Integer
name|getTimeToLive
parameter_list|()
block|{
return|return
name|timeToLive
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
name|this
operator|.
name|timeToLive
operator|=
name|timeToLive
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
DECL|method|getFromIndex ()
specifier|public
name|Long
name|getFromIndex
parameter_list|()
block|{
return|return
name|fromIndex
return|;
block|}
DECL|method|setFromIndex (Long fromIndex)
specifier|public
name|void
name|setFromIndex
parameter_list|(
name|Long
name|fromIndex
parameter_list|)
block|{
name|this
operator|.
name|fromIndex
operator|=
name|fromIndex
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
block|}
block|}
end_class

end_unit

