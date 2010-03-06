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
name|model
operator|.
name|language
operator|.
name|ExpressionDefinition
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

begin_comment
comment|/**  * Represents an XML&lt;resequence/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"resequence"
argument_list|)
DECL|class|ResequenceDefinition
specifier|public
class|class
name|ResequenceDefinition
extends|extends
name|ProcessorDefinition
argument_list|<
name|ProcessorDefinition
argument_list|>
block|{
annotation|@
name|XmlElementRef
DECL|field|expressions
specifier|private
name|List
argument_list|<
name|ExpressionDefinition
argument_list|>
name|expressions
init|=
operator|new
name|ArrayList
argument_list|<
name|ExpressionDefinition
argument_list|>
argument_list|()
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
comment|// Binding annotation at setter
DECL|field|batchConfig
specifier|private
name|BatchResequencerConfig
name|batchConfig
decl_stmt|;
comment|// Binding annotation at setter
DECL|field|streamConfig
specifier|private
name|StreamResequencerConfig
name|streamConfig
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|expressionList
specifier|private
name|List
argument_list|<
name|Expression
argument_list|>
name|expressionList
decl_stmt|;
DECL|method|ResequenceDefinition ()
specifier|public
name|ResequenceDefinition
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ResequenceDefinition (List<Expression> expressions)
specifier|public
name|ResequenceDefinition
parameter_list|(
name|List
argument_list|<
name|Expression
argument_list|>
name|expressions
parameter_list|)
block|{
name|this
operator|.
name|expressionList
operator|=
name|expressions
expr_stmt|;
name|this
operator|.
name|batch
argument_list|()
expr_stmt|;
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
literal|"resequence"
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Configures the stream-based resequencing algorithm using the default      * configuration.      *      * @return the builder      */
DECL|method|stream ()
specifier|public
name|ResequenceDefinition
name|stream
parameter_list|()
block|{
return|return
name|stream
argument_list|(
name|StreamResequencerConfig
operator|.
name|getDefault
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Configures the batch-based resequencing algorithm using the default      * configuration.      *      * @return the builder      */
DECL|method|batch ()
specifier|public
name|ResequenceDefinition
name|batch
parameter_list|()
block|{
return|return
name|batch
argument_list|(
name|BatchResequencerConfig
operator|.
name|getDefault
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Configures the stream-based resequencing algorithm using the given      * {@link StreamResequencerConfig}.      *      * @param config  the config      * @return the builder      */
DECL|method|stream (StreamResequencerConfig config)
specifier|public
name|ResequenceDefinition
name|stream
parameter_list|(
name|StreamResequencerConfig
name|config
parameter_list|)
block|{
name|this
operator|.
name|streamConfig
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|batchConfig
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the batch-based resequencing algorithm using the given      * {@link BatchResequencerConfig}.      *      * @param config  the config      * @return the builder      */
DECL|method|batch (BatchResequencerConfig config)
specifier|public
name|ResequenceDefinition
name|batch
parameter_list|(
name|BatchResequencerConfig
name|config
parameter_list|)
block|{
name|this
operator|.
name|batchConfig
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|streamConfig
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the expression to use for reordering      *      * @param expression  the expression      * @return the builder      */
DECL|method|expression (ExpressionDefinition expression)
specifier|public
name|ResequenceDefinition
name|expression
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
name|expressions
operator|.
name|add
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the timeout      * @param timeout  timeout in millis      * @return the builder      */
DECL|method|timeout (long timeout)
specifier|public
name|ResequenceDefinition
name|timeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
if|if
condition|(
name|batchConfig
operator|!=
literal|null
condition|)
block|{
name|batchConfig
operator|.
name|setBatchTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|streamConfig
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Sets the in batch size for number of exchanges received      * @param batchSize  the batch size      * @return the builder      */
DECL|method|size (int batchSize)
specifier|public
name|ResequenceDefinition
name|size
parameter_list|(
name|int
name|batchSize
parameter_list|)
block|{
if|if
condition|(
name|batchConfig
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"size() only supported for batch resequencer"
argument_list|)
throw|;
block|}
name|batchConfig
operator|.
name|setBatchSize
argument_list|(
name|batchSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the capacity for the stream resequencer      *      * @param capacity  the capacity      * @return the builder      */
DECL|method|capacity (int capacity)
specifier|public
name|ResequenceDefinition
name|capacity
parameter_list|(
name|int
name|capacity
parameter_list|)
block|{
if|if
condition|(
name|streamConfig
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"capacity() only supported for stream resequencer"
argument_list|)
throw|;
block|}
name|streamConfig
operator|.
name|setCapacity
argument_list|(
name|capacity
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the comparator to use for stream resequencer      *      * @param comparator  the comparator      * @return the builder      */
DECL|method|comparator (ExpressionResultComparator comparator)
specifier|public
name|ResequenceDefinition
name|comparator
parameter_list|(
name|ExpressionResultComparator
name|comparator
parameter_list|)
block|{
if|if
condition|(
name|streamConfig
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"comparator() only supported for stream resequencer"
argument_list|)
throw|;
block|}
name|streamConfig
operator|.
name|setComparator
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
return|return
name|this
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
literal|"Resequencer["
operator|+
name|getExpressions
argument_list|()
operator|+
literal|" -> "
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
name|ExpressionDefinition
operator|.
name|getLabel
argument_list|(
name|getExpressions
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getExpressions ()
specifier|public
name|List
argument_list|<
name|ExpressionDefinition
argument_list|>
name|getExpressions
parameter_list|()
block|{
return|return
name|expressions
return|;
block|}
DECL|method|getExpressionList ()
specifier|public
name|List
argument_list|<
name|Expression
argument_list|>
name|getExpressionList
parameter_list|()
block|{
return|return
name|expressionList
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
DECL|method|getBatchConfig ()
specifier|public
name|BatchResequencerConfig
name|getBatchConfig
parameter_list|()
block|{
return|return
name|batchConfig
return|;
block|}
DECL|method|getBatchConfig (BatchResequencerConfig defaultConfig)
specifier|public
name|BatchResequencerConfig
name|getBatchConfig
parameter_list|(
name|BatchResequencerConfig
name|defaultConfig
parameter_list|)
block|{
return|return
name|batchConfig
return|;
block|}
DECL|method|getStreamConfig ()
specifier|public
name|StreamResequencerConfig
name|getStreamConfig
parameter_list|()
block|{
return|return
name|streamConfig
return|;
block|}
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"batch-config"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|method|setBatchConfig (BatchResequencerConfig batchConfig)
specifier|public
name|void
name|setBatchConfig
parameter_list|(
name|BatchResequencerConfig
name|batchConfig
parameter_list|)
block|{
name|batch
argument_list|(
name|batchConfig
argument_list|)
expr_stmt|;
block|}
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"stream-config"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|method|setStreamConfig (StreamResequencerConfig streamConfig)
specifier|public
name|void
name|setStreamConfig
parameter_list|(
name|StreamResequencerConfig
name|streamConfig
parameter_list|)
block|{
name|stream
argument_list|(
name|streamConfig
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
if|if
condition|(
name|batchConfig
operator|!=
literal|null
condition|)
block|{
return|return
name|createBatchResequencer
argument_list|(
name|routeContext
argument_list|,
name|batchConfig
argument_list|)
return|;
block|}
else|else
block|{
comment|// streamConfig should be non-null if batchConfig is null
return|return
name|createStreamResequencer
argument_list|(
name|routeContext
argument_list|,
name|streamConfig
argument_list|)
return|;
block|}
block|}
comment|/**      * Creates a batch {@link Resequencer} instance applying the given      *<code>config</code>.      *       * @param routeContext route context.      * @param config batch resequencer configuration.      * @return the configured batch resequencer.      * @throws Exception can be thrown      */
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
name|routeContext
operator|.
name|createProcessor
argument_list|(
name|this
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
name|processor
argument_list|,
name|resolveExpressionList
argument_list|(
name|routeContext
argument_list|)
argument_list|)
decl_stmt|;
name|resequencer
operator|.
name|setBatchSize
argument_list|(
name|config
operator|.
name|getBatchSize
argument_list|()
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|setBatchTimeout
argument_list|(
name|config
operator|.
name|getBatchTimeout
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|resequencer
return|;
block|}
comment|/**      * Creates a {@link StreamResequencer} instance applying the given      *<code>config</code>.      *       * @param routeContext route context.      * @param config stream resequencer configuration.      * @return the configured stream resequencer.      * @throws Exception can be thrwon      */
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
name|config
operator|.
name|getComparator
argument_list|()
operator|.
name|setExpressions
argument_list|(
name|resolveExpressionList
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
name|Processor
name|processor
init|=
name|routeContext
operator|.
name|createProcessor
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|StreamResequencer
name|resequencer
init|=
operator|new
name|StreamResequencer
argument_list|(
name|processor
argument_list|,
name|config
operator|.
name|getComparator
argument_list|()
argument_list|)
decl_stmt|;
name|resequencer
operator|.
name|setTimeout
argument_list|(
name|config
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|resequencer
operator|.
name|setCapacity
argument_list|(
name|config
operator|.
name|getCapacity
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|resequencer
return|;
block|}
DECL|method|resolveExpressionList (RouteContext routeContext)
specifier|private
name|List
argument_list|<
name|Expression
argument_list|>
name|resolveExpressionList
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|expressionList
operator|==
literal|null
condition|)
block|{
name|expressionList
operator|=
operator|new
name|ArrayList
argument_list|<
name|Expression
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|ExpressionDefinition
name|expression
range|:
name|expressions
control|)
block|{
name|expressionList
operator|.
name|add
argument_list|(
name|expression
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|expressionList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No expressions configured for: "
operator|+
name|this
argument_list|)
throw|;
block|}
return|return
name|expressionList
return|;
block|}
block|}
end_class

end_unit

