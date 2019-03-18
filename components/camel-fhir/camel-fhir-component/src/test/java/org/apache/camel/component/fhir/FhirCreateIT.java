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
name|MethodOutcome
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
name|api
operator|.
name|ExtraParameters
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
name|hl7
operator|.
name|fhir
operator|.
name|dstu3
operator|.
name|model
operator|.
name|HumanName
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
comment|/**  * Test class for {@link org.apache.camel.component.fhir.api.FhirCreate} APIs.  * The class source won't be generated again if the generator MOJO finds it under src/test/java.  */
end_comment

begin_class
DECL|class|FhirCreateIT
specifier|public
class|class
name|FhirCreateIT
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
name|FhirCreateIT
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
name|FhirCreateApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testCreateResource ()
specifier|public
name|void
name|testCreateResource
parameter_list|()
throws|throws
name|Exception
block|{
name|Patient
name|patient
init|=
operator|new
name|Patient
argument_list|()
operator|.
name|addName
argument_list|(
operator|new
name|HumanName
argument_list|()
operator|.
name|addGiven
argument_list|(
literal|"Vincent"
argument_list|)
operator|.
name|setFamily
argument_list|(
literal|"Freeman"
argument_list|)
argument_list|)
decl_stmt|;
name|MethodOutcome
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://RESOURCE"
argument_list|,
name|patient
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"resource: "
operator|+
name|result
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"resource result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getCreated
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateStringResource ()
specifier|public
name|void
name|testCreateStringResource
parameter_list|()
throws|throws
name|Exception
block|{
name|Patient
name|patient
init|=
operator|new
name|Patient
argument_list|()
operator|.
name|addName
argument_list|(
operator|new
name|HumanName
argument_list|()
operator|.
name|addGiven
argument_list|(
literal|"Vincent"
argument_list|)
operator|.
name|setFamily
argument_list|(
literal|"Freeman"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|patientString
init|=
name|this
operator|.
name|fhirContext
operator|.
name|newXmlParser
argument_list|()
operator|.
name|encodeResourceToString
argument_list|(
name|patient
argument_list|)
decl_stmt|;
name|MethodOutcome
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://RESOURCE_STRING"
argument_list|,
name|patientString
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"resource: "
operator|+
name|result
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"resource result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getCreated
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateStringResourceEncodeXml ()
specifier|public
name|void
name|testCreateStringResourceEncodeXml
parameter_list|()
throws|throws
name|Exception
block|{
name|Patient
name|patient
init|=
operator|new
name|Patient
argument_list|()
operator|.
name|addName
argument_list|(
operator|new
name|HumanName
argument_list|()
operator|.
name|addGiven
argument_list|(
literal|"Vincent"
argument_list|)
operator|.
name|setFamily
argument_list|(
literal|"Freeman"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|patientString
init|=
name|this
operator|.
name|fhirContext
operator|.
name|newXmlParser
argument_list|()
operator|.
name|encodeResourceToString
argument_list|(
name|patient
argument_list|)
decl_stmt|;
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
name|ExtraParameters
operator|.
name|ENCODE_XML
operator|.
name|getHeaderName
argument_list|()
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|MethodOutcome
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://RESOURCE_STRING"
argument_list|,
name|patientString
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"resource: "
operator|+
name|result
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"resource result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getCreated
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
comment|// test route for resource
name|from
argument_list|(
literal|"direct://RESOURCE"
argument_list|)
operator|.
name|to
argument_list|(
literal|"fhir://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/resource?inBody=resource"
argument_list|)
expr_stmt|;
comment|// test route for resource
name|from
argument_list|(
literal|"direct://RESOURCE_STRING"
argument_list|)
operator|.
name|to
argument_list|(
literal|"fhir://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/resource?inBody=resourceAsString&log=true"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

