begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Expression
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
name|RouteNode
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
name|processor
operator|.
name|Traceable
import|;
end_import

begin_comment
comment|/**  * A default implementation of the {@link org.apache.camel.RouteNode}  *  * @version   */
end_comment

begin_class
DECL|class|DefaultRouteNode
specifier|public
class|class
name|DefaultRouteNode
implements|implements
name|RouteNode
block|{
DECL|field|expression
specifier|private
name|Expression
name|expression
decl_stmt|;
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|field|processorDefinition
specifier|private
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|processorDefinition
decl_stmt|;
DECL|method|DefaultRouteNode (ProcessorDefinition<?> processorDefinition, Processor processor)
specifier|public
name|DefaultRouteNode
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|processorDefinition
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|processorDefinition
operator|=
name|processorDefinition
expr_stmt|;
block|}
DECL|method|DefaultRouteNode (ProcessorDefinition<?> processorDefinition, Expression expression)
specifier|public
name|DefaultRouteNode
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|processorDefinition
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|processorDefinition
operator|=
name|processorDefinition
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
DECL|method|getProcessorDefinition ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|getProcessorDefinition
parameter_list|()
block|{
return|return
name|processorDefinition
return|;
block|}
DECL|method|getLabel (Exchange exchange)
specifier|public
name|String
name|getLabel
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
return|return
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
name|Processor
name|target
init|=
name|processor
decl_stmt|;
if|if
condition|(
name|target
operator|!=
literal|null
operator|&&
name|target
operator|instanceof
name|Traceable
condition|)
block|{
name|Traceable
name|trace
init|=
operator|(
name|Traceable
operator|)
name|target
decl_stmt|;
return|return
name|trace
operator|.
name|getTraceLabel
argument_list|()
return|;
block|}
comment|// default then to definition
return|return
name|processorDefinition
operator|.
name|getLabel
argument_list|()
return|;
block|}
DECL|method|isAbstract ()
specifier|public
name|boolean
name|isAbstract
parameter_list|()
block|{
return|return
name|processor
operator|==
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RouteNode["
operator|+
name|processorDefinition
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

