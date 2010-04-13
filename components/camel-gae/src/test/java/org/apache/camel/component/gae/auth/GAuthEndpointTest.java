begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.auth
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|auth
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|net
operator|.
name|URLEncoder
operator|.
name|encode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|auth
operator|.
name|GAuthEndpoint
operator|.
name|Name
operator|.
name|AUTHORIZE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|auth
operator|.
name|GAuthEndpoint
operator|.
name|Name
operator|.
name|UPGRADE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|auth
operator|.
name|GAuthTestUtils
operator|.
name|createComponent
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|GAuthEndpointTest
specifier|public
class|class
name|GAuthEndpointTest
block|{
DECL|field|component
specifier|private
name|GAuthComponent
name|component
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|component
operator|=
name|createComponent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCustomParams ()
specifier|public
name|void
name|testCustomParams
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|scope
init|=
literal|"http://scope1.example.org,http://scope2.example.org"
decl_stmt|;
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"gauth://authorize"
argument_list|)
operator|.
name|append
argument_list|(
literal|"?"
argument_list|)
operator|.
name|append
argument_list|(
literal|"scope="
operator|+
name|encode
argument_list|(
name|scope
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
literal|"callback="
operator|+
name|encode
argument_list|(
literal|"http://callback.example.org"
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
literal|"consumerKey=customConsumerKey"
argument_list|)
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
literal|"consumerSecret=customConsumerSecret"
argument_list|)
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
literal|"authorizeBindingRef=#customAuthorizeBinding"
argument_list|)
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
literal|"upgradeBindingRef=#customUpgradeBinding"
argument_list|)
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
literal|"keyLoaderRef=#gAuthKeyLoader"
argument_list|)
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
literal|"serviceRef=#gAuthService"
argument_list|)
decl_stmt|;
name|GAuthEndpoint
name|endpoint
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|AUTHORIZE
argument_list|,
name|endpoint
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"http://scope1.example.org"
block|,
literal|"http://scope2.example.org"
block|}
argument_list|,
name|endpoint
operator|.
name|getScopeArray
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|scope
argument_list|,
name|endpoint
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://callback.example.org"
argument_list|,
name|endpoint
operator|.
name|getCallback
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"customConsumerKey"
argument_list|,
name|endpoint
operator|.
name|getConsumerKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"customConsumerSecret"
argument_list|,
name|endpoint
operator|.
name|getConsumerSecret
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|endpoint
operator|.
name|getAuthorizeBinding
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|GAuthAuthorizeBinding
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|endpoint
operator|.
name|getUpgradeBinding
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|GAuthUpgradeBinding
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getAuthorizeBinding
argument_list|()
operator|instanceof
name|GAuthAuthorizeBinding
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getUpgradeBinding
argument_list|()
operator|instanceof
name|GAuthUpgradeBinding
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getKeyLoader
argument_list|()
operator|instanceof
name|GAuthPk8Loader
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getService
argument_list|()
operator|instanceof
name|GAuthServiceMock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultParams ()
specifier|public
name|void
name|testDefaultParams
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|.
name|setConsumerKey
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|component
operator|.
name|setConsumerSecret
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|GAuthEndpoint
name|endpoint
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"gauth:authorize"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getCallback
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConsumerKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConsumerSecret
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getKeyLoader
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getAuthorizeBinding
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|GAuthAuthorizeBinding
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getUpgradeBinding
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|GAuthUpgradeBinding
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|GAuthServiceImpl
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testComponentDefaultParams ()
specifier|public
name|void
name|testComponentDefaultParams
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|.
name|setKeyLoader
argument_list|(
operator|new
name|GAuthPk8Loader
argument_list|()
argument_list|)
expr_stmt|;
name|GAuthEndpoint
name|endpoint
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"gauth:authorize"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getCallback
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testConsumerKey"
argument_list|,
name|endpoint
operator|.
name|getConsumerKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testConsumerSecret"
argument_list|,
name|endpoint
operator|.
name|getConsumerSecret
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getAuthorizeBinding
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|GAuthAuthorizeBinding
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getUpgradeBinding
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|GAuthUpgradeBinding
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|GAuthServiceImpl
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getKeyLoader
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|GAuthPk8Loader
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateUpgradeEndpoint ()
specifier|public
name|void
name|testCreateUpgradeEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|GAuthEndpoint
name|endpoint
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"gauth:upgrade"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|UPGRADE
argument_list|,
name|endpoint
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|testInvalidEndpointName ()
specifier|public
name|void
name|testInvalidEndpointName
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"gauth:invalid"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

