begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest.openapi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
operator|.
name|openapi
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
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|junit
operator|.
name|WireMockRule
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
name|CamelContext
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
name|RoutesBuilder
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
name|rest
operator|.
name|RestEndpoint
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
name|rest
operator|.
name|openapi
operator|.
name|RestOpenApiComponent
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
name|converter
operator|.
name|jaxb
operator|.
name|JaxbDataFormat
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|ClassRule
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
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|client
operator|.
name|WireMock
operator|.
name|aResponse
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|client
operator|.
name|WireMock
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|client
operator|.
name|WireMock
operator|.
name|get
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|client
operator|.
name|WireMock
operator|.
name|getRequestedFor
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|client
operator|.
name|WireMock
operator|.
name|post
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|client
operator|.
name|WireMock
operator|.
name|postRequestedFor
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|client
operator|.
name|WireMock
operator|.
name|urlEqualTo
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|client
operator|.
name|WireMock
operator|.
name|urlPathEqualTo
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|core
operator|.
name|WireMockConfiguration
operator|.
name|wireMockConfig
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
DECL|class|RestOpenApiComponentTest
specifier|public
class|class
name|RestOpenApiComponentTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|ClassRule
DECL|field|petstore
specifier|public
specifier|static
name|WireMockRule
name|petstore
init|=
operator|new
name|WireMockRule
argument_list|(
name|wireMockConfig
argument_list|()
operator|.
name|dynamicPort
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|NO_BODY
specifier|static
specifier|final
name|Object
name|NO_BODY
init|=
literal|null
decl_stmt|;
annotation|@
name|Parameter
DECL|field|componentName
specifier|public
name|String
name|componentName
decl_stmt|;
annotation|@
name|Before
DECL|method|resetWireMock ()
specifier|public
name|void
name|resetWireMock
parameter_list|()
block|{
name|petstore
operator|.
name|resetRequests
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldBeAddingPets ()
specifier|public
name|void
name|shouldBeAddingPets
parameter_list|()
block|{
specifier|final
name|Pet
name|pet
init|=
operator|new
name|Pet
argument_list|()
decl_stmt|;
name|pet
operator|.
name|name
operator|=
literal|"Jean-Luc Picard"
expr_stmt|;
specifier|final
name|Pet
name|created
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:addPet"
argument_list|,
name|pet
argument_list|,
name|Pet
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|created
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|14
argument_list|)
argument_list|,
name|created
operator|.
name|id
argument_list|)
expr_stmt|;
name|petstore
operator|.
name|verify
argument_list|(
name|postRequestedFor
argument_list|(
name|urlEqualTo
argument_list|(
literal|"/v2/pet"
argument_list|)
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"Accept"
argument_list|,
name|equalTo
argument_list|(
literal|"application/xml, application/json"
argument_list|)
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|equalTo
argument_list|(
literal|"application/xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldBeGettingPetsById ()
specifier|public
name|void
name|shouldBeGettingPetsById
parameter_list|()
block|{
specifier|final
name|Pet
name|pet
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:getPetById"
argument_list|,
name|NO_BODY
argument_list|,
literal|"petId"
argument_list|,
literal|14
argument_list|,
name|Pet
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pet
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|14
argument_list|)
argument_list|,
name|pet
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Olafur Eliason Arnalds"
argument_list|,
name|pet
operator|.
name|name
argument_list|)
expr_stmt|;
name|petstore
operator|.
name|verify
argument_list|(
name|getRequestedFor
argument_list|(
name|urlEqualTo
argument_list|(
literal|"/v2/pet/14"
argument_list|)
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"Accept"
argument_list|,
name|equalTo
argument_list|(
literal|"application/xml, application/json"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldBeGettingPetsByIdSpecifiedInEndpointParameters ()
specifier|public
name|void
name|shouldBeGettingPetsByIdSpecifiedInEndpointParameters
parameter_list|()
block|{
specifier|final
name|Pet
name|pet
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:getPetByIdWithEndpointParams"
argument_list|,
name|NO_BODY
argument_list|,
name|Pet
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pet
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|14
argument_list|)
argument_list|,
name|pet
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Olafur Eliason Arnalds"
argument_list|,
name|pet
operator|.
name|name
argument_list|)
expr_stmt|;
name|petstore
operator|.
name|verify
argument_list|(
name|getRequestedFor
argument_list|(
name|urlEqualTo
argument_list|(
literal|"/v2/pet/14"
argument_list|)
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"Accept"
argument_list|,
name|equalTo
argument_list|(
literal|"application/xml, application/json"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldBeGettingPetsByIdWithApiKeysInHeader ()
specifier|public
name|void
name|shouldBeGettingPetsByIdWithApiKeysInHeader
parameter_list|()
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
name|headers
operator|.
name|put
argument_list|(
literal|"petId"
argument_list|,
literal|14
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"api_key"
argument_list|,
literal|"dolphins"
argument_list|)
expr_stmt|;
specifier|final
name|Pet
name|pet
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:getPetById"
argument_list|,
name|NO_BODY
argument_list|,
name|headers
argument_list|,
name|Pet
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pet
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|14
argument_list|)
argument_list|,
name|pet
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Olafur Eliason Arnalds"
argument_list|,
name|pet
operator|.
name|name
argument_list|)
expr_stmt|;
name|petstore
operator|.
name|verify
argument_list|(
name|getRequestedFor
argument_list|(
name|urlEqualTo
argument_list|(
literal|"/v2/pet/14"
argument_list|)
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"Accept"
argument_list|,
name|equalTo
argument_list|(
literal|"application/xml, application/json"
argument_list|)
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"api_key"
argument_list|,
name|equalTo
argument_list|(
literal|"dolphins"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldBeGettingPetsByIdWithApiKeysInQueryParameter ()
specifier|public
name|void
name|shouldBeGettingPetsByIdWithApiKeysInQueryParameter
parameter_list|()
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
name|headers
operator|.
name|put
argument_list|(
literal|"petId"
argument_list|,
literal|14
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"api_key"
argument_list|,
literal|"dolphins"
argument_list|)
expr_stmt|;
specifier|final
name|Pet
name|pet
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"altPetStore:getPetById"
argument_list|,
name|NO_BODY
argument_list|,
name|headers
argument_list|,
name|Pet
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pet
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|14
argument_list|)
argument_list|,
name|pet
operator|.
name|id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Olafur Eliason Arnalds"
argument_list|,
name|pet
operator|.
name|name
argument_list|)
expr_stmt|;
name|petstore
operator|.
name|verify
argument_list|(
name|getRequestedFor
argument_list|(
name|urlEqualTo
argument_list|(
literal|"/v2/pet/14?api_key=dolphins"
argument_list|)
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"Accept"
argument_list|,
name|equalTo
argument_list|(
literal|"application/xml, application/json"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldBeGettingPetsByStatus ()
specifier|public
name|void
name|shouldBeGettingPetsByStatus
parameter_list|()
block|{
specifier|final
name|Pets
name|pets
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:findPetsByStatus"
argument_list|,
name|NO_BODY
argument_list|,
literal|"status"
argument_list|,
literal|"available"
argument_list|,
name|Pets
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pets
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|pets
operator|.
name|pets
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|pets
operator|.
name|pets
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|petstore
operator|.
name|verify
argument_list|(
name|getRequestedFor
argument_list|(
name|urlPathEqualTo
argument_list|(
literal|"/v2/pet/findByStatus"
argument_list|)
argument_list|)
operator|.
name|withQueryParam
argument_list|(
literal|"status"
argument_list|,
name|equalTo
argument_list|(
literal|"available"
argument_list|)
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"Accept"
argument_list|,
name|equalTo
argument_list|(
literal|"application/xml, application/json"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|RestOpenApiComponent
name|component
init|=
operator|new
name|RestOpenApiComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setComponentName
argument_list|(
name|componentName
argument_list|)
expr_stmt|;
name|component
operator|.
name|setHost
argument_list|(
literal|"http://localhost:"
operator|+
name|petstore
operator|.
name|port
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"petStore"
argument_list|,
name|component
argument_list|)
expr_stmt|;
specifier|final
name|RestOpenApiComponent
name|altPetStore
init|=
operator|new
name|RestOpenApiComponent
argument_list|()
decl_stmt|;
name|altPetStore
operator|.
name|setComponentName
argument_list|(
name|componentName
argument_list|)
expr_stmt|;
name|altPetStore
operator|.
name|setHost
argument_list|(
literal|"http://localhost:"
operator|+
name|petstore
operator|.
name|port
argument_list|()
argument_list|)
expr_stmt|;
name|altPetStore
operator|.
name|setSpecificationUri
argument_list|(
name|RestOpenApiComponentTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/alt-petstore.json"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"altPetStore"
argument_list|,
name|altPetStore
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|JAXBContext
name|jaxbContext
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|Pet
operator|.
name|class
argument_list|,
name|Pets
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|JaxbDataFormat
name|jaxb
init|=
operator|new
name|JaxbDataFormat
argument_list|(
name|jaxbContext
argument_list|)
decl_stmt|;
name|jaxb
operator|.
name|setJaxbProviderProperties
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|Marshaller
operator|.
name|JAXB_FORMATTED_OUTPUT
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:getPetById"
argument_list|)
operator|.
name|to
argument_list|(
literal|"petStore:getPetById"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|jaxb
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:getPetByIdWithEndpointParams"
argument_list|)
operator|.
name|to
argument_list|(
literal|"petStore:getPetById?petId=14"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|jaxb
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:addPet"
argument_list|)
operator|.
name|marshal
argument_list|(
name|jaxb
argument_list|)
operator|.
name|to
argument_list|(
literal|"petStore:addPet"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|jaxb
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:findPetsByStatus"
argument_list|)
operator|.
name|to
argument_list|(
literal|"petStore:findPetsByStatus"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|jaxb
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
literal|"component = {0}"
argument_list|)
DECL|method|knownProducers ()
specifier|public
specifier|static
name|Iterable
argument_list|<
name|String
argument_list|>
name|knownProducers
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|RestEndpoint
operator|.
name|DEFAULT_REST_PRODUCER_COMPONENTS
argument_list|)
return|;
block|}
annotation|@
name|BeforeClass
DECL|method|setupStubs ()
specifier|public
specifier|static
name|void
name|setupStubs
parameter_list|()
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
name|petstore
operator|.
name|stubFor
argument_list|(
name|get
argument_list|(
name|urlEqualTo
argument_list|(
literal|"/openapi.json"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withBody
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|RestOpenApiComponentTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/openapi.json"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|petstore
operator|.
name|stubFor
argument_list|(
name|post
argument_list|(
name|urlEqualTo
argument_list|(
literal|"/v2/pet"
argument_list|)
argument_list|)
operator|.
name|withRequestBody
argument_list|(
name|equalTo
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Pet><name>Jean-Luc Picard</name></Pet>"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withStatus
argument_list|(
name|HttpURLConnection
operator|.
name|HTTP_CREATED
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Pet><id>14</id></Pet>"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|petstore
operator|.
name|stubFor
argument_list|(
name|get
argument_list|(
name|urlEqualTo
argument_list|(
literal|"/v2/pet/14"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withStatus
argument_list|(
name|HttpURLConnection
operator|.
name|HTTP_OK
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Pet><id>14</id><name>Olafur Eliason Arnalds</name></Pet>"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|petstore
operator|.
name|stubFor
argument_list|(
name|get
argument_list|(
name|urlPathEqualTo
argument_list|(
literal|"/v2/pet/findByStatus"
argument_list|)
argument_list|)
operator|.
name|withQueryParam
argument_list|(
literal|"status"
argument_list|,
name|equalTo
argument_list|(
literal|"available"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withStatus
argument_list|(
name|HttpURLConnection
operator|.
name|HTTP_OK
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><pets><Pet><id>1</id><name>Olafur Eliason Arnalds</name></Pet><Pet><name>Jean-Luc Picard</name></Pet></pets>"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|petstore
operator|.
name|stubFor
argument_list|(
name|get
argument_list|(
name|urlEqualTo
argument_list|(
literal|"/v2/pet/14?api_key=dolphins"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withStatus
argument_list|(
name|HttpURLConnection
operator|.
name|HTTP_OK
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Pet><id>14</id><name>Olafur Eliason Arnalds</name></Pet>"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

