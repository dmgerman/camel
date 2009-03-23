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
name|RuntimeCamelException
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|RollbackTest
specifier|public
class|class
name|RollbackTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testOk ()
specifier|public
name|void
name|testOk
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
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"ok"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testRollback ()
specifier|public
name|void
name|testRollback
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:rollback"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"bad"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a RollbackExchangeException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|RollbackExchangeException
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testRollbackWithExchange ()
specifier|public
name|void
name|testRollbackWithExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:rollback"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start"
argument_list|,
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"bad"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|RollbackExchangeException
operator|.
name|class
argument_list|,
name|out
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should be marked as rollback"
argument_list|,
literal|true
argument_list|,
name|out
operator|.
name|isRollbackOnly
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|1
argument_list|)
operator|.
name|delay
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|body
argument_list|()
operator|.
name|isNotEqualTo
argument_list|(
literal|"ok"
argument_list|)
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
name|assertFalse
argument_list|(
literal|"Rollback flag should have been cleared on redelivery"
argument_list|,
name|exchange
operator|.
name|isRollbackOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:rollback"
argument_list|)
operator|.
name|rollback
argument_list|(
literal|"That do not work"
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
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

