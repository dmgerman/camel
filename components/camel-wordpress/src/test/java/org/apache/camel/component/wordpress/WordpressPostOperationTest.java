begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|wordpress
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
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|WordpressConstants
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
name|wordpress
operator|.
name|api
operator|.
name|model
operator|.
name|Content
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
name|wordpress
operator|.
name|api
operator|.
name|model
operator|.
name|Post
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
name|wordpress
operator|.
name|api
operator|.
name|model
operator|.
name|PublishableStatus
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
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
import|;
end_import

begin_class
DECL|class|WordpressPostOperationTest
specifier|public
class|class
name|WordpressPostOperationTest
extends|extends
name|WordpressComponentTestSupport
block|{
DECL|method|WordpressPostOperationTest ()
specifier|public
name|WordpressPostOperationTest
parameter_list|()
block|{      }
annotation|@
name|Test
DECL|method|testPostSingleRequest ()
specifier|public
name|void
name|testPostSingleRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultSingle"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodyReceived
argument_list|()
operator|.
name|body
argument_list|(
name|Post
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPostListRequest ()
specifier|public
name|void
name|testPostListRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultList"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodyReceived
argument_list|()
operator|.
name|body
argument_list|(
name|Post
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInsertPost ()
specifier|public
name|void
name|testInsertPost
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultInsert"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodyReceived
argument_list|()
operator|.
name|body
argument_list|(
name|Post
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|Post
name|request
init|=
operator|new
name|Post
argument_list|()
decl_stmt|;
name|request
operator|.
name|setAuthor
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|request
operator|.
name|setTitle
argument_list|(
operator|new
name|Content
argument_list|(
literal|"hello from postman 2"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Post
name|response
init|=
operator|(
name|Post
operator|)
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:insertPost"
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|9
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
name|is
argument_list|(
name|PublishableStatus
operator|.
name|draft
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdatePost ()
specifier|public
name|void
name|testUpdatePost
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultUpdate"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodyReceived
argument_list|()
operator|.
name|body
argument_list|(
name|Post
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|Post
name|request
init|=
operator|new
name|Post
argument_list|()
decl_stmt|;
name|request
operator|.
name|setAuthor
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|request
operator|.
name|setTitle
argument_list|(
operator|new
name|Content
argument_list|(
literal|"hello from postman 2 - update"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Post
name|response
init|=
operator|(
name|Post
operator|)
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:updatePost"
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|9
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
name|is
argument_list|(
name|PublishableStatus
operator|.
name|draft
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getTitle
argument_list|()
operator|.
name|getRaw
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"hello from postman 2 - update"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getTitle
argument_list|()
operator|.
name|getRendered
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"hello from postman 2&#8211; update"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDeletePost ()
specifier|public
name|void
name|testDeletePost
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultDelete"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodyReceived
argument_list|()
operator|.
name|body
argument_list|(
name|Post
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|Post
name|response
init|=
operator|(
name|Post
operator|)
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:deletePost"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|9
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
name|is
argument_list|(
name|PublishableStatus
operator|.
name|trash
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
specifier|final
name|WordpressComponentConfiguration
name|configuration
init|=
operator|new
name|WordpressComponentConfiguration
argument_list|()
decl_stmt|;
specifier|final
name|WordpressComponent
name|component
init|=
operator|new
name|WordpressComponent
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setApiVersion
argument_list|(
name|WordpressConstants
operator|.
name|API_VERSION
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setUrl
argument_list|(
name|getServerBaseUrl
argument_list|()
argument_list|)
expr_stmt|;
name|component
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|addComponent
argument_list|(
literal|"wordpress"
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"wordpress:post?criteria.perPage=10&criteria.orderBy=author&criteria.categories=camel,dozer,json"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultList"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"wordpress:post?id=114913"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultSingle"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:deletePost"
argument_list|)
operator|.
name|to
argument_list|(
literal|"wordpress:post:delete?id=9&user=ben&password=password123"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultDelete"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:insertPost"
argument_list|)
operator|.
name|to
argument_list|(
literal|"wordpress:post?user=ben&password=password123"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultInsert"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:updatePost"
argument_list|)
operator|.
name|to
argument_list|(
literal|"wordpress:post?id=9&user=ben&password=password123"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultUpdate"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

