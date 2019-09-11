begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|management
operator|.
name|JmxManagementStrategy
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
name|junit5
operator|.
name|TestSupport
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
name|junit5
operator|.
name|EnableRouteCoverage
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
name|BeforeAll
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
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertTrue
import|;
end_import

begin_class
annotation|@
name|EnableRouteCoverage
DECL|class|CamelSpringRouteProcessorDumpRouteCoverageTest
specifier|public
class|class
name|CamelSpringRouteProcessorDumpRouteCoverageTest
extends|extends
name|CamelSpringPlainTest
block|{
annotation|@
name|BeforeAll
DECL|method|prepareFiles ()
specifier|public
specifier|static
name|void
name|prepareFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|TestSupport
operator|.
name|deleteDirectory
argument_list|(
literal|"target/camel-route-coverage"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testJmx ()
specifier|public
name|void
name|testJmx
parameter_list|()
block|{
comment|// JMX is enabled with route coverage
name|assertEquals
argument_list|(
name|JmxManagementStrategy
operator|.
name|class
argument_list|,
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|testRouteCoverage ()
specifier|public
name|void
name|testRouteCoverage
parameter_list|()
block|{
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// there should be files
name|String
index|[]
name|names
init|=
operator|new
name|File
argument_list|(
literal|"target/camel-route-coverage"
argument_list|)
operator|.
name|list
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

