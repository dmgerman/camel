begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|etcd
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|JsonNode
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|EtcdClient
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
name|etcd
operator|.
name|EtcdHelper
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

begin_class
DECL|class|SpringEtcdServiceCallRouteTest
specifier|public
class|class
name|SpringEtcdServiceCallRouteTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|MAPPER
specifier|private
specifier|static
specifier|final
name|ObjectMapper
name|MAPPER
init|=
name|EtcdHelper
operator|.
name|createObjectMapper
argument_list|()
decl_stmt|;
DECL|field|CLIENT
specifier|private
specifier|static
specifier|final
name|EtcdClient
name|CLIENT
init|=
operator|new
name|EtcdClient
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://localhost:2379"
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/etcd/cloud/SpringEtcdServiceCallRouteTest.xml"
argument_list|)
return|;
block|}
comment|// *************************************************************************
comment|// Setup / tear down
comment|// *************************************************************************
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|public
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|JsonNode
name|service1
init|=
name|MAPPER
operator|.
name|createObjectNode
argument_list|()
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"http-service"
argument_list|)
operator|.
name|put
argument_list|(
literal|"address"
argument_list|,
literal|"127.0.0.1"
argument_list|)
operator|.
name|put
argument_list|(
literal|"port"
argument_list|,
literal|"9091"
argument_list|)
decl_stmt|;
name|JsonNode
name|service2
init|=
name|MAPPER
operator|.
name|createObjectNode
argument_list|()
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"http-service"
argument_list|)
operator|.
name|put
argument_list|(
literal|"address"
argument_list|,
literal|"127.0.0.1"
argument_list|)
operator|.
name|put
argument_list|(
literal|"port"
argument_list|,
literal|"9092"
argument_list|)
decl_stmt|;
name|JsonNode
name|service3
init|=
name|MAPPER
operator|.
name|createObjectNode
argument_list|()
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"http-service"
argument_list|)
operator|.
name|put
argument_list|(
literal|"address"
argument_list|,
literal|"127.0.0.1"
argument_list|)
operator|.
name|put
argument_list|(
literal|"port"
argument_list|,
literal|"9093"
argument_list|)
decl_stmt|;
name|JsonNode
name|service4
init|=
name|MAPPER
operator|.
name|createObjectNode
argument_list|()
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
literal|"http-service"
argument_list|)
operator|.
name|put
argument_list|(
literal|"address"
argument_list|,
literal|"127.0.0.1"
argument_list|)
operator|.
name|put
argument_list|(
literal|"port"
argument_list|,
literal|"9094"
argument_list|)
decl_stmt|;
name|CLIENT
operator|.
name|put
argument_list|(
literal|"/etcd-services-1/"
operator|+
literal|"service-1"
argument_list|,
name|MAPPER
operator|.
name|writeValueAsString
argument_list|(
name|service1
argument_list|)
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|CLIENT
operator|.
name|put
argument_list|(
literal|"/etcd-services-1/"
operator|+
literal|"service-2"
argument_list|,
name|MAPPER
operator|.
name|writeValueAsString
argument_list|(
name|service2
argument_list|)
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|CLIENT
operator|.
name|put
argument_list|(
literal|"/etcd-services-2/"
operator|+
literal|"service-3"
argument_list|,
name|MAPPER
operator|.
name|writeValueAsString
argument_list|(
name|service3
argument_list|)
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|CLIENT
operator|.
name|put
argument_list|(
literal|"/etcd-services-2/"
operator|+
literal|"service-4"
argument_list|,
name|MAPPER
operator|.
name|writeValueAsString
argument_list|(
name|service4
argument_list|)
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|super
operator|.
name|doPreSetup
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|CLIENT
operator|.
name|deleteDir
argument_list|(
literal|"/etcd-services-1/"
argument_list|)
operator|.
name|recursive
argument_list|()
operator|.
name|send
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|CLIENT
operator|.
name|deleteDir
argument_list|(
literal|"/etcd-services-2/"
argument_list|)
operator|.
name|recursive
argument_list|()
operator|.
name|send
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Test
comment|// *************************************************************************
annotation|@
name|Test
DECL|method|testServiceCall ()
specifier|public
name|void
name|testServiceCall
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result-1"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result-1"
argument_list|)
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"service-1 9091"
argument_list|,
literal|"service-1 9092"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result-2"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result-2"
argument_list|)
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"service-2 9093"
argument_list|,
literal|"service-2 9094"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"service-1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"service-1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"service-2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"service-2"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

