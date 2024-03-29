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
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
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
name|CouchbaseClientIF
import|;
end_import

begin_import
import|import
name|net
operator|.
name|spy
operator|.
name|memcached
operator|.
name|PersistTo
import|;
end_import

begin_import
import|import
name|net
operator|.
name|spy
operator|.
name|memcached
operator|.
name|ReplicateTo
import|;
end_import

begin_import
import|import
name|net
operator|.
name|spy
operator|.
name|memcached
operator|.
name|internal
operator|.
name|OperationFuture
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
name|COUCHBASE_DELETE
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
name|COUCHBASE_GET
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
name|DEFAULT_TTL
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
name|HEADER_ID
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
name|HEADER_TTL
import|;
end_import

begin_comment
comment|/**  * Couchbase producer generates various type of operations. PUT, GET, and DELETE  * are currently supported  */
end_comment

begin_class
DECL|class|CouchbaseProducer
specifier|public
class|class
name|CouchbaseProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
name|CouchbaseEndpoint
name|endpoint
decl_stmt|;
DECL|field|client
specifier|private
name|CouchbaseClientIF
name|client
decl_stmt|;
DECL|field|startId
specifier|private
name|long
name|startId
decl_stmt|;
DECL|field|persistTo
specifier|private
name|PersistTo
name|persistTo
decl_stmt|;
DECL|field|replicateTo
specifier|private
name|ReplicateTo
name|replicateTo
decl_stmt|;
DECL|field|producerRetryAttempts
specifier|private
name|int
name|producerRetryAttempts
decl_stmt|;
DECL|field|producerRetryPause
specifier|private
name|int
name|producerRetryPause
decl_stmt|;
DECL|method|CouchbaseProducer (CouchbaseEndpoint endpoint, CouchbaseClientIF client, int persistTo, int replicateTo)
specifier|public
name|CouchbaseProducer
parameter_list|(
name|CouchbaseEndpoint
name|endpoint
parameter_list|,
name|CouchbaseClientIF
name|client
parameter_list|,
name|int
name|persistTo
parameter_list|,
name|int
name|replicateTo
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
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isAutoStartIdForInserts
argument_list|()
condition|)
block|{
name|this
operator|.
name|startId
operator|=
name|endpoint
operator|.
name|getStartingIdForInsertsFrom
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|producerRetryAttempts
operator|=
name|endpoint
operator|.
name|getProducerRetryAttempts
argument_list|()
expr_stmt|;
name|this
operator|.
name|producerRetryPause
operator|=
name|endpoint
operator|.
name|getProducerRetryPause
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|persistTo
condition|)
block|{
case|case
literal|0
case|:
name|this
operator|.
name|persistTo
operator|=
name|PersistTo
operator|.
name|ZERO
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|this
operator|.
name|persistTo
operator|=
name|PersistTo
operator|.
name|MASTER
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|this
operator|.
name|persistTo
operator|=
name|PersistTo
operator|.
name|TWO
expr_stmt|;
break|break;
case|case
literal|3
case|:
name|this
operator|.
name|persistTo
operator|=
name|PersistTo
operator|.
name|THREE
expr_stmt|;
break|break;
case|case
literal|4
case|:
name|this
operator|.
name|persistTo
operator|=
name|PersistTo
operator|.
name|FOUR
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported persistTo parameter. Supported values are 0 to 4. Currently provided: "
operator|+
name|persistTo
argument_list|)
throw|;
block|}
switch|switch
condition|(
name|replicateTo
condition|)
block|{
case|case
literal|0
case|:
name|this
operator|.
name|replicateTo
operator|=
name|ReplicateTo
operator|.
name|ZERO
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|this
operator|.
name|replicateTo
operator|=
name|ReplicateTo
operator|.
name|ONE
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|this
operator|.
name|replicateTo
operator|=
name|ReplicateTo
operator|.
name|TWO
expr_stmt|;
break|break;
case|case
literal|3
case|:
name|this
operator|.
name|replicateTo
operator|=
name|ReplicateTo
operator|.
name|THREE
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported replicateTo parameter. Supported values are 0 to 3. Currently provided: "
operator|+
name|replicateTo
argument_list|)
throw|;
block|}
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|String
name|id
init|=
operator|(
name|headers
operator|.
name|containsKey
argument_list|(
name|HEADER_ID
argument_list|)
operator|)
condition|?
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HEADER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
else|:
name|endpoint
operator|.
name|getId
argument_list|()
decl_stmt|;
name|int
name|ttl
init|=
operator|(
name|headers
operator|.
name|containsKey
argument_list|(
name|HEADER_TTL
argument_list|)
operator|)
condition|?
name|Integer
operator|.
name|parseInt
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HEADER_TTL
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
else|:
name|DEFAULT_TTL
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isAutoStartIdForInserts
argument_list|()
condition|)
block|{
name|id
operator|=
name|Long
operator|.
name|toString
argument_list|(
name|startId
argument_list|)
expr_stmt|;
name|startId
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CouchbaseException
argument_list|(
name|HEADER_ID
operator|+
literal|" is not specified in message header or endpoint URL."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getOperation
argument_list|()
operator|.
name|equals
argument_list|(
name|COUCHBASE_PUT
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Type of operation: PUT"
argument_list|)
expr_stmt|;
name|Object
name|obj
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|setDocument
argument_list|(
name|id
argument_list|,
name|ttl
argument_list|,
name|obj
argument_list|,
name|persistTo
argument_list|,
name|replicateTo
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getOperation
argument_list|()
operator|.
name|equals
argument_list|(
name|COUCHBASE_GET
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Type of operation: GET"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|client
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getOperation
argument_list|()
operator|.
name|equals
argument_list|(
name|COUCHBASE_DELETE
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Type of operation: DELETE"
argument_list|)
expr_stmt|;
name|Future
argument_list|<
name|Boolean
argument_list|>
name|result
init|=
name|client
operator|.
name|delete
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// cleanup the cache headers
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|HEADER_ID
argument_list|)
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
name|client
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|setDocument (String id, int expiry, Object obj, PersistTo persistTo, ReplicateTo replicateTo)
specifier|private
name|Boolean
name|setDocument
parameter_list|(
name|String
name|id
parameter_list|,
name|int
name|expiry
parameter_list|,
name|Object
name|obj
parameter_list|,
name|PersistTo
name|persistTo
parameter_list|,
name|ReplicateTo
name|replicateTo
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|setDocument
argument_list|(
name|id
argument_list|,
name|expiry
argument_list|,
name|obj
argument_list|,
name|producerRetryAttempts
argument_list|,
name|persistTo
argument_list|,
name|replicateTo
argument_list|)
return|;
block|}
DECL|method|setDocument (String id, int expiry, Object obj, int retryAttempts, PersistTo persistTo, ReplicateTo replicateTo)
specifier|private
name|Boolean
name|setDocument
parameter_list|(
name|String
name|id
parameter_list|,
name|int
name|expiry
parameter_list|,
name|Object
name|obj
parameter_list|,
name|int
name|retryAttempts
parameter_list|,
name|PersistTo
name|persistTo
parameter_list|,
name|ReplicateTo
name|replicateTo
parameter_list|)
throws|throws
name|Exception
block|{
name|OperationFuture
argument_list|<
name|Boolean
argument_list|>
name|result
init|=
name|client
operator|.
name|set
argument_list|(
name|id
argument_list|,
name|expiry
argument_list|,
name|obj
argument_list|,
name|persistTo
argument_list|,
name|replicateTo
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|result
operator|.
name|get
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unable to save Document. "
operator|+
name|id
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|retryAttempts
operator|<=
literal|0
condition|)
block|{
throw|throw
name|e
throw|;
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Unable to save Document, retrying in "
operator|+
name|producerRetryPause
operator|+
literal|"ms ("
operator|+
name|retryAttempts
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|producerRetryPause
argument_list|)
expr_stmt|;
return|return
name|setDocument
argument_list|(
name|id
argument_list|,
name|expiry
argument_list|,
name|obj
argument_list|,
name|retryAttempts
operator|-
literal|1
argument_list|,
name|persistTo
argument_list|,
name|replicateTo
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

