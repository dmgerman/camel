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
name|Consumer
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
comment|/**  * Allows SPI to plugin a {@link RestConsumerFactory} that creates the Camel {@link Consumer} responsible  * for handling incoming HTTP requests from clients that request to access REST services which has been created using  * the<a href="http://camel.apache.org/rest-dsl">rest-dsl</a>.  *  * @see RestApiConsumerFactory  * @see RestApiProcessorFactory  */
end_comment

begin_interface
DECL|interface|RestConsumerFactory
specifier|public
interface|interface
name|RestConsumerFactory
block|{
comment|/**      * Creates a new REST<a      * href="http://camel.apache.org/event-driven-consumer.html">Event      * Driven Consumer</a>, which consumes messages from the endpoint using the given processor      *      * @param camelContext  the camel context      * @param processor     the processor      * @param verb          HTTP verb such as GET, POST      * @param basePath      base path      * @param uriTemplate   uri template      * @param consumes      media-types for what this REST service consume as input (accept-type), is<tt>null</tt> or<tt>&#42;/&#42;</tt> for anything      * @param produces      media-types for what this REST service produces as output, can be<tt>null</tt>      * @param configuration REST configuration      * @param parameters    additional parameters      * @return a newly created REST consumer      * @throws Exception can be thrown      */
DECL|method|createConsumer (CamelContext camelContext, Processor processor, String verb, String basePath, String uriTemplate, String consumes, String produces, RestConfiguration configuration, Map<String, Object> parameters)
name|Consumer
name|createConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
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

