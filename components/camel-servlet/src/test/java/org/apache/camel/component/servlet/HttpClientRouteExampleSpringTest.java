begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servlet
package|;
end_package

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|GetMethodWebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|servletunit
operator|.
name|ServletUnitClient
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
DECL|class|HttpClientRouteExampleSpringTest
specifier|public
class|class
name|HttpClientRouteExampleSpringTest
extends|extends
name|ServletCamelRouterTestSupport
block|{
annotation|@
name|Test
DECL|method|testHttpRestricMethod ()
specifier|public
name|void
name|testHttpRestricMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|ServletUnitClient
name|client
init|=
name|newClient
argument_list|()
decl_stmt|;
comment|// Send a web get method request
name|WebRequest
name|req
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
name|CONTEXT_URL
operator|+
literal|"/services/hello"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|client
operator|.
name|getResponse
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response message."
argument_list|,
literal|"OK"
argument_list|,
name|response
operator|.
name|getResponseMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response text."
argument_list|,
literal|"Add a name parameter to uri, eg ?name=foo"
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|req
operator|=
operator|new
name|GetMethodWebRequest
argument_list|(
name|CONTEXT_URL
operator|+
literal|"/services/hello?name=Willem"
argument_list|)
expr_stmt|;
name|response
operator|=
name|client
operator|.
name|getResponse
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response text."
argument_list|,
literal|"Hello Willem how are you?"
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|startCamelContext
operator|=
literal|false
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|protected
name|String
name|getConfiguration
parameter_list|()
block|{
return|return
literal|"/org/apache/camel/component/servlet/web-example.xml"
return|;
block|}
block|}
end_class

end_unit

