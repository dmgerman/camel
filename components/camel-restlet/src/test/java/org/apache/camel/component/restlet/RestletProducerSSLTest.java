begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|Exchange
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
name|Processor
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
name|util
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
name|util
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
name|apache
operator|.
name|camel
operator|.
name|util
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|RestletProducerSSLTest
specifier|public
class|class
name|RestletProducerSSLTest
extends|extends
name|RestletTestSupport
block|{
DECL|field|BODY
specifier|public
specifier|static
specifier|final
name|String
name|BODY
init|=
literal|"some body"
decl_stmt|;
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
name|SSLContextParameters
name|sslContextParameters
init|=
name|createSSLContext
argument_list|()
decl_stmt|;
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
literal|"mySSLContextParameters"
argument_list|,
name|sslContextParameters
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Test
DECL|method|testRestletProducerComponentSSL ()
specifier|public
name|void
name|testRestletProducerComponentSSL
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:startWithEndpointSSL"
argument_list|,
literal|"neco"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|BODY
argument_list|,
name|out
argument_list|)
expr_stmt|;
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
literal|"direct:startWithEndpointSSL"
argument_list|)
operator|.
name|to
argument_list|(
literal|"restlet:https://localhost:"
operator|+
name|portNum
operator|+
literal|"/?restletMethod=post&sslContextParameters=#mySSLContextParameters"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"restlet:https://localhost:"
operator|+
name|portNum
operator|+
literal|"/?restletMethods=post&sslContextParameters=#mySSLContextParameters"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|BODY
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createSSLContext ()
specifier|protected
name|SSLContextParameters
name|createSSLContext
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
operator|new
name|File
argument_list|(
literal|"src/test/resources/jsse/localhost.ks"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|ksp
operator|.
name|setPassword
argument_list|(
literal|"changeit"
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
literal|"changeit"
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

