begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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
DECL|class|CassandraEndpoint
specifier|public
class|class
name|CassandraEndpoint
extends|extends
name|DefaultEndpoint
block|{
comment|/**      * Session holder      */
DECL|field|sessionHolder
specifier|private
name|CassandraSessionHolder
name|sessionHolder
decl_stmt|;
comment|/**      * CQL query      */
DECL|field|cql
specifier|private
name|String
name|cql
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
comment|/**      * Cassandra URI      *      * @param uri      * @param component Parent component      * @param cluster Cluster (required)      * @param session Session (optional)      * @param keyspace Keyspace (optional)      */
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
if|if
condition|(
name|session
operator|==
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
name|sessionHolder
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
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
DECL|method|getSession ()
specifier|public
name|Session
name|getSession
parameter_list|()
block|{
return|return
name|sessionHolder
operator|.
name|getSession
argument_list|()
return|;
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
DECL|method|getKeyspace ()
specifier|public
name|String
name|getKeyspace
parameter_list|()
block|{
return|return
name|sessionHolder
operator|.
name|getKeyspace
argument_list|()
return|;
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
DECL|method|setResultSetConversionStrategy (String converter)
specifier|public
name|void
name|setResultSetConversionStrategy
parameter_list|(
name|String
name|converter
parameter_list|)
block|{
name|this
operator|.
name|resultSetConversionStrategy
operator|=
name|ResultSetConversionStrategies
operator|.
name|fromName
argument_list|(
name|converter
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

