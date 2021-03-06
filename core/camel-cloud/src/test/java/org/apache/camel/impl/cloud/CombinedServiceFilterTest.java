begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|ContextTestSupport
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
name|cloud
operator|.
name|ServiceDefinition
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
name|model
operator|.
name|cloud
operator|.
name|CombinedServiceCallServiceFilterConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_class
DECL|class|CombinedServiceFilterTest
specifier|public
class|class
name|CombinedServiceFilterTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testMultiServiceFilterConfiguration ()
specifier|public
name|void
name|testMultiServiceFilterConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|CombinedServiceCallServiceFilterConfiguration
name|conf
init|=
operator|new
name|CombinedServiceCallServiceFilterConfiguration
argument_list|()
operator|.
name|healthy
argument_list|()
operator|.
name|passThrough
argument_list|()
decl_stmt|;
name|CombinedServiceFilter
name|filter
init|=
operator|(
name|CombinedServiceFilter
operator|)
name|conf
operator|.
name|newInstance
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|filter
operator|.
name|getDelegates
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|filter
operator|.
name|getDelegates
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|HealthyServiceFilter
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|filter
operator|.
name|getDelegates
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|PassThroughServiceFilter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMultiServiceFilter ()
specifier|public
name|void
name|testMultiServiceFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|CombinedServiceCallServiceFilterConfiguration
name|conf
init|=
operator|new
name|CombinedServiceCallServiceFilterConfiguration
argument_list|()
operator|.
name|healthy
argument_list|()
operator|.
name|custom
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
decl_stmt|;
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|services
init|=
name|conf
operator|.
name|newInstance
argument_list|(
name|context
argument_list|)
operator|.
name|apply
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|DefaultServiceDefinition
argument_list|(
literal|"no-name"
argument_list|,
literal|"127.0.0.1"
argument_list|,
literal|1000
argument_list|)
argument_list|,
operator|new
name|DefaultServiceDefinition
argument_list|(
literal|"no-name"
argument_list|,
literal|"127.0.0.1"
argument_list|,
literal|1001
argument_list|,
operator|new
name|DefaultServiceHealth
argument_list|(
literal|false
argument_list|)
argument_list|)
argument_list|,
operator|new
name|DefaultServiceDefinition
argument_list|(
literal|"no-name"
argument_list|,
literal|"127.0.0.1"
argument_list|,
literal|1002
argument_list|,
operator|new
name|DefaultServiceHealth
argument_list|(
literal|true
argument_list|)
argument_list|)
argument_list|,
operator|new
name|DefaultServiceDefinition
argument_list|(
literal|"no-name"
argument_list|,
literal|"127.0.0.1"
argument_list|,
literal|2001
argument_list|,
operator|new
name|DefaultServiceHealth
argument_list|(
literal|true
argument_list|)
argument_list|)
argument_list|,
operator|new
name|DefaultServiceDefinition
argument_list|(
literal|"no-name"
argument_list|,
literal|"127.0.0.1"
argument_list|,
literal|2001
argument_list|,
operator|new
name|DefaultServiceHealth
argument_list|(
literal|false
argument_list|)
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|services
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|services
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|s
lambda|->
operator|!
name|s
operator|.
name|getHealth
argument_list|()
operator|.
name|isHealthy
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|services
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|s
lambda|->
name|s
operator|.
name|getPort
argument_list|()
operator|>
literal|2000
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|services
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|s
lambda|->
name|s
operator|.
name|getPort
argument_list|()
operator|==
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|services
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|s
lambda|->
name|s
operator|.
name|getPort
argument_list|()
operator|==
literal|1002
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

