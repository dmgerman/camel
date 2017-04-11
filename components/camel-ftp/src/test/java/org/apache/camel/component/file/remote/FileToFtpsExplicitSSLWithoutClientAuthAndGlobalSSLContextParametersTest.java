begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|FileToFtpsExplicitSSLWithoutClientAuthAndGlobalSSLContextParametersTest
specifier|public
class|class
name|FileToFtpsExplicitSSLWithoutClientAuthAndGlobalSSLContextParametersTest
extends|extends
name|FileToFtpsExplicitSSLWithoutClientAuthTest
block|{
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
literal|"server.jks"
argument_list|)
expr_stmt|;
name|ksp
operator|.
name|setPassword
argument_list|(
literal|"password"
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
name|setSecureSocketProtocol
argument_list|(
literal|"SSL"
argument_list|)
expr_stmt|;
name|sslContextParameters
operator|.
name|setTrustManagers
argument_list|(
name|tmp
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
literal|"ftps"
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
DECL|method|getFtpUrl ()
specifier|protected
name|String
name|getFtpUrl
parameter_list|()
block|{
return|return
literal|"ftps://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/tmp2/camel?password=admin&consumer.initialDelay=2000&disableSecureDataChannelDefaults=true"
operator|+
literal|"&isImplicit=false&delete=true"
return|;
block|}
block|}
end_class

end_unit

