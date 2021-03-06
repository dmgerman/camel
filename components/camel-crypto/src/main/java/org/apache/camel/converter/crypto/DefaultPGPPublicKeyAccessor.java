begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.crypto
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|crypto
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|List
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKeyRingCollection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|operator
operator|.
name|bc
operator|.
name|BcKeyFingerprintCalculator
import|;
end_import

begin_comment
comment|/**  * Caches a public key ring.  */
end_comment

begin_class
DECL|class|DefaultPGPPublicKeyAccessor
specifier|public
class|class
name|DefaultPGPPublicKeyAccessor
implements|implements
name|PGPPublicKeyAccessor
block|{
DECL|field|pgpPublicKeyRing
specifier|private
specifier|final
name|PGPPublicKeyRingCollection
name|pgpPublicKeyRing
decl_stmt|;
DECL|method|DefaultPGPPublicKeyAccessor (byte[] publicKeyRing)
specifier|public
name|DefaultPGPPublicKeyAccessor
parameter_list|(
name|byte
index|[]
name|publicKeyRing
parameter_list|)
throws|throws
name|IOException
throws|,
name|PGPException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|publicKeyRing
argument_list|,
literal|"publicKeyRing"
argument_list|)
expr_stmt|;
name|pgpPublicKeyRing
operator|=
operator|new
name|PGPPublicKeyRingCollection
argument_list|(
name|PGPUtil
operator|.
name|getDecoderStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|publicKeyRing
argument_list|)
argument_list|)
argument_list|,
operator|new
name|BcKeyFingerprintCalculator
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEncryptionKeys (Exchange exchange, List<String> useridParts)
specifier|public
name|List
argument_list|<
name|PGPPublicKey
argument_list|>
name|getEncryptionKeys
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|useridParts
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|PGPDataFormatUtil
operator|.
name|findPublicKeys
argument_list|(
name|useridParts
argument_list|,
literal|true
argument_list|,
name|pgpPublicKeyRing
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getPublicKey (Exchange exchange, long keyId, List<String> userIdParts)
specifier|public
name|PGPPublicKey
name|getPublicKey
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|long
name|keyId
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|userIdParts
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|PGPDataFormatUtil
operator|.
name|getPublicKeyWithKeyIdAndUserID
argument_list|(
name|keyId
argument_list|,
name|userIdParts
argument_list|,
name|pgpPublicKeyRing
argument_list|)
return|;
block|}
block|}
end_class

end_unit

