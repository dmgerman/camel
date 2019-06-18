begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.soroushbot.service
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|soroushbot
operator|.
name|service
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
name|IOException
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|ClientBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|WebTarget
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|soroushbot
operator|.
name|IOUtils
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
name|soroushbot
operator|.
name|models
operator|.
name|MinorType
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
name|soroushbot
operator|.
name|models
operator|.
name|SoroushAction
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
name|soroushbot
operator|.
name|models
operator|.
name|SoroushMessage
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
name|soroushbot
operator|.
name|models
operator|.
name|response
operator|.
name|UploadFileResponse
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
name|soroushbot
operator|.
name|utils
operator|.
name|SoroushException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|glassfish
operator|.
name|jersey
operator|.
name|client
operator|.
name|ClientProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|glassfish
operator|.
name|jersey
operator|.
name|media
operator|.
name|multipart
operator|.
name|MultiPart
import|;
end_import

begin_import
import|import
name|org
operator|.
name|glassfish
operator|.
name|jersey
operator|.
name|media
operator|.
name|multipart
operator|.
name|file
operator|.
name|StreamDataBodyPart
import|;
end_import

begin_import
import|import
name|org
operator|.
name|glassfish
operator|.
name|jersey
operator|.
name|media
operator|.
name|sse
operator|.
name|EventInput
import|;
end_import

