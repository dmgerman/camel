begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.routingslip
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|routingslip
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
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|jndi
operator|.
name|JndiContext
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

begin_class
DECL|class|RoutingSlipWithExceptionTest
specifier|public
class|class
name|RoutingSlipWithExceptionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|ANSWER
specifier|protected
specifier|static
specifier|final
name|String
name|ANSWER
init|=
literal|"answer"
decl_stmt|;
DECL|field|ROUTING_SLIP_HEADER
specifier|protected
specifier|static
specifier|final
name|String
name|ROUTING_SLIP_HEADER
init|=
literal|"destinations"
decl_stmt|;
DECL|field|myBean
specifier|protected
name|MyBean
name|myBean
init|=
operator|new
name|MyBean
argument_list|()
decl_stmt|;
DECL|field|endEndpoint
specifier|private
name|MockEndpoint
name|endEndpoint
decl_stmt|;
DECL|field|exceptionEndpoint
specifier|private
name|MockEndpoint
name|exceptionEndpoint
decl_stmt|;
DECL|field|exceptionSettingEndpoint
specifier|private
name|MockEndpoint
name|exceptionSettingEndpoint
decl_stmt|;
DECL|field|aEndpoint
specifier|private
name|MockEndpoint
name|aEndpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|testNoException ()
specifier|public
name|void
name|testNoException
parameter_list|()
throws|throws
name|Exception
block|{
name|endEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|exceptionEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|aEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendRoutingSlipWithNoExceptionThrowingComponent
argument_list|()
expr_stmt|;
name|assertEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWithExceptionThrowingComponentFirstInList ()
specifier|public
name|void
name|testWithExceptionThrowingComponentFirstInList
parameter_list|()
throws|throws
name|Exception
block|{
name|endEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|exceptionEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|aEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sendRoutingSlipWithExceptionThrowingComponentFirstInList
argument_list|()
expr_stmt|;
name|assertEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWithExceptionThrowingComponentSecondInList ()
specifier|public
name|void
name|testWithExceptionThrowingComponentSecondInList
parameter_list|()
throws|throws
name|Exception
block|{
name|endEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|exceptionEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|aEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendRoutingSlipWithExceptionThrowingComponentSecondInList
argument_list|()
expr_stmt|;
name|assertEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWithExceptionSettingComponentFirstInList ()
specifier|public
name|void
name|testWithExceptionSettingComponentFirstInList
parameter_list|()
throws|throws
name|Exception
block|{
name|endEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|exceptionEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|aEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sendRoutingSlipWithExceptionSettingComponentFirstInList
argument_list|()
expr_stmt|;
name|assertEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWithExceptionSettingComponentSecondInList ()
specifier|public
name|void
name|testWithExceptionSettingComponentSecondInList
parameter_list|()
throws|throws
name|Exception
block|{
name|endEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|exceptionEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|aEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendRoutingSlipWithExceptionSettingComponentSecondInList
argument_list|()
expr_stmt|;
name|assertEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|assertEndpointsSatisfied ()
specifier|private
name|void
name|assertEndpointsSatisfied
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|endEndpoint
argument_list|,
name|exceptionEndpoint
argument_list|,
name|aEndpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|sendRoutingSlipWithExceptionThrowingComponentFirstInList ()
specifier|protected
name|void
name|sendRoutingSlipWithExceptionThrowingComponentFirstInList
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|ANSWER
argument_list|,
name|ROUTING_SLIP_HEADER
argument_list|,
literal|"bean:myBean?method=throwException,mock:a"
argument_list|)
expr_stmt|;
block|}
DECL|method|sendRoutingSlipWithExceptionThrowingComponentSecondInList ()
specifier|protected
name|void
name|sendRoutingSlipWithExceptionThrowingComponentSecondInList
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|ANSWER
argument_list|,
name|ROUTING_SLIP_HEADER
argument_list|,
literal|"mock:a,bean:myBean?method=throwException"
argument_list|)
expr_stmt|;
block|}
DECL|method|sendRoutingSlipWithNoExceptionThrowingComponent ()
specifier|protected
name|void
name|sendRoutingSlipWithNoExceptionThrowingComponent
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|ANSWER
argument_list|,
name|ROUTING_SLIP_HEADER
argument_list|,
literal|"mock:a"
argument_list|)
expr_stmt|;
block|}
DECL|method|sendRoutingSlipWithExceptionSettingComponentFirstInList ()
specifier|protected
name|void
name|sendRoutingSlipWithExceptionSettingComponentFirstInList
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|ANSWER
argument_list|,
name|ROUTING_SLIP_HEADER
argument_list|,
literal|"mock:exceptionSetting,mock:a"
argument_list|)
expr_stmt|;
block|}
DECL|method|sendRoutingSlipWithExceptionSettingComponentSecondInList ()
specifier|protected
name|void
name|sendRoutingSlipWithExceptionSettingComponentSecondInList
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|ANSWER
argument_list|,
name|ROUTING_SLIP_HEADER
argument_list|,
literal|"mock:a,mock:exceptionSetting"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|endEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:noexception"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|exceptionEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:exception"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|exceptionSettingEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:exceptionSetting"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|aEndpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:a"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|exceptionSettingEndpoint
operator|.
name|whenAnyExchangeReceived
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
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|Exception
argument_list|(
literal|"Throw me!"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Object
name|lookedUpBean
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
literal|"myBean"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"Lookup of 'myBean' should return same object!"
argument_list|,
name|myBean
argument_list|,
name|lookedUpBean
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiContext
name|answer
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
name|myBean
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|routingSlip
argument_list|(
name|header
argument_list|(
name|ROUTING_SLIP_HEADER
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:noexception"
argument_list|)
operator|.
name|doCatch
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:exception"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|method|MyBean ()
specifier|public
name|MyBean
parameter_list|()
block|{         }
DECL|method|throwException ()
specifier|public
name|void
name|throwException
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Throw me!"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

