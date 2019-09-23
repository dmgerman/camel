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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|spi
operator|.
name|annotations
operator|.
name|Component
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
name|support
operator|.
name|DefaultComponent
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
name|FsUrlStreamHandlerFactory
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

begin_class
annotation|@
name|Component
argument_list|(
literal|"hdfs"
argument_list|)
DECL|class|HdfsComponent
specifier|public
class|class
name|HdfsComponent
extends|extends
name|DefaultComponent
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
name|HdfsComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|HdfsComponent ()
specifier|public
name|HdfsComponent
parameter_list|()
block|{
name|initHdfs
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
specifier|final
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|HdfsEndpoint
name|hdfsEndpoint
init|=
operator|new
name|HdfsEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|hdfsEndpoint
operator|.
name|getConfig
argument_list|()
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|hdfsEndpoint
return|;
block|}
DECL|method|initHdfs ()
specifier|protected
name|void
name|initHdfs
parameter_list|()
block|{
try|try
block|{
name|URL
operator|.
name|setURLStreamHandlerFactory
argument_list|(
operator|new
name|FsUrlStreamHandlerFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ignore as its most likely already set
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot set URLStreamHandlerFactory due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getJAASConfiguration ()
specifier|static
name|Configuration
name|getJAASConfiguration
parameter_list|()
block|{
name|Configuration
name|auth
init|=
literal|null
decl_stmt|;
try|try
block|{
name|auth
operator|=
name|Configuration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Existing JAAS Configuration {}"
argument_list|,
name|auth
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cannot load existing JAAS configuration"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|auth
return|;
block|}
comment|/**      * To use the given configuration for security with JAAS.      */
DECL|method|setJAASConfiguration (Configuration auth)
specifier|static
name|void
name|setJAASConfiguration
parameter_list|(
name|Configuration
name|auth
parameter_list|)
block|{
if|if
condition|(
name|auth
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Restoring existing JAAS Configuration {}"
argument_list|,
name|auth
argument_list|)
expr_stmt|;
try|try
block|{
name|Configuration
operator|.
name|setConfiguration
argument_list|(
name|auth
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cannot restore JAAS Configuration. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"No JAAS Configuration to restore"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

