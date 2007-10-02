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
name|TryType
import|;
end_import

begin_comment
comment|/**  * No catch blocks but handle all should work  *  * @author<a href="mailto:nsandhu@raleys.com">nsandhu</a>  */
end_comment

begin_class
DECL|class|ValidationHandleAllNoCatchTest
specifier|public
class|class
name|ValidationHandleAllNoCatchTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|validator
specifier|protected
name|Processor
name|validator
init|=
operator|new
name|MyValidator
argument_list|()
decl_stmt|;
DECL|field|validEndpoint
specifier|protected
name|MockEndpoint
name|validEndpoint
decl_stmt|;
DECL|field|allEndpoint
specifier|protected
name|MockEndpoint
name|allEndpoint
decl_stmt|;
DECL|method|testValidMessage ()
specifier|public
name|void
name|testValidMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|allEndpoint
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
literal|"<valid/>"
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|allEndpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvalidMessage ()
specifier|public
name|void
name|testInvalidMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|validEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|allEndpoint
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
literal|"<invalid/>"
argument_list|,
literal|"foo"
argument_list|,
literal|"notMatchedHeaderValue"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|validEndpoint
argument_list|,
name|allEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|validEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:valid"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|allEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:all"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
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
name|TryType
name|tryType
init|=
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|tryBlock
argument_list|()
operator|.
name|process
argument_list|(
name|validator
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:valid"
argument_list|)
decl_stmt|;
name|tryType
operator|.
name|handleAll
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:all"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

