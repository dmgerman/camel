begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.exceptionpolicy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|exceptionpolicy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|CamelException
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
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|ExceptionType
import|;
end_import

begin_comment
comment|/**  * Unit test with a user plugged in exception policy to use instead of default.  */
end_comment

begin_class
DECL|class|CustomExceptionPolicyStrategyTest
specifier|public
class|class
name|CustomExceptionPolicyStrategyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|MESSAGE_INFO
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE_INFO
init|=
literal|"messageInfo"
decl_stmt|;
DECL|field|ERROR_QUEUE
specifier|private
specifier|static
specifier|final
name|String
name|ERROR_QUEUE
init|=
literal|"mock:error"
decl_stmt|;
DECL|class|MyPolicyException
specifier|public
specifier|static
class|class
name|MyPolicyException
extends|extends
name|Exception
block|{     }
comment|// START SNIPPET e2
DECL|class|MyPolicy
specifier|public
specifier|static
class|class
name|MyPolicy
implements|implements
name|ExceptionPolicyStrategy
block|{
DECL|method|getExceptionPolicy (Map<Class, ExceptionType> exceptionPolicices, Exchange exchange, Throwable exception)
specifier|public
name|ExceptionType
name|getExceptionPolicy
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|,
name|ExceptionType
argument_list|>
name|exceptionPolicices
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
block|{
comment|// This is just an example that always forces the exception type configured
comment|// with MyPolicyException to win.
return|return
name|exceptionPolicices
operator|.
name|get
argument_list|(
name|MyPolicyException
operator|.
name|class
argument_list|)
return|;
block|}
block|}
comment|// END SNIPPET e2
DECL|method|testCustomPolicy ()
specifier|public
name|void
name|testCustomPolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
name|ERROR_QUEUE
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|MESSAGE_INFO
argument_list|,
literal|"Damm my policy exception"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
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
comment|// START SNIPPET e1
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// configure the error handler to use my policy instead of the default from Camel
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|()
operator|.
name|exceptionPolicyStrategy
argument_list|(
operator|new
name|MyPolicy
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|exception
argument_list|(
name|MyPolicyException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|1
argument_list|)
operator|.
name|setHeader
argument_list|(
name|MESSAGE_INFO
argument_list|,
literal|"Damm my policy exception"
argument_list|)
operator|.
name|to
argument_list|(
name|ERROR_QUEUE
argument_list|)
expr_stmt|;
name|exception
argument_list|(
name|CamelException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
operator|.
name|setHeader
argument_list|(
name|MESSAGE_INFO
argument_list|,
literal|"Damm a Camel exception"
argument_list|)
operator|.
name|to
argument_list|(
name|ERROR_QUEUE
argument_list|)
expr_stmt|;
comment|// END SNIPPET e1
name|from
argument_list|(
literal|"direct:a"
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
name|String
name|s
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
literal|"Hello Camel"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Forced for testing"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|exchange
operator|.
name|getOut
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

