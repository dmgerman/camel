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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|NameValuePair
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
name|entity
operator|.
name|UrlEncodedFormEntity
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
name|HttpEntityEnclosingRequestBase
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
name|client
operator|.
name|methods
operator|.
name|HttpUriRequest
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
name|message
operator|.
name|BasicNameValuePair
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
DECL|class|RestletPostFormTest
specifier|public
class|class
name|RestletPostFormTest
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
literal|"/users?restletMethod=POST"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|PostProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|PostProcessor
class|class
name|PostProcessor
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
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"OK"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testPostBody ()
specifier|public
name|void
name|testPostBody
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpUriRequest
name|method
init|=
operator|new
name|HttpPost
argument_list|(
literal|"http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|NameValuePair
argument_list|>
name|urlParameters
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|urlParameters
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
operator|(
operator|(
name|HttpEntityEnclosingRequestBase
operator|)
name|method
operator|)
operator|.
name|setEntity
argument_list|(
operator|new
name|UrlEncodedFormEntity
argument_list|(
name|urlParameters
argument_list|)
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|doExecute
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|assertHttpResponse
argument_list|(
name|response
argument_list|,
literal|200
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

