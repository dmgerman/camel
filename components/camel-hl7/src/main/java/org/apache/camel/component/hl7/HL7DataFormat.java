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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|spi
operator|.
name|DataFormat
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
name|ServiceSupport
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
name|util
operator|.
name|ExchangeHelper
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import static
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
name|HL7Constants
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * HL7 DataFormat (supports v2.x of the HL7 protocol).  *<p/>  * This data format supports two operations:  *<ul>  *<li>marshal = from Message to String (can be used when returning as response using the HL7 MLLP codec)</li>  *<li>unmarshal = from String to Message (can be used when receiving streamed data from the HL7 MLLP codec).  *   This operation will also enrich the message by adding the MSH fields (MSH-3 to MSH-12) as headers on the message.</li>  *</ul>  *<p/>  * Uses the<a href="http://hl7api.sourceforge.net/index.html">HAPI (HL7 API)</a> for HL7 parsing.  *<p/>  * Uses the default GenericParser from the HAPI API. This DataFormat<b>only</b> supports both the EDI based HL7  * messages and the XML based messages.  *<p/>  * The<tt>unmarshal</tt> operation adds these MSH fields as headers on the Camel message (key, MSH-field):  *<ul>  *<li>CamelHL7SendingApplication = MSH-3</li>  *<li>CamelHL7SendingFacility = MSH-4</li>  *<li>CamelHL7ReceivingApplication = MSH-5</li>  *<li>CamelHL7ReceivingFacility = MSH-6</li>  *<li>CamelHL7Timestamp = MSH-7</li>  *<li>CamelHL7Security = MSH-8</li>  *<li>CamelHL7MessageType = MSH-9-1</li>  *<li>CamelHL7TriggerEvent = MSH-9-2</li>  *<li>CamelHL7MessageControl = MSH-10</li>  *<li>CamelHL7ProcessingId = MSH-11</li>  *<li>CamelHL7VersionId = MSH-12</li>  *<li>CamelHL7Charset = MSH-18</li>  *</ul>  * All headers are String types.  *<p/>  *  * @see org.apache.camel.component.hl7.HL7MLLPCodec  */
end_comment

