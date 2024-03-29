begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|event
operator|.
name|CacheEntryEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|event
operator|.
name|CacheEntryEventFilter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|event
operator|.
name|CacheEntryListenerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|event
operator|.
name|EventType
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
name|BindToRegistry
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

begin_class
DECL|class|JCacheConsumerTest
specifier|public
class|class
name|JCacheConsumerTest
extends|extends
name|JCacheComponentTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"myFilter"
argument_list|)
DECL|method|addCacheEntryEventFilter ()
specifier|public
name|CacheEntryEventFilter
name|addCacheEntryEventFilter
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|CacheEntryEventFilter
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|evaluate
parameter_list|(
name|CacheEntryEvent
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|event
parameter_list|)
throws|throws
name|CacheEntryListenerException
block|{
if|if
condition|(
name|event
operator|.
name|getEventType
argument_list|()
operator|==
name|EventType
operator|.
name|REMOVED
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|val
init|=
name|event
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
operator|!
name|val
operator|.
name|startsWith
argument_list|(
literal|"to-filter-"
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testFilters ()
specifier|public
name|void
name|testFilters
parameter_list|()
throws|throws
name|Exception
block|{
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
name|val1
init|=
literal|"to-filter-"
operator|+
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
name|val1
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|val2
argument_list|)
expr_stmt|;
name|cache
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockCreated
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:created"
argument_list|)
decl_stmt|;
name|mockCreated
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockCreated
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
name|mockCreated
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
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|equals
argument_list|(
name|val1
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockUpdated
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:updated"
argument_list|)
decl_stmt|;
name|mockUpdated
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockUpdated
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
name|mockUpdated
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
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|body
operator|.
name|equalsIgnoreCase
argument_list|(
name|val2
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockRemoved
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:removed"
argument_list|)
decl_stmt|;
name|mockRemoved
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockRemoved
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
name|mockRemoved
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
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|body
operator|==
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockMyFilter
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:my-filter"
argument_list|)
decl_stmt|;
name|mockMyFilter
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockMyFilter
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
name|mockMyFilter
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
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|body
operator|.
name|equalsIgnoreCase
argument_list|(
name|val2
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
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
literal|"jcache://test-cache?filteredEvents=UPDATED,REMOVED,EXPIRED"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:created"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jcache://test-cache?filteredEvents=CREATED,REMOVED,EXPIRED"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:updated"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jcache://test-cache?filteredEvents=CREATED,UPDATED,EXPIRED"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:removed"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jcache://test-cache?eventFilters=#myFilter"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:my-filter"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

