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
name|java
operator|.
name|util
operator|.
name|List
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

begin_class
DECL|class|RedeliveryPolicyPerExceptionTest
specifier|public
class|class
name|RedeliveryPolicyPerExceptionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|a
specifier|protected
name|MockEndpoint
name|a
decl_stmt|;
DECL|field|b
specifier|protected
name|MockEndpoint
name|b
decl_stmt|;
annotation|@
name|Test
DECL|method|testUsingCustomExceptionHandlerAndOneRedelivery ()
specifier|public
name|void
name|testUsingCustomExceptionHandlerAndOneRedelivery
parameter_list|()
throws|throws
name|Exception
block|{
name|a
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|a
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"List should not be empty!"
argument_list|,
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Found message with headers: "
operator|+
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertMessageHeader
argument_list|(
name|in
argument_list|,
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertMessageHeader
argument_list|(
name|in
argument_list|,
name|Exchange
operator|.
name|REDELIVERY_MAX_COUNTER
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertMessageHeader
argument_list|(
name|in
argument_list|,
name|Exchange
operator|.
name|REDELIVERED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUsingCustomExceptionHandlerWithNoRedeliveries ()
specifier|public
name|void
name|testUsingCustomExceptionHandlerWithNoRedeliveries
parameter_list|()
throws|throws
name|Exception
block|{
name|b
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|b
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"List should not be empty!"
argument_list|,
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Found message with headers: "
operator|+
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertMessageHeader
argument_list|(
name|in
argument_list|,
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertMessageHeader
argument_list|(
name|in
argument_list|,
name|Exchange
operator|.
name|REDELIVERY_MAX_COUNTER
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertMessageHeader
argument_list|(
name|in
argument_list|,
name|Exchange
operator|.
name|REDELIVERED
argument_list|,
literal|false
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
name|a
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:a"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|b
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:b"
argument_list|,
name|MockEndpoint
operator|.
name|class
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
if|if
condition|(
literal|"b"
operator|.
name|equals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"MyCustomException"
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"MyCustomException"
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|NullPointerException
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit
