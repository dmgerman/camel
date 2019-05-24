begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ganglia.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ganglia
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
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|gmetric
operator|.
name|GMetric
operator|.
name|UDPAddressingMode
import|;
end_import

begin_import
import|import
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|gmetric
operator|.
name|GMetricSlope
import|;
end_import

begin_import
import|import
name|info
operator|.
name|ganglia
operator|.
name|gmetric4j
operator|.
name|gmetric
operator|.
name|GMetricType
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
comment|/**  * The ganglia component is used for sending metrics to the Ganglia monitoring  * system.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.ganglia"
argument_list|)
DECL|class|GangliaComponentConfiguration
specifier|public
class|class
name|GangliaComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the ganglia component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use the shared configuration      */
DECL|field|configuration
specifier|private
name|GangliaConfigurationNestedConfiguration
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
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|GangliaConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( GangliaConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|GangliaConfigurationNestedConfiguration
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
DECL|class|GangliaConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|GangliaConfigurationNestedConfiguration
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
name|ganglia
operator|.
name|GangliaConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * Host name for Ganglia server          */
DECL|field|host
specifier|private
name|String
name|host
init|=
literal|"239.2.11.71"
decl_stmt|;
comment|/**          * Port for Ganglia server          */
DECL|field|port
specifier|private
name|Integer
name|port
init|=
literal|8649
decl_stmt|;
comment|/**          * Send the UDP metric packets using MULTICAST or UNICAST          */
DECL|field|mode
specifier|private
name|UDPAddressingMode
name|mode
init|=
name|UDPAddressingMode
operator|.
name|MULTICAST
decl_stmt|;
comment|/**          * If using multicast, set the TTL of the packets          */
DECL|field|ttl
specifier|private
name|Integer
name|ttl
init|=
literal|5
decl_stmt|;
comment|/**          * Use the wire format of Ganglia 3.1.0 and later versions. Set this to          * false to use Ganglia 3.0.x or earlier.          */
DECL|field|wireFormat31x
specifier|private
name|Boolean
name|wireFormat31x
init|=
literal|true
decl_stmt|;
comment|/**          * Spoofing information IP:hostname          */
DECL|field|spoofHostname
specifier|private
name|String
name|spoofHostname
decl_stmt|;
comment|/**          * The group that the metric belongs to.          */
DECL|field|groupName
specifier|private
name|String
name|groupName
init|=
literal|"java"
decl_stmt|;
comment|/**          * Prefix the metric name with this string and an underscore.          */
DECL|field|prefix
specifier|private
name|String
name|prefix
decl_stmt|;
comment|/**          * The name to use for the metric.          */
DECL|field|metricName
specifier|private
name|String
name|metricName
init|=
literal|"metric"
decl_stmt|;
comment|/**          * The type of value          */
DECL|field|type
specifier|private
name|GMetricType
name|type
init|=
name|GMetricType
operator|.
name|STRING
decl_stmt|;
comment|/**          * The slope          */
DECL|field|slope
specifier|private
name|GMetricSlope
name|slope
init|=
name|GMetricSlope
operator|.
name|BOTH
decl_stmt|;
comment|/**          * Any unit of measurement that qualifies the metric, e.g. widgets,          * litres, bytes. Do not include a prefix such as k (kilo) or m (milli),          * other tools may scale the units later. The value should be unscaled.          */
DECL|field|units
specifier|private
name|String
name|units
decl_stmt|;
comment|/**          * Maximum time in seconds that the value can be considered current.          * After this, Ganglia considers the value to have expired.          */
DECL|field|tmax
specifier|private
name|Integer
name|tmax
init|=
literal|60
decl_stmt|;
comment|/**          * Minumum time in seconds before Ganglia will purge the metric value if          * it expires. Set to 0 and the value will remain in Ganglia          * indefinitely until a gmond agent restart.          */
DECL|field|dmax
specifier|private
name|Integer
name|dmax
init|=
literal|0
decl_stmt|;
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getMode ()
specifier|public
name|UDPAddressingMode
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
DECL|method|setMode (UDPAddressingMode mode)
specifier|public
name|void
name|setMode
parameter_list|(
name|UDPAddressingMode
name|mode
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
block|}
DECL|method|getTtl ()
specifier|public
name|Integer
name|getTtl
parameter_list|()
block|{
return|return
name|ttl
return|;
block|}
DECL|method|setTtl (Integer ttl)
specifier|public
name|void
name|setTtl
parameter_list|(
name|Integer
name|ttl
parameter_list|)
block|{
name|this
operator|.
name|ttl
operator|=
name|ttl
expr_stmt|;
block|}
DECL|method|getWireFormat31x ()
specifier|public
name|Boolean
name|getWireFormat31x
parameter_list|()
block|{
return|return
name|wireFormat31x
return|;
block|}
DECL|method|setWireFormat31x (Boolean wireFormat31x)
specifier|public
name|void
name|setWireFormat31x
parameter_list|(
name|Boolean
name|wireFormat31x
parameter_list|)
block|{
name|this
operator|.
name|wireFormat31x
operator|=
name|wireFormat31x
expr_stmt|;
block|}
DECL|method|getSpoofHostname ()
specifier|public
name|String
name|getSpoofHostname
parameter_list|()
block|{
return|return
name|spoofHostname
return|;
block|}
DECL|method|setSpoofHostname (String spoofHostname)
specifier|public
name|void
name|setSpoofHostname
parameter_list|(
name|String
name|spoofHostname
parameter_list|)
block|{
name|this
operator|.
name|spoofHostname
operator|=
name|spoofHostname
expr_stmt|;
block|}
DECL|method|getGroupName ()
specifier|public
name|String
name|getGroupName
parameter_list|()
block|{
return|return
name|groupName
return|;
block|}
DECL|method|setGroupName (String groupName)
specifier|public
name|void
name|setGroupName
parameter_list|(
name|String
name|groupName
parameter_list|)
block|{
name|this
operator|.
name|groupName
operator|=
name|groupName
expr_stmt|;
block|}
DECL|method|getPrefix ()
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
DECL|method|setPrefix (String prefix)
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
block|}
DECL|method|getMetricName ()
specifier|public
name|String
name|getMetricName
parameter_list|()
block|{
return|return
name|metricName
return|;
block|}
DECL|method|setMetricName (String metricName)
specifier|public
name|void
name|setMetricName
parameter_list|(
name|String
name|metricName
parameter_list|)
block|{
name|this
operator|.
name|metricName
operator|=
name|metricName
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|GMetricType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (GMetricType type)
specifier|public
name|void
name|setType
parameter_list|(
name|GMetricType
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
DECL|method|getSlope ()
specifier|public
name|GMetricSlope
name|getSlope
parameter_list|()
block|{
return|return
name|slope
return|;
block|}
DECL|method|setSlope (GMetricSlope slope)
specifier|public
name|void
name|setSlope
parameter_list|(
name|GMetricSlope
name|slope
parameter_list|)
block|{
name|this
operator|.
name|slope
operator|=
name|slope
expr_stmt|;
block|}
DECL|method|getUnits ()
specifier|public
name|String
name|getUnits
parameter_list|()
block|{
return|return
name|units
return|;
block|}
DECL|method|setUnits (String units)
specifier|public
name|void
name|setUnits
parameter_list|(
name|String
name|units
parameter_list|)
block|{
name|this
operator|.
name|units
operator|=
name|units
expr_stmt|;
block|}
DECL|method|getTmax ()
specifier|public
name|Integer
name|getTmax
parameter_list|()
block|{
return|return
name|tmax
return|;
block|}
DECL|method|setTmax (Integer tmax)
specifier|public
name|void
name|setTmax
parameter_list|(
name|Integer
name|tmax
parameter_list|)
block|{
name|this
operator|.
name|tmax
operator|=
name|tmax
expr_stmt|;
block|}
DECL|method|getDmax ()
specifier|public
name|Integer
name|getDmax
parameter_list|()
block|{
return|return
name|dmax
return|;
block|}
DECL|method|setDmax (Integer dmax)
specifier|public
name|void
name|setDmax
parameter_list|(
name|Integer
name|dmax
parameter_list|)
block|{
name|this
operator|.
name|dmax
operator|=
name|dmax
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

