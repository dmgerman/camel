begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan.remote
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
operator|.
name|remote
package|;
end_package

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
name|component
operator|.
name|infinispan
operator|.
name|InfinispanConstants
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
name|infinispan
operator|.
name|InfinispanQueryBuilder
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
name|Search
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
name|api
operator|.
name|BasicCache
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

begin_class
DECL|class|InfinispanRemoteOperation
specifier|public
specifier|final
class|class
name|InfinispanRemoteOperation
block|{
DECL|method|InfinispanRemoteOperation ()
specifier|private
name|InfinispanRemoteOperation
parameter_list|()
block|{     }
DECL|method|buildQuery (BasicCache<Object, Object> cache, Exchange exchange)
specifier|public
specifier|static
name|Query
name|buildQuery
parameter_list|(
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|InfinispanQueryBuilder
name|queryBuilder
init|=
operator|(
name|InfinispanQueryBuilder
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|InfinispanConstants
operator|.
name|QUERY_BUILDER
argument_list|)
decl_stmt|;
if|if
condition|(
name|queryBuilder
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RemoteCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|remoteCache
init|=
operator|(
name|RemoteCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
operator|)
name|cache
decl_stmt|;
return|return
name|queryBuilder
operator|.
name|build
argument_list|(
name|Search
operator|.
name|getQueryFactory
argument_list|(
name|remoteCache
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

