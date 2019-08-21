begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|junit
operator|.
name|Test
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

begin_class
DECL|class|RedeliveryErrorHandlerNonBlockedRedeliveryHeaderTest
specifier|public
class|class
name|RedeliveryErrorHandlerNonBlockedRedeliveryHeaderTest
extends|extends
name|ContextTestSupport
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
name|RedeliveryErrorHandlerNonBlockedRedeliveryHeaderTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|attempt
specifier|private
specifier|static
specifier|volatile
name|int
name|attempt
decl_stmt|;
annotation|@
name|Test
DECL|method|testRedelivery ()
specifier|public
name|void
name|testRedelivery
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|before
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|before
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Hello Camel"
argument_list|)
expr_stmt|;
comment|// we use NON blocked redelivery delay so the messages arrive which
comment|// completes first
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Camel"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"seda:start"
argument_list|,
literal|"World"
argument_list|,
name|Exchange
operator|.
name|REDELIVERY_DELAY
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"seda:start"
argument_list|,
literal|"Camel"
argument_list|,
name|Exchange
operator|.
name|REDELIVERY_DELAY
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
comment|// use async delayed which means non blocking
comment|// set a high default value which we override by the headers so
comment|// this test can complete in due time
name|errorHandler
argument_list|(
name|defaultErrorHandler
argument_list|()
operator|.
name|maximumRedeliveries
argument_list|(
literal|5
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|10000
argument_list|)
operator|.
name|asyncDelayedRedelivery
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:before"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:before"
argument_list|)
operator|.
name|process
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Processing at attempt {} {}"
argument_list|,
name|attempt
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|.
name|contains
argument_list|(
literal|"World"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|++
name|attempt
operator|<=
literal|2
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Processing failed will thrown an exception"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
throw|;
block|}
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello "
operator|+
name|body
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Processing at attempt {} complete {}"
argument_list|,
name|attempt
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:after"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

