begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stomp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stomp
package|;
end_package

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
name|activemq
operator|.
name|broker
operator|.
name|BrokerService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|broker
operator|.
name|SslContext
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
name|JndiRegistry
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
name|Registry
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
name|SimpleRegistry
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
name|jsse
operator|.
name|KeyManagersParameters
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
name|jsse
operator|.
name|KeyStoreParameters
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
name|jsse
operator|.
name|SSLContextParameters
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
name|jsse
operator|.
name|TrustManagersParameters
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
name|test
operator|.
name|AvailablePortFinder
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Stomp
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_class
DECL|class|StompBaseTest
specifier|public
specifier|abstract
class|class
name|StompBaseTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|brokerService
specifier|protected
name|BrokerService
name|brokerService
decl_stmt|;
DECL|field|numberOfMessages
specifier|protected
name|int
name|numberOfMessages
init|=
literal|100
decl_stmt|;
DECL|field|port
specifier|protected
name|int
name|port
decl_stmt|;
DECL|field|canTest
specifier|private
name|boolean
name|canTest
decl_stmt|;
DECL|field|serverSslContextParameters
specifier|private
name|SSLContextParameters
name|serverSslContextParameters
decl_stmt|;
DECL|field|serverSslContext
specifier|private
name|SSLContext
name|serverSslContext
decl_stmt|;
DECL|field|clientSslContextParameters
specifier|private
name|SSLContextParameters
name|clientSslContextParameters
decl_stmt|;
DECL|field|clientSslContext
specifier|private
name|SSLContext
name|clientSslContext
decl_stmt|;
DECL|method|getPort ()
specifier|protected
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * Whether we can test on this box, as not all boxes can be used for reliable CI testing.      */
DECL|method|canTest ()
specifier|protected
name|boolean
name|canTest
parameter_list|()
block|{
return|return
name|canTest
return|;
block|}
DECL|method|isUseSsl ()
specifier|protected
name|boolean
name|isUseSsl
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelRegistry ()
specifier|protected
name|Registry
name|createCamelRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
if|if
condition|(
name|isUseSsl
argument_list|()
condition|)
block|{
name|registry
operator|.
name|bind
argument_list|(
literal|"sslContextParameters"
argument_list|,
name|getClientSSLContextParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|registry
return|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|61613
argument_list|)
expr_stmt|;
try|try
block|{
name|brokerService
operator|=
operator|new
name|BrokerService
argument_list|()
expr_stmt|;
name|brokerService
operator|.
name|setPersistent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|setAdvisorySupport
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|isUseSsl
argument_list|()
condition|)
block|{
name|SslContext
name|sslContext
init|=
operator|new
name|SslContext
argument_list|()
decl_stmt|;
name|sslContext
operator|.
name|setSSLContext
argument_list|(
name|getServerSSLContext
argument_list|()
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|setSslContext
argument_list|(
name|sslContext
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|addConnector
argument_list|(
literal|"stomp+ssl://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"?trace=true"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|brokerService
operator|.
name|addConnector
argument_list|(
literal|"stomp://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"?trace=true"
argument_list|)
expr_stmt|;
block|}
name|brokerService
operator|.
name|start
argument_list|()
expr_stmt|;
name|brokerService
operator|.
name|waitUntilStarted
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|canTest
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Cannot test due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|" more details in the log"
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot test due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|canTest
operator|=
literal|false
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|brokerService
operator|!=
literal|null
condition|)
block|{
name|brokerService
operator|.
name|stop
argument_list|()
expr_stmt|;
name|brokerService
operator|.
name|waitUntilStopped
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createStompClient ()
specifier|protected
name|Stomp
name|createStompClient
parameter_list|()
throws|throws
name|Exception
block|{
name|Stomp
name|stomp
decl_stmt|;
if|if
condition|(
name|isUseSsl
argument_list|()
condition|)
block|{
name|stomp
operator|=
operator|new
name|Stomp
argument_list|(
literal|"ssl://localhost:"
operator|+
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|stomp
operator|.
name|setSslContext
argument_list|(
name|getClientSSLContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stomp
operator|=
operator|new
name|Stomp
argument_list|(
literal|"tcp://localhost:"
operator|+
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|stomp
return|;
block|}
DECL|method|getServerSSLContextParameters ()
specifier|protected
name|SSLContextParameters
name|getServerSSLContextParameters
parameter_list|()
block|{
if|if
condition|(
name|serverSslContextParameters
operator|==
literal|null
condition|)
block|{
name|serverSslContextParameters
operator|=
name|getSSLContextParameters
argument_list|(
literal|"jsse/server.keystore"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
block|}
return|return
name|serverSslContextParameters
return|;
block|}
DECL|method|getServerSSLContext ()
specifier|protected
name|SSLContext
name|getServerSSLContext
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|serverSslContext
operator|==
literal|null
condition|)
block|{
name|serverSslContext
operator|=
name|getServerSSLContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|serverSslContext
return|;
block|}
DECL|method|getClientSSLContextParameters ()
specifier|protected
name|SSLContextParameters
name|getClientSSLContextParameters
parameter_list|()
block|{
if|if
condition|(
name|clientSslContextParameters
operator|==
literal|null
condition|)
block|{
name|clientSslContextParameters
operator|=
name|getSSLContextParameters
argument_list|(
literal|"jsse/client.keystore"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
block|}
return|return
name|clientSslContextParameters
return|;
block|}
DECL|method|getClientSSLContext ()
specifier|protected
name|SSLContext
name|getClientSSLContext
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|clientSslContext
operator|==
literal|null
condition|)
block|{
name|clientSslContext
operator|=
name|getClientSSLContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|clientSslContext
return|;
block|}
DECL|method|getSSLContextParameters (String path, String password)
specifier|private
name|SSLContextParameters
name|getSSLContextParameters
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|KeyStoreParameters
name|ksp
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|ksp
operator|.
name|setResource
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|ksp
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|KeyManagersParameters
name|kmp
init|=
operator|new
name|KeyManagersParameters
argument_list|()
decl_stmt|;
name|kmp
operator|.
name|setKeyPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|kmp
operator|.
name|setKeyStore
argument_list|(
name|ksp
argument_list|)
expr_stmt|;
name|TrustManagersParameters
name|tmp
init|=
operator|new
name|TrustManagersParameters
argument_list|()
decl_stmt|;
name|tmp
operator|.
name|setKeyStore
argument_list|(
name|ksp
argument_list|)
expr_stmt|;
name|SSLContextParameters
name|sslContextParameters
init|=
operator|new
name|SSLContextParameters
argument_list|()
decl_stmt|;
name|sslContextParameters
operator|.
name|setKeyManagers
argument_list|(
name|kmp
argument_list|)
expr_stmt|;
name|sslContextParameters
operator|.
name|setTrustManagers
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
return|return
name|sslContextParameters
return|;
block|}
block|}
end_class

end_unit

