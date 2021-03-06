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
name|io
operator|.
name|smallrye
operator|.
name|metrics
operator|.
name|MetricRegistries
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
name|DefaultCamelContext
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
name|Registry
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
name|MetricRegistry
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
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|MicroProfileMetricsHelperTest
specifier|public
class|class
name|MicroProfileMetricsHelperTest
block|{
annotation|@
name|Test
DECL|method|testParseTag ()
specifier|public
name|void
name|testParseTag
parameter_list|()
block|{
name|Tag
name|tag
init|=
name|MicroProfileMetricsHelper
operator|.
name|parseTag
argument_list|(
literal|"foo=bar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|tag
operator|.
name|getTagName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|tag
operator|.
name|getTagValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|testParseTagForEmptyString ()
specifier|public
name|void
name|testParseTagForEmptyString
parameter_list|()
block|{
name|MicroProfileMetricsHelper
operator|.
name|parseTag
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|testParseTagForInvalidString ()
specifier|public
name|void
name|testParseTagForInvalidString
parameter_list|()
block|{
name|MicroProfileMetricsHelper
operator|.
name|parseTag
argument_list|(
literal|"badtag"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|testParseTagForStringWithNotEnoughElements ()
specifier|public
name|void
name|testParseTagForStringWithNotEnoughElements
parameter_list|()
block|{
name|MicroProfileMetricsHelper
operator|.
name|parseTag
argument_list|(
literal|"badtag="
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParseTags ()
specifier|public
name|void
name|testParseTags
parameter_list|()
block|{
name|Tag
index|[]
name|tags
init|=
name|MicroProfileMetricsHelper
operator|.
name|parseTagArray
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"foo=bar"
block|,
literal|"cheese=wine"
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|tags
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|tags
index|[
literal|0
index|]
operator|.
name|getTagName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|tags
index|[
literal|0
index|]
operator|.
name|getTagValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cheese"
argument_list|,
name|tags
index|[
literal|1
index|]
operator|.
name|getTagName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"wine"
argument_list|,
name|tags
index|[
literal|1
index|]
operator|.
name|getTagValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetMetricRegistry ()
specifier|public
name|void
name|testGetMetricRegistry
parameter_list|()
block|{
name|DefaultCamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|Registry
name|registry
init|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
name|MicroProfileMetricsConstants
operator|.
name|METRIC_REGISTRY_NAME
argument_list|,
name|MetricRegistries
operator|.
name|get
argument_list|(
name|MetricRegistry
operator|.
name|Type
operator|.
name|APPLICATION
argument_list|)
argument_list|)
expr_stmt|;
name|MicroProfileMetricsHelper
operator|.
name|getMetricRegistry
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalStateException
operator|.
name|class
argument_list|)
DECL|method|testGetMetricRegistryWhenNoRegistryConfigured ()
specifier|public
name|void
name|testGetMetricRegistryWhenNoRegistryConfigured
parameter_list|()
block|{
name|MicroProfileMetricsHelper
operator|.
name|getMetricRegistry
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

