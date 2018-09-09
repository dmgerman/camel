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
name|DriverManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|jdbc
operator|.
name|PGDriver
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
name|ClassResolver
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
name|Metadata
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
comment|/**  * The pgevent component allows for producing/consuming PostgreSQL events related to the listen/notify commands.  *<p/>  * This requires using PostgreSQL 8.3 or newer.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.15.0"
argument_list|,
name|scheme
operator|=
literal|"pgevent"
argument_list|,
name|title
operator|=
literal|"PostgresSQL Event"
argument_list|,
name|syntax
operator|=
literal|"pgevent:host:port/database/channel"
argument_list|,
name|consumerClass
operator|=
name|PgEventConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"database,sql"
argument_list|)
DECL|class|PgEventEndpoint
specifier|public
class|class
name|PgEventEndpoint
extends|extends
name|DefaultEndpoint
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
name|PgEventEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|FORMAT1
specifier|private
specifier|static
specifier|final
name|String
name|FORMAT1
init|=
literal|"^pgevent://([^:]*):(\\d+)/(\\w+)/(\\w+).*$"
decl_stmt|;
DECL|field|FORMAT2
specifier|private
specifier|static
specifier|final
name|String
name|FORMAT2
init|=
literal|"^pgevent://([^:]+)/(\\w+)/(\\w+).*$"
decl_stmt|;
DECL|field|FORMAT3
specifier|private
specifier|static
specifier|final
name|String
name|FORMAT3
init|=
literal|"^pgevent:///(\\w+)/(\\w+).*$"
decl_stmt|;
DECL|field|FORMAT4
specifier|private
specifier|static
specifier|final
name|String
name|FORMAT4
init|=
literal|"^pgevent:(\\w+)/(\\w+)/(\\w+).*$"
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|defaultValue
operator|=
literal|"localhost"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
init|=
literal|"localhost"
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|defaultValue
operator|=
literal|"5432"
argument_list|)
DECL|field|port
specifier|private
name|Integer
name|port
init|=
literal|5432
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|database
specifier|private
name|String
name|database
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|channel
specifier|private
name|String
name|channel
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"postgres"
argument_list|,
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|user
specifier|private
name|String
name|user
init|=
literal|"postgres"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|pass
specifier|private
name|String
name|pass
decl_stmt|;
annotation|@
name|UriParam
DECL|field|datasource
specifier|private
name|DataSource
name|datasource
decl_stmt|;
DECL|field|uri
specifier|private
specifier|final
name|String
name|uri
decl_stmt|;
DECL|field|dbConnection
specifier|private
name|PGConnection
name|dbConnection
decl_stmt|;
DECL|method|PgEventEndpoint (String uri, PgEventComponent component)
specifier|public
name|PgEventEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|PgEventComponent
name|component
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
name|uri
operator|=
name|uri
expr_stmt|;
name|parseUri
argument_list|()
expr_stmt|;
block|}
DECL|method|PgEventEndpoint (String uri, PgEventComponent component, DataSource dataSource)
specifier|public
name|PgEventEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|PgEventComponent
name|component
parameter_list|,
name|DataSource
name|dataSource
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
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|datasource
operator|=
name|dataSource
expr_stmt|;
name|parseUri
argument_list|()
expr_stmt|;
block|}
DECL|method|initJdbc ()
specifier|public
specifier|final
name|PGConnection
name|initJdbc
parameter_list|()
throws|throws
name|Exception
block|{
name|PGConnection
name|conn
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|getDatasource
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|conn
operator|=
name|PgEventHelper
operator|.
name|toPGConnection
argument_list|(
name|this
operator|.
name|getDatasource
argument_list|()
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// ensure we can load the class
name|ClassResolver
name|classResolver
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
decl_stmt|;
name|classResolver
operator|.
name|resolveMandatoryClass
argument_list|(
name|PGDriver
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|PgEventComponent
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|conn
operator|=
operator|(
name|PGConnection
operator|)
name|DriverManager
operator|.
name|getConnection
argument_list|(
literal|"jdbc:pgsql://"
operator|+
name|this
operator|.
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|this
operator|.
name|getPort
argument_list|()
operator|+
literal|"/"
operator|+
name|this
operator|.
name|getDatabase
argument_list|()
argument_list|,
name|this
operator|.
name|getUser
argument_list|()
argument_list|,
name|this
operator|.
name|getPass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|conn
return|;
block|}
comment|/**      * Parse the provided URI and extract available parameters      *      * @throws IllegalArgumentException if there is an error in the parameters      */
DECL|method|parseUri ()
specifier|protected
specifier|final
name|void
name|parseUri
parameter_list|()
throws|throws
name|IllegalArgumentException
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"URI: {}"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
if|if
condition|(
name|uri
operator|.
name|matches
argument_list|(
name|FORMAT1
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"FORMAT1"
argument_list|)
expr_stmt|;
name|String
index|[]
name|parts
init|=
name|uri
operator|.
name|replaceFirst
argument_list|(
name|FORMAT1
argument_list|,
literal|"$1:$2:$3:$4"
argument_list|)
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|host
operator|=
name|parts
index|[
literal|0
index|]
expr_stmt|;
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|database
operator|=
name|parts
index|[
literal|2
index|]
expr_stmt|;
name|channel
operator|=
name|parts
index|[
literal|3
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|matches
argument_list|(
name|FORMAT2
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"FORMAT2"
argument_list|)
expr_stmt|;
name|String
index|[]
name|parts
init|=
name|uri
operator|.
name|replaceFirst
argument_list|(
name|FORMAT2
argument_list|,
literal|"$1:$2:$3"
argument_list|)
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|host
operator|=
name|parts
index|[
literal|0
index|]
expr_stmt|;
name|port
operator|=
literal|5432
expr_stmt|;
name|database
operator|=
name|parts
index|[
literal|1
index|]
expr_stmt|;
name|channel
operator|=
name|parts
index|[
literal|2
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|matches
argument_list|(
name|FORMAT3
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"FORMAT3"
argument_list|)
expr_stmt|;
name|String
index|[]
name|parts
init|=
name|uri
operator|.
name|replaceFirst
argument_list|(
name|FORMAT3
argument_list|,
literal|"$1:$2"
argument_list|)
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|host
operator|=
literal|"localhost"
expr_stmt|;
name|port
operator|=
literal|5432
expr_stmt|;
name|database
operator|=
name|parts
index|[
literal|0
index|]
expr_stmt|;
name|channel
operator|=
name|parts
index|[
literal|1
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|matches
argument_list|(
name|FORMAT4
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"FORMAT4"
argument_list|)
expr_stmt|;
name|String
index|[]
name|parts
init|=
name|uri
operator|.
name|replaceFirst
argument_list|(
name|FORMAT4
argument_list|,
literal|"$1:$2"
argument_list|)
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|database
operator|=
name|parts
index|[
literal|0
index|]
expr_stmt|;
name|channel
operator|=
name|parts
index|[
literal|1
index|]
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The provided URL does not match the acceptable patterns."
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|validateInputs
argument_list|()
expr_stmt|;
return|return
operator|new
name|PgEventProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|validateInputs ()
specifier|private
name|void
name|validateInputs
parameter_list|()
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|getChannel
argument_list|()
operator|==
literal|null
operator|||
name|getChannel
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A required parameter was not set when creating this Endpoint (channel)"
argument_list|)
throw|;
block|}
if|if
condition|(
name|datasource
operator|==
literal|null
operator|&&
name|user
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A required parameter was "
operator|+
literal|"not set when creating this Endpoint (pgUser or pgDataSource)"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
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
name|validateInputs
argument_list|()
expr_stmt|;
name|PgEventConsumer
name|consumer
init|=
operator|new
name|PgEventConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
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
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/**      * To connect using hostname and port to the database.      */
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
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
comment|/**      * To connect using hostname and port to the database.      */
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
DECL|method|getDatabase ()
specifier|public
name|String
name|getDatabase
parameter_list|()
block|{
return|return
name|database
return|;
block|}
comment|/**      * The database name      */
DECL|method|setDatabase (String database)
specifier|public
name|void
name|setDatabase
parameter_list|(
name|String
name|database
parameter_list|)
block|{
name|this
operator|.
name|database
operator|=
name|database
expr_stmt|;
block|}
DECL|method|getChannel ()
specifier|public
name|String
name|getChannel
parameter_list|()
block|{
return|return
name|channel
return|;
block|}
comment|/**      * The channel name      */
DECL|method|setChannel (String channel)
specifier|public
name|void
name|setChannel
parameter_list|(
name|String
name|channel
parameter_list|)
block|{
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
block|}
DECL|method|getUser ()
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
comment|/**      * Username for login      */
DECL|method|setUser (String user)
specifier|public
name|void
name|setUser
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
DECL|method|getPass ()
specifier|public
name|String
name|getPass
parameter_list|()
block|{
return|return
name|pass
return|;
block|}
comment|/**      * Password for login      */
DECL|method|setPass (String pass)
specifier|public
name|void
name|setPass
parameter_list|(
name|String
name|pass
parameter_list|)
block|{
name|this
operator|.
name|pass
operator|=
name|pass
expr_stmt|;
block|}
DECL|method|getDatasource ()
specifier|public
name|DataSource
name|getDatasource
parameter_list|()
block|{
return|return
name|datasource
return|;
block|}
comment|/**      * To connect using the given {@link javax.sql.DataSource} instead of using hostname and port.      */
DECL|method|setDatasource (DataSource datasource)
specifier|public
name|void
name|setDatasource
parameter_list|(
name|DataSource
name|datasource
parameter_list|)
block|{
name|this
operator|.
name|datasource
operator|=
name|datasource
expr_stmt|;
block|}
block|}
end_class

end_unit

