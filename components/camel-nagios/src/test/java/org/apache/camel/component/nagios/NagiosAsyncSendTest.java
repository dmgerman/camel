begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nagios
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nagios
package|;
end_package

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|NonBlockingNagiosPassiveCheckSender
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
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_class
DECL|class|NagiosAsyncSendTest
specifier|public
class|class
name|NagiosAsyncSendTest
extends|extends
name|NagiosTest
block|{
annotation|@
name|BeforeClass
DECL|method|setSender ()
specifier|public
specifier|static
name|void
name|setSender
parameter_list|()
block|{
name|nagiosPassiveCheckSender
operator|=
name|Mockito
operator|.
name|mock
argument_list|(
name|NonBlockingNagiosPassiveCheckSender
operator|.
name|class
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|uri
init|=
literal|"nagios:127.0.0.1:25664?password=secret&sendSync=false"
decl_stmt|;
name|NagiosComponent
name|nagiosComponent
init|=
operator|new
name|NagiosComponent
argument_list|()
decl_stmt|;
name|nagiosComponent
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|NagiosEndpoint
name|nagiosEndpoint
init|=
operator|(
name|NagiosEndpoint
operator|)
name|nagiosComponent
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|nagiosEndpoint
operator|.
name|setSender
argument_list|(
name|nagiosPassiveCheckSender
argument_list|)
expr_stmt|;
name|nagiosEndpoint
operator|.
name|createProducer
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|nagiosEndpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

