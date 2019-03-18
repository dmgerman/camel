begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jgroups.springboot.cluster.springboot
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
operator|.
name|springboot
operator|.
name|cluster
operator|.
name|springboot
package|;
end_package

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

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.jgroups.lock.cluster.service"
argument_list|)
DECL|class|JGroupsLockClusterServiceConfiguration
specifier|public
class|class
name|JGroupsLockClusterServiceConfiguration
block|{
comment|/**      * Sets if the jgroups lock cluster service should be enabled or not, default is false.      */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
decl_stmt|;
comment|/**      * Cluster Service ID      */
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * JGrups configuration File name      */
DECL|field|jgroupsConfig
specifier|private
name|String
name|jgroupsConfig
decl_stmt|;
comment|/**      * JGroups Cluster name      */
DECL|field|jgroupsClusterName
specifier|private
name|String
name|jgroupsClusterName
decl_stmt|;
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getJgroupsConfig ()
specifier|public
name|String
name|getJgroupsConfig
parameter_list|()
block|{
return|return
name|jgroupsConfig
return|;
block|}
DECL|method|setJgroupsConfig (String jgroupsConfig)
specifier|public
name|void
name|setJgroupsConfig
parameter_list|(
name|String
name|jgroupsConfig
parameter_list|)
block|{
name|this
operator|.
name|jgroupsConfig
operator|=
name|jgroupsConfig
expr_stmt|;
block|}
DECL|method|getJgroupsClusterName ()
specifier|public
name|String
name|getJgroupsClusterName
parameter_list|()
block|{
return|return
name|jgroupsClusterName
return|;
block|}
DECL|method|setJgroupsClusterName (String jgroupsClusterName)
specifier|public
name|void
name|setJgroupsClusterName
parameter_list|(
name|String
name|jgroupsClusterName
parameter_list|)
block|{
name|this
operator|.
name|jgroupsClusterName
operator|=
name|jgroupsClusterName
expr_stmt|;
block|}
block|}
end_class

end_unit

