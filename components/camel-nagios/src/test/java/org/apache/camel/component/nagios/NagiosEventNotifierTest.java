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
name|MessagePayload
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|NagiosPassiveCheckSender
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|jsendnsca
operator|.
name|PassiveCheckSender
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
name|Before
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

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
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

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|*
import|;
end_import

begin_class
DECL|class|NagiosEventNotifierTest
specifier|public
class|class
name|NagiosEventNotifierTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|canRun
specifier|protected
name|boolean
name|canRun
decl_stmt|;
annotation|@
name|Mock
DECL|field|nagiosPassiveCheckSender
specifier|private
name|PassiveCheckSender
name|nagiosPassiveCheckSender
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|NagiosPassiveCheckSender
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Before
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|canRun
operator|=
literal|true
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|NagiosEventNotifier
name|notifier
init|=
operator|new
name|NagiosEventNotifier
argument_list|(
name|nagiosPassiveCheckSender
argument_list|)
decl_stmt|;
name|notifier
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|notifier
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setPort
argument_list|(
literal|25669
argument_list|)
expr_stmt|;
name|notifier
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
name|notifier
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testNagiosEventNotifierOk ()
specifier|public
name|void
name|testNagiosEventNotifierOk
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canRun
condition|)
block|{
return|return;
block|}
name|getMockEndpoint
argument_list|(
literal|"mock:ok"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:ok"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|nagiosPassiveCheckSender
argument_list|,
name|atLeast
argument_list|(
literal|11
argument_list|)
argument_list|)
operator|.
name|send
argument_list|(
name|any
argument_list|(
name|MessagePayload
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNagiosEventNotifierError ()
specifier|public
name|void
name|testNagiosEventNotifierError
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canRun
condition|)
block|{
return|return;
block|}
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:fail"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|nagiosPassiveCheckSender
argument_list|,
name|atLeast
argument_list|(
literal|9
argument_list|)
argument_list|)
operator|.
name|send
argument_list|(
name|any
argument_list|(
name|MessagePayload
operator|.
name|class
argument_list|)
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
name|from
argument_list|(
literal|"direct:ok"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:ok"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:fail"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

