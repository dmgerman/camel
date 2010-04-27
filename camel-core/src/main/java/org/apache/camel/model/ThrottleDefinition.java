begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|Processor
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
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;throttle/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"throttle"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ThrottleDefinition
specifier|public
class|class
name|ThrottleDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|ProcessorDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|maximumRequestsPerPeriod
specifier|private
name|Long
name|maximumRequestsPerPeriod
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|timePeriodMillis
specifier|private
name|long
name|timePeriodMillis
init|=
literal|1000
decl_stmt|;
DECL|method|ThrottleDefinition ()
specifier|public
name|ThrottleDefinition
parameter_list|()
block|{     }
DECL|method|ThrottleDefinition (long maximumRequestsPerPeriod)
specifier|public
name|ThrottleDefinition
parameter_list|(
name|long
name|maximumRequestsPerPeriod
parameter_list|)
block|{
name|this
operator|.
name|maximumRequestsPerPeriod
operator|=
name|maximumRequestsPerPeriod
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
literal|"Throttle["
operator|+
name|getMaximumRequestsPerPeriod
argument_list|()
operator|+
literal|" request per "
operator|+
name|getTimePeriodMillis
argument_list|()
operator|+
literal|" millis -> "
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"throttle"
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|""
operator|+
name|getMaximumRequestsPerPeriod
argument_list|()
operator|+
literal|" per "
operator|+
name|getTimePeriodMillis
argument_list|()
operator|+
literal|" (ms)"
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Processor
name|childProcessor
init|=
name|this
operator|.
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
operator|new
name|Throttler
argument_list|(
name|childProcessor
argument_list|,
name|maximumRequestsPerPeriod
argument_list|,
name|timePeriodMillis
argument_list|)
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the time period during which the maximum request count is valid for      *      * @param timePeriodMillis  period in millis      * @return the builder      */
DECL|method|timePeriodMillis (long timePeriodMillis)
specifier|public
name|ThrottleDefinition
name|timePeriodMillis
parameter_list|(
name|long
name|timePeriodMillis
parameter_list|)
block|{
name|setTimePeriodMillis
argument_list|(
name|timePeriodMillis
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the time period during which the maximum request count per period      *      * @param maximumRequestsPerPeriod  the maximum request count number per time period      * @return the builder      */
DECL|method|maximumRequestsPerPeriod (Long maximumRequestsPerPeriod)
specifier|public
name|ThrottleDefinition
name|maximumRequestsPerPeriod
parameter_list|(
name|Long
name|maximumRequestsPerPeriod
parameter_list|)
block|{
name|setMaximumRequestsPerPeriod
argument_list|(
name|maximumRequestsPerPeriod
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getMaximumRequestsPerPeriod ()
specifier|public
name|Long
name|getMaximumRequestsPerPeriod
parameter_list|()
block|{
return|return
name|maximumRequestsPerPeriod
return|;
block|}
DECL|method|setMaximumRequestsPerPeriod (Long maximumRequestsPerPeriod)
specifier|public
name|void
name|setMaximumRequestsPerPeriod
parameter_list|(
name|Long
name|maximumRequestsPerPeriod
parameter_list|)
block|{
name|this
operator|.
name|maximumRequestsPerPeriod
operator|=
name|maximumRequestsPerPeriod
expr_stmt|;
block|}
DECL|method|getTimePeriodMillis ()
specifier|public
name|long
name|getTimePeriodMillis
parameter_list|()
block|{
return|return
name|timePeriodMillis
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
name|this
operator|.
name|timePeriodMillis
operator|=
name|timePeriodMillis
expr_stmt|;
block|}
block|}
end_class

end_unit

