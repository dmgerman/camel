begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|KeyStore
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
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
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
name|CamelContext
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
name|builder
operator|.
name|RouteBuilder
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
name|spring
operator|.
name|SpringCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|SpringSignatureTest
specifier|public
class|class
name|SpringSignatureTest
extends|extends
name|SignatureTest
block|{
DECL|field|rsaPair
specifier|private
specifier|static
name|KeyPair
name|rsaPair
decl_stmt|;
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|rsaPair
operator|=
name|getKeyPair
argument_list|(
literal|"RSA"
argument_list|)
expr_stmt|;
return|return
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/crypto/SpringSignatureTest.xml"
argument_list|)
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|keystore ()
specifier|public
specifier|static
name|KeyStore
name|keystore
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|loadKeystore
argument_list|()
return|;
block|}
DECL|method|privateKeyFromKeystore ()
specifier|public
specifier|static
name|PrivateKey
name|privateKeyFromKeystore
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SignatureTest
argument_list|()
operator|.
name|getKeyFromKeystore
argument_list|()
return|;
block|}
DECL|method|certificateFromKeystore ()
specifier|public
specifier|static
name|Certificate
name|certificateFromKeystore
parameter_list|()
throws|throws
name|Exception
block|{
name|KeyStore
name|keystore
init|=
name|loadKeystore
argument_list|()
decl_stmt|;
return|return
name|keystore
operator|.
name|getCertificate
argument_list|(
literal|"bob"
argument_list|)
return|;
block|}
DECL|method|privateKey ()
specifier|public
specifier|static
name|PrivateKey
name|privateKey
parameter_list|()
throws|throws
name|Exception
block|{
name|KeyStore
name|keystore
init|=
name|loadKeystore
argument_list|()
decl_stmt|;
return|return
operator|(
name|PrivateKey
operator|)
name|keystore
operator|.
name|getKey
argument_list|(
literal|"bob"
argument_list|,
literal|"letmein"
operator|.
name|toCharArray
argument_list|()
argument_list|)
return|;
block|}
DECL|method|publicKey ()
specifier|public
specifier|static
name|PublicKey
name|publicKey
parameter_list|()
throws|throws
name|Exception
block|{
name|KeyStore
name|keystore
init|=
name|loadKeystore
argument_list|()
decl_stmt|;
name|Certificate
name|cert
init|=
name|keystore
operator|.
name|getCertificate
argument_list|(
literal|"bob"
argument_list|)
decl_stmt|;
return|return
name|cert
operator|.
name|getPublicKey
argument_list|()
return|;
block|}
DECL|method|privateRSAKey ()
specifier|public
specifier|static
name|PrivateKey
name|privateRSAKey
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|rsaPair
operator|.
name|getPrivate
argument_list|()
return|;
block|}
DECL|method|publicRSAKey ()
specifier|public
specifier|static
name|PublicKey
name|publicRSAKey
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|rsaPair
operator|.
name|getPublic
argument_list|()
return|;
block|}
DECL|method|random ()
specifier|public
specifier|static
name|SecureRandom
name|random
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SecureRandom
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilders ()
specifier|protected
name|RouteBuilder
index|[]
name|createRouteBuilders
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
index|[]
block|{}
return|;
block|}
block|}
end_class

end_unit

