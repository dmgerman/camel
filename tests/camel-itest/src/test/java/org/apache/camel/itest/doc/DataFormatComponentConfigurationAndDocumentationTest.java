begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.doc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|doc
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Map
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
name|CatalogCamelContext
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
name|support
operator|.
name|JSonSchemaHelper
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
name|CamelTestSupport
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

begin_class
DECL|class|DataFormatComponentConfigurationAndDocumentationTest
specifier|public
class|class
name|DataFormatComponentConfigurationAndDocumentationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testFlatpackDefaultValue ()
specifier|public
name|void
name|testFlatpackDefaultValue
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|String
name|json
init|=
name|context
operator|.
name|adapt
argument_list|(
name|CatalogCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getEipParameterJsonSchema
argument_list|(
literal|"flatpack"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"name\": \"flatpack"
argument_list|)
argument_list|)
expr_stmt|;
comment|// the default value is a bit tricky as its ", which is written escaped as \"
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"textQualifier\": { \"kind\": \"attribute\", \"displayName\": \"Text Qualifier\", \"required\": false, \"type\": \"string\""
operator|+
literal|", \"javaType\": \"java.lang.String\", \"deprecated\": false, \"secret\": false"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"properties"
argument_list|,
name|json
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|rows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|found
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
literal|"textQualifier"
operator|.
name|equals
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
condition|)
block|{
name|found
operator|=
name|row
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
name|found
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"textQualifier"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"attribute"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"kind"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"required"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"string"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.String"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"deprecated"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"secret"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"If the text is qualified with a character. Uses quote character by default."
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUniVocityTsvEscapeChar ()
specifier|public
name|void
name|testUniVocityTsvEscapeChar
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|String
name|json
init|=
name|context
operator|.
name|adapt
argument_list|(
name|CatalogCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getEipParameterJsonSchema
argument_list|(
literal|"univocity-tsv"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"name\": \"univocity-tsv"
argument_list|)
argument_list|)
expr_stmt|;
comment|// the default value is a bit tricky as its \, which is written escaped as \\
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"escapeChar\": { \"kind\": \"attribute\", \"displayName\": \"Escape Char\", \"required\": false, \"type\": \"string\", \"javaType\": \"java.lang.String\","
operator|+
literal|" \"deprecated\": false, \"secret\": false, \"defaultValue\": \"\\\\\", \"description\": \"The escape character.\""
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"properties"
argument_list|,
name|json
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|16
argument_list|,
name|rows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|found
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
literal|"escapeChar"
operator|.
name|equals
argument_list|(
name|row
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
condition|)
block|{
name|found
operator|=
name|row
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
name|found
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"escapeChar"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"attribute"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"kind"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"required"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"string"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.String"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"deprecated"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"false"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"secret"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"\\"
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"defaultValue"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The escape character."
argument_list|,
name|found
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

