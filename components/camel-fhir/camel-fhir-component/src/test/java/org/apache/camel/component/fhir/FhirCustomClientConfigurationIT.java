begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.fhir
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|model
operator|.
name|primitive
operator|.
name|IdDt
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
name|model
operator|.
name|primitive
operator|.
name|UriDt
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
name|MethodOutcome
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
name|RequestFormatParamStyleEnum
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
name|RequestTypeEnum
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
name|api
operator|.
name|Header
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
name|IClientInterceptor
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
name|IHttpClient
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
name|IRestfulClient
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
name|exceptions
operator|.
name|FhirClientConnectionException
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
name|gclient
operator|.
name|ICreate
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
name|gclient
operator|.
name|IDelete
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
name|gclient
operator|.
name|IFetchConformanceUntyped
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
name|gclient
operator|.
name|IGetPage
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
name|gclient
operator|.
name|IHistory
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
name|gclient
operator|.
name|IMeta
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
name|gclient
operator|.
name|IOperation
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
name|gclient
operator|.
name|IPatch
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
name|gclient
operator|.
name|IRead
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
name|gclient
operator|.
name|ITransaction
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
name|gclient
operator|.
name|IUntypedQuery
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
name|gclient
operator|.
name|IUpdate
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
name|gclient
operator|.
name|IValidate
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
name|builder
operator|.
name|RouteBuilder
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
name|internal
operator|.
name|FhirApiCollection
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
name|internal
operator|.
name|FhirCreateApiMethod
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
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hl7
operator|.
name|fhir
operator|.
name|instance
operator|.
name|model
operator|.
name|api
operator|.
name|IBaseBundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hl7
operator|.
name|fhir
operator|.
name|instance
operator|.
name|model
operator|.
name|api
operator|.
name|IBaseResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Test class for {@link FhirConfiguration} APIs.  */
end_comment

