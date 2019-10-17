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
name|java
operator|.
name|net
operator|.
name|URI
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
name|hdfs
operator|.
name|kerberos
operator|.
name|KerberosAuthentication
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
name|hdfs
operator|.
name|kerberos
operator|.
name|KerberosConfigurationBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|conf
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
name|hadoop
operator|.
name|fs
operator|.
name|FileSystem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|fs
operator|.
name|Path
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
block|{
comment|// hidden
block|}
DECL|method|newHdfsInfo (String hdfsPath, HdfsConfiguration endpointConfig)
specifier|static
name|HdfsInfo
name|newHdfsInfo
parameter_list|(
name|String
name|hdfsPath
parameter_list|,
name|HdfsConfiguration
name|endpointConfig
parameter_list|)
throws|throws
name|IOException
block|{
comment|// need to remember auth as Hadoop will override that, which otherwise means the Auth is broken afterwards
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
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
name|newHdfsInfoWithoutAuth
argument_list|(
name|hdfsPath
argument_list|,
name|endpointConfig
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
DECL|method|newHdfsInfoWithoutAuth (String hdfsPath, HdfsConfiguration endpointConfig)
specifier|static
name|HdfsInfo
name|newHdfsInfoWithoutAuth
parameter_list|(
name|String
name|hdfsPath
parameter_list|,
name|HdfsConfiguration
name|endpointConfig
parameter_list|)
throws|throws
name|IOException
block|{
name|Configuration
name|configuration
init|=
name|newConfiguration
argument_list|(
name|endpointConfig
argument_list|)
decl_stmt|;
name|authenticate
argument_list|(
name|configuration
argument_list|,
name|endpointConfig
argument_list|)
expr_stmt|;
name|FileSystem
name|fileSystem
init|=
name|newFileSystem
argument_list|(
name|configuration
argument_list|,
name|hdfsPath
argument_list|,
name|endpointConfig
argument_list|)
decl_stmt|;
name|Path
name|path
init|=
operator|new
name|Path
argument_list|(
name|hdfsPath
argument_list|)
decl_stmt|;
return|return
operator|new
name|HdfsInfo
argument_list|(
name|configuration
argument_list|,
name|fileSystem
argument_list|,
name|path
argument_list|)
return|;
block|}
DECL|method|newConfiguration (HdfsConfiguration endpointConfig)
specifier|static
name|Configuration
name|newConfiguration
parameter_list|(
name|HdfsConfiguration
name|endpointConfig
parameter_list|)
block|{
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpointConfig
operator|.
name|isKerberosAuthentication
argument_list|()
condition|)
block|{
name|KerberosConfigurationBuilder
operator|.
name|withKerberosConfiguration
argument_list|(
name|configuration
argument_list|,
name|endpointConfig
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpointConfig
operator|.
name|hasClusterConfiguration
argument_list|()
condition|)
block|{
name|HaConfigurationBuilder
operator|.
name|withClusterConfiguration
argument_list|(
name|configuration
argument_list|,
name|endpointConfig
argument_list|)
expr_stmt|;
block|}
return|return
name|configuration
return|;
block|}
DECL|method|authenticate (Configuration configuration, HdfsConfiguration endpointConfig)
specifier|static
name|void
name|authenticate
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|HdfsConfiguration
name|endpointConfig
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|endpointConfig
operator|.
name|isKerberosAuthentication
argument_list|()
condition|)
block|{
name|String
name|userName
init|=
name|endpointConfig
operator|.
name|getKerberosUsername
argument_list|()
decl_stmt|;
name|String
name|keytabLocation
init|=
name|endpointConfig
operator|.
name|getKerberosKeytabLocation
argument_list|()
decl_stmt|;
operator|new
name|KerberosAuthentication
argument_list|(
name|configuration
argument_list|,
name|userName
argument_list|,
name|keytabLocation
argument_list|)
operator|.
name|loginWithKeytab
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * this will connect to the hadoop hdfs file system, and in case of no connection      * then the hardcoded timeout in hadoop is 45 x 20 sec = 15 minutes      */
DECL|method|newFileSystem (Configuration configuration, String hdfsPath, HdfsConfiguration endpointConfig)
specifier|static
name|FileSystem
name|newFileSystem
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|String
name|hdfsPath
parameter_list|,
name|HdfsConfiguration
name|endpointConfig
parameter_list|)
throws|throws
name|IOException
block|{
name|FileSystem
name|fileSystem
decl_stmt|;
if|if
condition|(
name|endpointConfig
operator|.
name|hasClusterConfiguration
argument_list|()
condition|)
block|{
comment|// using default FS that was set during in the cluster configuration (@see org.apache.camel.component.hdfs.HaConfigurationBuilder)
name|fileSystem
operator|=
name|FileSystem
operator|.
name|get
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fileSystem
operator|=
name|FileSystem
operator|.
name|get
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|hdfsPath
argument_list|)
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
return|return
name|fileSystem
return|;
block|}
block|}
end_class

end_unit

