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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|junit
operator|.
name|Before
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

begin_import
import|import static
name|org
operator|.
name|awaitility
operator|.
name|Awaitility
operator|.
name|await
import|;
end_import

begin_class
DECL|class|TransactionalClientDataSourceTransactedWithFileTest
specifier|public
class|class
name|TransactionalClientDataSourceTransactedWithFileTest
extends|extends
name|TransactionClientDataSourceSupport
block|{
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/transacted"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTransactionSuccess ()
specifier|public
name|void
name|testTransactionSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/transacted/okay"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"okay.txt"
argument_list|)
expr_stmt|;
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|3
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|untilAsserted
argument_list|(
parameter_list|()
lambda|->
block|{
comment|// wait for route to complete
name|int
name|count
init|=
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from books"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of books"
argument_list|,
literal|3
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTransactionRollback ()
specifier|public
name|void
name|testTransactionRollback
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/transacted/fail"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"fail.txt"
argument_list|)
expr_stmt|;
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|3
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|untilAsserted
argument_list|(
parameter_list|()
lambda|->
block|{
comment|// should not be able to process the file so we still got 1 book as we did from the start
name|int
name|count
init|=
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from books"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of books"
argument_list|,
literal|1
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
argument_list|)
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
name|from
argument_list|(
literal|"file://target/transacted/okay?initialDelay=0&delay=10"
argument_list|)
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
name|from
argument_list|(
literal|"file://target/transacted/fail?initialDelay=0&delay=10"
argument_list|)
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
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

