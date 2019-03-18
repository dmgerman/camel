begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.service.lra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|service
operator|.
name|lra
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|LRAOptionsIT
specifier|public
class|class
name|LRAOptionsIT
extends|extends
name|AbstractLRATestSupport
block|{
annotation|@
name|Test
DECL|method|testHeaderForwardedToComplete ()
specifier|public
name|void
name|testHeaderForwardedToComplete
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|complete
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:complete"
argument_list|)
decl_stmt|;
name|complete
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|complete
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"id"
argument_list|,
literal|"myheader"
argument_list|)
expr_stmt|;
name|complete
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"name"
argument_list|,
literal|"Nicola"
argument_list|)
expr_stmt|;
name|complete
operator|.
name|expectedMessagesMatches
argument_list|(
name|ex
lambda|->
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|SAGA_LONG_RUNNING_ACTION
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:workflow"
argument_list|,
literal|"Hello"
argument_list|,
literal|"myname"
argument_list|,
literal|"Nicola"
argument_list|)
expr_stmt|;
name|complete
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHeaderForwardedToCompensate ()
specifier|public
name|void
name|testHeaderForwardedToCompensate
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|compensate
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:compensate"
argument_list|)
decl_stmt|;
name|compensate
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|compensate
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"id"
argument_list|,
literal|"myheader"
argument_list|)
expr_stmt|;
name|compensate
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"name"
argument_list|,
literal|"Nicola"
argument_list|)
expr_stmt|;
name|compensate
operator|.
name|expectedMessagesMatches
argument_list|(
name|ex
lambda|->
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|SAGA_LONG_RUNNING_ACTION
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:workflow"
argument_list|,
literal|"compensate"
argument_list|,
literal|"myname"
argument_list|,
literal|"Nicola"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"Should throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// OK
block|}
name|compensate
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RuntimeCamelException
operator|.
name|class
argument_list|)
DECL|method|testRouteDoesNotHangOnOptionError ()
specifier|public
name|void
name|testRouteDoesNotHangOnOptionError
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:wrong-expression"
argument_list|,
literal|"Hello"
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
literal|"direct:workflow"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|option
argument_list|(
literal|"id"
argument_list|,
name|constant
argument_list|(
literal|"myheader"
argument_list|)
argument_list|)
operator|.
name|option
argument_list|(
literal|"name"
argument_list|,
name|header
argument_list|(
literal|"myname"
argument_list|)
argument_list|)
operator|.
name|completion
argument_list|(
literal|"mock:complete"
argument_list|)
operator|.
name|compensation
argument_list|(
literal|"mock:compensate"
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
name|isEqualTo
argument_list|(
literal|"compensate"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|ex
lambda|->
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"forced compensate"
argument_list|)
throw|;
block|}
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"myname"
argument_list|,
name|constant
argument_list|(
literal|"TryToOverride"
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"name"
argument_list|,
name|constant
argument_list|(
literal|"TryToOverride"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:wrong-expression"
argument_list|)
operator|.
name|saga
argument_list|()
operator|.
name|option
argument_list|(
literal|"id"
argument_list|,
name|simple
argument_list|(
literal|"${10 / 0}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:info"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

