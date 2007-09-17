begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|RouteContext
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
name|CatchProcessor
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

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"try"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|TryType
specifier|public
class|class
name|TryType
extends|extends
name|OutputType
argument_list|<
name|TryType
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|catchClauses
specifier|private
name|List
argument_list|<
name|CatchType
argument_list|>
name|catchClauses
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|finallyClause
specifier|private
name|FinallyType
name|finallyClause
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|initialized
specifier|private
name|boolean
name|initialized
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|outputsWithoutCatches
specifier|private
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputsWithoutCatches
decl_stmt|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Try[ "
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
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
name|getOutputsWithoutCatches
argument_list|()
argument_list|)
decl_stmt|;
name|Processor
name|finallyProcessor
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|finallyClause
operator|!=
literal|null
condition|)
block|{
name|finallyProcessor
operator|=
name|finallyClause
operator|.
name|createProcessor
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|CatchProcessor
argument_list|>
name|catchProcessors
init|=
operator|new
name|ArrayList
argument_list|<
name|CatchProcessor
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|catchClauses
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|CatchType
name|catchClause
range|:
name|catchClauses
control|)
block|{
name|catchProcessors
operator|.
name|add
argument_list|(
name|catchClause
operator|.
name|createProcessor
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
comment|// Fluent API
comment|// -------------------------------------------------------------------------
DECL|method|handle (Class<?> exceptionType)
specifier|public
name|CatchType
name|handle
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|exceptionType
parameter_list|)
block|{
name|CatchType
name|answer
init|=
operator|new
name|CatchType
argument_list|(
name|exceptionType
argument_list|)
decl_stmt|;
name|addOutput
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|handleAll ()
specifier|public
name|FinallyType
name|handleAll
parameter_list|()
block|{
name|FinallyType
name|answer
init|=
operator|new
name|FinallyType
argument_list|()
decl_stmt|;
name|addOutput
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getCatchClauses ()
specifier|public
name|List
argument_list|<
name|CatchType
argument_list|>
name|getCatchClauses
parameter_list|()
block|{
if|if
condition|(
name|catchClauses
operator|==
literal|null
condition|)
block|{
name|checkInitialized
argument_list|()
expr_stmt|;
block|}
return|return
name|catchClauses
return|;
block|}
DECL|method|getFinallyClause ()
specifier|public
name|FinallyType
name|getFinallyClause
parameter_list|()
block|{
if|if
condition|(
name|finallyClause
operator|==
literal|null
condition|)
block|{
name|checkInitialized
argument_list|()
expr_stmt|;
block|}
return|return
name|finallyClause
return|;
block|}
DECL|method|getOutputsWithoutCatches ()
specifier|public
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputsWithoutCatches
parameter_list|()
block|{
if|if
condition|(
name|outputsWithoutCatches
operator|==
literal|null
condition|)
block|{
name|checkInitialized
argument_list|()
expr_stmt|;
block|}
return|return
name|outputsWithoutCatches
return|;
block|}
DECL|method|setOutputs (List<ProcessorType<?>> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
parameter_list|)
block|{
name|initialized
operator|=
literal|false
expr_stmt|;
name|super
operator|.
name|setOutputs
argument_list|(
name|outputs
argument_list|)
expr_stmt|;
block|}
DECL|method|addOutput (ProcessorType output)
specifier|public
name|void
name|addOutput
parameter_list|(
name|ProcessorType
name|output
parameter_list|)
block|{
name|initialized
operator|=
literal|false
expr_stmt|;
name|getOutputs
argument_list|()
operator|.
name|add
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks whether or not this object has been initialized      */
DECL|method|checkInitialized ()
specifier|protected
name|void
name|checkInitialized
parameter_list|()
block|{
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
name|initialized
operator|=
literal|true
expr_stmt|;
name|outputsWithoutCatches
operator|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|catchClauses
operator|=
operator|new
name|ArrayList
argument_list|<
name|CatchType
argument_list|>
argument_list|()
expr_stmt|;
name|finallyClause
operator|=
literal|null
expr_stmt|;
for|for
control|(
name|ProcessorType
name|output
range|:
name|outputs
control|)
block|{
if|if
condition|(
name|output
operator|instanceof
name|CatchType
condition|)
block|{
name|catchClauses
operator|.
name|add
argument_list|(
operator|(
name|CatchType
operator|)
name|output
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|output
operator|instanceof
name|FinallyType
condition|)
block|{
if|if
condition|(
name|finallyClause
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Multiple finally clauses added: "
operator|+
name|finallyClause
operator|+
literal|" and "
operator|+
name|output
argument_list|)
throw|;
block|}
else|else
block|{
name|finallyClause
operator|=
operator|(
name|FinallyType
operator|)
name|output
expr_stmt|;
block|}
block|}
else|else
block|{
name|outputsWithoutCatches
operator|.
name|add
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

