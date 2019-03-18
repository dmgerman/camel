begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
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
name|List
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
name|model
operator|.
name|CatchDefinition
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
name|model
operator|.
name|FinallyDefinition
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
name|model
operator|.
name|ProcessorDefinition
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
name|model
operator|.
name|TryDefinition
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
name|processor
operator|.
name|TryProcessor
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
name|RouteContext
import|;
end_import

begin_class
DECL|class|TryReifier
class|class
name|TryReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|TryDefinition
argument_list|>
block|{
DECL|method|TryReifier (ProcessorDefinition<?> definition)
name|TryReifier
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|TryDefinition
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Processor
name|tryProcessor
init|=
name|createOutputsProcessor
argument_list|(
name|routeContext
argument_list|,
name|definition
operator|.
name|getOutputsWithoutCatches
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tryProcessor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Definition has no children on "
operator|+
name|this
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|Processor
argument_list|>
name|catchProcessors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getCatchClauses
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|CatchDefinition
name|catchClause
range|:
name|definition
operator|.
name|getCatchClauses
argument_list|()
control|)
block|{
name|catchProcessors
operator|.
name|add
argument_list|(
name|createProcessor
argument_list|(
name|routeContext
argument_list|,
name|catchClause
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|FinallyDefinition
name|finallyDefinition
init|=
name|definition
operator|.
name|getFinallyClause
argument_list|()
decl_stmt|;
if|if
condition|(
name|finallyDefinition
operator|==
literal|null
condition|)
block|{
name|finallyDefinition
operator|=
operator|new
name|FinallyDefinition
argument_list|()
expr_stmt|;
name|finallyDefinition
operator|.
name|setParent
argument_list|(
name|definition
argument_list|)
expr_stmt|;
block|}
name|Processor
name|finallyProcessor
init|=
name|createProcessor
argument_list|(
name|routeContext
argument_list|,
name|finallyDefinition
argument_list|)
decl_stmt|;
comment|// must have either a catch or finally
if|if
condition|(
name|definition
operator|.
name|getFinallyClause
argument_list|()
operator|==
literal|null
operator|&&
name|definition
operator|.
name|getCatchClauses
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"doTry must have one or more catch or finally blocks on "
operator|+
name|this
argument_list|)
throw|;
block|}
return|return
operator|new
name|TryProcessor
argument_list|(
name|tryProcessor
argument_list|,
name|catchProcessors
argument_list|,
name|finallyProcessor
argument_list|)
return|;
block|}
block|}
end_class

end_unit

