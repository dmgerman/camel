begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonProperty
import|;
end_import

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

begin_comment
comment|//CHECKSTYLE:OFF
end_comment

begin_comment
comment|/**  * Salesforce DTO for SObject Asset  */
end_comment

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"Asset"
argument_list|)
DECL|class|Asset
specifier|public
class|class
name|Asset
extends|extends
name|AbstractSObjectBase
block|{
DECL|method|Asset ()
specifier|public
name|Asset
parameter_list|()
block|{
name|getAttributes
argument_list|()
operator|.
name|setType
argument_list|(
literal|"Asset"
argument_list|)
expr_stmt|;
block|}
comment|// ContactId
DECL|field|ContactId
specifier|private
name|String
name|ContactId
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"ContactId"
argument_list|)
DECL|method|getContactId ()
specifier|public
name|String
name|getContactId
parameter_list|()
block|{
return|return
name|this
operator|.
name|ContactId
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"ContactId"
argument_list|)
DECL|method|setContactId (String ContactId)
specifier|public
name|void
name|setContactId
parameter_list|(
name|String
name|ContactId
parameter_list|)
block|{
name|this
operator|.
name|ContactId
operator|=
name|ContactId
expr_stmt|;
block|}
comment|// AccountId
DECL|field|AccountId
specifier|private
name|String
name|AccountId
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"AccountId"
argument_list|)
DECL|method|getAccountId ()
specifier|public
name|String
name|getAccountId
parameter_list|()
block|{
return|return
name|this
operator|.
name|AccountId
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"AccountId"
argument_list|)
DECL|method|setAccountId (String AccountId)
specifier|public
name|void
name|setAccountId
parameter_list|(
name|String
name|AccountId
parameter_list|)
block|{
name|this
operator|.
name|AccountId
operator|=
name|AccountId
expr_stmt|;
block|}
comment|// Product2Id
DECL|field|Product2Id
specifier|private
name|String
name|Product2Id
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"Product2Id"
argument_list|)
DECL|method|getProduct2Id ()
specifier|public
name|String
name|getProduct2Id
parameter_list|()
block|{
return|return
name|this
operator|.
name|Product2Id
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Product2Id"
argument_list|)
DECL|method|setProduct2Id (String Product2Id)
specifier|public
name|void
name|setProduct2Id
parameter_list|(
name|String
name|Product2Id
parameter_list|)
block|{
name|this
operator|.
name|Product2Id
operator|=
name|Product2Id
expr_stmt|;
block|}
comment|// IsCompetitorProduct
DECL|field|IsCompetitorProduct
specifier|private
name|Boolean
name|IsCompetitorProduct
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"IsCompetitorProduct"
argument_list|)
DECL|method|getIsCompetitorProduct ()
specifier|public
name|Boolean
name|getIsCompetitorProduct
parameter_list|()
block|{
return|return
name|this
operator|.
name|IsCompetitorProduct
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"IsCompetitorProduct"
argument_list|)
DECL|method|setIsCompetitorProduct (Boolean IsCompetitorProduct)
specifier|public
name|void
name|setIsCompetitorProduct
parameter_list|(
name|Boolean
name|IsCompetitorProduct
parameter_list|)
block|{
name|this
operator|.
name|IsCompetitorProduct
operator|=
name|IsCompetitorProduct
expr_stmt|;
block|}
comment|// SerialNumber
DECL|field|SerialNumber
specifier|private
name|String
name|SerialNumber
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"SerialNumber"
argument_list|)
DECL|method|getSerialNumber ()
specifier|public
name|String
name|getSerialNumber
parameter_list|()
block|{
return|return
name|this
operator|.
name|SerialNumber
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"SerialNumber"
argument_list|)
DECL|method|setSerialNumber (String SerialNumber)
specifier|public
name|void
name|setSerialNumber
parameter_list|(
name|String
name|SerialNumber
parameter_list|)
block|{
name|this
operator|.
name|SerialNumber
operator|=
name|SerialNumber
expr_stmt|;
block|}
comment|// InstallDate
DECL|field|InstallDate
specifier|private
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
name|InstallDate
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"InstallDate"
argument_list|)
DECL|method|getInstallDate ()
specifier|public
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
name|getInstallDate
parameter_list|()
block|{
return|return
name|this
operator|.
name|InstallDate
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"InstallDate"
argument_list|)
DECL|method|setInstallDate (java.time.ZonedDateTime InstallDate)
specifier|public
name|void
name|setInstallDate
parameter_list|(
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
name|InstallDate
parameter_list|)
block|{
name|this
operator|.
name|InstallDate
operator|=
name|InstallDate
expr_stmt|;
block|}
comment|// PurchaseDate
DECL|field|PurchaseDate
specifier|private
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
name|PurchaseDate
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"PurchaseDate"
argument_list|)
DECL|method|getPurchaseDate ()
specifier|public
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
name|getPurchaseDate
parameter_list|()
block|{
return|return
name|this
operator|.
name|PurchaseDate
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"PurchaseDate"
argument_list|)
DECL|method|setPurchaseDate (java.time.ZonedDateTime PurchaseDate)
specifier|public
name|void
name|setPurchaseDate
parameter_list|(
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
name|PurchaseDate
parameter_list|)
block|{
name|this
operator|.
name|PurchaseDate
operator|=
name|PurchaseDate
expr_stmt|;
block|}
comment|// UsageEndDate
DECL|field|UsageEndDate
specifier|private
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
name|UsageEndDate
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"UsageEndDate"
argument_list|)
DECL|method|getUsageEndDate ()
specifier|public
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
name|getUsageEndDate
parameter_list|()
block|{
return|return
name|this
operator|.
name|UsageEndDate
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"UsageEndDate"
argument_list|)
DECL|method|setUsageEndDate (java.time.ZonedDateTime UsageEndDate)
specifier|public
name|void
name|setUsageEndDate
parameter_list|(
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
name|UsageEndDate
parameter_list|)
block|{
name|this
operator|.
name|UsageEndDate
operator|=
name|UsageEndDate
expr_stmt|;
block|}
comment|// Price
DECL|field|Price
specifier|private
name|Double
name|Price
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"Price"
argument_list|)
DECL|method|getPrice ()
specifier|public
name|Double
name|getPrice
parameter_list|()
block|{
return|return
name|this
operator|.
name|Price
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Price"
argument_list|)
DECL|method|setPrice (Double Price)
specifier|public
name|void
name|setPrice
parameter_list|(
name|Double
name|Price
parameter_list|)
block|{
name|this
operator|.
name|Price
operator|=
name|Price
expr_stmt|;
block|}
comment|// Quantity
DECL|field|Quantity
specifier|private
name|Double
name|Quantity
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"Quantity"
argument_list|)
DECL|method|getQuantity ()
specifier|public
name|Double
name|getQuantity
parameter_list|()
block|{
return|return
name|this
operator|.
name|Quantity
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Quantity"
argument_list|)
DECL|method|setQuantity (Double Quantity)
specifier|public
name|void
name|setQuantity
parameter_list|(
name|Double
name|Quantity
parameter_list|)
block|{
name|this
operator|.
name|Quantity
operator|=
name|Quantity
expr_stmt|;
block|}
comment|// Description
DECL|field|Description
specifier|private
name|String
name|Description
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"Description"
argument_list|)
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|this
operator|.
name|Description
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Description"
argument_list|)
DECL|method|setDescription (String Description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|Description
parameter_list|)
block|{
name|this
operator|.
name|Description
operator|=
name|Description
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//CHECKSTYLE:ON
end_comment

end_unit

