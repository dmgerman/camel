begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|FhirSearchApiMethod
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
name|Bundle
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
comment|/**  * Test class for {@link org.apache.camel.component.fhir.api.FhirSearch} APIs.  * The class source won't be generated again if the generator MOJO finds it under src/test/java.  */
end_comment

begin_class
DECL|class|FhirSearchIT
specifier|public
class|class
name|FhirSearchIT
extends|extends
name|AbstractFhirTestSupport
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
name|FhirSearchIT
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
name|FhirApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|FhirSearchApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testSearchByUrl ()
specifier|public
name|void
name|testSearchByUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|url
init|=
literal|"Patient?given=Vincent&family=Freeman&_format=json"
decl_stmt|;
name|Bundle
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://SEARCH_BY_URL"
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"searchByUrl: "
operator|+
name|result
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"searchByUrl result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|Patient
name|patient
init|=
operator|(
name|Patient
operator|)
name|result
operator|.
name|getEntry
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getResource
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|patient
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Freeman"
argument_list|,
name|patient
operator|.
name|getName
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getFamily
argument_list|()
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
comment|// test route for searchByUrl
name|from
argument_list|(
literal|"direct://SEARCH_BY_URL"
argument_list|)
operator|.
name|to
argument_list|(
literal|"fhir://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/searchByUrl?inBody=url"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

