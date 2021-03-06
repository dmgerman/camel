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
name|test
operator|.
name|spring
operator|.
name|CamelSpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import static
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
operator|.
name|JCachePolicyTestBase
operator|.
name|*
import|;
end_import

begin_class
DECL|class|SpringJCachePolicyTest
specifier|public
class|class
name|SpringJCachePolicyTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/jcache/policy/SpringJCachePolicyTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|BeforeClass
DECL|method|beforeAll ()
specifier|public
specifier|static
name|void
name|beforeAll
parameter_list|()
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"hazelcast.config"
argument_list|,
literal|"classpath:org/apache/camel/component/jcache/policy/hazelcast-spring.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|afterAll ()
specifier|public
specifier|static
name|void
name|afterAll
parameter_list|()
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"hazelcast.config"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
block|{
comment|//reset mock
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:spring"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
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
name|generateValue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//Verify value gets cached and route is not executed for the second time
annotation|@
name|Test
DECL|method|testXmlDslValueGetsCached ()
specifier|public
name|void
name|testXmlDslValueGetsCached
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
literal|"mock:spring"
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
literal|"direct:spring"
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
literal|"spring"
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
literal|"direct:spring"
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
comment|//Verify if we call the route with different keys, both gets cached
annotation|@
name|Test
DECL|method|testXmlDslDifferent ()
specifier|public
name|void
name|testXmlDslDifferent
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|key1
init|=
name|randomString
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:spring"
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
literal|"direct:spring"
argument_list|,
name|key1
argument_list|)
decl_stmt|;
comment|//We got back the value, mock was called once, value got cached.
name|Cache
name|cache
init|=
name|lookupCache
argument_list|(
literal|"spring"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key1
argument_list|)
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key1
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
comment|//Send again, different key
specifier|final
name|String
name|key2
init|=
name|randomString
argument_list|()
decl_stmt|;
name|responseBody
operator|=
name|this
operator|.
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:spring"
argument_list|,
name|key2
argument_list|)
expr_stmt|;
comment|//We got back the stored value, mock was called again, value got cached.
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key2
argument_list|)
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|generateValue
argument_list|(
name|key2
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
comment|//Verify policy applies only on the section of the route wrapped
annotation|@
name|Test
DECL|method|testXmlDslPartial ()
specifier|public
name|void
name|testXmlDslPartial
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
literal|"mock:spring"
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
literal|"direct:spring-partial"
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
literal|"spring"
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
literal|"direct:spring-partial"
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
comment|//Use a key expression ${header.mykey}
annotation|@
name|Test
DECL|method|testXmlDslKeyExpression ()
specifier|public
name|void
name|testXmlDslKeyExpression
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
literal|"mock:spring"
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
literal|"direct:spring-byheader"
argument_list|,
name|body
argument_list|,
literal|"mykey"
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
literal|"spring"
argument_list|)
decl_stmt|;
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
literal|"direct:spring-byheader"
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
block|}
end_class

end_unit

