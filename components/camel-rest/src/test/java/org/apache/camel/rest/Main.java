begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Connector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|nio
operator|.
name|SelectChannelConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mortbay
operator|.
name|jetty
operator|.
name|webapp
operator|.
name|WebAppContext
import|;
end_import

begin_comment
comment|/**  * A simple bootstrap class for starting Jetty in your IDE using the local web  * application.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|Main
specifier|public
specifier|final
class|class
name|Main
block|{
DECL|field|PORT
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
literal|8080
decl_stmt|;
DECL|field|WEBAPP_DIR
specifier|public
specifier|static
specifier|final
name|String
name|WEBAPP_DIR
init|=
literal|"src/main/webapp"
decl_stmt|;
DECL|field|WEBAPP_CTX
specifier|public
specifier|static
specifier|final
name|String
name|WEBAPP_CTX
init|=
literal|"/"
decl_stmt|;
DECL|field|server
specifier|protected
specifier|static
name|Server
name|server
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
DECL|method|Main ()
specifier|private
name|Main
parameter_list|()
block|{     }
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
comment|// now lets start the web server
name|int
name|port
init|=
name|PORT
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|String
name|text
init|=
name|args
index|[
literal|0
index|]
decl_stmt|;
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Starting Web Server on port: "
operator|+
name|port
argument_list|)
expr_stmt|;
name|run
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
DECL|method|run (int port)
specifier|public
specifier|static
name|void
name|run
parameter_list|(
name|int
name|port
parameter_list|)
throws|throws
name|Exception
block|{
name|SelectChannelConnector
name|connector
init|=
operator|new
name|SelectChannelConnector
argument_list|()
decl_stmt|;
name|connector
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setServer
argument_list|(
name|server
argument_list|)
expr_stmt|;
name|WebAppContext
name|context
init|=
operator|new
name|WebAppContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setResourceBase
argument_list|(
name|WEBAPP_DIR
argument_list|)
expr_stmt|;
name|context
operator|.
name|setContextPath
argument_list|(
name|WEBAPP_CTX
argument_list|)
expr_stmt|;
name|context
operator|.
name|setServer
argument_list|(
name|server
argument_list|)
expr_stmt|;
name|server
operator|.
name|setHandlers
argument_list|(
operator|new
name|Handler
index|[]
block|{
name|context
block|}
argument_list|)
expr_stmt|;
name|server
operator|.
name|setConnectors
argument_list|(
operator|new
name|Connector
index|[]
block|{
name|connector
block|}
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"=============================================================================="
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Started the Camel REST Console: point your web browser at http://localhost:"
operator|+
name|port
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"=============================================================================="
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
specifier|static
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

