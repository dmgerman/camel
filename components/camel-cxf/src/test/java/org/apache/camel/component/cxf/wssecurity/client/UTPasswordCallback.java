begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.wssecurity.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|wssecurity
operator|.
name|client
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
name|util
operator|.
name|HashMap
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
name|callback
operator|.
name|Callback
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
name|callback
operator|.
name|CallbackHandler
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
name|callback
operator|.
name|UnsupportedCallbackException
import|;
end_import

begin_class
DECL|class|UTPasswordCallback
specifier|public
class|class
name|UTPasswordCallback
implements|implements
name|CallbackHandler
block|{
DECL|field|passwords
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|passwords
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|UTPasswordCallback ()
specifier|public
name|UTPasswordCallback
parameter_list|()
block|{
name|passwords
operator|.
name|put
argument_list|(
literal|"Alice"
argument_list|,
literal|"ecilA"
argument_list|)
expr_stmt|;
name|passwords
operator|.
name|put
argument_list|(
literal|"abcd"
argument_list|,
literal|"dcba"
argument_list|)
expr_stmt|;
name|passwords
operator|.
name|put
argument_list|(
literal|"alice"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|passwords
operator|.
name|put
argument_list|(
literal|"bob"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Here, we attempt to get the password from the private      * alias/passwords map.      */
annotation|@
name|Override
DECL|method|handle (Callback[] callbacks)
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|Callback
name|callback
range|:
name|callbacks
control|)
block|{
try|try
block|{
name|String
name|id
init|=
operator|(
name|String
operator|)
name|callback
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getIdentifier"
argument_list|)
operator|.
name|invoke
argument_list|(
name|callback
argument_list|)
decl_stmt|;
name|String
name|pass
init|=
name|passwords
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|pass
operator|!=
literal|null
condition|)
block|{
name|callback
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"setPassword"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|invoke
argument_list|(
name|callback
argument_list|,
name|pass
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|UnsupportedCallbackException
name|e
init|=
operator|new
name|UnsupportedCallbackException
argument_list|(
name|callback
argument_list|)
decl_stmt|;
name|e
operator|.
name|initCause
argument_list|(
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
comment|/**      * Add an alias/password pair to the callback mechanism.      */
DECL|method|setAliasPassword (String alias, String password)
specifier|public
name|void
name|setAliasPassword
parameter_list|(
name|String
name|alias
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|passwords
operator|.
name|put
argument_list|(
name|alias
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

