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
name|ErrorHandlerBuilder
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

begin_comment
comment|/**  * Unit test for useOriginalnBody option on DeadLetterChannel  *  * @version   */
end_comment

begin_class
DECL|class|DeadLetterChannelUseOriginalInBodyTest
specifier|public
class|class
name|DeadLetterChannelUseOriginalInBodyTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testUseOriginalnBody ()
specifier|public
name|void
name|testUseOriginalnBody
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|dead
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
decl_stmt|;
name|dead
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testDoNotUseOriginalInBody ()
specifier|public
name|void
name|testDoNotUseOriginalInBody
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|dead
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
decl_stmt|;
name|dead
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:b"
argument_list|,
literal|"Hello"
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
comment|// will use original
name|ErrorHandlerBuilder
name|a
init|=
name|deadLetterChannel
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|logStackTrace
argument_list|(
literal|false
argument_list|)
operator|.
name|useOriginalMessage
argument_list|()
decl_stmt|;
comment|// will NOT use original
name|ErrorHandlerBuilder
name|b
init|=
name|deadLetterChannel
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|logStackTrace
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|a
argument_list|)
operator|.
name|setBody
argument_list|(
name|body
argument_list|()
operator|.
name|append
argument_list|(
literal|" World"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyThrowProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|b
argument_list|)
operator|.
name|setBody
argument_list|(
name|body
argument_list|()
operator|.
name|append
argument_list|(
literal|" World"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyThrowProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyThrowProcessor
specifier|public
specifier|static
class|class
name|MyThrowProcessor
implements|implements
name|Processor
block|{
DECL|method|MyThrowProcessor ()
specifier|public
name|MyThrowProcessor
parameter_list|()
block|{         }
DECL|method|process (Exchange exchange)
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
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
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
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

