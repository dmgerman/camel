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
name|Locale
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
name|TimeUnit
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlJavaTypeAdapter
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
name|builder
operator|.
name|xml
operator|.
name|TimeUnitAdapter
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
name|SamplingThrottler
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
name|spi
operator|.
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * Extract a sample of the messages passing through a route  *  * @version   */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,management"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"sample"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|SamplingDefinition
specifier|public
class|class
name|SamplingDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|SamplingDefinition
argument_list|>
block|{
comment|// use Long to let it be optional in JAXB so when using XML the default is 1 second
comment|// TODO: Camel 3.0 Should extend NoOutputDefinition
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|samplePeriod
specifier|private
name|Long
name|samplePeriod
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|messageFrequency
specifier|private
name|Long
name|messageFrequency
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|TimeUnitAdapter
operator|.
name|class
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"SECONDS"
argument_list|)
DECL|field|units
specifier|private
name|TimeUnit
name|units
decl_stmt|;
DECL|method|SamplingDefinition ()
specifier|public
name|SamplingDefinition
parameter_list|()
block|{     }
DECL|method|SamplingDefinition (long samplePeriod, TimeUnit units)
specifier|public
name|SamplingDefinition
parameter_list|(
name|long
name|samplePeriod
parameter_list|,
name|TimeUnit
name|units
parameter_list|)
block|{
name|this
operator|.
name|samplePeriod
operator|=
name|samplePeriod
expr_stmt|;
name|this
operator|.
name|units
operator|=
name|units
expr_stmt|;
block|}
DECL|method|SamplingDefinition (long messageFrequency)
specifier|public
name|SamplingDefinition
parameter_list|(
name|long
name|messageFrequency
parameter_list|)
block|{
name|this
operator|.
name|messageFrequency
operator|=
name|messageFrequency
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
literal|"Sample["
operator|+
name|description
argument_list|()
operator|+
literal|" -> "
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|description ()
specifier|protected
name|String
name|description
parameter_list|()
block|{
if|if
condition|(
name|messageFrequency
operator|!=
literal|null
condition|)
block|{
return|return
literal|"1 Exchange per "
operator|+
name|getMessageFrequency
argument_list|()
operator|+
literal|" messages received"
return|;
block|}
else|else
block|{
name|TimeUnit
name|tu
init|=
name|getUnits
argument_list|()
operator|!=
literal|null
condition|?
name|getUnits
argument_list|()
else|:
name|TimeUnit
operator|.
name|SECONDS
decl_stmt|;
return|return
literal|"1 Exchange per "
operator|+
name|getSamplePeriod
argument_list|()
operator|+
literal|" "
operator|+
name|tu
operator|.
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
return|;
block|}
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
literal|"sample["
operator|+
name|description
argument_list|()
operator|+
literal|"]"
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
if|if
condition|(
name|messageFrequency
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|SamplingThrottler
argument_list|(
name|childProcessor
argument_list|,
name|messageFrequency
argument_list|)
return|;
block|}
else|else
block|{
comment|// should default be 1 sample period
name|long
name|time
init|=
name|getSamplePeriod
argument_list|()
operator|!=
literal|null
condition|?
name|getSamplePeriod
argument_list|()
else|:
literal|1L
decl_stmt|;
comment|// should default be in seconds
name|TimeUnit
name|tu
init|=
name|getUnits
argument_list|()
operator|!=
literal|null
condition|?
name|getUnits
argument_list|()
else|:
name|TimeUnit
operator|.
name|SECONDS
decl_stmt|;
return|return
operator|new
name|SamplingThrottler
argument_list|(
name|childProcessor
argument_list|,
name|time
argument_list|,
name|tu
argument_list|)
return|;
block|}
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the sample message count which only a single {@link org.apache.camel.Exchange} will pass through after this many received.      *      * @param messageFrequency       * @return the builder      */
DECL|method|sampleMessageFrequency (long messageFrequency)
specifier|public
name|SamplingDefinition
name|sampleMessageFrequency
parameter_list|(
name|long
name|messageFrequency
parameter_list|)
block|{
name|setMessageFrequency
argument_list|(
name|messageFrequency
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the sample period during which only a single {@link org.apache.camel.Exchange} will pass through.      *      * @param samplePeriod the period      * @return the builder      */
DECL|method|samplePeriod (long samplePeriod)
specifier|public
name|SamplingDefinition
name|samplePeriod
parameter_list|(
name|long
name|samplePeriod
parameter_list|)
block|{
name|setSamplePeriod
argument_list|(
name|samplePeriod
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the time units for the sample period, defaulting to seconds.      *      * @param units the time unit of the sample period.      * @return the builder      */
DECL|method|timeUnits (TimeUnit units)
specifier|public
name|SamplingDefinition
name|timeUnits
parameter_list|(
name|TimeUnit
name|units
parameter_list|)
block|{
name|setUnits
argument_list|(
name|units
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getSamplePeriod ()
specifier|public
name|Long
name|getSamplePeriod
parameter_list|()
block|{
return|return
name|samplePeriod
return|;
block|}
comment|/**      * Sets the sample period during which only a single Exchange will pass through.      */
DECL|method|setSamplePeriod (Long samplePeriod)
specifier|public
name|void
name|setSamplePeriod
parameter_list|(
name|Long
name|samplePeriod
parameter_list|)
block|{
name|this
operator|.
name|samplePeriod
operator|=
name|samplePeriod
expr_stmt|;
block|}
DECL|method|getMessageFrequency ()
specifier|public
name|Long
name|getMessageFrequency
parameter_list|()
block|{
return|return
name|messageFrequency
return|;
block|}
comment|/**      * Sets the sample message count which only a single Exchange will pass through after this many received.      */
DECL|method|setMessageFrequency (Long messageFrequency)
specifier|public
name|void
name|setMessageFrequency
parameter_list|(
name|Long
name|messageFrequency
parameter_list|)
block|{
name|this
operator|.
name|messageFrequency
operator|=
name|messageFrequency
expr_stmt|;
block|}
DECL|method|setUnits (String units)
specifier|public
name|void
name|setUnits
parameter_list|(
name|String
name|units
parameter_list|)
block|{
name|this
operator|.
name|units
operator|=
name|TimeUnit
operator|.
name|valueOf
argument_list|(
name|units
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the time units for the sample period, defaulting to seconds.      */
DECL|method|setUnits (TimeUnit units)
specifier|public
name|void
name|setUnits
parameter_list|(
name|TimeUnit
name|units
parameter_list|)
block|{
name|this
operator|.
name|units
operator|=
name|units
expr_stmt|;
block|}
DECL|method|getUnits ()
specifier|public
name|TimeUnit
name|getUnits
parameter_list|()
block|{
return|return
name|units
return|;
block|}
block|}
end_class

end_unit

