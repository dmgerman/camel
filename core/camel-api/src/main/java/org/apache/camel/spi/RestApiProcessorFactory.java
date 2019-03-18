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
name|Processor
import|;
end_import

begin_comment
comment|/**  * Allows SPI to plugin a {@link RestApiProcessorFactory} that creates the Camel {@link Processor} responsible  * for servicing and generating the REST API documentation.  *<p/>  * For example the<tt>camel-swagger-java</tt> component provides such a factory that uses Swagger to generate the documentation.  */
end_comment

begin_interface
DECL|interface|RestApiProcessorFactory
specifier|public
interface|interface
name|RestApiProcessorFactory
block|{
comment|/**      * Creates a new REST API<a      * href="http://camel.apache.org/processor.html">Processor      *</a>, which provides API listing of the REST services      *      * @param camelContext      the camel context      * @param contextPath       the context-path      * @param contextIdPattern  id pattern to only allow Rest APIs from rest services within CamelContext's which name matches the pattern.      * @param parameters        additional parameters      * @return a newly created REST API provider      * @throws Exception can be thrown      */
DECL|method|createApiProcessor (CamelContext camelContext, String contextPath, String contextIdPattern, boolean contextIdListing, RestConfiguration configuration, Map<String, Object> parameters)
name|Processor
name|createApiProcessor
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|contextPath
parameter_list|,
name|String
name|contextIdPattern
parameter_list|,
name|boolean
name|contextIdListing
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

