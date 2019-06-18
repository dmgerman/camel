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
comment|/**  * To collect various metrics directly from Camel routes using the Micrometer  * library.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|MicrometerEndpointBuilderFactory
specifier|public
interface|interface
name|MicrometerEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Micrometer component.      */
DECL|interface|MicrometerEndpointBuilder
specifier|public
interface|interface
name|MicrometerEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedMicrometerEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedMicrometerEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Type of metrics.          * The option is a<code>io.micrometer.core.instrument.Meter$Type</code>          * type.          * @group producer          */
DECL|method|metricsType (Type metricsType)
specifier|default
name|MicrometerEndpointBuilder
name|metricsType
parameter_list|(
name|Type
name|metricsType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"metricsType"
argument_list|,
name|metricsType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Type of metrics.          * The option will be converted to a          *<code>io.micrometer.core.instrument.Meter$Type</code> type.          * @group producer          */
DECL|method|metricsType (String metricsType)
specifier|default
name|MicrometerEndpointBuilder
name|metricsType
parameter_list|(
name|String
name|metricsType
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"metricsType"
argument_list|,
name|metricsType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Name of metrics.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|metricsName (String metricsName)
specifier|default
name|MicrometerEndpointBuilder
name|metricsName
parameter_list|(
name|String
name|metricsName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"metricsName"
argument_list|,
name|metricsName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Tags of metrics.          * The option is a          *<code>java.lang.Iterable&lt;io.micrometer.core.instrument.Tag&gt;</code> type.          * @group producer          */
DECL|method|tags (Iterable<Object> tags)
specifier|default
name|MicrometerEndpointBuilder
name|tags
parameter_list|(
name|Iterable
argument_list|<
name|Object
argument_list|>
name|tags
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"tags"
argument_list|,
name|tags
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Tags of metrics.          * The option will be converted to a          *<code>java.lang.Iterable&lt;io.micrometer.core.instrument.Tag&gt;</code> type.          * @group producer          */
DECL|method|tags (String tags)
specifier|default
name|MicrometerEndpointBuilder
name|tags
parameter_list|(
name|String
name|tags
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"tags"
argument_list|,
name|tags
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Action expression when using timer type.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|action (String action)
specifier|default
name|MicrometerEndpointBuilder
name|action
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Decrement value expression when using counter type.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|decrement (String decrement)
specifier|default
name|MicrometerEndpointBuilder
name|decrement
parameter_list|(
name|String
name|decrement
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Increment value expression when using counter type.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|increment (String increment)
specifier|default
name|MicrometerEndpointBuilder
name|increment
parameter_list|(
name|String
name|increment
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Value expression when using histogram type.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|value (String value)
specifier|default
name|MicrometerEndpointBuilder
name|value
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|setProperty
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
comment|/**      * Advanced builder for endpoint for the Micrometer component.      */
DECL|interface|AdvancedMicrometerEndpointBuilder
specifier|public
interface|interface
name|AdvancedMicrometerEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|MicrometerEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|MicrometerEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedMicrometerEndpointBuilder
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
name|AdvancedMicrometerEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedMicrometerEndpointBuilder
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
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedMicrometerEndpointBuilder
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
comment|/**      * Proxy enum for<code>io.micrometer.core.instrument.Meter$Type</code>      * enum.      */
DECL|enum|Type
enum|enum
name|Type
block|{
DECL|enumConstant|COUNTER
name|COUNTER
block|,
DECL|enumConstant|GAUGE
name|GAUGE
block|,
DECL|enumConstant|LONG_TASK_TIMER
name|LONG_TASK_TIMER
block|,
DECL|enumConstant|TIMER
name|TIMER
block|,
DECL|enumConstant|DISTRIBUTION_SUMMARY
name|DISTRIBUTION_SUMMARY
block|,
DECL|enumConstant|OTHER
name|OTHER
block|;     }
comment|/**      * To collect various metrics directly from Camel routes using the      * Micrometer library.      * Maven coordinates: org.apache.camel:camel-micrometer      */
DECL|method|micrometer (String path)
specifier|default
name|MicrometerEndpointBuilder
name|micrometer
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|MicrometerEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|MicrometerEndpointBuilder
implements|,
name|AdvancedMicrometerEndpointBuilder
block|{
specifier|public
name|MicrometerEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"micrometer"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|MicrometerEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

