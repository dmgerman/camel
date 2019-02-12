begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.runtimecatalog.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|runtimecatalog
operator|.
name|impl
package|;
end_package

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
name|util
operator|.
name|HashMap
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
name|runtimecatalog
operator|.
name|JSonSchemaResolver
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

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|AbstractCamelCatalogTest
specifier|public
class|class
name|AbstractCamelCatalogTest
block|{
DECL|field|catalog
name|AbstractCamelCatalog
name|catalog
init|=
operator|new
name|AbstractCamelCatalog
argument_list|()
block|{     }
decl_stmt|;
DECL|field|resolver
name|JSonSchemaResolver
name|resolver
decl_stmt|;
annotation|@
name|Before
DECL|method|setupMockCatalog ()
specifier|public
name|void
name|setupMockCatalog
parameter_list|()
block|{
name|resolver
operator|=
name|mock
argument_list|(
name|JSonSchemaResolver
operator|.
name|class
argument_list|)
expr_stmt|;
name|catalog
operator|.
name|setJSonSchemaResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConstructEndpointUris ()
specifier|public
name|void
name|shouldConstructEndpointUris
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|when
argument_list|(
name|resolver
operator|.
name|getComponentJSonSchema
argument_list|(
literal|"comp"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"{\n"
comment|//
operator|+
literal|"  \"component\": {\n"
comment|//
operator|+
literal|"    \"syntax\": \"comp:param1:param2\"\n"
comment|//
operator|+
literal|"  }\n"
comment|//
operator|+
literal|"}"
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"param1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"param2"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"param3"
argument_list|,
literal|"value3"
argument_list|)
expr_stmt|;
specifier|final
name|String
name|endpointUri
init|=
name|catalog
operator|.
name|doAsEndpointUri
argument_list|(
literal|"comp"
argument_list|,
name|properties
argument_list|,
literal|"&"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"comp:value1:value2?param3=value3"
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConstructEndpointUrisWithPropertyPlaceholders ()
specifier|public
name|void
name|shouldConstructEndpointUrisWithPropertyPlaceholders
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|when
argument_list|(
name|resolver
operator|.
name|getComponentJSonSchema
argument_list|(
literal|"comp"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"{\n"
comment|//
operator|+
literal|"  \"component\": {\n"
comment|//
operator|+
literal|"    \"syntax\": \"comp:param1:param2\"\n"
comment|//
operator|+
literal|"  }\n"
comment|//
operator|+
literal|"}"
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"param1"
argument_list|,
literal|"{{prop1}}"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"param2"
argument_list|,
literal|"{{prop2}}"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"param3"
argument_list|,
literal|"{{prop3}}"
argument_list|)
expr_stmt|;
specifier|final
name|String
name|endpointUri
init|=
name|catalog
operator|.
name|doAsEndpointUri
argument_list|(
literal|"comp"
argument_list|,
name|properties
argument_list|,
literal|"&"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"comp:{{prop1}}:{{prop2}}?param3={{prop3}}"
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConstructEndpointUrisWhenValuesContainTokens ()
specifier|public
name|void
name|shouldConstructEndpointUrisWhenValuesContainTokens
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|when
argument_list|(
name|resolver
operator|.
name|getComponentJSonSchema
argument_list|(
literal|"comp"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"{\n"
comment|//
operator|+
literal|"  \"component\": {\n"
comment|//
operator|+
literal|"    \"syntax\": \"comp:param1:param2\"\n"
comment|//
operator|+
literal|"  }\n"
comment|//
operator|+
literal|"}"
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"param1"
argument_list|,
literal|"{value1}"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"param2"
argument_list|,
literal|"/value2/"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"param3"
argument_list|,
literal|"/value3/{param}"
argument_list|)
expr_stmt|;
specifier|final
name|String
name|endpointUri
init|=
name|catalog
operator|.
name|doAsEndpointUri
argument_list|(
literal|"comp"
argument_list|,
name|properties
argument_list|,
literal|"&"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"comp:{value1}:/value2/?param3=/value3/{param}"
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldContextPathAndQuery ()
specifier|public
name|void
name|shouldContextPathAndQuery
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|when
argument_list|(
name|resolver
operator|.
name|getComponentJSonSchema
argument_list|(
literal|"comp"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"{\n"
comment|//
operator|+
literal|"  \"component\": {\n"
comment|//
operator|+
literal|"    \"syntax\": \"comp:value1\"\n"
comment|//
operator|+
literal|"  }\n"
comment|//
operator|+
literal|"}"
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"value1"
argument_list|,
literal|"camel"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
specifier|final
name|String
name|endpointUri
init|=
name|catalog
operator|.
name|doAsEndpointUri
argument_list|(
literal|"comp"
argument_list|,
name|properties
argument_list|,
literal|"&"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"comp:camel?foo=123"
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldEmptyContextPath ()
specifier|public
name|void
name|shouldEmptyContextPath
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|when
argument_list|(
name|resolver
operator|.
name|getComponentJSonSchema
argument_list|(
literal|"comp"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"{\n"
comment|//
operator|+
literal|"  \"component\": {\n"
comment|//
operator|+
literal|"    \"syntax\": \"comp\"\n"
comment|//
operator|+
literal|"  }\n"
comment|//
operator|+
literal|"}"
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|String
name|endpointUri
init|=
name|catalog
operator|.
name|doAsEndpointUri
argument_list|(
literal|"comp"
argument_list|,
name|properties
argument_list|,
literal|"&"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"comp"
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldEmptyContextPathWithQuery ()
specifier|public
name|void
name|shouldEmptyContextPathWithQuery
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|when
argument_list|(
name|resolver
operator|.
name|getComponentJSonSchema
argument_list|(
literal|"comp"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"{\n"
comment|//
operator|+
literal|"  \"component\": {\n"
comment|//
operator|+
literal|"    \"syntax\": \"comp\"\n"
comment|//
operator|+
literal|"  }\n"
comment|//
operator|+
literal|"}"
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
specifier|final
name|String
name|endpointUri
init|=
name|catalog
operator|.
name|doAsEndpointUri
argument_list|(
literal|"comp"
argument_list|,
name|properties
argument_list|,
literal|"&"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"comp?foo=123"
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
