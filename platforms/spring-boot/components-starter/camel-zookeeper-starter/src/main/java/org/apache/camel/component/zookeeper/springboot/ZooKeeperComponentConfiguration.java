begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper.springboot
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
operator|.
name|springboot
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
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|DeprecatedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The zookeeper component allows interaction with a ZooKeeper cluster.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.zookeeper"
argument_list|)
DECL|class|ZooKeeperComponentConfiguration
specifier|public
class|class
name|ZooKeeperComponentConfiguration
block|{
comment|/**      * To use a shared ZooKeeperConfiguration      */
DECL|field|configuration
specifier|private
name|ZooKeeperConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|ZooKeeperConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( ZooKeeperConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|ZooKeeperConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|ZooKeeperConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|ZooKeeperConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
name|ZooKeeperConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * The zookeeper server hosts          */
DECL|field|servers
specifier|private
name|List
name|servers
decl_stmt|;
comment|/**          * The time interval to wait on connection before timing out.          */
DECL|field|timeout
specifier|private
name|Integer
name|timeout
init|=
literal|5000
decl_stmt|;
comment|/**          * Whether the children of the node should be listed          */
DECL|field|listChildren
specifier|private
name|Boolean
name|listChildren
init|=
literal|false
decl_stmt|;
comment|/**          * The node in the ZooKeeper server (aka znode)          */
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
comment|/**          * Should changes to the znode be 'watched' and repeatedly processed.          */
DECL|field|repeat
specifier|private
name|Boolean
name|repeat
init|=
literal|false
decl_stmt|;
comment|/**          * Not in use          *           * @deprecated The usage of this option has no effect at all.          */
annotation|@
name|Deprecated
DECL|field|awaitExistence
specifier|private
name|Boolean
name|awaitExistence
init|=
literal|true
decl_stmt|;
comment|/**          * The time interval to backoff for after an error before retrying.          */
DECL|field|backoff
specifier|private
name|Long
name|backoff
init|=
literal|5000L
decl_stmt|;
comment|/**          * Should the endpoint create the node if it does not currently exist.          */
DECL|field|create
specifier|private
name|Boolean
name|create
init|=
literal|false
decl_stmt|;
comment|/**          * The create mode that should be used for the newly created node          */
DECL|field|createMode
specifier|private
name|String
name|createMode
init|=
literal|"EPHEMERAL"
decl_stmt|;
comment|/**          * Upon the delete of a znode, should an empty message be send to the          * consumer          */
DECL|field|sendEmptyMessageOnDelete
specifier|private
name|Boolean
name|sendEmptyMessageOnDelete
init|=
literal|true
decl_stmt|;
DECL|method|getServers ()
specifier|public
name|List
name|getServers
parameter_list|()
block|{
return|return
name|servers
return|;
block|}
DECL|method|setServers (List servers)
specifier|public
name|void
name|setServers
parameter_list|(
name|List
name|servers
parameter_list|)
block|{
name|this
operator|.
name|servers
operator|=
name|servers
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|Integer
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Integer timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Integer
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getListChildren ()
specifier|public
name|Boolean
name|getListChildren
parameter_list|()
block|{
return|return
name|listChildren
return|;
block|}
DECL|method|setListChildren (Boolean listChildren)
specifier|public
name|void
name|setListChildren
parameter_list|(
name|Boolean
name|listChildren
parameter_list|)
block|{
name|this
operator|.
name|listChildren
operator|=
name|listChildren
expr_stmt|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|setPath (String path)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
DECL|method|getRepeat ()
specifier|public
name|Boolean
name|getRepeat
parameter_list|()
block|{
return|return
name|repeat
return|;
block|}
DECL|method|setRepeat (Boolean repeat)
specifier|public
name|void
name|setRepeat
parameter_list|(
name|Boolean
name|repeat
parameter_list|)
block|{
name|this
operator|.
name|repeat
operator|=
name|repeat
expr_stmt|;
block|}
annotation|@
name|Deprecated
annotation|@
name|DeprecatedConfigurationProperty
DECL|method|getAwaitExistence ()
specifier|public
name|Boolean
name|getAwaitExistence
parameter_list|()
block|{
return|return
name|awaitExistence
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setAwaitExistence (Boolean awaitExistence)
specifier|public
name|void
name|setAwaitExistence
parameter_list|(
name|Boolean
name|awaitExistence
parameter_list|)
block|{
name|this
operator|.
name|awaitExistence
operator|=
name|awaitExistence
expr_stmt|;
block|}
DECL|method|getBackoff ()
specifier|public
name|Long
name|getBackoff
parameter_list|()
block|{
return|return
name|backoff
return|;
block|}
DECL|method|setBackoff (Long backoff)
specifier|public
name|void
name|setBackoff
parameter_list|(
name|Long
name|backoff
parameter_list|)
block|{
name|this
operator|.
name|backoff
operator|=
name|backoff
expr_stmt|;
block|}
DECL|method|getCreate ()
specifier|public
name|Boolean
name|getCreate
parameter_list|()
block|{
return|return
name|create
return|;
block|}
DECL|method|setCreate (Boolean create)
specifier|public
name|void
name|setCreate
parameter_list|(
name|Boolean
name|create
parameter_list|)
block|{
name|this
operator|.
name|create
operator|=
name|create
expr_stmt|;
block|}
DECL|method|getCreateMode ()
specifier|public
name|String
name|getCreateMode
parameter_list|()
block|{
return|return
name|createMode
return|;
block|}
DECL|method|setCreateMode (String createMode)
specifier|public
name|void
name|setCreateMode
parameter_list|(
name|String
name|createMode
parameter_list|)
block|{
name|this
operator|.
name|createMode
operator|=
name|createMode
expr_stmt|;
block|}
DECL|method|getSendEmptyMessageOnDelete ()
specifier|public
name|Boolean
name|getSendEmptyMessageOnDelete
parameter_list|()
block|{
return|return
name|sendEmptyMessageOnDelete
return|;
block|}
DECL|method|setSendEmptyMessageOnDelete (Boolean sendEmptyMessageOnDelete)
specifier|public
name|void
name|setSendEmptyMessageOnDelete
parameter_list|(
name|Boolean
name|sendEmptyMessageOnDelete
parameter_list|)
block|{
name|this
operator|.
name|sendEmptyMessageOnDelete
operator|=
name|sendEmptyMessageOnDelete
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

