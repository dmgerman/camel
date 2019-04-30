begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|swagger
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|jaxrs
operator|.
name|config
operator|.
name|BeanConfig
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
name|Operation
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
name|Path
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
name|models
operator|.
name|parameters
operator|.
name|Parameter
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
name|engine
operator|.
name|DefaultClassResolver
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
name|RestDefinition
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
name|RestParamType
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
DECL|class|RestSwaggerArrayEnumTest
specifier|public
class|class
name|RestSwaggerArrayEnumTest
block|{
annotation|@
name|Test
DECL|method|shouldGenerateEnumValuesForArraysAndNonArrays ()
specifier|public
name|void
name|shouldGenerateEnumValuesForArraysAndNonArrays
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|RestSwaggerReader
name|reader
init|=
operator|new
name|RestSwaggerReader
argument_list|()
decl_stmt|;
specifier|final
name|RestDefinition
name|restDefinition
init|=
operator|new
name|RestDefinition
argument_list|()
decl_stmt|;
name|restDefinition
operator|.
name|get
argument_list|(
literal|"/operation"
argument_list|)
operator|.
name|param
argument_list|()
operator|.
name|name
argument_list|(
literal|"pathParam"
argument_list|)
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|path
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"string"
argument_list|)
operator|.
name|allowableValues
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|param
argument_list|()
operator|.
name|name
argument_list|(
literal|"queryParam"
argument_list|)
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|query
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"int"
argument_list|)
operator|.
name|allowableValues
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|,
literal|"3"
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|param
argument_list|()
operator|.
name|name
argument_list|(
literal|"headerParam"
argument_list|)
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|header
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"float"
argument_list|)
operator|.
name|allowableValues
argument_list|(
literal|"1.1"
argument_list|,
literal|"2.2"
argument_list|,
literal|"3.3"
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|param
argument_list|()
operator|.
name|name
argument_list|(
literal|"pathArrayParam"
argument_list|)
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|path
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"array"
argument_list|)
operator|.
name|arrayType
argument_list|(
literal|"string"
argument_list|)
operator|.
name|allowableValues
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|param
argument_list|()
operator|.
name|name
argument_list|(
literal|"queryArrayParam"
argument_list|)
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|query
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"array"
argument_list|)
operator|.
name|arrayType
argument_list|(
literal|"int"
argument_list|)
operator|.
name|allowableValues
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|,
literal|"3"
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|param
argument_list|()
operator|.
name|name
argument_list|(
literal|"headerArrayParam"
argument_list|)
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|header
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"array"
argument_list|)
operator|.
name|arrayType
argument_list|(
literal|"float"
argument_list|)
operator|.
name|allowableValues
argument_list|(
literal|"1.1"
argument_list|,
literal|"2.2"
argument_list|,
literal|"3.3"
argument_list|)
operator|.
name|endParam
argument_list|()
expr_stmt|;
specifier|final
name|Swagger
name|swagger
init|=
name|reader
operator|.
name|read
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|restDefinition
argument_list|)
argument_list|,
literal|null
argument_list|,
operator|new
name|BeanConfig
argument_list|()
argument_list|,
literal|"camel-1"
argument_list|,
operator|new
name|DefaultClassResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|swagger
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Path
argument_list|>
name|paths
init|=
name|swagger
operator|.
name|getPaths
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|paths
argument_list|)
operator|.
name|containsKey
argument_list|(
literal|"/operation"
argument_list|)
expr_stmt|;
specifier|final
name|Operation
name|getOperation
init|=
name|paths
operator|.
name|get
argument_list|(
literal|"/operation"
argument_list|)
operator|.
name|getGet
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|getOperation
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Parameter
argument_list|>
name|parameters
init|=
name|getOperation
operator|.
name|getParameters
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|parameters
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|ParameterAssert
operator|.
name|assertThat
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|hasName
argument_list|(
literal|"pathParam"
argument_list|)
operator|.
name|isGivenIn
argument_list|(
literal|"path"
argument_list|)
operator|.
name|isOfType
argument_list|(
literal|"string"
argument_list|)
operator|.
name|hasEnumSpecifiedWith
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
expr_stmt|;
name|ParameterAssert
operator|.
name|assertThat
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|hasName
argument_list|(
literal|"queryParam"
argument_list|)
operator|.
name|isGivenIn
argument_list|(
literal|"query"
argument_list|)
operator|.
name|isOfType
argument_list|(
literal|"string"
argument_list|)
operator|.
name|hasEnumSpecifiedWith
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|ParameterAssert
operator|.
name|assertThat
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|hasName
argument_list|(
literal|"headerParam"
argument_list|)
operator|.
name|isGivenIn
argument_list|(
literal|"header"
argument_list|)
operator|.
name|isOfType
argument_list|(
literal|"string"
argument_list|)
operator|.
name|hasEnumSpecifiedWith
argument_list|(
literal|"1.1"
argument_list|,
literal|"2.2"
argument_list|,
literal|"3.3"
argument_list|)
expr_stmt|;
name|ParameterAssert
operator|.
name|assertThat
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|.
name|hasName
argument_list|(
literal|"pathArrayParam"
argument_list|)
operator|.
name|isGivenIn
argument_list|(
literal|"path"
argument_list|)
operator|.
name|isOfType
argument_list|(
literal|"array"
argument_list|)
operator|.
name|isOfArrayType
argument_list|(
literal|"string"
argument_list|)
operator|.
name|hasArrayEnumSpecifiedWith
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
expr_stmt|;
name|ParameterAssert
operator|.
name|assertThat
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|)
operator|.
name|hasName
argument_list|(
literal|"queryParam"
argument_list|)
operator|.
name|isGivenIn
argument_list|(
literal|"query"
argument_list|)
operator|.
name|isOfType
argument_list|(
literal|"array"
argument_list|)
operator|.
name|isOfArrayType
argument_list|(
literal|"int"
argument_list|)
operator|.
name|hasArrayEnumSpecifiedWith
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|ParameterAssert
operator|.
name|assertThat
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|)
operator|.
name|hasName
argument_list|(
literal|"headerParam"
argument_list|)
operator|.
name|isGivenIn
argument_list|(
literal|"header"
argument_list|)
operator|.
name|isOfType
argument_list|(
literal|"array"
argument_list|)
operator|.
name|isOfArrayType
argument_list|(
literal|"float"
argument_list|)
operator|.
name|hasArrayEnumSpecifiedWith
argument_list|(
literal|1.1f
argument_list|,
literal|2.2f
argument_list|,
literal|3.3f
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

