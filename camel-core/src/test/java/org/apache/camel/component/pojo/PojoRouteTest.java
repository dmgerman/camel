begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pojo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pojo
package|;
end_package

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
name|component
operator|.
name|bean
operator|.
name|ProxyHelper
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
name|impl
operator|.
name|DefaultCamelContext
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
name|util
operator|.
name|jndi
operator|.
name|JndiContext
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|PojoRouteTest
specifier|public
class|class
name|PojoRouteTest
extends|extends
name|TestCase
block|{
DECL|method|testPojoRoutes ()
specifier|public
name|void
name|testPojoRoutes
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: register
comment|// lets populate the context with the services we need
comment|// note that we could just use a spring.xml file to avoid this step
name|JndiContext
name|context
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"bye"
argument_list|,
operator|new
name|SayService
argument_list|(
literal|"Good Bye!"
argument_list|)
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
comment|// END SNIPPET: register
comment|// START SNIPPET: route
comment|// lets add simple route
name|camelContext
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:hello"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:bye"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// END SNIPPET: route
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// START SNIPPET: invoke
name|Endpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"direct:hello"
argument_list|)
decl_stmt|;
name|ISay
name|proxy
init|=
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
name|ISay
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|rc
init|=
name|proxy
operator|.
name|say
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Good Bye!"
argument_list|,
name|rc
argument_list|)
expr_stmt|;
comment|// END SNIPPET: invoke
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

