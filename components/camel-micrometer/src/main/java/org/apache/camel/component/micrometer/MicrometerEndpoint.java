begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
package|;
end_package

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|Meter
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|MeterRegistry
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|Tag
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
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|impl
operator|.
name|DefaultEndpoint
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
name|Metadata
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
name|UriEndpoint
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
name|UriPath
import|;
end_import

begin_comment
comment|/**  * To collect various metrics directly from Camel routes using the Micrometer library.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.22.0"
argument_list|,
name|scheme
operator|=
literal|"micrometer"
argument_list|,
name|title
operator|=
literal|"Micrometer"
argument_list|,
name|syntax
operator|=
literal|"micrometer:metricsType:meterName"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"monitoring"
argument_list|)
DECL|class|MicrometerEndpoint
specifier|public
class|class
name|MicrometerEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|registry
specifier|protected
name|MeterRegistry
name|registry
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Type of metrics"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|metricsType
specifier|protected
specifier|final
name|Meter
operator|.
name|Type
name|metricsType
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name of metrics"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|metricsName
specifier|protected
specifier|final
name|String
name|metricsName
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Tags of metrics"
argument_list|)
DECL|field|tags
specifier|protected
specifier|final
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|tags
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Action expression when using timer type"
argument_list|)
DECL|field|action
specifier|private
name|String
name|action
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Value expression when using histogram type"
argument_list|)
DECL|field|value
specifier|private
name|String
name|value
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Increment value expression when using counter type"
argument_list|)
DECL|field|increment
specifier|private
name|String
name|increment
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Decrement value expression when using counter type"
argument_list|)
DECL|field|decrement
specifier|private
name|String
name|decrement
decl_stmt|;
DECL|method|MicrometerEndpoint (String uri, Component component, MeterRegistry registry, Meter.Type metricsType, String metricsName, Iterable<Tag> tags)
specifier|public
name|MicrometerEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|MeterRegistry
name|registry
parameter_list|,
name|Meter
operator|.
name|Type
name|metricsType
parameter_list|,
name|String
name|metricsName
parameter_list|,
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|tags
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
name|this
operator|.
name|metricsType
operator|=
name|metricsType
expr_stmt|;
name|this
operator|.
name|metricsName
operator|=
name|metricsName
expr_stmt|;
name|this
operator|.
name|tags
operator|=
name|tags
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot consume from "
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|": "
operator|+
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
block|{
if|if
condition|(
name|metricsType
operator|==
name|Meter
operator|.
name|Type
operator|.
name|COUNTER
condition|)
block|{
return|return
operator|new
name|CounterProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|metricsType
operator|==
name|Meter
operator|.
name|Type
operator|.
name|DISTRIBUTION_SUMMARY
condition|)
block|{
return|return
operator|new
name|DistributionSummaryProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|metricsType
operator|==
name|Meter
operator|.
name|Type
operator|.
name|TIMER
condition|)
block|{
return|return
operator|new
name|TimerProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Metrics type "
operator|+
name|metricsType
operator|+
literal|" is not supported"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getRegistry ()
specifier|public
name|MeterRegistry
name|getRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
DECL|method|getMetricsName ()
specifier|public
name|String
name|getMetricsName
parameter_list|()
block|{
return|return
name|metricsName
return|;
block|}
DECL|method|getTags ()
specifier|public
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|getTags
parameter_list|()
block|{
return|return
name|tags
return|;
block|}
DECL|method|getMetricsType ()
specifier|public
name|Meter
operator|.
name|Type
name|getMetricsType
parameter_list|()
block|{
return|return
name|metricsType
return|;
block|}
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
DECL|method|getValue ()
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|setValue (String value)
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getIncrement ()
specifier|public
name|String
name|getIncrement
parameter_list|()
block|{
return|return
name|increment
return|;
block|}
DECL|method|setIncrement (String increment)
specifier|public
name|void
name|setIncrement
parameter_list|(
name|String
name|increment
parameter_list|)
block|{
name|this
operator|.
name|increment
operator|=
name|increment
expr_stmt|;
block|}
DECL|method|getDecrement ()
specifier|public
name|String
name|getDecrement
parameter_list|()
block|{
return|return
name|decrement
return|;
block|}
DECL|method|setDecrement (String decrement)
specifier|public
name|void
name|setDecrement
parameter_list|(
name|String
name|decrement
parameter_list|)
block|{
name|this
operator|.
name|decrement
operator|=
name|decrement
expr_stmt|;
block|}
DECL|method|setRegistry (MeterRegistry registry)
name|void
name|setRegistry
parameter_list|(
name|MeterRegistry
name|registry
parameter_list|)
block|{
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
block|}
block|}
end_class

end_unit

