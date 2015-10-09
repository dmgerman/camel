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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|ObjectUtils
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
name|core
operator|.
name|impl
operator|.
name|score
operator|.
name|director
operator|.
name|ScoreDirector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|optaplanner
operator|.
name|core
operator|.
name|impl
operator|.
name|solver
operator|.
name|ProblemFactChange
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
name|domain
operator|.
name|CloudComputer
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
name|CloudProcess
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
DECL|class|OptaPlannerDaemonSolverTest
specifier|public
class|class
name|OptaPlannerDaemonSolverTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testAsynchronousProblemSolving ()
specifier|public
name|void
name|testAsynchronousProblemSolving
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|setExpectedCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:in"
argument_list|,
name|planningProblem
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|reset
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|setExpectedCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:in"
argument_list|,
operator|new
name|ProblemFactChange
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|doChange
parameter_list|(
name|ScoreDirector
name|scoreDirector
parameter_list|)
block|{
name|CloudBalance
name|cloudBalance
init|=
operator|(
name|CloudBalance
operator|)
name|scoreDirector
operator|.
name|getWorkingSolution
argument_list|()
decl_stmt|;
name|CloudComputer
name|computer
init|=
literal|null
decl_stmt|;
for|for
control|(
name|CloudProcess
name|process
range|:
name|cloudBalance
operator|.
name|getProcessList
argument_list|()
control|)
block|{
name|computer
operator|=
name|process
operator|.
name|getComputer
argument_list|()
expr_stmt|;
if|if
condition|(
name|ObjectUtils
operator|.
name|equals
argument_list|(
name|process
operator|.
name|getComputer
argument_list|()
argument_list|,
name|computer
argument_list|)
condition|)
block|{
name|scoreDirector
operator|.
name|beforeVariableChanged
argument_list|(
name|process
argument_list|,
literal|"computer"
argument_list|)
expr_stmt|;
name|process
operator|.
name|setComputer
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|scoreDirector
operator|.
name|afterVariableChanged
argument_list|(
name|process
argument_list|,
literal|"computer"
argument_list|)
expr_stmt|;
block|}
block|}
name|cloudBalance
operator|.
name|setComputerList
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|CloudComputer
argument_list|>
argument_list|(
name|cloudBalance
operator|.
name|getComputerList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|CloudComputer
argument_list|>
name|it
init|=
name|cloudBalance
operator|.
name|getComputerList
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|CloudComputer
name|workingComputer
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectUtils
operator|.
name|equals
argument_list|(
name|workingComputer
argument_list|,
name|computer
argument_list|)
condition|)
block|{
name|scoreDirector
operator|.
name|beforeProblemFactRemoved
argument_list|(
name|workingComputer
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
comment|// remove from list
name|scoreDirector
operator|.
name|beforeProblemFactRemoved
argument_list|(
name|workingComputer
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|assertIsSatisfied
argument_list|()
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
literal|"foo"
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
literal|"optaplanner:org/apache/camel/component/optaplanner/daemonSolverConfig.xml?async=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"optaplanner:org/apache/camel/component/optaplanner/daemonSolverConfig.xml"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:com.mycompany.order?showAll=true&multiline=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

