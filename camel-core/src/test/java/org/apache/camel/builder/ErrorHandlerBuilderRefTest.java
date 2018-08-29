begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_class
DECL|class|ErrorHandlerBuilderRefTest
specifier|public
class|class
name|ErrorHandlerBuilderRefTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|errorHandlerBuilderRef
name|ErrorHandlerBuilderRef
name|errorHandlerBuilderRef
init|=
operator|new
name|ErrorHandlerBuilderRef
argument_list|(
literal|"ref"
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"ref"
argument_list|,
operator|new
name|DefaultErrorHandlerBuilder
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setErrorHandlerBuilder
argument_list|(
name|errorHandlerBuilderRef
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testErrorHandlerBuilderRef ()
specifier|public
name|void
name|testErrorHandlerBuilderRef
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|uuid
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|TempRouteBuilder
argument_list|(
name|uuid
argument_list|)
argument_list|)
expr_stmt|;
name|checkObjectSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
name|checkObjectSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|checkObjectSize (int size)
specifier|private
name|void
name|checkObjectSize
parameter_list|(
name|int
name|size
parameter_list|)
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"Get a wrong size of Route"
argument_list|,
name|size
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Field
name|field
init|=
name|ErrorHandlerBuilderRef
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"handlers"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong size of ErrorHandler"
argument_list|,
name|size
argument_list|,
operator|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|field
operator|.
name|get
argument_list|(
name|errorHandlerBuilderRef
argument_list|)
operator|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|TempRouteBuilder
specifier|private
specifier|static
class|class
name|TempRouteBuilder
extends|extends
name|RouteBuilder
block|{
DECL|field|routeId
specifier|final
name|String
name|routeId
decl_stmt|;
DECL|method|TempRouteBuilder (String routeId)
name|TempRouteBuilder
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
name|this
operator|.
name|routeId
operator|=
name|routeId
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
name|routeId
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
block|}
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
literal|"direct:start"
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

