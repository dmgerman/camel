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
DECL|class|SimpleLanguageConfigurationAndDocumentationTest
specifier|public
class|class
name|SimpleLanguageConfigurationAndDocumentationTest
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
DECL|method|testLanguageJsonSchema ()
specifier|public
name|void
name|testLanguageJsonSchema
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
name|getLanguageParameterJsonSchema
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have found some auto-generated JSON"
argument_list|,
name|json
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
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
literal|"\"name\": \"simple\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"modelName\": \"simple\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"resultType\": { \"kind\": \"attribute\", \"displayName\": \"Result Type\", \"required\": false, \"type\": \"string\""
operator|+
literal|", \"javaType\": \"java.lang.String\", \"deprecated\": false"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

