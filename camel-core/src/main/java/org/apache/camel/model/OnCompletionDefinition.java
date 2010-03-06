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
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|XmlAttribute
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
name|XmlElement
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
name|XmlElementRef
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
name|Predicate
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
name|ExpressionClause
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
name|OnCompletionProcessor
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
name|UnitOfWorkProcessor
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

begin_comment
comment|/**  * Represents an XML&lt;onCompletion/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"onCompletion"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|OnCompletionDefinition
specifier|public
class|class
name|OnCompletionDefinition
extends|extends
name|ProcessorDefinition
argument_list|<
name|ProcessorDefinition
argument_list|>
implements|implements
name|ExecutorServiceAware
argument_list|<
name|OnCompletionDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|onCompleteOnly
specifier|private
name|Boolean
name|onCompleteOnly
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|onFailureOnly
specifier|private
name|Boolean
name|onFailureOnly
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"onWhen"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|onWhen
specifier|private
name|WhenDefinition
name|onWhen
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|outputs
specifier|private
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|executorServiceRef
specifier|private
name|String
name|executorServiceRef
decl_stmt|;
DECL|method|OnCompletionDefinition ()
specifier|public
name|OnCompletionDefinition
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"onCompletion["
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"onCompletion"
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"onCompletion"
return|;
block|}
annotation|@
name|Override
DECL|method|isAbstract ()
specifier|public
name|boolean
name|isAbstract
parameter_list|()
block|{
return|return
literal|true
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
name|childProcessor
init|=
name|createOutputsProcessor
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
comment|// wrap the on completion route in a unit of work processor
name|childProcessor
operator|=
operator|new
name|UnitOfWorkProcessor
argument_list|(
name|routeContext
argument_list|,
name|childProcessor
argument_list|)
expr_stmt|;
name|Predicate
name|when
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|onWhen
operator|!=
literal|null
condition|)
block|{
name|when
operator|=
name|onWhen
operator|.
name|getExpression
argument_list|()
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|onCompleteOnly
operator|&&
name|onFailureOnly
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Both onCompleteOnly and onFailureOnly cannot be true. Only one of them can be true. On node: "
operator|+
name|this
argument_list|)
throw|;
block|}
if|if
condition|(
name|executorServiceRef
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|executorServiceRef
argument_list|,
name|ExecutorService
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ExecutorServiceRef "
operator|+
name|executorServiceRef
operator|+
literal|" not found in registry."
argument_list|)
throw|;
block|}
block|}
name|OnCompletionProcessor
name|answer
init|=
operator|new
name|OnCompletionProcessor
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|childProcessor
argument_list|,
name|onCompleteOnly
argument_list|,
name|onFailureOnly
argument_list|,
name|when
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setExecutorService
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Removes all existing {@link org.apache.camel.model.OnCompletionDefinition} from the definition.      *<p/>      * This is used to let route scoped<tt>onCompletion</tt> overrule any global<tt>onCompletion</tt>.      * Hence we remove all existing as they are global.      *      * @param definition the parent definition that is the route      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|removeAllOnCompletionDefinition (ProcessorDefinition definition)
specifier|public
name|void
name|removeAllOnCompletionDefinition
parameter_list|(
name|ProcessorDefinition
name|definition
parameter_list|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|definition
operator|.
name|getOutputs
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
name|ProcessorDefinition
name|out
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|out
operator|instanceof
name|OnCompletionDefinition
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|end ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
extends|extends
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|end
parameter_list|()
block|{
comment|// pop parent block, as we added our self as block to parent when synchronized was defined in the route
name|getParent
argument_list|()
operator|.
name|popBlock
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|end
argument_list|()
return|;
block|}
comment|/**      * Will only synchronize when the {@link org.apache.camel.Exchange} completed successfully (no errors).      *      * @return the builder      */
DECL|method|onCompleteOnly ()
specifier|public
name|OnCompletionDefinition
name|onCompleteOnly
parameter_list|()
block|{
comment|// must define return type as OutputDefinition and not this type to avoid end user being able
comment|// to invoke onFailureOnly/onCompleteOnly more than once
name|setOnCompleteOnly
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|setOnFailureOnly
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Will only synchronize when the {@link org.apache.camel.Exchange} ended with failure (exception or FAULT message).      *      * @return the builder      */
DECL|method|onFailureOnly ()
specifier|public
name|OnCompletionDefinition
name|onFailureOnly
parameter_list|()
block|{
comment|// must define return type as OutputDefinition and not this type to avoid end user being able
comment|// to invoke onFailureOnly/onCompleteOnly more than once
name|setOnCompleteOnly
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|setOnFailureOnly
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets an additional predicate that should be true before the onCompletion is triggered.      *<p/>      * To be used for fine grained controlling whether a completion callback should be invoked or not      *      * @param predicate predicate that determines true or false      * @return the builder      */
DECL|method|onWhen (Predicate predicate)
specifier|public
name|OnCompletionDefinition
name|onWhen
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|setOnWhen
argument_list|(
operator|new
name|WhenDefinition
argument_list|(
name|predicate
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Creates an expression to configure an additional predicate that should be true before the      * onCompletion is triggered.      *<p/>      * To be used for fine grained controlling whether a completion callback should be invoked or not      *      * @return the expression clause to configure      */
DECL|method|onWhen ()
specifier|public
name|ExpressionClause
argument_list|<
name|OnCompletionDefinition
argument_list|>
name|onWhen
parameter_list|()
block|{
name|onWhen
operator|=
operator|new
name|WhenDefinition
argument_list|()
expr_stmt|;
name|ExpressionClause
argument_list|<
name|OnCompletionDefinition
argument_list|>
name|clause
init|=
operator|new
name|ExpressionClause
argument_list|<
name|OnCompletionDefinition
argument_list|>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|onWhen
operator|.
name|setExpression
argument_list|(
name|clause
argument_list|)
expr_stmt|;
return|return
name|clause
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|executorService (ExecutorService executorService)
specifier|public
name|OnCompletionDefinition
name|executorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|setExecutorService
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|executorServiceRef (String executorServiceRef)
specifier|public
name|OnCompletionDefinition
name|executorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|setExecutorServiceRef
argument_list|(
name|executorServiceRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
return|;
block|}
DECL|method|setOutputs (List<ProcessorDefinition> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
parameter_list|)
block|{
name|this
operator|.
name|outputs
operator|=
name|outputs
expr_stmt|;
block|}
DECL|method|getOnCompleteOnly ()
specifier|public
name|Boolean
name|getOnCompleteOnly
parameter_list|()
block|{
return|return
name|onCompleteOnly
return|;
block|}
DECL|method|setOnCompleteOnly (Boolean onCompleteOnly)
specifier|public
name|void
name|setOnCompleteOnly
parameter_list|(
name|Boolean
name|onCompleteOnly
parameter_list|)
block|{
name|this
operator|.
name|onCompleteOnly
operator|=
name|onCompleteOnly
expr_stmt|;
block|}
DECL|method|getOnFailureOnly ()
specifier|public
name|Boolean
name|getOnFailureOnly
parameter_list|()
block|{
return|return
name|onFailureOnly
return|;
block|}
DECL|method|setOnFailureOnly (Boolean onFailureOnly)
specifier|public
name|void
name|setOnFailureOnly
parameter_list|(
name|Boolean
name|onFailureOnly
parameter_list|)
block|{
name|this
operator|.
name|onFailureOnly
operator|=
name|onFailureOnly
expr_stmt|;
block|}
DECL|method|getOnWhen ()
specifier|public
name|WhenDefinition
name|getOnWhen
parameter_list|()
block|{
return|return
name|onWhen
return|;
block|}
DECL|method|setOnWhen (WhenDefinition onWhen)
specifier|public
name|void
name|setOnWhen
parameter_list|(
name|WhenDefinition
name|onWhen
parameter_list|)
block|{
name|this
operator|.
name|onWhen
operator|=
name|onWhen
expr_stmt|;
block|}
DECL|method|getExecutorService ()
specifier|public
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
return|return
name|executorService
return|;
block|}
DECL|method|setExecutorService (ExecutorService executorService)
specifier|public
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
block|}
DECL|method|getExecutorServiceRef ()
specifier|public
name|String
name|getExecutorServiceRef
parameter_list|()
block|{
return|return
name|executorServiceRef
return|;
block|}
DECL|method|setExecutorServiceRef (String executorServiceRef)
specifier|public
name|void
name|setExecutorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|this
operator|.
name|executorServiceRef
operator|=
name|executorServiceRef
expr_stmt|;
block|}
block|}
end_class

end_unit

