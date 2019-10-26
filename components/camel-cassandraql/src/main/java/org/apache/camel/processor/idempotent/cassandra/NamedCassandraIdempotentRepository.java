begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent.cassandra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
operator|.
name|cassandra
package|;
end_package

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|Cluster
import|;
end_import

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|driver
operator|.
name|core
operator|.
name|Session
import|;
end_import

begin_comment
comment|/**  * Concrete implementation of {@link  CassandraIdempotentRepository} using 2  * columns as primary key: name (partition key) and key (clustering key).  */
end_comment

begin_class
DECL|class|NamedCassandraIdempotentRepository
specifier|public
class|class
name|NamedCassandraIdempotentRepository
extends|extends
name|CassandraIdempotentRepository
block|{
DECL|method|NamedCassandraIdempotentRepository ()
specifier|public
name|NamedCassandraIdempotentRepository
parameter_list|()
block|{
name|setPKColumns
argument_list|(
literal|"NAME"
argument_list|,
literal|"KEY"
argument_list|)
expr_stmt|;
name|setName
argument_list|(
literal|"DEFAULT"
argument_list|)
expr_stmt|;
block|}
DECL|method|NamedCassandraIdempotentRepository (Session session, String name)
specifier|public
name|NamedCassandraIdempotentRepository
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|setPKColumns
argument_list|(
literal|"NAME"
argument_list|,
literal|"KEY"
argument_list|)
expr_stmt|;
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|NamedCassandraIdempotentRepository (Cluster cluster, String keyspace, String name)
specifier|public
name|NamedCassandraIdempotentRepository
parameter_list|(
name|Cluster
name|cluster
parameter_list|,
name|String
name|keyspace
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|cluster
argument_list|,
name|keyspace
argument_list|)
expr_stmt|;
name|setPKColumns
argument_list|(
literal|"NAME"
argument_list|,
literal|"KEY"
argument_list|)
expr_stmt|;
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getPrefixPKValues
argument_list|()
index|[
literal|0
index|]
return|;
block|}
DECL|method|setName (String name)
specifier|public
specifier|final
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setPrefixPKValues
argument_list|(
operator|new
name|String
index|[]
block|{
name|name
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

