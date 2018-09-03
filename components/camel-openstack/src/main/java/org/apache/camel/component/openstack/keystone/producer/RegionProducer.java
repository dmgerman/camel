begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.keystone.producer
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
name|keystone
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
name|keystone
operator|.
name|KeystoneConstants
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
name|keystone
operator|.
name|KeystoneEndpoint
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
name|identity
operator|.
name|v3
operator|.
name|Region
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
name|identity
operator|.
name|v3
operator|.
name|builder
operator|.
name|RegionBuilder
import|;
end_import

begin_class
DECL|class|RegionProducer
specifier|public
class|class
name|RegionProducer
extends|extends
name|AbstractKeystoneProducer
block|{
DECL|method|RegionProducer (KeystoneEndpoint endpoint, OSClient client)
specifier|public
name|RegionProducer
parameter_list|(
name|KeystoneEndpoint
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
name|Region
name|in
init|=
name|messageToRegion
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Region
name|out
init|=
name|osV3Client
operator|.
name|identity
argument_list|()
operator|.
name|regions
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
name|String
name|id
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenstackConstants
operator|.
name|ID
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
literal|"Region ID"
argument_list|)
expr_stmt|;
specifier|final
name|Region
name|out
init|=
name|osV3Client
operator|.
name|identity
argument_list|()
operator|.
name|regions
argument_list|()
operator|.
name|get
argument_list|(
name|id
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
name|Region
argument_list|>
name|out
init|=
name|osV3Client
operator|.
name|identity
argument_list|()
operator|.
name|regions
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
name|Region
name|in
init|=
name|messageToRegion
argument_list|(
name|msg
argument_list|)
decl_stmt|;
specifier|final
name|Region
name|out
init|=
name|osV3Client
operator|.
name|identity
argument_list|()
operator|.
name|regions
argument_list|()
operator|.
name|update
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
literal|"Region ID"
argument_list|)
expr_stmt|;
specifier|final
name|ActionResponse
name|response
init|=
name|osV3Client
operator|.
name|identity
argument_list|()
operator|.
name|regions
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
name|msg
argument_list|,
literal|"Delete network"
operator|+
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|messageToRegion (Message message)
specifier|private
name|Region
name|messageToRegion
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Region
name|region
init|=
name|message
operator|.
name|getBody
argument_list|(
name|Region
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|region
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
name|RegionBuilder
name|builder
init|=
name|Builders
operator|.
name|region
argument_list|()
decl_stmt|;
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|KeystoneConstants
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
name|KeystoneConstants
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
name|region
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
return|return
name|region
return|;
block|}
block|}
end_class

end_unit

