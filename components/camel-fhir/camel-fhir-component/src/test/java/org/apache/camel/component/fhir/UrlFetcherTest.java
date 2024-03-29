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
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|context
operator|.
name|FhirContext
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
name|IGenericClient
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
name|interceptor
operator|.
name|LoggingInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hl7
operator|.
name|fhir
operator|.
name|dstu3
operator|.
name|model
operator|.
name|Patient
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

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Helper class to generate search URLs based on HAPI-FHIR's search API"
argument_list|)
DECL|class|UrlFetcherTest
specifier|public
class|class
name|UrlFetcherTest
block|{
annotation|@
name|Test
DECL|method|getUrlTest ()
specifier|public
name|void
name|getUrlTest
parameter_list|()
block|{
comment|// Create a client to talk to your favorite test server
name|FhirContext
name|ctx
init|=
name|FhirContext
operator|.
name|forDstu3
argument_list|()
decl_stmt|;
name|IGenericClient
name|client
init|=
name|ctx
operator|.
name|newRestfulGenericClient
argument_list|(
literal|"http://localhost:8080/hapi-fhir-jpaserver-example/baseDstu3"
argument_list|)
decl_stmt|;
comment|// URL will be logged in console, see log4j2.properties
name|client
operator|.
name|registerInterceptor
argument_list|(
operator|new
name|LoggingInterceptor
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|search
argument_list|()
operator|.
name|forResource
argument_list|(
literal|"Patient"
argument_list|)
operator|.
name|where
argument_list|(
name|Patient
operator|.
name|IDENTIFIER
operator|.
name|exactly
argument_list|()
operator|.
name|identifier
argument_list|(
literal|"this/is/my/id"
argument_list|)
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

