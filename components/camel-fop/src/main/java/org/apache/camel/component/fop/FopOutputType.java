begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.fop
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|fop
package|;
end_package

begin_enum
DECL|enum|FopOutputType
specifier|public
enum|enum
name|FopOutputType
block|{
DECL|enumConstant|pdf
name|pdf
argument_list|(
literal|"application/pdf"
argument_list|)
block|,
DECL|enumConstant|ps
name|ps
argument_list|(
literal|"application/postscript"
argument_list|)
block|,
DECL|enumConstant|pcl
name|pcl
argument_list|(
literal|"application/x-pcl"
argument_list|)
block|,
DECL|enumConstant|png
name|png
argument_list|(
literal|"image/png"
argument_list|)
block|,
DECL|enumConstant|jpeg
name|jpeg
argument_list|(
literal|"image/jpeg"
argument_list|)
block|,
DECL|enumConstant|svg
name|svg
argument_list|(
literal|"image/svg+xml"
argument_list|)
block|,
DECL|enumConstant|xml
name|xml
argument_list|(
literal|"application/X-fop-areatree"
argument_list|)
block|,
DECL|enumConstant|mif
name|mif
argument_list|(
literal|"application/mif"
argument_list|)
block|,
DECL|enumConstant|rtf
name|rtf
argument_list|(
literal|"application/rtf"
argument_list|)
block|,
DECL|enumConstant|txt
name|txt
argument_list|(
literal|"text/plain"
argument_list|)
block|;
DECL|field|outputFormatExtended
specifier|private
specifier|final
name|String
name|outputFormatExtended
decl_stmt|;
DECL|method|FopOutputType (String outputFormatExtended)
name|FopOutputType
parameter_list|(
name|String
name|outputFormatExtended
parameter_list|)
block|{
name|this
operator|.
name|outputFormatExtended
operator|=
name|outputFormatExtended
expr_stmt|;
block|}
DECL|method|getFormatExtended ()
specifier|public
name|String
name|getFormatExtended
parameter_list|()
block|{
return|return
name|outputFormatExtended
return|;
block|}
DECL|method|asFooOutputType (String outputFormatExtended)
specifier|public
specifier|static
name|FopOutputType
name|asFooOutputType
parameter_list|(
name|String
name|outputFormatExtended
parameter_list|)
block|{
if|if
condition|(
literal|"application/pdf"
operator|.
name|equalsIgnoreCase
argument_list|(
name|outputFormatExtended
argument_list|)
condition|)
block|{
return|return
name|pdf
return|;
block|}
elseif|else
if|if
condition|(
literal|"application/postscript"
operator|.
name|equalsIgnoreCase
argument_list|(
name|outputFormatExtended
argument_list|)
condition|)
block|{
return|return
name|ps
return|;
block|}
elseif|else
if|if
condition|(
literal|"application/x-pcl"
operator|.
name|equalsIgnoreCase
argument_list|(
name|outputFormatExtended
argument_list|)
condition|)
block|{
return|return
name|pcl
return|;
block|}
elseif|else
if|if
condition|(
literal|"image/png"
operator|.
name|equalsIgnoreCase
argument_list|(
name|outputFormatExtended
argument_list|)
condition|)
block|{
return|return
name|png
return|;
block|}
elseif|else
if|if
condition|(
literal|"image/jpeg"
operator|.
name|equalsIgnoreCase
argument_list|(
name|outputFormatExtended
argument_list|)
condition|)
block|{
return|return
name|jpeg
return|;
block|}
elseif|else
if|if
condition|(
literal|"image/svg+xml"
operator|.
name|equalsIgnoreCase
argument_list|(
name|outputFormatExtended
argument_list|)
condition|)
block|{
return|return
name|svg
return|;
block|}
elseif|else
if|if
condition|(
literal|"application/X-fop-areatree"
operator|.
name|equalsIgnoreCase
argument_list|(
name|outputFormatExtended
argument_list|)
condition|)
block|{
return|return
name|xml
return|;
block|}
elseif|else
if|if
condition|(
literal|"application/mif"
operator|.
name|equalsIgnoreCase
argument_list|(
name|outputFormatExtended
argument_list|)
condition|)
block|{
return|return
name|mif
return|;
block|}
elseif|else
if|if
condition|(
literal|"application/rtf"
operator|.
name|equalsIgnoreCase
argument_list|(
name|outputFormatExtended
argument_list|)
condition|)
block|{
return|return
name|rtf
return|;
block|}
elseif|else
if|if
condition|(
literal|"text/plain"
operator|.
name|equalsIgnoreCase
argument_list|(
name|outputFormatExtended
argument_list|)
condition|)
block|{
return|return
name|txt
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_enum

end_unit

