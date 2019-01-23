begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo4
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|CamelExecutionException
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
name|support
operator|.
name|IntrospectionSupport
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpHost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|ClientProtocolException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpGet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpUriRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|CloseableHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClients
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|BasicHttpContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|ExecutionContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpContext
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
name|client
operator|.
name|api
operator|.
name|ODataClient
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
name|client
operator|.
name|api
operator|.
name|domain
operator|.
name|ClientObjectFactory
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
name|client
operator|.
name|core
operator|.
name|ODataClientFactory
import|;
end_import

begin_comment
comment|/**  * Abstract base class for Olingo 4.0 Integration tests generated by Camel API  * component maven plugin.  */
end_comment

begin_class
DECL|class|AbstractOlingo4TestSupport
specifier|public
class|class
name|AbstractOlingo4TestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_SERVICE_BASE_URL
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_SERVICE_BASE_URL
init|=
literal|"http://services.odata.org/TripPinRESTierService"
decl_stmt|;
DECL|field|odataClient
specifier|protected
specifier|final
name|ODataClient
name|odataClient
init|=
name|ODataClientFactory
operator|.
name|getClient
argument_list|()
decl_stmt|;
DECL|field|objFactory
specifier|protected
specifier|final
name|ClientObjectFactory
name|objFactory
init|=
name|odataClient
operator|.
name|getObjectFactory
argument_list|()
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
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|options
operator|.
name|put
argument_list|(
literal|"serviceUri"
argument_list|,
name|getRealServiceUrl
argument_list|(
name|TEST_SERVICE_BASE_URL
argument_list|)
argument_list|)
expr_stmt|;
name|options
operator|.
name|put
argument_list|(
literal|"contentType"
argument_list|,
literal|"application/json;charset=utf-8"
argument_list|)
expr_stmt|;
specifier|final
name|Olingo4Configuration
name|configuration
init|=
operator|new
name|Olingo4Configuration
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|options
argument_list|)
expr_stmt|;
comment|// add OlingoComponent to Camel context
specifier|final
name|Olingo4Component
name|component
init|=
operator|new
name|Olingo4Component
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"olingo4"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|/*      * Every request to the demo OData 4.0      * (http://services.odata.org/TripPinRESTierService) generates unique      * service URL with postfix like (S(tuivu3up5ygvjzo5fszvnwfv)) for each      * session This method makes request to the base URL and return URL with      * generated postfix      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|getRealServiceUrl (String baseUrl)
specifier|protected
name|String
name|getRealServiceUrl
parameter_list|(
name|String
name|baseUrl
parameter_list|)
throws|throws
name|ClientProtocolException
throws|,
name|IOException
block|{
name|CloseableHttpClient
name|httpclient
init|=
name|HttpClients
operator|.
name|createDefault
argument_list|()
decl_stmt|;
name|HttpGet
name|httpGet
init|=
operator|new
name|HttpGet
argument_list|(
name|baseUrl
argument_list|)
decl_stmt|;
name|HttpContext
name|httpContext
init|=
operator|new
name|BasicHttpContext
argument_list|()
decl_stmt|;
name|httpclient
operator|.
name|execute
argument_list|(
name|httpGet
argument_list|,
name|httpContext
argument_list|)
expr_stmt|;
name|HttpUriRequest
name|currentReq
init|=
operator|(
name|HttpUriRequest
operator|)
name|httpContext
operator|.
name|getAttribute
argument_list|(
name|ExecutionContext
operator|.
name|HTTP_REQUEST
argument_list|)
decl_stmt|;
name|HttpHost
name|currentHost
init|=
operator|(
name|HttpHost
operator|)
name|httpContext
operator|.
name|getAttribute
argument_list|(
name|ExecutionContext
operator|.
name|HTTP_TARGET_HOST
argument_list|)
decl_stmt|;
name|String
name|currentUrl
init|=
operator|(
name|currentReq
operator|.
name|getURI
argument_list|()
operator|.
name|isAbsolute
argument_list|()
operator|)
condition|?
name|currentReq
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
else|:
operator|(
name|currentHost
operator|.
name|toURI
argument_list|()
operator|+
name|currentReq
operator|.
name|getURI
argument_list|()
operator|)
decl_stmt|;
return|return
name|currentUrl
return|;
block|}
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
comment|// only create the context once for this class
return|return
literal|true
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|requestBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
operator|(
name|T
operator|)
name|template
argument_list|()
operator|.
name|requestBodyAndHeaders
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|requestBody (String endpoint, Object body)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBody
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
operator|(
name|T
operator|)
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
name|endpoint
argument_list|,
name|body
argument_list|)
return|;
block|}
block|}
end_class

end_unit

