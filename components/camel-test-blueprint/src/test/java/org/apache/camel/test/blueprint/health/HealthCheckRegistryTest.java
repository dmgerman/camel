begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
operator|.
name|health
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|health
operator|.
name|HealthCheckRegistry
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
name|health
operator|.
name|HealthCheckRepository
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
name|health
operator|.
name|RegistryRepository
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
name|health
operator|.
name|RoutePerformanceCounterEvaluators
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
name|health
operator|.
name|RoutesHealthCheckRepository
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
name|blueprint
operator|.
name|CamelBlueprintTestSupport
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
DECL|class|HealthCheckRegistryTest
specifier|public
class|class
name|HealthCheckRegistryTest
extends|extends
name|CamelBlueprintTestSupport
block|{
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"org/apache/camel/test/blueprint/health/HealthCheckRegistryTest.xml"
return|;
block|}
annotation|@
name|Test
DECL|method|testRepositories ()
specifier|public
name|void
name|testRepositories
parameter_list|()
block|{
name|Collection
argument_list|<
name|HealthCheckRepository
argument_list|>
name|repos
init|=
name|HealthCheckRegistry
operator|.
name|get
argument_list|(
name|context
argument_list|)
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|repos
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|repos
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|RegistryRepository
operator|.
name|class
operator|::
name|isInstance
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|repos
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|RoutesHealthCheckRepository
operator|.
name|class
operator|::
name|isInstance
argument_list|)
argument_list|)
expr_stmt|;
name|Optional
argument_list|<
name|RoutesHealthCheckRepository
argument_list|>
name|repo
init|=
name|repos
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|RoutesHealthCheckRepository
operator|.
name|class
operator|::
name|isInstance
argument_list|)
operator|.
name|map
argument_list|(
name|RoutesHealthCheckRepository
operator|.
name|class
operator|::
name|cast
argument_list|)
operator|.
name|findFirst
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|repo
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|repo
operator|.
name|get
argument_list|()
operator|.
name|evaluators
argument_list|()
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repo
operator|.
name|get
argument_list|()
operator|.
name|evaluators
argument_list|()
operator|.
name|filter
argument_list|(
name|RoutePerformanceCounterEvaluators
operator|.
name|ExchangesFailed
operator|.
name|class
operator|::
name|isInstance
argument_list|)
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repo
operator|.
name|get
argument_list|()
operator|.
name|evaluators
argument_list|()
operator|.
name|filter
argument_list|(
name|RoutePerformanceCounterEvaluators
operator|.
name|LastProcessingTime
operator|.
name|class
operator|::
name|isInstance
argument_list|)
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repo
operator|.
name|get
argument_list|()
operator|.
name|evaluators
argument_list|(
literal|"route-1"
argument_list|)
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repo
operator|.
name|get
argument_list|()
operator|.
name|evaluators
argument_list|(
literal|"route-1"
argument_list|)
operator|.
name|filter
argument_list|(
name|RoutePerformanceCounterEvaluators
operator|.
name|ExchangesInflight
operator|.
name|class
operator|::
name|isInstance
argument_list|)
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

