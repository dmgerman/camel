begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
package|;
end_package

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|ASN1ObjectIdentifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|pkcs
operator|.
name|PKCSObjectIdentifiers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSAlgorithm
import|;
end_import

begin_enum
DECL|enum|AS2EncryptionAlgorithm
specifier|public
enum|enum
name|AS2EncryptionAlgorithm
block|{
DECL|enumConstant|AES128_CBC
name|AES128_CBC
parameter_list|(
name|CMSAlgorithm
operator|.
name|AES128_CBC
parameter_list|)
operator|,
DECL|enumConstant|AES192_CBC
constructor|AES192_CBC(CMSAlgorithm.AES192_CBC
block|)
enum|,
DECL|enumConstant|AES256_CBC
name|AES256_CBC
argument_list|(
name|CMSAlgorithm
operator|.
name|AES256_CBC
argument_list|)
operator|,
DECL|enumConstant|AES128_CCM
name|AES128_CCM
argument_list|(
name|CMSAlgorithm
operator|.
name|AES128_CCM
argument_list|)
operator|,
DECL|enumConstant|AES192_CCM
name|AES192_CCM
argument_list|(
name|CMSAlgorithm
operator|.
name|AES192_CCM
argument_list|)
operator|,
DECL|enumConstant|AES256_CCM
name|AES256_CCM
argument_list|(
name|CMSAlgorithm
operator|.
name|AES256_CCM
argument_list|)
operator|,
DECL|enumConstant|AES128_GCM
name|AES128_GCM
argument_list|(
name|CMSAlgorithm
operator|.
name|AES128_GCM
argument_list|)
operator|,
DECL|enumConstant|AES192_GCM
name|AES192_GCM
argument_list|(
name|CMSAlgorithm
operator|.
name|AES192_GCM
argument_list|)
operator|,
DECL|enumConstant|AES256_GCM
name|AES256_GCM
argument_list|(
name|CMSAlgorithm
operator|.
name|AES256_GCM
argument_list|)
operator|,
DECL|enumConstant|CAMELLIA128_CBC
name|CAMELLIA128_CBC
argument_list|(
name|CMSAlgorithm
operator|.
name|CAMELLIA128_CBC
argument_list|)
operator|,
DECL|enumConstant|CAMELLIA192_CBC
name|CAMELLIA192_CBC
argument_list|(
name|CMSAlgorithm
operator|.
name|CAMELLIA192_CBC
argument_list|)
operator|,
DECL|enumConstant|CAMELLIA256_CBC
name|CAMELLIA256_CBC
argument_list|(
name|CMSAlgorithm
operator|.
name|CAMELLIA256_CBC
argument_list|)
operator|,
DECL|enumConstant|CAST5_CBC
name|CAST5_CBC
argument_list|(
name|CMSAlgorithm
operator|.
name|CAST5_CBC
argument_list|)
operator|,
DECL|enumConstant|DES_CBC
name|DES_CBC
argument_list|(
name|CMSAlgorithm
operator|.
name|DES_CBC
argument_list|)
operator|,
DECL|enumConstant|DES_EDE3_CBC
name|DES_EDE3_CBC
argument_list|(
name|CMSAlgorithm
operator|.
name|DES_EDE3_CBC
argument_list|)
operator|,
DECL|enumConstant|GOST28147_GCFB
name|GOST28147_GCFB
argument_list|(
name|CMSAlgorithm
operator|.
name|GOST28147_GCFB
argument_list|)
operator|,
DECL|enumConstant|IDEA_CBC
name|IDEA_CBC
argument_list|(
name|CMSAlgorithm
operator|.
name|IDEA_CBC
argument_list|)
operator|,
DECL|enumConstant|RC2_CBC
name|RC2_CBC
argument_list|(
name|CMSAlgorithm
operator|.
name|RC2_CBC
argument_list|)
operator|,
DECL|enumConstant|RC4
name|RC4
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|rc4
argument_list|)
operator|,
DECL|enumConstant|SEED_CBC
name|SEED_CBC
argument_list|(
name|CMSAlgorithm
operator|.
name|SEED_CBC
argument_list|)
enum|;
end_enum

begin_decl_stmt
DECL|field|algorithmOID
specifier|private
name|ASN1ObjectIdentifier
name|algorithmOID
decl_stmt|;
end_decl_stmt

begin_constructor
DECL|method|AS2EncryptionAlgorithm (ASN1ObjectIdentifier algorithmOID)
specifier|private
name|AS2EncryptionAlgorithm
parameter_list|(
name|ASN1ObjectIdentifier
name|algorithmOID
parameter_list|)
block|{
name|this
operator|.
name|algorithmOID
operator|=
name|algorithmOID
expr_stmt|;
block|}
end_constructor

begin_function
DECL|method|getAlgorithmName ()
specifier|public
name|String
name|getAlgorithmName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
argument_list|()
return|;
block|}
end_function

begin_function
DECL|method|getAlgorithmOID ()
specifier|public
name|ASN1ObjectIdentifier
name|getAlgorithmOID
parameter_list|()
block|{
return|return
name|algorithmOID
return|;
block|}
end_function

begin_function
DECL|method|getAS2Algorithm (String algorithmName)
specifier|public
specifier|static
name|AS2EncryptionAlgorithm
name|getAS2Algorithm
parameter_list|(
name|String
name|algorithmName
parameter_list|)
block|{
return|return
name|AS2EncryptionAlgorithm
operator|.
name|valueOf
argument_list|(
name|algorithmName
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

