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
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|ProcessorDefinition
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
name|model
operator|.
name|SendDefinition
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
name|spi
operator|.
name|IdAware
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SimpleProcessorIdAwareTest
specifier|public
class|class
name|SimpleProcessorIdAwareTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testIdAware ()
specifier|public
name|void
name|testIdAware
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
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Processor
argument_list|>
name|matches
init|=
name|context
operator|.
name|getRoute
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|filter
argument_list|(
literal|"b*"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|matches
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|bar
init|=
name|matches
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Processor
name|baz
init|=
name|matches
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
operator|(
operator|(
name|IdAware
operator|)
name|bar
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"baz"
argument_list|,
operator|(
operator|(
name|IdAware
operator|)
name|baz
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|bar
operator|=
name|context
operator|.
name|getProcessor
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bar
argument_list|)
expr_stmt|;
name|baz
operator|=
name|context
operator|.
name|getProcessor
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|baz
argument_list|)
expr_stmt|;
name|Processor
name|unknown
init|=
name|context
operator|.
name|getProcessor
argument_list|(
literal|"unknown"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|unknown
argument_list|)
expr_stmt|;
name|Processor
name|result
init|=
name|context
operator|.
name|getProcessor
argument_list|(
literal|"result"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|ProcessorDefinition
name|def
init|=
name|context
operator|.
name|getProcessorDefinition
argument_list|(
literal|"result"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|def
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"result"
argument_list|,
name|def
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|SendDefinition
name|send
init|=
name|assertIsInstanceOf
argument_list|(
name|SendDefinition
operator|.
name|class
argument_list|,
name|def
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|send
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock:result"
argument_list|,
name|send
operator|.
name|getEndpointUri
argument_list|()
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:bar"
argument_list|)
operator|.
name|id
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|id
argument_list|(
literal|"result"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:baz"
argument_list|)
operator|.
name|id
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

