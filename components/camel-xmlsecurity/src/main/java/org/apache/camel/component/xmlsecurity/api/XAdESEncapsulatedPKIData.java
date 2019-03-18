begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmlsecurity.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmlsecurity
operator|.
name|api
package|;
end_package

begin_comment
comment|/**  * Class representing the Encapsulated PKI Data of the XAdES specification.  */
end_comment

begin_class
DECL|class|XAdESEncapsulatedPKIData
specifier|public
class|class
name|XAdESEncapsulatedPKIData
block|{
DECL|field|base64Conent
specifier|private
specifier|final
name|String
name|base64Conent
decl_stmt|;
DECL|field|encoding
specifier|private
specifier|final
name|String
name|encoding
decl_stmt|;
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
comment|/**      * Constructor      *       * @param base64Conent      *            base64 encoded content      * @param encoding      *            , can be<code>null</code> or empty; encoding      *            http://uri.etsi.org/01903/v1.2.2#DER for denoting that the      *            original PKI data were ASN.1 data encoded in DER.      *            http://uri.etsi.org/01903/v1.2.2#BER for denoting that the      *            original PKI data were ASN.1 data encoded in BER.      *            http://uri.etsi.org/01903/v1.2.2#CER for denoting that the      *            original PKI data were ASN.1 data encoded in CER.      *            http://uri.etsi.org/01903/v1.2.2#PER for denoting that the      *            original PKI data were ASN.1 data encoded in PER.      *            http://uri.etsi.org/01903/v1.2.2#XER for denoting that the      *            original PKI data were ASN.1 data encoded in XER.      *       * @param id      *            ID for the Id attribute, can be<code>null</code>      * @throws IllegalArgumentException      *             if<tt>base64Conent</tt> is<code>null</code> or empty      */
DECL|method|XAdESEncapsulatedPKIData (String base64Conent, String encoding, String id)
specifier|public
name|XAdESEncapsulatedPKIData
parameter_list|(
name|String
name|base64Conent
parameter_list|,
name|String
name|encoding
parameter_list|,
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|base64Conent
operator|==
literal|null
operator|||
name|base64Conent
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value for parameter 'base64Conent' is null or empty"
argument_list|)
throw|;
block|}
name|this
operator|.
name|base64Conent
operator|=
name|base64Conent
expr_stmt|;
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
comment|/**      * Returns the base 64 encoded content. Cannot be<code>null</code> or      * empty.      */
DECL|method|getBase64Conent ()
specifier|public
name|String
name|getBase64Conent
parameter_list|()
block|{
return|return
name|base64Conent
return|;
block|}
comment|/**      * Returns the character encoding of the content. Cannot be      *<code>null</code> or empty.      */
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
block|}
end_class

end_unit

