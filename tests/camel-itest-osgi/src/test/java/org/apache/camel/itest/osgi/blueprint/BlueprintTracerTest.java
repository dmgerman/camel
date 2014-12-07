begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|blueprint
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
name|processor
operator|.
name|interceptor
operator|.
name|DefaultTraceEventMessage
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
DECL|class|BlueprintTracerTest
specifier|public
class|class
name|BlueprintTracerTest
extends|extends
name|OSGiBlueprintTestSupport
block|{
DECL|field|name
specifier|protected
name|String
name|name
init|=
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testTracer ()
specifier|public
name|void
name|testTracer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// start bundle
name|getInstalledBundle
argument_list|(
name|name
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// must use the camel context from osgi
name|CamelContext
name|ctx
init|=
name|getOsgiService
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|"(camel.context.symbolicname="
operator|+
name|name
operator|+
literal|")"
argument_list|,
literal|30000
argument_list|)
decl_stmt|;
name|ProducerTemplate
name|myTemplate
init|=
name|ctx
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|myTemplate
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// do our testing
name|MockEndpoint
name|result
init|=
name|ctx
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|MockEndpoint
name|tracer
init|=
name|ctx
operator|.
name|getEndpoint
argument_list|(
literal|"mock:traced"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|myTemplate
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|DefaultTraceEventMessage
name|em
init|=
name|tracer
operator|.
name|getReceivedExchanges
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
argument_list|(
name|DefaultTraceEventMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|em
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"String"
argument_list|,
name|em
operator|.
name|getBodyType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|em
operator|.
name|getCausedByException
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|em
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|em
operator|.
name|getShortExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|em
operator|.
name|getExchangePattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|em
operator|.
name|getFromEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
comment|// there is always a breadcrumb header
name|assertNotNull
argument_list|(
name|em
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|em
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|em
operator|.
name|getOutBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|em
operator|.
name|getOutBodyType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|em
operator|.
name|getOutHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|em
operator|.
name|getPreviousNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|em
operator|.
name|getToNode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|em
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
name|myTemplate
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Configuration
DECL|method|configure ()
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|Option
index|[]
name|options
init|=
name|combine
argument_list|(
name|getDefaultCamelKarafOptions
argument_list|()
argument_list|,
name|bundle
argument_list|(
name|TinyBundles
operator|.
name|bundle
argument_list|()
operator|.
name|add
argument_list|(
literal|"OSGI-INF/blueprint/test.xml"
argument_list|,
name|BlueprintTracerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"blueprint-29.xml"
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
name|BlueprintTracerTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|,
literal|"1.0.0"
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|DYNAMICIMPORT_PACKAGE
argument_list|,
literal|"*"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|noStart
argument_list|()
argument_list|,
comment|// using the features to install the camel components
name|loadCamelFeatures
argument_list|(
literal|"camel-blueprint"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

