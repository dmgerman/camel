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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|spi
operator|.
name|InterceptStrategy
import|;
end_import

begin_comment
comment|/**  * Using a custom interceptor which is not a {@link org.apache.camel.AsyncProcessor} which Camel  * detects and uses a bridge to adapt to so the asynchronous engine can still run. Albeit not  * the most optimal solution but it runs. Camel will log a WARN so user can see the issue  * and change his interceptor to comply.  *  * @version   */
end_comment

begin_class
DECL|class|AsyncEndpointCustomInterceptorTest
specifier|public
class|class
name|AsyncEndpointCustomInterceptorTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|beforeThreadName
specifier|private
specifier|static
name|String
name|beforeThreadName
decl_stmt|;
DECL|field|afterThreadName
specifier|private
specifier|static
name|String
name|afterThreadName
decl_stmt|;
DECL|field|interceptor
specifier|private
name|MyInterceptor
name|interceptor
init|=
operator|new
name|MyInterceptor
argument_list|()
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
literal|"Hello Camel"
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
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Camel"
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Camel"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye Camel"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|interceptor
operator|.
name|getCounter
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should use different threads"
argument_list|,
name|beforeThreadName
operator|.
name|equalsIgnoreCase
argument_list|(
name|afterThreadName
argument_list|)
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
name|context
operator|.
name|addInterceptStrategy
argument_list|(
name|interceptor
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:before"
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
name|beforeThreadName
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
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
name|afterThreadName
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:after"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:after"
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
comment|// START SNIPPET: e1
DECL|class|MyInterceptor
specifier|private
specifier|static
class|class
name|MyInterceptor
implements|implements
name|InterceptStrategy
block|{
DECL|field|counter
specifier|private
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|method|wrapProcessorInInterceptors (final CamelContext context, final ProcessorDefinition<?> definition, final Processor target, final Processor nextTarget)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|,
specifier|final
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
specifier|final
name|Processor
name|target
parameter_list|,
specifier|final
name|Processor
name|nextTarget
parameter_list|)
throws|throws
name|Exception
block|{
return|return
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
comment|// we just want to count number of interceptions
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
comment|// and continue processing the exchange
name|target
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getCounter ()
specifier|public
name|int
name|getCounter
parameter_list|()
block|{
return|return
name|counter
operator|.
name|get
argument_list|()
return|;
block|}
block|}
comment|// END SNIPPET: e1
block|}
end_class

end_unit

