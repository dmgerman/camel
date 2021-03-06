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
name|Body
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
name|ExchangeException
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
name|Header
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
name|impl
operator|.
name|JndiRegistry
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
comment|/**  * Unit test for the retry until predicate  */
end_comment

begin_class
DECL|class|OnExceptionRetryUntilTest
specifier|public
class|class
name|OnExceptionRetryUntilTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|invoked
specifier|private
specifier|static
name|int
name|invoked
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myRetryHandler"
argument_list|,
operator|new
name|MyRetryBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testRetryUntil ()
specifier|public
name|void
name|testRetryUntil
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
comment|// as its based on a unit test we do not have any delays between
comment|// and do not log the stack trace
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|1
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
argument_list|)
expr_stmt|;
comment|// START SNIPPET: e1
comment|// we want to use a predicate for retries so we can determine in
comment|// our bean
comment|// when retry should stop, notice it will overrule the global
comment|// error handler
comment|// where we defined at most 1 redelivery attempt. Here we will
comment|// continue until
comment|// the predicate returns false
name|onException
argument_list|(
name|MyFunctionalException
operator|.
name|class
argument_list|)
operator|.
name|retryWhile
argument_list|(
name|method
argument_list|(
literal|"myRetryHandler"
argument_list|)
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Sorry"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
name|from
argument_list|(
literal|"direct:start"
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
throw|throw
operator|new
name|MyFunctionalException
argument_list|(
literal|"Sorry you cannot do this"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Sorry"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|invoked
argument_list|)
expr_stmt|;
block|}
comment|// START SNIPPET: e2
DECL|class|MyRetryBean
specifier|public
class|class
name|MyRetryBean
block|{
comment|// using bean binding we can bind the information from the exchange to
comment|// the types we have in our method signature
DECL|method|retry (@eaderExchange.REDELIVERY_COUNTER) Integer counter, @Body String body, @ExchangeException Exception causedBy)
specifier|public
name|boolean
name|retry
parameter_list|(
annotation|@
name|Header
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|)
name|Integer
name|counter
parameter_list|,
annotation|@
name|Body
name|String
name|body
parameter_list|,
annotation|@
name|ExchangeException
name|Exception
name|causedBy
parameter_list|)
block|{
comment|// NOTE: counter is the redelivery attempt, will start from 1
name|invoked
operator|++
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|causedBy
operator|instanceof
name|MyFunctionalException
argument_list|)
expr_stmt|;
comment|// we can of course do what ever we want to determine the result but
comment|// this is a unit test so we end after 3 attempts
return|return
name|counter
operator|<
literal|3
return|;
block|}
block|}
comment|// END SNIPPET: e2
block|}
end_class

end_unit

