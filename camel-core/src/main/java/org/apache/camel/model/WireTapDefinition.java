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
name|Endpoint
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
name|ExchangePattern
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
name|processor
operator|.
name|WireTapProcessor
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
name|util
operator|.
name|concurrent
operator|.
name|ExecutorServiceHelper
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;wireTap/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"wireTap"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|WireTapDefinition
specifier|public
class|class
name|WireTapDefinition
extends|extends
name|SendDefinition
argument_list|<
name|WireTapDefinition
argument_list|>
implements|implements
name|ExecutorServiceAwareDefinition
argument_list|<
name|ProcessorDefinition
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|newExchangeProcessor
specifier|private
name|Processor
name|newExchangeProcessor
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"processorRef"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|newExchangeProcessorRef
specifier|private
name|String
name|newExchangeProcessorRef
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"body"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|newExchangeExpression
specifier|private
name|ExpressionSubElementDefinition
name|newExchangeExpression
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
DECL|method|WireTapDefinition ()
specifier|public
name|WireTapDefinition
parameter_list|()
block|{     }
DECL|method|WireTapDefinition (String uri)
specifier|public
name|WireTapDefinition
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|WireTapDefinition (Endpoint endpoint)
specifier|public
name|WireTapDefinition
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|setEndpoint
argument_list|(
name|endpoint
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
name|Endpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|executorService
operator|=
name|ExecutorServiceHelper
operator|.
name|getConfiguredExecutorService
argument_list|(
name|routeContext
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|executorService
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newDefaultThreadPool
argument_list|(
name|this
argument_list|,
literal|"WireTap"
argument_list|)
expr_stmt|;
block|}
name|WireTapProcessor
name|answer
init|=
operator|new
name|WireTapProcessor
argument_list|(
name|endpoint
argument_list|,
name|getPattern
argument_list|()
argument_list|,
name|executorService
argument_list|)
decl_stmt|;
if|if
condition|(
name|newExchangeProcessorRef
operator|!=
literal|null
condition|)
block|{
name|newExchangeProcessor
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|newExchangeProcessorRef
argument_list|,
name|Processor
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setNewExchangeProcessor
argument_list|(
name|newExchangeProcessor
argument_list|)
expr_stmt|;
if|if
condition|(
name|newExchangeExpression
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setNewExchangeExpression
argument_list|(
name|newExchangeExpression
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getPattern ()
specifier|public
name|ExchangePattern
name|getPattern
parameter_list|()
block|{
return|return
name|ExchangePattern
operator|.
name|InOnly
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
literal|"WireTap["
operator|+
name|getLabel
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
literal|"wireTap"
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|executorService (ExecutorService executorService)
specifier|public
name|ProcessorDefinition
name|executorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
comment|// wiretap has no outputs and therefore we cannot use custom wiretap builder methods in Java DSL
comment|// as the Java DSL is stretched so far we can using regular Java
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"wireTap does not support these builder methods"
argument_list|)
throw|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|executorServiceRef (String executorServiceRef)
specifier|public
name|ProcessorDefinition
name|executorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
comment|// wiretap has no outputs and therefore we cannot use custom wiretap builder methods in Java DSL
comment|// as the Java DSL is stretched so far we can using regular Java
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"wireTap does not support these builder methods"
argument_list|)
throw|;
block|}
DECL|method|getNewExchangeProcessor ()
specifier|public
name|Processor
name|getNewExchangeProcessor
parameter_list|()
block|{
return|return
name|newExchangeProcessor
return|;
block|}
DECL|method|setNewExchangeProcessor (Processor processor)
specifier|public
name|void
name|setNewExchangeProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|newExchangeProcessor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|getNewExchangeProcessorRef ()
specifier|public
name|String
name|getNewExchangeProcessorRef
parameter_list|()
block|{
return|return
name|newExchangeProcessorRef
return|;
block|}
DECL|method|setNewExchangeProcessorRef (String ref)
specifier|public
name|void
name|setNewExchangeProcessorRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|newExchangeProcessorRef
operator|=
name|ref
expr_stmt|;
block|}
DECL|method|getNewExchangeExpression ()
specifier|public
name|ExpressionSubElementDefinition
name|getNewExchangeExpression
parameter_list|()
block|{
return|return
name|newExchangeExpression
return|;
block|}
DECL|method|setNewExchangeExpression (ExpressionSubElementDefinition expression)
specifier|public
name|void
name|setNewExchangeExpression
parameter_list|(
name|ExpressionSubElementDefinition
name|expression
parameter_list|)
block|{
name|this
operator|.
name|newExchangeExpression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|setNewExchangeExpression (Expression expression)
specifier|public
name|void
name|setNewExchangeExpression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|newExchangeExpression
operator|=
operator|new
name|ExpressionSubElementDefinition
argument_list|(
name|expression
argument_list|)
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

