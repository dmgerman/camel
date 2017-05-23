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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|UndertowPrefixMatchingTest
specifier|public
class|class
name|UndertowPrefixMatchingTest
extends|extends
name|BaseUndertowTest
block|{
annotation|@
name|Test
DECL|method|passOnExactPath ()
specifier|public
name|void
name|passOnExactPath
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:{{port}}/myapp/suffix"
argument_list|,
literal|"Hello Camel!"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:myapp"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|failsOnPrefixPath ()
specifier|public
name|void
name|failsOnPrefixPath
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:{{port}}/myapp"
argument_list|,
literal|"Hello Camel!"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should fail, something is wrong"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|ex
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
name|ex
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
DECL|method|passOnPrefixPath ()
specifier|public
name|void
name|passOnPrefixPath
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:{{port}}/bar/somethingNotImportant"
argument_list|,
literal|"Hello Camel!"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:myapp"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
literal|200
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
literal|"undertow:http://localhost:{{port}}/myapp/suffix"
argument_list|)
operator|.
name|transform
argument_list|(
name|bodyAs
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|append
argument_list|(
literal|" Must match exact path"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:myapp"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"undertow:http://localhost:{{port}}/bar?matchOnUriPrefix=true"
argument_list|)
operator|.
name|transform
argument_list|(
name|bodyAs
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|append
argument_list|(
literal|" Matching prefix"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

