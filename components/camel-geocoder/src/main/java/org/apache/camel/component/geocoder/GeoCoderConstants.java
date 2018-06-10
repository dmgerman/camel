begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.geocoder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|geocoder
package|;
end_package

begin_class
DECL|class|GeoCoderConstants
specifier|public
specifier|final
class|class
name|GeoCoderConstants
block|{
DECL|field|ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"CamelGeoCoderAddress"
decl_stmt|;
DECL|field|LATLNG
specifier|public
specifier|static
specifier|final
name|String
name|LATLNG
init|=
literal|"CamelGeoCoderLatlng"
decl_stmt|;
DECL|field|LAT
specifier|public
specifier|static
specifier|final
name|String
name|LAT
init|=
literal|"CamelGeoCoderLat"
decl_stmt|;
DECL|field|LNG
specifier|public
specifier|static
specifier|final
name|String
name|LNG
init|=
literal|"CamelGeoCoderLng"
decl_stmt|;
DECL|field|STATUS
specifier|public
specifier|static
specifier|final
name|String
name|STATUS
init|=
literal|"CamelGeoCoderStatus"
decl_stmt|;
DECL|field|REGION_CODE
specifier|public
specifier|static
specifier|final
name|String
name|REGION_CODE
init|=
literal|"CamelGeoCoderRegionCode"
decl_stmt|;
DECL|field|REGION_NAME
specifier|public
specifier|static
specifier|final
name|String
name|REGION_NAME
init|=
literal|"CamelGeoCoderRegionName"
decl_stmt|;
DECL|field|CITY
specifier|public
specifier|static
specifier|final
name|String
name|CITY
init|=
literal|"CamelGeoCoderCity"
decl_stmt|;
DECL|field|COUNTRY_LONG
specifier|public
specifier|static
specifier|final
name|String
name|COUNTRY_LONG
init|=
literal|"CamelGeoCoderCountryLong"
decl_stmt|;
DECL|field|COUNTRY_SHORT
specifier|public
specifier|static
specifier|final
name|String
name|COUNTRY_SHORT
init|=
literal|"CamelGeoCoderCountryShort"
decl_stmt|;
DECL|field|POSTAL_CODE
specifier|public
specifier|static
specifier|final
name|String
name|POSTAL_CODE
init|=
literal|"CamelGeoCoderPostalCode"
decl_stmt|;
DECL|method|GeoCoderConstants ()
specifier|private
name|GeoCoderConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

