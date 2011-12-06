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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|ManagedThrottlerMBean
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
name|model
operator|.
name|ProcessorDefinition
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
name|processor
operator|.
name|Throttler
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|Builder
operator|.
name|constant
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Throttler"
argument_list|)
DECL|class|ManagedThrottler
specifier|public
class|class
name|ManagedThrottler
extends|extends
name|ManagedProcessor
implements|implements
name|ManagedThrottlerMBean
block|{
DECL|field|throttler
specifier|private
specifier|final
name|Throttler
name|throttler
decl_stmt|;
DECL|method|ManagedThrottler (CamelContext context, Throttler throttler, ProcessorDefinition<?> definition)
specifier|public
name|ManagedThrottler
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Throttler
name|throttler
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|throttler
argument_list|,
name|definition
argument_list|)
expr_stmt|;
name|this
operator|.
name|throttler
operator|=
name|throttler
expr_stmt|;
block|}
DECL|method|getThrottler ()
specifier|public
name|Throttler
name|getThrottler
parameter_list|()
block|{
return|return
name|throttler
return|;
block|}
DECL|method|getMaximumRequestsPerPeriod ()
specifier|public
name|long
name|getMaximumRequestsPerPeriod
parameter_list|()
block|{
return|return
name|getThrottler
argument_list|()
operator|.
name|getCurrentMaximumRequestsPerPeriod
argument_list|()
return|;
block|}
DECL|method|setMaximumRequestsPerPeriod (long maximumRequestsPerPeriod)
specifier|public
name|void
name|setMaximumRequestsPerPeriod
parameter_list|(
name|long
name|maximumRequestsPerPeriod
parameter_list|)
block|{
name|getThrottler
argument_list|()
operator|.
name|setMaximumRequestsPerPeriodExpression
argument_list|(
name|constant
argument_list|(
name|maximumRequestsPerPeriod
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getTimePeriodMillis ()
specifier|public
name|long
name|getTimePeriodMillis
parameter_list|()
block|{
return|return
name|getThrottler
argument_list|()
operator|.
name|getTimePeriodMillis
argument_list|()
return|;
block|}
DECL|method|setTimePeriodMillis (long timePeriodMillis)
specifier|public
name|void
name|setTimePeriodMillis
parameter_list|(
name|long
name|timePeriodMillis
parameter_list|)
block|{
name|getThrottler
argument_list|()
operator|.
name|setTimePeriodMillis
argument_list|(
name|timePeriodMillis
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

