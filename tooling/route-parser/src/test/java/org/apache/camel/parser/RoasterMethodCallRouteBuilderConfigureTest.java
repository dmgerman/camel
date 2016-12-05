begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  Copyright 2005-2015 Red Hat, Inc.  *  *  Red Hat licenses this file to you under the Apache License, version  *  2.0 (the "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or  *  implied.  See the License for the specific language governing  *  permissions and limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
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
DECL|class|RoasterMethodCallRouteBuilderConfigureTest
specifier|public
class|class
name|RoasterMethodCallRouteBuilderConfigureTest
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
name|RoasterMethodCallRouteBuilderConfigureTest
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
literal|"src/test/java/org/apache/camel/parser/MyMethodCallRouteBuilder.java"
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
name|parseCamelConsumerUris
argument_list|(
name|method
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|ParserResult
name|result
range|:
name|list
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Consumer: "
operator|+
name|result
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"timer:foo"
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
name|list
operator|=
name|CamelJavaParserHelper
operator|.
name|parseCamelProducerUris
argument_list|(
name|method
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
for|for
control|(
name|ParserResult
name|result
range|:
name|list
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Producer: "
operator|+
name|result
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"file:output?fileExist={{whatToDoWhenExists}}"
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
literal|"log:b"
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
block|}
block|}
end_class

end_unit

