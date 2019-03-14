begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.as2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|as2
package|;
end_package

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
name|Processor
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
name|as2
operator|.
name|AS2Component
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
name|as2
operator|.
name|AS2Configuration
import|;
end_import

begin_class
DECL|class|ProvisionExchangeMessageCrypto
specifier|public
class|class
name|ProvisionExchangeMessageCrypto
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|AS2Component
name|component
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"as2"
argument_list|,
name|AS2Component
operator|.
name|class
argument_list|)
decl_stmt|;
name|AS2Configuration
name|configuration
init|=
name|component
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelAS2.signingAlgorithm"
argument_list|,
name|configuration
operator|.
name|getSigningAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelAS2.signingCertificateChain"
argument_list|,
name|configuration
operator|.
name|getSigningCertificateChain
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelAS2.signingPrivateKey"
argument_list|,
name|configuration
operator|.
name|getSigningPrivateKey
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelAS2.signedReceiptMicAlgorithms"
argument_list|,
name|configuration
operator|.
name|getSignedReceiptMicAlgorithms
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelAS2.encryptingAlgorithm"
argument_list|,
name|configuration
operator|.
name|getEncryptingAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelAS2.encryptingCertificateChain"
argument_list|,
name|configuration
operator|.
name|getEncryptingCertificateChain
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelAS2.decryptingPrivateKey"
argument_list|,
name|configuration
operator|.
name|getDecryptingPrivateKey
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelAS2.compressionAlgorithm"
argument_list|,
name|configuration
operator|.
name|getCompressionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

