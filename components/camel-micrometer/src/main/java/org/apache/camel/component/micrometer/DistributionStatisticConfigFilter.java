begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|Meter
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|config
operator|.
name|MeterFilter
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|distribution
operator|.
name|DistributionStatisticConfig
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
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|ALWAYS
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
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|CAMEL_METERS
import|;
end_import

begin_comment
comment|/**  * Filter for adding distribution statistics to Timers and Distribution Summaries.  * Configure and add this to the {@link io.micrometer.core.instrument.MeterRegistry}  * if desired:  *  *<pre>  *     DistributionStatisticConfigFilter filter = new DistributionStatisticConfigFilter()  *     // filter.set...  *     meterRegistry.config().meterFilter(filter)  *</pre>  */
end_comment

begin_class
DECL|class|DistributionStatisticConfigFilter
specifier|public
class|class
name|DistributionStatisticConfigFilter
implements|implements
name|MeterFilter
block|{
DECL|field|appliesTo
specifier|private
name|Predicate
argument_list|<
name|Meter
operator|.
name|Id
argument_list|>
name|appliesTo
init|=
name|ALWAYS
decl_stmt|;
DECL|field|maximumExpectedValue
specifier|private
name|Long
name|maximumExpectedValue
decl_stmt|;
DECL|field|minimumExpectedValue
specifier|private
name|Long
name|minimumExpectedValue
decl_stmt|;
DECL|field|publishPercentileHistogram
specifier|private
name|Boolean
name|publishPercentileHistogram
init|=
literal|true
decl_stmt|;
DECL|field|percentilePrecision
specifier|private
name|Integer
name|percentilePrecision
decl_stmt|;
DECL|field|bufferLength
specifier|private
name|Integer
name|bufferLength
decl_stmt|;
DECL|field|expiry
specifier|private
name|Duration
name|expiry
decl_stmt|;
DECL|field|percentiles
specifier|private
name|double
index|[]
name|percentiles
decl_stmt|;
DECL|field|slas
specifier|private
name|long
index|[]
name|slas
decl_stmt|;
annotation|@
name|Override
DECL|method|configure (Meter.Id id, DistributionStatisticConfig config)
specifier|public
name|DistributionStatisticConfig
name|configure
parameter_list|(
name|Meter
operator|.
name|Id
name|id
parameter_list|,
name|DistributionStatisticConfig
name|config
parameter_list|)
block|{
if|if
condition|(
name|CAMEL_METERS
operator|.
name|and
argument_list|(
name|appliesTo
argument_list|)
operator|.
name|test
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
name|DistributionStatisticConfig
operator|.
name|builder
argument_list|()
operator|.
name|percentilesHistogram
argument_list|(
name|publishPercentileHistogram
argument_list|)
operator|.
name|percentiles
argument_list|(
name|percentiles
argument_list|)
operator|.
name|percentilePrecision
argument_list|(
name|percentilePrecision
argument_list|)
operator|.
name|maximumExpectedValue
argument_list|(
name|maximumExpectedValue
argument_list|)
operator|.
name|minimumExpectedValue
argument_list|(
name|minimumExpectedValue
argument_list|)
operator|.
name|sla
argument_list|(
name|slas
argument_list|)
operator|.
name|bufferLength
argument_list|(
name|bufferLength
argument_list|)
operator|.
name|expiry
argument_list|(
name|expiry
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|merge
argument_list|(
name|config
argument_list|)
return|;
block|}
return|return
name|config
return|;
block|}
comment|/**      * Restrict a condition under which this config applies to a Camel meter      *      * @param appliesTo predicate that must return true so that this config applies      */
DECL|method|andAppliesTo (Predicate<Meter.Id> appliesTo)
specifier|public
name|DistributionStatisticConfigFilter
name|andAppliesTo
parameter_list|(
name|Predicate
argument_list|<
name|Meter
operator|.
name|Id
argument_list|>
name|appliesTo
parameter_list|)
block|{
name|this
operator|.
name|appliesTo
operator|=
name|this
operator|.
name|appliesTo
operator|.
name|and
argument_list|(
name|appliesTo
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Add a condition under which this config applies to a Camel meter      *      * @param appliesTo predicate that must return true so that this config applies      */
DECL|method|orAppliesTo (Predicate<Meter.Id> appliesTo)
specifier|public
name|DistributionStatisticConfigFilter
name|orAppliesTo
parameter_list|(
name|Predicate
argument_list|<
name|Meter
operator|.
name|Id
argument_list|>
name|appliesTo
parameter_list|)
block|{
name|this
operator|.
name|appliesTo
operator|=
name|this
operator|.
name|appliesTo
operator|.
name|or
argument_list|(
name|appliesTo
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum expected value for a distribution summary value.      * Controls the number of buckets shipped by publishPercentileHistogram as well as controlling the      * accuracy and memory footprint of the underlying HdrHistogram structure.      *      * @param maximumExpectedValue the maximum expected value for a distribution summary value      */
DECL|method|setMaximumExpectedValue (Long maximumExpectedValue)
specifier|public
name|DistributionStatisticConfigFilter
name|setMaximumExpectedValue
parameter_list|(
name|Long
name|maximumExpectedValue
parameter_list|)
block|{
name|this
operator|.
name|maximumExpectedValue
operator|=
name|maximumExpectedValue
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the minimum expected value for a distribution summary value.      * Controls the number of buckets shipped by publishPercentileHistogram as well as controlling the      * accuracy and memory footprint of the underlying HdrHistogram structure.      *      * @param minimumExpectedValue the minimum expected value for a distribution summary value      */
DECL|method|setMinimumExpectedValue (Long minimumExpectedValue)
specifier|public
name|DistributionStatisticConfigFilter
name|setMinimumExpectedValue
parameter_list|(
name|Long
name|minimumExpectedValue
parameter_list|)
block|{
name|this
operator|.
name|minimumExpectedValue
operator|=
name|minimumExpectedValue
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum expected duration for a timer value      * Controls the number of buckets shipped by publishPercentileHistogram as well as controlling the      * accuracy and memory footprint of the underlying HdrHistogram structure.      *      * @param maximumExpectedDuration the maximum expected duration for a timer value      */
DECL|method|setMaximumExpectedDuration (Duration maximumExpectedDuration)
specifier|public
name|DistributionStatisticConfigFilter
name|setMaximumExpectedDuration
parameter_list|(
name|Duration
name|maximumExpectedDuration
parameter_list|)
block|{
name|this
operator|.
name|maximumExpectedValue
operator|=
name|maximumExpectedDuration
operator|.
name|toNanos
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the minimum expected duration for a timer value      * Controls the number of buckets shipped by publishPercentileHistogram as well as controlling the      * accuracy and memory footprint of the underlying HdrHistogram structure.      *      * @param minimumExpectedDuration the minimum expected duration for a timer value      */
DECL|method|setMinimumExpectedDuration (Duration minimumExpectedDuration)
specifier|public
name|DistributionStatisticConfigFilter
name|setMinimumExpectedDuration
parameter_list|(
name|Duration
name|minimumExpectedDuration
parameter_list|)
block|{
name|this
operator|.
name|minimumExpectedValue
operator|=
name|minimumExpectedDuration
operator|.
name|toNanos
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether to publish aggregatable percentile approximations for Prometheus or Atlas.      * Has no effect on systems that do not support aggregatable percentile approximations.      * This defaults to true.      *      * @param publishPercentileHistogram Whether to publish aggregatable percentile approximations.      */
DECL|method|setPublishPercentileHistogram (Boolean publishPercentileHistogram)
specifier|public
name|DistributionStatisticConfigFilter
name|setPublishPercentileHistogram
parameter_list|(
name|Boolean
name|publishPercentileHistogram
parameter_list|)
block|{
name|this
operator|.
name|publishPercentileHistogram
operator|=
name|publishPercentileHistogram
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setBufferLength (Integer bufferLength)
specifier|public
name|DistributionStatisticConfigFilter
name|setBufferLength
parameter_list|(
name|Integer
name|bufferLength
parameter_list|)
block|{
name|this
operator|.
name|bufferLength
operator|=
name|bufferLength
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setExpiry (Duration expiry)
specifier|public
name|DistributionStatisticConfigFilter
name|setExpiry
parameter_list|(
name|Duration
name|expiry
parameter_list|)
block|{
name|this
operator|.
name|expiry
operator|=
name|expiry
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Calculate and publish percentile values. These values are non-aggregatable across dimensions.      *      * @param percentiles array of percentiles to be published      */
DECL|method|setPercentiles (double[] percentiles)
specifier|public
name|DistributionStatisticConfigFilter
name|setPercentiles
parameter_list|(
name|double
index|[]
name|percentiles
parameter_list|)
block|{
name|this
operator|.
name|percentiles
operator|=
name|percentiles
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setPercentilePrecision (Integer percentilePrecision)
specifier|public
name|DistributionStatisticConfigFilter
name|setPercentilePrecision
parameter_list|(
name|Integer
name|percentilePrecision
parameter_list|)
block|{
name|this
operator|.
name|percentilePrecision
operator|=
name|percentilePrecision
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Publish a cumulative histogram with buckets defined by your SLAs. Used together with publishPercentileHistogram      * on a monitoring system that supports aggregatable percentiles, this setting adds additional buckets to the published histogram.      * Used on a system that does not support aggregatable percentiles, this setting causes a histogram to be published with only these buckets.      *      * @param slas array of percentiles to be published      */
DECL|method|setSlas (long[] slas)
specifier|public
name|DistributionStatisticConfigFilter
name|setSlas
parameter_list|(
name|long
index|[]
name|slas
parameter_list|)
block|{
name|this
operator|.
name|slas
operator|=
name|slas
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

