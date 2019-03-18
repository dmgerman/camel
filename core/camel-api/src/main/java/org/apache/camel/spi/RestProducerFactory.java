begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

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
name|Producer
import|;
end_import

begin_comment
comment|/**  * Allows SPI to plugin a {@link RestProducerFactory} that creates the Camel {@link Producer} responsible  * for performing HTTP requests to call a remote REST service.  */
end_comment

begin_interface
DECL|interface|RestProducerFactory
specifier|public
interface|interface
name|RestProducerFactory
block|{
comment|/**      * Creates a new REST producer.      *      * @param camelContext        the camel context      * @param host                host in the syntax scheme:hostname:port, such as http:myserver:8080      * @param verb                HTTP verb such as GET, POST      * @param basePath            base path      * @param uriTemplate         uri template      * @param queryParameters     uri query parameters      * @param consumes            media-types for what the REST service consume as input (accept-type), is<tt>null</tt> or<tt>&#42;/&#42;</tt> for anything      * @param produces            media-types for what the REST service produces as output, can be<tt>null</tt>      * @param configuration       REST configuration      * @param parameters          additional parameters      * @return a newly created REST producer      * @throws Exception can be thrown      */
DECL|method|createProducer (CamelContext camelContext, String host, String verb, String basePath, String uriTemplate, String queryParameters, String consumes, String produces, RestConfiguration configuration, Map<String, Object> parameters)
name|Producer
name|createProducer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|host
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|queryParameters
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|RestConfiguration
name|configuration
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

