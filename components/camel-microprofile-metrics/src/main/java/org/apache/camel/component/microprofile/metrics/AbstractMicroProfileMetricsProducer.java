begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.microprofile.metrics
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|Message
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
name|DefaultProducer
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
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|Metadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MetadataBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|Metric
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MetricRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MetricType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|Tag
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|CAMEL_CONTEXT_TAG
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|HEADER_METRIC_DESCRIPTION
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|HEADER_METRIC_DISPLAY_NAME
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|HEADER_METRIC_NAME
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|HEADER_METRIC_TAGS
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|HEADER_METRIC_TYPE
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|HEADER_METRIC_UNIT
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|HEADER_PREFIX
import|;
end_import

begin_class
DECL|class|AbstractMicroProfileMetricsProducer
specifier|public
specifier|abstract
class|class
name|AbstractMicroProfileMetricsProducer
parameter_list|<
name|T
extends|extends
name|Metric
parameter_list|>
extends|extends
name|DefaultProducer
block|{
DECL|field|HEADER_PATTERN
specifier|private
specifier|static
specifier|final
name|String
name|HEADER_PATTERN
init|=
name|HEADER_PREFIX
operator|+
literal|"*"
decl_stmt|;
DECL|method|AbstractMicroProfileMetricsProducer (Endpoint endpoint)
specifier|public
name|AbstractMicroProfileMetricsProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|MicroProfileMetricsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|MicroProfileMetricsEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|MicroProfileMetricsEndpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|metricName
init|=
name|getStringHeader
argument_list|(
name|in
argument_list|,
name|HEADER_METRIC_NAME
argument_list|,
name|endpoint
operator|.
name|getMetricName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|metricDescription
init|=
name|getStringHeader
argument_list|(
name|in
argument_list|,
name|HEADER_METRIC_DESCRIPTION
argument_list|,
name|endpoint
operator|.
name|getDescription
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|metricDisplayName
init|=
name|getStringHeader
argument_list|(
name|in
argument_list|,
name|HEADER_METRIC_DISPLAY_NAME
argument_list|,
name|endpoint
operator|.
name|getDisplayName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|metricUnit
init|=
name|getStringHeader
argument_list|(
name|in
argument_list|,
name|HEADER_METRIC_UNIT
argument_list|,
name|endpoint
operator|.
name|getMetricUnit
argument_list|()
argument_list|)
decl_stmt|;
name|MetricType
name|metricType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HEADER_METRIC_TYPE
argument_list|,
name|endpoint
operator|.
name|getMetricType
argument_list|()
argument_list|,
name|MetricType
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Tag
argument_list|>
name|defaultTags
init|=
name|endpoint
operator|.
name|getTags
argument_list|()
decl_stmt|;
name|String
name|headerTags
init|=
name|getStringHeader
argument_list|(
name|in
argument_list|,
name|HEADER_METRIC_TAGS
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Tag
argument_list|>
name|allTags
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|allTags
operator|.
name|addAll
argument_list|(
name|defaultTags
argument_list|)
expr_stmt|;
name|allTags
operator|.
name|addAll
argument_list|(
name|MicroProfileMetricsHelper
operator|.
name|getMetricsTag
argument_list|(
name|headerTags
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Tag
argument_list|>
name|finalTags
init|=
name|allTags
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|tag
lambda|->
name|MicroProfileMetricsHelper
operator|.
name|parseTag
argument_list|(
name|tag
operator|.
name|getTagName
argument_list|()
operator|+
literal|"="
operator|+
name|tag
operator|.
name|getTagValue
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|finalTags
operator|.
name|add
argument_list|(
name|MicroProfileMetricsHelper
operator|.
name|parseTag
argument_list|(
name|CAMEL_CONTEXT_TAG
operator|+
literal|"="
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|MetadataBuilder
name|builder
init|=
operator|new
name|MetadataBuilder
argument_list|()
operator|.
name|withName
argument_list|(
name|metricName
argument_list|)
operator|.
name|withType
argument_list|(
name|metricType
argument_list|)
decl_stmt|;
if|if
condition|(
name|metricDescription
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|withDescription
argument_list|(
name|metricDescription
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|metricDisplayName
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|withDisplayName
argument_list|(
name|metricDisplayName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|metricUnit
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|withUnit
argument_list|(
name|metricUnit
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|doProcess
argument_list|(
name|exchange
argument_list|,
name|builder
operator|.
name|build
argument_list|()
argument_list|,
name|finalTags
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|clearMetricsHeaders
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doProcess (Exchange exchange, Metadata metadata, List<Tag> tags)
specifier|protected
name|void
name|doProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Metadata
name|metadata
parameter_list|,
name|List
argument_list|<
name|Tag
argument_list|>
name|tags
parameter_list|)
block|{
name|doProcess
argument_list|(
name|exchange
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|getOrRegisterMetric
argument_list|(
name|metadata
argument_list|,
name|tags
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getOrRegisterMetric (Metadata metadata, List<Tag> tags)
specifier|protected
name|T
name|getOrRegisterMetric
parameter_list|(
name|Metadata
name|metadata
parameter_list|,
name|List
argument_list|<
name|Tag
argument_list|>
name|tags
parameter_list|)
block|{
name|MetricRegistry
name|metricRegistry
init|=
name|getEndpoint
argument_list|()
operator|.
name|getMetricRegistry
argument_list|()
decl_stmt|;
return|return
name|registerMetric
argument_list|(
name|metadata
argument_list|,
name|tags
argument_list|)
operator|.
name|apply
argument_list|(
name|metricRegistry
argument_list|)
return|;
block|}
DECL|method|getStringHeader (Message in, String header, String defaultValue)
specifier|protected
name|String
name|getStringHeader
parameter_list|(
name|Message
name|in
parameter_list|,
name|String
name|header
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|String
name|headerValue
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|header
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|headerValue
argument_list|)
condition|?
name|headerValue
else|:
name|defaultValue
return|;
block|}
DECL|method|getLongHeader (Message in, String header, Long defaultValue)
specifier|protected
name|Long
name|getLongHeader
parameter_list|(
name|Message
name|in
parameter_list|,
name|String
name|header
parameter_list|,
name|Long
name|defaultValue
parameter_list|)
block|{
return|return
name|in
operator|.
name|getHeader
argument_list|(
name|header
argument_list|,
name|defaultValue
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getBooleanHeader (Message in, String header, Boolean defaultValue)
specifier|protected
name|Boolean
name|getBooleanHeader
parameter_list|(
name|Message
name|in
parameter_list|,
name|String
name|header
parameter_list|,
name|Boolean
name|defaultValue
parameter_list|)
block|{
name|Boolean
name|headerValue
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|header
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|headerValue
argument_list|)
condition|?
name|headerValue
else|:
name|defaultValue
return|;
block|}
DECL|method|clearMetricsHeaders (Message in)
specifier|protected
name|void
name|clearMetricsHeaders
parameter_list|(
name|Message
name|in
parameter_list|)
block|{
name|in
operator|.
name|removeHeaders
argument_list|(
name|HEADER_PATTERN
argument_list|)
expr_stmt|;
block|}
DECL|method|doProcess (Exchange exchange, MicroProfileMetricsEndpoint endpoint, T meter)
specifier|protected
specifier|abstract
name|void
name|doProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|MicroProfileMetricsEndpoint
name|endpoint
parameter_list|,
name|T
name|meter
parameter_list|)
function_decl|;
DECL|method|registerMetric (Metadata metadata, List<Tag> tags)
specifier|protected
specifier|abstract
name|Function
argument_list|<
name|MetricRegistry
argument_list|,
name|T
argument_list|>
name|registerMetric
parameter_list|(
name|Metadata
name|metadata
parameter_list|,
name|List
argument_list|<
name|Tag
argument_list|>
name|tags
parameter_list|)
function_decl|;
block|}
end_class

end_unit

