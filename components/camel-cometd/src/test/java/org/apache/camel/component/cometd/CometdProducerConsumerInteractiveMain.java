begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cometd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cometd
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|impl
operator|.
name|DefaultCamelContext
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

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Run this test manually"
argument_list|)
DECL|class|CometdProducerConsumerInteractiveMain
specifier|public
class|class
name|CometdProducerConsumerInteractiveMain
block|{
DECL|field|URI
specifier|private
specifier|static
specifier|final
name|String
name|URI
init|=
literal|"cometd://127.0.0.1:9091/channel/test?baseResource=file:./src/test/resources/webapp&"
operator|+
literal|"timeout=240000&interval=0&maxInterval=30000&multiFrameInterval=1500&jsonCommented=true&logLevel=2"
decl_stmt|;
DECL|field|URIS
specifier|private
specifier|static
specifier|final
name|String
name|URIS
init|=
literal|"cometds://127.0.0.1:9443/channel/test?baseResource=file:./src/test/resources/webapp&"
operator|+
literal|"timeout=240000&interval=0&maxInterval=30000&multiFrameInterval=1500&jsonCommented=true&logLevel=2"
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|pwd
specifier|private
name|String
name|pwd
init|=
literal|"changeit"
decl_stmt|;
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|CometdProducerConsumerInteractiveMain
name|me
init|=
operator|new
name|CometdProducerConsumerInteractiveMain
argument_list|()
decl_stmt|;
name|me
operator|.
name|testCometdProducerConsumerInteractive
argument_list|()
expr_stmt|;
block|}
DECL|method|testCometdProducerConsumerInteractive ()
specifier|public
name|void
name|testCometdProducerConsumerInteractive
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|private
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
throws|throws
name|URISyntaxException
block|{
name|CometdComponent
name|component
init|=
operator|(
name|CometdComponent
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"cometds"
argument_list|)
decl_stmt|;
name|component
operator|.
name|setSslPassword
argument_list|(
name|pwd
argument_list|)
expr_stmt|;
name|component
operator|.
name|setSslKeyPassword
argument_list|(
name|pwd
argument_list|)
expr_stmt|;
name|URI
name|keyStoreUrl
init|=
name|CometdProducerConsumerInteractiveMain
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/jsse/localhost.p12"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|component
operator|.
name|setSslKeystore
argument_list|(
name|keyStoreUrl
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"stream:in"
argument_list|)
operator|.
name|to
argument_list|(
name|URI
argument_list|)
operator|.
name|to
argument_list|(
name|URIS
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

