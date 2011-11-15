begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.irc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|irc
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

begin_class
annotation|@
name|Ignore
DECL|class|IrcsWithSslContextParamsRouteTest
specifier|public
class|class
name|IrcsWithSslContextParamsRouteTest
extends|extends
name|IrcRouteTest
block|{
comment|// TODO This test is disabled until we can find a public SSL enabled IRC
comment|// server to test against. To use this test, follow the following procedures:
comment|// 1) Download and install UnrealIRCd 3.2.9 from http://www.unrealircd.com/
comment|// 2) Copy the contents of the src/test/unrealircd folder into the installation
comment|//    folder of UnrealIRCd.
comment|// 3) Start UnrealIRCd and execute this test.  Often the test executes quicker than
comment|//    the IRC server responds and the assertion will fail.  In order to get the test to
comment|//    pass reliably, you may need to set a break point in IrcEndpoint#joinChanel in order
comment|//    to slow the route creation down enough for the event listener to be in place
comment|//    when camel-con joins the room.
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
literal|"localhost.ks"
argument_list|)
expr_stmt|;
name|ksp
operator|.
name|setPassword
argument_list|(
literal|"changeit"
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
name|setTrustManagers
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
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
literal|"sslContextParameters"
argument_list|,
name|sslContextParameters
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Override
DECL|method|sendUri ()
specifier|protected
name|String
name|sendUri
parameter_list|()
block|{
return|return
literal|"ircs://camel-prd-user@localhost:6669/#camel-test?nickname=camel-prd&password=password&sslContextParameters=#sslContextParameters"
return|;
block|}
annotation|@
name|Override
DECL|method|fromUri ()
specifier|protected
name|String
name|fromUri
parameter_list|()
block|{
return|return
literal|"ircs://camel-con-user@localhost:6669/#camel-test?nickname=camel-con&password=password&sslContextParameters=#sslContextParameters"
return|;
block|}
block|}
end_class

end_unit

