begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|List
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|HealthCheck
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
name|HealthCheckResultBuilder
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
DECL|class|DefaultHealthCheckRegistryTest
specifier|public
class|class
name|DefaultHealthCheckRegistryTest
block|{
annotation|@
name|Test
DECL|method|testDefaultHealthCheckRegistryRepositorySetter ()
specifier|public
name|void
name|testDefaultHealthCheckRegistryRepositorySetter
parameter_list|()
block|{
name|HealthCheckRegistry
name|registry1
init|=
operator|new
name|DefaultHealthCheckRegistry
argument_list|()
decl_stmt|;
name|HealthCheckRegistry
name|registry2
init|=
operator|new
name|DefaultHealthCheckRegistry
argument_list|()
decl_stmt|;
name|registry1
operator|.
name|addRepository
argument_list|(
parameter_list|()
lambda|->
name|Stream
operator|.
name|of
argument_list|(
operator|new
name|MyHealthCheck
argument_list|(
literal|"G1"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|registry2
operator|.
name|setRepositories
argument_list|(
name|registry1
operator|.
name|getRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|registry1
operator|.
name|getRepositories
argument_list|()
operator|.
name|toArray
argument_list|()
argument_list|,
name|registry2
operator|.
name|getRepositories
argument_list|()
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultHealthCheckRegistry ()
specifier|public
name|void
name|testDefaultHealthCheckRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|HealthCheckRegistry
name|registry
init|=
operator|new
name|DefaultHealthCheckRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|MyHealthCheck
argument_list|(
literal|"G1"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|MyHealthCheck
argument_list|(
literal|"G1"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|MyHealthCheck
argument_list|(
literal|"G1"
argument_list|,
literal|"2"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|MyHealthCheck
argument_list|(
literal|"G2"
argument_list|,
literal|"3"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|HealthCheck
argument_list|>
name|checks
init|=
name|registry
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|checks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|HealthCheck
name|check
range|:
name|checks
control|)
block|{
name|HealthCheck
operator|.
name|Result
name|response
init|=
name|check
operator|.
name|call
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|,
name|response
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|response
operator|.
name|getMessage
argument_list|()
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|response
operator|.
name|getError
argument_list|()
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testDefaultHealthCheckRegistryWithRepositories ()
specifier|public
name|void
name|testDefaultHealthCheckRegistryWithRepositories
parameter_list|()
throws|throws
name|Exception
block|{
name|HealthCheckRegistry
name|registry
init|=
operator|new
name|DefaultHealthCheckRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|MyHealthCheck
argument_list|(
literal|"G1"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|MyHealthCheck
argument_list|(
literal|"G1"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|MyHealthCheck
argument_list|(
literal|"G1"
argument_list|,
literal|"2"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|MyHealthCheck
argument_list|(
literal|"G2"
argument_list|,
literal|"3"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|addRepository
argument_list|(
parameter_list|()
lambda|->
name|Stream
operator|.
name|of
argument_list|(
operator|new
name|MyHealthCheck
argument_list|(
literal|"G1"
argument_list|,
literal|"1"
argument_list|)
argument_list|,
operator|new
name|MyHealthCheck
argument_list|(
literal|"G1"
argument_list|,
literal|"4"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|HealthCheck
argument_list|>
name|checks
init|=
name|registry
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|checks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|checks
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|h
lambda|->
name|h
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
literal|"4"
argument_list|)
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
literal|3
argument_list|,
name|checks
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|h
lambda|->
name|h
operator|.
name|getGroup
argument_list|()
operator|.
name|equals
argument_list|(
literal|"G1"
argument_list|)
argument_list|)
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|HealthCheck
name|check
range|:
name|checks
control|)
block|{
name|HealthCheck
operator|.
name|Result
name|response
init|=
name|check
operator|.
name|call
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UP
argument_list|,
name|response
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|response
operator|.
name|getMessage
argument_list|()
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|response
operator|.
name|getError
argument_list|()
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MyHealthCheck
specifier|private
class|class
name|MyHealthCheck
extends|extends
name|AbstractHealthCheck
block|{
DECL|method|MyHealthCheck (String group, String id)
specifier|protected
name|MyHealthCheck
parameter_list|(
name|String
name|group
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|group
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|getConfiguration
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doCall (HealthCheckResultBuilder builder, Map<String, Object> options)
specifier|public
name|void
name|doCall
parameter_list|(
name|HealthCheckResultBuilder
name|builder
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
name|builder
operator|.
name|up
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

