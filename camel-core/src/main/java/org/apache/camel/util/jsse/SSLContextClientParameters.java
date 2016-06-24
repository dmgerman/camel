begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.jsse
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|ArrayList
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
name|SNIHostName
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
name|SNIServerName
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
name|SSLServerSocketFactory
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
comment|/**  * Configuration model for client side JSSE options.  */
end_comment

begin_class
DECL|class|SSLContextClientParameters
specifier|public
class|class
name|SSLContextClientParameters
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
name|SSLContextClientParameters
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|sniHostNames
specifier|private
name|List
argument_list|<
name|SNIServerName
argument_list|>
name|sniHostNames
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|addAllSniHostNames (List<String> sniHostNames)
specifier|public
name|void
name|addAllSniHostNames
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|sniHostNames
parameter_list|)
block|{
for|for
control|(
name|String
name|sniHostName
range|:
name|sniHostNames
control|)
block|{
name|this
operator|.
name|sniHostNames
operator|.
name|add
argument_list|(
operator|new
name|SNIHostName
argument_list|(
name|sniHostName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setSniHostName (String sniHostName)
specifier|public
name|void
name|setSniHostName
parameter_list|(
name|String
name|sniHostName
parameter_list|)
block|{
name|this
operator|.
name|sniHostNames
operator|.
name|add
argument_list|(
operator|new
name|SNIHostName
argument_list|(
name|sniHostName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSNIHostNames ()
specifier|protected
name|List
argument_list|<
name|SNIServerName
argument_list|>
name|getSNIHostNames
parameter_list|()
block|{
return|return
name|sniHostNames
return|;
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
literal|"Configuring client-side SSLContext parameters on SSLContext [{}]..."
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
name|info
argument_list|(
literal|"Configuring client-side SSLContext session timeout on SSLContext [{}] to [{}]."
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
name|getClientSessionContext
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
literal|"Configured client-side SSLContext parameters on SSLContext [{}]."
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      *<p/>      * This implementation returns the empty list as the enabled cipher suites      * and protocols are not client and server side specific in an      * {@code SSLEngine}. Consequently, overriding them here would be a bit odd      * as the client side specific configuration shouldn't really override a      * shared client/server configuration option.      */
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
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
comment|/**      * This class has no bearing on {@code SSLServerSocketFactory} instances and therefore provides no      * configurers for that purpose.      */
annotation|@
name|Override
DECL|method|getSSLServerSocketFactoryConfigurers (SSLContext context)
specifier|protected
name|List
argument_list|<
name|Configurer
argument_list|<
name|SSLServerSocketFactory
argument_list|>
argument_list|>
name|getSSLServerSocketFactoryConfigurers
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
literal|"SSLContextClientParameters[getCipherSuites()="
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

