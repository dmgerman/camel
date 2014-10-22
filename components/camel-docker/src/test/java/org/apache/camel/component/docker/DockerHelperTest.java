begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.docker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|docker
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

begin_class
DECL|class|DockerHelperTest
specifier|public
class|class
name|DockerHelperTest
block|{
annotation|@
name|Test
DECL|method|transformHeaderTestFromHeader ()
specifier|public
name|void
name|transformHeaderTestFromHeader
parameter_list|()
block|{
name|String
name|headerField
init|=
name|DockerHelper
operator|.
name|transformFromHeaderName
argument_list|(
name|DockerConstants
operator|.
name|DOCKER_REGISTRY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"registry"
argument_list|,
name|headerField
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

