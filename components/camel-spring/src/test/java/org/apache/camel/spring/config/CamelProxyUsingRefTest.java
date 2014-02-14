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
DECL|class|CamelProxyUsingRefTest
specifier|public
class|class
name|CamelProxyUsingRefTest
extends|extends
name|TestCase
block|{
DECL|method|testCamelProxyUsingRef ()
specifier|public
name|void
name|testCamelProxyUsingRef
parameter_list|()
throws|throws
name|Exception
block|{
name|AbstractApplicationContext
name|ac
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/config/CamelProxyUsingRefTest.xml"
argument_list|)
decl_stmt|;
name|MyProxySender
name|sender
init|=
name|ac
operator|.
name|getBean
argument_list|(
literal|"myProxySender"
argument_list|,
name|MyProxySender
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
name|sender
operator|.
name|hello
argument_list|(
literal|"World"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
comment|// we're done so let's properly close the application context
name|IOHelper
operator|.
name|close
argument_list|(
name|ac
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

