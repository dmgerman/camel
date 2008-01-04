begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.transport
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
name|transport
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
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|ContextTestSupport
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
name|DefaultCamelContext
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
name|BusFactory
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
name|Exchange
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
name|ExchangeImpl
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
name|Service
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|Conduit
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
name|ConduitInitiatorManager
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
name|MessageObserver
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
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
name|wsdl11
operator|.
name|WSDLServiceFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
import|;
end_import

begin_class
DECL|class|CamelTestSupport
specifier|public
specifier|abstract
class|class
name|CamelTestSupport
extends|extends
name|ContextTestSupport
block|{
DECL|field|bus
specifier|protected
name|Bus
name|bus
decl_stmt|;
DECL|field|endpointInfo
specifier|protected
name|EndpointInfo
name|endpointInfo
decl_stmt|;
DECL|field|target
specifier|protected
name|EndpointReferenceType
name|target
decl_stmt|;
DECL|field|observer
specifier|protected
name|MessageObserver
name|observer
decl_stmt|;
DECL|field|inMessage
specifier|protected
name|Message
name|inMessage
decl_stmt|;
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|BusFactory
name|bf
init|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
comment|//setup the camel transport for the bus
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|()
expr_stmt|;
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
name|CamelTransportFactory
name|camelTransportFactory
init|=
operator|new
name|CamelTransportFactory
argument_list|()
decl_stmt|;
comment|//set the context here to the transport factory;
name|camelTransportFactory
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|ConduitInitiatorManager
name|cim
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|endpointInfo
operator|=
operator|new
name|EndpointInfo
argument_list|()
expr_stmt|;
block|}
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|setupCamelConduit (EndpointInfo endpointInfo, boolean send, boolean decoupled)
specifier|protected
name|CamelConduit
name|setupCamelConduit
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|boolean
name|send
parameter_list|,
name|boolean
name|decoupled
parameter_list|)
block|{
if|if
condition|(
name|decoupled
condition|)
block|{
comment|// setup the reference type
block|}
else|else
block|{
name|target
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|EndpointReferenceType
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|CamelConduit
name|camelConduit
init|=
operator|new
name|CamelConduit
argument_list|(
name|context
argument_list|,
name|bus
argument_list|,
name|endpointInfo
argument_list|,
name|target
argument_list|)
decl_stmt|;
if|if
condition|(
name|send
condition|)
block|{
comment|// setMessageObserver
name|observer
operator|=
operator|new
name|MessageObserver
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|inMessage
operator|=
name|m
expr_stmt|;
block|}
block|}
expr_stmt|;
name|camelConduit
operator|.
name|setMessageObserver
argument_list|(
name|observer
argument_list|)
expr_stmt|;
block|}
return|return
name|camelConduit
return|;
block|}
DECL|method|sendoutMessage (Conduit conduit, Message message, Boolean isOneWay, String content)
specifier|protected
name|void
name|sendoutMessage
parameter_list|(
name|Conduit
name|conduit
parameter_list|,
name|Message
name|message
parameter_list|,
name|Boolean
name|isOneWay
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|IOException
block|{
name|Exchange
name|cxfExchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|cxfExchange
operator|==
literal|null
condition|)
block|{
name|cxfExchange
operator|=
operator|new
name|ExchangeImpl
argument_list|()
expr_stmt|;
name|cxfExchange
operator|.
name|setOneWay
argument_list|(
name|isOneWay
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|cxfExchange
argument_list|)
expr_stmt|;
name|cxfExchange
operator|.
name|setInMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|conduit
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|assertFalse
argument_list|(
literal|"CamelConduit can't perpare to send out message"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|OutputStream
name|os
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The OutputStream should not be null "
argument_list|,
name|os
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|content
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

