begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.microprofile.metrics
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|Metadata
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
name|HEADER_METRIC_DESCRIPTION
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
name|HEADER_METRIC_DISPLAY_NAME
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
name|HEADER_METRIC_UNIT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MetricUnits
operator|.
name|KILOBYTES
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MetricUnits
operator|.
name|MEGABITS
import|;
end_import

begin_class
DECL|class|MicroProfileMetricsMetadataTest
specifier|public
class|class
name|MicroProfileMetricsMetadataTest
extends|extends
name|MicroProfileMetricsTestSupport
block|{
DECL|field|METRIC_DISPLAY_NAME
specifier|private
specifier|static
specifier|final
name|String
name|METRIC_DISPLAY_NAME
init|=
literal|"Test Display Name"
decl_stmt|;
DECL|field|METRIC_DISPLAY_NAME_MODIFIED
specifier|private
specifier|static
specifier|final
name|String
name|METRIC_DISPLAY_NAME_MODIFIED
init|=
name|METRIC_DISPLAY_NAME
operator|+
literal|" Modified"
decl_stmt|;
DECL|field|METRIC_DESCRIPTION
specifier|private
specifier|static
specifier|final
name|String
name|METRIC_DESCRIPTION
init|=
literal|"Test Description"
decl_stmt|;
DECL|field|METRIC_DESCRIPTION_MODIFIED
specifier|private
specifier|static
specifier|final
name|String
name|METRIC_DESCRIPTION_MODIFIED
init|=
name|METRIC_DESCRIPTION
operator|+
literal|" Modified"
decl_stmt|;
annotation|@
name|Test
DECL|method|testMetricMetadata ()
specifier|public
name|void
name|testMetricMetadata
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:metadata"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Metadata
name|metadata
init|=
name|getMetricMetadata
argument_list|(
literal|"test-counter"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|METRIC_DESCRIPTION
argument_list|,
name|metadata
operator|.
name|getDescription
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|METRIC_DISPLAY_NAME
argument_list|,
name|metadata
operator|.
name|getDisplayName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|KILOBYTES
argument_list|,
name|metadata
operator|.
name|getUnit
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMetricMetadataFromHeader ()
specifier|public
name|void
name|testMetricMetadataFromHeader
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:metadataHeader"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Metadata
name|metadata
init|=
name|getMetricMetadata
argument_list|(
literal|"test-counter-header"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|METRIC_DESCRIPTION
argument_list|,
name|metadata
operator|.
name|getDescription
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|METRIC_DISPLAY_NAME
argument_list|,
name|metadata
operator|.
name|getDisplayName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|KILOBYTES
argument_list|,
name|metadata
operator|.
name|getUnit
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMetricMetadataFromHeadersOverride ()
specifier|public
name|void
name|testMetricMetadataFromHeadersOverride
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HEADER_METRIC_DESCRIPTION
argument_list|,
name|METRIC_DISPLAY_NAME_MODIFIED
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HEADER_METRIC_DISPLAY_NAME
argument_list|,
name|METRIC_DESCRIPTION_MODIFIED
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HEADER_METRIC_UNIT
argument_list|,
name|MEGABITS
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:metadata"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|Metadata
name|metadata
init|=
name|getMetricMetadata
argument_list|(
literal|"test-counter"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|METRIC_DISPLAY_NAME_MODIFIED
argument_list|,
name|metadata
operator|.
name|getDescription
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|METRIC_DESCRIPTION_MODIFIED
argument_list|,
name|metadata
operator|.
name|getDisplayName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MEGABITS
argument_list|,
name|metadata
operator|.
name|getUnit
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct:metadata"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"microprofile-metrics:counter:test-counter?description=%s&displayName=%s&metricUnit=%s"
argument_list|,
name|METRIC_DESCRIPTION
argument_list|,
name|METRIC_DISPLAY_NAME
argument_list|,
name|KILOBYTES
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:metadataHeader"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HEADER_METRIC_DESCRIPTION
argument_list|,
name|constant
argument_list|(
name|METRIC_DESCRIPTION
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HEADER_METRIC_DISPLAY_NAME
argument_list|,
name|constant
argument_list|(
name|METRIC_DISPLAY_NAME
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HEADER_METRIC_UNIT
argument_list|,
name|constant
argument_list|(
name|KILOBYTES
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"microprofile-metrics:counter:test-counter-header"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

