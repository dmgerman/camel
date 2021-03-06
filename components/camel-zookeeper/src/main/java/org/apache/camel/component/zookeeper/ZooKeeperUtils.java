begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|List
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
name|zookeeper
operator|.
name|operations
operator|.
name|WatchedEventProvider
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
name|zookeeper
operator|.
name|operations
operator|.
name|ZooKeeperOperation
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
name|support
operator|.
name|ExchangeHelper
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
name|CreateMode
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
name|ZooDefs
operator|.
name|Ids
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
name|ACL
import|;
end_import

begin_comment
comment|/**  *<code>ZooKeeperUtils</code> contains static utility functions mostly for  * retrieving optional message properties from Message headers.  */
end_comment

begin_class
DECL|class|ZooKeeperUtils
specifier|public
specifier|final
class|class
name|ZooKeeperUtils
block|{
DECL|method|ZooKeeperUtils ()
specifier|private
name|ZooKeeperUtils
parameter_list|()
block|{ }
comment|/**      * Pulls a createMode flag from the header keyed by      * {@link ZooKeeperMessage#ZOOKEEPER_CREATE_MODE} in the given message and      * attempts to parse a {@link CreateMode} from it.      *      * @param message the message that may contain a ZOOKEEPER_CREATE_MODE      *            header.      * @return the parsed {@link CreateMode} or null if the header was null or      *         not a valid mode flag.      */
DECL|method|getCreateMode (Message message, CreateMode defaultMode)
specifier|public
specifier|static
name|CreateMode
name|getCreateMode
parameter_list|(
name|Message
name|message
parameter_list|,
name|CreateMode
name|defaultMode
parameter_list|)
block|{
name|CreateMode
name|mode
init|=
literal|null
decl_stmt|;
try|try
block|{
name|mode
operator|=
name|message
operator|.
name|getHeader
argument_list|(
name|ZooKeeperMessage
operator|.
name|ZOOKEEPER_CREATE_MODE
argument_list|,
name|CreateMode
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{         }
if|if
condition|(
name|mode
operator|==
literal|null
condition|)
block|{
name|String
name|modeHeader
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|ZooKeeperMessage
operator|.
name|ZOOKEEPER_CREATE_MODE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|mode
operator|=
name|getCreateModeFromString
argument_list|(
name|modeHeader
argument_list|,
name|defaultMode
argument_list|)
expr_stmt|;
block|}
return|return
name|mode
operator|==
literal|null
condition|?
name|defaultMode
else|:
name|mode
return|;
block|}
DECL|method|getCreateModeFromString (String modeHeader, CreateMode defaultMode)
specifier|public
specifier|static
name|CreateMode
name|getCreateModeFromString
parameter_list|(
name|String
name|modeHeader
parameter_list|,
name|CreateMode
name|defaultMode
parameter_list|)
block|{
name|CreateMode
name|mode
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|modeHeader
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|mode
operator|=
name|CreateMode
operator|.
name|valueOf
argument_list|(
name|modeHeader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{             }
block|}
return|return
name|mode
operator|==
literal|null
condition|?
name|defaultMode
else|:
name|mode
return|;
block|}
comment|/**      * Pulls the target node from the header keyed by      * {@link ZooKeeperMessage#ZOOKEEPER_NODE}. This node is then typically used      * in place of the configured node extracted from the endpoint uri.      *      * @param message the message that may contain a ZOOKEEPER_NODE header.      * @return the node property or null if the header was null      */
DECL|method|getNodeFromMessage (Message message, String defaultNode)
specifier|public
specifier|static
name|String
name|getNodeFromMessage
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|defaultNode
parameter_list|)
block|{
return|return
name|getZookeeperProperty
argument_list|(
name|message
argument_list|,
name|ZooKeeperMessage
operator|.
name|ZOOKEEPER_NODE
argument_list|,
name|defaultNode
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getVersionFromMessage (Message message)
specifier|public
specifier|static
name|Integer
name|getVersionFromMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|getZookeeperProperty
argument_list|(
name|message
argument_list|,
name|ZooKeeperMessage
operator|.
name|ZOOKEEPER_NODE_VERSION
argument_list|,
operator|-
literal|1
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getPayloadFromExchange (Exchange exchange)
specifier|public
specifier|static
name|byte
index|[]
name|getPayloadFromExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|ExchangeHelper
operator|.
name|convertToType
argument_list|(
name|exchange
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getAclListFromMessage (Message in)
specifier|public
specifier|static
name|List
argument_list|<
name|ACL
argument_list|>
name|getAclListFromMessage
parameter_list|(
name|Message
name|in
parameter_list|)
block|{
return|return
name|getZookeeperProperty
argument_list|(
name|in
argument_list|,
name|ZooKeeperMessage
operator|.
name|ZOOKEEPER_ACL
argument_list|,
name|Ids
operator|.
name|OPEN_ACL_UNSAFE
argument_list|,
name|List
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getZookeeperProperty (Message m, String propertyName, T defaultValue, Class<? extends T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getZookeeperProperty
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|T
name|defaultValue
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|T
name|value
init|=
name|m
operator|.
name|getHeader
argument_list|(
name|propertyName
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|defaultValue
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
DECL|method|getWatchedEvent (ZooKeeperOperation<?> zooKeeperOperation)
specifier|public
specifier|static
name|WatchedEvent
name|getWatchedEvent
parameter_list|(
name|ZooKeeperOperation
argument_list|<
name|?
argument_list|>
name|zooKeeperOperation
parameter_list|)
block|{
name|WatchedEvent
name|watchedEvent
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|zooKeeperOperation
operator|instanceof
name|WatchedEventProvider
condition|)
block|{
name|watchedEvent
operator|=
operator|(
operator|(
name|WatchedEventProvider
operator|)
name|zooKeeperOperation
operator|)
operator|.
name|getWatchedEvent
argument_list|()
expr_stmt|;
block|}
return|return
name|watchedEvent
return|;
block|}
DECL|method|hasWatchedEvent (ZooKeeperOperation<?> zooKeeperOperation)
specifier|public
specifier|static
name|boolean
name|hasWatchedEvent
parameter_list|(
name|ZooKeeperOperation
argument_list|<
name|?
argument_list|>
name|zooKeeperOperation
parameter_list|)
block|{
return|return
name|getWatchedEvent
argument_list|(
name|zooKeeperOperation
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

