begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|List
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quickfixj
operator|.
name|jmx
operator|.
name|JmxExporter
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
name|DefaultMessageFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|DoNotSend
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
name|FieldNotFound
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|FileLogFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|FileStoreFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|IncorrectDataFormat
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|IncorrectTagValue
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Initiator
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|JdbcLogFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|JdbcSetting
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|JdbcStoreFactory
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
name|MemoryStoreFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Message
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
name|MessageStoreFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|RejectLogon
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|ScreenLogFactory
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
name|SleepycatStoreFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|SocketAcceptor
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|SocketInitiator
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|ThreadedSocketAcceptor
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|ThreadedSocketInitiator
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|UnsupportedMessageType
import|;
end_import

begin_comment
comment|/**  * This is a wrapper class that provided QuickFIX/J initialization capabilities  * beyond those supported in the core QuickFIX/J distribution.  *   * Specifically, it infers dependencies on specific implementations of message  * stores and logs. It also supports extended QuickFIX/J settings properties to  * specify threading models, custom store and log implementations, etc.  *   * The wrapper will create an initiator or acceptor or both depending on the  * roles of sessions described in the settings file.  */
end_comment

begin_class
DECL|class|QuickfixjEngine
specifier|public
class|class
name|QuickfixjEngine
block|{
DECL|field|DEFAULT_START_TIME
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_START_TIME
init|=
literal|"00:00:00"
decl_stmt|;
DECL|field|DEFAULT_END_TIME
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_END_TIME
init|=
literal|"00:00:00"
decl_stmt|;
DECL|field|DEFAULT_HEARTBTINT
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_HEARTBTINT
init|=
literal|30
decl_stmt|;
DECL|field|SETTING_THREAD_MODEL
specifier|public
specifier|static
specifier|final
name|String
name|SETTING_THREAD_MODEL
init|=
literal|"ThreadModel"
decl_stmt|;
DECL|field|SETTING_USE_JMX
specifier|public
specifier|static
specifier|final
name|String
name|SETTING_USE_JMX
init|=
literal|"UseJmx"
decl_stmt|;
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
name|QuickfixjEngine
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|acceptor
specifier|private
specifier|final
name|Acceptor
name|acceptor
decl_stmt|;
DECL|field|initiator
specifier|private
specifier|final
name|Initiator
name|initiator
decl_stmt|;
DECL|field|forcedShutdown
specifier|private
specifier|final
name|boolean
name|forcedShutdown
decl_stmt|;
DECL|field|messageStoreFactory
specifier|private
specifier|final
name|MessageStoreFactory
name|messageStoreFactory
decl_stmt|;
DECL|field|sessionLogFactory
specifier|private
specifier|final
name|LogFactory
name|sessionLogFactory
decl_stmt|;
DECL|field|messageFactory
specifier|private
specifier|final
name|MessageFactory
name|messageFactory
decl_stmt|;
DECL|field|started
specifier|private
name|boolean
name|started
decl_stmt|;
DECL|field|settingsResourceName
specifier|private
name|String
name|settingsResourceName
decl_stmt|;
DECL|field|eventListeners
specifier|private
name|List
argument_list|<
name|QuickfixjEventListener
argument_list|>
name|eventListeners
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|QuickfixjEventListener
argument_list|>
argument_list|()
decl_stmt|;
DECL|enum|ThreadModel
specifier|public
enum|enum
name|ThreadModel
block|{
DECL|enumConstant|ThreadPerConnector
DECL|enumConstant|ThreadPerSession
name|ThreadPerConnector
block|,
name|ThreadPerSession
block|;     }
DECL|method|QuickfixjEngine (String settingsResourceName, boolean forcedShutdown)
specifier|public
name|QuickfixjEngine
parameter_list|(
name|String
name|settingsResourceName
parameter_list|,
name|boolean
name|forcedShutdown
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
name|this
operator|.
name|forcedShutdown
operator|=
name|forcedShutdown
expr_stmt|;
name|this
operator|.
name|settingsResourceName
operator|=
name|settingsResourceName
expr_stmt|;
name|InputStream
name|inputStream
init|=
name|ObjectHelper
operator|.
name|loadResourceAsStream
argument_list|(
name|settingsResourceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|inputStream
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not load "
operator|+
name|settingsResourceName
argument_list|)
throw|;
block|}
name|SessionSettings
name|settings
init|=
operator|new
name|SessionSettings
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
comment|// TODO Make the message factory configurable for advanced users
name|messageFactory
operator|=
operator|new
name|DefaultMessageFactory
argument_list|()
expr_stmt|;
name|sessionLogFactory
operator|=
name|inferLogFactory
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|messageStoreFactory
operator|=
name|inferMessageStoreFactory
argument_list|(
name|settings
argument_list|)
expr_stmt|;
comment|// Set default session schedule if not specified in configuration
if|if
condition|(
operator|!
name|settings
operator|.
name|isSetting
argument_list|(
name|Session
operator|.
name|SETTING_START_TIME
argument_list|)
condition|)
block|{
name|settings
operator|.
name|setString
argument_list|(
name|Session
operator|.
name|SETTING_START_TIME
argument_list|,
name|DEFAULT_START_TIME
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|settings
operator|.
name|isSetting
argument_list|(
name|Session
operator|.
name|SETTING_END_TIME
argument_list|)
condition|)
block|{
name|settings
operator|.
name|setString
argument_list|(
name|Session
operator|.
name|SETTING_END_TIME
argument_list|,
name|DEFAULT_END_TIME
argument_list|)
expr_stmt|;
block|}
comment|// Default heartbeat interval
if|if
condition|(
operator|!
name|settings
operator|.
name|isSetting
argument_list|(
name|Session
operator|.
name|SETTING_HEARTBTINT
argument_list|)
condition|)
block|{
name|settings
operator|.
name|setLong
argument_list|(
name|Session
operator|.
name|SETTING_HEARTBTINT
argument_list|,
name|DEFAULT_HEARTBTINT
argument_list|)
expr_stmt|;
block|}
comment|// Allow specification of the QFJ threading model
name|ThreadModel
name|threadModel
init|=
name|ThreadModel
operator|.
name|ThreadPerConnector
decl_stmt|;
if|if
condition|(
name|settings
operator|.
name|isSetting
argument_list|(
name|SETTING_THREAD_MODEL
argument_list|)
condition|)
block|{
name|threadModel
operator|=
name|ThreadModel
operator|.
name|valueOf
argument_list|(
name|settings
operator|.
name|getString
argument_list|(
name|SETTING_THREAD_MODEL
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|JmxExporter
name|jmxExporter
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|settings
operator|.
name|isSetting
argument_list|(
name|SETTING_USE_JMX
argument_list|)
operator|&&
name|settings
operator|.
name|getBool
argument_list|(
name|SETTING_USE_JMX
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Enabling JMX for QuickFIX/J"
argument_list|)
expr_stmt|;
name|jmxExporter
operator|=
operator|new
name|JmxExporter
argument_list|()
expr_stmt|;
block|}
comment|// From original component implementation...
comment|// To avoid this exception in OSGi platform
comment|// java.lang.NoClassDefFoundError: quickfix/fix41/MessageFactory
name|ClassLoader
name|ccl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isConnectorRole
argument_list|(
name|settings
argument_list|,
name|SessionFactory
operator|.
name|ACCEPTOR_CONNECTION_TYPE
argument_list|)
condition|)
block|{
name|acceptor
operator|=
name|createAcceptor
argument_list|(
operator|new
name|Dispatcher
argument_list|()
argument_list|,
name|settings
argument_list|,
name|messageStoreFactory
argument_list|,
name|sessionLogFactory
argument_list|,
name|messageFactory
argument_list|,
name|threadModel
argument_list|)
expr_stmt|;
if|if
condition|(
name|jmxExporter
operator|!=
literal|null
condition|)
block|{
name|jmxExporter
operator|.
name|export
argument_list|(
name|acceptor
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|acceptor
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|isConnectorRole
argument_list|(
name|settings
argument_list|,
name|SessionFactory
operator|.
name|INITIATOR_CONNECTION_TYPE
argument_list|)
condition|)
block|{
name|initiator
operator|=
name|createInitiator
argument_list|(
operator|new
name|Dispatcher
argument_list|()
argument_list|,
name|settings
argument_list|,
name|messageStoreFactory
argument_list|,
name|sessionLogFactory
argument_list|,
name|messageFactory
argument_list|,
name|threadModel
argument_list|)
expr_stmt|;
if|if
condition|(
name|jmxExporter
operator|!=
literal|null
condition|)
block|{
name|jmxExporter
operator|.
name|export
argument_list|(
name|initiator
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|initiator
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|acceptor
operator|==
literal|null
operator|&&
name|initiator
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ConfigError
argument_list|(
literal|"No connector role"
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|ccl
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|acceptor
operator|!=
literal|null
condition|)
block|{
name|acceptor
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|initiator
operator|!=
literal|null
condition|)
block|{
name|initiator
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|started
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|stop
argument_list|(
name|forcedShutdown
argument_list|)
expr_stmt|;
block|}
DECL|method|stop (boolean force)
specifier|public
name|void
name|stop
parameter_list|(
name|boolean
name|force
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|acceptor
operator|!=
literal|null
condition|)
block|{
name|acceptor
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|initiator
operator|!=
literal|null
condition|)
block|{
name|initiator
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|started
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|isStarted ()
specifier|public
name|boolean
name|isStarted
parameter_list|()
block|{
return|return
name|started
return|;
block|}
DECL|method|createInitiator (Application application, SessionSettings settings, MessageStoreFactory messageStoreFactory, LogFactory sessionLogFactory, MessageFactory messageFactory, ThreadModel threadModel)
specifier|private
name|Initiator
name|createInitiator
parameter_list|(
name|Application
name|application
parameter_list|,
name|SessionSettings
name|settings
parameter_list|,
name|MessageStoreFactory
name|messageStoreFactory
parameter_list|,
name|LogFactory
name|sessionLogFactory
parameter_list|,
name|MessageFactory
name|messageFactory
parameter_list|,
name|ThreadModel
name|threadModel
parameter_list|)
throws|throws
name|ConfigError
block|{
name|Initiator
name|initiator
decl_stmt|;
if|if
condition|(
name|threadModel
operator|==
name|ThreadModel
operator|.
name|ThreadPerSession
condition|)
block|{
name|initiator
operator|=
operator|new
name|ThreadedSocketInitiator
argument_list|(
name|application
argument_list|,
name|messageStoreFactory
argument_list|,
name|settings
argument_list|,
name|sessionLogFactory
argument_list|,
name|messageFactory
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|threadModel
operator|==
name|ThreadModel
operator|.
name|ThreadPerConnector
condition|)
block|{
name|initiator
operator|=
operator|new
name|SocketInitiator
argument_list|(
name|application
argument_list|,
name|messageStoreFactory
argument_list|,
name|settings
argument_list|,
name|sessionLogFactory
argument_list|,
name|messageFactory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ConfigError
argument_list|(
literal|"Unknown thread mode: "
operator|+
name|threadModel
argument_list|)
throw|;
block|}
return|return
name|initiator
return|;
block|}
DECL|method|createAcceptor (Application application, SessionSettings settings, MessageStoreFactory messageStoreFactory, LogFactory sessionLogFactory, MessageFactory messageFactory, ThreadModel threadModel)
specifier|private
name|Acceptor
name|createAcceptor
parameter_list|(
name|Application
name|application
parameter_list|,
name|SessionSettings
name|settings
parameter_list|,
name|MessageStoreFactory
name|messageStoreFactory
parameter_list|,
name|LogFactory
name|sessionLogFactory
parameter_list|,
name|MessageFactory
name|messageFactory
parameter_list|,
name|ThreadModel
name|threadModel
parameter_list|)
throws|throws
name|ConfigError
block|{
name|Acceptor
name|acceptor
decl_stmt|;
if|if
condition|(
name|threadModel
operator|==
name|ThreadModel
operator|.
name|ThreadPerSession
condition|)
block|{
name|acceptor
operator|=
operator|new
name|ThreadedSocketAcceptor
argument_list|(
name|application
argument_list|,
name|messageStoreFactory
argument_list|,
name|settings
argument_list|,
name|sessionLogFactory
argument_list|,
name|messageFactory
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|threadModel
operator|==
name|ThreadModel
operator|.
name|ThreadPerConnector
condition|)
block|{
name|acceptor
operator|=
operator|new
name|SocketAcceptor
argument_list|(
name|application
argument_list|,
name|messageStoreFactory
argument_list|,
name|settings
argument_list|,
name|sessionLogFactory
argument_list|,
name|messageFactory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ConfigError
argument_list|(
literal|"Unknown thread mode: "
operator|+
name|threadModel
argument_list|)
throw|;
block|}
return|return
name|acceptor
return|;
block|}
DECL|method|inferMessageStoreFactory (SessionSettings settings)
specifier|private
name|MessageStoreFactory
name|inferMessageStoreFactory
parameter_list|(
name|SessionSettings
name|settings
parameter_list|)
throws|throws
name|ConfigError
block|{
name|Set
argument_list|<
name|MessageStoreFactory
argument_list|>
name|impliedMessageStoreFactories
init|=
operator|new
name|HashSet
argument_list|<
name|MessageStoreFactory
argument_list|>
argument_list|()
decl_stmt|;
name|isJdbcStore
argument_list|(
name|settings
argument_list|,
name|impliedMessageStoreFactories
argument_list|)
expr_stmt|;
name|isFileStore
argument_list|(
name|settings
argument_list|,
name|impliedMessageStoreFactories
argument_list|)
expr_stmt|;
name|isSleepycatStore
argument_list|(
name|settings
argument_list|,
name|impliedMessageStoreFactories
argument_list|)
expr_stmt|;
if|if
condition|(
name|impliedMessageStoreFactories
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|ConfigError
argument_list|(
literal|"Ambiguous message store implied in configuration."
argument_list|)
throw|;
block|}
name|MessageStoreFactory
name|messageStoreFactory
decl_stmt|;
if|if
condition|(
name|impliedMessageStoreFactories
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|messageStoreFactory
operator|=
operator|(
name|MessageStoreFactory
operator|)
name|impliedMessageStoreFactories
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|messageStoreFactory
operator|=
operator|new
name|MemoryStoreFactory
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Inferring message store factory: "
operator|+
name|messageStoreFactory
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|messageStoreFactory
return|;
block|}
DECL|method|isSleepycatStore (SessionSettings settings, Set<MessageStoreFactory> impliedMessageStoreFactories)
specifier|private
name|void
name|isSleepycatStore
parameter_list|(
name|SessionSettings
name|settings
parameter_list|,
name|Set
argument_list|<
name|MessageStoreFactory
argument_list|>
name|impliedMessageStoreFactories
parameter_list|)
block|{
if|if
condition|(
name|settings
operator|.
name|isSetting
argument_list|(
name|SleepycatStoreFactory
operator|.
name|SETTING_SLEEPYCAT_DATABASE_DIR
argument_list|)
condition|)
block|{
name|impliedMessageStoreFactories
operator|.
name|add
argument_list|(
operator|new
name|SleepycatStoreFactory
argument_list|(
name|settings
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isFileStore (SessionSettings settings, Set<MessageStoreFactory> impliedMessageStoreFactories)
specifier|private
name|void
name|isFileStore
parameter_list|(
name|SessionSettings
name|settings
parameter_list|,
name|Set
argument_list|<
name|MessageStoreFactory
argument_list|>
name|impliedMessageStoreFactories
parameter_list|)
block|{
if|if
condition|(
name|settings
operator|.
name|isSetting
argument_list|(
name|FileStoreFactory
operator|.
name|SETTING_FILE_STORE_PATH
argument_list|)
condition|)
block|{
name|impliedMessageStoreFactories
operator|.
name|add
argument_list|(
operator|new
name|FileStoreFactory
argument_list|(
name|settings
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isJdbcStore (SessionSettings settings, Set<MessageStoreFactory> impliedMessageStoreFactories)
specifier|private
name|void
name|isJdbcStore
parameter_list|(
name|SessionSettings
name|settings
parameter_list|,
name|Set
argument_list|<
name|MessageStoreFactory
argument_list|>
name|impliedMessageStoreFactories
parameter_list|)
block|{
if|if
condition|(
name|settings
operator|.
name|isSetting
argument_list|(
name|JdbcSetting
operator|.
name|SETTING_JDBC_DRIVER
argument_list|)
condition|)
block|{
name|impliedMessageStoreFactories
operator|.
name|add
argument_list|(
operator|new
name|JdbcStoreFactory
argument_list|(
name|settings
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|inferLogFactory (SessionSettings settings)
specifier|private
name|LogFactory
name|inferLogFactory
parameter_list|(
name|SessionSettings
name|settings
parameter_list|)
throws|throws
name|ConfigError
block|{
name|Set
argument_list|<
name|LogFactory
argument_list|>
name|impliedLogFactories
init|=
operator|new
name|HashSet
argument_list|<
name|LogFactory
argument_list|>
argument_list|()
decl_stmt|;
name|isFileLog
argument_list|(
name|settings
argument_list|,
name|impliedLogFactories
argument_list|)
expr_stmt|;
name|isScreenLog
argument_list|(
name|settings
argument_list|,
name|impliedLogFactories
argument_list|)
expr_stmt|;
name|isJdbcLog
argument_list|(
name|settings
argument_list|,
name|impliedLogFactories
argument_list|)
expr_stmt|;
if|if
condition|(
name|impliedLogFactories
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|ConfigError
argument_list|(
literal|"Ambiguous log factory implied in configuration"
argument_list|)
throw|;
block|}
name|LogFactory
name|sessionLogFactory
decl_stmt|;
if|if
condition|(
name|impliedLogFactories
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|sessionLogFactory
operator|=
operator|(
name|LogFactory
operator|)
name|impliedLogFactories
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// Default
name|sessionLogFactory
operator|=
operator|new
name|ScreenLogFactory
argument_list|(
name|settings
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Inferring log factory: "
operator|+
name|sessionLogFactory
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sessionLogFactory
return|;
block|}
DECL|method|isScreenLog (SessionSettings settings, Set<LogFactory> impliedLogFactories)
specifier|private
name|void
name|isScreenLog
parameter_list|(
name|SessionSettings
name|settings
parameter_list|,
name|Set
argument_list|<
name|LogFactory
argument_list|>
name|impliedLogFactories
parameter_list|)
block|{
if|if
condition|(
name|settings
operator|.
name|isSetting
argument_list|(
name|ScreenLogFactory
operator|.
name|SETTING_LOG_EVENTS
argument_list|)
operator|||
name|settings
operator|.
name|isSetting
argument_list|(
name|ScreenLogFactory
operator|.
name|SETTING_LOG_INCOMING
argument_list|)
operator|||
name|settings
operator|.
name|isSetting
argument_list|(
name|ScreenLogFactory
operator|.
name|SETTING_LOG_OUTGOING
argument_list|)
condition|)
block|{
name|impliedLogFactories
operator|.
name|add
argument_list|(
operator|new
name|ScreenLogFactory
argument_list|(
name|settings
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isFileLog (SessionSettings settings, Set<LogFactory> impliedLogFactories)
specifier|private
name|void
name|isFileLog
parameter_list|(
name|SessionSettings
name|settings
parameter_list|,
name|Set
argument_list|<
name|LogFactory
argument_list|>
name|impliedLogFactories
parameter_list|)
block|{
if|if
condition|(
name|settings
operator|.
name|isSetting
argument_list|(
name|FileLogFactory
operator|.
name|SETTING_FILE_LOG_PATH
argument_list|)
condition|)
block|{
name|impliedLogFactories
operator|.
name|add
argument_list|(
operator|new
name|FileLogFactory
argument_list|(
name|settings
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isJdbcLog (SessionSettings settings, Set<LogFactory> impliedLogFactories)
specifier|private
name|void
name|isJdbcLog
parameter_list|(
name|SessionSettings
name|settings
parameter_list|,
name|Set
argument_list|<
name|LogFactory
argument_list|>
name|impliedLogFactories
parameter_list|)
block|{
if|if
condition|(
name|impliedLogFactories
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|&&
name|settings
operator|.
name|isSetting
argument_list|(
name|JdbcSetting
operator|.
name|SETTING_JDBC_DRIVER
argument_list|)
condition|)
block|{
name|impliedLogFactories
operator|.
name|add
argument_list|(
operator|new
name|JdbcLogFactory
argument_list|(
name|settings
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isConnectorRole (SessionSettings settings, String connectorRole)
specifier|private
name|boolean
name|isConnectorRole
parameter_list|(
name|SessionSettings
name|settings
parameter_list|,
name|String
name|connectorRole
parameter_list|)
throws|throws
name|ConfigError
block|{
name|boolean
name|hasRole
init|=
literal|false
decl_stmt|;
name|Iterator
argument_list|<
name|SessionID
argument_list|>
name|sessionIdItr
init|=
name|settings
operator|.
name|sectionIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|sessionIdItr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
try|try
block|{
if|if
condition|(
name|connectorRole
operator|.
name|equals
argument_list|(
name|settings
operator|.
name|getString
argument_list|(
operator|(
name|SessionID
operator|)
name|sessionIdItr
operator|.
name|next
argument_list|()
argument_list|,
name|SessionFactory
operator|.
name|SETTING_CONNECTION_TYPE
argument_list|)
argument_list|)
condition|)
block|{
name|hasRole
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|FieldConvertError
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConfigError
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|hasRole
return|;
block|}
DECL|method|addEventListener (QuickfixjEventListener listener)
specifier|public
name|void
name|addEventListener
parameter_list|(
name|QuickfixjEventListener
name|listener
parameter_list|)
block|{
name|eventListeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
DECL|method|removeEventListener (QuickfixjEventListener listener)
specifier|public
name|void
name|removeEventListener
parameter_list|(
name|QuickfixjEventListener
name|listener
parameter_list|)
block|{
name|eventListeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
DECL|class|Dispatcher
specifier|private
class|class
name|Dispatcher
implements|implements
name|Application
block|{
DECL|method|fromAdmin (Message message, SessionID sessionID)
specifier|public
name|void
name|fromAdmin
parameter_list|(
name|Message
name|message
parameter_list|,
name|SessionID
name|sessionID
parameter_list|)
throws|throws
name|FieldNotFound
throws|,
name|IncorrectDataFormat
throws|,
name|IncorrectTagValue
throws|,
name|RejectLogon
block|{
name|dispatch
argument_list|(
name|QuickfixjEventCategory
operator|.
name|AdminMessageReceived
argument_list|,
name|sessionID
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|fromApp (Message message, SessionID sessionID)
specifier|public
name|void
name|fromApp
parameter_list|(
name|Message
name|message
parameter_list|,
name|SessionID
name|sessionID
parameter_list|)
throws|throws
name|FieldNotFound
throws|,
name|IncorrectDataFormat
throws|,
name|IncorrectTagValue
throws|,
name|UnsupportedMessageType
block|{
name|dispatch
argument_list|(
name|QuickfixjEventCategory
operator|.
name|AppMessageReceived
argument_list|,
name|sessionID
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|onCreate (SessionID sessionID)
specifier|public
name|void
name|onCreate
parameter_list|(
name|SessionID
name|sessionID
parameter_list|)
block|{
name|dispatch
argument_list|(
name|QuickfixjEventCategory
operator|.
name|SessionCreated
argument_list|,
name|sessionID
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|onLogon (SessionID sessionID)
specifier|public
name|void
name|onLogon
parameter_list|(
name|SessionID
name|sessionID
parameter_list|)
block|{
name|dispatch
argument_list|(
name|QuickfixjEventCategory
operator|.
name|SessionLogon
argument_list|,
name|sessionID
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|onLogout (SessionID sessionID)
specifier|public
name|void
name|onLogout
parameter_list|(
name|SessionID
name|sessionID
parameter_list|)
block|{
name|dispatch
argument_list|(
name|QuickfixjEventCategory
operator|.
name|SessionLogoff
argument_list|,
name|sessionID
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|toAdmin (Message message, SessionID sessionID)
specifier|public
name|void
name|toAdmin
parameter_list|(
name|Message
name|message
parameter_list|,
name|SessionID
name|sessionID
parameter_list|)
block|{
name|dispatch
argument_list|(
name|QuickfixjEventCategory
operator|.
name|AdminMessageSent
argument_list|,
name|sessionID
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|toApp (Message message, SessionID sessionID)
specifier|public
name|void
name|toApp
parameter_list|(
name|Message
name|message
parameter_list|,
name|SessionID
name|sessionID
parameter_list|)
throws|throws
name|DoNotSend
block|{
name|dispatch
argument_list|(
name|QuickfixjEventCategory
operator|.
name|AppMessageSent
argument_list|,
name|sessionID
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|dispatch (QuickfixjEventCategory quickfixjEventCategory, SessionID sessionID, Message message)
specifier|private
name|void
name|dispatch
parameter_list|(
name|QuickfixjEventCategory
name|quickfixjEventCategory
parameter_list|,
name|SessionID
name|sessionID
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
comment|// TODO Find a way to propagate exception to the QFJ engine (RejectLogon, DoNotSend, etc.)
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"FIX event dispatched: "
operator|+
name|quickfixjEventCategory
operator|+
literal|" "
operator|+
operator|(
name|message
operator|!=
literal|null
condition|?
name|message
else|:
literal|""
operator|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|QuickfixjEventListener
name|listener
range|:
name|eventListeners
control|)
block|{
try|try
block|{
name|listener
operator|.
name|onEvent
argument_list|(
name|quickfixjEventCategory
argument_list|,
name|sessionID
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error during event dispatching"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|getSettingsResourceName ()
specifier|public
name|String
name|getSettingsResourceName
parameter_list|()
block|{
return|return
name|settingsResourceName
return|;
block|}
comment|// For Testing
DECL|method|getInitiator ()
name|Initiator
name|getInitiator
parameter_list|()
block|{
return|return
name|initiator
return|;
block|}
comment|// For Testing
DECL|method|getAcceptor ()
name|Acceptor
name|getAcceptor
parameter_list|()
block|{
return|return
name|acceptor
return|;
block|}
comment|// For Testing
DECL|method|getMessageStoreFactory ()
name|MessageStoreFactory
name|getMessageStoreFactory
parameter_list|()
block|{
return|return
name|messageStoreFactory
return|;
block|}
comment|// For Testing
DECL|method|getLogFactory ()
name|LogFactory
name|getLogFactory
parameter_list|()
block|{
return|return
name|sessionLogFactory
return|;
block|}
comment|// For Testing
DECL|method|getMessageFactory ()
name|MessageFactory
name|getMessageFactory
parameter_list|()
block|{
return|return
name|messageFactory
return|;
block|}
block|}
end_class

end_unit

