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
name|junit
operator|.
name|Test
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|Node
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
name|junit
operator|.
name|Assert
import|;
end_import

begin_class
DECL|class|XmlLineNumberParserTest
specifier|public
class|class
name|XmlLineNumberParserTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testParse ()
specifier|public
name|void
name|testParse
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|fis
init|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/resources/org/apache/camel/util/camel-context.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Document
name|dom
init|=
name|XmlLineNumberParser
operator|.
name|parseXml
argument_list|(
name|fis
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dom
argument_list|)
expr_stmt|;
name|NodeList
name|list
init|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"beans"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|Node
name|node
init|=
name|list
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|lineNumber
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER
argument_list|)
decl_stmt|;
name|String
name|lineNumberEnd
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER_END
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"24"
argument_list|,
name|lineNumber
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"49"
argument_list|,
name|lineNumberEnd
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParseCamelContext ()
specifier|public
name|void
name|testParseCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|fis
init|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/resources/org/apache/camel/util/camel-context.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Document
name|dom
init|=
name|XmlLineNumberParser
operator|.
name|parseXml
argument_list|(
name|fis
argument_list|,
literal|null
argument_list|,
literal|"camelContext"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dom
argument_list|)
expr_stmt|;
name|NodeList
name|list
init|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"camelContext"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|Node
name|node
init|=
name|list
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|lineNumber
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER
argument_list|)
decl_stmt|;
name|String
name|lineNumberEnd
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER_END
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"29"
argument_list|,
name|lineNumber
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"47"
argument_list|,
name|lineNumberEnd
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testParseCamelContextForceNamespace ()
specifier|public
name|void
name|testParseCamelContextForceNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|fis
init|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/resources/org/apache/camel/util/camel-context.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Document
name|dom
init|=
name|XmlLineNumberParser
operator|.
name|parseXml
argument_list|(
name|fis
argument_list|,
literal|null
argument_list|,
literal|"camelContext"
argument_list|,
literal|"http://camel.apache.org/schema/spring"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dom
argument_list|)
expr_stmt|;
name|NodeList
name|list
init|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"camelContext"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|Node
name|node
init|=
name|list
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|lineNumber
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER
argument_list|)
decl_stmt|;
name|String
name|lineNumberEnd
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER_END
argument_list|)
decl_stmt|;
name|String
name|ns
init|=
name|node
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://camel.apache.org/schema/spring"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"29"
argument_list|,
name|lineNumber
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"47"
argument_list|,
name|lineNumberEnd
argument_list|)
expr_stmt|;
comment|// and there are two routes
name|list
operator|=
name|dom
operator|.
name|getElementsByTagName
argument_list|(
literal|"route"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|list
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|Node
name|node1
init|=
name|list
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Node
name|node2
init|=
name|list
operator|.
name|item
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|lineNumber1
init|=
operator|(
name|String
operator|)
name|node1
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER
argument_list|)
decl_stmt|;
name|String
name|lineNumberEnd1
init|=
operator|(
name|String
operator|)
name|node1
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER_END
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"31"
argument_list|,
name|lineNumber1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"37"
argument_list|,
name|lineNumberEnd1
argument_list|)
expr_stmt|;
name|String
name|lineNumber2
init|=
operator|(
name|String
operator|)
name|node2
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER
argument_list|)
decl_stmt|;
name|String
name|lineNumberEnd2
init|=
operator|(
name|String
operator|)
name|node2
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER_END
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"39"
argument_list|,
name|lineNumber2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"45"
argument_list|,
name|lineNumberEnd2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

