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
DECL|class|WeatherProducer
specifier|public
class|class
name|WeatherProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|query
specifier|private
specifier|final
name|String
name|query
decl_stmt|;
DECL|method|WeatherProducer (WeatherEndpoint endpoint, String query)
specifier|public
name|WeatherProducer
parameter_list|(
name|WeatherEndpoint
name|endpoint
parameter_list|,
name|String
name|query
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
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
name|String
name|q
init|=
name|query
decl_stmt|;
name|String
name|location
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|WeatherConstants
operator|.
name|WEATHER_LOCATION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|location
operator|!=
literal|null
condition|)
block|{
name|q
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQuery
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
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
name|method
init|=
operator|new
name|GetMethod
argument_list|(
name|q
argument_list|)
decl_stmt|;
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Going to execute the Weather query {}"
argument_list|,
name|q
argument_list|)
expr_stmt|;
name|int
name|statusCode
init|=
name|httpClient
operator|.
name|executeMethod
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|statusCode
operator|!=
name|HttpStatus
operator|.
name|SC_OK
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Got the invalid http status value '"
operator|+
name|method
operator|.
name|getStatusLine
argument_list|()
operator|+
literal|"' as the result of the query '"
operator|+
name|query
operator|+
literal|"'"
argument_list|)
throw|;
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
name|method
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
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Got the unexpected value '"
operator|+
name|weather
operator|+
literal|"' as the result of the query '"
operator|+
name|q
operator|+
literal|"'"
argument_list|)
throw|;
block|}
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
name|q
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|method
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

