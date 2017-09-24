begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|CamelContext
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
name|model
operator|.
name|DataFormatDefinition
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
name|DataFormat
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * The MIME Multipart data format can marshal a Camel message with attachments into a Camel message  * having a MIME-Multipart message as message body (and no attachments), and vise-versa when unmarshalling.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.17.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation"
argument_list|,
name|title
operator|=
literal|"MIME Multipart"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"mime-multipart"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|MimeMultipartDataFormat
specifier|public
class|class
name|MimeMultipartDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"mixed"
argument_list|)
DECL|field|multipartSubType
specifier|private
name|String
name|multipartSubType
init|=
literal|"mixed"
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|multipartWithoutAttachment
specifier|private
name|Boolean
name|multipartWithoutAttachment
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|headersInline
specifier|private
name|Boolean
name|headersInline
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|includeHeaders
specifier|private
name|String
name|includeHeaders
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|binaryContent
specifier|private
name|Boolean
name|binaryContent
decl_stmt|;
DECL|method|MimeMultipartDataFormat ()
specifier|public
name|MimeMultipartDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"mime-multipart"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureDataFormat (DataFormat dataFormat, CamelContext camelContext)
specifier|protected
name|void
name|configureDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|getMultipartSubType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"multipartSubType"
argument_list|,
name|getMultipartSubType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getMultipartWithoutAttachment
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"multipartWithoutAttachment"
argument_list|,
name|getMultipartWithoutAttachment
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getHeadersInline
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"headersInline"
argument_list|,
name|getHeadersInline
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getIncludeHeaders
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"includeHeaders"
argument_list|,
name|getIncludeHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getBinaryContent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"binaryContent"
argument_list|,
name|getBinaryContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|/**      * Specify the subtype of the MIME Multipart.      *<p>      * Default is "mixed".      */
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
comment|/**      * Defines whether a message without attachment is also marshaled into a      * MIME Multipart (with only one body part).      *<p>      * Default is "false".      */
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
comment|/**      * Defines whether the MIME-Multipart headers are part of the message body      * (true) or are set as Camel headers (false).      *<p>      * Default is "false".      */
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
comment|/**      * A regex that defines which Camel headers are also included as MIME headers      * into the MIME multipart. This will only work if headersInline is set to true.      *<p>      * Default is to include no headers      */
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
comment|/**      * Defines whether the content of binary parts in the MIME multipart is      * binary (true) or Base-64 encoded (false)      *<p>      * Default is "false".      */
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
block|}
end_class

end_unit

