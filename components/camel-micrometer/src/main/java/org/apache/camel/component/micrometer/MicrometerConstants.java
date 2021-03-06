begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
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
name|Meter
import|;
end_import

begin_class
DECL|class|MicrometerConstants
specifier|public
specifier|final
class|class
name|MicrometerConstants
block|{
DECL|field|HEADER_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_PREFIX
init|=
literal|"CamelMetrics"
decl_stmt|;
DECL|field|HEADER_TIMER_ACTION
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_TIMER_ACTION
init|=
name|HEADER_PREFIX
operator|+
literal|"TimerAction"
decl_stmt|;
DECL|field|HEADER_HISTOGRAM_VALUE
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_HISTOGRAM_VALUE
init|=
name|HEADER_PREFIX
operator|+
literal|"HistogramValue"
decl_stmt|;
DECL|field|HEADER_COUNTER_DECREMENT
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_COUNTER_DECREMENT
init|=
name|HEADER_PREFIX
operator|+
literal|"CounterDecrement"
decl_stmt|;
DECL|field|HEADER_COUNTER_INCREMENT
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_COUNTER_INCREMENT
init|=
name|HEADER_PREFIX
operator|+
literal|"CounterIncrement"
decl_stmt|;
DECL|field|HEADER_METRIC_NAME
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_METRIC_NAME
init|=
name|HEADER_PREFIX
operator|+
literal|"Name"
decl_stmt|;
DECL|field|HEADER_METRIC_TAGS
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_METRIC_TAGS
init|=
name|HEADER_PREFIX
operator|+
literal|"Tags"
decl_stmt|;
DECL|field|DEFAULT_CAMEL_MESSAGE_HISTORY_METER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_CAMEL_MESSAGE_HISTORY_METER_NAME
init|=
literal|"CamelMessageHistory"
decl_stmt|;
DECL|field|DEFAULT_CAMEL_ROUTE_POLICY_METER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_CAMEL_ROUTE_POLICY_METER_NAME
init|=
literal|"CamelRoutePolicy"
decl_stmt|;
DECL|field|DEFAULT_CAMEL_EXCHANGE_EVENT_METER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_CAMEL_EXCHANGE_EVENT_METER_NAME
init|=
literal|"CamelExchangeEventNotifier"
decl_stmt|;
DECL|field|DEFAULT_CAMEL_ROUTES_ADDED
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_CAMEL_ROUTES_ADDED
init|=
literal|"CamelRoutesAdded"
decl_stmt|;
DECL|field|DEFAULT_CAMEL_ROUTES_RUNNING
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_CAMEL_ROUTES_RUNNING
init|=
literal|"CamelRoutesRunning"
decl_stmt|;
DECL|field|ROUTE_ID_TAG
specifier|public
specifier|static
specifier|final
name|String
name|ROUTE_ID_TAG
init|=
literal|"routeId"
decl_stmt|;
DECL|field|NODE_ID_TAG
specifier|public
specifier|static
specifier|final
name|String
name|NODE_ID_TAG
init|=
literal|"nodeId"
decl_stmt|;
DECL|field|FAILED_TAG
specifier|public
specifier|static
specifier|final
name|String
name|FAILED_TAG
init|=
literal|"failed"
decl_stmt|;
DECL|field|CAMEL_CONTEXT_TAG
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CONTEXT_TAG
init|=
literal|"camelContext"
decl_stmt|;
DECL|field|EVENT_TYPE_TAG
specifier|public
specifier|static
specifier|final
name|String
name|EVENT_TYPE_TAG
init|=
literal|"eventType"
decl_stmt|;
DECL|field|METRICS_REGISTRY_NAME
specifier|public
specifier|static
specifier|final
name|String
name|METRICS_REGISTRY_NAME
init|=
literal|"metricsRegistry"
decl_stmt|;
DECL|field|SERVICE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_NAME
init|=
literal|"serviceName"
decl_stmt|;
DECL|field|ENDPOINT_NAME
specifier|public
specifier|static
specifier|final
name|String
name|ENDPOINT_NAME
init|=
literal|"endpointName"
decl_stmt|;
DECL|field|CAMEL_METERS
specifier|public
specifier|static
specifier|final
name|Predicate
argument_list|<
name|Meter
operator|.
name|Id
argument_list|>
name|CAMEL_METERS
init|=
name|id
lambda|->
name|id
operator|.
name|getTag
argument_list|(
name|CAMEL_CONTEXT_TAG
argument_list|)
operator|!=
literal|null
decl_stmt|;
DECL|field|TIMERS
specifier|public
specifier|static
specifier|final
name|Predicate
argument_list|<
name|Meter
operator|.
name|Id
argument_list|>
name|TIMERS
init|=
name|id
lambda|->
name|id
operator|.
name|getType
argument_list|()
operator|==
name|Meter
operator|.
name|Type
operator|.
name|TIMER
decl_stmt|;
DECL|field|DISTRIBUTION_SUMMARIES
specifier|public
specifier|static
specifier|final
name|Predicate
argument_list|<
name|Meter
operator|.
name|Id
argument_list|>
name|DISTRIBUTION_SUMMARIES
init|=
name|id
lambda|->
name|id
operator|.
name|getType
argument_list|()
operator|==
name|Meter
operator|.
name|Type
operator|.
name|DISTRIBUTION_SUMMARY
decl_stmt|;
DECL|field|ALWAYS
specifier|public
specifier|static
specifier|final
name|Predicate
argument_list|<
name|Meter
operator|.
name|Id
argument_list|>
name|ALWAYS
init|=
name|id
lambda|->
literal|true
decl_stmt|;
DECL|method|MicrometerConstants ()
specifier|private
name|MicrometerConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

