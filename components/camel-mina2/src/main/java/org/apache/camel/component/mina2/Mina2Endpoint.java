begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina2
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
name|MultipleConsumersSupport
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
name|UriParam
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|session
operator|.
name|IoSession
import|;
end_import

begin_comment
comment|/**  * Endpoint for Camel MINA.  *  * @version   */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"mina2"
argument_list|,
name|syntax
operator|=
literal|"mina2:protocol:host:port"
argument_list|,
name|consumerClass
operator|=
name|Mina2Consumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"networking,tcp,udp"
argument_list|)
DECL|class|Mina2Endpoint
specifier|public
class|class
name|Mina2Endpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|MultipleConsumersSupport
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|Mina2Configuration
name|configuration
decl_stmt|;
DECL|method|Mina2Endpoint ()
specifier|public
name|Mina2Endpoint
parameter_list|()
block|{     }
DECL|method|Mina2Endpoint (String endpointUri, Component component, Mina2Configuration configuration)
specifier|public
name|Mina2Endpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|Mina2Configuration
name|configuration
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
name|configuration
operator|=
name|configuration
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"configuration"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Mina2Producer
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"configuration"
argument_list|)
expr_stmt|;
name|Mina2Consumer
name|answer
init|=
operator|new
name|Mina2Consumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createExchange (IoSession session, Object payload)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Object
name|payload
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Mina2Constants
operator|.
name|MINA_IOSESSION
argument_list|,
name|session
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Mina2Constants
operator|.
name|MINA_LOCAL_ADDRESS
argument_list|,
name|session
operator|.
name|getLocalAddress
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Mina2Constants
operator|.
name|MINA_REMOTE_ADDRESS
argument_list|,
name|session
operator|.
name|getRemoteAddress
argument_list|()
argument_list|)
expr_stmt|;
name|Mina2PayloadHelper
operator|.
name|setIn
argument_list|(
name|exchange
argument_list|,
name|payload
argument_list|)
expr_stmt|;
return|return
name|exchange
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
DECL|method|isMultipleConsumersSupported ()
specifier|public
name|boolean
name|isMultipleConsumersSupported
parameter_list|()
block|{
comment|// only datagram should allow multiple consumers
return|return
name|configuration
operator|.
name|isDatagramProtocol
argument_list|()
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getConfiguration ()
specifier|public
name|Mina2Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (Mina2Configuration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Mina2Configuration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
block|}
end_class

end_unit

