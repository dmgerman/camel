begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmlsecurity.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmlsecurity
operator|.
name|util
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStore
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|KeySelector
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
name|xmlsecurity
operator|.
name|api
operator|.
name|KeyAccessor
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
name|xmlsecurity
operator|.
name|api
operator|.
name|DefaultKeyAccessor
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
name|xmlsecurity
operator|.
name|api
operator|.
name|DefaultKeySelector
import|;
end_import

begin_class
DECL|class|TestKeystore
specifier|public
class|class
name|TestKeystore
block|{
DECL|method|TestKeystore ()
specifier|public
name|TestKeystore
parameter_list|()
block|{      }
DECL|method|getKeyAccessor (String alias)
specifier|public
specifier|static
name|KeyAccessor
name|getKeyAccessor
parameter_list|(
name|String
name|alias
parameter_list|)
throws|throws
name|Exception
block|{
name|DefaultKeyAccessor
name|accessor
init|=
operator|new
name|DefaultKeyAccessor
argument_list|()
decl_stmt|;
name|accessor
operator|.
name|setKeyStore
argument_list|(
name|getKeyStore
argument_list|()
argument_list|)
expr_stmt|;
name|accessor
operator|.
name|setPassword
argument_list|(
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|accessor
operator|.
name|setAlias
argument_list|(
name|alias
argument_list|)
expr_stmt|;
return|return
name|accessor
return|;
block|}
DECL|method|getKeySelector (String alias)
specifier|public
specifier|static
name|KeySelector
name|getKeySelector
parameter_list|(
name|String
name|alias
parameter_list|)
throws|throws
name|Exception
block|{
name|DefaultKeySelector
name|selector
init|=
operator|new
name|DefaultKeySelector
argument_list|()
decl_stmt|;
name|selector
operator|.
name|setKeyStore
argument_list|(
name|getKeyStore
argument_list|()
argument_list|)
expr_stmt|;
name|selector
operator|.
name|setAlias
argument_list|(
name|alias
argument_list|)
expr_stmt|;
return|return
name|selector
return|;
block|}
DECL|method|getPassword ()
specifier|private
specifier|static
name|char
index|[]
name|getPassword
parameter_list|()
block|{
return|return
literal|"abcd1234"
operator|.
name|toCharArray
argument_list|()
return|;
block|}
DECL|method|getKeyStore ()
specifier|private
specifier|static
name|KeyStore
name|getKeyStore
parameter_list|()
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|KeyStore
name|ks
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|TestKeystore
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"org/apache/camel/component/xmlsecurity/keystore.jks"
argument_list|)
decl_stmt|;
name|ks
operator|.
name|load
argument_list|(
name|is
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|ks
return|;
block|}
block|}
end_class

end_unit

