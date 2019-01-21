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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArraySet
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
name|ResolveEndpointFailedException
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
name|StartupListener
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
name|service
operator|.
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.StartupListener} that defers starting {@link Service}s, until as late as possible during  * the startup process of {@link CamelContext}.  */
end_comment

begin_class
DECL|class|DeferServiceStartupListener
specifier|public
class|class
name|DeferServiceStartupListener
implements|implements
name|StartupListener
implements|,
name|Ordered
block|{
DECL|field|services
specifier|private
specifier|final
name|Set
argument_list|<
name|Service
argument_list|>
name|services
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|addService (Service service)
specifier|public
name|void
name|addService
parameter_list|(
name|Service
name|service
parameter_list|)
block|{
name|services
operator|.
name|add
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onCamelContextStarted (CamelContext context, boolean alreadyStarted)
specifier|public
name|void
name|onCamelContextStarted
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|alreadyStarted
parameter_list|)
throws|throws
name|Exception
block|{
comment|// new services may be added while starting a service
comment|// so use a while loop to get the newly added services as well
while|while
condition|(
operator|!
name|services
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Service
name|service
init|=
name|services
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|Endpoint
condition|)
block|{
name|Endpoint
name|endpoint
init|=
operator|(
name|Endpoint
operator|)
name|service
decl_stmt|;
throw|throw
operator|new
name|ResolveEndpointFailedException
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
finally|finally
block|{
name|services
operator|.
name|remove
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getOrder ()
specifier|public
name|int
name|getOrder
parameter_list|()
block|{
comment|// we want to be last, so the other startup listeners run first
return|return
name|Ordered
operator|.
name|LOWEST
return|;
block|}
block|}
end_class

end_unit

