begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.properties
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|PropertiesComponentLoadPropertiesTest
specifier|public
class|class
name|PropertiesComponentLoadPropertiesTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testLoadProperties ()
specifier|public
name|void
name|testLoadProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|PropertiesComponent
name|pc
init|=
name|context
operator|.
name|getPropertiesComponent
argument_list|()
decl_stmt|;
name|Properties
name|prop
init|=
name|pc
operator|.
name|loadProperties
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|19
argument_list|,
name|prop
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{{cool.b}}"
argument_list|,
name|prop
operator|.
name|getProperty
argument_list|(
literal|"cool.a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"10"
argument_list|,
name|prop
operator|.
name|getProperty
argument_list|(
literal|"myQueueSize"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLoadPropertiesLocation ()
specifier|public
name|void
name|testLoadPropertiesLocation
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|PropertiesComponent
name|pc
init|=
name|context
operator|.
name|getPropertiesComponent
argument_list|()
decl_stmt|;
name|Properties
name|prop
init|=
name|pc
operator|.
name|loadProperties
argument_list|(
literal|"application.properties"
argument_list|,
literal|"example.properties"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|prop
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"World"
argument_list|,
name|prop
operator|.
name|getProperty
argument_list|(
literal|"hello"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2000"
argument_list|,
name|prop
operator|.
name|getProperty
argument_list|(
literal|"millisecs"
argument_list|)
argument_list|)
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
name|addComponent
argument_list|(
literal|"properties"
argument_list|,
operator|new
name|PropertiesComponent
argument_list|(
literal|"classpath:org/apache/camel/component/properties/myproperties.properties"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

