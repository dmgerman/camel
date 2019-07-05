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
comment|/**  * The ganglia component is used for sending metrics to the Ganglia monitoring  * system.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|GangliaEndpointBuilderFactory
specifier|public
interface|interface
name|GangliaEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Ganglia component.      */
DECL|interface|GangliaEndpointBuilder
specifier|public
interface|interface
name|GangliaEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGangliaEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGangliaEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Minumum time in seconds before Ganglia will purge the metric value if          * it expires. Set to 0 and the value will remain in Ganglia          * indefinitely until a gmond agent restart.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|dmax (int dmax)
specifier|default
name|GangliaEndpointBuilder
name|dmax
parameter_list|(
name|int
name|dmax
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"dmax"
argument_list|,
name|dmax
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Minumum time in seconds before Ganglia will purge the metric value if          * it expires. Set to 0 and the value will remain in Ganglia          * indefinitely until a gmond agent restart.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|dmax (String dmax)
specifier|default
name|GangliaEndpointBuilder
name|dmax
parameter_list|(
name|String
name|dmax
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"dmax"
argument_list|,
name|dmax
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The group that the metric belongs to.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|groupName (String groupName)
specifier|default
name|GangliaEndpointBuilder
name|groupName
parameter_list|(
name|String
name|groupName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"groupName"
argument_list|,
name|groupName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The name to use for the metric.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|metricName (String metricName)
specifier|default
name|GangliaEndpointBuilder
name|metricName
parameter_list|(
name|String
name|metricName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"metricName"
argument_list|,
name|metricName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Send the UDP metric packets using MULTICAST or UNICAST.          *           * The option is a:          *<code>info.ganglia.gmetric4j.gmetric.GMetric$UDPAddressingMode</code>          * type.          *           * Group: producer          */
DECL|method|mode (UDPAddressingMode mode)
specifier|default
name|GangliaEndpointBuilder
name|mode
parameter_list|(
name|UDPAddressingMode
name|mode
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Send the UDP metric packets using MULTICAST or UNICAST.          *           * The option will be converted to a          *<code>info.ganglia.gmetric4j.gmetric.GMetric$UDPAddressingMode</code>          * type.          *           * Group: producer          */
DECL|method|mode (String mode)
specifier|default
name|GangliaEndpointBuilder
name|mode
parameter_list|(
name|String
name|mode
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Prefix the metric name with this string and an underscore.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|prefix (String prefix)
specifier|default
name|GangliaEndpointBuilder
name|prefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"prefix"
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The slope.          *           * The option is a:          *<code>info.ganglia.gmetric4j.gmetric.GMetricSlope</code> type.          *           * Group: producer          */
DECL|method|slope (GMetricSlope slope)
specifier|default
name|GangliaEndpointBuilder
name|slope
parameter_list|(
name|GMetricSlope
name|slope
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"slope"
argument_list|,
name|slope
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The slope.          *           * The option will be converted to a          *<code>info.ganglia.gmetric4j.gmetric.GMetricSlope</code> type.          *           * Group: producer          */
DECL|method|slope (String slope)
specifier|default
name|GangliaEndpointBuilder
name|slope
parameter_list|(
name|String
name|slope
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"slope"
argument_list|,
name|slope
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Spoofing information IP:hostname.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|spoofHostname (String spoofHostname)
specifier|default
name|GangliaEndpointBuilder
name|spoofHostname
parameter_list|(
name|String
name|spoofHostname
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"spoofHostname"
argument_list|,
name|spoofHostname
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Maximum time in seconds that the value can be considered current.          * After this, Ganglia considers the value to have expired.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|tmax (int tmax)
specifier|default
name|GangliaEndpointBuilder
name|tmax
parameter_list|(
name|int
name|tmax
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"tmax"
argument_list|,
name|tmax
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Maximum time in seconds that the value can be considered current.          * After this, Ganglia considers the value to have expired.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|tmax (String tmax)
specifier|default
name|GangliaEndpointBuilder
name|tmax
parameter_list|(
name|String
name|tmax
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"tmax"
argument_list|,
name|tmax
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If using multicast, set the TTL of the packets.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|ttl (int ttl)
specifier|default
name|GangliaEndpointBuilder
name|ttl
parameter_list|(
name|int
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
comment|/**          * If using multicast, set the TTL of the packets.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|ttl (String ttl)
specifier|default
name|GangliaEndpointBuilder
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
comment|/**          * The type of value.          *           * The option is a:          *<code>info.ganglia.gmetric4j.gmetric.GMetricType</code> type.          *           * Group: producer          */
DECL|method|type (GMetricType type)
specifier|default
name|GangliaEndpointBuilder
name|type
parameter_list|(
name|GMetricType
name|type
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The type of value.          *           * The option will be converted to a          *<code>info.ganglia.gmetric4j.gmetric.GMetricType</code> type.          *           * Group: producer          */
DECL|method|type (String type)
specifier|default
name|GangliaEndpointBuilder
name|type
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Any unit of measurement that qualifies the metric, e.g. widgets,          * litres, bytes. Do not include a prefix such as k (kilo) or m (milli),          * other tools may scale the units later. The value should be unscaled.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|units (String units)
specifier|default
name|GangliaEndpointBuilder
name|units
parameter_list|(
name|String
name|units
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"units"
argument_list|,
name|units
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Use the wire format of Ganglia 3.1.0 and later versions. Set this to          * false to use Ganglia 3.0.x or earlier.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|wireFormat31x (boolean wireFormat31x)
specifier|default
name|GangliaEndpointBuilder
name|wireFormat31x
parameter_list|(
name|boolean
name|wireFormat31x
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"wireFormat31x"
argument_list|,
name|wireFormat31x
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Use the wire format of Ganglia 3.1.0 and later versions. Set this to          * false to use Ganglia 3.0.x or earlier.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|wireFormat31x (String wireFormat31x)
specifier|default
name|GangliaEndpointBuilder
name|wireFormat31x
parameter_list|(
name|String
name|wireFormat31x
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"wireFormat31x"
argument_list|,
name|wireFormat31x
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Ganglia component.      */
DECL|interface|AdvancedGangliaEndpointBuilder
specifier|public
interface|interface
name|AdvancedGangliaEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GangliaEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GangliaEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedGangliaEndpointBuilder
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
name|AdvancedGangliaEndpointBuilder
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
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedGangliaEndpointBuilder
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
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedGangliaEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>info.ganglia.gmetric4j.gmetric.GMetric$UDPAddressingMode</code>      * enum.      */
DECL|enum|UDPAddressingMode
enum|enum
name|UDPAddressingMode
block|{
DECL|enumConstant|MULTICAST
name|MULTICAST
block|,
DECL|enumConstant|UNICAST
name|UNICAST
block|;     }
comment|/**      * Proxy enum for<code>info.ganglia.gmetric4j.gmetric.GMetricSlope</code>      * enum.      */
DECL|enum|GMetricSlope
enum|enum
name|GMetricSlope
block|{
DECL|enumConstant|ZERO
name|ZERO
block|,
DECL|enumConstant|POSITIVE
name|POSITIVE
block|,
DECL|enumConstant|NEGATIVE
name|NEGATIVE
block|,
DECL|enumConstant|BOTH
name|BOTH
block|;     }
comment|/**      * Proxy enum for<code>info.ganglia.gmetric4j.gmetric.GMetricType</code>      * enum.      */
DECL|enum|GMetricType
enum|enum
name|GMetricType
block|{
DECL|enumConstant|STRING
name|STRING
block|,
DECL|enumConstant|INT8
name|INT8
block|,
DECL|enumConstant|UINT8
name|UINT8
block|,
DECL|enumConstant|INT16
name|INT16
block|,
DECL|enumConstant|UINT16
name|UINT16
block|,
DECL|enumConstant|INT32
name|INT32
block|,
DECL|enumConstant|UINT32
name|UINT32
block|,
DECL|enumConstant|FLOAT
name|FLOAT
block|,
DECL|enumConstant|DOUBLE
name|DOUBLE
block|;     }
comment|/**      * Ganglia (camel-ganglia)      * The ganglia component is used for sending metrics to the Ganglia      * monitoring system.      *       * Syntax:<code>ganglia:host:port</code>      * Category: monitoring      * Available as of version: 2.15      * Maven coordinates: org.apache.camel:camel-ganglia      */
DECL|method|ganglia (String path)
specifier|default
name|GangliaEndpointBuilder
name|ganglia
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|GangliaEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|GangliaEndpointBuilder
implements|,
name|AdvancedGangliaEndpointBuilder
block|{
specifier|public
name|GangliaEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"ganglia"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|GangliaEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

