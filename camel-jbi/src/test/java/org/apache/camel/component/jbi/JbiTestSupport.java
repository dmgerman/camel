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
name|camel
operator|.
name|CamelContext
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
name|Endpoint
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
name|TestSupport
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
name|Processor
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
name|ProducerCache
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|jbi
operator|.
name|container
operator|.
name|ActivationSpec
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|jbi
operator|.
name|container
operator|.
name|SpringJBIContainer
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JbiTestSupport
specifier|public
specifier|abstract
class|class
name|JbiTestSupport
extends|extends
name|TestSupport
block|{
DECL|field|receivedExchange
specifier|protected
name|Exchange
name|receivedExchange
decl_stmt|;
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|field|jbiContainer
specifier|protected
name|SpringJBIContainer
name|jbiContainer
init|=
operator|new
name|SpringJBIContainer
argument_list|()
decl_stmt|;
DECL|field|latch
specifier|protected
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|protected
name|Endpoint
argument_list|<
name|Exchange
argument_list|>
name|endpoint
decl_stmt|;
DECL|field|startEndpointUri
specifier|protected
name|String
name|startEndpointUri
init|=
literal|"jbi:endpoint:serviceNamespace:serviceA:endpointA"
decl_stmt|;
DECL|field|client
specifier|protected
name|ProducerCache
argument_list|<
name|Exchange
argument_list|>
name|client
init|=
operator|new
name|ProducerCache
argument_list|<
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Sends an exchange to the endpoint      */
DECL|method|sendExchange (final Object expectedBody)
specifier|protected
name|void
name|sendExchange
parameter_list|(
specifier|final
name|Object
name|expectedBody
parameter_list|)
block|{
name|client
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|assertReceivedValidExchange (Class type)
specifier|protected
name|Object
name|assertReceivedValidExchange
parameter_list|(
name|Class
name|type
parameter_list|)
throws|throws
name|Exception
block|{
comment|// lets wait on the message being received
name|boolean
name|received
init|=
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Did not receive the message!"
argument_list|,
name|received
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|receivedExchange
argument_list|)
expr_stmt|;
name|Message
name|receivedMessage
init|=
name|receivedExchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"cheese header"
argument_list|,
literal|123
argument_list|,
name|receivedMessage
operator|.
name|getHeader
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|body
init|=
name|receivedMessage
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Received body: "
operator|+
name|body
argument_list|)
expr_stmt|;
return|return
name|body
return|;
block|}
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
name|jbiContainer
operator|.
name|setEmbedded
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|CamelJbiComponent
name|component
init|=
operator|new
name|CamelJbiComponent
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ActivationSpec
argument_list|>
name|activationSpecList
init|=
operator|new
name|ArrayList
argument_list|<
name|ActivationSpec
argument_list|>
argument_list|()
decl_stmt|;
comment|// lets add the Camel endpoint
name|ActivationSpec
name|activationSpec
init|=
operator|new
name|ActivationSpec
argument_list|()
decl_stmt|;
name|activationSpec
operator|.
name|setId
argument_list|(
literal|"camel"
argument_list|)
expr_stmt|;
name|activationSpec
operator|.
name|setService
argument_list|(
operator|new
name|QName
argument_list|(
literal|"camel"
argument_list|,
literal|"camel"
argument_list|)
argument_list|)
expr_stmt|;
name|activationSpec
operator|.
name|setEndpoint
argument_list|(
literal|"camelEndpoint"
argument_list|)
expr_stmt|;
name|activationSpec
operator|.
name|setComponent
argument_list|(
name|component
argument_list|)
expr_stmt|;
name|activationSpecList
operator|.
name|add
argument_list|(
name|activationSpec
argument_list|)
expr_stmt|;
name|appendJbiActivationSpecs
argument_list|(
name|activationSpecList
argument_list|)
expr_stmt|;
name|ActivationSpec
index|[]
name|activationSpecs
init|=
name|activationSpecList
operator|.
name|toArray
argument_list|(
operator|new
name|ActivationSpec
index|[
name|activationSpecList
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|jbiContainer
operator|.
name|setActivationSpecs
argument_list|(
name|activationSpecs
argument_list|)
expr_stmt|;
name|jbiContainer
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
comment|// lets configure some componnets
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"jbi"
argument_list|,
name|component
argument_list|)
expr_stmt|;
comment|// lets add some routes
name|camelContext
operator|.
name|addRoutes
argument_list|(
name|createRoutes
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|startEndpointUri
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No endpoint found!"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|appendJbiActivationSpecs (List<ActivationSpec> activationSpecList)
specifier|protected
specifier|abstract
name|void
name|appendJbiActivationSpecs
parameter_list|(
name|List
argument_list|<
name|ActivationSpec
argument_list|>
name|activationSpecList
parameter_list|)
function_decl|;
DECL|method|createRoutes ()
specifier|protected
specifier|abstract
name|RouteBuilder
name|createRoutes
parameter_list|()
function_decl|;
block|}
end_class

end_unit

