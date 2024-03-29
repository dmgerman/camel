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
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPrivateKey
import|;
end_import

begin_interface
DECL|interface|PGPSecretKeyAccessor
specifier|public
interface|interface
name|PGPSecretKeyAccessor
block|{
comment|/**      * Returns the signer keys for the given user ID parts. This method is used      * for signing.      *       * @param exchange      *            exchange, can be<code>null</code>      * @param useridParts      *            parts of User IDs, can be<code>null</code> or empty, then an      *            empty list must be returned      * @return list of secret keys with their private keys and User Ids which      *         corresponds to one of the useridParts, must not be      *<code>null</code>, can be empty      */
DECL|method|getSignerKeys (Exchange exchange, List<String> useridParts)
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
function_decl|;
comment|/**      * Returns the private key with a certain key ID. This method is used for      * decrypting.      *       * @param exchange      *            exchange, can be<code>null</code>      *       * @param keyId      *            key ID      * @return private key or<code>null</code> if the key cannot be found      */
DECL|method|getPrivateKey (Exchange exchange, long keyId)
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
function_decl|;
block|}
end_interface

end_unit

