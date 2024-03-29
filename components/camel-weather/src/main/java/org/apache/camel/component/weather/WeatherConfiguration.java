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
name|Iterator
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
name|Scanner
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
name|geolocation
operator|.
name|FreeGeoIpGeoLocationProvider
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
name|spi
operator|.
name|UriParams
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
name|UriPath
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
name|HttpConnectionManager
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
name|weather
operator|.
name|WeatherLanguage
operator|.
name|en
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
name|weather
operator|.
name|WeatherMode
operator|.
name|JSON
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
annotation|@
name|UriParams
DECL|class|WeatherConfiguration
specifier|public
class|class
name|WeatherConfiguration
block|{
DECL|field|component
specifier|private
specifier|final
name|WeatherComponent
name|component
decl_stmt|;
DECL|field|weatherQuery
specifier|private
specifier|final
name|WeatherQuery
name|weatherQuery
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The name value is not used."
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|appid
specifier|private
name|String
name|appid
decl_stmt|;
annotation|@
name|UriParam
DECL|field|weatherApi
specifier|private
name|WeatherApi
name|weatherApi
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"filter"
argument_list|)
DECL|field|location
specifier|private
name|String
name|location
init|=
literal|""
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"filter"
argument_list|)
DECL|field|lat
specifier|private
name|String
name|lat
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"filter"
argument_list|)
DECL|field|lon
specifier|private
name|String
name|lon
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"filter"
argument_list|)
DECL|field|rightLon
specifier|private
name|String
name|rightLon
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"filter"
argument_list|)
DECL|field|topLat
specifier|private
name|String
name|topLat
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"filter"
argument_list|)
DECL|field|zoom
specifier|private
name|Integer
name|zoom
decl_stmt|;
annotation|@
name|UriParam
DECL|field|period
specifier|private
name|String
name|period
init|=
literal|""
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"JSON"
argument_list|)
DECL|field|mode
specifier|private
name|WeatherMode
name|mode
init|=
name|JSON
decl_stmt|;
annotation|@
name|UriParam
DECL|field|units
specifier|private
name|WeatherUnits
name|units
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"en"
argument_list|)
DECL|field|language
specifier|private
name|WeatherLanguage
name|language
init|=
name|en
decl_stmt|;
annotation|@
name|UriParam
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"filter"
argument_list|)
DECL|field|zip
specifier|private
name|String
name|zip
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"filter"
argument_list|,
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|ids
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|ids
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"filter"
argument_list|)
DECL|field|cnt
specifier|private
name|Integer
name|cnt
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyAuthMethod
specifier|private
name|String
name|proxyAuthMethod
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|proxyAuthUsername
specifier|private
name|String
name|proxyAuthUsername
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|proxyAuthPassword
specifier|private
name|String
name|proxyAuthPassword
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyAuthDomain
specifier|private
name|String
name|proxyAuthDomain
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyAuthHost
specifier|private
name|String
name|proxyAuthHost
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|httpConnectionManager
specifier|private
name|HttpConnectionManager
name|httpConnectionManager
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|geolocationAccessKey
specifier|private
name|String
name|geolocationAccessKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|geolocationRequestHostIP
specifier|private
name|String
name|geolocationRequestHostIP
decl_stmt|;
DECL|method|WeatherConfiguration (WeatherComponent component)
specifier|public
name|WeatherConfiguration
parameter_list|(
name|WeatherComponent
name|component
parameter_list|)
block|{
name|this
operator|.
name|component
operator|=
name|notNull
argument_list|(
name|component
argument_list|,
literal|"component"
argument_list|)
expr_stmt|;
name|weatherQuery
operator|=
operator|new
name|WeatherQuery
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|FreeGeoIpGeoLocationProvider
name|geoLocationProvider
init|=
operator|new
name|FreeGeoIpGeoLocationProvider
argument_list|(
name|component
argument_list|,
name|geolocationAccessKey
argument_list|)
decl_stmt|;
name|weatherQuery
operator|.
name|setGeoLocationProvider
argument_list|(
name|geoLocationProvider
argument_list|)
expr_stmt|;
block|}
DECL|method|getPeriod ()
specifier|public
name|String
name|getPeriod
parameter_list|()
block|{
return|return
name|period
return|;
block|}
comment|/**      * If null, the current weather will be returned, else use values of 5, 7,      * 14 days. Only the numeric value for the forecast period is actually      * parsed, so spelling, capitalisation of the time period is up to you (its      * ignored)      */
DECL|method|setPeriod (String period)
specifier|public
name|void
name|setPeriod
parameter_list|(
name|String
name|period
parameter_list|)
block|{
name|notNull
argument_list|(
name|period
argument_list|,
literal|"period"
argument_list|)
expr_stmt|;
name|int
name|result
init|=
literal|0
decl_stmt|;
try|try
block|{
name|result
operator|=
operator|new
name|Scanner
argument_list|(
name|period
argument_list|)
operator|.
name|useDelimiter
argument_list|(
literal|"\\D+"
argument_list|)
operator|.
name|nextInt
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore and fallback the period to be an empty string
block|}
if|if
condition|(
name|result
operator|!=
literal|0
condition|)
block|{
name|this
operator|.
name|period
operator|=
literal|""
operator|+
name|result
expr_stmt|;
block|}
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getMode ()
specifier|public
name|WeatherMode
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
comment|/**      * The output format of the weather data.      */
DECL|method|setMode (WeatherMode mode)
specifier|public
name|void
name|setMode
parameter_list|(
name|WeatherMode
name|mode
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|notNull
argument_list|(
name|mode
argument_list|,
literal|"mode"
argument_list|)
expr_stmt|;
block|}
DECL|method|getUnits ()
specifier|public
name|WeatherUnits
name|getUnits
parameter_list|()
block|{
return|return
name|units
return|;
block|}
comment|/**      * The units for temperature measurement.      */
DECL|method|setUnits (WeatherUnits units)
specifier|public
name|void
name|setUnits
parameter_list|(
name|WeatherUnits
name|units
parameter_list|)
block|{
name|this
operator|.
name|units
operator|=
name|notNull
argument_list|(
name|units
argument_list|,
literal|"units"
argument_list|)
expr_stmt|;
block|}
DECL|method|getLocation ()
specifier|public
name|String
name|getLocation
parameter_list|()
block|{
return|return
name|location
return|;
block|}
comment|/**      * If null Camel will try and determine your current location using the      * geolocation of your ip address, else specify the city,country. For well      * known city names, Open Weather Map will determine the best fit, but      * multiple results may be returned. Hence specifying and country as well      * will return more accurate data. If you specify "current" as the location      * then the component will try to get the current latitude and longitude and      * use that to get the weather details. You can use lat and lon options      * instead of location.      */
DECL|method|setLocation (String location)
specifier|public
name|void
name|setLocation
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|this
operator|.
name|location
operator|=
name|location
expr_stmt|;
block|}
DECL|method|getHeaderName ()
specifier|public
name|String
name|getHeaderName
parameter_list|()
block|{
return|return
name|headerName
return|;
block|}
comment|/**      * To store the weather result in this header instead of the message body.      * This is useable if you want to keep current message body as-is.      */
DECL|method|setHeaderName (String headerName)
specifier|public
name|void
name|setHeaderName
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|this
operator|.
name|headerName
operator|=
name|headerName
expr_stmt|;
block|}
DECL|method|getLat ()
specifier|public
name|String
name|getLat
parameter_list|()
block|{
return|return
name|lat
return|;
block|}
comment|/**      * Latitude of location. You can use lat and lon options instead of      * location. For boxed queries this is the bottom latitude.      */
DECL|method|setLat (String lat)
specifier|public
name|void
name|setLat
parameter_list|(
name|String
name|lat
parameter_list|)
block|{
name|this
operator|.
name|lat
operator|=
name|lat
expr_stmt|;
block|}
DECL|method|getLon ()
specifier|public
name|String
name|getLon
parameter_list|()
block|{
return|return
name|lon
return|;
block|}
comment|/**      * Longitude of location. You can use lat and lon options instead of      * location. For boxed queries this is the left longtitude.      */
DECL|method|setLon (String lon)
specifier|public
name|void
name|setLon
parameter_list|(
name|String
name|lon
parameter_list|)
block|{
name|this
operator|.
name|lon
operator|=
name|lon
expr_stmt|;
block|}
comment|/**      * APPID ID used to authenticate the user connected to the API Server      */
DECL|method|setAppid (String appid)
specifier|public
name|void
name|setAppid
parameter_list|(
name|String
name|appid
parameter_list|)
block|{
name|this
operator|.
name|appid
operator|=
name|appid
expr_stmt|;
block|}
DECL|method|getAppid ()
specifier|public
name|String
name|getAppid
parameter_list|()
block|{
return|return
name|appid
return|;
block|}
DECL|method|getQuery ()
name|String
name|getQuery
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|weatherQuery
operator|.
name|getQuery
argument_list|()
return|;
block|}
DECL|method|getQuery (String location)
name|String
name|getQuery
parameter_list|(
name|String
name|location
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|weatherQuery
operator|.
name|getQuery
argument_list|(
name|location
argument_list|)
return|;
block|}
DECL|method|getLanguage ()
specifier|public
name|WeatherLanguage
name|getLanguage
parameter_list|()
block|{
return|return
name|language
return|;
block|}
comment|/**      * Language of the response.      */
DECL|method|setLanguage (WeatherLanguage language)
specifier|public
name|void
name|setLanguage
parameter_list|(
name|WeatherLanguage
name|language
parameter_list|)
block|{
name|this
operator|.
name|language
operator|=
name|language
expr_stmt|;
block|}
DECL|method|getRightLon ()
specifier|public
name|String
name|getRightLon
parameter_list|()
block|{
return|return
name|rightLon
return|;
block|}
comment|/**      * For boxed queries this is the right longtitude. Needs to be used in      * combination with topLat and zoom.      */
DECL|method|setRightLon (String rightLon)
specifier|public
name|void
name|setRightLon
parameter_list|(
name|String
name|rightLon
parameter_list|)
block|{
name|this
operator|.
name|rightLon
operator|=
name|rightLon
expr_stmt|;
block|}
DECL|method|getTopLat ()
specifier|public
name|String
name|getTopLat
parameter_list|()
block|{
return|return
name|topLat
return|;
block|}
comment|/**      * For boxed queries this is the top latitude. Needs to be used in      * combination with rightLon and zoom.      */
DECL|method|setTopLat (String topLat)
specifier|public
name|void
name|setTopLat
parameter_list|(
name|String
name|topLat
parameter_list|)
block|{
name|this
operator|.
name|topLat
operator|=
name|topLat
expr_stmt|;
block|}
DECL|method|getZoom ()
specifier|public
name|Integer
name|getZoom
parameter_list|()
block|{
return|return
name|zoom
return|;
block|}
comment|/**      * For boxed queries this is the zoom. Needs to be used in combination with      * rightLon and topLat.      */
DECL|method|setZoom (Integer zoom)
specifier|public
name|void
name|setZoom
parameter_list|(
name|Integer
name|zoom
parameter_list|)
block|{
name|this
operator|.
name|zoom
operator|=
name|zoom
expr_stmt|;
block|}
DECL|method|getHttpConnectionManager ()
specifier|public
name|HttpConnectionManager
name|getHttpConnectionManager
parameter_list|()
block|{
return|return
name|httpConnectionManager
return|;
block|}
comment|/**      * To use a custom HttpConnectionManager to manage connections      */
DECL|method|setHttpConnectionManager (HttpConnectionManager httpConnectionManager)
specifier|public
name|void
name|setHttpConnectionManager
parameter_list|(
name|HttpConnectionManager
name|httpConnectionManager
parameter_list|)
block|{
name|this
operator|.
name|httpConnectionManager
operator|=
name|httpConnectionManager
expr_stmt|;
block|}
DECL|method|getProxyHost ()
specifier|public
name|String
name|getProxyHost
parameter_list|()
block|{
return|return
name|proxyHost
return|;
block|}
comment|/**      * The proxy host name      */
DECL|method|setProxyHost (String proxyHost)
specifier|public
name|void
name|setProxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|this
operator|.
name|proxyHost
operator|=
name|proxyHost
expr_stmt|;
block|}
DECL|method|getProxyPort ()
specifier|public
name|Integer
name|getProxyPort
parameter_list|()
block|{
return|return
name|proxyPort
return|;
block|}
comment|/**      * The proxy port number      */
DECL|method|setProxyPort (Integer proxyPort)
specifier|public
name|void
name|setProxyPort
parameter_list|(
name|Integer
name|proxyPort
parameter_list|)
block|{
name|this
operator|.
name|proxyPort
operator|=
name|proxyPort
expr_stmt|;
block|}
DECL|method|getProxyAuthMethod ()
specifier|public
name|String
name|getProxyAuthMethod
parameter_list|()
block|{
return|return
name|proxyAuthMethod
return|;
block|}
comment|/**      * Authentication method for proxy, either as Basic, Digest or NTLM.      */
DECL|method|setProxyAuthMethod (String proxyAuthMethod)
specifier|public
name|void
name|setProxyAuthMethod
parameter_list|(
name|String
name|proxyAuthMethod
parameter_list|)
block|{
name|this
operator|.
name|proxyAuthMethod
operator|=
name|proxyAuthMethod
expr_stmt|;
block|}
DECL|method|getProxyAuthUsername ()
specifier|public
name|String
name|getProxyAuthUsername
parameter_list|()
block|{
return|return
name|proxyAuthUsername
return|;
block|}
comment|/**      * Username for proxy authentication      */
DECL|method|setProxyAuthUsername (String proxyAuthUsername)
specifier|public
name|void
name|setProxyAuthUsername
parameter_list|(
name|String
name|proxyAuthUsername
parameter_list|)
block|{
name|this
operator|.
name|proxyAuthUsername
operator|=
name|proxyAuthUsername
expr_stmt|;
block|}
DECL|method|getProxyAuthPassword ()
specifier|public
name|String
name|getProxyAuthPassword
parameter_list|()
block|{
return|return
name|proxyAuthPassword
return|;
block|}
comment|/**      * Password for proxy authentication      */
DECL|method|setProxyAuthPassword (String proxyAuthPassword)
specifier|public
name|void
name|setProxyAuthPassword
parameter_list|(
name|String
name|proxyAuthPassword
parameter_list|)
block|{
name|this
operator|.
name|proxyAuthPassword
operator|=
name|proxyAuthPassword
expr_stmt|;
block|}
DECL|method|getProxyAuthDomain ()
specifier|public
name|String
name|getProxyAuthDomain
parameter_list|()
block|{
return|return
name|proxyAuthDomain
return|;
block|}
comment|/**      * Domain for proxy NTLM authentication      */
DECL|method|setProxyAuthDomain (String proxyAuthDomain)
specifier|public
name|void
name|setProxyAuthDomain
parameter_list|(
name|String
name|proxyAuthDomain
parameter_list|)
block|{
name|this
operator|.
name|proxyAuthDomain
operator|=
name|proxyAuthDomain
expr_stmt|;
block|}
DECL|method|getProxyAuthHost ()
specifier|public
name|String
name|getProxyAuthHost
parameter_list|()
block|{
return|return
name|proxyAuthHost
return|;
block|}
comment|/**      * Optional host for proxy NTLM authentication      */
DECL|method|setProxyAuthHost (String proxyAuthHost)
specifier|public
name|void
name|setProxyAuthHost
parameter_list|(
name|String
name|proxyAuthHost
parameter_list|)
block|{
name|this
operator|.
name|proxyAuthHost
operator|=
name|proxyAuthHost
expr_stmt|;
block|}
DECL|method|getZip ()
specifier|public
name|String
name|getZip
parameter_list|()
block|{
return|return
name|zip
return|;
block|}
comment|/**      * Zip-code, e.g. 94040,us      */
DECL|method|setZip (String zip)
specifier|public
name|void
name|setZip
parameter_list|(
name|String
name|zip
parameter_list|)
block|{
name|this
operator|.
name|zip
operator|=
name|zip
expr_stmt|;
block|}
DECL|method|getIds ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIds
parameter_list|()
block|{
return|return
name|ids
return|;
block|}
comment|/**      * List of id's of city/stations. You can separate multiple ids by comma.      */
DECL|method|setIds (String id)
specifier|public
name|void
name|setIds
parameter_list|(
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|ids
operator|==
literal|null
condition|)
block|{
name|ids
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|id
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|myId
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|myId
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setIds (List<String> ids)
specifier|public
name|void
name|setIds
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|)
block|{
name|this
operator|.
name|ids
operator|=
name|ids
expr_stmt|;
block|}
DECL|method|getCnt ()
specifier|public
name|Integer
name|getCnt
parameter_list|()
block|{
return|return
name|cnt
return|;
block|}
comment|/**      * Number of results to be found      */
DECL|method|setCnt (Integer cnt)
specifier|public
name|void
name|setCnt
parameter_list|(
name|Integer
name|cnt
parameter_list|)
block|{
name|this
operator|.
name|cnt
operator|=
name|cnt
expr_stmt|;
block|}
DECL|method|getWeatherApi ()
specifier|public
name|WeatherApi
name|getWeatherApi
parameter_list|()
block|{
return|return
name|weatherApi
return|;
block|}
comment|/**      * The API to be use (current, forecast/3 hour, forecast daily, station)      */
DECL|method|setWeatherApi (WeatherApi weatherApi)
specifier|public
name|void
name|setWeatherApi
parameter_list|(
name|WeatherApi
name|weatherApi
parameter_list|)
block|{
name|this
operator|.
name|weatherApi
operator|=
name|weatherApi
expr_stmt|;
block|}
DECL|method|getGeolocationAccessKey ()
specifier|public
name|String
name|getGeolocationAccessKey
parameter_list|()
block|{
return|return
name|geolocationAccessKey
return|;
block|}
comment|/**      * The geolocation service now needs an accessKey to be used      */
DECL|method|setGeolocationAccessKey (String geolocationAccessKey)
specifier|public
name|void
name|setGeolocationAccessKey
parameter_list|(
name|String
name|geolocationAccessKey
parameter_list|)
block|{
name|this
operator|.
name|geolocationAccessKey
operator|=
name|geolocationAccessKey
expr_stmt|;
block|}
DECL|method|getGeolocationRequestHostIP ()
specifier|public
name|String
name|getGeolocationRequestHostIP
parameter_list|()
block|{
return|return
name|geolocationRequestHostIP
return|;
block|}
comment|/**      * The geolocation service now needs to specify the IP associated to the      * accessKey you're using      */
DECL|method|setGeolocationRequestHostIP (String geolocationRequestHostIP)
specifier|public
name|void
name|setGeolocationRequestHostIP
parameter_list|(
name|String
name|geolocationRequestHostIP
parameter_list|)
block|{
name|this
operator|.
name|geolocationRequestHostIP
operator|=
name|geolocationRequestHostIP
expr_stmt|;
block|}
block|}
end_class

end_unit

