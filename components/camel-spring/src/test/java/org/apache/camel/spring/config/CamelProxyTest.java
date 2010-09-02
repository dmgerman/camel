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
name|TestSupport
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
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|SpringCamelContext
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
name|ApplicationContext
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelProxyTest
specifier|public
class|class
name|CamelProxyTest
extends|extends
name|TestCase
block|{
DECL|method|testCamelProxy ()
specifier|public
name|void
name|testCamelProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|ApplicationContext
name|ac
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/config/CamelProxyTest.xml"
argument_list|)
decl_stmt|;
name|MyProxySender
name|sender
init|=
operator|(
name|MyProxySender
operator|)
name|ac
operator|.
name|getBean
argument_list|(
literal|"myProxySender"
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
comment|// test sending inOnly message
name|MyProxySender
name|anotherSender
init|=
operator|(
name|MyProxySender
operator|)
name|ac
operator|.
name|getBean
argument_list|(
literal|"myAnotherProxySender"
argument_list|)
decl_stmt|;
comment|// must type cast to work with Spring 2.5.x
name|SpringCamelContext
name|context
init|=
operator|(
name|SpringCamelContext
operator|)
name|ac
operator|.
name|getBeansOfType
argument_list|(
name|SpringCamelContext
operator|.
name|class
argument_list|)
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|MockEndpoint
name|result
init|=
name|TestSupport
operator|.
name|resolveMandatoryEndpoint
argument_list|(
name|context
argument_list|,
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello my friends!"
argument_list|)
expr_stmt|;
name|anotherSender
operator|.
name|greeting
argument_list|(
literal|"Hello my friends!"
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|result
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// test sending inOnly message with other sender
name|MyProxySender
name|myProxySenderWithCamelContextId
init|=
operator|(
name|MyProxySender
operator|)
name|ac
operator|.
name|getBean
argument_list|(
literal|"myProxySenderWithCamelContextId"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello my friends again!"
argument_list|)
expr_stmt|;
name|myProxySenderWithCamelContextId
operator|.
name|greeting
argument_list|(
literal|"Hello my friends again!"
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

