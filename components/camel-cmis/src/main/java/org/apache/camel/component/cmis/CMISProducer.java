begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cmis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cmis
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
name|Message
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
name|NoSuchHeaderException
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
name|RuntimeExchangeException
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
name|DefaultProducer
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
name|ExchangeHelper
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
name|MessageHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|chemistry
operator|.
name|opencmis
operator|.
name|client
operator|.
name|api
operator|.
name|CmisObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|chemistry
operator|.
name|opencmis
operator|.
name|client
operator|.
name|api
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|chemistry
operator|.
name|opencmis
operator|.
name|client
operator|.
name|api
operator|.
name|Folder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|chemistry
operator|.
name|opencmis
operator|.
name|commons
operator|.
name|PropertyIds
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|chemistry
operator|.
name|opencmis
operator|.
name|commons
operator|.
name|data
operator|.
name|ContentStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|chemistry
operator|.
name|opencmis
operator|.
name|commons
operator|.
name|enums
operator|.
name|VersioningState
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|chemistry
operator|.
name|opencmis
operator|.
name|commons
operator|.
name|exceptions
operator|.
name|CmisObjectNotFoundException
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

begin_comment
comment|/**  * The CMIS producer.  */
end_comment

begin_class
DECL|class|CMISProducer
specifier|public
class|class
name|CMISProducer
extends|extends
name|DefaultProducer
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
name|CMISProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cmisSessionFacade
specifier|private
specifier|final
name|CMISSessionFacade
name|cmisSessionFacade
decl_stmt|;
DECL|method|CMISProducer (CMISEndpoint endpoint, CMISSessionFacade cmisSessionFacade)
specifier|public
name|CMISProducer
parameter_list|(
name|CMISEndpoint
name|endpoint
parameter_list|,
name|CMISSessionFacade
name|cmisSessionFacade
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|cmisSessionFacade
operator|=
name|cmisSessionFacade
expr_stmt|;
block|}
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
name|CmisObject
name|cmisObject
init|=
name|createNode
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|cmisObject
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createNode (Exchange exchange)
specifier|private
name|CmisObject
name|createNode
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|validateRequiredHeader
argument_list|(
name|exchange
argument_list|,
name|PropertyIds
operator|.
name|NAME
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|parentFolderPath
init|=
name|parentFolderPathFor
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Folder
name|parentFolder
init|=
name|getFolderOnPath
argument_list|(
name|exchange
argument_list|,
name|parentFolderPath
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cmisProperties
init|=
name|CMISHelper
operator|.
name|filterCMISProperties
argument_list|(
name|message
operator|.
name|getHeaders
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isDocument
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|String
name|fileName
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|PropertyIds
operator|.
name|NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|mimeType
init|=
name|getMimeType
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|byte
index|[]
name|buf
init|=
name|getBodyData
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|ContentStream
name|contentStream
init|=
name|cmisSessionFacade
operator|.
name|createContentStream
argument_list|(
name|fileName
argument_list|,
name|buf
argument_list|,
name|mimeType
argument_list|)
decl_stmt|;
return|return
name|storeDocument
argument_list|(
name|parentFolder
argument_list|,
name|cmisProperties
argument_list|,
name|contentStream
argument_list|,
name|cmisSessionFacade
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|isFolder
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return
name|storeFolder
argument_list|(
name|parentFolder
argument_list|,
name|cmisProperties
argument_list|)
return|;
block|}
else|else
block|{
comment|//other types
return|return
name|storeDocument
argument_list|(
name|parentFolder
argument_list|,
name|cmisProperties
argument_list|,
literal|null
argument_list|,
name|cmisSessionFacade
argument_list|)
return|;
block|}
block|}
DECL|method|getFolderOnPath (Exchange exchange, String path)
specifier|private
name|Folder
name|getFolderOnPath
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|path
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|Folder
operator|)
name|cmisSessionFacade
operator|.
name|getObjectByPath
argument_list|(
name|path
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|CmisObjectNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"Path not found "
operator|+
name|path
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|parentFolderPathFor (Message message)
specifier|private
name|String
name|parentFolderPathFor
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|customPath
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|CamelCMISConstants
operator|.
name|CMIS_FOLDER_PATH
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|customPath
operator|!=
literal|null
condition|)
block|{
return|return
name|customPath
return|;
block|}
if|if
condition|(
name|isFolder
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|String
name|path
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getHeader
argument_list|(
name|PropertyIds
operator|.
name|PATH
argument_list|)
decl_stmt|;
name|String
name|name
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getHeader
argument_list|(
name|PropertyIds
operator|.
name|NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
name|path
operator|.
name|length
argument_list|()
operator|>
name|name
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|length
argument_list|()
operator|-
name|name
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
literal|"/"
return|;
block|}
DECL|method|isFolder (Message message)
specifier|private
name|boolean
name|isFolder
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|baseTypeId
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_TYPE_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|baseTypeId
operator|!=
literal|null
condition|)
block|{
return|return
name|baseTypeId
operator|.
name|equals
argument_list|(
name|CamelCMISConstants
operator|.
name|CMIS_FOLDER
argument_list|)
return|;
block|}
return|return
name|message
operator|.
name|getBody
argument_list|()
operator|==
literal|null
return|;
block|}
DECL|method|storeFolder (Folder parentFolder, Map<String, Object> cmisProperties)
specifier|private
name|Folder
name|storeFolder
parameter_list|(
name|Folder
name|parentFolder
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cmisProperties
parameter_list|)
block|{
if|if
condition|(
operator|!
name|cmisProperties
operator|.
name|containsKey
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_TYPE_ID
argument_list|)
condition|)
block|{
name|cmisProperties
operator|.
name|put
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_TYPE_ID
argument_list|,
name|CamelCMISConstants
operator|.
name|CMIS_FOLDER
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating folder with properties: {0}"
argument_list|,
name|cmisProperties
argument_list|)
expr_stmt|;
return|return
name|parentFolder
operator|.
name|createFolder
argument_list|(
name|cmisProperties
argument_list|)
return|;
block|}
DECL|method|storeDocument (Folder parentFolder, Map<String, Object> cmisProperties, ContentStream contentStream, CMISSessionFacade cmisSessionFacade)
specifier|private
name|Document
name|storeDocument
parameter_list|(
name|Folder
name|parentFolder
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|cmisProperties
parameter_list|,
name|ContentStream
name|contentStream
parameter_list|,
name|CMISSessionFacade
name|cmisSessionFacade
parameter_list|)
block|{
if|if
condition|(
operator|!
name|cmisProperties
operator|.
name|containsKey
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_TYPE_ID
argument_list|)
condition|)
block|{
name|cmisProperties
operator|.
name|put
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_TYPE_ID
argument_list|,
name|CamelCMISConstants
operator|.
name|CMIS_DOCUMENT
argument_list|)
expr_stmt|;
block|}
name|VersioningState
name|versioningState
init|=
name|VersioningState
operator|.
name|NONE
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|cmisSessionFacade
operator|.
name|isObjectTypeVersionable
argument_list|(
operator|(
name|String
operator|)
name|cmisProperties
operator|.
name|get
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_TYPE_ID
argument_list|)
argument_list|)
condition|)
block|{
name|versioningState
operator|=
name|VersioningState
operator|.
name|MAJOR
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating document with properties: {0}"
argument_list|,
name|cmisProperties
argument_list|)
expr_stmt|;
return|return
name|parentFolder
operator|.
name|createDocument
argument_list|(
name|cmisProperties
argument_list|,
name|contentStream
argument_list|,
name|versioningState
argument_list|)
return|;
block|}
DECL|method|validateRequiredHeader (Exchange exchange, String name)
specifier|private
name|void
name|validateRequiredHeader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|NoSuchHeaderException
block|{
name|ExchangeHelper
operator|.
name|getMandatoryHeader
argument_list|(
name|exchange
argument_list|,
name|name
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|isDocument (Exchange exchange)
specifier|private
name|boolean
name|isDocument
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|baseTypeId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_TYPE_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|baseTypeId
operator|!=
literal|null
condition|)
block|{
return|return
name|baseTypeId
operator|.
name|equals
argument_list|(
name|CamelCMISConstants
operator|.
name|CMIS_DOCUMENT
argument_list|)
return|;
block|}
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
return|;
block|}
DECL|method|getBodyData (Message message)
specifier|private
name|byte
index|[]
name|getBodyData
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
operator|.
name|getBody
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
operator|.
name|getClass
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getMimeType (Message message)
specifier|private
name|String
name|getMimeType
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|NoSuchHeaderException
block|{
name|String
name|mimeType
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|PropertyIds
operator|.
name|CONTENT_STREAM_MIME_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|mimeType
operator|==
literal|null
condition|)
block|{
name|mimeType
operator|=
name|MessageHelper
operator|.
name|getContentType
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
return|return
name|mimeType
return|;
block|}
block|}
end_class

end_unit