begin_class
DECL|class|HL7DataFormat
specifier|public
class|class
name|HL7DataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
block|{
DECL|field|HEADER_MAP
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|HEADER_MAP
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|hapiContext
specifier|private
name|HapiContext
name|hapiContext
decl_stmt|;
DECL|field|parser
specifier|private
name|Parser
name|parser
decl_stmt|;
DECL|field|validate
specifier|private
name|boolean
name|validate
init|=
literal|true
decl_stmt|;
static|static
block|{
name|HEADER_MAP
operator|.
name|put
argument_list|(
name|HL7_SENDING_APPLICATION
argument_list|,
literal|"MSH-3"
argument_list|)
expr_stmt|;
name|HEADER_MAP
operator|.
name|put
argument_list|(
name|HL7_SENDING_FACILITY
argument_list|,
literal|"MSH-4"
argument_list|)
expr_stmt|;
name|HEADER_MAP
operator|.
name|put
argument_list|(
name|HL7_RECEIVING_APPLICATION
argument_list|,
literal|"MSH-5"
argument_list|)
expr_stmt|;
name|HEADER_MAP
operator|.
name|put
argument_list|(
name|HL7_RECEIVING_FACILITY
argument_list|,
literal|"MSH-6"
argument_list|)
expr_stmt|;
name|HEADER_MAP
operator|.
name|put
argument_list|(
name|HL7_TIMESTAMP
argument_list|,
literal|"MSH-7"
argument_list|)
expr_stmt|;
name|HEADER_MAP
operator|.
name|put
argument_list|(
name|HL7_SECURITY
argument_list|,
literal|"MSH-8"
argument_list|)
expr_stmt|;
name|HEADER_MAP
operator|.
name|put
argument_list|(
name|HL7_MESSAGE_TYPE
argument_list|,
literal|"MSH-9-1"
argument_list|)
expr_stmt|;
name|HEADER_MAP
operator|.
name|put
argument_list|(
name|HL7_TRIGGER_EVENT
argument_list|,
literal|"MSH-9-2"
argument_list|)
expr_stmt|;
name|HEADER_MAP
operator|.
name|put
argument_list|(
name|HL7_MESSAGE_CONTROL
argument_list|,
literal|"MSH-10"
argument_list|)
expr_stmt|;
name|HEADER_MAP
operator|.
name|put
argument_list|(
name|HL7_PROCESSING_ID
argument_list|,
literal|"MSH-11"
argument_list|)
expr_stmt|;
name|HEADER_MAP
operator|.
name|put
argument_list|(
name|HL7_VERSION_ID
argument_list|,
literal|"MSH-12"
argument_list|)
expr_stmt|;
name|HEADER_MAP
operator|.
name|put
argument_list|(
name|HL7_CHARSET
argument_list|,
literal|"MSH-18"
argument_list|)
expr_stmt|;
block|}
DECL|method|marshal (Exchange exchange, Object body, OutputStream outputStream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|message
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|Message
operator|.
name|class
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|String
name|charsetName
init|=
name|HL7Charset
operator|.
name|getCharsetName
argument_list|(
name|message
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|String
name|encoded
init|=
name|HL7Converter
operator|.
name|encode
argument_list|(
name|message
argument_list|,
name|parser
argument_list|)
decl_stmt|;
name|outputStream
operator|.
name|write
argument_list|(
name|encoded
operator|.
name|getBytes
argument_list|(
name|charsetName
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|unmarshal (Exchange exchange, InputStream inputStream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|body
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|inputStream
argument_list|)
decl_stmt|;
name|String
name|charsetName
init|=
name|HL7Charset
operator|.
name|getCharsetName
argument_list|(
name|body
argument_list|,
name|guessCharsetName
argument_list|(
name|body
argument_list|,
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|bodyAsString
init|=
operator|new
name|String
argument_list|(
name|body
argument_list|,
name|charsetName
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|HL7Converter
operator|.
name|parse
argument_list|(
name|bodyAsString
argument_list|,
name|parser
argument_list|)
decl_stmt|;
comment|// add MSH fields as message out headers
name|Terser
name|terser
init|=
operator|new
name|Terser
argument_list|(
name|message
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|HEADER_MAP
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|terser
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HL7_CONTEXT
argument_list|,
name|hapiContext
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|charsetName
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
DECL|method|isValidate ()
specifier|public
name|boolean
name|isValidate
parameter_list|()
block|{
return|return
name|validate
return|;
block|}
DECL|method|setValidate (boolean validate)
specifier|public
name|void
name|setValidate
parameter_list|(
name|boolean
name|validate
parameter_list|)
block|{
name|this
operator|.
name|validate
operator|=
name|validate
expr_stmt|;
block|}
DECL|method|getHapiContext ()
specifier|public
name|HapiContext
name|getHapiContext
parameter_list|()
block|{
return|return
name|hapiContext
return|;
block|}
DECL|method|setHapiContext (HapiContext context)
specifier|public
name|void
name|setHapiContext
parameter_list|(
name|HapiContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|hapiContext
operator|=
name|context
expr_stmt|;
block|}
DECL|method|getParser ()
specifier|public
name|Parser
name|getParser
parameter_list|()
block|{
return|return
name|parser
return|;
block|}
DECL|method|setParser (Parser parser)
specifier|public
name|void
name|setParser
parameter_list|(
name|Parser
name|parser
parameter_list|)
block|{
name|this
operator|.
name|parser
operator|=
name|parser
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|hapiContext
operator|==
literal|null
condition|)
block|{
name|hapiContext
operator|=
operator|new
name|DefaultHapiContext
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|parser
operator|==
literal|null
condition|)
block|{
name|parser
operator|=
name|hapiContext
operator|.
name|getGenericParser
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|validate
condition|)
block|{
name|parser
operator|.
name|setValidationContext
argument_list|(
name|ValidationContextFactory
operator|.
name|noValidation
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
comment|/**      * In HL7 the charset of the message can be set in MSH-18,      * but you need to decode the input stream in order to be able to read MSH-18.      * This works well for differentiating e.g. between ASCII, UTF-8 and ISI-8859 charsets,      * but not for multi-byte charsets like UTF-16, Big5 etc.      *      * This method is called to "guess" the initial encoding, and subclasses can overwrite it      * using 3rd party libraries like ICU4J that provide a CharsetDetector.      *      * The implementation in this class just assumes the charset defined in the exchange property or header by      * calling {@link org.apache.camel.util.IOHelper#getCharsetName(org.apache.camel.Exchange)}.      *      * @param b byte array      * @param exchange      * @return charset name      */
DECL|method|guessCharsetName (byte[] b, Exchange exchange)
specifier|protected
name|String
name|guessCharsetName
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

