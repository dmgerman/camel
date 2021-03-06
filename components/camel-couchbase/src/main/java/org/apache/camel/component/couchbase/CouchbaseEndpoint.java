begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.couchbase
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|com
operator|.
name|couchbase
operator|.
name|client
operator|.
name|CouchbaseClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|couchbase
operator|.
name|client
operator|.
name|CouchbaseConnectionFactoryBuilder
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
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ScheduledPollEndpoint
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|COUCHBASE_PUT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|COUCHBASE_URI_ERROR
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|DEFAULT_CONSUME_PROCESSED_STRATEGY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|DEFAULT_COUCHBASE_PORT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|DEFAULT_DESIGN_DOCUMENT_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|DEFAULT_MAX_RECONNECT_DELAY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|DEFAULT_OBS_POLL_INTERVAL
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|DEFAULT_OBS_TIMEOUT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|DEFAULT_OP_QUEUE_MAX_BLOCK_TIME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|DEFAULT_OP_TIMEOUT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|DEFAULT_PAUSE_BETWEEN_RETRIES
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|DEFAULT_PRODUCER_RETRIES
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|DEFAULT_READ_BUFFER_SIZE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|DEFAULT_TIMEOUT_EXCEPTION_THRESHOLD
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
operator|.
name|CouchbaseConstants
operator|.
name|DEFAULT_VIEWNAME
import|;
end_import

