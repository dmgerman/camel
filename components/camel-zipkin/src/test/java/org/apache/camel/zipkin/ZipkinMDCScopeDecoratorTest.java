begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.zipkin
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|zipkin
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
name|Ignore
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
name|slf4j
operator|.
name|MDC
import|;
end_import

begin_import
import|import
name|zipkin2
operator|.
name|reporter
operator|.
name|Reporter
import|;
end_import

begin_class
DECL|class|ZipkinMDCScopeDecoratorTest
specifier|public
class|class
name|ZipkinMDCScopeDecoratorTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|zipkin
specifier|private
name|ZipkinTracer
name|zipkin
decl_stmt|;
DECL|method|setSpanReporter (ZipkinTracer zipkin)
specifier|protected
name|void
name|setSpanReporter
parameter_list|(
name|ZipkinTracer
name|zipkin
parameter_list|)
block|{
name|zipkin
operator|.
name|setSpanReporter
argument_list|(
name|Reporter
operator|.
name|NOOP
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
name|zipkin
operator|=
operator|new
name|ZipkinTracer
argument_list|()
expr_stmt|;
comment|// we have 2 routes as services
name|zipkin
operator|.
name|addClientServiceMapping
argument_list|(
literal|"seda:cat"
argument_list|,
literal|"cat"
argument_list|)
expr_stmt|;
name|zipkin
operator|.
name|addServerServiceMapping
argument_list|(
literal|"seda:cat"
argument_list|,
literal|"cat"
argument_list|)
expr_stmt|;
comment|// capture message body as well
name|zipkin
operator|.
name|setIncludeMessageBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setSpanReporter
argument_list|(
name|zipkin
argument_list|)
expr_stmt|;
name|context
operator|.
name|setUseMDCLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// attaching ourself to CamelContext
name|zipkin
operator|.
name|init
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testZipkinRoute ()
specifier|public
name|void
name|testZipkinRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Camel say hello Cat"
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:cat"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:cat"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"cat"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Cat says hello Dog"
argument_list|)
operator|.
name|process
argument_list|(
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
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|MDC
operator|.
name|get
argument_list|(
literal|"traceId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|MDC
operator|.
name|get
argument_list|(
literal|"spanId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|MDC
operator|.
name|get
argument_list|(
literal|"parentId"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

