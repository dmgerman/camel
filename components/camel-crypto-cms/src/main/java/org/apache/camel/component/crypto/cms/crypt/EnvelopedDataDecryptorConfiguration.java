begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms.crypt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|crypt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|common
operator|.
name|CryptoCmsUnMarshallerConfiguration
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
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|exception
operator|.
name|CryptoCmsException
import|;
end_import

begin_interface
DECL|interface|EnvelopedDataDecryptorConfiguration
specifier|public
interface|interface
name|EnvelopedDataDecryptorConfiguration
extends|extends
name|CryptoCmsUnMarshallerConfiguration
block|{
comment|/**      * Returns the private keys with their public keys in the X.509 certificate      * which can be used for the decryption. The certificate is used for finding      * the corresponding Key Transport Recipient Info in the Enveloped Data      * object.      */
DECL|method|getPrivateKeyCertificateCollection (Exchange exchange)
name|Collection
argument_list|<
name|PrivateKeyWithCertificate
argument_list|>
name|getPrivateKeyCertificateCollection
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
function_decl|;
comment|/** Creates a copy of the current instance, for example by cloning. */
DECL|method|copy ()
name|EnvelopedDataDecryptorConfiguration
name|copy
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

