begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atmos
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atmos
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|atmos
operator|.
name|util
operator|.
name|AtmosOperation
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
name|atmos
operator|.
name|validator
operator|.
name|AtmosConfigurationValidator
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
name|Metadata
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
name|annotations
operator|.
name|Component
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
name|DefaultComponent
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"atmos"
argument_list|)
DECL|class|AtmosComponent
specifier|public
class|class
name|AtmosComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|fullTokenId
specifier|private
name|String
name|fullTokenId
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|sslValidation
specifier|private
name|boolean
name|sslValidation
decl_stmt|;
DECL|method|AtmosComponent ()
specifier|public
name|AtmosComponent
parameter_list|()
block|{     }
DECL|method|AtmosComponent (CamelContext context)
specifier|public
name|AtmosComponent
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
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|AtmosEndpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|AtmosConfiguration
name|configuration
init|=
operator|new
name|AtmosConfiguration
argument_list|()
decl_stmt|;
name|String
name|name
init|=
literal|null
decl_stmt|;
name|String
name|operation
init|=
name|remaining
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|remaining
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|name
operator|=
name|parts
index|[
literal|0
index|]
expr_stmt|;
name|operation
operator|=
name|parts
index|[
literal|1
index|]
expr_stmt|;
block|}
name|configuration
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setOperation
argument_list|(
name|AtmosOperation
operator|.
name|valueOf
argument_list|(
name|operation
argument_list|)
argument_list|)
expr_stmt|;
comment|// set options from component
name|configuration
operator|.
name|setUri
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|"uri"
argument_list|)
operator|==
literal|null
condition|?
name|this
operator|.
name|uri
else|:
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"uri"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setSecretKey
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|"secretKey"
argument_list|)
operator|==
literal|null
condition|?
name|this
operator|.
name|secretKey
else|:
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"secretKey"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setLocalPath
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"localPath"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRemotePath
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"remotePath"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setNewRemotePath
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"newRemotePath"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setQuery
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"query"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setFullTokenId
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|"fullTokenId"
argument_list|)
operator|==
literal|null
condition|?
name|this
operator|.
name|fullTokenId
else|:
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"fullTokenId"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setSslValidation
argument_list|(
name|this
operator|.
name|sslValidation
argument_list|)
expr_stmt|;
comment|//pass validation test
name|AtmosConfigurationValidator
operator|.
name|validate
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
comment|// and then override from parameters
name|AtmosEndpoint
name|endpoint
init|=
operator|new
name|AtmosEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getFullTokenId ()
specifier|public
name|String
name|getFullTokenId
parameter_list|()
block|{
return|return
name|fullTokenId
return|;
block|}
comment|/**      * The token id to pass to the Atmos client      */
DECL|method|setFullTokenId (String fullTokenId)
specifier|public
name|void
name|setFullTokenId
parameter_list|(
name|String
name|fullTokenId
parameter_list|)
block|{
name|this
operator|.
name|fullTokenId
operator|=
name|fullTokenId
expr_stmt|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|String
name|getSecretKey
parameter_list|()
block|{
return|return
name|secretKey
return|;
block|}
comment|/**      * The secret key to pass to the Atmos client (should be base64 encoded)      */
DECL|method|setSecretKey (String secretKey)
specifier|public
name|void
name|setSecretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|this
operator|.
name|secretKey
operator|=
name|secretKey
expr_stmt|;
block|}
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
comment|/**      * The URI of the server for the Atmos client to connect to      */
DECL|method|setUri (String uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|isSslValidation ()
specifier|public
name|boolean
name|isSslValidation
parameter_list|()
block|{
return|return
name|sslValidation
return|;
block|}
comment|/**      * Whether the Atmos client should perform SSL validation      */
DECL|method|setSslValidation (boolean sslValidation)
specifier|public
name|void
name|setSslValidation
parameter_list|(
name|boolean
name|sslValidation
parameter_list|)
block|{
name|this
operator|.
name|sslValidation
operator|=
name|sslValidation
expr_stmt|;
block|}
block|}
end_class

end_unit

