begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.testcontainers.junit5
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|testcontainers
operator|.
name|junit5
package|;
end_package

begin_import
import|import
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|containers
operator|.
name|GenericContainer
import|;
end_import

begin_class
DECL|class|ContainerAwareTestSupportIT
specifier|public
class|class
name|ContainerAwareTestSupportIT
extends|extends
name|ContainerAwareTestSupport
block|{
annotation|@
name|Test
DECL|method|testPropertyPlaceholders ()
specifier|public
name|void
name|testPropertyPlaceholders
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|GenericContainer
argument_list|<
name|?
argument_list|>
name|container
init|=
name|getContainer
argument_list|(
literal|"myconsul"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|host
init|=
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"{{container:host:myconsul}}"
argument_list|)
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|host
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|container
operator|.
name|getContainerIpAddress
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|port
init|=
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"{{container:port:8500@myconsul}}"
argument_list|)
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|port
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|""
operator|+
name|container
operator|.
name|getMappedPort
argument_list|(
literal|8500
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createContainer ()
specifier|protected
name|GenericContainer
argument_list|<
name|?
argument_list|>
name|createContainer
parameter_list|()
block|{
return|return
operator|new
name|GenericContainer
argument_list|<>
argument_list|(
literal|"consul:1.5.3"
argument_list|)
operator|.
name|withNetworkAliases
argument_list|(
literal|"myconsul"
argument_list|)
operator|.
name|withExposedPorts
argument_list|(
literal|8500
argument_list|)
operator|.
name|waitingFor
argument_list|(
name|Wait
operator|.
name|forLogMessageContaining
argument_list|(
literal|"Synced node info"
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|withCommand
argument_list|(
literal|"agent"
argument_list|,
literal|"-dev"
argument_list|,
literal|"-server"
argument_list|,
literal|"-bootstrap"
argument_list|,
literal|"-client"
argument_list|,
literal|"0.0.0.0"
argument_list|,
literal|"-log-level"
argument_list|,
literal|"trace"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

