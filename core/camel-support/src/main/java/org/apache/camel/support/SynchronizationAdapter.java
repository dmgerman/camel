begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
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
name|Route
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
name|Synchronization
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
name|SynchronizationRouteAware
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
name|SynchronizationVetoable
import|;
end_import

begin_comment
comment|/**  * Simple {@link Synchronization} adapter with empty methods for easier overriding  * of single methods.  */
end_comment

begin_class
DECL|class|SynchronizationAdapter
specifier|public
class|class
name|SynchronizationAdapter
implements|implements
name|SynchronizationVetoable
implements|,
name|Ordered
implements|,
name|SynchronizationRouteAware
block|{
DECL|method|onComplete (Exchange exchange)
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|onDone
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|onFailure (Exchange exchange)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|onDone
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|onDone (Exchange exchange)
specifier|public
name|void
name|onDone
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|allowHandover ()
specifier|public
name|boolean
name|allowHandover
parameter_list|()
block|{
comment|// allow by default
return|return
literal|true
return|;
block|}
DECL|method|getOrder ()
specifier|public
name|int
name|getOrder
parameter_list|()
block|{
comment|// no particular order by default
return|return
literal|0
return|;
block|}
DECL|method|onBeforeRoute (Route route, Exchange exchange)
specifier|public
name|void
name|onBeforeRoute
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|onAfterRoute (Route route, Exchange exchange)
specifier|public
name|void
name|onAfterRoute
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit

