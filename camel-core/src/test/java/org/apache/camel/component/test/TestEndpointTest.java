begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|test
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
name|CamelContext
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
name|Consumer
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
name|Endpoint
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
name|Producer
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
name|support
operator|.
name|DefaultEndpoint
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
name|support
operator|.
name|DefaultExchange
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
DECL|class|TestEndpointTest
specifier|public
class|class
name|TestEndpointTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|expectedBody
specifier|private
name|String
name|expectedBody
init|=
literal|"Hello World"
decl_stmt|;
annotation|@
name|Test
DECL|method|testMocksAreValid ()
specifier|public
name|void
name|testMocksAreValid
parameter_list|()
throws|throws
name|Exception
block|{
comment|// now run the test and send in a message with the expected body
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
name|expectedBody
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|MyEndpoint
name|my
init|=
operator|new
name|MyEndpoint
argument_list|(
literal|"my:foo"
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|context
operator|.
name|addEndpoint
argument_list|(
literal|"my:foo"
argument_list|,
name|my
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"test:my:foo?timeout=0"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyEndpoint
specifier|private
specifier|final
class|class
name|MyEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|method|MyEndpoint (String endpointUri, CamelContext camelContext)
specifier|private
name|MyEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// not needed for this test
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (final Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
specifier|final
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|Consumer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|MyEndpoint
operator|.
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
comment|// when starting then send a message to the processor
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

