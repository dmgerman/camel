begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.scp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|scp
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
name|nio
operator|.
name|file
operator|.
name|Files
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
name|BindToRegistry
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
name|JndiRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assume
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

begin_class
DECL|class|ScpSimpleProduceTest
specifier|public
class|class
name|ScpSimpleProduceTest
extends|extends
name|ScpServerTestSupport
block|{
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
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
literal|"file:"
operator|+
name|getScpPath
argument_list|()
operator|+
literal|"?recursive=true&delete=true"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testScpSimpleProduce ()
specifier|public
name|void
name|testScpSimpleProduce
parameter_list|()
throws|throws
name|Exception
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|this
operator|.
name|isSetupComplete
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|getScpUri
argument_list|()
operator|+
literal|"?username=admin&password=admin&knownHostsFile="
operator|+
name|getKnownHostsFile
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScpSimpleProduceTwoTimes ()
specifier|public
name|void
name|testScpSimpleProduceTwoTimes
parameter_list|()
throws|throws
name|Exception
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|this
operator|.
name|isSetupComplete
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|getScpUri
argument_list|()
operator|+
literal|"?username=admin&password=admin&knownHostsFile="
operator|+
name|getKnownHostsFile
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"bye.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScpSimpleSubPathProduce ()
specifier|public
name|void
name|testScpSimpleSubPathProduce
parameter_list|()
throws|throws
name|Exception
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|this
operator|.
name|isSetupComplete
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|getScpUri
argument_list|()
operator|+
literal|"?username=admin&password=admin&knownHostsFile="
operator|+
name|getKnownHostsFile
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"mysub/bye.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScpSimpleTwoSubPathProduce ()
specifier|public
name|void
name|testScpSimpleTwoSubPathProduce
parameter_list|()
throws|throws
name|Exception
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|this
operator|.
name|isSetupComplete
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Farewell World"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|getScpUri
argument_list|()
operator|+
literal|"?username=admin&password=admin&knownHostsFile="
operator|+
name|getKnownHostsFile
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
argument_list|,
literal|"Farewell World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"mysub/mysubsub/farewell.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScpProduceChmod ()
specifier|public
name|void
name|testScpProduceChmod
parameter_list|()
throws|throws
name|Exception
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|this
operator|.
name|isSetupComplete
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bonjour Monde"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|getScpUri
argument_list|()
operator|+
literal|"?username=admin&password=admin&chmod=640&knownHostsFile="
operator|+
name|getKnownHostsFile
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
argument_list|,
literal|"Bonjour Monde"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"monde.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Fails on CI servers"
argument_list|)
DECL|method|testScpProducePrivateKey ()
specifier|public
name|void
name|testScpProducePrivateKey
parameter_list|()
throws|throws
name|Exception
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|this
operator|.
name|isSetupComplete
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|getScpUri
argument_list|()
operator|+
literal|"?username=admin&privateKeyFile=src/test/resources/camel-key.priv&privateKeyFilePassphrase=password&knownHostsFile="
operator|+
name|getKnownHostsFile
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
argument_list|,
literal|"Hallo Welt"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"welt.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Fails on CI servers"
argument_list|)
DECL|method|testScpProducePrivateKeyFromClasspath ()
specifier|public
name|void
name|testScpProducePrivateKeyFromClasspath
parameter_list|()
throws|throws
name|Exception
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|this
operator|.
name|isSetupComplete
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|getScpUri
argument_list|()
operator|+
literal|"?username=admin&privateKeyFile=classpath:camel-key.priv&privateKeyFilePassphrase=password&knownHostsFile="
operator|+
name|getKnownHostsFile
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
argument_list|,
literal|"Hallo Welt"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"welt.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Fails on CI servers"
argument_list|)
DECL|method|testScpProducePrivateKeyByte ()
specifier|public
name|void
name|testScpProducePrivateKeyByte
parameter_list|()
throws|throws
name|Exception
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|this
operator|.
name|isSetupComplete
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|getScpUri
argument_list|()
operator|+
literal|"?username=admin&privateKeyBytes=#privKey&privateKeyFilePassphrase=password&knownHostsFile="
operator|+
name|getKnownHostsFile
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
argument_list|,
literal|"Hallo Welt"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"welt.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|BindToRegistry
argument_list|(
literal|"privKey"
argument_list|)
DECL|method|loadPrivateKey ()
specifier|public
name|byte
index|[]
name|loadPrivateKey
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|privKey
init|=
name|Files
operator|.
name|readAllBytes
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/camel-key.priv"
argument_list|)
operator|.
name|toPath
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|privKey
return|;
block|}
block|}
end_class

end_unit

