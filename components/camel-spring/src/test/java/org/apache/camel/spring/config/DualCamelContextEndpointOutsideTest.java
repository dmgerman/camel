begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|SpringTestSupport
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
name|support
operator|.
name|service
operator|.
name|ServiceHelper
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
name|AbstractXmlApplicationContext
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

begin_class
DECL|class|DualCamelContextEndpointOutsideTest
specifier|public
class|class
name|DualCamelContextEndpointOutsideTest
extends|extends
name|SpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/config/DualCamelContextEndpointOutsideTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testDualCamelContextEndpoint ()
specifier|public
name|void
name|testDualCamelContextEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelA
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"camel-A"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|camelA
argument_list|)
expr_stmt|;
name|CamelContext
name|camelB
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"camel-B"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|camelB
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockA
init|=
name|camelA
operator|.
name|getEndpoint
argument_list|(
literal|"mock:mock1"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockA
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello A"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockB
init|=
name|camelB
operator|.
name|getEndpoint
argument_list|(
literal|"mock:mock2"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockB
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello B"
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|producer1
init|=
name|camelA
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|producer1
operator|.
name|sendBody
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"Hello A"
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|producer2
init|=
name|camelB
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|producer2
operator|.
name|sendBody
argument_list|(
literal|"direct:start2"
argument_list|,
literal|"Hello B"
argument_list|)
expr_stmt|;
comment|// make sure we properly stop the services we created
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|producer1
argument_list|,
name|producer2
argument_list|)
expr_stmt|;
name|mockA
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockB
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

