begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms.sig
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
name|sig
package|;
end_package

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
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|common
operator|.
name|CryptoCmsMarshallerConfiguration
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
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
annotation|@
name|UriParams
DECL|class|SignedDataCreatorConfiguration
specifier|public
class|class
name|SignedDataCreatorConfiguration
extends|extends
name|CryptoCmsMarshallerConfiguration
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SignedDataCreatorConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|includeContent
specifier|private
name|Boolean
name|includeContent
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"Signer information: reference to a bean which implements org.apache.camel.component.crypto.cms.api.SignerInfo"
argument_list|)
DECL|field|signer
specifier|private
specifier|final
name|List
argument_list|<
name|SignerInfo
argument_list|>
name|signer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
DECL|method|SignedDataCreatorConfiguration (CamelContext context)
specifier|public
name|SignedDataCreatorConfiguration
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|getIncludeContent ()
specifier|public
name|Boolean
name|getIncludeContent
parameter_list|()
block|{
return|return
name|includeContent
return|;
block|}
comment|/**      * Indicates whether the signed content should be included into the Signed      * Data instance. If false then a detached Signed Data instance is created      * in the header CamelCryptoCmsSignedData.      */
DECL|method|setIncludeContent (Boolean includeContent)
specifier|public
name|void
name|setIncludeContent
parameter_list|(
name|Boolean
name|includeContent
parameter_list|)
block|{
name|this
operator|.
name|includeContent
operator|=
name|includeContent
expr_stmt|;
block|}
DECL|method|getSigner ()
specifier|public
name|List
argument_list|<
name|SignerInfo
argument_list|>
name|getSigner
parameter_list|()
block|{
return|return
name|signer
return|;
block|}
DECL|method|setSigner (SignerInfo signer)
specifier|public
name|void
name|setSigner
parameter_list|(
name|SignerInfo
name|signer
parameter_list|)
block|{
name|this
operator|.
name|signer
operator|.
name|add
argument_list|(
name|signer
argument_list|)
expr_stmt|;
block|}
comment|// for multi values
DECL|method|setSigner (List<?> signers)
specifier|public
name|void
name|setSigner
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|signers
parameter_list|)
block|{
if|if
condition|(
name|signers
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Object
name|signerOb
range|:
name|signers
control|)
block|{
if|if
condition|(
name|signerOb
operator|instanceof
name|String
condition|)
block|{
name|String
name|signerName
init|=
operator|(
name|String
operator|)
name|signerOb
decl_stmt|;
name|String
name|valueNoHash
init|=
name|signerName
operator|.
name|replaceAll
argument_list|(
literal|"#"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|getContext
argument_list|()
operator|!=
literal|null
operator|&&
name|signerName
operator|!=
literal|null
condition|)
block|{
name|SignerInfo
name|signer
init|=
name|getContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|valueNoHash
argument_list|,
name|SignerInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|signer
operator|!=
literal|null
condition|)
block|{
name|setSigner
argument_list|(
name|signer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|CryptoCmsException
block|{
if|if
condition|(
name|signer
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logErrorAndThrow
argument_list|(
name|LOG
argument_list|,
literal|"No signer set."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

