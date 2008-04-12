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
name|impl
operator|.
name|JndiRegistry
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
comment|/**  * For documentation how to write files using the FileProducer.  */
end_comment

begin_class
DECL|class|ToFileRouteTest
specifier|public
class|class
name|ToFileRouteTest
extends|extends
name|ContextTestSupport
block|{
comment|// START SNIPPET: e1
DECL|method|testToFile ()
specifier|public
name|void
name|testToFile
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:reports"
argument_list|,
literal|"This is a great report"
argument_list|)
expr_stmt|;
comment|// give time for the file to be written before assertions
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// assert the file exists
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/test-reports/report.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The file should have been written"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
comment|// bind our processor in the registry with the given id
name|JndiRegistry
name|reg
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"processReport"
argument_list|,
operator|new
name|ProcessReport
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|reg
return|;
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
comment|// the reports from the seda queue is processed by our processor
comment|// before they are written to files in the target/reports directory
name|from
argument_list|(
literal|"seda:reports"
argument_list|)
operator|.
name|processRef
argument_list|(
literal|"processReport"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file://target/test-reports"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|ProcessReport
specifier|private
class|class
name|ProcessReport
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
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// do some business logic here
comment|// set the output to the file
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
comment|// set the output filename using java code logic, notice that this is done by setting
comment|// a special header property of the out exchange
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
literal|"report.txt"
argument_list|)
expr_stmt|;
block|}
block|}
comment|// END SNIPPET: e1
block|}
end_class

end_unit

