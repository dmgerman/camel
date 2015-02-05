begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|builder
operator|.
name|ProcessorBuilder
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

begin_class
DECL|class|DeadLetterChannelExceptionCausePropagatedTest
specifier|public
class|class
name|DeadLetterChannelExceptionCausePropagatedTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|RUNTIME_EXCEPTION
specifier|protected
specifier|static
specifier|final
name|RuntimeException
name|RUNTIME_EXCEPTION
init|=
operator|new
name|RuntimeException
argument_list|(
literal|"Expected exception."
argument_list|)
decl_stmt|;
DECL|field|body
specifier|protected
name|String
name|body
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
DECL|method|testFirstFewAttemptsFail ()
specifier|public
name|void
name|testFirstFewAttemptsFail
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|failedEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:failed"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|successEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:success"
argument_list|)
decl_stmt|;
name|failedEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|failedEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|RUNTIME_EXCEPTION
argument_list|)
expr_stmt|;
name|failedEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|successEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|failedEndpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|RuntimeException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:failed"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
name|ProcessorBuilder
operator|.
name|throwException
argument_list|(
name|RUNTIME_EXCEPTION
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:success"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

