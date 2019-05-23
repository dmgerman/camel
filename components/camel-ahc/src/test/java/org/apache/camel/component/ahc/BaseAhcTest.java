begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
package|;
end_package

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
name|component
operator|.
name|properties
operator|.
name|PropertiesComponent
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
name|support
operator|.
name|jsse
operator|.
name|ClientAuthentication
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
name|SSLContextServerParameters
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

begin_class
DECL|class|BaseAhcTest
specifier|public
specifier|abstract
class|class
name|BaseAhcTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|KEY_STORE_PASSWORD
specifier|protected
specifier|static
specifier|final
name|String
name|KEY_STORE_PASSWORD
init|=
literal|"changeit"
decl_stmt|;
DECL|field|port
specifier|private
specifier|static
specifier|volatile
name|int
name|port
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|initPort ()
specifier|public
specifier|static
name|void
name|initPort
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
literal|24000
argument_list|)
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
name|addComponent
argument_list|(
literal|"properties"
argument_list|,
operator|new
name|PropertiesComponent
argument_list|(
literal|"ref:prop"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|Properties
name|prop
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|prop
operator|.
name|setProperty
argument_list|(
literal|"port"
argument_list|,
literal|""
operator|+
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"prop"
argument_list|,
name|prop
argument_list|)
expr_stmt|;
if|if
condition|(
name|isHttps
argument_list|()
condition|)
block|{
name|addSslContextParametersToRegistry
argument_list|(
name|jndi
argument_list|)
expr_stmt|;
block|}
return|return
name|jndi
return|;
block|}
DECL|method|addSslContextParametersToRegistry (JndiRegistry registry)
specifier|protected
name|void
name|addSslContextParametersToRegistry
parameter_list|(
name|JndiRegistry
name|registry
parameter_list|)
block|{
name|registry
operator|.
name|bind
argument_list|(
literal|"sslContextParameters"
argument_list|,
name|createSSLContextParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createSSLContextParameters ()
specifier|protected
name|SSLContextParameters
name|createSSLContextParameters
parameter_list|()
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
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"jsse/localhost.ks"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ksp
operator|.
name|setPassword
argument_list|(
name|KEY_STORE_PASSWORD
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
name|KEY_STORE_PASSWORD
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
comment|// NOTE: Needed since the client uses a loose trust configuration when no ssl context
comment|// is provided.  We turn on WANT client-auth to prefer using authentication
name|SSLContextServerParameters
name|scsp
init|=
operator|new
name|SSLContextServerParameters
argument_list|()
decl_stmt|;
name|scsp
operator|.
name|setClientAuthentication
argument_list|(
name|ClientAuthentication
operator|.
name|WANT
operator|.
name|name
argument_list|()
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
name|sslContextParameters
operator|.
name|setServerParameters
argument_list|(
name|scsp
argument_list|)
expr_stmt|;
return|return
name|sslContextParameters
return|;
block|}
comment|/**      * Indicates if the URIs returned from {@link #getTestServerEndpointUri()} and      * {@link #getAhcEndpointUri()} should use the HTTPS protocol instead of      * the HTTP protocol.      *      * If true, an {@link SSLContextParameters} is also placed in the registry under the      * key {@code sslContextParameters}.  The parameters are not added to the endpoint URIs      * as that is test specific.      *      * @return false by default      */
DECL|method|isHttps ()
specifier|protected
name|boolean
name|isHttps
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getProtocol ()
specifier|protected
name|String
name|getProtocol
parameter_list|()
block|{
name|String
name|protocol
init|=
literal|"http"
decl_stmt|;
if|if
condition|(
name|isHttps
argument_list|()
condition|)
block|{
name|protocol
operator|=
name|protocol
operator|+
literal|"s"
expr_stmt|;
block|}
return|return
name|protocol
return|;
block|}
DECL|method|getTestServerEndpointUrl ()
specifier|protected
name|String
name|getTestServerEndpointUrl
parameter_list|()
block|{
return|return
name|getProtocol
argument_list|()
operator|+
literal|"://localhost:{{port}}/foo"
return|;
block|}
DECL|method|getTestServerEndpointUri ()
specifier|protected
name|String
name|getTestServerEndpointUri
parameter_list|()
block|{
return|return
literal|"jetty:"
operator|+
name|getTestServerEndpointUrl
argument_list|()
return|;
block|}
DECL|method|getTestServerEndpointTwoUrl ()
specifier|protected
name|String
name|getTestServerEndpointTwoUrl
parameter_list|()
block|{
comment|// Don't use the property placeholder here since we use the value outside of a
comment|// field that supports the placeholders.
return|return
name|getProtocol
argument_list|()
operator|+
literal|"://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/bar"
return|;
block|}
DECL|method|getTestServerEndpointTwoUri ()
specifier|protected
name|String
name|getTestServerEndpointTwoUri
parameter_list|()
block|{
return|return
literal|"jetty:"
operator|+
name|getTestServerEndpointTwoUrl
argument_list|()
return|;
block|}
DECL|method|getAhcEndpointUri ()
specifier|protected
name|String
name|getAhcEndpointUri
parameter_list|()
block|{
return|return
literal|"ahc:"
operator|+
name|getProtocol
argument_list|()
operator|+
literal|"://localhost:{{port}}/foo"
return|;
block|}
DECL|method|getNextPort ()
specifier|protected
specifier|synchronized
name|int
name|getNextPort
parameter_list|()
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
name|port
operator|+
literal|1
argument_list|)
expr_stmt|;
return|return
name|port
return|;
block|}
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
block|}
end_class

end_unit

