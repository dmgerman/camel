begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|BindToRegistry
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test for the HL7MLLPNetty Codec.  */
end_comment

begin_class
DECL|class|HL7MLLPNettyRouteToTest
specifier|public
class|class
name|HL7MLLPNettyRouteToTest
extends|extends
name|HL7TestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"hl7decoder"
argument_list|)
DECL|method|addDecoder ()
specifier|public
name|HL7MLLPNettyDecoderFactory
name|addDecoder
parameter_list|()
throws|throws
name|Exception
block|{
name|HL7MLLPNettyDecoderFactory
name|decoder
init|=
operator|new
name|HL7MLLPNettyDecoderFactory
argument_list|()
decl_stmt|;
name|decoder
operator|.
name|setCharset
argument_list|(
literal|"iso-8859-1"
argument_list|)
expr_stmt|;
name|decoder
operator|.
name|setConvertLFtoCR
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|decoder
return|;
block|}
annotation|@
name|BindToRegistry
argument_list|(
literal|"hl7encoder"
argument_list|)
DECL|method|addEncoder ()
specifier|public
name|HL7MLLPNettyEncoderFactory
name|addEncoder
parameter_list|()
throws|throws
name|Exception
block|{
name|HL7MLLPNettyEncoderFactory
name|encoder
init|=
operator|new
name|HL7MLLPNettyEncoderFactory
argument_list|()
decl_stmt|;
name|encoder
operator|.
name|setCharset
argument_list|(
literal|"iso-8859-1"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|setConvertLFtoCR
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|encoder
return|;
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"netty4:tcp://127.0.0.1:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"?sync=true&decoder=#hl7decoder&encoder=#hl7encoder"
argument_list|)
comment|// because HL7 message contains a bunch of control chars
comment|// then the logger do not log all of the data
operator|.
name|log
argument_list|(
literal|"HL7 message: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty4:tcp://127.0.0.1:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"?sync=true&decoder=#hl7decoder&encoder=#hl7encoder"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|input
init|=
name|exchange
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
name|input
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
name|input
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
name|Message
name|response
init|=
name|createHL7AsMessage
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testSendHL7Message ()
specifier|public
name|void
name|testSendHL7Message
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// START SNIPPET: e2
name|String
name|line1
init|=
literal|"MSH|^~\\&|MYSENDER|MYRECEIVER|MYAPPLICATION||200612211200||QRY^A19|1234|P|2.4"
decl_stmt|;
name|String
name|line2
init|=
literal|"QRD|200612211200|R|I|GetPatient|||1^RD|0101701234|DEM||"
decl_stmt|;
name|StringBuilder
name|in
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|in
operator|.
name|append
argument_list|(
name|line1
argument_list|)
expr_stmt|;
name|in
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|in
operator|.
name|append
argument_list|(
name|line2
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
name|in
operator|.
name|toString
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// END SNIPPET: e2
name|String
index|[]
name|lines
init|=
name|out
operator|.
name|split
argument_list|(
literal|"\r"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MSH|^~\\&|MYSENDER||||200701011539||ADR^A19^ADR_A19|456|P|2.4"
argument_list|,
name|lines
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"MSA|AA|123"
argument_list|,
name|lines
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|// START SNIPPET: e3
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
name|adr
operator|.
name|initQuickstart
argument_list|(
literal|"ADR"
argument_list|,
literal|"A19"
argument_list|,
literal|"P"
argument_list|)
expr_stmt|;
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
name|getMessageControlID
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"456"
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
return|;
block|}
comment|// END SNIPPET: e3
block|}
end_class

end_unit

