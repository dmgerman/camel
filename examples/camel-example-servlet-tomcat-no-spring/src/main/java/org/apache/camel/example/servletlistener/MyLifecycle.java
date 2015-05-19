begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.servletlistener
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
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
name|component
operator|.
name|servletlistener
operator|.
name|CamelContextLifecycle
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
name|servletlistener
operator|.
name|ServletCamelContext
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
name|JndiRegistry
import|;
end_import

begin_class
DECL|class|MyLifecycle
specifier|public
class|class
name|MyLifecycle
implements|implements
name|CamelContextLifecycle
argument_list|<
name|JndiRegistry
argument_list|>
block|{
annotation|@
name|Override
DECL|method|beforeStart (ServletCamelContext camelContext, JndiRegistry registry)
specifier|public
name|void
name|beforeStart
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|JndiRegistry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
comment|// enlist our bean(s) in the registry
name|registry
operator|.
name|bind
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
DECL|method|beforeStop (ServletCamelContext camelContext, JndiRegistry registry)
specifier|public
name|void
name|beforeStop
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|JndiRegistry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|afterStop (ServletCamelContext camelContext, JndiRegistry registry)
specifier|public
name|void
name|afterStop
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|JndiRegistry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|beforeAddRoutes (ServletCamelContext camelContext, JndiRegistry registry)
specifier|public
name|void
name|beforeAddRoutes
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|JndiRegistry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|afterAddRoutes (ServletCamelContext camelContext, JndiRegistry registry)
specifier|public
name|void
name|afterAddRoutes
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|JndiRegistry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|afterStart (ServletCamelContext camelContext, JndiRegistry registry)
specifier|public
name|void
name|afterStart
parameter_list|(
name|ServletCamelContext
name|camelContext
parameter_list|,
name|JndiRegistry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

