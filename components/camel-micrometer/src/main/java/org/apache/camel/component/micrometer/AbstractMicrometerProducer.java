begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|function
operator|.
name|Function
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
name|Expression
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
name|impl
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
name|language
operator|.
name|simple
operator|.
name|SimpleLanguage
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
name|micrometer
operator|.
name|MicrometerConstants
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
name|micrometer
operator|.
name|MicrometerConstants
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
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|HEADER_PREFIX
import|;
end_import

begin_class
DECL|class|AbstractMicrometerProducer
specifier|public
specifier|abstract
class|class
name|AbstractMicrometerProducer
parameter_list|<
name|T
extends|extends
name|Meter
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
DECL|method|AbstractMicrometerProducer (MicrometerEndpoint endpoint)
specifier|public
name|AbstractMicrometerProducer
parameter_list|(
name|MicrometerEndpoint
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
name|MicrometerEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|MicrometerEndpoint
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
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|defaultMetricsName
init|=
name|simple
argument_list|(
name|exchange
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getMetricsName
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|finalMetricsName
init|=
name|getStringHeader
argument_list|(
name|in
argument_list|,
name|HEADER_METRIC_NAME
argument_list|,
name|defaultMetricsName
argument_list|)
decl_stmt|;
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|defaultTags
init|=
name|getEndpoint
argument_list|()
operator|.
name|getTags
argument_list|()
decl_stmt|;
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|headerTags
init|=
name|getTagHeader
argument_list|(
name|in
argument_list|,
name|HEADER_METRIC_TAGS
argument_list|,
name|Tags
operator|.
name|empty
argument_list|()
argument_list|)
decl_stmt|;
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|finalTags
init|=
name|Tags
operator|.
name|concat
argument_list|(
name|defaultTags
argument_list|,
name|headerTags
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|tag
lambda|->
name|Tag
operator|.
name|of
argument_list|(
name|simple
argument_list|(
name|exchange
argument_list|,
name|tag
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|simple
argument_list|(
name|exchange
argument_list|,
name|tag
operator|.
name|getValue
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|reduce
argument_list|(
name|Tags
operator|.
name|empty
argument_list|()
argument_list|,
name|Tags
operator|::
name|and
argument_list|,
name|Tags
operator|::
name|and
argument_list|)
operator|.
name|and
argument_list|(
name|Tags
operator|.
name|of
argument_list|(
name|CAMEL_CONTEXT_TAG
argument_list|,
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
decl_stmt|;
try|try
block|{
name|doProcess
argument_list|(
name|exchange
argument_list|,
name|finalMetricsName
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
DECL|method|registrar (String name, Iterable<Tag> tags)
specifier|protected
specifier|abstract
name|Function
argument_list|<
name|MeterRegistry
argument_list|,
name|T
argument_list|>
name|registrar
parameter_list|(
name|String
name|name
parameter_list|,
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|tags
parameter_list|)
function_decl|;
DECL|method|doProcess (Exchange exchange, String name, Iterable<Tag> tags)
specifier|protected
name|void
name|doProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|name
parameter_list|,
name|Iterable
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
name|getOrRegisterMeter
argument_list|(
name|name
argument_list|,
name|tags
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getOrRegisterMeter (String name, Iterable<Tag> tags)
specifier|protected
name|T
name|getOrRegisterMeter
parameter_list|(
name|String
name|name
parameter_list|,
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|tags
parameter_list|)
block|{
name|MeterRegistry
name|registry
init|=
name|getEndpoint
argument_list|()
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
return|return
name|registrar
argument_list|(
name|name
argument_list|,
name|tags
argument_list|)
operator|.
name|apply
argument_list|(
name|registry
argument_list|)
return|;
block|}
DECL|method|doProcess (Exchange exchange, MicrometerEndpoint endpoint, T meter)
specifier|protected
specifier|abstract
name|void
name|doProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|MicrometerEndpoint
name|endpoint
parameter_list|,
name|T
name|meter
parameter_list|)
function_decl|;
DECL|method|simple (Exchange exchange, String expression, Class<C> clazz)
specifier|protected
parameter_list|<
name|C
parameter_list|>
name|C
name|simple
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|C
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
name|Expression
name|simple
init|=
name|SimpleLanguage
operator|.
name|simple
argument_list|(
name|expression
argument_list|)
decl_stmt|;
if|if
condition|(
name|simple
operator|!=
literal|null
condition|)
block|{
return|return
name|simple
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|clazz
argument_list|)
return|;
block|}
block|}
return|return
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|clazz
argument_list|,
name|expression
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
DECL|method|getDoubleHeader (Message in, String header, Double defaultValue)
specifier|protected
name|Double
name|getDoubleHeader
parameter_list|(
name|Message
name|in
parameter_list|,
name|String
name|header
parameter_list|,
name|Double
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
name|Double
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getTagHeader (Message in, String header, Iterable<Tag> defaultTags)
specifier|protected
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|getTagHeader
parameter_list|(
name|Message
name|in
parameter_list|,
name|String
name|header
parameter_list|,
name|Iterable
argument_list|<
name|Tag
argument_list|>
name|defaultTags
parameter_list|)
block|{
return|return
operator|(
name|Iterable
argument_list|<
name|Tag
argument_list|>
operator|)
name|in
operator|.
name|getHeader
argument_list|(
name|header
argument_list|,
name|defaultTags
argument_list|,
name|Iterable
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|clearMetricsHeaders (Message in)
specifier|protected
name|boolean
name|clearMetricsHeaders
parameter_list|(
name|Message
name|in
parameter_list|)
block|{
return|return
name|in
operator|.
name|removeHeaders
argument_list|(
name|HEADER_PATTERN
argument_list|)
return|;
block|}
block|}
end_class

end_unit

