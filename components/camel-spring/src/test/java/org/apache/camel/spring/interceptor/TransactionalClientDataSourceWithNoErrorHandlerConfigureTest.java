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

begin_comment
comment|/**  * Easier transaction configuration as we do not have to setup a transaction error handler  */
end_comment

begin_class
DECL|class|TransactionalClientDataSourceWithNoErrorHandlerConfigureTest
specifier|public
class|class
name|TransactionalClientDataSourceWithNoErrorHandlerConfigureTest
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
comment|// set the required policy for this route to indicate its transactional
name|from
argument_list|(
literal|"direct:okay"
argument_list|)
operator|.
name|policy
argument_list|(
literal|"PROPAGATION_REQUIRED"
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
comment|// set the required policy for this route to indicate its transactional
name|from
argument_list|(
literal|"direct:fail"
argument_list|)
operator|.
name|policy
argument_list|(
literal|"PROPAGATION_REQUIRED"
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
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

