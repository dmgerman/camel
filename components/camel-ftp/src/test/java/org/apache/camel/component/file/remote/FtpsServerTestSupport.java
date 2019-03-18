begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|remote
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
name|NoSuchAlgorithmException
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
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|FtpServerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|listener
operator|.
name|ListenerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|ssl
operator|.
name|SslConfigurationFactory
import|;
end_import

begin_comment
comment|/**  * Abstract base class for unit testing using a secure FTP Server (over SSL/TLS)  */
end_comment

begin_class
DECL|class|FtpsServerTestSupport
specifier|public
specifier|abstract
class|class
name|FtpsServerTestSupport
extends|extends
name|FtpServerTestSupport
block|{
DECL|field|AUTH_VALUE_SSL
specifier|protected
specifier|static
specifier|final
name|String
name|AUTH_VALUE_SSL
init|=
literal|"SSL"
decl_stmt|;
DECL|field|AUTH_VALUE_TLS
specifier|protected
specifier|static
specifier|final
name|String
name|AUTH_VALUE_TLS
init|=
literal|"TLS"
decl_stmt|;
DECL|field|FTPSERVER_KEYSTORE
specifier|protected
specifier|static
specifier|final
name|File
name|FTPSERVER_KEYSTORE
init|=
operator|new
name|File
argument_list|(
literal|"./src/test/resources/server.jks"
argument_list|)
decl_stmt|;
DECL|field|FTPSERVER_KEYSTORE_PASSWORD
specifier|protected
specifier|static
specifier|final
name|String
name|FTPSERVER_KEYSTORE_PASSWORD
init|=
literal|"password"
decl_stmt|;
annotation|@
name|Override
DECL|method|createFtpServerFactory ()
specifier|protected
name|FtpServerFactory
name|createFtpServerFactory
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
return|return
name|doCreateFtpServerFactory
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore if algorithm is not on the OS
name|NoSuchAlgorithmException
name|nsae
init|=
name|ObjectHelper
operator|.
name|getException
argument_list|(
name|NoSuchAlgorithmException
operator|.
name|class
argument_list|,
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|nsae
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
decl_stmt|;
name|String
name|message
init|=
name|nsae
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"SunX509 is not avail on this platform [{}] Testing is skipped! Real cause: {}"
argument_list|,
name|name
argument_list|,
name|message
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
else|else
block|{
comment|// some other error then throw it so the test can fail
throw|throw
name|e
throw|;
block|}
block|}
block|}
DECL|method|doCreateFtpServerFactory ()
specifier|protected
name|FtpServerFactory
name|doCreateFtpServerFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|FTPSERVER_KEYSTORE
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|FtpServerFactory
name|serverFactory
init|=
name|super
operator|.
name|createFtpServerFactory
argument_list|()
decl_stmt|;
name|ListenerFactory
name|listenerFactory
init|=
operator|new
name|ListenerFactory
argument_list|(
name|serverFactory
operator|.
name|getListener
argument_list|(
name|DEFAULT_LISTENER
argument_list|)
argument_list|)
decl_stmt|;
name|listenerFactory
operator|.
name|setImplicitSsl
argument_list|(
name|useImplicit
argument_list|()
argument_list|)
expr_stmt|;
name|listenerFactory
operator|.
name|setSslConfiguration
argument_list|(
name|createSslConfiguration
argument_list|()
operator|.
name|createSslConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|serverFactory
operator|.
name|addListener
argument_list|(
name|DEFAULT_LISTENER
argument_list|,
name|listenerFactory
operator|.
name|createListener
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|serverFactory
return|;
block|}
DECL|method|createSslConfiguration ()
specifier|protected
name|SslConfigurationFactory
name|createSslConfiguration
parameter_list|()
block|{
comment|// comment in, if you have trouble with SSL
comment|// System.setProperty("javax.net.debug", "all");
name|SslConfigurationFactory
name|sslConfigFactory
init|=
operator|new
name|SslConfigurationFactory
argument_list|()
decl_stmt|;
name|sslConfigFactory
operator|.
name|setSslProtocol
argument_list|(
name|getAuthValue
argument_list|()
argument_list|)
expr_stmt|;
name|sslConfigFactory
operator|.
name|setKeystoreFile
argument_list|(
name|FTPSERVER_KEYSTORE
argument_list|)
expr_stmt|;
name|sslConfigFactory
operator|.
name|setKeystoreType
argument_list|(
literal|"JKS"
argument_list|)
expr_stmt|;
name|sslConfigFactory
operator|.
name|setKeystoreAlgorithm
argument_list|(
literal|"SunX509"
argument_list|)
expr_stmt|;
name|sslConfigFactory
operator|.
name|setKeystorePassword
argument_list|(
name|FTPSERVER_KEYSTORE_PASSWORD
argument_list|)
expr_stmt|;
name|sslConfigFactory
operator|.
name|setKeyPassword
argument_list|(
name|FTPSERVER_KEYSTORE_PASSWORD
argument_list|)
expr_stmt|;
name|sslConfigFactory
operator|.
name|setClientAuthentication
argument_list|(
name|getClientAuth
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|getClientAuth
argument_list|()
argument_list|)
condition|)
block|{
name|sslConfigFactory
operator|.
name|setTruststoreFile
argument_list|(
name|FTPSERVER_KEYSTORE
argument_list|)
expr_stmt|;
name|sslConfigFactory
operator|.
name|setTruststoreType
argument_list|(
literal|"JKS"
argument_list|)
expr_stmt|;
name|sslConfigFactory
operator|.
name|setTruststoreAlgorithm
argument_list|(
literal|"SunX509"
argument_list|)
expr_stmt|;
name|sslConfigFactory
operator|.
name|setTruststorePassword
argument_list|(
name|FTPSERVER_KEYSTORE_PASSWORD
argument_list|)
expr_stmt|;
block|}
return|return
name|sslConfigFactory
return|;
block|}
comment|/**      * Set what client authentication level to use, supported values are "yes"      * or "true" for required authentication, "want" for wanted authentication      * and "false" or "none" for no authentication. Defaults to "none".      *       * @return clientAuthReqd      */
DECL|method|getClientAuth ()
specifier|protected
specifier|abstract
name|String
name|getClientAuth
parameter_list|()
function_decl|;
comment|/**      * Should listeners created by this factory automatically be in SSL mode       * automatically or must the client explicitly request to use SSL      */
DECL|method|useImplicit ()
specifier|protected
specifier|abstract
name|boolean
name|useImplicit
parameter_list|()
function_decl|;
comment|/**      * Set the SSL protocol used for this channel. Supported values are "SSL"      * and "TLS".      */
DECL|method|getAuthValue ()
specifier|protected
specifier|abstract
name|String
name|getAuthValue
parameter_list|()
function_decl|;
block|}
end_class

end_unit

