begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.printer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|printer
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|DocFlavor
import|;
end_import

begin_class
DECL|class|DocFlavorAssigner
specifier|public
class|class
name|DocFlavorAssigner
block|{
DECL|field|d
specifier|private
name|DocFlavor
name|d
init|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|AUTOSENSE
decl_stmt|;
DECL|method|forMimeTypeAUTOSENSE (String flavor)
specifier|public
name|DocFlavor
name|forMimeTypeAUTOSENSE
parameter_list|(
name|String
name|flavor
parameter_list|)
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|AUTOSENSE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|AUTOSENSE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|AUTOSENSE
expr_stmt|;
block|}
return|return
name|d
return|;
block|}
DECL|method|forMimeTypeGIF (String flavor)
specifier|public
name|DocFlavor
name|forMimeTypeGIF
parameter_list|(
name|String
name|flavor
parameter_list|)
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|GIF
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|GIF
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|GIF
expr_stmt|;
block|}
return|return
name|d
return|;
block|}
DECL|method|forMimeTypeJPEG (String flavor)
specifier|public
name|DocFlavor
name|forMimeTypeJPEG
parameter_list|(
name|String
name|flavor
parameter_list|)
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|JPEG
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|JPEG
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|JPEG
expr_stmt|;
block|}
return|return
name|d
return|;
block|}
DECL|method|forMimeTypePDF (String flavor)
specifier|public
name|DocFlavor
name|forMimeTypePDF
parameter_list|(
name|String
name|flavor
parameter_list|)
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|PDF
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|PDF
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|PDF
expr_stmt|;
block|}
return|return
name|d
return|;
block|}
DECL|method|forMimeTypePCL (String flavor)
specifier|public
name|DocFlavor
name|forMimeTypePCL
parameter_list|(
name|String
name|flavor
parameter_list|)
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|PCL
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|PCL
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|PCL
expr_stmt|;
block|}
return|return
name|d
return|;
block|}
DECL|method|forMimeTypePOSTSCRIPT (String flavor)
specifier|public
name|DocFlavor
name|forMimeTypePOSTSCRIPT
parameter_list|(
name|String
name|flavor
parameter_list|)
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|POSTSCRIPT
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|POSTSCRIPT
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|POSTSCRIPT
expr_stmt|;
block|}
return|return
name|d
return|;
block|}
DECL|method|forMimeTypeHOST (String flavor, String mimeType)
specifier|public
name|DocFlavor
name|forMimeTypeHOST
parameter_list|(
name|String
name|flavor
parameter_list|,
name|String
name|mimeType
parameter_list|)
block|{
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_HTML_HOST"
argument_list|)
condition|)
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|TEXT_HTML_HOST
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|TEXT_HTML_HOST
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|TEXT_HTML_HOST
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|TEXT_PLAIN_HOST
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|TEXT_PLAIN_HOST
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|TEXT_PLAIN_HOST
expr_stmt|;
block|}
block|}
return|return
name|d
return|;
block|}
DECL|method|forMimeTypeUSASCII (String flavor, String mimeType)
specifier|public
name|DocFlavor
name|forMimeTypeUSASCII
parameter_list|(
name|String
name|flavor
parameter_list|,
name|String
name|mimeType
parameter_list|)
block|{
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_HTML_US_ASCII"
argument_list|)
condition|)
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|TEXT_HTML_US_ASCII
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|TEXT_HTML_US_ASCII
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|TEXT_HTML_US_ASCII
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|TEXT_PLAIN_US_ASCII
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|TEXT_PLAIN_US_ASCII
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|TEXT_PLAIN_US_ASCII
expr_stmt|;
block|}
block|}
return|return
name|d
return|;
block|}
DECL|method|forMimeTypeUTF16 (String flavor, String mimeType)
specifier|public
name|DocFlavor
name|forMimeTypeUTF16
parameter_list|(
name|String
name|flavor
parameter_list|,
name|String
name|mimeType
parameter_list|)
block|{
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_HTML_UTF_16"
argument_list|)
condition|)
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|TEXT_HTML_UTF_16
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|TEXT_HTML_UTF_16
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|TEXT_HTML_UTF_16
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|TEXT_PLAIN_UTF_16
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|TEXT_PLAIN_UTF_16
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|TEXT_PLAIN_UTF_16
expr_stmt|;
block|}
block|}
return|return
name|d
return|;
block|}
DECL|method|forMimeTypeUTF16LE (String flavor, String mimeType)
specifier|public
name|DocFlavor
name|forMimeTypeUTF16LE
parameter_list|(
name|String
name|flavor
parameter_list|,
name|String
name|mimeType
parameter_list|)
block|{
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_HTML_UTF_16LE"
argument_list|)
condition|)
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|TEXT_HTML_UTF_16LE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|TEXT_HTML_UTF_16LE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|TEXT_HTML_UTF_16LE
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|TEXT_PLAIN_UTF_16LE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|TEXT_PLAIN_UTF_16LE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|TEXT_PLAIN_UTF_16LE
expr_stmt|;
block|}
block|}
return|return
name|d
return|;
block|}
DECL|method|forMimeTypeUTF16BE (String flavor, String mimeType)
specifier|public
name|DocFlavor
name|forMimeTypeUTF16BE
parameter_list|(
name|String
name|flavor
parameter_list|,
name|String
name|mimeType
parameter_list|)
block|{
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_HTML_UTF_16BE"
argument_list|)
condition|)
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|TEXT_HTML_UTF_16BE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|TEXT_HTML_UTF_16BE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|TEXT_HTML_UTF_16BE
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|TEXT_PLAIN_UTF_16BE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|TEXT_PLAIN_UTF_16BE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|TEXT_PLAIN_UTF_16BE
expr_stmt|;
block|}
block|}
return|return
name|d
return|;
block|}
DECL|method|forMimeTypeUTF8 (String flavor, String mimeType)
specifier|public
name|DocFlavor
name|forMimeTypeUTF8
parameter_list|(
name|String
name|flavor
parameter_list|,
name|String
name|mimeType
parameter_list|)
block|{
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_HTML_UTF_16BE"
argument_list|)
condition|)
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|TEXT_HTML_UTF_8
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|TEXT_HTML_UTF_8
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|TEXT_HTML_UTF_8
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.BYTE_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|TEXT_PLAIN_UTF_8
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.INPUT_STREAM"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|INPUT_STREAM
operator|.
name|TEXT_PLAIN_UTF_8
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.URL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|URL
operator|.
name|TEXT_PLAIN_UTF_8
expr_stmt|;
block|}
block|}
return|return
name|d
return|;
block|}
DECL|method|forMimeTypeBasic (String flavor, String mimeType)
specifier|public
name|DocFlavor
name|forMimeTypeBasic
parameter_list|(
name|String
name|flavor
parameter_list|,
name|String
name|mimeType
parameter_list|)
block|{
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_HTML_UTF_16BE"
argument_list|)
condition|)
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.CHAR_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|CHAR_ARRAY
operator|.
name|TEXT_HTML
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.READER"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|READER
operator|.
name|TEXT_HTML
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.STRING"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|STRING
operator|.
name|TEXT_HTML
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.CHAR_ARRAY"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|CHAR_ARRAY
operator|.
name|TEXT_PLAIN
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.READER"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|READER
operator|.
name|TEXT_PLAIN
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|flavor
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DocFlavor.STRING"
argument_list|)
condition|)
block|{
name|d
operator|=
name|DocFlavor
operator|.
name|STRING
operator|.
name|TEXT_PLAIN
expr_stmt|;
block|}
block|}
return|return
name|d
return|;
block|}
DECL|method|forMimeTypePAGEABLE (String flavor)
specifier|public
name|DocFlavor
name|forMimeTypePAGEABLE
parameter_list|(
name|String
name|flavor
parameter_list|)
block|{
return|return
name|d
operator|=
name|DocFlavor
operator|.
name|SERVICE_FORMATTED
operator|.
name|PAGEABLE
return|;
block|}
DECL|method|forMimeTypePRINTABLE (String flavor)
specifier|public
name|DocFlavor
name|forMimeTypePRINTABLE
parameter_list|(
name|String
name|flavor
parameter_list|)
block|{
return|return
name|d
operator|=
name|DocFlavor
operator|.
name|SERVICE_FORMATTED
operator|.
name|PRINTABLE
return|;
block|}
DECL|method|forMimeTypeRENDERABLEIMAGE (String flavor)
specifier|public
name|DocFlavor
name|forMimeTypeRENDERABLEIMAGE
parameter_list|(
name|String
name|flavor
parameter_list|)
block|{
return|return
name|d
operator|=
name|DocFlavor
operator|.
name|SERVICE_FORMATTED
operator|.
name|RENDERABLE_IMAGE
return|;
block|}
block|}
end_class

end_unit

