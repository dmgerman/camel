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
name|RuntimeCamelException
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

begin_comment
comment|/**  * Unit test to verify that redelivery counters is working as expected.  */
end_comment

begin_class
DECL|class|DeadLetterChannelRedeliveryTest
specifier|public
class|class
name|DeadLetterChannelRedeliveryTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|counter
specifier|private
specifier|static
name|int
name|counter
decl_stmt|;
DECL|method|testRedeliveryTest ()
specifier|public
name|void
name|testRedeliveryTest
parameter_list|()
throws|throws
name|Exception
block|{
name|counter
operator|=
literal|0
expr_stmt|;
comment|// We expect the exchange here after 1 delivery and 2 re-deliveries
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"CamelRedelivered"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"CamelRedeliveryCounter"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|counter
argument_list|)
expr_stmt|;
comment|// One call + 2 re-deliveries
block|}
DECL|method|testNoRedeliveriesTest ()
specifier|public
name|void
name|testNoRedeliveriesTest
parameter_list|()
throws|throws
name|Exception
block|{
name|counter
operator|=
literal|0
expr_stmt|;
comment|// We expect the exchange here after 1 delivery
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:no"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"CamelRedelivered"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"CamelRedeliveryCounter"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:no"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|counter
argument_list|)
expr_stmt|;
comment|// One call
block|}
DECL|method|testOneRedeliveryTest ()
specifier|public
name|void
name|testOneRedeliveryTest
parameter_list|()
throws|throws
name|Exception
block|{
name|counter
operator|=
literal|0
expr_stmt|;
comment|// We expect the exchange here after 1 delivery and 1 re delivery
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:one"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"CamelRedelivered"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"CamelRedeliveryCounter"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:one"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|counter
argument_list|)
expr_stmt|;
comment|// One call + 1 re-delivery
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we use handled(false) to instruct DLC to not handle the exception and therefore
comment|// we can assert the number of redeliver attempts to see if that works correct
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|handled
argument_list|(
literal|false
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
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
throws|throws
name|Exception
block|{
name|counter
operator|++
expr_stmt|;
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Forced exception by unit test"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:no"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:no"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|handled
argument_list|(
literal|false
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
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
throws|throws
name|Exception
block|{
name|counter
operator|++
expr_stmt|;
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Forced exception by unit test"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:one"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:one"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|1
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|handled
argument_list|(
literal|false
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
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
throws|throws
name|Exception
block|{
name|counter
operator|++
expr_stmt|;
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Forced exception by unit test"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

