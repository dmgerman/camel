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
name|junit
operator|.
name|Test
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
name|RecipientList
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|DeadLetterChannelRestartFromBeginningTest
specifier|public
class|class
name|DeadLetterChannelRestartFromBeginningTest
extends|extends
name|ContextTestSupport
block|{
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
literal|"retryBean"
argument_list|,
operator|new
name|RetryBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testRestartFromBeginning ()
specifier|public
name|void
name|testRestartFromBeginning
parameter_list|()
throws|throws
name|Exception
block|{
comment|// 1 original + 4 redeliveries
name|getMockEndpoint
argument_list|(
literal|"mock:start"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Camel"
argument_list|,
literal|"Camel"
argument_list|,
literal|"Camel"
argument_list|,
literal|"Camel"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Camel"
argument_list|)
expr_stmt|;
comment|// use fire and forget
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
name|setAssertPeriod
argument_list|(
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
comment|// use the DLQ and let the retryBean handle this
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"bean:retryBean"
argument_list|)
operator|.
name|useOriginalMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// use seda:retry as a way of retrying from the input route
comment|// the seda:start could be any other kind of fire and forget endpoint
name|from
argument_list|(
literal|"seda:start"
argument_list|,
literal|"seda:retry"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:start"
argument_list|,
literal|"mock:start"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"Hello "
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|private
name|int
name|counter
decl_stmt|;
annotation|@
name|Override
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
comment|// fail the first 3 times
if|if
condition|(
name|counter
operator|++
operator|<=
literal|3
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
throw|;
block|}
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
comment|/**      * Bean used as dead letter queue, that decides what to do with the message      */
DECL|class|RetryBean
specifier|public
specifier|static
class|class
name|RetryBean
block|{
comment|// use recipient list to decide what to do with the message
annotation|@
name|RecipientList
DECL|method|handleError (Exchange exchange)
specifier|public
name|String
name|handleError
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// store a property on the exchange with the number of total attempts
name|int
name|attempts
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"attempts"
argument_list|,
literal|0
argument_list|,
name|int
operator|.
name|class
argument_list|)
decl_stmt|;
name|attempts
operator|++
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"attempts"
argument_list|,
name|attempts
argument_list|)
expr_stmt|;
comment|// we want to retry at most 4 times
if|if
condition|(
name|attempts
operator|<=
literal|4
condition|)
block|{
return|return
literal|"seda:retry"
return|;
block|}
else|else
block|{
comment|// okay we give up its a poison message
return|return
literal|"log:giveup"
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

