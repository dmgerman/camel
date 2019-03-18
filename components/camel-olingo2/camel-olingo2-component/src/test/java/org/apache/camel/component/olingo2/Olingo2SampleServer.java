begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|handler
operator|.
name|DefaultHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|handler
operator|.
name|HandlerCollection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|webapp
operator|.
name|WebAppContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The embedded server for hosting the olingo2 sample service during the tests  */
end_comment

begin_class
DECL|class|Olingo2SampleServer
specifier|public
class|class
name|Olingo2SampleServer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Olingo2SampleServer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|server
specifier|private
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Server
name|server
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
comment|/**      *       * @param port      * @param resourcePath      */
DECL|method|Olingo2SampleServer (int port, String resourcePath)
specifier|public
name|Olingo2SampleServer
parameter_list|(
name|int
name|port
parameter_list|,
name|String
name|resourcePath
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|server
operator|=
operator|new
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Server
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|WebAppContext
name|webappcontext
init|=
operator|new
name|WebAppContext
argument_list|()
decl_stmt|;
name|String
name|contextPath
init|=
literal|null
decl_stmt|;
try|try
block|{
name|contextPath
operator|=
name|Olingo2SampleServer
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|resourcePath
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Unable to read the resource at {}"
argument_list|,
name|resourcePath
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|webappcontext
operator|.
name|setContextPath
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|webappcontext
operator|.
name|setWar
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
name|HandlerCollection
name|handlers
init|=
operator|new
name|HandlerCollection
argument_list|()
decl_stmt|;
name|handlers
operator|.
name|setHandlers
argument_list|(
operator|new
name|Handler
index|[]
block|{
name|webappcontext
block|,
operator|new
name|DefaultHandler
argument_list|()
block|}
argument_list|)
expr_stmt|;
name|server
operator|.
name|setHandler
argument_list|(
name|handlers
argument_list|)
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Olingo sample server started at port {}"
argument_list|,
name|port
argument_list|)
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
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
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
DECL|method|generateSampleData (String serviceUrl)
specifier|static
name|void
name|generateSampleData
parameter_list|(
name|String
name|serviceUrl
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
comment|// need to use reflection to avoid a build error even when the sample source is not available
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.olingo.sample.annotation.util.AnnotationSampleDataGenerator"
argument_list|)
decl_stmt|;
name|Method
name|m
init|=
name|clz
operator|.
name|getMethod
argument_list|(
literal|"generateData"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|m
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|serviceUrl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Unable to load the required sample class"
argument_list|,
name|t
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"unable to load the required sample class"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

