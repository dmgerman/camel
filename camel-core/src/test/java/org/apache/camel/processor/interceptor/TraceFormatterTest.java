begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|ProcessorType
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|TraceFormatterTest
specifier|public
class|class
name|TraceFormatterTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|tracedBodies
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|tracedBodies
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|testSendingSomeMessagesBeingTraced ()
specifier|public
name|void
name|testSendingSomeMessagesBeingTraced
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:traced"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello London"
argument_list|,
literal|"to"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// assert we received the correct bodies at the given time of interception
comment|// and that the bodies haven't changed during the routing of the original
comment|// exchange that changes its body over time (Hello London -> Bye World)
name|assertEquals
argument_list|(
literal|"Hello London"
argument_list|,
name|tracedBodies
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|tracedBodies
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Goodday World"
argument_list|,
name|tracedBodies
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|tracedBodies
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e1
comment|// we create a tracer where we want to use our own formatter instead of the default one
name|Tracer
name|tracer
init|=
operator|new
name|Tracer
argument_list|()
decl_stmt|;
comment|// use our own formatter instead of the default one
name|MyTraceFormatter
name|formatter
init|=
operator|new
name|MyTraceFormatter
argument_list|()
decl_stmt|;
name|tracer
operator|.
name|setFormatter
argument_list|(
name|formatter
argument_list|)
expr_stmt|;
comment|// and we must remeber to add the tracer to Camel
name|getContext
argument_list|()
operator|.
name|addInterceptStrategy
argument_list|(
name|tracer
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
comment|// this is only for unit testing to use mock for assertion
name|tracer
operator|.
name|setDestinationUri
argument_list|(
literal|"direct:traced"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|(
literal|"Goodday World"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:traced"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyTraveAssertProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:traced"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyProcessor
class|class
name|MyProcessor
implements|implements
name|Processor
block|{
DECL|field|msg
specifier|private
name|String
name|msg
decl_stmt|;
DECL|method|MyProcessor (String msg)
name|MyProcessor
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|this
operator|.
name|msg
operator|=
name|msg
expr_stmt|;
block|}
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MyTraveAssertProcessor
class|class
name|MyTraveAssertProcessor
implements|implements
name|Processor
block|{
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
comment|// take a snapshot at current time for assertion later
comment|// after mock assertions in unit test method
name|TraceEventMessage
name|event
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|TraceEventMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|tracedBodies
operator|.
name|add
argument_list|(
operator|new
name|String
argument_list|(
name|event
operator|.
name|getBody
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// START SNIPPET: e2
comment|// here we have out own formatter where we can create the output we want for trace logs
comment|// as this is a test we just create a simple string with * around the body
DECL|class|MyTraceFormatter
class|class
name|MyTraceFormatter
implements|implements
name|TraceFormatter
block|{
DECL|method|format (TraceInterceptor interceptor, ProcessorType node, Exchange exchange)
specifier|public
name|Object
name|format
parameter_list|(
name|TraceInterceptor
name|interceptor
parameter_list|,
name|ProcessorType
name|node
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
literal|"***"
operator|+
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
operator|+
literal|"***"
return|;
block|}
block|}
comment|// END SNIPPET: e2
block|}
end_class

end_unit

