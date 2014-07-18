begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
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
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSession
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|NettySSLTest
specifier|public
class|class
name|NettySSLTest
extends|extends
name|BaseNettyTest
block|{
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
literal|"ksf"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/keystore.jks"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"tsf"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/keystore.jks"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
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
DECL|method|testSSLInOutWithNettyConsumer ()
specifier|public
name|void
name|testSSLInOutWithNettyConsumer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ibm jdks dont have sun security algorithms
if|if
condition|(
name|isJavaVendor
argument_list|(
literal|"ibm"
argument_list|)
condition|)
block|{
return|return;
block|}
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// needClientAuth=true so we can get the client certificate details
name|from
argument_list|(
literal|"netty:tcp://localhost:{{port}}?sync=true&ssl=true&passphrase=changeit&keyStoreFile=#ksf&trustStoreFile=#tsf&needClientAuth=true"
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
name|SSLSession
name|session
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_SSL_SESSION
argument_list|,
name|SSLSession
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|javax
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
name|cert
init|=
name|session
operator|.
name|getPeerCertificateChain
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|Principal
name|principal
init|=
name|cert
operator|.
name|getSubjectDN
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Client Cert SubjectDN: {}"
argument_list|,
name|principal
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"When You Go Home, Tell Them Of Us And Say, For Your Tomorrow, We Gave Our Today."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Cannot start conversion without SSLSession"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty:tcp://localhost:{{port}}?sync=true&ssl=true&passphrase=changeit&keyStoreFile=#ksf&trustStoreFile=#tsf"
argument_list|,
literal|"Epitaph in Kohima, India marking the WWII Battle of Kohima and Imphal, Burma Campaign - Attributed to John Maxwell Edmonds"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"When You Go Home, Tell Them Of Us And Say, For Your Tomorrow, We Gave Our Today."
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

