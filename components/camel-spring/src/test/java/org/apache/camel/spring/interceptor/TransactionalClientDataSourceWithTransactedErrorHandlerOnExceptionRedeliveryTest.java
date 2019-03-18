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

begin_class
DECL|class|TransactionalClientDataSourceWithTransactedErrorHandlerOnExceptionRedeliveryTest
specifier|public
class|class
name|TransactionalClientDataSourceWithTransactedErrorHandlerOnExceptionRedeliveryTest
extends|extends
name|TransactionalClientDataSourceRedeliveryTest
block|{
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
comment|// START SNIPPET: e1
comment|// configure transacted error handler to use up till 4 redeliveries
comment|// we have not passed in any spring TX manager. Camel will automatic
comment|// find it in the spring application context. You only need to help
comment|// Camel in case you have multiple TX managers
name|errorHandler
argument_list|(
name|transactionErrorHandler
argument_list|()
operator|.
name|maximumRedeliveries
argument_list|(
literal|6
argument_list|)
argument_list|)
expr_stmt|;
comment|// speical for this exception we only want to do it at most 4 times
name|onException
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:okay"
argument_list|)
comment|// marks this route as transacted, and we dont pass in any parameters so we
comment|// will auto lookup and use the Policy defined in the spring XML file
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
comment|// marks this route as transacted that will use the single policy defined in the registry
name|from
argument_list|(
literal|"direct:fail"
argument_list|)
comment|// marks this route as transacted, and we dont pass in any parameters so we
comment|// will auto lookup and use the Policy defined in the spring XML file
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
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

