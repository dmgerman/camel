begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_comment
comment|/**  * Holds an {@link Endpoint} configuration as a pojo that can be manipulated and validated.  * Camel endpoint configuration is strongly related to URIs.  */
end_comment

begin_interface
DECL|interface|EndpointConfiguration
specifier|public
interface|interface
name|EndpointConfiguration
block|{
DECL|field|URI_SCHEME
name|String
name|URI_SCHEME
init|=
literal|"scheme"
decl_stmt|;
DECL|field|URI_SCHEME_SPECIFIC_PART
name|String
name|URI_SCHEME_SPECIFIC_PART
init|=
literal|"schemeSpecificPart"
decl_stmt|;
DECL|field|URI_AUTHORITY
name|String
name|URI_AUTHORITY
init|=
literal|"authority"
decl_stmt|;
DECL|field|URI_USER_INFO
name|String
name|URI_USER_INFO
init|=
literal|"userInfo"
decl_stmt|;
DECL|field|URI_HOST
name|String
name|URI_HOST
init|=
literal|"host"
decl_stmt|;
DECL|field|URI_PORT
name|String
name|URI_PORT
init|=
literal|"port"
decl_stmt|;
DECL|field|URI_PATH
name|String
name|URI_PATH
init|=
literal|"path"
decl_stmt|;
DECL|field|URI_QUERY
name|String
name|URI_QUERY
init|=
literal|"query"
decl_stmt|;
DECL|field|URI_FRAGMENT
name|String
name|URI_FRAGMENT
init|=
literal|"fragment"
decl_stmt|;
comment|/**      * {@link org.apache.camel.spi.DataFormat} operations.      */
DECL|enum|UriFormat
specifier|public
enum|enum
name|UriFormat
block|{
DECL|enumConstant|Canonical
DECL|enumConstant|Provider
DECL|enumConstant|Consumer
DECL|enumConstant|Complete
name|Canonical
block|,
name|Provider
block|,
name|Consumer
block|,
name|Complete
block|}
comment|/**      * Returns the URI configuration of an {@link Endpoint}.      *      * @return the configuration URI.      */
DECL|method|getURI ()
name|URI
name|getURI
parameter_list|()
function_decl|;
comment|/**      * Gets the value of a particular parameter.      *      * @param name the parameter name      * @return the configuration URI.      * @throws RuntimeCamelException is thrown if error getting the parameter      */
DECL|method|getParameter (String name)
parameter_list|<
name|T
parameter_list|>
name|T
name|getParameter
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|RuntimeCamelException
function_decl|;
comment|/**      * Sets the value of a particular parameter.      *      * @param name  the parameter name      * @param value the parameter value      * @throws RuntimeCamelException is thrown if error setting the parameter      */
DECL|method|setParameter (String name, T value)
parameter_list|<
name|T
parameter_list|>
name|void
name|setParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|T
name|value
parameter_list|)
throws|throws
name|RuntimeCamelException
function_decl|;
comment|/**      * Returns the formatted configuration string of an {@link Endpoint}.      *      * @param format the format      * @return the configuration URI in String format.      */
DECL|method|toUriString (UriFormat format)
name|String
name|toUriString
parameter_list|(
name|UriFormat
name|format
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

