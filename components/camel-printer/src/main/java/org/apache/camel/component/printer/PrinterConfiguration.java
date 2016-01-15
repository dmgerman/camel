begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|print
operator|.
name|DocFlavor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|standard
operator|.
name|MediaSizeName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|standard
operator|.
name|OrientationRequested
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|standard
operator|.
name|Sides
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
name|UriPath
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
name|URISupport
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|PrinterConfiguration
specifier|public
class|class
name|PrinterConfiguration
block|{
DECL|field|uri
specifier|private
name|URI
name|uri
decl_stmt|;
DECL|field|mediaSizeName
specifier|private
name|MediaSizeName
name|mediaSizeName
decl_stmt|;
DECL|field|internalSides
specifier|private
name|Sides
name|internalSides
decl_stmt|;
DECL|field|internalOrientation
specifier|private
name|OrientationRequested
name|internalOrientation
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|hostname
specifier|private
name|String
name|hostname
decl_stmt|;
annotation|@
name|UriPath
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|UriPath
DECL|field|printername
specifier|private
name|String
name|printername
decl_stmt|;
annotation|@
name|UriParam
DECL|field|printerPrefix
specifier|private
name|String
name|printerPrefix
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|copies
specifier|private
name|int
name|copies
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
DECL|field|flavor
specifier|private
name|String
name|flavor
decl_stmt|;
annotation|@
name|UriParam
DECL|field|docFlavor
specifier|private
name|DocFlavor
name|docFlavor
decl_stmt|;
annotation|@
name|UriParam
DECL|field|mimeType
specifier|private
name|String
name|mimeType
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"na-letter"
argument_list|)
DECL|field|mediaSize
specifier|private
name|String
name|mediaSize
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"one-sided"
argument_list|,
name|enums
operator|=
literal|"one-sided,duplex,tumble,two-sided-short-edge,two-sided-long-edge"
argument_list|)
DECL|field|sides
specifier|private
name|String
name|sides
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"portrait"
argument_list|,
name|enums
operator|=
literal|"portrait,landscape,reverse-portrait,reverse-landscape"
argument_list|)
DECL|field|orientation
specifier|private
name|String
name|orientation
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|sendToPrinter
specifier|private
name|boolean
name|sendToPrinter
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|mediaTray
specifier|private
name|String
name|mediaTray
decl_stmt|;
DECL|method|PrinterConfiguration ()
specifier|public
name|PrinterConfiguration
parameter_list|()
block|{     }
DECL|method|PrinterConfiguration (URI uri)
specifier|public
name|PrinterConfiguration
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|parseURI (URI uri)
specifier|public
name|void
name|parseURI
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|protocol
init|=
name|uri
operator|.
name|getScheme
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"lpr"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognized Print protocol: "
operator|+
name|protocol
operator|+
literal|" for uri: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|setHostname
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|setPort
argument_list|(
name|uri
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
comment|// use path as printer name, but without any leading slashes
name|String
name|path
init|=
name|uri
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|path
operator|=
name|ObjectHelper
operator|.
name|removeStartingCharacters
argument_list|(
name|path
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|path
operator|=
name|ObjectHelper
operator|.
name|removeStartingCharacters
argument_list|(
name|path
argument_list|,
literal|'\\'
argument_list|)
expr_stmt|;
name|setPrintername
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|printSettings
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|setFlavor
argument_list|(
operator|(
name|String
operator|)
name|printSettings
operator|.
name|get
argument_list|(
literal|"flavor"
argument_list|)
argument_list|)
expr_stmt|;
name|setMimeType
argument_list|(
operator|(
name|String
operator|)
name|printSettings
operator|.
name|get
argument_list|(
literal|"mimeType"
argument_list|)
argument_list|)
expr_stmt|;
name|setDocFlavor
argument_list|(
name|assignDocFlavor
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
argument_list|)
expr_stmt|;
name|setPrinterPrefix
argument_list|(
operator|(
name|String
operator|)
name|printSettings
operator|.
name|get
argument_list|(
literal|"printerPrefix"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|printSettings
operator|.
name|containsKey
argument_list|(
literal|"copies"
argument_list|)
condition|)
block|{
name|setCopies
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|printSettings
operator|.
name|get
argument_list|(
literal|"copies"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setMediaSize
argument_list|(
operator|(
name|String
operator|)
name|printSettings
operator|.
name|get
argument_list|(
literal|"mediaSize"
argument_list|)
argument_list|)
expr_stmt|;
name|setSides
argument_list|(
operator|(
name|String
operator|)
name|printSettings
operator|.
name|get
argument_list|(
literal|"sides"
argument_list|)
argument_list|)
expr_stmt|;
name|setOrientation
argument_list|(
operator|(
name|String
operator|)
name|printSettings
operator|.
name|get
argument_list|(
literal|"orientation"
argument_list|)
argument_list|)
expr_stmt|;
name|setMediaSizeName
argument_list|(
name|assignMediaSize
argument_list|(
name|mediaSize
argument_list|)
argument_list|)
expr_stmt|;
name|setInternalSides
argument_list|(
name|assignSides
argument_list|(
name|sides
argument_list|)
argument_list|)
expr_stmt|;
name|setInternalOrientation
argument_list|(
name|assignOrientation
argument_list|(
name|orientation
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|printSettings
operator|.
name|containsKey
argument_list|(
literal|"sendToPrinter"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
operator|(
name|Boolean
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|printSettings
operator|.
name|get
argument_list|(
literal|"sendToPrinter"
argument_list|)
argument_list|)
operator|)
condition|)
block|{
name|setSendToPrinter
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|printSettings
operator|.
name|containsKey
argument_list|(
literal|"mediaTray"
argument_list|)
condition|)
block|{
name|setMediaTray
argument_list|(
operator|(
name|String
operator|)
name|printSettings
operator|.
name|get
argument_list|(
literal|"mediaTray"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assignDocFlavor (String flavor, String mimeType)
specifier|private
name|DocFlavor
name|assignDocFlavor
parameter_list|(
name|String
name|flavor
parameter_list|,
name|String
name|mimeType
parameter_list|)
throws|throws
name|Exception
block|{
comment|// defaults
if|if
condition|(
name|mimeType
operator|==
literal|null
condition|)
block|{
name|mimeType
operator|=
literal|"AUTOSENSE"
expr_stmt|;
block|}
if|if
condition|(
name|flavor
operator|==
literal|null
condition|)
block|{
name|flavor
operator|=
literal|"DocFlavor.BYTE_ARRAY"
expr_stmt|;
block|}
name|DocFlavor
name|d
init|=
name|DocFlavor
operator|.
name|BYTE_ARRAY
operator|.
name|AUTOSENSE
decl_stmt|;
name|DocFlavorAssigner
name|docFlavorAssigner
init|=
operator|new
name|DocFlavorAssigner
argument_list|()
decl_stmt|;
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"AUTOSENSE"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeAUTOSENSE
argument_list|(
name|flavor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"GIF"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeGIF
argument_list|(
name|flavor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"JPEG"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeJPEG
argument_list|(
name|flavor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"PDF"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypePDF
argument_list|(
name|flavor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"PCL"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypePCL
argument_list|(
name|flavor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"POSTSCRIPT"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypePOSTSCRIPT
argument_list|(
name|flavor
argument_list|)
expr_stmt|;
block|}
elseif|else
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
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeHOST
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
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
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeUSASCII
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
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
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeUTF16
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
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
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeUTF16LE
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
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
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeUTF16BE
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_HTML_UTF_8"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeUTF8
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_PLAIN_HOST"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeHOST
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_PLAIN_US_ASCII"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeUSASCII
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_PLAIN_UTF_16"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeUTF16
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_PLAIN_UTF_16LE"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeUTF16LE
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_PLAIN_UTF_16BE"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeUTF16BE
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_PLAIN_UTF_8"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeUTF8
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_HTML"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeBasic
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"TEXT_PLAIN"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeBasic
argument_list|(
name|flavor
argument_list|,
name|mimeType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"PAGEABLE"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypePAGEABLE
argument_list|(
name|flavor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"PRINTABLE"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypePRINTABLE
argument_list|(
name|flavor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mimeType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"RENDERABLE_IMAGE"
argument_list|)
condition|)
block|{
name|d
operator|=
name|docFlavorAssigner
operator|.
name|forMimeTypeRENDERABLEIMAGE
argument_list|(
name|flavor
argument_list|)
expr_stmt|;
block|}
return|return
name|d
return|;
block|}
DECL|method|assignMediaSize (String size)
specifier|private
name|MediaSizeName
name|assignMediaSize
parameter_list|(
name|String
name|size
parameter_list|)
block|{
name|MediaSizeAssigner
name|mediaSizeAssigner
init|=
operator|new
name|MediaSizeAssigner
argument_list|()
decl_stmt|;
name|MediaSizeName
name|answer
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|null
condition|)
block|{
comment|// default to NA letter if no size configured
name|answer
operator|=
name|MediaSizeName
operator|.
name|NA_LETTER
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|toLowerCase
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"iso"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|mediaSizeAssigner
operator|.
name|selectMediaSizeNameISO
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|startsWith
argument_list|(
literal|"jis"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|mediaSizeAssigner
operator|.
name|selectMediaSizeNameJIS
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|size
operator|.
name|startsWith
argument_list|(
literal|"na"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|mediaSizeAssigner
operator|.
name|selectMediaSizeNameNA
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|mediaSizeAssigner
operator|.
name|selectMediaSizeNameOther
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|assignSides (String sidesString)
specifier|public
name|Sides
name|assignSides
parameter_list|(
name|String
name|sidesString
parameter_list|)
block|{
name|Sides
name|answer
decl_stmt|;
if|if
condition|(
name|sidesString
operator|==
literal|null
condition|)
block|{
comment|// default to one side if no slides configured
name|answer
operator|=
name|Sides
operator|.
name|ONE_SIDED
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sidesString
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"one-sided"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|Sides
operator|.
name|ONE_SIDED
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sidesString
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"duplex"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|Sides
operator|.
name|DUPLEX
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sidesString
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"tumble"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|Sides
operator|.
name|TUMBLE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sidesString
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"two-sided-short-edge"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|Sides
operator|.
name|TWO_SIDED_SHORT_EDGE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sidesString
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"two-sided-long-edge"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|Sides
operator|.
name|TWO_SIDED_LONG_EDGE
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|Sides
operator|.
name|ONE_SIDED
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|assignOrientation (final String orientation)
specifier|public
name|OrientationRequested
name|assignOrientation
parameter_list|(
specifier|final
name|String
name|orientation
parameter_list|)
block|{
name|OrientationRequested
name|answer
decl_stmt|;
if|if
condition|(
name|orientation
operator|==
literal|null
condition|)
block|{
comment|// default to portrait
name|answer
operator|=
name|OrientationRequested
operator|.
name|PORTRAIT
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|orientation
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"portrait"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|OrientationRequested
operator|.
name|PORTRAIT
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|orientation
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"landscape"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|OrientationRequested
operator|.
name|LANDSCAPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|orientation
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"reverse-portrait"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|OrientationRequested
operator|.
name|REVERSE_PORTRAIT
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|orientation
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"reverse-landscape"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|OrientationRequested
operator|.
name|REVERSE_LANDSCAPE
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|OrientationRequested
operator|.
name|PORTRAIT
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getUri ()
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|setUri (URI uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getHostname ()
specifier|public
name|String
name|getHostname
parameter_list|()
block|{
return|return
name|hostname
return|;
block|}
comment|/**      * Hostname of the printer      */
DECL|method|setHostname (String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * Port number of the printer      */
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getPrintername ()
specifier|public
name|String
name|getPrintername
parameter_list|()
block|{
return|return
name|printername
return|;
block|}
comment|/**      * Name of the printer      */
DECL|method|setPrintername (String printername)
specifier|public
name|void
name|setPrintername
parameter_list|(
name|String
name|printername
parameter_list|)
block|{
name|this
operator|.
name|printername
operator|=
name|printername
expr_stmt|;
block|}
DECL|method|getCopies ()
specifier|public
name|int
name|getCopies
parameter_list|()
block|{
return|return
name|copies
return|;
block|}
comment|/**      * Number of copies to print      */
DECL|method|setCopies (int copies)
specifier|public
name|void
name|setCopies
parameter_list|(
name|int
name|copies
parameter_list|)
block|{
name|this
operator|.
name|copies
operator|=
name|copies
expr_stmt|;
block|}
DECL|method|getFlavor ()
specifier|public
name|String
name|getFlavor
parameter_list|()
block|{
return|return
name|flavor
return|;
block|}
comment|/**      * Sets DocFlavor to use.      */
DECL|method|setFlavor (String flavor)
specifier|public
name|void
name|setFlavor
parameter_list|(
name|String
name|flavor
parameter_list|)
block|{
name|this
operator|.
name|flavor
operator|=
name|flavor
expr_stmt|;
block|}
DECL|method|getDocFlavor ()
specifier|public
name|DocFlavor
name|getDocFlavor
parameter_list|()
block|{
return|return
name|docFlavor
return|;
block|}
comment|/**      * Sets DocFlavor to use.      */
DECL|method|setDocFlavor (DocFlavor docFlavor)
specifier|public
name|void
name|setDocFlavor
parameter_list|(
name|DocFlavor
name|docFlavor
parameter_list|)
block|{
name|this
operator|.
name|docFlavor
operator|=
name|docFlavor
expr_stmt|;
block|}
DECL|method|getMediaSize ()
specifier|public
name|String
name|getMediaSize
parameter_list|()
block|{
return|return
name|mediaSize
return|;
block|}
comment|/**      * Sets the stationary as defined by enumeration names in the javax.print.attribute.standard.MediaSizeName API.      * The default setting is to use North American Letter sized stationary.      * The value's case is ignored, e.g. values of iso_a4 and ISO_A4 may be used.      */
DECL|method|setMediaSize (String mediaSize)
specifier|public
name|void
name|setMediaSize
parameter_list|(
name|String
name|mediaSize
parameter_list|)
block|{
name|this
operator|.
name|mediaSize
operator|=
name|mediaSize
expr_stmt|;
block|}
DECL|method|getSides ()
specifier|public
name|String
name|getSides
parameter_list|()
block|{
return|return
name|sides
return|;
block|}
comment|/**      * Sets one sided or two sided printing based on the javax.print.attribute.standard.Sides API      */
DECL|method|setSides (String sides)
specifier|public
name|void
name|setSides
parameter_list|(
name|String
name|sides
parameter_list|)
block|{
name|this
operator|.
name|sides
operator|=
name|sides
expr_stmt|;
block|}
DECL|method|getMediaSizeName ()
specifier|public
name|MediaSizeName
name|getMediaSizeName
parameter_list|()
block|{
return|return
name|mediaSizeName
return|;
block|}
DECL|method|setMediaSizeName (MediaSizeName mediaSizeName)
specifier|public
name|void
name|setMediaSizeName
parameter_list|(
name|MediaSizeName
name|mediaSizeName
parameter_list|)
block|{
name|this
operator|.
name|mediaSizeName
operator|=
name|mediaSizeName
expr_stmt|;
block|}
DECL|method|getInternalSides ()
specifier|public
name|Sides
name|getInternalSides
parameter_list|()
block|{
return|return
name|internalSides
return|;
block|}
DECL|method|setInternalSides (Sides internalSides)
specifier|public
name|void
name|setInternalSides
parameter_list|(
name|Sides
name|internalSides
parameter_list|)
block|{
name|this
operator|.
name|internalSides
operator|=
name|internalSides
expr_stmt|;
block|}
DECL|method|getInternalOrientation ()
specifier|public
name|OrientationRequested
name|getInternalOrientation
parameter_list|()
block|{
return|return
name|internalOrientation
return|;
block|}
DECL|method|setInternalOrientation (OrientationRequested internalOrientation)
specifier|public
name|void
name|setInternalOrientation
parameter_list|(
name|OrientationRequested
name|internalOrientation
parameter_list|)
block|{
name|this
operator|.
name|internalOrientation
operator|=
name|internalOrientation
expr_stmt|;
block|}
DECL|method|getOrientation ()
specifier|public
name|String
name|getOrientation
parameter_list|()
block|{
return|return
name|orientation
return|;
block|}
comment|/**      * Sets the page orientation.      */
DECL|method|setOrientation (String orientation)
specifier|public
name|void
name|setOrientation
parameter_list|(
name|String
name|orientation
parameter_list|)
block|{
name|this
operator|.
name|orientation
operator|=
name|orientation
expr_stmt|;
block|}
DECL|method|getMimeType ()
specifier|public
name|String
name|getMimeType
parameter_list|()
block|{
return|return
name|mimeType
return|;
block|}
comment|/**      * Sets mimeTypes supported by the javax.print.DocFlavor API      */
DECL|method|setMimeType (String mimeType)
specifier|public
name|void
name|setMimeType
parameter_list|(
name|String
name|mimeType
parameter_list|)
block|{
name|this
operator|.
name|mimeType
operator|=
name|mimeType
expr_stmt|;
block|}
DECL|method|isSendToPrinter ()
specifier|public
name|boolean
name|isSendToPrinter
parameter_list|()
block|{
return|return
name|sendToPrinter
return|;
block|}
comment|/**      * etting this option to false prevents sending of the print data to the printer      */
DECL|method|setSendToPrinter (boolean sendToPrinter)
specifier|public
name|void
name|setSendToPrinter
parameter_list|(
name|boolean
name|sendToPrinter
parameter_list|)
block|{
name|this
operator|.
name|sendToPrinter
operator|=
name|sendToPrinter
expr_stmt|;
block|}
DECL|method|getMediaTray ()
specifier|public
name|String
name|getMediaTray
parameter_list|()
block|{
return|return
name|mediaTray
return|;
block|}
comment|/**      * Sets MediaTray supported by the javax.print.DocFlavor API, for example upper,middle etc.      */
DECL|method|setMediaTray (String mediaTray)
specifier|public
name|void
name|setMediaTray
parameter_list|(
name|String
name|mediaTray
parameter_list|)
block|{
name|this
operator|.
name|mediaTray
operator|=
name|mediaTray
expr_stmt|;
block|}
DECL|method|getPrinterPrefix ()
specifier|public
name|String
name|getPrinterPrefix
parameter_list|()
block|{
return|return
name|printerPrefix
return|;
block|}
comment|/**      * Sets the prefix name of the printer, it is useful when the printer name does not start with //hostname/printer      */
DECL|method|setPrinterPrefix (String printerPrefix)
specifier|public
name|void
name|setPrinterPrefix
parameter_list|(
name|String
name|printerPrefix
parameter_list|)
block|{
name|this
operator|.
name|printerPrefix
operator|=
name|printerPrefix
expr_stmt|;
block|}
block|}
end_class

end_unit

