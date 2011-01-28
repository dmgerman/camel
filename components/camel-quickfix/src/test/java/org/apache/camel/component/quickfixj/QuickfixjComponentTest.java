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
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
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
name|TimeUnit
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
name|Consumer
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
name|Endpoint
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
name|ExchangePattern
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
name|MultipleConsumersSupport
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
name|Processor
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
name|Producer
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
name|ServiceSupport
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
name|converter
operator|.
name|StaticMethodTypeConverter
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
name|ServiceHelper
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
name|DefaultMessageFactory
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|FixVersions
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
name|SenderCompID
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
name|TargetCompID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|fix44
operator|.
name|Email
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
name|notNullValue
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
name|assertThat
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
name|assertTrue
import|;
end_import

begin_class
DECL|class|QuickfixjComponentTest
specifier|public
class|class
name|QuickfixjComponentTest
block|{
DECL|field|settingsFile
specifier|private
name|File
name|settingsFile
decl_stmt|;
DECL|field|tempdir
specifier|private
name|File
name|tempdir
decl_stmt|;
DECL|field|contextClassLoader
specifier|private
name|ClassLoader
name|contextClassLoader
decl_stmt|;
DECL|field|sessionID
specifier|private
name|SessionID
name|sessionID
decl_stmt|;
DECL|field|settings
specifier|private
name|SessionSettings
name|settings
decl_stmt|;
DECL|field|component
specifier|private
name|QuickfixjComponent
name|component
decl_stmt|;
DECL|field|engineMessageFactory
specifier|private
name|MessageFactory
name|engineMessageFactory
decl_stmt|;
DECL|field|engineMessageStoreFactory
specifier|private
name|MessageStoreFactory
name|engineMessageStoreFactory
decl_stmt|;
DECL|field|engineLogFactory
specifier|private
name|LogFactory
name|engineLogFactory
decl_stmt|;
DECL|method|setSessionID (SessionSettings sessionSettings, SessionID sessionID)
specifier|private
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
DECL|method|getEndpointUri (final String configFilename, SessionID sid)
specifier|private
name|String
name|getEndpointUri
parameter_list|(
specifier|final
name|String
name|configFilename
parameter_list|,
name|SessionID
name|sid
parameter_list|)
block|{
name|String
name|uri
init|=
literal|"quickfix:"
operator|+
name|configFilename
decl_stmt|;
if|if
condition|(
name|sid
operator|!=
literal|null
condition|)
block|{
name|uri
operator|+=
literal|"?sessionID="
operator|+
name|sid
expr_stmt|;
block|}
return|return
name|uri
return|;
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
name|sessionID
operator|=
operator|new
name|SessionID
argument_list|(
name|FixVersions
operator|.
name|BEGINSTRING_FIX44
argument_list|,
literal|"FOO"
argument_list|,
literal|"BAR"
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
name|setSessionID
argument_list|(
name|settings
argument_list|,
name|sessionID
argument_list|)
expr_stmt|;
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
block|}
DECL|method|setUpComponent ()
specifier|private
name|void
name|setUpComponent
parameter_list|()
throws|throws
name|IOException
throws|,
name|MalformedURLException
throws|,
name|NoSuchMethodException
block|{
name|setUpComponent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|setUpComponent (boolean injectQfjPlugins)
specifier|private
name|void
name|setUpComponent
parameter_list|(
name|boolean
name|injectQfjPlugins
parameter_list|)
throws|throws
name|IOException
throws|,
name|MalformedURLException
throws|,
name|NoSuchMethodException
block|{
name|DefaultCamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|component
operator|=
operator|new
name|QuickfixjComponent
argument_list|()
expr_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
if|if
condition|(
name|injectQfjPlugins
condition|)
block|{
name|engineMessageFactory
operator|=
operator|new
name|DefaultMessageFactory
argument_list|()
expr_stmt|;
name|engineMessageStoreFactory
operator|=
operator|new
name|MemoryStoreFactory
argument_list|()
expr_stmt|;
name|engineLogFactory
operator|=
operator|new
name|ScreenLogFactory
argument_list|()
expr_stmt|;
name|component
operator|.
name|setMessageFactory
argument_list|(
name|engineMessageFactory
argument_list|)
expr_stmt|;
name|component
operator|.
name|setMessageStoreFactory
argument_list|(
name|engineMessageStoreFactory
argument_list|)
expr_stmt|;
name|component
operator|.
name|setLogFactory
argument_list|(
name|engineLogFactory
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|is
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Method
name|converterMethod
init|=
name|QuickfixjConverters
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"toSessionID"
argument_list|,
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|String
operator|.
name|class
block|}
block|)
function|;
name|camelContext
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addTypeConverter
argument_list|(
name|SessionID
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
operator|new
name|StaticMethodTypeConverter
argument_list|(
name|converterMethod
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_class

begin_function
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
if|if
condition|(
name|component
operator|!=
literal|null
condition|)
block|{
name|component
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|createEndpointBeforeComponentStart ()
specifier|public
name|void
name|createEndpointBeforeComponentStart
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpComponent
argument_list|()
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
name|INITIATOR_CONNECTION_TYPE
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setLong
argument_list|(
name|sessionID
argument_list|,
name|Initiator
operator|.
name|SETTING_SOCKET_CONNECT_PORT
argument_list|,
literal|1234
argument_list|)
expr_stmt|;
name|writeSettings
argument_list|()
expr_stmt|;
name|Endpoint
name|e1
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
name|getEndpointUri
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|is
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|get
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|get
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
operator|(
name|QuickfixjEndpoint
operator|)
name|e1
operator|)
operator|.
name|getSessionID
argument_list|()
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Should used cached QFJ engine
name|Endpoint
name|e2
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
name|getEndpointUri
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|,
name|sessionID
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|is
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|get
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|get
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
operator|(
name|QuickfixjEndpoint
operator|)
name|e2
operator|)
operator|.
name|getSessionID
argument_list|()
argument_list|,
name|is
argument_list|(
name|sessionID
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|get
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// Move these too an endpoint testcase if one exists
name|assertThat
argument_list|(
name|e2
operator|.
name|isSingleton
argument_list|()
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
operator|(
name|MultipleConsumersSupport
operator|)
name|e2
operator|)
operator|.
name|isMultipleConsumersSupported
argument_list|()
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|createEndpointAfterComponentStart ()
specifier|public
name|void
name|createEndpointAfterComponentStart
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpComponent
argument_list|()
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
name|INITIATOR_CONNECTION_TYPE
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setLong
argument_list|(
name|sessionID
argument_list|,
name|Initiator
operator|.
name|SETTING_SOCKET_CONNECT_PORT
argument_list|,
literal|1234
argument_list|)
expr_stmt|;
name|writeSettings
argument_list|()
expr_stmt|;
name|component
operator|.
name|start
argument_list|()
expr_stmt|;
name|Endpoint
name|e1
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
name|getEndpointUri
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|is
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|get
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|get
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
operator|(
name|QuickfixjEndpoint
operator|)
name|e1
operator|)
operator|.
name|getSessionID
argument_list|()
argument_list|,
name|is
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Should used cached QFJ engine
name|Endpoint
name|e2
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
name|getEndpointUri
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|,
name|sessionID
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|is
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|get
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|is
argument_list|(
name|notNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|get
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
operator|(
name|QuickfixjEndpoint
operator|)
name|e2
operator|)
operator|.
name|getSessionID
argument_list|()
argument_list|,
name|is
argument_list|(
name|sessionID
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|componentStop ()
specifier|public
name|void
name|componentStop
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpComponent
argument_list|()
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
name|INITIATOR_CONNECTION_TYPE
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setLong
argument_list|(
name|sessionID
argument_list|,
name|Initiator
operator|.
name|SETTING_SOCKET_CONNECT_PORT
argument_list|,
literal|1234
argument_list|)
expr_stmt|;
name|writeSettings
argument_list|()
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
name|getEndpointUri
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Consumer
name|consumer
init|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|QuickfixjEventCategory
name|eventCategory
init|=
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
decl_stmt|;
if|if
condition|(
name|eventCategory
operator|==
name|QuickfixjEventCategory
operator|.
name|SessionCreated
condition|)
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
comment|// Endpoint automatically starts the consumer
name|assertThat
argument_list|(
operator|(
operator|(
name|ServiceSupport
operator|)
name|consumer
operator|)
operator|.
name|isStarted
argument_list|()
argument_list|,
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Session not created"
argument_list|,
name|latch
operator|.
name|await
argument_list|(
literal|5000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|get
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|,
name|is
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|messagePublication ()
specifier|public
name|void
name|messagePublication
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpComponent
argument_list|()
expr_stmt|;
comment|// Create settings file with both acceptor and initiator
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
name|SessionID
name|acceptorSessionID
init|=
operator|new
name|SessionID
argument_list|(
name|FixVersions
operator|.
name|BEGINSTRING_FIX44
argument_list|,
literal|"ACCEPTOR"
argument_list|,
literal|"INITIATOR"
argument_list|)
decl_stmt|;
name|settings
operator|.
name|setString
argument_list|(
name|acceptorSessionID
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
name|acceptorSessionID
argument_list|,
name|Acceptor
operator|.
name|SETTING_SOCKET_ACCEPT_PORT
argument_list|,
literal|1234
argument_list|)
expr_stmt|;
name|setSessionID
argument_list|(
name|settings
argument_list|,
name|acceptorSessionID
argument_list|)
expr_stmt|;
name|SessionID
name|initiatorSessionID
init|=
operator|new
name|SessionID
argument_list|(
name|FixVersions
operator|.
name|BEGINSTRING_FIX44
argument_list|,
literal|"INITIATOR"
argument_list|,
literal|"ACCEPTOR"
argument_list|)
decl_stmt|;
name|settings
operator|.
name|setString
argument_list|(
name|initiatorSessionID
argument_list|,
name|SessionFactory
operator|.
name|SETTING_CONNECTION_TYPE
argument_list|,
name|SessionFactory
operator|.
name|INITIATOR_CONNECTION_TYPE
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setLong
argument_list|(
name|initiatorSessionID
argument_list|,
name|Initiator
operator|.
name|SETTING_SOCKET_CONNECT_PORT
argument_list|,
literal|1234
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setLong
argument_list|(
name|initiatorSessionID
argument_list|,
name|Initiator
operator|.
name|SETTING_RECONNECT_INTERVAL
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|setSessionID
argument_list|(
name|settings
argument_list|,
name|initiatorSessionID
argument_list|)
expr_stmt|;
name|writeSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
name|getEndpointUri
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
comment|// Start the component and wait for the FIX sessions to be logged on
specifier|final
name|CountDownLatch
name|logonLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|messageLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|Consumer
name|consumer
init|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|QuickfixjEventCategory
name|eventCategory
init|=
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
decl_stmt|;
if|if
condition|(
name|eventCategory
operator|==
name|QuickfixjEventCategory
operator|.
name|SessionLogon
condition|)
block|{
name|logonLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|eventCategory
operator|==
name|QuickfixjEventCategory
operator|.
name|AppMessageReceived
condition|)
block|{
name|messageLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|component
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Session not created"
argument_list|,
name|logonLatch
operator|.
name|await
argument_list|(
literal|5000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|Endpoint
name|producerEndpoint
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
name|getEndpointUri
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|,
name|acceptorSessionID
argument_list|)
argument_list|)
decl_stmt|;
name|Producer
name|producer
init|=
name|producerEndpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
comment|// FIX message to send
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
literal|"Test"
argument_list|)
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|email
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// Produce with no session ID specified, session ID must be in message
name|Producer
name|producer2
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|email
operator|.
name|getHeader
argument_list|()
operator|.
name|setString
argument_list|(
name|SenderCompID
operator|.
name|FIELD
argument_list|,
name|acceptorSessionID
operator|.
name|getSenderCompID
argument_list|()
argument_list|)
expr_stmt|;
name|email
operator|.
name|getHeader
argument_list|()
operator|.
name|setString
argument_list|(
name|TargetCompID
operator|.
name|FIELD
argument_list|,
name|acceptorSessionID
operator|.
name|getTargetCompID
argument_list|()
argument_list|)
expr_stmt|;
name|producer2
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Messages not received"
argument_list|,
name|messageLatch
operator|.
name|await
argument_list|(
literal|5000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|userSpecifiedQuickfixjPlugins ()
specifier|public
name|void
name|userSpecifiedQuickfixjPlugins
parameter_list|()
throws|throws
name|Exception
block|{
name|setUpComponent
argument_list|(
literal|true
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
name|INITIATOR_CONNECTION_TYPE
argument_list|)
expr_stmt|;
name|settings
operator|.
name|setLong
argument_list|(
name|sessionID
argument_list|,
name|Initiator
operator|.
name|SETTING_SOCKET_CONNECT_PORT
argument_list|,
literal|1234
argument_list|)
expr_stmt|;
name|writeSettings
argument_list|()
expr_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
name|getEndpointUri
argument_list|(
name|settingsFile
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|component
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|is
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|QuickfixjEngine
name|engine
init|=
name|component
operator|.
name|getEngines
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|engine
operator|.
name|getMessageFactory
argument_list|()
argument_list|,
name|is
argument_list|(
name|engineMessageFactory
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|engine
operator|.
name|getMessageStoreFactory
argument_list|()
argument_list|,
name|is
argument_list|(
name|engineMessageStoreFactory
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|engine
operator|.
name|getLogFactory
argument_list|()
argument_list|,
name|is
argument_list|(
name|engineLogFactory
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|writeSettings ()
specifier|private
name|void
name|writeSettings
parameter_list|()
throws|throws
name|IOException
block|{
name|writeSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|writeSettings (SessionSettings settings)
specifier|private
name|void
name|writeSettings
parameter_list|(
name|SessionSettings
name|settings
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
end_function

unit|}
end_unit

