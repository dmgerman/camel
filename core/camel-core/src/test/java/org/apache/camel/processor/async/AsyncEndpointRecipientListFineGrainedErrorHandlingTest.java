begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Test
import|;
end_import

begin_class
DECL|class|AsyncEndpointRecipientListFineGrainedErrorHandlingTest
specifier|public
class|class
name|AsyncEndpointRecipientListFineGrainedErrorHandlingTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|counter
specifier|private
specifier|static
name|int
name|counter
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
literal|"fail"
argument_list|,
operator|new
name|MyFailBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testAsyncEndpointOK ()
specifier|public
name|void
name|testAsyncEndpointOK
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
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
name|recipientList
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|stopOnException
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
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
literal|"mock:foo"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:baz"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"foo"
argument_list|,
literal|"mock:foo,async:bye:camel,mock:bar,mock:baz"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsyncEndpointERROR ()
specifier|public
name|void
name|testAsyncEndpointERROR
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
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
name|recipientList
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|stopOnException
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
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
literal|"mock:foo"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:baz"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"foo"
argument_list|,
literal|"mock:foo,mock:bar,bean:fail,mock:baz"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|counter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|class|MyFailBean
specifier|public
specifier|static
class|class
name|MyFailBean
block|{
DECL|method|doSomething (Exchange exchange)
specifier|public
name|String
name|doSomething
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|counter
operator|++
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean://fail"
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

