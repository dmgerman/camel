begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
package|;
end_package

begin_class
DECL|class|ServiceNowConstants
specifier|public
specifier|final
class|class
name|ServiceNowConstants
block|{
DECL|field|CAMEL_HEADER_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_HEADER_PREFIX
init|=
literal|"CamelServiceNow"
decl_stmt|;
DECL|field|RESOURCE
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE
init|=
literal|"CamelServiceNowResource"
decl_stmt|;
DECL|field|ACTION
specifier|public
specifier|static
specifier|final
name|String
name|ACTION
init|=
literal|"CamelServiceNowAction"
decl_stmt|;
DECL|field|ACTION_SUBJECT
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_SUBJECT
init|=
literal|"CamelServiceNowActionSubject"
decl_stmt|;
DECL|field|MODEL
specifier|public
specifier|static
specifier|final
name|String
name|MODEL
init|=
literal|"CamelServiceNowModel"
decl_stmt|;
DECL|field|REQUEST_MODEL
specifier|public
specifier|static
specifier|final
name|String
name|REQUEST_MODEL
init|=
literal|"CamelServiceNowRequestModel"
decl_stmt|;
DECL|field|RESPONSE_MODEL
specifier|public
specifier|static
specifier|final
name|String
name|RESPONSE_MODEL
init|=
literal|"CamelServiceNowResponseModel"
decl_stmt|;
DECL|field|OFFSET_NEXT
specifier|public
specifier|static
specifier|final
name|String
name|OFFSET_NEXT
init|=
literal|"CamelServiceNowOffsetNext"
decl_stmt|;
DECL|field|OFFSET_PREV
specifier|public
specifier|static
specifier|final
name|String
name|OFFSET_PREV
init|=
literal|"CamelServiceNowOffsetPrev"
decl_stmt|;
DECL|field|OFFSET_FIRST
specifier|public
specifier|static
specifier|final
name|String
name|OFFSET_FIRST
init|=
literal|"CamelServiceNowOffsetFirst"
decl_stmt|;
DECL|field|OFFSET_LAST
specifier|public
specifier|static
specifier|final
name|String
name|OFFSET_LAST
init|=
literal|"CamelServiceNowOffsetLast"
decl_stmt|;
DECL|field|CONTENT_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_TYPE
init|=
literal|"CamelServiceNowContentType"
decl_stmt|;
DECL|field|CONTENT_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_ENCODING
init|=
literal|"CamelServiceNowContentEncoding"
decl_stmt|;
DECL|field|CONTENT_META
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_META
init|=
literal|"CamelServiceNowContentMeta"
decl_stmt|;
DECL|field|RESPONSE_META
specifier|public
specifier|static
specifier|final
name|String
name|RESPONSE_META
init|=
literal|"CamelServiceNowResponseMeta"
decl_stmt|;
DECL|field|ATTACHMENT_META_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|ATTACHMENT_META_HEADER
init|=
literal|"X-Attachment-Metadata"
decl_stmt|;
DECL|field|RESOURCE_TABLE
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_TABLE
init|=
literal|"table"
decl_stmt|;
DECL|field|RESOURCE_AGGREGATE
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_AGGREGATE
init|=
literal|"aggregate"
decl_stmt|;
DECL|field|RESOURCE_IMPORT
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_IMPORT
init|=
literal|"import"
decl_stmt|;
DECL|field|RESOURCE_ATTACHMENT
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_ATTACHMENT
init|=
literal|"attachment"
decl_stmt|;
DECL|field|RESOURCE_SCORECARDS
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_SCORECARDS
init|=
literal|"scorecards"
decl_stmt|;
DECL|field|RESOURCE_MISC
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_MISC
init|=
literal|"misc"
decl_stmt|;
DECL|field|RESOURCE_SERVICE_CATALOG
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_SERVICE_CATALOG
init|=
literal|"service_catalog"
decl_stmt|;
DECL|field|RESOURCE_SERVICE_CATALOG_ITEMS
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_SERVICE_CATALOG_ITEMS
init|=
literal|"service_catalog_items"
decl_stmt|;
DECL|field|RESOURCE_SERVICE_CATALOG_CARTS
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_SERVICE_CATALOG_CARTS
init|=
literal|"service_catalog_cart"
decl_stmt|;
DECL|field|RESOURCE_SERVICE_CATALOG_CATEGORIES
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_SERVICE_CATALOG_CATEGORIES
init|=
literal|"service_catalog_categories"
decl_stmt|;
DECL|field|ACTION_RETRIEVE
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_RETRIEVE
init|=
literal|"retrieve"
decl_stmt|;
DECL|field|ACTION_CONTENT
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_CONTENT
init|=
literal|"content"
decl_stmt|;
DECL|field|ACTION_CREATE
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_CREATE
init|=
literal|"create"
decl_stmt|;
DECL|field|ACTION_MODIFY
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_MODIFY
init|=
literal|"modify"
decl_stmt|;
DECL|field|ACTION_DELETE
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_DELETE
init|=
literal|"delete"
decl_stmt|;
DECL|field|ACTION_UPDATE
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_UPDATE
init|=
literal|"update"
decl_stmt|;
DECL|field|ACTION_UPLOAD
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_UPLOAD
init|=
literal|"upload"
decl_stmt|;
DECL|field|ACTION_SUBJECT_CATEGORIES
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_SUBJECT_CATEGORIES
init|=
literal|"categories"
decl_stmt|;
DECL|field|ACTION_SUBJECT_CART
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_SUBJECT_CART
init|=
literal|"cart"
decl_stmt|;
DECL|field|ACTION_SUBJECT_PRODUCER
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_SUBJECT_PRODUCER
init|=
literal|"producer"
decl_stmt|;
DECL|field|ACTION_SUBJECT_GUIDE
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_SUBJECT_GUIDE
init|=
literal|"guide"
decl_stmt|;
DECL|field|ACTION_SUBJECT_SUBMIT_GUIDE
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_SUBJECT_SUBMIT_GUIDE
init|=
literal|"submit_guide"
decl_stmt|;
DECL|field|ACTION_SUBJECT_CHECKOUT_GUIDE
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_SUBJECT_CHECKOUT_GUIDE
init|=
literal|"checkout_guide"
decl_stmt|;
DECL|field|ACTION_SUBJECT_PERFORMANCE_ANALYTICS
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_SUBJECT_PERFORMANCE_ANALYTICS
init|=
literal|"performance_analytics"
decl_stmt|;
DECL|field|ACTION_SUBJECT_USER_ROLE_INHERITANCE
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_SUBJECT_USER_ROLE_INHERITANCE
init|=
literal|"user_role_inheritance"
decl_stmt|;
DECL|field|ACTION_SUBJECT_IDENTIFY_RECONCILE
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_SUBJECT_IDENTIFY_RECONCILE
init|=
literal|"identify_reconcile"
decl_stmt|;
DECL|field|ACTION_SUBJECT_DELIVERY_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_SUBJECT_DELIVERY_ADDRESS
init|=
literal|"delivery_address"
decl_stmt|;
DECL|field|ACTION_SUBJECT_CHECKOUT
specifier|public
specifier|static
specifier|final
name|String
name|ACTION_SUBJECT_CHECKOUT
init|=
literal|"checkout"
decl_stmt|;
DECL|field|LINK_NEXT
specifier|public
specifier|static
specifier|final
name|String
name|LINK_NEXT
init|=
literal|"next"
decl_stmt|;
DECL|field|LINK_PREV
specifier|public
specifier|static
specifier|final
name|String
name|LINK_PREV
init|=
literal|"prev"
decl_stmt|;
DECL|field|LINK_FIRST
specifier|public
specifier|static
specifier|final
name|String
name|LINK_FIRST
init|=
literal|"first"
decl_stmt|;
DECL|field|LINK_LAST
specifier|public
specifier|static
specifier|final
name|String
name|LINK_LAST
init|=
literal|"last"
decl_stmt|;
DECL|method|ServiceNowConstants ()
specifier|private
name|ServiceNowConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

