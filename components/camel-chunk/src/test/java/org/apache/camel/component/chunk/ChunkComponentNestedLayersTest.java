begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.chunk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|chunk
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
comment|/**  * Unit test for {@link ChunkComponent} and {@link ChunkEndpoint}  */
end_comment

begin_class
DECL|class|ChunkComponentNestedLayersTest
specifier|public
class|class
name|ChunkComponentNestedLayersTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:endSimple"
argument_list|)
DECL|field|endSimpleMock
specifier|protected
name|MockEndpoint
name|endSimpleMock
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"direct:startSimple"
argument_list|)
DECL|field|startSimpleProducerTemplate
specifier|protected
name|ProducerTemplate
name|startSimpleProducerTemplate
decl_stmt|;
comment|/**      * Test using two following distinct themeLayer of the same theme without Resource URI header defined      */
annotation|@
name|Test
DECL|method|testChunkDoubleLayers ()
specifier|public
name|void
name|testChunkDoubleLayers
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Prepare
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"The Body"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"name"
argument_list|,
literal|"Andrew"
argument_list|)
expr_stmt|;
name|endSimpleMock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endSimpleMock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<div>\nMars to Andrew. Come in, Andrew. This is the body:<div>\nEarth to Andrew. Come in, Andrew.\n</div>\n\n</div>\n"
argument_list|)
expr_stmt|;
comment|// Act
name|startSimpleProducerTemplate
operator|.
name|send
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// Verify
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:startSimple"
argument_list|)
operator|.
name|to
argument_list|(
literal|"chunk:example?themeLayer=example_1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"chunk:example?themeLayer=example_2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endSimple"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

