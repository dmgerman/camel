begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|Endpoint
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
name|http
operator|.
name|HttpConstants
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
name|util
operator|.
name|MessageHelper
import|;
end_import

begin_comment
comment|/**  * Unit test for exposing a http server that returns images  */
end_comment

begin_class
DECL|class|JettyImageFileTest
specifier|public
class|class
name|JettyImageFileTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|sendImageContent (boolean usingGZip)
specifier|private
name|void
name|sendImageContent
parameter_list|(
name|boolean
name|usingGZip
parameter_list|)
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"http://localhost:9080/myapp/myservice"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|usingGZip
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HttpConstants
operator|.
name|CONTENT_ENCODING
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
block|}
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong content-type "
argument_list|,
name|MessageHelper
operator|.
name|getContentType
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
argument_list|,
literal|"image/jpeg"
argument_list|)
expr_stmt|;
block|}
DECL|method|testImageContentType ()
specifier|public
name|void
name|testImageContentType
parameter_list|()
throws|throws
name|Exception
block|{
name|sendImageContent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testImageContentWithGZip ()
specifier|public
name|void
name|testImageContentWithGZip
parameter_list|()
throws|throws
name|Exception
block|{
name|sendImageContent
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"jetty:http://localhost:9080/myapp/myservice"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|MyImageService
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyImageService
specifier|public
class|class
name|MyImageService
implements|implements
name|Processor
block|{
DECL|method|process (Exchange exchange)
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/data/logo.jpeg"
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"image/jpeg"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

