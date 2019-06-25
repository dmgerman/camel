begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|java
operator|.
name|util
operator|.
name|Objects
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Iterators
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
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
name|Route
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
name|ServiceStatus
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
name|ignite
operator|.
name|cache
operator|.
name|IgniteCacheComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|IgniteCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|cache
operator|.
name|CacheEntryEventSerializableFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|cache
operator|.
name|query
operator|.
name|ScanQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|lang
operator|.
name|IgniteBiPredicate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assert_
import|;
end_import

begin_class
DECL|class|IgniteCacheContinuousQueryTest
specifier|public
class|class
name|IgniteCacheContinuousQueryTest
extends|extends
name|AbstractIgniteTest
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"query1"
argument_list|)
DECL|field|scanQuery1
specifier|private
name|ScanQuery
argument_list|<
name|Integer
argument_list|,
name|Person
argument_list|>
name|scanQuery1
init|=
operator|new
name|ScanQuery
argument_list|<>
argument_list|(
operator|new
name|IgniteBiPredicate
argument_list|<
name|Integer
argument_list|,
name|Person
argument_list|>
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|Integer
name|key
parameter_list|,
name|Person
name|person
parameter_list|)
block|{
return|return
name|person
operator|.
name|getId
argument_list|()
operator|>
literal|50
return|;
block|}
block|}
argument_list|)
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"remoteFilter1"
argument_list|)
DECL|field|remoteFilter
specifier|private
name|CacheEntryEventSerializableFilter
argument_list|<
name|Integer
argument_list|,
name|Person
argument_list|>
name|remoteFilter
init|=
operator|new
name|CacheEntryEventSerializableFilter
argument_list|<
name|Integer
argument_list|,
name|IgniteCacheContinuousQueryTest
operator|.
name|Person
argument_list|>
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|5624973479995548199L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|evaluate
parameter_list|(
name|CacheEntryEvent
argument_list|<
name|?
extends|extends
name|Integer
argument_list|,
name|?
extends|extends
name|Person
argument_list|>
name|event
parameter_list|)
throws|throws
name|CacheEntryListenerException
block|{
return|return
name|event
operator|.
name|getValue
argument_list|()
operator|.
name|getId
argument_list|()
operator|>
literal|150
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|Override
DECL|method|getScheme ()
specifier|protected
name|String
name|getScheme
parameter_list|()
block|{
return|return
literal|"ignite-cache"
return|;
block|}
annotation|@
name|Override
DECL|method|createComponent ()
specifier|protected
name|AbstractIgniteComponent
name|createComponent
parameter_list|()
block|{
return|return
name|IgniteCacheComponent
operator|.
name|fromConfiguration
argument_list|(
name|createConfiguration
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testContinuousQueryDoNotFireExistingEntries ()
specifier|public
name|void
name|testContinuousQueryDoNotFireExistingEntries
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"continuousQuery"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:test1"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|Person
argument_list|>
name|persons
init|=
name|createPersons
argument_list|(
literal|1
argument_list|,
literal|100
argument_list|)
decl_stmt|;
name|IgniteCache
argument_list|<
name|Integer
argument_list|,
name|Person
argument_list|>
name|cache
init|=
name|ignite
argument_list|()
operator|.
name|getOrCreateCache
argument_list|(
literal|"testcontinuous1"
argument_list|)
decl_stmt|;
name|cache
operator|.
name|putAll
argument_list|(
name|persons
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|getMockEndpoint
argument_list|(
literal|"mock:test1"
argument_list|)
operator|.
name|getExchanges
argument_list|()
control|)
block|{
name|assert_
argument_list|()
operator|.
name|that
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_CACHE_NAME
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"testcontinuous1"
argument_list|)
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|that
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_CACHE_EVENT_TYPE
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|EventType
operator|.
name|CREATED
argument_list|)
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|that
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_CACHE_KEY
argument_list|)
argument_list|)
operator|.
name|isIn
argument_list|(
name|persons
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|that
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
operator|.
name|isIn
argument_list|(
name|persons
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testContinuousQueryFireExistingEntriesWithQuery ()
specifier|public
name|void
name|testContinuousQueryFireExistingEntriesWithQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:test2"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|Person
argument_list|>
name|persons
init|=
name|createPersons
argument_list|(
literal|1
argument_list|,
literal|100
argument_list|)
decl_stmt|;
name|IgniteCache
argument_list|<
name|Integer
argument_list|,
name|Person
argument_list|>
name|cache
init|=
name|ignite
argument_list|()
operator|.
name|getOrCreateCache
argument_list|(
literal|"testcontinuous1"
argument_list|)
decl_stmt|;
name|cache
operator|.
name|putAll
argument_list|(
name|persons
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"continuousQuery.fireExistingEntries"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:test2"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|persons
operator|=
name|createPersons
argument_list|(
literal|101
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|cache
operator|.
name|putAll
argument_list|(
name|persons
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testContinuousQueryFireExistingEntriesWithQueryAndRemoteFilter ()
specifier|public
name|void
name|testContinuousQueryFireExistingEntriesWithQueryAndRemoteFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:test3"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|Person
argument_list|>
name|persons
init|=
name|createPersons
argument_list|(
literal|1
argument_list|,
literal|100
argument_list|)
decl_stmt|;
name|IgniteCache
argument_list|<
name|Integer
argument_list|,
name|Person
argument_list|>
name|cache
init|=
name|ignite
argument_list|()
operator|.
name|getOrCreateCache
argument_list|(
literal|"testcontinuous1"
argument_list|)
decl_stmt|;
name|cache
operator|.
name|putAll
argument_list|(
name|persons
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"remoteFilter"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:test3"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|persons
operator|=
name|createPersons
argument_list|(
literal|101
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|cache
operator|.
name|putAll
argument_list|(
name|persons
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testContinuousQueryGroupedUpdates ()
specifier|public
name|void
name|testContinuousQueryGroupedUpdates
parameter_list|()
throws|throws
name|Exception
block|{
comment|// One hundred Iterables of 1 item each.
name|getMockEndpoint
argument_list|(
literal|"mock:test4"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"groupedUpdate"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|Person
argument_list|>
name|persons
init|=
name|createPersons
argument_list|(
literal|1
argument_list|,
literal|100
argument_list|)
decl_stmt|;
name|IgniteCache
argument_list|<
name|Integer
argument_list|,
name|Person
argument_list|>
name|cache
init|=
name|ignite
argument_list|()
operator|.
name|getOrCreateCache
argument_list|(
literal|"testcontinuous1"
argument_list|)
decl_stmt|;
name|cache
operator|.
name|putAll
argument_list|(
name|persons
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|getMockEndpoint
argument_list|(
literal|"mock:test4"
argument_list|)
operator|.
name|getExchanges
argument_list|()
control|)
block|{
name|assert_
argument_list|()
operator|.
name|that
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_CACHE_NAME
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"testcontinuous1"
argument_list|)
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|that
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|Iterable
operator|.
name|class
argument_list|)
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|that
argument_list|(
name|Iterators
operator|.
name|size
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Iterable
operator|.
name|class
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"ignite-cache:testcontinuous1?query=#query1"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"continuousQuery"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:test1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"ignite-cache:testcontinuous1?query=#query1&fireExistingQueryResults=true"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"continuousQuery.fireExistingEntries"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:test2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"ignite-cache:testcontinuous1?query=#query1&remoteFilter=#remoteFilter1&fireExistingQueryResults=true"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"remoteFilter"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:test3"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"ignite-cache:testcontinuous1?pageSize=10&oneExchangePerUpdate=false"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"groupedUpdate"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:test4"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createPersons (int from, int count)
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|Person
argument_list|>
name|createPersons
parameter_list|(
name|int
name|from
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|Person
argument_list|>
name|answer
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|int
name|max
init|=
name|from
operator|+
name|count
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|from
init|;
name|i
operator|<
name|max
condition|;
name|i
operator|++
control|)
block|{
name|answer
operator|.
name|put
argument_list|(
name|i
argument_list|,
name|Person
operator|.
name|create
argument_list|(
name|i
argument_list|,
literal|"name"
operator|+
name|i
argument_list|,
literal|"surname"
operator|+
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
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
return|return
literal|true
return|;
block|}
annotation|@
name|After
DECL|method|deleteCaches ()
specifier|public
name|void
name|deleteCaches
parameter_list|()
block|{
for|for
control|(
name|String
name|cacheName
range|:
name|ImmutableSet
operator|.
expr|<
name|String
operator|>
name|of
argument_list|(
literal|"testcontinuous1"
argument_list|,
literal|"testcontinuous2"
argument_list|,
literal|"testcontinuous3"
argument_list|)
control|)
block|{
name|IgniteCache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|cache
init|=
name|ignite
argument_list|()
operator|.
name|cache
argument_list|(
name|cacheName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|After
DECL|method|stopAllRoutes ()
specifier|public
name|void
name|stopAllRoutes
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|Route
name|route
range|:
name|context
operator|.
name|getRoutes
argument_list|()
control|)
block|{
if|if
condition|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
operator|!=
name|ServiceStatus
operator|.
name|Started
condition|)
block|{
return|return;
block|}
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|resetMocks
argument_list|()
expr_stmt|;
block|}
DECL|class|Person
specifier|public
specifier|static
class|class
name|Person
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|6582521698437964648L
decl_stmt|;
DECL|field|id
specifier|private
name|Integer
name|id
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|surname
specifier|private
name|String
name|surname
decl_stmt|;
DECL|method|create (Integer id, String name, String surname)
specifier|public
specifier|static
name|Person
name|create
parameter_list|(
name|Integer
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|surname
parameter_list|)
block|{
name|Person
name|p
init|=
operator|new
name|Person
argument_list|()
decl_stmt|;
name|p
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|p
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|p
operator|.
name|setSurname
argument_list|(
name|surname
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
DECL|method|getId ()
specifier|public
name|Integer
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (Integer id)
specifier|public
name|void
name|setId
parameter_list|(
name|Integer
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getSurname ()
specifier|public
name|String
name|getSurname
parameter_list|()
block|{
return|return
name|surname
return|;
block|}
DECL|method|setSurname (String surname)
specifier|public
name|void
name|setSurname
parameter_list|(
name|String
name|surname
parameter_list|)
block|{
name|this
operator|.
name|surname
operator|=
name|surname
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|id
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|id
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|name
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|name
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
operator|(
name|surname
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|surname
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
literal|null
operator|||
operator|!
operator|(
name|obj
operator|instanceof
name|Person
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Person
name|other
init|=
operator|(
name|Person
operator|)
name|obj
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|this
operator|.
name|id
argument_list|,
name|other
operator|.
name|id
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|this
operator|.
name|name
argument_list|,
name|other
operator|.
name|name
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|this
operator|.
name|surname
argument_list|,
name|other
operator|.
name|surname
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

