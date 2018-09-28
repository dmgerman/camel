begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.vm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|vm
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
name|ProducerTemplate
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
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|AbstractVmTestSupport
specifier|public
specifier|abstract
class|class
name|AbstractVmTestSupport
extends|extends
name|ContextTestSupport
block|{
DECL|field|context2
specifier|protected
name|CamelContext
name|context2
decl_stmt|;
DECL|field|template2
specifier|protected
name|ProducerTemplate
name|template2
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
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
name|context2
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|template2
operator|=
name|context2
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|template2
argument_list|,
name|context2
argument_list|)
expr_stmt|;
comment|// add routes after CamelContext has been started
name|RouteBuilder
name|routeBuilder
init|=
name|createRouteBuilderForSecondContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|routeBuilder
operator|!=
literal|null
condition|)
block|{
name|context2
operator|.
name|addRoutes
argument_list|(
name|routeBuilder
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|context2
argument_list|,
name|template2
argument_list|)
expr_stmt|;
name|VmComponent
operator|.
name|ENDPOINTS
operator|.
name|clear
argument_list|()
expr_stmt|;
name|VmComponent
operator|.
name|QUEUES
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilderForSecondContext ()
specifier|protected
name|RouteBuilder
name|createRouteBuilderForSecondContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

