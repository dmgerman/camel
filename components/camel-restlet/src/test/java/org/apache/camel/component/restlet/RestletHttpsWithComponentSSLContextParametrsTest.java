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
DECL|class|RestletHttpsWithComponentSSLContextParametrsTest
specifier|public
class|class
name|RestletHttpsWithComponentSSLContextParametrsTest
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
name|RestletComponent
name|restletSSL
init|=
operator|new
name|RestletComponent
argument_list|()
decl_stmt|;
name|RestletComponent
name|restletWithGlobalSSL
init|=
operator|new
name|RestletComponent
argument_list|()
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"restlet-SSL"
argument_list|,
name|restletSSL
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"restlet-withGlobalSSL"
argument_list|,
name|restletWithGlobalSSL
argument_list|)
expr_stmt|;
name|SSLContextParameters
name|scp
init|=
name|generateSSLContextParametrs
argument_list|(
literal|"changeit"
argument_list|)
decl_stmt|;
name|SSLContextParameters
name|globalScp
init|=
name|generateSSLContextParametrs
argument_list|(
literal|"wrongPassword"
argument_list|)
decl_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"restlet-SSL"
argument_list|,
name|RestletComponent
operator|.
name|class
argument_list|)
operator|.
name|setSslContextParameters
argument_list|(
name|scp
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"restlet-withGlobalSSL"
argument_list|,
name|RestletComponent
operator|.
name|class
argument_list|)
operator|.
name|setUseGlobalSslContextParameters
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"restlet-withGlobalSSL"
argument_list|,
name|RestletComponent
operator|.
name|class
argument_list|)
operator|.
name|setSslContextParameters
argument_list|(
name|scp
argument_list|)
expr_stmt|;
name|context
operator|.
name|setSSLContextParameters
argument_list|(
name|globalScp
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"restlet-SSL:https://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/SSL?restletMethods=post"
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
name|convertBody
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"restlet-withGlobalSSL:https://localhost:"
operator|+
operator|(
name|portNum
operator|+
literal|1
operator|)
operator|+
literal|"/users/globalSSL?restletMethods=post"
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
name|convertBody
argument_list|(
name|exchange
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
DECL|method|testComponentSSLContextParametrs ()
specifier|public
name|void
name|testComponentSSLContextParametrs
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test that ssl context set for component
name|postRequestMessage
argument_list|(
name|REQUEST_MESSAGE
argument_list|,
literal|"SSL"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testComponentGlobalSSLContextParametrs ()
specifier|public
name|void
name|testComponentGlobalSSLContextParametrs
parameter_list|()
throws|throws
name|Exception
block|{
comment|// test that ssl context set for component has bigger priority than
comment|// global context
name|postRequestMessage
argument_list|(
name|REQUEST_MESSAGE
argument_list|,
literal|"globalSSL"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|postRequestMessage (String message, String path, Integer portIncrement)
specifier|private
name|void
name|postRequestMessage
parameter_list|(
name|String
name|message
parameter_list|,
name|String
name|path
parameter_list|,
name|Integer
name|portIncrement
parameter_list|)
throws|throws
name|Exception
block|{
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
operator|(
name|portNum
operator|+
name|portIncrement
operator|)
operator|+
literal|"/users/"
operator|+
name|path
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
name|assertTrue
argument_list|(
name|s
operator|.
name|contains
argument_list|(
literal|"<status>OK</status>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|convertBody (Exchange exchange)
name|void
name|convertBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
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
name|contains
argument_list|(
name|REQUEST_MESSAGE
argument_list|)
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
DECL|method|generateSSLContextParametrs (String password)
name|SSLContextParameters
name|generateSSLContextParametrs
parameter_list|(
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
return|return
name|sslContextParameters
return|;
block|}
block|}
end_class

end_unit

