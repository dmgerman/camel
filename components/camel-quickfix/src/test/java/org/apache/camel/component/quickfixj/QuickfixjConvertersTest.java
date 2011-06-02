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
name|File
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
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
name|component
operator|.
name|quickfixj
operator|.
name|converter
operator|.
name|QuickfixjConverters
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
name|DefaultCamelContext
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
name|mina
operator|.
name|common
operator|.
name|TransportType
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
name|ConfigError
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|DataDictionary
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
name|Initiator
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
name|HopCompID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|MsgType
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|fix44
operator|.
name|Message
operator|.
name|Header
operator|.
name|NoHops
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|instanceOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|nullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|QuickfixjConvertersTest
specifier|public
class|class
name|QuickfixjConvertersTest
block|{
DECL|field|camelContext
specifier|private
specifier|static
name|DefaultCamelContext
name|camelContext
decl_stmt|;
DECL|field|settingsFile
specifier|private
name|File
name|settingsFile
decl_stmt|;
DECL|field|contextClassLoader
specifier|private
name|ClassLoader
name|contextClassLoader
decl_stmt|;
DECL|field|settings
specifier|private
name|SessionSettings
name|settings
decl_stmt|;
DECL|field|tempdir
specifier|private
name|File
name|tempdir
decl_stmt|;
DECL|field|quickfixjEngine
specifier|private
name|QuickfixjEngine
name|quickfixjEngine
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|classSetUp ()
specifier|public
specifier|static
name|void
name|classSetUp
parameter_list|()
throws|throws
name|Exception
block|{
name|camelContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
block|}
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
name|settingsFile
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"quickfixj_test_"
argument_list|,
literal|".cfg"
argument_list|)
expr_stmt|;
name|tempdir
operator|=
name|settingsFile
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
name|URL
index|[]
name|urls
init|=
operator|new
name|URL
index|[]
block|{
name|tempdir
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
block|}
decl_stmt|;
name|contextClassLoader
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
name|ClassLoader
name|testClassLoader
init|=
operator|new
name|URLClassLoader
argument_list|(
name|urls
argument_list|,
name|contextClassLoader
argument_list|)
decl_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|testClassLoader
argument_list|)
expr_stmt|;
name|settings
operator|=
operator|new
name|SessionSettings
argument_list|()
expr_stmt|;
name|settings
operator|.
name|setString
argument_list|(
name|Acceptor
operator|.
name|SETTING_SOCKET_ACCEPT_PROTOCOL
argument_list|,
name|TransportType
operator|.
name|VM_PIPE
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setString
argument_list|(
name|Initiator
operator|.
name|SETTING_SOCKET_CONNECT_PROTOCOL
argument_list|,
name|TransportType
operator|.
name|VM_PIPE
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|contextClassLoader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertSessionID ()
specifier|public
name|void
name|convertSessionID
parameter_list|()
block|{
name|Object
name|value
init|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|SessionID
operator|.
name|class
argument_list|,
literal|"FIX.4.0:FOO->BAR"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|value
argument_list|,
name|instanceOf
argument_list|(
name|SessionID
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
name|SessionID
operator|)
name|value
argument_list|,
name|is
argument_list|(
operator|new
name|SessionID
argument_list|(
literal|"FIX.4.0"
argument_list|,
literal|"FOO"
argument_list|,
literal|"BAR"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertToExchange ()
specifier|public
name|void
name|convertToExchange
parameter_list|()
block|{
name|SessionID
name|sessionID
init|=
operator|new
name|SessionID
argument_list|(
literal|"FIX.4.0"
argument_list|,
literal|"FOO"
argument_list|,
literal|"BAR"
argument_list|)
decl_stmt|;
name|QuickfixjEndpoint
name|endpoint
init|=
operator|new
name|QuickfixjEndpoint
argument_list|(
literal|null
argument_list|,
literal|""
argument_list|,
name|camelContext
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|Message
argument_list|()
decl_stmt|;
name|message
operator|.
name|getHeader
argument_list|()
operator|.
name|setString
argument_list|(
name|MsgType
operator|.
name|FIELD
argument_list|,
name|MsgType
operator|.
name|ORDER_SINGLE
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|QuickfixjConverters
operator|.
name|toExchange
argument_list|(
name|endpoint
argument_list|,
name|sessionID
argument_list|,
name|message
argument_list|,
name|QuickfixjEventCategory
operator|.
name|AppMessageSent
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
operator|(
name|SessionID
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|QuickfixjEndpoint
operator|.
name|SESSION_ID_KEY
argument_list|)
argument_list|,
name|is
argument_list|(
name|sessionID
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
name|QuickfixjEventCategory
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|QuickfixjEndpoint
operator|.
name|EVENT_CATEGORY_KEY
argument_list|)
argument_list|,
name|is
argument_list|(
name|QuickfixjEventCategory
operator|.
name|AppMessageSent
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|QuickfixjEndpoint
operator|.
name|MESSAGE_TYPE_KEY
argument_list|)
argument_list|,
name|is
argument_list|(
name|MsgType
operator|.
name|ORDER_SINGLE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertToExchangeWithNullMessage ()
specifier|public
name|void
name|convertToExchangeWithNullMessage
parameter_list|()
block|{
name|SessionID
name|sessionID
init|=
operator|new
name|SessionID
argument_list|(
literal|"FIX.4.0"
argument_list|,
literal|"FOO"
argument_list|,
literal|"BAR"
argument_list|)
decl_stmt|;
name|QuickfixjEndpoint
name|endpoint
init|=
operator|new
name|QuickfixjEndpoint
argument_list|(
literal|null
argument_list|,
literal|""
argument_list|,
name|camelContext
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|QuickfixjConverters
operator|.
name|toExchange
argument_list|(
name|endpoint
argument_list|,
name|sessionID
argument_list|,
literal|null
argument_list|,
name|QuickfixjEventCategory
operator|.
name|AppMessageSent
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
operator|(
name|SessionID
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|QuickfixjEndpoint
operator|.
name|SESSION_ID_KEY
argument_list|)
argument_list|,
name|is
argument_list|(
name|sessionID
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
name|QuickfixjEventCategory
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|QuickfixjEndpoint
operator|.
name|EVENT_CATEGORY_KEY
argument_list|)
argument_list|,
name|is
argument_list|(
name|QuickfixjEventCategory
operator|.
name|AppMessageSent
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|QuickfixjEndpoint
operator|.
name|MESSAGE_TYPE_KEY
argument_list|)
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertMessageWithoutRepeatingGroups ()
specifier|public
name|void
name|convertMessageWithoutRepeatingGroups
parameter_list|()
block|{
name|String
name|data
init|=
literal|"8=FIX.4.0\0019=100\00135=D\00134=2\00149=TW\00156=ISLD\00111=ID\00121=1\001"
operator|+
literal|"40=1\00154=1\00140=2\00138=200\00155=INTC\00110=160\001"
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Message
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|value
argument_list|,
name|instanceOf
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|convertMessageWithRepeatingGroupsUsingSessionID ()
specifier|public
name|void
name|convertMessageWithRepeatingGroupsUsingSessionID
parameter_list|()
throws|throws
name|Exception
block|{
name|SessionID
name|sessionID
init|=
operator|new
name|SessionID
argument_list|(
literal|"FIX.4.4"
argument_list|,
literal|"FOO"
argument_list|,
literal|"BAR"
argument_list|)
decl_stmt|;
name|createSession
argument_list|(
name|sessionID
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|data
init|=
literal|"8=FIX.4.4\0019=40\00135=A\001"
operator|+
literal|"627=2\001628=FOO\001628=BAR\001"
operator|+
literal|"98=0\001384=2\001372=D\001385=R\001372=8\001385=S\00110=230\001"
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|QuickfixjEndpoint
operator|.
name|SESSION_ID_KEY
argument_list|,
name|sessionID
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|NoHops
name|hop
init|=
operator|new
name|NoHops
argument_list|()
decl_stmt|;
name|message
operator|.
name|getHeader
argument_list|()
operator|.
name|getGroup
argument_list|(
literal|1
argument_list|,
name|hop
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FOO"
argument_list|,
name|hop
operator|.
name|getString
argument_list|(
name|HopCompID
operator|.
name|FIELD
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|getHeader
argument_list|()
operator|.
name|getGroup
argument_list|(
literal|2
argument_list|,
name|hop
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BAR"
argument_list|,
name|hop
operator|.
name|getString
argument_list|(
name|HopCompID
operator|.
name|FIELD
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|quickfixjEngine
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|convertMessageWithRepeatingGroupsUsingExchangeDictionary ()
specifier|public
name|void
name|convertMessageWithRepeatingGroupsUsingExchangeDictionary
parameter_list|()
throws|throws
name|Exception
block|{
name|SessionID
name|sessionID
init|=
operator|new
name|SessionID
argument_list|(
literal|"FIX.4.4"
argument_list|,
literal|"FOO"
argument_list|,
literal|"BAR"
argument_list|)
decl_stmt|;
name|createSession
argument_list|(
name|sessionID
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|data
init|=
literal|"8=FIX.4.4\0019=40\00135=A\001"
operator|+
literal|"627=2\001628=FOO\001628=BAR\001"
operator|+
literal|"98=0\001384=2\001372=D\001385=R\001372=8\001385=S\00110=230\001"
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|QuickfixjEndpoint
operator|.
name|DATA_DICTIONARY_KEY
argument_list|,
operator|new
name|DataDictionary
argument_list|(
literal|"FIX44.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|NoHops
name|hop
init|=
operator|new
name|NoHops
argument_list|()
decl_stmt|;
name|message
operator|.
name|getHeader
argument_list|()
operator|.
name|getGroup
argument_list|(
literal|1
argument_list|,
name|hop
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FOO"
argument_list|,
name|hop
operator|.
name|getString
argument_list|(
name|HopCompID
operator|.
name|FIELD
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|getHeader
argument_list|()
operator|.
name|getGroup
argument_list|(
literal|2
argument_list|,
name|hop
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BAR"
argument_list|,
name|hop
operator|.
name|getString
argument_list|(
name|HopCompID
operator|.
name|FIELD
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|quickfixjEngine
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|convertMessageWithRepeatingGroupsUsingExchangeDictionaryResource ()
specifier|public
name|void
name|convertMessageWithRepeatingGroupsUsingExchangeDictionaryResource
parameter_list|()
throws|throws
name|Exception
block|{
name|SessionID
name|sessionID
init|=
operator|new
name|SessionID
argument_list|(
literal|"FIX.4.4"
argument_list|,
literal|"FOO"
argument_list|,
literal|"BAR"
argument_list|)
decl_stmt|;
name|createSession
argument_list|(
name|sessionID
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|data
init|=
literal|"8=FIX.4.4\0019=40\00135=A\001"
operator|+
literal|"627=2\001628=FOO\001628=BAR\001"
operator|+
literal|"98=0\001384=2\001372=D\001385=R\001372=8\001385=S\00110=230\001"
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|QuickfixjEndpoint
operator|.
name|DATA_DICTIONARY_KEY
argument_list|,
literal|"FIX44.xml"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|NoHops
name|hop
init|=
operator|new
name|NoHops
argument_list|()
decl_stmt|;
name|message
operator|.
name|getHeader
argument_list|()
operator|.
name|getGroup
argument_list|(
literal|1
argument_list|,
name|hop
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"FOO"
argument_list|,
name|hop
operator|.
name|getString
argument_list|(
name|HopCompID
operator|.
name|FIELD
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|getHeader
argument_list|()
operator|.
name|getGroup
argument_list|(
literal|2
argument_list|,
name|hop
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BAR"
argument_list|,
name|hop
operator|.
name|getString
argument_list|(
name|HopCompID
operator|.
name|FIELD
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|quickfixjEngine
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createSession (SessionID sessionID)
specifier|private
name|void
name|createSession
parameter_list|(
name|SessionID
name|sessionID
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigError
throws|,
name|FieldConvertError
throws|,
name|JMException
throws|,
name|Exception
block|{
name|SessionSettings
name|settings
init|=
operator|new
name|SessionSettings
argument_list|()
decl_stmt|;
name|settings
operator|.
name|setString
argument_list|(
name|Acceptor
operator|.
name|SETTING_SOCKET_ACCEPT_PROTOCOL
argument_list|,
name|TransportType
operator|.
name|VM_PIPE
operator|.
name|toString
argument_list|()
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
literal|1234
argument_list|)
expr_stmt|;
name|TestSupport
operator|.
name|setSessionID
argument_list|(
name|settings
argument_list|,
name|sessionID
argument_list|)
expr_stmt|;
name|TestSupport
operator|.
name|writeSettings
argument_list|(
name|settings
argument_list|,
name|settingsFile
argument_list|)
expr_stmt|;
name|quickfixjEngine
operator|=
operator|new
name|QuickfixjEngine
argument_list|(
literal|"quickfix:test"
argument_list|,
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|quickfixjEngine
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

