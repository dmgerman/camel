begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.generator.openapi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|generator
operator|.
name|openapi
package|;
end_package

begin_import
import|import
name|io
operator|.
name|apicurio
operator|.
name|datamodels
operator|.
name|openapi
operator|.
name|models
operator|.
name|OasOperation
import|;
end_import

begin_interface
annotation|@
name|FunctionalInterface
DECL|interface|DestinationGenerator
specifier|public
interface|interface
name|DestinationGenerator
block|{
DECL|method|generateDestinationFor (OasOperation operation)
name|String
name|generateDestinationFor
parameter_list|(
name|OasOperation
name|operation
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

