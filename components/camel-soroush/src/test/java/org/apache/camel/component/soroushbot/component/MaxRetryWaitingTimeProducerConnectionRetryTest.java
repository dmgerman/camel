begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.soroushbot.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|soroushbot
operator|.
name|component
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|soroushbot
operator|.
name|models
operator|.
name|Endpoint
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
name|soroushbot
operator|.
name|models
operator|.
name|MinorType
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
name|soroushbot
operator|.
name|models
operator|.
name|SoroushMessage
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
name|soroushbot
operator|.
name|support
operator|.
name|SoroushBotTestSupport
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
name|soroushbot
operator|.
name|support
operator|.
name|SoroushBotWS
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_class
DECL|class|MaxRetryWaitingTimeProducerConnectionRetryTest
specifier|public
class|class
name|MaxRetryWaitingTimeProducerConnectionRetryTest
extends|extends
name|SoroushBotTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"direct:soroush"
argument_list|)
DECL|field|endpoint
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
name|endpoint
decl_stmt|;
annotation|@
name|Override
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|SoroushBotWS
operator|.
name|clear
argument_list|()
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
literal|"direct:soroush"
argument_list|)
operator|.
name|to
argument_list|(
literal|"soroush://"
operator|+
name|Endpoint
operator|.
name|sendMessage
operator|+
literal|"/retry 5?maxConnectionRetry=5"
operator|+
literal|"&retryWaitingTime=500&retryExponentialCoefficient=2&backOffStrategy=exponential&maxRetryWaitingTime=2500"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:afterAllRetry"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:beforeAllRetry"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|retryOnFailure ()
specifier|public
name|void
name|retryOnFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|SoroushMessage
name|body
init|=
operator|new
name|SoroushMessage
argument_list|()
decl_stmt|;
name|body
operator|.
name|setType
argument_list|(
name|MinorType
operator|.
name|TEXT
argument_list|)
expr_stmt|;
name|body
operator|.
name|setFrom
argument_list|(
literal|"b1"
argument_list|)
expr_stmt|;
name|body
operator|.
name|setTo
argument_list|(
literal|"u1"
argument_list|)
expr_stmt|;
comment|//send message in other thread
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
name|context
argument_list|()
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
name|endpoint
argument_list|,
name|body
argument_list|)
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|beforeAllRetry
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:beforeAllRetry"
argument_list|)
decl_stmt|;
comment|//cause this thread to sleep .5+1+2+2.5 second. even in this time,no message should be sent
name|beforeAllRetry
operator|.
name|setAssertPeriod
argument_list|(
literal|5500
argument_list|)
expr_stmt|;
name|beforeAllRetry
operator|.
name|setExpectedCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|beforeAllRetry
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|MockEndpoint
name|afterAllRetry
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:afterAllRetry"
argument_list|)
decl_stmt|;
name|afterAllRetry
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|//cause this thread to sleep an addition of 1 second, during this time,the message must be sent to the server
name|afterAllRetry
operator|.
name|setAssertPeriod
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|afterAllRetry
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"message sent successfully"
argument_list|,
name|SoroushBotWS
operator|.
name|getReceivedMessages
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

