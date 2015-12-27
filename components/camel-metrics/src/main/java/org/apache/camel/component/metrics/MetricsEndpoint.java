begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.metrics
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|metrics
package|;
end_package

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|MetricRegistry
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
comment|/**  * To collect various metrics directly from Camel routes using the DropWizard metrics library.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"metrics"
argument_list|,
name|title
operator|=
literal|"Metrics"
argument_list|,
name|syntax
operator|=
literal|"metrics:metricsType:metricsName"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"monitoring"
argument_list|)
DECL|class|MetricsEndpoint
specifier|public
class|class
name|MetricsEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|registry
specifier|protected
specifier|final
name|MetricRegistry
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
name|MetricsType
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
name|UriParam
argument_list|(
name|description
operator|=
literal|"Action when using timer type"
argument_list|)
DECL|field|action
specifier|private
name|MetricsTimerAction
name|action
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Mark when using meter type"
argument_list|)
DECL|field|mark
specifier|private
name|Long
name|mark
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Value value when using histogram type"
argument_list|)
DECL|field|value
specifier|private
name|Long
name|value
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Increment value when using counter type"
argument_list|)
DECL|field|increment
specifier|private
name|Long
name|increment
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Decrement value when using counter type"
argument_list|)
DECL|field|decrement
specifier|private
name|Long
name|decrement
decl_stmt|;
DECL|method|MetricsEndpoint (String uri, Component component, MetricRegistry registry, MetricsType metricsType, String metricsName)
specifier|public
name|MetricsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|MetricRegistry
name|registry
parameter_list|,
name|MetricsType
name|metricsType
parameter_list|,
name|String
name|metricsName
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
throws|throws
name|Exception
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
throws|throws
name|Exception
block|{
if|if
condition|(
name|metricsType
operator|==
name|MetricsType
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
name|MetricsType
operator|.
name|HISTOGRAM
condition|)
block|{
return|return
operator|new
name|HistogramProducer
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
name|MetricsType
operator|.
name|METER
condition|)
block|{
return|return
operator|new
name|MeterProducer
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
name|MetricsType
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
name|MetricRegistry
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
DECL|method|getMetricsType ()
specifier|public
name|MetricsType
name|getMetricsType
parameter_list|()
block|{
return|return
name|metricsType
return|;
block|}
DECL|method|getAction ()
specifier|public
name|MetricsTimerAction
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
DECL|method|setAction (MetricsTimerAction action)
specifier|public
name|void
name|setAction
parameter_list|(
name|MetricsTimerAction
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
DECL|method|getMark ()
specifier|public
name|Long
name|getMark
parameter_list|()
block|{
return|return
name|mark
return|;
block|}
DECL|method|setMark (Long mark)
specifier|public
name|void
name|setMark
parameter_list|(
name|Long
name|mark
parameter_list|)
block|{
name|this
operator|.
name|mark
operator|=
name|mark
expr_stmt|;
block|}
DECL|method|getValue ()
specifier|public
name|Long
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|setValue (Long value)
specifier|public
name|void
name|setValue
parameter_list|(
name|Long
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
name|Long
name|getIncrement
parameter_list|()
block|{
return|return
name|increment
return|;
block|}
DECL|method|setIncrement (Long increment)
specifier|public
name|void
name|setIncrement
parameter_list|(
name|Long
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
name|Long
name|getDecrement
parameter_list|()
block|{
return|return
name|decrement
return|;
block|}
DECL|method|setDecrement (Long decrement)
specifier|public
name|void
name|setDecrement
parameter_list|(
name|Long
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
block|}
end_class

end_unit

