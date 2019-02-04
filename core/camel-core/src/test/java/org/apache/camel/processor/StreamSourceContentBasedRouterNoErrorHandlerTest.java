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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * Test for handling a StreamSource in a content-based router with XPath predicates  */
end_comment

begin_class
DECL|class|StreamSourceContentBasedRouterNoErrorHandlerTest
specifier|public
class|class
name|StreamSourceContentBasedRouterNoErrorHandlerTest
extends|extends
name|StreamSourceContentBasedRouterTest
block|{
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
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
comment|// should work with no error handler as the stream cache
comment|// is enabled and make sure the predicates can be evaluated
comment|// multiple times
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/message/text() = 'xx'"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:x"
argument_list|)
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/message/text() = 'yy'"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:y"
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

