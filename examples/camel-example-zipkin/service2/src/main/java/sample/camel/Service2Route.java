begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|sample.camel
package|package
name|sample
operator|.
name|camel
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
name|zipkin
operator|.
name|ZipkinTracer
import|;
end_import

begin_class
DECL|class|Service2Route
specifier|public
class|class
name|Service2Route
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create zipkin
name|ZipkinTracer
name|zipkin
init|=
operator|new
name|ZipkinTracer
argument_list|()
decl_stmt|;
name|zipkin
operator|.
name|setEndpoint
argument_list|(
literal|"http://localhost:9411/api/v2/spans"
argument_list|)
expr_stmt|;
comment|// set the service name
name|zipkin
operator|.
name|setServiceName
argument_list|(
literal|"service2"
argument_list|)
expr_stmt|;
comment|// capture 100% of all the events
name|zipkin
operator|.
name|setRate
argument_list|(
literal|1.0f
argument_list|)
expr_stmt|;
comment|// include message bodies in the traces (not recommended for production)
name|zipkin
operator|.
name|setIncludeMessageBodyStreams
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// add zipkin to CamelContext
name|zipkin
operator|.
name|init
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"undertow:http://0.0.0.0:7070/service2"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"service2"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|log
argument_list|(
literal|" Service2 request: ${body}"
argument_list|)
operator|.
name|delay
argument_list|(
name|simple
argument_list|(
literal|"${random(1000,2000)}"
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|simple
argument_list|(
literal|"Service2-${body}"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"Service2 response: ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

