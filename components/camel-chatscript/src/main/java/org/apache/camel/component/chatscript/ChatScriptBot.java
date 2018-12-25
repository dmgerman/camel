begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.chatscript
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|chatscript
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

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
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
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
DECL|class|ChatScriptBot
specifier|public
class|class
name|ChatScriptBot
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ChatScriptBot
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|host
name|String
name|host
decl_stmt|;
DECL|field|port
name|int
name|port
decl_stmt|;
DECL|field|message
name|String
name|message
decl_stmt|;
DECL|field|botName
name|String
name|botName
decl_stmt|;
DECL|field|userName
name|String
name|userName
decl_stmt|;
DECL|field|initialized
name|boolean
name|initialized
decl_stmt|;
DECL|method|ChatScriptBot (String iHost, int port, String iBotName, String iUserName)
specifier|public
name|ChatScriptBot
parameter_list|(
name|String
name|iHost
parameter_list|,
name|int
name|port
parameter_list|,
name|String
name|iBotName
parameter_list|,
name|String
name|iUserName
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|iHost
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|botName
operator|=
name|iBotName
expr_stmt|;
name|this
operator|.
name|userName
operator|=
name|iUserName
expr_stmt|;
block|}
DECL|method|sendChat (String input)
specifier|public
name|String
name|sendChat
parameter_list|(
name|String
name|input
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
return|return
name|init
argument_list|(
literal|null
argument_list|)
return|;
block|}
name|ChatScriptMessage
name|g
init|=
operator|new
name|ChatScriptMessage
argument_list|(
name|this
operator|.
name|userName
argument_list|,
name|this
operator|.
name|botName
argument_list|,
name|input
argument_list|)
decl_stmt|;
return|return
name|doMessage
argument_list|(
name|g
operator|.
name|toCSFormat
argument_list|()
argument_list|)
return|;
block|}
DECL|method|sendChat (ChatScriptMessage input)
specifier|public
name|String
name|sendChat
parameter_list|(
name|ChatScriptMessage
name|input
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
return|return
name|init
argument_list|(
name|input
argument_list|)
return|;
block|}
return|return
name|doMessage
argument_list|(
name|input
operator|.
name|toCSFormat
argument_list|()
argument_list|)
return|;
block|}
DECL|method|doMessage (ChatScriptMessage msg)
specifier|private
name|String
name|doMessage
parameter_list|(
name|ChatScriptMessage
name|msg
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|doMessage
argument_list|(
name|msg
operator|.
name|toCSFormat
argument_list|()
argument_list|)
return|;
block|}
DECL|method|doMessage (String msg)
specifier|private
name|String
name|doMessage
parameter_list|(
name|String
name|msg
parameter_list|)
throws|throws
name|Exception
block|{
name|Socket
name|echoSocket
decl_stmt|;
name|String
name|resp
init|=
literal|""
decl_stmt|;
try|try
block|{
name|echoSocket
operator|=
operator|new
name|Socket
argument_list|(
name|this
operator|.
name|host
argument_list|,
name|this
operator|.
name|port
argument_list|)
expr_stmt|;
name|PrintWriter
name|out
init|=
operator|new
name|PrintWriter
argument_list|(
name|echoSocket
operator|.
name|getOutputStream
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|BufferedReader
name|in
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|echoSocket
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|out
operator|.
name|println
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|resp
operator|=
name|in
operator|.
name|readLine
argument_list|()
expr_stmt|;
name|echoSocket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unable to send message to ChatScript Server. Reason:"
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|resp
return|;
block|}
DECL|method|init (ChatScriptMessage input)
specifier|public
name|String
name|init
parameter_list|(
name|ChatScriptMessage
name|input
parameter_list|)
throws|throws
name|Exception
block|{
name|ChatScriptMessage
name|g
init|=
operator|new
name|ChatScriptMessage
argument_list|(
name|input
operator|.
name|getUserName
argument_list|()
argument_list|,
name|this
operator|.
name|botName
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|response
init|=
name|doMessage
argument_list|(
name|g
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Conversation started between the bot "
operator|+
name|this
operator|.
name|botName
operator|+
literal|" and "
operator|+
name|input
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|initialized
operator|=
literal|true
expr_stmt|;
return|return
name|response
return|;
block|}
DECL|method|getBotType ()
specifier|public
name|String
name|getBotType
parameter_list|()
block|{
return|return
literal|"ChatSCript"
return|;
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
comment|//TODO
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
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (int iPort)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|iPort
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|iPort
expr_stmt|;
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
DECL|method|setMessage (String iMessage)
specifier|public
name|void
name|setMessage
parameter_list|(
name|String
name|iMessage
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|iMessage
expr_stmt|;
block|}
DECL|method|getBotName ()
specifier|public
name|String
name|getBotName
parameter_list|()
block|{
return|return
name|botName
return|;
block|}
DECL|method|setBotName (String iBotName)
specifier|public
name|void
name|setBotName
parameter_list|(
name|String
name|iBotName
parameter_list|)
block|{
name|this
operator|.
name|botName
operator|=
name|iBotName
expr_stmt|;
block|}
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
DECL|method|setUserName (String iUserName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|iUserName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|iUserName
expr_stmt|;
block|}
DECL|method|isInitialized ()
specifier|public
name|boolean
name|isInitialized
parameter_list|()
block|{
return|return
name|initialized
return|;
block|}
DECL|method|setInitialized (boolean initialized)
specifier|public
name|void
name|setInitialized
parameter_list|(
name|boolean
name|initialized
parameter_list|)
block|{
name|this
operator|.
name|initialized
operator|=
name|initialized
expr_stmt|;
block|}
block|}
end_class

end_unit

