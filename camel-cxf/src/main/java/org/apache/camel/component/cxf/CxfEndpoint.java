begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
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
name|cxf
operator|.
name|BusException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|EndpointInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|local
operator|.
name|LocalTransportFactory
import|;
end_import

begin_comment
comment|/**  * The endpoint in the service engine  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfEndpoint
specifier|public
class|class
name|CxfEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|CxfExchange
argument_list|>
block|{
DECL|field|binding
specifier|private
name|CxfBinding
name|binding
decl_stmt|;
DECL|field|component
specifier|private
specifier|final
name|CxfComponent
name|component
decl_stmt|;
DECL|field|endpointInfo
specifier|private
specifier|final
name|EndpointInfo
name|endpointInfo
decl_stmt|;
DECL|field|inOut
specifier|private
name|boolean
name|inOut
init|=
literal|true
decl_stmt|;
DECL|method|CxfEndpoint (String uri, CxfComponent component, EndpointInfo endpointInfo)
specifier|public
name|CxfEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CxfComponent
name|component
parameter_list|,
name|EndpointInfo
name|endpointInfo
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
name|component
operator|=
name|component
expr_stmt|;
name|this
operator|.
name|endpointInfo
operator|=
name|endpointInfo
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|CxfExchange
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|startService
argument_list|(
operator|new
name|CxfProducer
argument_list|(
name|this
argument_list|,
name|getLocalTransportFactory
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor<CxfExchange> processor)
specifier|public
name|Consumer
argument_list|<
name|CxfExchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
argument_list|<
name|CxfExchange
argument_list|>
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|startService
argument_list|(
operator|new
name|CxfConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|getLocalTransportFactory
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|CxfExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|CxfExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createExchange (Message inMessage)
specifier|public
name|CxfExchange
name|createExchange
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
return|return
operator|new
name|CxfExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
name|inMessage
argument_list|)
return|;
block|}
DECL|method|getBinding ()
specifier|public
name|CxfBinding
name|getBinding
parameter_list|()
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
operator|new
name|CxfBinding
argument_list|()
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
DECL|method|setBinding (CxfBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|CxfBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|isInOut ()
specifier|public
name|boolean
name|isInOut
parameter_list|()
block|{
return|return
name|inOut
return|;
block|}
DECL|method|setInOut (boolean inOut)
specifier|public
name|void
name|setInOut
parameter_list|(
name|boolean
name|inOut
parameter_list|)
block|{
name|this
operator|.
name|inOut
operator|=
name|inOut
expr_stmt|;
block|}
DECL|method|getLocalTransportFactory ()
specifier|public
name|LocalTransportFactory
name|getLocalTransportFactory
parameter_list|()
throws|throws
name|BusException
block|{
return|return
name|component
operator|.
name|getLocalTransportFactory
argument_list|()
return|;
block|}
DECL|method|getEndpointInfo ()
specifier|public
name|EndpointInfo
name|getEndpointInfo
parameter_list|()
block|{
return|return
name|endpointInfo
return|;
block|}
DECL|method|getComponent ()
specifier|public
name|CxfComponent
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
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

