begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|HttpHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
import|import
name|org
operator|.
name|junit
operator|.
name|experimental
operator|.
name|categories
operator|.
name|Category
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_import
import|import
name|okhttp3
operator|.
name|HttpUrl
import|;
end_import

begin_import
import|import
name|okhttp3
operator|.
name|mockwebserver
operator|.
name|Dispatcher
import|;
end_import

begin_import
import|import
name|okhttp3
operator|.
name|mockwebserver
operator|.
name|MockResponse
import|;
end_import

begin_import
import|import
name|okhttp3
operator|.
name|mockwebserver
operator|.
name|MockWebServer
import|;
end_import

begin_import
import|import
name|okhttp3
operator|.
name|mockwebserver
operator|.
name|RecordedRequest
import|;
end_import

begin_class
annotation|@
name|Category
argument_list|(
name|Standalone
operator|.
name|class
argument_list|)
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
DECL|class|RawPayloadTest
specifier|public
class|class
name|RawPayloadTest
extends|extends
name|AbstractSalesforceTestBase
block|{
DECL|field|OAUTH2_TOKEN_PATH
specifier|private
specifier|static
specifier|final
name|String
name|OAUTH2_TOKEN_PATH
init|=
literal|"/services/oauth2/token"
decl_stmt|;
DECL|field|XML_RESPONSE
specifier|private
specifier|static
specifier|final
name|String
name|XML_RESPONSE
init|=
literal|"<response/>"
decl_stmt|;
DECL|field|JSON_RESPONSE
specifier|private
specifier|static
specifier|final
name|String
name|JSON_RESPONSE
init|=
literal|"{ \"response\" : \"mock\" }"
decl_stmt|;
DECL|field|loginUrl
specifier|private
specifier|static
name|HttpUrl
name|loginUrl
decl_stmt|;
DECL|field|server
specifier|private
specifier|static
name|MockWebServer
name|server
decl_stmt|;
annotation|@
name|Parameter
DECL|field|format
specifier|public
specifier|static
name|String
name|format
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
literal|1
argument_list|)
DECL|field|endpointUri
specifier|public
specifier|static
name|String
name|endpointUri
decl_stmt|;
DECL|field|lastFormat
specifier|private
specifier|static
name|String
name|lastFormat
decl_stmt|;
DECL|field|expectedResponse
specifier|private
specifier|static
name|String
name|expectedResponse
decl_stmt|;
DECL|field|requestBody
specifier|private
specifier|static
name|String
name|requestBody
decl_stmt|;
DECL|field|headers
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
decl_stmt|;
annotation|@
name|Override
DECL|method|createComponent ()
specifier|protected
name|void
name|createComponent
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create the component
name|SalesforceComponent
name|component
init|=
operator|new
name|SalesforceComponent
argument_list|()
decl_stmt|;
specifier|final
name|SalesforceEndpointConfig
name|config
init|=
operator|new
name|SalesforceEndpointConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setApiVersion
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"apiVersion"
argument_list|,
name|salesforceApiVersionToUse
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|setConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|SalesforceLoginConfig
name|dummyLoginConfig
init|=
operator|new
name|SalesforceLoginConfig
argument_list|()
decl_stmt|;
name|dummyLoginConfig
operator|.
name|setClientId
argument_list|(
literal|"ignored"
argument_list|)
expr_stmt|;
name|dummyLoginConfig
operator|.
name|setClientSecret
argument_list|(
literal|"ignored"
argument_list|)
expr_stmt|;
name|dummyLoginConfig
operator|.
name|setRefreshToken
argument_list|(
literal|"ignored"
argument_list|)
expr_stmt|;
name|dummyLoginConfig
operator|.
name|setLoginUrl
argument_list|(
name|loginUrl
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|component
operator|.
name|setLoginConfig
argument_list|(
name|dummyLoginConfig
argument_list|)
expr_stmt|;
comment|// add it to context
name|context
argument_list|()
operator|.
name|addComponent
argument_list|(
literal|"salesforce"
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|shutDownServer ()
specifier|public
specifier|static
name|void
name|shutDownServer
parameter_list|()
throws|throws
name|IOException
block|{
comment|// shutdown mock server
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|BeforeClass
DECL|method|startServer ()
specifier|public
specifier|static
name|void
name|startServer
parameter_list|()
throws|throws
name|IOException
block|{
comment|// create mock server
name|server
operator|=
operator|new
name|MockWebServer
argument_list|()
expr_stmt|;
name|server
operator|.
name|setDispatcher
argument_list|(
operator|new
name|Dispatcher
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|MockResponse
name|dispatch
parameter_list|(
name|RecordedRequest
name|recordedRequest
parameter_list|)
throws|throws
name|InterruptedException
block|{
if|if
condition|(
name|recordedRequest
operator|.
name|getPath
argument_list|()
operator|.
name|equals
argument_list|(
name|OAUTH2_TOKEN_PATH
argument_list|)
condition|)
block|{
return|return
operator|new
name|MockResponse
argument_list|()
operator|.
name|setResponseCode
argument_list|(
literal|200
argument_list|)
operator|.
name|setBody
argument_list|(
literal|"{ \"access_token\": \"mock_token\", \"instance_url\": \""
operator|+
name|loginUrl
operator|+
literal|"\"}"
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|MockResponse
argument_list|()
operator|.
name|setResponseCode
argument_list|(
literal|200
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HttpHeader
operator|.
name|CONTENT_TYPE
operator|.
name|toString
argument_list|()
argument_list|,
name|recordedRequest
operator|.
name|getHeader
argument_list|(
name|HttpHeader
operator|.
name|CONTENT_TYPE
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|(
literal|"XML"
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|?
name|XML_RESPONSE
else|:
name|JSON_RESPONSE
argument_list|)
return|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
comment|// start the server
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|loginUrl
operator|=
name|server
operator|.
name|url
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|setupRequestResponse ()
specifier|public
name|void
name|setupRequestResponse
parameter_list|()
block|{
if|if
condition|(
operator|!
name|format
operator|.
name|equals
argument_list|(
name|lastFormat
argument_list|)
condition|)
block|{
comment|// expected response and test request
specifier|final
name|boolean
name|isXml
init|=
literal|"XML"
operator|.
name|equals
argument_list|(
name|format
argument_list|)
decl_stmt|;
name|expectedResponse
operator|=
name|isXml
condition|?
name|XML_RESPONSE
else|:
name|JSON_RESPONSE
expr_stmt|;
if|if
condition|(
name|isXml
condition|)
block|{
name|requestBody
operator|=
literal|"<request/>"
expr_stmt|;
block|}
else|else
block|{
name|requestBody
operator|=
literal|"{ \"request\" : \"mock\" }"
expr_stmt|;
block|}
name|headers
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"sObjectId"
argument_list|,
literal|"mockId"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"sObjectIdValue"
argument_list|,
literal|"mockIdValue"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|"mockId"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|SalesforceEndpointConfig
operator|.
name|APEX_QUERY_PARAM_PREFIX
operator|+
literal|"id"
argument_list|,
literal|"mockId"
argument_list|)
expr_stmt|;
name|lastFormat
operator|=
name|format
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testRestApi ()
specifier|public
name|void
name|testRestApi
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|responseBody
init|=
name|template
argument_list|()
operator|.
name|requestBodyAndHeaders
argument_list|(
name|endpointUri
argument_list|,
name|requestBody
argument_list|,
name|headers
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Null response for endpoint "
operator|+
name|endpointUri
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected response for endpoint "
operator|+
name|endpointUri
argument_list|,
name|expectedResponse
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doCreateRouteBuilder ()
specifier|protected
name|RouteBuilder
name|doCreateRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create test route
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
block|{
comment|// testGetVersion
name|from
argument_list|(
literal|"direct:getVersions"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:getVersions?rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testGetResources
name|from
argument_list|(
literal|"direct:getResources"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:getResources?rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testGetGlobalObjects
name|from
argument_list|(
literal|"direct:getGlobalObjects"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:getGlobalObjects?rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testGetBasicInfo
name|from
argument_list|(
literal|"direct:getBasicInfo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:getBasicInfo?sObjectName=Merchandise__c&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testGetDescription
name|from
argument_list|(
literal|"direct:getDescription"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:getDescription?sObjectName=Merchandise__c&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testGetSObject
name|from
argument_list|(
literal|"direct:getSObject"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:getSObject?sObjectName=Merchandise__c&sObjectFields=Description__c,Price__c&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testCreateSObject
name|from
argument_list|(
literal|"direct:createSObject"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:createSObject?sObjectName=Merchandise__c&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testUpdateSObject
name|from
argument_list|(
literal|"direct:updateSObject"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:updateSObject?sObjectName=Merchandise__c&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testDeleteSObject
name|from
argument_list|(
literal|"direct:deleteSObject"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:deleteSObject?sObjectName=Merchandise__c&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testGetSObjectWithId
name|from
argument_list|(
literal|"direct:getSObjectWithId"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:getSObjectWithId?sObjectName=Line_Item__c&sObjectIdName=Name&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testUpsertSObject
name|from
argument_list|(
literal|"direct:upsertSObject"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:upsertSObject?sObjectName=Line_Item__c&sObjectIdName=Name&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testDeleteSObjectWithId
name|from
argument_list|(
literal|"direct:deleteSObjectWithId"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:deleteSObjectWithId?sObjectName=Line_Item__c&sObjectIdName=Name&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testGetBlobField
name|from
argument_list|(
literal|"direct:getBlobField"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:getBlobField?sObjectName=Document&sObjectBlobFieldName=Body&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testQuery
name|from
argument_list|(
literal|"direct:query"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:query?sObjectQuery=SELECT name from Line_Item__c&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testQueryAll
name|from
argument_list|(
literal|"direct:queryAll"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:queryAll?sObjectQuery=SELECT name from Line_Item__c&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testSearch
name|from
argument_list|(
literal|"direct:search"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:search?sObjectSearch=FIND {Wee}&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
comment|// testApexCall
name|from
argument_list|(
literal|"direct:apexCallGet"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:apexCall?apexMethod=GET&apexUrl=Merchandise/{id}&sObjectName=Merchandise__c&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:apexCallGetWithId"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:apexCall/Merchandise/?apexMethod=GET&id=dummyId&rawPayload=true&format="
operator|+
name|format
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:apexCallPatch"
argument_list|)
operator|.
name|to
argument_list|(
literal|"salesforce:apexCall/Merchandise/?rawPayload=true&format="
operator|+
name|format
operator|+
literal|"&apexMethod=PATCH"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Parameters
argument_list|(
name|name
operator|=
literal|"format = {0}, endpoint = {1}"
argument_list|)
DECL|method|parameters ()
specifier|public
specifier|static
name|List
argument_list|<
name|String
index|[]
argument_list|>
name|parameters
parameter_list|()
block|{
specifier|final
name|String
index|[]
name|endpoints
init|=
block|{
literal|"direct:getVersions"
block|,
literal|"direct:getResources"
block|,
literal|"direct:getGlobalObjects"
block|,
literal|"direct:getBasicInfo"
block|,
literal|"direct:getDescription"
block|,
literal|"direct:getSObject"
block|,
literal|"direct:createSObject"
block|,
literal|"direct:updateSObject"
block|,
literal|"direct:deleteSObject"
block|,
literal|"direct:getSObjectWithId"
block|,
literal|"direct:upsertSObject"
block|,
literal|"direct:deleteSObjectWithId"
block|,
literal|"direct:getBlobField"
block|,
literal|"direct:query"
block|,
literal|"direct:queryAll"
block|,
literal|"direct:search"
block|,
literal|"direct:apexCallGet"
block|,
literal|"direct:apexCallGetWithId"
block|,
literal|"direct:apexCallPatch"
block|}
decl_stmt|;
specifier|final
name|String
index|[]
name|formats
init|=
block|{
literal|"XML"
block|,
literal|"JSON"
block|}
decl_stmt|;
return|return
name|Stream
operator|.
name|of
argument_list|(
name|formats
argument_list|)
operator|.
name|flatMap
argument_list|(
name|f
lambda|->
name|Stream
operator|.
name|of
argument_list|(
name|endpoints
argument_list|)
operator|.
name|map
argument_list|(
name|e
lambda|->
operator|new
name|String
index|[]
block|{
name|f
block|,
name|e
block|}
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

