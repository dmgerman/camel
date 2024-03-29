begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.soroushbot.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|soroushbot
operator|.
name|component
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
name|RoutesBuilder
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
name|component
operator|.
name|soroushbot
operator|.
name|models
operator|.
name|SoroushAction
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
name|soroushbot
operator|.
name|models
operator|.
name|SoroushMessage
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
name|soroushbot
operator|.
name|support
operator|.
name|SoroushBotTestSupport
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
name|soroushbot
operator|.
name|utils
operator|.
name|CongestionException
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
DECL|class|ConsumerExceptionHandledWithErrorHandlerTest
specifier|public
class|class
name|ConsumerExceptionHandledWithErrorHandlerTest
extends|extends
name|SoroushBotTestSupport
block|{
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|onException
argument_list|(
name|CongestionException
operator|.
name|class
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|SoroushMessage
name|originalMessage
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"OriginalMessage"
argument_list|,
name|SoroushMessage
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|originalMessage
operator|==
literal|null
operator|||
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|SoroushMessage
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ROUTE_STOP
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:exceptionRoute"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"soroush://"
operator|+
name|SoroushAction
operator|.
name|getMessage
operator|+
literal|"/7?concurrentConsumers=2&queueCapacityPerThread=1&bridgeErrorHandler=true"
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:mainRoute"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|checkIfMessageGoesToExceptionRoute ()
specifier|public
name|void
name|checkIfMessageGoesToExceptionRoute
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|exceptionEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:exceptionRoute"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mainEndPoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:mainRoute"
argument_list|)
decl_stmt|;
name|exceptionEndpoint
operator|.
name|setExpectedCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mainEndPoint
operator|.
name|setExpectedCount
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|exceptionEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mainEndPoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

