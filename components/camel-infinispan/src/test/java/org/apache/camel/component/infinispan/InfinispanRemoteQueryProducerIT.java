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
name|java
operator|.
name|util
operator|.
name|List
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
name|Processor
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
name|MarshallerRegistration
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
name|InfinispanConstants
operator|.
name|OPERATION
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
name|InfinispanConstants
operator|.
name|QUERY_BUILDER
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
name|USERS
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
name|hasUser
import|;
end_import

begin_class
DECL|class|InfinispanRemoteQueryProducerIT
specifier|public
class|class
name|InfinispanRemoteQueryProducerIT
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"noResultQueryBuilder"
argument_list|)
DECL|field|NO_RESULT_QUERY_BUILDER
specifier|private
specifier|static
specifier|final
name|InfinispanQueryBuilder
name|NO_RESULT_QUERY_BUILDER
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
literal|"%abc%"
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
literal|"withResultQueryBuilder"
argument_list|)
DECL|field|WITH_RESULT_QUERY_BUILDER
specifier|private
specifier|static
specifier|final
name|InfinispanQueryBuilder
name|WITH_RESULT_QUERY_BUILDER
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
literal|"%A"
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"infinispan:remote_query?cacheContainer=#myCustomContainer"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:noQueryResults"
argument_list|)
operator|.
name|to
argument_list|(
literal|"infinispan:remote_query?cacheContainer=#myCustomContainer&queryBuilder=#noResultQueryBuilder"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:queryWithResults"
argument_list|)
operator|.
name|to
argument_list|(
literal|"infinispan:remote_query?cacheContainer=#myCustomContainer&queryBuilder=#withResultQueryBuilder"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
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
name|InfinispanRemoteQueryProducerIT
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
name|registerMarshallers
argument_list|(
name|ProtoStreamMarshaller
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
name|ProtoStreamMarshaller
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
block|}
annotation|@
name|Override
DECL|method|doPostSetup ()
specifier|protected
name|void
name|doPostSetup
parameter_list|()
throws|throws
name|Exception
block|{
comment|// pre-load data
name|RemoteCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|manager
operator|.
name|getCache
argument_list|(
literal|"remote_query"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|User
name|user
range|:
name|USERS
control|)
block|{
name|String
name|key
init|=
name|createKey
argument_list|(
name|user
argument_list|)
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|user
argument_list|)
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
block|}
annotation|@
name|Test
DECL|method|producerQueryOperationWithoutQueryBuilder ()
specifier|public
name|void
name|producerQueryOperationWithoutQueryBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|request
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|OPERATION
argument_list|,
name|InfinispanOperation
operator|.
name|QUERY
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|request
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|User
argument_list|>
name|queryResult
init|=
name|request
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|queryResult
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|producerQueryWithoutResult ()
specifier|public
name|void
name|producerQueryWithoutResult
parameter_list|()
throws|throws
name|Exception
block|{
name|producerQueryWithoutResult
argument_list|(
literal|"direct:start"
argument_list|,
name|NO_RESULT_QUERY_BUILDER
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|producerQueryWithoutResultAndQueryBuilderFromConfig ()
specifier|public
name|void
name|producerQueryWithoutResultAndQueryBuilderFromConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|producerQueryWithoutResult
argument_list|(
literal|"direct:noQueryResults"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|producerQueryWithoutResult (String endpoint, final InfinispanQueryBuilder builder)
specifier|private
name|void
name|producerQueryWithoutResult
parameter_list|(
name|String
name|endpoint
parameter_list|,
specifier|final
name|InfinispanQueryBuilder
name|builder
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|request
init|=
name|template
operator|.
name|request
argument_list|(
name|endpoint
argument_list|,
name|createQueryProcessor
argument_list|(
name|builder
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|request
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|User
argument_list|>
name|queryResult
init|=
name|request
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|queryResult
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queryResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|producerQueryWithResult ()
specifier|public
name|void
name|producerQueryWithResult
parameter_list|()
throws|throws
name|Exception
block|{
name|producerQueryWithResult
argument_list|(
literal|"direct:start"
argument_list|,
name|WITH_RESULT_QUERY_BUILDER
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|producerQueryWithResultAndQueryBuilderFromConfig ()
specifier|public
name|void
name|producerQueryWithResultAndQueryBuilderFromConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|producerQueryWithResult
argument_list|(
literal|"direct:queryWithResults"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|producerQueryWithResult (String endpoint, final InfinispanQueryBuilder builder)
specifier|private
name|void
name|producerQueryWithResult
parameter_list|(
name|String
name|endpoint
parameter_list|,
specifier|final
name|InfinispanQueryBuilder
name|builder
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|request
init|=
name|template
operator|.
name|request
argument_list|(
name|endpoint
argument_list|,
name|createQueryProcessor
argument_list|(
name|builder
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|request
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|User
argument_list|>
name|queryResult
init|=
name|request
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|queryResult
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queryResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasUser
argument_list|(
name|queryResult
argument_list|,
literal|"nameA"
argument_list|,
literal|"surnameA"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasUser
argument_list|(
name|queryResult
argument_list|,
literal|"nameA"
argument_list|,
literal|"surnameB"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createQueryProcessor (final InfinispanQueryBuilder builder)
specifier|private
name|Processor
name|createQueryProcessor
parameter_list|(
specifier|final
name|InfinispanQueryBuilder
name|builder
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|OPERATION
argument_list|,
name|InfinispanOperation
operator|.
name|QUERY
argument_list|)
expr_stmt|;
if|if
condition|(
name|builder
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|QUERY_BUILDER
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

