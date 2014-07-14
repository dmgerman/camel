begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
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
name|util
operator|.
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_comment
comment|/**  * Represents the configuration data for communicating over email  *  * @version   */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|MailConfiguration
specifier|public
class|class
name|MailConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|javaMailSender
specifier|private
name|JavaMailSender
name|javaMailSender
decl_stmt|;
DECL|field|javaMailProperties
specifier|private
name|Properties
name|javaMailProperties
decl_stmt|;
DECL|field|additionalJavaMailProperties
specifier|private
name|Properties
name|additionalJavaMailProperties
decl_stmt|;
annotation|@
name|UriParam
DECL|field|protocol
specifier|private
name|String
name|protocol
decl_stmt|;
annotation|@
name|UriParam
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriParam
DECL|field|port
specifier|private
name|int
name|port
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
DECL|field|username
specifier|private
name|String
name|username
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
DECL|field|subject
specifier|private
name|String
name|subject
decl_stmt|;
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
annotation|@
name|UriParam
DECL|field|mapMailMessage
specifier|private
name|boolean
name|mapMailMessage
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|from
specifier|private
name|String
name|from
init|=
name|MailConstants
operator|.
name|MAIL_DEFAULT_FROM
decl_stmt|;
annotation|@
name|UriParam
DECL|field|folderName
specifier|private
name|String
name|folderName
init|=
name|MailConstants
operator|.
name|MAIL_DEFAULT_FOLDER
decl_stmt|;
annotation|@
name|UriParam
DECL|field|delete
specifier|private
name|boolean
name|delete
decl_stmt|;
annotation|@
name|UriParam
DECL|field|copyTo
specifier|private
name|String
name|copyTo
decl_stmt|;
annotation|@
name|UriParam
DECL|field|unseen
specifier|private
name|boolean
name|unseen
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|ignoreUriScheme
specifier|private
name|boolean
name|ignoreUriScheme
decl_stmt|;
DECL|field|recipients
specifier|private
name|Map
argument_list|<
name|Message
operator|.
name|RecipientType
argument_list|,
name|String
argument_list|>
name|recipients
init|=
operator|new
name|HashMap
argument_list|<
name|Message
operator|.
name|RecipientType
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
DECL|field|replyTo
specifier|private
name|String
name|replyTo
decl_stmt|;
annotation|@
name|UriParam
DECL|field|fetchSize
specifier|private
name|int
name|fetchSize
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
DECL|field|debugMode
specifier|private
name|boolean
name|debugMode
decl_stmt|;
annotation|@
name|UriParam
DECL|field|connectionTimeout
specifier|private
name|int
name|connectionTimeout
init|=
name|MailConstants
operator|.
name|MAIL_DEFAULT_CONNECTION_TIMEOUT
decl_stmt|;
annotation|@
name|UriParam
DECL|field|dummyTrustManager
specifier|private
name|boolean
name|dummyTrustManager
decl_stmt|;
annotation|@
name|UriParam
DECL|field|contentType
specifier|private
name|String
name|contentType
init|=
literal|"text/plain"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|alternativeBodyHeader
specifier|private
name|String
name|alternativeBodyHeader
init|=
name|MailConstants
operator|.
name|MAIL_ALTERNATIVE_BODY
decl_stmt|;
annotation|@
name|UriParam
DECL|field|useInlineAttachments
specifier|private
name|boolean
name|useInlineAttachments
decl_stmt|;
annotation|@
name|UriParam
DECL|field|ignoreUnsupportedCharset
specifier|private
name|boolean
name|ignoreUnsupportedCharset
decl_stmt|;
annotation|@
name|UriParam
DECL|field|disconnect
specifier|private
name|boolean
name|disconnect
decl_stmt|;
annotation|@
name|UriParam
DECL|field|closeFolder
specifier|private
name|boolean
name|closeFolder
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|peek
specifier|private
name|boolean
name|peek
init|=
literal|true
decl_stmt|;
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
DECL|method|MailConfiguration ()
specifier|public
name|MailConfiguration
parameter_list|()
block|{     }
comment|/**      * Returns a copy of this configuration      */
DECL|method|copy ()
specifier|public
name|MailConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
name|MailConfiguration
name|copy
init|=
operator|(
name|MailConfiguration
operator|)
name|clone
argument_list|()
decl_stmt|;
comment|// must set a new recipients map as clone just reuse the same reference
name|copy
operator|.
name|recipients
operator|=
operator|new
name|HashMap
argument_list|<
name|Message
operator|.
name|RecipientType
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|copy
operator|.
name|recipients
operator|.
name|putAll
argument_list|(
name|this
operator|.
name|recipients
argument_list|)
expr_stmt|;
return|return
name|copy
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
DECL|method|configure (URI uri)
specifier|public
name|void
name|configure
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|String
name|value
init|=
name|uri
operator|.
name|getHost
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|setHost
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isIgnoreUriScheme
argument_list|()
condition|)
block|{
name|String
name|scheme
init|=
name|uri
operator|.
name|getScheme
argument_list|()
decl_stmt|;
if|if
condition|(
name|scheme
operator|!=
literal|null
condition|)
block|{
name|setProtocol
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|userInfo
init|=
name|uri
operator|.
name|getUserInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|userInfo
operator|!=
literal|null
condition|)
block|{
name|setUsername
argument_list|(
name|userInfo
argument_list|)
expr_stmt|;
block|}
name|int
name|port
init|=
name|uri
operator|.
name|getPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|port
operator|>
literal|0
condition|)
block|{
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|port
operator|<=
literal|0
operator|&&
name|this
operator|.
name|port
operator|<=
literal|0
condition|)
block|{
comment|// resolve default port if no port number was provided, and not already configured with a port number
name|setPort
argument_list|(
name|MailUtils
operator|.
name|getDefaultPortForProtocol
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createJavaMailSender ()
specifier|protected
name|JavaMailSender
name|createJavaMailSender
parameter_list|()
block|{
name|JavaMailSender
name|answer
init|=
operator|new
name|DefaultJavaMailSender
argument_list|()
decl_stmt|;
if|if
condition|(
name|javaMailProperties
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setJavaMailProperties
argument_list|(
name|javaMailProperties
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// set default properties if none provided
name|answer
operator|.
name|setJavaMailProperties
argument_list|(
name|createJavaMailProperties
argument_list|()
argument_list|)
expr_stmt|;
comment|// add additional properties if provided
if|if
condition|(
name|additionalJavaMailProperties
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|getJavaMailProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|additionalJavaMailProperties
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|host
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|port
operator|>=
literal|0
condition|)
block|{
name|answer
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|password
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|protocol
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setProtocol
argument_list|(
name|protocol
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use our authenticator that does no live user interaction but returns the already configured username and password
name|Session
name|session
init|=
name|Session
operator|.
name|getInstance
argument_list|(
name|answer
operator|.
name|getJavaMailProperties
argument_list|()
argument_list|,
operator|new
name|DefaultAuthenticator
argument_list|(
name|getUsername
argument_list|()
argument_list|,
name|getPassword
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// sets the debug mode of the underlying mail framework
name|session
operator|.
name|setDebug
argument_list|(
name|debugMode
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createJavaMailProperties ()
specifier|private
name|Properties
name|createJavaMailProperties
parameter_list|()
block|{
comment|// clone the system properties and set the java mail properties
name|Properties
name|properties
init|=
operator|(
name|Properties
operator|)
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|clone
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".connectiontimeout"
argument_list|,
name|connectionTimeout
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".timeout"
argument_list|,
name|connectionTimeout
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".host"
argument_list|,
name|host
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".port"
argument_list|,
literal|""
operator|+
name|port
argument_list|)
expr_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".user"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"mail.user"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".auth"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|properties
operator|.
name|put
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".auth"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
block|}
name|properties
operator|.
name|put
argument_list|(
literal|"mail.transport.protocol"
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"mail.store.protocol"
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"mail.host"
argument_list|,
name|host
argument_list|)
expr_stmt|;
if|if
condition|(
name|debugMode
condition|)
block|{
comment|// add more debug for the SSL communication as well
name|properties
operator|.
name|put
argument_list|(
literal|"javax.net.debug"
argument_list|,
literal|"all"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sslContextParameters
operator|!=
literal|null
operator|&&
name|isSecureProtocol
argument_list|()
condition|)
block|{
name|SSLContext
name|sslContext
decl_stmt|;
try|try
block|{
name|sslContext
operator|=
name|sslContextParameters
operator|.
name|createSSLContext
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
literal|"Error initializing SSLContext."
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|properties
operator|.
name|put
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".socketFactory"
argument_list|,
name|sslContext
operator|.
name|getSocketFactory
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".socketFactory.fallback"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".socketFactory.port"
argument_list|,
literal|""
operator|+
name|port
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dummyTrustManager
operator|&&
name|isSecureProtocol
argument_list|()
condition|)
block|{
comment|// set the custom SSL properties
name|properties
operator|.
name|put
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".socketFactory.class"
argument_list|,
literal|"org.apache.camel.component.mail.security.DummySSLSocketFactory"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".socketFactory.fallback"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"mail."
operator|+
name|protocol
operator|+
literal|".socketFactory.port"
argument_list|,
literal|""
operator|+
name|port
argument_list|)
expr_stmt|;
block|}
return|return
name|properties
return|;
block|}
comment|/**      * Is the used protocol to be secure or not      */
DECL|method|isSecureProtocol ()
specifier|public
name|boolean
name|isSecureProtocol
parameter_list|()
block|{
return|return
name|this
operator|.
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"smtps"
argument_list|)
operator|||
name|this
operator|.
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"pop3s"
argument_list|)
operator|||
name|this
operator|.
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"imaps"
argument_list|)
return|;
block|}
DECL|method|getMailStoreLogInformation ()
specifier|public
name|String
name|getMailStoreLogInformation
parameter_list|()
block|{
name|String
name|ssl
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|isSecureProtocol
argument_list|()
condition|)
block|{
name|ssl
operator|=
literal|" (SSL enabled"
operator|+
operator|(
name|dummyTrustManager
condition|?
literal|" using DummyTrustManager)"
else|:
literal|")"
operator|)
expr_stmt|;
block|}
return|return
name|protocol
operator|+
literal|"://"
operator|+
name|host
operator|+
literal|":"
operator|+
name|port
operator|+
name|ssl
operator|+
literal|", folder="
operator|+
name|folderName
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getJavaMailSender ()
specifier|public
name|JavaMailSender
name|getJavaMailSender
parameter_list|()
block|{
return|return
name|javaMailSender
return|;
block|}
DECL|method|setJavaMailSender (JavaMailSender javaMailSender)
specifier|public
name|void
name|setJavaMailSender
parameter_list|(
name|JavaMailSender
name|javaMailSender
parameter_list|)
block|{
name|this
operator|.
name|javaMailSender
operator|=
name|javaMailSender
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
DECL|method|getJavaMailProperties ()
specifier|public
name|Properties
name|getJavaMailProperties
parameter_list|()
block|{
return|return
name|javaMailProperties
return|;
block|}
comment|/**      * Sets the java mail options. Will clear any default properties and only use the properties      * provided for this method.      */
DECL|method|setJavaMailProperties (Properties javaMailProperties)
specifier|public
name|void
name|setJavaMailProperties
parameter_list|(
name|Properties
name|javaMailProperties
parameter_list|)
block|{
name|this
operator|.
name|javaMailProperties
operator|=
name|javaMailProperties
expr_stmt|;
block|}
DECL|method|getAdditionalJavaMailProperties ()
specifier|public
name|Properties
name|getAdditionalJavaMailProperties
parameter_list|()
block|{
if|if
condition|(
name|additionalJavaMailProperties
operator|==
literal|null
condition|)
block|{
name|additionalJavaMailProperties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
block|}
return|return
name|additionalJavaMailProperties
return|;
block|}
comment|/**      * Sets additional java mail properties, that will append/override any default properties      * that is set based on all the other options. This is useful if you need to add some      * special options but want to keep the others as is.      */
DECL|method|setAdditionalJavaMailProperties (Properties additionalJavaMailProperties)
specifier|public
name|void
name|setAdditionalJavaMailProperties
parameter_list|(
name|Properties
name|additionalJavaMailProperties
parameter_list|)
block|{
name|this
operator|.
name|additionalJavaMailProperties
operator|=
name|additionalJavaMailProperties
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
DECL|method|getSubject ()
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
DECL|method|setSubject (String subject)
specifier|public
name|void
name|setSubject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
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
DECL|method|getProtocol ()
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
DECL|method|setProtocol (String protocol)
specifier|public
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
block|}
DECL|method|getSession ()
specifier|public
name|Session
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
DECL|method|setSession (Session session)
specifier|public
name|void
name|setSession
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
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
if|if
condition|(
name|getRecipients
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// set default destination to username@host for backwards compatibility
comment|// can be overridden by URI parameters
name|String
name|address
init|=
name|username
decl_stmt|;
if|if
condition|(
name|address
operator|.
name|indexOf
argument_list|(
literal|"@"
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|address
operator|+=
literal|"@"
operator|+
name|host
expr_stmt|;
block|}
name|setTo
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getFrom ()
specifier|public
name|String
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
DECL|method|setFrom (String from)
specifier|public
name|void
name|setFrom
parameter_list|(
name|String
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
block|}
DECL|method|isDelete ()
specifier|public
name|boolean
name|isDelete
parameter_list|()
block|{
return|return
name|delete
return|;
block|}
DECL|method|setDelete (boolean delete)
specifier|public
name|void
name|setDelete
parameter_list|(
name|boolean
name|delete
parameter_list|)
block|{
name|this
operator|.
name|delete
operator|=
name|delete
expr_stmt|;
block|}
DECL|method|isMapMailMessage ()
specifier|public
name|boolean
name|isMapMailMessage
parameter_list|()
block|{
return|return
name|mapMailMessage
return|;
block|}
DECL|method|setMapMailMessage (boolean mapMailMessage)
specifier|public
name|void
name|setMapMailMessage
parameter_list|(
name|boolean
name|mapMailMessage
parameter_list|)
block|{
name|this
operator|.
name|mapMailMessage
operator|=
name|mapMailMessage
expr_stmt|;
block|}
DECL|method|getFolderName ()
specifier|public
name|String
name|getFolderName
parameter_list|()
block|{
return|return
name|folderName
return|;
block|}
DECL|method|setFolderName (String folderName)
specifier|public
name|void
name|setFolderName
parameter_list|(
name|String
name|folderName
parameter_list|)
block|{
name|this
operator|.
name|folderName
operator|=
name|folderName
expr_stmt|;
block|}
DECL|method|isIgnoreUriScheme ()
specifier|public
name|boolean
name|isIgnoreUriScheme
parameter_list|()
block|{
return|return
name|ignoreUriScheme
return|;
block|}
DECL|method|setIgnoreUriScheme (boolean ignoreUriScheme)
specifier|public
name|void
name|setIgnoreUriScheme
parameter_list|(
name|boolean
name|ignoreUriScheme
parameter_list|)
block|{
name|this
operator|.
name|ignoreUriScheme
operator|=
name|ignoreUriScheme
expr_stmt|;
block|}
DECL|method|isUnseen ()
specifier|public
name|boolean
name|isUnseen
parameter_list|()
block|{
return|return
name|unseen
return|;
block|}
DECL|method|setUnseen (boolean unseen)
specifier|public
name|void
name|setUnseen
parameter_list|(
name|boolean
name|unseen
parameter_list|)
block|{
name|this
operator|.
name|unseen
operator|=
name|unseen
expr_stmt|;
block|}
comment|/**      * Sets the<tt>To</tt> email address. Separate multiple email addresses with comma.      */
DECL|method|setTo (String address)
specifier|public
name|void
name|setTo
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|recipients
operator|.
name|put
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
argument_list|,
name|address
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the<tt>CC</tt> email address. Separate multiple email addresses with comma.      */
DECL|method|setCC (String address)
specifier|public
name|void
name|setCC
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|recipients
operator|.
name|put
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|CC
argument_list|,
name|address
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the<tt>BCC</tt> email address. Separate multiple email addresses with comma.      */
DECL|method|setBCC (String address)
specifier|public
name|void
name|setBCC
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|recipients
operator|.
name|put
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|BCC
argument_list|,
name|address
argument_list|)
expr_stmt|;
block|}
DECL|method|getRecipients ()
specifier|public
name|Map
argument_list|<
name|Message
operator|.
name|RecipientType
argument_list|,
name|String
argument_list|>
name|getRecipients
parameter_list|()
block|{
return|return
name|recipients
return|;
block|}
DECL|method|getReplyTo ()
specifier|public
name|String
name|getReplyTo
parameter_list|()
block|{
return|return
name|replyTo
return|;
block|}
DECL|method|setReplyTo (String replyTo)
specifier|public
name|void
name|setReplyTo
parameter_list|(
name|String
name|replyTo
parameter_list|)
block|{
name|this
operator|.
name|replyTo
operator|=
name|replyTo
expr_stmt|;
block|}
DECL|method|getFetchSize ()
specifier|public
name|int
name|getFetchSize
parameter_list|()
block|{
return|return
name|fetchSize
return|;
block|}
DECL|method|setFetchSize (int fetchSize)
specifier|public
name|void
name|setFetchSize
parameter_list|(
name|int
name|fetchSize
parameter_list|)
block|{
name|this
operator|.
name|fetchSize
operator|=
name|fetchSize
expr_stmt|;
block|}
DECL|method|isDebugMode ()
specifier|public
name|boolean
name|isDebugMode
parameter_list|()
block|{
return|return
name|debugMode
return|;
block|}
DECL|method|setDebugMode (boolean debugMode)
specifier|public
name|void
name|setDebugMode
parameter_list|(
name|boolean
name|debugMode
parameter_list|)
block|{
name|this
operator|.
name|debugMode
operator|=
name|debugMode
expr_stmt|;
block|}
DECL|method|getConnectionTimeout ()
specifier|public
name|long
name|getConnectionTimeout
parameter_list|()
block|{
return|return
name|connectionTimeout
return|;
block|}
DECL|method|setConnectionTimeout (int connectionTimeout)
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
name|int
name|connectionTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectionTimeout
operator|=
name|connectionTimeout
expr_stmt|;
block|}
DECL|method|isDummyTrustManager ()
specifier|public
name|boolean
name|isDummyTrustManager
parameter_list|()
block|{
return|return
name|dummyTrustManager
return|;
block|}
DECL|method|setDummyTrustManager (boolean dummyTrustManager)
specifier|public
name|void
name|setDummyTrustManager
parameter_list|(
name|boolean
name|dummyTrustManager
parameter_list|)
block|{
name|this
operator|.
name|dummyTrustManager
operator|=
name|dummyTrustManager
expr_stmt|;
block|}
DECL|method|getContentType ()
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
DECL|method|setContentType (String contentType)
specifier|public
name|void
name|setContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|this
operator|.
name|contentType
operator|=
name|contentType
expr_stmt|;
block|}
DECL|method|getAlternativeBodyHeader ()
specifier|public
name|String
name|getAlternativeBodyHeader
parameter_list|()
block|{
return|return
name|alternativeBodyHeader
return|;
block|}
DECL|method|setAlternativeBodyHeader (String alternativeBodyHeader)
specifier|public
name|void
name|setAlternativeBodyHeader
parameter_list|(
name|String
name|alternativeBodyHeader
parameter_list|)
block|{
name|this
operator|.
name|alternativeBodyHeader
operator|=
name|alternativeBodyHeader
expr_stmt|;
block|}
DECL|method|isUseInlineAttachments ()
specifier|public
name|boolean
name|isUseInlineAttachments
parameter_list|()
block|{
return|return
name|useInlineAttachments
return|;
block|}
DECL|method|setUseInlineAttachments (boolean useInlineAttachments)
specifier|public
name|void
name|setUseInlineAttachments
parameter_list|(
name|boolean
name|useInlineAttachments
parameter_list|)
block|{
name|this
operator|.
name|useInlineAttachments
operator|=
name|useInlineAttachments
expr_stmt|;
block|}
DECL|method|isIgnoreUnsupportedCharset ()
specifier|public
name|boolean
name|isIgnoreUnsupportedCharset
parameter_list|()
block|{
return|return
name|ignoreUnsupportedCharset
return|;
block|}
DECL|method|setIgnoreUnsupportedCharset (boolean ignoreUnsupportedCharset)
specifier|public
name|void
name|setIgnoreUnsupportedCharset
parameter_list|(
name|boolean
name|ignoreUnsupportedCharset
parameter_list|)
block|{
name|this
operator|.
name|ignoreUnsupportedCharset
operator|=
name|ignoreUnsupportedCharset
expr_stmt|;
block|}
DECL|method|isDisconnect ()
specifier|public
name|boolean
name|isDisconnect
parameter_list|()
block|{
return|return
name|disconnect
return|;
block|}
DECL|method|setDisconnect (boolean disconnect)
specifier|public
name|void
name|setDisconnect
parameter_list|(
name|boolean
name|disconnect
parameter_list|)
block|{
name|this
operator|.
name|disconnect
operator|=
name|disconnect
expr_stmt|;
block|}
DECL|method|isCloseFolder ()
specifier|public
name|boolean
name|isCloseFolder
parameter_list|()
block|{
return|return
name|closeFolder
return|;
block|}
DECL|method|setCloseFolder (boolean closeFolder)
specifier|public
name|void
name|setCloseFolder
parameter_list|(
name|boolean
name|closeFolder
parameter_list|)
block|{
name|this
operator|.
name|closeFolder
operator|=
name|closeFolder
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
DECL|method|getCopyTo ()
specifier|public
name|String
name|getCopyTo
parameter_list|()
block|{
return|return
name|copyTo
return|;
block|}
DECL|method|setCopyTo (String copyTo)
specifier|public
name|void
name|setCopyTo
parameter_list|(
name|String
name|copyTo
parameter_list|)
block|{
name|this
operator|.
name|copyTo
operator|=
name|copyTo
expr_stmt|;
block|}
DECL|method|isPeek ()
specifier|public
name|boolean
name|isPeek
parameter_list|()
block|{
return|return
name|peek
return|;
block|}
DECL|method|setPeek (boolean peek)
specifier|public
name|void
name|setPeek
parameter_list|(
name|boolean
name|peek
parameter_list|)
block|{
name|this
operator|.
name|peek
operator|=
name|peek
expr_stmt|;
block|}
block|}
end_class

end_unit

