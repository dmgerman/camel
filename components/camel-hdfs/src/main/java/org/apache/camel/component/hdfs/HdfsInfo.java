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
name|HdfsKerberosConfigurationFactory
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
name|KerberosConfiguration
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|HdfsInfo
specifier|public
specifier|final
class|class
name|HdfsInfo
block|{
DECL|field|configuration
specifier|private
name|Configuration
name|configuration
decl_stmt|;
DECL|field|fileSystem
specifier|private
name|FileSystem
name|fileSystem
decl_stmt|;
DECL|field|path
specifier|private
name|Path
name|path
decl_stmt|;
DECL|method|HdfsInfo (String hdfsPath, HdfsConfiguration endpointConfig)
name|HdfsInfo
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
name|this
operator|.
name|configuration
operator|=
name|newConfiguration
argument_list|(
name|endpointConfig
argument_list|)
expr_stmt|;
name|this
operator|.
name|fileSystem
operator|=
name|newFileSystem
argument_list|(
name|this
operator|.
name|configuration
argument_list|,
name|hdfsPath
argument_list|,
name|endpointConfig
argument_list|)
expr_stmt|;
name|this
operator|.
name|path
operator|=
operator|new
name|Path
argument_list|(
name|hdfsPath
argument_list|)
expr_stmt|;
block|}
DECL|method|getConf ()
specifier|public
name|Configuration
name|getConf
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getFileSystem ()
specifier|public
name|FileSystem
name|getFileSystem
parameter_list|()
block|{
return|return
name|fileSystem
return|;
block|}
DECL|method|getPath ()
specifier|public
name|Path
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|newConfiguration (HdfsConfiguration endpointConfig)
specifier|private
name|Configuration
name|newConfiguration
parameter_list|(
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
name|List
argument_list|<
name|String
argument_list|>
name|namedNodes
init|=
name|endpointConfig
operator|.
name|getKerberosNamedNodeList
argument_list|()
decl_stmt|;
name|String
name|kerberosConfigFileLocation
init|=
name|endpointConfig
operator|.
name|getKerberosConfigFileLocation
argument_list|()
decl_stmt|;
return|return
operator|new
name|KerberosConfiguration
argument_list|(
name|namedNodes
argument_list|,
name|kerberosConfigFileLocation
argument_list|,
name|endpointConfig
operator|.
name|getReplication
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|Configuration
argument_list|()
return|;
block|}
block|}
comment|/**      * this will connect to the hadoop hdfs file system, and in case of no connection      * then the hardcoded timeout in hadoop is 45 x 20 sec = 15 minutes      */
DECL|method|newFileSystem (Configuration configuration, String hdfsPath, HdfsConfiguration endpointConfig)
specifier|private
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
operator|(
operator|(
name|KerberosConfiguration
operator|)
name|configuration
operator|)
operator|.
name|loginWithKeytab
argument_list|(
name|userName
argument_list|,
name|keytabLocation
argument_list|)
expr_stmt|;
block|}
return|return
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
return|;
block|}
block|}
end_class

end_unit

