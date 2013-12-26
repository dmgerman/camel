begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jgroups
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jgroups
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
name|Predicate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|Address
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|View
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jgroups
operator|.
name|JGroupsEndpoint
operator|.
name|HEADER_JGROUPS_CHANNEL_ADDRESS
import|;
end_import

begin_class
DECL|class|JGroupsFilters
specifier|public
specifier|final
class|class
name|JGroupsFilters
block|{
DECL|field|COORDINATOR_NODE_INDEX
specifier|private
specifier|static
specifier|final
name|int
name|COORDINATOR_NODE_INDEX
init|=
literal|0
decl_stmt|;
DECL|method|JGroupsFilters ()
specifier|private
name|JGroupsFilters
parameter_list|()
block|{     }
comment|/**      * Creates predicate rejecting messages that are instances of {@link org.jgroups.View}, but have not been received      * by the coordinator JGroups node. This filter is useful for keeping only view messages indicating that receiving      * endpoint is a master node.      *      * @return predicate filtering out non-coordinator view messages.      */
DECL|method|dropNonCoordinatorViews ()
specifier|public
specifier|static
name|Predicate
name|dropNonCoordinatorViews
parameter_list|()
block|{
return|return
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|View
condition|)
block|{
name|View
name|view
init|=
operator|(
name|View
operator|)
name|body
decl_stmt|;
name|Address
name|channelAddress
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HEADER_JGROUPS_CHANNEL_ADDRESS
argument_list|,
name|Address
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|channelAddress
operator|.
name|equals
argument_list|(
name|view
operator|.
name|getMembers
argument_list|()
operator|.
name|get
argument_list|(
name|COORDINATOR_NODE_INDEX
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

