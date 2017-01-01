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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|DocumentType
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
name|client
operator|.
name|api
operator|.
name|ItemIterable
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
name|ObjectType
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
name|OperationContext
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
name|QueryResult
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
name|Session
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
name|SessionParameter
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
name|BaseTypeId
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
name|BindingType
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
name|CmisVersion
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
DECL|class|CMISSessionFacade
specifier|public
class|class
name|CMISSessionFacade
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
name|CMISSessionFacade
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|session
specifier|private
specifier|transient
name|Session
name|session
decl_stmt|;
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"100"
argument_list|)
DECL|field|pageSize
specifier|private
name|int
name|pageSize
init|=
literal|100
decl_stmt|;
annotation|@
name|UriParam
DECL|field|readCount
specifier|private
name|int
name|readCount
decl_stmt|;
annotation|@
name|UriParam
DECL|field|readContent
specifier|private
name|boolean
name|readContent
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
DECL|field|username
specifier|private
name|String
name|username
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
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
DECL|field|repositoryId
specifier|private
name|String
name|repositoryId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
DECL|method|CMISSessionFacade (String url)
specifier|public
name|CMISSessionFacade
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
DECL|method|initSession ()
name|void
name|initSession
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameter
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|parameter
operator|.
name|put
argument_list|(
name|SessionParameter
operator|.
name|BINDING_TYPE
argument_list|,
name|BindingType
operator|.
name|ATOMPUB
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|parameter
operator|.
name|put
argument_list|(
name|SessionParameter
operator|.
name|ATOMPUB_URL
argument_list|,
name|this
operator|.
name|url
argument_list|)
expr_stmt|;
name|parameter
operator|.
name|put
argument_list|(
name|SessionParameter
operator|.
name|USER
argument_list|,
name|this
operator|.
name|username
argument_list|)
expr_stmt|;
name|parameter
operator|.
name|put
argument_list|(
name|SessionParameter
operator|.
name|PASSWORD
argument_list|,
name|this
operator|.
name|password
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|repositoryId
operator|!=
literal|null
condition|)
block|{
name|parameter
operator|.
name|put
argument_list|(
name|SessionParameter
operator|.
name|REPOSITORY_ID
argument_list|,
name|this
operator|.
name|repositoryId
argument_list|)
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|SessionFactoryLocator
operator|.
name|getSessionFactory
argument_list|()
operator|.
name|createSession
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|session
operator|=
name|SessionFactoryLocator
operator|.
name|getSessionFactory
argument_list|()
operator|.
name|getRepositories
argument_list|(
name|parameter
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|createSession
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|poll (CMISConsumer cmisConsumer)
specifier|public
name|int
name|poll
parameter_list|(
name|CMISConsumer
name|cmisConsumer
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
return|return
name|pollWithQuery
argument_list|(
name|cmisConsumer
argument_list|)
return|;
block|}
return|return
name|pollTree
argument_list|(
name|cmisConsumer
argument_list|)
return|;
block|}
DECL|method|pollTree (CMISConsumer cmisConsumer)
specifier|private
name|int
name|pollTree
parameter_list|(
name|CMISConsumer
name|cmisConsumer
parameter_list|)
throws|throws
name|Exception
block|{
name|Folder
name|rootFolder
init|=
name|session
operator|.
name|getRootFolder
argument_list|()
decl_stmt|;
name|RecursiveTreeWalker
name|treeWalker
init|=
operator|new
name|RecursiveTreeWalker
argument_list|(
name|cmisConsumer
argument_list|,
name|readContent
argument_list|,
name|readCount
argument_list|,
name|pageSize
argument_list|)
decl_stmt|;
return|return
name|treeWalker
operator|.
name|processFolderRecursively
argument_list|(
name|rootFolder
argument_list|)
return|;
block|}
DECL|method|pollWithQuery (CMISConsumer cmisConsumer)
specifier|private
name|int
name|pollWithQuery
parameter_list|(
name|CMISConsumer
name|cmisConsumer
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
name|int
name|pageNumber
init|=
literal|0
decl_stmt|;
name|boolean
name|finished
init|=
literal|false
decl_stmt|;
name|ItemIterable
argument_list|<
name|QueryResult
argument_list|>
name|itemIterable
init|=
name|executeQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
while|while
condition|(
operator|!
name|finished
condition|)
block|{
name|ItemIterable
argument_list|<
name|QueryResult
argument_list|>
name|currentPage
init|=
name|itemIterable
operator|.
name|skipTo
argument_list|(
name|count
argument_list|)
operator|.
name|getPage
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing page {}"
argument_list|,
name|pageNumber
argument_list|)
expr_stmt|;
for|for
control|(
name|QueryResult
name|item
range|:
name|currentPage
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
name|CMISHelper
operator|.
name|propertyDataToMap
argument_list|(
name|item
operator|.
name|getProperties
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|objectTypeId
init|=
name|item
operator|.
name|getPropertyValueById
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_TYPE_ID
argument_list|)
decl_stmt|;
name|InputStream
name|inputStream
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|readContent
operator|&&
name|CamelCMISConstants
operator|.
name|CMIS_DOCUMENT
operator|.
name|equals
argument_list|(
name|objectTypeId
argument_list|)
condition|)
block|{
name|inputStream
operator|=
name|getContentStreamFor
argument_list|(
name|item
argument_list|)
expr_stmt|;
block|}
name|cmisConsumer
operator|.
name|sendExchangeWithPropsAndBody
argument_list|(
name|properties
argument_list|,
name|inputStream
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
if|if
condition|(
name|count
operator|==
name|readCount
condition|)
block|{
name|finished
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|pageNumber
operator|++
expr_stmt|;
if|if
condition|(
operator|!
name|currentPage
operator|.
name|getHasMoreItems
argument_list|()
condition|)
block|{
name|finished
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|count
return|;
block|}
comment|//some duplication
DECL|method|retrieveResult (Boolean retrieveContent, Integer readSize, ItemIterable<QueryResult> itemIterable)
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|retrieveResult
parameter_list|(
name|Boolean
name|retrieveContent
parameter_list|,
name|Integer
name|readSize
parameter_list|,
name|ItemIterable
argument_list|<
name|QueryResult
argument_list|>
name|itemIterable
parameter_list|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|queryForContent
init|=
name|retrieveContent
operator|!=
literal|null
condition|?
name|retrieveContent
else|:
name|readContent
decl_stmt|;
name|int
name|documentsToRead
init|=
name|readSize
operator|!=
literal|null
condition|?
name|readSize
else|:
name|readCount
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
name|int
name|pageNumber
init|=
literal|0
decl_stmt|;
name|boolean
name|finished
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|!
name|finished
condition|)
block|{
name|ItemIterable
argument_list|<
name|QueryResult
argument_list|>
name|currentPage
init|=
name|itemIterable
operator|.
name|skipTo
argument_list|(
name|count
argument_list|)
operator|.
name|getPage
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing page {}"
argument_list|,
name|pageNumber
argument_list|)
expr_stmt|;
for|for
control|(
name|QueryResult
name|item
range|:
name|currentPage
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
name|CMISHelper
operator|.
name|propertyDataToMap
argument_list|(
name|item
operator|.
name|getProperties
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|queryForContent
condition|)
block|{
name|InputStream
name|inputStream
init|=
name|getContentStreamFor
argument_list|(
name|item
argument_list|)
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|CamelCMISConstants
operator|.
name|CAMEL_CMIS_CONTENT_STREAM
argument_list|,
name|inputStream
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|add
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
if|if
condition|(
name|count
operator|==
name|documentsToRead
condition|)
block|{
name|finished
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|pageNumber
operator|++
expr_stmt|;
if|if
condition|(
operator|!
name|currentPage
operator|.
name|getHasMoreItems
argument_list|()
condition|)
block|{
name|finished
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|executeQuery (String query)
specifier|public
name|ItemIterable
argument_list|<
name|QueryResult
argument_list|>
name|executeQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|OperationContext
name|operationContext
init|=
name|session
operator|.
name|createOperationContext
argument_list|()
decl_stmt|;
name|operationContext
operator|.
name|setMaxItemsPerPage
argument_list|(
name|pageSize
argument_list|)
expr_stmt|;
return|return
name|session
operator|.
name|query
argument_list|(
name|query
argument_list|,
literal|false
argument_list|,
name|operationContext
argument_list|)
return|;
block|}
DECL|method|getDocument (QueryResult queryResult)
specifier|public
name|Document
name|getDocument
parameter_list|(
name|QueryResult
name|queryResult
parameter_list|)
block|{
if|if
condition|(
name|CamelCMISConstants
operator|.
name|CMIS_DOCUMENT
operator|.
name|equals
argument_list|(
name|queryResult
operator|.
name|getPropertyValueById
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_TYPE_ID
argument_list|)
argument_list|)
operator|||
name|CamelCMISConstants
operator|.
name|CMIS_DOCUMENT
operator|.
name|equals
argument_list|(
name|queryResult
operator|.
name|getPropertyValueById
argument_list|(
name|PropertyIds
operator|.
name|BASE_TYPE_ID
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|objectId
init|=
operator|(
name|String
operator|)
name|queryResult
operator|.
name|getPropertyById
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_ID
argument_list|)
operator|.
name|getFirstValue
argument_list|()
decl_stmt|;
return|return
operator|(
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
operator|)
name|session
operator|.
name|getObject
argument_list|(
name|objectId
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getContentStreamFor (QueryResult item)
specifier|public
name|InputStream
name|getContentStreamFor
parameter_list|(
name|QueryResult
name|item
parameter_list|)
block|{
name|Document
name|document
init|=
name|getDocument
argument_list|(
name|item
argument_list|)
decl_stmt|;
if|if
condition|(
name|document
operator|!=
literal|null
operator|&&
name|document
operator|.
name|getContentStream
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|document
operator|.
name|getContentStream
argument_list|()
operator|.
name|getStream
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getObjectByPath (String path)
specifier|public
name|CmisObject
name|getObjectByPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|session
operator|.
name|getObjectByPath
argument_list|(
name|path
argument_list|)
return|;
block|}
DECL|method|isObjectTypeVersionable (String objectType)
specifier|public
name|boolean
name|isObjectTypeVersionable
parameter_list|(
name|String
name|objectType
parameter_list|)
block|{
if|if
condition|(
name|CamelCMISConstants
operator|.
name|CMIS_DOCUMENT
operator|.
name|equals
argument_list|(
name|getCMISTypeFor
argument_list|(
name|objectType
argument_list|)
argument_list|)
condition|)
block|{
name|ObjectType
name|typeDefinition
init|=
name|session
operator|.
name|getTypeDefinition
argument_list|(
name|objectType
argument_list|)
decl_stmt|;
return|return
operator|(
operator|(
name|DocumentType
operator|)
name|typeDefinition
operator|)
operator|.
name|isVersionable
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|supportsSecondaries ()
specifier|public
name|boolean
name|supportsSecondaries
parameter_list|()
block|{
if|if
condition|(
name|session
operator|.
name|getRepositoryInfo
argument_list|()
operator|.
name|getCmisVersion
argument_list|()
operator|==
name|CmisVersion
operator|.
name|CMIS_1_0
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|ObjectType
name|type
range|:
name|session
operator|.
name|getTypeChildren
argument_list|(
literal|null
argument_list|,
literal|false
argument_list|)
control|)
block|{
if|if
condition|(
name|BaseTypeId
operator|.
name|CMIS_SECONDARY
operator|.
name|value
argument_list|()
operator|.
name|equals
argument_list|(
name|type
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|createContentStream (String fileName, byte[] buf, String mimeType)
specifier|public
name|ContentStream
name|createContentStream
parameter_list|(
name|String
name|fileName
parameter_list|,
name|byte
index|[]
name|buf
parameter_list|,
name|String
name|mimeType
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|buf
operator|!=
literal|null
condition|?
name|session
operator|.
name|getObjectFactory
argument_list|()
operator|.
name|createContentStream
argument_list|(
name|fileName
argument_list|,
name|buf
operator|.
name|length
argument_list|,
name|mimeType
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|buf
argument_list|)
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|getCMISTypeFor (String customOrCMISType)
specifier|public
name|String
name|getCMISTypeFor
parameter_list|(
name|String
name|customOrCMISType
parameter_list|)
block|{
name|ObjectType
name|objectBaseType
init|=
name|session
operator|.
name|getTypeDefinition
argument_list|(
name|customOrCMISType
argument_list|)
operator|.
name|getBaseType
argument_list|()
decl_stmt|;
return|return
name|objectBaseType
operator|==
literal|null
condition|?
name|customOrCMISType
else|:
name|objectBaseType
operator|.
name|getId
argument_list|()
return|;
block|}
DECL|method|getPropertiesFor (String objectType)
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getPropertiesFor
parameter_list|(
name|String
name|objectType
parameter_list|)
block|{
return|return
name|session
operator|.
name|getTypeDefinition
argument_list|(
name|objectType
argument_list|)
operator|.
name|getPropertyDefinitions
argument_list|()
operator|.
name|keySet
argument_list|()
return|;
block|}
DECL|method|createOperationContext ()
specifier|public
name|OperationContext
name|createOperationContext
parameter_list|()
block|{
return|return
name|session
operator|.
name|createOperationContext
argument_list|()
return|;
block|}
comment|/**      * Username for the cmis repository      */
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
comment|/**      * Password for the cmis repository      */
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
comment|/**      * The Id of the repository to use. If not specified the first available repository is used      */
DECL|method|setRepositoryId (String repositoryId)
specifier|public
name|void
name|setRepositoryId
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
block|}
comment|/**      * If set to true, the content of document node will be retrieved in addition to the properties      */
DECL|method|setReadContent (boolean readContent)
specifier|public
name|void
name|setReadContent
parameter_list|(
name|boolean
name|readContent
parameter_list|)
block|{
name|this
operator|.
name|readContent
operator|=
name|readContent
expr_stmt|;
block|}
comment|/**      * Max number of nodes to read      */
DECL|method|setReadCount (int readCount)
specifier|public
name|void
name|setReadCount
parameter_list|(
name|int
name|readCount
parameter_list|)
block|{
name|this
operator|.
name|readCount
operator|=
name|readCount
expr_stmt|;
block|}
comment|/**      * The cmis query to execute against the repository.      * If not specified, the consumer will retrieve every node from the content repository by iterating the content tree recursively      */
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
comment|/**      * Number of nodes to retrieve per page      */
DECL|method|setPageSize (int pageSize)
specifier|public
name|void
name|setPageSize
parameter_list|(
name|int
name|pageSize
parameter_list|)
block|{
name|this
operator|.
name|pageSize
operator|=
name|pageSize
expr_stmt|;
block|}
block|}
end_class

end_unit

