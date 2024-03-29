begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

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
name|ArrayList
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
name|XmlRouteParser
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
name|CamelSimpleExpressionDetails
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
DECL|class|XmlFilterRouteTest
specifier|public
class|class
name|XmlFilterRouteTest
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
name|XmlFilterRouteTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testXml ()
specifier|public
name|void
name|testXml
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|CamelSimpleExpressionDetails
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
literal|"src/test/resources/org/apache/camel/parser/xml/myfiltercamel.xml"
argument_list|)
decl_stmt|;
name|String
name|fqn
init|=
literal|"src/test/resources/org/apache/camel/camel/parser/xml/myfiltercamel.xml"
decl_stmt|;
name|String
name|baseDir
init|=
literal|"src/test/resources"
decl_stmt|;
name|XmlRouteParser
operator|.
name|parseXmlRouteSimpleExpressions
argument_list|(
name|is
argument_list|,
name|baseDir
argument_list|,
name|fqn
argument_list|,
name|list
argument_list|)
expr_stmt|;
for|for
control|(
name|CamelSimpleExpressionDetails
name|detail
range|:
name|list
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
name|detail
operator|.
name|getSimple
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
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
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"${body} == 'Camel'"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getSimple
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isPredicate
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

