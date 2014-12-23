begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.utils.cassandra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|utils
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
comment|/**  * Holds a Cassandra Session and manages its lifecycle  */
end_comment

begin_class
DECL|class|CassandraSessionHolder
specifier|public
class|class
name|CassandraSessionHolder
block|{
comment|/**      * Cluster      */
DECL|field|cluster
specifier|private
specifier|final
name|Cluster
name|cluster
decl_stmt|;
comment|/**      * Session      */
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
comment|/**      * Keyspace name      */
DECL|field|keyspace
specifier|private
name|String
name|keyspace
decl_stmt|;
comment|/**      * Indicates whether Session is externally managed      */
DECL|field|managedSession
specifier|private
specifier|final
name|boolean
name|managedSession
decl_stmt|;
DECL|method|CassandraSessionHolder (Cluster cluster, String keyspace)
specifier|public
name|CassandraSessionHolder
parameter_list|(
name|Cluster
name|cluster
parameter_list|,
name|String
name|keyspace
parameter_list|)
block|{
name|this
operator|.
name|cluster
operator|=
name|cluster
expr_stmt|;
name|this
operator|.
name|keyspace
operator|=
name|keyspace
expr_stmt|;
name|this
operator|.
name|managedSession
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|CassandraSessionHolder (Session session)
specifier|public
name|CassandraSessionHolder
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|this
operator|.
name|cluster
operator|=
name|session
operator|.
name|getCluster
argument_list|()
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|keyspace
operator|=
name|session
operator|.
name|getLoggedKeyspace
argument_list|()
expr_stmt|;
name|this
operator|.
name|managedSession
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|managedSession
condition|)
block|{
if|if
condition|(
name|keyspace
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|session
operator|=
name|cluster
operator|.
name|connect
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|session
operator|=
name|cluster
operator|.
name|connect
argument_list|(
name|keyspace
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
if|if
condition|(
operator|!
name|managedSession
condition|)
block|{
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
name|session
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getSession ()
specifier|public
name|Session
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
DECL|method|getCluster ()
specifier|public
name|Cluster
name|getCluster
parameter_list|()
block|{
return|return
name|cluster
return|;
block|}
DECL|method|getKeyspace ()
specifier|public
name|String
name|getKeyspace
parameter_list|()
block|{
return|return
name|keyspace
return|;
block|}
block|}
end_class

end_unit

