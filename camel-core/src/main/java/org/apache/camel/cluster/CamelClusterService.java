begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cluster
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
name|Map
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
name|CamelContextAware
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
name|Ordered
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
name|Service
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
name|IdAware
import|;
end_import

begin_interface
DECL|interface|CamelClusterService
specifier|public
interface|interface
name|CamelClusterService
extends|extends
name|Service
extends|,
name|CamelContextAware
extends|,
name|IdAware
extends|,
name|Ordered
block|{
annotation|@
name|Override
DECL|method|getOrder ()
specifier|default
name|int
name|getOrder
parameter_list|()
block|{
return|return
name|Ordered
operator|.
name|LOWEST
return|;
block|}
comment|/**      * Get a view of the cluster bound to a namespace creating it if needed. Multiple      * calls to this method with the same namespace should return the same instance.      * The instance is automatically started the first time it is instantiated and      * if the cluster service is ready.      *      * @param namespace the namespace the view refer to.      * @return the view.      * @throws Exception if the view can't be created.      */
DECL|method|getView (String namespace)
name|CamelClusterView
name|getView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Release a view if it has no references.      *      * @param view the view.      * @throws Exception      */
DECL|method|releaseView (CamelClusterView view)
name|void
name|releaseView
parameter_list|(
name|CamelClusterView
name|view
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Return the namespaces handled by this service.      */
DECL|method|getNamespaces ()
name|Collection
argument_list|<
name|String
argument_list|>
name|getNamespaces
parameter_list|()
function_decl|;
comment|/**      * Force start of the view associated to the give namespace.      */
DECL|method|startView (String namespace)
name|void
name|startView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Force stop of the view associated to the give namespace.      */
DECL|method|stopView (String namespace)
name|void
name|stopView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Check if the service is the leader on the given namespace.      *      * @param namespace the namespace.      * @return      */
DECL|method|isLeader (String namespace)
name|boolean
name|isLeader
parameter_list|(
name|String
name|namespace
parameter_list|)
function_decl|;
comment|/**      * Attributes associated to the service.      */
DECL|method|getAttributes ()
specifier|default
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getAttributes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
comment|/**      * Access the underlying concrete CamelClusterService implementation to      * provide access to further features.      *      * @param clazz the proprietary class or interface of the underlying concrete CamelClusterService.      * @return an instance of the underlying concrete CamelClusterService as the required type.      */
DECL|method|unwrap (Class<T> clazz)
specifier|default
parameter_list|<
name|T
extends|extends
name|CamelClusterService
parameter_list|>
name|T
name|unwrap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|CamelClusterService
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|this
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to unwrap this CamelClusterService type ("
operator|+
name|getClass
argument_list|()
operator|+
literal|") to the required type ("
operator|+
name|clazz
operator|+
literal|")"
argument_list|)
throw|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|Selector
interface|interface
name|Selector
block|{
comment|/**          * Select a specific CamelClusterService instance among a collection.          */
DECL|method|select (Collection<CamelClusterService> services)
name|Optional
argument_list|<
name|CamelClusterService
argument_list|>
name|select
parameter_list|(
name|Collection
argument_list|<
name|CamelClusterService
argument_list|>
name|services
parameter_list|)
function_decl|;
block|}
block|}
end_interface

end_unit

