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

begin_comment
comment|/**  * Unit test for pipeline keeping the MEP (CAMEL-1233)  */
end_comment

begin_class
DECL|class|PipelineMEPTest
specifier|public
class|class
name|PipelineMEPTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testInOnly ()
specifier|public
name|void
name|testInOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:a"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
name|out
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should keep MEP as InOnly
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInOut ()
specifier|public
name|void
name|testInOut
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:a"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|out
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should keep MEP as InOut
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPattern
argument_list|()
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
name|inProcessor
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
name|number
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|number
operator|==
literal|null
condition|)
block|{
name|number
operator|=
literal|0
expr_stmt|;
block|}
name|number
operator|=
name|number
operator|+
literal|1
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|number
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
specifier|final
name|Processor
name|outProcessor
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
name|number
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|number
operator|==
literal|null
condition|)
block|{
name|number
operator|=
literal|0
expr_stmt|;
block|}
name|number
operator|=
name|number
operator|+
literal|1
expr_stmt|;
comment|// this is a bit evil we let you set on OUT body even if the MEP is InOnly
comment|// however the result after the routing is correct using APIs to get the result
comment|// however the exchange will carry body IN and OUT when the route completes, as
comment|// we operate on the original exchange in this processor
comment|// (= we are the first node in the route after the from consumer)
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|number
argument_list|)
expr_stmt|;
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
literal|"direct:a"
argument_list|)
operator|.
name|process
argument_list|(
name|outProcessor
argument_list|)
comment|// this pipeline is not really needed by to have some more routing in there to test with
operator|.
name|pipeline
argument_list|(
literal|"log:x"
argument_list|,
literal|"log:y"
argument_list|)
operator|.
name|process
argument_list|(
name|inProcessor
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

