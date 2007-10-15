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
name|XmlAccessType
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
name|Route
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

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"resequencer"
argument_list|)
DECL|class|ResequencerType
specifier|public
class|class
name|ResequencerType
extends|extends
name|ProcessorType
argument_list|<
name|ProcessorType
argument_list|>
block|{
annotation|@
name|XmlElementRef
DECL|field|interceptors
specifier|private
name|List
argument_list|<
name|InterceptorType
argument_list|>
name|interceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptorType
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|expressions
specifier|private
name|List
argument_list|<
name|ExpressionType
argument_list|>
name|expressions
init|=
operator|new
name|ArrayList
argument_list|<
name|ExpressionType
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|outputs
specifier|private
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
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
DECL|method|ResequencerType ()
specifier|public
name|ResequencerType
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ResequencerType (List<Expression> expressions)
specifier|public
name|ResequencerType
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
comment|/**      * Configures the stream-based resequencing algorithm using the default      * configuration.      *       * @return<code>this</code> instance.      */
DECL|method|stream ()
specifier|public
name|ResequencerType
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
comment|/**      * Configures the batch-based resequencing algorithm using the default      * configuration.      *       * @return<code>this</code> instance.      */
DECL|method|batch ()
specifier|public
name|ResequencerType
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
comment|/**      * Configures the stream-based resequencing algorithm using the given      * {@link StreamResequencerConfig}.      *       * @return<code>this</code> instance.      */
DECL|method|stream (StreamResequencerConfig config)
specifier|public
name|ResequencerType
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
comment|/**      * Configures the batch-based resequencing algorithm using the given      * {@link BatchResequencerConfig}.      *       * @return<code>this</code> instance.      */
DECL|method|batch (BatchResequencerConfig config)
specifier|public
name|ResequencerType
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
DECL|method|expression (ExpressionType expression)
specifier|public
name|ResequencerType
name|expression
parameter_list|(
name|ExpressionType
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Resequencer[ "
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
name|ExpressionType
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
name|ExpressionType
argument_list|>
name|getExpressions
parameter_list|()
block|{
return|return
name|expressions
return|;
block|}
DECL|method|getInterceptors ()
specifier|public
name|List
argument_list|<
name|InterceptorType
argument_list|>
name|getInterceptors
parameter_list|()
block|{
return|return
name|interceptors
return|;
block|}
DECL|method|setInterceptors (List<InterceptorType> interceptors)
specifier|public
name|void
name|setInterceptors
parameter_list|(
name|List
argument_list|<
name|InterceptorType
argument_list|>
name|interceptors
parameter_list|)
block|{
name|this
operator|.
name|interceptors
operator|=
name|interceptors
expr_stmt|;
block|}
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorType
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
comment|//
comment|// TODO: find out how to have these two within an<xsd:choice>
comment|//
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
comment|//
comment|// END_TODO
comment|//
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
return|return
name|createStreamResequencer
argument_list|(
name|routeContext
argument_list|,
name|streamConfig
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addRoutes (RouteContext routeContext, Collection<Route> routes)
specifier|public
name|void
name|addRoutes
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
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
name|routes
operator|.
name|add
argument_list|(
name|createBatchResequencerRoute
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// StreamResequencer created via createProcessor method
name|super
operator|.
name|addRoutes
argument_list|(
name|routeContext
argument_list|,
name|routes
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createBatchResequencerRoute (RouteContext routeContext)
specifier|private
name|Route
argument_list|<
name|Exchange
argument_list|>
name|createBatchResequencerRoute
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Resequencer
name|resequencer
init|=
name|createBatchResequencer
argument_list|(
name|routeContext
argument_list|,
name|batchConfig
argument_list|)
decl_stmt|;
return|return
operator|new
name|Route
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|routeContext
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|resequencer
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"BatchResequencerRoute["
operator|+
name|getEndpoint
argument_list|()
operator|+
literal|" -> "
operator|+
name|resequencer
operator|.
name|getProcessor
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
return|;
block|}
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
name|getEndpoint
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
argument_list|,
name|config
operator|.
name|getCapacity
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
name|ExpressionType
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

