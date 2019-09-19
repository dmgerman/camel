begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.microprofile.metrics.event.notifier.exchange
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|event
operator|.
name|notifier
operator|.
name|exchange
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|CamelExecutionException
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
name|Exchange
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
name|RoutesBuilder
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
name|RouteBuilder
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
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsTestSupport
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
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|gauge
operator|.
name|AtomicIntegerGauge
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|support
operator|.
name|ExpressionAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|Counter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|Tag
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|Timer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|CAMEL_CONTEXT_METRIC_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|CAMEL_CONTEXT_TAG
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|EXCHANGES_COMPLETED_METRIC_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|EXCHANGES_EXTERNAL_REDELIVERIES_METRIC_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|EXCHANGES_FAILED_METRIC_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|EXCHANGES_FAILURES_HANDLED_METRIC_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|EXCHANGES_INFLIGHT_METRIC_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|EXCHANGES_TOTAL_METRIC_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|PROCESSING_METRICS_SUFFIX
import|;
end_import

begin_class
DECL|class|MicroProfileMetricsExchangeEventNotifierTest
specifier|public
class|class
name|MicroProfileMetricsExchangeEventNotifierTest
extends|extends
name|MicroProfileMetricsTestSupport
block|{
annotation|@
name|Test
DECL|method|testMicroProfileMetricsEventNotifier ()
specifier|public
name|void
name|testMicroProfileMetricsEventNotifier
parameter_list|()
block|{
name|int
name|count
init|=
literal|10
decl_stmt|;
name|Long
name|delay
init|=
literal|50L
decl_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|returnReplyBody
argument_list|(
operator|new
name|ExpressionAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|50L
argument_list|)
expr_stmt|;
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|CamelExecutionException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
name|count
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Expected
block|}
block|}
name|Timer
name|timer
init|=
name|getTimer
argument_list|(
literal|"mock://result"
operator|+
name|PROCESSING_METRICS_SUFFIX
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|timer
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|timer
operator|.
name|getSnapshot
argument_list|()
operator|.
name|getMean
argument_list|()
operator|>
name|delay
operator|.
name|doubleValue
argument_list|()
argument_list|)
expr_stmt|;
name|Tag
index|[]
name|tags
init|=
operator|new
name|Tag
index|[]
block|{
operator|new
name|Tag
argument_list|(
name|CAMEL_CONTEXT_TAG
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|)
block|}
decl_stmt|;
name|Counter
name|exchangesCompleted
init|=
name|getCounter
argument_list|(
name|CAMEL_CONTEXT_METRIC_NAME
operator|+
name|EXCHANGES_COMPLETED_METRIC_NAME
argument_list|,
name|tags
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|count
argument_list|,
name|exchangesCompleted
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|Counter
name|exchangesFailed
init|=
name|getCounter
argument_list|(
name|CAMEL_CONTEXT_METRIC_NAME
operator|+
name|EXCHANGES_FAILED_METRIC_NAME
argument_list|,
name|tags
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|exchangesFailed
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|Counter
name|exchangesTotal
init|=
name|getCounter
argument_list|(
name|CAMEL_CONTEXT_METRIC_NAME
operator|+
name|EXCHANGES_TOTAL_METRIC_NAME
argument_list|,
name|tags
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|count
argument_list|,
name|exchangesTotal
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|AtomicIntegerGauge
name|exchangesInflight
init|=
name|getAtomicIntegerGauge
argument_list|(
name|CAMEL_CONTEXT_METRIC_NAME
operator|+
name|EXCHANGES_INFLIGHT_METRIC_NAME
argument_list|,
name|tags
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|exchangesInflight
operator|.
name|getValue
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Counter
name|externalRedeliveries
init|=
name|getCounter
argument_list|(
name|CAMEL_CONTEXT_METRIC_NAME
operator|+
name|EXCHANGES_EXTERNAL_REDELIVERIES_METRIC_NAME
argument_list|,
name|tags
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|externalRedeliveries
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|Counter
name|failuresHandled
init|=
name|getCounter
argument_list|(
name|CAMEL_CONTEXT_METRIC_NAME
operator|+
name|EXCHANGES_FAILURES_HANDLED_METRIC_NAME
argument_list|,
name|tags
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|failuresHandled
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|MicroProfileMetricsExchangeEventNotifier
name|eventNotifier
init|=
operator|new
name|MicroProfileMetricsExchangeEventNotifier
argument_list|()
decl_stmt|;
name|eventNotifier
operator|.
name|setNamingStrategy
argument_list|(
parameter_list|(
name|exchange
parameter_list|,
name|endpoint
parameter_list|)
lambda|->
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|eventNotifier
operator|.
name|setMetricRegistry
argument_list|(
name|metricRegistry
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
name|eventNotifier
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|onException
argument_list|(
name|IllegalStateException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"test"
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|Integer
name|count
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|IllegalStateException
name|foo
init|=
operator|new
name|IllegalStateException
argument_list|(
literal|"Invalid count"
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
throw|throw
name|foo
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

