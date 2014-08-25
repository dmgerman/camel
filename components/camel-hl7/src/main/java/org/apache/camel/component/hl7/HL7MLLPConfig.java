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
name|parser
operator|.
name|Parser
import|;
end_import

begin_class
DECL|class|HL7MLLPConfig
class|class
name|HL7MLLPConfig
block|{
DECL|field|charset
specifier|private
name|Charset
name|charset
init|=
name|Charset
operator|.
name|defaultCharset
argument_list|()
decl_stmt|;
DECL|field|convertLFtoCR
specifier|private
name|boolean
name|convertLFtoCR
decl_stmt|;
comment|// HL7 MLLP start and end markers
DECL|field|startByte
specifier|private
name|char
name|startByte
init|=
literal|0x0b
decl_stmt|;
comment|// 11 decimal
DECL|field|endByte1
specifier|private
name|char
name|endByte1
init|=
literal|0x1c
decl_stmt|;
comment|// 28 decimal
DECL|field|endByte2
specifier|private
name|char
name|endByte2
init|=
literal|0x0d
decl_stmt|;
comment|// 13 decimal
DECL|field|hapiContext
specifier|private
name|HapiContext
name|hapiContext
init|=
operator|new
name|DefaultHapiContext
argument_list|()
decl_stmt|;
DECL|field|parser
specifier|private
name|Parser
name|parser
init|=
name|hapiContext
operator|.
name|getGenericParser
argument_list|()
decl_stmt|;
DECL|method|getCharset ()
specifier|public
name|Charset
name|getCharset
parameter_list|()
block|{
return|return
name|charset
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
name|this
operator|.
name|charset
operator|=
name|charset
expr_stmt|;
block|}
DECL|method|isConvertLFtoCR ()
specifier|public
name|boolean
name|isConvertLFtoCR
parameter_list|()
block|{
return|return
name|convertLFtoCR
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
name|this
operator|.
name|convertLFtoCR
operator|=
name|convertLFtoCR
expr_stmt|;
block|}
DECL|method|getStartByte ()
specifier|public
name|char
name|getStartByte
parameter_list|()
block|{
return|return
name|startByte
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
name|this
operator|.
name|startByte
operator|=
name|startByte
expr_stmt|;
block|}
DECL|method|getEndByte1 ()
specifier|public
name|char
name|getEndByte1
parameter_list|()
block|{
return|return
name|endByte1
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
name|this
operator|.
name|endByte1
operator|=
name|endByte1
expr_stmt|;
block|}
DECL|method|getEndByte2 ()
specifier|public
name|char
name|getEndByte2
parameter_list|()
block|{
return|return
name|endByte2
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
name|this
operator|.
name|endByte2
operator|=
name|endByte2
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
DECL|method|setHapiContext (HapiContext hapiContext)
specifier|public
name|void
name|setHapiContext
parameter_list|(
name|HapiContext
name|hapiContext
parameter_list|)
block|{
name|this
operator|.
name|hapiContext
operator|=
name|hapiContext
expr_stmt|;
name|this
operator|.
name|parser
operator|=
name|hapiContext
operator|.
name|getPipeParser
argument_list|()
expr_stmt|;
block|}
DECL|method|isValidate ()
specifier|public
name|boolean
name|isValidate
parameter_list|()
block|{
return|return
name|parser
operator|.
name|getParserConfiguration
argument_list|()
operator|.
name|isValidating
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
name|parser
operator|.
name|getParserConfiguration
argument_list|()
operator|.
name|setValidating
argument_list|(
name|validate
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

