begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|Body
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
name|Headers
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
name|OutHeaders
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
name|util
operator|.
name|jndi
operator|.
name|JndiContext
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|BeanWithHeadersAndBodyInject3Test
specifier|public
class|class
name|BeanWithHeadersAndBodyInject3Test
extends|extends
name|ContextTestSupport
block|{
DECL|field|myBean
specifier|private
name|MyBean
name|myBean
init|=
operator|new
name|MyBean
argument_list|()
decl_stmt|;
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:myBean?method=doSomething"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:finish"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|testAnnotations ()
specifier|public
name|void
name|testAnnotations
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|end
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:finish"
argument_list|)
decl_stmt|;
name|end
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Test Input"
argument_list|)
expr_stmt|;
name|end
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|end
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello!"
argument_list|,
name|end
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiContext
name|answer
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
name|myBean
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|method|doSomething (@ody String body, @Headers Map headers, @OutHeaders Map outHeaders)
specifier|public
name|String
name|doSomething
parameter_list|(
annotation|@
name|Body
name|String
name|body
parameter_list|,
annotation|@
name|Headers
name|Map
name|headers
parameter_list|,
annotation|@
name|OutHeaders
name|Map
name|outHeaders
parameter_list|)
block|{
return|return
literal|"Hello!"
return|;
block|}
block|}
block|}
end_class

end_unit

