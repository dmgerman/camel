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
name|ThreadPoolExecutor
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
name|MulticastProcessor
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"multicast"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|MulticastType
specifier|public
class|class
name|MulticastType
extends|extends
name|OutputType
argument_list|<
name|ProcessorType
argument_list|>
block|{
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
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|threadPoolExecutor
specifier|private
name|ThreadPoolExecutor
name|threadPoolExecutor
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
literal|"Multicast["
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
return|return
name|createOutputsProcessor
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
DECL|method|createCompositeProcessor (List<Processor> list)
specifier|protected
name|Processor
name|createCompositeProcessor
parameter_list|(
name|List
argument_list|<
name|Processor
argument_list|>
name|list
parameter_list|)
block|{
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
return|return
operator|new
name|MulticastProcessor
argument_list|(
name|list
argument_list|,
name|aggregationStrategy
argument_list|,
name|isParallelProcessing
argument_list|()
argument_list|,
name|threadPoolExecutor
argument_list|)
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
DECL|method|getThreadPoolExecutor ()
specifier|public
name|ThreadPoolExecutor
name|getThreadPoolExecutor
parameter_list|()
block|{
return|return
name|threadPoolExecutor
return|;
block|}
DECL|method|setThreadPoolExecutor (ThreadPoolExecutor executor)
specifier|public
name|void
name|setThreadPoolExecutor
parameter_list|(
name|ThreadPoolExecutor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|threadPoolExecutor
operator|=
name|executor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|wrapProcessorInInterceptors (RouteContext routeContext, Processor target)
specifier|protected
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|target
parameter_list|)
throws|throws
name|Exception
block|{
comment|// No need to wrap me in interceptors as they are all applied directly to my children
return|return
name|target
return|;
block|}
block|}
end_class

end_unit

