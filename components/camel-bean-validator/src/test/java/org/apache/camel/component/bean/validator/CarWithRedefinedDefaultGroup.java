begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|GroupSequence
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|constraints
operator|.
name|NotNull
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|constraints
operator|.
name|Size
import|;
end_import

begin_class
annotation|@
name|GroupSequence
argument_list|(
block|{
name|CarWithRedefinedDefaultGroup
operator|.
name|class
block|,
name|OptionalChecks
operator|.
name|class
block|}
argument_list|)
DECL|class|CarWithRedefinedDefaultGroup
specifier|public
class|class
name|CarWithRedefinedDefaultGroup
implements|implements
name|Car
block|{
annotation|@
name|NotNull
argument_list|(
name|groups
operator|=
name|RequiredChecks
operator|.
name|class
argument_list|)
DECL|field|manufacturer
specifier|private
name|String
name|manufacturer
decl_stmt|;
annotation|@
name|NotNull
argument_list|(
name|groups
operator|=
name|RequiredChecks
operator|.
name|class
argument_list|)
annotation|@
name|Size
argument_list|(
name|min
operator|=
literal|5
argument_list|,
name|max
operator|=
literal|14
argument_list|,
name|groups
operator|=
name|OptionalChecks
operator|.
name|class
argument_list|)
DECL|field|licensePlate
specifier|private
name|String
name|licensePlate
decl_stmt|;
DECL|method|CarWithRedefinedDefaultGroup (String manufacturer, String licencePlate)
specifier|public
name|CarWithRedefinedDefaultGroup
parameter_list|(
name|String
name|manufacturer
parameter_list|,
name|String
name|licencePlate
parameter_list|)
block|{
name|this
operator|.
name|manufacturer
operator|=
name|manufacturer
expr_stmt|;
name|this
operator|.
name|licensePlate
operator|=
name|licencePlate
expr_stmt|;
block|}
DECL|method|getManufacturer ()
specifier|public
name|String
name|getManufacturer
parameter_list|()
block|{
return|return
name|manufacturer
return|;
block|}
DECL|method|setManufacturer (String manufacturer)
specifier|public
name|void
name|setManufacturer
parameter_list|(
name|String
name|manufacturer
parameter_list|)
block|{
name|this
operator|.
name|manufacturer
operator|=
name|manufacturer
expr_stmt|;
block|}
DECL|method|getLicensePlate ()
specifier|public
name|String
name|getLicensePlate
parameter_list|()
block|{
return|return
name|licensePlate
return|;
block|}
DECL|method|setLicensePlate (String licensePlate)
specifier|public
name|void
name|setLicensePlate
parameter_list|(
name|String
name|licensePlate
parameter_list|)
block|{
name|this
operator|.
name|licensePlate
operator|=
name|licensePlate
expr_stmt|;
block|}
block|}
end_class

end_unit

