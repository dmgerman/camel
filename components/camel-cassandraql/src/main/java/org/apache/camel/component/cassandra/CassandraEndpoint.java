begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cassandra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|ConsistencyLevel
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
name|PreparedStatement
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
name|ResultSet
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Component
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
name|Consumer
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
name|Message
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
name|Producer
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
name|impl
operator|.
name|DefaultEndpoint
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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
name|util
operator|.
name|CamelContextHelper
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
name|utils
operator|.
name|cassandra
operator|.
name|CassandraSessionHolder
import|;
end_import

begin_comment
comment|/**  * Cassandra 2 CQL3 endpoint  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"cql"
argument_list|,
name|consumerClass
operator|=
name|CassandraConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"database,nosql"
argument_list|)
DECL|class|CassandraEndpoint
specifier|public
class|class
name|CassandraEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|sessionHolder
specifier|private
specifier|volatile
name|CassandraSessionHolder
name|sessionHolder
decl_stmt|;
annotation|@
name|UriPath
DECL|field|beanRef
specifier|private
name|String
name|beanRef
decl_stmt|;
annotation|@
name|UriPath
DECL|field|hosts
specifier|private
name|String
name|hosts
decl_stmt|;
annotation|@
name|UriPath
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
annotation|@
name|UriPath
DECL|field|keyspace
specifier|private
name|String
name|keyspace
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cql
specifier|private
name|String
name|cql
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clusterName
specifier|private
name|String
name|clusterName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|UriParam
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cluster
specifier|private
name|Cluster
name|cluster
decl_stmt|;
annotation|@
name|UriParam
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
comment|/**      * Consistency level: ONE, TWO, QUORUM, LOCAL_QUORUM, ALL...      */
annotation|@
name|UriParam
DECL|field|consistencyLevel
specifier|private
name|ConsistencyLevel
name|consistencyLevel
decl_stmt|;
comment|/**      * How many rows should be retrieved in message body      */
annotation|@
name|UriParam
DECL|field|resultSetConversionStrategy
specifier|private
name|ResultSetConversionStrategy
name|resultSetConversionStrategy
init|=
name|ResultSetConversionStrategies
operator|.
name|all
argument_list|()
decl_stmt|;
DECL|method|CassandraEndpoint (String endpointUri, Component component)
specifier|public
name|CassandraEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|CassandraEndpoint (String uri, CassandraComponent component, Cluster cluster, Session session, String keyspace)
specifier|public
name|CassandraEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CassandraComponent
name|component
parameter_list|,
name|Cluster
name|cluster
parameter_list|,
name|Session
name|session
parameter_list|,
name|String
name|keyspace
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|cluster
operator|=
name|cluster
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
name|keyspace
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|CassandraProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|CassandraConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
comment|// we can get the cluster using various ways
if|if
condition|(
name|cluster
operator|==
literal|null
operator|&&
name|beanRef
operator|!=
literal|null
condition|)
block|{
name|Object
name|bean
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|beanRef
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|Session
condition|)
block|{
name|session
operator|=
operator|(
name|Session
operator|)
name|bean
expr_stmt|;
name|cluster
operator|=
name|session
operator|.
name|getCluster
argument_list|()
expr_stmt|;
name|keyspace
operator|=
name|session
operator|.
name|getLoggedKeyspace
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|bean
operator|instanceof
name|Cluster
condition|)
block|{
name|cluster
operator|=
operator|(
name|Cluster
operator|)
name|bean
expr_stmt|;
name|session
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CQL Bean type should be of type Session or Cluster but was "
operator|+
name|bean
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|cluster
operator|==
literal|null
operator|&&
name|hosts
operator|!=
literal|null
condition|)
block|{
comment|// use the cluster builder to create the cluster
name|cluster
operator|=
name|createClusterBuilder
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|cluster
operator|!=
literal|null
condition|)
block|{
name|sessionHolder
operator|=
operator|new
name|CassandraSessionHolder
argument_list|(
name|cluster
argument_list|,
name|keyspace
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sessionHolder
operator|=
operator|new
name|CassandraSessionHolder
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
name|sessionHolder
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|sessionHolder
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|getSessionHolder ()
specifier|protected
name|CassandraSessionHolder
name|getSessionHolder
parameter_list|()
block|{
return|return
name|sessionHolder
return|;
block|}
DECL|method|createClusterBuilder ()
specifier|protected
name|Cluster
operator|.
name|Builder
name|createClusterBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|Cluster
operator|.
name|Builder
name|clusterBuilder
init|=
name|Cluster
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|host
range|:
name|hosts
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|clusterBuilder
operator|=
name|clusterBuilder
operator|.
name|addContactPoint
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|port
operator|!=
literal|null
condition|)
block|{
name|clusterBuilder
operator|=
name|clusterBuilder
operator|.
name|withPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|clusterName
operator|!=
literal|null
condition|)
block|{
name|clusterBuilder
operator|=
name|clusterBuilder
operator|.
name|withClusterName
argument_list|(
name|clusterName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
operator|!
name|username
operator|.
name|isEmpty
argument_list|()
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
name|clusterBuilder
operator|.
name|withCredentials
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
return|return
name|clusterBuilder
return|;
block|}
comment|/**      * Create and configure a Prepared CQL statement      */
DECL|method|prepareStatement (String cql)
specifier|protected
name|PreparedStatement
name|prepareStatement
parameter_list|(
name|String
name|cql
parameter_list|)
block|{
name|PreparedStatement
name|preparedStatement
init|=
name|getSessionHolder
argument_list|()
operator|.
name|getSession
argument_list|()
operator|.
name|prepare
argument_list|(
name|cql
argument_list|)
decl_stmt|;
if|if
condition|(
name|consistencyLevel
operator|!=
literal|null
condition|)
block|{
name|preparedStatement
operator|.
name|setConsistencyLevel
argument_list|(
name|consistencyLevel
argument_list|)
expr_stmt|;
block|}
return|return
name|preparedStatement
return|;
block|}
comment|/**      * Create and configure a Prepared CQL statement      */
DECL|method|prepareStatement ()
specifier|protected
name|PreparedStatement
name|prepareStatement
parameter_list|()
block|{
return|return
name|prepareStatement
argument_list|(
name|cql
argument_list|)
return|;
block|}
comment|/**      * Copy ResultSet into Message.      */
DECL|method|fillMessage (ResultSet resultSet, Message message)
specifier|protected
name|void
name|fillMessage
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|message
operator|.
name|setBody
argument_list|(
name|resultSetConversionStrategy
operator|.
name|getBody
argument_list|(
name|resultSet
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getBeanRef ()
specifier|public
name|String
name|getBeanRef
parameter_list|()
block|{
return|return
name|beanRef
return|;
block|}
DECL|method|setBeanRef (String beanRef)
specifier|public
name|void
name|setBeanRef
parameter_list|(
name|String
name|beanRef
parameter_list|)
block|{
name|this
operator|.
name|beanRef
operator|=
name|beanRef
expr_stmt|;
block|}
DECL|method|getHosts ()
specifier|public
name|String
name|getHosts
parameter_list|()
block|{
return|return
name|hosts
return|;
block|}
DECL|method|setHosts (String hosts)
specifier|public
name|void
name|setHosts
parameter_list|(
name|String
name|hosts
parameter_list|)
block|{
name|this
operator|.
name|hosts
operator|=
name|hosts
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
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
DECL|method|setKeyspace (String keyspace)
specifier|public
name|void
name|setKeyspace
parameter_list|(
name|String
name|keyspace
parameter_list|)
block|{
name|this
operator|.
name|keyspace
operator|=
name|keyspace
expr_stmt|;
block|}
DECL|method|getCql ()
specifier|public
name|String
name|getCql
parameter_list|()
block|{
return|return
name|cql
return|;
block|}
DECL|method|setCql (String cql)
specifier|public
name|void
name|setCql
parameter_list|(
name|String
name|cql
parameter_list|)
block|{
name|this
operator|.
name|cql
operator|=
name|cql
expr_stmt|;
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
DECL|method|setCluster (Cluster cluster)
specifier|public
name|void
name|setCluster
parameter_list|(
name|Cluster
name|cluster
parameter_list|)
block|{
name|this
operator|.
name|cluster
operator|=
name|cluster
expr_stmt|;
block|}
DECL|method|getSession ()
specifier|public
name|Session
name|getSession
parameter_list|()
block|{
if|if
condition|(
name|session
operator|==
literal|null
condition|)
block|{
return|return
name|sessionHolder
operator|.
name|getSession
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|session
return|;
block|}
block|}
DECL|method|setSession (Session session)
specifier|public
name|void
name|setSession
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
DECL|method|getClusterName ()
specifier|public
name|String
name|getClusterName
parameter_list|()
block|{
return|return
name|clusterName
return|;
block|}
DECL|method|setClusterName (String clusterName)
specifier|public
name|void
name|setClusterName
parameter_list|(
name|String
name|clusterName
parameter_list|)
block|{
name|this
operator|.
name|clusterName
operator|=
name|clusterName
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getConsistencyLevel ()
specifier|public
name|ConsistencyLevel
name|getConsistencyLevel
parameter_list|()
block|{
return|return
name|consistencyLevel
return|;
block|}
DECL|method|setConsistencyLevel (ConsistencyLevel consistencyLevel)
specifier|public
name|void
name|setConsistencyLevel
parameter_list|(
name|ConsistencyLevel
name|consistencyLevel
parameter_list|)
block|{
name|this
operator|.
name|consistencyLevel
operator|=
name|consistencyLevel
expr_stmt|;
block|}
DECL|method|getResultSetConversionStrategy ()
specifier|public
name|ResultSetConversionStrategy
name|getResultSetConversionStrategy
parameter_list|()
block|{
return|return
name|resultSetConversionStrategy
return|;
block|}
DECL|method|setResultSetConversionStrategy (ResultSetConversionStrategy resultSetConversionStrategy)
specifier|public
name|void
name|setResultSetConversionStrategy
parameter_list|(
name|ResultSetConversionStrategy
name|resultSetConversionStrategy
parameter_list|)
block|{
name|this
operator|.
name|resultSetConversionStrategy
operator|=
name|resultSetConversionStrategy
expr_stmt|;
block|}
block|}
end_class

end_unit

