begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
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
name|BindToRegistry
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
name|component
operator|.
name|seda
operator|.
name|BlockingQueueFactory
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
name|seda
operator|.
name|PriorityBlockingQueueFactory
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
name|seda
operator|.
name|SedaComponent
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
name|junit
operator|.
name|Assert
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
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_class
DECL|class|MainIoCAutowireTest
specifier|public
class|class
name|MainIoCAutowireTest
block|{
annotation|@
name|Test
DECL|method|autowireNonNullOnlyDisabledTest ()
specifier|public
name|void
name|autowireNonNullOnlyDisabledTest
parameter_list|()
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"seda"
argument_list|,
name|createSedaComponent
argument_list|()
argument_list|)
expr_stmt|;
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|main
operator|.
name|addConfigurationClass
argument_list|(
name|MyConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|main
operator|.
name|addRouteBuilder
argument_list|(
name|MyRouteBuilder
operator|.
name|class
argument_list|)
expr_stmt|;
name|main
operator|.
name|configure
argument_list|()
operator|.
name|setAutowireNonNullOnlyComponentProperties
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|main
operator|.
name|setPropertyPlaceholderLocations
argument_list|(
literal|"empty.properties"
argument_list|)
expr_stmt|;
name|main
operator|.
name|start
argument_list|()
expr_stmt|;
name|BlockingQueueFactory
name|qf
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"seda"
argument_list|,
name|SedaComponent
operator|.
name|class
argument_list|)
operator|.
name|getDefaultQueueFactory
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|qf
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|PriorityBlockingQueueFactory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|autowireNonNullOnlyEnabledTest ()
specifier|public
name|void
name|autowireNonNullOnlyEnabledTest
parameter_list|()
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"seda"
argument_list|,
name|createSedaComponent
argument_list|()
argument_list|)
expr_stmt|;
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|main
operator|.
name|addConfigurationClass
argument_list|(
name|MyConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|main
operator|.
name|addRouteBuilder
argument_list|(
name|MyRouteBuilder
operator|.
name|class
argument_list|)
expr_stmt|;
name|main
operator|.
name|configure
argument_list|()
operator|.
name|setAutowireNonNullOnlyComponentProperties
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|main
operator|.
name|setPropertyPlaceholderLocations
argument_list|(
literal|"empty.properties"
argument_list|)
expr_stmt|;
name|main
operator|.
name|start
argument_list|()
expr_stmt|;
name|BlockingQueueFactory
name|qf
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"seda"
argument_list|,
name|SedaComponent
operator|.
name|class
argument_list|)
operator|.
name|getDefaultQueueFactory
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|qf
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|MySedaBlockingQueueFactory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|MyConfiguration
specifier|public
specifier|static
class|class
name|MyConfiguration
block|{
annotation|@
name|BindToRegistry
DECL|method|queueFactory (CamelContext myCamel)
specifier|public
name|BlockingQueueFactory
name|queueFactory
parameter_list|(
name|CamelContext
name|myCamel
parameter_list|)
block|{
comment|// we can optionally include camel context as parameter
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|myCamel
argument_list|)
expr_stmt|;
return|return
operator|new
name|PriorityBlockingQueueFactory
argument_list|()
return|;
block|}
block|}
DECL|class|MyRouteBuilder
specifier|public
specifier|static
class|class
name|MyRouteBuilder
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createSedaComponent ()
specifier|public
specifier|static
name|SedaComponent
name|createSedaComponent
parameter_list|()
block|{
name|SedaComponent
name|seda
init|=
operator|new
name|SedaComponent
argument_list|()
decl_stmt|;
name|seda
operator|.
name|setDefaultQueueFactory
argument_list|(
operator|new
name|MySedaBlockingQueueFactory
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|seda
return|;
block|}
block|}
end_class

end_unit

