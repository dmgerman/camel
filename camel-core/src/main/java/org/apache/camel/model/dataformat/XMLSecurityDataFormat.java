begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|model
operator|.
name|DataFormatDefinition
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

begin_comment
comment|/**  * Represents as XML Security Encrypter/Decrypter {@link DataFormat}  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"secureXML"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|XMLSecurityDataFormat
specifier|public
class|class
name|XMLSecurityDataFormat
extends|extends
name|DataFormatDefinition
block|{
DECL|field|TRIPLEDES
specifier|private
specifier|static
specifier|final
specifier|transient
name|String
name|TRIPLEDES
init|=
literal|"http://www.w3.org/2001/04/xmlenc#tripledes-cbc"
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|xmlCipherAlgorithm
specifier|private
name|String
name|xmlCipherAlgorithm
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|passPhrase
specifier|private
name|String
name|passPhrase
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|secureTag
specifier|private
name|String
name|secureTag
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|secureTagContents
specifier|private
name|boolean
name|secureTagContents
decl_stmt|;
DECL|method|XMLSecurityDataFormat ()
specifier|public
name|XMLSecurityDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"secureXML"
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, boolean secureTagContents)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|setSecureTag
argument_list|(
name|secureTag
argument_list|)
expr_stmt|;
name|this
operator|.
name|setSecureTagContents
argument_list|(
name|secureTagContents
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, boolean secureTagContents, String passPhrase)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|String
name|passPhrase
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|setSecureTag
argument_list|(
name|secureTag
argument_list|)
expr_stmt|;
name|this
operator|.
name|setSecureTagContents
argument_list|(
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPassPhrase
argument_list|(
name|passPhrase
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, boolean secureTagContents, String passPhrase, String xmlCipherAlgorithm)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|String
name|passPhrase
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|setSecureTag
argument_list|(
name|secureTag
argument_list|)
expr_stmt|;
name|this
operator|.
name|setSecureTagContents
argument_list|(
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPassPhrase
argument_list|(
name|passPhrase
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureDataFormat (DataFormat dataFormat)
specifier|protected
name|void
name|configureDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|)
block|{
if|if
condition|(
name|getSecureTag
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"secureTag"
argument_list|,
name|getSecureTag
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"secureTag"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"secureTagContents"
argument_list|,
name|isSecureTagContents
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|passPhrase
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"passPhrase"
argument_list|,
name|getPassPhrase
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"passPhrase"
argument_list|,
literal|"Just another 24 Byte key"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getXmlCipherAlgorithm
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"xmlCipherAlgorithm"
argument_list|,
name|getXmlCipherAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"xmlCipherAlgorithm"
argument_list|,
name|TRIPLEDES
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getXmlCipherAlgorithm ()
specifier|public
name|String
name|getXmlCipherAlgorithm
parameter_list|()
block|{
return|return
name|xmlCipherAlgorithm
return|;
block|}
DECL|method|setXmlCipherAlgorithm (String xmlCipherAlgorithm)
specifier|public
name|void
name|setXmlCipherAlgorithm
parameter_list|(
name|String
name|xmlCipherAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|xmlCipherAlgorithm
operator|=
name|xmlCipherAlgorithm
expr_stmt|;
block|}
DECL|method|getPassPhrase ()
specifier|public
name|String
name|getPassPhrase
parameter_list|()
block|{
return|return
name|passPhrase
return|;
block|}
DECL|method|setPassPhrase (String passPhrase)
specifier|public
name|void
name|setPassPhrase
parameter_list|(
name|String
name|passPhrase
parameter_list|)
block|{
name|this
operator|.
name|passPhrase
operator|=
name|passPhrase
expr_stmt|;
block|}
DECL|method|getSecureTag ()
specifier|public
name|String
name|getSecureTag
parameter_list|()
block|{
return|return
name|secureTag
return|;
block|}
DECL|method|setSecureTag (String secureTag)
specifier|public
name|void
name|setSecureTag
parameter_list|(
name|String
name|secureTag
parameter_list|)
block|{
name|this
operator|.
name|secureTag
operator|=
name|secureTag
expr_stmt|;
block|}
DECL|method|isSecureTagContents ()
specifier|public
name|boolean
name|isSecureTagContents
parameter_list|()
block|{
return|return
name|secureTagContents
return|;
block|}
DECL|method|setSecureTagContents (boolean secureTagContents)
specifier|public
name|void
name|setSecureTagContents
parameter_list|(
name|boolean
name|secureTagContents
parameter_list|)
block|{
name|this
operator|.
name|secureTagContents
operator|=
name|secureTagContents
expr_stmt|;
block|}
block|}
end_class

end_unit

