begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|BindToRegistry
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
name|http
operator|.
name|common
operator|.
name|HttpHeaderFilterStrategy
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
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Server
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
DECL|class|BaseJettyTest
specifier|public
specifier|abstract
class|class
name|BaseJettyTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port
specifier|private
specifier|static
specifier|volatile
name|int
name|port
decl_stmt|;
DECL|field|port2
specifier|private
specifier|static
specifier|volatile
name|int
name|port2
decl_stmt|;
DECL|field|counter
specifier|private
specifier|final
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|1
argument_list|)
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
argument_list|()
expr_stmt|;
comment|// find another ports for proxy route test
name|port2
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
name|getPropertiesComponent
argument_list|()
operator|.
name|setLocation
argument_list|(
literal|"ref:prop"
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|BindToRegistry
argument_list|(
literal|"prop"
argument_list|)
DECL|method|loadProp ()
specifier|public
name|Properties
name|loadProp
parameter_list|()
throws|throws
name|Exception
block|{
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
name|prop
operator|.
name|setProperty
argument_list|(
literal|"port2"
argument_list|,
literal|""
operator|+
name|getPort2
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|prop
return|;
block|}
DECL|method|getNextPort ()
specifier|protected
name|int
name|getNextPort
parameter_list|()
block|{
return|return
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
return|;
block|}
DECL|method|setSSLProps (JettyHttpComponent jetty, String path, String keyStorePasswd, String keyPasswd)
specifier|public
name|void
name|setSSLProps
parameter_list|(
name|JettyHttpComponent
name|jetty
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|keyStorePasswd
parameter_list|,
name|String
name|keyPasswd
parameter_list|)
block|{
if|if
condition|(
name|jettyVersion
argument_list|()
operator|==
literal|9
condition|)
block|{
name|jetty
operator|.
name|addSslSocketConnectorProperty
argument_list|(
literal|"protocol"
argument_list|,
literal|"TLSv1.2"
argument_list|)
expr_stmt|;
name|jetty
operator|.
name|addSslSocketConnectorProperty
argument_list|(
literal|"keyStorePassword"
argument_list|,
name|keyStorePasswd
argument_list|)
expr_stmt|;
name|jetty
operator|.
name|addSslSocketConnectorProperty
argument_list|(
literal|"keyManagerPassword"
argument_list|,
name|keyPasswd
argument_list|)
expr_stmt|;
name|jetty
operator|.
name|addSslSocketConnectorProperty
argument_list|(
literal|"keyStorePath"
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|jetty
operator|.
name|addSslSocketConnectorProperty
argument_list|(
literal|"trustStoreType"
argument_list|,
literal|"JKS"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jetty
operator|.
name|addSslSocketConnectorProperty
argument_list|(
literal|"protocol"
argument_list|,
literal|"TLSv1.2"
argument_list|)
expr_stmt|;
name|jetty
operator|.
name|addSslSocketConnectorProperty
argument_list|(
literal|"password"
argument_list|,
name|keyStorePasswd
argument_list|)
expr_stmt|;
name|jetty
operator|.
name|addSslSocketConnectorProperty
argument_list|(
literal|"keyPassword"
argument_list|,
name|keyPasswd
argument_list|)
expr_stmt|;
name|jetty
operator|.
name|addSslSocketConnectorProperty
argument_list|(
literal|"keystore"
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|jetty
operator|.
name|addSslSocketConnectorProperty
argument_list|(
literal|"truststoreType"
argument_list|,
literal|"JKS"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getPort ()
specifier|protected
specifier|static
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|getPort2 ()
specifier|protected
specifier|static
name|int
name|getPort2
parameter_list|()
block|{
return|return
name|port2
return|;
block|}
DECL|method|jettyVersion ()
specifier|public
name|int
name|jettyVersion
parameter_list|()
block|{
try|try
block|{
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
literal|"org.eclipse.jetty.server.ssl.SslSelectChannelConnector"
argument_list|)
expr_stmt|;
return|return
literal|8
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|9
return|;
block|}
block|}
DECL|method|allowNullHeaders ()
specifier|protected
name|void
name|allowNullHeaders
parameter_list|()
block|{
name|JettyHttpComponent
name|jetty
init|=
operator|(
name|JettyHttpComponent
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"jetty"
argument_list|)
decl_stmt|;
name|HttpHeaderFilterStrategy
name|filterStrat
init|=
operator|new
name|HttpHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|filterStrat
operator|.
name|setAllowNullValues
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|jetty
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|filterStrat
argument_list|)
expr_stmt|;
block|}
DECL|method|isJetty8 ()
specifier|protected
name|boolean
name|isJetty8
parameter_list|()
block|{
name|String
name|majorVersion
init|=
name|Server
operator|.
name|getVersion
argument_list|()
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
return|return
literal|"8"
operator|.
name|equals
argument_list|(
name|majorVersion
argument_list|)
return|;
block|}
block|}
end_class

end_unit

