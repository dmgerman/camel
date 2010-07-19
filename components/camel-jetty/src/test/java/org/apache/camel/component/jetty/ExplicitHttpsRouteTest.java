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
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|ssl
operator|.
name|SslSocketConnector
import|;
end_import

begin_class
DECL|class|ExplicitHttpsRouteTest
specifier|public
class|class
name|ExplicitHttpsRouteTest
extends|extends
name|HttpsRouteTest
block|{
comment|// START SNIPPET: e2
DECL|method|createSslSocketConnector ()
specifier|private
name|SslSocketConnector
name|createSslSocketConnector
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|SslSocketConnector
name|sslSocketConnector
init|=
operator|new
name|SslSocketConnector
argument_list|()
decl_stmt|;
name|sslSocketConnector
operator|.
name|setKeyPassword
argument_list|(
name|pwd
argument_list|)
expr_stmt|;
name|sslSocketConnector
operator|.
name|setPassword
argument_list|(
name|pwd
argument_list|)
expr_stmt|;
name|URL
name|keyStoreUrl
init|=
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
decl_stmt|;
name|sslSocketConnector
operator|.
name|setKeystore
argument_list|(
name|keyStoreUrl
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|sslSocketConnector
operator|.
name|setTruststoreType
argument_list|(
literal|"JKS"
argument_list|)
expr_stmt|;
return|return
name|sslSocketConnector
return|;
block|}
comment|// END SNIPPET: e2
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
comment|// START SNIPPET: e1
comment|// create SSL socket connectors for port 9080 and 9090
name|Map
argument_list|<
name|Integer
argument_list|,
name|SslSocketConnector
argument_list|>
name|connectors
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|SslSocketConnector
argument_list|>
argument_list|()
decl_stmt|;
name|connectors
operator|.
name|put
argument_list|(
literal|9080
argument_list|,
name|createSslSocketConnector
argument_list|()
argument_list|)
expr_stmt|;
name|connectors
operator|.
name|put
argument_list|(
literal|9090
argument_list|,
name|createSslSocketConnector
argument_list|()
argument_list|)
expr_stmt|;
comment|// create jetty component
name|JettyHttpComponent
name|jetty
init|=
operator|new
name|JettyHttpComponent
argument_list|()
decl_stmt|;
comment|// add connectors
name|jetty
operator|.
name|setSslSocketConnectors
argument_list|(
name|connectors
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
comment|// END SNIPPET: e1
name|from
argument_list|(
literal|"jetty:https://localhost:9080/test"
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
literal|"jetty:https://localhost:9080/hello"
argument_list|)
operator|.
name|process
argument_list|(
name|proc
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:https://localhost:9090/test"
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

