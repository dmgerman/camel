begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
package|;
end_package

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

begin_comment
comment|/**  * Tests for the MllpEndpoint class.  */
end_comment

begin_class
DECL|class|MllpEndpointTest
specifier|public
class|class
name|MllpEndpointTest
block|{
comment|/**      * Assert that the default maxConcurrentConsumers property is correctly set on the endpoint instance.      */
annotation|@
name|Test
DECL|method|testCreateEndpointWithDefaultConfigurations ()
specifier|public
name|void
name|testCreateEndpointWithDefaultConfigurations
parameter_list|()
block|{
name|MllpEndpoint
name|mllpEndpoint
init|=
operator|new
name|MllpEndpoint
argument_list|(
literal|"mllp://dummy"
argument_list|,
operator|new
name|MllpComponent
argument_list|()
argument_list|,
operator|new
name|MllpConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|mllpEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMaxConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Assert that the maxConcurrentConsumers property overridden in the MllpConfiguration      * object is correctly set on the endpoint instance.      */
annotation|@
name|Test
DECL|method|testCreateEndpointWithCustomMaxConcurrentConsumers ()
specifier|public
name|void
name|testCreateEndpointWithCustomMaxConcurrentConsumers
parameter_list|()
block|{
specifier|final
name|int
name|maxConcurrentConsumers
init|=
literal|10
decl_stmt|;
name|MllpConfiguration
name|mllpConfiguration
init|=
operator|new
name|MllpConfiguration
argument_list|()
decl_stmt|;
name|mllpConfiguration
operator|.
name|setMaxConcurrentConsumers
argument_list|(
name|maxConcurrentConsumers
argument_list|)
expr_stmt|;
name|MllpEndpoint
name|mllpEndpoint
init|=
operator|new
name|MllpEndpoint
argument_list|(
literal|"mllp://dummy"
argument_list|,
operator|new
name|MllpComponent
argument_list|()
argument_list|,
name|mllpConfiguration
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|maxConcurrentConsumers
argument_list|,
name|mllpEndpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMaxConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

