begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
package|;
end_package

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
name|spi
operator|.
name|CamelEvent
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
name|CamelEvent
operator|.
name|ExchangeSentEvent
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
name|DefaultExchange
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
name|EventNotifierSupport
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

begin_class
DECL|class|SentExchangeEventNotifierIssueTest
specifier|public
class|class
name|SentExchangeEventNotifierIssueTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|notifier
specifier|private
name|MyNotifier
name|notifier
init|=
operator|new
name|MyNotifier
argument_list|()
decl_stmt|;
DECL|class|MyNotifier
specifier|private
class|class
name|MyNotifier
extends|extends
name|EventNotifierSupport
block|{
DECL|field|counter
specifier|private
name|int
name|counter
decl_stmt|;
annotation|@
name|Override
DECL|method|notify (CamelEvent event)
specifier|public
name|void
name|notify
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
throws|throws
name|Exception
block|{
name|counter
operator|++
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isEnabled (CamelEvent event)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
block|{
return|return
name|event
operator|instanceof
name|ExchangeSentEvent
return|;
block|}
DECL|method|getCounter ()
specifier|public
name|int
name|getCounter
parameter_list|()
block|{
return|return
name|counter
return|;
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|counter
operator|=
literal|0
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|init
argument_list|()
expr_stmt|;
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
name|notifier
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testExchangeSentNotifier ()
specifier|public
name|void
name|testExchangeSentNotifier
parameter_list|()
throws|throws
name|Exception
block|{
name|notifier
operator|.
name|reset
argument_list|()
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"I was here"
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// should only be one event
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|notifier
operator|.
name|getCounter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExchangeSentNotifierExchange ()
specifier|public
name|void
name|testExchangeSentNotifierExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|notifier
operator|.
name|reset
argument_list|()
expr_stmt|;
name|Exchange
name|out
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"I was here"
argument_list|,
name|out
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
comment|// should only be one event
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|notifier
operator|.
name|getCounter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExchangeSentNotifierManualExchange ()
specifier|public
name|void
name|testExchangeSentNotifierManualExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|notifier
operator|.
name|reset
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
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
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"I was here"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
comment|// should only be one event
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|notifier
operator|.
name|getCounter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"I was here"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

