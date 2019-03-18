begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.docker.ssl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|docker
operator|.
name|ssl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyManagementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStoreException
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
name|java
operator|.
name|security
operator|.
name|UnrecoverableKeyException
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
name|SSLContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|core
operator|.
name|SSLConfig
import|;
end_import

begin_comment
comment|/**  * A marker implementation of {@link SSLConfig}  */
end_comment

begin_class
DECL|class|NoImplSslConfig
specifier|public
class|class
name|NoImplSslConfig
implements|implements
name|SSLConfig
block|{
annotation|@
name|Override
DECL|method|getSSLContext ()
specifier|public
name|SSLContext
name|getSSLContext
parameter_list|()
throws|throws
name|KeyManagementException
throws|,
name|UnrecoverableKeyException
throws|,
name|NoSuchAlgorithmException
throws|,
name|KeyStoreException
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

