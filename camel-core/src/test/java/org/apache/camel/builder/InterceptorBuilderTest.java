begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|CamelContext
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
name|Producer
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
name|TestSupport
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
name|DefaultCamelContext
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
name|processor
operator|.
name|DelegateProcessor
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 530102 $  */
end_comment

begin_class
DECL|class|InterceptorBuilderTest
specifier|public
class|class
name|InterceptorBuilderTest
extends|extends
name|TestSupport
block|{
comment|/**      * Validates that interceptors are executed in the right order.      * @throws Exception      */
DECL|method|testRouteWithInterceptor ()
specifier|public
name|void
name|testRouteWithInterceptor
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|container
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|ArrayList
argument_list|<
name|String
argument_list|>
name|order
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|DelegateProcessor
argument_list|<
name|Exchange
argument_list|>
name|interceptor1
init|=
operator|new
name|DelegateProcessor
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|order
operator|.
name|add
argument_list|(
literal|"START:1"
argument_list|)
expr_stmt|;
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|order
operator|.
name|add
argument_list|(
literal|"END:1"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
specifier|final
name|DelegateProcessor
argument_list|<
name|Exchange
argument_list|>
name|interceptor2
init|=
operator|new
name|DelegateProcessor
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|order
operator|.
name|add
argument_list|(
literal|"START:2"
argument_list|)
expr_stmt|;
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|order
operator|.
name|add
argument_list|(
literal|"END:2"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|intercept
argument_list|()
operator|.
name|add
argument_list|(
name|interceptor1
argument_list|)
operator|.
name|add
argument_list|(
name|interceptor2
argument_list|)
operator|.
name|target
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:d"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|container
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
name|endpoint
init|=
name|container
operator|.
name|getEndpoint
argument_list|(
literal|"direct:a"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Producer
argument_list|<
name|Exchange
argument_list|>
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ArrayList
argument_list|<
name|String
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"START:1"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"START:2"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"END:2"
argument_list|)
expr_stmt|;
name|expected
operator|.
name|add
argument_list|(
literal|"END:1"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Interceptor invocation order:"
operator|+
name|order
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|order
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

