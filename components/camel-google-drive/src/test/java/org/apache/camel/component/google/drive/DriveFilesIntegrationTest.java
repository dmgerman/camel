begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Camel Api Route test generated by camel-component-util-maven-plugin  * Generated on: Thu Jul 03 16:04:18 NDT 2014  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.drive
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|drive
package|;
end_package

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
name|Iterator
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
name|google
operator|.
name|drive
operator|.
name|internal
operator|.
name|GoogleDriveApiCollection
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
name|google
operator|.
name|drive
operator|.
name|internal
operator|.
name|DriveFilesApiMethod
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|http
operator|.
name|FileContent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|util
operator|.
name|DateTime
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|model
operator|.
name|File
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|model
operator|.
name|FileList
import|;
end_import

begin_comment
comment|/**  * Test class for com.google.api.services.drive.Drive$Files APIs.  */
end_comment

begin_class
DECL|class|DriveFilesIntegrationTest
specifier|public
class|class
name|DriveFilesIntegrationTest
extends|extends
name|AbstractGoogleDriveTestSupport
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
name|DriveFilesIntegrationTest
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
name|GoogleDriveApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|DriveFilesApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|TEST_UPLOAD_FILE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_UPLOAD_FILE
init|=
literal|"src/test/resources/log4j.properties"
decl_stmt|;
DECL|field|TEST_UPLOAD_IMG
specifier|private
specifier|static
specifier|final
name|String
name|TEST_UPLOAD_IMG
init|=
literal|"src/test/resources/camel-box-small.png"
decl_stmt|;
DECL|field|UPLOAD_FILE
specifier|private
specifier|static
specifier|final
name|java
operator|.
name|io
operator|.
name|File
name|UPLOAD_FILE
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|File
argument_list|(
name|TEST_UPLOAD_FILE
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testCopy ()
specifier|public
name|void
name|testCopy
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|testFile
init|=
name|uploadTestFile
argument_list|()
decl_stmt|;
name|String
name|fromFileId
init|=
name|testFile
operator|.
name|getId
argument_list|()
decl_stmt|;
name|File
name|toFile
init|=
operator|new
name|File
argument_list|()
decl_stmt|;
name|toFile
operator|.
name|setTitle
argument_list|(
name|UPLOAD_FILE
operator|.
name|getName
argument_list|()
operator|+
literal|"_copy"
argument_list|)
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
literal|"CamelGoogleDrive.fileId"
argument_list|,
name|fromFileId
argument_list|)
expr_stmt|;
comment|// parameter type is com.google.api.services.drive.model.File
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleDrive.content"
argument_list|,
name|toFile
argument_list|)
expr_stmt|;
specifier|final
name|File
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://COPY"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"copy result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|toFile
operator|.
name|getTitle
argument_list|()
argument_list|,
name|result
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"copy: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDelete ()
specifier|public
name|void
name|testDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|testFile
init|=
name|uploadTestFile
argument_list|()
decl_stmt|;
name|String
name|fileId
init|=
name|testFile
operator|.
name|getId
argument_list|()
decl_stmt|;
comment|// using String message body for single parameter "fileId"
name|sendBody
argument_list|(
literal|"direct://DELETE"
argument_list|,
name|fileId
argument_list|)
expr_stmt|;
comment|// the file should be gone now
specifier|final
name|File
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GET"
argument_list|,
name|fileId
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"get result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGet ()
specifier|public
name|void
name|testGet
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|testFile
init|=
name|uploadTestFile
argument_list|()
decl_stmt|;
name|String
name|fileId
init|=
name|testFile
operator|.
name|getId
argument_list|()
decl_stmt|;
comment|// using String message body for single parameter "fileId"
specifier|final
name|File
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GET"
argument_list|,
name|fileId
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"get result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"get: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInsert ()
specifier|public
name|void
name|testInsert
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|()
decl_stmt|;
name|file
operator|.
name|setTitle
argument_list|(
name|UPLOAD_FILE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// using com.google.api.services.drive.model.File message body for single parameter "content"
name|File
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://INSERT"
argument_list|,
name|file
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"insert result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"insert: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|uploadTestFile ()
specifier|private
name|File
name|uploadTestFile
parameter_list|()
block|{
name|File
name|fileMetadata
init|=
operator|new
name|File
argument_list|()
decl_stmt|;
name|fileMetadata
operator|.
name|setTitle
argument_list|(
name|UPLOAD_FILE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|FileContent
name|mediaContent
init|=
operator|new
name|FileContent
argument_list|(
literal|null
argument_list|,
name|UPLOAD_FILE
argument_list|)
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
comment|// parameter type is com.google.api.services.drive.model.File
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleDrive.content"
argument_list|,
name|fileMetadata
argument_list|)
expr_stmt|;
comment|// parameter type is com.google.api.client.http.AbstractInputStreamContent
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleDrive.mediaContent"
argument_list|,
name|mediaContent
argument_list|)
expr_stmt|;
name|File
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://INSERT_1"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Test
DECL|method|testInsert_1 ()
specifier|public
name|void
name|testInsert_1
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|result
init|=
name|uploadTestFile
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"insert result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"insert: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testList ()
specifier|public
name|void
name|testList
parameter_list|()
throws|throws
name|Exception
block|{
comment|// upload a test file
name|File
name|theTestFile
init|=
name|uploadTestFile
argument_list|()
decl_stmt|;
specifier|final
name|FileList
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://LIST"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"list result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"list: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testPatch ()
specifier|public
name|void
name|testPatch
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO have to support setting patch parameters before callinfg execute like:
comment|/*       File file = new File();       file.setTitle(newTitle);        // Rename the file using a patch request.       Files.Patch patchRequest = service.files().patch(fileId, file);       patchRequest.setFields("title");        File updatedFile = patchRequest.execute();          */
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
literal|"CamelGoogleDrive.fileId"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is com.google.api.services.drive.model.File
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleDrive.content"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
specifier|final
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|Drive
operator|.
name|Files
operator|.
name|Patch
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://PATCH"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"patch result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"patch: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTouch ()
specifier|public
name|void
name|testTouch
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|theTestFile
init|=
name|uploadTestFile
argument_list|()
decl_stmt|;
name|DateTime
name|createdDate
init|=
name|theTestFile
operator|.
name|getModifiedDate
argument_list|()
decl_stmt|;
comment|// using String message body for single parameter "fileId"
name|File
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://TOUCH"
argument_list|,
name|theTestFile
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"touch result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getModifiedDate
argument_list|()
operator|.
name|getValue
argument_list|()
operator|>
name|createdDate
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTrash ()
specifier|public
name|void
name|testTrash
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|testFile
init|=
name|uploadTestFile
argument_list|()
decl_stmt|;
name|String
name|fileId
init|=
name|testFile
operator|.
name|getId
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"trash result"
argument_list|,
name|requestBody
argument_list|(
literal|"direct://TRASH"
argument_list|,
name|fileId
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"untrash result"
argument_list|,
name|requestBody
argument_list|(
literal|"direct://UNTRASH"
argument_list|,
name|fileId
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdate ()
specifier|public
name|void
name|testUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|theTestFile
init|=
name|uploadTestFile
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
literal|"CamelGoogleDrive.fileId"
argument_list|,
name|theTestFile
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.google.api.services.drive.model.File
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleDrive.content"
argument_list|,
name|theTestFile
argument_list|)
expr_stmt|;
name|File
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://UPDATE"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"update result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"update: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdate_1 ()
specifier|public
name|void
name|testUpdate_1
parameter_list|()
throws|throws
name|Exception
block|{
comment|// First retrieve the file from the API.
name|File
name|testFile
init|=
name|uploadTestFile
argument_list|()
decl_stmt|;
name|String
name|fileId
init|=
name|testFile
operator|.
name|getId
argument_list|()
decl_stmt|;
comment|// using String message body for single parameter "fileId"
specifier|final
name|File
name|file
init|=
name|requestBody
argument_list|(
literal|"direct://GET"
argument_list|,
name|fileId
argument_list|)
decl_stmt|;
comment|// File's new metadata.
name|file
operator|.
name|setTitle
argument_list|(
literal|"camel.png"
argument_list|)
expr_stmt|;
name|file
operator|.
name|setMimeType
argument_list|(
literal|"application/vnd.google-apps.photo"
argument_list|)
expr_stmt|;
comment|// File's new content.
name|java
operator|.
name|io
operator|.
name|File
name|fileContent
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|File
argument_list|(
name|TEST_UPLOAD_IMG
argument_list|)
decl_stmt|;
name|FileContent
name|mediaContent
init|=
operator|new
name|FileContent
argument_list|(
literal|"application/vnd.google-apps.photo"
argument_list|,
name|fileContent
argument_list|)
decl_stmt|;
comment|// Send the request to the API.
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
literal|"CamelGoogleDrive.fileId"
argument_list|,
name|fileId
argument_list|)
expr_stmt|;
comment|// parameter type is com.google.api.services.drive.model.File
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleDrive.content"
argument_list|,
name|file
argument_list|)
expr_stmt|;
comment|// parameter type is com.google.api.client.http.AbstractInputStreamContent
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleDrive.mediaContent"
argument_list|,
name|mediaContent
argument_list|)
expr_stmt|;
name|File
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://UPDATE_1"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"update result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"update: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
comment|// TODO provide parameter values for watch
annotation|@
name|Ignore
annotation|@
name|Test
DECL|method|testWatch ()
specifier|public
name|void
name|testWatch
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
literal|"CamelGoogleDrive.fileId"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// parameter type is com.google.api.services.drive.model.Channel
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleDrive.contentChannel"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
specifier|final
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|drive
operator|.
name|Drive
operator|.
name|Files
operator|.
name|Watch
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://WATCH"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"watch result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"watch: "
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
comment|// test route for copy
name|from
argument_list|(
literal|"direct://COPY"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/copy"
argument_list|)
expr_stmt|;
comment|// test route for delete
name|from
argument_list|(
literal|"direct://DELETE"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/delete?inBody=fileId"
argument_list|)
expr_stmt|;
comment|// test route for get
name|from
argument_list|(
literal|"direct://GET"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/get?inBody=fileId"
argument_list|)
expr_stmt|;
comment|// test route for insert
name|from
argument_list|(
literal|"direct://INSERT"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/insert?inBody=content"
argument_list|)
expr_stmt|;
comment|// test route for insert
name|from
argument_list|(
literal|"direct://INSERT_1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/insert"
argument_list|)
expr_stmt|;
comment|// test route for list
name|from
argument_list|(
literal|"direct://LIST"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/list"
argument_list|)
expr_stmt|;
comment|// test route for patch
name|from
argument_list|(
literal|"direct://PATCH"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/patch"
argument_list|)
expr_stmt|;
comment|// test route for touch
name|from
argument_list|(
literal|"direct://TOUCH"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/touch?inBody=fileId"
argument_list|)
expr_stmt|;
comment|// test route for trash
name|from
argument_list|(
literal|"direct://TRASH"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/trash?inBody=fileId"
argument_list|)
expr_stmt|;
comment|// test route for untrash
name|from
argument_list|(
literal|"direct://UNTRASH"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/untrash?inBody=fileId"
argument_list|)
expr_stmt|;
comment|// test route for update
name|from
argument_list|(
literal|"direct://UPDATE"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/update"
argument_list|)
expr_stmt|;
comment|// test route for update
name|from
argument_list|(
literal|"direct://UPDATE_1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/update"
argument_list|)
expr_stmt|;
comment|// test route for watch
name|from
argument_list|(
literal|"direct://WATCH"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-drive://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/watch"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

