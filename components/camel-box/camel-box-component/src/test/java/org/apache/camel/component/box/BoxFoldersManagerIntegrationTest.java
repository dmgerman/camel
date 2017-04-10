begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|box
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxAPIConnection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxFolder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxItem
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxSharedLink
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
name|builder
operator|.
name|RouteBuilder
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
name|box
operator|.
name|api
operator|.
name|BoxFoldersManager
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
name|box
operator|.
name|internal
operator|.
name|BoxApiCollection
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
name|box
operator|.
name|internal
operator|.
name|BoxFoldersManagerApiMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|Test
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
comment|/**  * Test class for {@link BoxFoldersManager}  * APIs.  */
end_comment

begin_class
DECL|class|BoxFoldersManagerIntegrationTest
specifier|public
class|class
name|BoxFoldersManagerIntegrationTest
extends|extends
name|AbstractBoxTestSupport
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
name|BoxFoldersManagerIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PATH_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PREFIX
init|=
name|BoxApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|BoxFoldersManagerApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|CAMEL_TEST_FOLDER
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_FOLDER
init|=
literal|"CamelTestFolder"
decl_stmt|;
DECL|field|CAMEL_TEST_FOLDER_DESCRIPTION
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_FOLDER_DESCRIPTION
init|=
literal|"This is a description of CamelTestFolder"
decl_stmt|;
DECL|field|CAMEL_TEST_COPY_FOLDER
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_COPY_FOLDER
init|=
name|BoxFoldersManagerIntegrationTest
operator|.
name|CAMEL_TEST_FOLDER
operator|+
literal|"_Copy"
decl_stmt|;
DECL|field|CAMEL_TEST_MOVE_FOLDER
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_MOVE_FOLDER
init|=
name|BoxFoldersManagerIntegrationTest
operator|.
name|CAMEL_TEST_FOLDER
operator|+
literal|"_Move"
decl_stmt|;
DECL|field|CAMEL_TEST_RENAME_FOLDER
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_RENAME_FOLDER
init|=
name|BoxFoldersManagerIntegrationTest
operator|.
name|CAMEL_TEST_FOLDER
operator|+
literal|"_Rename"
decl_stmt|;
DECL|field|CAMEL_TEST_ROOT_FOLDER_ID
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_ROOT_FOLDER_ID
init|=
literal|"0"
decl_stmt|;
DECL|field|CAMEL_TEST_DESTINATION_FOLDER_ID
specifier|private
specifier|static
specifier|final
name|String
name|CAMEL_TEST_DESTINATION_FOLDER_ID
init|=
literal|"0"
decl_stmt|;
annotation|@
name|Test
DECL|method|testCreateFolder ()
specifier|public
name|void
name|testCreateFolder
parameter_list|()
throws|throws
name|Exception
block|{
comment|// delete folder created in test setup.
name|deleteTestFolder
argument_list|()
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
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
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.parentFolderId"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.folderName"
argument_list|,
name|CAMEL_TEST_FOLDER
argument_list|)
expr_stmt|;
name|testFolder
operator|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://CREATEFOLDER"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"createFolder result"
argument_list|,
name|testFolder
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"createFolder folder name"
argument_list|,
name|CAMEL_TEST_FOLDER
argument_list|,
name|testFolder
operator|.
name|getInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"createFolder: "
operator|+
name|testFolder
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDeleteFolder ()
specifier|public
name|void
name|testDeleteFolder
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String message body for single parameter "folderId"
name|requestBody
argument_list|(
literal|"direct://DELETEFOLDER"
argument_list|,
name|testFolder
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|BoxFolder
name|rootFolder
init|=
name|BoxFolder
operator|.
name|getRootFolder
argument_list|(
name|getConnection
argument_list|()
argument_list|)
decl_stmt|;
name|Iterable
argument_list|<
name|BoxItem
operator|.
name|Info
argument_list|>
name|it
init|=
name|rootFolder
operator|.
name|search
argument_list|(
literal|"^"
operator|+
name|CAMEL_TEST_FOLDER
operator|+
literal|"$"
argument_list|)
decl_stmt|;
name|int
name|searchResults
init|=
name|sizeOfIterable
argument_list|(
name|it
argument_list|)
decl_stmt|;
name|boolean
name|exists
init|=
name|searchResults
operator|>
literal|0
condition|?
literal|true
else|:
literal|false
decl_stmt|;
name|assertEquals
argument_list|(
literal|"deleteFolder exists"
argument_list|,
literal|false
argument_list|,
name|exists
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"deleteFolder: exists? "
operator|+
name|exists
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCopyFolder ()
specifier|public
name|void
name|testCopyFolder
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxFolder
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
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
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.folderId"
argument_list|,
name|testFolder
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.destinationFolderId"
argument_list|,
name|CAMEL_TEST_DESTINATION_FOLDER_ID
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.newName"
argument_list|,
name|CAMEL_TEST_COPY_FOLDER
argument_list|)
expr_stmt|;
name|result
operator|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://COPYFOLDER"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"copyFolder result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"copyFolder folder name"
argument_list|,
name|CAMEL_TEST_COPY_FOLDER
argument_list|,
name|result
operator|.
name|getInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"copyFolder: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|result
operator|.
name|delete
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{                 }
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testCreateSharedLink ()
specifier|public
name|void
name|testCreateSharedLink
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
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
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.folderId"
argument_list|,
name|testFolder
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.sdk.BoxSharedLink.Access
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.access"
argument_list|,
name|BoxSharedLink
operator|.
name|Access
operator|.
name|COLLABORATORS
argument_list|)
expr_stmt|;
comment|// parameter type is java.util.Date
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.unshareDate"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.sdk.BoxSharedLink.Permissions
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.permissions"
argument_list|,
operator|new
name|BoxSharedLink
operator|.
name|Permissions
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxSharedLink
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://CREATEFOLDERSHAREDLINK"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"createFolderSharedLink result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"createFolderSharedLink: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetFolder ()
specifier|public
name|void
name|testGetFolder
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using String[] message body for single parameter "path"
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxFolder
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GETFOLDER"
argument_list|,
operator|new
name|String
index|[]
block|{
name|CAMEL_TEST_FOLDER
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getFolder result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getFolder folder id"
argument_list|,
name|testFolder
operator|.
name|getID
argument_list|()
argument_list|,
name|result
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getFolder: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetFolderInfo ()
specifier|public
name|void
name|testGetFolderInfo
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
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
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.folderId"
argument_list|,
name|testFolder
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is String[]
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.fields"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"name"
block|}
argument_list|)
expr_stmt|;
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxFolder
operator|.
name|Info
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETFOLDERINFO"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getFolderInfo result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"getFolderInfo result.getName()"
argument_list|,
name|result
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getFolderInfo info name"
argument_list|,
name|CAMEL_TEST_FOLDER
argument_list|,
name|result
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getFolderInfo: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetFolderItems ()
specifier|public
name|void
name|testGetFolderItems
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
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
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.folderId"
argument_list|,
name|CAMEL_TEST_ROOT_FOLDER_ID
argument_list|)
expr_stmt|;
comment|// parameter type is Long
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.offset"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is Long
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.limit"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is String[]
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.fields"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|final
name|java
operator|.
name|util
operator|.
name|Collection
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GETFOLDERITEMS"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getFolderItems result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getFolderItems: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetRootFolder ()
specifier|public
name|void
name|testGetRootFolder
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxFolder
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GETROOTFOLDER"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getRootFolder result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"getRootFolder: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMoveFolder ()
specifier|public
name|void
name|testMoveFolder
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
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
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.folderId"
argument_list|,
name|testFolder
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.destinationFolderId"
argument_list|,
name|CAMEL_TEST_DESTINATION_FOLDER_ID
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.newName"
argument_list|,
name|CAMEL_TEST_MOVE_FOLDER
argument_list|)
expr_stmt|;
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxFolder
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://MOVEFOLDER"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"moveFolder result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"moveFolder folder name"
argument_list|,
name|CAMEL_TEST_MOVE_FOLDER
argument_list|,
name|result
operator|.
name|getInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"moveFolder: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRenameFolder ()
specifier|public
name|void
name|testRenameFolder
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
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
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.folderId"
argument_list|,
name|testFolder
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.newFolderName"
argument_list|,
name|CAMEL_TEST_RENAME_FOLDER
argument_list|)
expr_stmt|;
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxFolder
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://RENAMEFOLDER"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"renameFolder result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"moveFolder folder name"
argument_list|,
name|CAMEL_TEST_RENAME_FOLDER
argument_list|,
name|result
operator|.
name|getInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"renameFolder: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdateInfo ()
specifier|public
name|void
name|testUpdateInfo
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BoxFolder
operator|.
name|Info
name|testFolderInfo
init|=
name|testFolder
operator|.
name|getInfo
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
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
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.folderId"
argument_list|,
name|testFolder
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.box.sdk.BoxFolder.Info
name|testFolderInfo
operator|.
name|setDescription
argument_list|(
name|CAMEL_TEST_FOLDER_DESCRIPTION
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelBox.info"
argument_list|,
name|testFolderInfo
argument_list|)
expr_stmt|;
specifier|final
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxFolder
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://UPDATEFOLDERINFO"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"updateInfo result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"update folder info description"
argument_list|,
name|CAMEL_TEST_FOLDER_DESCRIPTION
argument_list|,
name|result
operator|.
name|getInfo
argument_list|()
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"updateInfo: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// test route for copyFolder
name|from
argument_list|(
literal|"direct://COPYFOLDER"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/copyFolder"
argument_list|)
expr_stmt|;
comment|// test route for createFolder
name|from
argument_list|(
literal|"direct://CREATEFOLDER"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/createFolder"
argument_list|)
expr_stmt|;
comment|// test route for createFolderSharedLink
name|from
argument_list|(
literal|"direct://CREATEFOLDERSHAREDLINK"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/createFolderSharedLink"
argument_list|)
expr_stmt|;
comment|// test route for deleteFolder
name|from
argument_list|(
literal|"direct://DELETEFOLDER"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/deleteFolder?inBody=folderId"
argument_list|)
expr_stmt|;
comment|// test route for getFolder
name|from
argument_list|(
literal|"direct://GETFOLDER"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getFolder?inBody=path"
argument_list|)
expr_stmt|;
comment|// test route for getFolderInfo
name|from
argument_list|(
literal|"direct://GETFOLDERINFO"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getFolderInfo"
argument_list|)
expr_stmt|;
comment|// test route for getFolderItems
name|from
argument_list|(
literal|"direct://GETFOLDERITEMS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getFolderItems"
argument_list|)
expr_stmt|;
comment|// test route for getRootFolder
name|from
argument_list|(
literal|"direct://GETROOTFOLDER"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/getRootFolder"
argument_list|)
expr_stmt|;
comment|// test route for moveFolder
name|from
argument_list|(
literal|"direct://MOVEFOLDER"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/moveFolder"
argument_list|)
expr_stmt|;
comment|// test route for renameFolder
name|from
argument_list|(
literal|"direct://RENAMEFOLDER"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/renameFolder"
argument_list|)
expr_stmt|;
comment|// test route for updateFolderInfo
name|from
argument_list|(
literal|"direct://UPDATEFOLDERINFO"
argument_list|)
operator|.
name|to
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/updateFolderInfo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Before
DECL|method|setupTest ()
specifier|public
name|void
name|setupTest
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestFolder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|teardownTest ()
specifier|public
name|void
name|teardownTest
parameter_list|()
block|{
name|deleteTestFolder
argument_list|()
expr_stmt|;
block|}
DECL|method|getConnection ()
specifier|public
name|BoxAPIConnection
name|getConnection
parameter_list|()
block|{
name|BoxEndpoint
name|endpoint
init|=
operator|(
name|BoxEndpoint
operator|)
name|context
argument_list|()
operator|.
name|getEndpoint
argument_list|(
literal|"box://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/copyFolder"
argument_list|)
decl_stmt|;
return|return
name|endpoint
operator|.
name|getBoxConnection
argument_list|()
return|;
block|}
DECL|method|createTestFolder ()
specifier|private
name|void
name|createTestFolder
parameter_list|()
block|{
name|BoxFolder
name|rootFolder
init|=
name|BoxFolder
operator|.
name|getRootFolder
argument_list|(
name|getConnection
argument_list|()
argument_list|)
decl_stmt|;
name|testFolder
operator|=
name|rootFolder
operator|.
name|createFolder
argument_list|(
name|CAMEL_TEST_FOLDER
argument_list|)
operator|.
name|getResource
argument_list|()
expr_stmt|;
block|}
DECL|method|sizeOfIterable (Iterable<?> it)
specifier|private
name|int
name|sizeOfIterable
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|it
parameter_list|)
block|{
if|if
condition|(
name|it
operator|instanceof
name|Collection
condition|)
block|{
return|return
operator|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|it
operator|)
operator|.
name|size
argument_list|()
return|;
block|}
else|else
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|Object
name|obj
range|:
name|it
control|)
block|{
name|i
operator|++
expr_stmt|;
block|}
return|return
name|i
return|;
block|}
block|}
block|}
end_class

end_unit

