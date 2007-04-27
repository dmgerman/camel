begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|util
operator|.
name|CamelClient
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
name|mock
operator|.
name|MockEndpoint
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

begin_comment
comment|/**  * A useful base class which creates a {@link CamelContext} with some routes along with a {@link CamelClient}  * for use in the test case  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|ContextTestSupport
specifier|public
specifier|abstract
class|class
name|ContextTestSupport
extends|extends
name|TestSupport
block|{
DECL|field|context
specifier|protected
name|CamelContext
name|context
decl_stmt|;
DECL|field|client
specifier|protected
name|CamelClient
argument_list|<
name|Exchange
argument_list|>
name|client
decl_stmt|;
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
name|context
operator|=
name|createCamelContext
argument_list|()
expr_stmt|;
name|client
operator|=
operator|new
name|CamelClient
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|context
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
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
comment|// no routes added by default
block|}
block|}
return|;
block|}
DECL|method|resolveMandatoryEndpoint (String uri)
specifier|protected
name|Endpoint
name|resolveMandatoryEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|resolveMandatoryEndpoint
argument_list|(
name|context
argument_list|,
name|uri
argument_list|)
return|;
block|}
comment|/**      * Sends a message to the given endpoint URI with the body value      *      * @param endpointUri the URI of the endpoint to send to      * @param body the body for the message      */
DECL|method|send (String endpointUri, final Object body)
specifier|protected
name|void
name|send
parameter_list|(
name|String
name|endpointUri
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|)
block|{
name|client
operator|.
name|send
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|Processor
argument_list|<
name|Exchange
argument_list|>
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
name|body
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"testCase"
argument_list|,
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

