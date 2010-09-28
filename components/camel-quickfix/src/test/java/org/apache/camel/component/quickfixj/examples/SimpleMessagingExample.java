begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfixj.examples
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
operator|.
name|examples
package|;
end_package

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
name|builder
operator|.
name|PredicateBuilder
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
name|quickfixj
operator|.
name|QuickfixjEndpoint
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
name|QuickfixjEventCategory
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
name|TestSupport
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
name|examples
operator|.
name|transform
operator|.
name|QuickfixjMessageJsonPrinter
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
name|examples
operator|.
name|util
operator|.
name|CountDownLatchDecrementer
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
name|field
operator|.
name|MsgType
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

begin_comment
comment|/**  * This example demonstrates several features of the QuickFIX/J component. It uses  * QFJ session events to synchronize application behavior (e.g., Session logon).  *   */
end_comment

begin_class
DECL|class|SimpleMessagingExample
specifier|public
class|class
name|SimpleMessagingExample
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
name|SimpleMessagingExample
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
operator|new
name|SimpleMessagingExample
argument_list|()
operator|.
name|sendMessage
argument_list|()
expr_stmt|;
block|}
DECL|method|sendMessage ()
specifier|public
name|void
name|sendMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
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
name|receivedMessageLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|RouteBuilder
name|routes
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Release latch when session logon events are received
comment|// We expect two events, one for the trader session and one for the market session
name|from
argument_list|(
literal|"quickfixj:examples/inprocess.cfg"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
name|QuickfixjEndpoint
operator|.
name|EVENT_CATEGORY_KEY
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|QuickfixjEventCategory
operator|.
name|SessionLogon
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|CountDownLatchDecrementer
argument_list|(
literal|"logon"
argument_list|,
name|logonLatch
argument_list|)
argument_list|)
expr_stmt|;
comment|// For all received messages, print the JSON-formatted message to stdout
name|from
argument_list|(
literal|"quickfixj:examples/inprocess.cfg"
argument_list|)
operator|.
name|filter
argument_list|(
name|PredicateBuilder
operator|.
name|or
argument_list|(
name|header
argument_list|(
name|QuickfixjEndpoint
operator|.
name|EVENT_CATEGORY_KEY
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|QuickfixjEventCategory
operator|.
name|AdminMessageReceived
argument_list|)
argument_list|,
name|header
argument_list|(
name|QuickfixjEndpoint
operator|.
name|EVENT_CATEGORY_KEY
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|QuickfixjEventCategory
operator|.
name|AppMessageReceived
argument_list|)
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|QuickfixjMessageJsonPrinter
argument_list|()
argument_list|)
expr_stmt|;
comment|// If the market session receives an email then release the latch
name|from
argument_list|(
literal|"quickfixj:examples/inprocess.cfg?sessionID=FIX.4.2:MARKET->TRADER"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
name|QuickfixjEndpoint
operator|.
name|MESSAGE_TYPE_KEY
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|MsgType
operator|.
name|EMAIL
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|CountDownLatchDecrementer
argument_list|(
literal|"message"
argument_list|,
name|receivedMessageLatch
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|routes
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting Camel context"
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|logonLatch
operator|.
name|await
argument_list|(
literal|5L
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Logon did not succeed"
argument_list|)
throw|;
block|}
name|String
name|marketUri
init|=
literal|"quickfixj:examples/inprocess.cfg?sessionID=FIX.4.2:TRADER->MARKET"
decl_stmt|;
name|Producer
name|producer
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|marketUri
argument_list|)
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|Email
name|email
init|=
name|TestSupport
operator|.
name|createEmailMessage
argument_list|(
literal|"Example"
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
if|if
condition|(
operator|!
name|receivedMessageLatch
operator|.
name|await
argument_list|(
literal|5L
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Message did not reach market"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Message received, shutting down Camel context"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Example complete"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

