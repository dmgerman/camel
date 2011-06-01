begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.transport
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|transport
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
DECL|class|CamelJBIClientProxyTest
specifier|public
class|class
name|CamelJBIClientProxyTest
block|{
DECL|field|proxy
specifier|private
name|HelloService
name|proxy
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|ClassPathXmlApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
comment|// setup service
name|applicationContext
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"/org/apache/camel/component/cxf/transport/CamelJBIClientProxy.xml"
block|}
argument_list|)
expr_stmt|;
name|applicationContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|proxy
operator|=
operator|(
name|HelloService
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"client"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The proxy should not be null."
argument_list|,
name|proxy
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCallFromProxy ()
specifier|public
name|void
name|testCallFromProxy
parameter_list|()
throws|throws
name|Exception
block|{
comment|// The echo parameter will be ignore, since the service has the fix response
name|String
name|response
init|=
name|proxy
operator|.
name|echo
argument_list|(
literal|"Hello World!"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response "
argument_list|,
literal|"echo Hello World!"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCallFromCamel ()
specifier|public
name|void
name|testCallFromCamel
parameter_list|()
throws|throws
name|Exception
block|{
comment|// get camel context
name|CamelContext
name|context
init|=
operator|(
name|CamelContext
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"conduit_context"
argument_list|)
decl_stmt|;
name|ProducerTemplate
name|producer
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
comment|// The echo parameter will be ignore, since the service has the fix response
name|String
name|response
init|=
name|producer
operator|.
name|requestBody
argument_list|(
literal|"direct://jbiStart"
argument_list|,
literal|"Hello World!"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response "
argument_list|,
literal|"echo Hello World!"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
name|applicationContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

