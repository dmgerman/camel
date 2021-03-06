begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|health
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Route
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
name|health
operator|.
name|HealthCheckResultBuilder
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

begin_class
DECL|class|RouteHealthCheckTest
specifier|public
class|class
name|RouteHealthCheckTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|TEST_ROUTE_ID
specifier|private
specifier|static
specifier|final
name|String
name|TEST_ROUTE_ID
init|=
literal|"Test-Route"
decl_stmt|;
annotation|@
name|Test
DECL|method|testDoCallDoesNotHaveNPEWhenJmxDisabled ()
specifier|public
name|void
name|testDoCallDoesNotHaveNPEWhenJmxDisabled
parameter_list|()
block|{
name|Route
name|route
init|=
name|context
operator|.
name|getRoute
argument_list|(
name|TEST_ROUTE_ID
argument_list|)
decl_stmt|;
name|RouteHealthCheck
name|healthCheck
init|=
operator|new
name|RouteHealthCheck
argument_list|(
name|route
argument_list|)
decl_stmt|;
specifier|final
name|HealthCheckResultBuilder
name|builder
init|=
name|HealthCheckResultBuilder
operator|.
name|on
argument_list|(
name|healthCheck
argument_list|)
decl_stmt|;
name|healthCheck
operator|.
name|doCall
argument_list|(
name|builder
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
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
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:input"
argument_list|)
operator|.
name|id
argument_list|(
name|TEST_ROUTE_ID
argument_list|)
operator|.
name|log
argument_list|(
literal|"Message"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

