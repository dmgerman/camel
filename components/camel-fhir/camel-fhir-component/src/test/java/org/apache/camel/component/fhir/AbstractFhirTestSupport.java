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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|context
operator|.
name|FhirVersionEnum
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
name|server
operator|.
name|exceptions
operator|.
name|ResourceGoneException
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
name|support
operator|.
name|IntrospectionSupport
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
name|Before
import|;
end_import

begin_comment
comment|/**  * Abstract base class for Fhir Integration tests generated by Camel API component maven plugin.  */
end_comment

begin_class
DECL|class|AbstractFhirTestSupport
specifier|public
specifier|abstract
class|class
name|AbstractFhirTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_OPTIONS_PROPERTIES
specifier|private
specifier|static
specifier|final
name|String
name|TEST_OPTIONS_PROPERTIES
init|=
literal|"/test-options.properties"
decl_stmt|;
DECL|field|FHIR_CONTEXT_THREAD_LOCAL
specifier|private
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|FhirContext
argument_list|>
name|FHIR_CONTEXT_THREAD_LOCAL
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|GENERIC_CLIENT_THREAD_LOCAL
specifier|private
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|IGenericClient
argument_list|>
name|GENERIC_CLIENT_THREAD_LOCAL
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|patient
specifier|protected
name|Patient
name|patient
decl_stmt|;
DECL|field|fhirContext
name|FhirContext
name|fhirContext
decl_stmt|;
DECL|field|fhirClient
name|IGenericClient
name|fhirClient
decl_stmt|;
annotation|@
name|Before
DECL|method|cleanFhirServerState ()
specifier|public
name|void
name|cleanFhirServerState
parameter_list|()
block|{
if|if
condition|(
name|patientExists
argument_list|()
condition|)
block|{
name|deletePatient
argument_list|()
expr_stmt|;
block|}
name|createPatient
argument_list|()
expr_stmt|;
block|}
DECL|method|patientExists ()
name|boolean
name|patientExists
parameter_list|()
block|{
try|try
block|{
name|Bundle
name|bundle
init|=
name|fhirClient
operator|.
name|search
argument_list|()
operator|.
name|byUrl
argument_list|(
literal|"Patient?given=Vincent&family=Freeman"
argument_list|)
operator|.
name|returnBundle
argument_list|(
name|Bundle
operator|.
name|class
argument_list|)
operator|.
name|execute
argument_list|()
decl_stmt|;
return|return
operator|!
name|bundle
operator|.
name|getEntry
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|ResourceGoneException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|deletePatient ()
specifier|private
name|void
name|deletePatient
parameter_list|()
block|{
name|fhirClient
operator|.
name|delete
argument_list|()
operator|.
name|resourceConditionalByUrl
argument_list|(
literal|"Patient?given=Vincent&family=Freeman"
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
DECL|method|createPatient ()
specifier|private
name|void
name|createPatient
parameter_list|()
block|{
name|this
operator|.
name|patient
operator|=
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
operator|.
name|setActive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|patient
operator|.
name|setId
argument_list|(
name|fhirClient
operator|.
name|create
argument_list|()
operator|.
name|resource
argument_list|(
name|patient
argument_list|)
operator|.
name|execute
argument_list|()
operator|.
name|getId
argument_list|()
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
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
comment|// read Fhir component configuration from TEST_OPTIONS_PROPERTIES
specifier|final
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|properties
operator|.
name|load
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|TEST_OPTIONS_PROPERTIES
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s could not be loaded: %s"
argument_list|,
name|TEST_OPTIONS_PROPERTIES
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|options
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|FhirVersionEnum
name|version
init|=
name|FhirVersionEnum
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
literal|"fhirVersion"
argument_list|)
argument_list|)
decl_stmt|;
name|this
operator|.
name|fhirContext
operator|=
operator|new
name|FhirContext
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|FHIR_CONTEXT_THREAD_LOCAL
operator|.
name|set
argument_list|(
name|this
operator|.
name|fhirContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|fhirClient
operator|=
name|this
operator|.
name|fhirContext
operator|.
name|newRestfulGenericClient
argument_list|(
operator|(
name|String
operator|)
name|options
operator|.
name|get
argument_list|(
literal|"serverUrl"
argument_list|)
argument_list|)
expr_stmt|;
name|GENERIC_CLIENT_THREAD_LOCAL
operator|.
name|set
argument_list|(
name|this
operator|.
name|fhirClient
argument_list|)
expr_stmt|;
specifier|final
name|FhirConfiguration
name|configuration
init|=
operator|new
name|FhirConfiguration
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setFhirContext
argument_list|(
name|this
operator|.
name|fhirContext
argument_list|)
expr_stmt|;
comment|// add FhirComponent to Camel context
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
name|component
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
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
name|Override
DECL|method|postProcessTest ()
specifier|protected
name|void
name|postProcessTest
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|postProcessTest
argument_list|()
expr_stmt|;
name|this
operator|.
name|fhirContext
operator|=
name|FHIR_CONTEXT_THREAD_LOCAL
operator|.
name|get
argument_list|()
expr_stmt|;
name|this
operator|.
name|fhirClient
operator|=
name|GENERIC_CLIENT_THREAD_LOCAL
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
comment|// only create the context once for this class
return|return
literal|true
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|requestBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
operator|(
name|T
operator|)
name|template
argument_list|()
operator|.
name|requestBodyAndHeaders
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|requestBody (String endpoint, Object body)
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBody
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
operator|(
name|T
operator|)
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
name|endpoint
argument_list|,
name|body
argument_list|)
return|;
block|}
block|}
end_class

end_unit

