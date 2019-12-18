begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Component
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
name|Consumer
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
name|Producer
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|support
operator|.
name|DefaultEndpoint
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
name|SolverFactory
import|;
end_import

begin_comment
comment|/**  * Solves the planning problem contained in a message with OptaPlanner.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.13.0"
argument_list|,
name|scheme
operator|=
literal|"optaplanner"
argument_list|,
name|title
operator|=
literal|"OptaPlanner"
argument_list|,
name|syntax
operator|=
literal|"optaplanner:configFile"
argument_list|,
name|label
operator|=
literal|"engine,planning"
argument_list|)
DECL|class|OptaPlannerEndpoint
specifier|public
class|class
name|OptaPlannerEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|SOLVERS
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Solver
argument_list|<
name|Object
argument_list|>
argument_list|>
name|SOLVERS
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|solverFactory
specifier|private
name|SolverFactory
argument_list|<
name|Object
argument_list|>
name|solverFactory
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|OptaPlannerConfiguration
name|configuration
decl_stmt|;
DECL|method|OptaPlannerEndpoint (String uri, Component component, OptaPlannerConfiguration configuration)
specifier|public
name|OptaPlannerEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|OptaPlannerConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|OptaPlannerConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getOrCreateSolver (String solverId)
specifier|protected
name|Solver
argument_list|<
name|Object
argument_list|>
name|getOrCreateSolver
parameter_list|(
name|String
name|solverId
parameter_list|)
block|{
synchronized|synchronized
init|(
name|SOLVERS
init|)
block|{
name|Solver
argument_list|<
name|Object
argument_list|>
name|solver
init|=
name|SOLVERS
operator|.
name|get
argument_list|(
name|solverId
argument_list|)
decl_stmt|;
if|if
condition|(
name|solver
operator|==
literal|null
condition|)
block|{
name|solver
operator|=
name|createSolver
argument_list|()
expr_stmt|;
name|SOLVERS
operator|.
name|put
argument_list|(
name|solverId
argument_list|,
name|solver
argument_list|)
expr_stmt|;
block|}
return|return
name|solver
return|;
block|}
block|}
DECL|method|createSolver ()
specifier|protected
name|Solver
argument_list|<
name|Object
argument_list|>
name|createSolver
parameter_list|()
block|{
return|return
name|solverFactory
operator|.
name|buildSolver
argument_list|()
return|;
block|}
DECL|method|getSolver (String solverId)
specifier|protected
name|Solver
argument_list|<
name|Object
argument_list|>
name|getSolver
parameter_list|(
name|String
name|solverId
parameter_list|)
block|{
synchronized|synchronized
init|(
name|SOLVERS
init|)
block|{
return|return
name|SOLVERS
operator|.
name|get
argument_list|(
name|solverId
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
block|{
return|return
operator|new
name|OptaPlannerProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|OptaPlannerConsumer
name|consumer
init|=
operator|new
name|OptaPlannerConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|ClassLoader
name|classLoader
init|=
name|getCamelContext
argument_list|()
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
name|solverFactory
operator|=
name|SolverFactory
operator|.
name|createFromXmlResource
argument_list|(
name|configuration
operator|.
name|getConfigFile
argument_list|()
argument_list|,
name|classLoader
argument_list|)
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
synchronized|synchronized
init|(
name|SOLVERS
init|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Solver
argument_list|<
name|Object
argument_list|>
argument_list|>
name|solver
range|:
name|SOLVERS
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|solver
operator|.
name|getValue
argument_list|()
operator|.
name|terminateEarly
argument_list|()
expr_stmt|;
name|SOLVERS
operator|.
name|remove
argument_list|(
name|solver
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

