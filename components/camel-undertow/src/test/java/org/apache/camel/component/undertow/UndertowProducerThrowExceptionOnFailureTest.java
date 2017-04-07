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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"CAMEL-11111"
argument_list|)
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
try|try
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
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should not throw an exception"
argument_list|)
expr_stmt|;
block|}
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
name|String
name|out
init|=
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
decl_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//t.printStackTrace();
name|assertNotNull
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
name|fail
argument_list|(
literal|"Should throw an exception"
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

