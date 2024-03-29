begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.fhir
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|fhir
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
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|api
operator|.
name|EncodingEnum
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|api
operator|.
name|SummaryEnum
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|IClientInterceptor
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|client
operator|.
name|impl
operator|.
name|GenericClient
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
name|fhir
operator|.
name|internal
operator|.
name|FhirApiCollection
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
name|fhir
operator|.
name|internal
operator|.
name|FhirCreateApiMethod
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
name|impl
operator|.
name|DefaultCamelContext
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
comment|/**  * Test class for {@link FhirConfiguration} APIs.  */
end_comment

begin_class
DECL|class|FhirConfigurationIT
specifier|public
class|class
name|FhirConfigurationIT
extends|extends
name|AbstractFhirTestSupport
block|{
DECL|field|PATH_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PREFIX
init|=
name|FhirApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|FhirCreateApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|TEST_URI
specifier|private
specifier|static
specifier|final
name|String
name|TEST_URI
init|=
literal|"fhir://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/resource?inBody=resourceAsString&log=true&"
operator|+
literal|"encoding=JSON&summary=TEXT&compress=true&username=art&password=tatum&sessionCookie=mycookie%3DChips%20Ahoy"
operator|+
literal|"&accessToken=token&serverUrl=http://localhost:8080/hapi-fhir-jpaserver-example/baseDstu3&fhirVersion=DSTU3"
decl_stmt|;
DECL|field|componentConfiguration
specifier|private
name|FhirConfiguration
name|componentConfiguration
decl_stmt|;
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
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|createRegistry
argument_list|()
argument_list|)
decl_stmt|;
comment|// add FhirComponent to Camel context but don't set up componentConfiguration
specifier|final
name|FhirComponent
name|component
init|=
operator|new
name|FhirComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|FhirConfiguration
name|fhirConfiguration
init|=
operator|new
name|FhirConfiguration
argument_list|()
decl_stmt|;
name|fhirConfiguration
operator|.
name|setLog
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|fhirConfiguration
operator|.
name|setEncoding
argument_list|(
literal|"JSON"
argument_list|)
expr_stmt|;
name|fhirConfiguration
operator|.
name|setSummary
argument_list|(
literal|"TEXT"
argument_list|)
expr_stmt|;
name|fhirConfiguration
operator|.
name|setCompress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|fhirConfiguration
operator|.
name|setUsername
argument_list|(
literal|"art"
argument_list|)
expr_stmt|;
name|fhirConfiguration
operator|.
name|setPassword
argument_list|(
literal|"tatum"
argument_list|)
expr_stmt|;
name|fhirConfiguration
operator|.
name|setSessionCookie
argument_list|(
literal|"mycookie=Chips Ahoy"
argument_list|)
expr_stmt|;
name|fhirConfiguration
operator|.
name|setAccessToken
argument_list|(
literal|"token"
argument_list|)
expr_stmt|;
name|fhirConfiguration
operator|.
name|setServerUrl
argument_list|(
literal|"http://localhost:8080/hapi-fhir-jpaserver-example/baseDstu3"
argument_list|)
expr_stmt|;
name|fhirConfiguration
operator|.
name|setFhirVersion
argument_list|(
literal|"DSTU3"
argument_list|)
expr_stmt|;
name|component
operator|.
name|setConfiguration
argument_list|(
name|fhirConfiguration
argument_list|)
expr_stmt|;
name|this
operator|.
name|componentConfiguration
operator|=
name|fhirConfiguration
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"fhir"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testConfiguration ()
specifier|public
name|void
name|testConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|FhirEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
name|TEST_URI
argument_list|,
name|FhirEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|GenericClient
name|client
init|=
operator|(
name|GenericClient
operator|)
name|endpoint
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|FhirConfiguration
name|configuration
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|this
operator|.
name|componentConfiguration
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:8080/hapi-fhir-jpaserver-example/baseDstu3"
argument_list|,
name|client
operator|.
name|getUrlBase
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EncodingEnum
operator|.
name|JSON
argument_list|,
name|client
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SummaryEnum
operator|.
name|TEXT
argument_list|,
name|client
operator|.
name|getSummary
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|interceptors
init|=
name|client
operator|.
name|getInterceptorService
argument_list|()
operator|.
name|getAllRegisteredInterceptors
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|cleanFhirServerState ()
specifier|public
name|void
name|cleanFhirServerState
parameter_list|()
block|{
comment|// do nothing
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct://CONFIGURATION"
argument_list|)
operator|.
name|to
argument_list|(
name|TEST_URI
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

