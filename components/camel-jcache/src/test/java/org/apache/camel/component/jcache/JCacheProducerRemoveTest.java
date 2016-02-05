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

begin_class
DECL|class|JCacheProducerRemoveTest
specifier|public
class|class
name|JCacheProducerRemoveTest
extends|extends
name|JCacheComponentTestSupport
block|{
annotation|@
name|Test
DECL|method|testRemove ()
specifier|public
name|void
name|testRemove
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
literal|"REMOVE"
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
literal|"direct:remove"
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
literal|"mock:remove"
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
name|expectedHeaderReceived
argument_list|(
name|JCacheConstants
operator|.
name|RESULT
argument_list|,
literal|true
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
DECL|method|testRemoveIf ()
specifier|public
name|void
name|testRemoveIf
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
literal|"REMOVE"
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
name|headers
operator|.
name|put
argument_list|(
name|JCacheConstants
operator|.
name|OLD_VALUE
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:remove-if"
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
literal|"mock:remove-if"
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
name|expectedHeaderReceived
argument_list|(
name|JCacheConstants
operator|.
name|RESULT
argument_list|,
literal|true
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
DECL|method|testRemoveIfFailure ()
specifier|public
name|void
name|testRemoveIfFailure
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
literal|"REMOVE"
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
name|headers
operator|.
name|put
argument_list|(
name|JCacheConstants
operator|.
name|OLD_VALUE
argument_list|,
literal|"x"
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:remove-if-failure"
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
literal|"mock:remove-if-failure"
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
name|expectedHeaderReceived
argument_list|(
name|JCacheConstants
operator|.
name|RESULT
argument_list|,
literal|false
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
block|}
annotation|@
name|Test
DECL|method|testRemoveAll ()
specifier|public
name|void
name|testRemoveAll
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
name|generateRandomMap
argument_list|(
literal|2
argument_list|)
decl_stmt|;
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
literal|"REMOVEALL"
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:remove-all"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|values
operator|.
name|keySet
argument_list|()
control|)
block|{
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
block|}
annotation|@
name|Test
DECL|method|testRemoveSubset ()
specifier|public
name|void
name|testRemoveSubset
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
name|values1
init|=
name|generateRandomMap
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|values2
init|=
name|generateRandomMap
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|cache
operator|.
name|putAll
argument_list|(
name|values1
argument_list|)
expr_stmt|;
name|cache
operator|.
name|putAll
argument_list|(
name|values2
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
literal|"REMOVEALL"
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
name|values2
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:remove-subset"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|values1
operator|.
name|keySet
argument_list|()
control|)
block|{
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
block|}
for|for
control|(
name|Object
name|key
range|:
name|values2
operator|.
name|keySet
argument_list|()
control|)
block|{
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
literal|"direct:remove"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jcache://test-cache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:remove"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:remove-if"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jcache://test-cache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:remove-if"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:remove-if-failure"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jcache://test-cache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:remove-if-failure"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:remove-all"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jcache://test-cache"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:remove-subset"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jcache://test-cache"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

