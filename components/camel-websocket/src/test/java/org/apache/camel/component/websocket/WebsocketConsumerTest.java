begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.websocket
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|websocket
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
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
name|Message
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
name|spi
operator|.
name|ExceptionHandler
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|InOrder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|doThrow
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|inOrder
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|times
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|WebsocketConsumerTest
specifier|public
class|class
name|WebsocketConsumerTest
block|{
DECL|field|CONNECTION_KEY
specifier|private
specifier|static
specifier|final
name|String
name|CONNECTION_KEY
init|=
literal|"random-connection-key"
decl_stmt|;
DECL|field|MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE
init|=
literal|"message"
decl_stmt|;
DECL|field|ADDRESS
specifier|private
specifier|static
specifier|final
name|InetSocketAddress
name|ADDRESS
init|=
name|InetSocketAddress
operator|.
name|createUnresolved
argument_list|(
literal|"127.0.0.1"
argument_list|,
literal|12345
argument_list|)
decl_stmt|;
annotation|@
name|Mock
DECL|field|endpoint
specifier|private
name|WebsocketEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Mock
DECL|field|exceptionHandler
specifier|private
name|ExceptionHandler
name|exceptionHandler
decl_stmt|;
annotation|@
name|Mock
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
annotation|@
name|Mock
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
annotation|@
name|Mock
DECL|field|outMessage
specifier|private
name|Message
name|outMessage
decl_stmt|;
DECL|field|exception
specifier|private
name|Exception
name|exception
init|=
operator|new
name|Exception
argument_list|(
literal|"BAD NEWS EVERYONE!"
argument_list|)
decl_stmt|;
DECL|field|websocketConsumer
specifier|private
name|WebsocketConsumer
name|websocketConsumer
decl_stmt|;
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
name|websocketConsumer
operator|=
operator|new
name|WebsocketConsumer
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|websocketConsumer
operator|.
name|setExceptionHandler
argument_list|(
name|exceptionHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendExchange ()
specifier|public
name|void
name|testSendExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|endpoint
operator|.
name|createExchange
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|websocketConsumer
operator|.
name|sendMessage
argument_list|(
name|CONNECTION_KEY
argument_list|,
name|MESSAGE
argument_list|,
name|ADDRESS
argument_list|)
expr_stmt|;
name|InOrder
name|inOrder
init|=
name|inOrder
argument_list|(
name|endpoint
argument_list|,
name|exceptionHandler
argument_list|,
name|processor
argument_list|,
name|exchange
argument_list|,
name|outMessage
argument_list|)
decl_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|createExchange
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|outMessage
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|WebsocketConstants
operator|.
name|CONNECTION_KEY
argument_list|,
name|CONNECTION_KEY
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|outMessage
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|(
name|MESSAGE
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|processor
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getException
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendExchangeWithException ()
specifier|public
name|void
name|testSendExchangeWithException
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|endpoint
operator|.
name|createExchange
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|doThrow
argument_list|(
name|exception
argument_list|)
operator|.
name|when
argument_list|(
name|processor
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|exception
argument_list|)
expr_stmt|;
name|websocketConsumer
operator|.
name|sendMessage
argument_list|(
name|CONNECTION_KEY
argument_list|,
name|MESSAGE
argument_list|,
name|ADDRESS
argument_list|)
expr_stmt|;
name|InOrder
name|inOrder
init|=
name|inOrder
argument_list|(
name|endpoint
argument_list|,
name|exceptionHandler
argument_list|,
name|processor
argument_list|,
name|exchange
argument_list|,
name|outMessage
argument_list|)
decl_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|createExchange
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|outMessage
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|WebsocketConstants
operator|.
name|CONNECTION_KEY
argument_list|,
name|CONNECTION_KEY
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|outMessage
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|(
name|MESSAGE
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|processor
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|getException
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exceptionHandler
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|handleException
argument_list|(
name|any
argument_list|()
argument_list|,
name|eq
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|eq
argument_list|(
name|exception
argument_list|)
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendExchangeWithExchangeExceptionIsNull ()
specifier|public
name|void
name|testSendExchangeWithExchangeExceptionIsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|endpoint
operator|.
name|createExchange
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|doThrow
argument_list|(
name|exception
argument_list|)
operator|.
name|when
argument_list|(
name|processor
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|websocketConsumer
operator|.
name|sendMessage
argument_list|(
name|CONNECTION_KEY
argument_list|,
name|MESSAGE
argument_list|,
name|ADDRESS
argument_list|)
expr_stmt|;
name|InOrder
name|inOrder
init|=
name|inOrder
argument_list|(
name|endpoint
argument_list|,
name|exceptionHandler
argument_list|,
name|processor
argument_list|,
name|exchange
argument_list|,
name|outMessage
argument_list|)
decl_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|endpoint
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|createExchange
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|outMessage
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|WebsocketConstants
operator|.
name|CONNECTION_KEY
argument_list|,
name|CONNECTION_KEY
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|outMessage
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|(
name|MESSAGE
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|processor
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|exchange
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getException
argument_list|()
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

