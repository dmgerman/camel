begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.weather
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|weather
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
name|support
operator|.
name|ScheduledPollConsumer
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
name|commons
operator|.
name|httpclient
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|GetMethod
import|;
end_import

begin_class
DECL|class|WeatherConsumer
specifier|public
class|class
name|WeatherConsumer
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|DEFAULT_CONSUMER_DELAY
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_CONSUMER_DELAY
init|=
literal|60
operator|*
literal|60
operator|*
literal|1000L
decl_stmt|;
DECL|field|query
specifier|private
specifier|final
name|String
name|query
decl_stmt|;
DECL|method|WeatherConsumer (WeatherEndpoint endpoint, Processor processor, String query)
specifier|public
name|WeatherConsumer
parameter_list|(
name|WeatherEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|query
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|WeatherEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|WeatherEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Going to execute the Weather query {}"
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|HttpClient
name|httpClient
init|=
operator|(
operator|(
name|WeatherComponent
operator|)
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|)
operator|.
name|getHttpClient
argument_list|()
decl_stmt|;
name|GetMethod
name|getMethod
init|=
operator|new
name|GetMethod
argument_list|(
name|query
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|status
init|=
name|httpClient
operator|.
name|executeMethod
argument_list|(
name|getMethod
argument_list|)
decl_stmt|;
if|if
condition|(
name|status
operator|!=
name|HttpStatus
operator|.
name|SC_OK
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"HTTP call for weather returned error status code {} - {} as a result with query: {}"
argument_list|,
name|status
argument_list|,
name|getMethod
operator|.
name|getStatusLine
argument_list|()
argument_list|,
name|query
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
name|String
name|weather
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|getMethod
operator|.
name|getResponseBodyAsStream
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Got back the Weather information {}"
argument_list|,
name|weather
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|weather
argument_list|)
condition|)
block|{
comment|// empty response
return|return
literal|0
return|;
block|}
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|String
name|header
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getHeaderName
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|header
argument_list|,
name|weather
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|weather
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|WeatherConstants
operator|.
name|WEATHER_QUERY
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
finally|finally
block|{
name|getMethod
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

