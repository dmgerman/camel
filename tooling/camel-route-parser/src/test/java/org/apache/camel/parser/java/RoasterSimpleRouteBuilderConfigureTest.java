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
name|BufferedReader
import|;
end_import

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
name|io
operator|.
name|FileReader
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
name|ParserResult
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
name|helper
operator|.
name|CamelJavaParserHelper
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
name|MethodSource
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
DECL|class|RoasterSimpleRouteBuilderConfigureTest
specifier|public
class|class
name|RoasterSimpleRouteBuilderConfigureTest
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
name|RoasterSimpleRouteBuilderConfigureTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|parse ()
specifier|public
name|void
name|parse
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
literal|"src/test/java/org/apache/camel/parser/java/MySimpleRouteBuilder.java"
argument_list|)
argument_list|)
decl_stmt|;
name|MethodSource
argument_list|<
name|JavaClassSource
argument_list|>
name|method
init|=
name|clazz
operator|.
name|getMethod
argument_list|(
literal|"configure"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ParserResult
argument_list|>
name|list
init|=
name|CamelJavaParserHelper
operator|.
name|parseCamelSimpleExpressions
argument_list|(
name|method
argument_list|)
decl_stmt|;
for|for
control|(
name|ParserResult
name|simple
range|:
name|list
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Simple: "
operator|+
name|simple
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"  Line: "
operator|+
name|findLineNumber
argument_list|(
name|simple
operator|.
name|getPosition
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"${body}> 100"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPredicate
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|27
argument_list|,
name|findLineNumber
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPosition
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"${body}> 200"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getPredicate
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|30
argument_list|,
name|findLineNumber
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getPosition
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|findLineNumber (int pos)
specifier|public
specifier|static
name|int
name|findLineNumber
parameter_list|(
name|int
name|pos
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|lines
init|=
literal|0
decl_stmt|;
name|int
name|current
init|=
literal|0
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"src/test/java/org/apache/camel/parser/java/MySimpleRouteBuilder.java"
argument_list|)
decl_stmt|;
try|try
init|(
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
argument_list|)
init|)
block|{
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|br
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|lines
operator|++
expr_stmt|;
name|current
operator|+=
name|line
operator|.
name|length
argument_list|()
expr_stmt|;
if|if
condition|(
name|current
operator|>
name|pos
condition|)
block|{
return|return
name|lines
return|;
block|}
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
block|}
end_class

end_unit

