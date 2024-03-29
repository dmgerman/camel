begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|component
operator|.
name|cxf
operator|.
name|common
operator|.
name|header
operator|.
name|CxfHeaderFilterStrategy
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
name|HeaderFilterStrategy
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
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
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
name|AbstractTransportFactory
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
name|ConduitInitiator
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
name|Destination
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
name|DestinationFactory
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
DECL|class|CamelTransportFactory
specifier|public
class|class
name|CamelTransportFactory
extends|extends
name|AbstractTransportFactory
implements|implements
name|ConduitInitiator
implements|,
name|DestinationFactory
implements|,
name|CamelContextAware
block|{
DECL|field|TRANSPORT_ID
specifier|public
specifier|static
specifier|final
name|String
name|TRANSPORT_ID
init|=
literal|"http://cxf.apache.org/transports/camel"
decl_stmt|;
DECL|field|DEFAULT_NAMESPACES
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|DEFAULT_NAMESPACES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|TRANSPORT_ID
argument_list|)
decl_stmt|;
DECL|field|URI_PREFIXES
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|URI_PREFIXES
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|field|checkException
specifier|private
name|boolean
name|checkException
decl_stmt|;
DECL|field|bus
specifier|private
name|Bus
name|bus
decl_stmt|;
static|static
block|{
name|URI_PREFIXES
operator|.
name|add
argument_list|(
literal|"camel://"
argument_list|)
expr_stmt|;
block|}
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|CamelTransportFactory ()
specifier|public
name|CamelTransportFactory
parameter_list|()
block|{
name|CxfHeaderFilterStrategy
name|defaultHeaderFilterStrategy
init|=
operator|new
name|CxfHeaderFilterStrategy
argument_list|()
decl_stmt|;
comment|// Doesn't filter the camel relates headers by default
name|defaultHeaderFilterStrategy
operator|.
name|setOutFilterPattern
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|headerFilterStrategy
operator|=
name|defaultHeaderFilterStrategy
expr_stmt|;
block|}
DECL|method|CamelTransportFactory (Bus b)
specifier|public
name|CamelTransportFactory
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|DEFAULT_NAMESPACES
argument_list|)
expr_stmt|;
name|bus
operator|=
name|b
expr_stmt|;
name|registerFactory
argument_list|()
expr_stmt|;
name|CxfHeaderFilterStrategy
name|defaultHeaderFilterStrategy
init|=
operator|new
name|CxfHeaderFilterStrategy
argument_list|()
decl_stmt|;
comment|// Doesn't filter the camel relates headers by default
name|defaultHeaderFilterStrategy
operator|.
name|setOutFilterPattern
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|headerFilterStrategy
operator|=
name|defaultHeaderFilterStrategy
expr_stmt|;
block|}
DECL|method|setCheckException (boolean check)
specifier|public
name|void
name|setCheckException
parameter_list|(
name|boolean
name|check
parameter_list|)
block|{
name|checkException
operator|=
name|check
expr_stmt|;
block|}
DECL|method|isCheckException ()
specifier|public
name|boolean
name|isCheckException
parameter_list|()
block|{
return|return
name|checkException
return|;
block|}
DECL|method|getConduit (EndpointInfo targetInfo)
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|targetInfo
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getConduit
argument_list|(
name|targetInfo
argument_list|,
literal|null
argument_list|,
name|bus
argument_list|)
return|;
block|}
DECL|method|getConduit (EndpointInfo endpointInfo, EndpointReferenceType target)
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|EndpointReferenceType
name|target
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getConduit
argument_list|(
name|endpointInfo
argument_list|,
name|target
argument_list|,
name|bus
argument_list|)
return|;
block|}
DECL|method|getDestination (EndpointInfo endpointInfo)
specifier|public
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getDestination
argument_list|(
name|endpointInfo
argument_list|,
name|bus
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getUriPrefixes ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getUriPrefixes
parameter_list|()
block|{
return|return
name|URI_PREFIXES
return|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext c)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|c
parameter_list|)
block|{
name|camelContext
operator|=
name|c
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDestination (EndpointInfo ei, Bus b)
specifier|public
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|Bus
name|b
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|CamelDestination
argument_list|(
name|camelContext
argument_list|,
name|b
argument_list|,
name|this
argument_list|,
name|ei
argument_list|,
name|headerFilterStrategy
argument_list|,
name|checkException
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getConduit (EndpointInfo targetInfo, Bus b)
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|targetInfo
parameter_list|,
name|Bus
name|b
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getConduit
argument_list|(
name|targetInfo
argument_list|,
literal|null
argument_list|,
name|b
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getConduit (EndpointInfo localInfo, EndpointReferenceType target, Bus b)
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|localInfo
parameter_list|,
name|EndpointReferenceType
name|target
parameter_list|,
name|Bus
name|b
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|CamelConduit
argument_list|(
name|camelContext
argument_list|,
name|b
argument_list|,
name|localInfo
argument_list|,
name|target
argument_list|,
name|headerFilterStrategy
argument_list|)
return|;
block|}
comment|// CXF 2.x support methods
DECL|method|setBus (Bus b)
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|unregisterFactory
argument_list|()
expr_stmt|;
name|bus
operator|=
name|b
expr_stmt|;
name|registerFactory
argument_list|()
expr_stmt|;
block|}
DECL|method|registerFactory ()
specifier|public
specifier|final
name|void
name|registerFactory
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|==
name|bus
condition|)
block|{
return|return;
block|}
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
if|if
condition|(
literal|null
operator|!=
name|dfm
operator|&&
name|getTransportIds
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|ns
range|:
name|getTransportIds
argument_list|()
control|)
block|{
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
name|ns
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|cim
operator|!=
literal|null
operator|&&
name|getTransportIds
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|ns
range|:
name|getTransportIds
argument_list|()
control|)
block|{
name|cim
operator|.
name|registerConduitInitiator
argument_list|(
name|ns
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|unregisterFactory ()
specifier|public
specifier|final
name|void
name|unregisterFactory
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|==
name|bus
condition|)
block|{
return|return;
block|}
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
if|if
condition|(
literal|null
operator|!=
name|dfm
operator|&&
name|getTransportIds
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|ns
range|:
name|getTransportIds
argument_list|()
control|)
block|{
try|try
block|{
if|if
condition|(
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
name|ns
argument_list|)
operator|==
name|this
condition|)
block|{
name|dfm
operator|.
name|deregisterDestinationFactory
argument_list|(
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|BusException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
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
if|if
condition|(
name|cim
operator|!=
literal|null
operator|&&
name|getTransportIds
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|ns
range|:
name|getTransportIds
argument_list|()
control|)
block|{
try|try
block|{
if|if
condition|(
name|cim
operator|.
name|getConduitInitiator
argument_list|(
name|ns
argument_list|)
operator|==
name|this
condition|)
block|{
name|cim
operator|.
name|deregisterConduitInitiator
argument_list|(
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|BusException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

