begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.bind
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|bind
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|component
operator|.
name|jms
operator|.
name|JmsBinding
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
name|test
operator|.
name|junit4
operator|.
name|CamelSpringTestSupport
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
DECL|class|JmsMessageBindTest
specifier|public
class|class
name|JmsMessageBindTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Test
DECL|method|testSendAMessageToBean ()
specifier|public
name|void
name|testSendAMessageToBean
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Completed"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
comment|// this header should not be sent as its value cannot be serialized
name|headers
operator|.
name|put
argument_list|(
literal|"binding"
argument_list|,
operator|new
name|JmsBinding
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"activemq:Test.BindingQueue"
argument_list|,
literal|"SomeBody"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
comment|// lets wait for the method to be invoked
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// now lets test that the bean is correct
name|MyBean
name|bean
init|=
name|getMandatoryBean
argument_list|(
name|MyBean
operator|.
name|class
argument_list|,
literal|"myBean"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"body"
argument_list|,
literal|"SomeBody"
argument_list|,
name|bean
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|beanHeaders
init|=
name|bean
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No headers!"
argument_list|,
name|beanHeaders
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo header"
argument_list|,
literal|"bar"
argument_list|,
name|beanHeaders
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Should get a null value"
argument_list|,
name|beanHeaders
operator|.
name|get
argument_list|(
literal|"binding"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/jms/bind/spring.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

