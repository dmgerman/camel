begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.connector
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|connector
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

begin_interface
DECL|interface|ConnectorDataStore
specifier|public
interface|interface
name|ConnectorDataStore
block|{
DECL|method|addConnector (ConnectorDto dto, String connectorJson, String connectorSchemaJson)
name|void
name|addConnector
parameter_list|(
name|ConnectorDto
name|dto
parameter_list|,
name|String
name|connectorJson
parameter_list|,
name|String
name|connectorSchemaJson
parameter_list|)
function_decl|;
DECL|method|removeConnector (ConnectorDto dto)
name|void
name|removeConnector
parameter_list|(
name|ConnectorDto
name|dto
parameter_list|)
function_decl|;
DECL|method|findConnector ()
name|List
argument_list|<
name|ConnectorDto
argument_list|>
name|findConnector
parameter_list|()
function_decl|;
DECL|method|findConnector (String filter)
name|List
argument_list|<
name|ConnectorDto
argument_list|>
name|findConnector
parameter_list|(
name|String
name|filter
parameter_list|)
function_decl|;
DECL|method|connectorJSon (ConnectorDto dto)
name|String
name|connectorJSon
parameter_list|(
name|ConnectorDto
name|dto
parameter_list|)
function_decl|;
DECL|method|connectorSchemaJSon (ConnectorDto dto)
name|String
name|connectorSchemaJSon
parameter_list|(
name|ConnectorDto
name|dto
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

