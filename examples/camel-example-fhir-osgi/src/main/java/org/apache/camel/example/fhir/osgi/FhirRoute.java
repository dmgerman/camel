begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.fhir.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|fhir
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|HL7Exception
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|model
operator|.
name|v24
operator|.
name|message
operator|.
name|ORU_R01
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|hl7v2
operator|.
name|model
operator|.
name|v24
operator|.
name|segment
operator|.
name|PID
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
name|LoggingLevel
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
name|http
operator|.
name|ProtocolException
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

begin_class
DECL|class|FhirRoute
specifier|public
class|class
name|FhirRoute
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"file:{{input}}"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"fhir-example-osgi"
argument_list|)
operator|.
name|onException
argument_list|(
name|ProtocolException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|,
literal|"Error connecting to FHIR server with URL:{{serverUrl}}, "
operator|+
literal|"please check the org.apache.camel.example.fhir.osgi.configuration.cfg configuration file ${exception.message}"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|onException
argument_list|(
name|HL7Exception
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|,
literal|"Error unmarshalling ${file:name} ${exception.message}"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
literal|"Converting ${file:name}"
argument_list|)
comment|// unmarshall file to hl7 message
operator|.
name|unmarshal
argument_list|()
operator|.
name|hl7
argument_list|()
comment|// very simple mapping from a HLV2 patient to dstu3 patient
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|ORU_R01
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ORU_R01
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|PID
name|pid
init|=
name|msg
operator|.
name|getPATIENT_RESULT
argument_list|()
operator|.
name|getPATIENT
argument_list|()
operator|.
name|getPID
argument_list|()
decl_stmt|;
name|String
name|surname
init|=
name|pid
operator|.
name|getPatientName
argument_list|()
index|[
literal|0
index|]
operator|.
name|getFamilyName
argument_list|()
operator|.
name|getFn1_Surname
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|pid
operator|.
name|getPatientName
argument_list|()
index|[
literal|0
index|]
operator|.
name|getGivenName
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|patientId
init|=
name|msg
operator|.
name|getPATIENT_RESULT
argument_list|()
operator|.
name|getPATIENT
argument_list|()
operator|.
name|getPID
argument_list|()
operator|.
name|getPatientID
argument_list|()
operator|.
name|getCx1_ID
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Patient
name|patient
init|=
operator|new
name|Patient
argument_list|()
decl_stmt|;
name|patient
operator|.
name|addName
argument_list|()
operator|.
name|addGiven
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|patient
operator|.
name|getNameFirstRep
argument_list|()
operator|.
name|setFamily
argument_list|(
name|surname
argument_list|)
expr_stmt|;
name|patient
operator|.
name|setId
argument_list|(
name|patientId
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|patient
argument_list|)
expr_stmt|;
block|}
argument_list|)
comment|// marshall to JSON for logging
operator|.
name|marshal
argument_list|()
operator|.
name|fhirJson
argument_list|(
literal|"{{fhirVersion}}"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Inserting Patient: ${body}"
argument_list|)
comment|// create Patient in our FHIR server
operator|.
name|to
argument_list|(
literal|"fhir://create/resource?inBody=resourceAsString&serverUrl={{serverUrl}}&fhirVersion={{fhirVersion}}"
argument_list|)
comment|// log the outcome
operator|.
name|log
argument_list|(
literal|"Patient created successfully: ${body.getCreated}"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

