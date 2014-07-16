begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
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
name|blueprint
operator|.
name|handler
operator|.
name|CamelNamespaceHandler
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
name|TestSupport
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
name|Element
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

begin_class
DECL|class|BlueprintJaxbRestTest
specifier|public
class|class
name|BlueprintJaxbRestTest
extends|extends
name|TestSupport
block|{
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isJava16
argument_list|()
operator|&&
name|isJavaVendor
argument_list|(
literal|"ibm"
argument_list|)
condition|)
block|{
comment|// does not test well on java6 with ibm
return|return;
block|}
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|db
operator|.
name|parse
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-rest.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|Element
name|elem
init|=
literal|null
decl_stmt|;
name|NodeList
name|nl
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nl
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|nl
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|elem
operator|=
operator|(
name|Element
operator|)
name|node
expr_stmt|;
break|break;
block|}
block|}
name|CamelNamespaceHandler
operator|.
name|renameNamespaceRecursive
argument_list|(
name|elem
argument_list|,
name|CamelNamespaceHandler
operator|.
name|BLUEPRINT_NS
argument_list|,
name|CamelNamespaceHandler
operator|.
name|SPRING_NS
argument_list|)
expr_stmt|;
name|JAXBContext
name|context
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
literal|"org.apache.camel.blueprint:"
operator|+
literal|"org.apache.camel:org.apache.camel.model:"
operator|+
literal|"org.apache.camel.model.config:"
operator|+
literal|"org.apache.camel.model.dataformat:"
operator|+
literal|"org.apache.camel.model.language:"
operator|+
literal|"org.apache.camel.model.loadbalancer:"
operator|+
literal|"org.apache.camel.model.rest"
argument_list|)
decl_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|context
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|Object
name|object
init|=
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|elem
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|object
operator|instanceof
name|CamelContextFactoryBean
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
operator|(
operator|(
name|CamelContextFactoryBean
operator|)
name|object
operator|)
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
operator|(
operator|(
name|CamelContextFactoryBean
operator|)
name|object
operator|)
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CamelContextFactoryBean
name|cfb
init|=
operator|(
name|CamelContextFactoryBean
operator|)
name|object
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cfb
operator|.
name|getRests
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cfb
operator|.
name|getRests
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPaths
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/say/hello"
argument_list|,
name|cfb
operator|.
name|getRests
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPaths
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/say/bye"
argument_list|,
name|cfb
operator|.
name|getRests
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPaths
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cfb
operator|.
name|getRests
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPaths
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cfb
operator|.
name|getRests
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPaths
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"get"
argument_list|,
name|cfb
operator|.
name|getRests
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPaths
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"get"
argument_list|,
name|cfb
operator|.
name|getRests
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPaths
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"post"
argument_list|,
name|cfb
operator|.
name|getRests
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPaths
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

