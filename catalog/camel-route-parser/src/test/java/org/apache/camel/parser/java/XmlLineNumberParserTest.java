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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
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
name|XmlLineNumberParser
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|XmlLineNumberParserTest
specifier|public
class|class
name|XmlLineNumberParserTest
block|{
annotation|@
name|Test
DECL|method|testRespectNamespace ()
specifier|public
name|void
name|testRespectNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
literal|"src/test/resources/org/apache/camel/parser/xml/mycamel.xml"
argument_list|)
decl_stmt|;
name|Document
name|parsedXml
init|=
name|XmlLineNumberParser
operator|.
name|parseXml
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|NodeList
name|fromCamelWithNamespace
init|=
name|parsedXml
operator|.
name|getElementsByTagNameNS
argument_list|(
literal|"http://camel.apache.org/schema/spring"
argument_list|,
literal|"from"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|fromCamelWithNamespace
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
