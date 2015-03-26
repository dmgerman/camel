begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.binding
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|binding
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
name|CamelContextAware
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
name|Component
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
name|Consumer
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
name|Producer
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
name|DefaultEndpoint
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
name|PipelineHelper
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
name|Binding
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
name|HasBinding
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
name|UriEndpoint
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
name|UriPath
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
name|ServiceHelper
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
import|;
end_import

begin_comment
comment|/**  * Applies a {@link org.apache.camel.spi.Binding} to an underlying {@link Endpoint} so that the binding processes messages  * before its sent to the endpoint and processes messages received by the endpoint consumer before its passed  * to the real consumer.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"binding"
argument_list|,
name|title
operator|=
literal|"Binding"
argument_list|,
name|syntax
operator|=
literal|"binding:bindingName:delegateUri"
argument_list|,
name|consumerClass
operator|=
name|BindingConsumerProcessor
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"core,transformation"
argument_list|)
DECL|class|BindingEndpoint
specifier|public
class|class
name|BindingEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|HasBinding
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|bindingName
specifier|private
specifier|final
name|String
name|bindingName
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|delegateUri
specifier|private
specifier|final
name|String
name|delegateUri
decl_stmt|;
DECL|field|binding
specifier|private
name|Binding
name|binding
decl_stmt|;
DECL|field|delegate
specifier|private
name|Endpoint
name|delegate
decl_stmt|;
annotation|@
name|Deprecated
DECL|method|BindingEndpoint (String uri, Component component, Binding binding, Endpoint delegate)
specifier|public
name|BindingEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|Binding
name|binding
parameter_list|,
name|Endpoint
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|bindingName
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|delegateUri
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|BindingEndpoint (String uri, Component component, String bindingName, String delegateUri)
specifier|public
name|BindingEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|bindingName
parameter_list|,
name|String
name|delegateUri
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|bindingName
operator|=
name|bindingName
expr_stmt|;
name|this
operator|.
name|delegateUri
operator|=
name|delegateUri
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|BindingProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|Processor
name|bindingProcessor
init|=
operator|new
name|BindingConsumerProcessor
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
return|return
name|delegate
operator|.
name|createConsumer
argument_list|(
name|bindingProcessor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getBinding ()
specifier|public
name|Binding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
DECL|method|getDelegate ()
specifier|public
name|Endpoint
name|getDelegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
comment|/**      * Name of the binding to lookup in the Camel registry.      */
DECL|method|getBindingName ()
specifier|public
name|String
name|getBindingName
parameter_list|()
block|{
return|return
name|bindingName
return|;
block|}
comment|/**      * Uri of the delegate endpoint.      */
DECL|method|getDelegateUri ()
specifier|public
name|String
name|getDelegateUri
parameter_list|()
block|{
return|return
name|delegateUri
return|;
block|}
comment|/**      * Applies the {@link Binding} processor to the given exchange before passing it on to the delegateProcessor (either a producer or consumer)      */
DECL|method|pipelineBindingProcessor (Processor bindingProcessor, Exchange exchange, Processor delegateProcessor)
specifier|public
name|void
name|pipelineBindingProcessor
parameter_list|(
name|Processor
name|bindingProcessor
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|delegateProcessor
parameter_list|)
throws|throws
name|Exception
block|{
name|bindingProcessor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Exchange
name|delegateExchange
init|=
name|PipelineHelper
operator|.
name|createNextExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|delegateProcessor
operator|.
name|process
argument_list|(
name|delegateExchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|bindingName
argument_list|,
name|Binding
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|delegate
operator|==
literal|null
condition|)
block|{
name|delegate
operator|=
name|getMandatoryEndpoint
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|delegateUri
argument_list|)
expr_stmt|;
block|}
comment|// inject CamelContext
if|if
condition|(
name|binding
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|binding
operator|)
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|delegate
argument_list|,
name|binding
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|delegate
argument_list|,
name|binding
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

