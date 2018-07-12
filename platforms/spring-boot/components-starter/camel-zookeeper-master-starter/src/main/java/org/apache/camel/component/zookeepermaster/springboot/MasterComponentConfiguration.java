begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeepermaster.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeepermaster
operator|.
name|springboot
package|;
end_package

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
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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

begin_comment
comment|/**  * Represents an endpoint which only becomes active when it obtains the master  * lock  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.zookeeper-master"
argument_list|)
DECL|class|MasterComponentConfiguration
specifier|public
class|class
name|MasterComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * To use a custom ContainerIdFactory for creating container ids. The option      * is a org.apache.camel.component.zookeepermaster.ContainerIdFactory type.      */
DECL|field|containerIdFactory
specifier|private
name|String
name|containerIdFactory
decl_stmt|;
comment|/**      * The root path to use in zookeeper where information is stored which nodes      * are master/slave etc. Will by default use:      * /camel/zookeepermaster/clusters/master      */
DECL|field|zkRoot
specifier|private
name|String
name|zkRoot
init|=
literal|"/camel/zookeepermaster/clusters/master"
decl_stmt|;
comment|/**      * To use a custom configured CuratorFramework as connection to zookeeper      * ensemble. The option is a org.apache.curator.framework.CuratorFramework      * type.      */
DECL|field|curator
specifier|private
name|String
name|curator
decl_stmt|;
comment|/**      * Timeout in millis to use when connecting to the zookeeper ensemble      */
DECL|field|maximumConnectionTimeout
specifier|private
name|Integer
name|maximumConnectionTimeout
init|=
literal|10000
decl_stmt|;
comment|/**      * The url for the zookeeper ensemble      */
DECL|field|zooKeeperUrl
specifier|private
name|String
name|zooKeeperUrl
init|=
literal|"localhost:2181"
decl_stmt|;
comment|/**      * The password to use when connecting to the zookeeper ensemble      */
DECL|field|zooKeeperPassword
specifier|private
name|String
name|zooKeeperPassword
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getContainerIdFactory ()
specifier|public
name|String
name|getContainerIdFactory
parameter_list|()
block|{
return|return
name|containerIdFactory
return|;
block|}
DECL|method|setContainerIdFactory (String containerIdFactory)
specifier|public
name|void
name|setContainerIdFactory
parameter_list|(
name|String
name|containerIdFactory
parameter_list|)
block|{
name|this
operator|.
name|containerIdFactory
operator|=
name|containerIdFactory
expr_stmt|;
block|}
DECL|method|getZkRoot ()
specifier|public
name|String
name|getZkRoot
parameter_list|()
block|{
return|return
name|zkRoot
return|;
block|}
DECL|method|setZkRoot (String zkRoot)
specifier|public
name|void
name|setZkRoot
parameter_list|(
name|String
name|zkRoot
parameter_list|)
block|{
name|this
operator|.
name|zkRoot
operator|=
name|zkRoot
expr_stmt|;
block|}
DECL|method|getCurator ()
specifier|public
name|String
name|getCurator
parameter_list|()
block|{
return|return
name|curator
return|;
block|}
DECL|method|setCurator (String curator)
specifier|public
name|void
name|setCurator
parameter_list|(
name|String
name|curator
parameter_list|)
block|{
name|this
operator|.
name|curator
operator|=
name|curator
expr_stmt|;
block|}
DECL|method|getMaximumConnectionTimeout ()
specifier|public
name|Integer
name|getMaximumConnectionTimeout
parameter_list|()
block|{
return|return
name|maximumConnectionTimeout
return|;
block|}
DECL|method|setMaximumConnectionTimeout (Integer maximumConnectionTimeout)
specifier|public
name|void
name|setMaximumConnectionTimeout
parameter_list|(
name|Integer
name|maximumConnectionTimeout
parameter_list|)
block|{
name|this
operator|.
name|maximumConnectionTimeout
operator|=
name|maximumConnectionTimeout
expr_stmt|;
block|}
DECL|method|getZooKeeperUrl ()
specifier|public
name|String
name|getZooKeeperUrl
parameter_list|()
block|{
return|return
name|zooKeeperUrl
return|;
block|}
DECL|method|setZooKeeperUrl (String zooKeeperUrl)
specifier|public
name|void
name|setZooKeeperUrl
parameter_list|(
name|String
name|zooKeeperUrl
parameter_list|)
block|{
name|this
operator|.
name|zooKeeperUrl
operator|=
name|zooKeeperUrl
expr_stmt|;
block|}
DECL|method|getZooKeeperPassword ()
specifier|public
name|String
name|getZooKeeperPassword
parameter_list|()
block|{
return|return
name|zooKeeperPassword
return|;
block|}
DECL|method|setZooKeeperPassword (String zooKeeperPassword)
specifier|public
name|void
name|setZooKeeperPassword
parameter_list|(
name|String
name|zooKeeperPassword
parameter_list|)
block|{
name|this
operator|.
name|zooKeeperPassword
operator|=
name|zooKeeperPassword
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
block|}
end_class

end_unit

