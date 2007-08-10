begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|remote
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|ContextTestSupport
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
name|ftpserver
operator|.
name|ConfigurableFtpServerContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|FtpServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|config
operator|.
name|PropertiesConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|ftplet
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|interfaces
operator|.
name|FtpServerContext
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|FtpRouteTest
specifier|public
class|class
name|FtpRouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
DECL|field|ftpUrl
specifier|protected
name|String
name|ftpUrl
init|=
literal|"ftp://admin@localhost:20010/tmp/camel?password=admin"
decl_stmt|;
DECL|field|ftpServer
specifier|protected
name|FtpServer
name|ftpServer
decl_stmt|;
DECL|field|expectedBody
specifier|protected
name|String
name|expectedBody
init|=
literal|"Hello there!"
decl_stmt|;
DECL|method|testFtpRoute ()
specifier|public
name|void
name|testFtpRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
comment|// TODO when we support multiple marshallers for messages
comment|// we can support passing headers over files using serialized/XML files
comment|//resultEndpoint.message(0).header("cheese").isEqualTo(123);
name|sendExchange
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendExchange (final Object expectedBody)
specifier|protected
name|void
name|sendExchange
parameter_list|(
specifier|final
name|Object
name|expectedBody
parameter_list|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|ftpUrl
argument_list|,
name|expectedBody
argument_list|,
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|ftpServer
operator|=
name|createFtpServer
argument_list|()
expr_stmt|;
name|ftpServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|=
operator|(
name|MockEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|createFtpServer
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|ftpServer
operator|!=
literal|null
condition|)
block|{
name|ftpServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|ftpUrl
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
DECL|method|createFtpServer ()
specifier|protected
name|FtpServer
name|createFtpServer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// get the configuration object
name|Properties
name|properties
init|=
name|createFtpServerProperties
argument_list|()
decl_stmt|;
name|Configuration
name|config
init|=
operator|new
name|PropertiesConfiguration
argument_list|(
name|properties
argument_list|)
decl_stmt|;
comment|// create servce context
name|FtpServerContext
name|ftpConfig
init|=
operator|new
name|ConfigurableFtpServerContext
argument_list|(
name|config
argument_list|)
decl_stmt|;
comment|// create the server object and start it
return|return
operator|new
name|FtpServer
argument_list|(
name|ftpConfig
argument_list|)
return|;
block|}
DECL|method|createFtpServerProperties ()
specifier|protected
name|Properties
name|createFtpServerProperties
parameter_list|()
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
comment|//properties.setProperty("config.data-connection.passive.ports", "20010");
name|properties
operator|.
name|setProperty
argument_list|(
literal|"config.listeners.default.port"
argument_list|,
literal|"20010"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"config.create-default-user"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
return|return
name|properties
return|;
block|}
block|}
end_class

end_unit

