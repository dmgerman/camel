begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail.security
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
operator|.
name|security
package|;
end_package

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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLHandshakeException
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
name|CamelExecutionException
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
comment|/**  * Test of integration between the mail component and JSSE Configuration Utility.  * This test does not easily automate.  This test is therefore ignored and the  * source is maintained here for easier development in the future.  */
end_comment

begin_class
annotation|@
name|Ignore
DECL|class|SslContextParametersMailRouteTest
specifier|public
class|class
name|SslContextParametersMailRouteTest
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
DECL|field|email
specifier|private
name|String
name|email
init|=
literal|"USERNAME@gmail.com"
decl_stmt|;
DECL|field|username
specifier|private
name|String
name|username
init|=
literal|"USERNAME@gmail.com"
decl_stmt|;
DECL|field|imapHost
specifier|private
name|String
name|imapHost
init|=
literal|"imap.gmail.com"
decl_stmt|;
DECL|field|smtpHost
specifier|private
name|String
name|smtpHost
init|=
literal|"smtp.gmail.com"
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
init|=
literal|"PASSWORD"
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendAndReceiveMails ()
specifier|public
name|void
name|testSendAndReceiveMails
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"imaps://"
operator|+
name|imapHost
operator|+
literal|"?username="
operator|+
name|username
operator|+
literal|"&password="
operator|+
name|password
operator|+
literal|"&delete=false&unseen=true&fetchSize=1&consumer.useFixedDelay=true&consumer.delay=1000"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:in"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"smtps://"
operator|+
name|smtpHost
operator|+
literal|"?username="
operator|+
name|username
operator|+
literal|"&password="
operator|+
name|password
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
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:in"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Test Email Body\r\n"
argument_list|)
expr_stmt|;
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"To"
argument_list|,
name|email
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"From"
argument_list|,
name|email
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Reply-to"
argument_list|,
name|email
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Subject"
argument_list|,
literal|"SSL/TLS Test"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:in"
argument_list|,
literal|"Test Email Body"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendAndReceiveMailsWithCustomTrustStore ()
specifier|public
name|void
name|testSendAndReceiveMailsWithCustomTrustStore
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"smtps://"
operator|+
name|smtpHost
operator|+
literal|"?username="
operator|+
name|username
operator|+
literal|"&password="
operator|+
name|password
operator|+
literal|"&sslContextParameters=#sslContextParameters"
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
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"To"
argument_list|,
name|email
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"From"
argument_list|,
name|email
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Reply-to"
argument_list|,
name|email
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Subject"
argument_list|,
literal|"SSL/TLS Test"
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:in"
argument_list|,
literal|"Test Email Body"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
operator|instanceof
name|SSLHandshakeException
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"unable to find valid certification path to requested target"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|reg
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|addSslContextParametersToRegistry
argument_list|(
name|reg
argument_list|)
expr_stmt|;
return|return
name|reg
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
name|registry
operator|.
name|bind
argument_list|(
literal|"sslContextParameters"
argument_list|,
name|sslContextParameters
argument_list|)
expr_stmt|;
block|}
comment|/**      * Stop Camel startup.      */
annotation|@
name|Override
DECL|method|isUseAdviceWith ()
specifier|public
name|boolean
name|isUseAdviceWith
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

