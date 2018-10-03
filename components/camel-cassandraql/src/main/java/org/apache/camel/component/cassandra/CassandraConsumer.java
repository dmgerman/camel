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
name|support
operator|.
name|ScheduledPollConsumer
import|;
end_import

begin_comment
comment|/**  * Cassandra 2 CQL3 consumer.  */
end_comment

begin_class
DECL|class|CassandraConsumer
specifier|public
class|class
name|CassandraConsumer
extends|extends
name|ScheduledPollConsumer
block|{
comment|/**      * Prepared statement used for polling      */
DECL|field|preparedStatement
specifier|private
name|PreparedStatement
name|preparedStatement
decl_stmt|;
DECL|method|CassandraConsumer (CassandraEndpoint endpoint, Processor processor)
specifier|public
name|CassandraConsumer
parameter_list|(
name|CassandraEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|CassandraEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|CassandraEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Execute CQL Query
name|Session
name|session
init|=
name|getEndpoint
argument_list|()
operator|.
name|getSessionHolder
argument_list|()
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|ResultSet
name|resultSet
decl_stmt|;
if|if
condition|(
name|isPrepareStatements
argument_list|()
condition|)
block|{
name|resultSet
operator|=
name|session
operator|.
name|execute
argument_list|(
name|preparedStatement
operator|.
name|bind
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resultSet
operator|=
name|session
operator|.
name|execute
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCql
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Create message from ResultSet
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|fillMessage
argument_list|(
name|resultSet
argument_list|,
name|message
argument_list|)
expr_stmt|;
try|try
block|{
comment|// send message to next processor in the route
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
comment|// number of messages polled
block|}
finally|finally
block|{
comment|// log exception if an exception occurred and was not handled
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|isPrepareStatements
argument_list|()
condition|)
block|{
name|preparedStatement
operator|=
name|getEndpoint
argument_list|()
operator|.
name|prepareStatement
argument_list|()
expr_stmt|;
block|}
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
name|this
operator|.
name|preparedStatement
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|isPrepareStatements ()
specifier|public
name|boolean
name|isPrepareStatements
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|isPrepareStatements
argument_list|()
return|;
block|}
block|}
end_class

end_unit

