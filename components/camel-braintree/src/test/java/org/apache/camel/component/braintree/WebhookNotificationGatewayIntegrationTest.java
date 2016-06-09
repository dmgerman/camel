begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.braintree
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|braintree
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|braintreegateway
operator|.
name|BraintreeGateway
import|;
end_import

begin_import
import|import
name|com
operator|.
name|braintreegateway
operator|.
name|WebhookNotification
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
name|builder
operator|.
name|RouteBuilder
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
name|braintree
operator|.
name|internal
operator|.
name|BraintreeApiCollection
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
name|braintree
operator|.
name|internal
operator|.
name|BraintreeConstants
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
name|braintree
operator|.
name|internal
operator|.
name|WebhookNotificationGatewayApiMethod
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

begin_class
DECL|class|WebhookNotificationGatewayIntegrationTest
specifier|public
class|class
name|WebhookNotificationGatewayIntegrationTest
extends|extends
name|AbstractBraintreeTestSupport
block|{
DECL|field|PATH_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PREFIX
init|=
name|BraintreeApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|WebhookNotificationGatewayApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testParse ()
specifier|public
name|void
name|testParse
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|BraintreeGateway
name|gateway
init|=
name|getGateway
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|notification
init|=
name|gateway
operator|.
name|webhookTesting
argument_list|()
operator|.
name|sampleNotification
argument_list|(
name|WebhookNotification
operator|.
name|Kind
operator|.
name|SUBSCRIPTION_WENT_PAST_DUE
argument_list|,
literal|"my_id"
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|BraintreeConstants
operator|.
name|PROPERTY_PREFIX
operator|+
literal|"signature"
argument_list|,
name|notification
operator|.
name|get
argument_list|(
literal|"bt_signature"
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|BraintreeConstants
operator|.
name|PROPERTY_PREFIX
operator|+
literal|"payload"
argument_list|,
name|notification
operator|.
name|get
argument_list|(
literal|"bt_payload"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|WebhookNotification
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://PARSE"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"parse result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"my_id"
argument_list|,
name|result
operator|.
name|getSubscription
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// test route for parse
name|from
argument_list|(
literal|"direct://PARSE"
argument_list|)
operator|.
name|to
argument_list|(
literal|"braintree://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/parse"
argument_list|)
expr_stmt|;
comment|// test route for verify
name|from
argument_list|(
literal|"direct://VERIFY"
argument_list|)
operator|.
name|to
argument_list|(
literal|"braintree://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/verify?inBody=challenge"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

