begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|config
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
name|spring
operator|.
name|SpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelProxyVoidAsInOnlyTest
specifier|public
class|class
name|CamelProxyVoidAsInOnlyTest
extends|extends
name|SpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/config/CamelProxyVoidAsInOnlyTest.xml"
argument_list|)
return|;
block|}
DECL|method|testVoidAsInOnlyCamelProxy ()
specifier|public
name|void
name|testVoidAsInOnlyCamelProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// this one should be InOnly and act as fire and forget
name|MyOtherProxySender
name|sender
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"myOtherSender"
argument_list|,
name|MyOtherProxySender
operator|.
name|class
argument_list|)
decl_stmt|;
name|sender
operator|.
name|greeting
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// so sending this message should arrive before the former message
name|template
operator|.
name|sendBody
argument_list|(
literal|"mock:result"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

