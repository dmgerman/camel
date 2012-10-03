begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.componenet.cmis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|componenet
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
name|BufferedReader
import|;
end_import

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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|impl
operator|.
name|DefaultExchange
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|Repository
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
name|client
operator|.
name|api
operator|.
name|SessionFactory
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
name|runtime
operator|.
name|SessionFactoryImpl
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
name|UnfileObject
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
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|webapp
operator|.
name|WebAppContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_class
DECL|class|CMISTestSupport
specifier|public
class|class
name|CMISTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|CMIS_ENDPOINT_TEST_SERVER
specifier|protected
specifier|static
specifier|final
name|String
name|CMIS_ENDPOINT_TEST_SERVER
init|=
literal|"http://localhost:9090/chemistry-opencmis-server-inmemory/atom"
decl_stmt|;
DECL|field|OPEN_CMIS_SERVER_WAR_PATH
specifier|protected
specifier|static
specifier|final
name|String
name|OPEN_CMIS_SERVER_WAR_PATH
init|=
literal|"target/dependency/chemistry-opencmis-server-inmemory-0.7.0.war"
decl_stmt|;
DECL|field|cmisServer
specifier|protected
specifier|static
name|Server
name|cmisServer
decl_stmt|;
DECL|method|createExchangeWithInBody (String body)
specifier|protected
name|Exchange
name|createExchangeWithInBody
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|DefaultExchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
DECL|method|retrieveCMISObjectByIdFromServer (String nodeId)
specifier|protected
name|CmisObject
name|retrieveCMISObjectByIdFromServer
parameter_list|(
name|String
name|nodeId
parameter_list|)
throws|throws
name|Exception
block|{
name|Session
name|session
init|=
name|createSession
argument_list|()
decl_stmt|;
return|return
name|session
operator|.
name|getObject
argument_list|(
name|nodeId
argument_list|)
return|;
block|}
DECL|method|deleteAllContent ()
specifier|protected
name|void
name|deleteAllContent
parameter_list|()
block|{
name|Session
name|session
init|=
name|createSession
argument_list|()
decl_stmt|;
name|Folder
name|rootFolder
init|=
name|session
operator|.
name|getRootFolder
argument_list|()
decl_stmt|;
name|ItemIterable
argument_list|<
name|CmisObject
argument_list|>
name|children
init|=
name|rootFolder
operator|.
name|getChildren
argument_list|()
decl_stmt|;
for|for
control|(
name|CmisObject
name|cmisObject
range|:
name|children
control|)
block|{
if|if
condition|(
literal|"cmis:folder"
operator|.
name|equals
argument_list|(
name|cmisObject
operator|.
name|getPropertyValue
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_TYPE_ID
argument_list|)
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|notDeltedIdList
init|=
operator|(
operator|(
name|Folder
operator|)
name|cmisObject
operator|)
operator|.
name|deleteTree
argument_list|(
literal|true
argument_list|,
name|UnfileObject
operator|.
name|DELETE
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|notDeltedIdList
operator|!=
literal|null
operator|&&
name|notDeltedIdList
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot empty repo"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|cmisObject
operator|.
name|delete
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|session
operator|.
name|getBinding
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|createSession ()
specifier|protected
name|Session
name|createSession
parameter_list|()
block|{
name|SessionFactory
name|sessionFactory
init|=
name|SessionFactoryImpl
operator|.
name|newInstance
argument_list|()
decl_stmt|;
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
name|ATOMPUB_URL
argument_list|,
name|CMIS_ENDPOINT_TEST_SERVER
argument_list|)
expr_stmt|;
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
name|Repository
name|repository
init|=
name|sessionFactory
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
decl_stmt|;
return|return
name|repository
operator|.
name|createSession
argument_list|()
return|;
block|}
DECL|method|getDocumentContentAsString (String nodeId)
specifier|protected
name|String
name|getDocumentContentAsString
parameter_list|(
name|String
name|nodeId
parameter_list|)
throws|throws
name|Exception
block|{
name|CmisObject
name|cmisObject
init|=
name|retrieveCMISObjectByIdFromServer
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
operator|(
name|Document
operator|)
name|cmisObject
decl_stmt|;
name|InputStream
name|inputStream
init|=
name|doc
operator|.
name|getContentStream
argument_list|()
operator|.
name|getStream
argument_list|()
decl_stmt|;
return|return
name|readFromStream
argument_list|(
name|inputStream
argument_list|)
return|;
block|}
DECL|method|readFromStream (InputStream in)
specifier|protected
name|String
name|readFromStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|Exception
block|{
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|strLine
decl_stmt|;
while|while
condition|(
operator|(
name|strLine
operator|=
name|br
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|strLine
argument_list|)
expr_stmt|;
block|}
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|createFolderWithName (String folderName)
specifier|protected
name|Folder
name|createFolderWithName
parameter_list|(
name|String
name|folderName
parameter_list|)
block|{
name|Folder
name|rootFolder
init|=
name|createSession
argument_list|()
operator|.
name|getRootFolder
argument_list|()
decl_stmt|;
return|return
name|createChildFolderWithName
argument_list|(
name|rootFolder
argument_list|,
name|folderName
argument_list|)
return|;
block|}
DECL|method|createChildFolderWithName (Folder parent, String childName)
specifier|protected
name|Folder
name|createChildFolderWithName
parameter_list|(
name|Folder
name|parent
parameter_list|,
name|String
name|childName
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|newFolderProps
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
name|newFolderProps
operator|.
name|put
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_TYPE_ID
argument_list|,
literal|"cmis:folder"
argument_list|)
expr_stmt|;
name|newFolderProps
operator|.
name|put
argument_list|(
name|PropertyIds
operator|.
name|NAME
argument_list|,
name|childName
argument_list|)
expr_stmt|;
return|return
name|parent
operator|.
name|createFolder
argument_list|(
name|newFolderProps
argument_list|)
return|;
block|}
DECL|method|createTextDocument (Folder newFolder, String content, String fileName)
specifier|protected
name|void
name|createTextDocument
parameter_list|(
name|Folder
name|newFolder
parameter_list|,
name|String
name|content
parameter_list|,
name|String
name|fileName
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|byte
index|[]
name|buf
init|=
name|content
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|input
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|buf
argument_list|)
decl_stmt|;
name|ContentStream
name|contentStream
init|=
name|createSession
argument_list|()
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
literal|"text/plain; charset=UTF-8"
argument_list|,
name|input
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|PropertyIds
operator|.
name|OBJECT_TYPE_ID
argument_list|,
literal|"cmis:document"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|PropertyIds
operator|.
name|NAME
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|newFolder
operator|.
name|createDocument
argument_list|(
name|properties
argument_list|,
name|contentStream
argument_list|,
name|VersioningState
operator|.
name|NONE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|BeforeClass
DECL|method|startServer ()
specifier|public
specifier|static
name|void
name|startServer
parameter_list|()
throws|throws
name|Exception
block|{
name|cmisServer
operator|=
operator|new
name|Server
argument_list|(
literal|9090
argument_list|)
expr_stmt|;
name|cmisServer
operator|.
name|setHandler
argument_list|(
operator|new
name|WebAppContext
argument_list|(
name|OPEN_CMIS_SERVER_WAR_PATH
argument_list|,
literal|"/chemistry-opencmis-server-inmemory"
argument_list|)
argument_list|)
expr_stmt|;
name|cmisServer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|stopServer ()
specifier|public
specifier|static
name|void
name|stopServer
parameter_list|()
throws|throws
name|Exception
block|{
name|cmisServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteAllContent
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

