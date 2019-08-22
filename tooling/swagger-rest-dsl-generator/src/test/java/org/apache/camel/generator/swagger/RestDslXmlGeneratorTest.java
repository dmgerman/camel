begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.generator.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|generator
operator|.
name|swagger
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|Swagger
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|parser
operator|.
name|SwaggerParser
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
name|impl
operator|.
name|DefaultCamelContext
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
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|RestDslXmlGeneratorTest
specifier|public
class|class
name|RestDslXmlGeneratorTest
block|{
DECL|field|swagger
specifier|final
name|Swagger
name|swagger
init|=
operator|new
name|SwaggerParser
argument_list|()
operator|.
name|read
argument_list|(
literal|"petstore.json"
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldGenerateBlueprintXml ()
specifier|public
name|void
name|shouldGenerateBlueprintXml
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|String
name|xml
init|=
name|RestDslGenerator
operator|.
name|toXml
argument_list|(
name|swagger
argument_list|)
operator|.
name|withBlueprint
argument_list|()
operator|.
name|generate
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|xml
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"http://camel.apache.org/schema/blueprint"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldGenerateXml ()
specifier|public
name|void
name|shouldGenerateXml
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|String
name|xml
init|=
name|RestDslGenerator
operator|.
name|toXml
argument_list|(
name|swagger
argument_list|)
operator|.
name|generate
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|xml
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"http://camel.apache.org/schema/spring"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldGenerateXmlWithDefaultnamespace ()
specifier|public
name|void
name|shouldGenerateXmlWithDefaultnamespace
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|String
name|xml
init|=
name|RestDslGenerator
operator|.
name|toXml
argument_list|(
name|swagger
argument_list|)
operator|.
name|generate
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|DocumentBuilderFactory
name|builderFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|builderFactory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|DocumentBuilder
name|builder
init|=
name|builderFactory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
specifier|final
name|Document
name|document
init|=
name|builder
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|xml
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|document
operator|.
name|isDefaultNamespace
argument_list|(
literal|"http://camel.apache.org/schema/spring"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldGenerateXmlWithDefaults ()
specifier|public
name|void
name|shouldGenerateXmlWithDefaults
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|String
name|xml
init|=
name|RestDslGenerator
operator|.
name|toXml
argument_list|(
name|swagger
argument_list|)
operator|.
name|generate
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|URI
name|file
init|=
name|RestDslGeneratorTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/SwaggerPetstoreXml.txt"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
specifier|final
name|String
name|expectedContent
init|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|file
argument_list|)
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|xml
argument_list|)
operator|.
name|isXmlEqualTo
argument_list|(
name|expectedContent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldGenerateXmlWithRestComponent ()
specifier|public
name|void
name|shouldGenerateXmlWithRestComponent
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|String
name|xml
init|=
name|RestDslGenerator
operator|.
name|toXml
argument_list|(
name|swagger
argument_list|)
operator|.
name|withRestComponent
argument_list|(
literal|"servlet"
argument_list|)
operator|.
name|withRestContextPath
argument_list|(
literal|"/foo"
argument_list|)
operator|.
name|generate
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|URI
name|file
init|=
name|RestDslGeneratorTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/SwaggerPetstoreWithRestComponentXml.txt"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
specifier|final
name|String
name|expectedContent
init|=
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|file
argument_list|)
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|xml
argument_list|)
operator|.
name|isXmlEqualTo
argument_list|(
name|expectedContent
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

