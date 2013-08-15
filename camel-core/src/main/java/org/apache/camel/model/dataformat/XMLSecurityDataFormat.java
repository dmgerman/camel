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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|NamespaceAware
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
name|jsse
operator|.
name|KeyStoreParameters
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
implements|implements
name|NamespaceAware
block|{
DECL|field|TRIPLEDES
specifier|private
specifier|static
specifier|final
name|String
name|TRIPLEDES
init|=
literal|"http://www.w3.org/2001/04/xmlenc#tripledes-cbc"
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|xmlCipherAlgorithm
specifier|private
name|String
name|xmlCipherAlgorithm
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|passPhrase
specifier|private
name|String
name|passPhrase
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|secureTag
specifier|private
name|String
name|secureTag
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|secureTagContents
specifier|private
name|Boolean
name|secureTagContents
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|keyCipherAlgorithm
specifier|private
name|String
name|keyCipherAlgorithm
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|recipientKeyAlias
specifier|private
name|String
name|recipientKeyAlias
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|keyOrTrustStoreParametersId
specifier|private
name|String
name|keyOrTrustStoreParametersId
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|keyPassword
specifier|private
name|String
name|keyPassword
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|digestAlgorithm
specifier|private
name|String
name|digestAlgorithm
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|mgfAlgorithm
specifier|private
name|String
name|mgfAlgorithm
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|keyOrTrustStoreParameters
specifier|private
name|KeyStoreParameters
name|keyOrTrustStoreParameters
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|namespaces
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
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
DECL|method|XMLSecurityDataFormat (String secureTag, Map<String, String> namespaces, boolean secureTagContents)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
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
name|this
operator|.
name|setNamespaces
argument_list|(
name|namespaces
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
argument_list|(
name|secureTag
argument_list|,
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
DECL|method|XMLSecurityDataFormat (String secureTag, Map<String, String> namespaces, boolean secureTagContents, String passPhrase)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|String
name|passPhrase
parameter_list|)
block|{
name|this
argument_list|(
name|secureTag
argument_list|,
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
name|setNamespaces
argument_list|(
name|namespaces
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
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|,
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
DECL|method|XMLSecurityDataFormat (String secureTag, Map<String, String> namespaces, boolean secureTagContents, String passPhrase, String xmlCipherAlgorithm)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
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
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|,
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
name|this
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated  use {{@link #XMLSecurityDataFormat(String, boolean, String, String, String, String)} or       *                  {{@link #XMLSecurityDataFormat(String, boolean, String, String, String, KeyStoreParameters)} instead      */
annotation|@
name|Deprecated
DECL|method|XMLSecurityDataFormat (String secureTag, boolean secureTagContents, String recipientKeyAlias, String xmlCipherAlgorithm, String keyCipherAlgorithm)
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
name|recipientKeyAlias
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|,
name|String
name|keyCipherAlgorithm
parameter_list|)
block|{
name|this
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setRecipientKeyAlias
argument_list|(
name|recipientKeyAlias
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|keyCipherAlgorithm
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, boolean secureTagContents, String recipientKeyAlias, String xmlCipherAlgorithm, String keyCipherAlgorithm, String keyOrTrustStoreParametersId)
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
name|recipientKeyAlias
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|,
name|String
name|keyCipherAlgorithm
parameter_list|,
name|String
name|keyOrTrustStoreParametersId
parameter_list|)
block|{
name|this
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setRecipientKeyAlias
argument_list|(
name|recipientKeyAlias
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|keyCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyOrTrustStoreParametersId
argument_list|(
name|keyOrTrustStoreParametersId
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, boolean secureTagContents, String recipientKeyAlias, String xmlCipherAlgorithm, String keyCipherAlgorithm, KeyStoreParameters keyOrTrustStoreParameters)
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
name|recipientKeyAlias
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|,
name|String
name|keyCipherAlgorithm
parameter_list|,
name|KeyStoreParameters
name|keyOrTrustStoreParameters
parameter_list|)
block|{
name|this
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setRecipientKeyAlias
argument_list|(
name|recipientKeyAlias
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|keyCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyOrTrustStoreParameters
argument_list|(
name|keyOrTrustStoreParameters
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, boolean secureTagContents, String recipientKeyAlias, String xmlCipherAlgorithm, String keyCipherAlgorithm, String keyOrTrustStoreParametersId, String keyPassword)
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
name|recipientKeyAlias
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|,
name|String
name|keyCipherAlgorithm
parameter_list|,
name|String
name|keyOrTrustStoreParametersId
parameter_list|,
name|String
name|keyPassword
parameter_list|)
block|{
name|this
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setRecipientKeyAlias
argument_list|(
name|recipientKeyAlias
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|keyCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyOrTrustStoreParametersId
argument_list|(
name|keyOrTrustStoreParametersId
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyPassword
argument_list|(
name|keyPassword
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, boolean secureTagContents, String recipientKeyAlias, String xmlCipherAlgorithm, String keyCipherAlgorithm, KeyStoreParameters keyOrTrustStoreParameters, String keyPassword)
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
name|recipientKeyAlias
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|,
name|String
name|keyCipherAlgorithm
parameter_list|,
name|KeyStoreParameters
name|keyOrTrustStoreParameters
parameter_list|,
name|String
name|keyPassword
parameter_list|)
block|{
name|this
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setRecipientKeyAlias
argument_list|(
name|recipientKeyAlias
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|keyCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyOrTrustStoreParameters
argument_list|(
name|keyOrTrustStoreParameters
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyPassword
argument_list|(
name|keyPassword
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated  use {{@link #XMLSecurityDataFormat(String, Map, boolean, String, String, String, String)} or       *                  {{@link #XMLSecurityDataFormat(String, Map, boolean, String, String, String, KeyStoreParameters)} instead      */
annotation|@
name|Deprecated
DECL|method|XMLSecurityDataFormat (String secureTag, Map<String, String> namespaces, boolean secureTagContents, String recipientKeyAlias, String xmlCipherAlgorithm, String keyCipherAlgorithm)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|String
name|recipientKeyAlias
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|,
name|String
name|keyCipherAlgorithm
parameter_list|)
block|{
name|this
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setRecipientKeyAlias
argument_list|(
name|recipientKeyAlias
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|keyCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, Map<String, String> namespaces, boolean secureTagContents, String recipientKeyAlias, String xmlCipherAlgorithm, String keyCipherAlgorithm, String keyOrTrustStoreParametersId)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|String
name|recipientKeyAlias
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|,
name|String
name|keyCipherAlgorithm
parameter_list|,
name|String
name|keyOrTrustStoreParametersId
parameter_list|)
block|{
name|this
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setRecipientKeyAlias
argument_list|(
name|recipientKeyAlias
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|keyCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyOrTrustStoreParametersId
argument_list|(
name|keyOrTrustStoreParametersId
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, Map<String, String> namespaces, boolean secureTagContents, String recipientKeyAlias, String xmlCipherAlgorithm, String keyCipherAlgorithm, KeyStoreParameters keyOrTrustStoreParameters)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|String
name|recipientKeyAlias
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|,
name|String
name|keyCipherAlgorithm
parameter_list|,
name|KeyStoreParameters
name|keyOrTrustStoreParameters
parameter_list|)
block|{
name|this
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setRecipientKeyAlias
argument_list|(
name|recipientKeyAlias
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|keyCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyOrTrustStoreParameters
argument_list|(
name|keyOrTrustStoreParameters
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, Map<String, String> namespaces, boolean secureTagContents, String recipientKeyAlias, String xmlCipherAlgorithm, String keyCipherAlgorithm, String keyOrTrustStoreParametersId, String keyPassword)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|String
name|recipientKeyAlias
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|,
name|String
name|keyCipherAlgorithm
parameter_list|,
name|String
name|keyOrTrustStoreParametersId
parameter_list|,
name|String
name|keyPassword
parameter_list|)
block|{
name|this
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setRecipientKeyAlias
argument_list|(
name|recipientKeyAlias
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|keyCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyOrTrustStoreParametersId
argument_list|(
name|keyOrTrustStoreParametersId
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyPassword
argument_list|(
name|keyPassword
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, Map<String, String> namespaces, boolean secureTagContents, String recipientKeyAlias, String xmlCipherAlgorithm, String keyCipherAlgorithm, KeyStoreParameters keyOrTrustStoreParameters, String keyPassword)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|String
name|recipientKeyAlias
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|,
name|String
name|keyCipherAlgorithm
parameter_list|,
name|KeyStoreParameters
name|keyOrTrustStoreParameters
parameter_list|,
name|String
name|keyPassword
parameter_list|)
block|{
name|this
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setRecipientKeyAlias
argument_list|(
name|recipientKeyAlias
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|keyCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyOrTrustStoreParameters
argument_list|(
name|keyOrTrustStoreParameters
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyPassword
argument_list|(
name|keyPassword
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, Map<String, String> namespaces, boolean secureTagContents, String recipientKeyAlias, String xmlCipherAlgorithm, String keyCipherAlgorithm, KeyStoreParameters keyOrTrustStoreParameters, String keyPassword, String digestAlgorithm)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|String
name|recipientKeyAlias
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|,
name|String
name|keyCipherAlgorithm
parameter_list|,
name|KeyStoreParameters
name|keyOrTrustStoreParameters
parameter_list|,
name|String
name|keyPassword
parameter_list|,
name|String
name|digestAlgorithm
parameter_list|)
block|{
name|this
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setRecipientKeyAlias
argument_list|(
name|recipientKeyAlias
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|keyCipherAlgorithm
argument_list|)
expr_stmt|;
name|this
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyOrTrustStoreParameters
argument_list|(
name|keyOrTrustStoreParameters
argument_list|)
expr_stmt|;
name|this
operator|.
name|setKeyPassword
argument_list|(
name|keyPassword
argument_list|)
expr_stmt|;
name|this
operator|.
name|setDigestAlgorithm
argument_list|(
name|digestAlgorithm
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
operator|.
name|getBytes
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
if|if
condition|(
name|getKeyCipherAlgorithm
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"keyCipherAlgorithm"
argument_list|,
name|getKeyCipherAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getRecipientKeyAlias
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"recipientKeyAlias"
argument_list|,
name|getRecipientKeyAlias
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getKeyOrTrustStoreParametersId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"keyOrTrustStoreParametersId"
argument_list|,
name|getKeyOrTrustStoreParametersId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keyOrTrustStoreParameters
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"keyOrTrustStoreParameters"
argument_list|,
name|this
operator|.
name|keyOrTrustStoreParameters
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namespaces
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"namespaces"
argument_list|,
name|this
operator|.
name|namespaces
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keyPassword
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"keyPassword"
argument_list|,
name|this
operator|.
name|getKeyPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|digestAlgorithm
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"digestAlgorithm"
argument_list|,
name|this
operator|.
name|getDigestAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mgfAlgorithm
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|dataFormat
argument_list|,
literal|"mgfAlgorithm"
argument_list|,
name|this
operator|.
name|getMgfAlgorithm
argument_list|()
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
DECL|method|getSecureTagContents ()
specifier|public
name|Boolean
name|getSecureTagContents
parameter_list|()
block|{
return|return
name|secureTagContents
return|;
block|}
DECL|method|setSecureTagContents (Boolean secureTagContents)
specifier|public
name|void
name|setSecureTagContents
parameter_list|(
name|Boolean
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
DECL|method|isSecureTagContents ()
specifier|public
name|boolean
name|isSecureTagContents
parameter_list|()
block|{
return|return
name|secureTagContents
operator|!=
literal|null
operator|&&
name|secureTagContents
return|;
block|}
DECL|method|setKeyCipherAlgorithm (String keyCipherAlgorithm)
specifier|public
name|void
name|setKeyCipherAlgorithm
parameter_list|(
name|String
name|keyCipherAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|keyCipherAlgorithm
operator|=
name|keyCipherAlgorithm
expr_stmt|;
block|}
DECL|method|getKeyCipherAlgorithm ()
specifier|public
name|String
name|getKeyCipherAlgorithm
parameter_list|()
block|{
return|return
name|keyCipherAlgorithm
return|;
block|}
DECL|method|setRecipientKeyAlias (String recipientKeyAlias)
specifier|public
name|void
name|setRecipientKeyAlias
parameter_list|(
name|String
name|recipientKeyAlias
parameter_list|)
block|{
name|this
operator|.
name|recipientKeyAlias
operator|=
name|recipientKeyAlias
expr_stmt|;
block|}
DECL|method|getRecipientKeyAlias ()
specifier|public
name|String
name|getRecipientKeyAlias
parameter_list|()
block|{
return|return
name|recipientKeyAlias
return|;
block|}
DECL|method|setKeyOrTrustStoreParametersId (String id)
specifier|public
name|void
name|setKeyOrTrustStoreParametersId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|keyOrTrustStoreParametersId
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getKeyOrTrustStoreParametersId ()
specifier|public
name|String
name|getKeyOrTrustStoreParametersId
parameter_list|()
block|{
return|return
name|this
operator|.
name|keyOrTrustStoreParametersId
return|;
block|}
DECL|method|setKeyOrTrustStoreParameters (KeyStoreParameters keyOrTrustStoreParameters)
specifier|private
name|void
name|setKeyOrTrustStoreParameters
parameter_list|(
name|KeyStoreParameters
name|keyOrTrustStoreParameters
parameter_list|)
block|{
name|this
operator|.
name|keyOrTrustStoreParameters
operator|=
name|keyOrTrustStoreParameters
expr_stmt|;
block|}
DECL|method|getKeyPassword ()
specifier|public
name|String
name|getKeyPassword
parameter_list|()
block|{
return|return
name|this
operator|.
name|keyPassword
return|;
block|}
DECL|method|setKeyPassword (String keyPassword)
specifier|public
name|void
name|setKeyPassword
parameter_list|(
name|String
name|keyPassword
parameter_list|)
block|{
name|this
operator|.
name|keyPassword
operator|=
name|keyPassword
expr_stmt|;
block|}
DECL|method|getDigestAlgorithm ()
specifier|public
name|String
name|getDigestAlgorithm
parameter_list|()
block|{
return|return
name|digestAlgorithm
return|;
block|}
DECL|method|setDigestAlgorithm (String digestAlgorithm)
specifier|public
name|void
name|setDigestAlgorithm
parameter_list|(
name|String
name|digestAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|digestAlgorithm
operator|=
name|digestAlgorithm
expr_stmt|;
block|}
DECL|method|getMgfAlgorithm ()
specifier|public
name|String
name|getMgfAlgorithm
parameter_list|()
block|{
return|return
name|mgfAlgorithm
return|;
block|}
DECL|method|setMgfAlgorithm (String mgfAlgorithm)
specifier|public
name|void
name|setMgfAlgorithm
parameter_list|(
name|String
name|mgfAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|mgfAlgorithm
operator|=
name|mgfAlgorithm
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setNamespaces (Map<String, String> nspaces)
specifier|public
name|void
name|setNamespaces
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nspaces
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|namespaces
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|namespaces
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|namespaces
operator|.
name|putAll
argument_list|(
name|nspaces
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

