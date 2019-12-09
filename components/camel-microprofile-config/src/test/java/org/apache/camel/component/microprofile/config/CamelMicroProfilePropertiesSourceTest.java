begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.microprofile.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|config
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|io
operator|.
name|smallrye
operator|.
name|config
operator|.
name|PropertiesConfigSource
import|;
end_import

begin_import
import|import
name|io
operator|.
name|smallrye
operator|.
name|config
operator|.
name|SmallRyeConfigBuilder
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
name|RoutesBuilder
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
name|spi
operator|.
name|PropertiesComponent
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
name|spi
operator|.
name|PropertiesSource
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
name|spi
operator|.
name|Registry
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
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|config
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|config
operator|.
name|spi
operator|.
name|ConfigProviderResolver
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
DECL|class|CamelMicroProfilePropertiesSourceTest
specifier|public
class|class
name|CamelMicroProfilePropertiesSourceTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|config
specifier|private
name|Config
name|config
decl_stmt|;
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
comment|// setup MPC
name|Properties
name|prop
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"start"
argument_list|,
literal|"direct:start"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"hi"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"my-mock"
argument_list|,
literal|"result"
argument_list|)
expr_stmt|;
comment|// create PMC config source and register it so we can use it for testing
specifier|final
name|PropertiesConfigSource
name|pcs
init|=
operator|new
name|PropertiesConfigSource
argument_list|(
name|prop
argument_list|,
literal|"my-smallrye-config"
argument_list|)
decl_stmt|;
name|config
operator|=
operator|new
name|SmallRyeConfigBuilder
argument_list|()
operator|.
name|withSources
argument_list|(
name|pcs
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|ConfigProviderResolver
operator|.
name|instance
argument_list|()
operator|.
name|registerConfig
argument_list|(
name|config
argument_list|,
name|CamelMicroProfilePropertiesSourceTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|createCamelContext
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|config
operator|!=
literal|null
condition|)
block|{
name|ConfigProviderResolver
operator|.
name|instance
argument_list|()
operator|.
name|releaseConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|bindToRegistry (Registry registry)
specifier|protected
name|void
name|bindToRegistry
parameter_list|(
name|Registry
name|registry
parameter_list|)
throws|throws
name|Exception
block|{
name|Properties
name|prop
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|prop
operator|.
name|put
argument_list|(
literal|"who"
argument_list|,
literal|"Camel"
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"ps"
argument_list|,
operator|new
name|PropertiesSource
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"ps"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|prop
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLoadAll ()
specifier|public
name|void
name|testLoadAll
parameter_list|()
throws|throws
name|Exception
block|{
name|PropertiesComponent
name|pc
init|=
name|context
operator|.
name|getPropertiesComponent
argument_list|()
decl_stmt|;
name|Properties
name|properties
init|=
name|pc
operator|.
name|loadProperties
argument_list|()
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"start"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"direct:start"
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"hi"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"World"
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"my-mock"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"result"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLoadFiltered ()
specifier|public
name|void
name|testLoadFiltered
parameter_list|()
throws|throws
name|Exception
block|{
name|PropertiesComponent
name|pc
init|=
name|context
operator|.
name|getPropertiesComponent
argument_list|()
decl_stmt|;
name|Properties
name|properties
init|=
name|pc
operator|.
name|loadProperties
argument_list|(
name|k
lambda|->
name|k
operator|.
name|length
argument_list|()
operator|>
literal|2
argument_list|)
decl_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|properties
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"start"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"direct:start"
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|properties
operator|.
name|get
argument_list|(
literal|"my-mock"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"result"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMicroProfileConfig ()
specifier|public
name|void
name|testMicroProfileConfig
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
literal|"Hello World from Camel"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|context
operator|.
name|resolvePropertyPlaceholders
argument_list|(
literal|"Hello {{hi}} from {{who}}"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
literal|"{{start}}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:{{my-mock}}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

