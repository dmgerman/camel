begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo4
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelExecutionException
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
name|olingo4
operator|.
name|api
operator|.
name|batch
operator|.
name|Olingo4BatchChangeRequest
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
name|olingo4
operator|.
name|api
operator|.
name|batch
operator|.
name|Olingo4BatchQueryRequest
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
name|olingo4
operator|.
name|api
operator|.
name|batch
operator|.
name|Olingo4BatchRequest
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
name|olingo4
operator|.
name|api
operator|.
name|batch
operator|.
name|Olingo4BatchResponse
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
name|olingo4
operator|.
name|api
operator|.
name|batch
operator|.
name|Operation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|ClientComplexValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|ClientEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|ClientEntitySet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|ClientPrimitiveValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|ClientServiceDocument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edm
operator|.
name|Edm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|ex
operator|.
name|ODataError
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|http
operator|.
name|HttpStatusCode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|uri
operator|.
name|queryoption
operator|.
name|SystemQueryOptionKind
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
comment|/**  * Test class for {@link org.apache.camel.component.olingo4.api.Olingo4App}  * APIs.  *<p>  * The integration test runs against using the sample OData 4.0 remote TripPin  * service published on http://services.odata.org/TripPinRESTierService.  *</p>  */
end_comment

begin_class
DECL|class|Olingo4ComponentTest
specifier|public
class|class
name|Olingo4ComponentTest
extends|extends
name|AbstractOlingo4TestSupport
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
name|Olingo4ComponentTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PEOPLE
specifier|private
specifier|static
specifier|final
name|String
name|PEOPLE
init|=
literal|"People"
decl_stmt|;
DECL|field|TEST_PEOPLE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_PEOPLE
init|=
literal|"People('russellwhyte')"
decl_stmt|;
DECL|field|TEST_CREATE_KEY
specifier|private
specifier|static
specifier|final
name|String
name|TEST_CREATE_KEY
init|=
literal|"'lewisblack'"
decl_stmt|;
DECL|field|TEST_CREATE_PEOPLE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_CREATE_PEOPLE
init|=
name|PEOPLE
operator|+
literal|"("
operator|+
name|TEST_CREATE_KEY
operator|+
literal|")"
decl_stmt|;
DECL|field|TEST_CREATE_RESOURCE_CONTENT_ID
specifier|private
specifier|static
specifier|final
name|String
name|TEST_CREATE_RESOURCE_CONTENT_ID
init|=
literal|"1"
decl_stmt|;
DECL|field|TEST_UPDATE_RESOURCE_CONTENT_ID
specifier|private
specifier|static
specifier|final
name|String
name|TEST_UPDATE_RESOURCE_CONTENT_ID
init|=
literal|"2"
decl_stmt|;
DECL|field|TEST_CREATE_JSON
specifier|private
specifier|static
specifier|final
name|String
name|TEST_CREATE_JSON
init|=
literal|"{\n"
operator|+
literal|"  \"UserName\": \"lewisblack\",\n"
operator|+
literal|"  \"FirstName\": \"Lewis\",\n"
operator|+
literal|"  \"LastName\": \"Black\"\n"
operator|+
literal|"}"
decl_stmt|;
DECL|field|TEST_UPDATE_JSON
specifier|private
specifier|static
specifier|final
name|String
name|TEST_UPDATE_JSON
init|=
literal|"{\n"
operator|+
literal|"  \"UserName\": \"lewisblack\",\n"
operator|+
literal|"  \"FirstName\": \"Lewis\",\n"
operator|+
literal|"  \"MiddleName\": \"Black\",\n"
operator|+
literal|"  \"LastName\": \"Black\"\n"
operator|+
literal|"}"
decl_stmt|;
annotation|@
name|Test
DECL|method|testRead ()
specifier|public
name|void
name|testRead
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
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Read metadata ($metadata) object
specifier|final
name|Edm
name|metadata
init|=
operator|(
name|Edm
operator|)
name|requestBodyAndHeaders
argument_list|(
literal|"direct://readmetadata"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|metadata
operator|.
name|getSchemas
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Read service document object
specifier|final
name|ClientServiceDocument
name|document
init|=
operator|(
name|ClientServiceDocument
operator|)
name|requestBodyAndHeaders
argument_list|(
literal|"direct://readdocument"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|document
operator|.
name|getEntitySets
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Service document has {} entity sets"
argument_list|,
name|document
operator|.
name|getEntitySets
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Read entity set of the People object
specifier|final
name|ClientEntitySet
name|entities
init|=
operator|(
name|ClientEntitySet
operator|)
name|requestBodyAndHeaders
argument_list|(
literal|"direct://readentities"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|entities
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|entities
operator|.
name|getEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Read object count with query options passed through header
specifier|final
name|Long
name|count
init|=
operator|(
name|Long
operator|)
name|requestBodyAndHeaders
argument_list|(
literal|"direct://readcount"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|count
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ClientPrimitiveValue
name|value
init|=
operator|(
name|ClientPrimitiveValue
operator|)
name|requestBodyAndHeaders
argument_list|(
literal|"direct://readvalue"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Client value \"{}\" has type {}"
argument_list|,
name|value
argument_list|,
name|value
operator|.
name|getTypeName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Male"
argument_list|,
name|value
operator|.
name|asPrimitive
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ClientPrimitiveValue
name|singleProperty
init|=
operator|(
name|ClientPrimitiveValue
operator|)
name|requestBodyAndHeaders
argument_list|(
literal|"direct://readsingleprop"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|singleProperty
operator|.
name|isPrimitive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"San Francisco International Airport"
argument_list|,
name|singleProperty
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ClientComplexValue
name|complexProperty
init|=
operator|(
name|ClientComplexValue
operator|)
name|requestBodyAndHeaders
argument_list|(
literal|"direct://readcomplexprop"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|complexProperty
operator|.
name|isComplex
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"San Francisco"
argument_list|,
name|complexProperty
operator|.
name|get
argument_list|(
literal|"City"
argument_list|)
operator|.
name|getComplexValue
argument_list|()
operator|.
name|get
argument_list|(
literal|"Name"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ClientEntity
name|entity
init|=
operator|(
name|ClientEntity
operator|)
name|requestBodyAndHeaders
argument_list|(
literal|"direct://readentitybyid"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Russell"
argument_list|,
name|entity
operator|.
name|getProperty
argument_list|(
literal|"FirstName"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ClientEntity
name|unbFuncReturn
init|=
operator|(
name|ClientEntity
operator|)
name|requestBodyAndHeaders
argument_list|(
literal|"direct://callunboundfunction"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|unbFuncReturn
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadWithFilter ()
specifier|public
name|void
name|testReadWithFilter
parameter_list|()
block|{
comment|// Read entity set with filter of the Airports object
specifier|final
name|ClientEntitySet
name|entities
init|=
operator|(
name|ClientEntitySet
operator|)
name|requestBody
argument_list|(
literal|"direct://readwithfilter"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|entities
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|entities
operator|.
name|getEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateUpdateDelete ()
specifier|public
name|void
name|testCreateUpdateDelete
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|ClientEntity
name|clientEntity
init|=
name|createEntity
argument_list|()
decl_stmt|;
name|ClientEntity
name|entity
init|=
name|requestBody
argument_list|(
literal|"direct://create-entity"
argument_list|,
name|clientEntity
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Lewis"
argument_list|,
name|entity
operator|.
name|getProperty
argument_list|(
literal|"FirstName"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|entity
operator|.
name|getProperty
argument_list|(
literal|"MiddleName"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// update
name|clientEntity
operator|.
name|getProperties
argument_list|()
operator|.
name|add
argument_list|(
name|objFactory
operator|.
name|newPrimitiveProperty
argument_list|(
literal|"MiddleName"
argument_list|,
name|objFactory
operator|.
name|newPrimitiveValueBuilder
argument_list|()
operator|.
name|buildString
argument_list|(
literal|"Lewis"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|HttpStatusCode
name|status
init|=
name|requestBody
argument_list|(
literal|"direct://update-entity"
argument_list|,
name|clientEntity
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Update status"
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Update status"
argument_list|,
name|HttpStatusCode
operator|.
name|NO_CONTENT
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|status
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Update entity status: {}"
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// delete
name|status
operator|=
name|requestBody
argument_list|(
literal|"direct://delete-entity"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Delete status"
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Delete status"
argument_list|,
name|HttpStatusCode
operator|.
name|NO_CONTENT
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|status
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Delete status: {}"
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// check for delete
try|try
block|{
name|requestBody
argument_list|(
literal|"direct://read-deleted-entity"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Resource Not Found [HTTP/1.1 404 Not Found]"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testCreateUpdateDeleteFromJson ()
specifier|public
name|void
name|testCreateUpdateDeleteFromJson
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientEntity
name|entity
init|=
name|requestBody
argument_list|(
literal|"direct://create-entity"
argument_list|,
name|TEST_CREATE_JSON
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Lewis"
argument_list|,
name|entity
operator|.
name|getProperty
argument_list|(
literal|"FirstName"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Black"
argument_list|,
name|entity
operator|.
name|getProperty
argument_list|(
literal|"LastName"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"lewisblack"
argument_list|,
name|entity
operator|.
name|getProperty
argument_list|(
literal|"UserName"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|entity
operator|.
name|getProperty
argument_list|(
literal|"MiddleName"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// update
name|HttpStatusCode
name|status
init|=
name|requestBody
argument_list|(
literal|"direct://update-entity"
argument_list|,
name|TEST_UPDATE_JSON
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Update status"
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Update status"
argument_list|,
name|HttpStatusCode
operator|.
name|NO_CONTENT
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|status
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Update entity status: {}"
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// delete
name|status
operator|=
name|requestBody
argument_list|(
literal|"direct://delete-entity"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Delete status"
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Delete status"
argument_list|,
name|HttpStatusCode
operator|.
name|NO_CONTENT
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|status
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Delete status: {}"
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// check for delete
try|try
block|{
name|requestBody
argument_list|(
literal|"direct://read-deleted-entity"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Resource Not Found [HTTP/1.1 404 Not Found]"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createEntity ()
specifier|private
name|ClientEntity
name|createEntity
parameter_list|()
block|{
name|ClientEntity
name|clientEntity
init|=
name|objFactory
operator|.
name|newEntity
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|clientEntity
operator|.
name|getProperties
argument_list|()
operator|.
name|add
argument_list|(
name|objFactory
operator|.
name|newPrimitiveProperty
argument_list|(
literal|"UserName"
argument_list|,
name|objFactory
operator|.
name|newPrimitiveValueBuilder
argument_list|()
operator|.
name|buildString
argument_list|(
literal|"lewisblack"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|clientEntity
operator|.
name|getProperties
argument_list|()
operator|.
name|add
argument_list|(
name|objFactory
operator|.
name|newPrimitiveProperty
argument_list|(
literal|"FirstName"
argument_list|,
name|objFactory
operator|.
name|newPrimitiveValueBuilder
argument_list|()
operator|.
name|buildString
argument_list|(
literal|"Lewis"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|clientEntity
operator|.
name|getProperties
argument_list|()
operator|.
name|add
argument_list|(
name|objFactory
operator|.
name|newPrimitiveProperty
argument_list|(
literal|"LastName"
argument_list|,
name|objFactory
operator|.
name|newPrimitiveValueBuilder
argument_list|()
operator|.
name|buildString
argument_list|(
literal|"Black"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|clientEntity
return|;
block|}
annotation|@
name|Test
DECL|method|testBatch ()
specifier|public
name|void
name|testBatch
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|List
argument_list|<
name|Olingo4BatchRequest
argument_list|>
name|batchParts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// 1. Edm query
name|batchParts
operator|.
name|add
argument_list|(
name|Olingo4BatchQueryRequest
operator|.
name|resourcePath
argument_list|(
name|Constants
operator|.
name|METADATA
argument_list|)
operator|.
name|resourceUri
argument_list|(
name|TEST_SERVICE_BASE_URL
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
comment|// 2. Read entities
name|batchParts
operator|.
name|add
argument_list|(
name|Olingo4BatchQueryRequest
operator|.
name|resourcePath
argument_list|(
name|PEOPLE
argument_list|)
operator|.
name|resourceUri
argument_list|(
name|TEST_SERVICE_BASE_URL
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
comment|// 3. Read entity
name|batchParts
operator|.
name|add
argument_list|(
name|Olingo4BatchQueryRequest
operator|.
name|resourcePath
argument_list|(
name|TEST_PEOPLE
argument_list|)
operator|.
name|resourceUri
argument_list|(
name|TEST_SERVICE_BASE_URL
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
comment|// 4. Read with $top
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParams
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|queryParams
operator|.
name|put
argument_list|(
name|SystemQueryOptionKind
operator|.
name|TOP
operator|.
name|toString
argument_list|()
argument_list|,
literal|"5"
argument_list|)
expr_stmt|;
name|batchParts
operator|.
name|add
argument_list|(
name|Olingo4BatchQueryRequest
operator|.
name|resourcePath
argument_list|(
name|PEOPLE
argument_list|)
operator|.
name|resourceUri
argument_list|(
name|TEST_SERVICE_BASE_URL
argument_list|)
operator|.
name|queryParams
argument_list|(
name|queryParams
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
comment|// 5. Create entity
name|ClientEntity
name|clientEntity
init|=
name|createEntity
argument_list|()
decl_stmt|;
name|batchParts
operator|.
name|add
argument_list|(
name|Olingo4BatchChangeRequest
operator|.
name|resourcePath
argument_list|(
name|PEOPLE
argument_list|)
operator|.
name|resourceUri
argument_list|(
name|TEST_SERVICE_BASE_URL
argument_list|)
operator|.
name|contentId
argument_list|(
name|TEST_CREATE_RESOURCE_CONTENT_ID
argument_list|)
operator|.
name|operation
argument_list|(
name|Operation
operator|.
name|CREATE
argument_list|)
operator|.
name|body
argument_list|(
name|clientEntity
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
comment|// 6. Update middle name in created entry
name|clientEntity
operator|.
name|getProperties
argument_list|()
operator|.
name|add
argument_list|(
name|objFactory
operator|.
name|newPrimitiveProperty
argument_list|(
literal|"MiddleName"
argument_list|,
name|objFactory
operator|.
name|newPrimitiveValueBuilder
argument_list|()
operator|.
name|buildString
argument_list|(
literal|"Lewis"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|batchParts
operator|.
name|add
argument_list|(
name|Olingo4BatchChangeRequest
operator|.
name|resourcePath
argument_list|(
name|TEST_CREATE_PEOPLE
argument_list|)
operator|.
name|resourceUri
argument_list|(
name|TEST_SERVICE_BASE_URL
argument_list|)
operator|.
name|contentId
argument_list|(
name|TEST_UPDATE_RESOURCE_CONTENT_ID
argument_list|)
operator|.
name|operation
argument_list|(
name|Operation
operator|.
name|UPDATE
argument_list|)
operator|.
name|body
argument_list|(
name|clientEntity
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
comment|// 7. Delete entity
name|batchParts
operator|.
name|add
argument_list|(
name|Olingo4BatchChangeRequest
operator|.
name|resourcePath
argument_list|(
name|TEST_CREATE_PEOPLE
argument_list|)
operator|.
name|resourceUri
argument_list|(
name|TEST_SERVICE_BASE_URL
argument_list|)
operator|.
name|operation
argument_list|(
name|Operation
operator|.
name|DELETE
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
comment|// 8. Read deleted entity to verify delete
name|batchParts
operator|.
name|add
argument_list|(
name|Olingo4BatchQueryRequest
operator|.
name|resourcePath
argument_list|(
name|TEST_CREATE_PEOPLE
argument_list|)
operator|.
name|resourceUri
argument_list|(
name|TEST_SERVICE_BASE_URL
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
comment|// execute batch request
specifier|final
name|List
argument_list|<
name|Olingo4BatchResponse
argument_list|>
name|responseParts
init|=
name|requestBody
argument_list|(
literal|"direct://batch"
argument_list|,
name|batchParts
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Batch response"
argument_list|,
name|responseParts
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Batch responses expected"
argument_list|,
literal|8
argument_list|,
name|responseParts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Edm
name|edm
init|=
operator|(
name|Edm
operator|)
name|responseParts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|edm
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Edm entity sets: {}"
argument_list|,
name|edm
operator|.
name|getEntityContainer
argument_list|()
operator|.
name|getEntitySets
argument_list|()
argument_list|)
expr_stmt|;
name|ClientEntitySet
name|entitySet
init|=
operator|(
name|ClientEntitySet
operator|)
name|responseParts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|entitySet
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Read entities: {}"
argument_list|,
name|entitySet
operator|.
name|getEntities
argument_list|()
argument_list|)
expr_stmt|;
name|clientEntity
operator|=
operator|(
name|ClientEntity
operator|)
name|responseParts
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getBody
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|clientEntity
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Read entiry properties: {}"
argument_list|,
name|clientEntity
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|ClientEntitySet
name|entitySetWithTop
init|=
operator|(
name|ClientEntitySet
operator|)
name|responseParts
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|entitySetWithTop
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|entitySetWithTop
operator|.
name|getEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Read entities with $top=5: {}"
argument_list|,
name|entitySet
operator|.
name|getEntities
argument_list|()
argument_list|)
expr_stmt|;
name|clientEntity
operator|=
operator|(
name|ClientEntity
operator|)
name|responseParts
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getBody
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|clientEntity
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Created entity: {}"
argument_list|,
name|clientEntity
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|statusCode
init|=
name|responseParts
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|.
name|getStatusCode
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatusCode
operator|.
name|NO_CONTENT
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|statusCode
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Update MdiddleName status: {}"
argument_list|,
name|statusCode
argument_list|)
expr_stmt|;
name|statusCode
operator|=
name|responseParts
operator|.
name|get
argument_list|(
literal|6
argument_list|)
operator|.
name|getStatusCode
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpStatusCode
operator|.
name|NO_CONTENT
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|statusCode
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Delete entity status: {}"
argument_list|,
name|statusCode
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpStatusCode
operator|.
name|NOT_FOUND
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|responseParts
operator|.
name|get
argument_list|(
literal|7
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ODataError
name|error
init|=
operator|(
name|ODataError
operator|)
name|responseParts
operator|.
name|get
argument_list|(
literal|7
argument_list|)
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|error
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Read deleted entity error: {}"
argument_list|,
name|error
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|testEndpointHttpHeaders ()
specifier|public
name|void
name|testEndpointHttpHeaders
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|ClientEntity
name|entity
init|=
operator|(
name|ClientEntity
operator|)
name|requestBodyAndHeaders
argument_list|(
literal|"direct://read-etag"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:check-etag-header"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|responseHttpHeaders
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelOlingo4.responseHttpHeaders"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|responseHttpHeaders
operator|.
name|get
argument_list|(
literal|"ETag"
argument_list|)
argument_list|,
name|entity
operator|.
name|getETag
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|endpointHttpHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|endpointHttpHeaders
operator|.
name|put
argument_list|(
literal|"If-Match"
argument_list|,
name|entity
operator|.
name|getETag
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelOlingo4.endpointHttpHeaders"
argument_list|,
name|endpointHttpHeaders
argument_list|)
expr_stmt|;
name|requestBodyAndHeaders
argument_list|(
literal|"direct://delete-with-etag"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
comment|// check for deleted entity with ETag
try|try
block|{
name|requestBody
argument_list|(
literal|"direct://read-etag"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertStringContains
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"The request resource is not found."
argument_list|)
expr_stmt|;
block|}
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
comment|// test routes for read
name|from
argument_list|(
literal|"direct://readmetadata"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://read/$metadata"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://readdocument"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://read/"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://readentities"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://read/People?$top=5&$orderby=FirstName asc"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://readcount"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://read/People/$count"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://readvalue"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://read/People('russellwhyte')/Gender/$value"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://readsingleprop"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://read/Airports('KSFO')/Name"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://readcomplexprop"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://read/Airports('KSFO')/Location"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://readentitybyid"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://read/People('russellwhyte')"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://readwithfilter"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://read/Airports?$filter=Name eq 'San Francisco International Airport'"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://callunboundfunction"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://read/GetNearestAirport(lat=33,lon=-118)"
argument_list|)
expr_stmt|;
comment|// test route for create individual entity
name|from
argument_list|(
literal|"direct://create-entity"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://create/People"
argument_list|)
expr_stmt|;
comment|// test route for update
name|from
argument_list|(
literal|"direct://update-entity"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://update/People('lewisblack')"
argument_list|)
expr_stmt|;
comment|// test route for delete
name|from
argument_list|(
literal|"direct://delete-entity"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://delete/People('lewisblack')"
argument_list|)
expr_stmt|;
comment|// test route for delete
name|from
argument_list|(
literal|"direct://read-deleted-entity"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://delete/People('lewisblack')"
argument_list|)
expr_stmt|;
comment|// test route for batch
name|from
argument_list|(
literal|"direct://batch"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://batch"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://read-etag"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://read/Airlines('AA')"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:check-etag-header"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://delete-with-etag"
argument_list|)
operator|.
name|to
argument_list|(
literal|"olingo4://delete/Airlines('AA')"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

