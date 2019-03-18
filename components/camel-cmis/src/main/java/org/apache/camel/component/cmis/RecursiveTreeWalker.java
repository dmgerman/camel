begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|InputStream
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
DECL|class|RecursiveTreeWalker
specifier|public
class|class
name|RecursiveTreeWalker
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
name|RecursiveTreeWalker
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cmisConsumer
specifier|private
specifier|final
name|CMISConsumer
name|cmisConsumer
decl_stmt|;
DECL|field|readContent
specifier|private
specifier|final
name|boolean
name|readContent
decl_stmt|;
DECL|field|readCount
specifier|private
specifier|final
name|int
name|readCount
decl_stmt|;
DECL|field|pageSize
specifier|private
specifier|final
name|int
name|pageSize
decl_stmt|;
DECL|field|totalPolled
specifier|private
name|int
name|totalPolled
decl_stmt|;
DECL|method|RecursiveTreeWalker (CMISConsumer cmisConsumer, boolean readContent, int readCount, int pageSize)
specifier|public
name|RecursiveTreeWalker
parameter_list|(
name|CMISConsumer
name|cmisConsumer
parameter_list|,
name|boolean
name|readContent
parameter_list|,
name|int
name|readCount
parameter_list|,
name|int
name|pageSize
parameter_list|)
block|{
name|this
operator|.
name|cmisConsumer
operator|=
name|cmisConsumer
expr_stmt|;
name|this
operator|.
name|readContent
operator|=
name|readContent
expr_stmt|;
name|this
operator|.
name|readCount
operator|=
name|readCount
expr_stmt|;
name|this
operator|.
name|pageSize
operator|=
name|pageSize
expr_stmt|;
block|}
DECL|method|processFolderRecursively (Folder folder)
name|int
name|processFolderRecursively
parameter_list|(
name|Folder
name|folder
parameter_list|)
throws|throws
name|Exception
block|{
name|processFolderNode
argument_list|(
name|folder
argument_list|)
expr_stmt|;
name|OperationContext
name|operationContext
init|=
name|cmisConsumer
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
name|CmisObject
argument_list|>
name|itemIterable
init|=
name|folder
operator|.
name|getChildren
argument_list|(
name|operationContext
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
name|CmisObject
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
name|CmisObject
name|child
range|:
name|currentPage
control|)
block|{
if|if
condition|(
name|CMISHelper
operator|.
name|isFolder
argument_list|(
name|child
argument_list|)
condition|)
block|{
name|Folder
name|childFolder
init|=
operator|(
name|Folder
operator|)
name|child
decl_stmt|;
name|processFolderRecursively
argument_list|(
name|childFolder
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|processNonFolderNode
argument_list|(
name|child
argument_list|,
name|folder
argument_list|)
expr_stmt|;
block|}
name|count
operator|++
expr_stmt|;
if|if
condition|(
name|totalPolled
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
name|totalPolled
return|;
block|}
DECL|method|processNonFolderNode (CmisObject cmisObject, Folder parentFolder)
specifier|private
name|void
name|processNonFolderNode
parameter_list|(
name|CmisObject
name|cmisObject
parameter_list|,
name|Folder
name|parentFolder
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|inputStream
init|=
literal|null
decl_stmt|;
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
name|objectProperties
argument_list|(
name|cmisObject
argument_list|)
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|CamelCMISConstants
operator|.
name|CMIS_FOLDER_PATH
argument_list|,
name|parentFolder
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|CMISHelper
operator|.
name|isDocument
argument_list|(
name|cmisObject
argument_list|)
operator|&&
name|readContent
condition|)
block|{
name|ContentStream
name|contentStream
init|=
operator|(
operator|(
name|Document
operator|)
name|cmisObject
operator|)
operator|.
name|getContentStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentStream
operator|!=
literal|null
condition|)
block|{
name|inputStream
operator|=
name|contentStream
operator|.
name|getStream
argument_list|()
expr_stmt|;
block|}
block|}
name|sendNode
argument_list|(
name|properties
argument_list|,
name|inputStream
argument_list|)
expr_stmt|;
block|}
DECL|method|processFolderNode (Folder folder)
specifier|private
name|void
name|processFolderNode
parameter_list|(
name|Folder
name|folder
parameter_list|)
throws|throws
name|Exception
block|{
name|sendNode
argument_list|(
name|CMISHelper
operator|.
name|objectProperties
argument_list|(
name|folder
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|sendNode (Map<String, Object> properties, InputStream inputStream)
specifier|private
name|void
name|sendNode
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|totalPolled
operator|+=
name|cmisConsumer
operator|.
name|sendExchangeWithPropsAndBody
argument_list|(
name|properties
argument_list|,
name|inputStream
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

