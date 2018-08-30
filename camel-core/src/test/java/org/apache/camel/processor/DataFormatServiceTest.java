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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|CamelContextAware
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
name|spi
operator|.
name|DataFormat
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
name|ServiceSupport
import|;
end_import

begin_comment
comment|/**  * Test that data format receives callback if its a service / CamelContextAware  *  * @version   */
end_comment

begin_class
DECL|class|DataFormatServiceTest
specifier|public
class|class
name|DataFormatServiceTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|my
specifier|protected
specifier|final
name|MyDataFormat
name|my
init|=
operator|new
name|MyDataFormat
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testMarshal ()
specifier|public
name|void
name|testMarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|my
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|my
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hi Camel"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|my
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|my
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshal ()
specifier|public
name|void
name|testUnmarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|my
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|my
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:b"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|my
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|my
operator|.
name|getCamelContext
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
comment|// START SNIPPET: e1
comment|// marshal using our custom data format. (my is an instance of MyDataFormat)
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|marshal
argument_list|(
name|my
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
comment|// unmarshal using our custom data format.
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|my
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
comment|// START SNIPPET: e2
DECL|class|MyDataFormat
specifier|public
specifier|static
class|class
name|MyDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|CamelContextAware
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|stream
operator|.
name|write
argument_list|(
literal|"Hi Camel"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|"Bye World"
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
comment|// END SNIPPET: e2
block|}
end_class

end_unit

