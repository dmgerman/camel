begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|model
operator|.
name|Constants
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
name|MaskingFormatter
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
name|JndiTest
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

begin_class
DECL|class|LogEipMaskTest
specifier|public
class|class
name|LogEipMaskTest
block|{
DECL|field|registry
specifier|protected
name|JndiRegistry
name|registry
decl_stmt|;
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|registry
operator|=
operator|new
name|JndiRegistry
argument_list|(
name|JndiTest
operator|.
name|createInitialContext
argument_list|()
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testLogEipMask ()
specifier|public
name|void
name|testLogEipMask
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|createCamelContext
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:foo"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|setLogMask
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"mask password=\"my passw0rd!\""
argument_list|)
expr_stmt|;
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
literal|"direct:noMask"
argument_list|,
literal|"no-mask password=\"my passw0rd!\""
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCustomFormatter ()
specifier|public
name|void
name|testCustomFormatter
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|createCamelContext
argument_list|()
decl_stmt|;
name|MockMaskingFormatter
name|customFormatter
init|=
operator|new
name|MockMaskingFormatter
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
name|Constants
operator|.
name|CUSTOM_LOG_MASK_REF
argument_list|,
name|customFormatter
argument_list|)
expr_stmt|;
name|context
operator|.
name|setLogMask
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"mock password=\"my passw0rd!\""
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Got mock password=\"my passw0rd!\""
argument_list|,
name|customFormatter
operator|.
name|received
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|class|MockMaskingFormatter
specifier|public
specifier|static
class|class
name|MockMaskingFormatter
implements|implements
name|MaskingFormatter
block|{
DECL|field|received
specifier|private
name|String
name|received
decl_stmt|;
annotation|@
name|Override
DECL|method|format (String source)
specifier|public
name|String
name|format
parameter_list|(
name|String
name|source
parameter_list|)
block|{
name|received
operator|=
name|source
expr_stmt|;
return|return
name|source
return|;
block|}
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
literal|"direct:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Got ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:noMask"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"noMask"
argument_list|)
operator|.
name|logMask
argument_list|(
literal|"false"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Got ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:noMask"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

