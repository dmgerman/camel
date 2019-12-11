begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.msk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|msk
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
DECL|class|MSKComponentClientRegistryTest
specifier|public
class|class
name|MSKComponentClientRegistryTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|createEndpointWithMinimalMSKClientConfiguration ()
specifier|public
name|void
name|createEndpointWithMinimalMSKClientConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonMSKClientMock
name|awsMSKClient
init|=
operator|new
name|AmazonMSKClientMock
argument_list|()
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"awsMskClient"
argument_list|,
name|awsMSKClient
argument_list|)
expr_stmt|;
name|MSKComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"aws-msk"
argument_list|,
name|MSKComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|MSKEndpoint
name|endpoint
init|=
operator|(
name|MSKEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-msk://label"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMskClient
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
DECL|method|createEndpointWithMinimalMSKClientMisconfiguration ()
specifier|public
name|void
name|createEndpointWithMinimalMSKClientMisconfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|MSKComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"aws-msk"
argument_list|,
name|MSKComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|MSKEndpoint
name|endpoint
init|=
operator|(
name|MSKEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-msk://label"
argument_list|)
decl_stmt|;
block|}
block|}
end_class

end_unit

