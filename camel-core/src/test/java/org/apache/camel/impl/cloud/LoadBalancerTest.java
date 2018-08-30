begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|cloud
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|RejectedExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|junit
operator|.
name|BeforeClass
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

begin_class
DECL|class|LoadBalancerTest
specifier|public
class|class
name|LoadBalancerTest
block|{
DECL|field|serviceDiscovery
specifier|private
specifier|static
name|StaticServiceDiscovery
name|serviceDiscovery
init|=
operator|new
name|StaticServiceDiscovery
argument_list|()
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setUp ()
specifier|public
specifier|static
name|void
name|setUp
parameter_list|()
block|{
name|serviceDiscovery
operator|.
name|addServer
argument_list|(
literal|"no-name@127.0.0.1:2001"
argument_list|)
expr_stmt|;
name|serviceDiscovery
operator|.
name|addServer
argument_list|(
literal|"no-name@127.0.0.1:2002"
argument_list|)
expr_stmt|;
name|serviceDiscovery
operator|.
name|addServer
argument_list|(
literal|"no-name@127.0.0.1:1001"
argument_list|)
expr_stmt|;
name|serviceDiscovery
operator|.
name|addServer
argument_list|(
literal|"no-name@127.0.0.1:1002"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLoadBalancer ()
specifier|public
name|void
name|testLoadBalancer
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultServiceLoadBalancer
name|loadBalancer
init|=
operator|new
name|DefaultServiceLoadBalancer
argument_list|()
decl_stmt|;
name|loadBalancer
operator|.
name|setCamelContext
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|setServiceDiscovery
argument_list|(
name|serviceDiscovery
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|setServiceFilter
argument_list|(
name|services
lambda|->
name|services
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|s
lambda|->
name|s
operator|.
name|getPort
argument_list|()
operator|<
literal|2000
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|setServiceChooser
argument_list|(
operator|new
name|RoundRobinServiceChooser
argument_list|()
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|process
argument_list|(
literal|"no-name"
argument_list|,
name|service
lambda|->
block|{
name|assertEquals
argument_list|(
literal|1001
argument_list|,
name|service
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|process
argument_list|(
literal|"no-name"
argument_list|,
name|service
lambda|->
block|{
name|assertEquals
argument_list|(
literal|1002
argument_list|,
name|service
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RejectedExecutionException
operator|.
name|class
argument_list|)
DECL|method|testNoActiveServices ()
specifier|public
name|void
name|testNoActiveServices
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultServiceLoadBalancer
name|loadBalancer
init|=
operator|new
name|DefaultServiceLoadBalancer
argument_list|()
decl_stmt|;
name|loadBalancer
operator|.
name|setCamelContext
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|setServiceDiscovery
argument_list|(
name|serviceDiscovery
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|setServiceFilter
argument_list|(
name|services
lambda|->
name|services
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|s
lambda|->
name|s
operator|.
name|getPort
argument_list|()
operator|<
literal|1000
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|setServiceChooser
argument_list|(
operator|new
name|RoundRobinServiceChooser
argument_list|()
argument_list|)
expr_stmt|;
name|loadBalancer
operator|.
name|process
argument_list|(
literal|"no-name"
argument_list|,
name|service
lambda|->
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

