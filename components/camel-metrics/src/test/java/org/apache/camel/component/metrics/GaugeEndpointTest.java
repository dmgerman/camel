begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Producer
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
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
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
name|is
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
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
DECL|class|GaugeEndpointTest
specifier|public
class|class
name|GaugeEndpointTest
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
name|Object
name|VALUE
init|=
literal|"subject"
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
name|MetricsEndpoint
name|endpoint
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
name|MetricsEndpoint
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|registry
argument_list|,
name|MetricsType
operator|.
name|GAUGE
argument_list|,
name|METRICS_NAME
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGaugeEndpoint ()
specifier|public
name|void
name|testGaugeEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
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
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getSubject
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
name|GaugeProducer
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetSubject ()
specifier|public
name|void
name|testGetSubject
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getSubject
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
DECL|method|testSetSubject ()
specifier|public
name|void
name|testSetSubject
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|setSubject
argument_list|(
name|VALUE
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|endpoint
operator|.
name|getSubject
argument_list|()
argument_list|,
name|is
argument_list|(
name|VALUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

