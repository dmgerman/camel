begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|Endpoint
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
name|Expression
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
name|NoSuchEndpointException
import|;
end_import

begin_comment
comment|/**  * Type-safe endpoint DSL for building producer endpoints.  *  * @see EndpointConsumerBuilder  */
end_comment

begin_interface
DECL|interface|EndpointProducerBuilder
specifier|public
interface|interface
name|EndpointProducerBuilder
block|{
comment|/**      * Builds and resolves this endpoint DSL as an endpoint.      *      * @param context the camel context      * @return a built {@link Endpoint}      * @throws NoSuchEndpointException is thrown if the endpoint      */
DECL|method|resolve (CamelContext context)
name|Endpoint
name|resolve
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|NoSuchEndpointException
function_decl|;
comment|/**      * Builds the url of this endpoint. This API is only intended for Camel      * internally.      */
DECL|method|getUri ()
name|String
name|getUri
parameter_list|()
function_decl|;
comment|/**      * Adds an option to this endpoint. This API is only intended for Camel      * internally.      */
DECL|method|doSetProperty (String name, Object value)
name|void
name|doSetProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Builds an expression of this endpoint url. This API is only intended for      * Camel internally.      */
DECL|method|expr ()
name|Expression
name|expr
parameter_list|()
function_decl|;
comment|/**      * Builds a dynamic expression of this endpoint url. This API is only intended for      * Camel internally.      */
DECL|method|expr (CamelContext camelContext)
name|Expression
name|expr
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

