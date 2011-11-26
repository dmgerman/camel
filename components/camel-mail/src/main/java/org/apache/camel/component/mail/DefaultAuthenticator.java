begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Authenticator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|PasswordAuthentication
import|;
end_import

begin_comment
comment|/**  * Mail {@link Authenticator} that supplies username and password  */
end_comment

begin_class
DECL|class|DefaultAuthenticator
specifier|public
class|class
name|DefaultAuthenticator
extends|extends
name|Authenticator
block|{
DECL|field|username
specifier|private
specifier|final
name|String
name|username
decl_stmt|;
DECL|field|password
specifier|private
specifier|final
name|String
name|password
decl_stmt|;
DECL|method|DefaultAuthenticator (String username, String password)
specifier|public
name|DefaultAuthenticator
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
comment|/**      * Returns an authenticator object for use in sessions      */
DECL|method|getPasswordAuthentication ()
specifier|public
name|PasswordAuthentication
name|getPasswordAuthentication
parameter_list|()
block|{
return|return
operator|new
name|PasswordAuthentication
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
block|}
end_class

end_unit

