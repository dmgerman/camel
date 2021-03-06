begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|CamelContext
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
name|PollingConsumer
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

begin_comment
comment|/**  * An endpoint which allows exchanges to be sent into it which just invokes a  * given {@link Processor}. This component does not support the use of  * consumers.  *<p/>  *<br/>Implementors beware that this endpoint creates producers and consumers which  * do not allow full control of their lifecycle as {@link org.apache.camel.Service}  * or {@link org.apache.camel.SuspendableService} would do.  * If your producers/consumers need more control over their lifecycle it is advised  * instead to extend {@link DefaultEndpoint}, {@link DefaultProducer}  * and {@link DefaultConsumer}.  */
end_comment

begin_class
DECL|class|ProcessorEndpoint
specifier|public
class|class
name|ProcessorEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|method|ProcessorEndpoint ()
specifier|protected
name|ProcessorEndpoint
parameter_list|()
block|{     }
DECL|method|ProcessorEndpoint (String endpointUri, CamelContext context, Processor processor)
specifier|public
name|ProcessorEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|ProcessorEndpoint (String endpointUri, Component component, Processor processor)
specifier|public
name|ProcessorEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|ProcessorEndpoint (String endpointUri, Component component)
specifier|protected
name|ProcessorEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
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
name|DefaultProducer
argument_list|(
name|this
argument_list|)
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|PollingConsumer
name|answer
init|=
operator|new
name|ProcessorPollingConsumer
argument_list|(
name|this
argument_list|,
name|getProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|configurePollingConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|processor
operator|==
literal|null
condition|)
block|{
name|processor
operator|=
name|createProcessor
argument_list|()
expr_stmt|;
block|}
return|return
name|processor
return|;
block|}
DECL|method|setProcessor (Processor processor)
specifier|public
name|void
name|setProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|createProcessor ()
specifier|protected
name|Processor
name|createProcessor
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|onExchange (Exchange exchange)
specifier|protected
name|void
name|onExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

