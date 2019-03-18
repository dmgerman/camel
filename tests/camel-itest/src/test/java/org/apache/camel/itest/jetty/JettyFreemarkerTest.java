begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|jetty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
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
name|Processor
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
name|ResolveEndpointFailedException
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
name|support
operator|.
name|ResourceHelper
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
name|test
operator|.
name|AvailablePortFinder
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|util
operator|.
name|ObjectHelper
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
name|util
operator|.
name|StringHelper
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
DECL|class|JettyFreemarkerTest
specifier|public
class|class
name|JettyFreemarkerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|Test
DECL|method|testClasspath ()
specifier|public
name|void
name|testClasspath
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"firstName"
argument_list|,
literal|"John"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"lastName"
argument_list|,
literal|"Doe"
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"freemarker:org/apache/camel/itest/jetty/header.ftl"
argument_list|,
literal|""
argument_list|,
name|map
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Dear Doe, John"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testClasspathInvalidParameter ()
specifier|public
name|void
name|testClasspathInvalidParameter
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"firstName"
argument_list|,
literal|"John"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"lastName"
argument_list|,
literal|"Doe"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"freemarker:org/apache/camel/itest/jetty/?name=header.ftl"
argument_list|,
literal|""
argument_list|,
name|map
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"Unknown parameters=[{name=header.ftl}]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testHttp ()
specifier|public
name|void
name|testHttp
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"firstName"
argument_list|,
literal|"John"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"lastName"
argument_list|,
literal|"Doe"
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"freemarker://http://localhost:"
operator|+
name|port
operator|+
literal|"/test?name=header.ftl"
argument_list|,
literal|""
argument_list|,
name|map
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Dear Doe, John"
argument_list|,
name|response
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
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|8000
argument_list|)
expr_stmt|;
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
literal|"jetty:http://localhost:"
operator|+
name|port
operator|+
literal|"/test"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"name"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|name
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
comment|// strip off the locale
name|name
operator|=
name|StringHelper
operator|.
name|before
argument_list|(
name|name
argument_list|,
literal|"_"
argument_list|)
expr_stmt|;
name|name
operator|=
literal|"org/apache/camel/itest/jetty/"
operator|+
name|name
operator|+
literal|".ftl"
expr_stmt|;
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|String
name|xml
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

