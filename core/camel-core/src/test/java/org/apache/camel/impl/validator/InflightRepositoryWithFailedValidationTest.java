begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|validator
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
name|builder
operator|.
name|RouteBuilder
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
DECL|class|InflightRepositoryWithFailedValidationTest
specifier|public
class|class
name|InflightRepositoryWithFailedValidationTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testInflight ()
specifier|public
name|void
name|testInflight
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Exception
name|e
init|=
literal|null
decl_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"FAIL"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|e
operator|=
name|ex
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|size
argument_list|(
literal|"first"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|size
argument_list|(
literal|"second"
argument_list|)
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
name|validator
argument_list|()
operator|.
name|type
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|withExpression
argument_list|(
name|bodyAs
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"valid"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"first"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:validation"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:validation"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"second"
argument_list|)
operator|.
name|inputTypeWithValidate
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit
