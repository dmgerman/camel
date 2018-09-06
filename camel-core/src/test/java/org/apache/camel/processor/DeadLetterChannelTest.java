begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ContextTestSupport
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
name|LoggingLevel
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DeadLetterChannelTest
specifier|public
class|class
name|DeadLetterChannelTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|startEndpoint
specifier|protected
name|Endpoint
name|startEndpoint
decl_stmt|;
DECL|field|deadEndpoint
specifier|protected
name|MockEndpoint
name|deadEndpoint
decl_stmt|;
DECL|field|successEndpoint
specifier|protected
name|MockEndpoint
name|successEndpoint
decl_stmt|;
DECL|field|failUntilAttempt
specifier|protected
name|int
name|failUntilAttempt
init|=
literal|2
decl_stmt|;
DECL|field|body
specifier|protected
name|String
name|body
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
annotation|@
name|Test
DECL|method|testFirstFewAttemptsFail ()
specifier|public
name|void
name|testFirstFewAttemptsFail
parameter_list|()
throws|throws
name|Exception
block|{
name|successEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|successEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|REDELIVERED
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|successEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|successEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_MAX_COUNTER
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|deadEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLotsOfAttemptsFail ()
specifier|public
name|void
name|testLotsOfAttemptsFail
parameter_list|()
throws|throws
name|Exception
block|{
name|failUntilAttempt
operator|=
literal|5
expr_stmt|;
name|deadEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
comment|// no traces of redelivery as the dead letter channel will handle the exception when moving the DLQ
name|deadEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|REDELIVERED
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|deadEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|deadEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_MAX_COUNTER
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|successEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Throwable
name|t
init|=
name|deadEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have been a cause property"
argument_list|,
name|t
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|t
operator|instanceof
name|RuntimeException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Failed to process due to attempt: 3 being less than: 5"
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// must be InOnly
name|Exchange
name|dead
init|=
name|deadEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
name|dead
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLotsOfAttemptsFailInOut ()
specifier|public
name|void
name|testLotsOfAttemptsFailInOut
parameter_list|()
throws|throws
name|Exception
block|{
name|failUntilAttempt
operator|=
literal|5
expr_stmt|;
name|deadEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
comment|// no traces of redelivery as the dead letter channel will handle the exception when moving the DLQ
name|deadEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|REDELIVERED
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|deadEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|deadEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_MAX_COUNTER
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|successEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Throwable
name|t
init|=
name|deadEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have been a cause property"
argument_list|,
name|t
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|t
operator|instanceof
name|RuntimeException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Failed to process due to attempt: 3 being less than: 5"
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// must be InOnly
name|Exchange
name|dead
init|=
name|deadEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
name|dead
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|deadEndpoint
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:failed"
argument_list|)
expr_stmt|;
name|successEndpoint
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:success"
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
specifier|final
name|Processor
name|processor
init|=
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
block|{
name|Integer
name|counter
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|attempt
init|=
operator|(
name|counter
operator|==
literal|null
operator|)
condition|?
literal|1
else|:
name|counter
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|attempt
operator|<
name|failUntilAttempt
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to process due to attempt: "
operator|+
name|attempt
operator|+
literal|" being less than: "
operator|+
name|failUntilAttempt
argument_list|)
throw|;
block|}
block|}
block|}
decl_stmt|;
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:failed"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|50
argument_list|)
operator|.
name|loggingLevel
argument_list|(
name|LoggingLevel
operator|.
name|DEBUG
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|processor
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:success"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

