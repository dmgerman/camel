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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|AsyncEndpointSplitFineGrainedErrorHandlingTest
specifier|public
class|class
name|AsyncEndpointSplitFineGrainedErrorHandlingTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|counter
specifier|private
specifier|static
name|int
name|counter
decl_stmt|;
DECL|method|testAsyncEndpoint ()
specifier|public
name|void
name|testAsyncEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:after"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Camel"
argument_list|,
literal|"Bye Camel"
argument_list|,
literal|"Bye Camel"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A,B,C"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A,B,C"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// 3 split messages + 1 redelivery attempt
name|assertEquals
argument_list|(
literal|3
operator|+
literal|1
argument_list|,
name|counter
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
name|context
operator|.
name|addComponent
argument_list|(
literal|"async"
argument_list|,
operator|new
name|MyAsyncComponent
argument_list|()
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|Exception
operator|.
name|class
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
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|to
argument_list|(
literal|"async:Bye Camel"
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
if|if
condition|(
name|counter
operator|++
operator|==
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot do this"
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:after"
argument_list|)
operator|.
name|end
argument_list|()
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

