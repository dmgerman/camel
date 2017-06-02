begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.base64.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|base64
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|DataFormatConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Camel Base64 data format support  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.dataformat.base64"
argument_list|)
DECL|class|Base64DataFormatConfiguration
specifier|public
class|class
name|Base64DataFormatConfiguration
extends|extends
name|DataFormatConfigurationPropertiesCommon
block|{
comment|/**      * To specific a maximum line length for the encoded data. By default 76 is      * used.      */
DECL|field|lineLength
specifier|private
name|Integer
name|lineLength
init|=
literal|76
decl_stmt|;
comment|/**      * The line separators to use. By default \r\n is used.      */
DECL|field|lineSeparator
specifier|private
name|String
name|lineSeparator
init|=
literal|"\\r\\n"
decl_stmt|;
comment|/**      * Instead of emitting '' and '/' we emit '-' and '_' respectively. urlSafe      * is only applied to encode operations. Decoding seamlessly handles both      * modes. Is by default false.      */
DECL|field|urlSafe
specifier|private
name|Boolean
name|urlSafe
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
DECL|method|getLineLength ()
specifier|public
name|Integer
name|getLineLength
parameter_list|()
block|{
return|return
name|lineLength
return|;
block|}
DECL|method|setLineLength (Integer lineLength)
specifier|public
name|void
name|setLineLength
parameter_list|(
name|Integer
name|lineLength
parameter_list|)
block|{
name|this
operator|.
name|lineLength
operator|=
name|lineLength
expr_stmt|;
block|}
DECL|method|getLineSeparator ()
specifier|public
name|String
name|getLineSeparator
parameter_list|()
block|{
return|return
name|lineSeparator
return|;
block|}
DECL|method|setLineSeparator (String lineSeparator)
specifier|public
name|void
name|setLineSeparator
parameter_list|(
name|String
name|lineSeparator
parameter_list|)
block|{
name|this
operator|.
name|lineSeparator
operator|=
name|lineSeparator
expr_stmt|;
block|}
DECL|method|getUrlSafe ()
specifier|public
name|Boolean
name|getUrlSafe
parameter_list|()
block|{
return|return
name|urlSafe
return|;
block|}
DECL|method|setUrlSafe (Boolean urlSafe)
specifier|public
name|void
name|setUrlSafe
parameter_list|(
name|Boolean
name|urlSafe
parameter_list|)
block|{
name|this
operator|.
name|urlSafe
operator|=
name|urlSafe
expr_stmt|;
block|}
DECL|method|getContentTypeHeader ()
specifier|public
name|Boolean
name|getContentTypeHeader
parameter_list|()
block|{
return|return
name|contentTypeHeader
return|;
block|}
DECL|method|setContentTypeHeader (Boolean contentTypeHeader)
specifier|public
name|void
name|setContentTypeHeader
parameter_list|(
name|Boolean
name|contentTypeHeader
parameter_list|)
block|{
name|this
operator|.
name|contentTypeHeader
operator|=
name|contentTypeHeader
expr_stmt|;
block|}
block|}
end_class

end_unit

