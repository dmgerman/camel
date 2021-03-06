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
name|MeterRegistry
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
name|Tags
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
name|Producer
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
name|RuntimeCamelException
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|MicrometerEndpointTest
specifier|public
class|class
name|MicrometerEndpointTest
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
annotation|@
name|Mock
DECL|field|registry
specifier|private
name|MeterRegistry
name|registry
decl_stmt|;
annotation|@
name|Mock
DECL|field|processor
specifier|private
name|Processor
name|processor
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
DECL|field|in
specifier|private
name|Message
name|in
decl_stmt|;
DECL|field|endpoint
specifier|private
name|MicrometerEndpoint
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
block|{
name|endpoint
operator|=
operator|new
name|MicrometerEndpoint
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|registry
argument_list|,
name|Meter
operator|.
name|Type
operator|.
name|COUNTER
argument_list|,
name|METRICS_NAME
argument_list|,
name|Tags
operator|.
name|empty
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Producer
name|createProducer
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"not real endpoint"
return|;
block|}
block|}
expr_stmt|;
name|inOrder
operator|=
name|Mockito
operator|.
name|inOrder
argument_list|(
name|registry
argument_list|,
name|processor
argument_list|,
name|exchange
argument_list|,
name|in
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
block|{
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAbstractMetricsEndpoint ()
specifier|public
name|void
name|testAbstractMetricsEndpoint
parameter_list|()
block|{
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
name|getRegistry
argument_list|()
argument_list|,
name|is
argument_list|(
name|registry
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RuntimeCamelException
operator|.
name|class
argument_list|)
DECL|method|testCreateConsumer ()
specifier|public
name|void
name|testCreateConsumer
parameter_list|()
block|{
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIsSingleton ()
specifier|public
name|void
name|testIsSingleton
parameter_list|()
block|{
name|assertThat
argument_list|(
name|endpoint
operator|.
name|isSingleton
argument_list|()
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
DECL|method|testGetRegistry ()
specifier|public
name|void
name|testGetRegistry
parameter_list|()
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
block|}
annotation|@
name|Test
DECL|method|testGetMetricsName ()
specifier|public
name|void
name|testGetMetricsName
parameter_list|()
block|{
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
block|}
end_class

end_unit

