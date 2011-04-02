begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cometd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cometd
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|util
operator|.
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|builder
operator|.
name|RouteBuilder
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
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|Bayeux
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|Extension
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|RemoveListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|server
operator|.
name|AbstractBayeux
import|;
end_import

begin_class
DECL|class|CometdProducerConsumerInteractiveAuthenticatedMain
specifier|public
class|class
name|CometdProducerConsumerInteractiveAuthenticatedMain
block|{
DECL|field|URI
specifier|private
specifier|static
specifier|final
name|String
name|URI
init|=
literal|"cometd://127.0.0.1:9091/service/test?baseResource=file:./src/test/resources/webapp&"
operator|+
literal|"timeout=240000&interval=0&maxInterval=30000&multiFrameInterval=1500&jsonCommented=true&logLevel=2"
decl_stmt|;
DECL|field|URIS
specifier|private
specifier|static
specifier|final
name|String
name|URIS
init|=
literal|"cometds://127.0.0.1:9443/service/test?baseResource=file:./src/test/resources/webapp&"
operator|+
literal|"timeout=240000&interval=0&maxInterval=30000&multiFrameInterval=1500&jsonCommented=true&logLevel=2"
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|pwd
specifier|private
name|String
name|pwd
init|=
literal|"changeit"
decl_stmt|;
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|CometdProducerConsumerInteractiveAuthenticatedMain
name|me
init|=
operator|new
name|CometdProducerConsumerInteractiveAuthenticatedMain
argument_list|()
decl_stmt|;
name|me
operator|.
name|testCometdProducerConsumerInteractive
argument_list|()
expr_stmt|;
block|}
DECL|method|testCometdProducerConsumerInteractive ()
specifier|public
name|void
name|testCometdProducerConsumerInteractive
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|private
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|CometdComponent
name|component
init|=
operator|(
name|CometdComponent
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"cometds"
argument_list|)
decl_stmt|;
name|component
operator|.
name|setSslPassword
argument_list|(
name|pwd
argument_list|)
expr_stmt|;
name|component
operator|.
name|setSslKeyPassword
argument_list|(
name|pwd
argument_list|)
expr_stmt|;
name|CometdComponent
name|component2
init|=
operator|(
name|CometdComponent
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"cometd"
argument_list|)
decl_stmt|;
name|BayeuxAuthenticator
name|bayeuxAuthenticator
init|=
operator|new
name|BayeuxAuthenticator
argument_list|()
decl_stmt|;
name|component2
operator|.
name|setSecurityPolicy
argument_list|(
name|bayeuxAuthenticator
argument_list|)
expr_stmt|;
name|component2
operator|.
name|addExtension
argument_list|(
name|bayeuxAuthenticator
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"./src/test/resources/jsse/localhost.ks"
argument_list|)
decl_stmt|;
name|URI
name|keyStoreUrl
init|=
name|file
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|component
operator|.
name|setSslKeystore
argument_list|(
name|keyStoreUrl
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"stream:in"
argument_list|)
operator|.
name|to
argument_list|(
name|URI
argument_list|)
operator|.
name|to
argument_list|(
name|URIS
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * Custom SecurityPolicy, see http://cometd.org/documentation/howtos/authentication for details      */
DECL|class|BayeuxAuthenticator
specifier|public
specifier|static
specifier|final
class|class
name|BayeuxAuthenticator
extends|extends
name|AbstractBayeux
operator|.
name|DefaultPolicy
implements|implements
name|Extension
implements|,
name|RemoveListener
block|{
DECL|field|user
specifier|private
name|String
name|user
init|=
literal|"changeit"
decl_stmt|;
DECL|field|pwd
specifier|private
name|String
name|pwd
init|=
literal|"changeit"
decl_stmt|;
annotation|@
name|Override
DECL|method|canHandshake (Message message)
specifier|public
name|boolean
name|canHandshake
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ext
init|=
name|message
operator|.
name|getExt
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|ext
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Be sure the client does not cheat us
name|ext
operator|.
name|remove
argument_list|(
literal|"authenticationData"
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|authentication
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|ext
operator|.
name|get
argument_list|(
literal|"authentication"
argument_list|)
decl_stmt|;
if|if
condition|(
name|authentication
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Object
name|authenticationData
init|=
name|verify
argument_list|(
name|authentication
argument_list|)
decl_stmt|;
if|if
condition|(
name|authenticationData
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Store the authentication result in the message for later processing
name|ext
operator|.
name|put
argument_list|(
literal|"authenticationData"
argument_list|,
name|authenticationData
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|verify (Map<String, Object> authentication)
specifier|private
name|Object
name|verify
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|authentication
parameter_list|)
block|{
if|if
condition|(
operator|!
name|user
operator|.
name|equals
argument_list|(
name|authentication
operator|.
name|get
argument_list|(
literal|"user"
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|pwd
operator|.
name|equals
argument_list|(
name|authentication
operator|.
name|get
argument_list|(
literal|"credentials"
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
literal|"OK"
return|;
block|}
annotation|@
name|Override
DECL|method|sendMeta (Client remote, Message responseMessage)
specifier|public
name|Message
name|sendMeta
parameter_list|(
name|Client
name|remote
parameter_list|,
name|Message
name|responseMessage
parameter_list|)
block|{
if|if
condition|(
name|Bayeux
operator|.
name|META_HANDSHAKE
operator|.
name|equals
argument_list|(
name|responseMessage
operator|.
name|getChannel
argument_list|()
argument_list|)
condition|)
block|{
name|Message
name|requestMessage
init|=
name|responseMessage
operator|.
name|getAssociated
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestExt
init|=
name|requestMessage
operator|.
name|getExt
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestExt
operator|!=
literal|null
operator|&&
name|requestExt
operator|.
name|get
argument_list|(
literal|"authenticationData"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Object
name|authenticationData
init|=
name|requestExt
operator|.
name|get
argument_list|(
literal|"authenticationData"
argument_list|)
decl_stmt|;
comment|// Authentication successful
comment|// Link authentication data to the remote client
comment|// Be notified when the remote client disappears
name|remote
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Authentication failed
comment|// Add extra fields to the response
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseExt
init|=
name|responseMessage
operator|.
name|getExt
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|authentication
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|responseExt
operator|.
name|put
argument_list|(
literal|"authentication"
argument_list|,
name|authentication
argument_list|)
expr_stmt|;
name|authentication
operator|.
name|put
argument_list|(
literal|"failed"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// Tell the client to stop any further attempt to handshake
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|advice
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|advice
operator|.
name|put
argument_list|(
name|Bayeux
operator|.
name|RECONNECT_FIELD
argument_list|,
name|Bayeux
operator|.
name|NONE_RESPONSE
argument_list|)
expr_stmt|;
name|responseMessage
operator|.
name|put
argument_list|(
name|Bayeux
operator|.
name|ADVICE_FIELD
argument_list|,
name|advice
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|responseMessage
return|;
block|}
annotation|@
name|Override
DECL|method|removed (String clientId, boolean timeout)
specifier|public
name|void
name|removed
parameter_list|(
name|String
name|clientId
parameter_list|,
name|boolean
name|timeout
parameter_list|)
block|{
comment|// Remove authentication data
block|}
annotation|@
name|Override
DECL|method|rcv (Client client, Message message)
specifier|public
name|Message
name|rcv
parameter_list|(
name|Client
name|client
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
return|;
block|}
annotation|@
name|Override
DECL|method|rcvMeta (Client client, Message message)
specifier|public
name|Message
name|rcvMeta
parameter_list|(
name|Client
name|client
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
return|;
block|}
annotation|@
name|Override
DECL|method|send (Client client, Message message)
specifier|public
name|Message
name|send
parameter_list|(
name|Client
name|client
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
return|;
block|}
block|}
block|}
end_class

end_unit

