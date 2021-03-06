begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
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
name|CamelExchangeException
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
name|CamelExecutionException
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
name|RollbackExchangeException
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CharlesSplitAndTryCatchRollbackIssueTest
specifier|public
class|class
name|CharlesSplitAndTryCatchRollbackIssueTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSplitWithTryCatchAndRollbackOK ()
specifier|public
name|void
name|testSplitWithTryCatchAndRollbackOK
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|split
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|ile
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:ile"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|exception
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:exception"
argument_list|)
decl_stmt|;
name|split
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
name|ile
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A,B,C"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitWithTryCatchAndRollbackILE ()
specifier|public
name|void
name|testSplitWithTryCatchAndRollbackILE
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|split
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|ile
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:ile"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|exception
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:exception"
argument_list|)
decl_stmt|;
name|split
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
name|ile
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A,B,Forced,C"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitWithTryCatchAndRollbackException ()
specifier|public
name|void
name|testSplitWithTryCatchAndRollbackException
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|split
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|ile
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:ile"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|exception
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:exception"
argument_list|)
decl_stmt|;
name|split
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|ile
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A,B,Kaboom,C"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|CamelExchangeException
name|ee
init|=
name|assertIsInstanceOf
argument_list|(
name|CamelExchangeException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ee
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Multicast processing failed for number 2."
argument_list|)
argument_list|)
expr_stmt|;
name|RollbackExchangeException
name|re
init|=
name|assertIsInstanceOf
argument_list|(
name|RollbackExchangeException
operator|.
name|class
argument_list|,
name|ee
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|re
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Intended rollback"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSplitWithTryCatchAndRollbacILEAndException ()
specifier|public
name|void
name|testSplitWithTryCatchAndRollbacILEAndException
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|split
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:split"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|ile
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:ile"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|exception
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:exception"
argument_list|)
decl_stmt|;
name|split
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|ile
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A,Forced,B,Kaboom,C"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|CamelExchangeException
name|ee
init|=
name|assertIsInstanceOf
argument_list|(
name|CamelExchangeException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ee
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Multicast processing failed for number 3."
argument_list|)
argument_list|)
expr_stmt|;
name|RollbackExchangeException
name|re
init|=
name|assertIsInstanceOf
argument_list|(
name|RollbackExchangeException
operator|.
name|class
argument_list|,
name|ee
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|re
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Intended rollback"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|stopOnException
argument_list|()
operator|.
name|doTry
argument_list|()
operator|.
name|process
argument_list|(
operator|new
name|MyProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
operator|.
name|doCatch
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:ile"
argument_list|)
operator|.
name|doCatch
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:exception"
argument_list|)
operator|.
name|rollback
argument_list|()
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyProcessor
specifier|public
specifier|static
class|class
name|MyProcessor
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
name|String
name|body
init|=
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
decl_stmt|;
if|if
condition|(
literal|"Forced"
operator|.
name|equals
argument_list|(
name|body
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
literal|"Kaboom"
operator|.
name|equals
argument_list|(
name|body
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Kaboom"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

