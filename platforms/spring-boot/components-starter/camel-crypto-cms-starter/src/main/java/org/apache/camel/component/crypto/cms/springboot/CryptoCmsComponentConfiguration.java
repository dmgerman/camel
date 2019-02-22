begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms.springboot
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
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The crypto cms component is used for encrypting data in CMS Enveloped Data  * format, decrypting CMS Enveloped Data, signing data in CMS Signed Data  * format, and verifying CMS Signed Data.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.crypto-cms"
argument_list|)
DECL|class|CryptoCmsComponentConfiguration
specifier|public
class|class
name|CryptoCmsComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the crypto-cms component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To configure the shared SignedDataVerifierConfiguration, which determines      * the uri parameters for the verify operation. The option is a      * org.apache.camel.component.crypto.cms.sig.SignedDataVerifierConfiguration      * type.      */
DECL|field|signedDataVerifierConfiguration
specifier|private
name|String
name|signedDataVerifierConfiguration
decl_stmt|;
comment|/**      * To configure the shared EnvelopedDataDecryptorConfiguration, which      * determines the uri parameters for the decrypt operation. The option is a      * org.apache.camel.component.crypto.cms.crypt.EnvelopedDataDecryptorConfiguration type.      */
DECL|field|envelopedDataDecryptorConfiguration
specifier|private
name|String
name|envelopedDataDecryptorConfiguration
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getSignedDataVerifierConfiguration ()
specifier|public
name|String
name|getSignedDataVerifierConfiguration
parameter_list|()
block|{
return|return
name|signedDataVerifierConfiguration
return|;
block|}
DECL|method|setSignedDataVerifierConfiguration ( String signedDataVerifierConfiguration)
specifier|public
name|void
name|setSignedDataVerifierConfiguration
parameter_list|(
name|String
name|signedDataVerifierConfiguration
parameter_list|)
block|{
name|this
operator|.
name|signedDataVerifierConfiguration
operator|=
name|signedDataVerifierConfiguration
expr_stmt|;
block|}
DECL|method|getEnvelopedDataDecryptorConfiguration ()
specifier|public
name|String
name|getEnvelopedDataDecryptorConfiguration
parameter_list|()
block|{
return|return
name|envelopedDataDecryptorConfiguration
return|;
block|}
DECL|method|setEnvelopedDataDecryptorConfiguration ( String envelopedDataDecryptorConfiguration)
specifier|public
name|void
name|setEnvelopedDataDecryptorConfiguration
parameter_list|(
name|String
name|envelopedDataDecryptorConfiguration
parameter_list|)
block|{
name|this
operator|.
name|envelopedDataDecryptorConfiguration
operator|=
name|envelopedDataDecryptorConfiguration
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

