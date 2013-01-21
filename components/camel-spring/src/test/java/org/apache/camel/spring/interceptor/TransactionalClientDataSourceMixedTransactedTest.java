begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Same route but not transacted  */
end_comment

begin_class
DECL|class|TransactionalClientDataSourceMixedTransactedTest
specifier|public
class|class
name|TransactionalClientDataSourceMixedTransactedTest
extends|extends
name|TransactionalClientDataSourceTest
block|{
DECL|method|testTransactionRollback ()
specifier|public
name|void
name|testTransactionRollback
parameter_list|()
throws|throws
name|Exception
block|{
comment|// through the onException clause below we've marked the exceptions containing the message
comment|// "Donkey" as being handled so that we don't count with any exception on the client side.
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:fail"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|int
name|count
init|=
name|jdbc
operator|.
name|queryForInt
argument_list|(
literal|"select count(*) from books"
argument_list|)
decl_stmt|;
comment|// should get 2 books as the first operation will succeed and we are not transacted
name|assertEquals
argument_list|(
literal|"Number of books"
argument_list|,
literal|2
argument_list|,
name|count
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
comment|// ignore failure if its something with Donkey
name|onException
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
operator|.
name|onWhen
argument_list|(
name|exceptionMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Donkey"
argument_list|)
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:okay"
argument_list|)
comment|// mark this route as transacted
operator|.
name|transacted
argument_list|()
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Tiger in Action"
argument_list|)
argument_list|)
operator|.
name|beanRef
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
name|beanRef
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
name|beanRef
argument_list|(
literal|"bookService"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:fail"
argument_list|)
comment|// and this route is not transacted
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Tiger in Action"
argument_list|)
argument_list|)
operator|.
name|beanRef
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
name|beanRef
argument_list|(
literal|"bookService"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

