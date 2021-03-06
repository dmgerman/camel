begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfixj
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quickfixj
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Acceptor
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Application
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|ConfigError
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|DefaultSessionFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|FieldConvertError
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|MessageFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|MessageStore
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|MessageStoreFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|SessionFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|SessionID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|SessionSettings
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|EmailThreadID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|EmailType
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|Text
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|fix42
operator|.
name|Email
import|;
end_import

begin_class
DECL|class|TestSupport
specifier|public
specifier|final
class|class
name|TestSupport
block|{
DECL|method|TestSupport ()
specifier|private
name|TestSupport
parameter_list|()
block|{
comment|// Utility class
block|}
DECL|method|writeSettings (SessionSettings settings, File settingsFile)
specifier|public
specifier|static
name|void
name|writeSettings
parameter_list|(
name|SessionSettings
name|settings
parameter_list|,
name|File
name|settingsFile
parameter_list|)
throws|throws
name|IOException
block|{
name|FileOutputStream
name|settingsOut
init|=
operator|new
name|FileOutputStream
argument_list|(
name|settingsFile
argument_list|)
decl_stmt|;
try|try
block|{
name|settings
operator|.
name|toStream
argument_list|(
name|settingsOut
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|settingsOut
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|setSessionID (SessionSettings sessionSettings, SessionID sessionID)
specifier|public
specifier|static
name|void
name|setSessionID
parameter_list|(
name|SessionSettings
name|sessionSettings
parameter_list|,
name|SessionID
name|sessionID
parameter_list|)
block|{
name|sessionSettings
operator|.
name|setString
argument_list|(
name|sessionID
argument_list|,
name|SessionSettings
operator|.
name|BEGINSTRING
argument_list|,
name|sessionID
operator|.
name|getBeginString
argument_list|()
argument_list|)
expr_stmt|;
name|sessionSettings
operator|.
name|setString
argument_list|(
name|sessionID
argument_list|,
name|SessionSettings
operator|.
name|SENDERCOMPID
argument_list|,
name|sessionID
operator|.
name|getSenderCompID
argument_list|()
argument_list|)
expr_stmt|;
name|sessionSettings
operator|.
name|setString
argument_list|(
name|sessionID
argument_list|,
name|SessionSettings
operator|.
name|TARGETCOMPID
argument_list|,
name|sessionID
operator|.
name|getTargetCompID
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createEmailMessage (String subject)
specifier|public
specifier|static
name|Email
name|createEmailMessage
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|Email
name|email
init|=
operator|new
name|Email
argument_list|(
operator|new
name|EmailThreadID
argument_list|(
literal|"ID"
argument_list|)
argument_list|,
operator|new
name|EmailType
argument_list|(
name|EmailType
operator|.
name|NEW
argument_list|)
argument_list|,
operator|new
name|Subject
argument_list|(
name|subject
argument_list|)
argument_list|)
decl_stmt|;
name|Email
operator|.
name|LinesOfText
name|text
init|=
operator|new
name|Email
operator|.
name|LinesOfText
argument_list|()
decl_stmt|;
name|text
operator|.
name|set
argument_list|(
operator|new
name|Text
argument_list|(
literal|"Content"
argument_list|)
argument_list|)
expr_stmt|;
name|email
operator|.
name|addGroup
argument_list|(
name|text
argument_list|)
expr_stmt|;
return|return
name|email
return|;
block|}
DECL|method|createSession (SessionID sessionID)
specifier|public
specifier|static
name|Session
name|createSession
parameter_list|(
name|SessionID
name|sessionID
parameter_list|)
throws|throws
name|ConfigError
throws|,
name|IOException
block|{
name|MessageStoreFactory
name|mockMessageStoreFactory
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|MessageStoreFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|MessageStore
name|mockMessageStore
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|MessageStore
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockMessageStore
operator|.
name|getCreationTime
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockMessageStoreFactory
operator|.
name|create
argument_list|(
name|sessionID
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockMessageStore
argument_list|)
expr_stmt|;
name|DefaultSessionFactory
name|factory
init|=
operator|new
name|DefaultSessionFactory
argument_list|(
name|Mockito
operator|.
name|mock
argument_list|(
name|Application
operator|.
name|class
argument_list|)
argument_list|,
name|mockMessageStoreFactory
argument_list|,
name|Mockito
operator|.
name|mock
argument_list|(
name|LogFactory
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|SessionSettings
name|settings
init|=
operator|new
name|SessionSettings
argument_list|()
decl_stmt|;
name|settings
operator|.
name|setLong
argument_list|(
name|Session
operator|.
name|SETTING_HEARTBTINT
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setString
argument_list|(
name|Session
operator|.
name|SETTING_START_TIME
argument_list|,
literal|"00:00:00"
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setString
argument_list|(
name|Session
operator|.
name|SETTING_END_TIME
argument_list|,
literal|"00:00:00"
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setString
argument_list|(
name|SessionFactory
operator|.
name|SETTING_CONNECTION_TYPE
argument_list|,
name|SessionFactory
operator|.
name|ACCEPTOR_CONNECTION_TYPE
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setBool
argument_list|(
name|Session
operator|.
name|SETTING_USE_DATA_DICTIONARY
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|(
name|sessionID
argument_list|,
name|settings
argument_list|)
return|;
block|}
DECL|method|createEngine ()
specifier|public
specifier|static
name|QuickfixjEngine
name|createEngine
parameter_list|()
throws|throws
name|ConfigError
throws|,
name|FieldConvertError
throws|,
name|IOException
throws|,
name|JMException
block|{
return|return
name|createEngine
argument_list|(
literal|false
argument_list|)
return|;
block|}
DECL|method|createEngine (boolean lazy)
specifier|public
specifier|static
name|QuickfixjEngine
name|createEngine
parameter_list|(
name|boolean
name|lazy
parameter_list|)
throws|throws
name|ConfigError
throws|,
name|FieldConvertError
throws|,
name|IOException
throws|,
name|JMException
block|{
name|SessionID
name|sessionID
init|=
operator|new
name|SessionID
argument_list|(
literal|"FIX.4.4:SENDER->TARGET"
argument_list|)
decl_stmt|;
name|MessageStoreFactory
name|mockMessageStoreFactory
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|MessageStoreFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|MessageStore
name|mockMessageStore
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|MessageStore
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockMessageStore
operator|.
name|getCreationTime
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockMessageStoreFactory
operator|.
name|create
argument_list|(
name|sessionID
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockMessageStore
argument_list|)
expr_stmt|;
name|SessionSettings
name|settings
init|=
operator|new
name|SessionSettings
argument_list|()
decl_stmt|;
name|settings
operator|.
name|setLong
argument_list|(
name|sessionID
argument_list|,
name|Session
operator|.
name|SETTING_HEARTBTINT
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setString
argument_list|(
name|sessionID
argument_list|,
name|Session
operator|.
name|SETTING_START_TIME
argument_list|,
literal|"00:00:00"
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setString
argument_list|(
name|sessionID
argument_list|,
name|Session
operator|.
name|SETTING_END_TIME
argument_list|,
literal|"00:00:00"
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setString
argument_list|(
name|sessionID
argument_list|,
name|SessionFactory
operator|.
name|SETTING_CONNECTION_TYPE
argument_list|,
name|SessionFactory
operator|.
name|ACCEPTOR_CONNECTION_TYPE
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setLong
argument_list|(
name|sessionID
argument_list|,
name|Acceptor
operator|.
name|SETTING_SOCKET_ACCEPT_PORT
argument_list|,
literal|8000
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setBool
argument_list|(
name|sessionID
argument_list|,
name|Session
operator|.
name|SETTING_USE_DATA_DICTIONARY
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
operator|new
name|QuickfixjEngine
argument_list|(
literal|""
argument_list|,
name|settings
argument_list|,
name|mockMessageStoreFactory
argument_list|,
name|Mockito
operator|.
name|mock
argument_list|(
name|LogFactory
operator|.
name|class
argument_list|)
argument_list|,
name|Mockito
operator|.
name|mock
argument_list|(
name|MessageFactory
operator|.
name|class
argument_list|)
argument_list|,
name|lazy
argument_list|)
return|;
block|}
block|}
end_class

end_unit

