begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
DECL|class|ProcessorDefinitionHelperTest
specifier|public
class|class
name|ProcessorDefinitionHelperTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testFilterTypeInOutputs ()
specifier|public
name|void
name|testFilterTypeInOutputs
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteDefinition
name|route
init|=
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|ProcessorDefinitionHelper
operator|.
name|filterTypeInOutputs
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|,
name|ProcessorDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|it
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"choice1"
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"whenfoo"
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"whenbar"
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"baz"
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|it
operator|.
name|hasNext
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
literal|"direct:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|id
argument_list|(
literal|"whenfoo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|id
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|id
argument_list|(
literal|"whenbar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|id
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:baz"
argument_list|)
operator|.
name|id
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

