begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

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
name|javax
operator|.
name|jws
operator|.
name|WebMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
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
name|Processor
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
name|cxf
operator|.
name|CXFTestSupport
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
name|spring
operator|.
name|Main
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
name|TestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|Multipart
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
DECL|class|CxfRsRelayTest
specifier|public
class|class
name|CxfRsRelayTest
extends|extends
name|TestSupport
block|{
DECL|field|port6
specifier|private
specifier|static
name|int
name|port6
init|=
name|CXFTestSupport
operator|.
name|getPort6
argument_list|()
decl_stmt|;
comment|/**      * A sample service "interface" (technically, it is a class since we will      * use proxy-client. That interface exposes three methods over-loading each      * other : we are testing the appropriate one will be chosen at runtime.      *       */
annotation|@
name|WebService
annotation|@
name|Path
argument_list|(
literal|"/rootpath"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
DECL|class|UploadService
specifier|public
specifier|static
class|class
name|UploadService
block|{
annotation|@
name|WebMethod
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/path1"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
DECL|method|upload (@ultipartvalue = R, type = R) java.lang.Number content, @Multipart(value = R, type = R) String name)
specifier|public
name|void
name|upload
parameter_list|(
annotation|@
name|Multipart
argument_list|(
name|value
operator|=
literal|"content"
argument_list|,
name|type
operator|=
literal|"application/octet-stream"
argument_list|)
name|java
operator|.
name|lang
operator|.
name|Number
name|content
parameter_list|,
annotation|@
name|Multipart
argument_list|(
name|value
operator|=
literal|"name"
argument_list|,
name|type
operator|=
literal|"text/plain"
argument_list|)
name|String
name|name
parameter_list|)
block|{         }
annotation|@
name|WebMethod
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/path2"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"text/plain"
argument_list|)
DECL|method|upload ()
specifier|private
name|void
name|upload
parameter_list|()
block|{         }
annotation|@
name|WebMethod
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/path3"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"multipart/form-data"
argument_list|)
DECL|method|upload (@ultipartvalue = R, type = R) InputStream content, @Multipart(value = R, type = R) String name)
specifier|public
name|void
name|upload
parameter_list|(
annotation|@
name|Multipart
argument_list|(
name|value
operator|=
literal|"content"
argument_list|,
name|type
operator|=
literal|"application/octet-stream"
argument_list|)
name|InputStream
name|content
parameter_list|,
annotation|@
name|Multipart
argument_list|(
name|value
operator|=
literal|"name"
argument_list|,
name|type
operator|=
literal|"text/plain"
argument_list|)
name|String
name|name
parameter_list|)
block|{         }
block|}
DECL|field|SAMPLE_CONTENT_PATH
specifier|private
specifier|static
specifier|final
name|String
name|SAMPLE_CONTENT_PATH
init|=
literal|"/org/apache/camel/component/cxf/jaxrs/CxfRsSpringRelay.xml"
decl_stmt|;
DECL|field|SAMPLE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SAMPLE_NAME
init|=
literal|"CxfRsSpringRelay.xml"
decl_stmt|;
DECL|field|LATCH
specifier|private
specifier|static
specifier|final
name|CountDownLatch
name|LATCH
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|content
specifier|private
specifier|static
name|String
name|content
decl_stmt|;
DECL|field|name
specifier|private
specifier|static
name|String
name|name
decl_stmt|;
comment|/**      * That test builds a route chaining two cxfrs endpoints. It shows a request      * sent to the first one will be correctly transferred and consumed by the      * other one.      */
annotation|@
name|Test
DECL|method|testJaxrsRelayRoute ()
specifier|public
name|void
name|testJaxrsRelayRoute
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
try|try
block|{
name|main
operator|.
name|setApplicationContextUri
argument_list|(
literal|"org/apache/camel/component/cxf/jaxrs/CxfRsSpringRelay.xml"
argument_list|)
expr_stmt|;
name|main
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
name|t
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
comment|/**                  * Sends a request to the first endpoint in the route                  */
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|port6
operator|+
literal|"/CxfRsRelayTest/rest"
argument_list|,
name|UploadService
operator|.
name|class
argument_list|)
operator|.
name|upload
argument_list|(
name|CamelRouteBuilder
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|SAMPLE_CONTENT_PATH
argument_list|)
argument_list|,
name|SAMPLE_NAME
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error uploading to http://localhost:"
operator|+
name|port6
operator|+
literal|"/CxfRsRelayTest/rest"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|t
operator|.
name|start
argument_list|()
expr_stmt|;
name|LATCH
operator|.
name|await
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SAMPLE_NAME
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|CamelRouteBuilder
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|SAMPLE_CONTENT_PATH
argument_list|)
argument_list|)
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|writer
operator|.
name|toString
argument_list|()
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Route builder to be used with      * org/apache/camel/component/cxf/jaxrs/CxfRsSpringRelay.xml      *       */
DECL|class|CamelRouteBuilder
specifier|public
specifier|static
class|class
name|CamelRouteBuilder
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
name|InterruptedException
block|{
name|from
argument_list|(
literal|"upload1"
argument_list|)
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|)
operator|.
name|to
argument_list|(
literal|"upload2Client"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"upload2"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// once the message arrives in the second endpoint, stores
comment|// the message components and warns results can be compared
name|content
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"content"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|name
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"name"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|LATCH
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

