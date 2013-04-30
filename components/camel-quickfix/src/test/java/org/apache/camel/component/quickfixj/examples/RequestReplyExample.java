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
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|URLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|CamelContext
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
name|Header
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
name|MessagePredicate
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
name|QuickfixjProducer
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
name|transform
operator|.
name|QuickfixjMessageJsonTransformer
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|IOHelper
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
name|FieldNotFound
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
name|AvgPx
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
name|CumQty
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|ExecID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|ExecTransType
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|ExecType
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|LeavesQty
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
name|OrdStatus
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|OrderID
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
name|fix42
operator|.
name|ExecutionReport
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|fix42
operator|.
name|OrderStatusRequest
import|;
end_import

begin_class
DECL|class|RequestReplyExample
specifier|public
class|class
name|RequestReplyExample
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
name|RequestReplyExample
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
name|RequestReplyExample
argument_list|()
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
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
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|String
name|orderStatusServiceUrl
init|=
literal|"http://localhost:9123/order/status"
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
comment|// Synchronize the logon so we don't start sending status requests too early
name|from
argument_list|(
literal|"quickfix:examples/inprocess.cfg?sessionID=FIX.4.2:TRADER->MARKET"
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
comment|// Incoming status requests are passed to the order status service and afterwards we print out that
comment|// order status being delivered using the json printer.
name|from
argument_list|(
literal|"quickfix:examples/inprocess.cfg?sessionID=FIX.4.2:MARKET->TRADER&exchangePattern=InOut"
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
name|ORDER_STATUS_REQUEST
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log://OrderStatusRequestLog?showAll=true&showOut=true&multiline=true"
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|MarketOrderStatusService
argument_list|()
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|QuickfixjMessageJsonPrinter
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:"
operator|+
name|orderStatusServiceUrl
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|OrderStatusRequestTransformer
argument_list|()
argument_list|)
operator|.
name|routingSlip
argument_list|(
name|method
argument_list|(
name|FixSessionRouter
operator|.
name|class
argument_list|,
literal|"route"
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|QuickfixjMessageJsonTransformer
argument_list|()
argument_list|,
literal|"transform(${body})"
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
comment|// Send a request to the order status web service.
comment|// Verify that the response is a JSON response.
name|URL
name|orderStatusUrl
init|=
operator|new
name|URL
argument_list|(
name|orderStatusServiceUrl
operator|+
literal|"?sessionID=FIX.4.2:TRADER->MARKET&orderID=abc"
argument_list|)
decl_stmt|;
name|URLConnection
name|connection
init|=
name|orderStatusUrl
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|BufferedReader
name|orderStatusReply
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|connection
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
name|orderStatusReply
operator|.
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|line
operator|.
name|equals
argument_list|(
literal|"\"message\": {"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Don't appear to be a JSON response"
argument_list|)
throw|;
block|}
else|else
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|line
operator|=
name|orderStatusReply
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Web reply:\n"
operator|+
name|sb
argument_list|)
expr_stmt|;
block|}
name|orderStatusReply
operator|.
name|close
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Shutting down Camel context"
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
DECL|class|OrderStatusRequestTransformer
specifier|public
specifier|static
class|class
name|OrderStatusRequestTransformer
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
name|OrderStatusRequestTransformer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|transform (Exchange exchange)
specifier|public
name|void
name|transform
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|FieldNotFound
block|{
comment|// For the reply take the reverse sessionID into the account, see org.apache.camel.component.quickfixj.MessagePredicate
name|String
name|requestSessionID
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"sessionID"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|replySessionID
init|=
literal|"FIX.4.2:MARKET->TRADER"
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Given the requestSessionID '{}' calculated the replySessionID as '{}'"
argument_list|,
name|requestSessionID
argument_list|,
name|replySessionID
argument_list|)
expr_stmt|;
name|String
name|orderID
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"orderID"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|OrderStatusRequest
name|request
init|=
operator|new
name|OrderStatusRequest
argument_list|(
operator|new
name|ClOrdID
argument_list|(
literal|"XYZ"
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
argument_list|)
decl_stmt|;
name|request
operator|.
name|set
argument_list|(
operator|new
name|OrderID
argument_list|(
name|orderID
argument_list|)
argument_list|)
expr_stmt|;
comment|// Look for a reply execution report back to the requester session
comment|// and having the requested OrderID. This is a loose correlation but the best
comment|// we can do with FIX 4.2. Newer versions of FIX have an optional explicit correlation field.
name|exchange
operator|.
name|setProperty
argument_list|(
name|QuickfixjProducer
operator|.
name|CORRELATION_CRITERIA_KEY
argument_list|,
operator|new
name|MessagePredicate
argument_list|(
operator|new
name|SessionID
argument_list|(
name|replySessionID
argument_list|)
argument_list|,
name|MsgType
operator|.
name|EXECUTION_REPORT
argument_list|)
operator|.
name|withField
argument_list|(
name|OrderID
operator|.
name|FIELD
argument_list|,
name|request
operator|.
name|getString
argument_list|(
name|OrderID
operator|.
name|FIELD
argument_list|)
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
name|request
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MarketOrderStatusService
specifier|public
specifier|static
class|class
name|MarketOrderStatusService
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
name|MarketOrderStatusService
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|getOrderStatus (OrderStatusRequest request)
specifier|public
name|ExecutionReport
name|getOrderStatus
parameter_list|(
name|OrderStatusRequest
name|request
parameter_list|)
throws|throws
name|FieldNotFound
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Received order status request for orderId="
operator|+
name|request
operator|.
name|getOrderID
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|ExecutionReport
argument_list|(
name|request
operator|.
name|getOrderID
argument_list|()
argument_list|,
operator|new
name|ExecID
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
operator|new
name|ExecTransType
argument_list|(
name|ExecTransType
operator|.
name|STATUS
argument_list|)
argument_list|,
operator|new
name|ExecType
argument_list|(
name|ExecType
operator|.
name|REJECTED
argument_list|)
argument_list|,
operator|new
name|OrdStatus
argument_list|(
name|OrdStatus
operator|.
name|REJECTED
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
name|LeavesQty
argument_list|(
literal|100
argument_list|)
argument_list|,
operator|new
name|CumQty
argument_list|(
literal|0
argument_list|)
argument_list|,
operator|new
name|AvgPx
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|class|FixSessionRouter
specifier|public
specifier|static
class|class
name|FixSessionRouter
block|{
DECL|method|route (@eaderR) String sessionID)
specifier|public
name|String
name|route
parameter_list|(
annotation|@
name|Header
argument_list|(
literal|"sessionID"
argument_list|)
name|String
name|sessionID
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"quickfix:examples/inprocess.cfg?sessionID=%s"
argument_list|,
name|sessionID
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

