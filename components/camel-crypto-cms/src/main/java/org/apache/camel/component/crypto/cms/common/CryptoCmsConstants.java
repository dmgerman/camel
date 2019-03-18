begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms.common
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
name|common
package|;
end_package

begin_class
DECL|class|CryptoCmsConstants
specifier|public
specifier|final
class|class
name|CryptoCmsConstants
block|{
comment|/**      * Camel message header name for the CMS Signed Data. If in the camel      * component uri like "crypto-cms:sign://basic?includeContent=false"      * contains the option includeContent=false. Then the CMS Signed Data object      * is written into the CamelCryptoCmsSignedData header.      */
DECL|field|CAMEL_CRYPTO_CMS_SIGNED_DATA
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_CRYPTO_CMS_SIGNED_DATA
init|=
literal|"CamelCryptoCmsSignedData"
decl_stmt|;
DECL|method|CryptoCmsConstants ()
specifier|private
name|CryptoCmsConstants
parameter_list|()
block|{
comment|// no instance
block|}
block|}
end_class

end_unit

