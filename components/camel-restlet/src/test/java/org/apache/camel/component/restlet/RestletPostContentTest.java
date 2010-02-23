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
name|entity
operator|.
name|StringEntity
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
comment|/**  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RestletPostContentTest
specifier|public
class|class
name|RestletPostContentTest
extends|extends
name|RestletTestSupport
block|{
DECL|field|MSG_BODY
specifier|private
specifier|static
specifier|final
name|String
name|MSG_BODY
init|=
literal|"Hello World!"
decl_stmt|;
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
literal|"restlet:http://localhost:9080/users/{username}?restletMethod=POST"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|SetUserProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|SetUserProcessor
class|class
name|SetUserProcessor
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
name|MSG_BODY
argument_list|,
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
literal|"http://localhost:9080/users/homer"
argument_list|)
decl_stmt|;
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
name|StringEntity
argument_list|(
name|MSG_BODY
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

