begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|impl
operator|.
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|WatchedEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|data
operator|.
name|Stat
import|;
end_import

begin_comment
comment|/**  *<code>ZooKeeperMessage</code> is a {@link org.apache.camel.Message}  * representing interactions with a ZooKeeper service. It contains a number of  * optional Header Constants that are used by the Producer and consumer  * mechanisms to finely control these interactions.  */
end_comment

begin_class
DECL|class|ZooKeeperMessage
specifier|public
class|class
name|ZooKeeperMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|ZOOKEEPER_NODE
specifier|public
specifier|static
specifier|final
name|String
name|ZOOKEEPER_NODE
init|=
literal|"CamelZooKeeperNode"
decl_stmt|;
DECL|field|ZOOKEEPER_NODE_VERSION
specifier|public
specifier|static
specifier|final
name|String
name|ZOOKEEPER_NODE_VERSION
init|=
literal|"CamelZooKeeperVersion"
decl_stmt|;
DECL|field|ZOOKEEPER_ERROR_CODE
specifier|public
specifier|static
specifier|final
name|String
name|ZOOKEEPER_ERROR_CODE
init|=
literal|"CamelZooKeeperErrorCode"
decl_stmt|;
DECL|field|ZOOKEEPER_ACL
specifier|public
specifier|static
specifier|final
name|String
name|ZOOKEEPER_ACL
init|=
literal|"CamelZookeeperAcl"
decl_stmt|;
DECL|field|ZOOKEEPER_CREATE_MODE
specifier|public
specifier|static
specifier|final
name|String
name|ZOOKEEPER_CREATE_MODE
init|=
literal|"CamelZookeeperCreateMode"
decl_stmt|;
DECL|field|ZOOKEEPER_STATISTICS
specifier|public
specifier|static
specifier|final
name|String
name|ZOOKEEPER_STATISTICS
init|=
literal|"CamelZookeeperStatistics"
decl_stmt|;
DECL|field|ZOOKEEPER_EVENT_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|ZOOKEEPER_EVENT_TYPE
init|=
literal|"CamelZookeeperEventType"
decl_stmt|;
DECL|field|ZOOKEEPER_OPERATION
specifier|public
specifier|static
specifier|final
name|String
name|ZOOKEEPER_OPERATION
init|=
literal|"CamelZookeeperOperation"
decl_stmt|;
DECL|method|ZooKeeperMessage (String node, Stat statistics, WatchedEvent watchedEvent)
specifier|public
name|ZooKeeperMessage
parameter_list|(
name|String
name|node
parameter_list|,
name|Stat
name|statistics
parameter_list|,
name|WatchedEvent
name|watchedEvent
parameter_list|)
block|{
name|this
argument_list|(
name|node
argument_list|,
name|statistics
argument_list|,
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|Object
operator|>
name|emptyMap
argument_list|()
argument_list|,
name|watchedEvent
argument_list|)
expr_stmt|;
block|}
DECL|method|ZooKeeperMessage (String node, Stat statistics, Map<String, Object> headers)
specifier|public
name|ZooKeeperMessage
parameter_list|(
name|String
name|node
parameter_list|,
name|Stat
name|statistics
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
name|this
argument_list|(
name|node
argument_list|,
name|statistics
argument_list|,
name|headers
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ZooKeeperMessage (String node, Stat statistics, Map<String, Object> headers, WatchedEvent watchedEvent)
specifier|public
name|ZooKeeperMessage
parameter_list|(
name|String
name|node
parameter_list|,
name|Stat
name|statistics
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|WatchedEvent
name|watchedEvent
parameter_list|)
block|{
name|setHeaders
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|this
operator|.
name|setHeader
argument_list|(
name|ZOOKEEPER_NODE
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|this
operator|.
name|setHeader
argument_list|(
name|ZOOKEEPER_STATISTICS
argument_list|,
name|statistics
argument_list|)
expr_stmt|;
if|if
condition|(
name|watchedEvent
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|setHeader
argument_list|(
name|ZOOKEEPER_EVENT_TYPE
argument_list|,
name|watchedEvent
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getStatistics (Message message)
specifier|public
specifier|static
name|Stat
name|getStatistics
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Stat
name|stats
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|stats
operator|=
name|message
operator|.
name|getHeader
argument_list|(
name|ZOOKEEPER_STATISTICS
argument_list|,
name|Stat
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|stats
return|;
block|}
DECL|method|getPath (Message message)
specifier|public
specifier|static
name|String
name|getPath
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|path
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|path
operator|=
name|message
operator|.
name|getHeader
argument_list|(
name|ZOOKEEPER_NODE
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|path
return|;
block|}
block|}
end_class

end_unit

