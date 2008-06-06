begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|Endpoint
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
name|ProducerTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit38
operator|.
name|AbstractJUnit38SpringContextTests
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|BeanRouteUsingSpringEndpointTest
specifier|public
class|class
name|BeanRouteUsingSpringEndpointTest
extends|extends
name|AbstractJUnit38SpringContextTests
block|{
annotation|@
name|Autowired
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Resource
DECL|field|helloEndpoint
specifier|protected
name|Endpoint
name|helloEndpoint
decl_stmt|;
annotation|@
name|Resource
DECL|field|goodbyeEndpoint
specifier|protected
name|Endpoint
name|goodbyeEndpoint
decl_stmt|;
DECL|field|body
specifier|protected
name|String
name|body
init|=
literal|"James"
decl_stmt|;
DECL|method|testSayHello ()
specifier|public
name|void
name|testSayHello
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|helloEndpoint
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|goodbyeEndpoint
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|template
operator|.
name|sendBody
argument_list|(
name|helloEndpoint
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Returned value"
argument_list|,
literal|"Hello James!"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testSayGoodbye ()
specifier|public
name|void
name|testSayGoodbye
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|value
init|=
name|template
operator|.
name|sendBody
argument_list|(
name|goodbyeEndpoint
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Returned value"
argument_list|,
literal|"Bye James!"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

