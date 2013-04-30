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
name|util
operator|.
name|Timer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimerTask
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
name|ExchangePattern
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
name|mockito
operator|.
name|Matchers
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
name|org
operator|.
name|mockito
operator|.
name|invocation
operator|.
name|InvocationOnMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|stubbing
operator|.
name|Answer
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
name|FieldConvertError
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
name|Message
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|MessageUtils
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
name|SessionID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|BeginString
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
name|TargetCompID
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
DECL|class|QuickfixjProducerTest
specifier|public
class|class
name|QuickfixjProducerTest
block|{
DECL|field|mockExchange
specifier|private
name|Exchange
name|mockExchange
decl_stmt|;
DECL|field|mockEndpoint
specifier|private
name|QuickfixjEndpoint
name|mockEndpoint
decl_stmt|;
DECL|field|mockCamelMessage
specifier|private
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|mockCamelMessage
decl_stmt|;
DECL|field|producer
specifier|private
name|QuickfixjProducer
name|producer
decl_stmt|;
DECL|field|sessionID
specifier|private
name|SessionID
name|sessionID
decl_stmt|;
DECL|field|inboundFixMessage
specifier|private
name|Message
name|inboundFixMessage
decl_stmt|;
DECL|field|quickfixjEngine
specifier|private
name|QuickfixjEngine
name|quickfixjEngine
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
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
name|mockExchange
operator|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|=
name|Mockito
operator|.
name|mock
argument_list|(
name|QuickfixjEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|mockCamelMessage
operator|=
name|Mockito
operator|.
name|mock
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
operator|.
name|class
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockExchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockCamelMessage
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockExchange
operator|.
name|getPattern
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
name|quickfixjEngine
operator|=
name|TestSupport
operator|.
name|createEngine
argument_list|()
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockEndpoint
operator|.
name|getEngine
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|quickfixjEngine
argument_list|)
expr_stmt|;
name|inboundFixMessage
operator|=
operator|new
name|Message
argument_list|()
expr_stmt|;
name|inboundFixMessage
operator|.
name|getHeader
argument_list|()
operator|.
name|setString
argument_list|(
name|BeginString
operator|.
name|FIELD
argument_list|,
name|FixVersions
operator|.
name|BEGINSTRING_FIX44
argument_list|)
expr_stmt|;
name|inboundFixMessage
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
literal|"SENDER"
argument_list|)
expr_stmt|;
name|inboundFixMessage
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
literal|"TARGET"
argument_list|)
expr_stmt|;
name|sessionID
operator|=
name|MessageUtils
operator|.
name|getSessionID
argument_list|(
name|inboundFixMessage
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockCamelMessage
operator|.
name|getBody
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|inboundFixMessage
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockEndpoint
operator|.
name|getSessionID
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|sessionID
argument_list|)
expr_stmt|;
name|producer
operator|=
name|Mockito
operator|.
name|spy
argument_list|(
operator|new
name|QuickfixjProducer
argument_list|(
name|mockEndpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|TestException
specifier|public
class|class
name|TestException
extends|extends
name|RuntimeException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
block|}
annotation|@
name|Test
DECL|method|setExceptionOnExchange ()
specifier|public
name|void
name|setExceptionOnExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|Session
name|mockSession
init|=
name|Mockito
operator|.
name|spy
argument_list|(
name|TestSupport
operator|.
name|createSession
argument_list|(
name|sessionID
argument_list|)
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|doReturn
argument_list|(
name|mockSession
argument_list|)
operator|.
name|when
argument_list|(
name|producer
argument_list|)
operator|.
name|getSession
argument_list|(
name|MessageUtils
operator|.
name|getSessionID
argument_list|(
name|inboundFixMessage
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|doThrow
argument_list|(
operator|new
name|TestException
argument_list|()
argument_list|)
operator|.
name|when
argument_list|(
name|mockSession
argument_list|)
operator|.
name|send
argument_list|(
name|Mockito
operator|.
name|isA
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockExchange
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockExchange
argument_list|)
operator|.
name|setException
argument_list|(
name|Matchers
operator|.
name|isA
argument_list|(
name|TestException
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|processInOnlyExchangeSuccess ()
specifier|public
name|void
name|processInOnlyExchangeSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|Session
name|mockSession
init|=
name|Mockito
operator|.
name|spy
argument_list|(
name|TestSupport
operator|.
name|createSession
argument_list|(
name|sessionID
argument_list|)
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|doReturn
argument_list|(
name|mockSession
argument_list|)
operator|.
name|when
argument_list|(
name|producer
argument_list|)
operator|.
name|getSession
argument_list|(
name|MessageUtils
operator|.
name|getSessionID
argument_list|(
name|inboundFixMessage
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|doReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|when
argument_list|(
name|mockSession
argument_list|)
operator|.
name|send
argument_list|(
name|Mockito
operator|.
name|isA
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockExchange
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockExchange
argument_list|,
name|Mockito
operator|.
name|never
argument_list|()
argument_list|)
operator|.
name|setException
argument_list|(
name|Matchers
operator|.
name|isA
argument_list|(
name|IllegalStateException
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockSession
argument_list|)
operator|.
name|send
argument_list|(
name|inboundFixMessage
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|processInOnlyExchangeSendUnsuccessful ()
specifier|public
name|void
name|processInOnlyExchangeSendUnsuccessful
parameter_list|()
throws|throws
name|Exception
block|{
name|Session
name|mockSession
init|=
name|Mockito
operator|.
name|spy
argument_list|(
name|TestSupport
operator|.
name|createSession
argument_list|(
name|sessionID
argument_list|)
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|doReturn
argument_list|(
name|mockSession
argument_list|)
operator|.
name|when
argument_list|(
name|producer
argument_list|)
operator|.
name|getSession
argument_list|(
name|MessageUtils
operator|.
name|getSessionID
argument_list|(
name|inboundFixMessage
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|doReturn
argument_list|(
literal|false
argument_list|)
operator|.
name|when
argument_list|(
name|mockSession
argument_list|)
operator|.
name|send
argument_list|(
name|Mockito
operator|.
name|isA
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockExchange
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockSession
argument_list|)
operator|.
name|send
argument_list|(
name|inboundFixMessage
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockExchange
argument_list|)
operator|.
name|setException
argument_list|(
name|Matchers
operator|.
name|isA
argument_list|(
name|CannotSendException
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|processInOutExchangeSuccess ()
specifier|public
name|void
name|processInOutExchangeSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|Mockito
operator|.
name|when
argument_list|(
name|mockExchange
operator|.
name|copy
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockExchange
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockExchange
operator|.
name|getPattern
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|SessionID
name|responseSessionID
init|=
operator|new
name|SessionID
argument_list|(
name|sessionID
operator|.
name|getBeginString
argument_list|()
argument_list|,
name|sessionID
operator|.
name|getTargetCompID
argument_list|()
argument_list|,
name|sessionID
operator|.
name|getSenderCompID
argument_list|()
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockExchange
operator|.
name|getProperty
argument_list|(
name|QuickfixjProducer
operator|.
name|CORRELATION_CRITERIA_KEY
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|MessagePredicate
argument_list|(
name|responseSessionID
argument_list|,
name|MsgType
operator|.
name|EMAIL
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockExchange
operator|.
name|getProperty
argument_list|(
name|QuickfixjProducer
operator|.
name|CORRELATION_TIMEOUT_KEY
argument_list|,
literal|1000L
argument_list|,
name|Long
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|5000L
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|mockOutboundCamelMessage
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockOutboundCamelMessage
argument_list|)
expr_stmt|;
specifier|final
name|Message
name|outboundFixMessage
init|=
operator|new
name|Email
argument_list|()
decl_stmt|;
name|outboundFixMessage
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
literal|"TARGET"
argument_list|)
expr_stmt|;
name|outboundFixMessage
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
literal|"SENDER"
argument_list|)
expr_stmt|;
name|Session
name|mockSession
init|=
name|Mockito
operator|.
name|spy
argument_list|(
name|TestSupport
operator|.
name|createSession
argument_list|(
name|sessionID
argument_list|)
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|doReturn
argument_list|(
name|mockSession
argument_list|)
operator|.
name|when
argument_list|(
name|producer
argument_list|)
operator|.
name|getSession
argument_list|(
name|MessageUtils
operator|.
name|getSessionID
argument_list|(
name|inboundFixMessage
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|doAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Boolean
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
throws|throws
name|Throwable
block|{
operator|new
name|Timer
argument_list|()
operator|.
name|schedule
argument_list|(
operator|new
name|TimerTask
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|quickfixjEngine
operator|.
name|getMessageCorrelator
argument_list|()
operator|.
name|onEvent
argument_list|(
name|QuickfixjEventCategory
operator|.
name|AppMessageReceived
argument_list|,
name|sessionID
argument_list|,
name|outboundFixMessage
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|,
literal|10
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|(
name|mockSession
argument_list|)
operator|.
name|send
argument_list|(
name|Mockito
operator|.
name|isA
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockExchange
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockExchange
argument_list|,
name|Mockito
operator|.
name|never
argument_list|()
argument_list|)
operator|.
name|setException
argument_list|(
name|Matchers
operator|.
name|isA
argument_list|(
name|IllegalStateException
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockSession
argument_list|)
operator|.
name|send
argument_list|(
name|inboundFixMessage
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockOutboundCamelMessage
argument_list|)
operator|.
name|setBody
argument_list|(
name|outboundFixMessage
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|processInOutExchangeSendUnsuccessful ()
specifier|public
name|void
name|processInOutExchangeSendUnsuccessful
parameter_list|()
throws|throws
name|Exception
block|{
name|Mockito
operator|.
name|when
argument_list|(
name|mockExchange
operator|.
name|getPattern
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockExchange
operator|.
name|getProperty
argument_list|(
name|QuickfixjProducer
operator|.
name|CORRELATION_CRITERIA_KEY
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|MessagePredicate
argument_list|(
name|sessionID
argument_list|,
name|MsgType
operator|.
name|EMAIL
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockExchange
operator|.
name|getProperty
argument_list|(
name|QuickfixjProducer
operator|.
name|CORRELATION_TIMEOUT_KEY
argument_list|,
literal|1000L
argument_list|,
name|Long
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|5000L
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|mockOutboundCamelMessage
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|mockExchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockOutboundCamelMessage
argument_list|)
expr_stmt|;
specifier|final
name|Message
name|outboundFixMessage
init|=
operator|new
name|Email
argument_list|()
decl_stmt|;
name|outboundFixMessage
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
literal|"TARGET"
argument_list|)
expr_stmt|;
name|outboundFixMessage
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
literal|"SENDER"
argument_list|)
expr_stmt|;
name|Session
name|mockSession
init|=
name|Mockito
operator|.
name|spy
argument_list|(
name|TestSupport
operator|.
name|createSession
argument_list|(
name|sessionID
argument_list|)
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|doReturn
argument_list|(
name|mockSession
argument_list|)
operator|.
name|when
argument_list|(
name|producer
argument_list|)
operator|.
name|getSession
argument_list|(
name|MessageUtils
operator|.
name|getSessionID
argument_list|(
name|inboundFixMessage
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|doAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Boolean
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
throws|throws
name|Throwable
block|{
operator|new
name|Timer
argument_list|()
operator|.
name|schedule
argument_list|(
operator|new
name|TimerTask
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|quickfixjEngine
operator|.
name|getMessageCorrelator
argument_list|()
operator|.
name|onEvent
argument_list|(
name|QuickfixjEventCategory
operator|.
name|AppMessageReceived
argument_list|,
name|sessionID
argument_list|,
name|outboundFixMessage
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|,
literal|10
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|(
name|mockSession
argument_list|)
operator|.
name|send
argument_list|(
name|Mockito
operator|.
name|isA
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|mockExchange
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockOutboundCamelMessage
argument_list|,
name|Mockito
operator|.
name|never
argument_list|()
argument_list|)
operator|.
name|setBody
argument_list|(
name|Mockito
operator|.
name|isA
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockSession
argument_list|)
operator|.
name|send
argument_list|(
name|inboundFixMessage
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|mockExchange
argument_list|)
operator|.
name|setException
argument_list|(
name|Matchers
operator|.
name|isA
argument_list|(
name|CannotSendException
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

