begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|ssl
operator|.
name|SslContextFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_class
DECL|class|SecurityUtils
specifier|public
class|class
name|SecurityUtils
block|{
DECL|method|adaptToIBMCipherNames (final SslContextFactory sslContextFactory)
specifier|public
specifier|static
name|void
name|adaptToIBMCipherNames
parameter_list|(
specifier|final
name|SslContextFactory
name|sslContextFactory
parameter_list|)
block|{
comment|//jetty client adds into excluded cipher suites all ciphers starting with SSL_
comment|//it makes sense for Oracle jdk, but in IBM jdk all ciphers starts with SSL_, even ciphers for TLS
comment|//see https://github.com/eclipse/jetty.project/issues/2921
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.vendor"
argument_list|)
operator|.
name|contains
argument_list|(
literal|"IBM"
argument_list|)
condition|)
block|{
name|String
index|[]
name|excludedCiphersWithoutSSLExclusion
init|=
name|Arrays
operator|.
name|stream
argument_list|(
name|sslContextFactory
operator|.
name|getExcludeCipherSuites
argument_list|()
argument_list|)
operator|.
name|filter
argument_list|(
name|cipher
lambda|->
operator|!
name|cipher
operator|.
name|equals
argument_list|(
literal|"^SSL_.*$"
argument_list|)
argument_list|)
operator|.
name|toArray
argument_list|(
name|String
index|[]
operator|::
operator|new
argument_list|)
decl_stmt|;
name|sslContextFactory
operator|.
name|setExcludeCipherSuites
argument_list|(
name|excludedCiphersWithoutSSLExclusion
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

