begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer.json
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
operator|.
name|json
package|;
end_package

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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonProcessingException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectWriter
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
name|MeterRegistry
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
name|Tag
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
name|Tags
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
name|simple
operator|.
name|SimpleMeterRegistry
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
name|RuntimeCamelException
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
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
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
name|Registry
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
name|ServiceSupport
import|;
end_import

begin_class
DECL|class|AbstractMicrometerService
specifier|public
class|class
name|AbstractMicrometerService
extends|extends
name|ServiceSupport
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|meterRegistry
specifier|private
name|MeterRegistry
name|meterRegistry
decl_stmt|;
DECL|field|prettyPrint
specifier|private
name|boolean
name|prettyPrint
init|=
literal|true
decl_stmt|;
DECL|field|durationUnit
specifier|private
name|TimeUnit
name|durationUnit
init|=
name|TimeUnit
operator|.
name|MILLISECONDS
decl_stmt|;
DECL|field|matchingTags
specifier|private
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|matchingTags
init|=
name|Tags
operator|.
name|empty
argument_list|()
decl_stmt|;
DECL|field|matchingNames
specifier|private
name|Predicate
argument_list|<
name|String
argument_list|>
name|matchingNames
decl_stmt|;
DECL|field|mapper
specifier|private
specifier|transient
name|ObjectMapper
name|mapper
decl_stmt|;
DECL|field|secondsMapper
specifier|private
specifier|transient
name|ObjectMapper
name|secondsMapper
decl_stmt|;
DECL|method|getMeterRegistry ()
specifier|public
name|MeterRegistry
name|getMeterRegistry
parameter_list|()
block|{
return|return
name|meterRegistry
return|;
block|}
DECL|method|setMeterRegistry (MeterRegistry meterRegistry)
specifier|public
name|void
name|setMeterRegistry
parameter_list|(
name|MeterRegistry
name|meterRegistry
parameter_list|)
block|{
name|this
operator|.
name|meterRegistry
operator|=
name|meterRegistry
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|isPrettyPrint ()
specifier|public
name|boolean
name|isPrettyPrint
parameter_list|()
block|{
return|return
name|prettyPrint
return|;
block|}
DECL|method|setPrettyPrint (boolean prettyPrint)
specifier|public
name|void
name|setPrettyPrint
parameter_list|(
name|boolean
name|prettyPrint
parameter_list|)
block|{
name|this
operator|.
name|prettyPrint
operator|=
name|prettyPrint
expr_stmt|;
block|}
DECL|method|getDurationUnit ()
specifier|public
name|TimeUnit
name|getDurationUnit
parameter_list|()
block|{
return|return
name|durationUnit
return|;
block|}
DECL|method|setDurationUnit (TimeUnit durationUnit)
specifier|public
name|void
name|setDurationUnit
parameter_list|(
name|TimeUnit
name|durationUnit
parameter_list|)
block|{
name|this
operator|.
name|durationUnit
operator|=
name|durationUnit
expr_stmt|;
block|}
DECL|method|getMatchingTags ()
specifier|public
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|getMatchingTags
parameter_list|()
block|{
return|return
name|matchingTags
return|;
block|}
DECL|method|setMatchingTags (Iterable<Tag> matchingTags)
specifier|public
name|void
name|setMatchingTags
parameter_list|(
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|matchingTags
parameter_list|)
block|{
name|this
operator|.
name|matchingTags
operator|=
name|matchingTags
expr_stmt|;
block|}
DECL|method|getMatchingNames ()
specifier|public
name|Predicate
argument_list|<
name|String
argument_list|>
name|getMatchingNames
parameter_list|()
block|{
return|return
name|matchingNames
return|;
block|}
DECL|method|setMatchingNames (Predicate<String> matchingNames)
specifier|public
name|void
name|setMatchingNames
parameter_list|(
name|Predicate
argument_list|<
name|String
argument_list|>
name|matchingNames
parameter_list|)
block|{
name|this
operator|.
name|matchingNames
operator|=
name|matchingNames
expr_stmt|;
block|}
DECL|method|dumpStatisticsAsJson ()
specifier|public
name|String
name|dumpStatisticsAsJson
parameter_list|()
block|{
name|ObjectWriter
name|writer
init|=
name|mapper
operator|.
name|writer
argument_list|()
decl_stmt|;
if|if
condition|(
name|isPrettyPrint
argument_list|()
condition|)
block|{
name|writer
operator|=
name|writer
operator|.
name|withDefaultPrettyPrinter
argument_list|()
expr_stmt|;
block|}
try|try
block|{
return|return
name|writer
operator|.
name|writeValueAsString
argument_list|(
name|getMeterRegistry
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|JsonProcessingException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|dumpStatisticsAsJsonTimeUnitSeconds ()
specifier|public
name|String
name|dumpStatisticsAsJsonTimeUnitSeconds
parameter_list|()
block|{
name|ObjectWriter
name|writer
init|=
name|secondsMapper
operator|.
name|writer
argument_list|()
decl_stmt|;
if|if
condition|(
name|isPrettyPrint
argument_list|()
condition|)
block|{
name|writer
operator|=
name|writer
operator|.
name|withDefaultPrettyPrinter
argument_list|()
expr_stmt|;
block|}
try|try
block|{
return|return
name|writer
operator|.
name|writeValueAsString
argument_list|(
name|getMeterRegistry
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|JsonProcessingException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
block|{
if|if
condition|(
name|meterRegistry
operator|==
literal|null
condition|)
block|{
name|Registry
name|camelRegistry
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|meterRegistry
operator|=
name|camelRegistry
operator|.
name|lookupByNameAndType
argument_list|(
name|MicrometerConstants
operator|.
name|METRICS_REGISTRY_NAME
argument_list|,
name|MeterRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// create a new metricsRegistry by default
if|if
condition|(
name|meterRegistry
operator|==
literal|null
condition|)
block|{
name|meterRegistry
operator|=
operator|new
name|SimpleMeterRegistry
argument_list|()
expr_stmt|;
block|}
block|}
comment|// json mapper
name|this
operator|.
name|mapper
operator|=
operator|new
name|ObjectMapper
argument_list|()
operator|.
name|registerModule
argument_list|(
operator|new
name|MicrometerModule
argument_list|(
name|getDurationUnit
argument_list|()
argument_list|,
name|getMatchingNames
argument_list|()
argument_list|,
name|getMatchingTags
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|secondsMapper
operator|=
name|getDurationUnit
argument_list|()
operator|==
name|TimeUnit
operator|.
name|SECONDS
condition|?
name|this
operator|.
name|mapper
else|:
operator|new
name|ObjectMapper
argument_list|()
operator|.
name|registerModule
argument_list|(
operator|new
name|MicrometerModule
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|getMatchingNames
argument_list|()
argument_list|,
name|getMatchingTags
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
block|{      }
block|}
end_class

end_unit

