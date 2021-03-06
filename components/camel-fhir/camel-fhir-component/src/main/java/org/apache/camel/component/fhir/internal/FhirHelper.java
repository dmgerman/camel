begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.fhir.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|fhir
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|context
operator|.
name|FhirContext
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|context
operator|.
name|FhirVersionEnum
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|context
operator|.
name|PerformanceOptionsEnum
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|api
operator|.
name|EncodingEnum
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|api
operator|.
name|SummaryEnum
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|client
operator|.
name|apache
operator|.
name|GZipContentInterceptor
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|IGenericClient
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|IRestfulClientFactory
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|client
operator|.
name|api
operator|.
name|ServerValidationModeEnum
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|client
operator|.
name|interceptor
operator|.
name|BasicAuthInterceptor
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|client
operator|.
name|interceptor
operator|.
name|BearerTokenAuthInterceptor
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|client
operator|.
name|interceptor
operator|.
name|CookieInterceptor
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|rest
operator|.
name|client
operator|.
name|interceptor
operator|.
name|LoggingInterceptor
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
name|RuntimeCamelException
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
name|fhir
operator|.
name|FhirConfiguration
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Utility class for creating FHIR {@link ca.uhn.fhir.rest.client.api.IGenericClient}  */
end_comment

begin_class
DECL|class|FhirHelper
specifier|public
specifier|final
class|class
name|FhirHelper
block|{
DECL|method|FhirHelper ()
specifier|private
name|FhirHelper
parameter_list|()
block|{
comment|// hide utility class constructor
block|}
DECL|method|createClient (FhirConfiguration endpointConfiguration, CamelContext camelContext)
specifier|public
specifier|static
name|IGenericClient
name|createClient
parameter_list|(
name|FhirConfiguration
name|endpointConfiguration
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|endpointConfiguration
operator|.
name|getClient
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|endpointConfiguration
operator|.
name|getClient
argument_list|()
return|;
block|}
name|FhirContext
name|fhirContext
init|=
name|getFhirContext
argument_list|(
name|endpointConfiguration
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpointConfiguration
operator|.
name|isDeferModelScanning
argument_list|()
condition|)
block|{
name|fhirContext
operator|.
name|setPerformanceOptions
argument_list|(
name|PerformanceOptionsEnum
operator|.
name|DEFERRED_MODEL_SCANNING
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpointConfiguration
operator|.
name|getClientFactory
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|fhirContext
operator|.
name|setRestfulClientFactory
argument_list|(
name|endpointConfiguration
operator|.
name|getClientFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|IRestfulClientFactory
name|restfulClientFactory
init|=
name|fhirContext
operator|.
name|getRestfulClientFactory
argument_list|()
decl_stmt|;
name|configureClientFactory
argument_list|(
name|endpointConfiguration
argument_list|,
name|restfulClientFactory
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|IGenericClient
name|genericClient
init|=
name|fhirContext
operator|.
name|newRestfulGenericClient
argument_list|(
name|endpointConfiguration
operator|.
name|getServerUrl
argument_list|()
argument_list|)
decl_stmt|;
name|genericClient
operator|.
name|setPrettyPrint
argument_list|(
name|endpointConfiguration
operator|.
name|isPrettyPrint
argument_list|()
argument_list|)
expr_stmt|;
name|EncodingEnum
name|encoding
init|=
name|endpointConfiguration
operator|.
name|getEncoding
argument_list|()
decl_stmt|;
name|SummaryEnum
name|summary
init|=
name|endpointConfiguration
operator|.
name|getSummary
argument_list|()
decl_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|genericClient
operator|.
name|setEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|summary
operator|!=
literal|null
condition|)
block|{
name|genericClient
operator|.
name|setSummary
argument_list|(
name|summary
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpointConfiguration
operator|.
name|isForceConformanceCheck
argument_list|()
condition|)
block|{
name|genericClient
operator|.
name|forceConformanceCheck
argument_list|()
expr_stmt|;
block|}
name|registerClientInterceptors
argument_list|(
name|genericClient
argument_list|,
name|endpointConfiguration
argument_list|)
expr_stmt|;
return|return
name|genericClient
return|;
block|}
DECL|method|configureClientFactory (FhirConfiguration endpointConfiguration, IRestfulClientFactory restfulClientFactory, CamelContext camelContext)
specifier|private
specifier|static
name|void
name|configureClientFactory
parameter_list|(
name|FhirConfiguration
name|endpointConfiguration
parameter_list|,
name|IRestfulClientFactory
name|restfulClientFactory
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|Integer
name|connectionTimeout
init|=
name|endpointConfiguration
operator|.
name|getConnectionTimeout
argument_list|()
decl_stmt|;
name|Integer
name|socketTimeout
init|=
name|endpointConfiguration
operator|.
name|getSocketTimeout
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|connectionTimeout
argument_list|)
condition|)
block|{
name|restfulClientFactory
operator|.
name|setConnectTimeout
argument_list|(
name|connectionTimeout
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|socketTimeout
argument_list|)
condition|)
block|{
name|restfulClientFactory
operator|.
name|setSocketTimeout
argument_list|(
name|socketTimeout
argument_list|)
expr_stmt|;
block|}
name|configureProxy
argument_list|(
name|endpointConfiguration
argument_list|,
name|restfulClientFactory
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|configureProxy (FhirConfiguration endpointConfiguration, IRestfulClientFactory restfulClientFactory, CamelContext camelContext)
specifier|private
specifier|static
name|void
name|configureProxy
parameter_list|(
name|FhirConfiguration
name|endpointConfiguration
parameter_list|,
name|IRestfulClientFactory
name|restfulClientFactory
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|ServerValidationModeEnum
name|validationMode
init|=
name|endpointConfiguration
operator|.
name|getValidationMode
argument_list|()
decl_stmt|;
name|String
name|proxyHost
init|=
name|endpointConfiguration
operator|.
name|getProxyHost
argument_list|()
decl_stmt|;
name|Integer
name|proxyPort
init|=
name|endpointConfiguration
operator|.
name|getProxyPort
argument_list|()
decl_stmt|;
name|String
name|proxyUser
init|=
name|endpointConfiguration
operator|.
name|getProxyUser
argument_list|()
decl_stmt|;
name|String
name|proxyPassword
init|=
name|endpointConfiguration
operator|.
name|getProxyPassword
argument_list|()
decl_stmt|;
name|String
name|camelProxyHost
init|=
name|camelContext
operator|.
name|getGlobalOption
argument_list|(
literal|"http.proxyHost"
argument_list|)
decl_stmt|;
name|String
name|camelProxyPort
init|=
name|camelContext
operator|.
name|getGlobalOption
argument_list|(
literal|"http.proxyPort"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|camelProxyHost
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|camelProxyPort
argument_list|)
condition|)
block|{
name|restfulClientFactory
operator|.
name|setProxy
argument_list|(
name|camelProxyHost
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|camelProxyPort
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|proxyHost
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|proxyPort
argument_list|)
condition|)
block|{
name|restfulClientFactory
operator|.
name|setProxy
argument_list|(
name|proxyHost
argument_list|,
name|proxyPort
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|proxyUser
argument_list|)
condition|)
block|{
name|restfulClientFactory
operator|.
name|setProxyCredentials
argument_list|(
name|proxyUser
argument_list|,
name|proxyPassword
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|validationMode
argument_list|)
condition|)
block|{
name|restfulClientFactory
operator|.
name|setServerValidationMode
argument_list|(
name|validationMode
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|registerClientInterceptors (IGenericClient genericClient, FhirConfiguration endpointConfiguration)
specifier|private
specifier|static
name|void
name|registerClientInterceptors
parameter_list|(
name|IGenericClient
name|genericClient
parameter_list|,
name|FhirConfiguration
name|endpointConfiguration
parameter_list|)
block|{
name|String
name|username
init|=
name|endpointConfiguration
operator|.
name|getUsername
argument_list|()
decl_stmt|;
name|String
name|password
init|=
name|endpointConfiguration
operator|.
name|getPassword
argument_list|()
decl_stmt|;
name|String
name|accessToken
init|=
name|endpointConfiguration
operator|.
name|getAccessToken
argument_list|()
decl_stmt|;
name|String
name|sessionCookie
init|=
name|endpointConfiguration
operator|.
name|getSessionCookie
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|username
argument_list|)
condition|)
block|{
name|genericClient
operator|.
name|registerInterceptor
argument_list|(
operator|new
name|BasicAuthInterceptor
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|accessToken
argument_list|)
condition|)
block|{
name|genericClient
operator|.
name|registerInterceptor
argument_list|(
operator|new
name|BearerTokenAuthInterceptor
argument_list|(
name|accessToken
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpointConfiguration
operator|.
name|isLog
argument_list|()
condition|)
block|{
name|genericClient
operator|.
name|registerInterceptor
argument_list|(
operator|new
name|LoggingInterceptor
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpointConfiguration
operator|.
name|isCompress
argument_list|()
condition|)
block|{
name|genericClient
operator|.
name|registerInterceptor
argument_list|(
operator|new
name|GZipContentInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|sessionCookie
argument_list|)
condition|)
block|{
name|genericClient
operator|.
name|registerInterceptor
argument_list|(
operator|new
name|CookieInterceptor
argument_list|(
name|sessionCookie
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getFhirContext (FhirConfiguration endpointConfiguration)
specifier|private
specifier|static
name|FhirContext
name|getFhirContext
parameter_list|(
name|FhirConfiguration
name|endpointConfiguration
parameter_list|)
block|{
name|FhirContext
name|context
init|=
name|endpointConfiguration
operator|.
name|getFhirContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
return|return
name|context
return|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|endpointConfiguration
operator|.
name|getServerUrl
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"The FHIR URL must be set!"
argument_list|)
throw|;
block|}
name|FhirVersionEnum
name|fhirVersion
init|=
name|endpointConfiguration
operator|.
name|getFhirVersion
argument_list|()
decl_stmt|;
return|return
operator|new
name|FhirContext
argument_list|(
name|fhirVersion
argument_list|)
return|;
block|}
block|}
end_class

end_unit

