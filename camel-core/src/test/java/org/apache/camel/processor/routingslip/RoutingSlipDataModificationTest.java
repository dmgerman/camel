begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.routingslip
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|routingslip
package|;
end_package

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
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|Body
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
name|ContextTestSupport
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
name|Headers
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
name|OutHeaders
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
name|util
operator|.
name|jndi
operator|.
name|JndiContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_class
DECL|class|RoutingSlipDataModificationTest
specifier|public
class|class
name|RoutingSlipDataModificationTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|ANSWER
specifier|protected
specifier|static
specifier|final
name|String
name|ANSWER
init|=
literal|"answer"
decl_stmt|;
DECL|field|ROUTING_SLIP_HEADER
specifier|protected
specifier|static
specifier|final
name|String
name|ROUTING_SLIP_HEADER
init|=
literal|"routingSlipHeader"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|RoutingSlipDataModificationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|myBean
specifier|protected
name|MyBean
name|myBean
init|=
operator|new
name|MyBean
argument_list|()
decl_stmt|;
DECL|method|testModificationOfDataAlongRoute ()
specifier|public
name|void
name|testModificationOfDataAlongRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|x
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:x"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|y
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:y"
argument_list|)
decl_stmt|;
name|x
operator|.
name|expectedBodiesReceived
argument_list|(
name|ANSWER
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedBodiesReceived
argument_list|(
name|ANSWER
operator|+
name|ANSWER
argument_list|)
expr_stmt|;
name|sendBody
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendBody ()
specifier|protected
name|void
name|sendBody
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:a"
argument_list|,
name|ANSWER
argument_list|,
name|ROUTING_SLIP_HEADER
argument_list|,
literal|"mock:x,bean:myBean?method=modifyData"
argument_list|)
expr_stmt|;
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|Object
name|lookedUpBean
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"myBean"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"Lookup of 'myBean' should return same object!"
argument_list|,
name|myBean
argument_list|,
name|lookedUpBean
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiContext
name|answer
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
name|myBean
argument_list|)
expr_stmt|;
return|return
name|answer
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
comment|// START SNIPPET: example
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|routingSlip
argument_list|(
name|ROUTING_SLIP_HEADER
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:y"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
return|;
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|method|MyBean ()
specifier|public
name|MyBean
parameter_list|()
block|{         }
DECL|method|modifyData (@ody String body)
specifier|public
name|String
name|modifyData
parameter_list|(
annotation|@
name|Body
name|String
name|body
parameter_list|)
block|{
return|return
name|body
operator|+
name|body
return|;
block|}
block|}
block|}
end_class

end_unit

