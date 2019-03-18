begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_class
DECL|class|MllpExceptionTest
specifier|public
class|class
name|MllpExceptionTest
extends|extends
name|MllpExceptionTestSupport
block|{
DECL|field|EXCEPTION_MESSAGE
specifier|static
specifier|final
name|String
name|EXCEPTION_MESSAGE
init|=
literal|"Test MllpException"
decl_stmt|;
DECL|field|NULL_BYTE_ARRAY
specifier|static
specifier|final
name|byte
index|[]
name|NULL_BYTE_ARRAY
init|=
literal|null
decl_stmt|;
DECL|field|EMPTY_BYTE_ARRAY
specifier|static
specifier|final
name|byte
index|[]
name|EMPTY_BYTE_ARRAY
init|=
operator|new
name|byte
index|[
literal|0
index|]
decl_stmt|;
DECL|field|instance
name|MllpException
name|instance
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|HL7_MESSAGE_BYTES
argument_list|,
name|HL7_ACKNOWLEDGEMENT_BYTES
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetHl7MessageBytes ()
specifier|public
name|void
name|testGetHl7MessageBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7MessageBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7MessageBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7MessageBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7MessageBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7MessageBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7MessageBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7MessageBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|HL7_MESSAGE_BYTES
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|HL7_MESSAGE_BYTES
argument_list|,
name|instance
operator|.
name|getHl7MessageBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|HL7_MESSAGE_BYTES
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|HL7_MESSAGE_BYTES
argument_list|,
name|instance
operator|.
name|getHl7MessageBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|HL7_MESSAGE_BYTES
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|HL7_MESSAGE_BYTES
argument_list|,
name|instance
operator|.
name|getHl7MessageBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|HL7_MESSAGE_BYTES
argument_list|,
name|HL7_ACKNOWLEDGEMENT_BYTES
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|HL7_MESSAGE_BYTES
argument_list|,
name|instance
operator|.
name|getHl7MessageBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetHl7AcknowledgementBytes ()
specifier|public
name|void
name|testGetHl7AcknowledgementBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|HL7_MESSAGE_BYTES
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|HL7_MESSAGE_BYTES
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|HL7_MESSAGE_BYTES
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|HL7_MESSAGE_BYTES
argument_list|,
name|HL7_ACKNOWLEDGEMENT_BYTES
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|HL7_ACKNOWLEDGEMENT_BYTES
argument_list|,
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
literal|null
argument_list|,
name|HL7_ACKNOWLEDGEMENT_BYTES
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|HL7_ACKNOWLEDGEMENT_BYTES
argument_list|,
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|,
name|HL7_ACKNOWLEDGEMENT_BYTES
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|HL7_ACKNOWLEDGEMENT_BYTES
argument_list|,
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|HL7_MESSAGE_BYTES
argument_list|,
name|HL7_ACKNOWLEDGEMENT_BYTES
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|HL7_ACKNOWLEDGEMENT_BYTES
argument_list|,
name|instance
operator|.
name|getHl7AcknowledgementBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNullHl7Message ()
specifier|public
name|void
name|testNullHl7Message
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|MllpComponent
operator|.
name|MLLP_LOG_PHI_PROPERTY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
literal|null
argument_list|,
name|HL7_ACKNOWLEDGEMENT_BYTES
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedMessage
argument_list|(
literal|null
argument_list|,
name|HL7_ACKNOWLEDGEMENT
argument_list|)
argument_list|,
name|instance
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEmptyHl7Message ()
specifier|public
name|void
name|testEmptyHl7Message
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|MllpComponent
operator|.
name|MLLP_LOG_PHI_PROPERTY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|,
name|HL7_ACKNOWLEDGEMENT_BYTES
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedMessage
argument_list|(
literal|null
argument_list|,
name|HL7_ACKNOWLEDGEMENT
argument_list|)
argument_list|,
name|instance
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNullHl7Acknowledgement ()
specifier|public
name|void
name|testNullHl7Acknowledgement
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|MllpComponent
operator|.
name|MLLP_LOG_PHI_PROPERTY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|HL7_MESSAGE_BYTES
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedMessage
argument_list|(
name|HL7_MESSAGE
argument_list|,
literal|null
argument_list|)
argument_list|,
name|instance
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEmptyAcknowledgement ()
specifier|public
name|void
name|testEmptyAcknowledgement
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|MllpComponent
operator|.
name|MLLP_LOG_PHI_PROPERTY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|HL7_MESSAGE_BYTES
argument_list|,
name|EMPTY_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedMessage
argument_list|(
name|HL7_MESSAGE
argument_list|,
literal|null
argument_list|)
argument_list|,
name|instance
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNullHl7Payloads ()
specifier|public
name|void
name|testNullHl7Payloads
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|MllpComponent
operator|.
name|MLLP_LOG_PHI_PROPERTY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|instance
operator|=
operator|new
name|MllpException
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|,
name|NULL_BYTE_ARRAY
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedMessage
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|,
name|instance
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|expectedMessage (String hl7Message, String hl7Acknowledgment)
specifier|private
name|String
name|expectedMessage
parameter_list|(
name|String
name|hl7Message
parameter_list|,
name|String
name|hl7Acknowledgment
parameter_list|)
block|{
name|StringBuilder
name|expectedMessageBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|expectedMessageBuilder
operator|.
name|append
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|)
expr_stmt|;
if|if
condition|(
name|hl7Message
operator|!=
literal|null
condition|)
block|{
name|expectedMessageBuilder
operator|.
name|append
argument_list|(
literal|"\n\t{hl7Message ["
argument_list|)
operator|.
name|append
argument_list|(
name|hl7Message
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"] = "
argument_list|)
operator|.
name|append
argument_list|(
name|hl7Message
operator|.
name|replaceAll
argument_list|(
literal|"\r"
argument_list|,
literal|"<0x0D CR>"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\n"
argument_list|,
literal|"<0x0A LF>"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|hl7Acknowledgment
operator|!=
literal|null
condition|)
block|{
name|expectedMessageBuilder
operator|.
name|append
argument_list|(
literal|"\n\t{hl7Acknowledgement ["
argument_list|)
operator|.
name|append
argument_list|(
name|hl7Acknowledgment
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"] = "
argument_list|)
operator|.
name|append
argument_list|(
name|hl7Acknowledgment
operator|.
name|replaceAll
argument_list|(
literal|"\r"
argument_list|,
literal|"<0x0D CR>"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\n"
argument_list|,
literal|"<0x0A LF>"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
block|}
return|return
name|expectedMessageBuilder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