begin_class
DECL|class|FhirCustomClientConfigurationIT
specifier|public
class|class
name|FhirCustomClientConfigurationIT
extends|extends
name|AbstractFhirTestSupport
block|{
DECL|field|PATH_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PREFIX
init|=
name|FhirApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|FhirCreateApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
DECL|field|TEST_URI_CUSTOM_CLIENT
specifier|private
specifier|static
specifier|final
name|String
name|TEST_URI_CUSTOM_CLIENT
init|=
literal|"fhir://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/resource?inBody=resourceAsString&client=#customClient"
decl_stmt|;
DECL|field|TEST_URI_CUSTOM_CLIENT_FACTORY
specifier|private
specifier|static
specifier|final
name|String
name|TEST_URI_CUSTOM_CLIENT_FACTORY
init|=
literal|"fhir://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/resource?inBody=resourceAsString&clientFactory=#customClientFactory&serverUrl=foobar"
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|createRegistry
argument_list|()
argument_list|)
decl_stmt|;
comment|// add FhirComponent to Camel context but don't set up componentConfiguration
specifier|final
name|FhirComponent
name|component
init|=
operator|new
name|FhirComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"fhir"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"customClient"
argument_list|,
operator|new
name|CustomClient
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"customClientFactory"
argument_list|,
operator|new
name|CustomClientFactory
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Test
DECL|method|testConfigurationWithCustomClient ()
specifier|public
name|void
name|testConfigurationWithCustomClient
parameter_list|()
throws|throws
name|Exception
block|{
name|FhirEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
name|TEST_URI_CUSTOM_CLIENT
argument_list|,
name|FhirEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|IGenericClient
name|client
init|=
name|endpoint
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|client
operator|instanceof
name|CustomClient
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigurationWithCustomFactory ()
specifier|public
name|void
name|testConfigurationWithCustomFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|FhirEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
name|TEST_URI_CUSTOM_CLIENT_FACTORY
argument_list|,
name|FhirEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|IGenericClient
name|client
init|=
name|endpoint
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|client
operator|instanceof
name|CustomClient
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|cleanFhirServerState ()
specifier|public
name|void
name|cleanFhirServerState
parameter_list|()
block|{
comment|// do nothing
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct://CONFIGURATION_CUSTOM_CLIENT"
argument_list|)
operator|.
name|to
argument_list|(
name|TEST_URI_CUSTOM_CLIENT
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://CONFIGURATION_CUSTOM_CLIENT_FACTORY"
argument_list|)
operator|.
name|to
argument_list|(
name|TEST_URI_CUSTOM_CLIENT_FACTORY
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|CustomClientFactory
specifier|private
class|class
name|CustomClientFactory
implements|implements
name|IRestfulClientFactory
block|{
annotation|@
name|Override
DECL|method|getConnectionRequestTimeout ()
specifier|public
name|int
name|getConnectionRequestTimeout
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|getConnectTimeout ()
specifier|public
name|int
name|getConnectTimeout
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|getHttpClient (StringBuilder theUrl, Map<String, List<String>> theIfNoneExistParams, String theIfNoneExistString, RequestTypeEnum theRequestType, List<Header> theHeaders)
specifier|public
name|IHttpClient
name|getHttpClient
parameter_list|(
name|StringBuilder
name|theUrl
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|theIfNoneExistParams
parameter_list|,
name|String
name|theIfNoneExistString
parameter_list|,
name|RequestTypeEnum
name|theRequestType
parameter_list|,
name|List
argument_list|<
name|Header
argument_list|>
name|theHeaders
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getServerValidationModeEnum ()
specifier|public
name|ServerValidationModeEnum
name|getServerValidationModeEnum
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getServerValidationMode ()
specifier|public
name|ServerValidationModeEnum
name|getServerValidationMode
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getSocketTimeout ()
specifier|public
name|int
name|getSocketTimeout
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|getPoolMaxTotal ()
specifier|public
name|int
name|getPoolMaxTotal
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|getPoolMaxPerRoute ()
specifier|public
name|int
name|getPoolMaxPerRoute
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|newClient (Class<T> theClientType, String theServerBase)
specifier|public
parameter_list|<
name|T
extends|extends
name|IRestfulClient
parameter_list|>
name|T
name|newClient
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|theClientType
parameter_list|,
name|String
name|theServerBase
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|newGenericClient (String theServerBase)
specifier|public
name|IGenericClient
name|newGenericClient
parameter_list|(
name|String
name|theServerBase
parameter_list|)
block|{
return|return
operator|new
name|CustomClient
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setConnectionRequestTimeout (int theConnectionRequestTimeout)
specifier|public
name|void
name|setConnectionRequestTimeout
parameter_list|(
name|int
name|theConnectionRequestTimeout
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|setConnectTimeout (int theConnectTimeout)
specifier|public
name|void
name|setConnectTimeout
parameter_list|(
name|int
name|theConnectTimeout
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|setHttpClient (T theHttpClient)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|setHttpClient
parameter_list|(
name|T
name|theHttpClient
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|setProxy (String theHost, Integer thePort)
specifier|public
name|void
name|setProxy
parameter_list|(
name|String
name|theHost
parameter_list|,
name|Integer
name|thePort
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|setProxyCredentials (String theUsername, String thePassword)
specifier|public
name|void
name|setProxyCredentials
parameter_list|(
name|String
name|theUsername
parameter_list|,
name|String
name|thePassword
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|setServerValidationModeEnum (ServerValidationModeEnum theServerValidationMode)
specifier|public
name|void
name|setServerValidationModeEnum
parameter_list|(
name|ServerValidationModeEnum
name|theServerValidationMode
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|setServerValidationMode (ServerValidationModeEnum theServerValidationMode)
specifier|public
name|void
name|setServerValidationMode
parameter_list|(
name|ServerValidationModeEnum
name|theServerValidationMode
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|setSocketTimeout (int theSocketTimeout)
specifier|public
name|void
name|setSocketTimeout
parameter_list|(
name|int
name|theSocketTimeout
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|setPoolMaxTotal (int thePoolMaxTotal)
specifier|public
name|void
name|setPoolMaxTotal
parameter_list|(
name|int
name|thePoolMaxTotal
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|setPoolMaxPerRoute (int thePoolMaxPerRoute)
specifier|public
name|void
name|setPoolMaxPerRoute
parameter_list|(
name|int
name|thePoolMaxPerRoute
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|validateServerBase (String theServerBase, IHttpClient theHttpClient, IRestfulClient theClient)
specifier|public
name|void
name|validateServerBase
parameter_list|(
name|String
name|theServerBase
parameter_list|,
name|IHttpClient
name|theHttpClient
parameter_list|,
name|IRestfulClient
name|theClient
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|validateServerBaseIfConfiguredToDoSo (String theServerBase, IHttpClient theHttpClient, IRestfulClient theClient)
specifier|public
name|void
name|validateServerBaseIfConfiguredToDoSo
parameter_list|(
name|String
name|theServerBase
parameter_list|,
name|IHttpClient
name|theHttpClient
parameter_list|,
name|IRestfulClient
name|theClient
parameter_list|)
block|{          }
block|}
DECL|class|CustomClient
specifier|private
class|class
name|CustomClient
implements|implements
name|IGenericClient
block|{
annotation|@
name|Override
DECL|method|capabilities ()
specifier|public
name|IFetchConformanceUntyped
name|capabilities
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|create ()
specifier|public
name|ICreate
name|create
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|delete ()
specifier|public
name|IDelete
name|delete
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|fetchConformance ()
specifier|public
name|IFetchConformanceUntyped
name|fetchConformance
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|forceConformanceCheck ()
specifier|public
name|void
name|forceConformanceCheck
parameter_list|()
throws|throws
name|FhirClientConnectionException
block|{          }
annotation|@
name|Override
DECL|method|history ()
specifier|public
name|IHistory
name|history
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|loadPage ()
specifier|public
name|IGetPage
name|loadPage
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|meta ()
specifier|public
name|IMeta
name|meta
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|operation ()
specifier|public
name|IOperation
name|operation
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|patch ()
specifier|public
name|IPatch
name|patch
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|IRead
name|read
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|read (Class<T> theType, String theId)
specifier|public
parameter_list|<
name|T
extends|extends
name|IBaseResource
parameter_list|>
name|T
name|read
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|theType
parameter_list|,
name|String
name|theId
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|read (Class<T> theType, UriDt theUrl)
specifier|public
parameter_list|<
name|T
extends|extends
name|IBaseResource
parameter_list|>
name|T
name|read
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|theType
parameter_list|,
name|UriDt
name|theUrl
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|read (UriDt theUrl)
specifier|public
name|IBaseResource
name|read
parameter_list|(
name|UriDt
name|theUrl
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|fetchResourceFromUrl (Class<T> theResourceType, String theUrl)
specifier|public
parameter_list|<
name|T
extends|extends
name|IBaseResource
parameter_list|>
name|T
name|fetchResourceFromUrl
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|theResourceType
parameter_list|,
name|String
name|theUrl
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getEncoding ()
specifier|public
name|EncodingEnum
name|getEncoding
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getFhirContext ()
specifier|public
name|FhirContext
name|getFhirContext
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getHttpClient ()
specifier|public
name|IHttpClient
name|getHttpClient
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getInterceptors ()
specifier|public
name|List
argument_list|<
name|IClientInterceptor
argument_list|>
name|getInterceptors
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getServerBase ()
specifier|public
name|String
name|getServerBase
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|registerInterceptor (IClientInterceptor theInterceptor)
specifier|public
name|void
name|registerInterceptor
parameter_list|(
name|IClientInterceptor
name|theInterceptor
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|setEncoding (EncodingEnum theEncoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|EncodingEnum
name|theEncoding
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|setPrettyPrint (Boolean thePrettyPrint)
specifier|public
name|void
name|setPrettyPrint
parameter_list|(
name|Boolean
name|thePrettyPrint
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|setSummary (SummaryEnum theSummary)
specifier|public
name|void
name|setSummary
parameter_list|(
name|SummaryEnum
name|theSummary
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|search ()
specifier|public
parameter_list|<
name|T
extends|extends
name|IBaseBundle
parameter_list|>
name|IUntypedQuery
argument_list|<
name|T
argument_list|>
name|search
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setLogRequestAndResponse (boolean theLogRequestAndResponse)
specifier|public
name|void
name|setLogRequestAndResponse
parameter_list|(
name|boolean
name|theLogRequestAndResponse
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|transaction ()
specifier|public
name|ITransaction
name|transaction
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|unregisterInterceptor (IClientInterceptor theInterceptor)
specifier|public
name|void
name|unregisterInterceptor
parameter_list|(
name|IClientInterceptor
name|theInterceptor
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|setFormatParamStyle (RequestFormatParamStyleEnum requestFormatParamStyleEnum)
specifier|public
name|void
name|setFormatParamStyle
parameter_list|(
name|RequestFormatParamStyleEnum
name|requestFormatParamStyleEnum
parameter_list|)
block|{          }
annotation|@
name|Override
DECL|method|update ()
specifier|public
name|IUpdate
name|update
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|update (IdDt theId, IBaseResource theResource)
specifier|public
name|MethodOutcome
name|update
parameter_list|(
name|IdDt
name|theId
parameter_list|,
name|IBaseResource
name|theResource
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|update (String theId, IBaseResource theResource)
specifier|public
name|MethodOutcome
name|update
parameter_list|(
name|String
name|theId
parameter_list|,
name|IBaseResource
name|theResource
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|validate ()
specifier|public
name|IValidate
name|validate
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|validate (IBaseResource theResource)
specifier|public
name|MethodOutcome
name|validate
parameter_list|(
name|IBaseResource
name|theResource
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|vread (Class<T> theType, IdDt theId)
specifier|public
parameter_list|<
name|T
extends|extends
name|IBaseResource
parameter_list|>
name|T
name|vread
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|theType
parameter_list|,
name|IdDt
name|theId
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|vread (Class<T> theType, String theId, String theVersionId)
specifier|public
parameter_list|<
name|T
extends|extends
name|IBaseResource
parameter_list|>
name|T
name|vread
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|theType
parameter_list|,
name|String
name|theId
parameter_list|,
name|String
name|theVersionId
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

