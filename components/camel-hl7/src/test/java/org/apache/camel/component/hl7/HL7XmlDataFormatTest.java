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
name|DefaultHapiContext
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
name|HapiContext
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
name|parser
operator|.
name|GenericParser
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
name|parser
operator|.
name|Parser
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
name|util
operator|.
name|Terser
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
name|validation
operator|.
name|impl
operator|.
name|NoValidation
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|HL7XmlDataFormatTest
specifier|public
class|class
name|HL7XmlDataFormatTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|hl7
specifier|private
name|HL7DataFormat
name|hl7
decl_stmt|;
annotation|@
name|Test
DECL|method|testUnmarshalOk ()
specifier|public
name|void
name|testUnmarshalOk
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
literal|"direct:unmarshalOk"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalOkXml ()
specifier|public
name|void
name|testUnmarshalOkXml
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
name|String
name|body
init|=
name|createHL7AsString
argument_list|()
decl_stmt|;
name|Message
name|msg
init|=
name|hl7
operator|.
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|body
argument_list|)
decl_stmt|;
name|String
name|xml
init|=
name|hl7
operator|.
name|getParser
argument_list|()
operator|.
name|encode
argument_list|(
name|msg
argument_list|,
literal|"XML"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<ORM_O01"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshalOkXml"
argument_list|,
name|xml
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Message
name|received
init|=
name|mock
operator|.
name|getReceivedExchanges
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
name|getMandatoryBody
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"O01"
argument_list|,
operator|new
name|Terser
argument_list|(
name|received
argument_list|)
operator|.
name|get
argument_list|(
literal|"MSH-9-2"
argument_list|)
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
name|HapiContext
name|hapiContext
init|=
operator|new
name|DefaultHapiContext
argument_list|()
decl_stmt|;
name|hapiContext
operator|.
name|setValidationContext
argument_list|(
operator|new
name|NoValidation
argument_list|()
argument_list|)
expr_stmt|;
name|Parser
name|p
init|=
operator|new
name|GenericParser
argument_list|(
name|hapiContext
argument_list|)
decl_stmt|;
name|hl7
operator|=
operator|new
name|HL7DataFormat
argument_list|()
expr_stmt|;
name|hl7
operator|.
name|setParser
argument_list|(
name|p
argument_list|)
expr_stmt|;
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
literal|"direct:unmarshalOk"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|hl7
argument_list|(
literal|false
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unmarshal"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshalOkXml"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|hl7
argument_list|)
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
literal|"MSH|^~\\&|REQUESTING|ICE|INHOUSE|RTH00|20080808093202||ORM^O01|0808080932027444985|P|2.4|||AL|NE|||"
decl_stmt|;
name|String
name|line2
init|=
literal|"PID|1||ICE999999^^^ICE^ICE||Testpatient^Testy^^^Mr||19740401|M|||123 Barrel Drive^^^^SW18 4RT|||||2||||||||||||||"
decl_stmt|;
name|String
name|line3
init|=
literal|"NTE|1||Free text for entering clinical details|"
decl_stmt|;
name|String
name|line4
init|=
literal|"PV1|1||^^^^^^^^Admin Location|||||||||||||||NHS|"
decl_stmt|;
name|String
name|line5
init|=
literal|"ORC|NW|213||175|REQ||||20080808093202|ahsl^^Administrator||G999999^TestDoctor^GPtests^^^^^^NAT|^^^^^^^^Admin Location | 819-6000|200808080932||RTH00||ahsl^^Administrator||"
decl_stmt|;
name|String
name|line6
init|=
literal|"OBR|1|213||CCOR^Serum Cortisol ^ JRH06|||200808080932||0.100||||||^|G999999^TestDoctor^GPtests^^^^^^NAT|819-6000|ADM162||||||820|||^^^^^R||||||||"
decl_stmt|;
name|String
name|line7
init|=
literal|"OBR|2|213||GCU^Serum Copper ^ JRH06 |||200808080932||0.100||||||^|G999999^TestDoctor^GPtests^^^^^^NAT|819-6000|ADM162||||||820|||^^^^^R||||||||"
decl_stmt|;
name|String
name|line8
init|=
literal|"OBR|3|213||THYG^Serum Thyroglobulin ^JRH06|||200808080932||0.100||||||^|G999999^TestDoctor^GPtests^^^^^^NAT|819-6000|ADM162||||||820|||^^^^^R||||||||"
decl_stmt|;
name|StringBuilder
name|body
init|=
operator|new
name|StringBuilder
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
literal|"\r"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line2
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\r"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line3
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\r"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line4
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\r"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line5
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\r"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line6
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\r"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line7
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
literal|"\r"
argument_list|)
expr_stmt|;
name|body
operator|.
name|append
argument_list|(
name|line8
argument_list|)
expr_stmt|;
return|return
name|body
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

