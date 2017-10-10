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
comment|/**  * The Zip File data format is a message compression and de-compression format of zip files.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.11.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation,file"
argument_list|,
name|title
operator|=
literal|"Zip File"
argument_list|)
comment|// TODO: use zipfile as name in Camel 3.0
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"zipFile"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ZipFileDataFormat
specifier|public
class|class
name|ZipFileDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|usingIterator
specifier|private
name|Boolean
name|usingIterator
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|allowEmptyDirectory
specifier|private
name|Boolean
name|allowEmptyDirectory
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|preservePathElements
specifier|private
name|Boolean
name|preservePathElements
decl_stmt|;
DECL|method|ZipFileDataFormat ()
specifier|public
name|ZipFileDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"zipfile"
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
name|usingIterator
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
literal|"usingIterator"
argument_list|,
name|usingIterator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|allowEmptyDirectory
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
literal|"allowEmptyDirectory"
argument_list|,
name|allowEmptyDirectory
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|preservePathElements
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
literal|"preservePathElements"
argument_list|,
name|preservePathElements
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getUsingIterator ()
specifier|public
name|Boolean
name|getUsingIterator
parameter_list|()
block|{
return|return
name|usingIterator
return|;
block|}
DECL|method|getAllowEmptyDirectory ()
specifier|public
name|Boolean
name|getAllowEmptyDirectory
parameter_list|()
block|{
return|return
name|allowEmptyDirectory
return|;
block|}
DECL|method|getPreservePathElements ()
specifier|public
name|Boolean
name|getPreservePathElements
parameter_list|()
block|{
return|return
name|preservePathElements
return|;
block|}
comment|/**      * If the zip file has more then one entry, the setting this option to true, allows to work with the splitter EIP,      * to split the data using an iterator in a streaming mode.      */
DECL|method|setUsingIterator (Boolean usingIterator)
specifier|public
name|void
name|setUsingIterator
parameter_list|(
name|Boolean
name|usingIterator
parameter_list|)
block|{
name|this
operator|.
name|usingIterator
operator|=
name|usingIterator
expr_stmt|;
block|}
comment|/**      * If the zip file has more then one entry, setting this option to true, allows to get the iterator      * even if the directory is empty      */
DECL|method|setAllowEmptyDirectory (Boolean allowEmptyDirectory)
specifier|public
name|void
name|setAllowEmptyDirectory
parameter_list|(
name|Boolean
name|allowEmptyDirectory
parameter_list|)
block|{
name|this
operator|.
name|allowEmptyDirectory
operator|=
name|allowEmptyDirectory
expr_stmt|;
block|}
comment|/**      * If the file name contains path elements, setting this option to true, allows the path to be maintained      * in the zip file.      */
DECL|method|setPreservePathElements (Boolean preservePathElements)
specifier|public
name|void
name|setPreservePathElements
parameter_list|(
name|Boolean
name|preservePathElements
parameter_list|)
block|{
name|this
operator|.
name|preservePathElements
operator|=
name|preservePathElements
expr_stmt|;
block|}
block|}
end_class

end_unit

