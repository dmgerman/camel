begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.cinder.producer
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
name|cinder
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
name|cinder
operator|.
name|CinderConstants
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
name|cinder
operator|.
name|CinderEndpoint
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
name|storage
operator|.
name|block
operator|.
name|Volume
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
name|storage
operator|.
name|block
operator|.
name|VolumeType
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
name|storage
operator|.
name|block
operator|.
name|builder
operator|.
name|VolumeBuilder
import|;
end_import

begin_class
DECL|class|VolumeProducer
specifier|public
class|class
name|VolumeProducer
extends|extends
name|AbstractOpenstackProducer
block|{
DECL|method|VolumeProducer (CinderEndpoint endpoint, OSClient client)
specifier|public
name|VolumeProducer
parameter_list|(
name|CinderEndpoint
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
name|CinderConstants
operator|.
name|GET_ALL_TYPES
case|:
name|doGetAllTypes
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
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported operation "
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
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|Volume
name|in
init|=
name|messageToVolume
argument_list|(
name|msg
argument_list|)
decl_stmt|;
specifier|final
name|Volume
name|out
init|=
name|os
operator|.
name|blockStorage
argument_list|()
operator|.
name|volumes
argument_list|()
operator|.
name|create
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|msg
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
name|CinderConstants
operator|.
name|VOLUME_ID
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
literal|"Volume ID"
argument_list|)
expr_stmt|;
specifier|final
name|Volume
name|out
init|=
name|os
operator|.
name|blockStorage
argument_list|()
operator|.
name|volumes
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
name|out
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
name|Volume
argument_list|>
name|out
init|=
name|os
operator|.
name|blockStorage
argument_list|()
operator|.
name|volumes
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
DECL|method|doGetAllTypes (Exchange exchange)
specifier|private
name|void
name|doGetAllTypes
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
name|VolumeType
argument_list|>
name|out
init|=
name|os
operator|.
name|blockStorage
argument_list|()
operator|.
name|volumes
argument_list|()
operator|.
name|listVolumeTypes
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
name|CinderConstants
operator|.
name|VOLUME_ID
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
specifier|final
name|Volume
name|vol
init|=
name|messageToVolume
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|id
argument_list|,
literal|"Cinder Volume ID"
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|vol
operator|.
name|getDescription
argument_list|()
argument_list|,
literal|"Cinder Volume Description"
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|vol
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Cinder Volume Name"
argument_list|)
expr_stmt|;
specifier|final
name|ActionResponse
name|out
init|=
name|os
operator|.
name|blockStorage
argument_list|()
operator|.
name|volumes
argument_list|()
operator|.
name|update
argument_list|(
name|id
argument_list|,
name|vol
operator|.
name|getName
argument_list|()
argument_list|,
name|vol
operator|.
name|getDescription
argument_list|()
argument_list|)
decl_stmt|;
name|checkFailure
argument_list|(
name|out
argument_list|,
name|msg
argument_list|,
literal|"Update volume "
operator|+
name|id
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
name|CinderConstants
operator|.
name|VOLUME_ID
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
literal|"Cinder Volume ID"
argument_list|)
expr_stmt|;
specifier|final
name|ActionResponse
name|out
init|=
name|os
operator|.
name|blockStorage
argument_list|()
operator|.
name|volumes
argument_list|()
operator|.
name|delete
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|checkFailure
argument_list|(
name|out
argument_list|,
name|msg
argument_list|,
literal|"Delete volume "
operator|+
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|messageToVolume (Message message)
specifier|private
name|Volume
name|messageToVolume
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Volume
name|volume
init|=
name|message
operator|.
name|getBody
argument_list|(
name|Volume
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|volume
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
name|VolumeBuilder
name|builder
init|=
name|Builders
operator|.
name|volume
argument_list|()
decl_stmt|;
specifier|final
name|String
name|name
init|=
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
decl_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|name
argument_list|,
literal|"Name "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|name
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|OpenstackConstants
operator|.
name|DESCRIPTION
argument_list|)
condition|)
block|{
name|builder
operator|.
name|description
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|OpenstackConstants
operator|.
name|DESCRIPTION
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|CinderConstants
operator|.
name|SIZE
argument_list|)
condition|)
block|{
name|builder
operator|.
name|size
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|CinderConstants
operator|.
name|SIZE
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|CinderConstants
operator|.
name|VOLUME_TYPE
argument_list|)
condition|)
block|{
name|builder
operator|.
name|volumeType
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|CinderConstants
operator|.
name|VOLUME_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|CinderConstants
operator|.
name|IMAGE_REF
argument_list|)
condition|)
block|{
name|builder
operator|.
name|imageRef
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|CinderConstants
operator|.
name|IMAGE_REF
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|CinderConstants
operator|.
name|SNAPSHOT_ID
argument_list|)
condition|)
block|{
name|builder
operator|.
name|snapshot
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|CinderConstants
operator|.
name|SNAPSHOT_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|CinderConstants
operator|.
name|IS_BOOTABLE
argument_list|)
condition|)
block|{
name|builder
operator|.
name|bootable
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|CinderConstants
operator|.
name|IS_BOOTABLE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|volume
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
return|return
name|volume
return|;
block|}
block|}
end_class

end_unit

