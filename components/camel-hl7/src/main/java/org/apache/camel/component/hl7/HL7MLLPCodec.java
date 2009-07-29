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
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolCodecFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolDecoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolEncoder
import|;
end_import

begin_comment
comment|/**  * HL7 MLLP codec.  *<p/>  * This codec supports encoding/decoding the HL7 MLLP protocol.  * It will use the default markers for start and end combination:  *<ul>  *<li>0x0b (11 decimal) = start marker</li>  *<li>0x0d (13 decimal = the \r char) = segment terminators</li>  *<li>0x1c (28 decimal) = end 1 marker</li>  *<li>0x0d (13 decimal) = end 2 marker</li>  *</ul>  *<p/>  * The decoder is used for decoding from MLLP (bytes) to String. The String will not contain any of  * the start and end markers.  *<p/>  * The encoder is used for encoding from String to MLLP (bytes). The String should<b>not</b> contain  * any of the start and end markers, the encoder will add these, and stream the string as bytes.  * Also the enocder will convert any<tt>\n</tt> (line breaks) as segment terminators to<tt>\r</tt>.  *<p/>  * This codes supports charset encoding/decoding between bytes and String. The JVM platform default charset  * is used, but the charset can be configued on this codec using the setter method.  * The decoder will use the JVM platform default charset for decoding, but the charset can be configued on the this codec.  */
end_comment

begin_class
DECL|class|HL7MLLPCodec
specifier|public
class|class
name|HL7MLLPCodec
implements|implements
name|ProtocolCodecFactory
block|{
DECL|field|config
specifier|private
name|HL7MLLPConfig
name|config
init|=
operator|new
name|HL7MLLPConfig
argument_list|()
decl_stmt|;
DECL|method|getDecoder ()
specifier|public
name|ProtocolDecoder
name|getDecoder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|HL7MLLPDecoder
argument_list|(
name|config
argument_list|)
return|;
block|}
DECL|method|getEncoder ()
specifier|public
name|ProtocolEncoder
name|getEncoder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|HL7MLLPEncoder
argument_list|(
name|config
argument_list|)
return|;
block|}
DECL|method|setCharset (Charset charset)
specifier|public
name|void
name|setCharset
parameter_list|(
name|Charset
name|charset
parameter_list|)
block|{
name|config
operator|.
name|setCharset
argument_list|(
name|charset
argument_list|)
expr_stmt|;
block|}
DECL|method|setCharset (String charsetName)
specifier|public
name|void
name|setCharset
parameter_list|(
name|String
name|charsetName
parameter_list|)
block|{
name|config
operator|.
name|setCharset
argument_list|(
name|Charset
operator|.
name|forName
argument_list|(
name|charsetName
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getCharset ()
specifier|public
name|Charset
name|getCharset
parameter_list|()
block|{
return|return
name|config
operator|.
name|getCharset
argument_list|()
return|;
block|}
DECL|method|isConvertLFtoCR ()
specifier|public
name|boolean
name|isConvertLFtoCR
parameter_list|()
block|{
return|return
name|config
operator|.
name|isConvertLFtoCR
argument_list|()
return|;
block|}
DECL|method|setConvertLFtoCR (boolean convertLFtoCR)
specifier|public
name|void
name|setConvertLFtoCR
parameter_list|(
name|boolean
name|convertLFtoCR
parameter_list|)
block|{
name|config
operator|.
name|setConvertLFtoCR
argument_list|(
name|convertLFtoCR
argument_list|)
expr_stmt|;
block|}
DECL|method|getStartByte ()
specifier|public
name|char
name|getStartByte
parameter_list|()
block|{
return|return
name|config
operator|.
name|getStartByte
argument_list|()
return|;
block|}
DECL|method|setStartByte (char startByte)
specifier|public
name|void
name|setStartByte
parameter_list|(
name|char
name|startByte
parameter_list|)
block|{
name|config
operator|.
name|setStartByte
argument_list|(
name|startByte
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndByte1 ()
specifier|public
name|char
name|getEndByte1
parameter_list|()
block|{
return|return
name|config
operator|.
name|getEndByte1
argument_list|()
return|;
block|}
DECL|method|setEndByte1 (char endByte1)
specifier|public
name|void
name|setEndByte1
parameter_list|(
name|char
name|endByte1
parameter_list|)
block|{
name|config
operator|.
name|setEndByte1
argument_list|(
name|endByte1
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndByte2 ()
specifier|public
name|char
name|getEndByte2
parameter_list|()
block|{
return|return
name|config
operator|.
name|getEndByte2
argument_list|()
return|;
block|}
DECL|method|setEndByte2 (char endByte2)
specifier|public
name|void
name|setEndByte2
parameter_list|(
name|char
name|endByte2
parameter_list|)
block|{
name|config
operator|.
name|setEndByte2
argument_list|(
name|endByte2
argument_list|)
expr_stmt|;
block|}
DECL|method|isValidate ()
specifier|public
name|boolean
name|isValidate
parameter_list|()
block|{
return|return
name|config
operator|.
name|isValidate
argument_list|()
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
name|config
operator|.
name|setValidate
argument_list|(
name|validate
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

