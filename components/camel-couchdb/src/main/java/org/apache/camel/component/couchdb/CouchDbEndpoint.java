begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.couchdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchdb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonObject
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
name|lightcouch
operator|.
name|CouchDbClient
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"couchdb"
argument_list|,
name|syntax
operator|=
literal|"couchdb:protocol:hostname:port/database"
argument_list|,
name|consumerClass
operator|=
name|CouchDbConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"database,nosql"
argument_list|)
DECL|class|CouchDbEndpoint
specifier|public
class|class
name|CouchDbEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|DEFAULT_STYLE
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_STYLE
init|=
literal|"main_only"
decl_stmt|;
DECL|field|DEFAULT_HEARTBEAT
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_HEARTBEAT
init|=
literal|30000
decl_stmt|;
DECL|field|DEFAULT_PORT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PORT
init|=
literal|5984
decl_stmt|;
DECL|field|URI_ERROR
specifier|private
specifier|static
specifier|final
name|String
name|URI_ERROR
init|=
literal|"Invalid URI. Format must be of the form couchdb:http[s]://hostname[:port]/database?[options...]"
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
DECL|field|protocol
specifier|private
name|String
name|protocol
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
DECL|field|hostname
specifier|private
name|String
name|hostname
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|DEFAULT_PORT
argument_list|)
DECL|field|port
specifier|private
name|int
name|port
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
name|UriParam
argument_list|(
name|defaultValue
operator|=
name|DEFAULT_STYLE
argument_list|)
DECL|field|style
specifier|private
name|String
name|style
init|=
name|DEFAULT_STYLE
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
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|DEFAULT_HEARTBEAT
argument_list|)
DECL|field|heartbeat
specifier|private
name|long
name|heartbeat
init|=
name|DEFAULT_HEARTBEAT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|createDatabase
specifier|private
name|boolean
name|createDatabase
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|deletes
specifier|private
name|boolean
name|deletes
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|updates
specifier|private
name|boolean
name|updates
init|=
literal|true
decl_stmt|;
DECL|method|CouchDbEndpoint ()
specifier|public
name|CouchDbEndpoint
parameter_list|()
block|{     }
DECL|method|CouchDbEndpoint (String endpointUri, String remaining, CouchDbComponent component)
specifier|public
name|CouchDbEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|CouchDbComponent
name|component
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|protocol
operator|=
name|uri
operator|.
name|getScheme
argument_list|()
expr_stmt|;
if|if
condition|(
name|protocol
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|URI_ERROR
argument_list|)
throw|;
block|}
name|port
operator|=
name|uri
operator|.
name|getPort
argument_list|()
operator|==
operator|-
literal|1
condition|?
name|DEFAULT_PORT
else|:
name|uri
operator|.
name|getPort
argument_list|()
expr_stmt|;
if|if
condition|(
name|uri
operator|.
name|getPath
argument_list|()
operator|==
literal|null
operator|||
name|uri
operator|.
name|getPath
argument_list|()
operator|.
name|trim
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
name|URI_ERROR
argument_list|)
throw|;
block|}
name|database
operator|=
name|uri
operator|.
name|getPath
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|hostname
operator|=
name|uri
operator|.
name|getHost
argument_list|()
expr_stmt|;
if|if
condition|(
name|hostname
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|URI_ERROR
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
name|CouchDbConsumer
name|answer
init|=
operator|new
name|CouchDbConsumer
argument_list|(
name|this
argument_list|,
name|createClient
argument_list|()
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
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
return|return
operator|new
name|CouchDbProducer
argument_list|(
name|this
argument_list|,
name|createClient
argument_list|()
argument_list|)
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
DECL|method|createExchange (String seq, String id, JsonObject obj, boolean deleted)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|String
name|seq
parameter_list|,
name|String
name|id
parameter_list|,
name|JsonObject
name|obj
parameter_list|,
name|boolean
name|deleted
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_DATABASE
argument_list|,
name|database
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_SEQ
argument_list|,
name|seq
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_DOC_ID
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_DOC_REV
argument_list|,
name|obj
operator|.
name|get
argument_list|(
literal|"_rev"
argument_list|)
operator|.
name|getAsString
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CouchDbConstants
operator|.
name|HEADER_METHOD
argument_list|,
name|deleted
condition|?
literal|"DELETE"
else|:
literal|"UPDATE"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|obj
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createClient ()
specifier|protected
name|CouchDbClientWrapper
name|createClient
parameter_list|()
block|{
return|return
operator|new
name|CouchDbClientWrapper
argument_list|(
operator|new
name|CouchDbClient
argument_list|(
name|database
argument_list|,
name|createDatabase
argument_list|,
name|protocol
argument_list|,
name|hostname
argument_list|,
name|port
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
DECL|method|setProtocol (String protocol)
specifier|public
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
block|}
DECL|method|getHostname ()
specifier|public
name|String
name|getHostname
parameter_list|()
block|{
return|return
name|hostname
return|;
block|}
DECL|method|setHostname (String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
block|}
DECL|method|getStyle ()
specifier|public
name|String
name|getStyle
parameter_list|()
block|{
return|return
name|style
return|;
block|}
DECL|method|setStyle (String style)
specifier|public
name|void
name|setStyle
parameter_list|(
name|String
name|style
parameter_list|)
block|{
name|this
operator|.
name|style
operator|=
name|style
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
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
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
DECL|method|getHeartbeat ()
specifier|public
name|long
name|getHeartbeat
parameter_list|()
block|{
return|return
name|heartbeat
return|;
block|}
DECL|method|setHeartbeat (long heartbeat)
specifier|public
name|void
name|setHeartbeat
parameter_list|(
name|long
name|heartbeat
parameter_list|)
block|{
name|this
operator|.
name|heartbeat
operator|=
name|heartbeat
expr_stmt|;
block|}
DECL|method|isCreateDatabase ()
specifier|public
name|boolean
name|isCreateDatabase
parameter_list|()
block|{
return|return
name|createDatabase
return|;
block|}
DECL|method|setCreateDatabase (boolean createDatabase)
specifier|public
name|void
name|setCreateDatabase
parameter_list|(
name|boolean
name|createDatabase
parameter_list|)
block|{
name|this
operator|.
name|createDatabase
operator|=
name|createDatabase
expr_stmt|;
block|}
DECL|method|isDeletes ()
specifier|public
name|boolean
name|isDeletes
parameter_list|()
block|{
return|return
name|deletes
return|;
block|}
DECL|method|setDeletes (boolean deletes)
specifier|public
name|void
name|setDeletes
parameter_list|(
name|boolean
name|deletes
parameter_list|)
block|{
name|this
operator|.
name|deletes
operator|=
name|deletes
expr_stmt|;
block|}
DECL|method|isUpdates ()
specifier|public
name|boolean
name|isUpdates
parameter_list|()
block|{
return|return
name|updates
return|;
block|}
DECL|method|setUpdates (boolean updates)
specifier|public
name|void
name|setUpdates
parameter_list|(
name|boolean
name|updates
parameter_list|)
block|{
name|this
operator|.
name|updates
operator|=
name|updates
expr_stmt|;
block|}
block|}
end_class

end_unit

