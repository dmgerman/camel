begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.log
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|log
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
name|Callable
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
name|ExecutionException
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
name|FutureTask
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
name|ContextTestSupport
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
name|support
operator|.
name|processor
operator|.
name|DefaultExchangeFormatter
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
comment|/**  * Logger formatter test.  */
end_comment

begin_class
DECL|class|DefaultExchangeFormatterTest
specifier|public
class|class
name|DefaultExchangeFormatterTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSendMessageToLogDefault ()
specifier|public
name|void
name|testSendMessageToLogDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMessageToLogAllOff ()
specifier|public
name|void
name|testSendMessageToLogAllOff
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showBody=false&showBodyType=false&showExchangePattern=false"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMessageToLogSingleOptions ()
specifier|public
name|void
name|testSendMessageToLogSingleOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showExchangeId=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showExchangePattern=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showExchangePattern=false"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showProperties=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showHeaders=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showBodyType=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showBody=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showAll=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showFuture=true"
argument_list|,
operator|new
name|MyFuture
argument_list|(
operator|new
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|"foo"
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showFuture=false"
argument_list|,
operator|new
name|MyFuture
argument_list|(
operator|new
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|"bar"
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMessageToLogMultiOptions ()
specifier|public
name|void
name|testSendMessageToLogMultiOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showHeaders=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showProperties=true&showHeaders=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMessageToLogShowFalse ()
specifier|public
name|void
name|testSendMessageToLogShowFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?showBodyType=false"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMessageToLogMultiLine ()
specifier|public
name|void
name|testSendMessageToLogMultiLine
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?multiline=true"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendByteArrayMessageToLogDefault ()
specifier|public
name|void
name|testSendByteArrayMessageToLogDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST"
argument_list|,
literal|"Hello World"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendMessageToLogMaxChars ()
specifier|public
name|void
name|testSendMessageToLogMaxChars
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST"
argument_list|,
literal|"Hello World this is a very long string that is NOT going to be chopped by maxchars"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?maxChars=50"
argument_list|,
literal|"Hello World this is a very long string that is going to be chopped by maxchars"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"log:org.apache.camel.TEST?maxChars=50&showAll=true&multiline=true"
argument_list|,
literal|"Hello World this is a very long string that is going to be chopped by maxchars"
argument_list|)
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
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"log:org.apache.camel.TEST?showException=true"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendCaughtExchangeWithException ()
specifier|public
name|void
name|testSendCaughtExchangeWithException
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"log:org.apache.camel.TEST?showCaughtException=true"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
operator|new
name|IllegalArgumentException
argument_list|(
literal|"I am caught"
argument_list|)
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendCaughtExchangeWithExceptionAndMultiline ()
specifier|public
name|void
name|testSendCaughtExchangeWithExceptionAndMultiline
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"log:org.apache.camel.TEST?showCaughtException=true&multiline=true"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
operator|new
name|IllegalArgumentException
argument_list|(
literal|"I am caught"
argument_list|)
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendExchangeWithExceptionAndStackTrace ()
specifier|public
name|void
name|testSendExchangeWithExceptionAndStackTrace
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"log:org.apache.camel.TEST?showException=true&showStackTrace=true"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendCaughtExchangeWithExceptionAndStackTrace ()
specifier|public
name|void
name|testSendCaughtExchangeWithExceptionAndStackTrace
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"log:org.apache.camel.TEST?showCaughtException=true&showStackTrace=true"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
operator|new
name|IllegalArgumentException
argument_list|(
literal|"I am caught"
argument_list|)
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfiguration ()
specifier|public
name|void
name|testConfiguration
parameter_list|()
block|{
name|DefaultExchangeFormatter
name|formatter
init|=
operator|new
name|DefaultExchangeFormatter
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|formatter
operator|.
name|isShowExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|formatter
operator|.
name|isShowProperties
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|formatter
operator|.
name|isShowHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|formatter
operator|.
name|isShowBodyType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|formatter
operator|.
name|isShowBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|formatter
operator|.
name|isShowException
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|formatter
operator|.
name|isShowCaughtException
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|formatter
operator|.
name|isShowStackTrace
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|formatter
operator|.
name|isShowAll
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|formatter
operator|.
name|isMultiline
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10000
argument_list|,
name|formatter
operator|.
name|getMaxChars
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyFuture
specifier|private
specifier|static
class|class
name|MyFuture
extends|extends
name|FutureTask
argument_list|<
name|String
argument_list|>
block|{
DECL|method|MyFuture (Callable<String> callable)
name|MyFuture
parameter_list|(
name|Callable
argument_list|<
name|String
argument_list|>
name|callable
parameter_list|)
block|{
name|super
argument_list|(
name|callable
argument_list|)
expr_stmt|;
block|}
DECL|method|MyFuture (Runnable runnable, String o)
name|MyFuture
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|String
name|o
parameter_list|)
block|{
name|super
argument_list|(
name|runnable
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isDone ()
specifier|public
name|boolean
name|isDone
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
return|return
literal|"foo"
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ThisIsMyFuture"
return|;
block|}
block|}
block|}
end_class

end_unit

