begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
operator|.
name|rest
package|;
end_package

begin_class
DECL|class|CountryPojo
specifier|public
class|class
name|CountryPojo
block|{
DECL|field|iso
specifier|private
name|String
name|iso
decl_stmt|;
DECL|field|country
specifier|private
name|String
name|country
decl_stmt|;
DECL|method|getIso ()
specifier|public
name|String
name|getIso
parameter_list|()
block|{
return|return
name|iso
return|;
block|}
DECL|method|setIso (String iso)
specifier|public
name|void
name|setIso
parameter_list|(
name|String
name|iso
parameter_list|)
block|{
name|this
operator|.
name|iso
operator|=
name|iso
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
block|}
end_class

end_unit

