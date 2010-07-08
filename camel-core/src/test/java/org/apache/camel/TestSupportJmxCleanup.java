begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|management
operator|.
name|DefaultManagementAgent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_class
DECL|class|TestSupportJmxCleanup
specifier|public
specifier|final
class|class
name|TestSupportJmxCleanup
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|TestSupportJmxCleanup
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|removeMBeans (String domain)
specifier|public
specifier|static
name|void
name|removeMBeans
parameter_list|(
name|String
name|domain
parameter_list|)
throws|throws
name|Exception
block|{
name|MBeanServer
name|mbsc
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
init|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|getDomainName
argument_list|(
name|domain
argument_list|)
operator|+
literal|":*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjectName
name|on
range|:
name|s
control|)
block|{
name|mbsc
operator|.
name|unregisterMBean
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
block|}
comment|// useful helper to invoke in TestSupport to figure out what test leave junk behind
DECL|method|traceMBeans (String domain)
specifier|public
specifier|static
name|void
name|traceMBeans
parameter_list|(
name|String
name|domain
parameter_list|)
throws|throws
name|Exception
block|{
name|MBeanServer
name|mbsc
init|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
decl_stmt|;
name|String
name|d
init|=
name|getDomainName
argument_list|(
name|domain
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
init|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|d
operator|+
literal|":*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|" + "
operator|+
name|s
operator|.
name|size
argument_list|()
operator|+
literal|" ObjectNames registered in domain \""
operator|+
name|d
operator|+
literal|"\""
argument_list|)
expr_stmt|;
for|for
control|(
name|ObjectName
name|on
range|:
name|s
control|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|" |  "
operator|+
name|on
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getDomainName (String domain)
specifier|private
specifier|static
name|String
name|getDomainName
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
return|return
name|domain
operator|==
literal|null
condition|?
name|DefaultManagementAgent
operator|.
name|DEFAULT_DOMAIN
else|:
name|domain
return|;
block|}
DECL|method|TestSupportJmxCleanup ()
specifier|private
name|TestSupportJmxCleanup
parameter_list|()
block|{
comment|// no instances
block|}
block|}
end_class

end_unit

