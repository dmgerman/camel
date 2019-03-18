begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.beanstalk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|beanstalk
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
name|com
operator|.
name|surftools
operator|.
name|BeanstalkClient
operator|.
name|Job
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
name|EndpointInject
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|anyInt
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|anyLong
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
name|atLeastOnce
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
DECL|class|ConsumerToProducerHeadersTest
specifier|public
class|class
name|ConsumerToProducerHeadersTest
extends|extends
name|BeanstalkMockTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"beanstalk:tube=A"
argument_list|)
DECL|field|endpoint
specifier|protected
name|BeanstalkEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
DECL|field|testMessage
specifier|private
name|String
name|testMessage
init|=
literal|"hello, world"
decl_stmt|;
DECL|field|a
specifier|private
name|Processor
name|a
decl_stmt|;
DECL|field|b
specifier|private
name|Processor
name|b
decl_stmt|;
annotation|@
name|Test
DECL|method|testBeanstalkConsumerToProducer ()
specifier|public
name|void
name|testBeanstalkConsumerToProducer
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|long
name|jobId
init|=
literal|111
decl_stmt|;
specifier|final
name|byte
index|[]
name|payload
init|=
name|Helper
operator|.
name|stringToBytes
argument_list|(
name|testMessage
argument_list|)
decl_stmt|;
specifier|final
name|Job
name|jobMock
init|=
name|mock
argument_list|(
name|Job
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// stats that may be set in the consumer:
comment|// mock stats : "tube", "state", "age", "time-left", "timeouts", "releases", "buries", "kicks"
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|stats
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|stats
operator|.
name|put
argument_list|(
literal|"tube"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|stats
operator|.
name|put
argument_list|(
literal|"state"
argument_list|,
literal|"Test"
argument_list|)
expr_stmt|;
name|stats
operator|.
name|put
argument_list|(
literal|"age"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|stats
operator|.
name|put
argument_list|(
literal|"time-left"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|stats
operator|.
name|put
argument_list|(
literal|"timeouts"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|stats
operator|.
name|put
argument_list|(
literal|"releases"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|stats
operator|.
name|put
argument_list|(
literal|"buries"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|stats
operator|.
name|put
argument_list|(
literal|"kicks"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|jobMock
operator|.
name|getJobId
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|jobId
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|jobMock
operator|.
name|getData
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|client
operator|.
name|reserve
argument_list|(
name|anyInt
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|jobMock
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|client
operator|.
name|statsJob
argument_list|(
name|anyLong
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|stats
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|client
operator|.
name|put
argument_list|(
name|BeanstalkComponent
operator|.
name|DEFAULT_PRIORITY
argument_list|,
name|BeanstalkComponent
operator|.
name|DEFAULT_DELAY
argument_list|,
name|BeanstalkComponent
operator|.
name|DEFAULT_TIME_TO_RUN
argument_list|,
name|payload
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|jobId
argument_list|)
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedHeaderReceived
argument_list|(
name|Headers
operator|.
name|JOB_ID
argument_list|,
name|jobId
argument_list|)
expr_stmt|;
name|result
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Headers
operator|.
name|JOB_ID
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|jobId
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|client
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|reserve
argument_list|(
name|anyInt
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|client
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|statsJob
argument_list|(
name|anyLong
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
operator|(
name|TestExchangeCopyProcessor
operator|)
name|a
operator|)
operator|.
name|getExchangeCopy
argument_list|()
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|,
operator|(
operator|(
name|TestExchangeCopyProcessor
operator|)
name|b
operator|)
operator|.
name|getExchangeCopy
argument_list|()
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
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
block|{
name|a
operator|=
operator|new
name|TestExchangeCopyProcessor
argument_list|()
expr_stmt|;
name|b
operator|=
operator|new
name|TestExchangeCopyProcessor
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"beanstalk:tube=A"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|process
argument_list|(
name|a
argument_list|)
operator|.
name|to
argument_list|(
literal|"beanstalk:tube=B"
argument_list|)
operator|.
name|process
argument_list|(
name|b
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

