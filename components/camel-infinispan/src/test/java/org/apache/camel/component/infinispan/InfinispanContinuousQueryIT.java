begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
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
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|RemoteCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|RemoteCacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|configuration
operator|.
name|ConfigurationBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|marshall
operator|.
name|MarshallerUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|commons
operator|.
name|marshall
operator|.
name|ProtoStreamMarshaller
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|commons
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|protostream
operator|.
name|FileDescriptorSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|protostream
operator|.
name|SerializationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|protostream
operator|.
name|sampledomain
operator|.
name|User
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|protostream
operator|.
name|sampledomain
operator|.
name|marshallers
operator|.
name|GenderMarshaller
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|protostream
operator|.
name|sampledomain
operator|.
name|marshallers
operator|.
name|UserMarshaller
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|query
operator|.
name|dsl
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|query
operator|.
name|dsl
operator|.
name|QueryFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|query
operator|.
name|remote
operator|.
name|client
operator|.
name|ProtobufMetadataManagerConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|query
operator|.
name|remote
operator|.
name|client
operator|.
name|impl
operator|.
name|MarshallerRegistration
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
operator|.
name|util
operator|.
name|UserUtils
operator|.
name|CQ_USERS
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
name|infinispan
operator|.
name|util
operator|.
name|UserUtils
operator|.
name|createKey
import|;
end_import

