begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http.helper
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
operator|.
name|helper
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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|Exchange
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
name|Message
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
name|http
operator|.
name|HttpEndpoint
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
name|http
operator|.
name|HttpMethods
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
name|DefaultExchange
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
DECL|class|HttpHelperTest
specifier|public
class|class
name|HttpHelperTest
block|{
annotation|@
name|Test
DECL|method|testAppendHeader ()
specifier|public
name|void
name|testAppendHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|HttpHelper
operator|.
name|appendHeader
argument_list|(
name|headers
argument_list|,
literal|"foo"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|HttpHelper
operator|.
name|appendHeader
argument_list|(
name|headers
argument_list|,
literal|"bar"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|HttpHelper
operator|.
name|appendHeader
argument_list|(
name|headers
argument_list|,
literal|"baz"
argument_list|,
literal|"c"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|headers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAppendHeaderMultipleValues ()
specifier|public
name|void
name|testAppendHeaderMultipleValues
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|HttpHelper
operator|.
name|appendHeader
argument_list|(
name|headers
argument_list|,
literal|"foo"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|HttpHelper
operator|.
name|appendHeader
argument_list|(
name|headers
argument_list|,
literal|"bar"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|HttpHelper
operator|.
name|appendHeader
argument_list|(
name|headers
argument_list|,
literal|"bar"
argument_list|,
literal|"c"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|headers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|list
init|=
operator|(
name|List
operator|)
name|headers
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createURLShouldReturnTheHeaderURIIfNotBridgeEndpoint ()
specifier|public
name|void
name|createURLShouldReturnTheHeaderURIIfNotBridgeEndpoint
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|String
name|url
init|=
name|HttpHelper
operator|.
name|createURL
argument_list|(
name|createExchangeWithOptionalCamelHttpUriHeader
argument_list|(
literal|"http://apache.org"
argument_list|,
literal|null
argument_list|)
argument_list|,
name|createHttpEndpoint
argument_list|(
literal|false
argument_list|,
literal|"http://camel.apache.org"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://apache.org"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createURLShouldReturnTheEndpointURIIfBridgeEndpoint ()
specifier|public
name|void
name|createURLShouldReturnTheEndpointURIIfBridgeEndpoint
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|String
name|url
init|=
name|HttpHelper
operator|.
name|createURL
argument_list|(
name|createExchangeWithOptionalCamelHttpUriHeader
argument_list|(
literal|"http://apache.org"
argument_list|,
literal|null
argument_list|)
argument_list|,
name|createHttpEndpoint
argument_list|(
literal|true
argument_list|,
literal|"http://camel.apache.org"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://camel.apache.org"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createURLShouldReturnTheEndpointURIIfNotBridgeEndpoint ()
specifier|public
name|void
name|createURLShouldReturnTheEndpointURIIfNotBridgeEndpoint
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|String
name|url
init|=
name|HttpHelper
operator|.
name|createURL
argument_list|(
name|createExchangeWithOptionalCamelHttpUriHeader
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|,
name|createHttpEndpoint
argument_list|(
literal|false
argument_list|,
literal|"http://camel.apache.org"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://camel.apache.org"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createURLShouldReturnTheEndpointURIWithHeaderHttpPathAndAddOneSlash ()
specifier|public
name|void
name|createURLShouldReturnTheEndpointURIWithHeaderHttpPathAndAddOneSlash
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|String
name|url
init|=
name|HttpHelper
operator|.
name|createURL
argument_list|(
name|createExchangeWithOptionalCamelHttpUriHeader
argument_list|(
literal|null
argument_list|,
literal|"search"
argument_list|)
argument_list|,
name|createHttpEndpoint
argument_list|(
literal|true
argument_list|,
literal|"http://www.google.com"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://www.google.com/search"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createURLShouldReturnTheEndpointURIWithHeaderHttpPathAndRemoveOneSlash ()
specifier|public
name|void
name|createURLShouldReturnTheEndpointURIWithHeaderHttpPathAndRemoveOneSlash
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|String
name|url
init|=
name|HttpHelper
operator|.
name|createURL
argument_list|(
name|createExchangeWithOptionalCamelHttpUriHeader
argument_list|(
literal|null
argument_list|,
literal|"/search"
argument_list|)
argument_list|,
name|createHttpEndpoint
argument_list|(
literal|true
argument_list|,
literal|"http://www.google.com/"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://www.google.com/search"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createMethodAlwaysUseUserChoosenMethod ()
specifier|public
name|void
name|createMethodAlwaysUseUserChoosenMethod
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|HttpMethods
name|method
init|=
name|HttpHelper
operator|.
name|createMethod
argument_list|(
name|createExchangeWithOptionalHttpQueryAndHttpMethodHeader
argument_list|(
literal|"q=camel"
argument_list|,
name|HttpMethods
operator|.
name|POST
argument_list|)
argument_list|,
name|createHttpEndpoint
argument_list|(
literal|true
argument_list|,
literal|"http://www.google.com/search"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpMethods
operator|.
name|POST
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createMethodUseGETIfQueryIsProvidedInHeader ()
specifier|public
name|void
name|createMethodUseGETIfQueryIsProvidedInHeader
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|HttpMethods
name|method
init|=
name|HttpHelper
operator|.
name|createMethod
argument_list|(
name|createExchangeWithOptionalHttpQueryAndHttpMethodHeader
argument_list|(
literal|"q=camel"
argument_list|,
literal|null
argument_list|)
argument_list|,
name|createHttpEndpoint
argument_list|(
literal|true
argument_list|,
literal|"http://www.google.com/search"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpMethods
operator|.
name|GET
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createMethodUseGETIfQueryIsProvidedInEndpointURI ()
specifier|public
name|void
name|createMethodUseGETIfQueryIsProvidedInEndpointURI
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|HttpMethods
name|method
init|=
name|HttpHelper
operator|.
name|createMethod
argument_list|(
name|createExchangeWithOptionalHttpQueryAndHttpMethodHeader
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|,
name|createHttpEndpoint
argument_list|(
literal|true
argument_list|,
literal|"http://www.google.com/search?q=test"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpMethods
operator|.
name|GET
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createMethodUseGETIfNoneQueryOrPayloadIsProvided ()
specifier|public
name|void
name|createMethodUseGETIfNoneQueryOrPayloadIsProvided
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|HttpMethods
name|method
init|=
name|HttpHelper
operator|.
name|createMethod
argument_list|(
name|createExchangeWithOptionalHttpQueryAndHttpMethodHeader
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|,
name|createHttpEndpoint
argument_list|(
literal|true
argument_list|,
literal|"http://www.google.com/search"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpMethods
operator|.
name|GET
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createMethodUsePOSTIfNoneQueryButPayloadIsProvided ()
specifier|public
name|void
name|createMethodUsePOSTIfNoneQueryButPayloadIsProvided
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|HttpMethods
name|method
init|=
name|HttpHelper
operator|.
name|createMethod
argument_list|(
name|createExchangeWithOptionalHttpQueryAndHttpMethodHeader
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|,
name|createHttpEndpoint
argument_list|(
literal|true
argument_list|,
literal|"http://www.google.com/search"
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpMethods
operator|.
name|POST
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
DECL|method|createExchangeWithOptionalHttpQueryAndHttpMethodHeader (String httpQuery, HttpMethods httpMethod)
specifier|private
name|Exchange
name|createExchangeWithOptionalHttpQueryAndHttpMethodHeader
parameter_list|(
name|String
name|httpQuery
parameter_list|,
name|HttpMethods
name|httpMethod
parameter_list|)
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|inMsg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|httpQuery
operator|!=
literal|null
condition|)
block|{
name|inMsg
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
name|httpQuery
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpMethod
operator|!=
literal|null
condition|)
block|{
name|inMsg
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|httpMethod
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
DECL|method|createExchangeWithOptionalCamelHttpUriHeader (String endpointURI, String httpPath)
specifier|private
name|Exchange
name|createExchangeWithOptionalCamelHttpUriHeader
parameter_list|(
name|String
name|endpointURI
parameter_list|,
name|String
name|httpPath
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|DefaultExchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Message
name|inMsg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpointURI
operator|!=
literal|null
condition|)
block|{
name|inMsg
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
name|endpointURI
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpPath
operator|!=
literal|null
condition|)
block|{
name|inMsg
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
name|httpPath
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
DECL|method|createHttpEndpoint (boolean bridgeEndpoint, String endpointURI)
specifier|private
name|HttpEndpoint
name|createHttpEndpoint
parameter_list|(
name|boolean
name|bridgeEndpoint
parameter_list|,
name|String
name|endpointURI
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|HttpEndpoint
name|endpoint
init|=
operator|new
name|HttpEndpoint
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setBridgeEndpoint
argument_list|(
name|bridgeEndpoint
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpointURI
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setHttpUri
argument_list|(
operator|new
name|URI
argument_list|(
name|endpointURI
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

