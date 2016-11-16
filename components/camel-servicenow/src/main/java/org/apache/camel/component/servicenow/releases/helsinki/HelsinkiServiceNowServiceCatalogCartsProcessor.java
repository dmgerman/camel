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
name|ACTION_DELETE
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
name|ACTION_SUBJECT_CHECKOUT
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
name|ACTION_SUBJECT_DELIVERY_ADDRESS
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
name|ACTION_UPDATE
import|;
end_import

begin_class
DECL|class|HelsinkiServiceNowServiceCatalogCartsProcessor
class|class
name|HelsinkiServiceNowServiceCatalogCartsProcessor
extends|extends
name|AbstractServiceNowProcessor
block|{
DECL|method|HelsinkiServiceNowServiceCatalogCartsProcessor (ServiceNowEndpoint endpoint)
name|HelsinkiServiceNowServiceCatalogCartsProcessor
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
name|ACTION_SUBJECT_DELIVERY_ADDRESS
argument_list|,
name|this
operator|::
name|retrieveDeliveryAddress
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_RETRIEVE
argument_list|,
name|ACTION_SUBJECT_CHECKOUT
argument_list|,
name|this
operator|::
name|retrieveCheckoutCart
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_RETRIEVE
argument_list|,
name|ACTION_SUBJECT_CHECKOUT
argument_list|,
name|this
operator|::
name|retrieveCarts
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_UPDATE
argument_list|,
name|ACTION_SUBJECT_CHECKOUT
argument_list|,
name|this
operator|::
name|checkoutCart
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_UPDATE
argument_list|,
name|this
operator|::
name|updateCart
argument_list|)
expr_stmt|;
name|addDispatcher
argument_list|(
name|ACTION_DELETE
argument_list|,
name|this
operator|::
name|deleteCart
argument_list|)
expr_stmt|;
block|}
comment|/*      * This method retrieves the default list of cart contents, cart details,      * and price shown on the two-step checkout page.      *      * Method:      * - GET      *      * URL Format:      * - /sn_sc/servicecatalog/cart      */
DECL|method|retrieveCarts (Exchange exchange)
specifier|private
name|void
name|retrieveCarts
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
literal|"servicecatalog"
argument_list|)
operator|.
name|path
argument_list|(
literal|"cart"
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
comment|/*      * This method retrieves the shipping address of the requested user.      *      * Method:      * - GET      *      * URL Format:      * - /sn_sc/servicecatalog/cart/delivery_address/{user_id}      */
DECL|method|retrieveDeliveryAddress (Exchange exchange)
specifier|private
name|void
name|retrieveDeliveryAddress
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
literal|"servicecatalog"
argument_list|)
operator|.
name|path
argument_list|(
literal|"cart"
argument_list|)
operator|.
name|path
argument_list|(
literal|"delivery_address"
argument_list|)
operator|.
name|path
argument_list|(
name|getMandatoryRequestParamFromHeader
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_USER_ID
argument_list|,
name|in
argument_list|)
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
comment|/*      * This method edits and updates any item in the cart.      *      * Method:      * - POST      *      * URL Format:      * - /sn_sc/servicecatalog/cart/{cart_item_id}      */
DECL|method|updateCart (Exchange exchange)
specifier|private
name|void
name|updateCart
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
literal|"servicecatalog"
argument_list|)
operator|.
name|path
argument_list|(
literal|"cart"
argument_list|)
operator|.
name|path
argument_list|(
name|getMandatoryRequestParamFromHeader
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_CART_ITEM_ID
argument_list|,
name|in
argument_list|)
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
comment|/*      * This method deletes the cart and contents of the cart for a given user      * role and sys_id.      *      * Method:      * - DELETE      *      * URL Format:      * - /sn_sc/servicecatalog/cart/{sys_id}/empty      */
DECL|method|deleteCart (Exchange exchange)
specifier|private
name|void
name|deleteCart
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
literal|"servicecatalog"
argument_list|)
operator|.
name|path
argument_list|(
literal|"cart"
argument_list|)
operator|.
name|path
argument_list|(
name|getMandatoryRequestParamFromHeader
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_SYS_ID
argument_list|,
name|in
argument_list|)
argument_list|)
operator|.
name|path
argument_list|(
literal|"empty"
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|DELETE
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
comment|/*      * This method retrieves the checkout cart details based on the two-step      * checkout process enabled or disabled. If the user enables two-step checkout,      * the method returns cart order status and all the information required for      * two-step checkout. If the user disables two-step checkout, the method      * checks out the cart and returns the request number and request order ID.      *      * Method:      * - POST      *      * URL Format:      * - /sn_sc/servicecatalog/cart/checkout      */
DECL|method|retrieveCheckoutCart (Exchange exchange)
specifier|private
name|void
name|retrieveCheckoutCart
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
literal|"servicecatalog"
argument_list|)
operator|.
name|path
argument_list|(
literal|"cart"
argument_list|)
operator|.
name|path
argument_list|(
literal|"checkout"
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
comment|/*      * This method checks out the user cart, whether two-step parameter is      * enabled or disabled.      *      * Method:      * - POST      *      * URL Format:      * - /sn_sc/servicecatalog/cart/submit_order      */
DECL|method|checkoutCart (Exchange exchange)
specifier|private
name|void
name|checkoutCart
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
literal|"servicecatalog"
argument_list|)
operator|.
name|path
argument_list|(
literal|"cart"
argument_list|)
operator|.
name|path
argument_list|(
literal|"submit_order"
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
block|}
end_class

end_unit

