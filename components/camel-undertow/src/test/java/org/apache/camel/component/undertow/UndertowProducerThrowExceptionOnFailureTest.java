begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonParseException
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
name|CamelExecutionException
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
name|http
operator|.
name|common
operator|.
name|HttpOperationFailedException
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
name|model
operator|.
name|rest
operator|.
name|RestBindingMode
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
DECL|class|UndertowProducerThrowExceptionOnFailureTest
specifier|public
class|class
name|UndertowProducerThrowExceptionOnFailureTest
extends|extends
name|BaseUndertowTest
block|{
annotation|@
name|Test
DECL|method|testFailWithoutException ()
specifier|public
name|void
name|testFailWithoutException
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"undertow:http://localhost:{{port}}/fail?throwExceptionOnFailure=false"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Fail"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFailWithException ()
specifier|public
name|void
name|testFailWithException
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"undertow:http://localhost:{{port}}/fail?throwExceptionOnFailure=true"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|HttpOperationFailedException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|HttpOperationFailedException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|404
argument_list|,
name|cause
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testFailWithException2 ()
specifier|public
name|void
name|testFailWithException2
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|fluentTemplate
argument_list|()
operator|.
name|to
argument_list|(
literal|"undertow:http://localhost:{{port2}}/test/fail?throwExceptionOnFailure=true"
argument_list|)
operator|.
name|withHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"PUT"
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"This is not JSON format"
argument_list|)
operator|.
name|request
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|HttpOperationFailedException
name|httpException
init|=
name|assertIsInstanceOf
argument_list|(
name|HttpOperationFailedException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|400
argument_list|,
name|httpException
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/plain"
argument_list|,
name|httpException
operator|.
name|getResponseHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Invalid json data"
argument_list|,
name|httpException
operator|.
name|getResponseBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|restConfiguration
argument_list|()
operator|.
name|component
argument_list|(
literal|"undertow"
argument_list|)
operator|.
name|port
argument_list|(
name|getPort2
argument_list|()
argument_list|)
operator|.
name|bindingMode
argument_list|(
name|RestBindingMode
operator|.
name|json
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|JsonParseException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|constant
argument_list|(
literal|400
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|constant
argument_list|(
literal|"text/plain"
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Invalid json data"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/test"
argument_list|)
operator|.
name|put
argument_list|(
literal|"/fail"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"undertow:http://localhost:{{port}}/fail"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
operator|.
name|constant
argument_list|(
literal|404
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Fail"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

