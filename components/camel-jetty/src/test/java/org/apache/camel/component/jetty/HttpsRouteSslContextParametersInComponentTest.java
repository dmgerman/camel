begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|net
operator|.
name|URISyntaxException
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

begin_class
DECL|class|HttpsRouteSslContextParametersInComponentTest
specifier|public
class|class
name|HttpsRouteSslContextParametersInComponentTest
extends|extends
name|HttpsRouteTest
block|{
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|URISyntaxException
block|{
comment|// create jetty component
name|JettyHttpComponent
name|jetty
init|=
operator|new
name|JettyHttpComponent
argument_list|()
decl_stmt|;
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
name|pwd
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
name|pwd
argument_list|)
expr_stmt|;
name|kmp
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
name|jetty
operator|.
name|setSslContextParameters
argument_list|(
name|sslContextParameters
argument_list|)
expr_stmt|;
comment|// NOTE: These are here to check that they are properly ignored.
name|jetty
operator|.
name|addSslSocketConnectorProperty
argument_list|(
literal|"keyPassword"
argument_list|,
literal|"sadfasdfasdfas"
argument_list|)
expr_stmt|;
name|jetty
operator|.
name|addSslSocketConnectorProperty
argument_list|(
literal|"password"
argument_list|,
literal|"asdfasdfasdfdasfs"
argument_list|)
expr_stmt|;
name|jetty
operator|.
name|addSslSocketConnectorProperty
argument_list|(
literal|"keystore"
argument_list|,
literal|""
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
comment|// add jetty to camel context
name|context
operator|.
name|addComponent
argument_list|(
literal|"jetty"
argument_list|,
name|jetty
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:https://localhost:"
operator|+
name|port1
operator|+
literal|"/test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|Processor
name|proc
init|=
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
literal|"<b>Hello World</b>"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|from
argument_list|(
literal|"jetty:https://localhost:"
operator|+
name|port1
operator|+
literal|"/hello"
argument_list|)
operator|.
name|process
argument_list|(
name|proc
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:https://localhost:"
operator|+
name|port2
operator|+
literal|"/test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

