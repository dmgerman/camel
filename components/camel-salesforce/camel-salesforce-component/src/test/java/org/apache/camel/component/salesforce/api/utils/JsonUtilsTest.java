begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.utils
package|package
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
package|;
end_package

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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|module
operator|.
name|jsonSchema
operator|.
name|JsonSchema
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
name|module
operator|.
name|jsonSchema
operator|.
name|types
operator|.
name|ObjectSchema
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
name|dto
operator|.
name|generated
operator|.
name|Account
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link JsonUtils}  */
end_comment

begin_class
DECL|class|JsonUtilsTest
specifier|public
class|class
name|JsonUtilsTest
block|{
DECL|field|LOG
specifier|public
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JsonUtilsTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|getBasicApiJsonSchema ()
specifier|public
name|void
name|getBasicApiJsonSchema
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create basic api dto schema
name|LOG
operator|.
name|info
argument_list|(
literal|"Basic Api Schema..."
argument_list|)
expr_stmt|;
name|String
name|basicApiJsonSchema
init|=
name|JsonUtils
operator|.
name|getBasicApiJsonSchema
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
name|basicApiJsonSchema
argument_list|)
expr_stmt|;
comment|// parse schema to validate
name|ObjectMapper
name|objectMapper
init|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
decl_stmt|;
name|JsonSchema
name|jsonSchema
init|=
name|objectMapper
operator|.
name|readValue
argument_list|(
name|basicApiJsonSchema
argument_list|,
name|JsonSchema
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|jsonSchema
operator|.
name|isObjectSchema
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|ObjectSchema
operator|)
name|jsonSchema
operator|)
operator|.
name|getOneOf
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getSObjectJsonSchema ()
specifier|public
name|void
name|getSObjectJsonSchema
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create sobject dto schema
name|SObjectDescription
name|description
init|=
operator|new
name|Account
argument_list|()
operator|.
name|description
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"SObject Schema..."
argument_list|)
expr_stmt|;
name|String
name|sObjectJsonSchema
init|=
name|JsonUtils
operator|.
name|getSObjectJsonSchema
argument_list|(
name|description
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
name|sObjectJsonSchema
argument_list|)
expr_stmt|;
comment|// parse schema to validate
name|ObjectMapper
name|objectMapper
init|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
decl_stmt|;
name|JsonSchema
name|jsonSchema
init|=
name|objectMapper
operator|.
name|readValue
argument_list|(
name|sObjectJsonSchema
argument_list|,
name|JsonSchema
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|jsonSchema
operator|.
name|isObjectSchema
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
operator|(
name|ObjectSchema
operator|)
name|jsonSchema
operator|)
operator|.
name|getOneOf
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

