begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.microprofile.metrics.event.notifier.context
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
name|context
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|ServiceStatus
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
name|LambdaGauge
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
name|CAMEL_CONTEXT_STATUS_METRIC_NAME
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
name|CAMEL_CONTEXT_UPTIME_METRIC_NAME
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
name|MicroProfileMetricsHelper
operator|.
name|findMetric
import|;
end_import

begin_class
DECL|class|MicroProfileMetricsCamelContextEventNotifierTest
specifier|public
class|class
name|MicroProfileMetricsCamelContextEventNotifierTest
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
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Tag
argument_list|>
name|tags
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|tags
operator|.
name|add
argument_list|(
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
argument_list|)
expr_stmt|;
name|LambdaGauge
name|uptime
init|=
name|findMetric
argument_list|(
name|metricRegistry
argument_list|,
name|CAMEL_CONTEXT_UPTIME_METRIC_NAME
argument_list|,
name|LambdaGauge
operator|.
name|class
argument_list|,
name|tags
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|uptime
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|uptime
operator|.
name|getValue
argument_list|()
operator|.
name|intValue
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|LambdaGauge
name|status
init|=
name|findMetric
argument_list|(
name|metricRegistry
argument_list|,
name|CAMEL_CONTEXT_STATUS_METRIC_NAME
argument_list|,
name|LambdaGauge
operator|.
name|class
argument_list|,
name|tags
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|status
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
operator|.
name|ordinal
argument_list|()
argument_list|,
name|status
operator|.
name|getValue
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
operator|.
name|ordinal
argument_list|()
argument_list|,
name|status
operator|.
name|getValue
argument_list|()
operator|.
name|intValue
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
operator|new
name|MicroProfileMetricsCamelContextEventNotifier
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
block|}
end_class

end_unit

