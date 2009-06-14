begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ConnectException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyManagementException
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
name|RuntimeCamelException
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
comment|/**  * Unit test to test exception configuration  */
end_comment

begin_class
DECL|class|ExceptionBuilderTest
specifier|public
class|class
name|ExceptionBuilderTest
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
DECL|field|RESULT_QUEUE
specifier|private
specifier|static
specifier|final
name|String
name|RESULT_QUEUE
init|=
literal|"mock:result"
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
DECL|field|BUSINESS_ERROR_QUEUE
specifier|private
specifier|static
specifier|final
name|String
name|BUSINESS_ERROR_QUEUE
init|=
literal|"mock:badBusiness"
decl_stmt|;
DECL|field|SECURITY_ERROR_QUEUE
specifier|private
specifier|static
specifier|final
name|String
name|SECURITY_ERROR_QUEUE
init|=
literal|"mock:securityError"
decl_stmt|;
DECL|method|testNPE ()
specifier|public
name|void
name|testNPE
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|RESULT_QUEUE
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
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
literal|"Damm a NPE"
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Hello NPE"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a NullPointerException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|NullPointerException
argument_list|)
expr_stmt|;
comment|// expected
block|}
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|result
argument_list|,
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testIOException ()
specifier|public
name|void
name|testIOException
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|RESULT_QUEUE
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
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
literal|"Damm somekind of IO exception"
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Hello IO"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a IOException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IOException
argument_list|)
expr_stmt|;
comment|// expected
block|}
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|result
argument_list|,
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testException ()
specifier|public
name|void
name|testException
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|RESULT_QUEUE
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
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
literal|"Damm just exception"
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Hello Exception"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a Exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|Exception
argument_list|)
expr_stmt|;
comment|// expected
block|}
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|result
argument_list|,
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testMyBusinessException ()
specifier|public
name|void
name|testMyBusinessException
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|RESULT_QUEUE
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
name|BUSINESS_ERROR_QUEUE
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
literal|"Damm my business is not going to well"
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Hello business"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a MyBusinessException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|MyBusinessException
argument_list|)
expr_stmt|;
comment|// expected
block|}
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|result
argument_list|,
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testSecurityConfiguredWithTwoExceptions ()
specifier|public
name|void
name|testSecurityConfiguredWithTwoExceptions
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test that we also handles a configuration with 2 or more exceptions
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|RESULT_QUEUE
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
name|SECURITY_ERROR_QUEUE
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
literal|"Damm some security error"
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"I am not allowed to do this"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a GeneralSecurityException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|GeneralSecurityException
argument_list|)
expr_stmt|;
comment|// expected
block|}
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|result
argument_list|,
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|method|testSecurityConfiguredWithExceptionList ()
specifier|public
name|void
name|testSecurityConfiguredWithExceptionList
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test that we also handles a configuration with a list of exceptions
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|RESULT_QUEUE
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
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
literal|"Damm some access error"
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"I am not allowed to access this"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a GeneralSecurityException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalAccessException
argument_list|)
expr_stmt|;
comment|// expected
block|}
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|result
argument_list|,
name|mock
argument_list|)
expr_stmt|;
block|}
DECL|class|MyBaseBusinessException
specifier|public
specifier|static
class|class
name|MyBaseBusinessException
extends|extends
name|Exception
block|{     }
DECL|class|MyBusinessException
specifier|public
specifier|static
class|class
name|MyBusinessException
extends|extends
name|MyBaseBusinessException
block|{     }
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliverDelay
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
comment|// START SNIPPET: exceptionBuilder1
name|onException
argument_list|(
name|NullPointerException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|setHeader
argument_list|(
name|MESSAGE_INFO
argument_list|,
name|constant
argument_list|(
literal|"Damm a NPE"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|ERROR_QUEUE
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|1000L
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
operator|.
name|maximumRedeliveryDelay
argument_list|(
literal|30
operator|*
literal|1000L
argument_list|)
operator|.
name|backOffMultiplier
argument_list|(
literal|1.0
argument_list|)
operator|.
name|useExponentialBackOff
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MESSAGE_INFO
argument_list|,
name|constant
argument_list|(
literal|"Damm somekind of IO exception"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|ERROR_QUEUE
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|1000L
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|setHeader
argument_list|(
name|MESSAGE_INFO
argument_list|,
name|constant
argument_list|(
literal|"Damm just exception"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|ERROR_QUEUE
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|MyBaseBusinessException
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|1000L
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
name|constant
argument_list|(
literal|"Damm my business is not going to well"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|BUSINESS_ERROR_QUEUE
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|GeneralSecurityException
operator|.
name|class
argument_list|,
name|KeyException
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
name|constant
argument_list|(
literal|"Damm some security error"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|SECURITY_ERROR_QUEUE
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|InstantiationException
operator|.
name|class
argument_list|,
name|IllegalAccessException
operator|.
name|class
argument_list|,
name|ClassNotFoundException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|setHeader
argument_list|(
name|MESSAGE_INFO
argument_list|,
name|constant
argument_list|(
literal|"Damm some access error"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|ERROR_QUEUE
argument_list|)
expr_stmt|;
comment|// END SNIPPET: exceptionBuilder1
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
literal|"Hello NPE"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
elseif|else
if|if
condition|(
literal|"Hello IO"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ConnectException
argument_list|(
literal|"Forced for testing - cannot connect to remote server"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
literal|"Hello Exception"
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
elseif|else
if|if
condition|(
literal|"Hello business"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|MyBusinessException
argument_list|()
throw|;
block|}
elseif|else
if|if
condition|(
literal|"I am not allowed to do this"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|KeyManagementException
argument_list|()
throw|;
block|}
elseif|else
if|if
condition|(
literal|"I am not allowed to access this"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalAccessException
argument_list|()
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

