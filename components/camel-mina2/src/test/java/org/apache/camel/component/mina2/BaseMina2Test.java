begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina2
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
name|BindToRegistry
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
name|support
operator|.
name|jsse
operator|.
name|ClientAuthentication
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
name|camel
operator|.
name|support
operator|.
name|jsse
operator|.
name|SSLContextServerParameters
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
name|TrustManagersParameters
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
name|AvailablePortFinder
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
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_class
DECL|class|BaseMina2Test
specifier|public
class|class
name|BaseMina2Test
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
DECL|field|port
specifier|private
specifier|static
specifier|volatile
name|int
name|port
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|initPort ()
specifier|public
specifier|static
name|void
name|initPort
parameter_list|()
throws|throws
name|Exception
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
block|}
DECL|method|getNextPort ()
specifier|protected
name|int
name|getNextPort
parameter_list|()
block|{
return|return
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
return|;
block|}
DECL|method|getPort ()
specifier|protected
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|isUseSslContext ()
specifier|protected
name|boolean
name|isUseSslContext
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|BindToRegistry
argument_list|(
literal|"sslContextParameters"
argument_list|)
DECL|method|createSslContextParameters ()
specifier|public
name|SSLContextParameters
name|createSslContextParameters
parameter_list|()
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
comment|// NOTE: Needed since the client uses a loose trust configuration when no ssl context
comment|// is provided.  We turn on WANT client-auth to prefer using authentication
name|SSLContextServerParameters
name|scsp
init|=
operator|new
name|SSLContextServerParameters
argument_list|()
decl_stmt|;
name|scsp
operator|.
name|setClientAuthentication
argument_list|(
name|ClientAuthentication
operator|.
name|WANT
operator|.
name|name
argument_list|()
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
name|sslContextParameters
operator|.
name|setServerParameters
argument_list|(
name|scsp
argument_list|)
expr_stmt|;
return|return
name|sslContextParameters
return|;
block|}
block|}
end_class

end_unit

