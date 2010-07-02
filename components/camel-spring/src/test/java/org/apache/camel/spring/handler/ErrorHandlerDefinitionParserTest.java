begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.handler
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|handler
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|LoggingLevel
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
name|DeadLetterChannelBuilder
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
name|DefaultErrorHandlerBuilder
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
name|LoggingErrorHandlerBuilder
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
name|processor
operator|.
name|RedeliveryPolicy
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
name|spring
operator|.
name|CamelContextFactoryBean
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
name|spring
operator|.
name|spi
operator|.
name|TransactionErrorHandlerBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|ErrorHandlerDefinitionParserTest
specifier|public
class|class
name|ErrorHandlerDefinitionParserTest
extends|extends
name|TestCase
block|{
DECL|field|ctx
specifier|protected
name|ClassPathXmlApplicationContext
name|ctx
decl_stmt|;
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|ctx
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/handler/ErrorHandlerDefinitionParser.xml"
argument_list|)
expr_stmt|;
block|}
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoggingErrorHandler ()
specifier|public
name|void
name|testLoggingErrorHandler
parameter_list|()
block|{
name|LoggingErrorHandlerBuilder
name|errorHandler
init|=
operator|(
name|LoggingErrorHandlerBuilder
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"loggingErrorHandler"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The loglevel should be INFO"
argument_list|,
name|LoggingLevel
operator|.
name|INFO
argument_list|,
name|errorHandler
operator|.
name|getLevel
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDefaultErrorHandler ()
specifier|public
name|void
name|testDefaultErrorHandler
parameter_list|()
block|{
name|DefaultErrorHandlerBuilder
name|errorHandler
init|=
operator|(
name|DefaultErrorHandlerBuilder
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"defaultErrorHandler"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
name|RedeliveryPolicy
name|policy
init|=
name|errorHandler
operator|.
name|getRedeliveryPolicy
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|policy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong maximumRedeliveries"
argument_list|,
literal|2
argument_list|,
name|policy
operator|.
name|getMaximumRedeliveries
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong redeliveryDelay"
argument_list|,
literal|0
argument_list|,
name|policy
operator|.
name|getRedeliveryDelay
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong logStackTrace"
argument_list|,
literal|false
argument_list|,
name|policy
operator|.
name|isLogStackTrace
argument_list|()
argument_list|)
expr_stmt|;
name|errorHandler
operator|=
operator|(
name|DefaultErrorHandlerBuilder
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"errorHandler"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransactionErrorHandler ()
specifier|public
name|void
name|testTransactionErrorHandler
parameter_list|()
block|{
name|TransactionErrorHandlerBuilder
name|errorHandler
init|=
operator|(
name|TransactionErrorHandlerBuilder
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"transactionErrorHandler"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|errorHandler
operator|.
name|getTransactionTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|errorHandler
operator|.
name|getOnRedelivery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"It should be MyErrorProcessor"
argument_list|,
name|processor
operator|instanceof
name|MyErrorProcessor
argument_list|)
expr_stmt|;
block|}
DECL|method|testTXErrorHandler ()
specifier|public
name|void
name|testTXErrorHandler
parameter_list|()
block|{
name|TransactionErrorHandlerBuilder
name|errorHandler
init|=
operator|(
name|TransactionErrorHandlerBuilder
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"txEH"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|errorHandler
operator|.
name|getTransactionTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDeadLetterErrorHandler ()
specifier|public
name|void
name|testDeadLetterErrorHandler
parameter_list|()
block|{
name|DeadLetterChannelBuilder
name|errorHandler
init|=
operator|(
name|DeadLetterChannelBuilder
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"deadLetterErrorHandler"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get wrong deadletteruri"
argument_list|,
literal|"log:dead"
argument_list|,
name|errorHandler
operator|.
name|getDeadLetterUri
argument_list|()
argument_list|)
expr_stmt|;
name|RedeliveryPolicy
name|policy
init|=
name|errorHandler
operator|.
name|getRedeliveryPolicy
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|policy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong maximumRedeliveries"
argument_list|,
literal|2
argument_list|,
name|policy
operator|.
name|getMaximumRedeliveries
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong redeliveryDelay"
argument_list|,
literal|1000
argument_list|,
name|policy
operator|.
name|getRedeliveryDelay
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong logStackTrace"
argument_list|,
literal|true
argument_list|,
name|policy
operator|.
name|isLogHandled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong asyncRedeliveryDelayed"
argument_list|,
literal|true
argument_list|,
name|policy
operator|.
name|isAsyncDelayedRedelivery
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testErrorHandlerInsideCamelContext ()
specifier|public
name|void
name|testErrorHandlerInsideCamelContext
parameter_list|()
block|{
name|CamelContextFactoryBean
name|factoryBean
init|=
operator|(
name|CamelContextFactoryBean
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"&camel"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|factoryBean
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong ErrorHandlerRef"
argument_list|,
literal|"noErrorHandler"
argument_list|,
name|factoryBean
operator|.
name|getErrorHandlerRef
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

