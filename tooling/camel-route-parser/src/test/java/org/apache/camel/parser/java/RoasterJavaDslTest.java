begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser.java
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
operator|.
name|java
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
operator|.
name|RouteBuilderParser
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
name|parser
operator|.
name|model
operator|.
name|CamelNodeDetails
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
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|Roaster
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|source
operator|.
name|JavaClassSource
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

begin_class
DECL|class|RoasterJavaDslTest
specifier|public
class|class
name|RoasterJavaDslTest
extends|extends
name|CamelTestSupport
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
name|RoasterJavaDslTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|isDumpRouteCoverage ()
specifier|public
name|boolean
name|isDumpRouteCoverage
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Test
DECL|method|parseTree ()
specifier|public
name|void
name|parseTree
parameter_list|()
throws|throws
name|Exception
block|{
name|JavaClassSource
name|clazz
init|=
operator|(
name|JavaClassSource
operator|)
name|Roaster
operator|.
name|parse
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/java/org/apache/camel/parser/java/MyJavaDslRouteBuilder.java"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|CamelNodeDetails
argument_list|>
name|list
init|=
name|RouteBuilderParser
operator|.
name|parseRouteBuilderTree
argument_list|(
name|clazz
argument_list|,
literal|"."
argument_list|,
literal|"src/test/java/org/apache/camel/parser/java/MyJavaDslRouteBuilder.java"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CamelNodeDetails
name|details
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"src/test/java/org/apache/camel/parser/java/MyJavaDslRouteBuilder.java"
argument_list|,
name|details
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|details
operator|.
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"configure"
argument_list|,
name|details
operator|.
name|getMethodName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.camel.parser.java.MyJavaDslRouteBuilder"
argument_list|,
name|details
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"28"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLineNumber
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"28"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLineNumberEnd
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|tree
init|=
name|details
operator|.
name|dump
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"\n"
operator|+
name|tree
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tree
operator|.
name|contains
argument_list|(
literal|"28\tfrom"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tree
operator|.
name|contains
argument_list|(
literal|"29\t  log"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tree
operator|.
name|contains
argument_list|(
literal|"30\t  setHeader"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tree
operator|.
name|contains
argument_list|(
literal|"31\t  choice"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tree
operator|.
name|contains
argument_list|(
literal|"33\t    to"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tree
operator|.
name|contains
argument_list|(
literal|"34\t    toD"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tree
operator|.
name|contains
argument_list|(
literal|"36\t    toD"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tree
operator|.
name|contains
argument_list|(
literal|"38\t    log"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tree
operator|.
name|contains
argument_list|(
literal|"40\t  to"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteCoverage ()
specifier|public
name|void
name|testRouteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|MyJavaDslRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

