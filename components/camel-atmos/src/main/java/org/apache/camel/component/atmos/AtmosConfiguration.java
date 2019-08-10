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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|emc
operator|.
name|atmos
operator|.
name|api
operator|.
name|AtmosApi
import|;
end_import

begin_import
import|import
name|com
operator|.
name|emc
operator|.
name|atmos
operator|.
name|api
operator|.
name|AtmosConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|emc
operator|.
name|atmos
operator|.
name|api
operator|.
name|jersey
operator|.
name|AtmosApiClient
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
name|AtmosException
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriPath
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|AtmosConfiguration
specifier|public
class|class
name|AtmosConfiguration
block|{
DECL|field|client
specifier|private
name|AtmosApi
name|client
decl_stmt|;
annotation|@
name|UriPath
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|operation
specifier|private
name|AtmosOperation
name|operation
decl_stmt|;
annotation|@
name|UriParam
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
name|UriParam
DECL|field|localPath
specifier|private
name|String
name|localPath
decl_stmt|;
annotation|@
name|UriParam
DECL|field|remotePath
specifier|private
name|String
name|remotePath
decl_stmt|;
annotation|@
name|UriParam
DECL|field|newRemotePath
specifier|private
name|String
name|newRemotePath
decl_stmt|;
annotation|@
name|UriParam
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
annotation|@
name|UriParam
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
name|UriParam
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
name|UriParam
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
DECL|method|setClient (AtmosApi client)
specifier|public
name|void
name|setClient
parameter_list|(
name|AtmosApi
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|getClient ()
specifier|public
name|AtmosApi
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
comment|/**      * Obtain a new instance of AtmosApi client and store it in configuration.      *      * @throws AtmosException      */
DECL|method|createClient ()
specifier|public
name|void
name|createClient
parameter_list|()
throws|throws
name|AtmosException
block|{
name|AtmosConfig
name|config
init|=
literal|null
decl_stmt|;
try|try
block|{
name|config
operator|=
operator|new
name|AtmosConfig
argument_list|(
name|fullTokenId
argument_list|,
name|secretKey
argument_list|,
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|use
parameter_list|)
block|{
throw|throw
operator|new
name|AtmosException
argument_list|(
literal|"wrong syntax for Atmos URI!"
argument_list|,
name|use
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|sslValidation
condition|)
block|{
name|config
operator|.
name|setDisableSslValidation
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|AtmosApi
name|atmosclient
init|=
operator|new
name|AtmosApiClient
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|this
operator|.
name|client
operator|=
name|atmosclient
expr_stmt|;
block|}
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
comment|/**      * Atmos name      */
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
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
DECL|method|getLocalPath ()
specifier|public
name|String
name|getLocalPath
parameter_list|()
block|{
return|return
name|localPath
return|;
block|}
comment|/**      * Local path to put files      */
DECL|method|setLocalPath (String localPath)
specifier|public
name|void
name|setLocalPath
parameter_list|(
name|String
name|localPath
parameter_list|)
block|{
name|this
operator|.
name|localPath
operator|=
name|localPath
expr_stmt|;
block|}
DECL|method|getRemotePath ()
specifier|public
name|String
name|getRemotePath
parameter_list|()
block|{
return|return
name|remotePath
return|;
block|}
comment|/**      * Where to put files on Atmos      */
DECL|method|setRemotePath (String remotePath)
specifier|public
name|void
name|setRemotePath
parameter_list|(
name|String
name|remotePath
parameter_list|)
block|{
name|this
operator|.
name|remotePath
operator|=
name|remotePath
expr_stmt|;
block|}
DECL|method|getNewRemotePath ()
specifier|public
name|String
name|getNewRemotePath
parameter_list|()
block|{
return|return
name|newRemotePath
return|;
block|}
comment|/**      * New path on Atmos when moving files      */
DECL|method|setNewRemotePath (String newRemotePath)
specifier|public
name|void
name|setNewRemotePath
parameter_list|(
name|String
name|newRemotePath
parameter_list|)
block|{
name|this
operator|.
name|newRemotePath
operator|=
name|newRemotePath
expr_stmt|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
comment|/**      * Search query on Atmos      */
DECL|method|setQuery (String query)
specifier|public
name|void
name|setQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
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
comment|/**      * Atmos client fullTokenId      */
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
DECL|method|getOperation ()
specifier|public
name|AtmosOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * Operation to perform      */
DECL|method|setOperation (AtmosOperation operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|AtmosOperation
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
comment|/**      * Atomos server uri      */
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
comment|/**      * Atmos SSL validation      */
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

