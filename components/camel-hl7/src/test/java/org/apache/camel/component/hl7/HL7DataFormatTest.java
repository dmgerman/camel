begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hl7
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hl7
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
name|model
operator|.
name|Message
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
name|ADR_A19
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
name|MSA
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
name|MSH
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
name|QRD
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
name|ContextTestSupport
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

begin_comment
comment|/**  * Unit test for HL7 DataFormat.  */
end_comment

begin_class
DECL|class|HL7DataFormatTest
specifier|public
class|class
name|HL7DataFormatTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testMarshal ()
specifier|public
name|void
name|testMarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:marshal"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"MSA|AA|123"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"QRD|20080805120000"
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|createHL7AsMessage
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testUnmarshal ()
specifier|public
name|void
name|testUnmarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:unmarshal"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|Message
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|HL7Constants
operator|.
name|HL7_SENDING_APPLICATION
argument_list|,
literal|"MYSERVER"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|HL7Constants
operator|.
name|HL7_SENDING_FACILITY
argument_list|,
literal|"MYSENDERAPP"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|HL7Constants
operator|.
name|HL7_RECEIVING_APPLICATION
argument_list|,
literal|"MYCLIENT"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|HL7Constants
operator|.
name|HL7_RECEIVING_FACILITY
argument_list|,
literal|"MYCLIENTAPP"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|HL7Constants
operator|.
name|HL7_TIMESTAMP
argument_list|,
literal|"200612211200"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|HL7Constants
operator|.
name|HL7_SECURITY
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|HL7Constants
operator|.
name|HL7_MESSAGE_TYPE
argument_list|,
literal|"QRY"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|HL7Constants
operator|.
name|HL7_TRIGGER_EVENT
argument_list|,
literal|"A19"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|HL7Constants
operator|.
name|HL7_MESSAGE_CONTROL
argument_list|,
literal|"1234"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|HL7Constants
operator|.
name|HL7_PROCESSING_ID
argument_list|,
literal|"P"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|HL7Constants
operator|.
name|HL7_VERSION_ID
argument_list|,
literal|"2.4"
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|createHL7AsString
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Message
name|msg
init|=
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
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2.4"
argument_list|,
name|msg
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|QRD
name|qrd
init|=
operator|(
name|QRD
operator|)
name|msg
operator|.
name|get
argument_list|(
literal|"QRD"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"0101701234"
argument_list|,
name|qrd
operator|.
name|getWhoSubjectFilter
argument_list|(
literal|0
argument_list|)
operator|.
name|getIDNumber
argument_list|()
operator|.
name|getValue
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
name|from
argument_list|(
literal|"direct:marshal"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|hl7
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:marshal"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|hl7
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:unmarshal"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createHL7AsString ()
specifier|private
specifier|static
name|String
name|createHL7AsString
parameter_list|()
block|{
name|String
name|line1
init|=
literal|"MSH|^~\\&|MYSENDER|MYSENDERAPP|MYCLIENT|MYCLIENTAPP|200612211200||QRY^A19|1234|P|2.4"
decl_stmt|;
name|String
name|line2
init|=
literal|"QRD|200612211200|R|I|GetPatient|||1^RD|0101701234|DEM||"
decl_stmt|;
name|StringBuffer
name|body
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line1
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line2
argument_list|)
expr_stmt|;
return|return
name|body
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|createHL7AsMessage ()
specifier|private
specifier|static
name|Message
name|createHL7AsMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|ADR_A19
name|adr
init|=
operator|new
name|ADR_A19
argument_list|()
decl_stmt|;
comment|// Populate the MSH Segment
name|MSH
name|mshSegment
init|=
name|adr
operator|.
name|getMSH
argument_list|()
decl_stmt|;
name|mshSegment
operator|.
name|getFieldSeparator
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"|"
argument_list|)
expr_stmt|;
name|mshSegment
operator|.
name|getEncodingCharacters
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"^~\\&"
argument_list|)
expr_stmt|;
name|mshSegment
operator|.
name|getDateTimeOfMessage
argument_list|()
operator|.
name|getTimeOfAnEvent
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"200701011539"
argument_list|)
expr_stmt|;
name|mshSegment
operator|.
name|getSendingApplication
argument_list|()
operator|.
name|getNamespaceID
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"MYSENDER"
argument_list|)
expr_stmt|;
name|mshSegment
operator|.
name|getSequenceNumber
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|mshSegment
operator|.
name|getMessageType
argument_list|()
operator|.
name|getMessageType
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"ADR"
argument_list|)
expr_stmt|;
name|mshSegment
operator|.
name|getMessageType
argument_list|()
operator|.
name|getTriggerEvent
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"A19"
argument_list|)
expr_stmt|;
comment|// Populate the PID Segment
name|MSA
name|msa
init|=
name|adr
operator|.
name|getMSA
argument_list|()
decl_stmt|;
name|msa
operator|.
name|getAcknowledgementCode
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"AA"
argument_list|)
expr_stmt|;
name|msa
operator|.
name|getMessageControlID
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|QRD
name|qrd
init|=
name|adr
operator|.
name|getQRD
argument_list|()
decl_stmt|;
name|qrd
operator|.
name|getQueryDateTime
argument_list|()
operator|.
name|getTimeOfAnEvent
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"20080805120000"
argument_list|)
expr_stmt|;
return|return
name|adr
operator|.
name|getMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

