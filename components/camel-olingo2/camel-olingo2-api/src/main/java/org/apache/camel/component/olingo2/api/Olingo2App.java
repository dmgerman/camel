begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo2.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
operator|.
name|api
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
operator|.
name|api
operator|.
name|batch
operator|.
name|Olingo2BatchResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|commons
operator|.
name|HttpStatusCodes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|edm
operator|.
name|Edm
import|;
end_import

begin_comment
comment|/**  * Olingo2 Client Api Interface.  */
end_comment

begin_interface
DECL|interface|Olingo2App
specifier|public
interface|interface
name|Olingo2App
block|{
comment|/**      * Sets Service base URI.      * @param serviceUri      */
DECL|method|setServiceUri (String serviceUri)
name|void
name|setServiceUri
parameter_list|(
name|String
name|serviceUri
parameter_list|)
function_decl|;
comment|/**      * Returns Service base URI.      * @return service base URI.      */
DECL|method|getServiceUri ()
name|String
name|getServiceUri
parameter_list|()
function_decl|;
comment|/**      * Sets custom Http headers to add to every service request.      * @param httpHeaders custom Http headers.      */
DECL|method|setHttpHeaders (Map<String, String> httpHeaders)
name|void
name|setHttpHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|httpHeaders
parameter_list|)
function_decl|;
comment|/**      * Returns custom Http headers.      * @return custom Http headers.      */
DECL|method|getHttpHeaders ()
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getHttpHeaders
parameter_list|()
function_decl|;
comment|/**      * Returns content type for service calls. Defaults to<code>application/json;charset=utf-8</code>.      * @return content type.      */
DECL|method|getContentType ()
name|String
name|getContentType
parameter_list|()
function_decl|;
comment|/**      * Set default service call content type.      * @param contentType content type.      */
DECL|method|setContentType (String contentType)
name|void
name|setContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
function_decl|;
comment|/**      * Closes resources.      */
DECL|method|close ()
name|void
name|close
parameter_list|()
function_decl|;
comment|/**      * Reads an OData resource and invokes callback with appropriate result.      * @param edm Service Edm, read from calling<code>read(null, "$metdata", null, responseHandler)</code>      * @param resourcePath OData Resource path      * @param queryParams OData query params      *                    from http://www.odata.org/documentation/odata-version-2-0/uri-conventions#SystemQueryOptions      * @param endpointHttpHeaders HTTP Headers to add/override the component versions      * @param responseHandler callback handler      */
DECL|method|read (Edm edm, String resourcePath, Map<String, String> queryParams, Map<String, String> endpointHttpHeaders, Olingo2ResponseHandler<T> responseHandler)
parameter_list|<
name|T
parameter_list|>
name|void
name|read
parameter_list|(
name|Edm
name|edm
parameter_list|,
name|String
name|resourcePath
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParams
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|endpointHttpHeaders
parameter_list|,
name|Olingo2ResponseHandler
argument_list|<
name|T
argument_list|>
name|responseHandler
parameter_list|)
function_decl|;
comment|/**      * Reads an OData resource and invokes callback with the unparsed input stream.      * @param edm Service Edm, read from calling<code>read(null, "$metdata", null, responseHandler)</code>      * @param resourcePath OData Resource path      * @param queryParams OData query params      *                    from http://www.odata.org/documentation/odata-version-2-0/uri-conventions#SystemQueryOptions      * @param endpointHttpHeaders HTTP Headers to add/override the component versions      * @param responseHandler callback handler      */
DECL|method|uread (Edm edm, String resourcePath, Map<String, String> queryParams, Map<String, String> endpointHttpHeaders, Olingo2ResponseHandler<InputStream> responseHandler)
name|void
name|uread
parameter_list|(
name|Edm
name|edm
parameter_list|,
name|String
name|resourcePath
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParams
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|endpointHttpHeaders
parameter_list|,
name|Olingo2ResponseHandler
argument_list|<
name|InputStream
argument_list|>
name|responseHandler
parameter_list|)
function_decl|;
comment|/**      * Deletes an OData resource and invokes callback      * with {@link org.apache.olingo.odata2.api.commons.HttpStatusCodes} on success, or with exception on failure.      * @param resourcePath resource path for Entry      * @param endpointHttpHeaders HTTP Headers to add/override the component versions      * @param responseHandler {@link org.apache.olingo.odata2.api.commons.HttpStatusCodes} callback handler      */
DECL|method|delete (String resourcePath, Map<String, String> endpointHttpHeaders, Olingo2ResponseHandler<HttpStatusCodes> responseHandler)
name|void
name|delete
parameter_list|(
name|String
name|resourcePath
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|endpointHttpHeaders
parameter_list|,
name|Olingo2ResponseHandler
argument_list|<
name|HttpStatusCodes
argument_list|>
name|responseHandler
parameter_list|)
function_decl|;
comment|/**      * Creates a new OData resource.      * @param edm service Edm      * @param resourcePath resource path to create      * @param endpointHttpHeaders HTTP Headers to add/override the component versions      * @param data request data      * @param responseHandler callback handler      */
DECL|method|create (Edm edm, String resourcePath, Map<String, String> endpointHttpHeaders, Object data, Olingo2ResponseHandler<T> responseHandler)
parameter_list|<
name|T
parameter_list|>
name|void
name|create
parameter_list|(
name|Edm
name|edm
parameter_list|,
name|String
name|resourcePath
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|endpointHttpHeaders
parameter_list|,
name|Object
name|data
parameter_list|,
name|Olingo2ResponseHandler
argument_list|<
name|T
argument_list|>
name|responseHandler
parameter_list|)
function_decl|;
comment|/**      * Updates an OData resource.      * @param edm service Edm      * @param resourcePath resource path to update      * @param endpointHttpHeaders HTTP Headers to add/override the component versions      * @param data updated data      * @param responseHandler {@link org.apache.olingo.odata2.api.ep.entry.ODataEntry} callback handler      */
DECL|method|update (Edm edm, String resourcePath, Map<String, String> endpointHttpHeaders, Object data, Olingo2ResponseHandler<T> responseHandler)
parameter_list|<
name|T
parameter_list|>
name|void
name|update
parameter_list|(
name|Edm
name|edm
parameter_list|,
name|String
name|resourcePath
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|endpointHttpHeaders
parameter_list|,
name|Object
name|data
parameter_list|,
name|Olingo2ResponseHandler
argument_list|<
name|T
argument_list|>
name|responseHandler
parameter_list|)
function_decl|;
comment|/**      * Patches/merges an OData resource using HTTP PATCH.      * @param edm service Edm      * @param resourcePath resource path to update      * @param endpointHttpHeaders HTTP Headers to add/override the component versions      * @param data patch/merge data      * @param responseHandler {@link org.apache.olingo.odata2.api.ep.entry.ODataEntry} callback handler      */
DECL|method|patch (Edm edm, String resourcePath, Map<String, String> endpointHttpHeaders, Object data, Olingo2ResponseHandler<T> responseHandler)
parameter_list|<
name|T
parameter_list|>
name|void
name|patch
parameter_list|(
name|Edm
name|edm
parameter_list|,
name|String
name|resourcePath
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|endpointHttpHeaders
parameter_list|,
name|Object
name|data
parameter_list|,
name|Olingo2ResponseHandler
argument_list|<
name|T
argument_list|>
name|responseHandler
parameter_list|)
function_decl|;
comment|/**      * Patches/merges an OData resource using HTTP MERGE.      * @param edm service Edm      * @param resourcePath resource path to update      * @param endpointHttpHeaders HTTP Headers to add/override the component versions      * @param data patch/merge data      * @param responseHandler {@link org.apache.olingo.odata2.api.ep.entry.ODataEntry} callback handler      */
DECL|method|merge (Edm edm, String resourcePath, Map<String, String> endpointHttpHeaders, Object data, Olingo2ResponseHandler<T> responseHandler)
parameter_list|<
name|T
parameter_list|>
name|void
name|merge
parameter_list|(
name|Edm
name|edm
parameter_list|,
name|String
name|resourcePath
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|endpointHttpHeaders
parameter_list|,
name|Object
name|data
parameter_list|,
name|Olingo2ResponseHandler
argument_list|<
name|T
argument_list|>
name|responseHandler
parameter_list|)
function_decl|;
comment|/**      * Executes a batch request.      * @param edm service Edm      * @param endpointHttpHeaders HTTP Headers to add/override the component versions      * @param data ordered {@link org.apache.camel.component.olingo2.api.batch.Olingo2BatchRequest} list      * @param responseHandler callback handler      */
DECL|method|batch (Edm edm, Map<String, String> endpointHttpHeaders, Object data, Olingo2ResponseHandler<List<Olingo2BatchResponse>> responseHandler)
name|void
name|batch
parameter_list|(
name|Edm
name|edm
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|endpointHttpHeaders
parameter_list|,
name|Object
name|data
parameter_list|,
name|Olingo2ResponseHandler
argument_list|<
name|List
argument_list|<
name|Olingo2BatchResponse
argument_list|>
argument_list|>
name|responseHandler
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

