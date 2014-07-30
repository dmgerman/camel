begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.metrics.meter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|metrics
operator|.
name|meter
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
name|Producer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|InOrder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|runners
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|instanceOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|notNullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|nullValue
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|MeterEndpointTest
specifier|public
class|class
name|MeterEndpointTest
block|{
DECL|field|METRICS_NAME
specifier|private
specifier|static
specifier|final
name|String
name|METRICS_NAME
init|=
literal|"metrics.name"
decl_stmt|;
DECL|field|VALUE
specifier|private
specifier|static
specifier|final
name|Long
name|VALUE
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
annotation|@
name|Mock
DECL|field|registry
specifier|private
name|MetricRegistry
name|registry
decl_stmt|;
DECL|field|endpoint
specifier|private
name|MeterEndpoint
name|endpoint
decl_stmt|;
DECL|field|inOrder
specifier|private
name|InOrder
name|inOrder
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|=
operator|new
name|MeterEndpoint
argument_list|(
name|registry
argument_list|,
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|=
name|Mockito
operator|.
name|inOrder
argument_list|(
name|registry
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMeterEndpoint ()
specifier|public
name|void
name|testMeterEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|endpoint
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getRegistry
argument_list|()
argument_list|,
name|is
argument_list|(
name|registry
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getMetricsName
argument_list|()
argument_list|,
name|is
argument_list|(
name|METRICS_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateProducer ()
specifier|public
name|void
name|testCreateProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|producer
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|producer
argument_list|,
name|is
argument_list|(
name|instanceOf
argument_list|(
name|MeterProducer
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetMark ()
specifier|public
name|void
name|testGetMark
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getMark
argument_list|()
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetMark ()
specifier|public
name|void
name|testSetMark
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getMark
argument_list|()
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setMark
argument_list|(
name|VALUE
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getMark
argument_list|()
argument_list|,
name|is
argument_list|(
name|VALUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateEndpointUri ()
specifier|public
name|void
name|testCreateEndpointUri
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|endpoint
operator|.
name|createEndpointUri
argument_list|()
argument_list|,
name|is
argument_list|(
name|MeterEndpoint
operator|.
name|ENDPOINT_URI
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

