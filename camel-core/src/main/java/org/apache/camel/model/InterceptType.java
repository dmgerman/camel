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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Intercept
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
name|PredicateBuilder
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
name|Interceptor
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
comment|/**  * Represents an XML&lt;intercept/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"intercept"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|InterceptType
specifier|public
class|class
name|InterceptType
extends|extends
name|OutputType
argument_list|<
name|ProcessorType
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|proceed
specifier|private
name|ProceedType
name|proceed
init|=
operator|new
name|ProceedType
argument_list|()
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
literal|"Intercept["
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
name|Interceptor
name|interceptor
init|=
operator|new
name|Interceptor
argument_list|()
decl_stmt|;
name|routeContext
operator|.
name|intercept
argument_list|(
name|interceptor
argument_list|)
expr_stmt|;
specifier|final
name|Processor
name|interceptRoute
init|=
name|createOutputsProcessor
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|interceptor
operator|.
name|setInterceptorLogic
argument_list|(
name|interceptRoute
argument_list|)
expr_stmt|;
return|return
name|interceptor
return|;
block|}
comment|/**      * Applies this interceptor only if the given predicate is true      */
DECL|method|when (Predicate predicate)
specifier|public
name|ChoiceType
name|when
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|ChoiceType
name|choice
init|=
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|PredicateBuilder
operator|.
name|not
argument_list|(
name|predicate
argument_list|)
argument_list|)
decl_stmt|;
name|choice
operator|.
name|addOutput
argument_list|(
name|proceed
argument_list|)
expr_stmt|;
return|return
name|choice
operator|.
name|otherwise
argument_list|()
return|;
block|}
DECL|method|getProceed ()
specifier|public
name|ProceedType
name|getProceed
parameter_list|()
block|{
return|return
name|proceed
return|;
block|}
DECL|method|createProxy ()
specifier|public
name|InterceptType
name|createProxy
parameter_list|()
block|{
name|InterceptType
name|answer
init|=
operator|new
name|InterceptType
argument_list|()
decl_stmt|;
name|answer
operator|.
name|getOutputs
argument_list|()
operator|.
name|addAll
argument_list|(
name|this
operator|.
name|getOutputs
argument_list|()
argument_list|)
expr_stmt|;
comment|// hack: now we need to replace the proceed of the proxy with its own
comment|// a bit ugly, operating based on the assumption that the proceed is
comment|// in its outputs (if proceed() was called) and/or in the
comment|// outputs of the otherwise or last when clause for the predicated version.
name|proxifyProceed
argument_list|(
name|this
operator|.
name|getProceed
argument_list|()
argument_list|,
name|answer
operator|.
name|getProceed
argument_list|()
argument_list|,
name|answer
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// this is for the predicate version
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processor
init|=
name|answer
decl_stmt|;
name|processor
operator|=
operator|(
name|ProcessorType
argument_list|<
name|?
argument_list|>
operator|)
name|answer
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|processor
operator|instanceof
name|ChoiceType
condition|)
block|{
name|ChoiceType
name|choice
init|=
operator|(
name|ChoiceType
operator|)
name|processor
decl_stmt|;
name|proxifyProceed
argument_list|(
name|this
operator|.
name|getProceed
argument_list|()
argument_list|,
name|answer
operator|.
name|getProceed
argument_list|()
argument_list|,
name|choice
operator|.
name|getWhenClauses
argument_list|()
operator|.
name|get
argument_list|(
name|choice
operator|.
name|getWhenClauses
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|proxifyProceed
argument_list|(
name|this
operator|.
name|getProceed
argument_list|()
argument_list|,
name|answer
operator|.
name|getProceed
argument_list|()
argument_list|,
name|choice
operator|.
name|getOtherwise
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|proxifyProceed (ProceedType orig, ProceedType proxy, ProcessorType<?> processor)
specifier|private
name|void
name|proxifyProceed
parameter_list|(
name|ProceedType
name|orig
parameter_list|,
name|ProceedType
name|proxy
parameter_list|,
name|ProcessorType
argument_list|<
name|?
argument_list|>
name|processor
parameter_list|)
block|{
name|int
name|index
init|=
name|processor
operator|.
name|getOutputs
argument_list|()
operator|.
name|indexOf
argument_list|(
name|orig
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>=
literal|0
condition|)
block|{
comment|// replace original proceed with proxy
name|processor
operator|.
name|addOutput
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|outs
init|=
name|processor
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
name|outs
operator|.
name|remove
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
name|outs
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|proxy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

