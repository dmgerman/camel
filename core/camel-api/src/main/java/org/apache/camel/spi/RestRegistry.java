begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|List
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
name|Service
import|;
end_import

begin_comment
comment|/**  * A registry of all REST services running within the {@link org.apache.camel.CamelContext} which have been defined and created  * using the<a href="http://camel.apache.org/rest-dsl">Rest DSL</a>.  */
end_comment

begin_interface
DECL|interface|RestRegistry
specifier|public
interface|interface
name|RestRegistry
extends|extends
name|Service
block|{
comment|/**      * Details about the REST service      */
DECL|interface|RestService
interface|interface
name|RestService
block|{
comment|/**          * Gets the consumer of the REST service          */
DECL|method|getConsumer ()
name|Consumer
name|getConsumer
parameter_list|()
function_decl|;
comment|/**          * Gets the state of the REST service (started, stopped, etc)          */
DECL|method|getState ()
name|String
name|getState
parameter_list|()
function_decl|;
comment|/**          * Gets the absolute url to the REST service (baseUrl + uriTemplate)          */
DECL|method|getUrl ()
name|String
name|getUrl
parameter_list|()
function_decl|;
comment|/**          * Gets the base url to the REST service          */
DECL|method|getBaseUrl ()
name|String
name|getBaseUrl
parameter_list|()
function_decl|;
comment|/**          * Gets the base path to the REST service          */
DECL|method|getBasePath ()
name|String
name|getBasePath
parameter_list|()
function_decl|;
comment|/**          * Gets the uri template          */
DECL|method|getUriTemplate ()
name|String
name|getUriTemplate
parameter_list|()
function_decl|;
comment|/**          * Gets the HTTP method (GET, POST, PUT etc)          */
DECL|method|getMethod ()
name|String
name|getMethod
parameter_list|()
function_decl|;
comment|/**          * Optional details about what media-types the REST service accepts          */
DECL|method|getConsumes ()
name|String
name|getConsumes
parameter_list|()
function_decl|;
comment|/**          * Optional details about what media-types the REST service returns          */
DECL|method|getProduces ()
name|String
name|getProduces
parameter_list|()
function_decl|;
comment|/**          * Optional detail about input binding to a FQN class name.          *<p/>          * If the input accepts a list, then<tt>List&lt;class name&gt;</tt> is enclosed the name.          */
DECL|method|getInType ()
name|String
name|getInType
parameter_list|()
function_decl|;
comment|/**          * Optional detail about output binding to a FQN class name.          *<p/>          * If the output accepts a list, then<tt>List&lt;class name&gt;</tt> is enclosed the name.          */
DECL|method|getOutType ()
name|String
name|getOutType
parameter_list|()
function_decl|;
comment|/**          * Gets the id of the route this rest service will be using.          */
DECL|method|getRouteId ()
name|String
name|getRouteId
parameter_list|()
function_decl|;
comment|/**          * Optional description about this rest service.          */
DECL|method|getDescription ()
name|String
name|getDescription
parameter_list|()
function_decl|;
block|}
comment|/**      * Adds a new REST service to the registry.      *      * @param consumer    the consumer      * @param url         the absolute url of the REST service      * @param baseUrl     the base url of the REST service      * @param basePath    the base path      * @param uriTemplate the uri template      * @param method      the HTTP method      * @param consumes    optional details about what media-types the REST service accepts      * @param produces    optional details about what media-types the REST service returns      * @param inType      optional detail input binding to a FQN class name      * @param outType     optional detail output binding to a FQN class name      * @param routeId     the id of the route this rest service will be using      * @param description optional description about the service      */
DECL|method|addRestService (Consumer consumer, String url, String baseUrl, String basePath, String uriTemplate, String method, String consumes, String produces, String inType, String outType, String routeId, String description)
name|void
name|addRestService
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|baseUrl
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|String
name|inType
parameter_list|,
name|String
name|outType
parameter_list|,
name|String
name|routeId
parameter_list|,
name|String
name|description
parameter_list|)
function_decl|;
comment|/**      * Removes the REST service from the registry      *      * @param consumer  the consumer      */
DECL|method|removeRestService (Consumer consumer)
name|void
name|removeRestService
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
function_decl|;
comment|/**      * List all REST services from this registry.      *      * @return all the REST services      */
DECL|method|listAllRestServices ()
name|List
argument_list|<
name|RestService
argument_list|>
name|listAllRestServices
parameter_list|()
function_decl|;
comment|/**      * Number of rest services in the registry.      *      * @return number of rest services in the registry.      */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/**      * Outputs the Rest services API documentation in JSon (requires camel-swagger-java on classpath)      *      * @return  the API docs in JSon, or<tt>null</tt> if camel-swagger-java is not on classpath      */
DECL|method|apiDocAsJson ()
name|String
name|apiDocAsJson
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

