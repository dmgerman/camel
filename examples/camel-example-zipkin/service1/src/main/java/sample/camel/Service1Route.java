begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p/>  * http://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ZipkinEventNotifier
import|;
end_import

begin_class
DECL|class|Service1Route
specifier|public
class|class
name|Service1Route
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
block|{
comment|// you can configure the route rule with Java DSL here
comment|// TODO: use CDI to setup Camel instead of here in the route
comment|// create zipkin
name|ZipkinEventNotifier
name|zipkin
init|=
operator|new
name|ZipkinEventNotifier
argument_list|()
decl_stmt|;
name|zipkin
operator|.
name|setHostName
argument_list|(
literal|"192.168.99.100"
argument_list|)
expr_stmt|;
name|zipkin
operator|.
name|setPort
argument_list|(
literal|9410
argument_list|)
expr_stmt|;
name|zipkin
operator|.
name|addClientServiceMapping
argument_list|(
literal|"http://localhost:9090/service2"
argument_list|,
literal|"service2"
argument_list|)
expr_stmt|;
comment|//        zipkin.addClientServiceMapping("http://localhost:7070/service3", "service3");
comment|// add zipkin to CamelContext
name|getContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
name|zipkin
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:trigger?exchangePattern=InOut&period=30s"
argument_list|)
operator|.
name|bean
argument_list|(
literal|"counterBean"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:9090/service2"
argument_list|)
comment|//            .to("http://localhost:7070/service3")
operator|.
name|log
argument_list|(
literal|"Result: ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

