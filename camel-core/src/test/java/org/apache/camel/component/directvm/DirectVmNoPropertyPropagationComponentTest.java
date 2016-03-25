begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.directvm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|directvm
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|DirectVmNoPropertyPropagationComponentTest
specifier|public
class|class
name|DirectVmNoPropertyPropagationComponentTest
extends|extends
name|ContextTestSupport
block|{
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
name|DirectVmComponent
name|directvm
init|=
operator|new
name|DirectVmComponent
argument_list|()
decl_stmt|;
name|directvm
operator|.
name|setPropagateProperties
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"direct-vm"
argument_list|,
name|directvm
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|testPropertiesPropagatedOrNot ()
specifier|public
name|void
name|testPropertiesPropagatedOrNot
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct-vm:start.default"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
block|}
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
throws|throws
name|Exception
block|{
comment|// Starters.
name|from
argument_list|(
literal|"direct-vm:start.default"
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|"abc"
argument_list|,
name|constant
argument_list|(
literal|"def"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct-vm:foo.noprops"
argument_list|)
expr_stmt|;
comment|// Asserters.
name|from
argument_list|(
literal|"direct-vm:foo.noprops"
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
name|assertNull
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"abc"
argument_list|)
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

