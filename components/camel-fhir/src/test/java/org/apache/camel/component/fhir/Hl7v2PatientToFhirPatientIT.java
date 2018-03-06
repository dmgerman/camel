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
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|rest
operator|.
name|api
operator|.
name|MethodOutcome
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
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
name|Processor
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
name|hl7
operator|.
name|HL7DataFormat
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

begin_comment
comment|/**  * This test demonstrates how to convert a HL7V2 patient to a FHIR dtsu3 Patient.  */
end_comment

begin_class
DECL|class|Hl7v2PatientToFhirPatientIT
specifier|public
class|class
name|Hl7v2PatientToFhirPatientIT
extends|extends
name|CamelTestSupport
block|{
comment|/*     Segment Purpose                 FHIR Resource     MSH     Message header          MessageHeader     PID     Patient Identification  Patient     PV1     Patient Visit           Not used in this example     PV2     Patient Visit           â Additional data Not used in this example     ORC     Common Order            Not used in this example     OBR     Observation             Request Observation     OBX     Observation             ObservationProvider      See https://fhirblog.com/2014/10/05/mapping-hl7-version-2-to-fhir-messages for more information     */
DECL|field|HL7_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|HL7_MESSAGE
init|=
literal|"MSH|^~\\&|Amalga HIS|BUM|New Tester|MS|20111121103141||ORU^R01|2847970-2"
operator|+
literal|"01111211031|P|2.4|||AL|NE|764|ASCII|||\r"
operator|+
literal|"PID||100005056|100005056||Dasher^Mary^\"\"^^\"\"|\"\"|19810813000000|F||CA|Street 1^\"\"^\"\"^\"\"^34000^SGP^^"
operator|+
literal|"\"\"~\"\"^\"\"^\"\"^\"\"^Danling Street 5th^THA^^\"\"||326-2275^PRN^PH^^66^675~476-5059^ORN^CP^^66^359~(123)"
operator|+
literal|"456-7890^ORN^FX^^66^222~^NET^X.400^a@a.a~^NET^X.400^dummy@hotmail.com|(123)456-7890^WPN^PH^^66|UNK|S|BUD||BP000111899|"
operator|+
literal|"D99999^\"\"||CA|Bangkok|||THA||THA|\"\"|N\r"
operator|+
literal|"PV1||OPD   ||||\"\"^\"\"^\"\"||||CNSLT|||||C|VIP|||6262618|PB1||||||||||||||||||||||||20101208134638\r"
operator|+
literal|"PV2|||^Unknown|\"\"^\"\"||||\"\"|\"\"|0||\"\"|||||||||||||||||||||||||||||HP1\r"
operator|+
literal|"ORC|NW|\"\"|BMC1102771601|\"\"|CM||^^^^^\"\"|||||||||\"\"^\"\"^^^\"\"\r"
operator|+
literal|"OBR|1|\"\"|BMC1102771601|\"\"^Brain (CT)||20111028124215||||||||||||||||||CTSCAN|F||^^^^^ROUTINE|||\"\"||||||\"\"|||||||||||^\"\"\r"
operator|+
literal|"OBX|1|FT|\"\"^Brain (CT)||++++ text of report goes here +++|||REQAT|||FINAL|||20111121103040||75929^Gosselin^Angelina"
decl_stmt|;
DECL|field|fhirContext
specifier|private
name|FhirContext
name|fhirContext
init|=
name|FhirContext
operator|.
name|forDstu3
argument_list|()
decl_stmt|;
DECL|field|client
specifier|private
name|IGenericClient
name|client
init|=
name|fhirContext
operator|.
name|newRestfulGenericClient
argument_list|(
literal|"http://fhirtest.uhn.ca/baseDstu3"
argument_list|)
decl_stmt|;
DECL|field|hl7
specifier|private
name|HL7DataFormat
name|hl7
init|=
operator|new
name|HL7DataFormat
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testUnmarshalWithExplicitUTF16Charset ()
specifier|public
name|void
name|testUnmarshalWithExplicitUTF16Charset
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// Message with explicit encoding in MSH
name|String
name|charset
init|=
literal|"ASCII"
decl_stmt|;
name|byte
index|[]
name|body
init|=
name|HL7_MESSAGE
operator|.
name|getBytes
argument_list|(
name|Charset
operator|.
name|forName
argument_list|(
name|charset
argument_list|)
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:input"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|body
argument_list|)
argument_list|,
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|charset
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
specifier|final
name|MethodOutcome
name|results
init|=
operator|(
name|MethodOutcome
operator|)
name|mock
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
name|getBody
argument_list|()
decl_stmt|;
name|Patient
name|patient
init|=
operator|(
name|Patient
operator|)
name|results
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
literal|"Dasher"
argument_list|,
name|patient
operator|.
name|getNameFirstRep
argument_list|()
operator|.
name|getFamily
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
throws|throws
name|Exception
block|{
name|Processor
name|patientProcessor
init|=
operator|new
name|PatientProcessor
argument_list|(
name|client
argument_list|,
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:input"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|hl7
argument_list|)
operator|.
name|process
argument_list|(
name|patientProcessor
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

