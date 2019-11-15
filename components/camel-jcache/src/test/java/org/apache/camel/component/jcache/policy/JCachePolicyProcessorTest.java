begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcache.policy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcache
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|Caching
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|configuration
operator|.
name|MutableConfiguration
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
name|LoggingLevel
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
name|mock
operator|.
name|MockEndpoint
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
name|language
operator|.
name|simple
operator|.
name|types
operator|.
name|SimpleIllegalSyntaxException
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
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|JCachePolicyProcessorTest
specifier|public
class|class
name|JCachePolicyProcessorTest
extends|extends
name|JCachePolicyTestBase
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JCachePolicyProcessorTest
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//Basic test to verify value gets cached and route is not executed for the second time
annotation|@
name|Test
DECL|method|testValueGetsCached ()
specifier|public
name|void
name|testValueGetsCached
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
decl_stmt|;
name|Cache
name|cache
init|=
name|lookupCache
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
comment|//Send first, key is not in cache
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:cached-simple"
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|//We got back the value, mock was called once, value got cached.
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//Send again, key is already in cache
name|responseBody
operator|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:cached-simple"
argument_list|,
name|key
argument_list|)
expr_stmt|;
comment|//We got back the stored value, but the mock was not called again
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Verify policy applies only on the section of the route wrapped
annotation|@
name|Test
DECL|method|testPartial ()
specifier|public
name|void
name|testPartial
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mockUnwrapped
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:unwrapped"
argument_list|)
decl_stmt|;
comment|//Send first, key is not in cache
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:cached-partial"
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|//We got back the value, mock was called once, value got cached.
name|Cache
name|cache
init|=
name|lookupCache
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mockUnwrapped
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//Send again, key is already in cache
name|responseBody
operator|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:cached-partial"
argument_list|,
name|key
argument_list|)
expr_stmt|;
comment|//We got back the stored value, the mock was not called again, but the unwrapped mock was
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mockUnwrapped
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Cache is closed
annotation|@
name|Test
DECL|method|testClosedCache ()
specifier|public
name|void
name|testClosedCache
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
decl_stmt|;
comment|//Send first, key is not in cache
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:cached-closed"
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|//We got back the value, mock was called once
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//Send again, cache is closed
name|responseBody
operator|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:cached-closed"
argument_list|,
name|key
argument_list|)
expr_stmt|;
comment|//We got back the stored value, mock was called again
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Key is already stored
annotation|@
name|Test
DECL|method|testValueWasCached ()
specifier|public
name|void
name|testValueWasCached
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|value
init|=
literal|"test"
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
decl_stmt|;
comment|//Prestore value in cache
name|Cache
name|cache
init|=
name|lookupCache
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|//Send first, key is already in cache
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:cached-simple"
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|//We got back the value, mock was not called, cache was not modified
name|assertEquals
argument_list|(
name|value
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Null final body
annotation|@
name|Test
DECL|method|testNullResult ()
specifier|public
name|void
name|testNullResult
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|whenAnyExchangeReceived
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
comment|//Send first
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:cached-simple"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//Send again, nothing was cached
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:cached-simple"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Use a key expression ${header.mykey}
annotation|@
name|Test
DECL|method|testKeyExpression ()
specifier|public
name|void
name|testKeyExpression
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|body
init|=
name|randomString
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
decl_stmt|;
name|Cache
name|cache
init|=
name|lookupCache
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
comment|//Send first, key is not in cache
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:cached-byheader"
argument_list|,
name|body
argument_list|,
literal|"mykey"
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|//We got back the value, mock was called once, value got cached.
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|body
argument_list|)
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|body
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//Send again, use another body, but the same key
name|responseBody
operator|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:cached-byheader"
argument_list|,
name|randomString
argument_list|()
argument_list|,
literal|"mykey"
argument_list|,
name|key
argument_list|)
expr_stmt|;
comment|//We got back the stored value, and the mock was not called again
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|body
argument_list|)
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|body
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Key is null, ${header.mykey} is not set
annotation|@
name|Test
DECL|method|testKeyNull ()
specifier|public
name|void
name|testKeyNull
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
name|String
name|body
init|=
name|randomString
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
decl_stmt|;
name|Cache
name|cache
init|=
name|lookupCache
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
comment|//Send first, expected header is not set
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:cached-byheader"
argument_list|,
name|body
argument_list|)
decl_stmt|;
comment|//We got back the value, mock was called once, nothing is cached.
name|assertFalse
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|"null"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|body
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//Send again, use another body, but the same key
name|body
operator|=
name|randomString
argument_list|()
expr_stmt|;
name|responseBody
operator|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:cached-byheader"
argument_list|,
name|body
argument_list|)
expr_stmt|;
comment|//We got back the value, mock was called again, nothing is cached
name|assertFalse
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|"null"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|body
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Use an invalid key expression causing an exception
annotation|@
name|Test
DECL|method|testInvalidKeyExpression ()
specifier|public
name|void
name|testInvalidKeyExpression
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|body
init|=
name|randomString
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
decl_stmt|;
name|Cache
name|cache
init|=
name|lookupCache
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
comment|//Send
name|Exchange
name|response
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|request
argument_list|(
literal|"direct:cached-invalidkey"
argument_list|,
name|e
lambda|->
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
argument_list|)
decl_stmt|;
comment|//Exception is on the exchange, cache is empty, onException was called.
name|assertIsInstanceOf
argument_list|(
name|SimpleIllegalSyntaxException
operator|.
name|class
argument_list|,
name|response
operator|.
name|getException
argument_list|()
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"exception-"
operator|+
name|body
argument_list|,
name|response
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Value is cached after handled exception
annotation|@
name|Test
DECL|method|testHandledException ()
specifier|public
name|void
name|testHandledException
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
decl_stmt|;
name|Cache
name|cache
init|=
name|lookupCache
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
comment|//Send first, key is not in cache
name|Object
name|responseBody
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:cached-exception"
argument_list|,
name|key
argument_list|)
decl_stmt|;
comment|//We got back the value after exception handler, mock was called once, value got cached.
name|assertEquals
argument_list|(
literal|"handled-"
operator|+
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"handled-"
operator|+
name|generateValue
argument_list|(
name|key
argument_list|)
argument_list|,
name|responseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Nothing is cached after an unhandled exception
annotation|@
name|Test
DECL|method|testException ()
specifier|public
name|void
name|testException
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:value"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|whenAnyExchangeReceived
argument_list|(
name|e
lambda|->
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"unexpected"
argument_list|)
throw|;
block|}
argument_list|)
expr_stmt|;
name|Cache
name|cache
init|=
name|lookupCache
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
comment|//Send
name|Exchange
name|response
init|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|request
argument_list|(
literal|"direct:cached-exception"
argument_list|,
name|e
lambda|->
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
comment|//Exception is on the exchange, cache is empty
name|assertEquals
argument_list|(
literal|"unexpected"
argument_list|,
name|response
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|CacheManager
name|cacheManager
init|=
name|Caching
operator|.
name|getCachingProvider
argument_list|()
operator|.
name|getCacheManager
argument_list|()
decl_stmt|;
comment|//Simple cache - with default config
name|Cache
name|cache
init|=
name|cacheManager
operator|.
name|createCache
argument_list|(
literal|"simple"
argument_list|,
operator|new
name|MutableConfiguration
argument_list|<>
argument_list|()
argument_list|)
decl_stmt|;
name|JCachePolicy
name|jcachePolicy
init|=
operator|new
name|JCachePolicy
argument_list|()
decl_stmt|;
name|jcachePolicy
operator|.
name|setCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:cached-simple"
argument_list|)
operator|.
name|policy
argument_list|(
name|jcachePolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:value"
argument_list|)
expr_stmt|;
comment|//Example to wrap only part of the route
name|from
argument_list|(
literal|"direct:cached-partial"
argument_list|)
operator|.
name|policy
argument_list|(
name|jcachePolicy
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|DEBUG
argument_list|,
name|LOG
argument_list|,
literal|"Executing route, not found in cache. body:${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:value"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|DEBUG
argument_list|,
name|LOG
argument_list|,
literal|"This is always called. body:${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unwrapped"
argument_list|)
expr_stmt|;
comment|//Cache after exception handling
name|from
argument_list|(
literal|"direct:cached-exception"
argument_list|)
operator|.
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|onWhen
argument_list|(
name|exceptionMessage
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"test"
argument_list|)
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"handled-${body}"
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|policy
argument_list|(
name|jcachePolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:value"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|Exception
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
comment|//Closed cache
name|cache
operator|=
name|cacheManager
operator|.
name|createCache
argument_list|(
literal|"closed"
argument_list|,
operator|new
name|MutableConfiguration
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|close
argument_list|()
expr_stmt|;
name|jcachePolicy
operator|=
operator|new
name|JCachePolicy
argument_list|()
expr_stmt|;
name|jcachePolicy
operator|.
name|setCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:cached-closed"
argument_list|)
operator|.
name|policy
argument_list|(
name|jcachePolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:value"
argument_list|)
expr_stmt|;
comment|//Use ${header.mykey} as the key
name|jcachePolicy
operator|=
operator|new
name|JCachePolicy
argument_list|()
expr_stmt|;
name|jcachePolicy
operator|.
name|setCache
argument_list|(
name|cacheManager
operator|.
name|getCache
argument_list|(
literal|"simple"
argument_list|)
argument_list|)
expr_stmt|;
name|jcachePolicy
operator|.
name|setKeyExpression
argument_list|(
name|simple
argument_list|(
literal|"${header.mykey}"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:cached-byheader"
argument_list|)
operator|.
name|policy
argument_list|(
name|jcachePolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:value"
argument_list|)
expr_stmt|;
comment|//Use an invalid keyExpression
name|jcachePolicy
operator|=
operator|new
name|JCachePolicy
argument_list|()
expr_stmt|;
name|jcachePolicy
operator|.
name|setCache
argument_list|(
name|cacheManager
operator|.
name|getCache
argument_list|(
literal|"simple"
argument_list|)
argument_list|)
expr_stmt|;
name|jcachePolicy
operator|.
name|setKeyExpression
argument_list|(
name|simple
argument_list|(
literal|"${unexpected}"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:cached-invalidkey"
argument_list|)
operator|.
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"exception-${body}"
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|policy
argument_list|(
name|jcachePolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:value"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

