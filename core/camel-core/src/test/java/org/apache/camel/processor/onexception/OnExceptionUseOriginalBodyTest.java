begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.onexception
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|onexception
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
name|CamelExchangeException
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test for useOriginalBody option on DeadLetterChannel  */
end_comment

begin_class
DECL|class|OnExceptionUseOriginalBodyTest
specifier|public
class|class
name|OnExceptionUseOriginalBodyTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testUseOriginalBody ()
specifier|public
name|void
name|testUseOriginalBody
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
annotation|@
name|Test
DECL|method|testDoNotUseOriginalBody ()
specifier|public
name|void
name|testDoNotUseOriginalBody
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|dead
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
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
comment|// will not use original exchange
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|disableRedelivery
argument_list|()
operator|.
name|logStackTrace
argument_list|(
literal|false
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|useOriginalMessage
argument_list|()
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
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
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
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
argument_list|(
literal|true
argument_list|)
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
DECL|field|camelException
specifier|private
name|boolean
name|camelException
decl_stmt|;
DECL|method|MyThrowProcessor ()
specifier|public
name|MyThrowProcessor
parameter_list|()
block|{         }
DECL|method|MyThrowProcessor (boolean camelException)
specifier|public
name|MyThrowProcessor
parameter_list|(
name|boolean
name|camelException
parameter_list|)
block|{
name|this
operator|.
name|camelException
operator|=
name|camelException
expr_stmt|;
block|}
annotation|@
name|Override
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
if|if
condition|(
name|camelException
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"I cannot do it"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced"
argument_list|)
throw|;
block|}
block|}
DECL|method|isCamelException ()
specifier|public
name|boolean
name|isCamelException
parameter_list|()
block|{
return|return
name|camelException
return|;
block|}
DECL|method|setCamelException (boolean camelException)
specifier|public
name|void
name|setCamelException
parameter_list|(
name|boolean
name|camelException
parameter_list|)
block|{
name|this
operator|.
name|camelException
operator|=
name|camelException
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

