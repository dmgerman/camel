begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.tx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|tx
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
name|test
operator|.
name|AvailablePortFinder
import|;
end_import

begin_comment
comment|/**  * Route that listen on a JMS queue and send a request/reply over http  * before returning a response. Is transacted.  *<p/>  * Notice we use the SpringRouteBuilder that supports transacted  * error handler.  */
end_comment

begin_class
DECL|class|JmsToHttpWithRollbackRoute
specifier|public
class|class
name|JmsToHttpWithRollbackRoute
extends|extends
name|JmsToHttpRoute
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|8000
argument_list|)
expr_stmt|;
comment|// configure a global transacted error handler
name|errorHandler
argument_list|(
name|transactionErrorHandler
argument_list|(
name|required
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|data
argument_list|)
comment|// must setup policy for each route due CAMEL-1475 bug
operator|.
name|policy
argument_list|(
name|required
argument_list|)
comment|// send a request to http and get the response
operator|.
name|to
argument_list|(
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/sender"
argument_list|)
comment|// convert the response to String so we can work with it and avoid streams only be readable once
comment|// as the http component will return data as a stream
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
comment|// do a choice if the response is okay or not
operator|.
name|choice
argument_list|()
comment|// do a xpath to compare if the status is NOT okay
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/reply/status != 'ok'"
argument_list|)
comment|// as this is based on an unit test we use mocks to verify how many times we did rollback
operator|.
name|to
argument_list|(
literal|"mock:rollback"
argument_list|)
comment|// response is not okay so force a rollback
operator|.
name|rollback
argument_list|()
operator|.
name|otherwise
argument_list|()
comment|// otherwise since its okay, the route ends and the response is sent back
comment|// to the original caller
operator|.
name|end
argument_list|()
expr_stmt|;
comment|// this is our http route that will fail the first 2 attempts
comment|// before it sends an ok response
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port
operator|+
literal|"/sender"
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
if|if
condition|(
name|counter
operator|++
operator|<
literal|2
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|nok
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|ok
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

