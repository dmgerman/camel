begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servletlistener
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servletlistener
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
name|SimpleRegistry
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

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_comment
comment|/**  * Our custom {@link CamelContextLifecycle} which allows us to enlist beans in the {@link JndiContext}  * so the Camel application can lookup the beans in the {@link org.apache.camel.spi.Registry}.  *<p/>  * We can of course also do other kind of custom logic as well.  */
end_comment

begin_class
DECL|class|MyLifecycle
specifier|public
class|class
name|MyLifecycle
implements|implements
name|CamelContextLifecycle
argument_list|<
name|SimpleRegistry
argument_list|>
block|{
annotation|@
name|Override
DECL|method|beforeStart (ServletCamelContext camelContext, SimpleRegistry registry)
specifier|public
name|void
name|beforeStart
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|SimpleRegistry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
comment|// enlist our bean(s) in the registry
name|registry
operator|.
name|put
argument_list|(
literal|"myBean"
argument_list|,
operator|new
name|HelloBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterStart (ServletCamelContext camelContext, SimpleRegistry registry)
specifier|public
name|void
name|afterStart
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|SimpleRegistry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|beforeStop (ServletCamelContext camelContext, SimpleRegistry registry)
specifier|public
name|void
name|beforeStop
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|SimpleRegistry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|afterStop (ServletCamelContext camelContext, SimpleRegistry registry)
specifier|public
name|void
name|afterStop
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|SimpleRegistry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
comment|// unbind our bean when Camel has been stopped
name|registry
operator|.
name|remove
argument_list|(
literal|"myBean"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

