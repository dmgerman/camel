begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|IOException
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
name|net
operator|.
name|URISyntaxException
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
name|java
operator|.
name|time
operator|.
name|Instant
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
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
operator|.
name|RestsDefinition
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
DECL|class|RestDslGeneratorTest
specifier|public
class|class
name|RestDslGeneratorTest
block|{
DECL|field|generated
specifier|final
name|Instant
name|generated
init|=
name|Instant
operator|.
name|parse
argument_list|(
literal|"2017-10-17T00:00:00.000Z"
argument_list|)
decl_stmt|;
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
DECL|method|shouldCreateDefinitions ()
specifier|public
name|void
name|shouldCreateDefinitions
parameter_list|()
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
name|RestsDefinition
name|definition
init|=
name|RestDslGenerator
operator|.
name|toDefinition
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
name|definition
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldGenerateSourceCodeWithDefaults ()
specifier|public
name|void
name|shouldGenerateSourceCodeWithDefaults
parameter_list|()
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
specifier|final
name|StringBuilder
name|code
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|RestDslGenerator
operator|.
name|toAppendable
argument_list|(
name|swagger
argument_list|)
operator|.
name|withGeneratedTime
argument_list|(
name|generated
argument_list|)
operator|.
name|generate
argument_list|(
name|code
argument_list|)
expr_stmt|;
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
literal|"/SwaggerPetstore.txt"
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
name|code
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedContent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldGenerateSourceCodeWithRestComponent ()
specifier|public
name|void
name|shouldGenerateSourceCodeWithRestComponent
parameter_list|()
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
specifier|final
name|StringBuilder
name|code
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|RestDslGenerator
operator|.
name|toAppendable
argument_list|(
name|swagger
argument_list|)
operator|.
name|withGeneratedTime
argument_list|(
name|generated
argument_list|)
operator|.
name|withRestComponent
argument_list|(
literal|"servlet"
argument_list|)
operator|.
name|generate
argument_list|(
name|code
argument_list|)
expr_stmt|;
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
literal|"/SwaggerPetstoreWithRestComponent.txt"
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
name|code
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedContent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldGenerateSourceCodeWithOptions ()
specifier|public
name|void
name|shouldGenerateSourceCodeWithOptions
parameter_list|()
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
specifier|final
name|StringBuilder
name|code
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|RestDslGenerator
operator|.
name|toAppendable
argument_list|(
name|swagger
argument_list|)
operator|.
name|withGeneratedTime
argument_list|(
name|generated
argument_list|)
operator|.
name|withClassName
argument_list|(
literal|"MyRestRoute"
argument_list|)
operator|.
name|withPackageName
argument_list|(
literal|"com.example"
argument_list|)
operator|.
name|withIndent
argument_list|(
literal|"\t"
argument_list|)
operator|.
name|withSourceCodeTimestamps
argument_list|()
operator|.
name|withDestinationGenerator
argument_list|(
name|o
lambda|->
literal|"direct:rest-"
operator|+
name|o
operator|.
name|getOperationId
argument_list|()
argument_list|)
operator|.
name|generate
argument_list|(
name|code
argument_list|)
expr_stmt|;
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
literal|"/MyRestRoute.txt"
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
name|code
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedContent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldGenerateSourceCodeWithFilter ()
specifier|public
name|void
name|shouldGenerateSourceCodeWithFilter
parameter_list|()
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
specifier|final
name|StringBuilder
name|code
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|RestDslGenerator
operator|.
name|toAppendable
argument_list|(
name|swagger
argument_list|)
operator|.
name|withGeneratedTime
argument_list|(
name|generated
argument_list|)
operator|.
name|withClassName
argument_list|(
literal|"MyRestRoute"
argument_list|)
operator|.
name|withPackageName
argument_list|(
literal|"com.example"
argument_list|)
operator|.
name|withIndent
argument_list|(
literal|"\t"
argument_list|)
operator|.
name|withSourceCodeTimestamps
argument_list|()
operator|.
name|withOperationFilter
argument_list|(
literal|"find*,deletePet,updatePet"
argument_list|)
operator|.
name|withDestinationGenerator
argument_list|(
name|o
lambda|->
literal|"direct:rest-"
operator|+
name|o
operator|.
name|getOperationId
argument_list|()
argument_list|)
operator|.
name|generate
argument_list|(
name|code
argument_list|)
expr_stmt|;
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
literal|"/MyRestRouteFilter.txt"
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
name|code
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedContent
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