begin_comment
comment|/**  * Represents a Couchbase endpoint that can query Views with a Poll strategy  * and/or produce various type of operations.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.19.0"
argument_list|,
name|scheme
operator|=
literal|"couchbase"
argument_list|,
name|title
operator|=
literal|"Couchbase"
argument_list|,
name|syntax
operator|=
literal|"couchbase:protocol:hostname:port"
argument_list|,
name|label
operator|=
literal|"database,nosql"
argument_list|)
DECL|class|CouchbaseEndpoint
specifier|public
class|class
name|CouchbaseEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
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
literal|true
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
literal|"8091"
argument_list|)
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|UriParam
DECL|field|bucket
specifier|private
name|String
name|bucket
decl_stmt|;
comment|// Couchbase key
annotation|@
name|UriParam
DECL|field|key
specifier|private
name|String
name|key
decl_stmt|;
comment|// Authentication
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
DECL|field|username
specifier|private
name|String
name|username
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
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|// Additional hosts
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|additionalHosts
specifier|private
name|String
name|additionalHosts
decl_stmt|;
comment|// Persistence and replication parameters
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"0"
argument_list|)
DECL|field|persistTo
specifier|private
name|int
name|persistTo
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"0"
argument_list|)
DECL|field|replicateTo
specifier|private
name|int
name|replicateTo
decl_stmt|;
comment|// Producer parameters
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
name|COUCHBASE_PUT
argument_list|)
DECL|field|operation
specifier|private
name|String
name|operation
init|=
name|COUCHBASE_PUT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|autoStartIdForInserts
specifier|private
name|boolean
name|autoStartIdForInserts
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"2"
argument_list|)
DECL|field|producerRetryAttempts
specifier|private
name|int
name|producerRetryAttempts
init|=
name|DEFAULT_PRODUCER_RETRIES
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"5000"
argument_list|)
DECL|field|producerRetryPause
specifier|private
name|int
name|producerRetryPause
init|=
name|DEFAULT_PAUSE_BETWEEN_RETRIES
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|startingIdForInsertsFrom
specifier|private
name|long
name|startingIdForInsertsFrom
decl_stmt|;
comment|// View control
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
name|DEFAULT_DESIGN_DOCUMENT_NAME
argument_list|)
DECL|field|designDocumentName
specifier|private
name|String
name|designDocumentName
init|=
name|DEFAULT_DESIGN_DOCUMENT_NAME
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
name|DEFAULT_VIEWNAME
argument_list|)
DECL|field|viewName
specifier|private
name|String
name|viewName
init|=
name|DEFAULT_VIEWNAME
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|limit
specifier|private
name|int
name|limit
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|descending
specifier|private
name|boolean
name|descending
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|skip
specifier|private
name|int
name|skip
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|rangeStartKey
specifier|private
name|String
name|rangeStartKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|rangeEndKey
specifier|private
name|String
name|rangeEndKey
init|=
literal|""
decl_stmt|;
comment|// Consumer strategy
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
name|DEFAULT_CONSUME_PROCESSED_STRATEGY
argument_list|)
DECL|field|consumerProcessedStrategy
specifier|private
name|String
name|consumerProcessedStrategy
init|=
name|DEFAULT_CONSUME_PROCESSED_STRATEGY
decl_stmt|;
comment|// Connection fine tuning parameters
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"2500"
argument_list|)
DECL|field|opTimeOut
specifier|private
name|long
name|opTimeOut
init|=
name|DEFAULT_OP_TIMEOUT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"998"
argument_list|)
DECL|field|timeoutExceptionThreshold
specifier|private
name|int
name|timeoutExceptionThreshold
init|=
name|DEFAULT_TIMEOUT_EXCEPTION_THRESHOLD
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"16384"
argument_list|)
DECL|field|readBufferSize
specifier|private
name|int
name|readBufferSize
init|=
name|DEFAULT_READ_BUFFER_SIZE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|shouldOptimize
specifier|private
name|boolean
name|shouldOptimize
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"30000"
argument_list|)
DECL|field|maxReconnectDelay
specifier|private
name|long
name|maxReconnectDelay
init|=
name|DEFAULT_MAX_RECONNECT_DELAY
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"10000"
argument_list|)
DECL|field|opQueueMaxBlockTime
specifier|private
name|long
name|opQueueMaxBlockTime
init|=
name|DEFAULT_OP_QUEUE_MAX_BLOCK_TIME
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"400"
argument_list|)
DECL|field|obsPollInterval
specifier|private
name|long
name|obsPollInterval
init|=
name|DEFAULT_OBS_POLL_INTERVAL
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|obsTimeout
specifier|private
name|long
name|obsTimeout
init|=
name|DEFAULT_OBS_TIMEOUT
decl_stmt|;
DECL|method|CouchbaseEndpoint ()
specifier|public
name|CouchbaseEndpoint
parameter_list|()
block|{     }
DECL|method|CouchbaseEndpoint (String uri, String remaining, CouchbaseComponent component)
specifier|public
name|CouchbaseEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|CouchbaseComponent
name|component
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|URI
name|remainingUri
init|=
operator|new
name|URI
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|protocol
operator|=
name|remainingUri
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
name|COUCHBASE_URI_ERROR
argument_list|)
throw|;
block|}
name|port
operator|=
name|remainingUri
operator|.
name|getPort
argument_list|()
operator|==
operator|-
literal|1
condition|?
name|DEFAULT_COUCHBASE_PORT
else|:
name|remainingUri
operator|.
name|getPort
argument_list|()
expr_stmt|;
if|if
condition|(
name|remainingUri
operator|.
name|getPath
argument_list|()
operator|==
literal|null
operator|||
name|remainingUri
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
name|COUCHBASE_URI_ERROR
argument_list|)
throw|;
block|}
name|bucket
operator|=
name|remainingUri
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
name|remainingUri
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
name|COUCHBASE_URI_ERROR
argument_list|)
throw|;
block|}
block|}
DECL|method|CouchbaseEndpoint (String endpointUri, CouchbaseComponent component)
specifier|public
name|CouchbaseEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CouchbaseComponent
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
name|CouchbaseProducer
argument_list|(
name|this
argument_list|,
name|createClient
argument_list|()
argument_list|,
name|persistTo
argument_list|,
name|replicateTo
argument_list|)
return|;
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
return|return
operator|new
name|CouchbaseConsumer
argument_list|(
name|this
argument_list|,
name|createClient
argument_list|()
argument_list|,
name|processor
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
comment|/**      * The protocol to use      */
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
DECL|method|getBucket ()
specifier|public
name|String
name|getBucket
parameter_list|()
block|{
return|return
name|bucket
return|;
block|}
comment|/**      * The bucket to use      */
DECL|method|setBucket (String bucket)
specifier|public
name|void
name|setBucket
parameter_list|(
name|String
name|bucket
parameter_list|)
block|{
name|this
operator|.
name|bucket
operator|=
name|bucket
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
comment|/**      * The hostname to use      */
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
comment|/**      * The port number to use      */
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
DECL|method|getKey ()
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
comment|/**      * The key to use      */
DECL|method|setKey (String key)
specifier|public
name|void
name|setKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
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
comment|/**      * The username to use      */
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
comment|/**      * The password to use      */
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
DECL|method|getAdditionalHosts ()
specifier|public
name|String
name|getAdditionalHosts
parameter_list|()
block|{
return|return
name|additionalHosts
return|;
block|}
comment|/**      * The additional hosts      */
DECL|method|setAdditionalHosts (String additionalHosts)
specifier|public
name|void
name|setAdditionalHosts
parameter_list|(
name|String
name|additionalHosts
parameter_list|)
block|{
name|this
operator|.
name|additionalHosts
operator|=
name|additionalHosts
expr_stmt|;
block|}
DECL|method|getPersistTo ()
specifier|public
name|int
name|getPersistTo
parameter_list|()
block|{
return|return
name|persistTo
return|;
block|}
comment|/**      * Where to persist the data      */
DECL|method|setPersistTo (int persistTo)
specifier|public
name|void
name|setPersistTo
parameter_list|(
name|int
name|persistTo
parameter_list|)
block|{
name|this
operator|.
name|persistTo
operator|=
name|persistTo
expr_stmt|;
block|}
DECL|method|getReplicateTo ()
specifier|public
name|int
name|getReplicateTo
parameter_list|()
block|{
return|return
name|replicateTo
return|;
block|}
comment|/**      * Where to replicate the data      */
DECL|method|setReplicateTo (int replicateTo)
specifier|public
name|void
name|setReplicateTo
parameter_list|(
name|int
name|replicateTo
parameter_list|)
block|{
name|this
operator|.
name|replicateTo
operator|=
name|replicateTo
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * The operation to do      */
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|isAutoStartIdForInserts ()
specifier|public
name|boolean
name|isAutoStartIdForInserts
parameter_list|()
block|{
return|return
name|autoStartIdForInserts
return|;
block|}
comment|/**      * Define if we want an autostart Id when we are doing an insert operation      */
DECL|method|setAutoStartIdForInserts (boolean autoStartIdForInserts)
specifier|public
name|void
name|setAutoStartIdForInserts
parameter_list|(
name|boolean
name|autoStartIdForInserts
parameter_list|)
block|{
name|this
operator|.
name|autoStartIdForInserts
operator|=
name|autoStartIdForInserts
expr_stmt|;
block|}
DECL|method|getStartingIdForInsertsFrom ()
specifier|public
name|long
name|getStartingIdForInsertsFrom
parameter_list|()
block|{
return|return
name|startingIdForInsertsFrom
return|;
block|}
comment|/**      * Define the starting Id where we are doing an insert operation      */
DECL|method|setStartingIdForInsertsFrom (long startingIdForInsertsFrom)
specifier|public
name|void
name|setStartingIdForInsertsFrom
parameter_list|(
name|long
name|startingIdForInsertsFrom
parameter_list|)
block|{
name|this
operator|.
name|startingIdForInsertsFrom
operator|=
name|startingIdForInsertsFrom
expr_stmt|;
block|}
DECL|method|getProducerRetryAttempts ()
specifier|public
name|int
name|getProducerRetryAttempts
parameter_list|()
block|{
return|return
name|producerRetryAttempts
return|;
block|}
comment|/**      * Define the number of retry attempts      */
DECL|method|setProducerRetryAttempts (int producerRetryAttempts)
specifier|public
name|void
name|setProducerRetryAttempts
parameter_list|(
name|int
name|producerRetryAttempts
parameter_list|)
block|{
name|this
operator|.
name|producerRetryAttempts
operator|=
name|producerRetryAttempts
expr_stmt|;
block|}
DECL|method|getProducerRetryPause ()
specifier|public
name|int
name|getProducerRetryPause
parameter_list|()
block|{
return|return
name|producerRetryPause
return|;
block|}
comment|/**      * Define the retry pause between different attempts      */
DECL|method|setProducerRetryPause (int producerRetryPause)
specifier|public
name|void
name|setProducerRetryPause
parameter_list|(
name|int
name|producerRetryPause
parameter_list|)
block|{
name|this
operator|.
name|producerRetryPause
operator|=
name|producerRetryPause
expr_stmt|;
block|}
DECL|method|getDesignDocumentName ()
specifier|public
name|String
name|getDesignDocumentName
parameter_list|()
block|{
return|return
name|designDocumentName
return|;
block|}
comment|/**      * The design document name to use      */
DECL|method|setDesignDocumentName (String designDocumentName)
specifier|public
name|void
name|setDesignDocumentName
parameter_list|(
name|String
name|designDocumentName
parameter_list|)
block|{
name|this
operator|.
name|designDocumentName
operator|=
name|designDocumentName
expr_stmt|;
block|}
DECL|method|getViewName ()
specifier|public
name|String
name|getViewName
parameter_list|()
block|{
return|return
name|viewName
return|;
block|}
comment|/**      * The view name to use      */
DECL|method|setViewName (String viewName)
specifier|public
name|void
name|setViewName
parameter_list|(
name|String
name|viewName
parameter_list|)
block|{
name|this
operator|.
name|viewName
operator|=
name|viewName
expr_stmt|;
block|}
DECL|method|getLimit ()
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
comment|/**      * The output limit to use      */
DECL|method|setLimit (int limit)
specifier|public
name|void
name|setLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
block|}
DECL|method|isDescending ()
specifier|public
name|boolean
name|isDescending
parameter_list|()
block|{
return|return
name|descending
return|;
block|}
comment|/**      * Define if this operation is descending or not      */
DECL|method|setDescending (boolean descending)
specifier|public
name|void
name|setDescending
parameter_list|(
name|boolean
name|descending
parameter_list|)
block|{
name|this
operator|.
name|descending
operator|=
name|descending
expr_stmt|;
block|}
DECL|method|getSkip ()
specifier|public
name|int
name|getSkip
parameter_list|()
block|{
return|return
name|skip
return|;
block|}
comment|/**      * Define the skip to use      */
DECL|method|setSkip (int skip)
specifier|public
name|void
name|setSkip
parameter_list|(
name|int
name|skip
parameter_list|)
block|{
name|this
operator|.
name|skip
operator|=
name|skip
expr_stmt|;
block|}
DECL|method|getRangeStartKey ()
specifier|public
name|String
name|getRangeStartKey
parameter_list|()
block|{
return|return
name|rangeStartKey
return|;
block|}
comment|/**      * Define a range for the start key      */
DECL|method|setRangeStartKey (String rangeStartKey)
specifier|public
name|void
name|setRangeStartKey
parameter_list|(
name|String
name|rangeStartKey
parameter_list|)
block|{
name|this
operator|.
name|rangeStartKey
operator|=
name|rangeStartKey
expr_stmt|;
block|}
DECL|method|getRangeEndKey ()
specifier|public
name|String
name|getRangeEndKey
parameter_list|()
block|{
return|return
name|rangeEndKey
return|;
block|}
comment|/**      * Define a range for the end key      */
DECL|method|setRangeEndKey (String rangeEndKey)
specifier|public
name|void
name|setRangeEndKey
parameter_list|(
name|String
name|rangeEndKey
parameter_list|)
block|{
name|this
operator|.
name|rangeEndKey
operator|=
name|rangeEndKey
expr_stmt|;
block|}
DECL|method|getConsumerProcessedStrategy ()
specifier|public
name|String
name|getConsumerProcessedStrategy
parameter_list|()
block|{
return|return
name|consumerProcessedStrategy
return|;
block|}
comment|/**      * Define the consumer Processed strategy to use      */
DECL|method|setConsumerProcessedStrategy (String consumerProcessedStrategy)
specifier|public
name|void
name|setConsumerProcessedStrategy
parameter_list|(
name|String
name|consumerProcessedStrategy
parameter_list|)
block|{
name|this
operator|.
name|consumerProcessedStrategy
operator|=
name|consumerProcessedStrategy
expr_stmt|;
block|}
DECL|method|getOpTimeOut ()
specifier|public
name|long
name|getOpTimeOut
parameter_list|()
block|{
return|return
name|opTimeOut
return|;
block|}
comment|/**      * Define the operation timeout      */
DECL|method|setOpTimeOut (long opTimeOut)
specifier|public
name|void
name|setOpTimeOut
parameter_list|(
name|long
name|opTimeOut
parameter_list|)
block|{
name|this
operator|.
name|opTimeOut
operator|=
name|opTimeOut
expr_stmt|;
block|}
DECL|method|getTimeoutExceptionThreshold ()
specifier|public
name|int
name|getTimeoutExceptionThreshold
parameter_list|()
block|{
return|return
name|timeoutExceptionThreshold
return|;
block|}
comment|/**      * Define the threshold for throwing a timeout Exception      */
DECL|method|setTimeoutExceptionThreshold (int timeoutExceptionThreshold)
specifier|public
name|void
name|setTimeoutExceptionThreshold
parameter_list|(
name|int
name|timeoutExceptionThreshold
parameter_list|)
block|{
name|this
operator|.
name|timeoutExceptionThreshold
operator|=
name|timeoutExceptionThreshold
expr_stmt|;
block|}
DECL|method|getReadBufferSize ()
specifier|public
name|int
name|getReadBufferSize
parameter_list|()
block|{
return|return
name|readBufferSize
return|;
block|}
comment|/**      * Define the buffer size      */
DECL|method|setReadBufferSize (int readBufferSize)
specifier|public
name|void
name|setReadBufferSize
parameter_list|(
name|int
name|readBufferSize
parameter_list|)
block|{
name|this
operator|.
name|readBufferSize
operator|=
name|readBufferSize
expr_stmt|;
block|}
DECL|method|isShouldOptimize ()
specifier|public
name|boolean
name|isShouldOptimize
parameter_list|()
block|{
return|return
name|shouldOptimize
return|;
block|}
comment|/**      * Define if we want to use optimization or not where possible      */
DECL|method|setShouldOptimize (boolean shouldOptimize)
specifier|public
name|void
name|setShouldOptimize
parameter_list|(
name|boolean
name|shouldOptimize
parameter_list|)
block|{
name|this
operator|.
name|shouldOptimize
operator|=
name|shouldOptimize
expr_stmt|;
block|}
DECL|method|getMaxReconnectDelay ()
specifier|public
name|long
name|getMaxReconnectDelay
parameter_list|()
block|{
return|return
name|maxReconnectDelay
return|;
block|}
comment|/**      * Define the max delay during a reconnection      */
DECL|method|setMaxReconnectDelay (long maxReconnectDelay)
specifier|public
name|void
name|setMaxReconnectDelay
parameter_list|(
name|long
name|maxReconnectDelay
parameter_list|)
block|{
name|this
operator|.
name|maxReconnectDelay
operator|=
name|maxReconnectDelay
expr_stmt|;
block|}
DECL|method|getOpQueueMaxBlockTime ()
specifier|public
name|long
name|getOpQueueMaxBlockTime
parameter_list|()
block|{
return|return
name|opQueueMaxBlockTime
return|;
block|}
comment|/**      * Define the max time an operation can be in queue blocked      */
DECL|method|setOpQueueMaxBlockTime (long opQueueMaxBlockTime)
specifier|public
name|void
name|setOpQueueMaxBlockTime
parameter_list|(
name|long
name|opQueueMaxBlockTime
parameter_list|)
block|{
name|this
operator|.
name|opQueueMaxBlockTime
operator|=
name|opQueueMaxBlockTime
expr_stmt|;
block|}
DECL|method|getObsPollInterval ()
specifier|public
name|long
name|getObsPollInterval
parameter_list|()
block|{
return|return
name|obsPollInterval
return|;
block|}
comment|/**      * Define the observation polling interval      */
DECL|method|setObsPollInterval (long obsPollInterval)
specifier|public
name|void
name|setObsPollInterval
parameter_list|(
name|long
name|obsPollInterval
parameter_list|)
block|{
name|this
operator|.
name|obsPollInterval
operator|=
name|obsPollInterval
expr_stmt|;
block|}
DECL|method|getObsTimeout ()
specifier|public
name|long
name|getObsTimeout
parameter_list|()
block|{
return|return
name|obsTimeout
return|;
block|}
comment|/**      * Define the observation timeout      */
DECL|method|setObsTimeout (long obsTimeout)
specifier|public
name|void
name|setObsTimeout
parameter_list|(
name|long
name|obsTimeout
parameter_list|)
block|{
name|this
operator|.
name|obsTimeout
operator|=
name|obsTimeout
expr_stmt|;
block|}
DECL|method|makeBootstrapURI ()
specifier|public
name|URI
index|[]
name|makeBootstrapURI
parameter_list|()
throws|throws
name|URISyntaxException
block|{
if|if
condition|(
name|additionalHosts
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|additionalHosts
argument_list|)
condition|)
block|{
return|return
operator|new
name|URI
index|[]
block|{
operator|new
name|URI
argument_list|(
name|protocol
operator|+
literal|"://"
operator|+
name|hostname
operator|+
literal|":"
operator|+
name|port
operator|+
literal|"/pools"
argument_list|)
block|}
return|;
block|}
return|return
name|getAllUris
argument_list|()
return|;
block|}
DECL|method|getAllUris ()
specifier|private
name|URI
index|[]
name|getAllUris
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|String
index|[]
name|hosts
init|=
name|additionalHosts
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|hosts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|hosts
index|[
name|i
index|]
operator|=
name|hosts
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|hostList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|hostList
operator|.
name|add
argument_list|(
name|hostname
argument_list|)
expr_stmt|;
name|hostList
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|hosts
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|hostSet
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|(
name|hostList
argument_list|)
decl_stmt|;
name|hosts
operator|=
name|hostSet
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|hostSet
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
name|URI
index|[]
name|uriArray
init|=
operator|new
name|URI
index|[
name|hosts
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|hosts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|uriArray
index|[
name|i
index|]
operator|=
operator|new
name|URI
argument_list|(
name|protocol
operator|+
literal|"://"
operator|+
name|hosts
index|[
name|i
index|]
operator|+
literal|":"
operator|+
name|port
operator|+
literal|"/pools"
argument_list|)
expr_stmt|;
block|}
return|return
name|uriArray
return|;
block|}
DECL|method|createClient ()
specifier|private
name|CouchbaseClient
name|createClient
parameter_list|()
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
name|List
argument_list|<
name|URI
argument_list|>
name|hosts
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|makeBootstrapURI
argument_list|()
argument_list|)
decl_stmt|;
name|CouchbaseConnectionFactoryBuilder
name|cfb
init|=
operator|new
name|CouchbaseConnectionFactoryBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|opTimeOut
operator|!=
name|DEFAULT_OP_TIMEOUT
condition|)
block|{
name|cfb
operator|.
name|setOpTimeout
argument_list|(
name|opTimeOut
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|timeoutExceptionThreshold
operator|!=
name|DEFAULT_TIMEOUT_EXCEPTION_THRESHOLD
condition|)
block|{
name|cfb
operator|.
name|setTimeoutExceptionThreshold
argument_list|(
name|timeoutExceptionThreshold
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|readBufferSize
operator|!=
name|DEFAULT_READ_BUFFER_SIZE
condition|)
block|{
name|cfb
operator|.
name|setReadBufferSize
argument_list|(
name|readBufferSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|shouldOptimize
condition|)
block|{
name|cfb
operator|.
name|setShouldOptimize
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|maxReconnectDelay
operator|!=
name|DEFAULT_MAX_RECONNECT_DELAY
condition|)
block|{
name|cfb
operator|.
name|setMaxReconnectDelay
argument_list|(
name|maxReconnectDelay
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|opQueueMaxBlockTime
operator|!=
name|DEFAULT_OP_QUEUE_MAX_BLOCK_TIME
condition|)
block|{
name|cfb
operator|.
name|setOpQueueMaxBlockTime
argument_list|(
name|opQueueMaxBlockTime
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obsPollInterval
operator|!=
name|DEFAULT_OBS_POLL_INTERVAL
condition|)
block|{
name|cfb
operator|.
name|setObsPollInterval
argument_list|(
name|obsPollInterval
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obsTimeout
operator|!=
name|DEFAULT_OBS_TIMEOUT
condition|)
block|{
name|cfb
operator|.
name|setObsTimeout
argument_list|(
name|obsTimeout
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|CouchbaseClient
argument_list|(
name|cfb
operator|.
name|buildCouchbaseConnection
argument_list|(
name|hosts
argument_list|,
name|bucket
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

