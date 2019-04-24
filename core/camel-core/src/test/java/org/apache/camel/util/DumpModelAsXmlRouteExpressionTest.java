begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|MyBarSingleton
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
name|impl
operator|.
name|JndiRegistry
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|DumpModelAsXmlRouteExpressionTest
specifier|public
class|class
name|DumpModelAsXmlRouteExpressionTest
extends|extends
name|ContextTestSupport
block|{
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
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myCoolBean"
argument_list|,
operator|new
name|MyBarSingleton
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testDumpModelAsXml ()
specifier|public
name|void
name|testDumpModelAsXml
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"myRoute"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<simple>Hello ${body}</simple>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDumpModelAsXmlXPath ()
specifier|public
name|void
name|testDumpModelAsXmlXPath
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"myOtherRoute"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<xpath>/foo</xpath>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDumpModelAsXmlHeader ()
specifier|public
name|void
name|testDumpModelAsXmlHeader
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"myFooRoute"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<header>bar</header>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDumpModelAsXmlBean ()
specifier|public
name|void
name|testDumpModelAsXmlBean
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"myBeanRoute"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<setHeader headerName=\"foo\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<method ref=\"myCoolBean\"/>"
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
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"Hello ${body}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:other"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"myOtherRoute"
argument_list|)
operator|.
name|setBody
argument_list|(
name|xpath
argument_list|(
literal|"/foo"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"myFooRoute"
argument_list|)
operator|.
name|setBody
argument_list|(
name|header
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bean"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"myBeanRoute"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
name|method
argument_list|(
literal|"myCoolBean"
argument_list|)
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

