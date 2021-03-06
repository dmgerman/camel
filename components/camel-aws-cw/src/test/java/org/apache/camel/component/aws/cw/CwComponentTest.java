begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.cw
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|cw
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|cloudwatch
operator|.
name|AmazonCloudWatchClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|cloudwatch
operator|.
name|model
operator|.
name|Dimension
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|cloudwatch
operator|.
name|model
operator|.
name|MetricDatum
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|cloudwatch
operator|.
name|model
operator|.
name|PutMetricDataRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|cloudwatch
operator|.
name|model
operator|.
name|StandardUnit
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
name|BindToRegistry
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
name|ExchangePattern
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
import|import
name|org
operator|.
name|mockito
operator|.
name|ArgumentCaptor
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|Is
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_class
DECL|class|CwComponentTest
specifier|public
class|class
name|CwComponentTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"now"
argument_list|)
DECL|field|NOW
specifier|private
specifier|static
specifier|final
name|Date
name|NOW
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
DECL|field|LATER
specifier|private
specifier|static
specifier|final
name|Date
name|LATER
init|=
operator|new
name|Date
argument_list|(
name|NOW
operator|.
name|getTime
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"amazonCwClient"
argument_list|)
DECL|field|cloudWatchClient
specifier|private
name|AmazonCloudWatchClient
name|cloudWatchClient
init|=
name|mock
argument_list|(
name|AmazonCloudWatchClient
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|sendMetricFromHeaderValues ()
specifier|public
name|void
name|sendMetricFromHeaderValues
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CwConstants
operator|.
name|METRIC_NAMESPACE
argument_list|,
literal|"camel.apache.org/overriden"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CwConstants
operator|.
name|METRIC_NAME
argument_list|,
literal|"OverridenMetric"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CwConstants
operator|.
name|METRIC_VALUE
argument_list|,
name|Double
operator|.
name|valueOf
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CwConstants
operator|.
name|METRIC_UNIT
argument_list|,
name|StandardUnit
operator|.
name|Bytes
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CwConstants
operator|.
name|METRIC_TIMESTAMP
argument_list|,
name|LATER
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|PutMetricDataRequest
argument_list|>
name|argument
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|PutMetricDataRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|cloudWatchClient
argument_list|)
operator|.
name|putMetricData
argument_list|(
name|argument
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"camel.apache.org/overriden"
argument_list|,
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"OverridenMetric"
argument_list|,
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|getMetricData
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMetricName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|3
argument_list|)
argument_list|,
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|getMetricData
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|StandardUnit
operator|.
name|Bytes
operator|.
name|toString
argument_list|()
argument_list|,
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|getMetricData
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUnit
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LATER
argument_list|,
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|getMetricData
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendManuallyCreatedMetric ()
specifier|public
name|void
name|sendManuallyCreatedMetric
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|MetricDatum
name|metricDatum
init|=
operator|new
name|MetricDatum
argument_list|()
operator|.
name|withMetricName
argument_list|(
literal|"errorCount"
argument_list|)
operator|.
name|withValue
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|metricDatum
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|PutMetricDataRequest
argument_list|>
name|argument
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|PutMetricDataRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|cloudWatchClient
argument_list|)
operator|.
name|putMetricData
argument_list|(
name|argument
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"errorCount"
argument_list|,
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|getMetricData
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMetricName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
argument_list|,
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|getMetricData
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|useDefaultValuesForMetricUnitAndMetricValue ()
specifier|public
name|void
name|useDefaultValuesForMetricUnitAndMetricValue
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CwConstants
operator|.
name|METRIC_NAME
argument_list|,
literal|"errorCount"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|PutMetricDataRequest
argument_list|>
name|argument
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|PutMetricDataRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|cloudWatchClient
argument_list|)
operator|.
name|putMetricData
argument_list|(
name|argument
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"errorCount"
argument_list|,
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|getMetricData
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMetricName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|,
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|getMetricData
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|StandardUnit
operator|.
name|Count
operator|.
name|toString
argument_list|()
argument_list|,
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|getMetricData
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUnit
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setsMeticDimensions ()
specifier|public
name|void
name|setsMeticDimensions
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CwConstants
operator|.
name|METRIC_NAME
argument_list|,
literal|"errorCount"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dimensionsMap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|dimensionsMap
operator|.
name|put
argument_list|(
literal|"keyOne"
argument_list|,
literal|"valueOne"
argument_list|)
expr_stmt|;
name|dimensionsMap
operator|.
name|put
argument_list|(
literal|"keyTwo"
argument_list|,
literal|"valueTwo"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CwConstants
operator|.
name|METRIC_DIMENSIONS
argument_list|,
name|dimensionsMap
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|PutMetricDataRequest
argument_list|>
name|argument
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|PutMetricDataRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|cloudWatchClient
argument_list|)
operator|.
name|putMetricData
argument_list|(
name|argument
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Dimension
argument_list|>
name|dimensions
init|=
name|argument
operator|.
name|getValue
argument_list|()
operator|.
name|getMetricData
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getDimensions
argument_list|()
decl_stmt|;
name|Dimension
name|dimension
init|=
name|dimensions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|dimensions
operator|.
name|size
argument_list|()
argument_list|,
name|is
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"keyOne"
argument_list|,
name|dimension
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"valueOne"
argument_list|,
name|dimension
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-cw://camel.apache.org/test?amazonCwClient=#amazonCwClient&name=testMetric&unit=Count&timestamp=#now"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

