begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.zipkin.scribe
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|zipkin
operator|.
name|scribe
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
name|apache
operator|.
name|camel
operator|.
name|zipkin
operator|.
name|ZipkinTracer
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
comment|/**  * Integration test requires running Zipkin/Scribe running  *  *<p>The easiest way to run is locally:  *<pre>{@code  * curl -sSL https://zipkin.io/quickstart.sh | bash -s  * SCRIBE_ENABLED=true java -jar zipkin.jar  * }</pre>  *  *<p>This test has to be run with environment variables set. For example:  *<pre>{@code  * ZIPKIN_COLLECTOR_THRIFT_SERVICE_HOST=localhost  * ZIPKIN_COLLECTOR_THRIFT_SERVICE_PORT=9410  * }</pre>  */
end_comment

begin_class
DECL|class|ZipkinAutoConfigureScribe
specifier|public
class|class
name|ZipkinAutoConfigureScribe
extends|extends
name|CamelTestSupport
block|{
DECL|field|zipkin
specifier|private
name|ZipkinTracer
name|zipkin
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
comment|// we have one route as service
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
comment|// should auto configure as we have not setup a span reporter
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
literal|"Hello Cat"
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
name|log
argument_list|(
literal|"routing at ${routeId}"
argument_list|)
operator|.
name|delay
argument_list|(
name|simple
argument_list|(
literal|"${random(1000,2000)}"
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

