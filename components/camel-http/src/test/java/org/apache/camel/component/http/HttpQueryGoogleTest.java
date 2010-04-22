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

begin_comment
comment|/**  * Unit test to query Google using GET with endpoint having the query parameters.  */
end_comment

begin_class
DECL|class|HttpQueryGoogleTest
specifier|public
class|class
name|HttpQueryGoogleTest
extends|extends
name|CamelTestSupport
block|{
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Run manually"
argument_list|)
DECL|method|testQueryGoogle ()
specifier|public
name|void
name|testQueryGoogle
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://www.google.com/search?q=Camel"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Camel should be in search result from Google"
argument_list|,
name|data
operator|.
name|indexOf
argument_list|(
literal|"Camel"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

