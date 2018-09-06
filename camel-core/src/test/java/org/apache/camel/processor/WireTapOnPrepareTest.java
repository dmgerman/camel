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
comment|/**  *  */
end_comment

begin_class
DECL|class|WireTapOnPrepareTest
specifier|public
class|class
name|WireTapOnPrepareTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testWireTapOnPrepare ()
specifier|public
name|void
name|testWireTapOnPrepare
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"mock:a"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"1 Tony the Tiger"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
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
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"1 Tiger"
argument_list|)
expr_stmt|;
specifier|final
name|Animal
name|original
init|=
operator|new
name|Animal
argument_list|(
literal|1
argument_list|,
literal|"Tiger"
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|original
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
specifier|final
name|Animal
name|aAnimal
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|getExchanges
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
name|Animal
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Animal
name|bAnimal
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|getExchanges
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
name|Animal
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"Original instance should stay in main route"
argument_list|,
name|original
argument_list|,
name|bAnimal
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
literal|"Copy should go to Wire Tap Endpoint"
argument_list|,
name|original
argument_list|,
name|aAnimal
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
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|wireTap
argument_list|(
literal|"direct:a"
argument_list|,
operator|new
name|AnimalDeepClonePrepare
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:b"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ProcessorA
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|delay
argument_list|(
literal|1000
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ProcessorB
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|ProcessorA
specifier|public
specifier|static
class|class
name|ProcessorA
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
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
name|Animal
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Animal
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|body
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Tiger"
argument_list|,
name|body
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// adjust the name
name|body
operator|.
name|setName
argument_list|(
literal|"Tony the Tiger"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ProcessorB
specifier|public
specifier|static
class|class
name|ProcessorB
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
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
name|Animal
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Animal
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|body
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Tiger"
argument_list|,
name|body
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

