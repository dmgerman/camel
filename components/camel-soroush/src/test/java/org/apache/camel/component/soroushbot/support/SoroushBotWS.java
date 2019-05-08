begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.soroushbot.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|soroushbot
operator|.
name|support
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
name|io
operator|.
name|InputStream
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
name|Random
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Scanner
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|StreamingOutput
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|sse
operator|.
name|SseEventSink
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
name|soroushbot
operator|.
name|IOUtils
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
name|soroushbot
operator|.
name|models
operator|.
name|SoroushMessage
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
name|soroushbot
operator|.
name|models
operator|.
name|response
operator|.
name|SoroushResponse
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
name|soroushbot
operator|.
name|models
operator|.
name|response
operator|.
name|UploadFileResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|logging
operator|.
name|log4j
operator|.
name|LogManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|glassfish
operator|.
name|jersey
operator|.
name|media
operator|.
name|multipart
operator|.
name|FormDataContentDisposition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|glassfish
operator|.
name|jersey
operator|.
name|media
operator|.
name|multipart
operator|.
name|FormDataParam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|glassfish
operator|.
name|jersey
operator|.
name|media
operator|.
name|sse
operator|.
name|OutboundEvent
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|class|SoroushBotWS
specifier|public
class|class
name|SoroushBotWS
block|{
DECL|field|random
specifier|static
name|Random
name|random
init|=
operator|new
name|Random
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|tokenCount
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|tokenCount
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|receivedMessages
specifier|static
name|List
argument_list|<
name|SoroushMessage
argument_list|>
name|receivedMessages
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|fileIdToContent
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fileIdToContent
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|userIds
name|List
argument_list|<
name|String
argument_list|>
name|userIds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|botId
name|String
name|botId
init|=
literal|"botId"
decl_stmt|;
DECL|method|SoroushBotWS ()
specifier|public
name|SoroushBotWS
parameter_list|()
block|{
comment|//users of system
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|userIds
operator|.
name|add
argument_list|(
literal|"u"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getReceivedMessages ()
specifier|public
specifier|static
name|List
argument_list|<
name|SoroushMessage
argument_list|>
name|getReceivedMessages
parameter_list|()
block|{
return|return
name|receivedMessages
return|;
block|}
DECL|method|getFileIdToContent ()
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getFileIdToContent
parameter_list|()
block|{
return|return
name|fileIdToContent
return|;
block|}
DECL|method|clear ()
specifier|public
specifier|static
name|void
name|clear
parameter_list|()
block|{
name|receivedMessages
operator|.
name|clear
argument_list|()
expr_stmt|;
name|fileIdToContent
operator|.
name|clear
argument_list|()
expr_stmt|;
name|tokenCount
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"{token}/getMessage"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|SERVER_SENT_EVENTS
argument_list|)
DECL|method|getMessage (@athParamR) String token, @Context SseEventSink sink)
specifier|public
name|void
name|getMessage
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"token"
argument_list|)
name|String
name|token
parameter_list|,
annotation|@
name|Context
name|SseEventSink
name|sink
parameter_list|)
block|{
name|int
name|messageCount
init|=
name|getNumberOfMessage
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|int
name|delay
init|=
name|getMessageDelay
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|LogManager
operator|.
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"new connection for getting "
operator|+
name|messageCount
operator|+
literal|" message"
argument_list|)
expr_stmt|;
specifier|final
name|boolean
name|withFile
init|=
name|token
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"file"
argument_list|)
decl_stmt|;
comment|//        final EventOutput eventOutput = new EventOutput();
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|messageCount
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|OutboundEvent
operator|.
name|Builder
name|eventBuilder
init|=
operator|new
name|OutboundEvent
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|eventBuilder
operator|.
name|id
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|eventBuilder
operator|.
name|data
argument_list|(
name|SoroushMessage
operator|.
name|class
argument_list|,
name|getSoroushMessage
argument_list|(
name|i
argument_list|,
name|withFile
argument_list|)
argument_list|)
expr_stmt|;
name|eventBuilder
operator|.
name|mediaType
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
expr_stmt|;
specifier|final
name|OutboundEvent
name|event
init|=
name|eventBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
name|sink
operator|.
name|send
argument_list|(
name|event
argument_list|)
expr_stmt|;
comment|//                    eventOutput.write(event);
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"close"
argument_list|)
condition|)
block|{
name|sink
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|getMessageDelay (String token)
specifier|private
name|int
name|getMessageDelay
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|Scanner
name|s
init|=
operator|new
name|Scanner
argument_list|(
name|token
argument_list|)
decl_stmt|;
while|while
condition|(
name|s
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
literal|"delay"
operator|.
name|equalsIgnoreCase
argument_list|(
name|s
operator|.
name|next
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|s
operator|.
name|hasNextInt
argument_list|()
condition|)
block|{
return|return
name|s
operator|.
name|nextInt
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|10
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"{token}/sendMessage"
argument_list|)
DECL|method|sendMessage (SoroushMessage soroushMessage, @PathParam(R) String token)
specifier|public
name|Response
name|sendMessage
parameter_list|(
name|SoroushMessage
name|soroushMessage
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"token"
argument_list|)
name|String
name|token
parameter_list|)
block|{
name|String
name|tokenLower
init|=
name|token
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
name|Scanner
name|s
init|=
operator|new
name|Scanner
argument_list|(
name|tokenLower
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|next
argument_list|()
operator|.
name|equals
argument_list|(
literal|"retry"
argument_list|)
condition|)
block|{
name|int
name|retryCount
init|=
name|s
operator|.
name|nextInt
argument_list|()
decl_stmt|;
name|Integer
name|currentCount
init|=
name|tokenCount
operator|.
name|getOrDefault
argument_list|(
name|tokenLower
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentCount
operator|<
name|retryCount
condition|)
block|{
name|tokenCount
operator|.
name|put
argument_list|(
name|tokenLower
argument_list|,
name|currentCount
operator|+
literal|1
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|status
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|SERVICE_UNAVAILABLE
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
name|receivedMessages
operator|.
name|add
argument_list|(
name|soroushMessage
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
operator|new
name|SoroushResponse
argument_list|(
literal|200
argument_list|,
literal|"OK"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"{token}/uploadFile"
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|MULTIPART_FORM_DATA
argument_list|)
DECL|method|uploadFile (@athParamR) String token, @FormDataParam(R) InputStream fileInputStream, @FormDataParam(R) FormDataContentDisposition fileMetaData)
specifier|public
name|Response
name|uploadFile
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"token"
argument_list|)
name|String
name|token
parameter_list|,
annotation|@
name|FormDataParam
argument_list|(
literal|"file"
argument_list|)
name|InputStream
name|fileInputStream
parameter_list|,
annotation|@
name|FormDataParam
argument_list|(
literal|"file"
argument_list|)
name|FormDataContentDisposition
name|fileMetaData
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|key
init|=
name|Integer
operator|.
name|toString
argument_list|(
name|random
operator|.
name|nextInt
argument_list|()
argument_list|)
decl_stmt|;
name|fileIdToContent
operator|.
name|put
argument_list|(
name|key
argument_list|,
operator|new
name|String
argument_list|(
name|IOUtils
operator|.
name|readFully
argument_list|(
name|fileInputStream
argument_list|,
operator|-
literal|1
argument_list|,
literal|false
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
operator|new
name|UploadFileResponse
argument_list|(
literal|200
argument_list|,
literal|"OK"
argument_list|,
name|key
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"{token}/downloadFile/{key}"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_OCTET_STREAM
argument_list|)
DECL|method|downloadFile (@athParamR) String token, @PathParam(R) String key)
specifier|public
name|Response
name|downloadFile
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"token"
argument_list|)
name|String
name|token
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"key"
argument_list|)
name|String
name|key
parameter_list|)
block|{
name|String
name|content
init|=
name|fileIdToContent
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|==
literal|null
condition|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
literal|404
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
name|StreamingOutput
name|fileStream
init|=
operator|new
name|StreamingOutput
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|java
operator|.
name|io
operator|.
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
try|try
block|{
name|output
operator|.
name|write
argument_list|(
name|content
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|output
operator|.
name|flush
argument_list|()
expr_stmt|;
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|"File Not Found !!"
argument_list|)
throw|;
block|}
block|}
block|}
decl_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|fileStream
argument_list|,
name|MediaType
operator|.
name|APPLICATION_OCTET_STREAM
argument_list|)
operator|.
name|header
argument_list|(
literal|"content-disposition"
argument_list|,
literal|"attachment; filename = file"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|getNumberOfMessage (String token)
specifier|private
name|int
name|getNumberOfMessage
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|token
operator|=
name|token
operator|.
name|replaceAll
argument_list|(
literal|"[^0-9]"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
name|Scanner
name|s
init|=
operator|new
name|Scanner
argument_list|(
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|hasNextInt
argument_list|()
condition|)
block|{
return|return
name|s
operator|.
name|nextInt
argument_list|()
return|;
block|}
return|return
literal|10
return|;
block|}
DECL|method|getSoroushMessage (int i, boolean withFile)
specifier|private
name|SoroushMessage
name|getSoroushMessage
parameter_list|(
name|int
name|i
parameter_list|,
name|boolean
name|withFile
parameter_list|)
block|{
name|SoroushMessage
name|message
init|=
operator|new
name|SoroushMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|setFrom
argument_list|(
name|userIds
operator|.
name|get
argument_list|(
name|i
operator|%
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setTo
argument_list|(
name|botId
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
literal|"message body "
operator|+
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|withFile
operator|&&
name|i
operator|%
literal|4
operator|!=
literal|0
condition|)
block|{
name|String
name|key
init|=
name|Integer
operator|.
name|toString
argument_list|(
name|random
operator|.
name|nextInt
argument_list|()
argument_list|)
decl_stmt|;
name|fileIdToContent
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|random
operator|.
name|nextInt
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setFileUrl
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|%
literal|2
operator|!=
literal|0
condition|)
block|{
name|String
name|thumbnailKey
init|=
name|Integer
operator|.
name|toString
argument_list|(
name|random
operator|.
name|nextInt
argument_list|()
argument_list|)
decl_stmt|;
name|fileIdToContent
operator|.
name|put
argument_list|(
name|thumbnailKey
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|random
operator|.
name|nextInt
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setFileUrl
argument_list|(
name|thumbnailKey
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|message
return|;
block|}
block|}
end_class

end_unit

