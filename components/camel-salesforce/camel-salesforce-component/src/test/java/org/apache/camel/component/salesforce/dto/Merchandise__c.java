begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.dto
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

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"Merchandise__c"
argument_list|)
DECL|class|Merchandise__c
specifier|public
class|class
name|Merchandise__c
extends|extends
name|AbstractSObjectBase
block|{
DECL|field|Description__c
specifier|private
name|String
name|Description__c
decl_stmt|;
DECL|field|Price__c
specifier|private
name|Double
name|Price__c
decl_stmt|;
DECL|field|Total_Inventory__c
specifier|private
name|Double
name|Total_Inventory__c
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"Description__c"
argument_list|)
DECL|method|getDescription__c ()
specifier|public
name|String
name|getDescription__c
parameter_list|()
block|{
return|return
name|Description__c
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Description__c"
argument_list|)
DECL|method|setDescription__c (String description__c)
specifier|public
name|void
name|setDescription__c
parameter_list|(
name|String
name|description__c
parameter_list|)
block|{
name|Description__c
operator|=
name|description__c
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Price__c"
argument_list|)
DECL|method|getPrice__c ()
specifier|public
name|Double
name|getPrice__c
parameter_list|()
block|{
return|return
name|Price__c
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Price__c"
argument_list|)
DECL|method|setPrice__c (Double price__c)
specifier|public
name|void
name|setPrice__c
parameter_list|(
name|Double
name|price__c
parameter_list|)
block|{
name|Price__c
operator|=
name|price__c
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Total_Inventory__c"
argument_list|)
DECL|method|getTotal_Inventory__c ()
specifier|public
name|Double
name|getTotal_Inventory__c
parameter_list|()
block|{
return|return
name|Total_Inventory__c
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"Total_Inventory__c"
argument_list|)
DECL|method|setTotal_Inventory__c (Double total_Inventory__c)
specifier|public
name|void
name|setTotal_Inventory__c
parameter_list|(
name|Double
name|total_Inventory__c
parameter_list|)
block|{
name|Total_Inventory__c
operator|=
name|total_Inventory__c
expr_stmt|;
block|}
block|}
end_class

end_unit

