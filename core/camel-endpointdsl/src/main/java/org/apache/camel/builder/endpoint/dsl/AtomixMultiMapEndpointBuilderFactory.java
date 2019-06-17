begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The atomix-multimap component is used to access Atomix's distributed multi  * map.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|AtomixMultiMapEndpointBuilderFactory
specifier|public
interface|interface
name|AtomixMultiMapEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Atomix MultiMap component.      */
DECL|interface|AtomixMultiMapEndpointBuilder
specifier|public
interface|interface
name|AtomixMultiMapEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedAtomixMultiMapEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The distributed resource name.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|resourceName (String resourceName)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|resourceName
parameter_list|(
name|String
name|resourceName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"resourceName"
argument_list|,
name|resourceName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The Atomix instance to use.          * The option is a<code>io.atomix.AtomixClient</code> type.          * @group consumer          */
DECL|method|atomix (Object atomix)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|atomix
parameter_list|(
name|Object
name|atomix
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"atomix"
argument_list|,
name|atomix
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The Atomix instance to use.          * The option will be converted to a<code>io.atomix.AtomixClient</code>          * type.          * @group consumer          */
DECL|method|atomix (String atomix)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|atomix
parameter_list|(
name|String
name|atomix
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"atomix"
argument_list|,
name|atomix
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The Atomix configuration uri.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|configurationUri ( String configurationUri)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|configurationUri
parameter_list|(
name|String
name|configurationUri
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"configurationUri"
argument_list|,
name|configurationUri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The default action.          * The option is a          *<code>org.apache.camel.component.atomix.client.multimap.AtomixMultiMap$Action</code> type.          * @group consumer          */
DECL|method|defaultAction (Action defaultAction)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|defaultAction
parameter_list|(
name|Action
name|defaultAction
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultAction"
argument_list|,
name|defaultAction
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The default action.          * The option will be converted to a          *<code>org.apache.camel.component.atomix.client.multimap.AtomixMultiMap$Action</code> type.          * @group consumer          */
DECL|method|defaultAction (String defaultAction)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|defaultAction
parameter_list|(
name|String
name|defaultAction
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultAction"
argument_list|,
name|defaultAction
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The key to use if none is set in the header or to listen for events          * for a specific key.          * The option is a<code>java.lang.Object</code> type.          * @group consumer          */
DECL|method|key (Object key)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|key
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"key"
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The key to use if none is set in the header or to listen for events          * for a specific key.          * The option will be converted to a<code>java.lang.Object</code> type.          * @group consumer          */
DECL|method|key (String key)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|key
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"key"
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The address of the nodes composing the cluster.          * The option is a          *<code>java.util.List&lt;io.atomix.catalyst.transport.Address&gt;</code> type.          * @group consumer          */
DECL|method|nodes (List<Object> nodes)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|nodes
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|nodes
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"nodes"
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The address of the nodes composing the cluster.          * The option will be converted to a          *<code>java.util.List&lt;io.atomix.catalyst.transport.Address&gt;</code> type.          * @group consumer          */
DECL|method|nodes (String nodes)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|nodes
parameter_list|(
name|String
name|nodes
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"nodes"
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The header that wil carry the result.          * The option is a<code>java.lang.String</code> type.          * @group consumer          */
DECL|method|resultHeader (String resultHeader)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|resultHeader
parameter_list|(
name|String
name|resultHeader
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"resultHeader"
argument_list|,
name|resultHeader
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the Atomix transport.          * The option is a          *<code>java.lang.Class&lt;io.atomix.catalyst.transport.Transport&gt;</code> type.          * @group consumer          */
DECL|method|transport (Class<Object> transport)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|transport
parameter_list|(
name|Class
argument_list|<
name|Object
argument_list|>
name|transport
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transport"
argument_list|,
name|transport
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the Atomix transport.          * The option will be converted to a          *<code>java.lang.Class&lt;io.atomix.catalyst.transport.Transport&gt;</code> type.          * @group consumer          */
DECL|method|transport (String transport)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|transport
parameter_list|(
name|String
name|transport
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"transport"
argument_list|,
name|transport
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The resource ttl.          * The option is a<code>long</code> type.          * @group consumer          */
DECL|method|ttl (long ttl)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|ttl
parameter_list|(
name|long
name|ttl
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"ttl"
argument_list|,
name|ttl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The resource ttl.          * The option will be converted to a<code>long</code> type.          * @group consumer          */
DECL|method|ttl (String ttl)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|ttl
parameter_list|(
name|String
name|ttl
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"ttl"
argument_list|,
name|ttl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Atomix MultiMap component.      */
DECL|interface|AdvancedAtomixMultiMapEndpointBuilder
specifier|public
interface|interface
name|AdvancedAtomixMultiMapEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|AtomixMultiMapEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|AtomixMultiMapEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The cluster wide default resource configuration.          * The option is a<code>java.util.Properties</code> type.          * @group advanced          */
DECL|method|defaultResourceConfig ( Properties defaultResourceConfig)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|defaultResourceConfig
parameter_list|(
name|Properties
name|defaultResourceConfig
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultResourceConfig"
argument_list|,
name|defaultResourceConfig
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The cluster wide default resource configuration.          * The option will be converted to a<code>java.util.Properties</code>          * type.          * @group advanced          */
DECL|method|defaultResourceConfig ( String defaultResourceConfig)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|defaultResourceConfig
parameter_list|(
name|String
name|defaultResourceConfig
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultResourceConfig"
argument_list|,
name|defaultResourceConfig
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The local default resource options.          * The option is a<code>java.util.Properties</code> type.          * @group advanced          */
DECL|method|defaultResourceOptions ( Properties defaultResourceOptions)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|defaultResourceOptions
parameter_list|(
name|Properties
name|defaultResourceOptions
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultResourceOptions"
argument_list|,
name|defaultResourceOptions
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The local default resource options.          * The option will be converted to a<code>java.util.Properties</code>          * type.          * @group advanced          */
DECL|method|defaultResourceOptions ( String defaultResourceOptions)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|defaultResourceOptions
parameter_list|(
name|String
name|defaultResourceOptions
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"defaultResourceOptions"
argument_list|,
name|defaultResourceOptions
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets if the local member should join groups as PersistentMember or          * not. If set to ephemeral the local member will receive an auto          * generated ID thus the local one is ignored.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|ephemeral ( boolean ephemeral)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|ephemeral
parameter_list|(
name|boolean
name|ephemeral
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"ephemeral"
argument_list|,
name|ephemeral
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets if the local member should join groups as PersistentMember or          * not. If set to ephemeral the local member will receive an auto          * generated ID thus the local one is ignored.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|ephemeral (String ephemeral)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|ephemeral
parameter_list|(
name|String
name|ephemeral
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"ephemeral"
argument_list|,
name|ephemeral
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The read consistency level.          * The option is a<code>io.atomix.resource.ReadConsistency</code> type.          * @group advanced          */
DECL|method|readConsistency ( ReadConsistency readConsistency)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|readConsistency
parameter_list|(
name|ReadConsistency
name|readConsistency
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"readConsistency"
argument_list|,
name|readConsistency
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The read consistency level.          * The option will be converted to a          *<code>io.atomix.resource.ReadConsistency</code> type.          * @group advanced          */
DECL|method|readConsistency ( String readConsistency)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|readConsistency
parameter_list|(
name|String
name|readConsistency
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"readConsistency"
argument_list|,
name|readConsistency
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Cluster wide resources configuration.          * The option is a<code>java.util.Map&lt;java.lang.String,          * java.util.Properties&gt;</code> type.          * @group advanced          */
DECL|method|resourceConfigs ( Map<String, Properties> resourceConfigs)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|resourceConfigs
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
name|setProperty
argument_list|(
literal|"resourceConfigs"
argument_list|,
name|resourceConfigs
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Cluster wide resources configuration.          * The option will be converted to a          *<code>java.util.Map&lt;java.lang.String,          * java.util.Properties&gt;</code> type.          * @group advanced          */
DECL|method|resourceConfigs ( String resourceConfigs)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|resourceConfigs
parameter_list|(
name|String
name|resourceConfigs
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"resourceConfigs"
argument_list|,
name|resourceConfigs
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Local resources configurations.          * The option is a<code>java.util.Map&lt;java.lang.String,          * java.util.Properties&gt;</code> type.          * @group advanced          */
DECL|method|resourceOptions ( Map<String, Properties> resourceOptions)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|resourceOptions
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
name|setProperty
argument_list|(
literal|"resourceOptions"
argument_list|,
name|resourceOptions
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Local resources configurations.          * The option will be converted to a          *<code>java.util.Map&lt;java.lang.String,          * java.util.Properties&gt;</code> type.          * @group advanced          */
DECL|method|resourceOptions ( String resourceOptions)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|resourceOptions
parameter_list|(
name|String
name|resourceOptions
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"resourceOptions"
argument_list|,
name|resourceOptions
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedAtomixMultiMapEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.atomix.client.multimap.AtomixMultiMap$Action</code> enum.      */
DECL|enum|Action
specifier|public
specifier|static
enum|enum
name|Action
block|{
DECL|enumConstant|PUT
DECL|enumConstant|GET
DECL|enumConstant|CLEAR
DECL|enumConstant|SIZE
DECL|enumConstant|CONTAINS_KEY
DECL|enumConstant|IS_EMPTY
DECL|enumConstant|REMOVE
DECL|enumConstant|REMOVE_VALUE
name|PUT
block|,
name|GET
block|,
name|CLEAR
block|,
name|SIZE
block|,
name|CONTAINS_KEY
block|,
name|IS_EMPTY
block|,
name|REMOVE
block|,
name|REMOVE_VALUE
block|;     }
comment|/**      * Proxy enum for<code>io.atomix.resource.ReadConsistency</code> enum.      */
DECL|enum|ReadConsistency
specifier|public
specifier|static
enum|enum
name|ReadConsistency
block|{
DECL|enumConstant|ATOMIC
DECL|enumConstant|ATOMIC_LEASE
DECL|enumConstant|SEQUENTIAL
DECL|enumConstant|LOCAL
name|ATOMIC
block|,
name|ATOMIC_LEASE
block|,
name|SEQUENTIAL
block|,
name|LOCAL
block|;     }
comment|/**      * The atomix-multimap component is used to access Atomix's distributed      * multi map. Creates a builder to build endpoints for the Atomix MultiMap      * component.      */
DECL|method|atomixMultiMap (String path)
specifier|default
name|AtomixMultiMapEndpointBuilder
name|atomixMultiMap
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|AtomixMultiMapEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|AtomixMultiMapEndpointBuilder
implements|,
name|AdvancedAtomixMultiMapEndpointBuilder
block|{
specifier|public
name|AtomixMultiMapEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"atomix-multimap"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|AtomixMultiMapEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

