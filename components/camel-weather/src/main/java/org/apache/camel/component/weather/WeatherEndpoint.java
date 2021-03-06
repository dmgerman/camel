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
name|Consumer
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
name|Producer
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
name|UriEndpoint
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
name|UriParam
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
name|DefaultPollingEndpoint
import|;
end_import

begin_comment
comment|/**  * Polls the weather information from Open Weather Map.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.12.0"
argument_list|,
name|scheme
operator|=
literal|"weather"
argument_list|,
name|title
operator|=
literal|"Weather"
argument_list|,
name|syntax
operator|=
literal|"weather:name"
argument_list|,
name|label
operator|=
literal|"api"
argument_list|)
DECL|class|WeatherEndpoint
specifier|public
class|class
name|WeatherEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|WeatherConfiguration
name|configuration
decl_stmt|;
DECL|method|WeatherEndpoint (String uri, WeatherComponent component, WeatherConfiguration properties)
specifier|public
name|WeatherEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|WeatherComponent
name|component
parameter_list|,
name|WeatherConfiguration
name|properties
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|properties
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|WeatherConsumer
name|answer
init|=
operator|new
name|WeatherConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|this
operator|.
name|configuration
operator|.
name|getQuery
argument_list|()
argument_list|)
decl_stmt|;
comment|// ScheduledPollConsumer default delay is 500 millis and that is too often for polling a feed, so we override
comment|// with a new default value. End user can override this value by providing a consumer.delay parameter
name|answer
operator|.
name|setDelay
argument_list|(
name|WeatherConsumer
operator|.
name|DEFAULT_CONSUMER_DELAY
argument_list|)
expr_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|WeatherProducer
argument_list|(
name|this
argument_list|,
name|configuration
operator|.
name|getQuery
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|WeatherConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
block|}
end_class

end_unit

