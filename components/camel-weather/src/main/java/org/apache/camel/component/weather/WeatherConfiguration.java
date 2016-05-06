begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|component
operator|.
name|weather
operator|.
name|WeatherUnits
operator|.
name|METRIC
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
literal|"true"
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
literal|"true"
argument_list|)
DECL|field|appid
specifier|private
name|String
name|appid
decl_stmt|;
annotation|@
name|UriParam
DECL|field|location
specifier|private
name|String
name|location
init|=
literal|""
decl_stmt|;
annotation|@
name|UriParam
DECL|field|lat
specifier|private
name|String
name|lat
decl_stmt|;
annotation|@
name|UriParam
DECL|field|lon
specifier|private
name|String
name|lon
decl_stmt|;
annotation|@
name|UriParam
DECL|field|rightLon
specifier|private
name|String
name|rightLon
decl_stmt|;
annotation|@
name|UriParam
DECL|field|topLat
specifier|private
name|String
name|topLat
decl_stmt|;
annotation|@
name|UriParam
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
argument_list|(
name|defaultValue
operator|=
literal|"METRIC"
argument_list|)
DECL|field|units
specifier|private
name|WeatherUnits
name|units
init|=
name|METRIC
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
comment|/**      * If null, the current weather will be returned, else use values of 5, 7, 14 days.      * Only the numeric value for the forecast period is actually parsed, so spelling, capitalisation of the time period is up to you (its ignored)      */
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
comment|/**      * If null Camel will try and determine your current location using the geolocation of your ip address,      * else specify the city,country. For well known city names, Open Weather Map will determine the best fit,      * but multiple results may be returned. Hence specifying and country as well will return more accurate data.      * If you specify "current" as the location then the component will try to get the current latitude and longitude      * and use that to get the weather details. You can use lat and lon options instead of location.      */
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
comment|/**      * To store the weather result in this header instead of the message body. This is useable if you want to keep current message body as-is.      */
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
comment|/**      * Latitude of location. You can use lat and lon options instead of location.      * For boxed queries this is the bottom latitude.      */
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
comment|/**      * Longitude of location. You can use lat and lon options instead of location.      * For boxed queries this is the left longtitude.      */
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
operator|new
name|WeatherQuery
argument_list|(
name|this
operator|.
name|component
argument_list|,
name|this
argument_list|)
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
operator|new
name|WeatherQuery
argument_list|(
name|this
operator|.
name|component
argument_list|,
name|this
argument_list|)
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
comment|/**      * For boxed queries this is the right longtitude. Needs to be used      * in combination with topLat and zoom.      */
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
comment|/**      * For boxed queries this is the top latitude. Needs to be used      * in combination with rightLon and zoom.      */
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
comment|/**      * For boxed queries this is the zoom. Needs to be used      * in combination with rightLon and topLat.      */
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
block|}
end_class

end_unit

