begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/*  * Camel Api Route test generated by camel-component-util-maven-plugin  * Generated on: Wed Jul 09 19:57:11 PDT 2014  */
end_comment

begin_package
DECL|package|org.apache.camel.component.linkedin
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Test class for {@link org.apache.camel.component.linkedin.api.SearchResource} APIs.  */
end_comment

begin_class
DECL|class|SearchResourceIntegrationTest
specifier|public
class|class
name|SearchResourceIntegrationTest
extends|extends
name|AbstractLinkedInTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SearchResourceIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PATH_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PREFIX
init|=
literal|"search"
decl_stmt|;
annotation|@
name|Test
DECL|method|testSearchCompanies ()
specifier|public
name|void
name|testSearchCompanies
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.fields"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.keywords"
argument_list|,
literal|"linkedin"
argument_list|)
expr_stmt|;
comment|// all fields are nullable, and fields defaults to ""
comment|/*         // parameter type is String         headers.put("CamelLinkedIn.hq_only", null);         // parameter type is String         headers.put("CamelLinkedIn.facet", null);         // parameter type is String         headers.put("CamelLinkedIn.facets", null);         // parameter type is Long         headers.put("CamelLinkedIn.start", null);         // parameter type is Long         headers.put("CamelLinkedIn.count", null);         // parameter type is String         headers.put("CamelLinkedIn.sort", null); */
specifier|final
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
name|CompanySearch
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://SEARCHCOMPANIES"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"searchCompanies result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"searchCompanies: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
argument_list|(
literal|"Requires vetted API Access Program"
argument_list|)
annotation|@
name|Test
DECL|method|testSearchJobs ()
specifier|public
name|void
name|testSearchJobs
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.fields"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
comment|/*         // parameter type is String         headers.put("CamelLinkedIn.keywords", null);         // parameter type is String         headers.put("CamelLinkedIn.company_name", null);         // parameter type is String         headers.put("CamelLinkedIn.job_title", null);         // parameter type is String         headers.put("CamelLinkedIn.country_code", null);         // parameter type is String         headers.put("CamelLinkedIn.postal_code", null);         // parameter type is org.apache.camel.component.linkedin.api.model.Distance         headers.put("CamelLinkedIn.distance", null);         // parameter type is String         headers.put("CamelLinkedIn.facet", null);         // parameter type is String         headers.put("CamelLinkedIn.facets", null);         // parameter type is Long         headers.put("CamelLinkedIn.start", null);         // parameter type is Long         headers.put("CamelLinkedIn.count", null);         // parameter type is String         headers.put("CamelLinkedIn.sort", null); */
specifier|final
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
name|JobSearch
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://SEARCHJOBS"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"searchJobs result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"searchJobs: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
argument_list|(
literal|"Requires vetted API Access Program"
argument_list|)
annotation|@
name|Test
DECL|method|testSearchPeople ()
specifier|public
name|void
name|testSearchPeople
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.fields"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
comment|/*         // parameter type is String         headers.put("CamelLinkedIn.keywords", null);         // parameter type is String         headers.put("CamelLinkedIn.first_name", null);         // parameter type is String         headers.put("CamelLinkedIn.last_name", null);         // parameter type is String         headers.put("CamelLinkedIn.company_name", null);         // parameter type is String         headers.put("CamelLinkedIn.current_company", null);         // parameter type is String         headers.put("CamelLinkedIn.title", null);         // parameter type is String         headers.put("CamelLinkedIn.current_title", null);         // parameter type is String         headers.put("CamelLinkedIn.school_name", null);         // parameter type is String         headers.put("CamelLinkedIn.current_school", null);         // parameter type is String         headers.put("CamelLinkedIn.country_code", null);         // parameter type is String         headers.put("CamelLinkedIn.postal_code", null);         // parameter type is org.apache.camel.component.linkedin.api.model.Distance         headers.put("CamelLinkedIn.distance", null);         // parameter type is String         headers.put("CamelLinkedIn.facet", null);         // parameter type is String         headers.put("CamelLinkedIn.facets", null);         // parameter type is Long         headers.put("CamelLinkedIn.start", null);         // parameter type is Long         headers.put("CamelLinkedIn.count", null);         // parameter type is String         headers.put("CamelLinkedIn.sort", null); */
specifier|final
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
name|PeopleSearch
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://SEARCHPEOPLE"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"searchPeople result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"searchPeople: "
operator|+
name|result
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// test route for searchCompanies
name|from
argument_list|(
literal|"direct://SEARCHCOMPANIES"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/searchCompanies"
argument_list|)
expr_stmt|;
comment|// test route for searchJobs
name|from
argument_list|(
literal|"direct://SEARCHJOBS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/searchJobs"
argument_list|)
expr_stmt|;
comment|// test route for searchPeople
name|from
argument_list|(
literal|"direct://SEARCHPEOPLE"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/searchPeople"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

