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
name|apache
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpPost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|EntityUtils
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
comment|/**  *  * @version  */
end_comment

begin_class
DECL|class|RestletExceptionResponseTest
specifier|public
class|class
name|RestletExceptionResponseTest
extends|extends
name|RestletTestSupport
block|{
annotation|@
name|Override
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
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/{username}?restletMethod=POST"
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
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn something went wrong"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|"/users/tester?restletMethod=POST"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testExceptionResponse ()
specifier|public
name|void
name|testExceptionResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpResponse
name|response
init|=
name|doExecute
argument_list|(
operator|new
name|HttpPost
argument_list|(
literal|"http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/homer"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|body
init|=
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|assertHttpResponse
argument_list|(
name|response
argument_list|,
literal|500
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|body
operator|.
name|contains
argument_list|(
literal|"IllegalArgumentException"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|body
operator|.
name|contains
argument_list|(
literal|"Damn something went wrong"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRestletProducerGetExceptionResponse ()
specifier|public
name|void
name|testRestletProducerGetExceptionResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|sendRequest
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/tester?restletMethod=POST"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendRequestDirectEndpoint ()
specifier|public
name|void
name|testSendRequestDirectEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|sendRequest
argument_list|(
literal|"direct:start"
argument_list|)
expr_stmt|;
block|}
DECL|method|sendRequest (String endpointUri)
specifier|protected
name|void
name|sendRequest
parameter_list|(
name|String
name|endpointUri
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
literal|"<order foo='1'/>"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|RestletOperationException
name|exception
init|=
operator|(
name|RestletOperationException
operator|)
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|String
name|body
init|=
name|exception
operator|.
name|getResponseBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/tester"
argument_list|,
name|exception
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|body
operator|.
name|contains
argument_list|(
literal|"IllegalArgumentException"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|body
operator|.
name|contains
argument_list|(
literal|"Damn something went wrong"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

