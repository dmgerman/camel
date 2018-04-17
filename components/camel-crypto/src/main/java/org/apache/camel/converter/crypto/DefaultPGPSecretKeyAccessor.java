begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|List
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
name|PGPPrivateKey
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
name|PGPSecretKeyRingCollection
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
comment|/**  * Caches a Secret Keyring. Assumes that the password for all private keys is  * the same.  *   */
end_comment

begin_class
DECL|class|DefaultPGPSecretKeyAccessor
specifier|public
class|class
name|DefaultPGPSecretKeyAccessor
implements|implements
name|PGPSecretKeyAccessor
block|{
DECL|field|userIdPart2SecretKeyList
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|PGPSecretKeyAndPrivateKeyAndUserId
argument_list|>
argument_list|>
name|userIdPart2SecretKeyList
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
DECL|field|keyId2PrivateKey
specifier|private
specifier|final
name|Map
argument_list|<
name|Long
argument_list|,
name|PGPPrivateKey
argument_list|>
name|keyId2PrivateKey
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
DECL|field|pgpSecretKeyring
specifier|private
specifier|final
name|PGPSecretKeyRingCollection
name|pgpSecretKeyring
decl_stmt|;
DECL|field|password
specifier|private
specifier|final
name|String
name|password
decl_stmt|;
DECL|field|provider
specifier|private
specifier|final
name|String
name|provider
decl_stmt|;
comment|/**      *       * @param secretKeyRing      *            secret key ring as byte array      * @param password      *            password for the private keys, assuming that all private keys      *            have the same password      * @param provider      * @throws PGPException      * @throws IOException      */
DECL|method|DefaultPGPSecretKeyAccessor (byte[] secretKeyRing, String password, String provider)
specifier|public
name|DefaultPGPSecretKeyAccessor
parameter_list|(
name|byte
index|[]
name|secretKeyRing
parameter_list|,
name|String
name|password
parameter_list|,
name|String
name|provider
parameter_list|)
throws|throws
name|PGPException
throws|,
name|IOException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|secretKeyRing
argument_list|,
literal|"secretKeyRing"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|password
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|provider
argument_list|,
literal|"provider"
argument_list|)
expr_stmt|;
name|pgpSecretKeyring
operator|=
operator|new
name|PGPSecretKeyRingCollection
argument_list|(
name|PGPUtil
operator|.
name|getDecoderStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|secretKeyRing
argument_list|)
argument_list|)
argument_list|,
operator|new
name|BcKeyFingerprintCalculator
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
name|this
operator|.
name|provider
operator|=
name|provider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSignerKeys (Exchange exchange, List<String> useridParts)
specifier|public
name|List
argument_list|<
name|PGPSecretKeyAndPrivateKeyAndUserId
argument_list|>
name|getSignerKeys
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
name|List
argument_list|<
name|PGPSecretKeyAndPrivateKeyAndUserId
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|useridPart
range|:
name|useridParts
control|)
block|{
name|List
argument_list|<
name|PGPSecretKeyAndPrivateKeyAndUserId
argument_list|>
name|partResult
init|=
name|userIdPart2SecretKeyList
operator|.
name|get
argument_list|(
name|useridPart
argument_list|)
decl_stmt|;
if|if
condition|(
name|partResult
operator|==
literal|null
condition|)
block|{
name|partResult
operator|=
name|PGPDataFormatUtil
operator|.
name|findSecretKeysWithPrivateKeyAndUserId
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|useridPart
argument_list|,
name|password
argument_list|)
argument_list|,
name|provider
argument_list|,
name|pgpSecretKeyring
argument_list|)
expr_stmt|;
name|userIdPart2SecretKeyList
operator|.
name|put
argument_list|(
name|useridPart
argument_list|,
name|partResult
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|addAll
argument_list|(
name|partResult
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|getPrivateKey (Exchange exchange, long keyId)
specifier|public
name|PGPPrivateKey
name|getPrivateKey
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|long
name|keyId
parameter_list|)
throws|throws
name|Exception
block|{
name|Long
name|keyIdLong
init|=
name|Long
operator|.
name|valueOf
argument_list|(
name|keyId
argument_list|)
decl_stmt|;
name|PGPPrivateKey
name|result
init|=
name|keyId2PrivateKey
operator|.
name|get
argument_list|(
name|keyIdLong
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|PGPDataFormatUtil
operator|.
name|findPrivateKeyWithkeyId
argument_list|(
name|keyId
argument_list|,
name|password
argument_list|,
literal|null
argument_list|,
name|provider
argument_list|,
name|pgpSecretKeyring
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|keyId2PrivateKey
operator|.
name|put
argument_list|(
name|keyIdLong
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

