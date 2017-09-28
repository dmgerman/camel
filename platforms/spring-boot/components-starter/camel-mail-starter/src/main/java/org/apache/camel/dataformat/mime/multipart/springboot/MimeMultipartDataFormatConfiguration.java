begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.mime.multipart.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|mime
operator|.
name|multipart
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
comment|/**  * The MIME Multipart data format can marshal a Camel message with attachments  * into a Camel message having a MIME-Multipart message as message body (and no  * attachments) and vise-versa when unmarshalling.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.dataformat.mime-multipart"
argument_list|)
DECL|class|MimeMultipartDataFormatConfiguration
specifier|public
class|class
name|MimeMultipartDataFormatConfiguration
extends|extends
name|DataFormatConfigurationPropertiesCommon
block|{
comment|/**      * Specify the subtype of the MIME Multipart. Default is mixed.      */
DECL|field|multipartSubType
specifier|private
name|String
name|multipartSubType
init|=
literal|"mixed"
decl_stmt|;
comment|/**      * Defines whether a message without attachment is also marshaled into a      * MIME Multipart (with only one body part). Default is false.      */
DECL|field|multipartWithoutAttachment
specifier|private
name|Boolean
name|multipartWithoutAttachment
init|=
literal|false
decl_stmt|;
comment|/**      * Defines whether the MIME-Multipart headers are part of the message body      * (true) or are set as Camel headers (false). Default is false.      */
DECL|field|headersInline
specifier|private
name|Boolean
name|headersInline
init|=
literal|false
decl_stmt|;
comment|/**      * A regex that defines which Camel headers are also included as MIME      * headers into the MIME multipart. This will only work if headersInline is      * set to true. Default is to include no headers      */
DECL|field|includeHeaders
specifier|private
name|String
name|includeHeaders
decl_stmt|;
comment|/**      * Defines whether the content of binary parts in the MIME multipart is      * binary (true) or Base-64 encoded (false) Default is false.      */
DECL|field|binaryContent
specifier|private
name|Boolean
name|binaryContent
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
DECL|method|getMultipartSubType ()
specifier|public
name|String
name|getMultipartSubType
parameter_list|()
block|{
return|return
name|multipartSubType
return|;
block|}
DECL|method|setMultipartSubType (String multipartSubType)
specifier|public
name|void
name|setMultipartSubType
parameter_list|(
name|String
name|multipartSubType
parameter_list|)
block|{
name|this
operator|.
name|multipartSubType
operator|=
name|multipartSubType
expr_stmt|;
block|}
DECL|method|getMultipartWithoutAttachment ()
specifier|public
name|Boolean
name|getMultipartWithoutAttachment
parameter_list|()
block|{
return|return
name|multipartWithoutAttachment
return|;
block|}
DECL|method|setMultipartWithoutAttachment (Boolean multipartWithoutAttachment)
specifier|public
name|void
name|setMultipartWithoutAttachment
parameter_list|(
name|Boolean
name|multipartWithoutAttachment
parameter_list|)
block|{
name|this
operator|.
name|multipartWithoutAttachment
operator|=
name|multipartWithoutAttachment
expr_stmt|;
block|}
DECL|method|getHeadersInline ()
specifier|public
name|Boolean
name|getHeadersInline
parameter_list|()
block|{
return|return
name|headersInline
return|;
block|}
DECL|method|setHeadersInline (Boolean headersInline)
specifier|public
name|void
name|setHeadersInline
parameter_list|(
name|Boolean
name|headersInline
parameter_list|)
block|{
name|this
operator|.
name|headersInline
operator|=
name|headersInline
expr_stmt|;
block|}
DECL|method|getIncludeHeaders ()
specifier|public
name|String
name|getIncludeHeaders
parameter_list|()
block|{
return|return
name|includeHeaders
return|;
block|}
DECL|method|setIncludeHeaders (String includeHeaders)
specifier|public
name|void
name|setIncludeHeaders
parameter_list|(
name|String
name|includeHeaders
parameter_list|)
block|{
name|this
operator|.
name|includeHeaders
operator|=
name|includeHeaders
expr_stmt|;
block|}
DECL|method|getBinaryContent ()
specifier|public
name|Boolean
name|getBinaryContent
parameter_list|()
block|{
return|return
name|binaryContent
return|;
block|}
DECL|method|setBinaryContent (Boolean binaryContent)
specifier|public
name|void
name|setBinaryContent
parameter_list|(
name|Boolean
name|binaryContent
parameter_list|)
block|{
name|this
operator|.
name|binaryContent
operator|=
name|binaryContent
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

