begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * Unit test for the how FileProducer behaves a bit strantegly when generating filenames  */
end_comment

begin_class
DECL|class|FilerProducerFileNamesTest
specifier|public
class|class
name|FilerProducerFileNamesTest
extends|extends
name|ContextTestSupport
block|{
comment|// START SNIPPET: e1
DECL|method|testProducerWithMessageIdAsFileName ()
specifier|public
name|void
name|testProducerWithMessageIdAsFileName
parameter_list|()
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
literal|"direct:report"
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is a good report"
argument_list|)
expr_stmt|;
name|String
name|id
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:report"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/reports/report.txt/"
operator|+
name|id
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"File should exists"
argument_list|,
literal|true
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testProducerWithConfiguedFileNameInEndpointURI ()
specifier|public
name|void
name|testProducerWithConfiguedFileNameInEndpointURI
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:report2"
argument_list|,
literal|"This is another good report"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/report2.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"File should exists"
argument_list|,
literal|true
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testProducerWithHeaderFileName ()
specifier|public
name|void
name|testProducerWithHeaderFileName
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:report3"
argument_list|,
literal|"This is super good report"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/report-super.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"File should exists"
argument_list|,
literal|true
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"direct:report"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/reports/report.txt"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:report2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/report2.txt?autoCreate=false"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:report3"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
literal|"report-super.txt"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// END SNIPPET: e1
block|}
end_class

end_unit

