begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bonita.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bonita
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
name|HashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientRequestContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Cookie
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedHashMap
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
name|component
operator|.
name|bonita
operator|.
name|api
operator|.
name|filter
operator|.
name|BonitaAuthFilter
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
name|bonita
operator|.
name|api
operator|.
name|util
operator|.
name|BonitaAPIConfig
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
name|Rule
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
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|MockitoAnnotations
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
name|stubFor
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
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|BonitaAuthFilterConnectionTest
specifier|public
class|class
name|BonitaAuthFilterConnectionTest
block|{
annotation|@
name|Rule
DECL|field|wireMockRule
specifier|public
name|WireMockRule
name|wireMockRule
init|=
operator|new
name|WireMockRule
argument_list|(
literal|0
argument_list|)
decl_stmt|;
annotation|@
name|Mock
DECL|field|requestContext
specifier|private
name|ClientRequestContext
name|requestContext
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
name|MockitoAnnotations
operator|.
name|initMocks
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|requestContext
operator|.
name|getCookies
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|requestContext
operator|.
name|getHeaders
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|MultivaluedHashMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnection ()
specifier|public
name|void
name|testConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|port
init|=
name|wireMockRule
operator|.
name|port
argument_list|()
operator|+
literal|""
decl_stmt|;
name|stubFor
argument_list|(
name|post
argument_list|(
name|urlEqualTo
argument_list|(
literal|"/bonita/loginservice"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withHeader
argument_list|(
literal|"Set-Cookie"
argument_list|,
literal|"JSESSIONID=something"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|BonitaAPIConfig
name|bonitaApiConfig
init|=
operator|new
name|BonitaAPIConfig
argument_list|(
literal|"localhost"
argument_list|,
name|port
argument_list|,
literal|"username"
argument_list|,
literal|"password"
argument_list|)
decl_stmt|;
name|BonitaAuthFilter
name|bonitaAuthFilter
init|=
operator|new
name|BonitaAuthFilter
argument_list|(
name|bonitaApiConfig
argument_list|)
decl_stmt|;
name|bonitaAuthFilter
operator|.
name|filter
argument_list|(
name|requestContext
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|requestContext
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectionSupportCSRF ()
specifier|public
name|void
name|testConnectionSupportCSRF
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|port
init|=
name|wireMockRule
operator|.
name|port
argument_list|()
operator|+
literal|""
decl_stmt|;
name|stubFor
argument_list|(
name|post
argument_list|(
name|urlEqualTo
argument_list|(
literal|"/bonita/loginservice"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withHeader
argument_list|(
literal|"Set-Cookie"
argument_list|,
literal|"JSESSIONID=something"
argument_list|,
literal|"X-Bonita-API-Token=something"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|BonitaAPIConfig
name|bonitaApiConfig
init|=
operator|new
name|BonitaAPIConfig
argument_list|(
literal|"localhost"
argument_list|,
name|port
argument_list|,
literal|"username"
argument_list|,
literal|"password"
argument_list|)
decl_stmt|;
name|BonitaAuthFilter
name|bonitaAuthFilter
init|=
operator|new
name|BonitaAuthFilter
argument_list|(
name|bonitaApiConfig
argument_list|)
decl_stmt|;
name|bonitaAuthFilter
operator|.
name|filter
argument_list|(
name|requestContext
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|requestContext
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

