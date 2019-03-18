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
name|Message
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
name|metrics
operator|.
name|GaugeProducer
operator|.
name|CamelMetricsGauge
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
name|ArgumentMatcher
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|metrics
operator|.
name|MetricsConstants
operator|.
name|HEADER_GAUGE_SUBJECT
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
name|junit
operator|.
name|Assert
operator|.
name|assertThat
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
name|argThat
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
name|eq
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
name|times
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

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
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
DECL|class|GaugeProducerTest
specifier|public
class|class
name|GaugeProducerTest
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
DECL|field|METRICS_NAME_HEADER
specifier|private
specifier|static
specifier|final
name|String
name|METRICS_NAME_HEADER
init|=
literal|"metrics.name.header"
decl_stmt|;
DECL|field|VALUE
specifier|private
specifier|static
specifier|final
name|Object
name|VALUE
init|=
literal|"my subject"
decl_stmt|;
DECL|field|VALUE_HEADER
specifier|private
specifier|static
specifier|final
name|Object
name|VALUE_HEADER
init|=
literal|"my subject header"
decl_stmt|;
annotation|@
name|Mock
DECL|field|endpoint
specifier|private
name|MetricsEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Mock
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
annotation|@
name|Mock
DECL|field|registry
specifier|private
name|MetricRegistry
name|registry
decl_stmt|;
annotation|@
name|Mock
DECL|field|in
specifier|private
name|Message
name|in
decl_stmt|;
DECL|field|producer
specifier|private
name|GaugeProducer
name|producer
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
name|when
argument_list|(
name|endpoint
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getSubject
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|VALUE
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getMetricsName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|producer
operator|=
operator|new
name|GaugeProducer
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGaugeProducer ()
specifier|public
name|void
name|testGaugeProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|producer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|equals
argument_list|(
name|endpoint
argument_list|)
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefault ()
specifier|public
name|void
name|testDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|verify
argument_list|(
name|registry
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|register
argument_list|(
name|eq
argument_list|(
name|METRICS_NAME
argument_list|)
argument_list|,
name|argThat
argument_list|(
operator|new
name|ArgumentMatcher
argument_list|<
name|CamelMetricsGauge
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|CamelMetricsGauge
name|argument
parameter_list|)
block|{
return|return
name|VALUE
operator|.
name|equals
argument_list|(
name|argument
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProcessWithHeaderValues ()
specifier|public
name|void
name|testProcessWithHeaderValues
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|HEADER_GAUGE_SUBJECT
argument_list|,
name|Object
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|VALUE_HEADER
argument_list|)
expr_stmt|;
name|producer
operator|.
name|doProcess
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|,
name|registry
argument_list|,
name|METRICS_NAME_HEADER
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|in
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getHeader
argument_list|(
name|HEADER_GAUGE_SUBJECT
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|registry
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|register
argument_list|(
name|eq
argument_list|(
name|METRICS_NAME
argument_list|)
argument_list|,
name|argThat
argument_list|(
operator|new
name|ArgumentMatcher
argument_list|<
name|CamelMetricsGauge
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|CamelMetricsGauge
name|argument
parameter_list|)
block|{
return|return
name|VALUE
operator|.
name|equals
argument_list|(
name|argument
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|registry
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|register
argument_list|(
name|eq
argument_list|(
name|METRICS_NAME_HEADER
argument_list|)
argument_list|,
name|argThat
argument_list|(
operator|new
name|ArgumentMatcher
argument_list|<
name|CamelMetricsGauge
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|CamelMetricsGauge
name|argument
parameter_list|)
block|{
return|return
name|VALUE_HEADER
operator|.
name|equals
argument_list|(
name|argument
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

