begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|config
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
DECL|class|DefaultErrorHandlerConfigTest
specifier|public
class|class
name|DefaultErrorHandlerConfigTest
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
name|Autowired
DECL|field|context
specifier|protected
name|CamelContext
name|context
decl_stmt|;
DECL|field|expectedBody
specifier|protected
name|Object
name|expectedBody
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
DECL|method|testRouteInheritsConfiguredErrorHandler ()
specifier|public
name|void
name|testRouteInheritsConfiguredErrorHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|assertReceivedMessageWithErrorHandlerValue
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|testRouteConfiguredErrorHandler ()
specifier|public
name|void
name|testRouteConfiguredErrorHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|assertReceivedMessageWithErrorHandlerValue
argument_list|(
literal|"bar"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|assertReceivedMessageWithErrorHandlerValue (String name, String value)
specifier|protected
name|void
name|assertReceivedMessageWithErrorHandlerValue
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:"
operator|+
name|name
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|MockEndpoint
operator|.
name|resolve
argument_list|(
name|context
argument_list|,
literal|"mock:"
operator|+
name|name
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

