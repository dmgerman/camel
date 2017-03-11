begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeepermaster
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
package|;
end_package

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
name|Endpoint
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
name|Metadata
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
name|ObjectHelper
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
name|URISupport
import|;
end_import

begin_comment
comment|/**  * The zookeeper-master camel component ensures that only a single endpoint in a cluster is active at any  * point in time with all other JVMs being hot standbys which wait until the master JVM dies before  * taking over to provide high availability of a single consumer.  */
end_comment

begin_class
DECL|class|MasterComponent
specifier|public
class|class
name|MasterComponent
extends|extends
name|ZookeeperComponentSupport
block|{
DECL|field|containerIdFactory
specifier|private
name|ContainerIdFactory
name|containerIdFactory
init|=
operator|new
name|DefaultContainerIdFactory
argument_list|()
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"/camel/zookeepermaster/clusters/master"
argument_list|)
DECL|field|zkRoot
specifier|private
name|String
name|zkRoot
init|=
literal|"/camel/zookeepermaster/clusters/master"
decl_stmt|;
DECL|method|getContainerIdFactory ()
specifier|public
name|ContainerIdFactory
name|getContainerIdFactory
parameter_list|()
block|{
return|return
name|containerIdFactory
return|;
block|}
comment|/**      * To use a custom ContainerIdFactory for creating container ids.      */
DECL|method|setContainerIdFactory (ContainerIdFactory containerIdFactory)
specifier|public
name|void
name|setContainerIdFactory
parameter_list|(
name|ContainerIdFactory
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
comment|/**      * The root path to use in zookeeper where information is stored which nodes are master/slave etc.      * Will by default use: /camel/zookeepermaster/clusters/master      */
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
comment|//  Implementation methods
comment|//-------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> params)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|idx
init|=
name|remaining
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Missing : in URI so cannot split the group name from the actual URI for '"
operator|+
name|remaining
operator|+
literal|"'"
argument_list|)
throw|;
block|}
comment|// we are registering a regular endpoint
name|String
name|name
init|=
name|remaining
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|String
name|childUri
init|=
name|remaining
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
decl_stmt|;
comment|// we need to apply the params here
if|if
condition|(
name|params
operator|!=
literal|null
operator|&&
name|params
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|childUri
operator|=
name|childUri
operator|+
literal|"?"
operator|+
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|params
argument_list|)
expr_stmt|;
block|}
name|MasterEndpoint
name|answer
init|=
operator|new
name|MasterEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|name
argument_list|,
name|childUri
argument_list|)
decl_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getCamelClusterPath (String name)
specifier|protected
name|String
name|getCamelClusterPath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|path
init|=
name|name
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|zkRoot
argument_list|)
condition|)
block|{
name|path
operator|=
name|zkRoot
operator|+
literal|"/"
operator|+
name|name
expr_stmt|;
block|}
return|return
name|path
return|;
block|}
block|}
end_class

end_unit

