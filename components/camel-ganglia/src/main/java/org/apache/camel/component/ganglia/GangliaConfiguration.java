begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ganglia
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|RuntimeCamelException
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
name|spi
operator|.
name|UriParams
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
name|UriPath
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|GangliaConfiguration
specifier|public
class|class
name|GangliaConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|DEFAULT_DESTINATION
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_DESTINATION
init|=
literal|"239.2.11.71"
decl_stmt|;
DECL|field|DEFAULT_PORT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PORT
init|=
literal|8649
decl_stmt|;
DECL|field|DEFAULT_MODE
specifier|public
specifier|static
specifier|final
name|GMetric
operator|.
name|UDPAddressingMode
name|DEFAULT_MODE
init|=
name|GMetric
operator|.
name|UDPAddressingMode
operator|.
name|MULTICAST
decl_stmt|;
DECL|field|DEFAULT_TTL
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_TTL
init|=
literal|5
decl_stmt|;
DECL|field|DEFAULT_WIRE_FORMAT_31X
specifier|public
specifier|static
specifier|final
name|boolean
name|DEFAULT_WIRE_FORMAT_31X
init|=
literal|true
decl_stmt|;
DECL|field|DEFAULT_GROUP_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_GROUP_NAME
init|=
literal|"Java"
decl_stmt|;
DECL|field|DEFAULT_METRIC_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_METRIC_NAME
init|=
literal|"metric"
decl_stmt|;
DECL|field|DEFAULT_TYPE
specifier|public
specifier|static
specifier|final
name|GMetricType
name|DEFAULT_TYPE
init|=
name|GMetricType
operator|.
name|STRING
decl_stmt|;
DECL|field|DEFAULT_SLOPE
specifier|public
specifier|static
specifier|final
name|GMetricSlope
name|DEFAULT_SLOPE
init|=
name|GMetricSlope
operator|.
name|BOTH
decl_stmt|;
DECL|field|DEFAULT_UNITS
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_UNITS
init|=
literal|""
decl_stmt|;
DECL|field|DEFAULT_TMAX
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_TMAX
init|=
literal|60
decl_stmt|;
DECL|field|DEFAULT_DMAX
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_DMAX
init|=
literal|0
decl_stmt|;
annotation|@
name|UriPath
DECL|field|host
specifier|private
name|String
name|host
init|=
name|DEFAULT_DESTINATION
decl_stmt|;
annotation|@
name|UriPath
DECL|field|port
specifier|private
name|int
name|port
init|=
name|DEFAULT_PORT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"MULTICAST"
argument_list|)
DECL|field|mode
specifier|private
name|GMetric
operator|.
name|UDPAddressingMode
name|mode
init|=
name|DEFAULT_MODE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"5"
argument_list|)
DECL|field|ttl
specifier|private
name|int
name|ttl
init|=
name|DEFAULT_TTL
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|wireFormat31x
specifier|private
name|boolean
name|wireFormat31x
init|=
name|DEFAULT_WIRE_FORMAT_31X
decl_stmt|;
annotation|@
name|UriParam
DECL|field|spoofHostname
specifier|private
name|String
name|spoofHostname
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"java"
argument_list|)
DECL|field|groupName
specifier|private
name|String
name|groupName
init|=
name|DEFAULT_GROUP_NAME
decl_stmt|;
annotation|@
name|UriParam
DECL|field|prefix
specifier|private
name|String
name|prefix
init|=
literal|null
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"metric"
argument_list|)
DECL|field|metricName
specifier|private
name|String
name|metricName
init|=
name|DEFAULT_METRIC_NAME
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"STRING"
argument_list|)
DECL|field|type
specifier|private
name|GMetricType
name|type
init|=
name|DEFAULT_TYPE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"BOTH"
argument_list|)
DECL|field|slope
specifier|private
name|GMetricSlope
name|slope
init|=
name|DEFAULT_SLOPE
decl_stmt|;
annotation|@
name|UriParam
DECL|field|units
specifier|private
name|String
name|units
init|=
name|DEFAULT_UNITS
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"60"
argument_list|)
DECL|field|tmax
specifier|private
name|int
name|tmax
init|=
name|DEFAULT_TMAX
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"0"
argument_list|)
DECL|field|dmax
specifier|private
name|int
name|dmax
init|=
name|DEFAULT_DMAX
decl_stmt|;
comment|/**      * Returns a copy of this configuration      */
DECL|method|copy ()
specifier|public
name|GangliaConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
name|GangliaConfiguration
name|copy
init|=
operator|(
name|GangliaConfiguration
operator|)
name|clone
argument_list|()
decl_stmt|;
return|return
name|copy
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|configure (URI uri)
specifier|public
name|void
name|configure
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|String
name|value
init|=
name|uri
operator|.
name|getHost
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|setHost
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|int
name|port
init|=
name|uri
operator|.
name|getPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|port
operator|>
literal|0
condition|)
block|{
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createGMetric ()
specifier|public
name|GMetric
name|createGMetric
parameter_list|()
block|{
try|try
block|{
return|return
operator|new
name|GMetric
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|mode
argument_list|,
name|ttl
argument_list|,
name|wireFormat31x
argument_list|,
literal|null
argument_list|,
name|spoofHostname
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Failed to initialize Ganglia"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
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
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
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
name|GMetric
operator|.
name|UDPAddressingMode
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
DECL|method|setMode (GMetric.UDPAddressingMode mode)
specifier|public
name|void
name|setMode
parameter_list|(
name|GMetric
operator|.
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
name|int
name|getTtl
parameter_list|()
block|{
return|return
name|ttl
return|;
block|}
DECL|method|setTtl (int ttl)
specifier|public
name|void
name|setTtl
parameter_list|(
name|int
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
name|boolean
name|getWireFormat31x
parameter_list|()
block|{
return|return
name|wireFormat31x
return|;
block|}
DECL|method|setWireFormat31x (boolean wireFormat31x)
specifier|public
name|void
name|setWireFormat31x
parameter_list|(
name|boolean
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
DECL|method|isWireFormat31x ()
specifier|public
name|boolean
name|isWireFormat31x
parameter_list|()
block|{
return|return
name|wireFormat31x
return|;
block|}
DECL|method|getTmax ()
specifier|public
name|int
name|getTmax
parameter_list|()
block|{
return|return
name|tmax
return|;
block|}
DECL|method|setTmax (int tmax)
specifier|public
name|void
name|setTmax
parameter_list|(
name|int
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
name|int
name|getDmax
parameter_list|()
block|{
return|return
name|dmax
return|;
block|}
DECL|method|setDmax (int dmax)
specifier|public
name|void
name|setDmax
parameter_list|(
name|int
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"GangliaConfiguration["
operator|+
literal|"host="
operator|+
name|host
operator|+
literal|":"
operator|+
name|port
operator|+
literal|", mode="
operator|+
name|mode
operator|+
literal|", ttl="
operator|+
name|ttl
operator|+
literal|", wireFormat31x="
operator|+
name|wireFormat31x
operator|+
literal|", spoofHostname="
operator|+
name|spoofHostname
operator|+
literal|", groupName="
operator|+
name|groupName
operator|+
literal|", prefix="
operator|+
name|prefix
operator|+
literal|", metricName="
operator|+
name|metricName
operator|+
literal|", type="
operator|+
name|type
operator|+
literal|", slope="
operator|+
name|slope
operator|+
literal|", units="
operator|+
name|units
operator|+
literal|", tmax="
operator|+
name|tmax
operator|+
literal|", dmax="
operator|+
name|dmax
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

