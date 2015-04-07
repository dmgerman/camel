begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
package|;
end_package

begin_comment
comment|/**  * Salesforce DTO for Compound type urn:address.  */
end_comment

begin_class
DECL|class|Address
specifier|public
class|class
name|Address
extends|extends
name|GeoLocation
block|{
DECL|field|city
specifier|private
name|String
name|city
decl_stmt|;
DECL|field|country
specifier|private
name|String
name|country
decl_stmt|;
DECL|field|countryCode
specifier|private
name|String
name|countryCode
decl_stmt|;
DECL|field|postalCode
specifier|private
name|String
name|postalCode
decl_stmt|;
DECL|field|state
specifier|private
name|String
name|state
decl_stmt|;
DECL|field|stateCode
specifier|private
name|String
name|stateCode
decl_stmt|;
DECL|field|street
specifier|private
name|String
name|street
decl_stmt|;
DECL|method|getCity ()
specifier|public
name|String
name|getCity
parameter_list|()
block|{
return|return
name|city
return|;
block|}
DECL|method|setCity (String city)
specifier|public
name|void
name|setCity
parameter_list|(
name|String
name|city
parameter_list|)
block|{
name|this
operator|.
name|city
operator|=
name|city
expr_stmt|;
block|}
DECL|method|getCountry ()
specifier|public
name|String
name|getCountry
parameter_list|()
block|{
return|return
name|country
return|;
block|}
DECL|method|setCountry (String country)
specifier|public
name|void
name|setCountry
parameter_list|(
name|String
name|country
parameter_list|)
block|{
name|this
operator|.
name|country
operator|=
name|country
expr_stmt|;
block|}
DECL|method|getCountryCode ()
specifier|public
name|String
name|getCountryCode
parameter_list|()
block|{
return|return
name|countryCode
return|;
block|}
DECL|method|setCountryCode (String countryCode)
specifier|public
name|void
name|setCountryCode
parameter_list|(
name|String
name|countryCode
parameter_list|)
block|{
name|this
operator|.
name|countryCode
operator|=
name|countryCode
expr_stmt|;
block|}
DECL|method|getPostalCode ()
specifier|public
name|String
name|getPostalCode
parameter_list|()
block|{
return|return
name|postalCode
return|;
block|}
DECL|method|setPostalCode (String postalCode)
specifier|public
name|void
name|setPostalCode
parameter_list|(
name|String
name|postalCode
parameter_list|)
block|{
name|this
operator|.
name|postalCode
operator|=
name|postalCode
expr_stmt|;
block|}
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
DECL|method|setState (String state)
specifier|public
name|void
name|setState
parameter_list|(
name|String
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
DECL|method|getStateCode ()
specifier|public
name|String
name|getStateCode
parameter_list|()
block|{
return|return
name|stateCode
return|;
block|}
DECL|method|setStateCode (String stateCode)
specifier|public
name|void
name|setStateCode
parameter_list|(
name|String
name|stateCode
parameter_list|)
block|{
name|this
operator|.
name|stateCode
operator|=
name|stateCode
expr_stmt|;
block|}
DECL|method|getStreet ()
specifier|public
name|String
name|getStreet
parameter_list|()
block|{
return|return
name|street
return|;
block|}
DECL|method|setStreet (String street)
specifier|public
name|void
name|setStreet
parameter_list|(
name|String
name|street
parameter_list|)
block|{
name|this
operator|.
name|street
operator|=
name|street
expr_stmt|;
block|}
block|}
end_class

end_unit

