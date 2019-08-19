begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|EndpointInject
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
name|AvailablePortFinder
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
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|DataValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|Variant
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

begin_comment
comment|/**  * Unit tests for milo server component without using an actual connection  */
end_comment

begin_class
DECL|class|ServerLocalTest
specifier|public
class|class
name|ServerLocalTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|MILO_ITEM_1
specifier|private
specifier|static
specifier|final
name|String
name|MILO_ITEM_1
init|=
literal|"milo-server:myitem1"
decl_stmt|;
DECL|field|MOCK_TEST
specifier|private
specifier|static
specifier|final
name|String
name|MOCK_TEST
init|=
literal|"mock:test"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|MOCK_TEST
argument_list|)
DECL|field|testEndpoint
specifier|protected
name|MockEndpoint
name|testEndpoint
decl_stmt|;
annotation|@
name|Before
DECL|method|pickFreePort ()
specifier|public
name|void
name|pickFreePort
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|MiloServerComponent
name|component
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"milo-server"
argument_list|,
name|MiloServerComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|component
operator|.
name|setBindPort
argument_list|(
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|)
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
name|MILO_ITEM_1
argument_list|)
operator|.
name|to
argument_list|(
name|MOCK_TEST
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|shouldStartComponent ()
specifier|public
name|void
name|shouldStartComponent
parameter_list|()
block|{     }
annotation|@
name|Test
DECL|method|testAcceptVariantString ()
specifier|public
name|void
name|testAcceptVariantString
parameter_list|()
block|{
name|sendBody
argument_list|(
name|MILO_ITEM_1
argument_list|,
operator|new
name|Variant
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAcceptVariantDouble ()
specifier|public
name|void
name|testAcceptVariantDouble
parameter_list|()
block|{
name|sendBody
argument_list|(
name|MILO_ITEM_1
argument_list|,
operator|new
name|Variant
argument_list|(
literal|0.0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAcceptString ()
specifier|public
name|void
name|testAcceptString
parameter_list|()
block|{
name|sendBody
argument_list|(
name|MILO_ITEM_1
argument_list|,
literal|"Foo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAcceptDouble ()
specifier|public
name|void
name|testAcceptDouble
parameter_list|()
block|{
name|sendBody
argument_list|(
name|MILO_ITEM_1
argument_list|,
literal|0.0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAcceptDataValueString ()
specifier|public
name|void
name|testAcceptDataValueString
parameter_list|()
block|{
name|sendBody
argument_list|(
name|MILO_ITEM_1
argument_list|,
operator|new
name|DataValue
argument_list|(
operator|new
name|Variant
argument_list|(
literal|"Foo"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAcceptDataValueDouble ()
specifier|public
name|void
name|testAcceptDataValueDouble
parameter_list|()
block|{
name|sendBody
argument_list|(
name|MILO_ITEM_1
argument_list|,
operator|new
name|DataValue
argument_list|(
operator|new
name|Variant
argument_list|(
literal|0.0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

