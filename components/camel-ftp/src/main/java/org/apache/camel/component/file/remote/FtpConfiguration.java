begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|spi
operator|.
name|UriParam
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
name|UriParams
import|;
end_import

begin_comment
comment|/**  * FTP configuration  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|FtpConfiguration
specifier|public
class|class
name|FtpConfiguration
extends|extends
name|RemoteFileConfiguration
block|{
DECL|field|DEFAULT_FTP_PORT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_FTP_PORT
init|=
literal|21
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|account
specifier|private
name|String
name|account
decl_stmt|;
DECL|method|FtpConfiguration ()
specifier|public
name|FtpConfiguration
parameter_list|()
block|{
name|setProtocol
argument_list|(
literal|"ftp"
argument_list|)
expr_stmt|;
block|}
DECL|method|FtpConfiguration (URI uri)
specifier|public
name|FtpConfiguration
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setDefaultPort ()
specifier|protected
name|void
name|setDefaultPort
parameter_list|()
block|{
name|setPort
argument_list|(
name|DEFAULT_FTP_PORT
argument_list|)
expr_stmt|;
block|}
DECL|method|getAccount ()
specifier|public
name|String
name|getAccount
parameter_list|()
block|{
return|return
name|account
return|;
block|}
comment|/**      * Account to use for login      */
DECL|method|setAccount (String account)
specifier|public
name|void
name|setAccount
parameter_list|(
name|String
name|account
parameter_list|)
block|{
name|this
operator|.
name|account
operator|=
name|account
expr_stmt|;
block|}
block|}
end_class

end_unit

