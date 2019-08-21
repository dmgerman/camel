begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Map
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
name|Body
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
name|Headers
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

begin_comment
comment|/**  * Unit test to verify that handled policy is working as expected for wiki  * documentation.  */
end_comment

begin_class
DECL|class|DeadLetterChannelHandledExampleTest
specifier|public
class|class
name|DeadLetterChannelHandledExampleTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testOrderOK ()
specifier|public
name|void
name|testOrderOK
parameter_list|()
throws|throws
name|Exception
block|{
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
name|expectedBodiesReceived
argument_list|(
literal|"Order OK"
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"orderid"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|error
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|error
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Object
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Order: MacBook Pro"
argument_list|,
literal|"customerid"
argument_list|,
literal|"444"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Order OK"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOrderERROR ()
specifier|public
name|void
name|testOrderERROR
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|error
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|error
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Order ERROR"
argument_list|)
expr_stmt|;
name|error
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"orderid"
argument_list|,
literal|"failed"
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
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Object
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Order: kaboom"
argument_list|,
literal|"customerid"
argument_list|,
literal|"555"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Order ERROR"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
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
comment|// START SNIPPET: e1
comment|// we do special error handling for when OrderFailedException is
comment|// thrown
name|onException
argument_list|(
name|OrderFailedException
operator|.
name|class
argument_list|)
comment|// we mark the exchange as handled so the caller doesn't
comment|// receive the
comment|// OrderFailedException but whatever we want to return
comment|// instead
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
comment|// this bean handles the error handling where we can
comment|// customize the error
comment|// response using java code
operator|.
name|bean
argument_list|(
name|OrderService
operator|.
name|class
argument_list|,
literal|"orderFailed"
argument_list|)
comment|// and since this is an unit test we use mocks for testing
operator|.
name|to
argument_list|(
literal|"mock:error"
argument_list|)
expr_stmt|;
comment|// this is just the generic error handler where we set the
comment|// destination
comment|// and the number of redeliveries we want to try
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
comment|// this is our route where we handle orders
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// this bean is our order service
operator|.
name|bean
argument_list|(
name|OrderService
operator|.
name|class
argument_list|,
literal|"handleOrder"
argument_list|)
comment|// this is the destination if the order is OK
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
comment|// START SNIPPET: e2
comment|/**      * Order service as a plain POJO class      */
DECL|class|OrderService
specifier|public
specifier|static
class|class
name|OrderService
block|{
comment|/**          * This method handle our order input and return the order          *          * @param headers the in headers          * @param payload the in payload          * @return the out payload          * @throws OrderFailedException is thrown if the order cannot be          *             processed          */
DECL|method|handleOrder (@eaders Map headers, @Body String payload)
specifier|public
name|Object
name|handleOrder
parameter_list|(
annotation|@
name|Headers
name|Map
name|headers
parameter_list|,
annotation|@
name|Body
name|String
name|payload
parameter_list|)
throws|throws
name|OrderFailedException
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"customerid"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"customerid"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"Order: kaboom"
operator|.
name|equals
argument_list|(
name|payload
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OrderFailedException
argument_list|(
literal|"Cannot order: kaboom"
argument_list|)
throw|;
block|}
else|else
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"orderid"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
return|return
literal|"Order OK"
return|;
block|}
block|}
comment|/**          * This method creates the response to the caller if the order could not          * be processed          *           * @param headers the in headers          * @param payload the in payload          * @return the out payload          */
DECL|method|orderFailed (@eaders Map headers, @Body String payload)
specifier|public
name|Object
name|orderFailed
parameter_list|(
annotation|@
name|Headers
name|Map
name|headers
parameter_list|,
annotation|@
name|Body
name|String
name|payload
parameter_list|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"customerid"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"customerid"
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"orderid"
argument_list|,
literal|"failed"
argument_list|)
expr_stmt|;
return|return
literal|"Order ERROR"
return|;
block|}
block|}
comment|// END SNIPPET: e2
comment|// START SNIPPET: e3
comment|/**      * Exception thrown if the order cannot be processed      */
DECL|class|OrderFailedException
specifier|public
specifier|static
class|class
name|OrderFailedException
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|OrderFailedException (String message)
specifier|public
name|OrderFailedException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
comment|// END SNIPPET: e3
block|}
end_class

end_unit

