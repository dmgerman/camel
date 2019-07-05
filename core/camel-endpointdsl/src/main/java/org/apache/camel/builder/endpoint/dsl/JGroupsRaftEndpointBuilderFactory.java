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
name|ExchangePattern
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
name|ExceptionHandler
import|;
end_import

begin_comment
comment|/**  * The jgroups component provides exchange of messages between Camel and JGroups  * clusters.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|JGroupsRaftEndpointBuilderFactory
specifier|public
interface|interface
name|JGroupsRaftEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the JGroups raft component.      */
DECL|interface|JGroupsRaftEndpointConsumerBuilder
specifier|public
interface|interface
name|JGroupsRaftEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedJGroupsRaftEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedJGroupsRaftEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The name of the JGroupsraft cluster the component should connect to.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|clusterName ( String clusterName)
specifier|default
name|JGroupsRaftEndpointConsumerBuilder
name|clusterName
parameter_list|(
name|String
name|clusterName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clusterName"
argument_list|,
name|clusterName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|JGroupsRaftEndpointConsumerBuilder
name|bridgeErrorHandler
parameter_list|(
name|boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( String bridgeErrorHandler)
specifier|default
name|JGroupsRaftEndpointConsumerBuilder
name|bridgeErrorHandler
parameter_list|(
name|String
name|bridgeErrorHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If set to true, the consumer endpoint will receive roleChange event          * as well (not just connecting and/or using the state machine). By          * default it is set to false.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|enableRoleChangeEvents ( boolean enableRoleChangeEvents)
specifier|default
name|JGroupsRaftEndpointConsumerBuilder
name|enableRoleChangeEvents
parameter_list|(
name|boolean
name|enableRoleChangeEvents
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"enableRoleChangeEvents"
argument_list|,
name|enableRoleChangeEvents
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If set to true, the consumer endpoint will receive roleChange event          * as well (not just connecting and/or using the state machine). By          * default it is set to false.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|enableRoleChangeEvents ( String enableRoleChangeEvents)
specifier|default
name|JGroupsRaftEndpointConsumerBuilder
name|enableRoleChangeEvents
parameter_list|(
name|String
name|enableRoleChangeEvents
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"enableRoleChangeEvents"
argument_list|,
name|enableRoleChangeEvents
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint consumers for the JGroups raft component.      */
DECL|interface|AdvancedJGroupsRaftEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedJGroupsRaftEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|JGroupsRaftEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|JGroupsRaftEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedJGroupsRaftEndpointConsumerBuilder
name|exceptionHandler
parameter_list|(
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option will be converted to a          *<code>org.apache.camel.spi.ExceptionHandler</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( String exceptionHandler)
specifier|default
name|AdvancedJGroupsRaftEndpointConsumerBuilder
name|exceptionHandler
parameter_list|(
name|String
name|exceptionHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          *           * The option is a:<code>org.apache.camel.ExchangePattern</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exchangePattern ( ExchangePattern exchangePattern)
specifier|default
name|AdvancedJGroupsRaftEndpointConsumerBuilder
name|exchangePattern
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          *           * The option will be converted to a          *<code>org.apache.camel.ExchangePattern</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exchangePattern ( String exchangePattern)
specifier|default
name|AdvancedJGroupsRaftEndpointConsumerBuilder
name|exchangePattern
parameter_list|(
name|String
name|exchangePattern
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedJGroupsRaftEndpointConsumerBuilder
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedJGroupsRaftEndpointConsumerBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedJGroupsRaftEndpointConsumerBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedJGroupsRaftEndpointConsumerBuilder
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
comment|/**      * Builder for endpoint producers for the JGroups raft component.      */
DECL|interface|JGroupsRaftEndpointProducerBuilder
specifier|public
interface|interface
name|JGroupsRaftEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedJGroupsRaftEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedJGroupsRaftEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The name of the JGroupsraft cluster the component should connect to.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|clusterName ( String clusterName)
specifier|default
name|JGroupsRaftEndpointProducerBuilder
name|clusterName
parameter_list|(
name|String
name|clusterName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clusterName"
argument_list|,
name|clusterName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|JGroupsRaftEndpointProducerBuilder
name|lazyStartProducer
parameter_list|(
name|boolean
name|lazyStartProducer
parameter_list|)
block|{
name|setProperty
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
DECL|method|lazyStartProducer ( String lazyStartProducer)
specifier|default
name|JGroupsRaftEndpointProducerBuilder
name|lazyStartProducer
parameter_list|(
name|String
name|lazyStartProducer
parameter_list|)
block|{
name|setProperty
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
block|}
comment|/**      * Advanced builder for endpoint producers for the JGroups raft component.      */
DECL|interface|AdvancedJGroupsRaftEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedJGroupsRaftEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|JGroupsRaftEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|JGroupsRaftEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedJGroupsRaftEndpointProducerBuilder
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedJGroupsRaftEndpointProducerBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedJGroupsRaftEndpointProducerBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedJGroupsRaftEndpointProducerBuilder
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
comment|/**      * Builder for endpoint for the JGroups raft component.      */
DECL|interface|JGroupsRaftEndpointBuilder
specifier|public
interface|interface
name|JGroupsRaftEndpointBuilder
extends|extends
name|JGroupsRaftEndpointConsumerBuilder
extends|,
name|JGroupsRaftEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedJGroupsRaftEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedJGroupsRaftEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The name of the JGroupsraft cluster the component should connect to.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|clusterName (String clusterName)
specifier|default
name|JGroupsRaftEndpointBuilder
name|clusterName
parameter_list|(
name|String
name|clusterName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clusterName"
argument_list|,
name|clusterName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the JGroups raft component.      */
DECL|interface|AdvancedJGroupsRaftEndpointBuilder
specifier|public
interface|interface
name|AdvancedJGroupsRaftEndpointBuilder
extends|extends
name|AdvancedJGroupsRaftEndpointConsumerBuilder
extends|,
name|AdvancedJGroupsRaftEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|JGroupsRaftEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|JGroupsRaftEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedJGroupsRaftEndpointBuilder
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedJGroupsRaftEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedJGroupsRaftEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedJGroupsRaftEndpointBuilder
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
comment|/**      * JGroups raft (camel-jgroups-raft)      * The jgroups component provides exchange of messages between Camel and      * JGroups clusters.      *       * Syntax:<code>jgroups-raft:clusterName</code>      * Category: clustering,messaging      * Available as of version: 2.24      * Maven coordinates: org.apache.camel:camel-jgroups-raft      */
DECL|method|jGroupsRaft (String path)
specifier|default
name|JGroupsRaftEndpointBuilder
name|jGroupsRaft
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|JGroupsRaftEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|JGroupsRaftEndpointBuilder
implements|,
name|AdvancedJGroupsRaftEndpointBuilder
block|{
specifier|public
name|JGroupsRaftEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"jgroups-raft"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|JGroupsRaftEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

