begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.optaplanner
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|optaplanner
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
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
name|Exchange
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
name|Processor
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
name|builder
operator|.
name|RouteBuilder
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
name|junit4
operator|.
name|CamelTestSupport
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
name|optaplanner
operator|.
name|examples
operator|.
name|cloudbalancing
operator|.
name|domain
operator|.
name|CloudBalance
import|;
end_import

begin_import
import|import
name|org
operator|.
name|optaplanner
operator|.
name|examples
operator|.
name|cloudbalancing
operator|.
name|persistence
operator|.
name|CloudBalancingGenerator
import|;
end_import

begin_comment
comment|/**  * OptaPlanner unit test with Camel  */
end_comment

begin_class
DECL|class|OptaPlannerTest
specifier|public
class|class
name|OptaPlannerTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testCloudBalance_4computers_12processes ()
specifier|public
name|void
name|testCloudBalance_4computers_12processes
parameter_list|()
throws|throws
name|Exception
block|{
name|CloudBalancingGenerator
name|generator
init|=
operator|new
name|CloudBalancingGenerator
argument_list|(
literal|true
argument_list|)
decl_stmt|;
specifier|final
name|CloudBalance
name|planningProblem
init|=
name|generator
operator|.
name|createCloudBalance
argument_list|(
literal|4
argument_list|,
literal|12
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|planningProblem
operator|.
name|getScore
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|planningProblem
operator|.
name|getProcessList
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getComputer
argument_list|()
argument_list|)
expr_stmt|;
name|CloudBalance
name|bestSolution
init|=
operator|(
name|CloudBalance
operator|)
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:in"
argument_list|,
name|planningProblem
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|bestSolution
operator|.
name|getComputerList
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|12
argument_list|,
name|bestSolution
operator|.
name|getProcessList
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bestSolution
operator|.
name|getScore
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bestSolution
operator|.
name|getScore
argument_list|()
operator|.
name|isFeasible
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bestSolution
operator|.
name|getProcessList
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getComputer
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"optaplanner:/org/apache/camel/component/optaplanner/solverConfig.xml"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

