begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.lambda
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
name|lambda
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|lambda
operator|.
name|AWSLambdaClient
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

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_class
DECL|class|LambdaComponentClientRegistryTest
specifier|public
class|class
name|LambdaComponentClientRegistryTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|createEndpointWithMinimalKMSClientConfiguration ()
specifier|public
name|void
name|createEndpointWithMinimalKMSClientConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|AWSLambdaClient
name|awsLambdaClient
init|=
name|mock
argument_list|(
name|AWSLambdaClient
operator|.
name|class
argument_list|)
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"awsLambdaClient"
argument_list|,
name|awsLambdaClient
argument_list|)
expr_stmt|;
name|LambdaComponent
name|component
init|=
operator|new
name|LambdaComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|LambdaEndpoint
name|endpoint
init|=
operator|(
name|LambdaEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-lambda://myFunction?operation=getFunction&awsLambdaClient=#awsLambdaClient&accessKey=xxx&secretKey=yyy"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAwsLambdaClient
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
DECL|method|createEndpointWithMinimalKMSClientMisconfiguration ()
specifier|public
name|void
name|createEndpointWithMinimalKMSClientMisconfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|LambdaComponent
name|component
init|=
operator|new
name|LambdaComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|LambdaEndpoint
name|endpoint
init|=
operator|(
name|LambdaEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-lambda://myFunction?operation=getFunction&awsLambdaClient=#awsLambdaClient&accessKey=xxx&secretKey=yyy"
argument_list|)
decl_stmt|;
block|}
block|}
end_class

end_unit

