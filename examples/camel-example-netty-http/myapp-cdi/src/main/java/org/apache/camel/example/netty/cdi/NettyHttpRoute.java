begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.netty.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|netty
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|cdi
operator|.
name|ContextName
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
name|netty4
operator|.
name|http
operator|.
name|NettySharedHttpServer
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
name|cdi
operator|.
name|api
operator|.
name|Service
import|;
end_import

begin_class
annotation|@
name|ContextName
argument_list|(
literal|"netty-myapp-cdi"
argument_list|)
DECL|class|NettyHttpRoute
specifier|public
class|class
name|NettyHttpRoute
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Inject
annotation|@
name|Service
DECL|field|server
specifier|private
name|NettySharedHttpServer
name|server
decl_stmt|;
annotation|@
name|Produces
annotation|@
name|Named
argument_list|(
literal|"httpServer"
argument_list|)
DECL|method|server ()
specifier|private
name|NettySharedHttpServer
name|server
parameter_list|()
block|{
return|return
name|server
return|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"netty-http:http://localhost/cdi?matchOnUriPrefix=true&nettySharedHttpServer=#httpServer"
argument_list|)
operator|.
name|id
argument_list|(
literal|"http-route-cdi"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"Response from Camel CDI on route ${routeId} using thread: ${threadName}"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

