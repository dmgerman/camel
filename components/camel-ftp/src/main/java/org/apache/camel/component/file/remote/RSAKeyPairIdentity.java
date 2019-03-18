begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|remote
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|InvalidKeyException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPair
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Signature
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SignatureException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|RSAPublicKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|Identity
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|RSAKeyPairIdentity
specifier|public
class|class
name|RSAKeyPairIdentity
implements|implements
name|Identity
block|{
DECL|field|ALGORITHM_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|ALGORITHM_TYPE
init|=
literal|"ssh-rsa"
decl_stmt|;
DECL|field|log
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|keyPair
specifier|private
name|KeyPair
name|keyPair
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|method|RSAKeyPairIdentity (String name, KeyPair keyPair)
specifier|public
name|RSAKeyPairIdentity
parameter_list|(
name|String
name|name
parameter_list|,
name|KeyPair
name|keyPair
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|keyPair
operator|=
name|keyPair
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setPassphrase (byte[] passphrase)
specifier|public
name|boolean
name|setPassphrase
parameter_list|(
name|byte
index|[]
name|passphrase
parameter_list|)
throws|throws
name|JSchException
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getPublicKeyBlob ()
specifier|public
name|byte
index|[]
name|getPublicKeyBlob
parameter_list|()
block|{
name|RSAPublicKey
name|publicKey
init|=
operator|(
name|RSAPublicKey
operator|)
name|keyPair
operator|.
name|getPublic
argument_list|()
decl_stmt|;
name|byte
index|[]
name|sshRsa
init|=
name|ALGORITHM_TYPE
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|byte
index|[]
name|eArray
init|=
name|publicKey
operator|.
name|getPublicExponent
argument_list|()
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|byte
index|[]
name|nArray
init|=
name|publicKey
operator|.
name|getModulus
argument_list|()
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|byte
index|[]
name|result
init|=
operator|new
name|byte
index|[
name|sshRsa
operator|.
name|length
operator|+
literal|4
operator|+
name|eArray
operator|.
name|length
operator|+
literal|4
operator|+
name|nArray
operator|.
name|length
operator|+
literal|4
index|]
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
name|byte
index|[]
name|intAsByteArray
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|4
argument_list|)
operator|.
name|putInt
argument_list|(
name|sshRsa
operator|.
name|length
argument_list|)
operator|.
name|array
argument_list|()
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|intAsByteArray
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|index
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|index
operator|+=
literal|4
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|sshRsa
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|index
argument_list|,
name|sshRsa
operator|.
name|length
argument_list|)
expr_stmt|;
name|index
operator|+=
name|sshRsa
operator|.
name|length
expr_stmt|;
name|intAsByteArray
operator|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|4
argument_list|)
operator|.
name|putInt
argument_list|(
name|eArray
operator|.
name|length
argument_list|)
operator|.
name|array
argument_list|()
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|intAsByteArray
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|index
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|index
operator|+=
literal|4
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|eArray
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|index
argument_list|,
name|eArray
operator|.
name|length
argument_list|)
expr_stmt|;
name|index
operator|+=
name|eArray
operator|.
name|length
expr_stmt|;
name|intAsByteArray
operator|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|4
argument_list|)
operator|.
name|putInt
argument_list|(
name|nArray
operator|.
name|length
argument_list|)
operator|.
name|array
argument_list|()
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|intAsByteArray
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|index
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|index
operator|+=
literal|4
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|nArray
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|index
argument_list|,
name|nArray
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|getSignature (byte[] data)
specifier|public
name|byte
index|[]
name|getSignature
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
name|PrivateKey
name|prvKey
init|=
name|keyPair
operator|.
name|getPrivate
argument_list|()
decl_stmt|;
name|Signature
name|sig
decl_stmt|;
try|try
block|{
name|sig
operator|=
name|Signature
operator|.
name|getInstance
argument_list|(
literal|"SHA1withRSA"
argument_list|)
expr_stmt|;
name|sig
operator|.
name|initSign
argument_list|(
name|prvKey
argument_list|)
expr_stmt|;
name|sig
operator|.
name|update
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|byte
index|[]
name|sshRsa
init|=
name|ALGORITHM_TYPE
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|byte
index|[]
name|signature
init|=
name|sig
operator|.
name|sign
argument_list|()
decl_stmt|;
name|byte
index|[]
name|result
init|=
operator|new
name|byte
index|[
name|sshRsa
operator|.
name|length
operator|+
literal|4
operator|+
name|signature
operator|.
name|length
operator|+
literal|4
index|]
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
name|byte
index|[]
name|intAsByteArray
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|4
argument_list|)
operator|.
name|putInt
argument_list|(
name|sshRsa
operator|.
name|length
argument_list|)
operator|.
name|array
argument_list|()
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|intAsByteArray
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|index
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|index
operator|+=
literal|4
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|sshRsa
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|index
argument_list|,
name|sshRsa
operator|.
name|length
argument_list|)
expr_stmt|;
name|index
operator|+=
name|sshRsa
operator|.
name|length
expr_stmt|;
name|intAsByteArray
operator|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|4
argument_list|)
operator|.
name|putInt
argument_list|(
name|signature
operator|.
name|length
argument_list|)
operator|.
name|array
argument_list|()
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|intAsByteArray
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|index
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|index
operator|+=
literal|4
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|signature
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|index
argument_list|,
name|signature
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot sign"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidKeyException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot sign"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SignatureException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot sign"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|decrypt ()
specifier|public
name|boolean
name|decrypt
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getAlgName ()
specifier|public
name|String
name|getAlgName
parameter_list|()
block|{
return|return
name|ALGORITHM_TYPE
return|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|isEncrypted ()
specifier|public
name|boolean
name|isEncrypted
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{     }
block|}
end_class

end_unit

