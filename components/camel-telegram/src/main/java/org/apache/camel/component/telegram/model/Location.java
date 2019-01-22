begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
operator|.
name|model
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
name|annotation
operator|.
name|JsonIgnoreProperties
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
name|annotation
operator|.
name|JsonProperty
import|;
end_import

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|Location
specifier|public
class|class
name|Location
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"latitude"
argument_list|)
DECL|field|latitude
specifier|private
name|double
name|latitude
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"longitude"
argument_list|)
DECL|field|longitude
specifier|private
name|double
name|longitude
decl_stmt|;
DECL|method|getLatitude ()
specifier|public
name|double
name|getLatitude
parameter_list|()
block|{
return|return
name|latitude
return|;
block|}
DECL|method|setLatitude (float latitude)
specifier|public
name|void
name|setLatitude
parameter_list|(
name|float
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
name|double
name|getLongitude
parameter_list|()
block|{
return|return
name|longitude
return|;
block|}
DECL|method|setLongitude (float longitude)
specifier|public
name|void
name|setLongitude
parameter_list|(
name|float
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Location{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"longitude='"
argument_list|)
operator|.
name|append
argument_list|(
name|longitude
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", latitude="
argument_list|)
operator|.
name|append
argument_list|(
name|latitude
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

