begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfixj.examples.trading
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
operator|.
name|trading
package|;
end_package

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
name|ClOrdID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|HandlInst
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
name|OrdType
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|OrderQty
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|Price
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|Side
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|Symbol
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|TransactTime
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|fix42
operator|.
name|NewOrderSingle
import|;
end_import

begin_class
DECL|class|TradeExecutorExample
specifier|public
class|class
name|TradeExecutorExample
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
name|TradeExecutorExample
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
name|TradeExecutorExample
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
name|context
operator|.
name|addComponent
argument_list|(
literal|"trade-executor"
argument_list|,
operator|new
name|TradeExecutorComponent
argument_list|()
argument_list|)
expr_stmt|;
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
name|executionReportLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
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
comment|// We expect four logon events (four sessions)
name|from
argument_list|(
literal|"quickfix:examples/inprocess.cfg"
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
name|from
argument_list|(
literal|"quickfix:examples/inprocess.cfg?sessionID=FIX.4.2:MARKET->TRADER"
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
name|AppMessageReceived
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"trade-executor:market"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"trade-executor:market"
argument_list|)
operator|.
name|to
argument_list|(
literal|"quickfix:examples/inprocess.cfg"
argument_list|)
expr_stmt|;
comment|// Logger app messages as JSON
name|from
argument_list|(
literal|"quickfix:examples/inprocess.cfg"
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
name|AppMessageReceived
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
name|AppMessageSent
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
comment|// Release latch when trader receives execution report
name|from
argument_list|(
literal|"quickfix:examples/inprocess.cfg?sessionID=FIX.4.2:TRADER->MARKET"
argument_list|)
operator|.
name|filter
argument_list|(
name|PredicateBuilder
operator|.
name|and
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
name|AppMessageReceived
argument_list|)
argument_list|,
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
name|EXECUTION_REPORT
argument_list|)
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|CountDownLatchDecrementer
argument_list|(
literal|"execution report"
argument_list|,
name|executionReportLatch
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
comment|// This is not strictly necessary, but it prevents the need for session
comment|// synchronization due to app messages being sent before being logged on
if|if
condition|(
operator|!
name|logonLatch
operator|.
name|await
argument_list|(
literal|5
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
literal|"Logon did not complete"
argument_list|)
throw|;
block|}
name|String
name|gatewayUri
init|=
literal|"quickfix:examples/inprocess.cfg?sessionID=FIX.4.2:TRADER->MARKET"
decl_stmt|;
name|Endpoint
name|gatewayEndpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|gatewayUri
argument_list|)
decl_stmt|;
name|Producer
name|producer
init|=
name|gatewayEndpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Sending order"
argument_list|)
expr_stmt|;
name|NewOrderSingle
name|order
init|=
name|createNewOrderMessage
argument_list|()
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
name|order
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
name|executionReportLatch
operator|.
name|await
argument_list|(
literal|5
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
literal|"Did not receive execution reports"
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
literal|"Order execution example complete"
argument_list|)
expr_stmt|;
block|}
DECL|method|createNewOrderMessage ()
specifier|private
name|NewOrderSingle
name|createNewOrderMessage
parameter_list|()
block|{
name|NewOrderSingle
name|order
init|=
operator|new
name|NewOrderSingle
argument_list|(
operator|new
name|ClOrdID
argument_list|(
literal|"CLIENT_ORDER_ID"
argument_list|)
argument_list|,
operator|new
name|HandlInst
argument_list|(
literal|'1'
argument_list|)
argument_list|,
operator|new
name|Symbol
argument_list|(
literal|"GOOG"
argument_list|)
argument_list|,
operator|new
name|Side
argument_list|(
name|Side
operator|.
name|BUY
argument_list|)
argument_list|,
operator|new
name|TransactTime
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|,
operator|new
name|OrdType
argument_list|(
name|OrdType
operator|.
name|LIMIT
argument_list|)
argument_list|)
decl_stmt|;
name|order
operator|.
name|set
argument_list|(
operator|new
name|OrderQty
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|order
operator|.
name|set
argument_list|(
operator|new
name|Price
argument_list|(
literal|300.00
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|order
return|;
block|}
block|}
end_class

end_unit

