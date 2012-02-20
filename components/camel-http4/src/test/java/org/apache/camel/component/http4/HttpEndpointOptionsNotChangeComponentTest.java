begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
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
name|impl
operator|.
name|JndiRegistry
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
name|Test
import|;
end_import

begin_comment
comment|/**  * Having custom endpoint options should not override or change any component configured options.  *  * @version   */
end_comment

begin_class
DECL|class|HttpEndpointOptionsNotChangeComponentTest
specifier|public
class|class
name|HttpEndpointOptionsNotChangeComponentTest
extends|extends
name|CamelTestSupport
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
name|HttpComponent
name|http
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|http
operator|.
name|setHttpBinding
argument_list|(
operator|new
name|MyBinding
argument_list|()
argument_list|)
expr_stmt|;
comment|// must start component
name|http
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"other"
argument_list|,
operator|new
name|MyOtherBinding
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testDoNotMessWithComponent ()
specifier|public
name|void
name|testDoNotMessWithComponent
parameter_list|()
throws|throws
name|Exception
block|{
comment|// get default
name|HttpEndpoint
name|end
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"http4://www.google.com"
argument_list|,
name|HttpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|MyBinding
operator|.
name|class
argument_list|,
name|end
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
comment|// use a endpoint specific binding
name|HttpEndpoint
name|end2
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"http4://www.google.com?httpBinding=#other"
argument_list|,
name|HttpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|MyOtherBinding
operator|.
name|class
argument_list|,
name|end2
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
comment|// and the default option has not been messed with
name|HttpEndpoint
name|end3
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"http4://www.google.com"
argument_list|,
name|HttpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|MyBinding
operator|.
name|class
argument_list|,
name|end3
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|class|MyBinding
specifier|private
specifier|static
class|class
name|MyBinding
extends|extends
name|DefaultHttpBinding
block|{     }
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|class|MyOtherBinding
specifier|private
specifier|static
class|class
name|MyOtherBinding
extends|extends
name|DefaultHttpBinding
block|{     }
block|}
end_class

end_unit

