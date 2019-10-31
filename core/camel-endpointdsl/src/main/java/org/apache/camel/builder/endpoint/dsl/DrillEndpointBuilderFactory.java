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
comment|/**  * The drill component gives you the ability to quering into apache drill  * cluster.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|DrillEndpointBuilderFactory
specifier|public
interface|interface
name|DrillEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Drill component.      */
DECL|interface|DrillEndpointBuilder
specifier|public
interface|interface
name|DrillEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedDrillEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedDrillEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Cluster ID          * https://drill.apache.org/docs/using-the-jdbc-driver/#determining-the-cluster-id.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|clusterId (String clusterId)
specifier|default
name|DrillEndpointBuilder
name|clusterId
parameter_list|(
name|String
name|clusterId
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"clusterId"
argument_list|,
name|clusterId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Drill directory in ZooKeeper.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|directory (String directory)
specifier|default
name|DrillEndpointBuilder
name|directory
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"directory"
argument_list|,
name|directory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer (boolean lazyStartProducer)
specifier|default
name|DrillEndpointBuilder
name|lazyStartProducer
parameter_list|(
name|boolean
name|lazyStartProducer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer (String lazyStartProducer)
specifier|default
name|DrillEndpointBuilder
name|lazyStartProducer
parameter_list|(
name|String
name|lazyStartProducer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Connection mode: zk: Zookeeper drillbit: Drillbit direct connection          * https://drill.apache.org/docs/using-the-jdbc-driver/.          *           * The option is a:          *<code>org.apache.camel.component.drill.DrillConnectionMode</code>          * type.          *           * Group: producer          */
DECL|method|mode (DrillConnectionMode mode)
specifier|default
name|DrillEndpointBuilder
name|mode
parameter_list|(
name|DrillConnectionMode
name|mode
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"mode"
argument_list|,
name|mode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Connection mode: zk: Zookeeper drillbit: Drillbit direct connection          * https://drill.apache.org/docs/using-the-jdbc-driver/.          *           * The option will be converted to a          *<code>org.apache.camel.component.drill.DrillConnectionMode</code>          * type.          *           * Group: producer          */
DECL|method|mode (String mode)
specifier|default
name|DrillEndpointBuilder
name|mode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"mode"
argument_list|,
name|mode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * ZooKeeper port number.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|port (Integer port)
specifier|default
name|DrillEndpointBuilder
name|port
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * ZooKeeper port number.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|port (String port)
specifier|default
name|DrillEndpointBuilder
name|port
parameter_list|(
name|String
name|port
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Drill component.      */
DECL|interface|AdvancedDrillEndpointBuilder
specifier|public
interface|interface
name|AdvancedDrillEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|DrillEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|DrillEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedDrillEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedDrillEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedDrillEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedDrillEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.drill.DrillConnectionMode</code> enum.      */
DECL|enum|DrillConnectionMode
enum|enum
name|DrillConnectionMode
block|{
DECL|enumConstant|ZK
name|ZK
block|,
DECL|enumConstant|DRILLBIT
name|DRILLBIT
block|;     }
comment|/**      * Drill (camel-drill)      * The drill component gives you the ability to quering into apache drill      * cluster.      *       * Category: database,sql      * Available as of version: 2.19      * Maven coordinates: org.apache.camel:camel-drill      *       * Syntax:<code>drill:host</code>      *       * Path parameter: host (required)      * ZooKeeper host name or IP address. Use local instead of a host name or IP      * address to connect to the local Drillbit      */
DECL|method|drill (String path)
specifier|default
name|DrillEndpointBuilder
name|drill
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|DrillEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|DrillEndpointBuilder
implements|,
name|AdvancedDrillEndpointBuilder
block|{
specifier|public
name|DrillEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"drill"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|DrillEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

