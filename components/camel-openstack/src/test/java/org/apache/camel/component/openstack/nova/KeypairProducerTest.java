begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.nova
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openstack
operator|.
name|nova
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|doReturn
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
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
name|openstack
operator|.
name|nova
operator|.
name|producer
operator|.
name|KeypairProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|ArgumentCaptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Matchers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|compute
operator|.
name|Keypair
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|openstack
operator|.
name|compute
operator|.
name|domain
operator|.
name|NovaKeypair
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
name|List
import|;
end_import

begin_class
DECL|class|KeypairProducerTest
specifier|public
class|class
name|KeypairProducerTest
extends|extends
name|NovaProducerTestSupport
block|{
DECL|field|KEYPAIR_NAME
specifier|private
specifier|static
specifier|final
name|String
name|KEYPAIR_NAME
init|=
literal|"keypairName"
decl_stmt|;
annotation|@
name|Mock
DECL|field|osTestKeypair
specifier|private
name|Keypair
name|osTestKeypair
decl_stmt|;
DECL|field|dummyKeypair
specifier|private
name|Keypair
name|dummyKeypair
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|producer
operator|=
operator|new
name|KeypairProducer
argument_list|(
name|endpoint
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|dummyKeypair
operator|=
name|createDummyKeypair
argument_list|()
expr_stmt|;
name|when
argument_list|(
name|keypairService
operator|.
name|get
argument_list|(
name|Matchers
operator|.
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|osTestKeypair
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|keypairService
operator|.
name|create
argument_list|(
name|Matchers
operator|.
name|anyString
argument_list|()
argument_list|,
name|Matchers
operator|.
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|osTestKeypair
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|compute
operator|.
name|Keypair
argument_list|>
name|getAllList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|getAllList
operator|.
name|add
argument_list|(
name|osTestKeypair
argument_list|)
expr_stmt|;
name|getAllList
operator|.
name|add
argument_list|(
name|osTestKeypair
argument_list|)
expr_stmt|;
name|doReturn
argument_list|(
name|getAllList
argument_list|)
operator|.
name|when
argument_list|(
name|keypairService
argument_list|)
operator|.
name|list
argument_list|()
expr_stmt|;
name|when
argument_list|(
name|osTestKeypair
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyKeypair
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|osTestKeypair
operator|.
name|getPublicKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyKeypair
operator|.
name|getPublicKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createKeypair ()
specifier|public
name|void
name|createKeypair
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|fingerPrint
init|=
literal|"fp"
decl_stmt|;
specifier|final
name|String
name|privatecKey
init|=
literal|"prk"
decl_stmt|;
name|when
argument_list|(
name|osTestKeypair
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|KEYPAIR_NAME
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|osTestKeypair
operator|.
name|getPublicKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyKeypair
operator|.
name|getPublicKey
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|osTestKeypair
operator|.
name|getFingerprint
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|fingerPrint
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|osTestKeypair
operator|.
name|getPrivateKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|privatecKey
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|NovaConstants
operator|.
name|OPERATION
argument_list|,
name|NovaConstants
operator|.
name|CREATE
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|NovaConstants
operator|.
name|NAME
argument_list|,
name|KEYPAIR_NAME
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|nameCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|keypairCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|keypairService
argument_list|)
operator|.
name|create
argument_list|(
name|nameCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|keypairCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|KEYPAIR_NAME
argument_list|,
name|nameCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|keypairCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|Keypair
name|result
init|=
name|msg
operator|.
name|getBody
argument_list|(
name|Keypair
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|fingerPrint
argument_list|,
name|result
operator|.
name|getFingerprint
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|privatecKey
argument_list|,
name|result
operator|.
name|getPrivateKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dummyKeypair
operator|.
name|getName
argument_list|()
argument_list|,
name|result
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dummyKeypair
operator|.
name|getPublicKey
argument_list|()
argument_list|,
name|result
operator|.
name|getPublicKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createKeypairFromExisting ()
specifier|public
name|void
name|createKeypairFromExisting
parameter_list|()
throws|throws
name|Exception
block|{
name|msg
operator|.
name|setHeader
argument_list|(
name|NovaConstants
operator|.
name|OPERATION
argument_list|,
name|NovaConstants
operator|.
name|CREATE
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|NovaConstants
operator|.
name|NAME
argument_list|,
name|KEYPAIR_NAME
argument_list|)
expr_stmt|;
name|String
name|key
init|=
literal|"existing public key string"
decl_stmt|;
name|when
argument_list|(
name|osTestKeypair
operator|.
name|getPublicKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|nameCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|keypairCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|keypairService
argument_list|)
operator|.
name|create
argument_list|(
name|nameCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|keypairCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|KEYPAIR_NAME
argument_list|,
name|nameCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|key
argument_list|,
name|keypairCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|Keypair
name|result
init|=
name|msg
operator|.
name|getBody
argument_list|(
name|Keypair
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|dummyKeypair
operator|.
name|getName
argument_list|()
argument_list|,
name|result
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dummyKeypair
operator|.
name|getFingerprint
argument_list|()
argument_list|,
name|result
operator|.
name|getFingerprint
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dummyKeypair
operator|.
name|getPrivateKey
argument_list|()
argument_list|,
name|result
operator|.
name|getPrivateKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|key
argument_list|,
name|result
operator|.
name|getPublicKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createDummyKeypair ()
specifier|private
name|Keypair
name|createDummyKeypair
parameter_list|()
block|{
return|return
operator|new
name|NovaKeypair
argument_list|()
operator|.
name|create
argument_list|(
name|KEYPAIR_NAME
argument_list|,
literal|"string contains private key"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

