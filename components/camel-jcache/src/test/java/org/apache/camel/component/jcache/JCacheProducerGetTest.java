begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcache
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
package|;
end_package

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
name|Predicate
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|is
import|;
end_import

begin_class
DECL|class|JCacheProducerGetTest
specifier|public
class|class
name|JCacheProducerGetTest
extends|extends
name|JCacheComponentTestSupport
block|{
annotation|@
name|Test
DECL|method|testGet ()
specifier|public
name|void
name|testGet
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|getCacheFromEndpoint
argument_list|(
literal|"jcache://test-cache"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|val
init|=
name|randomString
argument_list|()
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|headers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|JCacheConstants
operator|.
name|ACTION
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|JCacheConstants
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:get"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:get"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|JCacheConstants
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessagesMatches
argument_list|(
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"body"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|equals
argument_list|(
name|val
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetAndRemove ()
specifier|public
name|void
name|testGetAndRemove
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|getCacheFromEndpoint
argument_list|(
literal|"jcache://test-cache"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|val
init|=
name|randomString
argument_list|()
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|headers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|JCacheConstants
operator|.
name|ACTION
argument_list|,
literal|"GETANDREMOVE"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|JCacheConstants
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:get-and-remove"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:get-and-remove"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|JCacheConstants
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessagesMatches
argument_list|(
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"body"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|equals
argument_list|(
name|val
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
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
block|}
annotation|@
name|Test
DECL|method|testGetAndReplace ()
specifier|public
name|void
name|testGetAndReplace
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|getCacheFromEndpoint
argument_list|(
literal|"jcache://test-cache"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|val
init|=
name|randomString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|val2
init|=
name|randomString
argument_list|()
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|headers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|JCacheConstants
operator|.
name|ACTION
argument_list|,
literal|"GETANDREPLACE"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|JCacheConstants
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:get-and-replace"
argument_list|,
name|val2
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:get-and-replace"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|JCacheConstants
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessagesMatches
argument_list|(
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"body"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|equals
argument_list|(
name|val
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
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
name|val2
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetAndPut ()
specifier|public
name|void
name|testGetAndPut
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|getCacheFromEndpoint
argument_list|(
literal|"jcache://test-cache"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|key
init|=
name|randomString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|val2
init|=
name|randomString
argument_list|()
decl_stmt|;
name|headers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|JCacheConstants
operator|.
name|ACTION
argument_list|,
literal|"GETANDPUT"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|JCacheConstants
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:get-and-put"
argument_list|,
name|val2
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:get-and-put"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|JCacheConstants
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessagesMatches
argument_list|(
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|==
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
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
name|val2
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetAll ()
specifier|public
name|void
name|testGetAll
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|getCacheFromEndpoint
argument_list|(
literal|"jcache://test-cache"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|values
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|values
operator|.
name|put
argument_list|(
name|randomString
argument_list|()
argument_list|,
name|randomString
argument_list|()
argument_list|)
expr_stmt|;
name|values
operator|.
name|put
argument_list|(
name|randomString
argument_list|()
argument_list|,
name|randomString
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|putAll
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|headers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|JCacheConstants
operator|.
name|ACTION
argument_list|,
literal|"GETALL"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|JCacheConstants
operator|.
name|KEYS
argument_list|,
name|values
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:get-all"
argument_list|,
name|values
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:get-all"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|JCacheConstants
operator|.
name|KEYS
argument_list|,
name|values
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessagesMatches
argument_list|(
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|values
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|values
argument_list|,
name|is
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
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
name|from
argument_list|(
literal|"direct:get"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jcache://test-cache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:get"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:get-and-remove"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jcache://test-cache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:get-and-remove"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:get-and-replace"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jcache://test-cache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:get-and-replace"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:get-and-put"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jcache://test-cache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:get-and-put"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:get-all"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jcache://test-cache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:get-all"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

