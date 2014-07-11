begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.linkedin.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
operator|.
name|api
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|linkedin
operator|.
name|api
operator|.
name|model
operator|.
name|JobSuggestions
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
name|linkedin
operator|.
name|api
operator|.
name|model
operator|.
name|Person
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

begin_comment
comment|/**  * Integration test for {@link PeopleResource}.  */
end_comment

begin_class
DECL|class|PeopleResourceIntegrationTest
specifier|public
class|class
name|PeopleResourceIntegrationTest
extends|extends
name|AbstractResourceIntegrationTest
block|{
DECL|field|peopleResource
specifier|private
specifier|static
name|PeopleResource
name|peopleResource
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|beforeClass ()
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
throws|throws
name|Exception
block|{
name|AbstractResourceIntegrationTest
operator|.
name|beforeClass
argument_list|()
expr_stmt|;
specifier|final
name|Class
argument_list|<
name|PeopleResource
argument_list|>
name|resourceClass
init|=
name|PeopleResource
operator|.
name|class
decl_stmt|;
name|PeopleResourceIntegrationTest
operator|.
name|peopleResource
operator|=
name|getResource
argument_list|(
name|resourceClass
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetPerson ()
specifier|public
name|void
name|testGetPerson
parameter_list|()
throws|throws
name|Exception
block|{
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
specifier|final
name|Person
name|person
init|=
name|peopleResource
operator|.
name|getPerson
argument_list|(
literal|":(id)"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|person
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|person
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getPerson result: "
operator|+
name|person
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|LinkedInException
operator|.
name|class
argument_list|)
DECL|method|testLinkedInError ()
specifier|public
name|void
name|testLinkedInError
parameter_list|()
throws|throws
name|Exception
block|{
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|peopleResource
operator|.
name|getPerson
argument_list|(
literal|"bad_fields_selector"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
argument_list|(
literal|"CXF swallows application exceptions from ClientResponseFilters"
argument_list|)
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|LinkedInException
operator|.
name|class
argument_list|)
DECL|method|testLinkedInException ()
specifier|public
name|void
name|testLinkedInException
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|peopleResource
operator|.
name|getPerson
argument_list|(
literal|"bad_fields_selector"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LinkedInException
name|e
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|e
operator|.
name|getError
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getPerson error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
annotation|@
name|Test
DECL|method|testOAuthTokenRefresh ()
specifier|public
name|void
name|testOAuthTokenRefresh
parameter_list|()
throws|throws
name|Exception
block|{
name|peopleResource
operator|.
name|getPerson
argument_list|(
literal|""
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// mark OAuth token as expired
specifier|final
name|OAuthToken
name|oAuthToken
init|=
name|requestFilter
operator|.
name|getOAuthToken
argument_list|()
decl_stmt|;
name|oAuthToken
operator|.
name|setExpiryTime
argument_list|(
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|peopleResource
operator|.
name|getPerson
argument_list|(
literal|""
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetSuggestedJobs ()
specifier|public
name|void
name|testGetSuggestedJobs
parameter_list|()
throws|throws
name|Exception
block|{
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
specifier|final
name|JobSuggestions
name|suggestedJobs
init|=
name|peopleResource
operator|.
name|getSuggestedJobs
argument_list|(
name|DEFAULT_FIELDS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|suggestedJobs
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Suggested Jobs "
operator|+
name|suggestedJobs
operator|.
name|getJobs
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

