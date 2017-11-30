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
name|Counter
import|;
end_import

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
name|HEADER_COUNTER_DECREMENT
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
name|HEADER_COUNTER_INCREMENT
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
DECL|class|CounterProducerTest
specifier|public
class|class
name|CounterProducerTest
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
DECL|field|INCREMENT
specifier|private
specifier|static
specifier|final
name|Long
name|INCREMENT
init|=
literal|100000L
decl_stmt|;
DECL|field|DECREMENT
specifier|private
specifier|static
specifier|final
name|Long
name|DECREMENT
init|=
literal|91929199L
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
DECL|field|counter
specifier|private
name|Counter
name|counter
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
name|CounterProducer
name|producer
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
name|producer
operator|=
operator|new
name|CounterProducer
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|inOrder
operator|=
name|Mockito
operator|.
name|inOrder
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|registry
argument_list|,
name|counter
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|registry
operator|.
name|counter
argument_list|(
name|METRICS_NAME
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|counter
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
block|}
annotation|@
name|Test
DECL|method|testCounterProducer ()
specifier|public
name|void
name|testCounterProducer
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
DECL|method|testProcessWithIncrementOnly ()
specifier|public
name|void
name|testProcessWithIncrementOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|action
init|=
literal|null
decl_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getIncrement
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|INCREMENT
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getDecrement
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|HEADER_COUNTER_INCREMENT
argument_list|,
name|INCREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|INCREMENT
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
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|inOrder
operator|.
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
name|counter
argument_list|(
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIncrement
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getDecrement
argument_list|()
expr_stmt|;
name|inOrder
operator|.
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
name|HEADER_COUNTER_INCREMENT
argument_list|,
name|INCREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
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
name|HEADER_COUNTER_DECREMENT
argument_list|,
name|action
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|counter
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|inc
argument_list|(
name|INCREMENT
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProcessWithDecrementOnly ()
specifier|public
name|void
name|testProcessWithDecrementOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|action
init|=
literal|null
decl_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getIncrement
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getDecrement
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|DECREMENT
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|HEADER_COUNTER_DECREMENT
argument_list|,
name|DECREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|DECREMENT
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
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|inOrder
operator|.
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
name|counter
argument_list|(
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIncrement
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getDecrement
argument_list|()
expr_stmt|;
name|inOrder
operator|.
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
name|HEADER_COUNTER_INCREMENT
argument_list|,
name|action
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
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
name|HEADER_COUNTER_DECREMENT
argument_list|,
name|DECREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|counter
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|dec
argument_list|(
name|DECREMENT
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDoProcessWithIncrementAndDecrement ()
specifier|public
name|void
name|testDoProcessWithIncrementAndDecrement
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|endpoint
operator|.
name|getIncrement
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|INCREMENT
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getDecrement
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|DECREMENT
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|HEADER_COUNTER_INCREMENT
argument_list|,
name|INCREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|INCREMENT
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|HEADER_COUNTER_DECREMENT
argument_list|,
name|DECREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|DECREMENT
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
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|inOrder
operator|.
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
name|counter
argument_list|(
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIncrement
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getDecrement
argument_list|()
expr_stmt|;
name|inOrder
operator|.
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
name|HEADER_COUNTER_INCREMENT
argument_list|,
name|INCREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
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
name|HEADER_COUNTER_DECREMENT
argument_list|,
name|DECREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|counter
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|inc
argument_list|(
name|INCREMENT
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProcessWithOutIncrementAndDecrement ()
specifier|public
name|void
name|testProcessWithOutIncrementAndDecrement
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|action
init|=
literal|null
decl_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getIncrement
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getDecrement
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
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
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|inOrder
operator|.
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
name|counter
argument_list|(
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIncrement
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getDecrement
argument_list|()
expr_stmt|;
name|inOrder
operator|.
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
name|HEADER_COUNTER_INCREMENT
argument_list|,
name|action
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
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
name|HEADER_COUNTER_DECREMENT
argument_list|,
name|action
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|counter
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|inc
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProcessOverridingIncrement ()
specifier|public
name|void
name|testProcessOverridingIncrement
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|endpoint
operator|.
name|getIncrement
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|INCREMENT
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getDecrement
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|DECREMENT
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|HEADER_COUNTER_INCREMENT
argument_list|,
name|INCREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|INCREMENT
operator|+
literal|1
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|HEADER_COUNTER_DECREMENT
argument_list|,
name|DECREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|DECREMENT
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
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|inOrder
operator|.
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
name|counter
argument_list|(
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIncrement
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getDecrement
argument_list|()
expr_stmt|;
name|inOrder
operator|.
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
name|HEADER_COUNTER_INCREMENT
argument_list|,
name|INCREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
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
name|HEADER_COUNTER_DECREMENT
argument_list|,
name|DECREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|counter
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|inc
argument_list|(
name|INCREMENT
operator|+
literal|1
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProcessOverridingDecrement ()
specifier|public
name|void
name|testProcessOverridingDecrement
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|action
init|=
literal|null
decl_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getIncrement
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getDecrement
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|DECREMENT
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|HEADER_COUNTER_DECREMENT
argument_list|,
name|DECREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|DECREMENT
operator|-
literal|1
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
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|inOrder
operator|.
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
name|counter
argument_list|(
name|METRICS_NAME
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIncrement
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getDecrement
argument_list|()
expr_stmt|;
name|inOrder
operator|.
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
name|HEADER_COUNTER_INCREMENT
argument_list|,
name|action
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
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
name|HEADER_COUNTER_DECREMENT
argument_list|,
name|DECREMENT
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|counter
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|dec
argument_list|(
name|DECREMENT
operator|-
literal|1
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

