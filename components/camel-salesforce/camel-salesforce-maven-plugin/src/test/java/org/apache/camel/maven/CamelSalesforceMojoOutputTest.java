begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
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
name|io
operator|.
name|IOException
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
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|SObjectDescription
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|utils
operator|.
name|JsonUtils
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
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|testing
operator|.
name|SilentLog
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
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
name|junit
operator|.
name|rules
operator|.
name|TemporaryFolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
DECL|class|CamelSalesforceMojoOutputTest
specifier|public
class|class
name|CamelSalesforceMojoOutputTest
block|{
DECL|field|TEST_CASE_FILE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_CASE_FILE
init|=
literal|"case.json"
decl_stmt|;
DECL|field|TEST_CALCULATED_FORMULA_FILE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_CALCULATED_FORMULA_FILE
init|=
literal|"complex_calculated_formula.json"
decl_stmt|;
DECL|field|FIXED_DATE
specifier|private
specifier|static
specifier|final
name|String
name|FIXED_DATE
init|=
literal|"Thu Mar 09 16:15:49 ART 2017"
decl_stmt|;
annotation|@
name|Rule
DECL|field|temp
specifier|public
name|TemporaryFolder
name|temp
init|=
operator|new
name|TemporaryFolder
argument_list|()
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
literal|0
argument_list|)
DECL|field|json
specifier|public
name|String
name|json
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
literal|1
argument_list|)
DECL|field|description
specifier|public
name|SObjectDescription
name|description
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
literal|2
argument_list|)
DECL|field|source
specifier|public
name|String
name|source
decl_stmt|;
DECL|field|mojo
specifier|private
name|CamelSalesforceMojo
name|mojo
decl_stmt|;
DECL|field|utility
specifier|private
name|CamelSalesforceMojo
operator|.
name|GeneratorUtility
name|utility
init|=
operator|new
name|CamelSalesforceMojo
operator|.
name|GeneratorUtility
argument_list|(
literal|false
argument_list|,
operator|new
name|SilentLog
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Parameters
argument_list|(
name|name
operator|=
literal|"json = {0}, source = {2}"
argument_list|)
DECL|method|parameters ()
specifier|public
specifier|static
name|Iterable
argument_list|<
name|Object
index|[]
argument_list|>
name|parameters
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|pair
argument_list|(
name|TEST_CASE_FILE
argument_list|,
literal|"Case.java"
argument_list|)
argument_list|,
name|pair
argument_list|(
name|TEST_CASE_FILE
argument_list|,
literal|"Case_PickListAccentMarkEnum.java"
argument_list|)
argument_list|,
name|pair
argument_list|(
name|TEST_CASE_FILE
argument_list|,
literal|"Case_PickListQuotationMarkEnum.java"
argument_list|)
argument_list|,
name|pair
argument_list|(
name|TEST_CASE_FILE
argument_list|,
literal|"Case_PickListSlashEnum.java"
argument_list|)
argument_list|,
name|pair
argument_list|(
name|TEST_CASE_FILE
argument_list|,
literal|"QueryRecordsCase.java"
argument_list|)
argument_list|,
name|pair
argument_list|(
name|TEST_CALCULATED_FORMULA_FILE
argument_list|,
literal|"ComplexCalculatedFormula.java"
argument_list|)
argument_list|,
name|pair
argument_list|(
name|TEST_CALCULATED_FORMULA_FILE
argument_list|,
literal|"QueryRecordsComplexCalculatedFormula.java"
argument_list|)
argument_list|)
return|;
block|}
DECL|method|pair (String json, String source)
specifier|static
name|Object
index|[]
name|pair
parameter_list|(
name|String
name|json
parameter_list|,
name|String
name|source
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|Object
index|[]
block|{
name|json
block|,
name|createSObjectDescription
argument_list|(
name|json
argument_list|)
block|,
name|source
block|}
return|;
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|mojo
operator|=
operator|new
name|CamelSalesforceMojo
argument_list|()
expr_stmt|;
name|mojo
operator|.
name|engine
operator|=
name|CamelSalesforceMojo
operator|.
name|createVelocityEngine
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProcessDescriptionPickLists ()
specifier|public
name|void
name|testProcessDescriptionPickLists
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|File
name|pkgDir
init|=
name|temp
operator|.
name|newFolder
argument_list|()
decl_stmt|;
name|mojo
operator|.
name|processDescription
argument_list|(
name|pkgDir
argument_list|,
name|description
argument_list|,
name|utility
argument_list|,
name|FIXED_DATE
argument_list|)
expr_stmt|;
name|File
name|generatedFile
init|=
operator|new
name|File
argument_list|(
name|pkgDir
argument_list|,
name|source
argument_list|)
decl_stmt|;
name|String
name|generatedContent
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|generatedFile
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
if|if
condition|(
name|TestSupport
operator|.
name|getJavaMajorVersion
argument_list|()
operator|>=
literal|9
operator|&&
operator|(
name|source
operator|.
name|equals
argument_list|(
literal|"Case.java"
argument_list|)
operator|||
name|source
operator|.
name|equals
argument_list|(
literal|"ComplexCalculatedFormula.java"
argument_list|)
operator|)
condition|)
block|{
comment|//Content is the same, the ordering is a bit different.
name|source
operator|+=
literal|"-Java9"
expr_stmt|;
block|}
name|String
name|expectedContent
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|CamelSalesforceMojoOutputTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/generated/"
operator|+
name|source
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Generated source file in "
operator|+
name|source
operator|+
literal|" must be equal to the one present in test/resources"
argument_list|,
name|generatedContent
argument_list|,
name|expectedContent
argument_list|)
expr_stmt|;
block|}
DECL|method|createSObjectDescription (String name)
specifier|static
name|SObjectDescription
name|createSObjectDescription
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|InputStream
name|inputStream
init|=
name|CamelSalesforceMojoOutputTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/"
operator|+
name|name
argument_list|)
init|)
block|{
name|ObjectMapper
name|mapper
init|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
decl_stmt|;
return|return
name|mapper
operator|.
name|readValue
argument_list|(
name|inputStream
argument_list|,
name|SObjectDescription
operator|.
name|class
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

