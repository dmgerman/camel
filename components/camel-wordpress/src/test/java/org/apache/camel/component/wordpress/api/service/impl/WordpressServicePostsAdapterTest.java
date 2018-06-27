begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress.api.service.impl
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
operator|.
name|api
operator|.
name|service
operator|.
name|impl
package|;
end_package

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
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|auth
operator|.
name|WordpressBasicAuthentication
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
name|Format
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
name|PostSearchCriteria
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
name|service
operator|.
name|WordpressServicePosts
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
name|test
operator|.
name|WordpressMockServerTestSupport
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
name|test
operator|.
name|WordpressServerHttpRequestHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|not
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
name|nullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|emptyCollectionOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|greaterThan
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|WordpressServicePostsAdapterTest
specifier|public
class|class
name|WordpressServicePostsAdapterTest
extends|extends
name|WordpressMockServerTestSupport
block|{
DECL|field|servicePosts
specifier|private
specifier|static
name|WordpressServicePosts
name|servicePosts
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|before ()
specifier|public
specifier|static
name|void
name|before
parameter_list|()
block|{
name|servicePosts
operator|=
name|serviceProvider
operator|.
name|getService
argument_list|(
name|WordpressServicePosts
operator|.
name|class
argument_list|)
expr_stmt|;
name|servicePosts
operator|.
name|setWordpressAuthentication
argument_list|(
operator|new
name|WordpressBasicAuthentication
argument_list|(
name|WordpressServerHttpRequestHandler
operator|.
name|USERNAME
argument_list|,
name|WordpressServerHttpRequestHandler
operator|.
name|PASSWORD
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRetrievePost ()
specifier|public
name|void
name|testRetrievePost
parameter_list|()
block|{
specifier|final
name|Post
name|post
init|=
name|servicePosts
operator|.
name|retrieve
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|post
argument_list|,
name|not
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|post
operator|.
name|getId
argument_list|()
argument_list|,
name|is
argument_list|(
name|greaterThan
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreatePost ()
specifier|public
name|void
name|testCreatePost
parameter_list|()
block|{
specifier|final
name|Post
name|entity
init|=
operator|new
name|Post
argument_list|()
decl_stmt|;
name|entity
operator|.
name|setAuthor
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|entity
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
name|entity
operator|.
name|setContent
argument_list|(
operator|new
name|Content
argument_list|(
literal|"hello world 2"
argument_list|)
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setFormat
argument_list|(
name|Format
operator|.
name|standard
argument_list|)
expr_stmt|;
specifier|final
name|Post
name|post
init|=
name|servicePosts
operator|.
name|create
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|post
argument_list|,
name|not
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|post
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
block|}
annotation|@
name|Test
DECL|method|testListPosts ()
specifier|public
name|void
name|testListPosts
parameter_list|()
block|{
specifier|final
name|PostSearchCriteria
name|criteria
init|=
operator|new
name|PostSearchCriteria
argument_list|()
decl_stmt|;
name|criteria
operator|.
name|setPage
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|criteria
operator|.
name|setPerPage
argument_list|(
literal|10
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Post
argument_list|>
name|posts
init|=
name|servicePosts
operator|.
name|list
argument_list|(
name|criteria
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|posts
argument_list|,
name|is
argument_list|(
name|not
argument_list|(
name|emptyCollectionOf
argument_list|(
name|Post
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|posts
operator|.
name|size
argument_list|()
argument_list|,
name|is
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

