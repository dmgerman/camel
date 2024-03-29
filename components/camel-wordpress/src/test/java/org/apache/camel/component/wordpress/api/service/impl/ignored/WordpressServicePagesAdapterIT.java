begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress.api.service.impl.ignored
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
operator|.
name|ignored
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
name|WordpressTestConstants
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
name|WordpressServiceProvider
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
name|Page
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
name|PageSearchCriteria
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
name|WordpressServicePages
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
name|Ignore
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
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Not implemented yet"
argument_list|)
DECL|class|WordpressServicePagesAdapterIT
specifier|public
class|class
name|WordpressServicePagesAdapterIT
block|{
DECL|field|servicePages
specifier|private
specifier|static
name|WordpressServicePages
name|servicePages
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
specifier|final
name|WordpressServiceProvider
name|serviceProvider
init|=
name|WordpressServiceProvider
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|serviceProvider
operator|.
name|init
argument_list|(
name|WordpressTestConstants
operator|.
name|WORDPRESS_DEMO_URL
argument_list|)
expr_stmt|;
name|servicePages
operator|=
name|serviceProvider
operator|.
name|getService
argument_list|(
name|WordpressServicePages
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRetrieve ()
specifier|public
name|void
name|testRetrieve
parameter_list|()
block|{
specifier|final
name|Page
name|page
init|=
name|servicePages
operator|.
name|retrieve
argument_list|(
literal|2
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|page
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
name|page
operator|.
name|getId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testList ()
specifier|public
name|void
name|testList
parameter_list|()
block|{
specifier|final
name|PageSearchCriteria
name|criteria
init|=
operator|new
name|PageSearchCriteria
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
literal|5
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Page
argument_list|>
name|posts
init|=
name|servicePages
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
name|Page
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
literal|5
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

