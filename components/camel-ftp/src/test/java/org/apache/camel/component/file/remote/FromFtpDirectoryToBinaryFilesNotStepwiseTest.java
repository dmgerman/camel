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

begin_comment
comment|/**  * Unit test to verify that we can pool a BINARY file in a directory from the  * FTP Server and store it on a local file path. Based on CAMEL-834.  */
end_comment

begin_class
DECL|class|FromFtpDirectoryToBinaryFilesNotStepwiseTest
specifier|public
class|class
name|FromFtpDirectoryToBinaryFilesNotStepwiseTest
extends|extends
name|FromFtpDirectoryToBinaryFilesTest
block|{
DECL|method|getFtpUrl ()
specifier|protected
name|String
name|getFtpUrl
parameter_list|()
block|{
return|return
literal|"ftp://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/incoming/?password=admin"
operator|+
literal|"&binary=true&useFixedDelay=false&recursive=false&delay=5000&stepwise=false"
return|;
block|}
block|}
end_class

end_unit

