begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpClient
import|;
end_import

begin_comment
comment|/**  * A plugable strategy for configuring the HttpClient used by this component  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|HttpClientConfigurer
specifier|public
interface|interface
name|HttpClientConfigurer
block|{
comment|/**      * Configure the HttpClient such as setting the authentication or proxying details      *      * @param client the client      */
DECL|method|configureHttpClient (HttpClient client)
name|void
name|configureHttpClient
parameter_list|(
name|HttpClient
name|client
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

