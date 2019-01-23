begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nats
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nats
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
name|Ignore
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

begin_comment
comment|/*   * The keystore used in this test it's from https://github.com/nats-io/jnats/tree/master/src/test/resources, in particular  * the tls_1222.conf file. Running this test will require use the server-cert.pem and server-key.pem in your gnatsd running instance.  */
end_comment

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Require a running Nats server"
argument_list|)
DECL|class|NatsProducerTLSTest
specifier|public
class|class
name|NatsProducerTLSTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|sendTest ()
specifier|public
name|void
name|sendTest
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:send"
argument_list|,
literal|"pippo"
argument_list|)
expr_stmt|;
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
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"ssl"
argument_list|,
name|createSSLContextParameters
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
DECL|method|createSSLContextParameters ()
specifier|private
name|SSLContextParameters
name|createSSLContextParameters
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
literal|"password"
argument_list|)
expr_stmt|;
name|keyStore
operator|.
name|setResource
argument_list|(
literal|"org/apache/camel/component/nats/keystore.jks"
argument_list|)
expr_stmt|;
name|keyManagersParameters
operator|.
name|setKeyPassword
argument_list|(
literal|"password"
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
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
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
name|from
argument_list|(
literal|"direct:send"
argument_list|)
operator|.
name|to
argument_list|(
literal|"nats://localhost:4222?topic=test&sslContextParameters=#ssl&secure=true"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

