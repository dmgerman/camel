begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.dozer.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|dozer
operator|.
name|model
package|;
end_package

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|Address
specifier|public
class|class
name|Address
block|{
DECL|field|zipCode
specifier|private
name|String
name|zipCode
decl_stmt|;
DECL|field|streetName
specifier|private
name|String
name|streetName
decl_stmt|;
DECL|method|getZipCode ()
specifier|public
name|String
name|getZipCode
parameter_list|()
block|{
return|return
name|zipCode
return|;
block|}
DECL|method|setZipCode (String zipCode)
specifier|public
name|void
name|setZipCode
parameter_list|(
name|String
name|zipCode
parameter_list|)
block|{
name|this
operator|.
name|zipCode
operator|=
name|zipCode
expr_stmt|;
block|}
DECL|method|getStreetName ()
specifier|public
name|String
name|getStreetName
parameter_list|()
block|{
return|return
name|streetName
return|;
block|}
DECL|method|setStreetName (String streetName)
specifier|public
name|void
name|setStreetName
parameter_list|(
name|String
name|streetName
parameter_list|)
block|{
name|this
operator|.
name|streetName
operator|=
name|streetName
expr_stmt|;
block|}
block|}
end_class

end_unit

