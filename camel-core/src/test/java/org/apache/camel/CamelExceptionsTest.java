begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|ExpressionBuilder
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
name|impl
operator|.
name|DefaultExchange
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|CamelExceptionsTest
specifier|public
class|class
name|CamelExceptionsTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testExpectedBodyTypeException ()
specifier|public
name|void
name|testExpectedBodyTypeException
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ExpectedBodyTypeException
name|e
init|=
operator|new
name|ExpectedBodyTypeException
argument_list|(
name|exchange
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|e
operator|.
name|getExpectedBodyType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExpressionEvaluationException ()
specifier|public
name|void
name|testExpressionEvaluationException
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ExpressionEvaluationException
name|e
init|=
operator|new
name|ExpressionEvaluationException
argument_list|(
name|exp
argument_list|,
name|exchange
argument_list|,
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exp
argument_list|,
name|e
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFailedToCreateConsumerException ()
specifier|public
name|void
name|testFailedToCreateConsumerException
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|FailedToCreateConsumerException
name|e
init|=
operator|new
name|FailedToCreateConsumerException
argument_list|(
name|endpoint
argument_list|,
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|e
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFailedToCreateProducerException ()
specifier|public
name|void
name|testFailedToCreateProducerException
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
decl_stmt|;
name|FailedToCreateProducerException
name|e
init|=
operator|new
name|FailedToCreateProducerException
argument_list|(
name|endpoint
argument_list|,
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|e
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalidPayloadRuntimeException ()
specifier|public
name|void
name|testInvalidPayloadRuntimeException
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|InvalidPayloadRuntimeException
name|e
init|=
operator|new
name|InvalidPayloadRuntimeException
argument_list|(
name|exchange
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|e
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|InvalidPayloadRuntimeException
name|e2
init|=
operator|new
name|InvalidPayloadRuntimeException
argument_list|(
name|exchange
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e2
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|e2
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|InvalidPayloadRuntimeException
name|e3
init|=
operator|new
name|InvalidPayloadRuntimeException
argument_list|(
name|exchange
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e3
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|e3
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRuntimeTransformException ()
specifier|public
name|void
name|testRuntimeTransformException
parameter_list|()
block|{
name|RuntimeTransformException
name|e
init|=
operator|new
name|RuntimeTransformException
argument_list|(
literal|"Forced"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Forced"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|RuntimeTransformException
name|e2
init|=
operator|new
name|RuntimeTransformException
argument_list|(
literal|"Forced"
argument_list|,
operator|new
name|IllegalAccessException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Forced"
argument_list|,
name|e2
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e2
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|RuntimeTransformException
name|e3
init|=
operator|new
name|RuntimeTransformException
argument_list|(
operator|new
name|IllegalAccessException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.IllegalAccessException: Damn"
argument_list|,
name|e3
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e3
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRuntimeExpressionException ()
specifier|public
name|void
name|testRuntimeExpressionException
parameter_list|()
block|{
name|RuntimeExpressionException
name|e
init|=
operator|new
name|RuntimeExpressionException
argument_list|(
literal|"Forced"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Forced"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|RuntimeExpressionException
name|e2
init|=
operator|new
name|RuntimeExpressionException
argument_list|(
literal|"Forced"
argument_list|,
operator|new
name|IllegalAccessException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Forced"
argument_list|,
name|e2
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e2
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|RuntimeExpressionException
name|e3
init|=
operator|new
name|RuntimeExpressionException
argument_list|(
operator|new
name|IllegalAccessException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.IllegalAccessException: Damn"
argument_list|,
name|e3
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e3
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRollbackExchangeException ()
specifier|public
name|void
name|testRollbackExchangeException
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|RollbackExchangeException
name|e
init|=
operator|new
name|RollbackExchangeException
argument_list|(
name|exchange
argument_list|,
operator|new
name|IllegalAccessException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|RollbackExchangeException
name|e2
init|=
operator|new
name|RollbackExchangeException
argument_list|(
literal|"Forced"
argument_list|,
name|exchange
argument_list|,
operator|new
name|IllegalAccessException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e2
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e2
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testValidationException ()
specifier|public
name|void
name|testValidationException
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ValidationException
name|e
init|=
operator|new
name|ValidationException
argument_list|(
name|exchange
argument_list|,
literal|"Forced"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|ValidationException
name|e2
init|=
operator|new
name|ValidationException
argument_list|(
literal|"Forced"
argument_list|,
name|exchange
argument_list|,
operator|new
name|IllegalAccessException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e2
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e2
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoSuchBeanException ()
specifier|public
name|void
name|testNoSuchBeanException
parameter_list|()
block|{
name|NoSuchBeanException
name|e
init|=
operator|new
name|NoSuchBeanException
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|e
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|NoSuchBeanException
name|e2
init|=
operator|new
name|NoSuchBeanException
argument_list|(
literal|"foo"
argument_list|,
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|e2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e2
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCamelExecutionException ()
specifier|public
name|void
name|testCamelExecutionException
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|CamelExecutionException
name|e
init|=
operator|new
name|CamelExecutionException
argument_list|(
literal|"Forced"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|CamelExecutionException
name|e2
init|=
operator|new
name|CamelExecutionException
argument_list|(
literal|"Forced"
argument_list|,
name|exchange
argument_list|,
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e2
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e2
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e2
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCamelException ()
specifier|public
name|void
name|testCamelException
parameter_list|()
block|{
name|CamelException
name|e
init|=
operator|new
name|CamelException
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|CamelException
name|e2
init|=
operator|new
name|CamelException
argument_list|(
literal|"Forced"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|e2
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Forced"
argument_list|,
name|e2
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|CamelException
name|e3
init|=
operator|new
name|CamelException
argument_list|(
literal|"Forced"
argument_list|,
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e3
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Forced"
argument_list|,
name|e3
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|CamelException
name|e4
init|=
operator|new
name|CamelException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e4
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e4
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testServiceStatus ()
specifier|public
name|void
name|testServiceStatus
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|ServiceStatus
operator|.
name|Started
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceStatus
operator|.
name|Starting
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceStatus
operator|.
name|Starting
operator|.
name|isStoppable
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceStatus
operator|.
name|Stopping
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceStatus
operator|.
name|Starting
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceStatus
operator|.
name|Started
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceStatus
operator|.
name|Stopping
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
operator|.
name|isStartable
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceStatus
operator|.
name|Started
operator|.
name|isStartable
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceStatus
operator|.
name|Starting
operator|.
name|isStartable
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceStatus
operator|.
name|Stopping
operator|.
name|isStartable
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ServiceStatus
operator|.
name|Started
operator|.
name|isStoppable
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceStatus
operator|.
name|Starting
operator|.
name|isStoppable
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
operator|.
name|isStoppable
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ServiceStatus
operator|.
name|Stopping
operator|.
name|isStoppable
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRuntimeExchangeException ()
specifier|public
name|void
name|testRuntimeExchangeException
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|RuntimeExchangeException
name|e
init|=
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"Forced"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|RuntimeExchangeException
name|e2
init|=
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"Forced"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e2
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|e2
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExchangePattern ()
specifier|public
name|void
name|testExchangePattern
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
operator|.
name|isInCapable
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExchangePattern
operator|.
name|InOptionalOut
operator|.
name|isInCapable
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExchangePattern
operator|.
name|InOut
operator|.
name|isInCapable
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ExchangePattern
operator|.
name|RobustOutOnly
operator|.
name|isInCapable
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
operator|.
name|isFaultCapable
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExchangePattern
operator|.
name|InOptionalOut
operator|.
name|isFaultCapable
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExchangePattern
operator|.
name|InOut
operator|.
name|isFaultCapable
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
operator|.
name|isOutCapable
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExchangePattern
operator|.
name|InOptionalOut
operator|.
name|isOutCapable
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ExchangePattern
operator|.
name|InOut
operator|.
name|isOutCapable
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
name|ExchangePattern
operator|.
name|asEnum
argument_list|(
literal|"InOnly"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|ExchangePattern
operator|.
name|asEnum
argument_list|(
literal|"InOut"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|ExchangePattern
operator|.
name|asEnum
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testInvalidPayloadException ()
specifier|public
name|void
name|testInvalidPayloadException
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|InvalidPayloadException
name|e
init|=
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|e
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExchangeTimedOutException ()
specifier|public
name|void
name|testExchangeTimedOutException
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ExchangeTimedOutException
name|e
init|=
operator|new
name|ExchangeTimedOutException
argument_list|(
name|exchange
argument_list|,
literal|5000
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5000
argument_list|,
name|e
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExpressionIllegalSyntaxException ()
specifier|public
name|void
name|testExpressionIllegalSyntaxException
parameter_list|()
block|{
name|ExpressionIllegalSyntaxException
name|e
init|=
operator|new
name|ExpressionIllegalSyntaxException
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|e
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoFactoryAvailableException ()
specifier|public
name|void
name|testNoFactoryAvailableException
parameter_list|()
block|{
name|NoFactoryAvailableException
name|e
init|=
operator|new
name|NoFactoryAvailableException
argument_list|(
literal|"killer"
argument_list|,
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"killer"
argument_list|,
name|e
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCamelExchangeException ()
specifier|public
name|void
name|testCamelExchangeException
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|CamelExchangeException
name|e
init|=
operator|new
name|CamelExchangeException
argument_list|(
literal|"Forced"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoSuchHeaderException ()
specifier|public
name|void
name|testNoSuchHeaderException
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|NoSuchHeaderException
name|e
init|=
operator|new
name|NoSuchHeaderException
argument_list|(
name|exchange
argument_list|,
literal|"foo"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|e
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|e
operator|.
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoSuchPropertyException ()
specifier|public
name|void
name|testNoSuchPropertyException
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|NoSuchPropertyException
name|e
init|=
operator|new
name|NoSuchPropertyException
argument_list|(
name|exchange
argument_list|,
literal|"foo"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|e
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|e
operator|.
name|getPropertyName
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|exchange
argument_list|,
name|e
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRuntimeCamelException ()
specifier|public
name|void
name|testRuntimeCamelException
parameter_list|()
block|{
name|RuntimeCamelException
name|e
init|=
operator|new
name|RuntimeCamelException
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFailedToStartRouteException ()
specifier|public
name|void
name|testFailedToStartRouteException
parameter_list|()
block|{
name|FailedToStartRouteException
name|e
init|=
operator|new
name|FailedToStartRouteException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoTypeConversionAvailableException ()
specifier|public
name|void
name|testNoTypeConversionAvailableException
parameter_list|()
block|{
name|NoTypeConversionAvailableException
name|e
init|=
operator|new
name|NoTypeConversionAvailableException
argument_list|(
literal|"foo"
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Date
operator|.
name|class
argument_list|,
name|e
operator|.
name|getToType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|e
operator|.
name|getFromType
argument_list|()
argument_list|)
expr_stmt|;
name|NoTypeConversionAvailableException
name|e2
init|=
operator|new
name|NoTypeConversionAvailableException
argument_list|(
literal|null
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|e2
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Date
operator|.
name|class
argument_list|,
name|e2
operator|.
name|getToType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|null
argument_list|,
name|e2
operator|.
name|getFromType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testResolveEndpointFailedException ()
specifier|public
name|void
name|testResolveEndpointFailedException
parameter_list|()
block|{
name|ResolveEndpointFailedException
name|e
init|=
operator|new
name|ResolveEndpointFailedException
argument_list|(
literal|"foo:bar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo:bar"
argument_list|,
name|e
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

