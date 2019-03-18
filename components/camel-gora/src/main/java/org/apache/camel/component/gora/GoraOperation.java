begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gora
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gora
package|;
end_package

begin_comment
comment|/**  * Camel-Gora Operations.  */
end_comment

begin_enum
DECL|enum|GoraOperation
specifier|public
enum|enum
name|GoraOperation
block|{
comment|/**      * Gora "put" operation      */
DECL|enumConstant|PUT
name|PUT
argument_list|(
literal|"put"
argument_list|)
block|,
comment|/**      * Gora fetch/"get" operation      */
DECL|enumConstant|GET
name|GET
argument_list|(
literal|"get"
argument_list|)
block|,
comment|/**      * Gora "delete" operation      */
DECL|enumConstant|DELETE
name|DELETE
argument_list|(
literal|"delete"
argument_list|)
block|,
comment|/**      * Gora "get schema name" operation      */
DECL|enumConstant|GET_SCHEMA_NAME
name|GET_SCHEMA_NAME
argument_list|(
literal|"getSchemaName"
argument_list|)
block|,
comment|/**      * Gora "delete schema" operation      */
DECL|enumConstant|DELETE_SCHEMA
name|DELETE_SCHEMA
argument_list|(
literal|"deleteSchema"
argument_list|)
block|,
comment|/**      * Gora "create schema" operation      */
DECL|enumConstant|CREATE_SCHEMA
name|CREATE_SCHEMA
argument_list|(
literal|"createSchema"
argument_list|)
block|,
comment|/**      * Gora "query" operation      */
DECL|enumConstant|QUERY
name|QUERY
argument_list|(
literal|"query"
argument_list|)
block|,
comment|/**      * Gora "deleteByQuery" operation      */
DECL|enumConstant|DELETE_BY_QUERY
name|DELETE_BY_QUERY
argument_list|(
literal|"deleteByQuery"
argument_list|)
block|,
comment|/**      * Gora "schemaExists" operation      */
DECL|enumConstant|SCHEMA_EXIST
name|SCHEMA_EXIST
argument_list|(
literal|"schemaExists"
argument_list|)
block|;
comment|/**      * Enum value      */
DECL|field|value
specifier|public
specifier|final
name|String
name|value
decl_stmt|;
comment|/**      * Enum constructor      */
DECL|method|GoraOperation (final String str)
name|GoraOperation
parameter_list|(
specifier|final
name|String
name|str
parameter_list|)
block|{
name|value
operator|=
name|str
expr_stmt|;
block|}
block|}
end_enum

end_unit

