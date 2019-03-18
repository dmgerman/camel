begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.crypto
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|crypto
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|DefaultPGPPassphraseAccessor
specifier|public
class|class
name|DefaultPGPPassphraseAccessor
implements|implements
name|PGPPassphraseAccessor
block|{
DECL|field|userId2Passphrase
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|userId2Passphrase
decl_stmt|;
DECL|method|DefaultPGPPassphraseAccessor (Map<String, String> userId2Passphrase)
specifier|public
name|DefaultPGPPassphraseAccessor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|userId2Passphrase
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|userId2Passphrase
argument_list|,
literal|"userIdPassphrase"
argument_list|)
expr_stmt|;
name|this
operator|.
name|userId2Passphrase
operator|=
name|userId2Passphrase
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getPassphrase (String userId)
specifier|public
name|String
name|getPassphrase
parameter_list|(
name|String
name|userId
parameter_list|)
block|{
return|return
name|userId2Passphrase
operator|.
name|get
argument_list|(
name|userId
argument_list|)
return|;
block|}
block|}
end_class

end_unit

