begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|LinkedHashMap
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
name|ContextTestSupport
import|;
end_import

begin_comment
comment|/**  * @version  */
end_comment

begin_class
DECL|class|URISupportTest
specifier|public
class|class
name|URISupportTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testNormalizeEndpointUri ()
specifier|public
name|void
name|testNormalizeEndpointUri
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out1
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"smtp://localhost?username=davsclaus&password=secret"
argument_list|)
decl_stmt|;
name|String
name|out2
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"smtp://localhost?password=secret&username=davsclaus"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|out1
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"smtp://localhost?username=davsclaus&password=secret"
argument_list|)
expr_stmt|;
name|out2
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"smtp:localhost?password=secret&username=davsclaus"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|out1
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"smtp:localhost?password=secret&username=davsclaus"
argument_list|)
expr_stmt|;
name|out2
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"smtp://localhost?username=davsclaus&password=secret"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|out1
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"seda:foo?concurrentConsumer=2"
argument_list|)
expr_stmt|;
name|out2
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"seda:foo?concurrentConsumer=2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|out1
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"seda:foo?concurrentConsumer=2"
argument_list|)
expr_stmt|;
name|out2
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|out1
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"foo:?test=1"
argument_list|)
expr_stmt|;
name|out2
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"foo://?test=1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo://?test=1"
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
block|}
DECL|method|testNormalizeEndpointUriNoParam ()
specifier|public
name|void
name|testNormalizeEndpointUriNoParam
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out1
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"direct:foo"
argument_list|)
decl_stmt|;
name|String
name|out2
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"direct:foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|out1
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"direct://foo"
argument_list|)
expr_stmt|;
name|out2
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"direct://foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|out1
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"direct:foo"
argument_list|)
expr_stmt|;
name|out2
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"direct://foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|out1
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"direct://foo"
argument_list|)
expr_stmt|;
name|out2
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"direct:foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|out1
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"direct://foo"
argument_list|)
expr_stmt|;
name|out2
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"direct:bar"
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
block|}
DECL|method|testNormalizeEndpointUriWithFragments ()
specifier|public
name|void
name|testNormalizeEndpointUriWithFragments
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out1
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"irc://someserver/#camel?user=davsclaus"
argument_list|)
decl_stmt|;
name|String
name|out2
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"irc:someserver/#camel?user=davsclaus"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|out1
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"irc://someserver/#camel?user=davsclaus"
argument_list|)
expr_stmt|;
name|out2
operator|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"irc:someserver/#camel?user=hadrian"
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
block|}
DECL|method|testNormalizeHttpEndpoint ()
specifier|public
name|void
name|testNormalizeHttpEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out1
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"http://www.google.com?q=Camel"
argument_list|)
decl_stmt|;
name|String
name|out2
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"http:www.google.com?q=Camel"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have //"
argument_list|,
name|out1
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have //"
argument_list|,
name|out2
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNormalizeIPv6HttpEndpoint ()
specifier|public
name|void
name|testNormalizeIPv6HttpEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|result
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"http://[2a00:8a00:6000:40::1413]:30300/test"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://[2a00:8a00:6000:40::1413]:30300/test"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|testNormalizeHttpEndpointUnicodedParameter ()
specifier|public
name|void
name|testNormalizeHttpEndpointUnicodedParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"http://www.google.com?q=S\u00F8ren"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://www.google.com?q=S%C3%B8ren"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseParametersUnicodedValue ()
specifier|public
name|void
name|testParseParametersUnicodedValue
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"http://www.google.com?q=S\u00F8ren"
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parameters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"S\u00F8ren"
argument_list|,
name|parameters
operator|.
name|get
argument_list|(
literal|"q"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNormalizeHttpEndpointURLEncodedParameter ()
specifier|public
name|void
name|testNormalizeHttpEndpointURLEncodedParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"http://www.google.com?q=S%C3%B8ren%20Hansen"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://www.google.com?q=S%C3%B8ren+Hansen"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseParametersURLEncodeddValue ()
specifier|public
name|void
name|testParseParametersURLEncodeddValue
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"http://www.google.com?q=S%C3%B8ren+Hansen"
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|parameters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"S\u00F8ren Hansen"
argument_list|,
name|parameters
operator|.
name|get
argument_list|(
literal|"q"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNormalizeUriWhereParamererIsFaulty ()
specifier|public
name|void
name|testNormalizeUriWhereParamererIsFaulty
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"stream:uri?file:///d:/temp/data/log/quickfix.log&scanStream=true"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateRemaingURI ()
specifier|public
name|void
name|testCreateRemaingURI
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|original
init|=
operator|new
name|URI
argument_list|(
literal|"http://camel.apache.org"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|param
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|param
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|URI
name|newUri
init|=
name|URISupport
operator|.
name|createRemainingURI
argument_list|(
name|original
argument_list|,
name|param
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|newUri
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|newUri
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://camel.apache.org?foo=123"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateURIWithQueryHasOneFragment ()
specifier|public
name|void
name|testCreateURIWithQueryHasOneFragment
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"smtp://localhost#fragmentOne"
argument_list|)
decl_stmt|;
name|URI
name|resultUri
init|=
name|URISupport
operator|.
name|createURIWithQuery
argument_list|(
name|uri
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultUri
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"smtp://localhost#fragmentOne"
argument_list|,
name|resultUri
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateURIWithQueryHasOneFragmentAndQueryParameter ()
specifier|public
name|void
name|testCreateURIWithQueryHasOneFragmentAndQueryParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"smtp://localhost#fragmentOne"
argument_list|)
decl_stmt|;
name|URI
name|resultUri
init|=
name|URISupport
operator|.
name|createURIWithQuery
argument_list|(
name|uri
argument_list|,
literal|"utm_campaign=launch"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resultUri
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"smtp://localhost?utm_campaign=launch#fragmentOne"
argument_list|,
name|resultUri
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNormalizeEndpointWithEqualSignInParameter ()
specifier|public
name|void
name|testNormalizeEndpointWithEqualSignInParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"jms:queue:foo?selector=somekey='somevalue'&foo=bar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
comment|// Camel will safe encode the URI
name|assertEquals
argument_list|(
literal|"jms://queue:foo?foo=bar&selector=somekey%3D%27somevalue%27"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testNormalizeEndpointWithPercentSignInParameter ()
specifier|public
name|void
name|testNormalizeEndpointWithPercentSignInParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"http://someendpoint?username=james&password=%25test"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
comment|// Camel will safe encode the URI
name|assertEquals
argument_list|(
literal|"http://someendpoint?password=%25test&username=james"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseParameters ()
specifier|public
name|void
name|testParseParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
literal|"quartz:myGroup/myTimerName?cron=0+0+*+*+*+?"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|u
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|params
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"0 0 * * * ?"
argument_list|,
name|params
operator|.
name|get
argument_list|(
literal|"cron"
argument_list|)
argument_list|)
expr_stmt|;
name|u
operator|=
operator|new
name|URI
argument_list|(
literal|"quartz:myGroup/myTimerName?cron=0+0+*+*+*+?&bar=123"
argument_list|)
expr_stmt|;
name|params
operator|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|u
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|params
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"0 0 * * * ?"
argument_list|,
name|params
operator|.
name|get
argument_list|(
literal|"cron"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|params
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateRemainingURIEncoding ()
specifier|public
name|void
name|testCreateRemainingURIEncoding
parameter_list|()
throws|throws
name|Exception
block|{
comment|// the uri is already encoded, but we create a new one with new query parameters
name|String
name|uri
init|=
literal|"http://localhost:23271/myapp/mytest?columns=name%2Ctotalsens%2Cupsens&username=apiuser"
decl_stmt|;
comment|// these are the parameters which is tricky to encode
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"abc def"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|"123,456"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"S\u00F8ren"
argument_list|)
expr_stmt|;
comment|// danish letter
comment|// create new uri with the parameters
name|URI
name|out
init|=
name|URISupport
operator|.
name|createRemainingURI
argument_list|(
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:23271/myapp/mytest?foo=abc+def&bar=123%2C456&name=S%C3%B8ren"
argument_list|,
name|out
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:23271/myapp/mytest?foo=abc+def&bar=123%2C456&name=S%C3%B8ren"
argument_list|,
name|out
operator|.
name|toASCIIString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNormalizeEndpointUriWithDualParameters ()
specifier|public
name|void
name|testNormalizeEndpointUriWithDualParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out1
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"smtp://localhost?to=foo&to=bar&from=me"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"smtp://localhost?from=me&to=foo&to=bar"
argument_list|,
name|out1
argument_list|)
expr_stmt|;
name|String
name|out2
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"smtp://localhost?to=foo&to=bar&from=me&from=you"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"smtp://localhost?from=me&from=you&to=foo&to=bar"
argument_list|,
name|out2
argument_list|)
expr_stmt|;
block|}
DECL|method|testSanitizeUriWithUserInfo ()
specifier|public
name|void
name|testSanitizeUriWithUserInfo
parameter_list|()
block|{
name|String
name|uri
init|=
literal|"jt400://GEORGE:HARRISON@LIVERPOOL/QSYS.LIB/BEATLES.LIB/PENNYLANE.DTAQ"
decl_stmt|;
name|String
name|expected
init|=
literal|"jt400://GEORGE:xxxxxx@LIVERPOOL/QSYS.LIB/BEATLES.LIB/PENNYLANE.DTAQ"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSanitizePathWithUserInfo ()
specifier|public
name|void
name|testSanitizePathWithUserInfo
parameter_list|()
block|{
name|String
name|path
init|=
literal|"GEORGE:HARRISON@LIVERPOOL/QSYS.LIB/BEATLES.LIB/PENNYLANE.PGM"
decl_stmt|;
name|String
name|expected
init|=
literal|"GEORGE:xxxxxx@LIVERPOOL/QSYS.LIB/BEATLES.LIB/PENNYLANE.PGM"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|URISupport
operator|.
name|sanitizePath
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSanitizePathWithoutSensitiveInfoIsUnchanged ()
specifier|public
name|void
name|testSanitizePathWithoutSensitiveInfoIsUnchanged
parameter_list|()
block|{
name|String
name|path
init|=
literal|"myhost:8080/mypath"
decl_stmt|;
name|assertEquals
argument_list|(
name|path
argument_list|,
name|URISupport
operator|.
name|sanitizePath
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSanitizeUriWithRawPassword ()
specifier|public
name|void
name|testSanitizeUriWithRawPassword
parameter_list|()
block|{
name|String
name|uri
init|=
literal|"http://foo?username=me&password=RAW(me#@123)&foo=bar"
decl_stmt|;
name|String
name|expected
init|=
literal|"http://foo?username=me&password=xxxxxx&foo=bar"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSanitizeUriRawUnsafePassword ()
specifier|public
name|void
name|testSanitizeUriRawUnsafePassword
parameter_list|()
block|{
name|String
name|uri
init|=
literal|"sftp://localhost/target?password=RAW(beforeAmp&afterAmp)&username=jrandom"
decl_stmt|;
name|String
name|expected
init|=
literal|"sftp://localhost/target?password=xxxxxx&username=jrandom"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNormalizeEndpointUriWithUserInfoSpecialSign ()
specifier|public
name|void
name|testNormalizeEndpointUriWithUserInfoSpecialSign
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out1
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"ftp://us%40r:t%st@localhost:21000/tmp3/camel?foo=us@r"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ftp://us%40r:t%25st@localhost:21000/tmp3/camel?foo=us%40r"
argument_list|,
name|out1
argument_list|)
expr_stmt|;
name|String
name|out2
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"ftp://us%40r:t%25st@localhost:21000/tmp3/camel?foo=us@r"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ftp://us%40r:t%25st@localhost:21000/tmp3/camel?foo=us%40r"
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|String
name|out3
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"ftp://us@r:t%st@localhost:21000/tmp3/camel?foo=us@r"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ftp://us%40r:t%25st@localhost:21000/tmp3/camel?foo=us%40r"
argument_list|,
name|out3
argument_list|)
expr_stmt|;
name|String
name|out4
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"ftp://us@r:t%25st@localhost:21000/tmp3/camel?foo=us@r"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ftp://us%40r:t%25st@localhost:21000/tmp3/camel?foo=us%40r"
argument_list|,
name|out4
argument_list|)
expr_stmt|;
block|}
DECL|method|testSpecialUriFromXmppComponent ()
specifier|public
name|void
name|testSpecialUriFromXmppComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out1
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"xmpp://camel-user@localhost:123/test-user@localhost?password=secret&serviceName=someCoolChat"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"xmpp://camel-user@localhost:123/test-user@localhost?password=secret&serviceName=someCoolChat"
argument_list|,
name|out1
argument_list|)
expr_stmt|;
block|}
DECL|method|testRawParameter ()
specifier|public
name|void
name|testRawParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"xmpp://camel-user@localhost:123/test-user@localhost?password=RAW(++?w0rd)&serviceName=some chat"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"xmpp://camel-user@localhost:123/test-user@localhost?password=RAW(++?w0rd)&serviceName=some+chat"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|String
name|out2
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"xmpp://camel-user@localhost:123/test-user@localhost?password=RAW(foo %% bar)&serviceName=some chat"
argument_list|)
decl_stmt|;
comment|// Just make sure the RAW parameter can be resolved rightly, we need to replace the % into %25
name|assertEquals
argument_list|(
literal|"xmpp://camel-user@localhost:123/test-user@localhost?password=RAW(foo %25%25 bar)&serviceName=some+chat"
argument_list|,
name|out2
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseQuery ()
specifier|public
name|void
name|testParseQuery
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
name|map
init|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
literal|"password=secret&serviceName=somechat"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"secret"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"somechat"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"serviceName"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
literal|"password=RAW(++?w0rd)&serviceName=somechat"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"RAW(++?w0rd)"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"somechat"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"serviceName"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
literal|"password=RAW(++?)w&rd)&serviceName=somechat"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"RAW(++?)w&rd)"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"somechat"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"serviceName"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
literal|"password=RAW(%2520w&rd)&serviceName=somechat"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"RAW(%2520w&rd)"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"somechat"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"serviceName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseQueryLenient ()
specifier|public
name|void
name|testParseQueryLenient
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|URISupport
operator|.
name|parseQuery
argument_list|(
literal|"password=secret&serviceName=somechat&"
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
literal|"password=secret&serviceName=somechat&"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"secret"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"somechat"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"serviceName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testResolveRawParameterValues ()
specifier|public
name|void
name|testResolveRawParameterValues
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
name|map
init|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
literal|"password=secret&serviceName=somechat"
argument_list|)
decl_stmt|;
name|URISupport
operator|.
name|resolveRawParameterValues
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"secret"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"somechat"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"serviceName"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
literal|"password=RAW(++?w0rd)&serviceName=somechat"
argument_list|)
expr_stmt|;
name|URISupport
operator|.
name|resolveRawParameterValues
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"++?w0rd"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"somechat"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"serviceName"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
literal|"password=RAW(++?)w&rd)&serviceName=somechat"
argument_list|)
expr_stmt|;
name|URISupport
operator|.
name|resolveRawParameterValues
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"++?)w&rd"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"somechat"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"serviceName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAppendParameterToUriAndReplaceExistingOne ()
specifier|public
name|void
name|testAppendParameterToUriAndReplaceExistingOne
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
name|newParameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|newParameters
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"456"
argument_list|)
expr_stmt|;
name|newParameters
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|String
name|newUri
init|=
name|URISupport
operator|.
name|appendParametersToURI
argument_list|(
literal|"stub:foo?foo=123"
argument_list|,
name|newParameters
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"stub://foo?foo=456&bar=yes"
argument_list|,
name|newUri
argument_list|)
expr_stmt|;
block|}
DECL|method|testPathAndQueryOf ()
specifier|public
name|void
name|testPathAndQueryOf
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|URISupport
operator|.
name|pathAndQueryOf
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://localhost"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|URISupport
operator|.
name|pathAndQueryOf
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://localhost:80"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|URISupport
operator|.
name|pathAndQueryOf
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://localhost:80/"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/path"
argument_list|,
name|URISupport
operator|.
name|pathAndQueryOf
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://localhost:80/path"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/path/"
argument_list|,
name|URISupport
operator|.
name|pathAndQueryOf
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://localhost:80/path/"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/path?query=value"
argument_list|,
name|URISupport
operator|.
name|pathAndQueryOf
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://localhost:80/path?query=value"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

