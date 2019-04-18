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
comment|/**  * FTP Secure (FTP over SSL/TLS) configuration  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|FtpsConfiguration
specifier|public
class|class
name|FtpsConfiguration
extends|extends
name|FtpConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"TLSv1.2"
argument_list|,
name|label
operator|=
literal|"security"
argument_list|)
comment|// TODO : switch to TLSv1.3 when we fully upgrade to JDK11
DECL|field|securityProtocol
specifier|private
name|String
name|securityProtocol
init|=
literal|"TLSv1.2"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|isImplicit
specifier|private
name|boolean
name|isImplicit
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|disableSecureDataChannelDefaults
specifier|private
name|boolean
name|disableSecureDataChannelDefaults
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|execProt
specifier|private
name|String
name|execProt
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|execPbsz
specifier|private
name|Long
name|execPbsz
decl_stmt|;
DECL|method|FtpsConfiguration ()
specifier|public
name|FtpsConfiguration
parameter_list|()
block|{
name|setProtocol
argument_list|(
literal|"ftps"
argument_list|)
expr_stmt|;
block|}
DECL|method|FtpsConfiguration (URI uri)
specifier|public
name|FtpsConfiguration
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
comment|/**      * Returns the underlying security protocol.      */
DECL|method|getSecurityProtocol ()
specifier|public
name|String
name|getSecurityProtocol
parameter_list|()
block|{
return|return
name|securityProtocol
return|;
block|}
comment|/**      * Set the underlying security protocol.      */
DECL|method|setSecurityProtocol (String securityProtocol)
specifier|public
name|void
name|setSecurityProtocol
parameter_list|(
name|String
name|securityProtocol
parameter_list|)
block|{
name|this
operator|.
name|securityProtocol
operator|=
name|securityProtocol
expr_stmt|;
block|}
comment|/**      * Returns the security mode(Implicit/Explicit).      * true - Implicit Mode / False - Explicit Mode      */
DECL|method|isImplicit ()
specifier|public
name|boolean
name|isImplicit
parameter_list|()
block|{
return|return
name|isImplicit
return|;
block|}
comment|/**      * Set the security mode(Implicit/Explicit).      * true - Implicit Mode / False - Explicit Mode      */
DECL|method|setIsImplicit (boolean isImplicit)
specifier|public
name|void
name|setIsImplicit
parameter_list|(
name|boolean
name|isImplicit
parameter_list|)
block|{
name|this
operator|.
name|isImplicit
operator|=
name|isImplicit
expr_stmt|;
block|}
DECL|method|isDisableSecureDataChannelDefaults ()
specifier|public
name|boolean
name|isDisableSecureDataChannelDefaults
parameter_list|()
block|{
return|return
name|disableSecureDataChannelDefaults
return|;
block|}
comment|/**      * Use this option to disable default options when using secure data channel.      *<p/>      * This allows you to be in full control what the execPbsz and execProt setting should be used.      *<p/>      * Default is<tt>false</tt>      * @see #setExecPbsz(Long)      * @see #setExecProt(String)      */
DECL|method|setDisableSecureDataChannelDefaults (boolean disableSecureDataChannelDefaults)
specifier|public
name|void
name|setDisableSecureDataChannelDefaults
parameter_list|(
name|boolean
name|disableSecureDataChannelDefaults
parameter_list|)
block|{
name|this
operator|.
name|disableSecureDataChannelDefaults
operator|=
name|disableSecureDataChannelDefaults
expr_stmt|;
block|}
DECL|method|getExecProt ()
specifier|public
name|String
name|getExecProt
parameter_list|()
block|{
return|return
name|execProt
return|;
block|}
comment|/**      * The exec protection level      *<p/>      * PROT command. C - Clear S - Safe(SSL protocol only) E - Confidential(SSL protocol only) P - Private      *      * @param execProt either C, S, E or P      */
DECL|method|setExecProt (String execProt)
specifier|public
name|void
name|setExecProt
parameter_list|(
name|String
name|execProt
parameter_list|)
block|{
name|this
operator|.
name|execProt
operator|=
name|execProt
expr_stmt|;
block|}
DECL|method|getExecPbsz ()
specifier|public
name|Long
name|getExecPbsz
parameter_list|()
block|{
return|return
name|execPbsz
return|;
block|}
comment|/**      * When using secure data channel you can set the exec protection buffer size      *      * @param execPbsz the buffer size      */
DECL|method|setExecPbsz (Long execPbsz)
specifier|public
name|void
name|setExecPbsz
parameter_list|(
name|Long
name|execPbsz
parameter_list|)
block|{
name|this
operator|.
name|execPbsz
operator|=
name|execPbsz
expr_stmt|;
block|}
block|}
end_class

end_unit

