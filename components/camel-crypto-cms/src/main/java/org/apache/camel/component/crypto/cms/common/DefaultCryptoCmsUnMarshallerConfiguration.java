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

begin_class
annotation|@
name|UriParams
DECL|class|DefaultCryptoCmsUnMarshallerConfiguration
specifier|public
class|class
name|DefaultCryptoCmsUnMarshallerConfiguration
extends|extends
name|DefaultCryptoCmsConfiguration
implements|implements
name|CryptoCmsUnMarshallerConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"decrypt_verify"
argument_list|)
DECL|field|fromBase64
specifier|private
name|boolean
name|fromBase64
decl_stmt|;
DECL|method|DefaultCryptoCmsUnMarshallerConfiguration ()
specifier|public
name|DefaultCryptoCmsUnMarshallerConfiguration
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|isFromBase64 ()
specifier|public
name|boolean
name|isFromBase64
parameter_list|()
block|{
return|return
name|fromBase64
return|;
block|}
comment|/**      * If<tt>true</tt> then the CMS message is base 64 encoded and must be      * decoded during the processing. Default value is<code>false</code>.      */
DECL|method|setFromBase64 (boolean base64)
specifier|public
name|void
name|setFromBase64
parameter_list|(
name|boolean
name|base64
parameter_list|)
block|{
name|this
operator|.
name|fromBase64
operator|=
name|base64
expr_stmt|;
block|}
block|}
end_class

end_unit

