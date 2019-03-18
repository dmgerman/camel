begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|CallableStatement
import|;
end_import

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
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_comment
comment|/**  * The PgEvent producer.  */
end_comment

begin_class
DECL|class|PgEventProducer
specifier|public
class|class
name|PgEventProducer
extends|extends
name|DefaultProducer
block|{
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
DECL|method|PgEventProducer (PgEventEndpoint endpoint)
specifier|public
name|PgEventProducer
parameter_list|(
name|PgEventEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
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
DECL|method|process (Exchange exchange)
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
try|try
block|{
if|if
condition|(
name|dbConnection
operator|.
name|isClosed
argument_list|()
condition|)
block|{
name|dbConnection
operator|=
name|endpoint
operator|.
name|initJdbc
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidStateException
argument_list|(
literal|"Database connection closed and could not be re-opened."
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|String
name|payload
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|dbConnection
operator|.
name|isServerMinimumVersion
argument_list|(
literal|9
argument_list|,
literal|0
argument_list|)
condition|)
block|{
try|try
init|(
name|CallableStatement
name|statement
init|=
name|dbConnection
operator|.
name|prepareCall
argument_list|(
literal|"{call pg_notify(?, ?)}"
argument_list|)
init|)
block|{
name|statement
operator|.
name|setString
argument_list|(
literal|1
argument_list|,
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
name|statement
operator|.
name|setString
argument_list|(
literal|2
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|statement
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|sql
init|=
name|String
operator|.
name|format
argument_list|(
literal|"NOTIFY %s, '%s'"
argument_list|,
name|endpoint
operator|.
name|getChannel
argument_list|()
argument_list|,
name|payload
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
name|dbConnection
operator|=
name|endpoint
operator|.
name|initJdbc
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
if|if
condition|(
name|dbConnection
operator|!=
literal|null
condition|)
block|{
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

