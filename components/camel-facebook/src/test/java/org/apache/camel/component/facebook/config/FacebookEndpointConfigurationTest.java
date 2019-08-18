begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.facebook.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|facebook
operator|.
name|config
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
name|component
operator|.
name|facebook
operator|.
name|FacebookComponent
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
name|facebook
operator|.
name|FacebookEndpoint
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
name|Test
import|;
end_import

begin_class
DECL|class|FacebookEndpointConfigurationTest
specifier|public
class|class
name|FacebookEndpointConfigurationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testConfigurationBeanUriParam ()
specifier|public
name|void
name|testConfigurationBeanUriParam
parameter_list|()
throws|throws
name|Exception
block|{
name|FacebookComponent
name|component
init|=
operator|new
name|FacebookComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|FacebookEndpoint
name|facebookEndpoint
init|=
operator|(
name|FacebookEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"facebook://getFeed?configuration=#configuration"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Configuration bean wasn't taken into account!"
argument_list|,
literal|"fakeId"
argument_list|,
name|facebookEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOAuthAppId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Configuration bean wasn't taken into account!"
argument_list|,
literal|"fakeSecret"
argument_list|,
name|facebookEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOAuthAppSecret
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|BindToRegistry
argument_list|(
literal|"configuration"
argument_list|)
DECL|method|createConf ()
specifier|public
name|FacebookEndpointConfiguration
name|createConf
parameter_list|()
throws|throws
name|Exception
block|{
name|FacebookEndpointConfiguration
name|facebookEndpointConfiguration
init|=
operator|new
name|FacebookEndpointConfiguration
argument_list|()
decl_stmt|;
name|facebookEndpointConfiguration
operator|.
name|setOAuthAppId
argument_list|(
literal|"fakeId"
argument_list|)
expr_stmt|;
name|facebookEndpointConfiguration
operator|.
name|setOAuthAppSecret
argument_list|(
literal|"fakeSecret"
argument_list|)
expr_stmt|;
return|return
name|facebookEndpointConfiguration
return|;
block|}
block|}
end_class

end_unit

