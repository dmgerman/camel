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
comment|/**  * To collect various metrics directly from Camel routes using the DropWizard  * metrics library.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|MetricsEndpointBuilderFactory
specifier|public
interface|interface
name|MetricsEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Metrics component.      */
DECL|interface|MetricsEndpointBuilder
specifier|public
interface|interface
name|MetricsEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedMetricsEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedMetricsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Action when using timer type.          *           * The option is a:          *<code>org.apache.camel.component.metrics.MetricsTimerAction</code>          * type.          *           * Group: producer          */
DECL|method|action (MetricsTimerAction action)
specifier|default
name|MetricsEndpointBuilder
name|action
parameter_list|(
name|MetricsTimerAction
name|action
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"action"
argument_list|,
name|action
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Action when using timer type.          *           * The option will be converted to a          *<code>org.apache.camel.component.metrics.MetricsTimerAction</code>          * type.          *           * Group: producer          */
DECL|method|action (String action)
specifier|default
name|MetricsEndpointBuilder
name|action
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"action"
argument_list|,
name|action
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Decrement value when using counter type.          *           * The option is a:<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|decrement (Long decrement)
specifier|default
name|MetricsEndpointBuilder
name|decrement
parameter_list|(
name|Long
name|decrement
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"decrement"
argument_list|,
name|decrement
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Decrement value when using counter type.          *           * The option will be converted to a<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|decrement (String decrement)
specifier|default
name|MetricsEndpointBuilder
name|decrement
parameter_list|(
name|String
name|decrement
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"decrement"
argument_list|,
name|decrement
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Increment value when using counter type.          *           * The option is a:<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|increment (Long increment)
specifier|default
name|MetricsEndpointBuilder
name|increment
parameter_list|(
name|Long
name|increment
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"increment"
argument_list|,
name|increment
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Increment value when using counter type.          *           * The option will be converted to a<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|increment (String increment)
specifier|default
name|MetricsEndpointBuilder
name|increment
parameter_list|(
name|String
name|increment
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"increment"
argument_list|,
name|increment
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|MetricsEndpointBuilder
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
DECL|method|lazyStartProducer ( String lazyStartProducer)
specifier|default
name|MetricsEndpointBuilder
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
comment|/**          * Mark when using meter type.          *           * The option is a:<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|mark (Long mark)
specifier|default
name|MetricsEndpointBuilder
name|mark
parameter_list|(
name|Long
name|mark
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"mark"
argument_list|,
name|mark
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Mark when using meter type.          *           * The option will be converted to a<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|mark (String mark)
specifier|default
name|MetricsEndpointBuilder
name|mark
parameter_list|(
name|String
name|mark
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"mark"
argument_list|,
name|mark
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Subject value when using gauge type.          *           * The option is a:<code>java.lang.Object</code> type.          *           * Group: producer          */
DECL|method|subject (Object subject)
specifier|default
name|MetricsEndpointBuilder
name|subject
parameter_list|(
name|Object
name|subject
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"subject"
argument_list|,
name|subject
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Subject value when using gauge type.          *           * The option will be converted to a<code>java.lang.Object</code> type.          *           * Group: producer          */
DECL|method|subject (String subject)
specifier|default
name|MetricsEndpointBuilder
name|subject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"subject"
argument_list|,
name|subject
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Value value when using histogram type.          *           * The option is a:<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|value (Long value)
specifier|default
name|MetricsEndpointBuilder
name|value
parameter_list|(
name|Long
name|value
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Value value when using histogram type.          *           * The option will be converted to a<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|value (String value)
specifier|default
name|MetricsEndpointBuilder
name|value
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Metrics component.      */
DECL|interface|AdvancedMetricsEndpointBuilder
specifier|public
interface|interface
name|AdvancedMetricsEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|MetricsEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|MetricsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedMetricsEndpointBuilder
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
name|AdvancedMetricsEndpointBuilder
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
name|AdvancedMetricsEndpointBuilder
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
name|AdvancedMetricsEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.metrics.MetricsTimerAction</code> enum.      */
DECL|enum|MetricsTimerAction
enum|enum
name|MetricsTimerAction
block|{
DECL|enumConstant|start
name|start
block|,
DECL|enumConstant|stop
name|stop
block|;     }
comment|/**      * Metrics (camel-metrics)      * To collect various metrics directly from Camel routes using the      * DropWizard metrics library.      *       * Category: monitoring      * Available as of version: 2.14      * Maven coordinates: org.apache.camel:camel-metrics      *       * Syntax:<code>metrics:metricsType:metricsName</code>      *       * Path parameter: metricsType (required)      * Type of metrics      * The value can be one of: GAUGE, COUNTER, HISTOGRAM, METER, TIMER      *       * Path parameter: metricsName (required)      * Name of metrics      */
DECL|method|metrics (String path)
specifier|default
name|MetricsEndpointBuilder
name|metrics
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|MetricsEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|MetricsEndpointBuilder
implements|,
name|AdvancedMetricsEndpointBuilder
block|{
specifier|public
name|MetricsEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"metrics"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|MetricsEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

