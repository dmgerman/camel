begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Salesforce DTO for compound type GeoLocation.  */
end_comment

begin_class
DECL|class|GeoLocation
specifier|public
class|class
name|GeoLocation
extends|extends
name|AbstractDTOBase
block|{
DECL|field|latitude
specifier|private
name|Double
name|latitude
decl_stmt|;
DECL|field|longitude
specifier|private
name|Double
name|longitude
decl_stmt|;
DECL|method|getLatitude ()
specifier|public
name|Double
name|getLatitude
parameter_list|()
block|{
return|return
name|latitude
return|;
block|}
DECL|method|setLatitude (Double latitude)
specifier|public
name|void
name|setLatitude
parameter_list|(
name|Double
name|latitude
parameter_list|)
block|{
name|this
operator|.
name|latitude
operator|=
name|latitude
expr_stmt|;
block|}
DECL|method|getLongitude ()
specifier|public
name|Double
name|getLongitude
parameter_list|()
block|{
return|return
name|longitude
return|;
block|}
DECL|method|setLongitude (Double longitude)
specifier|public
name|void
name|setLongitude
parameter_list|(
name|Double
name|longitude
parameter_list|)
block|{
name|this
operator|.
name|longitude
operator|=
name|longitude
expr_stmt|;
block|}
block|}
end_class

end_unit

