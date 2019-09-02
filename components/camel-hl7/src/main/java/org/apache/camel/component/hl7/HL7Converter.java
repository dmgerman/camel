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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|DefaultModelClassFactory
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
name|ParserConfiguration
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
name|UnexpectedSegmentBehaviourEnum
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
name|ValidationContextFactory
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
name|Converter
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
name|converter
operator|.
name|IOConverter
import|;
end_import

begin_comment
comment|/**  * HL7 converters.  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|generateLoader
operator|=
literal|true
argument_list|)
DECL|class|HL7Converter
specifier|public
specifier|final
class|class
name|HL7Converter
block|{
DECL|field|DEFAULT_CONTEXT
specifier|private
specifier|static
specifier|final
name|HapiContext
name|DEFAULT_CONTEXT
decl_stmt|;
static|static
block|{
name|ParserConfiguration
name|parserConfiguration
init|=
operator|new
name|ParserConfiguration
argument_list|()
decl_stmt|;
name|parserConfiguration
operator|.
name|setDefaultObx2Type
argument_list|(
literal|"ST"
argument_list|)
expr_stmt|;
name|parserConfiguration
operator|.
name|setInvalidObx2Type
argument_list|(
literal|"ST"
argument_list|)
expr_stmt|;
name|parserConfiguration
operator|.
name|setUnexpectedSegmentBehaviour
argument_list|(
name|UnexpectedSegmentBehaviourEnum
operator|.
name|ADD_INLINE
argument_list|)
expr_stmt|;
name|DEFAULT_CONTEXT
operator|=
operator|new
name|DefaultHapiContext
argument_list|(
name|parserConfiguration
argument_list|,
name|ValidationContextFactory
operator|.
name|noValidation
argument_list|()
argument_list|,
operator|new
name|DefaultModelClassFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|HL7Converter ()
specifier|private
name|HL7Converter
parameter_list|()
block|{
comment|// Helper class
block|}
annotation|@
name|Converter
DECL|method|toString (Message message)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|HL7Exception
block|{
return|return
name|message
operator|.
name|encode
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteArray (Message message, Exchange exchange)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|Message
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|HL7Exception
throws|,
name|IOException
block|{
return|return
name|IOConverter
operator|.
name|toByteArray
argument_list|(
name|message
operator|.
name|encode
argument_list|()
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toMessage (String body)
specifier|public
specifier|static
name|Message
name|toMessage
parameter_list|(
name|String
name|body
parameter_list|)
throws|throws
name|HL7Exception
block|{
return|return
name|DEFAULT_CONTEXT
operator|.
name|getGenericParser
argument_list|()
operator|.
name|parse
argument_list|(
name|body
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toMessage (byte[] body, Exchange exchange)
specifier|public
specifier|static
name|Message
name|toMessage
parameter_list|(
name|byte
index|[]
name|body
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|HL7Exception
throws|,
name|IOException
block|{
return|return
name|DEFAULT_CONTEXT
operator|.
name|getGenericParser
argument_list|()
operator|.
name|parse
argument_list|(
name|IOConverter
operator|.
name|toString
argument_list|(
name|body
argument_list|,
name|exchange
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

