begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.async
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|async
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
name|WaitForTaskToComplete
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|AsyncRouteIfReplyExpectedTest
specifier|public
class|class
name|AsyncRouteIfReplyExpectedTest
extends|extends
name|AsyncRouteTest
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we start this route async
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// we play a bit with the message
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|append
argument_list|(
literal|" World"
argument_list|)
argument_list|)
comment|// now turn the route into async from this point forward
comment|// the caller will have a Future<Exchange> returned as response in OUT
comment|// to be used to grap the async response when he fell like it
comment|// only wait if we expect a reply (also default for async)
operator|.
name|async
argument_list|()
operator|.
name|waitForTaskToComplete
argument_list|(
name|WaitForTaskToComplete
operator|.
name|IfReplyExpected
argument_list|)
comment|// from this point forward this is the async route doing its work
comment|// so we do a bit of delay to simulate heavy work that takes time
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|delay
argument_list|(
literal|100
argument_list|)
comment|// and we also work with the message so we can prepare a response
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|()
argument_list|)
comment|// and we use mocks for unit testing
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

