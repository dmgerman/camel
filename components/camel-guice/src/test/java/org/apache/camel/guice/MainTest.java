begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|CamelContext
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|MainTest
specifier|public
class|class
name|MainTest
extends|extends
name|Assert
block|{
DECL|field|main
specifier|protected
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
DECL|field|uri
specifier|protected
name|String
name|uri
init|=
literal|"mock:results"
decl_stmt|;
DECL|field|expectedBody
specifier|protected
name|Object
name|expectedBody
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
annotation|@
name|Test
DECL|method|testMain ()
specifier|public
name|void
name|testMain
parameter_list|()
throws|throws
name|Exception
block|{
name|main
operator|.
name|start
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|CamelContext
argument_list|>
name|contexts
init|=
name|main
operator|.
name|getCamelContexts
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Expected size : "
operator|+
name|contexts
argument_list|,
literal|1
argument_list|,
name|contexts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|contexts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ProducerTemplate
name|template
init|=
name|main
operator|.
name|getCamelTemplate
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"should have a template!"
argument_list|,
name|template
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|uri
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|main
operator|!=
literal|null
condition|)
block|{
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

