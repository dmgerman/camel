begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jgroups.springboot.ha.springboot
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
name|ha
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
block|}
end_class

end_unit

