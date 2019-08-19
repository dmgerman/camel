begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.utils
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
name|utils
package|;
end_package

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
name|SObjectField
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
name|dto
operator|.
name|generated
operator|.
name|Account
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|QueryHelperTest
specifier|public
class|class
name|QueryHelperTest
block|{
annotation|@
name|Test
DECL|method|shouldFilterAndGatherAllFieldNames ()
specifier|public
name|void
name|shouldFilterAndGatherAllFieldNames
parameter_list|()
block|{
name|assertThat
argument_list|(
name|QueryHelper
operator|.
name|filteredFieldNamesOf
argument_list|(
operator|new
name|Account
argument_list|()
argument_list|,
name|SObjectField
operator|::
name|isCustom
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
literal|"External_Id__c"
argument_list|,
literal|"Shipping_Location__c"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldGatherAllFieldNames ()
specifier|public
name|void
name|shouldGatherAllFieldNames
parameter_list|()
block|{
name|assertThat
argument_list|(
name|QueryHelper
operator|.
name|fieldNamesOf
argument_list|(
operator|new
name|Account
argument_list|()
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
literal|"Id"
argument_list|,
literal|"Name"
argument_list|,
literal|"ShippingCity"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldGenerateQueryForAllFields ()
specifier|public
name|void
name|shouldGenerateQueryForAllFields
parameter_list|()
block|{
name|assertThat
argument_list|(
name|QueryHelper
operator|.
name|queryToFetchAllFieldsOf
argument_list|(
operator|new
name|Account
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"SELECT Id, IsDeleted, MasterRecordId, Name, Type, ParentId, BillingStreet, BillingCity, "
operator|+
literal|"BillingState, BillingPostalCode, BillingCountry, BillingLatitude, BillingLongitude, "
operator|+
literal|"BillingGeocodeAccuracy, BillingAddress, ShippingStreet, ShippingCity, ShippingState, "
operator|+
literal|"ShippingPostalCode, ShippingCountry, ShippingLatitude, ShippingLongitude, "
operator|+
literal|"ShippingGeocodeAccuracy, ShippingAddress, Phone, Fax, AccountNumber, Website, "
operator|+
literal|"PhotoUrl, Sic, Industry, AnnualRevenue, NumberOfEmployees, Ownership, TickerSymbol, "
operator|+
literal|"Description, Rating, Site, OwnerId, CreatedDate, CreatedById, LastModifiedDate, "
operator|+
literal|"LastModifiedById, SystemModstamp, LastActivityDate, LastViewedDate, LastReferencedDate, "
operator|+
literal|"Jigsaw, JigsawCompanyId, CleanStatus, AccountSource, DunsNumber, Tradestyle, NaicsCode, "
operator|+
literal|"NaicsDesc, YearStarted, SicDesc, DandbCompanyId, OperatingHoursId, Shipping_Location__Latitude__s, "
operator|+
literal|"Shipping_Location__Longitude__s, Shipping_Location__c, External_Id__c FROM Account"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldGenerateQueryForFilteredFields ()
specifier|public
name|void
name|shouldGenerateQueryForFilteredFields
parameter_list|()
block|{
name|String
name|s
init|=
name|QueryHelper
operator|.
name|queryToFetchFilteredFieldsOf
argument_list|(
operator|new
name|Account
argument_list|()
argument_list|,
name|SObjectField
operator|::
name|isCustom
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|QueryHelper
operator|.
name|queryToFetchFilteredFieldsOf
argument_list|(
operator|new
name|Account
argument_list|()
argument_list|,
name|SObjectField
operator|::
name|isCustom
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"SELECT Shipping_Location__Latitude__s, Shipping_Location__Longitude__s, Shipping_Location__c, External_Id__c FROM Account"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

