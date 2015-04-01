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
name|io
operator|.
name|UnsupportedEncodingException
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
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
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
name|Arrays
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
name|UriParams
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
name|URISupport
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|jsse
operator|.
name|SSLContextParameters
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
name|SSLDefaultTrustManager
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
name|SSLTrustManager
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
annotation|@
name|UriParams
DECL|class|IrcConfiguration
specifier|public
class|class
name|IrcConfiguration
implements|implements
name|Cloneable
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
name|IrcConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|channels
specifier|private
name|List
argument_list|<
name|IrcChannel
argument_list|>
name|channels
init|=
operator|new
name|ArrayList
argument_list|<
name|IrcChannel
argument_list|>
argument_list|()
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
DECL|field|hostname
specifier|private
name|String
name|hostname
decl_stmt|;
annotation|@
name|UriPath
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|ports
specifier|private
name|int
index|[]
name|ports
init|=
block|{
literal|6667
block|,
literal|6668
block|,
literal|6669
block|}
decl_stmt|;
annotation|@
name|UriParam
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
DECL|field|nickname
specifier|private
name|String
name|nickname
decl_stmt|;
annotation|@
name|UriParam
DECL|field|realname
specifier|private
name|String
name|realname
decl_stmt|;
annotation|@
name|UriParam
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
DECL|field|trustManager
specifier|private
name|SSLTrustManager
name|trustManager
init|=
operator|new
name|SSLDefaultTrustManager
argument_list|()
decl_stmt|;
DECL|field|usingSSL
specifier|private
name|boolean
name|usingSSL
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|persistent
specifier|private
name|boolean
name|persistent
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|colors
specifier|private
name|boolean
name|colors
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|onNick
specifier|private
name|boolean
name|onNick
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|onQuit
specifier|private
name|boolean
name|onQuit
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|onJoin
specifier|private
name|boolean
name|onJoin
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|onKick
specifier|private
name|boolean
name|onKick
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|onMode
specifier|private
name|boolean
name|onMode
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|onPart
specifier|private
name|boolean
name|onPart
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|onReply
specifier|private
name|boolean
name|onReply
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|onTopic
specifier|private
name|boolean
name|onTopic
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|onPrivmsg
specifier|private
name|boolean
name|onPrivmsg
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|autoRejoin
specifier|private
name|boolean
name|autoRejoin
init|=
literal|true
decl_stmt|;
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
DECL|method|IrcConfiguration ()
specifier|public
name|IrcConfiguration
parameter_list|()
block|{     }
DECL|method|IrcConfiguration (String hostname, String nickname, String displayname, List<IrcChannel> channels)
specifier|public
name|IrcConfiguration
parameter_list|(
name|String
name|hostname
parameter_list|,
name|String
name|nickname
parameter_list|,
name|String
name|displayname
parameter_list|,
name|List
argument_list|<
name|IrcChannel
argument_list|>
name|channels
parameter_list|)
block|{
name|this
argument_list|(
name|hostname
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|nickname
argument_list|,
name|displayname
argument_list|,
name|channels
argument_list|)
expr_stmt|;
block|}
DECL|method|IrcConfiguration (String hostname, String username, String password, String nickname, String displayname, List<IrcChannel> channels)
specifier|public
name|IrcConfiguration
parameter_list|(
name|String
name|hostname
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|String
name|nickname
parameter_list|,
name|String
name|displayname
parameter_list|,
name|List
argument_list|<
name|IrcChannel
argument_list|>
name|channels
parameter_list|)
block|{
name|this
operator|.
name|channels
operator|=
name|channels
expr_stmt|;
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
name|this
operator|.
name|nickname
operator|=
name|nickname
expr_stmt|;
name|this
operator|.
name|realname
operator|=
name|displayname
expr_stmt|;
block|}
DECL|method|copy ()
specifier|public
name|IrcConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|IrcConfiguration
operator|)
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
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
block|}
DECL|method|getCacheKey ()
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
name|hostname
operator|+
literal|":"
operator|+
name|nickname
return|;
block|}
comment|/*      * Return space separated list of channel names without pwd      */
DECL|method|getListOfChannels ()
specifier|public
name|String
name|getListOfChannels
parameter_list|()
block|{
name|String
name|retval
init|=
literal|""
decl_stmt|;
for|for
control|(
name|IrcChannel
name|channel
range|:
name|channels
control|)
block|{
name|retval
operator|+=
operator|(
name|retval
operator|.
name|isEmpty
argument_list|()
condition|?
literal|""
else|:
literal|" "
operator|)
operator|+
name|channel
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
DECL|method|configure (String uriStr)
specifier|public
name|void
name|configure
parameter_list|(
name|String
name|uriStr
parameter_list|)
throws|throws
name|URISyntaxException
throws|,
name|UnsupportedEncodingException
block|{
comment|// fix provided URI and handle that we can use # to indicate the IRC room
if|if
condition|(
name|uriStr
operator|.
name|startsWith
argument_list|(
literal|"ircs"
argument_list|)
condition|)
block|{
name|setUsingSSL
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|uriStr
operator|.
name|startsWith
argument_list|(
literal|"ircs://"
argument_list|)
condition|)
block|{
name|uriStr
operator|=
name|uriStr
operator|.
name|replace
argument_list|(
literal|"ircs:"
argument_list|,
literal|"ircs://"
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|uriStr
operator|.
name|startsWith
argument_list|(
literal|"irc://"
argument_list|)
condition|)
block|{
name|uriStr
operator|=
name|uriStr
operator|.
name|replace
argument_list|(
literal|"irc:"
argument_list|,
literal|"irc://"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|uriStr
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|uriStr
operator|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|uriStr
argument_list|,
literal|"?"
argument_list|)
expr_stmt|;
block|}
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|uriStr
argument_list|)
decl_stmt|;
comment|// Because we can get a "sanitized" URI, we need to deal with the situation where the
comment|// user info includes the username and password together or else we get a mangled username
comment|// that includes the user's secret being sent to the server.
name|String
name|userInfo
init|=
name|uri
operator|.
name|getUserInfo
argument_list|()
decl_stmt|;
name|String
name|username
init|=
literal|null
decl_stmt|;
name|String
name|password
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|userInfo
operator|!=
literal|null
condition|)
block|{
name|int
name|colonIndex
init|=
name|userInfo
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|colonIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|username
operator|=
name|userInfo
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|colonIndex
argument_list|)
expr_stmt|;
name|password
operator|=
name|userInfo
operator|.
name|substring
argument_list|(
name|colonIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|username
operator|=
name|userInfo
expr_stmt|;
block|}
block|}
if|if
condition|(
name|uri
operator|.
name|getPort
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|setPorts
argument_list|(
operator|new
name|int
index|[]
block|{
name|uri
operator|.
name|getPort
argument_list|()
block|}
argument_list|)
expr_stmt|;
name|setPort
argument_list|(
name|uri
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setNickname
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|setRealname
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|setHostname
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|path
init|=
name|uri
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
operator|!
name|path
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Channel {} should not be specified in the URI path. Use an @channel query parameter instead."
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setChannel (String channel)
specifier|public
name|void
name|setChannel
parameter_list|(
name|String
name|channel
parameter_list|)
block|{
name|channels
operator|.
name|add
argument_list|(
name|createChannel
argument_list|(
name|channel
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setChannel (List<String> channels)
specifier|public
name|void
name|setChannel
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|channels
parameter_list|)
block|{
for|for
control|(
name|String
name|ci
range|:
name|channels
control|)
block|{
name|this
operator|.
name|channels
operator|.
name|add
argument_list|(
name|createChannel
argument_list|(
name|ci
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getChannels ()
specifier|public
name|List
argument_list|<
name|IrcChannel
argument_list|>
name|getChannels
parameter_list|()
block|{
return|return
name|channels
return|;
block|}
DECL|method|findChannel (String name)
specifier|public
name|IrcChannel
name|findChannel
parameter_list|(
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|IrcChannel
name|channel
range|:
name|channels
control|)
block|{
if|if
condition|(
name|channel
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|channel
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|setTrustManager (SSLTrustManager trustManager)
specifier|public
name|void
name|setTrustManager
parameter_list|(
name|SSLTrustManager
name|trustManager
parameter_list|)
block|{
name|this
operator|.
name|trustManager
operator|=
name|trustManager
expr_stmt|;
block|}
DECL|method|getTrustManager ()
specifier|public
name|SSLTrustManager
name|getTrustManager
parameter_list|()
block|{
return|return
name|trustManager
return|;
block|}
DECL|method|getUsingSSL ()
specifier|public
name|boolean
name|getUsingSSL
parameter_list|()
block|{
return|return
name|usingSSL
return|;
block|}
DECL|method|setUsingSSL (boolean usingSSL)
specifier|private
name|void
name|setUsingSSL
parameter_list|(
name|boolean
name|usingSSL
parameter_list|)
block|{
name|this
operator|.
name|usingSSL
operator|=
name|usingSSL
expr_stmt|;
block|}
DECL|method|getHostname ()
specifier|public
name|String
name|getHostname
parameter_list|()
block|{
return|return
name|hostname
return|;
block|}
DECL|method|setHostname (String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
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
DECL|method|getNickname ()
specifier|public
name|String
name|getNickname
parameter_list|()
block|{
return|return
name|nickname
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
DECL|method|getRealname ()
specifier|public
name|String
name|getRealname
parameter_list|()
block|{
return|return
name|realname
return|;
block|}
DECL|method|setRealname (String realname)
specifier|public
name|void
name|setRealname
parameter_list|(
name|String
name|realname
parameter_list|)
block|{
name|this
operator|.
name|realname
operator|=
name|realname
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPorts ()
specifier|public
name|int
index|[]
name|getPorts
parameter_list|()
block|{
return|return
name|ports
return|;
block|}
DECL|method|setPorts (int[] ports)
specifier|public
name|void
name|setPorts
parameter_list|(
name|int
index|[]
name|ports
parameter_list|)
block|{
name|this
operator|.
name|ports
operator|=
name|ports
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
DECL|method|isPersistent ()
specifier|public
name|boolean
name|isPersistent
parameter_list|()
block|{
return|return
name|persistent
return|;
block|}
DECL|method|setPersistent (boolean persistent)
specifier|public
name|void
name|setPersistent
parameter_list|(
name|boolean
name|persistent
parameter_list|)
block|{
name|this
operator|.
name|persistent
operator|=
name|persistent
expr_stmt|;
block|}
DECL|method|isColors ()
specifier|public
name|boolean
name|isColors
parameter_list|()
block|{
return|return
name|colors
return|;
block|}
DECL|method|setColors (boolean colors)
specifier|public
name|void
name|setColors
parameter_list|(
name|boolean
name|colors
parameter_list|)
block|{
name|this
operator|.
name|colors
operator|=
name|colors
expr_stmt|;
block|}
DECL|method|isOnNick ()
specifier|public
name|boolean
name|isOnNick
parameter_list|()
block|{
return|return
name|onNick
return|;
block|}
DECL|method|setOnNick (boolean onNick)
specifier|public
name|void
name|setOnNick
parameter_list|(
name|boolean
name|onNick
parameter_list|)
block|{
name|this
operator|.
name|onNick
operator|=
name|onNick
expr_stmt|;
block|}
DECL|method|isOnQuit ()
specifier|public
name|boolean
name|isOnQuit
parameter_list|()
block|{
return|return
name|onQuit
return|;
block|}
DECL|method|setOnQuit (boolean onQuit)
specifier|public
name|void
name|setOnQuit
parameter_list|(
name|boolean
name|onQuit
parameter_list|)
block|{
name|this
operator|.
name|onQuit
operator|=
name|onQuit
expr_stmt|;
block|}
DECL|method|isOnJoin ()
specifier|public
name|boolean
name|isOnJoin
parameter_list|()
block|{
return|return
name|onJoin
return|;
block|}
DECL|method|setOnJoin (boolean onJoin)
specifier|public
name|void
name|setOnJoin
parameter_list|(
name|boolean
name|onJoin
parameter_list|)
block|{
name|this
operator|.
name|onJoin
operator|=
name|onJoin
expr_stmt|;
block|}
DECL|method|isOnKick ()
specifier|public
name|boolean
name|isOnKick
parameter_list|()
block|{
return|return
name|onKick
return|;
block|}
DECL|method|setOnKick (boolean onKick)
specifier|public
name|void
name|setOnKick
parameter_list|(
name|boolean
name|onKick
parameter_list|)
block|{
name|this
operator|.
name|onKick
operator|=
name|onKick
expr_stmt|;
block|}
DECL|method|isOnMode ()
specifier|public
name|boolean
name|isOnMode
parameter_list|()
block|{
return|return
name|onMode
return|;
block|}
DECL|method|setOnMode (boolean onMode)
specifier|public
name|void
name|setOnMode
parameter_list|(
name|boolean
name|onMode
parameter_list|)
block|{
name|this
operator|.
name|onMode
operator|=
name|onMode
expr_stmt|;
block|}
DECL|method|isOnPart ()
specifier|public
name|boolean
name|isOnPart
parameter_list|()
block|{
return|return
name|onPart
return|;
block|}
DECL|method|setOnPart (boolean onPart)
specifier|public
name|void
name|setOnPart
parameter_list|(
name|boolean
name|onPart
parameter_list|)
block|{
name|this
operator|.
name|onPart
operator|=
name|onPart
expr_stmt|;
block|}
DECL|method|isOnReply ()
specifier|public
name|boolean
name|isOnReply
parameter_list|()
block|{
return|return
name|onReply
return|;
block|}
DECL|method|setOnReply (boolean onReply)
specifier|public
name|void
name|setOnReply
parameter_list|(
name|boolean
name|onReply
parameter_list|)
block|{
name|this
operator|.
name|onReply
operator|=
name|onReply
expr_stmt|;
block|}
DECL|method|isOnTopic ()
specifier|public
name|boolean
name|isOnTopic
parameter_list|()
block|{
return|return
name|onTopic
return|;
block|}
DECL|method|setOnTopic (boolean onTopic)
specifier|public
name|void
name|setOnTopic
parameter_list|(
name|boolean
name|onTopic
parameter_list|)
block|{
name|this
operator|.
name|onTopic
operator|=
name|onTopic
expr_stmt|;
block|}
DECL|method|isOnPrivmsg ()
specifier|public
name|boolean
name|isOnPrivmsg
parameter_list|()
block|{
return|return
name|onPrivmsg
return|;
block|}
DECL|method|setOnPrivmsg (boolean onPrivmsg)
specifier|public
name|void
name|setOnPrivmsg
parameter_list|(
name|boolean
name|onPrivmsg
parameter_list|)
block|{
name|this
operator|.
name|onPrivmsg
operator|=
name|onPrivmsg
expr_stmt|;
block|}
DECL|method|isAutoRejoin ()
specifier|public
name|boolean
name|isAutoRejoin
parameter_list|()
block|{
return|return
name|autoRejoin
return|;
block|}
DECL|method|setAutoRejoin (boolean autoRejoin)
specifier|public
name|void
name|setAutoRejoin
parameter_list|(
name|boolean
name|autoRejoin
parameter_list|)
block|{
name|this
operator|.
name|autoRejoin
operator|=
name|autoRejoin
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"IrcConfiguration[hostname: "
operator|+
name|hostname
operator|+
literal|", ports="
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|ports
argument_list|)
operator|+
literal|", username="
operator|+
name|username
operator|+
literal|"]"
return|;
block|}
DECL|method|createChannel (String channelInfo)
specifier|private
specifier|static
name|IrcChannel
name|createChannel
parameter_list|(
name|String
name|channelInfo
parameter_list|)
block|{
name|String
index|[]
name|pair
init|=
name|channelInfo
operator|.
name|split
argument_list|(
literal|"!"
argument_list|)
decl_stmt|;
return|return
operator|new
name|IrcChannel
argument_list|(
name|pair
index|[
literal|0
index|]
argument_list|,
name|pair
operator|.
name|length
operator|>
literal|1
condition|?
name|pair
index|[
literal|1
index|]
else|:
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|sanitize (String uri)
specifier|public
specifier|static
name|String
name|sanitize
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
comment|// may be removed in camel-3.0.0
comment|// make sure it's an URL first
name|int
name|colon
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|colon
operator|!=
operator|-
literal|1
operator|&&
name|uri
operator|.
name|indexOf
argument_list|(
literal|"://"
argument_list|)
operator|!=
name|colon
condition|)
block|{
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|colon
argument_list|)
operator|+
literal|"://"
operator|+
name|uri
operator|.
name|substring
argument_list|(
name|colon
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|uri
argument_list|)
argument_list|)
decl_stmt|;
name|String
index|[]
name|userInfo
init|=
name|u
operator|.
name|getUserInfo
argument_list|()
operator|!=
literal|null
condition|?
name|u
operator|.
name|getUserInfo
argument_list|()
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
else|:
literal|null
decl_stmt|;
name|String
name|username
init|=
name|userInfo
operator|!=
literal|null
condition|?
name|userInfo
index|[
literal|0
index|]
else|:
literal|null
decl_stmt|;
name|String
name|password
init|=
name|userInfo
operator|!=
literal|null
operator|&&
name|userInfo
operator|.
name|length
operator|>
literal|1
condition|?
name|userInfo
index|[
literal|1
index|]
else|:
literal|null
decl_stmt|;
name|String
name|path
init|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|u
operator|.
name|getPath
argument_list|()
operator|!=
literal|null
condition|?
name|u
operator|.
name|getPath
argument_list|()
else|:
literal|""
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
operator|&&
operator|!
name|path
operator|.
name|startsWith
argument_list|(
literal|"##"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|u
argument_list|)
decl_stmt|;
name|String
name|user
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"username"
argument_list|)
decl_stmt|;
name|String
name|nick
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"nickname"
argument_list|)
decl_stmt|;
comment|// not specified in authority
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|username
operator|==
literal|null
condition|)
block|{
name|username
operator|=
name|user
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|username
operator|.
name|equals
argument_list|(
name|user
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Username specified twice in endpoint URI with different values. "
operator|+
literal|"The userInfo value ('{}') will be used, paramter ('{}') ignored"
argument_list|,
name|username
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
name|parameters
operator|.
name|remove
argument_list|(
literal|"username"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|nick
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|username
operator|==
literal|null
condition|)
block|{
name|username
operator|=
name|nick
expr_stmt|;
block|}
if|if
condition|(
name|username
operator|.
name|equals
argument_list|(
name|nick
argument_list|)
condition|)
block|{
name|parameters
operator|.
name|remove
argument_list|(
literal|"nickname"
argument_list|)
expr_stmt|;
comment|// redundant
block|}
block|}
if|if
condition|(
name|username
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"IrcEndpoint URI with no user/nick specified is invalid"
argument_list|)
throw|;
block|}
name|String
name|pwd
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"password"
argument_list|)
decl_stmt|;
if|if
condition|(
name|pwd
operator|!=
literal|null
condition|)
block|{
name|password
operator|=
name|pwd
expr_stmt|;
name|parameters
operator|.
name|remove
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
block|}
comment|// Remove unneeded '#' channel prefixes per convention
comment|// and replace ',' separators and merge channel and key using convention "channel!key"
name|List
argument_list|<
name|String
argument_list|>
name|cl
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|channels
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"channels"
argument_list|)
decl_stmt|;
name|String
name|keys
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"keys"
argument_list|)
decl_stmt|;
name|keys
operator|=
name|keys
operator|==
literal|null
condition|?
name|keys
else|:
name|keys
operator|+
literal|" "
expr_stmt|;
comment|// if @keys ends with a ',' it will miss the last empty key after split(",")
if|if
condition|(
name|channels
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|chs
init|=
name|channels
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|String
index|[]
name|ks
init|=
name|keys
operator|!=
literal|null
condition|?
name|keys
operator|.
name|split
argument_list|(
literal|","
argument_list|)
else|:
literal|null
decl_stmt|;
name|parameters
operator|.
name|remove
argument_list|(
literal|"channels"
argument_list|)
expr_stmt|;
name|int
name|count
init|=
name|chs
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|ks
operator|!=
literal|null
condition|)
block|{
name|parameters
operator|.
name|remove
argument_list|(
literal|"keys"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|path
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Specifying a channel '{}' in the URI path is ambiguous"
operator|+
literal|" when @channels and @keys are provided and will be ignored"
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|path
operator|=
literal|""
expr_stmt|;
block|}
if|if
condition|(
name|ks
operator|.
name|length
operator|!=
name|chs
operator|.
name|length
condition|)
block|{
name|count
operator|=
name|count
operator|<
name|ks
operator|.
name|length
condition|?
name|count
else|:
name|ks
operator|.
name|length
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"Different count of @channels and @keys. Only the first {} are used."
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|String
name|channel
init|=
name|chs
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|ks
operator|!=
literal|null
condition|?
name|ks
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|channel
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
operator|&&
operator|!
name|channel
operator|.
name|startsWith
argument_list|(
literal|"##"
argument_list|)
condition|)
block|{
name|channel
operator|=
name|channel
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
operator|!
name|key
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|channel
operator|+=
literal|"!"
operator|+
name|key
expr_stmt|;
block|}
name|cl
operator|.
name|add
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|path
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No channel specified for the irc endpoint"
argument_list|)
expr_stmt|;
block|}
name|cl
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
name|parameters
operator|.
name|put
argument_list|(
literal|"channel"
argument_list|,
name|cl
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|u
operator|.
name|getScheme
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"://"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|password
operator|==
literal|null
condition|?
literal|""
else|:
literal|":"
operator|+
name|password
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"@"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|u
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|u
operator|.
name|getPort
argument_list|()
operator|==
operator|-
literal|1
condition|?
literal|""
else|:
literal|":"
operator|+
name|u
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
comment|// ignore the path we have it as a @channel now
name|String
name|query
init|=
name|formatQuery
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|query
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"?"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
comment|// make things a bit more predictable
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
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
block|}
DECL|method|formatQuery (Map<String, Object> params)
specifier|private
specifier|static
name|String
name|formatQuery
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
parameter_list|)
block|{
if|if
condition|(
name|params
operator|==
literal|null
operator|||
name|params
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|""
return|;
block|}
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|pair
range|:
name|params
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|pair
operator|.
name|getValue
argument_list|()
decl_stmt|;
comment|// the value may be a list since the same key has multiple values
if|if
condition|(
name|value
operator|instanceof
name|List
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|value
decl_stmt|;
for|for
control|(
name|Object
name|s
range|:
name|list
control|)
block|{
name|addQueryParameter
argument_list|(
name|result
argument_list|,
name|pair
operator|.
name|getKey
argument_list|()
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|addQueryParameter
argument_list|(
name|result
argument_list|,
name|pair
operator|.
name|getKey
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|addQueryParameter (StringBuilder sb, String key, Object value)
specifier|private
specifier|static
name|void
name|addQueryParameter
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|sb
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|?
literal|""
else|:
literal|"&"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|String
name|s
init|=
name|value
operator|.
name|toString
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|s
operator|.
name|isEmpty
argument_list|()
condition|?
literal|""
else|:
literal|"="
operator|+
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

