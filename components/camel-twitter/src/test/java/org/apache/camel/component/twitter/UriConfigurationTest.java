begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
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
name|Endpoint
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
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|UriConfigurationTest
specifier|public
class|class
name|UriConfigurationTest
extends|extends
name|Assert
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|field|support
specifier|private
name|CamelTwitterTestSupport
name|support
init|=
operator|new
name|CamelTwitterTestSupport
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testBasicAuthentication ()
specifier|public
name|void
name|testBasicAuthentication
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"twitter:search?"
operator|+
name|support
operator|.
name|getUriTokens
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Endpoint not a TwitterEndpoint: "
operator|+
name|endpoint
argument_list|,
name|endpoint
operator|instanceof
name|TwitterEndpoint
argument_list|)
expr_stmt|;
name|TwitterEndpoint
name|twitterEndpoint
init|=
operator|(
name|TwitterEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|twitterEndpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getConsumerKey
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|twitterEndpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getConsumerSecret
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|twitterEndpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getAccessToken
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|twitterEndpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getAccessTokenSecret
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPageSetting ()
specifier|public
name|void
name|testPageSetting
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"twitter:search?count=50&numberOfPages=2"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Endpoint not a TwitterEndpoint: "
operator|+
name|endpoint
argument_list|,
name|endpoint
operator|instanceof
name|TwitterEndpoint
argument_list|)
expr_stmt|;
name|TwitterEndpoint
name|twitterEndpoint
init|=
operator|(
name|TwitterEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|50
argument_list|)
argument_list|,
name|twitterEndpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
argument_list|,
name|twitterEndpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpProxySetting ()
specifier|public
name|void
name|testHttpProxySetting
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"twitter:search?httpProxyHost=example.com&httpProxyPort=3338&httpProxyUser=test&httpProxyPassword=pwd"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Endpoint not a TwitterEndpoint: "
operator|+
name|endpoint
argument_list|,
name|endpoint
operator|instanceof
name|TwitterEndpoint
argument_list|)
expr_stmt|;
name|TwitterEndpoint
name|twitterEndpoint
init|=
operator|(
name|TwitterEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
literal|"example.com"
argument_list|,
name|twitterEndpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getHttpProxyHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|3338
argument_list|)
argument_list|,
name|twitterEndpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getHttpProxyPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|twitterEndpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getHttpProxyUser
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pwd"
argument_list|,
name|twitterEndpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getHttpProxyPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

