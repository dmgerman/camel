begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atmos
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atmos
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|util
operator|.
name|URISupport
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
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
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
name|mockito
operator|.
name|AdditionalAnswers
operator|.
name|returnsFirstArg
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|anyString
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|AtmosComponentTest
specifier|public
class|class
name|AtmosComponentTest
block|{
DECL|field|FAKE_REMOTE_PATH
specifier|private
specifier|static
specifier|final
name|String
name|FAKE_REMOTE_PATH
init|=
literal|"/remote"
decl_stmt|;
DECL|field|FAKE_SECRET
specifier|private
specifier|static
specifier|final
name|String
name|FAKE_SECRET
init|=
literal|"fake-secret"
decl_stmt|;
DECL|field|FAKE_TOKEN
specifier|private
specifier|static
specifier|final
name|String
name|FAKE_TOKEN
init|=
literal|"fake-token"
decl_stmt|;
DECL|field|FAKE_URI
specifier|private
specifier|static
specifier|final
name|String
name|FAKE_URI
init|=
literal|"http://fake/uri"
decl_stmt|;
annotation|@
name|Mock
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Test
DECL|method|testComponentOptions ()
specifier|public
name|void
name|testComponentOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|then
argument_list|(
name|returnsFirstArg
argument_list|()
argument_list|)
expr_stmt|;
name|AtmosComponent
name|component
init|=
operator|new
name|AtmosComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|setFullTokenId
argument_list|(
name|FAKE_TOKEN
argument_list|)
expr_stmt|;
name|component
operator|.
name|setSecretKey
argument_list|(
name|FAKE_SECRET
argument_list|)
expr_stmt|;
name|component
operator|.
name|setSslValidation
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|component
operator|.
name|setUri
argument_list|(
name|FAKE_URI
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"remotePath"
argument_list|,
name|FAKE_REMOTE_PATH
argument_list|)
expr_stmt|;
name|AtmosEndpoint
name|endpoint
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"atmos://foo?remotePath=/remote"
argument_list|,
literal|"foo/get"
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|AtmosConfiguration
name|configuration
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|FAKE_TOKEN
argument_list|,
name|configuration
operator|.
name|getFullTokenId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|FAKE_SECRET
argument_list|,
name|configuration
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|configuration
operator|.
name|isSslValidation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|FAKE_URI
argument_list|,
name|configuration
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUriParamsOverrideComponentOptions ()
specifier|public
name|void
name|testUriParamsOverrideComponentOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|then
argument_list|(
name|returnsFirstArg
argument_list|()
argument_list|)
expr_stmt|;
name|AtmosComponent
name|component
init|=
operator|new
name|AtmosComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|setFullTokenId
argument_list|(
literal|"fakeTokenToBeOverridden"
argument_list|)
expr_stmt|;
name|component
operator|.
name|setSecretKey
argument_list|(
literal|"fakeSecretToBeOverridden"
argument_list|)
expr_stmt|;
name|component
operator|.
name|setSslValidation
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|component
operator|.
name|setUri
argument_list|(
literal|"http://fake/uri/to/be/overridden"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"remotePath"
argument_list|,
name|FAKE_REMOTE_PATH
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"fullTokenId"
argument_list|,
name|FAKE_TOKEN
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"secretKey"
argument_list|,
name|FAKE_SECRET
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"sslValidation"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
literal|"uri"
argument_list|,
name|FAKE_URI
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|URISupport
operator|.
name|appendParametersToURI
argument_list|(
literal|"atmos://foo"
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|AtmosEndpoint
name|endpoint
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
literal|"foo/get"
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|AtmosConfiguration
name|configuration
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|FAKE_TOKEN
argument_list|,
name|configuration
operator|.
name|getFullTokenId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|FAKE_SECRET
argument_list|,
name|configuration
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|configuration
operator|.
name|isSslValidation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|FAKE_URI
argument_list|,
name|configuration
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

