begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/*  * Camel Api Route test generated by camel-component-util-maven-plugin  * Generated on: Wed Jul 09 19:57:10 PDT 2014  */
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
operator|.
name|internal
operator|.
name|CompaniesResourceApiMethod
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
name|internal
operator|.
name|LinkedInApiCollection
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
comment|/**  * Test class for {@link org.apache.camel.component.linkedin.api.CompaniesResource} APIs.  */
end_comment

begin_class
DECL|class|CompaniesResourceIntegrationTest
specifier|public
class|class
name|CompaniesResourceIntegrationTest
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
name|CompaniesResourceIntegrationTest
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
name|LinkedInApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|CompaniesResourceApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|TEST_COMPANY_ID
specifier|private
specifier|static
specifier|final
name|Long
name|TEST_COMPANY_ID
init|=
literal|1337L
decl_stmt|;
comment|// TODO provide parameter values for addCompanyUpdateCommentAsCompany
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testAddCompanyUpdateCommentAsCompany ()
specifier|public
name|void
name|testAddCompanyUpdateCommentAsCompany
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
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.company_id"
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.update_key"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is org.apache.camel.component.linkedin.api.model.UpdateComment
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.updatecomment"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|requestBodyAndHeaders
argument_list|(
literal|"direct://ADDCOMPANYUPDATECOMMENTASCOMPANY"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
comment|// TODO provide parameter values for addShare
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testAddShare ()
specifier|public
name|void
name|testAddShare
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
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.company_id"
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
comment|// parameter type is org.apache.camel.component.linkedin.api.model.Share
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.share"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|requestBodyAndHeaders
argument_list|(
literal|"direct://ADDSHARE"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetCompanies ()
specifier|public
name|void
name|testGetCompanies
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
comment|// use defaults
comment|// parameter type is String
comment|//        headers.put("CamelLinkedIn.fields", null);
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.email_domain"
argument_list|,
literal|"linkedin.com"
argument_list|)
expr_stmt|;
comment|// parameter type is Boolean
comment|//        headers.put("CamelLinkedIn.is_company_admin", null);
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
name|Companies
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETCOMPANIES"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getCompanies result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getCompanies: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetCompanyById ()
specifier|public
name|void
name|testGetCompanyById
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
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.company_id"
argument_list|,
name|TEST_COMPANY_ID
argument_list|)
expr_stmt|;
comment|// use default value
comment|/*         // parameter type is String         headers.put("CamelLinkedIn.fields", null); */
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
name|Company
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETCOMPANYBYID"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getCompanyById result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getCompanyById: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetCompanyByName ()
specifier|public
name|void
name|testGetCompanyByName
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
literal|"CamelLinkedIn.universal_name"
argument_list|,
literal|"linkedin"
argument_list|)
expr_stmt|;
comment|// use default fields
comment|/*         // parameter type is String         headers.put("CamelLinkedIn.fields", null); */
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
name|Company
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETCOMPANYBYNAME"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getCompanyByName result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getCompanyByName: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
comment|// TODO provide parameter values for getCompanyUpdateComments
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testGetCompanyUpdateComments ()
specifier|public
name|void
name|testGetCompanyUpdateComments
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
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.company_id"
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.update_key"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.fields"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is Boolean
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.secure_urls"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
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
name|UpdateComments
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETCOMPANYUPDATECOMMENTS"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getCompanyUpdateComments result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getCompanyUpdateComments: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
comment|// TODO provide parameter values for getCompanyUpdateLikes
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testGetCompanyUpdateLikes ()
specifier|public
name|void
name|testGetCompanyUpdateLikes
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
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.company_id"
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.update_key"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.fields"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is Boolean
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.secure_urls"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
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
name|Likes
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETCOMPANYUPDATELIKES"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getCompanyUpdateLikes result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getCompanyUpdateLikes: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetCompanyUpdates ()
specifier|public
name|void
name|testGetCompanyUpdates
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
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.company_id"
argument_list|,
name|TEST_COMPANY_ID
argument_list|)
expr_stmt|;
comment|// use defaults
comment|/*         // parameter type is String         headers.put("CamelLinkedIn.fields", null);         // parameter type is org.apache.camel.component.linkedin.api.Eventtype         headers.put("CamelLinkedIn.event_type", null);         // parameter type is Long         headers.put("CamelLinkedIn.start", null);         // parameter type is Long         headers.put("CamelLinkedIn.count", null); */
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
name|Updates
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETCOMPANYUPDATES"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getCompanyUpdates result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getCompanyUpdates: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
comment|// TODO provide parameter values for getHistoricalFollowStatistics
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testGetHistoricalFollowStatistics ()
specifier|public
name|void
name|testGetHistoricalFollowStatistics
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
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.company_id"
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
comment|// parameter type is Long
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.start_timestamp"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is Long
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.end_timestamp"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is org.apache.camel.component.linkedin.api.Timegranularity
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.time_granularity"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
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
name|HistoricalFollowStatistics
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETHISTORICALFOLLOWSTATISTICS"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getHistoricalFollowStatistics result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getHistoricalFollowStatistics: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
comment|// TODO provide parameter values for getHistoricalStatusUpdateStatistics
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testGetHistoricalStatusUpdateStatistics ()
specifier|public
name|void
name|testGetHistoricalStatusUpdateStatistics
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
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.company_id"
argument_list|,
name|TEST_COMPANY_ID
argument_list|)
expr_stmt|;
comment|// parameter type is Long
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.start_timestamp"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is Long
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.end_timestamp"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is org.apache.camel.component.linkedin.api.Timegranularity
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.time_granularity"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.statistics_update_key"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
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
name|HistoricalStatusUpdateStatistics
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETHISTORICALSTATUSUPDATESTATISTICS"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getHistoricalStatusUpdateStatistics result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getHistoricalStatusUpdateStatistics: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetNumberOfFollowers ()
specifier|public
name|void
name|testGetNumberOfFollowers
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
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.company_id"
argument_list|,
name|TEST_COMPANY_ID
argument_list|)
expr_stmt|;
comment|// parameter type is java.util.List
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.geos"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is java.util.List
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.companySizes"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is java.util.List
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.jobFunc"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is java.util.List
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.industries"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is java.util.List
name|headers
operator|.
name|put
argument_list|(
literal|"CamelLinkedIn.seniorities"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
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
name|NumFollowers
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETNUMBEROFFOLLOWERS"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getNumberOfFollowers result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getNumberOfFollowers: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
comment|// TODO provide parameter values for getStatistics
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testGetStatistics ()
specifier|public
name|void
name|testGetStatistics
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using long message body for single parameter "company_id"
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
name|CompanyStatistics
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GETSTATISTICS"
argument_list|,
literal|0L
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getStatistics result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getStatistics: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIsShareEnabled ()
specifier|public
name|void
name|testIsShareEnabled
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using long message body for single parameter "company_id"
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
name|IsCompanyShareEnabled
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://ISSHAREENABLED"
argument_list|,
name|TEST_COMPANY_ID
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"isShareEnabled result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"isShareEnabled: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIsViewerShareEnabled ()
specifier|public
name|void
name|testIsViewerShareEnabled
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using long message body for single parameter "company_id"
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
name|IsCompanyShareEnabled
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://ISVIEWERSHAREENABLED"
argument_list|,
name|TEST_COMPANY_ID
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"isViewerShareEnabled result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"isViewerShareEnabled: "
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
comment|// test route for addCompanyUpdateCommentAsCompany
name|from
argument_list|(
literal|"direct://ADDCOMPANYUPDATECOMMENTASCOMPANY"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/addCompanyUpdateCommentAsCompany"
argument_list|)
expr_stmt|;
comment|// test route for addShare
name|from
argument_list|(
literal|"direct://ADDSHARE"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/addShare"
argument_list|)
expr_stmt|;
comment|// test route for getCompanies
name|from
argument_list|(
literal|"direct://GETCOMPANIES"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getCompanies"
argument_list|)
expr_stmt|;
comment|// test route for getCompanyById
name|from
argument_list|(
literal|"direct://GETCOMPANYBYID"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getCompanyById"
argument_list|)
expr_stmt|;
comment|// test route for getCompanyByName
name|from
argument_list|(
literal|"direct://GETCOMPANYBYNAME"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getCompanyByName"
argument_list|)
expr_stmt|;
comment|// test route for getCompanyUpdateComments
name|from
argument_list|(
literal|"direct://GETCOMPANYUPDATECOMMENTS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getCompanyUpdateComments"
argument_list|)
expr_stmt|;
comment|// test route for getCompanyUpdateLikes
name|from
argument_list|(
literal|"direct://GETCOMPANYUPDATELIKES"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getCompanyUpdateLikes"
argument_list|)
expr_stmt|;
comment|// test route for getCompanyUpdates
name|from
argument_list|(
literal|"direct://GETCOMPANYUPDATES"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getCompanyUpdates"
argument_list|)
expr_stmt|;
comment|// test route for getHistoricalFollowStatistics
name|from
argument_list|(
literal|"direct://GETHISTORICALFOLLOWSTATISTICS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getHistoricalFollowStatistics"
argument_list|)
expr_stmt|;
comment|// test route for getHistoricalStatusUpdateStatistics
name|from
argument_list|(
literal|"direct://GETHISTORICALSTATUSUPDATESTATISTICS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getHistoricalStatusUpdateStatistics"
argument_list|)
expr_stmt|;
comment|// test route for getNumberOfFollowers
name|from
argument_list|(
literal|"direct://GETNUMBEROFFOLLOWERS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getNumberOfFollowers"
argument_list|)
expr_stmt|;
comment|// test route for getStatistics
name|from
argument_list|(
literal|"direct://GETSTATISTICS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getStatistics?inBody=company_id"
argument_list|)
expr_stmt|;
comment|// test route for isShareEnabled
name|from
argument_list|(
literal|"direct://ISSHAREENABLED"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/isShareEnabled?inBody=company_id"
argument_list|)
expr_stmt|;
comment|// test route for isViewerShareEnabled
name|from
argument_list|(
literal|"direct://ISVIEWERSHAREENABLED"
argument_list|)
operator|.
name|to
argument_list|(
literal|"linkedin://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/isViewerShareEnabled?inBody=company_id"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

