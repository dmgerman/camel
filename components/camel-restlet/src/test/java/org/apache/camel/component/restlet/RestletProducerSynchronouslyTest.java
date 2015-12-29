begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|RestletProducerSynchronouslyTest
specifier|public
class|class
name|RestletProducerSynchronouslyTest
extends|extends
name|RestletTestSupport
block|{
annotation|@
name|Test
DECL|method|testRestletProducerGet ()
specifier|public
name|void
name|testRestletProducerGet
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
literal|"id"
argument_list|,
literal|123
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"123;Donald Duck"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRestletProducerDelete ()
specifier|public
name|void
name|testRestletProducerDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:delete"
argument_list|,
literal|null
argument_list|,
literal|"id"
argument_list|,
literal|123
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"123;Donald Duck"
argument_list|,
name|out
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
comment|// force synchronous processing using restlet
name|RestletComponent
name|restlet
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"restlet"
argument_list|,
name|RestletComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|restlet
operator|.
name|setSynchronous
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/123/basic"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:reply"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:delete"
argument_list|)
operator|.
name|to
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/123/basicrestletMethod=DELETE"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/{id}/basic?restletMethods=GET,DELETE"
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
name|String
name|id
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"id"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|id
operator|+
literal|";Donald Duck"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

