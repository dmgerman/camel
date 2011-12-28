begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.loanbroker.queue.version
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|loanbroker
operator|.
name|queue
operator|.
name|version
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
name|loanbroker
operator|.
name|queue
operator|.
name|version
operator|.
name|bank
operator|.
name|BankProcessor
import|;
end_import

begin_comment
comment|/**  * The route for the loan broker example.  */
end_comment

begin_class
DECL|class|LoanBrokerRoute
specifier|public
class|class
name|LoanBrokerRoute
extends|extends
name|RouteBuilder
block|{
comment|/**      * Let's configure the Camel routing rules using Java code...      */
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// START SNIPPET: dsl-2
name|from
argument_list|(
literal|"jms:queue:loan"
argument_list|)
comment|// let the credit agency do the first work
operator|.
name|process
argument_list|(
operator|new
name|CreditAgencyProcessor
argument_list|()
argument_list|)
comment|// send the request to the three banks
operator|.
name|multicast
argument_list|(
operator|new
name|BankResponseAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|parallelProcessing
argument_list|()
operator|.
name|to
argument_list|(
literal|"jms:queue:bank1"
argument_list|,
literal|"jms:queue:bank2"
argument_list|,
literal|"jms:queue:bank3"
argument_list|)
operator|.
name|end
argument_list|()
comment|// and prepare the reply message
operator|.
name|process
argument_list|(
operator|new
name|ReplyProcessor
argument_list|()
argument_list|)
expr_stmt|;
comment|// Each bank processor will process the message and put the response message back
name|from
argument_list|(
literal|"jms:queue:bank1"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|BankProcessor
argument_list|(
literal|"bank1"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jms:queue:bank2"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|BankProcessor
argument_list|(
literal|"bank2"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jms:queue:bank3"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|BankProcessor
argument_list|(
literal|"bank3"
argument_list|)
argument_list|)
expr_stmt|;
comment|// END SNIPPET: dsl-2
block|}
block|}
end_class

end_unit

