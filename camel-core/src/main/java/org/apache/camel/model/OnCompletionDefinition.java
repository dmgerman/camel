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
name|Collection
import|;
end_import

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
name|Map
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
name|processor
operator|.
name|CamelInternalProcessor
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
name|spi
operator|.
name|Metadata
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
comment|/**  * Route to be executed when normal route processing completes  *  * @version   */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing"
argument_list|)
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
name|OnCompletionDefinition
argument_list|>
implements|implements
name|ExecutorServiceAwareDefinition
argument_list|<
name|OnCompletionDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"AfterConsumer"
argument_list|)
DECL|field|mode
specifier|private
name|OnCompletionMode
name|mode
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|onCompleteOnly
specifier|private
name|Boolean
name|onCompleteOnly
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|onFailureOnly
specifier|private
name|Boolean
name|onFailureOnly
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"onWhen"
argument_list|)
DECL|field|onWhen
specifier|private
name|WhenDefinition
name|onWhen
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|parallelProcessing
specifier|private
name|Boolean
name|parallelProcessing
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|executorServiceRef
specifier|private
name|String
name|executorServiceRef
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"useOriginalMessage"
argument_list|)
DECL|field|useOriginalMessagePolicy
specifier|private
name|Boolean
name|useOriginalMessagePolicy
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|outputs
specifier|private
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
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
name|XmlTransient
DECL|field|routeScoped
specifier|private
name|Boolean
name|routeScoped
decl_stmt|;
comment|// TODO: in Camel 3.0 the OnCompletionDefinition should not contain state and OnCompletion processors
annotation|@
name|XmlTransient
DECL|field|onCompletions
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Processor
argument_list|>
name|onCompletions
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Processor
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|OnCompletionDefinition ()
specifier|public
name|OnCompletionDefinition
parameter_list|()
block|{     }
DECL|method|isRouteScoped ()
specifier|public
name|boolean
name|isRouteScoped
parameter_list|()
block|{
comment|// is context scoped by default
return|return
name|routeScoped
operator|!=
literal|null
condition|?
name|routeScoped
else|:
literal|false
return|;
block|}
DECL|method|getOnCompletion (String routeId)
specifier|public
name|Processor
name|getOnCompletion
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
return|return
name|onCompletions
operator|.
name|get
argument_list|(
name|routeId
argument_list|)
return|;
block|}
DECL|method|getOnCompletions ()
specifier|public
name|Collection
argument_list|<
name|Processor
argument_list|>
name|getOnCompletions
parameter_list|()
block|{
return|return
name|onCompletions
operator|.
name|values
argument_list|()
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
DECL|method|isTopLevelOnly ()
specifier|public
name|boolean
name|isTopLevelOnly
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
comment|// assign whether this was a route scoped onCompletion or not
comment|// we need to know this later when setting the parent, as only route scoped should have parent
comment|// Note: this logic can possible be removed when the Camel routing engine decides at runtime
comment|// to apply onCompletion in a more dynamic fashion than current code base
comment|// and therefore is in a better position to decide among context/route scoped OnCompletion at runtime
if|if
condition|(
name|routeScoped
operator|==
literal|null
condition|)
block|{
name|routeScoped
operator|=
name|super
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
expr_stmt|;
block|}
name|boolean
name|isOnCompleteOnly
init|=
name|getOnCompleteOnly
argument_list|()
operator|!=
literal|null
operator|&&
name|getOnCompleteOnly
argument_list|()
decl_stmt|;
name|boolean
name|isOnFailureOnly
init|=
name|getOnFailureOnly
argument_list|()
operator|!=
literal|null
operator|&&
name|getOnFailureOnly
argument_list|()
decl_stmt|;
name|boolean
name|isParallelProcessing
init|=
name|getParallelProcessing
argument_list|()
operator|!=
literal|null
operator|&&
name|getParallelProcessing
argument_list|()
decl_stmt|;
name|boolean
name|original
init|=
name|getUseOriginalMessagePolicy
argument_list|()
operator|!=
literal|null
operator|&&
name|getUseOriginalMessagePolicy
argument_list|()
decl_stmt|;
if|if
condition|(
name|isOnCompleteOnly
operator|&&
name|isOnFailureOnly
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
name|String
name|routeId
init|=
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|idOrCreate
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
decl_stmt|;
name|Processor
name|childProcessor
init|=
name|this
operator|.
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// wrap the on completion route in a unit of work processor
name|CamelInternalProcessor
name|internal
init|=
operator|new
name|CamelInternalProcessor
argument_list|(
name|childProcessor
argument_list|)
decl_stmt|;
name|internal
operator|.
name|addAdvice
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|UnitOfWorkProcessorAdvice
argument_list|(
name|routeId
argument_list|)
argument_list|)
expr_stmt|;
name|internal
operator|.
name|addAdvice
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|RouteContextAdvice
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
name|onCompletions
operator|.
name|put
argument_list|(
name|routeId
argument_list|,
name|internal
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
name|boolean
name|shutdownThreadPool
init|=
name|ProcessorDefinitionHelper
operator|.
name|willCreateNewThreadPool
argument_list|(
name|routeContext
argument_list|,
name|this
argument_list|,
name|isParallelProcessing
argument_list|)
decl_stmt|;
name|ExecutorService
name|threadPool
init|=
name|ProcessorDefinitionHelper
operator|.
name|getConfiguredExecutorService
argument_list|(
name|routeContext
argument_list|,
literal|"OnCompletion"
argument_list|,
name|this
argument_list|,
name|isParallelProcessing
argument_list|)
decl_stmt|;
comment|// should be after consumer by default
name|boolean
name|afterConsumer
init|=
name|mode
operator|==
literal|null
operator|||
name|mode
operator|==
name|OnCompletionMode
operator|.
name|AfterConsumer
decl_stmt|;
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
name|internal
argument_list|,
name|threadPool
argument_list|,
name|shutdownThreadPool
argument_list|,
name|isOnCompleteOnly
argument_list|,
name|isOnFailureOnly
argument_list|,
name|when
argument_list|,
name|original
argument_list|,
name|afterConsumer
argument_list|)
decl_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Removes all existing {@link org.apache.camel.model.OnCompletionDefinition} from the definition.      *<p/>      * This is used to let route scoped<tt>onCompletion</tt> overrule any global<tt>onCompletion</tt>.      * Hence we remove all existing as they are global.      *      * @param definition the parent definition that is the route      */
DECL|method|removeAllOnCompletionDefinition (ProcessorDefinition<?> definition)
specifier|public
name|void
name|removeAllOnCompletionDefinition
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
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
argument_list|<
name|?
argument_list|>
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
DECL|method|end ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
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
comment|/**      * Sets the mode to be after route is done (default due backwards compatible).      *<p/>      * This executes the on completion work<i>after</i> the route consumer have written response      * back to the callee (if its InOut mode).      *      * @return the builder      */
DECL|method|modeAfterConsumer ()
specifier|public
name|OnCompletionDefinition
name|modeAfterConsumer
parameter_list|()
block|{
name|setMode
argument_list|(
name|OnCompletionMode
operator|.
name|AfterConsumer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the mode to be before consumer is done.      *<p/>      * This allows the on completion work to execute<i>before</i> the route consumer, writes any response      * back to the callee (if its InOut mode).      *      * @return the builder      */
DECL|method|modeBeforeConsumer ()
specifier|public
name|OnCompletionDefinition
name|modeBeforeConsumer
parameter_list|()
block|{
name|setMode
argument_list|(
name|OnCompletionMode
operator|.
name|BeforeConsumer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Will only synchronize when the {@link org.apache.camel.Exchange} completed successfully (no errors).      *      * @return the builder      */
DECL|method|onCompleteOnly ()
specifier|public
name|OnCompletionDefinition
name|onCompleteOnly
parameter_list|()
block|{
name|boolean
name|isOnFailureOnly
init|=
name|getOnFailureOnly
argument_list|()
operator|!=
literal|null
operator|&&
name|getOnFailureOnly
argument_list|()
decl_stmt|;
if|if
condition|(
name|isOnFailureOnly
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
name|boolean
name|isOnCompleteOnly
init|=
name|getOnCompleteOnly
argument_list|()
operator|!=
literal|null
operator|&&
name|getOnCompleteOnly
argument_list|()
decl_stmt|;
if|if
condition|(
name|isOnCompleteOnly
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
comment|/**      * Will use the original input body when an {@link org.apache.camel.Exchange} for this on completion.      *<p/>      * By default this feature is off.      *      * @return the builder      */
DECL|method|useOriginalBody ()
specifier|public
name|OnCompletionDefinition
name|useOriginalBody
parameter_list|()
block|{
name|setUseOriginalMessagePolicy
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
comment|/**      * To use a custom Thread Pool to be used for parallel processing.      * Notice if you set this option, then parallel processing is automatic implied, and you do not have to enable that option as well.      */
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
comment|/**      * Refers to a custom Thread Pool to be used for parallel processing.      * Notice if you set this option, then parallel processing is automatic implied, and you do not have to enable that option as well.      */
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
comment|/**      * If enabled then the on completion process will run asynchronously by a separate thread from a thread pool.      * By default this is false, meaning the on completion process will run synchronously using the same caller thread as from the route.      *      * @return the builder      */
DECL|method|parallelProcessing ()
specifier|public
name|OnCompletionDefinition
name|parallelProcessing
parameter_list|()
block|{
name|setParallelProcessing
argument_list|(
literal|true
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
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
return|;
block|}
DECL|method|setOutputs (List<ProcessorDefinition<?>> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
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
DECL|method|isOutputSupported ()
specifier|public
name|boolean
name|isOutputSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getMode ()
specifier|public
name|OnCompletionMode
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
comment|/**      * Sets the on completion mode.      *<p/>      * The default value is AfterConsumer      */
DECL|method|setMode (OnCompletionMode mode)
specifier|public
name|void
name|setMode
parameter_list|(
name|OnCompletionMode
name|mode
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|mode
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
DECL|method|getUseOriginalMessagePolicy ()
specifier|public
name|Boolean
name|getUseOriginalMessagePolicy
parameter_list|()
block|{
return|return
name|useOriginalMessagePolicy
return|;
block|}
comment|/**      * Will use the original input body when an {@link org.apache.camel.Exchange} for this on completion.      *<p/>      * By default this feature is off.      */
DECL|method|setUseOriginalMessagePolicy (Boolean useOriginalMessagePolicy)
specifier|public
name|void
name|setUseOriginalMessagePolicy
parameter_list|(
name|Boolean
name|useOriginalMessagePolicy
parameter_list|)
block|{
name|this
operator|.
name|useOriginalMessagePolicy
operator|=
name|useOriginalMessagePolicy
expr_stmt|;
block|}
DECL|method|getParallelProcessing ()
specifier|public
name|Boolean
name|getParallelProcessing
parameter_list|()
block|{
return|return
name|parallelProcessing
return|;
block|}
DECL|method|setParallelProcessing (Boolean parallelProcessing)
specifier|public
name|void
name|setParallelProcessing
parameter_list|(
name|Boolean
name|parallelProcessing
parameter_list|)
block|{
name|this
operator|.
name|parallelProcessing
operator|=
name|parallelProcessing
expr_stmt|;
block|}
block|}
end_class

end_unit

