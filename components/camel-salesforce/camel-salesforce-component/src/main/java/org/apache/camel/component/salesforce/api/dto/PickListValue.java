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
comment|/**  * Salesforce DTO for picklist value.  */
end_comment

begin_class
DECL|class|PickListValue
specifier|public
class|class
name|PickListValue
block|{
DECL|field|value
specifier|private
name|String
name|value
decl_stmt|;
DECL|field|label
specifier|private
name|String
name|label
decl_stmt|;
DECL|field|active
specifier|private
name|Boolean
name|active
decl_stmt|;
DECL|field|defaultValue
specifier|private
name|Boolean
name|defaultValue
decl_stmt|;
DECL|field|validFor
specifier|private
name|byte
index|[]
name|validFor
decl_stmt|;
DECL|method|getValue ()
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|setValue (String value)
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|label
return|;
block|}
DECL|method|setLabel (String label)
specifier|public
name|void
name|setLabel
parameter_list|(
name|String
name|label
parameter_list|)
block|{
name|this
operator|.
name|label
operator|=
name|label
expr_stmt|;
block|}
DECL|method|getActive ()
specifier|public
name|Boolean
name|getActive
parameter_list|()
block|{
return|return
name|active
return|;
block|}
DECL|method|setActive (Boolean active)
specifier|public
name|void
name|setActive
parameter_list|(
name|Boolean
name|active
parameter_list|)
block|{
name|this
operator|.
name|active
operator|=
name|active
expr_stmt|;
block|}
DECL|method|getDefaultValue ()
specifier|public
name|Boolean
name|getDefaultValue
parameter_list|()
block|{
return|return
name|defaultValue
return|;
block|}
DECL|method|setDefaultValue (Boolean defaultValue)
specifier|public
name|void
name|setDefaultValue
parameter_list|(
name|Boolean
name|defaultValue
parameter_list|)
block|{
name|this
operator|.
name|defaultValue
operator|=
name|defaultValue
expr_stmt|;
block|}
DECL|method|getValidFor ()
specifier|public
name|byte
index|[]
name|getValidFor
parameter_list|()
block|{
return|return
name|validFor
return|;
block|}
DECL|method|setValidFor (byte[] validFor)
specifier|public
name|void
name|setValidFor
parameter_list|(
name|byte
index|[]
name|validFor
parameter_list|)
block|{
name|this
operator|.
name|validFor
operator|=
name|validFor
expr_stmt|;
block|}
block|}
end_class

end_unit

