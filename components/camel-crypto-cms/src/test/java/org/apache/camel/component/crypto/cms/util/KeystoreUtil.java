begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|util
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
name|support
operator|.
name|jsse
operator|.
name|KeyStoreParameters
import|;
end_import

begin_class
DECL|class|KeystoreUtil
specifier|public
specifier|final
class|class
name|KeystoreUtil
block|{
DECL|method|KeystoreUtil ()
specifier|private
name|KeystoreUtil
parameter_list|()
block|{      }
DECL|method|getKeyStoreParameters (String keystoreName)
specifier|public
specifier|static
name|KeyStoreParameters
name|getKeyStoreParameters
parameter_list|(
name|String
name|keystoreName
parameter_list|)
block|{
name|KeyStoreParameters
name|keystorePas
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|keystorePas
operator|.
name|setType
argument_list|(
literal|"JCEKS"
argument_list|)
expr_stmt|;
name|keystorePas
operator|.
name|setResource
argument_list|(
literal|"keystore/"
operator|+
name|keystoreName
argument_list|)
expr_stmt|;
name|String
name|pw
decl_stmt|;
if|if
condition|(
name|keystoreName
operator|.
name|startsWith
argument_list|(
literal|"test"
argument_list|)
condition|)
block|{
name|pw
operator|=
literal|"abcd1234"
expr_stmt|;
block|}
else|else
block|{
name|pw
operator|=
literal|"Abcd1234"
expr_stmt|;
block|}
name|keystorePas
operator|.
name|setPassword
argument_list|(
name|pw
argument_list|)
expr_stmt|;
return|return
name|keystorePas
return|;
block|}
block|}
end_class

end_unit

