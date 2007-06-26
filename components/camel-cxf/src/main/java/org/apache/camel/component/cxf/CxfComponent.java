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
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|impl
operator|.
name|DefaultComponent
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
name|bus
operator|.
name|CXFBusFactory
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
name|DestinationFactoryManager
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

begin_import
import|import
name|org
operator|.
name|xmlsoap
operator|.
name|schemas
operator|.
name|wsdl
operator|.
name|http
operator|.
name|AddressType
import|;
end_import

begin_comment
comment|/**  * Defines the<a href="http://activemq.apache.org/camel/cxf.html">CXF Component</a>   * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfComponent
specifier|public
class|class
name|CxfComponent
extends|extends
name|DefaultComponent
argument_list|<
name|CxfExchange
argument_list|>
block|{
DECL|field|localTransportFactory
specifier|private
name|LocalTransportFactory
name|localTransportFactory
decl_stmt|;
DECL|method|CxfComponent ()
specifier|public
name|CxfComponent
parameter_list|()
block|{     }
DECL|method|CxfComponent (CamelContext context)
specifier|public
name|CxfComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
argument_list|<
name|CxfExchange
argument_list|>
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
comment|// TODO this is a hack!!!
name|EndpointInfo
name|endpointInfo
init|=
operator|new
name|EndpointInfo
argument_list|(
literal|null
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|)
decl_stmt|;
name|AddressType
name|a
init|=
operator|new
name|AddressType
argument_list|()
decl_stmt|;
name|a
operator|.
name|setLocation
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|endpointInfo
operator|.
name|addExtensor
argument_list|(
name|a
argument_list|)
expr_stmt|;
return|return
operator|new
name|CxfEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|endpointInfo
argument_list|)
return|;
block|}
DECL|method|getLocalTransportFactory ()
specifier|public
name|LocalTransportFactory
name|getLocalTransportFactory
parameter_list|()
throws|throws
name|BusException
block|{
if|if
condition|(
name|localTransportFactory
operator|==
literal|null
condition|)
block|{
name|localTransportFactory
operator|=
name|findLocalTransportFactory
argument_list|()
expr_stmt|;
if|if
condition|(
name|localTransportFactory
operator|==
literal|null
condition|)
block|{
name|localTransportFactory
operator|=
operator|new
name|LocalTransportFactory
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|localTransportFactory
return|;
block|}
DECL|method|setLocalTransportFactory (LocalTransportFactory localTransportFactory)
specifier|public
name|void
name|setLocalTransportFactory
parameter_list|(
name|LocalTransportFactory
name|localTransportFactory
parameter_list|)
block|{
name|this
operator|.
name|localTransportFactory
operator|=
name|localTransportFactory
expr_stmt|;
block|}
DECL|method|findLocalTransportFactory ()
specifier|protected
name|LocalTransportFactory
name|findLocalTransportFactory
parameter_list|()
throws|throws
name|BusException
block|{
name|Bus
name|bus
init|=
name|CXFBusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|(
name|LocalTransportFactory
operator|)
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
return|;
block|}
block|}
end_class

end_unit

