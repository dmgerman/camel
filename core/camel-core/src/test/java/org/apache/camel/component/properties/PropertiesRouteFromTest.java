begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.properties
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
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
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|ModelHelper
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
name|model
operator|.
name|ProcessorDefinition
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
name|model
operator|.
name|SendDefinition
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|PropertiesRouteFromTest
specifier|public
class|class
name|PropertiesRouteFromTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testPropertiesRouteFrom ()
specifier|public
name|void
name|testPropertiesRouteFrom
parameter_list|()
throws|throws
name|Exception
block|{
name|ProcessorDefinition
name|out
init|=
name|context
operator|.
name|getRouteDefinition
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{{cool.end}}"
argument_list|,
operator|(
operator|(
name|SendDefinition
operator|)
name|out
operator|)
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|context
operator|.
name|getRouteDefinition
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|getInput
argument_list|()
operator|.
name|getUri
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{{cool.start}}"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
comment|// use a routes definition to dump the routes
name|String
name|xml
init|=
name|ModelHelper
operator|.
name|dumpModelAsXml
argument_list|(
name|context
argument_list|,
name|context
operator|.
name|getRouteDefinition
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<from uri=\"{{cool.start}}\"/>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<to id=\"to1\" uri=\"{{cool.end}}\"/>"
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
name|from
argument_list|(
literal|"{{cool.start}}"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"{{cool.end}}"
argument_list|)
expr_stmt|;
block|}
block|}
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
name|addComponent
argument_list|(
literal|"properties"
argument_list|,
operator|new
name|PropertiesComponent
argument_list|(
literal|"classpath:org/apache/camel/component/properties/myproperties.properties"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

