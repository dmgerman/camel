begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
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
name|impl
operator|.
name|JndiRegistry
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
name|spi
operator|.
name|DataFormat
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
name|message
operator|.
name|ADT_A01
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
name|QRY_A19
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
name|PID
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

begin_comment
comment|/**  * Unit test for HL7 routing.  */
end_comment

begin_class
DECL|class|HL7RouteTest
specifier|public
class|class
name|HL7RouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|HL7MLLPCodec
name|codec
init|=
operator|new
name|HL7MLLPCodec
argument_list|()
decl_stmt|;
name|codec
operator|.
name|setCharset
argument_list|(
literal|"iso-8859-1"
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"hl7codec"
argument_list|,
name|codec
argument_list|)
expr_stmt|;
name|MyHL7BusinessLogic
name|logic
init|=
operator|new
name|MyHL7BusinessLogic
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"hl7service"
argument_list|,
name|logic
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|method|testSendA19 ()
specifier|public
name|void
name|testSendA19
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:a19"
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
name|in
init|=
operator|new
name|StringBuffer
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
operator|(
name|String
operator|)
name|template
operator|.
name|requestBody
argument_list|(
literal|"mina:tcp://localhost:8888?sync=true&codec=hl7codec"
argument_list|,
name|in
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
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
literal|"MSH|^~\\&|MYSENDER||||200701011539||ADR^A19||||123"
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
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
block|}
DECL|method|testSendA01 ()
specifier|public
name|void
name|testSendA01
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:a01"
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
name|String
name|line1
init|=
literal|"MSH|^~\\&|MYSENDER|MYSENDERAPP|MYCLIENT|MYCLIENTAPP|200612211200||ADT^A01|1234|P|2.4"
decl_stmt|;
name|String
name|line2
init|=
literal|"PID|||123456||Doe^John"
decl_stmt|;
name|StringBuffer
name|in
init|=
operator|new
name|StringBuffer
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
operator|(
name|String
operator|)
name|template
operator|.
name|requestBody
argument_list|(
literal|"mina:tcp://localhost:8888?sync=true&codec=hl7codec"
argument_list|,
name|in
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
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
literal|"MSH|^~\\&|MYSENDER||||200701011539||ADT^A01||||123"
argument_list|,
name|lines
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PID|||123456||Doe^John"
argument_list|,
name|lines
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
block|}
DECL|method|testSendUnknown ()
specifier|public
name|void
name|testSendUnknown
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:unknown"
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
name|String
name|line1
init|=
literal|"MSH|^~\\&|MYSENDER|MYSENDERAPP|MYCLIENT|MYCLIENTAPP|200612211200||ADT^A02|1234|P|2.4"
decl_stmt|;
name|String
name|line2
init|=
literal|"PID|||123456||Doe^John"
decl_stmt|;
name|StringBuffer
name|in
init|=
operator|new
name|StringBuffer
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
name|template
operator|.
name|requestBody
argument_list|(
literal|"mina:tcp://localhost:8888?sync=true&codec=hl7codec"
argument_list|,
name|in
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisifed
argument_list|()
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
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e1
name|DataFormat
name|hl7
init|=
operator|new
name|HL7DataFormat
argument_list|()
decl_stmt|;
comment|// we setup or HL7 listener on port 8888 (using the hl7codec) and in sync mode so we can return a response
name|from
argument_list|(
literal|"mina:tcp://localhost:8888?sync=true&codec=hl7codec"
argument_list|)
comment|// we use the HL7 data format to unmarshal from HL7 stream to the HAPI Message model
comment|// this ensures that the camel message has been enriched with hl7 specific headers to
comment|// make the routing much easier (see below)
operator|.
name|unmarshal
argument_list|(
name|hl7
argument_list|)
comment|// using choice as the content base router
operator|.
name|choice
argument_list|()
comment|// where we choose that A19 queries invoke the handleA19 method on our hl7service bean
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"hl7.msh.triggerEvent"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"A19"
argument_list|)
argument_list|)
operator|.
name|beanRef
argument_list|(
literal|"hl7service"
argument_list|,
literal|"handleA19"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a19"
argument_list|)
comment|// and A01 should invoke the handleA01 method on our hl7service bean
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"hl7.msh.triggerEvent"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"A01"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a01"
argument_list|)
operator|.
name|beanRef
argument_list|(
literal|"hl7service"
argument_list|,
literal|"handleA01"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a19"
argument_list|)
comment|// other types should go to mock:unknown
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:unknown"
argument_list|)
comment|// end choice block
operator|.
name|end
argument_list|()
comment|// marhsal response back
operator|.
name|marshal
argument_list|(
name|hl7
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
comment|// START SNIPPET: e2
DECL|class|MyHL7BusinessLogic
specifier|public
class|class
name|MyHL7BusinessLogic
block|{
comment|// This is a plain POJO that has<b>no</b> imports whatsoever on Apache Camel.
comment|// its a plain POJO only importing the HAPI library so we can much easier work with the HL7 format.
DECL|method|handleA19 (Message msg)
specifier|public
name|Message
name|handleA19
parameter_list|(
name|Message
name|msg
parameter_list|)
throws|throws
name|Exception
block|{
comment|// here you can have your business logic for A19 messages
name|assertTrue
argument_list|(
name|msg
operator|instanceof
name|QRY_A19
argument_list|)
expr_stmt|;
comment|// just return the same dummy response
return|return
name|createADR19Message
argument_list|()
return|;
block|}
DECL|method|handleA01 (Message msg)
specifier|public
name|Message
name|handleA01
parameter_list|(
name|Message
name|msg
parameter_list|)
throws|throws
name|Exception
block|{
comment|// here you can have your business logic for A01 messages
name|assertTrue
argument_list|(
name|msg
operator|instanceof
name|ADT_A01
argument_list|)
expr_stmt|;
comment|// just return the same dummy response
return|return
name|createADT01Message
argument_list|()
return|;
block|}
block|}
comment|// END SNIPPET: e2
DECL|method|createADR19Message ()
specifier|private
specifier|static
name|Message
name|createADR19Message
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
DECL|method|createADT01Message ()
specifier|private
specifier|static
name|Message
name|createADT01Message
parameter_list|()
throws|throws
name|Exception
block|{
name|ADT_A01
name|adt
init|=
operator|new
name|ADT_A01
argument_list|()
decl_stmt|;
comment|// Populate the MSH Segment
name|MSH
name|mshSegment
init|=
name|adt
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
literal|"ADT"
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
literal|"A01"
argument_list|)
expr_stmt|;
comment|// Populate the PID Segment
name|PID
name|pid
init|=
name|adt
operator|.
name|getPID
argument_list|()
decl_stmt|;
name|pid
operator|.
name|getPatientName
argument_list|(
literal|0
argument_list|)
operator|.
name|getFamilyName
argument_list|()
operator|.
name|getSurname
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"Doe"
argument_list|)
expr_stmt|;
name|pid
operator|.
name|getPatientName
argument_list|(
literal|0
argument_list|)
operator|.
name|getGivenName
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"John"
argument_list|)
expr_stmt|;
name|pid
operator|.
name|getPatientIdentifierList
argument_list|(
literal|0
argument_list|)
operator|.
name|getID
argument_list|()
operator|.
name|setValue
argument_list|(
literal|"123456"
argument_list|)
expr_stmt|;
return|return
name|adt
operator|.
name|getMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

