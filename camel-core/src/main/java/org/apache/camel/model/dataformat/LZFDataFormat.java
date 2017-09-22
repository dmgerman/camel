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
comment|/**  * The LZF data format is a message compression and de-compression format (uses the LZF deflate algorithm).  */
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
literal|"LZF Deflate Compression"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"lzf"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|LZFDataFormat
specifier|public
class|class
name|LZFDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|usingParallelCompression
specifier|private
name|Boolean
name|usingParallelCompression
decl_stmt|;
DECL|method|LZFDataFormat ()
specifier|public
name|LZFDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"lzf"
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
name|usingParallelCompression
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
literal|"usingParallelCompression"
argument_list|,
name|usingParallelCompression
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getUsingParallelCompression ()
specifier|public
name|Boolean
name|getUsingParallelCompression
parameter_list|()
block|{
return|return
name|usingParallelCompression
return|;
block|}
comment|/**      * Enable encoding (compress) using multiple processing cores.      */
DECL|method|setUsingParallelCompression (Boolean usingParallelCompression)
specifier|public
name|void
name|setUsingParallelCompression
parameter_list|(
name|Boolean
name|usingParallelCompression
parameter_list|)
block|{
name|this
operator|.
name|usingParallelCompression
operator|=
name|usingParallelCompression
expr_stmt|;
block|}
block|}
end_class

end_unit

