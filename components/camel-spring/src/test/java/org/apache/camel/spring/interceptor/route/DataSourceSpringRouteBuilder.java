begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.interceptor.route
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
operator|.
name|route
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

begin_class
DECL|class|DataSourceSpringRouteBuilder
specifier|public
class|class
name|DataSourceSpringRouteBuilder
extends|extends
name|SpringRouteBuilder
block|{
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// get the required policy
name|SpringTransactionPolicy
name|required
init|=
name|lookup
argument_list|(
literal|"PROPAGATION_REQUIRED"
argument_list|,
name|SpringTransactionPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// For spring based transaction, end users are encouraged to use the
comment|// transaction error handler instead of the default DeadLetterChannel.
name|errorHandler
argument_list|(
name|transactionErrorHandler
argument_list|(
name|required
argument_list|)
argument_list|)
expr_stmt|;
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
block|}
block|}
end_class

end_unit

