begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo4.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo4
operator|.
name|api
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

begin_comment
comment|/**  * Callback interface to asynchronously process Olingo4 response.  */
end_comment

begin_interface
DECL|interface|Olingo4ResponseHandler
specifier|public
interface|interface
name|Olingo4ResponseHandler
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Handle response data on successful completion of Olingo4 request.      * @param response response data from Olingo4, may be NULL for Olingo4 operations with no response data.      * @param responseHeaders the response HTTP headers received from the endpoint.      */
DECL|method|onResponse (T response, Map<String, String> responseHeaders)
name|void
name|onResponse
parameter_list|(
name|T
name|response
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|responseHeaders
parameter_list|)
function_decl|;
comment|/**      * Handle exception raised from Olingo4 request.      * @param ex exception from Olingo4 request.      * May be an instance of {@link org.apache.olingo.commons.api.ex.ODataException} or      * some other exception, such as {@link java.io.IOException}      */
DECL|method|onException (Exception ex)
name|void
name|onException
parameter_list|(
name|Exception
name|ex
parameter_list|)
function_decl|;
comment|/**      * Handle Olingo4 request cancellation.      * May be caused by the underlying HTTP connection being shutdown asynchronously.      */
DECL|method|onCanceled ()
name|void
name|onCanceled
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

