begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.mockendpoints
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|mockendpoints
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
name|EndpointInject
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
name|Produce
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
name|SpringRouteBuilder
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
name|spring
operator|.
name|CamelSpringBootRunner
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
name|spring
operator|.
name|MockEndpointsAndSkip
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
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
name|boot
operator|.
name|autoconfigure
operator|.
name|SpringBootApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|SpringBootTest
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
name|annotation
operator|.
name|Configuration
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
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|CamelSpringBootRunner
operator|.
name|class
argument_list|)
annotation|@
name|MockEndpointsAndSkip
argument_list|(
literal|"direct:b"
argument_list|)
annotation|@
name|SpringBootApplication
annotation|@
name|SpringBootTest
argument_list|(
name|classes
operator|=
name|MockEndpointsAndSkipDirtiesContextTest
operator|.
name|class
argument_list|)
annotation|@
name|DirtiesContext
argument_list|(
name|classMode
operator|=
name|DirtiesContext
operator|.
name|ClassMode
operator|.
name|AFTER_EACH_TEST_METHOD
argument_list|)
DECL|class|MockEndpointsAndSkipDirtiesContextTest
specifier|public
class|class
name|MockEndpointsAndSkipDirtiesContextTest
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:a"
argument_list|)
DECL|field|producer
specifier|private
name|ProducerTemplate
name|producer
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:end"
argument_list|)
DECL|field|end
specifier|private
name|MockEndpoint
name|end
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:direct:b"
argument_list|)
DECL|field|directB
specifier|private
name|MockEndpoint
name|directB
decl_stmt|;
annotation|@
name|Autowired
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Configuration
DECL|class|Config
specifier|public
specifier|static
class|class
name|Config
extends|extends
name|SpringRouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testMock ()
specifier|public
name|void
name|testMock
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|end
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|directB
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMock2 ()
specifier|public
name|void
name|testMock2
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|end
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|directB
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"bye"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
literal|"bye"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

