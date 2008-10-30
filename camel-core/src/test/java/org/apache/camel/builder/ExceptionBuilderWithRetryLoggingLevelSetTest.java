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
name|LoggingLevel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_comment
comment|/**  * Unit test to test exception configuration  */
end_comment

begin_class
DECL|class|ExceptionBuilderWithRetryLoggingLevelSetTest
specifier|public
class|class
name|ExceptionBuilderWithRetryLoggingLevelSetTest
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
DECL|field|logger
specifier|private
name|CustomLog
name|logger
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|logger
operator|=
operator|new
name|CustomLog
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|testExceptionIsLoggedWithCustomLogLevel ()
specifier|public
name|void
name|testExceptionIsLoggedWithCustomLogLevel
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
name|assertTrue
argument_list|(
name|logger
operator|.
name|loggedTrace
operator|&&
name|logger
operator|.
name|loggedFatal
argument_list|)
expr_stmt|;
block|}
DECL|method|testExceptionIsLoggedWithDefaultLevel ()
specifier|public
name|void
name|testExceptionIsLoggedWithDefaultLevel
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
name|assertTrue
argument_list|(
operator|!
name|logger
operator|.
name|loggedTrace
operator|&&
operator|!
name|logger
operator|.
name|loggedFatal
argument_list|)
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
argument_list|()
operator|.
name|log
argument_list|(
name|logger
argument_list|)
argument_list|)
expr_stmt|;
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
comment|// START SNIPPET: exceptionBuilder1
name|onException
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
operator|.
name|initialRedeliveryDelay
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
literal|10000L
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
name|retryAttemptedLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|TRACE
argument_list|)
operator|.
name|retriesExhaustedLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|FATAL
argument_list|)
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
literal|"Forced for testing - can not connect to remote server"
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
DECL|class|CustomLog
specifier|private
class|class
name|CustomLog
implements|implements
name|Log
block|{
DECL|field|loggedTrace
name|boolean
name|loggedTrace
init|=
literal|false
decl_stmt|;
DECL|field|loggedFatal
name|boolean
name|loggedFatal
init|=
literal|false
decl_stmt|;
DECL|method|isDebugEnabled ()
specifier|public
name|boolean
name|isDebugEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isErrorEnabled ()
specifier|public
name|boolean
name|isErrorEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isFatalEnabled ()
specifier|public
name|boolean
name|isFatalEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isInfoEnabled ()
specifier|public
name|boolean
name|isInfoEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isTraceEnabled ()
specifier|public
name|boolean
name|isTraceEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isWarnEnabled ()
specifier|public
name|boolean
name|isWarnEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|trace (Object message)
specifier|public
name|void
name|trace
parameter_list|(
name|Object
name|message
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|loggedTrace
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|trace (Object message, Throwable t)
specifier|public
name|void
name|trace
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|loggedTrace
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|debug (Object message)
specifier|public
name|void
name|debug
parameter_list|(
name|Object
name|message
parameter_list|)
block|{         }
DECL|method|debug (Object message, Throwable t)
specifier|public
name|void
name|debug
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{         }
DECL|method|info (Object message)
specifier|public
name|void
name|info
parameter_list|(
name|Object
name|message
parameter_list|)
block|{         }
DECL|method|info (Object message, Throwable t)
specifier|public
name|void
name|info
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{         }
DECL|method|warn (Object message)
specifier|public
name|void
name|warn
parameter_list|(
name|Object
name|message
parameter_list|)
block|{         }
DECL|method|warn (Object message, Throwable t)
specifier|public
name|void
name|warn
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{         }
DECL|method|error (Object message)
specifier|public
name|void
name|error
parameter_list|(
name|Object
name|message
parameter_list|)
block|{         }
DECL|method|error (Object message, Throwable t)
specifier|public
name|void
name|error
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{          }
DECL|method|fatal (Object message)
specifier|public
name|void
name|fatal
parameter_list|(
name|Object
name|message
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|loggedFatal
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|fatal (Object message, Throwable t)
specifier|public
name|void
name|fatal
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|loggedFatal
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

