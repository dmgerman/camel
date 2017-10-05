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
name|impl
operator|.
name|DefaultConsumer
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
name|api
operator|.
name|solver
operator|.
name|Solver
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
name|api
operator|.
name|solver
operator|.
name|event
operator|.
name|BestSolutionChangedEvent
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
name|api
operator|.
name|solver
operator|.
name|event
operator|.
name|SolverEventListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * OptaPlanner component for Camel  */
end_comment

begin_class
DECL|class|OptaPlannerConsumer
specifier|public
class|class
name|OptaPlannerConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|OptaPlannerConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|OptaPlannerEndpoint
name|endpoint
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|OptaPlannerConfiguration
name|configuration
decl_stmt|;
DECL|field|listener
specifier|private
specifier|final
name|SolverEventListener
argument_list|<
name|Object
argument_list|>
name|listener
decl_stmt|;
DECL|method|OptaPlannerConsumer (OptaPlannerEndpoint endpoint, Processor processor, OptaPlannerConfiguration configuration)
specifier|public
name|OptaPlannerConsumer
parameter_list|(
name|OptaPlannerEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|OptaPlannerConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|listener
operator|=
operator|new
name|SolverEventListener
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|bestSolutionChanged
parameter_list|(
name|BestSolutionChangedEvent
argument_list|<
name|Object
argument_list|>
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|isEveryProblemFactChangeProcessed
argument_list|()
operator|&&
name|event
operator|.
name|getNewBestScore
argument_list|()
operator|.
name|isSolutionInitialized
argument_list|()
condition|)
block|{
name|processEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
block|}
DECL|method|processEvent (BestSolutionChangedEvent<Object> event)
specifier|public
name|void
name|processEvent
parameter_list|(
name|BestSolutionChangedEvent
argument_list|<
name|Object
argument_list|>
name|event
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|OptaPlannerConstants
operator|.
name|BEST_SOLUTION
argument_list|,
name|event
operator|.
name|getNewBestSolution
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error processing event "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|Solver
argument_list|<
name|Object
argument_list|>
name|solver
init|=
name|endpoint
operator|.
name|getOrCreateSolver
argument_list|(
name|configuration
operator|.
name|getSolverId
argument_list|()
argument_list|)
decl_stmt|;
name|solver
operator|.
name|addEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|Solver
argument_list|<
name|Object
argument_list|>
name|solver
init|=
name|endpoint
operator|.
name|getOrCreateSolver
argument_list|(
name|configuration
operator|.
name|getSolverId
argument_list|()
argument_list|)
decl_stmt|;
name|solver
operator|.
name|removeEventListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

