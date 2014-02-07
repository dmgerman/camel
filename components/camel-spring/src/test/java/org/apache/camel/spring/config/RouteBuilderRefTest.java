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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|RouteBuilderRefTest
specifier|public
class|class
name|RouteBuilderRefTest
extends|extends
name|XmlConfigTestSupport
block|{
DECL|method|testUsingRouteBuilderRefInCamelXml ()
specifier|public
name|void
name|testUsingRouteBuilderRefInCamelXml
parameter_list|()
throws|throws
name|Exception
block|{
name|AbstractApplicationContext
name|applicationContext
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/config/routeBuilderRef.xml"
argument_list|)
decl_stmt|;
name|CamelContext
name|context
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"camel5"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertValidContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
comment|// we're done so let's properly close the application context
name|IOHelper
operator|.
name|close
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

