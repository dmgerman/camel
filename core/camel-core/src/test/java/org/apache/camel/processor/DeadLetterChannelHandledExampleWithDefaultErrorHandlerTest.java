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

begin_comment
comment|/**  * Unit test to verify that handled policy is working as expected for wiki documentation.  */
end_comment

begin_class
DECL|class|DeadLetterChannelHandledExampleWithDefaultErrorHandlerTest
specifier|public
class|class
name|DeadLetterChannelHandledExampleWithDefaultErrorHandlerTest
extends|extends
name|DeadLetterChannelHandledExampleTest
block|{
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
comment|// we do special error handling for when OrderFailedException is thrown
name|onException
argument_list|(
name|OrderFailedException
operator|.
name|class
argument_list|)
comment|// we mark the exchange as handled so the caller doesn't receive the
comment|// OrderFailedException but whatever we want to return instead
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
comment|// this bean handles the error handling where we can customize the error
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
block|}
end_class

end_unit

