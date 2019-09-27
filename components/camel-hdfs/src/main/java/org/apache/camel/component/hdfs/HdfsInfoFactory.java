begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs
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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|Configuration
import|;
end_import

begin_class
DECL|class|HdfsInfoFactory
specifier|public
specifier|final
class|class
name|HdfsInfoFactory
block|{
DECL|method|HdfsInfoFactory ()
specifier|private
name|HdfsInfoFactory
parameter_list|()
block|{     }
DECL|method|newHdfsInfo (String hdfsPath, HdfsConfiguration configuration)
specifier|public
specifier|static
name|HdfsInfo
name|newHdfsInfo
parameter_list|(
name|String
name|hdfsPath
parameter_list|,
name|HdfsConfiguration
name|configuration
parameter_list|)
throws|throws
name|IOException
block|{
comment|// need to remember auth as Hadoop will override that, which otherwise means the Auth is broken afterwards
name|Configuration
name|auth
init|=
name|HdfsComponent
operator|.
name|getJAASConfiguration
argument_list|()
decl_stmt|;
try|try
block|{
return|return
operator|new
name|HdfsInfo
argument_list|(
name|hdfsPath
argument_list|,
name|configuration
argument_list|)
return|;
block|}
finally|finally
block|{
name|HdfsComponent
operator|.
name|setJAASConfiguration
argument_list|(
name|auth
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

