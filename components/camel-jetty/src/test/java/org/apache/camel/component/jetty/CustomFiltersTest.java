begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
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

begin_class
DECL|class|CustomFiltersTest
specifier|public
class|class
name|CustomFiltersTest
extends|extends
name|BaseJettyTest
block|{
DECL|class|MyTestFilter
specifier|private
specifier|static
class|class
name|MyTestFilter
implements|implements
name|Filter
block|{
DECL|field|keyWord
specifier|private
name|String
name|keyWord
decl_stmt|;
annotation|@
name|Override
DECL|method|doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
comment|// set a marker attribute to show that this filter class was used
operator|(
operator|(
name|HttpServletResponse
operator|)
name|response
operator|)
operator|.
name|addHeader
argument_list|(
literal|"MyTestFilter"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|HttpServletResponse
operator|)
name|response
operator|)
operator|.
name|setHeader
argument_list|(
literal|"KeyWord"
argument_list|,
name|keyWord
argument_list|)
expr_stmt|;
name|chain
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init (FilterConfig filterConfig)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|filterConfig
parameter_list|)
throws|throws
name|ServletException
block|{
name|keyWord
operator|=
name|filterConfig
operator|.
name|getInitParameter
argument_list|(
literal|"keyWord"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
comment|// do nothing here
block|}
block|}
DECL|method|sendRequestAndVerify (String url)
specifier|private
name|void
name|sendRequestAndVerify
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|Exception
block|{
name|HttpClient
name|httpclient
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
name|PostMethod
name|httppost
init|=
operator|new
name|PostMethod
argument_list|(
name|url
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
name|httppost
operator|.
name|setRequestEntity
argument_list|(
name|reqEntity
argument_list|)
expr_stmt|;
name|int
name|status
init|=
name|httpclient
operator|.
name|executeMethod
argument_list|(
name|httppost
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
name|httppost
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
name|assertNotNull
argument_list|(
literal|"Did not use custom multipart filter"
argument_list|,
name|httppost
operator|.
name|getResponseHeader
argument_list|(
literal|"MyTestFilter"
argument_list|)
argument_list|)
expr_stmt|;
comment|// just make sure the KeyWord header is set
name|assertEquals
argument_list|(
literal|"Did not set the right KeyWord header"
argument_list|,
literal|"KEY"
argument_list|,
name|httppost
operator|.
name|getResponseHeader
argument_list|(
literal|"KeyWord"
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFilters ()
specifier|public
name|void
name|testFilters
parameter_list|()
throws|throws
name|Exception
block|{
name|sendRequestAndVerify
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/testFilters"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Filter
argument_list|>
name|filters
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|filters
operator|.
name|add
argument_list|(
operator|new
name|MyTestFilter
argument_list|()
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myFilters"
argument_list|,
name|filters
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
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
comment|// Test the filter list options
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}/testFilters?filtersRef=myFilters&filterInit.keyWord=KEY"
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
comment|// The other form date can be get from the message header
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

