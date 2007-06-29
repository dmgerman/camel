begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbi
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|client
operator|.
name|ServiceMixClient
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jbi
operator|.
name|messaging
operator|.
name|MessageExchange
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jbi
operator|.
name|servicedesc
operator|.
name|ServiceEndpoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|JbiEndpointUsingNameUriIntegrationTest
specifier|public
class|class
name|JbiEndpointUsingNameUriIntegrationTest
extends|extends
name|NonJbiCamelEndpointsIntegrationTest
block|{
comment|/*     * @see TestCase#setUp()     */
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|suName
operator|=
literal|"su4"
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|configureExchange (ServiceMixClient client, MessageExchange exchange)
specifier|protected
name|void
name|configureExchange
parameter_list|(
name|ServiceMixClient
name|client
parameter_list|,
name|MessageExchange
name|exchange
parameter_list|)
block|{
name|ServiceEndpoint
name|endpoint
init|=
name|client
operator|.
name|getContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|CamelJbiEndpoint
operator|.
name|SERVICE_NAME
argument_list|,
literal|"cheese"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have a Camel endpoint exposed in JBI!"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

