begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.irc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|irc
package|;
end_package

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
name|RuntimeCamelException
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
name|DefaultComponent
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
name|UnsafeUriCharactersEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|schwering
operator|.
name|irc
operator|.
name|lib
operator|.
name|IRCConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|schwering
operator|.
name|irc
operator|.
name|lib
operator|.
name|IRCEventListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|schwering
operator|.
name|irc
operator|.
name|lib
operator|.
name|ssl
operator|.
name|SSLIRCConnection
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
comment|/**  * Defines the<a href="http://camel.apache.org/irc.html">IRC Component</a>  *  * @version   */
end_comment

begin_class
DECL|class|IrcComponent
specifier|public
class|class
name|IrcComponent
extends|extends
name|DefaultComponent
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
name|IrcComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|connectionCache
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|IRCConnection
argument_list|>
name|connectionCache
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|IRCConnection
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|public
name|IrcEndpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// every endpoint gets it's own configuration
name|IrcConfiguration
name|config
init|=
operator|new
name|IrcConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|configure
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|IrcEndpoint
name|endpoint
init|=
operator|new
name|IrcEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getIRCConnection (IrcConfiguration configuration)
specifier|public
specifier|synchronized
name|IRCConnection
name|getIRCConnection
parameter_list|(
name|IrcConfiguration
name|configuration
parameter_list|)
block|{
specifier|final
name|IRCConnection
name|connection
decl_stmt|;
if|if
condition|(
name|connectionCache
operator|.
name|containsKey
argument_list|(
name|configuration
operator|.
name|getCacheKey
argument_list|()
argument_list|)
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
literal|"Returning Cached Connection to {}:{}"
argument_list|,
name|configuration
operator|.
name|getHostname
argument_list|()
argument_list|,
name|configuration
operator|.
name|getNickname
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|connection
operator|=
name|connectionCache
operator|.
name|get
argument_list|(
name|configuration
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|connection
operator|=
name|createConnection
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|connectionCache
operator|.
name|put
argument_list|(
name|configuration
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|connection
argument_list|)
expr_stmt|;
block|}
return|return
name|connection
return|;
block|}
DECL|method|createConnection (IrcConfiguration configuration)
specifier|protected
name|IRCConnection
name|createConnection
parameter_list|(
name|IrcConfiguration
name|configuration
parameter_list|)
block|{
name|IRCConnection
name|conn
init|=
literal|null
decl_stmt|;
name|IRCEventListener
name|ircLogger
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getUsingSSL
argument_list|()
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
literal|"Creating SSL Connection to {} destination(s): {} nick: {} user: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|configuration
operator|.
name|getHostname
argument_list|()
block|,
name|configuration
operator|.
name|getListOfChannels
argument_list|()
block|,
name|configuration
operator|.
name|getNickname
argument_list|()
block|,
name|configuration
operator|.
name|getUsername
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
name|SSLIRCConnection
name|sconn
init|=
operator|new
name|SSLIRCConnection
argument_list|(
name|configuration
operator|.
name|getHostname
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPorts
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|,
name|configuration
operator|.
name|getNickname
argument_list|()
argument_list|,
name|configuration
operator|.
name|getUsername
argument_list|()
argument_list|,
name|configuration
operator|.
name|getRealname
argument_list|()
argument_list|)
decl_stmt|;
name|sconn
operator|.
name|addTrustManager
argument_list|(
name|configuration
operator|.
name|getTrustManager
argument_list|()
argument_list|)
expr_stmt|;
name|conn
operator|=
name|sconn
expr_stmt|;
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
literal|"Creating Connection to {} destination(s): {} nick: {} user: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|configuration
operator|.
name|getHostname
argument_list|()
block|,
name|configuration
operator|.
name|getListOfChannels
argument_list|()
block|,
name|configuration
operator|.
name|getNickname
argument_list|()
block|,
name|configuration
operator|.
name|getUsername
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
name|conn
operator|=
operator|new
name|IRCConnection
argument_list|(
name|configuration
operator|.
name|getHostname
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPorts
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPassword
argument_list|()
argument_list|,
name|configuration
operator|.
name|getNickname
argument_list|()
argument_list|,
name|configuration
operator|.
name|getUsername
argument_list|()
argument_list|,
name|configuration
operator|.
name|getRealname
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|conn
operator|.
name|setEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setColors
argument_list|(
name|configuration
operator|.
name|isColors
argument_list|()
argument_list|)
expr_stmt|;
name|conn
operator|.
name|setPong
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
literal|"Adding IRC event logging listener"
argument_list|)
expr_stmt|;
name|ircLogger
operator|=
name|createIrcLogger
argument_list|(
name|configuration
operator|.
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
name|conn
operator|.
name|addIRCEventListener
argument_list|(
name|ircLogger
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|conn
operator|.
name|connect
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
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|conn
return|;
block|}
DECL|method|closeConnection (String key, IRCConnection connection)
specifier|public
name|void
name|closeConnection
parameter_list|(
name|String
name|key
parameter_list|,
name|IRCConnection
name|connection
parameter_list|)
block|{
try|try
block|{
name|connection
operator|.
name|doQuit
argument_list|()
expr_stmt|;
name|connection
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
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error during closing connection."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
comment|// lets use a copy so we can clear the connections eagerly in case of exceptions
name|Map
argument_list|<
name|String
argument_list|,
name|IRCConnection
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|IRCConnection
argument_list|>
argument_list|(
name|connectionCache
argument_list|)
decl_stmt|;
name|connectionCache
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|IRCConnection
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|closeConnection
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|createIrcLogger (String hostname)
specifier|protected
name|IRCEventListener
name|createIrcLogger
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
return|return
operator|new
name|IrcLogger
argument_list|(
name|LOG
argument_list|,
name|hostname
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|preProcessUri (String uri)
specifier|protected
name|String
name|preProcessUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|IrcConfiguration
operator|.
name|sanitize
argument_list|(
name|uri
argument_list|)
return|;
block|}
block|}
end_class

end_unit

