begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|component
operator|.
name|chatscript
operator|.
name|utils
operator|.
name|ChatScriptConstants
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
name|util
operator|.
name|ObjectHelper
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
name|chatscript
operator|.
name|utils
operator|.
name|ChatScriptConstants
operator|.
name|DEFAULT_PORT
import|;
end_import

begin_comment
comment|/**  * Represents a ChatScript endpoint.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"3.0.0"
argument_list|,
name|scheme
operator|=
literal|"chatscript"
argument_list|,
name|title
operator|=
literal|"ChatScript"
argument_list|,
name|syntax
operator|=
literal|"chatscript:host:port/botname"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"ai,chatscript"
argument_list|)
DECL|class|ChatScriptEndpoint
specifier|public
class|class
name|ChatScriptEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Hostname or IP of the server on which CS server is running"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Port on which ChatScript is listening to"
argument_list|,
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
argument_list|(
name|description
operator|=
literal|"Name of the Bot in CS to converse with"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|botName
specifier|private
name|String
name|botName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Username who initializes the CS conversation. To be set when chat is initialized from camel route"
argument_list|,
name|label
operator|=
literal|"username"
argument_list|)
DECL|field|chatUserName
specifier|private
name|String
name|chatUserName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Issues :reset command to start a new conversation everytime"
argument_list|,
name|label
operator|=
literal|"reset"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|resetchat
specifier|private
name|boolean
name|resetchat
decl_stmt|;
DECL|field|bot
specifier|private
name|ChatScriptBot
name|bot
decl_stmt|;
DECL|method|ChatScriptEndpoint ()
specifier|public
name|ChatScriptEndpoint
parameter_list|()
block|{     }
DECL|method|ChatScriptEndpoint (String uri, String remaining, ChatScriptComponent component)
specifier|public
name|ChatScriptEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|ChatScriptComponent
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
literal|"tcp://"
operator|+
name|remaining
argument_list|)
decl_stmt|;
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
name|DEFAULT_PORT
else|:
name|remainingUri
operator|.
name|getPort
argument_list|()
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|remainingUri
operator|.
name|getPath
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ChatScriptConstants
operator|.
name|URI_ERROR
argument_list|)
throw|;
block|}
name|host
operator|=
name|remainingUri
operator|.
name|getHost
argument_list|()
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|host
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ChatScriptConstants
operator|.
name|URI_ERROR
argument_list|)
throw|;
block|}
name|botName
operator|=
name|remainingUri
operator|.
name|getPath
argument_list|()
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|botName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|ChatScriptConstants
operator|.
name|URI_ERROR
argument_list|)
throw|;
block|}
name|botName
operator|=
name|botName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|setBot
argument_list|(
operator|new
name|ChatScriptBot
argument_list|(
name|getHost
argument_list|()
argument_list|,
name|getPort
argument_list|()
argument_list|,
name|getBotName
argument_list|()
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|isResetChat ()
specifier|public
name|boolean
name|isResetChat
parameter_list|()
block|{
return|return
name|resetchat
return|;
block|}
DECL|method|setResetchat (boolean resetChat)
specifier|public
name|void
name|setResetchat
parameter_list|(
name|boolean
name|resetChat
parameter_list|)
block|{
name|this
operator|.
name|resetchat
operator|=
name|resetChat
expr_stmt|;
block|}
DECL|method|getChatUserName ()
specifier|public
name|String
name|getChatUserName
parameter_list|()
block|{
return|return
name|chatUserName
return|;
block|}
DECL|method|setChatUserName (String chatusername)
specifier|public
name|void
name|setChatUserName
parameter_list|(
name|String
name|chatusername
parameter_list|)
block|{
name|this
operator|.
name|chatUserName
operator|=
name|chatusername
expr_stmt|;
block|}
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
name|ChatScriptProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Chatscript consumer not supported"
argument_list|)
throw|;
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
DECL|method|setHost (String hostName)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|hostName
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|hostName
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
DECL|method|setBotName (String botname)
specifier|public
name|void
name|setBotName
parameter_list|(
name|String
name|botname
parameter_list|)
block|{
name|this
operator|.
name|botName
operator|=
name|botname
expr_stmt|;
block|}
DECL|method|getDefaultPort ()
specifier|public
specifier|static
name|int
name|getDefaultPort
parameter_list|()
block|{
return|return
name|DEFAULT_PORT
return|;
block|}
DECL|method|getBot ()
specifier|public
name|ChatScriptBot
name|getBot
parameter_list|()
block|{
return|return
name|bot
return|;
block|}
DECL|method|setBot (ChatScriptBot thisBot)
specifier|public
name|void
name|setBot
parameter_list|(
name|ChatScriptBot
name|thisBot
parameter_list|)
block|{
name|this
operator|.
name|bot
operator|=
name|thisBot
expr_stmt|;
block|}
block|}
end_class

end_unit

