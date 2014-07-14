begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wsservlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|wsservlet
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
name|concurrent
operator|.
name|CountDownLatch
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
name|ExecutionException
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
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|AsyncHttpClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|AsyncHttpClientConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|websocket
operator|.
name|WebSocket
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|websocket
operator|.
name|WebSocketByteListener
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|websocket
operator|.
name|WebSocketTextListener
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|websocket
operator|.
name|WebSocketUpgradeHandler
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
DECL|class|TestClient
specifier|public
class|class
name|TestClient
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
name|TestClient
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|received
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|received
decl_stmt|;
DECL|field|latch
specifier|private
name|CountDownLatch
name|latch
decl_stmt|;
DECL|field|client
specifier|private
name|AsyncHttpClient
name|client
decl_stmt|;
DECL|field|websocket
specifier|private
name|WebSocket
name|websocket
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
DECL|method|TestClient (String url, AsyncHttpClientConfig conf)
specifier|public
name|TestClient
parameter_list|(
name|String
name|url
parameter_list|,
name|AsyncHttpClientConfig
name|conf
parameter_list|)
block|{
name|this
argument_list|(
name|url
argument_list|,
name|conf
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|TestClient (String url, int count)
specifier|public
name|TestClient
parameter_list|(
name|String
name|url
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|this
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
DECL|method|TestClient (String url)
specifier|public
name|TestClient
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
argument_list|(
name|url
argument_list|,
literal|null
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|TestClient (String url, AsyncHttpClientConfig conf, int count)
specifier|public
name|TestClient
parameter_list|(
name|String
name|url
parameter_list|,
name|AsyncHttpClientConfig
name|conf
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|this
operator|.
name|received
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|latch
operator|=
operator|new
name|CountDownLatch
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|conf
operator|==
literal|null
condition|?
operator|new
name|AsyncHttpClient
argument_list|()
else|:
operator|new
name|AsyncHttpClient
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
DECL|method|connect ()
specifier|public
name|void
name|connect
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
throws|,
name|IOException
block|{
name|websocket
operator|=
name|client
operator|.
name|prepareGet
argument_list|(
name|url
argument_list|)
operator|.
name|execute
argument_list|(
operator|new
name|WebSocketUpgradeHandler
operator|.
name|Builder
argument_list|()
operator|.
name|addWebSocketListener
argument_list|(
operator|new
name|TestWebSocketListener
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
DECL|method|sendTextMessage (String message)
specifier|public
name|void
name|sendTextMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|websocket
operator|.
name|sendTextMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|sendBytesMessage (byte[] message)
specifier|public
name|void
name|sendBytesMessage
parameter_list|(
name|byte
index|[]
name|message
parameter_list|)
block|{
name|websocket
operator|.
name|sendMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|await (int secs)
specifier|public
name|boolean
name|await
parameter_list|(
name|int
name|secs
parameter_list|)
throws|throws
name|InterruptedException
block|{
return|return
name|latch
operator|.
name|await
argument_list|(
name|secs
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
return|;
block|}
DECL|method|reset (int count)
specifier|public
name|void
name|reset
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|latch
operator|=
operator|new
name|CountDownLatch
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|received
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|getReceived ()
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getReceived
parameter_list|()
block|{
return|return
name|received
return|;
block|}
DECL|method|getReceived (Class<T> cls)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|getReceived
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|received
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|getValue
argument_list|(
name|o
argument_list|,
name|cls
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getValue (Object o, Class<T> cls)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getValue
parameter_list|(
name|Object
name|o
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|.
name|isInstance
argument_list|(
name|o
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|o
return|;
block|}
elseif|else
if|if
condition|(
name|cls
operator|==
name|String
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|byte
index|[]
condition|)
block|{
return|return
operator|(
name|T
operator|)
operator|new
name|String
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|o
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|(
name|T
operator|)
name|o
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|cls
operator|==
name|byte
index|[]
operator|.
name|class
condition|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
return|return
call|(
name|T
call|)
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
operator|.
name|getBytes
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|websocket
operator|.
name|close
argument_list|()
expr_stmt|;
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|class|TestWebSocketListener
specifier|private
class|class
name|TestWebSocketListener
implements|implements
name|WebSocketTextListener
implements|,
name|WebSocketByteListener
block|{
annotation|@
name|Override
DECL|method|onOpen (WebSocket websocket)
specifier|public
name|void
name|onOpen
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"[ws] opened"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onClose (WebSocket websocket)
specifier|public
name|void
name|onClose
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"[ws] closed"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onError (Throwable t)
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"[ws] error"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onMessage (byte[] message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|byte
index|[]
name|message
parameter_list|)
block|{
name|received
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"[ws] received bytes --> "
operator|+
name|message
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFragment (byte[] fragment, boolean last)
specifier|public
name|void
name|onFragment
parameter_list|(
name|byte
index|[]
name|fragment
parameter_list|,
name|boolean
name|last
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
annotation|@
name|Override
DECL|method|onMessage (String message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|received
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"[ws] received --> "
operator|+
name|message
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFragment (String fragment, boolean last)
specifier|public
name|void
name|onFragment
parameter_list|(
name|String
name|fragment
parameter_list|,
name|boolean
name|last
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
block|}
block|}
end_class

end_unit

