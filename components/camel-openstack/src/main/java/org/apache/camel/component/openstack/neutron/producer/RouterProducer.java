begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.neutron.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openstack
operator|.
name|neutron
operator|.
name|producer
package|;
end_package

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
name|Message
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
name|openstack
operator|.
name|common
operator|.
name|AbstractOpenstackProducer
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
name|openstack
operator|.
name|common
operator|.
name|OpenstackConstants
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
name|openstack
operator|.
name|neutron
operator|.
name|NeutronConstants
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
name|openstack
operator|.
name|neutron
operator|.
name|NeutronEndpoint
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
name|camel
operator|.
name|util
operator|.
name|StringHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|api
operator|.
name|Builders
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|api
operator|.
name|OSClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|common
operator|.
name|ActionResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|network
operator|.
name|AttachInterfaceType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|network
operator|.
name|Router
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|network
operator|.
name|RouterInterface
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|network
operator|.
name|builder
operator|.
name|RouterBuilder
import|;
end_import

begin_class
DECL|class|RouterProducer
specifier|public
class|class
name|RouterProducer
extends|extends
name|AbstractOpenstackProducer
block|{
DECL|method|RouterProducer (NeutronEndpoint endpoint, OSClient client)
specifier|public
name|RouterProducer
parameter_list|(
name|NeutronEndpoint
name|endpoint
parameter_list|,
name|OSClient
name|client
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|client
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
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
specifier|final
name|String
name|operation
init|=
name|getOperation
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|OpenstackConstants
operator|.
name|CREATE
case|:
name|doCreate
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|OpenstackConstants
operator|.
name|GET
case|:
name|doGet
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|OpenstackConstants
operator|.
name|GET_ALL
case|:
name|doGetAll
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|OpenstackConstants
operator|.
name|UPDATE
case|:
name|doUpdate
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|OpenstackConstants
operator|.
name|DELETE
case|:
name|doDelete
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|NeutronConstants
operator|.
name|ATTACH_INTERFACE
case|:
name|doAttach
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|NeutronConstants
operator|.
name|DETACH_INTERFACE
case|:
name|doDetach
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsuproutered operation "
operator|+
name|operation
argument_list|)
throw|;
block|}
block|}
DECL|method|doCreate (Exchange exchange)
specifier|private
name|void
name|doCreate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|Router
name|in
init|=
name|messageToRouter
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Router
name|out
init|=
name|os
operator|.
name|networking
argument_list|()
operator|.
name|router
argument_list|()
operator|.
name|create
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|doGet (Exchange exchange)
specifier|private
name|void
name|doGet
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|String
name|id
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|OpenstackConstants
operator|.
name|ID
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
name|NeutronConstants
operator|.
name|ROUTER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|id
argument_list|,
literal|"Router ID"
argument_list|)
expr_stmt|;
specifier|final
name|Router
name|result
init|=
name|os
operator|.
name|networking
argument_list|()
operator|.
name|router
argument_list|()
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|doGetAll (Exchange exchange)
specifier|private
name|void
name|doGetAll
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|?
extends|extends
name|Router
argument_list|>
name|out
init|=
name|os
operator|.
name|networking
argument_list|()
operator|.
name|router
argument_list|()
operator|.
name|list
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|doUpdate (Exchange exchange)
specifier|private
name|void
name|doUpdate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|Router
name|router
init|=
name|messageToRouter
argument_list|(
name|msg
argument_list|)
decl_stmt|;
specifier|final
name|Router
name|updatedRouter
init|=
name|os
operator|.
name|networking
argument_list|()
operator|.
name|router
argument_list|()
operator|.
name|update
argument_list|(
name|router
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|updatedRouter
argument_list|)
expr_stmt|;
block|}
DECL|method|doDelete (Exchange exchange)
specifier|private
name|void
name|doDelete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|String
name|id
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|OpenstackConstants
operator|.
name|ID
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
name|NeutronConstants
operator|.
name|ROUTER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|id
argument_list|,
literal|"Router ID"
argument_list|)
expr_stmt|;
specifier|final
name|ActionResponse
name|response
init|=
name|os
operator|.
name|networking
argument_list|()
operator|.
name|router
argument_list|()
operator|.
name|delete
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|checkFailure
argument_list|(
name|response
argument_list|,
name|exchange
argument_list|,
literal|"Delete router with ID "
operator|+
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|doDetach (Exchange exchange)
specifier|private
name|void
name|doDetach
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|String
name|routerId
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|NeutronConstants
operator|.
name|ROUTER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|subnetId
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|NeutronConstants
operator|.
name|SUBNET_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|portId
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|NeutronConstants
operator|.
name|PORT_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|routerId
argument_list|,
literal|"Router ID"
argument_list|)
expr_stmt|;
name|RouterInterface
name|iface
init|=
name|os
operator|.
name|networking
argument_list|()
operator|.
name|router
argument_list|()
operator|.
name|detachInterface
argument_list|(
name|routerId
argument_list|,
name|subnetId
argument_list|,
name|portId
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|iface
argument_list|)
expr_stmt|;
block|}
DECL|method|doAttach (Exchange exchange)
specifier|private
name|void
name|doAttach
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|String
name|routerId
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|NeutronConstants
operator|.
name|ROUTER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|subnetPortId
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|NeutronConstants
operator|.
name|SUBNET_ID
argument_list|,
name|msg
operator|.
name|getHeader
argument_list|(
name|NeutronConstants
operator|.
name|PORT_ID
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|AttachInterfaceType
name|type
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|NeutronConstants
operator|.
name|ITERFACE_TYPE
argument_list|,
name|AttachInterfaceType
operator|.
name|class
argument_list|)
decl_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|routerId
argument_list|,
literal|"Router ID"
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|subnetPortId
argument_list|,
literal|"Subnet/Port ID"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|type
argument_list|,
literal|"AttachInterfaceType "
argument_list|)
expr_stmt|;
name|RouterInterface
name|routerInterface
init|=
name|os
operator|.
name|networking
argument_list|()
operator|.
name|router
argument_list|()
operator|.
name|attachInterface
argument_list|(
name|routerId
argument_list|,
name|type
argument_list|,
name|subnetPortId
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|routerInterface
argument_list|)
expr_stmt|;
block|}
DECL|method|messageToRouter (Message message)
specifier|private
name|Router
name|messageToRouter
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Router
name|router
init|=
name|message
operator|.
name|getBody
argument_list|(
name|Router
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|router
operator|==
literal|null
condition|)
block|{
name|Map
name|headers
init|=
name|message
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|RouterBuilder
name|builder
init|=
name|Builders
operator|.
name|router
argument_list|()
decl_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|OpenstackConstants
operator|.
name|NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
literal|"Name"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|name
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|OpenstackConstants
operator|.
name|NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|NeutronConstants
operator|.
name|TENANT_ID
argument_list|)
condition|)
block|{
name|builder
operator|.
name|tenantId
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|NeutronConstants
operator|.
name|TENANT_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|router
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
return|return
name|router
return|;
block|}
block|}
end_class

end_unit

