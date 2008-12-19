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
name|concurrent
operator|.
name|Executor
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
name|LinkedBlockingQueue
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
name|ThreadPoolExecutor
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
name|TimeUnit
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
name|model
operator|.
name|language
operator|.
name|ExpressionType
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
name|Splitter
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
name|aggregate
operator|.
name|AggregationStrategy
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
name|aggregate
operator|.
name|UseLatestAggregationStrategy
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
comment|/**  * Represents an XML&lt;split/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"split"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|SplitterType
specifier|public
class|class
name|SplitterType
extends|extends
name|ExpressionNode
block|{
annotation|@
name|XmlTransient
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|parallelProcessing
specifier|private
name|Boolean
name|parallelProcessing
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|executor
specifier|private
name|Executor
name|executor
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|threadPoolExecutorRef
specifier|private
name|String
name|threadPoolExecutorRef
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|streaming
specifier|private
name|Boolean
name|streaming
init|=
literal|false
decl_stmt|;
DECL|method|SplitterType ()
specifier|public
name|SplitterType
parameter_list|()
block|{     }
DECL|method|SplitterType (Expression expression)
specifier|public
name|SplitterType
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|SplitterType (ExpressionType expression)
specifier|public
name|SplitterType
parameter_list|(
name|ExpressionType
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|)
expr_stmt|;
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
literal|"Splitter["
operator|+
name|getExpression
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
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"splitter"
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
name|routeContext
operator|.
name|createProcessor
argument_list|(
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|aggregationStrategy
operator|==
literal|null
condition|)
block|{
name|aggregationStrategy
operator|=
operator|new
name|UseLatestAggregationStrategy
argument_list|()
expr_stmt|;
block|}
name|executor
operator|=
name|createThreadPoolExecutor
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
return|return
operator|new
name|Splitter
argument_list|(
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
argument_list|,
name|childProcessor
argument_list|,
name|aggregationStrategy
argument_list|,
name|isParallelProcessing
argument_list|()
argument_list|,
name|executor
argument_list|,
name|streaming
argument_list|)
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Set the expression that the splitter will use      *      * @return the builder      */
DECL|method|expression ()
specifier|public
name|ExpressionClause
argument_list|<
name|SplitterType
argument_list|>
name|expression
parameter_list|()
block|{
return|return
name|ExpressionClause
operator|.
name|createAndSetExpression
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Set the aggregationStrategy      *      * @return the builder      */
DECL|method|aggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|SplitterType
name|aggregationStrategy
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|setAggregationStrategy
argument_list|(
name|aggregationStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Doing the splitting work in parallel      *      * @return the builder      */
DECL|method|parallelProcessing ()
specifier|public
name|SplitterType
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
comment|/**      * Set the splitting action's thread model      *      * @param parallelProcessing<tt>true</tt> to use a thread pool, if<tt>false</tt> then work is done in the      * calling thread.      *      * @return the builder      */
DECL|method|parallelProcessing (boolean parallelProcessing)
specifier|public
name|SplitterType
name|parallelProcessing
parameter_list|(
name|boolean
name|parallelProcessing
parameter_list|)
block|{
name|setParallelProcessing
argument_list|(
name|parallelProcessing
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables streaming.       * See {@link SplitterType#setStreaming(boolean)} for more information      *      * @return the builder      */
DECL|method|streaming ()
specifier|public
name|SplitterType
name|streaming
parameter_list|()
block|{
name|setStreaming
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Setting the executor for executing the splitting action.       *      * @return the builder      */
DECL|method|executor (Executor executor)
specifier|public
name|SplitterType
name|executor
parameter_list|(
name|Executor
name|executor
parameter_list|)
block|{
name|setExecutor
argument_list|(
name|executor
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getAggregationStrategy ()
specifier|public
name|AggregationStrategy
name|getAggregationStrategy
parameter_list|()
block|{
return|return
name|aggregationStrategy
return|;
block|}
DECL|method|setAggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|void
name|setAggregationStrategy
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|this
operator|.
name|aggregationStrategy
operator|=
name|aggregationStrategy
expr_stmt|;
block|}
DECL|method|isParallelProcessing ()
specifier|public
name|boolean
name|isParallelProcessing
parameter_list|()
block|{
return|return
name|parallelProcessing
operator|!=
literal|null
condition|?
name|parallelProcessing
else|:
literal|false
return|;
block|}
DECL|method|setParallelProcessing (boolean parallelProcessing)
specifier|public
name|void
name|setParallelProcessing
parameter_list|(
name|boolean
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
comment|/**      * The splitter should use streaming -- exchanges are being sent as the data for them becomes available.      * This improves throughput and memory usage, but it has a drawback:       * - the sent exchanges will no longer contain the {@link Splitter#SPLIT_SIZE} header property       *       * @return whether or not streaming should be used      */
DECL|method|isStreaming ()
specifier|public
name|boolean
name|isStreaming
parameter_list|()
block|{
return|return
name|streaming
operator|!=
literal|null
condition|?
name|streaming
else|:
literal|false
return|;
block|}
DECL|method|setStreaming (boolean streaming)
specifier|public
name|void
name|setStreaming
parameter_list|(
name|boolean
name|streaming
parameter_list|)
block|{
name|this
operator|.
name|streaming
operator|=
name|streaming
expr_stmt|;
block|}
DECL|method|createThreadPoolExecutor (RouteContext routeContext)
specifier|private
name|Executor
name|createThreadPoolExecutor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|Executor
name|executor
init|=
name|getExecutor
argument_list|()
decl_stmt|;
if|if
condition|(
name|executor
operator|==
literal|null
operator|&&
name|threadPoolExecutorRef
operator|!=
literal|null
condition|)
block|{
name|executor
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|threadPoolExecutorRef
argument_list|,
name|ThreadPoolExecutor
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|executor
operator|==
literal|null
condition|)
block|{
comment|// fall back and use default
name|executor
operator|=
operator|new
name|ThreadPoolExecutor
argument_list|(
literal|4
argument_list|,
literal|16
argument_list|,
literal|0L
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|,
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|executor
return|;
block|}
DECL|method|getExecutor ()
specifier|public
name|Executor
name|getExecutor
parameter_list|()
block|{
return|return
name|executor
return|;
block|}
DECL|method|setExecutor (Executor executor)
specifier|public
name|void
name|setExecutor
parameter_list|(
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
block|}
end_class

end_unit

