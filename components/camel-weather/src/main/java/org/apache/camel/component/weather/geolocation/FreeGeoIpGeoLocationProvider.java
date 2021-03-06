begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.weather.geolocation
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
operator|.
name|geolocation
package|;
end_package

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
name|JsonNode
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|weather
operator|.
name|WeatherComponent
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
name|NameValuePair
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isEmpty
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
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_class
DECL|class|FreeGeoIpGeoLocationProvider
specifier|public
class|class
name|FreeGeoIpGeoLocationProvider
implements|implements
name|GeoLocationProvider
block|{
DECL|field|component
specifier|private
specifier|final
name|WeatherComponent
name|component
decl_stmt|;
DECL|method|FreeGeoIpGeoLocationProvider (WeatherComponent component, String accessKey)
specifier|public
name|FreeGeoIpGeoLocationProvider
parameter_list|(
name|WeatherComponent
name|component
parameter_list|,
name|String
name|accessKey
parameter_list|)
block|{
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCurrentGeoLocation ()
specifier|public
name|GeoLocation
name|getCurrentGeoLocation
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpClient
name|httpClient
init|=
name|component
operator|.
name|getHttpClient
argument_list|()
decl_stmt|;
if|if
condition|(
name|isEmpty
argument_list|(
name|component
operator|.
name|getGeolocationAccessKey
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The geolocation service requires a mandatory geolocationAccessKey"
argument_list|)
throw|;
block|}
if|if
condition|(
name|isEmpty
argument_list|(
name|component
operator|.
name|getGeolocationRequestHostIP
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The geolocation service requires a mandatory geolocationRequestHostIP"
argument_list|)
throw|;
block|}
name|GetMethod
name|getMethod
init|=
operator|new
name|GetMethod
argument_list|(
literal|"http://api.ipstack.com/"
operator|+
name|component
operator|.
name|getGeolocationRequestHostIP
argument_list|()
argument_list|)
decl_stmt|;
name|getMethod
operator|.
name|setQueryString
argument_list|(
operator|new
name|NameValuePair
index|[]
block|{
operator|new
name|NameValuePair
argument_list|(
literal|"access_key"
argument_list|,
name|component
operator|.
name|getGeolocationAccessKey
argument_list|()
argument_list|)
block|,
operator|new
name|NameValuePair
argument_list|(
literal|"legacy"
argument_list|,
literal|"1"
argument_list|)
block|,
operator|new
name|NameValuePair
argument_list|(
literal|"output"
argument_list|,
literal|"json"
argument_list|)
block|}
argument_list|)
expr_stmt|;
try|try
block|{
name|int
name|statusCode
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
literal|"Got the unexpected http-status '"
operator|+
name|getMethod
operator|.
name|getStatusLine
argument_list|()
operator|+
literal|"' for the geolocation"
argument_list|)
throw|;
block|}
name|String
name|geoLocation
init|=
name|component
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
if|if
condition|(
name|isEmpty
argument_list|(
name|geoLocation
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Got the unexpected value '"
operator|+
name|geoLocation
operator|+
literal|"' for the geolocation"
argument_list|)
throw|;
block|}
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|JsonNode
name|node
init|=
name|mapper
operator|.
name|readValue
argument_list|(
name|geoLocation
argument_list|,
name|JsonNode
operator|.
name|class
argument_list|)
decl_stmt|;
name|JsonNode
name|latitudeNode
init|=
name|notNull
argument_list|(
name|node
operator|.
name|get
argument_list|(
literal|"latitude"
argument_list|)
argument_list|,
literal|"latitude"
argument_list|)
decl_stmt|;
name|JsonNode
name|longitudeNode
init|=
name|notNull
argument_list|(
name|node
operator|.
name|get
argument_list|(
literal|"longitude"
argument_list|)
argument_list|,
literal|"longitude"
argument_list|)
decl_stmt|;
return|return
operator|new
name|GeoLocation
argument_list|(
name|longitudeNode
operator|.
name|asText
argument_list|()
argument_list|,
name|latitudeNode
operator|.
name|asText
argument_list|()
argument_list|)
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

