begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|util
operator|.
name|CamelContextHelper
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
name|LRUSoftCache
import|;
end_import

begin_comment
comment|/**  * Endpoint registry which is a based on a {@link org.apache.camel.util.LRUSoftCache}.  *<p/>  * We use a soft reference cache to allow the JVM to re-claim memory if it runs low on memory.  *  * @version   */
end_comment

begin_class
DECL|class|EndpointRegistry
specifier|public
class|class
name|EndpointRegistry
extends|extends
name|LRUSoftCache
argument_list|<
name|EndpointKey
argument_list|,
name|Endpoint
argument_list|>
block|{
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|method|EndpointRegistry (CamelContext context)
specifier|public
name|EndpointRegistry
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|CamelContextHelper
operator|.
name|getMaximumEndpointCacheSize
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|EndpointRegistry (CamelContext context, Map<EndpointKey, Endpoint> endpoints)
specifier|public
name|EndpointRegistry
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Map
argument_list|<
name|EndpointKey
argument_list|,
name|Endpoint
argument_list|>
name|endpoints
parameter_list|)
block|{
name|this
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|putAll
argument_list|(
name|endpoints
argument_list|)
expr_stmt|;
block|}
comment|/**      * Purges the cache      */
DECL|method|purge ()
specifier|public
name|void
name|purge
parameter_list|()
block|{
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"EndpointRegistry for "
operator|+
name|context
operator|.
name|getName
argument_list|()
operator|+
literal|", capacity: "
operator|+
name|getMaxCacheSize
argument_list|()
return|;
block|}
block|}
end_class

end_unit

