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
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|Deflater
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
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * Represents the ZIP XML {@link org.apache.camel.spi.DataFormat}  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"zip"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ZipDataFormat
specifier|public
class|class
name|ZipDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|compressionLevel
specifier|private
name|int
name|compressionLevel
init|=
name|Deflater
operator|.
name|BEST_SPEED
decl_stmt|;
DECL|method|ZipDataFormat ()
specifier|public
name|ZipDataFormat
parameter_list|()
block|{     }
DECL|method|ZipDataFormat (int compressionLevel)
specifier|public
name|ZipDataFormat
parameter_list|(
name|int
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
annotation|@
name|Override
DECL|method|createDataFormat (RouteContext routeContext)
specifier|protected
name|DataFormat
name|createDataFormat
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
return|return
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|ZipDataFormat
argument_list|(
name|compressionLevel
argument_list|)
return|;
block|}
DECL|method|getCompressionLevel ()
specifier|public
name|int
name|getCompressionLevel
parameter_list|()
block|{
return|return
name|compressionLevel
return|;
block|}
DECL|method|setCompressionLevel (int compressionLevel)
specifier|public
name|void
name|setCompressionLevel
parameter_list|(
name|int
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