begin_import
import|import
name|org
operator|.
name|glassfish
operator|.
name|jersey
operator|.
name|media
operator|.
name|sse
operator|.
name|InboundEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|glassfish
operator|.
name|jersey
operator|.
name|media
operator|.
name|sse
operator|.
name|SseFeature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_comment
comment|/**  * check if soroush BOT Api work as expected  */
end_comment

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Need the token to work"
argument_list|)
DECL|class|SoroushServiceTest
specifier|public
class|class
name|SoroushServiceTest
block|{
DECL|field|authorizationToken
specifier|static
name|String
name|authorizationToken
decl_stmt|;
DECL|field|receiverId
specifier|static
name|String
name|receiverId
decl_stmt|;
DECL|field|soroushService
specifier|private
specifier|static
name|SoroushService
name|soroushService
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setUp ()
specifier|public
specifier|static
name|void
name|setUp
parameter_list|()
block|{
name|authorizationToken
operator|=
name|System
operator|.
name|getenv
argument_list|(
literal|"soroushBotAuthorizationToken"
argument_list|)
expr_stmt|;
name|receiverId
operator|=
name|System
operator|.
name|getenv
argument_list|(
literal|"soroushBotReceiverId"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"you need to define `soroushBotAuthorizationToken` and "
operator|+
literal|"`soroushBotReceiverId` environment variable in order to do integration test "
argument_list|,
name|authorizationToken
operator|!=
literal|null
operator|&&
name|receiverId
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|soroushService
operator|=
name|SoroushService
operator|.
name|get
argument_list|()
expr_stmt|;
name|soroushService
operator|.
name|setAlternativeUrl
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * try to connect to soroush. if any problem occurs  an exception will throw and this test will fail.      */
annotation|@
name|Test
DECL|method|connectToGetMessageEndPoint ()
specifier|public
name|void
name|connectToGetMessageEndPoint
parameter_list|()
block|{
name|Client
name|client
init|=
name|ClientBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|register
argument_list|(
name|SseFeature
operator|.
name|class
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|client
operator|.
name|property
argument_list|(
name|ClientProperties
operator|.
name|CONNECT_TIMEOUT
argument_list|,
literal|2000
argument_list|)
expr_stmt|;
name|WebTarget
name|target
init|=
name|client
operator|.
name|target
argument_list|(
name|soroushService
operator|.
name|generateUrl
argument_list|(
name|authorizationToken
argument_list|,
name|SoroushAction
operator|.
name|getMessage
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|EventInput
name|eventInput
init|=
name|target
operator|.
name|request
argument_list|()
operator|.
name|get
argument_list|(
name|EventInput
operator|.
name|class
argument_list|)
decl_stmt|;
name|eventInput
operator|.
name|setChunkType
argument_list|(
name|MediaType
operator|.
name|SERVER_SENT_EVENTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventInput
operator|.
name|isClosed
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|canNotReadMessageDueToWrongAuthorizationToken ()
specifier|public
name|void
name|canNotReadMessageDueToWrongAuthorizationToken
parameter_list|()
block|{
name|Client
name|client
init|=
name|ClientBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|register
argument_list|(
name|SseFeature
operator|.
name|class
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|client
operator|.
name|property
argument_list|(
name|ClientProperties
operator|.
name|CONNECT_TIMEOUT
argument_list|,
literal|2000
argument_list|)
expr_stmt|;
name|WebTarget
name|target
init|=
name|client
operator|.
name|target
argument_list|(
name|soroushService
operator|.
name|generateUrl
argument_list|(
literal|"bad_string"
operator|+
name|authorizationToken
argument_list|,
name|SoroushAction
operator|.
name|getMessage
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|EventInput
name|eventInput
init|=
name|target
operator|.
name|request
argument_list|()
operator|.
name|get
argument_list|(
name|EventInput
operator|.
name|class
argument_list|)
decl_stmt|;
name|eventInput
operator|.
name|setChunkType
argument_list|(
name|MediaType
operator|.
name|SERVER_SENT_EVENTS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|eventInput
operator|.
name|isClosed
argument_list|()
argument_list|)
expr_stmt|;
name|InboundEvent
name|read
init|=
name|eventInput
operator|.
name|read
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|read
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendMessageToAPerson ()
specifier|public
name|void
name|sendMessageToAPerson
parameter_list|()
throws|throws
name|IOException
throws|,
name|SoroushException
block|{
name|WebTarget
name|target
init|=
name|soroushService
operator|.
name|createSendMessageTarget
argument_list|(
name|authorizationToken
argument_list|,
literal|2000
argument_list|)
decl_stmt|;
name|SoroushMessage
name|message
init|=
operator|new
name|SoroushMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
literal|"content"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setTo
argument_list|(
name|receiverId
argument_list|)
expr_stmt|;
name|message
operator|.
name|setType
argument_list|(
name|MinorType
operator|.
name|TEXT
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|target
operator|.
name|request
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|post
argument_list|(
name|Entity
operator|.
name|json
argument_list|(
name|message
argument_list|)
argument_list|)
decl_stmt|;
name|soroushService
operator|.
name|assertSuccessful
argument_list|(
name|response
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|uploadAndDownloadFile ()
specifier|public
name|void
name|uploadAndDownloadFile
parameter_list|()
throws|throws
name|IOException
throws|,
name|SoroushException
block|{
name|WebTarget
name|target
init|=
name|soroushService
operator|.
name|createUploadFileTarget
argument_list|(
name|authorizationToken
argument_list|,
literal|2000
argument_list|)
decl_stmt|;
name|MultiPart
name|multipart
init|=
operator|new
name|MultiPart
argument_list|()
decl_stmt|;
name|multipart
operator|.
name|setMediaType
argument_list|(
name|MediaType
operator|.
name|MULTIPART_FORM_DATA_TYPE
argument_list|)
expr_stmt|;
name|String
name|fileData
init|=
literal|"data"
decl_stmt|;
name|multipart
operator|.
name|bodyPart
argument_list|(
operator|new
name|StreamDataBodyPart
argument_list|(
literal|"file"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|fileData
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|,
name|MediaType
operator|.
name|APPLICATION_OCTET_STREAM_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|target
operator|.
name|request
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|post
argument_list|(
name|Entity
operator|.
name|entity
argument_list|(
name|multipart
argument_list|,
name|multipart
operator|.
name|getMediaType
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|UploadFileResponse
name|uploadFileResponse
init|=
name|soroushService
operator|.
name|assertSuccessful
argument_list|(
name|response
argument_list|,
name|UploadFileResponse
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|uploadFileResponse
operator|.
name|getFileUrl
argument_list|()
argument_list|)
expr_stmt|;
name|WebTarget
name|downloadFileTarget
init|=
name|soroushService
operator|.
name|createDownloadFileTarget
argument_list|(
name|authorizationToken
argument_list|,
name|uploadFileResponse
operator|.
name|getFileUrl
argument_list|()
argument_list|,
literal|2000
argument_list|)
decl_stmt|;
name|Response
name|downloadResponse
init|=
name|downloadFileTarget
operator|.
name|request
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|remoteData
init|=
operator|new
name|String
argument_list|(
name|IOUtils
operator|.
name|readFully
argument_list|(
name|downloadResponse
operator|.
name|readEntity
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|,
operator|-
literal|1
argument_list|,
literal|false
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"file contents are identical"
argument_list|,
name|fileData
argument_list|,
name|remoteData
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

