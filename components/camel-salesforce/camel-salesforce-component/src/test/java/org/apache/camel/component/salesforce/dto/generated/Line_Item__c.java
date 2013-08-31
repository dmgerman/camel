begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.dto.generated
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
name|dto
operator|.
name|generated
package|;
end_package

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|annotations
operator|.
name|XStreamAlias
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|AbstractSObjectBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonProperty
import|;
end_import

begin_comment
comment|//CHECKSTYLE:OFF
end_comment

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"Line_Item__c"
argument_list|)
DECL|class|Line_Item__c
specifier|public
class|class
name|Line_Item__c
extends|extends
name|AbstractSObjectBase
block|{
DECL|field|Unit_Price__c
specifier|private
name|Double
name|Unit_Price__c
decl_stmt|;
DECL|field|Units_Sold__c
specifier|private
name|Double
name|Units_Sold__c
decl_stmt|;
DECL|field|Merchandise__c
specifier|private
name|String
name|Merchandise__c
decl_stmt|;
DECL|field|Invoice_Statement__c
specifier|private
name|String
name|Invoice_Statement__c
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"Unit_Price__c"
argument_list|)
DECL|method|getUnit_Price__c ()
specifier|public
name|Double
name|getUnit_Price__c
parameter_list|()
block|{
return|return
name|Unit_Price__c
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Unit_Price__c"
argument_list|)
DECL|method|setUnit_Price__c (Double unit_Price__c)
specifier|public
name|void
name|setUnit_Price__c
parameter_list|(
name|Double
name|unit_Price__c
parameter_list|)
block|{
name|Unit_Price__c
operator|=
name|unit_Price__c
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Units_Sold__c"
argument_list|)
DECL|method|getUnits_Sold__c ()
specifier|public
name|Double
name|getUnits_Sold__c
parameter_list|()
block|{
return|return
name|Units_Sold__c
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Units_Sold__c"
argument_list|)
DECL|method|setUnits_Sold__c (Double units_Sold__c)
specifier|public
name|void
name|setUnits_Sold__c
parameter_list|(
name|Double
name|units_Sold__c
parameter_list|)
block|{
name|Units_Sold__c
operator|=
name|units_Sold__c
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Merchandise__c"
argument_list|)
DECL|method|getMerchandise__c ()
specifier|public
name|String
name|getMerchandise__c
parameter_list|()
block|{
return|return
name|Merchandise__c
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Merchandise__c"
argument_list|)
DECL|method|setMerchandise__c (String merchandise__c)
specifier|public
name|void
name|setMerchandise__c
parameter_list|(
name|String
name|merchandise__c
parameter_list|)
block|{
name|Merchandise__c
operator|=
name|merchandise__c
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Invoice_Statement__c"
argument_list|)
DECL|method|getInvoice_Statement__c ()
specifier|public
name|String
name|getInvoice_Statement__c
parameter_list|()
block|{
return|return
name|Invoice_Statement__c
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Invoice_Statement__c"
argument_list|)
DECL|method|setInvoice_Statement__c (String invoice_Statement__c)
specifier|public
name|void
name|setInvoice_Statement__c
parameter_list|(
name|String
name|invoice_Statement__c
parameter_list|)
block|{
name|Invoice_Statement__c
operator|=
name|invoice_Statement__c
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//CHECKSTYLE:ON
end_comment

end_unit

