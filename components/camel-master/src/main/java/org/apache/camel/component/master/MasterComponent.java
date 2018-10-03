begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.master
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|master
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
name|DefaultComponent
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
name|impl
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
name|impl
operator|.
name|cluster
operator|.
name|ClusterServiceSelectors
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
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * The master camel component provides a way to ensures that only a single endpoint  * in a cluster is active at any point in time with automatic failover if the  * JVM dies or the leadership is lot for any reason.  *<p>  * This feature is useful if you need to consume from a backend that does not  * support concurrent consumption.  */
end_comment

begin_class
DECL|class|MasterComponent
specifier|public
class|class
name|MasterComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|service
specifier|private
name|CamelClusterService
name|service
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|serviceSelector
specifier|private
name|CamelClusterService
operator|.
name|Selector
name|serviceSelector
decl_stmt|;
DECL|method|MasterComponent ()
specifier|public
name|MasterComponent
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|MasterComponent (CamelContext context)
specifier|public
name|MasterComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|serviceSelector
operator|=
name|ClusterServiceSelectors
operator|.
name|DEFAULT_SELECTOR
expr_stmt|;
block|}
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
comment|// we are registering a regular endpoint
name|String
name|namespace
init|=
name|StringHelper
operator|.
name|before
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|delegateUri
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|namespace
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|delegateUri
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Wrong uri syntax : master:namespace:uri, got "
operator|+
name|remaining
argument_list|)
throw|;
block|}
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
name|delegateUri
operator|=
name|delegateUri
operator|+
literal|"?"
operator|+
name|uri
operator|.
name|substring
argument_list|(
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|MasterEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|getClusterService
argument_list|()
argument_list|,
name|namespace
argument_list|,
name|delegateUri
argument_list|)
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
comment|/**      * Inject the service to use.      */
DECL|method|setService (CamelClusterService service)
specifier|public
name|void
name|setService
parameter_list|(
name|CamelClusterService
name|service
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
block|}
DECL|method|getServiceSelector ()
specifier|public
name|CamelClusterService
operator|.
name|Selector
name|getServiceSelector
parameter_list|()
block|{
return|return
name|serviceSelector
return|;
block|}
comment|/**      *      * Inject the service selector used to lookup the {@link CamelClusterService} to use.      */
DECL|method|setServiceSelector (CamelClusterService.Selector serviceSelector)
specifier|public
name|void
name|setServiceSelector
parameter_list|(
name|CamelClusterService
operator|.
name|Selector
name|serviceSelector
parameter_list|)
block|{
name|this
operator|.
name|serviceSelector
operator|=
name|serviceSelector
expr_stmt|;
block|}
comment|// ********************************
comment|// Helpers
comment|// ********************************
DECL|method|getClusterService ()
specifier|private
name|CamelClusterService
name|getClusterService
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|CamelContext
name|context
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|context
argument_list|,
literal|"Camel Context"
argument_list|)
expr_stmt|;
name|service
operator|=
name|ClusterServiceHelper
operator|.
name|lookupService
argument_list|(
name|context
argument_list|,
name|serviceSelector
argument_list|)
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|IllegalStateException
argument_list|(
literal|"No cluster service found"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|service
return|;
block|}
block|}
end_class

end_unit