begin_class
DECL|class|InfinispanContinuousQueryIT
specifier|public
class|class
name|InfinispanContinuousQueryIT
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"continuousQueryBuilder"
argument_list|)
DECL|field|CONTINUOUS_QUERY_BUILDER
specifier|private
specifier|static
specifier|final
name|InfinispanQueryBuilder
name|CONTINUOUS_QUERY_BUILDER
init|=
operator|new
name|InfinispanQueryBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Query
name|build
parameter_list|(
name|QueryFactory
name|queryFactory
parameter_list|)
block|{
return|return
name|queryFactory
operator|.
name|from
argument_list|(
name|User
operator|.
name|class
argument_list|)
operator|.
name|having
argument_list|(
literal|"name"
argument_list|)
operator|.
name|like
argument_list|(
literal|"CQ%"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"continuousQueryBuilderNoMatch"
argument_list|)
DECL|field|CONTINUOUS_QUERY_BUILDER_NO_MATCH
specifier|private
specifier|static
specifier|final
name|InfinispanQueryBuilder
name|CONTINUOUS_QUERY_BUILDER_NO_MATCH
init|=
operator|new
name|InfinispanQueryBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Query
name|build
parameter_list|(
name|QueryFactory
name|queryFactory
parameter_list|)
block|{
return|return
name|queryFactory
operator|.
name|from
argument_list|(
name|User
operator|.
name|class
argument_list|)
operator|.
name|having
argument_list|(
literal|"name"
argument_list|)
operator|.
name|like
argument_list|(
literal|"%TEST%"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"continuousQueryBuilderAll"
argument_list|)
DECL|field|CONTINUOUS_QUERY_BUILDER_ALL
specifier|private
specifier|static
specifier|final
name|InfinispanQueryBuilder
name|CONTINUOUS_QUERY_BUILDER_ALL
init|=
operator|new
name|InfinispanQueryBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Query
name|build
parameter_list|(
name|QueryFactory
name|queryFactory
parameter_list|)
block|{
return|return
name|queryFactory
operator|.
name|from
argument_list|(
name|User
operator|.
name|class
argument_list|)
operator|.
name|having
argument_list|(
literal|"name"
argument_list|)
operator|.
name|like
argument_list|(
literal|"%Q0%"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"myCustomContainer"
argument_list|)
DECL|field|manager
specifier|private
name|RemoteCacheManager
name|manager
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"continuousQueryBuilder"
argument_list|)
DECL|field|cache
specifier|private
name|RemoteCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
decl_stmt|;
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|IOException
block|{
name|ConfigurationBuilder
name|builder
init|=
operator|new
name|ConfigurationBuilder
argument_list|()
operator|.
name|addServer
argument_list|()
operator|.
name|host
argument_list|(
literal|"localhost"
argument_list|)
operator|.
name|port
argument_list|(
literal|11222
argument_list|)
operator|.
name|marshaller
argument_list|(
operator|new
name|ProtoStreamMarshaller
argument_list|()
argument_list|)
decl_stmt|;
name|manager
operator|=
operator|new
name|RemoteCacheManager
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|RemoteCache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|metadataCache
init|=
name|manager
operator|.
name|getCache
argument_list|(
name|ProtobufMetadataManagerConstants
operator|.
name|PROTOBUF_METADATA_CACHE_NAME
argument_list|)
decl_stmt|;
name|metadataCache
operator|.
name|put
argument_list|(
literal|"sample_bank_account/bank.proto"
argument_list|,
name|Util
operator|.
name|read
argument_list|(
name|InfinispanContinuousQueryIT
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/sample_bank_account/bank.proto"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|MarshallerRegistration
operator|.
name|init
argument_list|(
name|MarshallerUtil
operator|.
name|getSerializationContext
argument_list|(
name|manager
argument_list|)
argument_list|)
expr_stmt|;
name|SerializationContext
name|serCtx
init|=
name|MarshallerUtil
operator|.
name|getSerializationContext
argument_list|(
name|manager
argument_list|)
decl_stmt|;
name|serCtx
operator|.
name|registerProtoFiles
argument_list|(
name|FileDescriptorSource
operator|.
name|fromResources
argument_list|(
literal|"/sample_bank_account/bank.proto"
argument_list|)
argument_list|)
expr_stmt|;
name|serCtx
operator|.
name|registerMarshaller
argument_list|(
operator|new
name|UserMarshaller
argument_list|()
argument_list|)
expr_stmt|;
name|serCtx
operator|.
name|registerMarshaller
argument_list|(
operator|new
name|GenderMarshaller
argument_list|()
argument_list|)
expr_stmt|;
comment|// pre-load data
name|cache
operator|=
name|manager
operator|.
name|administration
argument_list|()
operator|.
name|getOrCreateCache
argument_list|(
literal|"remote_query"
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
expr_stmt|;
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|continuousQuery ()
specifier|public
name|void
name|continuousQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|continuousQueryBuilderNoMatch
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:continuousQueryNoMatch"
argument_list|)
decl_stmt|;
name|continuousQueryBuilderNoMatch
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|continuousQueryBuilderAll
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:continuousQueryAll"
argument_list|)
decl_stmt|;
name|continuousQueryBuilderAll
operator|.
name|expectedMessageCount
argument_list|(
name|CQ_USERS
operator|.
name|length
operator|*
literal|2
argument_list|)
expr_stmt|;
name|MockEndpoint
name|continuousQuery
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:continuousQuery"
argument_list|)
decl_stmt|;
name|continuousQuery
operator|.
name|expectedMessageCount
argument_list|(
literal|4
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|continuousQuery
operator|.
name|message
argument_list|(
name|i
argument_list|)
operator|.
name|header
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|createKey
argument_list|(
name|CQ_USERS
index|[
name|i
operator|%
literal|2
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|continuousQuery
operator|.
name|message
argument_list|(
name|i
argument_list|)
operator|.
name|header
argument_list|(
name|InfinispanConstants
operator|.
name|CACHE_NAME
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|cache
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|>=
literal|2
condition|)
block|{
name|continuousQuery
operator|.
name|message
argument_list|(
name|i
argument_list|)
operator|.
name|header
argument_list|(
name|InfinispanConstants
operator|.
name|EVENT_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|InfinispanConstants
operator|.
name|CACHE_ENTRY_LEAVING
argument_list|)
expr_stmt|;
name|continuousQuery
operator|.
name|message
argument_list|(
name|i
argument_list|)
operator|.
name|header
argument_list|(
name|InfinispanConstants
operator|.
name|EVENT_DATA
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|continuousQuery
operator|.
name|message
argument_list|(
name|i
argument_list|)
operator|.
name|header
argument_list|(
name|InfinispanConstants
operator|.
name|EVENT_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|InfinispanConstants
operator|.
name|CACHE_ENTRY_JOINING
argument_list|)
expr_stmt|;
name|continuousQuery
operator|.
name|message
argument_list|(
name|i
argument_list|)
operator|.
name|header
argument_list|(
name|InfinispanConstants
operator|.
name|EVENT_DATA
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|continuousQuery
operator|.
name|message
argument_list|(
name|i
argument_list|)
operator|.
name|header
argument_list|(
name|InfinispanConstants
operator|.
name|EVENT_DATA
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|User
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
specifier|final
name|User
name|user
range|:
name|CQ_USERS
control|)
block|{
name|cache
operator|.
name|put
argument_list|(
name|createKey
argument_list|(
name|user
argument_list|)
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|CQ_USERS
operator|.
name|length
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|User
name|user
range|:
name|CQ_USERS
control|)
block|{
name|cache
operator|.
name|remove
argument_list|(
name|createKey
argument_list|(
name|user
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|cache
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|continuousQuery
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|continuousQueryBuilderNoMatch
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|continuousQueryBuilderAll
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"infinispan:remote_query?cacheContainer=#myCustomContainer&queryBuilder=#continuousQueryBuilder"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:continuousQuery"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"infinispan:remote_query?cacheContainer=#myCustomContainer&queryBuilder=#continuousQueryBuilderNoMatch"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:continuousQueryNoMatch"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"infinispan:remote_query?cacheContainer=#myCustomContainer&queryBuilder=#continuousQueryBuilderAll"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:continuousQueryAll"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

