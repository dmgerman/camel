begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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

begin_comment
comment|/**  * Unit test used for FTP wiki documentation  */
end_comment

begin_class
DECL|class|FromFtpToBinarySampleTest
specifier|public
class|class
name|FromFtpToBinarySampleTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testDummy ()
specifier|public
name|void
name|testDummy
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this is a noop test
block|}
comment|// START SNIPPET: e1
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
comment|// we use a delay of 60 minutes (eg. once pr. hour we poll the FTP server
name|long
name|delay
init|=
literal|3600000
decl_stmt|;
comment|// from the given FTP server we poll (= download) all the files
comment|// from the public/reports folder as BINARY types and store this as files
comment|// in a local directory. Camel will use the filenames from the FTPServer
comment|// notice that the FTPConsumer properties must be prefixed with "consumer." in the URL
comment|// the delay parameter is from the FileConsumer component so we should use consumer.delay as
comment|// the URI parameter name. The FTP Component is an extension of the File Component.
name|from
argument_list|(
literal|"ftp://tiger:scott@localhost/public/reports?binary=true&consumer.delay="
operator|+
name|delay
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
comment|// END SNIPPET: e1
block|}
end_class

end_unit

