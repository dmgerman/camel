begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|Optional
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
name|CamelContext
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
name|ServiceStatus
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
name|StatefulService
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedClusterServiceMBean
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
name|cluster
operator|.
name|CamelClusterService
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
name|cluster
operator|.
name|ClusterServiceHelper
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
name|spi
operator|.
name|ManagementStrategy
import|;
end_import

begin_class
DECL|class|ManagedClusterService
specifier|public
class|class
name|ManagedClusterService
implements|implements
name|ManagedClusterServiceMBean
block|{
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|field|service
specifier|private
specifier|final
name|CamelClusterService
name|service
decl_stmt|;
DECL|method|ManagedClusterService (CamelContext context, CamelClusterService service)
specifier|public
name|ManagedClusterService
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|CamelClusterService
name|service
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
block|}
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
comment|// do nothing
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|getService ()
specifier|public
name|CamelClusterService
name|getService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext is not started"
argument_list|)
throw|;
block|}
name|service
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext is not started"
argument_list|)
throw|;
block|}
name|service
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
comment|// must use String type to be sure remote JMX can read the attribute without requiring Camel classes.
if|if
condition|(
name|service
operator|instanceof
name|StatefulService
condition|)
block|{
name|ServiceStatus
name|status
init|=
operator|(
operator|(
name|StatefulService
operator|)
name|service
operator|)
operator|.
name|getStatus
argument_list|()
decl_stmt|;
return|return
name|status
operator|.
name|name
argument_list|()
return|;
block|}
comment|// assume started if not a ServiceSupport instance
return|return
name|ServiceStatus
operator|.
name|Started
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelId ()
specifier|public
name|String
name|getCamelId
parameter_list|()
block|{
return|return
name|context
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getNamespaces ()
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getNamespaces
parameter_list|()
block|{
return|return
name|ClusterServiceHelper
operator|.
name|lookupService
argument_list|(
name|context
argument_list|)
operator|.
name|map
argument_list|(
name|CamelClusterService
operator|::
name|getNamespaces
argument_list|)
operator|.
name|orElseGet
argument_list|(
name|Collections
operator|::
name|emptyList
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|startView (String namespace)
specifier|public
name|void
name|startView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
name|Optional
argument_list|<
name|CamelClusterService
argument_list|>
name|service
init|=
name|ClusterServiceHelper
operator|.
name|lookupService
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|service
operator|.
name|get
argument_list|()
operator|.
name|startView
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|stopView (String namespace)
specifier|public
name|void
name|stopView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
name|Optional
argument_list|<
name|CamelClusterService
argument_list|>
name|service
init|=
name|ClusterServiceHelper
operator|.
name|lookupService
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|service
operator|.
name|get
argument_list|()
operator|.
name|stopView
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isLeader (String namespace)
specifier|public
name|boolean
name|isLeader
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
return|return
name|ClusterServiceHelper
operator|.
name|lookupService
argument_list|(
name|context
argument_list|)
operator|.
name|map
argument_list|(
name|s
lambda|->
name|s
operator|.
name|isLeader
argument_list|(
name|namespace
argument_list|)
argument_list|)
operator|.
name|orElse
argument_list|(
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

