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

begin_comment
comment|/**  * Couchbase Constants and default connection parameters  */
end_comment

begin_interface
DECL|interface|CouchbaseConstants
specifier|public
interface|interface
name|CouchbaseConstants
block|{
DECL|field|COUCHBASE_URI_ERROR
name|String
name|COUCHBASE_URI_ERROR
init|=
literal|"Invalid URI. Format must be of the form couchbase:http[s]://hostname[:port]/bucket?[options...]"
decl_stmt|;
DECL|field|COUCHBASE_PUT
name|String
name|COUCHBASE_PUT
init|=
literal|"CCB_PUT"
decl_stmt|;
DECL|field|COUCHBASE_GET
name|String
name|COUCHBASE_GET
init|=
literal|"CCB_GET"
decl_stmt|;
DECL|field|COUCHBASE_DELETE
name|String
name|COUCHBASE_DELETE
init|=
literal|"CCB_DEL"
decl_stmt|;
DECL|field|DEFAULT_DESIGN_DOCUMENT_NAME
name|String
name|DEFAULT_DESIGN_DOCUMENT_NAME
init|=
literal|"beer"
decl_stmt|;
DECL|field|DEFAULT_VIEWNAME
name|String
name|DEFAULT_VIEWNAME
init|=
literal|"brewery_beers"
decl_stmt|;
DECL|field|HEADER_KEY
name|String
name|HEADER_KEY
init|=
literal|"CCB_KEY"
decl_stmt|;
DECL|field|HEADER_ID
name|String
name|HEADER_ID
init|=
literal|"CCB_ID"
decl_stmt|;
DECL|field|HEADER_TTL
name|String
name|HEADER_TTL
init|=
literal|"CCB_TTL"
decl_stmt|;
DECL|field|HEADER_DESIGN_DOCUMENT_NAME
name|String
name|HEADER_DESIGN_DOCUMENT_NAME
init|=
literal|"CCB_DDN"
decl_stmt|;
DECL|field|HEADER_VIEWNAME
name|String
name|HEADER_VIEWNAME
init|=
literal|"CCB_VN"
decl_stmt|;
DECL|field|DEFAULT_PRODUCER_RETRIES
name|int
name|DEFAULT_PRODUCER_RETRIES
init|=
literal|2
decl_stmt|;
DECL|field|DEFAULT_PAUSE_BETWEEN_RETRIES
name|int
name|DEFAULT_PAUSE_BETWEEN_RETRIES
init|=
literal|5000
decl_stmt|;
DECL|field|DEFAULT_COUCHBASE_PORT
name|int
name|DEFAULT_COUCHBASE_PORT
init|=
literal|8091
decl_stmt|;
DECL|field|DEFAULT_TTL
name|int
name|DEFAULT_TTL
init|=
literal|0
decl_stmt|;
DECL|field|DEFAULT_OP_TIMEOUT
name|long
name|DEFAULT_OP_TIMEOUT
init|=
literal|2500
decl_stmt|;
DECL|field|DEFAULT_TIMEOUT_EXCEPTION_THRESHOLD
name|int
name|DEFAULT_TIMEOUT_EXCEPTION_THRESHOLD
init|=
literal|998
decl_stmt|;
DECL|field|DEFAULT_READ_BUFFER_SIZE
name|int
name|DEFAULT_READ_BUFFER_SIZE
init|=
literal|16384
decl_stmt|;
DECL|field|DEFAULT_OP_QUEUE_MAX_BLOCK_TIME
name|long
name|DEFAULT_OP_QUEUE_MAX_BLOCK_TIME
init|=
literal|10000
decl_stmt|;
DECL|field|DEFAULT_MAX_RECONNECT_DELAY
name|long
name|DEFAULT_MAX_RECONNECT_DELAY
init|=
literal|30000
decl_stmt|;
DECL|field|DEFAULT_OBS_POLL_INTERVAL
name|long
name|DEFAULT_OBS_POLL_INTERVAL
init|=
literal|400
decl_stmt|;
DECL|field|DEFAULT_OBS_TIMEOUT
name|long
name|DEFAULT_OBS_TIMEOUT
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|DEFAULT_CONSUME_PROCESSED_STRATEGY
name|String
name|DEFAULT_CONSUME_PROCESSED_STRATEGY
init|=
literal|"none"
decl_stmt|;
block|}
end_interface

end_unit

