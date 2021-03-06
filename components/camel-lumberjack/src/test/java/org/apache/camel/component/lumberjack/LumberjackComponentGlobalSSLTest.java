begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.lumberjack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|lumberjack
package|;
end_package

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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|LumberjackComponentGlobalSSLTest
specifier|public
class|class
name|LumberjackComponentGlobalSSLTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port
specifier|private
specifier|static
name|int
name|port
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|beforeClass ()
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setSSLContextParameters
argument_list|(
name|createServerSSLContextParameters
argument_list|()
argument_list|)
expr_stmt|;
name|LumberjackComponent
name|component
init|=
operator|(
name|LumberjackComponent
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"lumberjack"
argument_list|)
decl_stmt|;
name|component
operator|.
name|setUseGlobalSslContextParameters
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
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
comment|// Lumberjack configured with SSL
name|from
argument_list|(
literal|"lumberjack:0.0.0.0:"
operator|+
name|port
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:output"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|shouldListenToMessagesOverSSL ()
specifier|public
name|void
name|shouldListenToMessagesOverSSL
parameter_list|()
throws|throws
name|Exception
block|{
comment|// We're expecting 25 messages with Maps
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:output"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|mock
operator|.
name|allMessages
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// When sending messages
name|List
argument_list|<
name|Integer
argument_list|>
name|responses
init|=
name|LumberjackUtil
operator|.
name|sendMessages
argument_list|(
name|port
argument_list|,
name|createClientSSLContextParameters
argument_list|()
argument_list|)
decl_stmt|;
comment|// Then we should have the messages we're expecting
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// And we should have replied with 2 acknowledgments for each window frame
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|10
argument_list|,
literal|15
argument_list|)
argument_list|,
name|responses
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates the {@link SSLContextParameters} Camel object for the Lumberjack component      *      * @return The {@link SSLContextParameters} Camel object for the Lumberjack component      */
DECL|method|createServerSSLContextParameters ()
specifier|private
name|SSLContextParameters
name|createServerSSLContextParameters
parameter_list|()
block|{
name|SSLContextParameters
name|sslContextParameters
init|=
operator|new
name|SSLContextParameters
argument_list|()
decl_stmt|;
name|KeyManagersParameters
name|keyManagersParameters
init|=
operator|new
name|KeyManagersParameters
argument_list|()
decl_stmt|;
name|KeyStoreParameters
name|keyStore
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|keyStore
operator|.
name|setPassword
argument_list|(
literal|"changeit"
argument_list|)
expr_stmt|;
name|keyStore
operator|.
name|setResource
argument_list|(
literal|"org/apache/camel/component/lumberjack/keystore.jks"
argument_list|)
expr_stmt|;
name|keyManagersParameters
operator|.
name|setKeyPassword
argument_list|(
literal|"changeit"
argument_list|)
expr_stmt|;
name|keyManagersParameters
operator|.
name|setKeyStore
argument_list|(
name|keyStore
argument_list|)
expr_stmt|;
name|sslContextParameters
operator|.
name|setKeyManagers
argument_list|(
name|keyManagersParameters
argument_list|)
expr_stmt|;
return|return
name|sslContextParameters
return|;
block|}
DECL|method|createClientSSLContextParameters ()
specifier|private
name|SSLContextParameters
name|createClientSSLContextParameters
parameter_list|()
block|{
name|SSLContextParameters
name|sslContextParameters
init|=
operator|new
name|SSLContextParameters
argument_list|()
decl_stmt|;
name|TrustManagersParameters
name|trustManagersParameters
init|=
operator|new
name|TrustManagersParameters
argument_list|()
decl_stmt|;
name|KeyStoreParameters
name|trustStore
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|trustStore
operator|.
name|setPassword
argument_list|(
literal|"changeit"
argument_list|)
expr_stmt|;
name|trustStore
operator|.
name|setResource
argument_list|(
literal|"org/apache/camel/component/lumberjack/keystore.jks"
argument_list|)
expr_stmt|;
name|trustManagersParameters
operator|.
name|setKeyStore
argument_list|(
name|trustStore
argument_list|)
expr_stmt|;
name|sslContextParameters
operator|.
name|setTrustManagers
argument_list|(
name|trustManagersParameters
argument_list|)
expr_stmt|;
return|return
name|sslContextParameters
return|;
block|}
block|}
end_class

end_unit

