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
name|Set
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
DECL|class|GatherAllStaticEndpointUrisTest
specifier|public
class|class
name|GatherAllStaticEndpointUrisTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testGatherAllStaticEndpointUris ()
specifier|public
name|void
name|testGatherAllStaticEndpointUris
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteDefinition
name|route
init|=
name|context
operator|.
name|getRouteDefinition
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|uris
init|=
name|RouteDefinitionHelper
operator|.
name|gatherAllStaticEndpointUris
argument_list|(
name|context
argument_list|,
name|route
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|uris
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|uris
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|RouteDefinition
name|route2
init|=
name|context
operator|.
name|getRouteDefinition
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|uris2
init|=
name|RouteDefinitionHelper
operator|.
name|gatherAllStaticEndpointUris
argument_list|(
name|context
argument_list|,
name|route2
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|uris2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|uris2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|uris2out
init|=
name|RouteDefinitionHelper
operator|.
name|gatherAllStaticEndpointUris
argument_list|(
name|context
argument_list|,
name|route2
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|uris2out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|uris2out
operator|.
name|size
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
literal|"direct:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:bar"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Hello World"
argument_list|)
operator|.
name|wireTap
argument_list|(
literal|"mock:tap"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|enrich
argument_list|(
literal|"seda:stuff"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:bar"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Bye World"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

