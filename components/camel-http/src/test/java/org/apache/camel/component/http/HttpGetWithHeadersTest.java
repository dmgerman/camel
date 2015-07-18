begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
package|;
end_package

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
name|Before
import|;
end_import

begin_class
DECL|class|HttpGetWithHeadersTest
specifier|public
class|class
name|HttpGetWithHeadersTest
extends|extends
name|HttpGetTest
block|{
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
literal|"direct:start"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"TestHeader"
argument_list|,
name|constant
argument_list|(
literal|"test"
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"Content-Length"
argument_list|,
name|constant
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"Accept-Language"
argument_list|,
name|constant
argument_list|(
literal|"pl"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://www.google.com/search"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// "Szukaj" is "Search" in polish language
name|expectedText
operator|=
literal|"Szukaj"
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|checkHeaders (Map<String, Object> headers)
specifier|protected
name|void
name|checkHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Should be more than one header but was: "
operator|+
name|headers
argument_list|,
name|headers
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should get the TestHeader"
argument_list|,
literal|"test"
argument_list|,
name|headers
operator|.
name|get
argument_list|(
literal|"TestHeader"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

