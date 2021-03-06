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
name|ResequenceDefinition
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
name|config
operator|.
name|BatchResequencerConfig
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
name|config
operator|.
name|StreamResequencerConfig
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
name|Resequencer
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
name|StreamResequencer
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
name|resequencer
operator|.
name|DefaultExchangeComparator
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
name|resequencer
operator|.
name|ExpressionResultComparator
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
name|CamelContextHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|ResequenceReifier
specifier|public
class|class
name|ResequenceReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|ResequenceDefinition
argument_list|>
block|{
DECL|method|ResequenceReifier (ProcessorDefinition<?> definition)
specifier|public
name|ResequenceReifier
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
name|ResequenceDefinition
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
comment|// if configured from XML then streamConfig has been set with the
comment|// configuration
if|if
condition|(
name|definition
operator|.
name|getResequencerConfig
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|definition
operator|.
name|getResequencerConfig
argument_list|()
operator|instanceof
name|StreamResequencerConfig
condition|)
block|{
name|definition
operator|.
name|setStreamConfig
argument_list|(
operator|(
name|StreamResequencerConfig
operator|)
name|definition
operator|.
name|getResequencerConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|definition
operator|.
name|setBatchConfig
argument_list|(
operator|(
name|BatchResequencerConfig
operator|)
name|definition
operator|.
name|getResequencerConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|definition
operator|.
name|getStreamConfig
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|createStreamResequencer
argument_list|(
name|routeContext
argument_list|,
name|definition
operator|.
name|getStreamConfig
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
if|if
condition|(
name|definition
operator|.
name|getBatchConfig
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// default as batch mode
name|definition
operator|.
name|batch
argument_list|()
expr_stmt|;
block|}
return|return
name|createBatchResequencer
argument_list|(
name|routeContext
argument_list|,
name|definition
operator|.
name|getBatchConfig
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**      * Creates a batch {@link Resequencer} instance applying the given      *<code>config</code>.      *      * @param routeContext route context.      * @param config batch resequencer configuration.      * @return the configured batch resequencer.      * @throws Exception can be thrown      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|createBatchResequencer (RouteContext routeContext, BatchResequencerConfig config)
specifier|protected
name|Resequencer
name|createBatchResequencer
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|BatchResequencerConfig
name|config
parameter_list|)
throws|throws
name|Exception
block|{
name|Processor
name|processor
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
name|Expression
name|expression
init|=
name|definition
operator|.
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
comment|// and wrap in unit of work
name|CamelInternalProcessor
name|internal
init|=
operator|new
name|CamelInternalProcessor
argument_list|(
name|processor
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
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|config
argument_list|,
literal|"config"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|boolean
name|isReverse
init|=
name|config
operator|.
name|getReverse
argument_list|()
operator|!=
literal|null
operator|&&
name|parseBoolean
argument_list|(
name|routeContext
argument_list|,
name|config
operator|.
name|getReverse
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|isAllowDuplicates
init|=
name|config
operator|.
name|getAllowDuplicates
argument_list|()
operator|!=
literal|null
operator|&&
name|parseBoolean
argument_list|(
name|routeContext
argument_list|,
name|config
operator|.
name|getAllowDuplicates
argument_list|()
argument_list|)
decl_stmt|;
name|Resequencer
name|resequencer
init|=
operator|new
name|Resequencer
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|internal
argument_list|,
name|expression
argument_list|,
name|isAllowDuplicates
argument_list|,
name|isReverse
argument_list|)
decl_stmt|;
name|resequencer
operator|.
name|setBatchSize
argument_list|(
name|parseInt
argument_list|(
name|routeContext
argument_list|,
name|config
operator|.
name|getBatchSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|setBatchTimeout
argument_list|(
name|parseLong
argument_list|(
name|routeContext
argument_list|,
name|config
operator|.
name|getBatchTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|setReverse
argument_list|(
name|isReverse
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|setAllowDuplicates
argument_list|(
name|isAllowDuplicates
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getIgnoreInvalidExchanges
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|resequencer
operator|.
name|setIgnoreInvalidExchanges
argument_list|(
name|parseBoolean
argument_list|(
name|routeContext
argument_list|,
name|config
operator|.
name|getIgnoreInvalidExchanges
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|resequencer
return|;
block|}
comment|/**      * Creates a {@link StreamResequencer} instance applying the given      *<code>config</code>.      *      * @param routeContext route context.      * @param config stream resequencer configuration.      * @return the configured stream resequencer.      * @throws Exception can be thrwon      */
DECL|method|createStreamResequencer (RouteContext routeContext, StreamResequencerConfig config)
specifier|protected
name|StreamResequencer
name|createStreamResequencer
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|StreamResequencerConfig
name|config
parameter_list|)
throws|throws
name|Exception
block|{
name|Processor
name|processor
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
name|Expression
name|expression
init|=
name|definition
operator|.
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|CamelInternalProcessor
name|internal
init|=
operator|new
name|CamelInternalProcessor
argument_list|(
name|processor
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
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|config
argument_list|,
literal|"config"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ExpressionResultComparator
name|comparator
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getComparatorRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|comparator
operator|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|config
operator|.
name|getComparatorRef
argument_list|()
argument_list|,
name|ExpressionResultComparator
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|comparator
operator|=
name|config
operator|.
name|getComparator
argument_list|()
expr_stmt|;
if|if
condition|(
name|comparator
operator|==
literal|null
condition|)
block|{
name|comparator
operator|=
operator|new
name|DefaultExchangeComparator
argument_list|()
expr_stmt|;
block|}
block|}
name|comparator
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|StreamResequencer
name|resequencer
init|=
operator|new
name|StreamResequencer
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|internal
argument_list|,
name|comparator
argument_list|,
name|expression
argument_list|)
decl_stmt|;
name|resequencer
operator|.
name|setTimeout
argument_list|(
name|parseLong
argument_list|(
name|routeContext
argument_list|,
name|config
operator|.
name|getTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getDeliveryAttemptInterval
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|resequencer
operator|.
name|setDeliveryAttemptInterval
argument_list|(
name|parseLong
argument_list|(
name|routeContext
argument_list|,
name|config
operator|.
name|getDeliveryAttemptInterval
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|resequencer
operator|.
name|setCapacity
argument_list|(
name|parseInt
argument_list|(
name|routeContext
argument_list|,
name|config
operator|.
name|getCapacity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|setRejectOld
argument_list|(
name|parseBoolean
argument_list|(
name|routeContext
argument_list|,
name|config
operator|.
name|getRejectOld
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getIgnoreInvalidExchanges
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|resequencer
operator|.
name|setIgnoreInvalidExchanges
argument_list|(
name|parseBoolean
argument_list|(
name|routeContext
argument_list|,
name|config
operator|.
name|getIgnoreInvalidExchanges
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|resequencer
return|;
block|}
block|}
end_class

end_unit

