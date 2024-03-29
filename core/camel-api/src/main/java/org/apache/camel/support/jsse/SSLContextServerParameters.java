begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.jsse
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|jsse
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLEngine
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
name|SSLServerSocket
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
name|SSLSocketFactory
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
DECL|class|SSLContextServerParameters
specifier|public
class|class
name|SSLContextServerParameters
extends|extends
name|BaseSSLContextParameters
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
name|SSLContextServerParameters
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The optional configuration options for server-side client-authentication requirements.      */
DECL|field|clientAuthentication
specifier|protected
name|String
name|clientAuthentication
decl_stmt|;
comment|/**      * @see #setClientAuthentication(String)      */
DECL|method|getClientAuthentication ()
specifier|public
name|String
name|getClientAuthentication
parameter_list|()
block|{
return|return
name|clientAuthentication
return|;
block|}
comment|/**      * Sets the configuration options for server-side client-authentication requirements.      * The value must be one of NONE, WANT, REQUIRE as defined in {@link ClientAuthentication}.      *       * @param value the desired configuration options or {@code null} to use the defaults      */
DECL|method|setClientAuthentication (String value)
specifier|public
name|void
name|setClientAuthentication
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|clientAuthentication
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getAllowPassthrough ()
specifier|protected
name|boolean
name|getAllowPassthrough
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|configureSSLContext (SSLContext context)
specifier|protected
name|void
name|configureSSLContext
parameter_list|(
name|SSLContext
name|context
parameter_list|)
throws|throws
name|GeneralSecurityException
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Configuring server-side SSLContext parameters on SSLContext [{}]..."
argument_list|,
name|context
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|getSessionTimeout
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Configuring server-side SSLContext session timeout on SSLContext [{}] to [{}]."
argument_list|,
name|context
argument_list|,
name|this
operator|.
name|getSessionTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|configureSessionContext
argument_list|(
name|context
operator|.
name|getServerSessionContext
argument_list|()
argument_list|,
name|this
operator|.
name|getSessionTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Configured server-side SSLContext parameters on SSLContext [{}]."
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      *<p/>      * This implementation allows for configuration of the need and want settings      * for client authentication, but ignores the enabled cipher suites      * and protocols as they are not client and server side specific in an      * {@code SSLEngine}. Consequently, overriding them here would be a bit odd      * as the server side specific configuration shouldn't really override a      * shared client/server configuration option.      */
annotation|@
name|Override
DECL|method|getSSLEngineConfigurers (SSLContext context)
specifier|protected
name|List
argument_list|<
name|Configurer
argument_list|<
name|SSLEngine
argument_list|>
argument_list|>
name|getSSLEngineConfigurers
parameter_list|(
name|SSLContext
name|context
parameter_list|)
block|{
comment|// NOTE: if the super class gets additional shared configuration options beyond
comment|// cipher suites and protocols, this method needs to address that.
comment|// As is, we do NOT pass the configurers along for those two settings.
name|List
argument_list|<
name|Configurer
argument_list|<
name|SSLEngine
argument_list|>
argument_list|>
name|sslEngineConfigurers
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|getClientAuthentication
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|ClientAuthentication
name|clientAuthValue
init|=
name|ClientAuthentication
operator|.
name|valueOf
argument_list|(
name|this
operator|.
name|parsePropertyValue
argument_list|(
name|this
operator|.
name|getClientAuthentication
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Configurer
argument_list|<
name|SSLEngine
argument_list|>
name|sslEngineConfigurer
init|=
operator|new
name|Configurer
argument_list|<
name|SSLEngine
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SSLEngine
name|configure
parameter_list|(
name|SSLEngine
name|engine
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Configuring client-auth on SSLEngine [{}] to [{}]."
argument_list|,
name|engine
argument_list|,
name|clientAuthValue
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|clientAuthValue
condition|)
block|{
case|case
name|NONE
case|:
name|engine
operator|.
name|setWantClientAuth
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setNeedClientAuth
argument_list|(
literal|false
argument_list|)
expr_stmt|;
break|break;
case|case
name|WANT
case|:
name|engine
operator|.
name|setWantClientAuth
argument_list|(
literal|true
argument_list|)
expr_stmt|;
break|break;
case|case
name|REQUIRE
case|:
name|engine
operator|.
name|setNeedClientAuth
argument_list|(
literal|true
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unknown ClientAuthentication value: "
operator|+
name|clientAuthValue
argument_list|)
throw|;
block|}
return|return
name|engine
return|;
block|}
block|}
decl_stmt|;
name|sslEngineConfigurers
operator|.
name|add
argument_list|(
name|sslEngineConfigurer
argument_list|)
expr_stmt|;
block|}
return|return
name|sslEngineConfigurers
return|;
block|}
annotation|@
name|Override
DECL|method|getSSLServerSocketFactorySSLServerSocketConfigurers (SSLContext context)
specifier|protected
name|List
argument_list|<
name|Configurer
argument_list|<
name|SSLServerSocket
argument_list|>
argument_list|>
name|getSSLServerSocketFactorySSLServerSocketConfigurers
parameter_list|(
name|SSLContext
name|context
parameter_list|)
block|{
name|List
argument_list|<
name|Configurer
argument_list|<
name|SSLServerSocket
argument_list|>
argument_list|>
name|sslServerSocketConfigurers
init|=
name|super
operator|.
name|getSSLServerSocketFactorySSLServerSocketConfigurers
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|getClientAuthentication
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|ClientAuthentication
name|clientAuthValue
init|=
name|ClientAuthentication
operator|.
name|valueOf
argument_list|(
name|this
operator|.
name|parsePropertyValue
argument_list|(
name|this
operator|.
name|getClientAuthentication
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Configurer
argument_list|<
name|SSLServerSocket
argument_list|>
name|sslServerSocketConfigurer
init|=
operator|new
name|Configurer
argument_list|<
name|SSLServerSocket
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SSLServerSocket
name|configure
parameter_list|(
name|SSLServerSocket
name|socket
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Configuring client-auth on SSLServerSocket [{}] to [{}]."
argument_list|,
name|socket
argument_list|,
name|clientAuthValue
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|clientAuthValue
condition|)
block|{
case|case
name|NONE
case|:
name|socket
operator|.
name|setWantClientAuth
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|socket
operator|.
name|setNeedClientAuth
argument_list|(
literal|false
argument_list|)
expr_stmt|;
break|break;
case|case
name|WANT
case|:
name|socket
operator|.
name|setWantClientAuth
argument_list|(
literal|true
argument_list|)
expr_stmt|;
break|break;
case|case
name|REQUIRE
case|:
name|socket
operator|.
name|setNeedClientAuth
argument_list|(
literal|true
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unknown ClientAuthentication value: "
operator|+
name|clientAuthValue
argument_list|)
throw|;
block|}
return|return
name|socket
return|;
block|}
block|}
decl_stmt|;
name|sslServerSocketConfigurers
operator|.
name|add
argument_list|(
name|sslServerSocketConfigurer
argument_list|)
expr_stmt|;
block|}
return|return
name|sslServerSocketConfigurers
return|;
block|}
comment|/**      * This class has no bearing on {@code SSLSocketFactory} instances and therefore provides no      * configurers for that purpose.      */
annotation|@
name|Override
DECL|method|getSSLSocketFactoryConfigurers (SSLContext context)
specifier|protected
name|List
argument_list|<
name|Configurer
argument_list|<
name|SSLSocketFactory
argument_list|>
argument_list|>
name|getSSLSocketFactoryConfigurers
parameter_list|(
name|SSLContext
name|context
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"SSLContextServerParameters[clientAuthentication="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|clientAuthentication
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", getCipherSuites()="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|getCipherSuites
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", getCipherSuitesFilter()="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|getCipherSuitesFilter
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", getSecureSocketProtocols()="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|getSecureSocketProtocols
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", getSecureSocketProtocolsFilter()="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|getSecureSocketProtocolsFilter
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", getSessionTimeout()="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|getSessionTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

