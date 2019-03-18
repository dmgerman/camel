begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|net
operator|.
name|URL
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
name|SSLContextParametersAware
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
name|http
operator|.
name|HttpResponse
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
name|client
operator|.
name|methods
operator|.
name|HttpPost
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
name|entity
operator|.
name|StringEntity
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
DECL|class|RestletHttpsWithGlobalSSLContextParametersTest
specifier|public
class|class
name|RestletHttpsWithGlobalSSLContextParametersTest
extends|extends
name|RestletTestSupport
block|{
DECL|field|REQUEST_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_MESSAGE
init|=
literal|"<mail><body>HelloWorld!</body><subject>test</subject><to>x@y.net</to></mail>"
decl_stmt|;
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
name|getPath
argument_list|()
operator|.
name|toString
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
name|context
operator|.
name|setSSLContextParameters
argument_list|(
name|sslContextParameters
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SSLContextParametersAware
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"restlet"
argument_list|)
operator|)
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// enable POST support
name|from
argument_list|(
literal|"restlet:https://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/?restletMethods=post"
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
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong request message"
argument_list|,
name|body
operator|.
name|indexOf
argument_list|(
name|REQUEST_MESSAGE
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<status>OK</status>"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/xml"
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
annotation|@
name|Test
DECL|method|testPostXml ()
specifier|public
name|void
name|testPostXml
parameter_list|()
throws|throws
name|Exception
block|{
name|postRequestMessage
argument_list|(
name|REQUEST_MESSAGE
argument_list|)
expr_stmt|;
block|}
DECL|method|postRequestMessage (String message)
specifier|private
name|void
name|postRequestMessage
parameter_list|(
name|String
name|message
parameter_list|)
throws|throws
name|Exception
block|{
comment|// ensure jsse clients can validate the self signed dummy localhost cert,
comment|// use the server keystore as the trust store for these tests
name|URL
name|trustStoreUrl
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
name|System
operator|.
name|setProperty
argument_list|(
literal|"javax.net.ssl.trustStore"
argument_list|,
name|trustStoreUrl
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|HttpPost
name|post
init|=
operator|new
name|HttpPost
argument_list|(
literal|"https://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/"
argument_list|)
decl_stmt|;
name|post
operator|.
name|addHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|post
operator|.
name|setEntity
argument_list|(
operator|new
name|StringEntity
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|doExecute
argument_list|(
name|post
argument_list|)
decl_stmt|;
name|assertHttpResponse
argument_list|(
name|response
argument_list|,
literal|200
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<status>OK</status>"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

