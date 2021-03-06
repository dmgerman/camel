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
name|ASN1Integer
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
name|DERNull
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
name|bsi
operator|.
name|BSIObjectIdentifiers
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
name|nist
operator|.
name|NISTObjectIdentifiers
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
name|oiw
operator|.
name|OIWObjectIdentifiers
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
name|asn1
operator|.
name|pkcs
operator|.
name|RSASSAPSSparams
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
name|teletrust
operator|.
name|TeleTrusTObjectIdentifiers
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
name|x509
operator|.
name|AlgorithmIdentifier
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
name|x9
operator|.
name|X9ObjectIdentifiers
import|;
end_import

begin_interface
DECL|interface|AS2SignatureAlgorithmParams
interface|interface
name|AS2SignatureAlgorithmParams
block|{
DECL|field|SHA1ALGID
name|AlgorithmIdentifier
name|SHA1ALGID
init|=
operator|new
name|AlgorithmIdentifier
argument_list|(
name|OIWObjectIdentifiers
operator|.
name|idSHA1
argument_list|,
name|DERNull
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
DECL|field|SHA1ALGPARAMS
name|RSASSAPSSparams
name|SHA1ALGPARAMS
init|=
operator|new
name|RSASSAPSSparams
argument_list|(
name|SHA1ALGID
argument_list|,
operator|new
name|AlgorithmIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|id_mgf1
argument_list|,
name|SHA1ALGID
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|20
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|SHA224ALGID
name|AlgorithmIdentifier
name|SHA224ALGID
init|=
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha224
argument_list|,
name|DERNull
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
DECL|field|SHA224ALGPARAMS
name|RSASSAPSSparams
name|SHA224ALGPARAMS
init|=
operator|new
name|RSASSAPSSparams
argument_list|(
name|SHA224ALGID
argument_list|,
operator|new
name|AlgorithmIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|id_mgf1
argument_list|,
name|SHA224ALGID
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|28
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|SHA256ALGID
name|AlgorithmIdentifier
name|SHA256ALGID
init|=
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha256
argument_list|,
name|DERNull
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
DECL|field|SHA256ALGPARAMS
name|RSASSAPSSparams
name|SHA256ALGPARAMS
init|=
operator|new
name|RSASSAPSSparams
argument_list|(
name|SHA256ALGID
argument_list|,
operator|new
name|AlgorithmIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|id_mgf1
argument_list|,
name|SHA256ALGID
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|32
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|SHA384ALGID
name|AlgorithmIdentifier
name|SHA384ALGID
init|=
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha384
argument_list|,
name|DERNull
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
DECL|field|SHA384ALGPARAMS
name|RSASSAPSSparams
name|SHA384ALGPARAMS
init|=
operator|new
name|RSASSAPSSparams
argument_list|(
name|SHA384ALGID
argument_list|,
operator|new
name|AlgorithmIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|id_mgf1
argument_list|,
name|SHA384ALGID
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|48
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|SHA512ALGID
name|AlgorithmIdentifier
name|SHA512ALGID
init|=
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha512
argument_list|,
name|DERNull
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
DECL|field|SHA512ALGPARAMS
name|RSASSAPSSparams
name|SHA512ALGPARAMS
init|=
operator|new
name|RSASSAPSSparams
argument_list|(
name|SHA512ALGID
argument_list|,
operator|new
name|AlgorithmIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|id_mgf1
argument_list|,
name|SHA512ALGID
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|64
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|SHA3_224ALGID
name|AlgorithmIdentifier
name|SHA3_224ALGID
init|=
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha3_224
argument_list|,
name|DERNull
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
DECL|field|SHA3_224ALGPARAMS
name|RSASSAPSSparams
name|SHA3_224ALGPARAMS
init|=
operator|new
name|RSASSAPSSparams
argument_list|(
name|SHA3_224ALGID
argument_list|,
operator|new
name|AlgorithmIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|id_mgf1
argument_list|,
name|SHA3_224ALGID
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|28
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|SHA3_256ALGID
name|AlgorithmIdentifier
name|SHA3_256ALGID
init|=
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha3_256
argument_list|,
name|DERNull
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
DECL|field|SHA3_256ALGPARAMS
name|RSASSAPSSparams
name|SHA3_256ALGPARAMS
init|=
operator|new
name|RSASSAPSSparams
argument_list|(
name|SHA3_256ALGID
argument_list|,
operator|new
name|AlgorithmIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|id_mgf1
argument_list|,
name|SHA3_256ALGID
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|32
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|SHA3_384ALGID
name|AlgorithmIdentifier
name|SHA3_384ALGID
init|=
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha3_384
argument_list|,
name|DERNull
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
DECL|field|SHA3_384ALGPARAMS
name|RSASSAPSSparams
name|SHA3_384ALGPARAMS
init|=
operator|new
name|RSASSAPSSparams
argument_list|(
name|SHA3_384ALGID
argument_list|,
operator|new
name|AlgorithmIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|id_mgf1
argument_list|,
name|SHA3_384ALGID
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|48
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|SHA3_512ALGID
name|AlgorithmIdentifier
name|SHA3_512ALGID
init|=
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha3_512
argument_list|,
name|DERNull
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
DECL|field|SHA3_512ALGPARAMS
name|RSASSAPSSparams
name|SHA3_512ALGPARAMS
init|=
operator|new
name|RSASSAPSSparams
argument_list|(
name|SHA3_512ALGID
argument_list|,
operator|new
name|AlgorithmIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|id_mgf1
argument_list|,
name|SHA3_512ALGID
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|64
argument_list|)
argument_list|,
operator|new
name|ASN1Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
block|}
end_interface

begin_enum
DECL|enum|AS2SignatureAlgorithm
specifier|public
enum|enum
name|AS2SignatureAlgorithm
block|{
DECL|enumConstant|SHA3_224WITHRSA
name|SHA3_224WITHRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_rsassa_pkcs1_v1_5_with_sha3_224
argument_list|)
argument_list|,
literal|"SHA3-224"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_256WITHRSA
name|SHA3_256WITHRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_rsassa_pkcs1_v1_5_with_sha3_256
argument_list|)
argument_list|,
literal|"SHA3-256"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_384withRSA
name|SHA3_384withRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_rsassa_pkcs1_v1_5_with_sha3_384
argument_list|)
argument_list|,
literal|"SHA3-384"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_512WITHRSA
name|SHA3_512WITHRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_rsassa_pkcs1_v1_5_with_sha3_512
argument_list|)
argument_list|,
literal|"SHA3-512"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|MD5WITHRSA
name|MD5WITHRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|OIWObjectIdentifiers
operator|.
name|md5WithRSA
argument_list|)
argument_list|,
literal|"MD5"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA1WITHRSA
name|SHA1WITHRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_rsassa_pkcs1_v1_5_with_sha3_224
argument_list|)
argument_list|,
literal|"SHA1"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|MD2WITHRSA
name|MD2WITHRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_rsassa_pkcs1_v1_5_with_sha3_224
argument_list|)
argument_list|,
literal|"MD2"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA224WITHRSA
name|SHA224WITHRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|sha1WithRSAEncryption
argument_list|)
argument_list|,
literal|"SHA224"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA256WITHRSA
name|SHA256WITHRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|sha256WithRSAEncryption
argument_list|)
argument_list|,
literal|"SHA256"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA384WITHRSA
name|SHA384WITHRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|sha384WithRSAEncryption
argument_list|)
argument_list|,
literal|"SHA384"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA512WITHRSA
name|SHA512WITHRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|PKCSObjectIdentifiers
operator|.
name|sha512WithRSAEncryption
argument_list|)
argument_list|,
literal|"SHA512"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|RIPEMD128WITHRSA
name|RIPEMD128WITHRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|TeleTrusTObjectIdentifiers
operator|.
name|rsaSignatureWithripemd128
argument_list|)
argument_list|,
literal|"RIPEMD128"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|RIPEMD160WITHRSA
name|RIPEMD160WITHRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|TeleTrusTObjectIdentifiers
operator|.
name|rsaSignatureWithripemd160
argument_list|)
argument_list|,
literal|"RIPEMD160"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|RIPEMD256WITHRSA
name|RIPEMD256WITHRSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|TeleTrusTObjectIdentifiers
operator|.
name|rsaSignatureWithripemd256
argument_list|)
argument_list|,
literal|"RIPEMD256"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA224WITHDSA
name|SHA224WITHDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|dsa_with_sha224
argument_list|)
argument_list|,
literal|"SHA224"
argument_list|,
literal|"DSA"
argument_list|)
block|,
DECL|enumConstant|SHA256WITHDSA
name|SHA256WITHDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|dsa_with_sha256
argument_list|)
argument_list|,
literal|"SHA256"
argument_list|,
literal|"DSA"
argument_list|)
block|,
DECL|enumConstant|SHA384WITHDSA
name|SHA384WITHDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|dsa_with_sha384
argument_list|)
argument_list|,
literal|"SHA384"
argument_list|,
literal|"DSA"
argument_list|)
block|,
DECL|enumConstant|SHA512WITHDSA
name|SHA512WITHDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|dsa_with_sha512
argument_list|)
argument_list|,
literal|"SHA512"
argument_list|,
literal|"DSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_224WITHDSA
name|SHA3_224WITHDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_dsa_with_sha3_224
argument_list|)
argument_list|,
literal|"SHA3-224"
argument_list|,
literal|"DSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_256WITHDSA
name|SHA3_256WITHDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_dsa_with_sha3_256
argument_list|)
argument_list|,
literal|"SHA3-256"
argument_list|,
literal|"DSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_384WITHDSA
name|SHA3_384WITHDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_dsa_with_sha3_384
argument_list|)
argument_list|,
literal|"SHA3-384"
argument_list|,
literal|"DSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_512WITHDSA
name|SHA3_512WITHDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_dsa_with_sha3_512
argument_list|)
argument_list|,
literal|"SHA3-512"
argument_list|,
literal|"DSA"
argument_list|)
block|,
DECL|enumConstant|SHA1WITHDSA
name|SHA1WITHDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|OIWObjectIdentifiers
operator|.
name|dsaWithSHA1
argument_list|)
argument_list|,
literal|"SHA1"
argument_list|,
literal|"DSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_224WITHECDSA
name|SHA3_224WITHECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_ecdsa_with_sha3_224
argument_list|)
argument_list|,
literal|"SHA3-224"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_256WITHECDSA
name|SHA3_256WITHECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_ecdsa_with_sha3_256
argument_list|)
argument_list|,
literal|"SHA3-256"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_384WITHECDSA
name|SHA3_384WITHECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_ecdsa_with_sha3_384
argument_list|)
argument_list|,
literal|"SHA3-384"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_512WITHECDSA
name|SHA3_512WITHECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_ecdsa_with_sha3_512
argument_list|)
argument_list|,
literal|"SHA3-512"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA1WITHECDSA
name|SHA1WITHECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|X9ObjectIdentifiers
operator|.
name|ecdsa_with_SHA1
argument_list|)
argument_list|,
literal|"SHA1"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA224WITHECDSA
name|SHA224WITHECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|X9ObjectIdentifiers
operator|.
name|ecdsa_with_SHA224
argument_list|)
argument_list|,
literal|"SHA224"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA256WITHECDSA
name|SHA256WITHECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|X9ObjectIdentifiers
operator|.
name|ecdsa_with_SHA256
argument_list|)
argument_list|,
literal|"SHA256"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA384WITHECDSA
name|SHA384WITHECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|X9ObjectIdentifiers
operator|.
name|ecdsa_with_SHA384
argument_list|)
argument_list|,
literal|"SHA384"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA512WITHECDSA
name|SHA512WITHECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|X9ObjectIdentifiers
operator|.
name|ecdsa_with_SHA512
argument_list|)
argument_list|,
literal|"SHA512"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA1WITHPLAIN_ECDSA
name|SHA1WITHPLAIN_ECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|BSIObjectIdentifiers
operator|.
name|ecdsa_plain_SHA1
argument_list|)
argument_list|,
literal|"SHA1"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA224WITHPLAIN_ECDSA
name|SHA224WITHPLAIN_ECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|BSIObjectIdentifiers
operator|.
name|ecdsa_plain_SHA224
argument_list|)
argument_list|,
literal|"SHA224"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA256WITHPLAIN_ECDSA
name|SHA256WITHPLAIN_ECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|BSIObjectIdentifiers
operator|.
name|ecdsa_plain_SHA256
argument_list|)
argument_list|,
literal|"SHA256"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA384WITHPLAIN_ECDSA
name|SHA384WITHPLAIN_ECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|BSIObjectIdentifiers
operator|.
name|ecdsa_plain_SHA384
argument_list|)
argument_list|,
literal|"SHA384"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA512WITHPLAIN_ECDSA
name|SHA512WITHPLAIN_ECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|BSIObjectIdentifiers
operator|.
name|ecdsa_plain_SHA512
argument_list|)
argument_list|,
literal|"SHA512"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|RIPEMD160WITHPLAIN_ECDSA
name|RIPEMD160WITHPLAIN_ECDSA
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|BSIObjectIdentifiers
operator|.
name|ecdsa_plain_RIPEMD160
argument_list|)
argument_list|,
literal|"RIPEMD160"
argument_list|,
literal|"ECDSA"
argument_list|)
block|,
DECL|enumConstant|SHA1WITHRSAANDMGF1
name|SHA1WITHRSAANDMGF1
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|OIWObjectIdentifiers
operator|.
name|idSHA1
argument_list|,
name|AS2SignatureAlgorithmParams
operator|.
name|SHA1ALGPARAMS
argument_list|)
argument_list|,
literal|"SHA1"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA224WITHRSAANDMGF1
name|SHA224WITHRSAANDMGF1
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha224
argument_list|,
name|AS2SignatureAlgorithmParams
operator|.
name|SHA224ALGPARAMS
argument_list|)
argument_list|,
literal|"SHA224"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA256WITHRSAANDMGF1
name|SHA256WITHRSAANDMGF1
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha256
argument_list|,
name|AS2SignatureAlgorithmParams
operator|.
name|SHA256ALGPARAMS
argument_list|)
argument_list|,
literal|"SHA256"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA384WITHRSAANDMGF1
name|SHA384WITHRSAANDMGF1
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha384
argument_list|,
name|AS2SignatureAlgorithmParams
operator|.
name|SHA384ALGPARAMS
argument_list|)
argument_list|,
literal|"SHA384"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA512WITHRSAANDMGF1
name|SHA512WITHRSAANDMGF1
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha512
argument_list|,
name|AS2SignatureAlgorithmParams
operator|.
name|SHA512ALGPARAMS
argument_list|)
argument_list|,
literal|"SHA512"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_224WITHRSAANDMGF1
name|SHA3_224WITHRSAANDMGF1
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha3_224
argument_list|,
name|AS2SignatureAlgorithmParams
operator|.
name|SHA3_224ALGPARAMS
argument_list|)
argument_list|,
literal|"SHA3_224"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_256WITHRSAANDMGF1
name|SHA3_256WITHRSAANDMGF1
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha3_256
argument_list|,
name|AS2SignatureAlgorithmParams
operator|.
name|SHA3_256ALGPARAMS
argument_list|)
argument_list|,
literal|"SHA3_256"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_384WITHRSAANDMGF1
name|SHA3_384WITHRSAANDMGF1
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha3_384
argument_list|,
name|AS2SignatureAlgorithmParams
operator|.
name|SHA3_384ALGPARAMS
argument_list|)
argument_list|,
literal|"SHA3_384"
argument_list|,
literal|"RSA"
argument_list|)
block|,
DECL|enumConstant|SHA3_512WITHRSAANDMGF1
name|SHA3_512WITHRSAANDMGF1
argument_list|(
operator|new
name|AlgorithmIdentifier
argument_list|(
name|NISTObjectIdentifiers
operator|.
name|id_sha3_512
argument_list|,
name|AS2SignatureAlgorithmParams
operator|.
name|SHA3_512ALGPARAMS
argument_list|)
argument_list|,
literal|"SHA3_512"
argument_list|,
literal|"RSA"
argument_list|)
block|;
DECL|field|signatureAlgorithm
specifier|private
specifier|final
name|AlgorithmIdentifier
name|signatureAlgorithm
decl_stmt|;
DECL|field|digestAlgorithmName
specifier|private
specifier|final
name|String
name|digestAlgorithmName
decl_stmt|;
DECL|field|encryptionAlgorithmName
specifier|private
specifier|final
name|String
name|encryptionAlgorithmName
decl_stmt|;
DECL|method|AS2SignatureAlgorithm (AlgorithmIdentifier signatureAlgorithm, String digestAlgorithmName, String encryptionAlgorithmName)
name|AS2SignatureAlgorithm
parameter_list|(
name|AlgorithmIdentifier
name|signatureAlgorithm
parameter_list|,
name|String
name|digestAlgorithmName
parameter_list|,
name|String
name|encryptionAlgorithmName
parameter_list|)
block|{
name|this
operator|.
name|signatureAlgorithm
operator|=
name|signatureAlgorithm
expr_stmt|;
name|this
operator|.
name|digestAlgorithmName
operator|=
name|digestAlgorithmName
expr_stmt|;
name|this
operator|.
name|encryptionAlgorithmName
operator|=
name|encryptionAlgorithmName
expr_stmt|;
block|}
DECL|method|getSignatureAlgorithm ()
specifier|public
name|AlgorithmIdentifier
name|getSignatureAlgorithm
parameter_list|()
block|{
return|return
name|signatureAlgorithm
return|;
block|}
DECL|method|getSignatureAlgorithmName ()
specifier|public
name|String
name|getSignatureAlgorithmName
parameter_list|()
block|{
return|return
name|name
argument_list|()
operator|.
name|replace
argument_list|(
literal|"_"
argument_list|,
literal|"-"
argument_list|)
return|;
block|}
DECL|method|getDigestAlgorithmName ()
specifier|public
name|String
name|getDigestAlgorithmName
parameter_list|()
block|{
return|return
name|digestAlgorithmName
return|;
block|}
DECL|method|getEncryptionAlgorithmName ()
specifier|public
name|String
name|getEncryptionAlgorithmName
parameter_list|()
block|{
return|return
name|encryptionAlgorithmName
return|;
block|}
block|}
end_enum

end_unit

