begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|Message
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpMethodBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|GetMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|PostMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|StringRequestEntity
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

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|*
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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

begin_class
DECL|class|HttpMethodRestrictTest
specifier|public
class|class
name|HttpMethodRestrictTest
extends|extends
name|BaseJettyTest
block|{
DECL|method|getUrl ()
specifier|private
name|String
name|getUrl
parameter_list|()
block|{
return|return
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/methodRestrict"
return|;
block|}
annotation|@
name|Test
DECL|method|testProperHttpMethod ()
specifier|public
name|void
name|testProperHttpMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpClient
name|httpClient
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
name|PostMethod
name|httpPost
init|=
operator|new
name|PostMethod
argument_list|(
name|getUrl
argument_list|()
argument_list|)
decl_stmt|;
name|StringRequestEntity
name|reqEntity
init|=
operator|new
name|StringRequestEntity
argument_list|(
literal|"This is a test"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|httpPost
operator|.
name|setRequestEntity
argument_list|(
name|reqEntity
argument_list|)
expr_stmt|;
name|int
name|status
init|=
name|httpClient
operator|.
name|executeMethod
argument_list|(
name|httpPost
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response status"
argument_list|,
literal|200
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|httpPost
operator|.
name|getResponseBodyAsString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong result"
argument_list|,
literal|"This is a test response"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testImproperHttpMethod ()
specifier|public
name|void
name|testImproperHttpMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpClient
name|httpClient
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
name|GetMethod
name|httpGet
init|=
operator|new
name|GetMethod
argument_list|(
name|getUrl
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|status
init|=
name|httpClient
operator|.
name|executeMethod
argument_list|(
name|httpGet
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response status"
argument_list|,
literal|405
argument_list|,
name|status
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}/methodRestrict?httpMethodRestrict=POST"
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
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|request
init|=
name|in
operator|.
name|getBody
argument_list|(
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
name|request
operator|+
literal|" response"
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

