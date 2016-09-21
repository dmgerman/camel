begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dropbox
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dropbox
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
name|Endpoint
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
name|dropbox
operator|.
name|util
operator|.
name|DropboxOperation
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
name|dropbox
operator|.
name|util
operator|.
name|DropboxPropertyManager
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
name|dropbox
operator|.
name|util
operator|.
name|DropboxUploadMode
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
name|dropbox
operator|.
name|validator
operator|.
name|DropboxConfigurationValidator
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
name|impl
operator|.
name|UriEndpointComponent
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
DECL|class|DropboxComponent
specifier|public
class|class
name|DropboxComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DropboxComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|DropboxComponent ()
specifier|public
name|DropboxComponent
parameter_list|()
block|{
name|super
argument_list|(
name|DropboxEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a camel endpoint after passing validation on the incoming url.      * @param uri the full URI of the endpoint      * @param remaining the remaining part of the URI without the query      *                parameters or component prefix      * @param parameters the optional parameters passed in      * @return the camel endpoint      * @throws Exception      */
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
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
name|DropboxConfiguration
name|configuration
init|=
operator|new
name|DropboxConfiguration
argument_list|()
decl_stmt|;
comment|// set options from component
name|configuration
operator|.
name|setAccessToken
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"accessToken"
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
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"remotePath"
argument_list|)
operator|)
operator|.
name|replaceAll
argument_list|(
literal|"\\s"
argument_list|,
literal|"+"
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
name|setOperation
argument_list|(
name|DropboxOperation
operator|.
name|valueOf
argument_list|(
name|remaining
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setClientIdentifier
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|"clientIdentifier"
argument_list|)
operator|==
literal|null
condition|?
name|DropboxPropertyManager
operator|.
name|getInstance
argument_list|()
operator|.
name|getProperty
argument_list|(
literal|"clientIdentifier"
argument_list|)
else|:
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"clientIdentifier"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|parameters
operator|.
name|get
argument_list|(
literal|"uploadMode"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|setUploadMode
argument_list|(
name|DropboxUploadMode
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"uploadMode"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//pass validation test
name|DropboxConfigurationValidator
operator|.
name|validate
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
comment|// and then override from parameters
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
operator|new
name|DropboxEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

