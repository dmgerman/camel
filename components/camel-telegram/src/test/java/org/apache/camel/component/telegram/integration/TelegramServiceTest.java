begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
operator|.
name|integration
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
operator|.
name|TelegramParseMode
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
name|telegram
operator|.
name|TelegramService
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
name|telegram
operator|.
name|TelegramServiceProvider
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
name|telegram
operator|.
name|model
operator|.
name|OutgoingAudioMessage
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
name|telegram
operator|.
name|model
operator|.
name|OutgoingDocumentMessage
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
name|telegram
operator|.
name|model
operator|.
name|OutgoingPhotoMessage
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
name|telegram
operator|.
name|model
operator|.
name|OutgoingTextMessage
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
name|telegram
operator|.
name|model
operator|.
name|OutgoingVideoMessage
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
name|telegram
operator|.
name|model
operator|.
name|UpdateResult
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
name|telegram
operator|.
name|util
operator|.
name|TelegramTestUtil
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
name|Test
import|;
end_import

begin_comment
comment|/**  * Tests if the BotAPI are working correctly.  */
end_comment

begin_class
DECL|class|TelegramServiceTest
specifier|public
class|class
name|TelegramServiceTest
block|{
DECL|field|authorizationToken
specifier|private
specifier|static
name|String
name|authorizationToken
decl_stmt|;
DECL|field|chatId
specifier|private
specifier|static
name|String
name|chatId
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|init ()
specifier|public
specifier|static
name|void
name|init
parameter_list|()
block|{
name|authorizationToken
operator|=
name|System
operator|.
name|getenv
argument_list|(
literal|"TELEGRAM_AUTHORIZATION_TOKEN"
argument_list|)
expr_stmt|;
name|chatId
operator|=
name|System
operator|.
name|getenv
argument_list|(
literal|"TELEGRAM_CHAT_ID"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetUpdates ()
specifier|public
name|void
name|testGetUpdates
parameter_list|()
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|UpdateResult
name|res
init|=
name|service
operator|.
name|getUpdates
argument_list|(
name|authorizationToken
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|res
operator|.
name|isOk
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMessage ()
specifier|public
name|void
name|testSendMessage
parameter_list|()
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|OutgoingTextMessage
name|msg
init|=
operator|new
name|OutgoingTextMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setChatId
argument_list|(
name|chatId
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setText
argument_list|(
literal|"This is an auto-generated message from the Bot"
argument_list|)
expr_stmt|;
name|service
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMessageHtml ()
specifier|public
name|void
name|testSendMessageHtml
parameter_list|()
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|OutgoingTextMessage
name|msg
init|=
operator|new
name|OutgoingTextMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setChatId
argument_list|(
name|chatId
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setText
argument_list|(
literal|"This is a<b>HTML</b><i>auto-generated</i> message from the Bot"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setParseMode
argument_list|(
name|TelegramParseMode
operator|.
name|HTML
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMessageMarkdown ()
specifier|public
name|void
name|testSendMessageMarkdown
parameter_list|()
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|OutgoingTextMessage
name|msg
init|=
operator|new
name|OutgoingTextMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setChatId
argument_list|(
name|chatId
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setText
argument_list|(
literal|"This is a *Markdown* _auto-generated_ message from the Bot"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setParseMode
argument_list|(
name|TelegramParseMode
operator|.
name|MARKDOWN
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendFull ()
specifier|public
name|void
name|testSendFull
parameter_list|()
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|OutgoingTextMessage
name|msg
init|=
operator|new
name|OutgoingTextMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setChatId
argument_list|(
name|chatId
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setText
argument_list|(
literal|"This is an *auto-generated* message from the Bot"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setDisableWebPagePreview
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setParseMode
argument_list|(
literal|"Markdown"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setDisableNotification
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|service
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendPhoto ()
specifier|public
name|void
name|testSendPhoto
parameter_list|()
throws|throws
name|IOException
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|byte
index|[]
name|image
init|=
name|TelegramTestUtil
operator|.
name|createSampleImage
argument_list|(
literal|"PNG"
argument_list|)
decl_stmt|;
name|OutgoingPhotoMessage
name|msg
init|=
operator|new
name|OutgoingPhotoMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setPhoto
argument_list|(
name|image
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setChatId
argument_list|(
name|chatId
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setFilenameWithExtension
argument_list|(
literal|"file.png"
argument_list|)
expr_stmt|;
name|service
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendPhotoFull ()
specifier|public
name|void
name|testSendPhotoFull
parameter_list|()
throws|throws
name|IOException
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|byte
index|[]
name|image
init|=
name|TelegramTestUtil
operator|.
name|createSampleImage
argument_list|(
literal|"PNG"
argument_list|)
decl_stmt|;
name|OutgoingPhotoMessage
name|msg
init|=
operator|new
name|OutgoingPhotoMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setPhoto
argument_list|(
name|image
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setChatId
argument_list|(
name|chatId
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setFilenameWithExtension
argument_list|(
literal|"file.png"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setCaption
argument_list|(
literal|"Photo"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setDisableNotification
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|service
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendAudio ()
specifier|public
name|void
name|testSendAudio
parameter_list|()
throws|throws
name|IOException
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|byte
index|[]
name|audio
init|=
name|TelegramTestUtil
operator|.
name|createSampleAudio
argument_list|()
decl_stmt|;
name|OutgoingAudioMessage
name|msg
init|=
operator|new
name|OutgoingAudioMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setAudio
argument_list|(
name|audio
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setChatId
argument_list|(
name|chatId
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setFilenameWithExtension
argument_list|(
literal|"audio.mp3"
argument_list|)
expr_stmt|;
name|service
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendAudioFull ()
specifier|public
name|void
name|testSendAudioFull
parameter_list|()
throws|throws
name|IOException
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|byte
index|[]
name|audio
init|=
name|TelegramTestUtil
operator|.
name|createSampleAudio
argument_list|()
decl_stmt|;
name|OutgoingAudioMessage
name|msg
init|=
operator|new
name|OutgoingAudioMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setAudio
argument_list|(
name|audio
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setChatId
argument_list|(
name|chatId
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setFilenameWithExtension
argument_list|(
literal|"audio.mp3"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setTitle
argument_list|(
literal|"Audio"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setDurationSeconds
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setPerformer
argument_list|(
literal|"Myself"
argument_list|)
expr_stmt|;
name|service
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendVideo ()
specifier|public
name|void
name|testSendVideo
parameter_list|()
throws|throws
name|IOException
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|byte
index|[]
name|video
init|=
name|TelegramTestUtil
operator|.
name|createSampleVideo
argument_list|()
decl_stmt|;
name|OutgoingVideoMessage
name|msg
init|=
operator|new
name|OutgoingVideoMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setVideo
argument_list|(
name|video
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setChatId
argument_list|(
name|chatId
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setFilenameWithExtension
argument_list|(
literal|"video.mp4"
argument_list|)
expr_stmt|;
name|service
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendVideoFull ()
specifier|public
name|void
name|testSendVideoFull
parameter_list|()
throws|throws
name|IOException
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|byte
index|[]
name|video
init|=
name|TelegramTestUtil
operator|.
name|createSampleVideo
argument_list|()
decl_stmt|;
name|OutgoingVideoMessage
name|msg
init|=
operator|new
name|OutgoingVideoMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setVideo
argument_list|(
name|video
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setChatId
argument_list|(
name|chatId
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setFilenameWithExtension
argument_list|(
literal|"video.mp4"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setDurationSeconds
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setCaption
argument_list|(
literal|"A Video"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setWidth
argument_list|(
literal|90
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeight
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|service
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendDocument ()
specifier|public
name|void
name|testSendDocument
parameter_list|()
throws|throws
name|IOException
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|byte
index|[]
name|document
init|=
name|TelegramTestUtil
operator|.
name|createSampleDocument
argument_list|()
decl_stmt|;
name|OutgoingDocumentMessage
name|msg
init|=
operator|new
name|OutgoingDocumentMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setChatId
argument_list|(
name|chatId
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setFilenameWithExtension
argument_list|(
literal|"file.txt"
argument_list|)
expr_stmt|;
name|service
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendDocumentFull ()
specifier|public
name|void
name|testSendDocumentFull
parameter_list|()
throws|throws
name|IOException
block|{
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|byte
index|[]
name|document
init|=
name|TelegramTestUtil
operator|.
name|createSampleDocument
argument_list|()
decl_stmt|;
name|OutgoingDocumentMessage
name|msg
init|=
operator|new
name|OutgoingDocumentMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setDocument
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setChatId
argument_list|(
name|chatId
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setFilenameWithExtension
argument_list|(
literal|"file.png"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setCaption
argument_list|(
literal|"A document"
argument_list|)
expr_stmt|;
name|service
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

