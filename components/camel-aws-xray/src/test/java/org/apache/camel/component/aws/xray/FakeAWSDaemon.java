begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|xray
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|invoke
operator|.
name|MethodHandles
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|DatagramPacket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|DatagramSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketException
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|ExecutorService
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
name|Executors
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
name|TimeUnit
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
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|TestDataBuilder
operator|.
name|TestEntity
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
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|TestDataBuilder
operator|.
name|TestSegment
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
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|TestDataBuilder
operator|.
name|TestSubsegment
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
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|TestDataBuilder
operator|.
name|TestTrace
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
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|json
operator|.
name|JsonArray
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
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|json
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
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|json
operator|.
name|JsonParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExternalResource
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

begin_class
DECL|class|FakeAWSDaemon
specifier|public
class|class
name|FakeAWSDaemon
extends|extends
name|ExternalResource
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
name|MethodHandles
operator|.
name|lookup
argument_list|()
operator|.
name|lookupClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|receivedTraces
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|TestTrace
argument_list|>
name|receivedTraces
init|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|socketListener
specifier|private
name|UDPSocketListener
name|socketListener
init|=
operator|new
name|UDPSocketListener
argument_list|(
name|receivedTraces
argument_list|)
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
init|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|before ()
specifier|protected
name|void
name|before
parameter_list|()
throws|throws
name|Throwable
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting up Mock-AWS daemon"
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|submit
argument_list|(
name|socketListener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|after ()
specifier|protected
name|void
name|after
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Shutting down Mock-AWS daemon"
argument_list|)
expr_stmt|;
name|socketListener
operator|.
name|close
argument_list|()
expr_stmt|;
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|executorService
operator|.
name|awaitTermination
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
condition|)
block|{
name|executorService
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|executorService
operator|.
name|awaitTermination
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not terminate UDP server"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|iEx
parameter_list|)
block|{
name|executorService
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getReceivedData ()
name|Map
argument_list|<
name|String
argument_list|,
name|TestTrace
argument_list|>
name|getReceivedData
parameter_list|()
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"List of received data packages requested: {}"
argument_list|,
name|receivedTraces
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|receivedTraces
return|;
block|}
DECL|class|UDPSocketListener
specifier|private
specifier|static
specifier|final
class|class
name|UDPSocketListener
implements|implements
name|Runnable
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
name|MethodHandles
operator|.
name|lookup
argument_list|()
operator|.
name|lookupClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|serverSocket
specifier|private
name|DatagramSocket
name|serverSocket
decl_stmt|;
DECL|field|receivedTraces
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|TestTrace
argument_list|>
name|receivedTraces
decl_stmt|;
DECL|field|done
specifier|private
specifier|volatile
name|boolean
name|done
decl_stmt|;
DECL|method|UDPSocketListener (Map<String, TestTrace> receivedTraces)
specifier|private
name|UDPSocketListener
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|TestTrace
argument_list|>
name|receivedTraces
parameter_list|)
block|{
name|this
operator|.
name|receivedTraces
operator|=
name|receivedTraces
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting UDP socket listening on port 2000"
argument_list|)
expr_stmt|;
name|serverSocket
operator|=
operator|new
name|DatagramSocket
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
name|byte
index|[]
name|receiveData
init|=
operator|new
name|byte
index|[
literal|2048
index|]
decl_stmt|;
name|DatagramPacket
name|receivedPacket
init|=
operator|new
name|DatagramPacket
argument_list|(
name|receiveData
argument_list|,
name|receiveData
operator|.
name|length
argument_list|)
decl_stmt|;
name|serverSocket
operator|.
name|receive
argument_list|(
name|receivedPacket
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Receiving UDP data"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
operator|new
name|String
argument_list|(
name|receivedPacket
operator|.
name|getData
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|locSegment
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|raw
init|=
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
index|[]
name|segments
init|=
name|raw
operator|.
name|split
argument_list|(
literal|"\\n"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|segment
range|:
name|segments
control|)
block|{
name|locSegment
operator|=
name|segment
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing received segment: {}"
argument_list|,
name|segment
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|segment
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|segment
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
operator|||
name|StringUtils
operator|.
name|countMatches
argument_list|(
name|segment
argument_list|,
literal|"{"
argument_list|)
operator|!=
name|StringUtils
operator|.
name|countMatches
argument_list|(
name|segment
argument_list|,
literal|"}"
argument_list|)
operator|||
name|StringUtils
operator|.
name|countMatches
argument_list|(
name|segment
argument_list|,
literal|"["
argument_list|)
operator|!=
name|StringUtils
operator|.
name|countMatches
argument_list|(
name|segment
argument_list|,
literal|"]"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Skipping incomplete content: {}"
argument_list|,
name|segment
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|segment
operator|.
name|contains
argument_list|(
literal|"format"
argument_list|)
operator|&&
name|segment
operator|.
name|contains
argument_list|(
literal|"version"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Skipping format and version JSON"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Converting segment {} to a Java object"
argument_list|,
name|segment
argument_list|)
expr_stmt|;
comment|// clean the JSON string received
name|LOG
operator|.
name|trace
argument_list|(
literal|"Original JSON content: {}"
argument_list|,
name|segment
argument_list|)
expr_stmt|;
name|locSegment
operator|=
name|segment
expr_stmt|;
name|JsonObject
name|json
init|=
operator|(
name|JsonObject
operator|)
name|JsonParser
operator|.
name|parse
argument_list|(
name|segment
argument_list|)
decl_stmt|;
name|String
name|traceId
init|=
name|json
operator|.
name|getString
argument_list|(
literal|"trace_id"
argument_list|)
decl_stmt|;
name|TestTrace
name|testTrace
init|=
name|receivedTraces
operator|.
name|get
argument_list|(
name|traceId
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|testTrace
condition|)
block|{
name|testTrace
operator|=
operator|new
name|TestTrace
argument_list|()
expr_stmt|;
block|}
name|testTrace
operator|.
name|withSegment
argument_list|(
name|convertData
argument_list|(
name|json
argument_list|)
argument_list|)
expr_stmt|;
name|receivedTraces
operator|.
name|put
argument_list|(
name|traceId
argument_list|,
name|testTrace
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|delete
argument_list|(
literal|0
argument_list|,
name|segment
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|1
operator|&&
name|sb
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'\n'
condition|)
block|{
name|sb
operator|.
name|deleteCharAt
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Item {} received. JSON content: {}, Raw: {}"
argument_list|,
name|receivedTraces
operator|.
name|size
argument_list|()
argument_list|,
name|receivedTraces
argument_list|,
name|raw
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|jsonEx
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not convert segment "
operator|+
name|locSegment
operator|+
literal|" to a Java object"
argument_list|,
name|jsonEx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|SocketException
name|sex
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"UDP socket closed"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"UDP socket failed due to "
operator|+
name|ex
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|convertData (JsonObject json)
specifier|private
name|TestSegment
name|convertData
parameter_list|(
name|JsonObject
name|json
parameter_list|)
block|{
name|String
name|name
init|=
name|json
operator|.
name|getString
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|double
name|startTime
init|=
name|json
operator|.
name|getDouble
argument_list|(
literal|"start_time"
argument_list|)
decl_stmt|;
name|TestSegment
name|segment
init|=
operator|new
name|TestSegment
argument_list|(
name|name
argument_list|,
name|startTime
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|.
name|has
argument_list|(
literal|"subsegments"
argument_list|)
condition|)
block|{
name|JsonArray
name|jsonSubsegments
init|=
operator|(
name|JsonArray
operator|)
name|json
operator|.
name|get
argument_list|(
literal|"subsegments"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|TestSubsegment
argument_list|>
name|subsegments
init|=
name|convertSubsegments
argument_list|(
name|jsonSubsegments
argument_list|)
decl_stmt|;
for|for
control|(
name|TestSubsegment
name|subsegment
range|:
name|subsegments
control|)
block|{
name|segment
operator|.
name|withSubsegment
argument_list|(
name|subsegment
argument_list|)
expr_stmt|;
block|}
block|}
name|addAnnotationsIfAvailable
argument_list|(
name|segment
argument_list|,
name|json
argument_list|)
expr_stmt|;
name|addMetadataIfAvailable
argument_list|(
name|segment
argument_list|,
name|json
argument_list|)
expr_stmt|;
return|return
name|segment
return|;
block|}
DECL|method|convertSubsegments (JsonArray jsonSubsegments)
specifier|private
name|List
argument_list|<
name|TestSubsegment
argument_list|>
name|convertSubsegments
parameter_list|(
name|JsonArray
name|jsonSubsegments
parameter_list|)
block|{
name|List
argument_list|<
name|TestSubsegment
argument_list|>
name|subsegments
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|jsonSubsegments
operator|.
name|size
argument_list|()
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
name|jsonSubsegments
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|JsonObject
name|jsonSubsegment
init|=
name|jsonSubsegments
operator|.
name|toArray
argument_list|(
operator|new
name|JsonObject
index|[
name|jsonSubsegments
operator|.
name|size
argument_list|()
index|]
argument_list|)
index|[
name|i
index|]
decl_stmt|;
name|subsegments
operator|.
name|add
argument_list|(
name|convertSubsegment
argument_list|(
name|jsonSubsegment
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|subsegments
return|;
block|}
DECL|method|convertSubsegment (JsonObject json)
specifier|private
name|TestSubsegment
name|convertSubsegment
parameter_list|(
name|JsonObject
name|json
parameter_list|)
block|{
name|TestSubsegment
name|subsegment
init|=
operator|new
name|TestSubsegment
argument_list|(
operator|(
name|String
operator|)
name|json
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|.
name|has
argument_list|(
literal|"subsegments"
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|TestSubsegment
argument_list|>
name|subsegments
init|=
name|convertSubsegments
argument_list|(
operator|(
name|JsonArray
operator|)
name|json
operator|.
name|get
argument_list|(
literal|"subsegments"
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|TestSubsegment
name|tss
range|:
name|subsegments
control|)
block|{
name|subsegment
operator|.
name|withSubsegment
argument_list|(
name|tss
argument_list|)
expr_stmt|;
block|}
block|}
name|addAnnotationsIfAvailable
argument_list|(
name|subsegment
argument_list|,
name|json
argument_list|)
expr_stmt|;
name|addMetadataIfAvailable
argument_list|(
name|subsegment
argument_list|,
name|json
argument_list|)
expr_stmt|;
return|return
name|subsegment
return|;
block|}
DECL|method|addAnnotationsIfAvailable (TestEntity<?> entity, JsonObject json)
specifier|private
name|void
name|addAnnotationsIfAvailable
parameter_list|(
name|TestEntity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|JsonObject
name|json
parameter_list|)
block|{
if|if
condition|(
name|json
operator|.
name|has
argument_list|(
literal|"annotations"
argument_list|)
condition|)
block|{
name|JsonObject
name|annotations
init|=
operator|(
name|JsonObject
operator|)
name|json
operator|.
name|get
argument_list|(
literal|"annotations"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|annotations
operator|.
name|getKeys
argument_list|()
control|)
block|{
name|entity
operator|.
name|withAnnotation
argument_list|(
operator|(
name|String
operator|)
name|key
argument_list|,
name|annotations
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|addMetadataIfAvailable (TestEntity<?> entity, JsonObject json)
specifier|private
name|void
name|addMetadataIfAvailable
parameter_list|(
name|TestEntity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|JsonObject
name|json
parameter_list|)
block|{
if|if
condition|(
name|json
operator|.
name|has
argument_list|(
literal|"metadata"
argument_list|)
condition|)
block|{
name|JsonObject
name|rawMetadata
init|=
operator|(
name|JsonObject
operator|)
name|json
operator|.
name|get
argument_list|(
literal|"metadata"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|metadata
init|=
name|parseMetadata
argument_list|(
name|rawMetadata
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|namespace
range|:
name|metadata
operator|.
name|keySet
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|key
range|:
name|metadata
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
operator|.
name|keySet
argument_list|()
control|)
block|{
name|entity
operator|.
name|withMetadata
argument_list|(
name|namespace
argument_list|,
name|key
argument_list|,
name|metadata
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|parseMetadata (JsonObject json)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|parseMetadata
parameter_list|(
name|JsonObject
name|json
parameter_list|)
block|{
comment|/*              "metadata" : {                 "default" : {                     "meta1" : "meta1"                 },                 "customNamespace" : {                     "meta2" : "meta2"                 }              }              */
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|metadata
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|json
operator|.
name|getKeys
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|namespace
range|:
name|json
operator|.
name|getKeys
argument_list|()
control|)
block|{
name|JsonObject
name|namespaceData
init|=
operator|(
name|JsonObject
operator|)
name|json
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|metadata
operator|.
name|containsKey
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|metadata
operator|.
name|put
argument_list|(
name|namespace
argument_list|,
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|namespaceData
operator|.
name|getKeys
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|key
range|:
name|namespaceData
operator|.
name|getKeys
argument_list|()
control|)
block|{
name|metadata
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|namespaceData
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|metadata
return|;
block|}
DECL|method|close ()
specifier|private
name|void
name|close
parameter_list|()
block|{
name|done
operator|=
literal|true
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|serverSocket
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Shutting down UDP socket"
argument_list|)
expr_stmt|;
name|serverSocket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

