begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|jetty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|ValidationException
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
DECL|class|JettyValidatorTest
specifier|public
class|class
name|JettyValidatorTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testValideRequest ()
specifier|public
name|void
name|testValideRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|inputStream
init|=
name|HttpClient
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"ValidRequest.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"the inputStream should not be null"
argument_list|,
name|inputStream
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|HttpClient
operator|.
name|send
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The response should be ok"
argument_list|,
name|response
argument_list|,
literal|"<ok/>"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalideRequest ()
specifier|public
name|void
name|testInvalideRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|inputStream
init|=
name|HttpClient
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"InvalidRequest.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"the inputStream should not be null"
argument_list|,
name|inputStream
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|HttpClient
operator|.
name|send
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The response should be error"
argument_list|,
name|response
argument_list|,
literal|"<error/>"
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"jetty:http://localhost:8192/test"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:in"
argument_list|)
operator|.
name|doTry
argument_list|()
operator|.
name|to
argument_list|(
literal|"validator:OptimizationRequest.xsd"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"<ok/>"
argument_list|)
argument_list|)
operator|.
name|doCatch
argument_list|(
name|ValidationException
operator|.
name|class
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"<error/>"
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:out"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

