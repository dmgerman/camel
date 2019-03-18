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
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
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
DECL|class|GenerateXmFromCamelContextTest
specifier|public
class|class
name|GenerateXmFromCamelContextTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testCreateRouteFromCamelContext ()
specifier|public
name|void
name|testCreateRouteFromCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|list
init|=
name|context
operator|.
name|getRouteDefinitions
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Size of list "
operator|+
name|list
argument_list|,
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|RouteDefinition
name|routeType
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Found route: "
operator|+
name|routeType
argument_list|)
expr_stmt|;
comment|// now lets marshall it!
name|dump
argument_list|(
name|routeType
argument_list|)
expr_stmt|;
block|}
DECL|method|dump (Object object)
specifier|protected
name|void
name|dump
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|Exception
block|{
name|JAXBContext
name|jaxbContext
init|=
name|XmlTestSupport
operator|.
name|createJaxbContext
argument_list|()
decl_stmt|;
name|Marshaller
name|marshaller
init|=
name|jaxbContext
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|marshaller
operator|.
name|setProperty
argument_list|(
name|Marshaller
operator|.
name|JAXB_FORMATTED_OUTPUT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
name|object
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Created: "
operator|+
name|buffer
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should contain the description"
argument_list|,
name|out
operator|.
name|indexOf
argument_list|(
literal|"<from uri=\"direct:start\"/>"
argument_list|)
operator|>
literal|0
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
name|filter
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/foo/bar = 'abc'"
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

