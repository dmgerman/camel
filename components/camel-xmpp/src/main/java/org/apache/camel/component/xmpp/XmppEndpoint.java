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
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
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
name|impl
operator|.
name|DefaultHeaderFilterStrategy
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
name|HeaderFilterStrategy
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
name|HeaderFilterStrategyAware
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
name|util
operator|.
name|ObjectHelper
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
name|StringHelper
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
name|SmackException
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
name|XMPPException
operator|.
name|XMPPErrorException
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
name|Stanza
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
name|StanzaError
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
name|StanzaError
operator|.
name|Condition
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
name|tcp
operator|.
name|XMPPTCPConnection
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
name|tcp
operator|.
name|XMPPTCPConnectionConfiguration
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
name|iqregister
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
name|smackx
operator|.
name|muc
operator|.
name|MultiUserChatManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jxmpp
operator|.
name|jid
operator|.
name|DomainBareJid
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jxmpp
operator|.
name|jid
operator|.
name|parts
operator|.
name|Localpart
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jxmpp
operator|.
name|jid
operator|.
name|parts
operator|.
name|Resourcepart
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jxmpp
operator|.
name|stringprep
operator|.
name|XmppStringprepException
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

begin_comment
comment|/**  * To send and receive messages from a XMPP (chat) server.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.0"
argument_list|,
name|scheme
operator|=
literal|"xmpp"
argument_list|,
name|title
operator|=
literal|"XMPP"
argument_list|,
name|syntax
operator|=
literal|"xmpp:host:port/participant"
argument_list|,
name|alternativeSyntax
operator|=
literal|"xmpp:user:password@host:port/participant"
argument_list|,
name|consumerClass
operator|=
name|XmppConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"chat,messaging"
argument_list|)
DECL|class|XmppEndpoint
specifier|public
class|class
name|XmppEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|HeaderFilterStrategyAware
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
name|XmppEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|connection
specifier|private
specifier|volatile
name|XMPPTCPConnection
name|connection
decl_stmt|;
DECL|field|binding
specifier|private
name|XmppBinding
name|binding
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
DECL|field|host
specifier|private
name|String
name|host
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
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|participant
specifier|private
name|String
name|participant
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
DECL|field|user
specifier|private
name|String
name|user
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
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common,advanced"
argument_list|,
name|defaultValue
operator|=
literal|"Camel"
argument_list|)
DECL|field|resource
specifier|private
name|String
name|resource
init|=
literal|"Camel"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|login
specifier|private
name|boolean
name|login
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common,advanced"
argument_list|)
DECL|field|createAccount
specifier|private
name|boolean
name|createAccount
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|room
specifier|private
name|String
name|room
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|nickname
specifier|private
name|String
name|nickname
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|serviceName
specifier|private
name|String
name|serviceName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|pubsub
specifier|private
name|boolean
name|pubsub
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|doc
specifier|private
name|boolean
name|doc
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|testConnectionOnStartup
specifier|private
name|boolean
name|testConnectionOnStartup
init|=
literal|true
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
literal|"10"
argument_list|)
DECL|field|connectionPollDelay
specifier|private
name|int
name|connectionPollDelay
init|=
literal|10
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"filter"
argument_list|)
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|connectionConfig
specifier|private
name|ConnectionConfiguration
name|connectionConfig
decl_stmt|;
DECL|method|XmppEndpoint ()
specifier|public
name|XmppEndpoint
parameter_list|()
block|{     }
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
block|}
annotation|@
name|Deprecated
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
name|isPubsub
argument_list|()
condition|)
block|{
return|return
name|createPubSubProducer
argument_list|()
return|;
block|}
if|if
condition|(
name|isDoc
argument_list|()
condition|)
block|{
return|return
name|createDirectProducer
argument_list|()
return|;
block|}
if|if
condition|(
name|getParticipant
argument_list|()
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
name|getParticipant
argument_list|()
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
DECL|method|createDirectProducer ()
specifier|public
name|Producer
name|createDirectProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|XmppDirectProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createPubSubProducer ()
specifier|public
name|Producer
name|createPubSubProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|XmppPubSubProducer
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
name|XmppConsumer
name|answer
init|=
operator|new
name|XmppConsumer
argument_list|(
name|this
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
DECL|method|createExchange (Stanza packet)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Stanza
name|packet
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
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|,
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|XmppMessage
argument_list|(
name|packet
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"xmpp://"
operator|+
name|host
operator|+
literal|":"
operator|+
name|port
operator|+
literal|"/"
operator|+
name|getParticipant
argument_list|()
operator|+
literal|"?serviceName="
operator|+
name|serviceName
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
DECL|method|createConnection ()
specifier|public
specifier|synchronized
name|XMPPTCPConnection
name|createConnection
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|SmackException
throws|,
name|XMPPException
block|{
if|if
condition|(
name|connection
operator|!=
literal|null
operator|&&
name|connection
operator|.
name|isConnected
argument_list|()
condition|)
block|{
comment|// use existing working connection
return|return
name|connection
return|;
block|}
comment|// prepare for creating new connection
name|connection
operator|=
literal|null
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating new connection ..."
argument_list|)
expr_stmt|;
name|XMPPTCPConnection
name|newConnection
init|=
name|createConnectionInternal
argument_list|()
decl_stmt|;
name|newConnection
operator|.
name|connect
argument_list|()
expr_stmt|;
name|newConnection
operator|.
name|addSyncStanzaListener
argument_list|(
operator|new
name|XmppLogger
argument_list|(
literal|"INBOUND"
argument_list|)
argument_list|,
name|stanza
lambda|->
literal|true
argument_list|)
expr_stmt|;
name|newConnection
operator|.
name|addSyncStanzaListener
argument_list|(
operator|new
name|XmppLogger
argument_list|(
literal|"OUTBOUND"
argument_list|)
argument_list|,
name|stanza
lambda|->
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|newConnection
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Logging in to XMPP as user: {} on connection: {}"
argument_list|,
name|user
argument_list|,
name|getConnectionMessage
argument_list|(
name|newConnection
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"No password configured for user: {} on connection: {}"
argument_list|,
name|user
argument_list|,
name|getConnectionMessage
argument_list|(
name|newConnection
argument_list|)
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
name|AccountManager
operator|.
name|getInstance
argument_list|(
name|newConnection
argument_list|)
decl_stmt|;
name|accountManager
operator|.
name|createAccount
argument_list|(
name|Localpart
operator|.
name|from
argument_list|(
name|user
argument_list|)
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|login
condition|)
block|{
if|if
condition|(
name|resource
operator|!=
literal|null
condition|)
block|{
name|newConnection
operator|.
name|login
argument_list|(
name|user
argument_list|,
name|password
argument_list|,
name|Resourcepart
operator|.
name|from
argument_list|(
name|resource
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newConnection
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
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Logging in anonymously to XMPP on connection: {}"
argument_list|,
name|getConnectionMessage
argument_list|(
name|newConnection
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|newConnection
operator|.
name|login
argument_list|()
expr_stmt|;
block|}
comment|// presence is not needed to be sent after login
block|}
comment|// okay new connection was created successfully so assign it as the connection
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created new connection successfully: {}"
argument_list|,
name|newConnection
argument_list|)
expr_stmt|;
name|connection
operator|=
name|newConnection
expr_stmt|;
return|return
name|connection
return|;
block|}
DECL|method|createConnectionInternal ()
specifier|private
name|XMPPTCPConnection
name|createConnectionInternal
parameter_list|()
throws|throws
name|UnknownHostException
throws|,
name|XmppStringprepException
block|{
if|if
condition|(
name|connectionConfig
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|XMPPTCPConnection
argument_list|(
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|XMPPTCPConnectionConfiguration
operator|.
name|class
argument_list|,
name|connectionConfig
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
name|port
operator|==
literal|0
condition|)
block|{
name|port
operator|=
literal|5222
expr_stmt|;
block|}
name|String
name|sName
init|=
name|getServiceName
argument_list|()
operator|==
literal|null
condition|?
name|host
else|:
name|getServiceName
argument_list|()
decl_stmt|;
name|XMPPTCPConnectionConfiguration
name|conf
init|=
name|XMPPTCPConnectionConfiguration
operator|.
name|builder
argument_list|()
operator|.
name|setHostAddress
argument_list|(
name|InetAddress
operator|.
name|getByName
argument_list|(
name|host
argument_list|)
argument_list|)
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
operator|.
name|setXmppDomain
argument_list|(
name|sName
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
operator|new
name|XMPPTCPConnection
argument_list|(
name|conf
argument_list|)
return|;
block|}
comment|/*      * If there is no "@" symbol in the room, find the chat service JID and      * return fully qualified JID for the room as room@conference.server.domain      */
DECL|method|resolveRoom (XMPPConnection connection)
specifier|public
name|String
name|resolveRoom
parameter_list|(
name|XMPPConnection
name|connection
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|SmackException
throws|,
name|XMPPException
block|{
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|room
argument_list|,
literal|"room"
argument_list|)
expr_stmt|;
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
name|MultiUserChatManager
name|multiUserChatManager
init|=
name|MultiUserChatManager
operator|.
name|getInstanceFor
argument_list|(
name|connection
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DomainBareJid
argument_list|>
name|xmppServiceDomains
init|=
name|multiUserChatManager
operator|.
name|getXMPPServiceDomains
argument_list|()
decl_stmt|;
if|if
condition|(
name|xmppServiceDomains
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|XMPPErrorException
argument_list|(
literal|null
argument_list|,
name|StanzaError
operator|.
name|from
argument_list|(
name|Condition
operator|.
name|item_not_found
argument_list|,
literal|"Cannot find any XMPPServiceDomain by MultiUserChatManager on connection: "
operator|+
name|getConnectionMessage
argument_list|(
name|connection
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|room
operator|+
literal|"@"
operator|+
name|xmppServiceDomains
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
DECL|method|getConnectionDescription ()
specifier|public
name|String
name|getConnectionDescription
parameter_list|()
block|{
return|return
name|host
operator|+
literal|":"
operator|+
name|port
operator|+
literal|"/"
operator|+
name|serviceName
return|;
block|}
DECL|method|getConnectionMessage (XMPPConnection connection)
specifier|public
specifier|static
name|String
name|getConnectionMessage
parameter_list|(
name|XMPPConnection
name|connection
parameter_list|)
block|{
return|return
name|connection
operator|.
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|connection
operator|.
name|getPort
argument_list|()
operator|+
literal|"/"
operator|+
name|connection
operator|.
name|getXMPPServiceDomain
argument_list|()
return|;
block|}
DECL|method|getChatId ()
specifier|public
name|String
name|getChatId
parameter_list|()
block|{
return|return
literal|"Chat:"
operator|+
name|getParticipant
argument_list|()
operator|+
literal|":"
operator|+
name|getUser
argument_list|()
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
argument_list|(
name|headerFilterStrategy
argument_list|)
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
comment|/**      * Hostname for the chat server      */
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
comment|/**      * Port number for the chat server      */
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
comment|/**      * User name (without server name). If not specified, anonymous login will be attempted.      */
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
comment|/**      * Password for login      */
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
comment|/**      * XMPP resource. The default is Camel.      */
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
comment|/**      * Whether to login the user.      */
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
comment|/**      * If true, an attempt to create an account will be made. Default is false.      */
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
comment|/**      * If this option is specified, the component will connect to MUC (Multi User Chat).      * Usually, the domain name for MUC is different from the login domain.      * For example, if you are superman@jabber.org and want to join the krypton room, then the room URL is      * krypton@conference.jabber.org. Note the conference part.      * It is not a requirement to provide the full room JID. If the room parameter does not contain the @ symbol,      * the domain part will be discovered and added by Camel      */
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
comment|// participant is optional so use user if not provided
return|return
name|participant
operator|!=
literal|null
condition|?
name|participant
else|:
name|user
return|;
block|}
comment|/**      * JID (Jabber ID) of person to receive messages. room parameter has precedence over participant.      */
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
comment|/**      * Use nickname when joining room. If room is specified and nickname is not, user will be used for the nickname.      */
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
comment|/**      * The name of the service you are connecting to. For Google Talk, this would be gmail.com.      */
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
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
comment|/**      * To use a custom HeaderFilterStrategy to filter header to and from Camel message.      */
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|getConnectionConfig ()
specifier|public
name|ConnectionConfiguration
name|getConnectionConfig
parameter_list|()
block|{
return|return
name|connectionConfig
return|;
block|}
comment|/**      * To use an existing connection configuration. Currently {@link org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration} is only supported (XMPP over TCP).      */
DECL|method|setConnectionConfig (ConnectionConfiguration connectionConfig)
specifier|public
name|void
name|setConnectionConfig
parameter_list|(
name|ConnectionConfiguration
name|connectionConfig
parameter_list|)
block|{
name|this
operator|.
name|connectionConfig
operator|=
name|connectionConfig
expr_stmt|;
block|}
DECL|method|isTestConnectionOnStartup ()
specifier|public
name|boolean
name|isTestConnectionOnStartup
parameter_list|()
block|{
return|return
name|testConnectionOnStartup
return|;
block|}
comment|/**      * Specifies whether to test the connection on startup. This is used to ensure that the XMPP client has a valid      * connection to the XMPP server when the route starts. Camel throws an exception on startup if a connection      * cannot be established. When this option is set to false, Camel will attempt to establish a "lazy" connection      * when needed by a producer, and will poll for a consumer connection until the connection is established. Default is true.      */
DECL|method|setTestConnectionOnStartup (boolean testConnectionOnStartup)
specifier|public
name|void
name|setTestConnectionOnStartup
parameter_list|(
name|boolean
name|testConnectionOnStartup
parameter_list|)
block|{
name|this
operator|.
name|testConnectionOnStartup
operator|=
name|testConnectionOnStartup
expr_stmt|;
block|}
DECL|method|getConnectionPollDelay ()
specifier|public
name|int
name|getConnectionPollDelay
parameter_list|()
block|{
return|return
name|connectionPollDelay
return|;
block|}
comment|/**      * The amount of time in seconds between polls (in seconds) to verify the health of the XMPP connection, or between attempts      * to establish an initial consumer connection. Camel will try to re-establish a connection if it has become inactive.      * Default is 10 seconds.      */
DECL|method|setConnectionPollDelay (int connectionPollDelay)
specifier|public
name|void
name|setConnectionPollDelay
parameter_list|(
name|int
name|connectionPollDelay
parameter_list|)
block|{
name|this
operator|.
name|connectionPollDelay
operator|=
name|connectionPollDelay
expr_stmt|;
block|}
comment|/**      * Accept pubsub packets on input, default is false      */
DECL|method|setPubsub (boolean pubsub)
specifier|public
name|void
name|setPubsub
parameter_list|(
name|boolean
name|pubsub
parameter_list|)
block|{
name|this
operator|.
name|pubsub
operator|=
name|pubsub
expr_stmt|;
if|if
condition|(
name|pubsub
condition|)
block|{
name|setDoc
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isPubsub ()
specifier|public
name|boolean
name|isPubsub
parameter_list|()
block|{
return|return
name|pubsub
return|;
block|}
comment|/**      * Set a doc header on the IN message containing a Document form of the incoming packet;      * default is true if presence or pubsub are true, otherwise false      */
DECL|method|setDoc (boolean doc)
specifier|public
name|void
name|setDoc
parameter_list|(
name|boolean
name|doc
parameter_list|)
block|{
name|this
operator|.
name|doc
operator|=
name|doc
expr_stmt|;
block|}
DECL|method|isDoc ()
specifier|public
name|boolean
name|isDoc
parameter_list|()
block|{
return|return
name|doc
return|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
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
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
name|connection
operator|=
literal|null
expr_stmt|;
name|binding
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

