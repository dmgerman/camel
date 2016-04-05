begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|scribe
operator|.
name|ScribeSpanCollector
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
name|NotifyBuilder
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
name|ZipkinLoggingSpanCollector
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
comment|/**  * Integration test requires running Zipkin/Scribe running  *  * The easiest way is to run using zipkin-docker: https://github.com/openzipkin/docker-zipkin  *  * Adjust the IP address to what IP docker-machines have assigned, you can use  *<tt>docker-machines ls</tt>  */
end_comment

begin_class
DECL|class|ZipkinMulticastRouteScribe
specifier|public
class|class
name|ZipkinMulticastRouteScribe
extends|extends
name|CamelTestSupport
block|{
DECL|field|ip
specifier|private
name|String
name|ip
init|=
literal|"192.168.99.100"
decl_stmt|;
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
name|zipkin
operator|.
name|addClientServiceMapping
argument_list|(
literal|"seda:a"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|zipkin
operator|.
name|addClientServiceMapping
argument_list|(
literal|"seda:b"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|zipkin
operator|.
name|addClientServiceMapping
argument_list|(
literal|"seda:c"
argument_list|,
literal|"c"
argument_list|)
expr_stmt|;
name|zipkin
operator|.
name|addServerServiceMapping
argument_list|(
literal|"seda:a"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|zipkin
operator|.
name|addServerServiceMapping
argument_list|(
literal|"seda:b"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|zipkin
operator|.
name|addServerServiceMapping
argument_list|(
literal|"seda:c"
argument_list|,
literal|"c"
argument_list|)
expr_stmt|;
name|zipkin
operator|.
name|setSpanCollector
argument_list|(
operator|new
name|ScribeSpanCollector
argument_list|(
name|ip
argument_list|,
literal|9410
argument_list|)
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
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|1
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|notify
operator|.
name|matches
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
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
literal|"seda:a"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"start"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"a"
argument_list|)
operator|.
name|log
argument_list|(
literal|"routing at ${routeId}"
argument_list|)
operator|.
name|multicast
argument_list|()
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:c"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
literal|"End of routing"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:b"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"b"
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
name|from
argument_list|(
literal|"seda:c"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"c"
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
literal|"${random(0,100)}"
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

