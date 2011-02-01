begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.cxfbean
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
operator|.
name|cxfbean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|component
operator|.
name|cxf
operator|.
name|CxfConstants
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
name|component
operator|.
name|cxf
operator|.
name|transport
operator|.
name|CamelDestination
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
name|Bus
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
name|message
operator|.
name|MessageImpl
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
name|ConduitInitiator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A CXF transport {@link org.apache.cxf.transport.Destination} that listens   * Camel {@link Exchange} from an associated {@link CxfBeanEndpoint}.  *    * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfBeanDestination
specifier|public
class|class
name|CxfBeanDestination
extends|extends
name|CamelDestination
implements|implements
name|Processor
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CxfBeanDestination
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cxfBeanComponent
specifier|private
name|CxfBeanComponent
name|cxfBeanComponent
decl_stmt|;
DECL|field|endpoint
specifier|private
name|CxfBeanEndpoint
name|endpoint
decl_stmt|;
DECL|method|CxfBeanDestination (CxfBeanComponent cxfBeanComponent, Bus bus, ConduitInitiator conduitInitiator, EndpointInfo endpointInfo)
specifier|public
name|CxfBeanDestination
parameter_list|(
name|CxfBeanComponent
name|cxfBeanComponent
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|ConduitInitiator
name|conduitInitiator
parameter_list|,
name|EndpointInfo
name|endpointInfo
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|cxfBeanComponent
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|bus
argument_list|,
name|conduitInitiator
argument_list|,
name|endpointInfo
argument_list|)
expr_stmt|;
name|this
operator|.
name|cxfBeanComponent
operator|=
name|cxfBeanComponent
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|activate ()
specifier|public
name|void
name|activate
parameter_list|()
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Activating CxfBeanDestination "
operator|+
name|getCamelDestinationUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|=
name|cxfBeanComponent
operator|.
name|getEndpoint
argument_list|(
name|getCamelDestinationUri
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Failed to find endpoint "
operator|+
name|getCamelDestinationUri
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|endpoint
operator|.
name|setProcessor
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|deactivate ()
specifier|public
name|void
name|deactivate
parameter_list|()
block|{     }
DECL|method|process (Exchange camelExchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Received request : "
operator|+
name|camelExchange
argument_list|)
expr_stmt|;
block|}
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|cxfMessage
init|=
name|endpoint
operator|.
name|getCxfBeanBinding
argument_list|()
operator|.
name|createCxfMessageFromCamelExchange
argument_list|(
name|camelExchange
argument_list|,
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
decl_stmt|;
name|cxfMessage
operator|.
name|put
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_EXCHANGE
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
operator|(
operator|(
name|MessageImpl
operator|)
name|cxfMessage
operator|)
operator|.
name|setDestination
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// Handling the incoming message
comment|// The response message will be send back by the outgoing chain
name|incomingObserver
operator|.
name|onMessage
argument_list|(
name|cxfMessage
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|propagateResponseHeadersToCamel (Message outMessage, Exchange camelExchange)
specifier|protected
name|void
name|propagateResponseHeadersToCamel
parameter_list|(
name|Message
name|outMessage
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
block|{
name|endpoint
operator|.
name|getCxfBeanBinding
argument_list|()
operator|.
name|propagateResponseHeadersToCamel
argument_list|(
name|outMessage
argument_list|,
name|camelExchange
argument_list|,
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

