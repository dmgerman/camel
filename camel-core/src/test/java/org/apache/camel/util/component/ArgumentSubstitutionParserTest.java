begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|component
package|;
end_package

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
name|util
operator|.
name|component
operator|.
name|ArgumentSubstitutionParser
operator|.
name|Substitution
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
DECL|class|ArgumentSubstitutionParserTest
specifier|public
class|class
name|ArgumentSubstitutionParserTest
block|{
DECL|field|PERSON
specifier|private
specifier|static
specifier|final
name|String
name|PERSON
init|=
literal|"person"
decl_stmt|;
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
specifier|final
name|Substitution
index|[]
name|adapters
init|=
operator|new
name|Substitution
index|[
literal|4
index|]
decl_stmt|;
name|adapters
index|[
literal|0
index|]
operator|=
operator|new
name|Substitution
argument_list|(
literal|".+"
argument_list|,
literal|"name"
argument_list|,
name|PERSON
argument_list|)
expr_stmt|;
name|adapters
index|[
literal|1
index|]
operator|=
operator|new
name|Substitution
argument_list|(
literal|"greet.+"
argument_list|,
literal|"person([0-9]+)"
argument_list|,
literal|"astronaut$1"
argument_list|)
expr_stmt|;
name|adapters
index|[
literal|2
index|]
operator|=
operator|new
name|Substitution
argument_list|(
literal|".+"
argument_list|,
literal|"(.+)"
argument_list|,
literal|"java.util.List"
argument_list|,
literal|"$1List"
argument_list|)
expr_stmt|;
name|adapters
index|[
literal|3
index|]
operator|=
operator|new
name|Substitution
argument_list|(
literal|".+"
argument_list|,
literal|"(.+)"
argument_list|,
literal|".*?(\\w++)\\[\\]"
argument_list|,
literal|"$1Array"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|ApiMethodParser
argument_list|<
name|TestProxy
argument_list|>
name|parser
init|=
operator|new
name|ArgumentSubstitutionParser
argument_list|<>
argument_list|(
name|TestProxy
operator|.
name|class
argument_list|,
name|adapters
argument_list|)
decl_stmt|;
specifier|final
name|ArrayList
argument_list|<
name|String
argument_list|>
name|signatures
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|signatures
operator|.
name|add
argument_list|(
literal|"public String sayHi();"
argument_list|)
expr_stmt|;
name|signatures
operator|.
name|add
argument_list|(
literal|"public String sayHi(final String name);"
argument_list|)
expr_stmt|;
name|signatures
operator|.
name|add
argument_list|(
literal|"public final String greetMe(final String name);"
argument_list|)
expr_stmt|;
name|signatures
operator|.
name|add
argument_list|(
literal|"public final String greetUs(final String name1, String name2);"
argument_list|)
expr_stmt|;
name|signatures
operator|.
name|add
argument_list|(
literal|"public final String greetAll(String[] names);"
argument_list|)
expr_stmt|;
name|signatures
operator|.
name|add
argument_list|(
literal|"public final String greetAll(java.util.List<String> names);"
argument_list|)
expr_stmt|;
name|signatures
operator|.
name|add
argument_list|(
literal|"public final java.util.Map<String, String> greetAll(java.util.Map<String> nameMap);"
argument_list|)
expr_stmt|;
name|signatures
operator|.
name|add
argument_list|(
literal|"public final String[] greetTimes(String name, int times);"
argument_list|)
expr_stmt|;
name|signatures
operator|.
name|add
argument_list|(
literal|"public final String greetInnerChild(org.apache.camel.util.component.TestProxy.InnerChild child);"
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setSignatures
argument_list|(
name|signatures
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|ApiMethodParser
operator|.
name|ApiMethodModel
argument_list|>
name|methodModels
init|=
name|parser
operator|.
name|parse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|methodModels
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ApiMethodParser
operator|.
name|ApiMethodModel
name|sayHi1
init|=
name|methodModels
operator|.
name|get
argument_list|(
literal|8
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PERSON
argument_list|,
name|sayHi1
operator|.
name|getArguments
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"SAYHI_1"
argument_list|,
name|sayHi1
operator|.
name|getUniqueName
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ApiMethodParser
operator|.
name|ApiMethodModel
name|greetMe
init|=
name|methodModels
operator|.
name|get
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PERSON
argument_list|,
name|greetMe
operator|.
name|getArguments
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ApiMethodParser
operator|.
name|ApiMethodModel
name|greetUs
init|=
name|methodModels
operator|.
name|get
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"astronaut1"
argument_list|,
name|greetUs
operator|.
name|getArguments
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"astronaut2"
argument_list|,
name|greetUs
operator|.
name|getArguments
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ApiMethodParser
operator|.
name|ApiMethodModel
name|greetAll
init|=
name|methodModels
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"personMap"
argument_list|,
name|greetAll
operator|.
name|getArguments
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ApiMethodParser
operator|.
name|ApiMethodModel
name|greetAll1
init|=
name|methodModels
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"personsList"
argument_list|,
name|greetAll1
operator|.
name|getArguments
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ApiMethodParser
operator|.
name|ApiMethodModel
name|greetAll2
init|=
name|methodModels
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"stringArray"
argument_list|,
name|greetAll2
operator|.
name|getArguments
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|ApiMethodParser
operator|.
name|ApiMethodModel
name|greetInnerChild
init|=
name|methodModels
operator|.
name|get
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"child"
argument_list|,
name|greetInnerChild
operator|.
name|getArguments
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

