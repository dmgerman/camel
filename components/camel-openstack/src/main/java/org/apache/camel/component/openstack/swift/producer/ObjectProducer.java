begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.swift.producer
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
name|swift
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
name|swift
operator|.
name|SwiftConstants
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
name|swift
operator|.
name|SwiftEndpoint
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
name|common
operator|.
name|Payload
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
name|object
operator|.
name|SwiftObject
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
name|object
operator|.
name|options
operator|.
name|ObjectLocation
import|;
end_import

begin_class
DECL|class|ObjectProducer
specifier|public
class|class
name|ObjectProducer
extends|extends
name|AbstractOpenstackProducer
block|{
DECL|method|ObjectProducer (SwiftEndpoint endpoint, OSClient client)
specifier|public
name|ObjectProducer
parameter_list|(
name|SwiftEndpoint
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
name|SwiftConstants
operator|.
name|GET_METADATA
case|:
name|doGetMetadata
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|SwiftConstants
operator|.
name|CREATE_UPDATE_METADATA
case|:
name|doUpdateMetadata
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
name|Payload
name|payload
init|=
name|createPayload
argument_list|(
name|msg
argument_list|)
decl_stmt|;
specifier|final
name|String
name|containerName
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|SwiftConstants
operator|.
name|CONTAINER_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|objectName
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|SwiftConstants
operator|.
name|OBJECT_NAME
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
name|containerName
argument_list|,
literal|"Container name"
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|objectName
argument_list|,
literal|"Object name"
argument_list|)
expr_stmt|;
specifier|final
name|String
name|etag
init|=
name|os
operator|.
name|objectStorage
argument_list|()
operator|.
name|objects
argument_list|()
operator|.
name|put
argument_list|(
name|containerName
argument_list|,
name|objectName
argument_list|,
name|payload
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|etag
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
name|containerName
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|SwiftConstants
operator|.
name|CONTAINER_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|objectName
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|SwiftConstants
operator|.
name|OBJECT_NAME
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
name|containerName
argument_list|,
literal|"Container name"
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|objectName
argument_list|,
literal|"Object name"
argument_list|)
expr_stmt|;
specifier|final
name|SwiftObject
name|out
init|=
name|os
operator|.
name|objectStorage
argument_list|()
operator|.
name|objects
argument_list|()
operator|.
name|get
argument_list|(
name|containerName
argument_list|,
name|objectName
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
name|name
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|SwiftConstants
operator|.
name|CONTAINER_NAME
argument_list|,
name|msg
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
literal|"Container name"
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|?
extends|extends
name|SwiftObject
argument_list|>
name|out
init|=
name|os
operator|.
name|objectStorage
argument_list|()
operator|.
name|objects
argument_list|()
operator|.
name|list
argument_list|(
name|name
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
name|containerName
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|SwiftConstants
operator|.
name|CONTAINER_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|objectName
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|SwiftConstants
operator|.
name|OBJECT_NAME
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
name|containerName
argument_list|,
literal|"Container name"
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|objectName
argument_list|,
literal|"Object name"
argument_list|)
expr_stmt|;
specifier|final
name|ActionResponse
name|out
init|=
name|os
operator|.
name|objectStorage
argument_list|()
operator|.
name|objects
argument_list|()
operator|.
name|delete
argument_list|(
name|containerName
argument_list|,
name|objectName
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|out
operator|.
name|getFault
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setFault
argument_list|(
operator|!
name|out
operator|.
name|isSuccess
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doGetMetadata (Exchange exchange)
specifier|private
name|void
name|doGetMetadata
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
name|containerName
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|SwiftConstants
operator|.
name|CONTAINER_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|objectName
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|SwiftConstants
operator|.
name|OBJECT_NAME
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
name|containerName
argument_list|,
literal|"Container name"
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|objectName
argument_list|,
literal|"Object name"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|os
operator|.
name|objectStorage
argument_list|()
operator|.
name|objects
argument_list|()
operator|.
name|getMetadata
argument_list|(
name|containerName
argument_list|,
name|objectName
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|doUpdateMetadata (Exchange exchange)
specifier|private
name|void
name|doUpdateMetadata
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
name|containerName
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|SwiftConstants
operator|.
name|CONTAINER_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|objectName
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|SwiftConstants
operator|.
name|OBJECT_NAME
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
name|containerName
argument_list|,
literal|"Container name"
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|objectName
argument_list|,
literal|"Object name"
argument_list|)
expr_stmt|;
specifier|final
name|boolean
name|success
init|=
name|os
operator|.
name|objectStorage
argument_list|()
operator|.
name|objects
argument_list|()
operator|.
name|updateMetadata
argument_list|(
name|ObjectLocation
operator|.
name|create
argument_list|(
name|containerName
argument_list|,
name|objectName
argument_list|)
argument_list|,
name|msg
operator|.
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setFault
argument_list|(
operator|!
name|success
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|success
condition|)
block|{
name|msg
operator|.
name|setBody
argument_list|(
literal|"Updating metadata was not successful"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

