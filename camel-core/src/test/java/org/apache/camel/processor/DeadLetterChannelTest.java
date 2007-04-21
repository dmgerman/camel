begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
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
operator|.
name|assertIsSatisfied
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
name|Endpoint
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

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|DeadLetterChannelTest
specifier|public
class|class
name|DeadLetterChannelTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|startEndpoint
specifier|protected
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
name|startEndpoint
decl_stmt|;
DECL|field|deadEndpoint
DECL|field|successEndpoint
specifier|protected
name|MockEndpoint
name|deadEndpoint
decl_stmt|,
name|successEndpoint
decl_stmt|;
DECL|field|failUntilAttempt
specifier|protected
name|int
name|failUntilAttempt
init|=
literal|2
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
name|successEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
comment|//successEndpoint.message(0).header(DeadLetterChannel.REDELIVERED).isEqualTo(true);
name|deadEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertIsSatisfied
argument_list|(
name|deadEndpoint
argument_list|,
name|successEndpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|testLotsOfAttemptsFail ()
specifier|public
name|void
name|testLotsOfAttemptsFail
parameter_list|()
throws|throws
name|Exception
block|{
name|failUntilAttempt
operator|=
literal|5
expr_stmt|;
name|deadEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|successEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertIsSatisfied
argument_list|(
name|deadEndpoint
argument_list|,
name|successEndpoint
argument_list|)
expr_stmt|;
block|}
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deadEndpoint
operator|=
operator|(
name|MockEndpoint
operator|)
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:failed"
argument_list|)
expr_stmt|;
name|successEndpoint
operator|=
operator|(
name|MockEndpoint
operator|)
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:success"
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
specifier|final
name|Processor
argument_list|<
name|Exchange
argument_list|>
name|processor
init|=
operator|new
name|Processor
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Integer
name|counter
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|DeadLetterChannel
operator|.
name|REDELIVERY_COUNTER
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|attempt
init|=
operator|(
name|counter
operator|==
literal|null
operator|)
condition|?
literal|1
else|:
name|counter
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|attempt
operator|<
name|failUntilAttempt
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to process due to attempt: "
operator|+
name|attempt
operator|+
literal|" being less than: "
operator|+
name|failUntilAttempt
argument_list|)
throw|;
block|}
else|else
block|{
name|client
operator|.
name|send
argument_list|(
literal|"mock:success"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
return|return
operator|new
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:failed"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
operator|.
name|initialRedeliveryDelay
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|processor
argument_list|)
expr_stmt|;
comment|/*  TODO - currently process().to() results in two separate operations which have their own error handler                          .to("mock:success");                  */
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

