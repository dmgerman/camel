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
name|javax
operator|.
name|enterprise
operator|.
name|context
operator|.
name|ApplicationScoped
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|event
operator|.
name|Observes
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
name|CamelEvent
operator|.
name|CamelContextStartingEvent
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
annotation|@
name|ApplicationScoped
DECL|class|ClientApplication
specifier|public
class|class
name|ClientApplication
block|{
DECL|method|setupCamel (@bserves CamelContextStartingEvent event)
specifier|public
name|void
name|setupCamel
parameter_list|(
annotation|@
name|Observes
name|CamelContextStartingEvent
name|event
parameter_list|)
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
name|zipkin
operator|.
name|addClientServiceMapping
argument_list|(
literal|"http://localhost:9090/service1"
argument_list|,
literal|"service1"
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
comment|// register zipkin to CamelContext
name|zipkin
operator|.
name|init
argument_list|(
name|event
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

