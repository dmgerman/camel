begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pgevent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pgevent
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|com
operator|.
name|impossibl
operator|.
name|postgres
operator|.
name|api
operator|.
name|jdbc
operator|.
name|PGConnection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|impossibl
operator|.
name|postgres
operator|.
name|api
operator|.
name|jdbc
operator|.
name|PGNotificationListener
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
name|impl
operator|.
name|DefaultConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The PgEvent consumer.  */
end_comment

begin_class
DECL|class|PgEventConsumer
specifier|public
class|class
name|PgEventConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|PGNotificationListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PgEventConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|PgEventEndpoint
name|endpoint
decl_stmt|;
DECL|field|dbConnection
specifier|private
name|PGConnection
name|dbConnection
decl_stmt|;
DECL|method|PgEventConsumer (PgEventEndpoint endpoint, Processor processor)
specifier|public
name|PgEventConsumer
parameter_list|(
name|PgEventEndpoint
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
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
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
name|dbConnection
operator|=
name|endpoint
operator|.
name|initJdbc
argument_list|()
expr_stmt|;
name|String
name|sql
init|=
name|String
operator|.
name|format
argument_list|(
literal|"LISTEN %s"
argument_list|,
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|)
decl_stmt|;
try|try
init|(
name|PreparedStatement
name|statement
init|=
name|dbConnection
operator|.
name|prepareStatement
argument_list|(
name|sql
argument_list|)
init|)
block|{
name|statement
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|dbConnection
operator|.
name|addNotificationListener
argument_list|(
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|notification (int processId, String channel, String payload)
specifier|public
name|void
name|notification
parameter_list|(
name|int
name|processId
parameter_list|,
name|String
name|channel
parameter_list|,
name|String
name|payload
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Notification processId: {}, channel: {}, payload: {}"
argument_list|,
name|processId
argument_list|,
name|channel
argument_list|,
name|payload
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"channel"
argument_list|,
name|channel
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|payload
argument_list|)
expr_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|String
name|cause
init|=
literal|"Unable to process incoming notification from PostgreSQL: processId='"
operator|+
name|processId
operator|+
literal|"', channel='"
operator|+
name|channel
operator|+
literal|"', payload='"
operator|+
name|payload
operator|+
literal|"'"
decl_stmt|;
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|cause
argument_list|,
name|ex
argument_list|)
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
if|if
condition|(
name|dbConnection
operator|!=
literal|null
condition|)
block|{
name|dbConnection
operator|.
name|removeNotificationListener
argument_list|(
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|String
operator|.
name|format
argument_list|(
literal|"UNLISTEN %s"
argument_list|,
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|)
decl_stmt|;
try|try
init|(
name|PreparedStatement
name|statement
init|=
name|dbConnection
operator|.
name|prepareStatement
argument_list|(
name|sql
argument_list|)
init|)
block|{
name|statement
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|dbConnection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

