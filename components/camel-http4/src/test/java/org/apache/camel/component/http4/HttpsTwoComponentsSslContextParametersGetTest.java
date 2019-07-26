begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
package|;
end_package

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
name|http
operator|.
name|conn
operator|.
name|ssl
operator|.
name|NoopHostnameVerifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|bootstrap
operator|.
name|HttpServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|bootstrap
operator|.
name|ServerBootstrap
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
DECL|class|HttpsTwoComponentsSslContextParametersGetTest
specifier|public
class|class
name|HttpsTwoComponentsSslContextParametersGetTest
extends|extends
name|BaseHttpsTest
block|{
DECL|field|port2
specifier|private
name|int
name|port2
decl_stmt|;
DECL|field|localServer
specifier|private
name|HttpServer
name|localServer
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"x509HostnameVerifier"
argument_list|)
DECL|field|hostnameVerifier
specifier|private
name|NoopHostnameVerifier
name|hostnameVerifier
init|=
operator|new
name|NoopHostnameVerifier
argument_list|()
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"sslContextParameters"
argument_list|)
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
init|=
operator|new
name|SSLContextParameters
argument_list|()
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"sslContextParameters2"
argument_list|)
DECL|field|sslContextParameters2
specifier|private
name|SSLContextParameters
name|sslContextParameters2
init|=
operator|new
name|SSLContextParameters
argument_list|()
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"http4s-foo"
argument_list|)
DECL|field|httpComponent
specifier|private
name|HttpComponent
name|httpComponent
init|=
operator|new
name|HttpComponent
argument_list|()
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"http4s-bar"
argument_list|)
DECL|field|httpComponent1
specifier|private
name|HttpComponent
name|httpComponent1
init|=
operator|new
name|HttpComponent
argument_list|()
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|=
name|ServerBootstrap
operator|.
name|bootstrap
argument_list|()
operator|.
name|setHttpProcessor
argument_list|(
name|getBasicHttpProcessor
argument_list|()
argument_list|)
operator|.
name|setConnectionReuseStrategy
argument_list|(
name|getConnectionReuseStrategy
argument_list|()
argument_list|)
operator|.
name|setResponseFactory
argument_list|(
name|getHttpResponseFactory
argument_list|()
argument_list|)
operator|.
name|setExpectationVerifier
argument_list|(
name|getHttpExpectationVerifier
argument_list|()
argument_list|)
operator|.
name|setSslContext
argument_list|(
name|getSSLContext
argument_list|()
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|localServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
annotation|@
name|Override
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
name|localServer
operator|!=
literal|null
condition|)
block|{
name|localServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
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
name|Test
DECL|method|httpsTwoDifferentSSLContextNotSupported ()
specifier|public
name|void
name|httpsTwoDifferentSSLContextNotSupported
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|port2
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
name|localServer
operator|.
name|getLocalPort
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http4s-foo://127.0.0.1:"
operator|+
name|localServer
operator|.
name|getLocalPort
argument_list|()
operator|+
literal|"/mail?x509HostnameVerifier=#x509HostnameVerifier&sslContextParameters=#sslContextParameters"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http4s-bar://127.0.0.1:"
operator|+
name|port2
operator|+
literal|"/mail?x509HostnameVerifier=#x509HostnameVerifier&sslContextParameters=#sslContextParameters2"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// should be able to startup
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

