begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|MainTest
specifier|public
class|class
name|MainTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testMain ()
specifier|public
name|void
name|testMain
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets make a simple route
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|addRoutesBuilder
argument_list|(
operator|new
name|MyRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|enableTrace
argument_list|()
expr_stmt|;
name|main
operator|.
name|bind
argument_list|(
literal|"foo"
argument_list|,
literal|31
argument_list|)
expr_stmt|;
name|main
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|main
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find the registry bound object"
argument_list|,
literal|31
argument_list|,
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"mock:results"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|main
operator|.
name|getCamelTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<message>1</message>"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDisableHangupSupport ()
specifier|public
name|void
name|testDisableHangupSupport
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets make a simple route
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|addRoutesBuilder
argument_list|(
operator|new
name|MyRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|disableHangupSupport
argument_list|()
expr_stmt|;
name|main
operator|.
name|enableTrace
argument_list|()
expr_stmt|;
name|main
operator|.
name|bind
argument_list|(
literal|"foo"
argument_list|,
literal|31
argument_list|)
expr_stmt|;
name|main
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|main
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find the registry bound object"
argument_list|,
literal|31
argument_list|,
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"mock:results"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|main
operator|.
name|getCamelTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<message>1</message>"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLoadingRouteFromCommand ()
specifier|public
name|void
name|testLoadingRouteFromCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
comment|// let the main load the MyRouteBuilder
name|main
operator|.
name|parseArguments
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"-r"
block|,
literal|"org.apache.camel.main.MainTest$MyRouteBuilder"
block|}
argument_list|)
expr_stmt|;
name|main
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|main
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"mock:results"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|main
operator|.
name|getCamelTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<message>1</message>"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOptionalProperties ()
specifier|public
name|void
name|testOptionalProperties
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets make a simple route
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|addRoutesBuilder
argument_list|(
operator|new
name|MyRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|main
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
comment|// should load application.properties from classpath
name|assertEquals
argument_list|(
literal|"World"
argument_list|,
name|camelContext
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"{{hello}}"
argument_list|)
argument_list|)
expr_stmt|;
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDisableTracing ()
specifier|public
name|void
name|testDisableTracing
parameter_list|()
throws|throws
name|Exception
block|{
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
name|main
operator|.
name|addRoutesBuilder
argument_list|(
operator|new
name|MyRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|main
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Tracing should be disabled"
argument_list|,
name|camelContext
operator|.
name|isTracing
argument_list|()
argument_list|)
expr_stmt|;
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|class|MyRouteBuilder
specifier|public
specifier|static
class|class
name|MyRouteBuilder
extends|extends
name|RouteBuilder
block|{
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

