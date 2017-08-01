begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow.releases.helsinki
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
operator|.
name|releases
operator|.
name|helsinki
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|HttpMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|Exchange
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
name|Message
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
name|servicenow
operator|.
name|AbstractServiceNowProcessor
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
name|servicenow
operator|.
name|ServiceNowEndpoint
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
name|servicenow
operator|.
name|ServiceNowParams
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|ServiceNowConstants
operator|.
name|ACTION_CREATE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|ServiceNowConstants
operator|.
name|ACTION_RETRIEVE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|ServiceNowConstants
operator|.
name|ACTION_SUBJECT_CART
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|ServiceNowConstants
operator|.
name|ACTION_SUBJECT_CHECKOUT_GUIDE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|ServiceNowConstants
operator|.
name|ACTION_SUBJECT_PRODUCER
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
operator|.
name|ServiceNowConstants
operator|.
name|ACTION_SUBJECT_SUBMIT_GUIDE
import|;
end_import

begin_class
DECL|class|HelsinkiServiceNowServiceCatalogItemsProcessor
class|class
name|HelsinkiServiceNowServiceCatalogItemsProcessor
extends|extends
name|AbstractServiceNowProcessor
block|{
DECL|method|HelsinkiServiceNowServiceCatalogItemsProcessor (ServiceNowEndpoint endpoint)
name|HelsinkiServiceNowServiceCatalogItemsProcessor
parameter_list|(
name|ServiceNowEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_RETRIEVE
argument_list|,
name|ACTION_SUBJECT_SUBMIT_GUIDE
argument_list|,
name|this
operator|::
name|submitItemGuide
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_RETRIEVE
argument_list|,
name|ACTION_SUBJECT_CHECKOUT_GUIDE
argument_list|,
name|this
operator|::
name|checkoutItemGuide
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_RETRIEVE
argument_list|,
name|this
operator|::
name|retrieveItems
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_CREATE
argument_list|,
name|ACTION_SUBJECT_CART
argument_list|,
name|this
operator|::
name|addItemToCart
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_CREATE
argument_list|,
name|ACTION_SUBJECT_PRODUCER
argument_list|,
name|this
operator|::
name|submitItemProducer
argument_list|)
expr_stmt|;
block|}
comment|/*      * This method retrieves a list of catalogs to which the user has access or      * a single one if sys_id is defined.      *      * Method:      * - GET      *      * URL Format:      * - /sn_sc/servicecatalog/items      * - /sn_sc/servicecatalog/items/{sys_id}      */
DECL|method|retrieveItems (Exchange exchange)
specifier|private
name|void
name|retrieveItems
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|responseModel
init|=
name|getResponseModel
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|sysId
init|=
name|getSysID
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|apiVersion
init|=
name|getApiVersion
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|sysId
argument_list|)
condition|?
name|client
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"sn_sc"
argument_list|)
operator|.
name|path
argument_list|(
name|apiVersion
argument_list|)
operator|.
name|path
argument_list|(
literal|"servicecatalog"
argument_list|)
operator|.
name|path
argument_list|(
literal|"items"
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_CATEGORY
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_TYPE
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_LIMIT
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_TEXT
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_OFFSET
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_CATALOG
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_VIEW
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|responseModel
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|)
else|:
name|client
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"sn_sc"
argument_list|)
operator|.
name|path
argument_list|(
name|apiVersion
argument_list|)
operator|.
name|path
argument_list|(
literal|"items"
argument_list|)
operator|.
name|path
argument_list|(
literal|"items"
argument_list|)
operator|.
name|path
argument_list|(
name|sysId
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_VIEW
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|responseModel
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|)
decl_stmt|;
name|setBodyAndHeaders
argument_list|(
name|in
argument_list|,
name|responseModel
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
comment|/*      * This method retrieves a list of items based on the needs described for an      * order guide.      *      * Method:      * - POST      *      * URL Format:      * - /sn_sc/servicecatalog/items/{sys_id}/submit_guide      */
DECL|method|submitItemGuide (Exchange exchange)
specifier|private
name|void
name|submitItemGuide
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|responseModel
init|=
name|getResponseModel
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|sysId
init|=
name|getSysID
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|apiVersion
init|=
name|getApiVersion
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"sn_sc"
argument_list|)
operator|.
name|path
argument_list|(
name|apiVersion
argument_list|)
operator|.
name|path
argument_list|(
literal|"servicecatalog"
argument_list|)
operator|.
name|path
argument_list|(
literal|"items"
argument_list|)
operator|.
name|path
argument_list|(
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sysId
argument_list|,
literal|"sysId"
argument_list|)
argument_list|)
operator|.
name|path
argument_list|(
literal|"submit_guide"
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_VIEW
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|responseModel
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|,
name|in
operator|.
name|getMandatoryBody
argument_list|()
argument_list|)
decl_stmt|;
name|setBodyAndHeaders
argument_list|(
name|in
argument_list|,
name|responseModel
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
comment|/*      * This method retrieves an array of contents requested for checkout.      *      * Method:      * - POST      *      * URL Format:      * - /sn_sc/servicecatalog/items/{sys_id}/checkout_guide      */
DECL|method|checkoutItemGuide (Exchange exchange)
specifier|private
name|void
name|checkoutItemGuide
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|responseModel
init|=
name|getResponseModel
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|sysId
init|=
name|getSysID
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|apiVersion
init|=
name|getApiVersion
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"sn_sc"
argument_list|)
operator|.
name|path
argument_list|(
name|apiVersion
argument_list|)
operator|.
name|path
argument_list|(
literal|"servicecatalog"
argument_list|)
operator|.
name|path
argument_list|(
literal|"items"
argument_list|)
operator|.
name|path
argument_list|(
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sysId
argument_list|,
literal|"sysId"
argument_list|)
argument_list|)
operator|.
name|path
argument_list|(
literal|"submit_guide"
argument_list|)
operator|.
name|query
argument_list|(
name|responseModel
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|,
name|in
operator|.
name|getMandatoryBody
argument_list|()
argument_list|)
decl_stmt|;
name|setBodyAndHeaders
argument_list|(
name|in
argument_list|,
name|responseModel
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
comment|/*      * This method adds an item to the cart of the current user.      *      * Method:      * - POST      *      * URL Format:      * - /sn_sc/servicecatalog/items/{sys_id}/add_to_cart      */
DECL|method|addItemToCart (Exchange exchange)
specifier|private
name|void
name|addItemToCart
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|responseModel
init|=
name|getResponseModel
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|sysId
init|=
name|getSysID
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|apiVersion
init|=
name|getApiVersion
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"sn_sc"
argument_list|)
operator|.
name|path
argument_list|(
name|apiVersion
argument_list|)
operator|.
name|path
argument_list|(
literal|"servicecatalog"
argument_list|)
operator|.
name|path
argument_list|(
literal|"items"
argument_list|)
operator|.
name|path
argument_list|(
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sysId
argument_list|,
literal|"sysId"
argument_list|)
argument_list|)
operator|.
name|path
argument_list|(
literal|"add_to_cart"
argument_list|)
operator|.
name|query
argument_list|(
name|responseModel
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|)
decl_stmt|;
name|setBodyAndHeaders
argument_list|(
name|in
argument_list|,
name|responseModel
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
comment|/*      * This method creates a record and returns the Table API relative path and      * redirect url to access the created record.      *      * Method:      * - POST      *      * URL Format:      * - /sn_sc/servicecatalog/items/{sys_id}/submit_producer      */
DECL|method|submitItemProducer (Exchange exchange)
specifier|private
name|void
name|submitItemProducer
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|responseModel
init|=
name|getResponseModel
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|sysId
init|=
name|getSysID
argument_list|(
name|in
argument_list|)
decl_stmt|;
specifier|final
name|String
name|apiVersion
init|=
name|getApiVersion
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"sn_sc"
argument_list|)
operator|.
name|path
argument_list|(
name|apiVersion
argument_list|)
operator|.
name|path
argument_list|(
literal|"servicecatalog"
argument_list|)
operator|.
name|path
argument_list|(
literal|"items"
argument_list|)
operator|.
name|path
argument_list|(
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sysId
argument_list|,
literal|"sysId"
argument_list|)
argument_list|)
operator|.
name|path
argument_list|(
literal|"submit_producer"
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_VIEW
argument_list|,
name|in
argument_list|)
operator|.
name|query
argument_list|(
name|responseModel
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|,
name|in
operator|.
name|getMandatoryBody
argument_list|()
argument_list|)
decl_stmt|;
name|setBodyAndHeaders
argument_list|(
name|in
argument_list|,
name|responseModel
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

