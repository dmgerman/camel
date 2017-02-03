begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|DumpModelAsXmlChoiceFilterRouteTest
specifier|public
class|class
name|DumpModelAsXmlChoiceFilterRouteTest
extends|extends
name|ContextTestSupport
block|{
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
literal|"<header>gold</header>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<header>extra-gold</header>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<simple>${body} contains Camel</simple>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testDumpModelAsXmAl ()
specifier|public
name|void
name|testDumpModelAsXmAl
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
literal|"a"
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
literal|"<constant>bar</constant>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<expressionDefinition>header{test} is not null</expressionDefinition>"
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
name|to
argument_list|(
literal|"log:input"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|header
argument_list|(
literal|"gold"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:gold"
argument_list|)
operator|.
name|filter
argument_list|()
operator|.
name|header
argument_list|(
literal|"extra-gold"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:extra-gold"
argument_list|)
operator|.
name|endChoice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|simple
argument_list|(
literal|"${body} contains 'Camel'"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:camel"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:other"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"a"
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|constant
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"test"
argument_list|)
operator|.
name|isNotNull
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
literal|"not null"
argument_list|)
operator|.
name|when
argument_list|(
name|xpath
argument_list|(
literal|"/foo/bar"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"xpath"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

