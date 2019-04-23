begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
operator|.
name|dataformat
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Key
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|AlgorithmParameterSpec
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
name|model
operator|.
name|DataFormatDefinition
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
name|model
operator|.
name|dataformat
operator|.
name|CryptoDataFormat
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
name|DataFormat
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
name|RouteContext
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
name|support
operator|.
name|CamelContextHelper
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

begin_class
DECL|class|CryptoDataFormatReifier
specifier|public
class|class
name|CryptoDataFormatReifier
extends|extends
name|DataFormatReifier
argument_list|<
name|CryptoDataFormat
argument_list|>
block|{
DECL|method|CryptoDataFormatReifier (DataFormatDefinition definition)
specifier|public
name|CryptoDataFormatReifier
parameter_list|(
name|DataFormatDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|CryptoDataFormat
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doCreateDataFormat (CamelContext camelContext)
specifier|protected
name|DataFormat
name|doCreateDataFormat
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|DataFormat
name|cryptoFormat
init|=
name|super
operator|.
name|doCreateDataFormat
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|definition
operator|.
name|getKeyRef
argument_list|()
argument_list|)
condition|)
block|{
name|Key
name|key
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|definition
operator|.
name|getKeyRef
argument_list|()
argument_list|,
name|Key
operator|.
name|class
argument_list|)
decl_stmt|;
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|cryptoFormat
argument_list|,
literal|"key"
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|definition
operator|.
name|getAlgorithmParameterRef
argument_list|()
argument_list|)
condition|)
block|{
name|AlgorithmParameterSpec
name|spec
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|definition
operator|.
name|getAlgorithmParameterRef
argument_list|()
argument_list|,
name|AlgorithmParameterSpec
operator|.
name|class
argument_list|)
decl_stmt|;
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|cryptoFormat
argument_list|,
literal|"AlgorithmParameterSpec"
argument_list|,
name|spec
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|definition
operator|.
name|getInitVectorRef
argument_list|()
argument_list|)
condition|)
block|{
name|byte
index|[]
name|iv
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|definition
operator|.
name|getInitVectorRef
argument_list|()
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|cryptoFormat
argument_list|,
literal|"InitializationVector"
argument_list|,
name|iv
argument_list|)
expr_stmt|;
block|}
return|return
name|cryptoFormat
return|;
block|}
annotation|@
name|Override
DECL|method|configureDataFormat (DataFormat dataFormat, CamelContext camelContext)
specifier|protected
name|void
name|configureDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|Boolean
name|answer
init|=
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|definition
operator|.
name|getShouldAppendHMAC
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
operator|!
name|answer
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"shouldAppendHMAC"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"shouldAppendHMAC"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|definition
operator|.
name|getInline
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
name|answer
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"shouldInlineInitializationVector"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"shouldInlineInitializationVector"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getAlgorithm
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"algorithm"
argument_list|,
name|definition
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCryptoProvider
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"cryptoProvider"
argument_list|,
name|definition
operator|.
name|getCryptoProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getMacAlgorithm
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"macAlgorithm"
argument_list|,
name|definition
operator|.
name|getMacAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getBuffersize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"buffersize"
argument_list|,
name|definition
operator|.
name|getBuffersize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

