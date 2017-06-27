begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|Atomix
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|catalyst
operator|.
name|transport
operator|.
name|Address
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|catalyst
operator|.
name|transport
operator|.
name|Transport
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|catalyst
operator|.
name|transport
operator|.
name|netty
operator|.
name|NettyTransport
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|resource
operator|.
name|ReadConsistency
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
name|UriParam
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
DECL|class|AtomixConfiguration
specifier|public
class|class
name|AtomixConfiguration
parameter_list|<
name|T
extends|extends
name|Atomix
parameter_list|>
implements|implements
name|Cloneable
block|{
annotation|@
name|UriParam
argument_list|(
name|javaType
operator|=
literal|"io.atomix.Atomix"
argument_list|)
DECL|field|atomix
specifier|private
name|T
name|atomix
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|nodes
specifier|private
name|List
argument_list|<
name|Address
argument_list|>
name|nodes
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|javaType
operator|=
literal|"io.atomix.catalyst.transport.Transport"
argument_list|,
name|defaultValue
operator|=
literal|"io.atomix.catalyst.transport.netty.NettyTransport"
argument_list|)
DECL|field|transport
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Transport
argument_list|>
name|transport
init|=
name|NettyTransport
operator|.
name|class
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configurationUri
specifier|private
name|String
name|configurationUri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|readConsistency
specifier|private
name|ReadConsistency
name|readConsistency
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|defaultResourceConfig
specifier|private
name|Properties
name|defaultResourceConfig
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|defaultResourceOptions
specifier|private
name|Properties
name|defaultResourceOptions
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|prefix
operator|=
literal|"resource.config"
argument_list|)
DECL|field|resourceConfigs
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|resourceConfigs
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|prefix
operator|=
literal|"resource.options"
argument_list|)
DECL|field|resourceOptions
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|resourceOptions
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|ephemeral
specifier|private
name|boolean
name|ephemeral
decl_stmt|;
DECL|method|AtomixConfiguration ()
specifier|protected
name|AtomixConfiguration
parameter_list|()
block|{     }
comment|// *****************************************
comment|// Properties
comment|// *****************************************
DECL|method|getAtomix ()
specifier|public
name|T
name|getAtomix
parameter_list|()
block|{
return|return
name|atomix
return|;
block|}
comment|/**      * The Atomix instance to use      */
DECL|method|setAtomix (T client)
specifier|public
name|void
name|setAtomix
parameter_list|(
name|T
name|client
parameter_list|)
block|{
name|this
operator|.
name|atomix
operator|=
name|client
expr_stmt|;
block|}
DECL|method|getNodes ()
specifier|public
name|List
argument_list|<
name|Address
argument_list|>
name|getNodes
parameter_list|()
block|{
return|return
name|nodes
return|;
block|}
comment|/**      * The address of the nodes composing the cluster.      */
DECL|method|setNodes (List<Address> nodes)
specifier|public
name|void
name|setNodes
parameter_list|(
name|List
argument_list|<
name|Address
argument_list|>
name|nodes
parameter_list|)
block|{
name|this
operator|.
name|nodes
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|nodes
argument_list|,
literal|"Atomix Nodes"
argument_list|)
expr_stmt|;
block|}
DECL|method|setNodes (String nodes)
specifier|public
name|void
name|setNodes
parameter_list|(
name|String
name|nodes
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|nodes
argument_list|)
condition|)
block|{
name|setNodes
argument_list|(
name|Stream
operator|.
name|of
argument_list|(
name|nodes
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|Address
operator|::
operator|new
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getTransport ()
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|Transport
argument_list|>
name|getTransport
parameter_list|()
block|{
return|return
name|transport
return|;
block|}
comment|/**      * Sets the Atomix transport.      */
DECL|method|setTransport (Class<? extends Transport> transport)
specifier|public
name|void
name|setTransport
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Transport
argument_list|>
name|transport
parameter_list|)
block|{
name|this
operator|.
name|transport
operator|=
name|transport
expr_stmt|;
block|}
DECL|method|getConfigurationUri ()
specifier|public
name|String
name|getConfigurationUri
parameter_list|()
block|{
return|return
name|configurationUri
return|;
block|}
comment|/**      * The Atomix configuration uri.      */
DECL|method|setConfigurationUri (String configurationUri)
specifier|public
name|void
name|setConfigurationUri
parameter_list|(
name|String
name|configurationUri
parameter_list|)
block|{
name|this
operator|.
name|configurationUri
operator|=
name|configurationUri
expr_stmt|;
block|}
DECL|method|getReadConsistency ()
specifier|public
name|ReadConsistency
name|getReadConsistency
parameter_list|()
block|{
return|return
name|readConsistency
return|;
block|}
comment|/**      * The read consistency level.      */
DECL|method|setReadConsistency (ReadConsistency readConsistency)
specifier|public
name|void
name|setReadConsistency
parameter_list|(
name|ReadConsistency
name|readConsistency
parameter_list|)
block|{
name|this
operator|.
name|readConsistency
operator|=
name|readConsistency
expr_stmt|;
block|}
comment|// ***********************************
comment|// Properties - Resource configuration
comment|// ***********************************
DECL|method|getDefaultResourceConfig ()
specifier|public
name|Properties
name|getDefaultResourceConfig
parameter_list|()
block|{
return|return
name|defaultResourceConfig
return|;
block|}
comment|/**      * The cluster wide default resource configuration.      */
DECL|method|setDefaultResourceConfig (Properties defaultResourceConfig)
specifier|public
name|void
name|setDefaultResourceConfig
parameter_list|(
name|Properties
name|defaultResourceConfig
parameter_list|)
block|{
name|this
operator|.
name|defaultResourceConfig
operator|=
name|defaultResourceConfig
expr_stmt|;
block|}
DECL|method|getDefaultResourceOptions ()
specifier|public
name|Properties
name|getDefaultResourceOptions
parameter_list|()
block|{
return|return
name|defaultResourceOptions
return|;
block|}
comment|/**      * The local default resource options.      */
DECL|method|setDefaultResourceOptions (Properties defaultResourceOptions)
specifier|public
name|void
name|setDefaultResourceOptions
parameter_list|(
name|Properties
name|defaultResourceOptions
parameter_list|)
block|{
name|this
operator|.
name|defaultResourceOptions
operator|=
name|defaultResourceOptions
expr_stmt|;
block|}
DECL|method|getResourceConfigs ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|getResourceConfigs
parameter_list|()
block|{
return|return
name|resourceConfigs
return|;
block|}
comment|/**      * Cluster wide resources configuration.      */
DECL|method|setResourceConfigs (Map<String, Properties> resourceConfigs)
specifier|public
name|void
name|setResourceConfigs
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|resourceConfigs
parameter_list|)
block|{
name|this
operator|.
name|resourceConfigs
operator|=
name|resourceConfigs
expr_stmt|;
block|}
DECL|method|addResourceConfig (String name, Properties config)
specifier|public
name|void
name|addResourceConfig
parameter_list|(
name|String
name|name
parameter_list|,
name|Properties
name|config
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|resourceConfigs
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|resourceConfigs
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|resourceConfigs
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
DECL|method|getResourceConfig (String name)
specifier|public
name|Properties
name|getResourceConfig
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Properties
name|properties
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|resourceConfigs
operator|!=
literal|null
condition|)
block|{
name|Properties
name|props
init|=
name|this
operator|.
name|resourceConfigs
operator|.
name|getOrDefault
argument_list|(
name|name
argument_list|,
name|this
operator|.
name|defaultResourceConfig
argument_list|)
decl_stmt|;
if|if
condition|(
name|props
operator|!=
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|defaultResourceConfig
operator|!=
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|(
name|this
operator|.
name|defaultResourceConfig
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
block|}
return|return
name|properties
return|;
block|}
DECL|method|getResourceOptions ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|getResourceOptions
parameter_list|()
block|{
return|return
name|resourceOptions
return|;
block|}
comment|/**      * Local resources configurations      */
DECL|method|setResourceOptions (Map<String, Properties> resourceOptions)
specifier|public
name|void
name|setResourceOptions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|resourceOptions
parameter_list|)
block|{
name|this
operator|.
name|resourceOptions
operator|=
name|resourceOptions
expr_stmt|;
block|}
DECL|method|addResourceOption (String name, Properties config)
specifier|public
name|void
name|addResourceOption
parameter_list|(
name|String
name|name
parameter_list|,
name|Properties
name|config
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|resourceOptions
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|resourceOptions
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|resourceOptions
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
DECL|method|getResourceOptions (String name)
specifier|public
name|Properties
name|getResourceOptions
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Properties
name|properties
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|resourceOptions
operator|!=
literal|null
condition|)
block|{
name|Properties
name|props
init|=
name|this
operator|.
name|resourceOptions
operator|.
name|getOrDefault
argument_list|(
name|name
argument_list|,
name|this
operator|.
name|defaultResourceOptions
argument_list|)
decl_stmt|;
if|if
condition|(
name|props
operator|!=
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|defaultResourceOptions
operator|!=
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|(
name|this
operator|.
name|defaultResourceOptions
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
block|}
return|return
name|properties
return|;
block|}
DECL|method|isEphemeral ()
specifier|public
name|boolean
name|isEphemeral
parameter_list|()
block|{
return|return
name|ephemeral
return|;
block|}
comment|/**      * Sets if the local member should join groups as PersistentMember or not.      *      * If set to ephemeral the local member will receive an auto generated ID thus      * the local one is ignored.      */
DECL|method|setEphemeral (boolean ephemeral)
specifier|public
name|void
name|setEphemeral
parameter_list|(
name|boolean
name|ephemeral
parameter_list|)
block|{
name|this
operator|.
name|ephemeral
operator|=
name|ephemeral
expr_stmt|;
block|}
block|}
end_class

end_unit

