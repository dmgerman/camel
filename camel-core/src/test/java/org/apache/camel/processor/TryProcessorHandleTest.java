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

begin_comment
comment|/**  * Unit test for try .. handle routing (CAMEL-564).  */
end_comment

begin_class
DECL|class|TryProcessorHandleTest
specifier|public
class|class
name|TryProcessorHandleTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|handled
specifier|private
name|boolean
name|handled
decl_stmt|;
DECL|method|testTryCatchFinally ()
specifier|public
name|void
name|testTryCatchFinally
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
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:finally"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<test>Hello World!</test>"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have been handled"
argument_list|,
name|handled
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|process
argument_list|(
operator|new
name|ProcessorFail
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|doCatch
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ProcessorHandle
argument_list|()
argument_list|)
operator|.
name|doFinally
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:finally"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|ProcessorFail
specifier|private
class|class
name|ProcessorFail
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
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Force to fail"
argument_list|)
throw|;
block|}
block|}
DECL|class|ProcessorHandle
specifier|private
class|class
name|ProcessorHandle
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
name|handled
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should not be marked as failed"
argument_list|,
literal|false
argument_list|,
name|exchange
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|Exception
name|e
init|=
operator|(
name|Exception
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"There should be an exception"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|instanceof
name|IllegalStateException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Force to fail"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

