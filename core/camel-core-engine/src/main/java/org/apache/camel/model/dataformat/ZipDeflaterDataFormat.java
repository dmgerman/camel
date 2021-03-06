begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Zip Deflate Compression data format is a message compression and  * de-compression format (not zip files).  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.12.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation"
argument_list|,
name|title
operator|=
literal|"Zip Deflate Compression"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"zipdeflater"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ZipDeflaterDataFormat
specifier|public
class|class
name|ZipDeflaterDataFormat
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
literal|"-1"
argument_list|)
DECL|field|compressionLevel
specifier|private
name|Integer
name|compressionLevel
decl_stmt|;
DECL|method|ZipDeflaterDataFormat ()
specifier|public
name|ZipDeflaterDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"zipdeflater"
argument_list|)
expr_stmt|;
block|}
DECL|method|getCompressionLevel ()
specifier|public
name|Integer
name|getCompressionLevel
parameter_list|()
block|{
return|return
name|compressionLevel
return|;
block|}
comment|/**      * To specify a specific compression between 0-9. -1 is default compression,      * 0 is no compression, and 9 is best compression.      */
DECL|method|setCompressionLevel (Integer compressionLevel)
specifier|public
name|void
name|setCompressionLevel
parameter_list|(
name|Integer
name|compressionLevel
parameter_list|)
block|{
name|this
operator|.
name|compressionLevel
operator|=
name|compressionLevel
expr_stmt|;
block|}
block|}
end_class

end_unit

