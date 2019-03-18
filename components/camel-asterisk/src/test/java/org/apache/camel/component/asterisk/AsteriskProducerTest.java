begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.asterisk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|asterisk
package|;
end_package

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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
annotation|@
name|Ignore
argument_list|(
literal|"CAMEL-10321: Set host, username and password test asterisk producer."
argument_list|)
DECL|class|AsteriskProducerTest
specifier|public
class|class
name|AsteriskProducerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|hostname
specifier|private
name|String
name|hostname
init|=
literal|"192.168.0.254"
decl_stmt|;
DECL|field|username
specifier|private
name|String
name|username
init|=
literal|"username"
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
init|=
literal|"password"
decl_stmt|;
DECL|field|action
specifier|private
name|String
name|action
init|=
name|AsteriskAction
operator|.
name|EXTENSION_STATE
operator|.
name|name
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testSnmpProducer ()
specifier|public
name|void
name|testSnmpProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
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
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"asterisk://myVoIP?hostname="
operator|+
name|hostname
operator|+
literal|"&username="
operator|+
name|username
operator|+
literal|"&password="
operator|+
name|password
operator|+
literal|"&action="
operator|+
name|action
argument_list|)
operator|.
name|log
argument_list|(
literal|"${body}"
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

