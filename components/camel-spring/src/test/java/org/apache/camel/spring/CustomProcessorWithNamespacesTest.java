begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Exchange
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
name|Message
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
name|Processor
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
name|ProducerTemplate
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
name|Route
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
name|TestSupport
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
name|spring
operator|.
name|example
operator|.
name|MyProcessor
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|CustomProcessorWithNamespacesTest
specifier|public
class|class
name|CustomProcessorWithNamespacesTest
extends|extends
name|TestSupport
block|{
DECL|field|body
specifier|protected
name|String
name|body
init|=
literal|"<hello>world!</hello>"
decl_stmt|;
DECL|field|applicationContext
specifier|protected
name|AbstractXmlApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Test
DECL|method|testXMLRouteLoading ()
specifier|public
name|void
name|testXMLRouteLoading
parameter_list|()
throws|throws
name|Exception
block|{
name|applicationContext
operator|=
name|createApplicationContext
argument_list|()
expr_stmt|;
name|SpringCamelContext
name|context
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|SpringCamelContext
operator|.
name|class
argument_list|)
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertValidContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
comment|// now lets send a message
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|template
operator|.
name|start
argument_list|()
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"name"
argument_list|,
literal|"James"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
name|MyProcessor
name|myProcessor
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"myProcessor"
argument_list|,
name|MyProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|myProcessor
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should have received a single exchange: "
operator|+
name|list
argument_list|,
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertValidContext (SpringCamelContext context)
specifier|protected
name|void
name|assertValidContext
parameter_list|(
name|SpringCamelContext
name|context
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"No context found!"
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Route
argument_list|>
name|routes
init|=
name|context
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have some routes defined"
argument_list|,
name|routes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Number of routes defined"
argument_list|,
literal|1
argument_list|,
name|routes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Route
name|route
init|=
name|routes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Found route: "
operator|+
name|route
argument_list|)
expr_stmt|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/routingUsingProcessor.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

