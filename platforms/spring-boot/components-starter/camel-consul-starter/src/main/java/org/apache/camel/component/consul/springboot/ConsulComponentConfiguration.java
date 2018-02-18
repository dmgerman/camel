begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.springboot
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
name|springboot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

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
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|option
operator|.
name|ConsistencyMode
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
name|util
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
name|DeprecatedConfigurationProperty
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
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The camel consul component allows you to work with Consul, a distributed,  * highly available, datacenter-aware, service discovery and configuration  * system.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.consul"
argument_list|)
DECL|class|ConsulComponentConfiguration
specifier|public
class|class
name|ConsulComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * The Consul agent URL      */
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
comment|/**      * The data center      */
DECL|field|datacenter
specifier|private
name|String
name|datacenter
decl_stmt|;
comment|/**      * SSL configuration using an      * org.apache.camel.util.jsse.SSLContextParameters instance.      */
annotation|@
name|NestedConfigurationProperty
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
comment|/**      * Enable usage of global SSL context parameters.      */
DECL|field|useGlobalSslContextParameters
specifier|private
name|Boolean
name|useGlobalSslContextParameters
init|=
literal|false
decl_stmt|;
comment|/**      * Sets the ACL token to be used with Consul      */
DECL|field|aclToken
specifier|private
name|String
name|aclToken
decl_stmt|;
comment|/**      * Sets the username to be used for basic authentication      */
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
comment|/**      * Sets the password to be used for basic authentication      */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**      * Sets the common configuration shared among endpoints      */
DECL|field|configuration
specifier|private
name|ConsulConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
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
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getDatacenter ()
specifier|public
name|String
name|getDatacenter
parameter_list|()
block|{
return|return
name|datacenter
return|;
block|}
DECL|method|setDatacenter (String datacenter)
specifier|public
name|void
name|setDatacenter
parameter_list|(
name|String
name|datacenter
parameter_list|)
block|{
name|this
operator|.
name|datacenter
operator|=
name|datacenter
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
DECL|method|getAclToken ()
specifier|public
name|String
name|getAclToken
parameter_list|()
block|{
return|return
name|aclToken
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
name|this
operator|.
name|aclToken
operator|=
name|aclToken
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
name|ConsulConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( ConsulConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|ConsulConfigurationNestedConfiguration
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
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|ConsulConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|ConsulConfigurationNestedConfiguration
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
name|consul
operator|.
name|ConsulConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * The default action. Can be overridden by CamelConsulAction          */
DECL|field|action
specifier|private
name|String
name|action
decl_stmt|;
comment|/**          * Default to transform values retrieved from Consul i.e. on KV endpoint          * to string.          */
DECL|field|valueAsString
specifier|private
name|Boolean
name|valueAsString
decl_stmt|;
comment|/**          * The default key. Can be overridden by CamelConsulKey          */
DECL|field|key
specifier|private
name|String
name|key
decl_stmt|;
comment|/**          * The Consul agent URL          */
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
comment|/**          * The data center          *           * @deprecated replaced by {@link #setDatacenter(String)} ()}          */
annotation|@
name|Deprecated
DECL|field|dc
specifier|private
name|String
name|dc
decl_stmt|;
comment|/**          * The data center          */
DECL|field|datacenter
specifier|private
name|String
name|datacenter
decl_stmt|;
comment|/**          * The near node to use for queries.          */
DECL|field|nearNode
specifier|private
name|String
name|nearNode
decl_stmt|;
comment|/**          * The note meta-data to use for queries.          */
DECL|field|nodeMeta
specifier|private
name|List
name|nodeMeta
decl_stmt|;
comment|/**          * The consistencyMode used for queries, default ConsistencyMode.DEFAULT          */
DECL|field|consistencyMode
specifier|private
name|ConsistencyMode
name|consistencyMode
decl_stmt|;
comment|/**          * Set tags. You can separate multiple tags by comma.          */
DECL|field|tags
specifier|private
name|Set
name|tags
decl_stmt|;
comment|/**          * SSL configuration using an          * org.apache.camel.util.jsse.SSLContextParameters instance.          */
annotation|@
name|NestedConfigurationProperty
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
comment|/**          * Sets the ACL token to be used with Consul          */
DECL|field|aclToken
specifier|private
name|String
name|aclToken
decl_stmt|;
comment|/**          * Sets the username to be used for basic authentication          */
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
comment|/**          * Sets the password to be used for basic authentication          */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**          * Connect timeout for OkHttpClient          */
DECL|field|connectTimeoutMillis
specifier|private
name|Long
name|connectTimeoutMillis
decl_stmt|;
comment|/**          * Read timeout for OkHttpClient          */
DECL|field|readTimeoutMillis
specifier|private
name|Long
name|readTimeoutMillis
decl_stmt|;
comment|/**          * Write timeout for OkHttpClient          */
DECL|field|writeTimeoutMillis
specifier|private
name|Long
name|writeTimeoutMillis
decl_stmt|;
comment|/**          * Configure if the AgentClient should attempt a ping before returning          * the Consul instance          */
DECL|field|pingInstance
specifier|private
name|Boolean
name|pingInstance
decl_stmt|;
comment|/**          * The second to wait for a watch event, default 10 seconds          */
DECL|field|blockSeconds
specifier|private
name|Integer
name|blockSeconds
decl_stmt|;
comment|/**          * The first index for watch for, default 0          */
DECL|field|firstIndex
specifier|private
name|BigInteger
name|firstIndex
decl_stmt|;
comment|/**          * Recursively watch, default false          */
DECL|field|recursive
specifier|private
name|Boolean
name|recursive
decl_stmt|;
DECL|method|getAction ()
specifier|public
name|String
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
DECL|method|setAction (String action)
specifier|public
name|void
name|setAction
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
DECL|method|getValueAsString ()
specifier|public
name|Boolean
name|getValueAsString
parameter_list|()
block|{
return|return
name|valueAsString
return|;
block|}
DECL|method|setValueAsString (Boolean valueAsString)
specifier|public
name|void
name|setValueAsString
parameter_list|(
name|Boolean
name|valueAsString
parameter_list|)
block|{
name|this
operator|.
name|valueAsString
operator|=
name|valueAsString
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|setKey (String key)
specifier|public
name|void
name|setKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
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
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
annotation|@
name|Deprecated
annotation|@
name|DeprecatedConfigurationProperty
DECL|method|getDc ()
specifier|public
name|String
name|getDc
parameter_list|()
block|{
return|return
name|dc
return|;
block|}
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
name|this
operator|.
name|dc
operator|=
name|dc
expr_stmt|;
block|}
DECL|method|getDatacenter ()
specifier|public
name|String
name|getDatacenter
parameter_list|()
block|{
return|return
name|datacenter
return|;
block|}
DECL|method|setDatacenter (String datacenter)
specifier|public
name|void
name|setDatacenter
parameter_list|(
name|String
name|datacenter
parameter_list|)
block|{
name|this
operator|.
name|datacenter
operator|=
name|datacenter
expr_stmt|;
block|}
DECL|method|getNearNode ()
specifier|public
name|String
name|getNearNode
parameter_list|()
block|{
return|return
name|nearNode
return|;
block|}
DECL|method|setNearNode (String nearNode)
specifier|public
name|void
name|setNearNode
parameter_list|(
name|String
name|nearNode
parameter_list|)
block|{
name|this
operator|.
name|nearNode
operator|=
name|nearNode
expr_stmt|;
block|}
DECL|method|getNodeMeta ()
specifier|public
name|List
name|getNodeMeta
parameter_list|()
block|{
return|return
name|nodeMeta
return|;
block|}
DECL|method|setNodeMeta (List nodeMeta)
specifier|public
name|void
name|setNodeMeta
parameter_list|(
name|List
name|nodeMeta
parameter_list|)
block|{
name|this
operator|.
name|nodeMeta
operator|=
name|nodeMeta
expr_stmt|;
block|}
DECL|method|getConsistencyMode ()
specifier|public
name|ConsistencyMode
name|getConsistencyMode
parameter_list|()
block|{
return|return
name|consistencyMode
return|;
block|}
DECL|method|setConsistencyMode (ConsistencyMode consistencyMode)
specifier|public
name|void
name|setConsistencyMode
parameter_list|(
name|ConsistencyMode
name|consistencyMode
parameter_list|)
block|{
name|this
operator|.
name|consistencyMode
operator|=
name|consistencyMode
expr_stmt|;
block|}
DECL|method|getTags ()
specifier|public
name|Set
name|getTags
parameter_list|()
block|{
return|return
name|tags
return|;
block|}
DECL|method|setTags (Set tags)
specifier|public
name|void
name|setTags
parameter_list|(
name|Set
name|tags
parameter_list|)
block|{
name|this
operator|.
name|tags
operator|=
name|tags
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
DECL|method|getAclToken ()
specifier|public
name|String
name|getAclToken
parameter_list|()
block|{
return|return
name|aclToken
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
name|this
operator|.
name|aclToken
operator|=
name|aclToken
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
DECL|method|getConnectTimeoutMillis ()
specifier|public
name|Long
name|getConnectTimeoutMillis
parameter_list|()
block|{
return|return
name|connectTimeoutMillis
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
name|this
operator|.
name|connectTimeoutMillis
operator|=
name|connectTimeoutMillis
expr_stmt|;
block|}
DECL|method|getReadTimeoutMillis ()
specifier|public
name|Long
name|getReadTimeoutMillis
parameter_list|()
block|{
return|return
name|readTimeoutMillis
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
name|this
operator|.
name|readTimeoutMillis
operator|=
name|readTimeoutMillis
expr_stmt|;
block|}
DECL|method|getWriteTimeoutMillis ()
specifier|public
name|Long
name|getWriteTimeoutMillis
parameter_list|()
block|{
return|return
name|writeTimeoutMillis
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
name|this
operator|.
name|writeTimeoutMillis
operator|=
name|writeTimeoutMillis
expr_stmt|;
block|}
DECL|method|getPingInstance ()
specifier|public
name|Boolean
name|getPingInstance
parameter_list|()
block|{
return|return
name|pingInstance
return|;
block|}
DECL|method|setPingInstance (Boolean pingInstance)
specifier|public
name|void
name|setPingInstance
parameter_list|(
name|Boolean
name|pingInstance
parameter_list|)
block|{
name|this
operator|.
name|pingInstance
operator|=
name|pingInstance
expr_stmt|;
block|}
DECL|method|getBlockSeconds ()
specifier|public
name|Integer
name|getBlockSeconds
parameter_list|()
block|{
return|return
name|blockSeconds
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
name|this
operator|.
name|blockSeconds
operator|=
name|blockSeconds
expr_stmt|;
block|}
DECL|method|getFirstIndex ()
specifier|public
name|BigInteger
name|getFirstIndex
parameter_list|()
block|{
return|return
name|firstIndex
return|;
block|}
DECL|method|setFirstIndex (BigInteger firstIndex)
specifier|public
name|void
name|setFirstIndex
parameter_list|(
name|BigInteger
name|firstIndex
parameter_list|)
block|{
name|this
operator|.
name|firstIndex
operator|=
name|firstIndex
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
block|}
block|}
end_class

end_unit

