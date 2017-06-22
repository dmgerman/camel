begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|DefaultCamelContext
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
name|DefaultRuntimeEndpointRegistry
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
name|ExplicitCamelContextNameStrategy
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|ContextListCommandTest
specifier|public
class|class
name|ContextListCommandTest
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ContextListCommandTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testContextList ()
specifier|public
name|void
name|testContextList
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setNameStrategy
argument_list|(
operator|new
name|ExplicitCamelContextNameStrategy
argument_list|(
literal|"foobar"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|CamelController
name|controller
init|=
operator|new
name|DummyCamelController
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|ps
init|=
operator|new
name|PrintStream
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|ContextListCommand
name|command
init|=
operator|new
name|ContextListCommand
argument_list|()
decl_stmt|;
name|command
operator|.
name|execute
argument_list|(
name|controller
argument_list|,
name|ps
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|os
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"\n\n{}\n"
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// should contain a table with the context
name|assertTrue
argument_list|(
name|out
operator|.
name|contains
argument_list|(
literal|"foobar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|contains
argument_list|(
literal|"Started"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointStats ()
specifier|public
name|void
name|testEndpointStats
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setRuntimeEndpointRegistry
argument_list|(
operator|new
name|DefaultRuntimeEndpointRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setNameStrategy
argument_list|(
operator|new
name|ExplicitCamelContextNameStrategy
argument_list|(
literal|"foobar"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|CamelController
name|controller
init|=
operator|new
name|DummyCamelController
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintStream
name|ps
init|=
operator|new
name|PrintStream
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|EndpointStatisticCommand
name|command
init|=
operator|new
name|EndpointStatisticCommand
argument_list|(
literal|"foobar"
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|command
operator|.
name|execute
argument_list|(
name|controller
argument_list|,
name|ps
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|os
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"\n\n{}\n"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|contains
argument_list|(
literal|"direct://start"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|contains
argument_list|(
literal|"mock://result"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

