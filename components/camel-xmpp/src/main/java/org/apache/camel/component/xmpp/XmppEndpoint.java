begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmpp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|CamelException
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
name|ExchangePattern
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
name|commons
operator|.
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|AccountManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|ConnectionConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|XMPPConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|XMPPException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smackx
operator|.
name|muc
operator|.
name|MultiUserChat
import|;
end_import

begin_comment
comment|/**  * A XMPP Endpoint  *  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|XmppEndpoint
specifier|public
class|class
name|XmppEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|XmppEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|binding
specifier|private
name|XmppBinding
name|binding
decl_stmt|;
DECL|field|connection
specifier|private
name|XMPPConnection
name|connection
decl_stmt|;
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|user
specifier|private
name|String
name|user
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|resource
specifier|private
name|String
name|resource
init|=
literal|"Camel"
decl_stmt|;
DECL|field|login
specifier|private
name|boolean
name|login
init|=
literal|true
decl_stmt|;
DECL|field|createAccount
specifier|private
name|boolean
name|createAccount
decl_stmt|;
DECL|field|room
specifier|private
name|String
name|room
decl_stmt|;
DECL|field|participant
specifier|private
name|String
name|participant
decl_stmt|;
DECL|field|nickname
specifier|private
name|String
name|nickname
decl_stmt|;
DECL|field|serviceName
specifier|private
name|String
name|serviceName
decl_stmt|;
DECL|method|XmppEndpoint (String uri, XmppComponent component)
specifier|public
name|XmppEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|XmppComponent
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
name|binding
operator|=
operator|new
name|XmppBinding
argument_list|(
name|component
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|XmppEndpoint (String endpointUri)
specifier|public
name|XmppEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
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
if|if
condition|(
name|room
operator|!=
literal|null
condition|)
block|{
return|return
name|createGroupChatProducer
argument_list|()
return|;
block|}
else|else
block|{
if|if
condition|(
name|participant
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No room or participant configured on this endpoint: "
operator|+
name|this
argument_list|)
throw|;
block|}
return|return
name|createPrivateChatProducer
argument_list|(
name|participant
argument_list|)
return|;
block|}
block|}
DECL|method|createGroupChatProducer ()
specifier|public
name|Producer
name|createGroupChatProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|XmppGroupChatProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createPrivateChatProducer (String participant)
specifier|public
name|Producer
name|createPrivateChatProducer
parameter_list|(
name|String
name|participant
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|XmppPrivateChatProducer
argument_list|(
name|this
argument_list|,
name|participant
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
return|return
operator|new
name|XmppConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|XmppExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|pattern
argument_list|,
name|getBinding
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createExchange (Message message)
specifier|public
name|XmppExchange
name|createExchange
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|new
name|XmppExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
name|message
argument_list|)
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getBinding ()
specifier|public
name|XmppBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
operator|new
name|XmppBinding
argument_list|()
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
comment|/**      * Sets the binding used to convert from a Camel message to and from an XMPP      * message      */
DECL|method|setBinding (XmppBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|XmppBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
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
DECL|method|getResource ()
specifier|public
name|String
name|getResource
parameter_list|()
block|{
return|return
name|resource
return|;
block|}
DECL|method|setResource (String resource)
specifier|public
name|void
name|setResource
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|this
operator|.
name|resource
operator|=
name|resource
expr_stmt|;
block|}
DECL|method|isLogin ()
specifier|public
name|boolean
name|isLogin
parameter_list|()
block|{
return|return
name|login
return|;
block|}
DECL|method|setLogin (boolean login)
specifier|public
name|void
name|setLogin
parameter_list|(
name|boolean
name|login
parameter_list|)
block|{
name|this
operator|.
name|login
operator|=
name|login
expr_stmt|;
block|}
DECL|method|isCreateAccount ()
specifier|public
name|boolean
name|isCreateAccount
parameter_list|()
block|{
return|return
name|createAccount
return|;
block|}
DECL|method|setCreateAccount (boolean createAccount)
specifier|public
name|void
name|setCreateAccount
parameter_list|(
name|boolean
name|createAccount
parameter_list|)
block|{
name|this
operator|.
name|createAccount
operator|=
name|createAccount
expr_stmt|;
block|}
DECL|method|getRoom ()
specifier|public
name|String
name|getRoom
parameter_list|()
block|{
return|return
name|room
return|;
block|}
DECL|method|setRoom (String room)
specifier|public
name|void
name|setRoom
parameter_list|(
name|String
name|room
parameter_list|)
block|{
name|this
operator|.
name|room
operator|=
name|room
expr_stmt|;
block|}
DECL|method|getParticipant ()
specifier|public
name|String
name|getParticipant
parameter_list|()
block|{
return|return
name|participant
return|;
block|}
DECL|method|setParticipant (String participant)
specifier|public
name|void
name|setParticipant
parameter_list|(
name|String
name|participant
parameter_list|)
block|{
name|this
operator|.
name|participant
operator|=
name|participant
expr_stmt|;
block|}
DECL|method|getNickname ()
specifier|public
name|String
name|getNickname
parameter_list|()
block|{
return|return
name|nickname
operator|!=
literal|null
condition|?
name|nickname
else|:
name|getUser
argument_list|()
return|;
block|}
DECL|method|setNickname (String nickname)
specifier|public
name|void
name|setNickname
parameter_list|(
name|String
name|nickname
parameter_list|)
block|{
name|this
operator|.
name|nickname
operator|=
name|nickname
expr_stmt|;
block|}
DECL|method|setServiceName (String serviceName)
specifier|public
name|void
name|setServiceName
parameter_list|(
name|String
name|serviceName
parameter_list|)
block|{
name|this
operator|.
name|serviceName
operator|=
name|serviceName
expr_stmt|;
block|}
DECL|method|getServiceName ()
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
DECL|method|getConnection ()
specifier|public
name|XMPPConnection
name|getConnection
parameter_list|()
throws|throws
name|XMPPException
block|{
if|if
condition|(
name|connection
operator|==
literal|null
condition|)
block|{
name|connection
operator|=
name|createConnection
argument_list|()
expr_stmt|;
block|}
return|return
name|connection
return|;
block|}
DECL|method|setConnection (XMPPConnection connection)
specifier|public
name|void
name|setConnection
parameter_list|(
name|XMPPConnection
name|connection
parameter_list|)
block|{
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|createConnection ()
specifier|protected
name|XMPPConnection
name|createConnection
parameter_list|()
throws|throws
name|XMPPException
block|{
name|XMPPConnection
name|connection
decl_stmt|;
if|if
condition|(
name|port
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|getServiceName
argument_list|()
operator|==
literal|null
condition|)
block|{
name|connection
operator|=
operator|new
name|XMPPConnection
argument_list|(
operator|new
name|ConnectionConfiguration
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|connection
operator|=
operator|new
name|XMPPConnection
argument_list|(
operator|new
name|ConnectionConfiguration
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|getServiceName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|connection
operator|=
operator|new
name|XMPPConnection
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|connect
argument_list|()
expr_stmt|;
if|if
condition|(
name|login
operator|&&
operator|!
name|connection
operator|.
name|isAuthenticated
argument_list|()
condition|)
block|{
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Logging in to XMPP as user: "
operator|+
name|user
operator|+
literal|" on connection: "
operator|+
name|connection
argument_list|)
expr_stmt|;
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No password configured for user: "
operator|+
name|user
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|createAccount
condition|)
block|{
name|AccountManager
name|accountManager
init|=
operator|new
name|AccountManager
argument_list|(
name|connection
argument_list|)
decl_stmt|;
name|accountManager
operator|.
name|createAccount
argument_list|(
name|user
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resource
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|login
argument_list|(
name|user
argument_list|,
name|password
argument_list|,
name|resource
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|connection
operator|.
name|login
argument_list|(
name|user
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Logging in anonymously to XMPP on connection: "
operator|+
name|connection
argument_list|)
expr_stmt|;
name|connection
operator|.
name|loginAnonymously
argument_list|()
expr_stmt|;
block|}
comment|// presence is not needed to be sent after login
block|}
return|return
name|connection
return|;
block|}
comment|/*      * If there is no "@" symbol in the room, find the chat service JID and      * return fully qualified JID for the room as room@conference.server.domain      */
DECL|method|resolveRoom ()
specifier|public
name|String
name|resolveRoom
parameter_list|()
throws|throws
name|XMPPException
throws|,
name|CamelException
block|{
if|if
condition|(
name|room
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"room is not specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|room
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|,
literal|0
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|room
return|;
block|}
name|XMPPConnection
name|conn
init|=
name|getConnection
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|MultiUserChat
operator|.
name|getServiceNames
argument_list|(
name|conn
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Cannot find Multi User Chat service"
argument_list|)
throw|;
block|}
name|String
name|chatServer
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Detected chat server: "
operator|+
name|chatServer
argument_list|)
expr_stmt|;
block|}
return|return
name|room
operator|+
literal|"@"
operator|+
name|chatServer
return|;
block|}
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
block|}
end_class

end_unit

