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
comment|/**  * Abstract base class for unit testing using a secure FTP Server over TLS (explicit)  * and with client authentication.  */
end_comment

begin_class
DECL|class|FtpsServerExplicitTLSWithClientAuthTestSupport
specifier|public
specifier|abstract
class|class
name|FtpsServerExplicitTLSWithClientAuthTestSupport
extends|extends
name|FtpsServerTestSupport
block|{
comment|/*      * @see org.apache.camel.component.file.remote.FtpServerSecureTestSupport#getClientAuth()      */
annotation|@
name|Override
DECL|method|getClientAuth ()
specifier|protected
name|String
name|getClientAuth
parameter_list|()
block|{
return|return
literal|"true"
return|;
block|}
comment|/*      * @see org.apache.camel.component.file.remote.FtpServerSecureTestSupport#useImplicit()      */
annotation|@
name|Override
DECL|method|useImplicit ()
specifier|protected
name|boolean
name|useImplicit
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/*      * @see org.apache.camel.component.file.remote.FtpServerSecureTestSupport#getAuthValue()      */
annotation|@
name|Override
DECL|method|getAuthValue ()
specifier|protected
name|String
name|getAuthValue
parameter_list|()
block|{
return|return
name|AUTH_VALUE_TLS
return|;
block|}
block|}
end_class

end_unit

