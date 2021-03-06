begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.lambda.integration
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
operator|.
name|integration
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|*
import|;
end_import

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
name|model
operator|.
name|CreateFunctionResult
import|;
end_import

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
name|model
operator|.
name|DeleteFunctionResult
import|;
end_import

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
name|model
operator|.
name|GetFunctionResult
import|;
end_import

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
name|model
operator|.
name|ListFunctionsResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|util
operator|.
name|IOUtils
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
name|Exchange
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
name|ExchangePattern
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
name|Processor
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
name|builder
operator|.
name|RouteBuilder
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
name|aws
operator|.
name|lambda
operator|.
name|LambdaConstants
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
name|Ignore
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
annotation|@
name|Ignore
argument_list|(
literal|"Must be manually tested. Provide your own accessKey and secretKey!"
argument_list|)
DECL|class|LambdaComponentIntegrationTest
specifier|public
class|class
name|LambdaComponentIntegrationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|lambdaCreateFunctionTest ()
specifier|public
name|void
name|lambdaCreateFunctionTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:createFunction"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|LambdaConstants
operator|.
name|RUNTIME
argument_list|,
literal|"nodejs6.10"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|LambdaConstants
operator|.
name|HANDLER
argument_list|,
literal|"GetHelloWithName.handler"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|LambdaConstants
operator|.
name|DESCRIPTION
argument_list|,
literal|"Hello with node.js on Lambda"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|LambdaConstants
operator|.
name|ROLE
argument_list|,
literal|"arn:aws:iam::643534317684:role/lambda-execution-role"
argument_list|)
expr_stmt|;
name|ClassLoader
name|classLoader
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|classLoader
operator|.
name|getResource
argument_list|(
literal|"org/apache/camel/component/aws/lambda/function/node/GetHelloWithName.zip"
argument_list|)
operator|.
name|getFile
argument_list|()
argument_list|)
decl_stmt|;
name|FileInputStream
name|inputStream
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|inputStream
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|CreateFunctionResult
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|CreateFunctionResult
operator|.
name|class
argument_list|)
operator|.
name|getFunctionName
argument_list|()
argument_list|,
literal|"GetHelloWithName"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|lambdaListFunctionsTest ()
specifier|public
name|void
name|lambdaListFunctionsTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:listFunctions"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{              }
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|ListFunctionsResult
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|ListFunctionsResult
operator|.
name|class
argument_list|)
operator|.
name|getFunctions
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|lambdaGetFunctionTest ()
specifier|public
name|void
name|lambdaGetFunctionTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:getFunction"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{              }
block|}
argument_list|)
decl_stmt|;
name|GetFunctionResult
name|result
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|GetFunctionResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|result
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFunctionName
argument_list|()
argument_list|,
literal|"GetHelloWithName"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|result
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRuntime
argument_list|()
argument_list|,
literal|"nodejs6.10"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|lambdaInvokeFunctionTest ()
specifier|public
name|void
name|lambdaInvokeFunctionTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:invokeFunction"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"{\"name\":\"Camel\"}"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
literal|"{\"Hello\":\"Camel\"}"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|lambdaDeleteFunctionTest ()
specifier|public
name|void
name|lambdaDeleteFunctionTest
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:deleteFunction"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{             }
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|DeleteFunctionResult
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:createFunction"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-lambda://GetHelloWithName?operation=createFunction&accessKey=xxxx&secretKey=xxxx&awsLambdaEndpoint=lambda.eu-central-1.amazonaws.com"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:listFunctions"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-lambda://myFunction?operation=listFunctions&accessKey=xxxx&secretKey=xxxx&awsLambdaEndpoint=lambda.eu-central-1.amazonaws.com"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:getFunction"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-lambda://GetHelloWithName?operation=getFunction&accessKey=xxxx&secretKey=xxxx&awsLambdaEndpoint=lambda.eu-central-1.amazonaws.com"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:invokeFunction"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-lambda://GetHelloWithName?operation=invokeFunction&accessKey=xxxx&secretKey=xxxx&awsLambdaEndpoint=lambda.eu-central-1.amazonaws.com"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:deleteFunction"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-lambda://GetHelloWithName?operation=deleteFunction&accessKey=xxxx&secretKey=xxxx&awsLambdaEndpoint=lambda.eu-central-1.amazonaws.com"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

