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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|TracerTest
specifier|public
class|class
name|TracerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|tracer
specifier|private
name|Tracer
name|tracer
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"traceFormatter"
argument_list|,
operator|new
name|DefaultTraceFormatter
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|tracer
operator|=
name|Tracer
operator|.
name|createTracer
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|setTraceInterceptors
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|setTraceFilter
argument_list|(
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|setTraceExceptions
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|setLogStackTrace
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|setUseJpa
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|setDestination
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:traced"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|addInterceptStrategy
argument_list|(
name|tracer
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|tracer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|testTracer ()
specifier|public
name|void
name|testTracer
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|tracer
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:traced"
argument_list|)
decl_stmt|;
name|tracer
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
name|expectedMessageCount
argument_list|(
literal|3
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|DefaultTraceEventMessage
name|em
init|=
name|tracer
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
name|DefaultTraceEventMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Camel"
argument_list|,
name|em
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"String"
argument_list|,
name|em
operator|.
name|getBodyType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|em
operator|.
name|getCausedByException
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|em
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|em
operator|.
name|getShortExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|em
operator|.
name|getExchangePattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|em
operator|.
name|getFromEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
comment|// there is always a breadcrumb header
name|assertNotNull
argument_list|(
name|em
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|em
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|em
operator|.
name|getOutBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|em
operator|.
name|getOutBodyType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|em
operator|.
name|getOutHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|em
operator|.
name|getPreviousNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|em
operator|.
name|getToNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|em
operator|.
name|getTimestamp
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

