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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SplitTwoSubUnitOfWorkTest
specifier|public
class|class
name|SplitTwoSubUnitOfWorkTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|counter
specifier|private
specifier|static
name|int
name|counter
decl_stmt|;
DECL|method|testOK ()
specifier|public
name|void
name|testOK
parameter_list|()
throws|throws
name|Exception
block|{
name|counter
operator|=
literal|0
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Tiger"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Elephant"
argument_list|,
literal|"Lion"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:line"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Tiger"
argument_list|,
literal|"Camel"
argument_list|,
literal|"Elephant"
argument_list|,
literal|"Lion"
argument_list|)
expr_stmt|;
name|MyBody
name|body
init|=
operator|new
name|MyBody
argument_list|(
literal|"Tiger,Camel"
argument_list|,
literal|"Elephant,Lion"
argument_list|)
decl_stmt|;
name|template
operator|.
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
DECL|method|testError ()
specifier|public
name|void
name|testError
parameter_list|()
throws|throws
name|Exception
block|{
name|counter
operator|=
literal|0
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|MyBody
operator|.
name|class
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Tiger"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Elephant"
argument_list|,
literal|"Donkey"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:line"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Tiger"
argument_list|,
literal|"Camel"
argument_list|,
literal|"Elephant"
argument_list|)
expr_stmt|;
name|MyBody
name|body
init|=
operator|new
name|MyBody
argument_list|(
literal|"Tiger,Camel"
argument_list|,
literal|"Elephant,Donkey"
argument_list|)
decl_stmt|;
name|template
operator|.
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
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|counter
argument_list|)
expr_stmt|;
comment|// 1 first + 3 redeliveries
name|MyBody
name|dead
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|MyBody
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"Should be original message in DLC"
argument_list|,
name|body
argument_list|,
name|dead
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|useOriginalMessage
argument_list|()
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|split
argument_list|(
name|simple
argument_list|(
literal|"${body.foo}"
argument_list|)
argument_list|)
operator|.
name|shareUnitOfWork
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:line"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|split
argument_list|(
name|simple
argument_list|(
literal|"${body.bar}"
argument_list|)
argument_list|)
operator|.
name|shareUnitOfWork
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:line"
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
name|from
argument_list|(
literal|"direct:line"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:line"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|.
name|contains
argument_list|(
literal|"Donkey"
argument_list|)
condition|)
block|{
name|counter
operator|++
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Donkey not allowed"
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:line"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyBody
specifier|public
specifier|static
specifier|final
class|class
name|MyBody
block|{
DECL|field|foo
specifier|private
name|String
name|foo
decl_stmt|;
DECL|field|bar
specifier|private
name|String
name|bar
decl_stmt|;
DECL|method|MyBody (String foo, String bar)
specifier|private
name|MyBody
parameter_list|(
name|String
name|foo
parameter_list|,
name|String
name|bar
parameter_list|)
block|{
name|this
operator|.
name|foo
operator|=
name|foo
expr_stmt|;
name|this
operator|.
name|bar
operator|=
name|bar
expr_stmt|;
block|}
DECL|method|getFoo ()
specifier|public
name|String
name|getFoo
parameter_list|()
block|{
return|return
name|foo
return|;
block|}
DECL|method|getBar ()
specifier|public
name|String
name|getBar
parameter_list|()
block|{
return|return
name|bar
return|;
block|}
block|}
block|}
end_class

end_unit

