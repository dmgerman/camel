begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|interceptor
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
name|spring
operator|.
name|SpringRouteBuilder
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
name|SpringTransactionPolicy
import|;
end_import

begin_comment
comment|/**  * Unit test to demonstrate the transactional client pattern.  */
end_comment

begin_class
DECL|class|TransactionalClientDataSourceLookupTypeTest
specifier|public
class|class
name|TransactionalClientDataSourceLookupTypeTest
extends|extends
name|TransactionalClientDataSourceTest
block|{
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
comment|// START SNIPPET: e1
comment|// Notice that we use the SpringRouteBuilder that has a few more features than
comment|// the standard RouteBuilder
return|return
operator|new
name|SpringRouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e1
comment|// lookup the transaction policy
name|SpringTransactionPolicy
name|required
init|=
name|lookup
argument_list|(
name|SpringTransactionPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// use this error handler instead of DeadLetterChannel that is the default
comment|// Notice: transactionErrorHandler is in SpringRouteBuilder
if|if
condition|(
name|useTransactionErrorHandler
condition|)
block|{
comment|// useTransactionErrorHandler is only used for unit testing to reuse code
comment|// for doing a 2nd test without this transaction error handler, so ignore
comment|// this. For spring based transaction, end users are encouraged to use the
comment|// transaction error handler instead of the default DeadLetterChannel.
name|errorHandler
argument_list|(
name|transactionErrorHandler
argument_list|(
name|required
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// END SNIPPET: e1
comment|// START SNIPPET: e2
comment|// set the required policy for this route
name|from
argument_list|(
literal|"direct:okay"
argument_list|)
operator|.
name|policy
argument_list|(
name|required
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Tiger in Action"
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
literal|"bookService"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Elephant in Action"
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
literal|"bookService"
argument_list|)
expr_stmt|;
comment|// set the required policy for this route
name|from
argument_list|(
literal|"direct:fail"
argument_list|)
operator|.
name|policy
argument_list|(
name|required
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Tiger in Action"
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
literal|"bookService"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Donkey in Action"
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
literal|"bookService"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e2
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

