begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto
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
package|;
end_package

begin_comment
comment|/**  *<code>DigitalSignatureConstants</code> contains Constants for use as Message  * header keys.  */
end_comment

begin_interface
DECL|interface|DigitalSignatureConstants
specifier|public
interface|interface
name|DigitalSignatureConstants
block|{
DECL|field|SIGNATURE_PRIVATE_KEY
name|String
name|SIGNATURE_PRIVATE_KEY
init|=
literal|"CamelSignaturePrivateKey"
decl_stmt|;
DECL|field|SIGNATURE_PUBLIC_KEY_OR_CERT
name|String
name|SIGNATURE_PUBLIC_KEY_OR_CERT
init|=
literal|"CamelSignaturePublicKeyOrCert"
decl_stmt|;
DECL|field|SIGNATURE
name|String
name|SIGNATURE
init|=
literal|"CamelDigitalSignature"
decl_stmt|;
DECL|field|KEYSTORE_ALIAS
name|String
name|KEYSTORE_ALIAS
init|=
literal|"CamelSignatureKeyStoreAlias"
decl_stmt|;
DECL|field|KEYSTORE_PASSWORD
name|String
name|KEYSTORE_PASSWORD
init|=
literal|"CamelSignatureKeyStorePassword"
decl_stmt|;
block|}
end_interface

end_unit

